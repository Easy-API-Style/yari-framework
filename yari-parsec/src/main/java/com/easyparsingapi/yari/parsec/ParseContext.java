/*
 * Copyright (c) 2025 Easy API
 * Website : https://easyparsingapi.com/
 * GitHub  : https://github.com/Easy-API-Style/yari-framework
 * Contact : easy.api.contact@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easyparsingapi.yari.parsec;

import static com.easyparsingapi.yari.parsec.internal.util.Checks.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.parsec.Parser.ResultContext;
import com.easyparsingapi.yari.parsec.error.ParseErrorDetail;
import com.easyparsingapi.yari.parsec.error.ParserException;
import com.easyparsingapi.yari.parsec.internal.annotations.Private;
import com.easyparsingapi.yari.parsec.internal.util.Lists;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.location.SourceLocator;
import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;

/**
 * Represents the context state during parsing.
 */
public abstract class ParseContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseContext.class);

	static class Result {

		private final Object result;
		private final int start;
		private final int end;

		Result(Object result, int start, int end) {
			this.result = result;
			this.start = start;
			this.end = end;
		}

		public Object result() {
			return result;
		}

		public int start() {
			return start;
		}

		public int end() {
			return end;
		}

        public Object geValue() {
            return this.result;
		}

		@Override
		public String toString() {
			String result = "";
			if (this.result != null) {
				result = this.result.toString();
			}
			return result.toString();
		}

	}

    static final String EOF = "EOF";

    final CharSequence source;
    final SourceLocator locator;

    ApiParser.Config config;
    Function<ResultContext, Object> onMapAction;

    /**
     * The current position of the input.
     * Points to the token array for token level.
     */
    int at;
    /** The current logical step. */
    int step;
    /** The current parse result. */
    private Result result;
    /** The current parse sourceLocation. */
    private SourceLocation sourceLocation;

	private ParserTrace trace = new ParserTrace() {
	    boolean accept(String name) {
	       return !name.contains("token")
	               && !name.contains("phrase")
	               && !name.contains("anyLiteral")
	               && !name.contains("exponential")
	               && !name.contains("string")
	               && !name.contains("graveAccentString")
	               && !name.contains("singleQuoteString")
	               && !name.contains("doubleQuoteString")
	               && !name.contains("base_10")
	               && !name.contains("hexadecimal")
	               && !name.contains("keyword")
	               && !name.contains("trueOrFalse")
	               && !name.contains("integer")
	               && !name.contains("bigInteger")
	               && !name.contains("decimal")
	               && !name.contains("octal")
	               && !name.contains("atomic")
	               && !name.contains("regExpString")
	               && !name.contains("identifier")
	               && !name.contains("fragment");
	    }

        @Override
        public void push(String name) {
//            if (accept(name)) {
//                LOGGER.info("START --> {}", name);
//            }
        }

        @Override
        public void pop() {
        }

        @Override
        public TreeNode getCurrentNode() {
            return null;
        }

        @Override
        public void setCurrentResult(Object result) {
        }

        @Override
        public TreeNode getLatestChild() {
            return null;
        }

        @Override
        public void setLatestChild(TreeNode node) {
        }

        @Override
        public void startFresh(ParseContext context) {
        }

        @Override
        public void setStateAs(ParserTrace that) {
        }

        @Override
        public void succeeds(String name, Object result) {
//            if (accept(name)) {
//                LOGGER.info("DONE --> {} => {}", name, result);
//            }
        }

        @Override
        public void fails(String name) {
//            if (accept(name)) {
//                LOGGER.error("FAILED --> {}", name);
//            }
        }

    };

    enum ErrorType {

        /** Default value, no error. */
        NONE(false),
        /**
         * When the error is mostly lenient (as a delimiter of repetitions for example).
         */
        DELIMITING(false),
        /**
         * When {@link Parser#not()} is called. Signals that something isn't expected.
         */
        UNEXPECTED(false),
        /** When any expected input isn't found. */
        MISSING(true),
        /**
         * When {@link Parser#label()} is called. Signals that a logical stuff isn't found.
         */
        EXPECTING(true),
        /** When {@link Parsers#fail(String)} is called. Signals a serious problem. */
        FAILURE(false);

        ErrorType(boolean mergeable) {
            this.mergeable = mergeable;
        }

        final boolean mergeable;
    }

    private ErrorType currentErrorType = ErrorType.NONE;
    private int currentErrorAt;
    private int currentErrorIndex = 0; // is it necessary to set this to the starting index?
    private final ArrayList<Object> errors = Lists.arrayList(32);
    private String encountered = null; // for explicitly setting encountered token into ScannerState.
    private TreeNode currentErrorNode = null;

    // explicit suppresses error recording if true.
    private boolean errorSuppressed = false;
    private ErrorType overrideErrorType = ErrorType.NONE;

    // caller should not change input after it is passed in.
    ParseContext(CharSequence source,
    		     int at,
                 SourceLocator locator) {
        this(source,
        	 new Result(null, at, at),
        	 at,
        	 locator);
    }

    ParseContext(CharSequence source,
    		     Result result,
                 int at,
                 SourceLocator locator) {
        this.source = source;
        this.result = result;
        this.step = 0;
        this.at = at;
        this.locator = locator;
        this.currentErrorAt = at;
    }

    SourceLocator getSourceLocator() {
		return locator;
	}

    /**
     * Returns the configuration associated with the current parser.
     *
     * @return the {@link ApiParser.Config} instance, or {@code null} if none has been set
     */
    public ApiParser.Config getConfig() {
		return config;
	}

	void setConfig(ApiParser.Config config) {
		this.config = config;
		this.onMapAction = config != null ? config.onMap() : null;
	}

	Result getResult() {
		return result;
	}

	void setResult(Object result, int startTokenIndex, int endTokenIndex) {
		this.result = new Result(result, startTokenIndex, endTokenIndex);
	}

	void mapResult(Object result, Position start, Position end) {
	    if (this instanceof ParserState parserState && result != null) {
	        int startIndex = parserState.getSourceLocator().locate(start);
	        int endIndex = parserState.getSourceLocator().locate(end);
	        onMapAction.apply(new ResultContext(result, startIndex, endIndex, new SourceLocation(start, end)));
	    }
	}

	void mapResult(Object result, int startTokenIndex, int endTokenIndex) {
		setResult(result, startTokenIndex, endTokenIndex);
		if (this instanceof ParserState parserState  && result != null) {
			endTokenIndex = endTokenIndex > 0 && startTokenIndex != endTokenIndex
					           ? endTokenIndex - 1
					           : endTokenIndex;
			if (onMapAction != null && startTokenIndex <= endTokenIndex) {
				Token firstToken = parserState.getToken(startTokenIndex);
				Token lastToken = parserState.getToken(endTokenIndex);
				if (firstToken != null && lastToken != null) {
					int startIndex = firstToken.index();
					int endIndex = lastToken.index() + lastToken.length();
					Position start = firstToken.sourceLocation().start();
					Position end = lastToken.sourceLocation().end();
					setSourceLocation(new SourceLocation(start, end));

//				if (result.getClass().getName().toLowerCase().contains("infix")) {
//				    System.out.println("______________________");
//                    System.out.println(source.subSequence(startIndex, endIndex));
//                    System.out.println(result);
//                    System.out.println(new SourceLocation(start, end));
//                    System.out.println("______________________");
//				}
					onMapAction.apply(new ResultContext(result, startIndex, endIndex, sourceLocation));
				}
				else if (startTokenIndex == endTokenIndex
				              && startTokenIndex == parserState.getTokens().length) {
                    Position start = locator.locate(source.length());
                    Position end = locator.locate(source.length());
                    setSourceLocation(new SourceLocation(start, end));

				    onMapAction.apply(new ResultContext(result, startTokenIndex, endTokenIndex, sourceLocation));
				}
			}
		}
	}

	/**
	 * Returns the source {@link Position} corresponding to the given character offset in the original source.
	 *
	 * @param at the character offset within the source
	 * @return the {@link Position} (line and column) at the given offset
	 */
	public Position getSourcePosition(int at) {
		return locator.locate(at);
	}

	SourceLocation getSourceLocation() {
		return sourceLocation;
	}

	void setSourceLocation(SourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	/** Runs {@code parser} with error recording suppressed. */
    boolean withErrorSuppressed(Parser<?> parser) {
        boolean oldValue = errorSuppressed;
        errorSuppressed = true;
        boolean ok = parser.apply(this);
        errorSuppressed = oldValue;
        return ok;
    }

    /** Runs {@code parser} with error recording suppressed. */
    boolean applyAsDelimiter(Parser<?> parser) {
        ErrorType oldValue = overrideErrorType;
        overrideErrorType = ErrorType.DELIMITING;
        int oldStep = step;
        boolean ok = parser.apply(this);
        if (ok) {
            step = oldStep;
        }
        overrideErrorType = oldValue;
        return ok;
    }

    /**
     * Applies {@code parser} as a new tree node with {@code name},
     * and if fails, reports "expecting $name".
     */
    boolean applyNewNode(Parser<?> parser,
                         String name) {
        int physical = at;
        int logical = step;
        TreeNode latestChild = trace.getLatestChild();
        trace.push(name);
        if (parser.apply(this)) {
            trace.succeeds(name, result);
            trace.setCurrentResult(result);
            trace.pop();
            return true;
        }
        trace.fails(name);
        if (stillThere(physical, logical)) {
            expected(name);
        }
        trace.pop();
        // On failure, the erroneous path shouldn't be counted in the parse tree.
        trace.setLatestChild(latestChild);
        return false;
    }

    boolean applyNested(Parser<?> parser,
                        ParseContext nestedState) {
        // nested is either the token-level parser, or the inner scanner of a subpattern.
        try {
            if (parser.apply(nestedState)) {
                set(nestedState.step, at, nestedState.result);
                return true;
            }
            // index on token level is the "at" on character level
            set(step, nestedState.getIndex(), null);

            // always copy error because there could be false alarms in the character level.
            // For example, a "or" parser nested in a "many" failed in one of its branches.
            copyErrorFrom(nestedState);
            return false;
        }
        finally {
            trace.setStateAs(nestedState.trace);
        }
    }

    boolean repeat(Parser<?> parser,
                   int n) {
        for (int i = 0; i < n; i++) {
            if (!parser.apply(this)) {
                return false;
            }
        }
        return true;
    }

    <T> boolean repeat(Parser<? extends T> parser,
                       int n,
                       Collection<T> collection) {
        for (int i = 0; i < n; i++) {
            if (!parser.apply(this)) {
                return false;
            }
            collection.add(parser.getReturn(this));
        }
        return true;
    }

    ParserTrace getTrace() {
        return trace;
    }

    /** The physical index of the current most relevant error, {@code 0} if none. */
    int errorIndex() {
        return currentErrorIndex;
    }

    ParseTree buildParseTree() {
        TreeNode currentNode = trace.getCurrentNode();
        if (currentNode == null) {
            return null;
        }
        return currentNode.freeze(getIndex()).toParseTree();
    }

    ParseTree buildErrorParseTree() {
        // The current node is partially done because there was an error.
        // So orphanize it. But at the same time,
        // all ancestor nodes should have their endIndex set to where we are now.
        if (currentErrorNode == null) {
            return null;
        }
        return currentErrorNode.orphanize().freeze(getIndex()).toParseTree();
    }

    /** Only called when rendering the error in {@link ParserException}. */
    ParseErrorDetail renderError() {
        final int errorIndex = toIndex(currentErrorAt);
        final String encounteredName = getEncountered();
        final ArrayList<String> errorStrings = Lists.arrayList(errors.size());
        for (Object error : errors) {
            errorStrings.add(String.valueOf(error));
        }
        switch (currentErrorType) {
        case UNEXPECTED:
            return new EmptyParseError(errorIndex, encounteredName) {
                @Override
                public String getUnexpected() {
                    return errorStrings.get(0);
                }
            };
        case FAILURE:
            return new EmptyParseError(errorIndex, encounteredName) {
                @Override
                public String getFailureMessage() {
                    return errorStrings.get(0);
                }
            };
        case EXPECTING:
        case MISSING:
        case DELIMITING:
            return new EmptyParseError(errorIndex, encounteredName) {
                @Override
                public List<String> getExpected() {
                    return errorStrings;
                }
            };
        default:
            return new EmptyParseError(errorIndex, encounteredName);
        }
    }

    private String getEncountered() {
        if (encountered != null) {
            return encountered;
        }
        return getInputName(currentErrorAt)
        		   .replace("\n", "\\n")
        		   .replace("\r", "\\r");
    }

    /**
     * Returns the string representation of the current input (character or token).
     */
    abstract String getInputName(int pos);

    abstract boolean isEof();

    /** Returns the current index in the original source. */
    int getIndex() {
        return toIndex(at);
    }

    /** Returns the current token. Only applicable to token level parser. */
    abstract Token getToken();

    /** Peeks the current character. Only applicable to character level parser. */
    abstract char peekChar();

    /** Translates the logical position to physical index in the original source. */
    abstract int toIndex(int pos);

    @Private
    void raise(ErrorType type, Object subject) {
        if (errorSuppressed) {
            return;
        }
        if (at < currentErrorAt) {
            return;
        }
        if (overrideErrorType != ErrorType.NONE) {
            type = overrideErrorType;
        }
        if (at > currentErrorAt) {
            setErrorState(at, getIndex(), type);
            errors.add(subject);
            return;
        }
        // now error location is same
        if (type.ordinal() < currentErrorType.ordinal()) {
            return;
        }
        if (type.ordinal() > currentErrorType.ordinal()) {
            setErrorState(at, getIndex(), type);
            errors.add(subject);
            return;
        }
        // now even error type is same
        if (type.mergeable) {
            // merge expected error.
            errors.add(subject);
        }
    }

    void fail(String message) {
        raise(ErrorType.FAILURE, message);
    }

    void missing(Object what) {
        raise(ErrorType.MISSING, what);
    }

    void expected(Object what) {
        raise(ErrorType.EXPECTING, what);
    }

    void unexpected(String what) {
        raise(ErrorType.UNEXPECTED, what);
    }

    boolean stillThere(int wasAt, int originalStep) {
        if (step == originalStep) {
            // logical step didn't change, so logically we are still there,
            // undo any physical offset
            setAt(originalStep, wasAt);
            return true;
        }
        return false;
    }

    void set(int step, int at, Result ret) {
        this.step = step;
        this.at = at;
        this.result = ret;
    }

    void setAt(int step, int at) {
        this.step = step;
        this.at = at;
    }

    void next() {
        at++;
        step++;

    }

    void next(int n) {
        at += n;
        if (n > 0) {
            step++;
        }
    }

    /**
     * Enables parse tree tracing with {@code rootName} as the name of the root node.
     */
    void enableTrace(final String rootName) {
        this.trace = new ParserTrace() {
            private TreeNode current = new TreeNode(rootName, getIndex());

            @Override
            public void push(String name) {
                this.current = current.addChild(name, getIndex());
            }

            @Override
            public void pop() {
                current.setEndIndex(getIndex());
                this.current = current.parent();
            }

            @Override
            public TreeNode getCurrentNode() {
                return current;
            }

            @Override
            public void setCurrentResult(Object result) {
                current.setResult(result);
            }

            @Override
            public TreeNode getLatestChild() {
                return current.latestChild;
            }

            @Override
            public void setLatestChild(TreeNode latest) {
                checkState(latest == null || latest.parent() == current,
                           "Trying to set a child node not owned by the parent node");
                current.latestChild = latest;
            }

            @Override
            public void startFresh(ParseContext context) {
                context.enableTrace(rootName);
            }

            @Override
            public void setStateAs(ParserTrace that) {
                current = that.getCurrentNode();
            }

            @Override
            public void succeeds(String name, Object result) {

            }

            @Override
            public void fails(String name) {

            }
        };
    }

    /**
     * Allows tracing of parsing progress during error condition, to ease debugging.
     */
    interface ParserTrace {

        /**
         * Upon applying a parser with {@link Parser#label},
         * the label name is used to create a new child node under the current node.
         * The new child node is set to be the current node.
         */
        void push(String name);

        /**
         * Notifies the trace that the parser identified by {@code name} succeeded with the given {@code result}.
         *
         * @param name   the name of the parser that succeeded
         * @param result the parse result produced by the parser
         */
        void succeeds(String name, Object result);

        /**
         * Notifies the trace that the parser identified by {@code name} failed.
         *
         * @param name the name of the parser that failed
         */
        void fails(String name);

        /**
         * When a parser finishes, the current node is popped so we are back to the parent parser.
         */
        void pop();

        /**
         * Returns the current node, that is being parsed (not necessarily finished).
         */
        TreeNode getCurrentNode();

        /**
         * Whenever a labeled parser succeeds, it calls this method to set its result in the trace.
         */
        void setCurrentResult(Object result);

        /**
         * Called by branching parsers, to save the current state of tree,
         * before trying parsers that could modify the tree state.
         */
        TreeNode getLatestChild();

        /**
         * Called by labeled parser to reset the current child node when the current node failed.
         * Also called by {@link BestParser} to set the optimum parse tree.
         */
        void setLatestChild(TreeNode node);

        /** Called when tokenizer passes on to token-level parser. */
        void startFresh(ParseContext context);

        /**
         * Set the enclosing parser's tree state into the nested parser's state.
         * Called for both nested token-level parser and nested scanner.
         */
        void setStateAs(ParserTrace that);
    }

    private void setErrorState(int errorAt,
                               int errorIndex,
                               ErrorType errorType,
                               List<Object> errors) {
        setErrorState(errorAt, errorIndex, errorType);
        this.errors.addAll(errors);
    }

    private void setErrorState(int errorAt,
                               int errorIndex,
                               ErrorType errorType) {
        this.currentErrorIndex = errorIndex;
        this.currentErrorAt = errorAt;
        this.currentErrorType = errorType;
        this.currentErrorNode = trace.getCurrentNode();
        this.encountered = null;
        this.errors.clear();
    }

    private void copyErrorFrom(ParseContext that) {
        int errorIndex = that.errorIndex();
        setErrorState(errorIndex, errorIndex, that.currentErrorType, that.errors);
        if (!that.isEof()) {
            this.encountered = that.getEncountered();
        }
        currentErrorNode = that.currentErrorNode;
    }

    /**
     * Reads the characters as input. Only applicable to character level parsers.
     */
    abstract CharSequence characters();

    @Override
    public String toString() {
        return source.subSequence(getIndex(), source.length()).toString();
    }

}
