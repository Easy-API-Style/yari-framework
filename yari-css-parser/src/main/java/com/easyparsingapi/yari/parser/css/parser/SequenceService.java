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
package com.easyparsingapi.yari.parser.css.parser;

import java.util.LinkedList;

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Parsers.ParsingContext;
import com.easyparsingapi.yari.parsec.Parsers.TokenContext;
import com.easyparsingapi.yari.parsec.location.SourceLocation;

/**
 * Service responsible for detecting and analysing delimiter sequences
 * (parentheses, curly braces, brackets) in a CSS token stream.
 */
class SequenceService {

    /**
     * Enumeration of the supported sequence delimiter types.
     */
    static enum Type { bracket, curlingBracket, parenthesis }

    /**
     * Constructs a new {@code SequenceService} instance.
     */
    SequenceService() {
        super();
    }

    /**
     * Checks whether the current token in the parsing context opens a bracket sequence {@code []}.
     *
     * @param currentContext the current parsing context
     * @return {@code true} if a valid bracket sequence is detected, {@code false} otherwise
     */
    boolean bracket(final ParsingContext currentContext) {
        return bracket(currentContext.tokenContext());
    }

    /**
     * Checks whether the current token in the token context opens a bracket sequence {@code []}.
     *
     * @param tokenContext the current token context
     * @return {@code true} if a valid bracket sequence is detected, {@code false} otherwise
     */
    boolean bracket(final TokenContext tokenContext) {
        return inside(Type.bracket, tokenContext).isValid();
    }

    /**
     * Checks whether the current token in the parsing context opens a curly-brace sequence {@code {}}.
     *
     * @param currentContext the current parsing context
     * @return {@code true} if a valid curly-brace sequence is detected, {@code false} otherwise
     */
    boolean curlingBracket(final ParsingContext currentContext) {
        return curlingBracket(currentContext.tokenContext());
    }

    /**
     * Checks whether the current token in the token context opens a curly-brace sequence {@code {}}.
     *
     * @param tokenContext the current token context
     * @return {@code true} if a valid curly-brace sequence is detected, {@code false} otherwise
     */
    boolean curlingBracket(final TokenContext tokenContext) {
        return inside(Type.curlingBracket, tokenContext).isValid();
    }

    /**
     * Checks whether the current token in the parsing context opens a parenthesis sequence {@code ()}.
     *
     * @param currentContext the current parsing context
     * @return {@code true} if a valid parenthesis sequence is detected, {@code false} otherwise
     */
    boolean parenthesis(final ParsingContext currentContext) {
        return parenthesis(currentContext.tokenContext());
    }

    /**
     * Checks whether the current token in the token context opens a parenthesis sequence {@code ()}.
     *
     * @param tokenContext the current token context
     * @return {@code true} if a valid parenthesis sequence is detected, {@code false} otherwise
     */
    boolean parenthesis(final TokenContext tokenContext) {
        return inside(Type.parenthesis, tokenContext).isValid();
    }

    /**
     * Checks whether the token at the specified index in the token context opens a parenthesis sequence {@code ()}.
     *
     * @param tokenContext the current token context
     * @param index        the index of the token to check in the token array
     * @return {@code true} if a valid parenthesis sequence is detected at that index, {@code false} otherwise
     */
    boolean parenthesis(final TokenContext tokenContext,
                        final int index) {
        return inside(Type.parenthesis, tokenContext.tokens(), index).isValid();
    }

    /**
     * Analyses the delimited sequence of the given type starting from the current position of the token context.
     *
     * @param type         the expected delimiter type
     * @param tokenContext the current token context
     * @return the {@link Sequence} resulting from the analysis
     */
    Sequence inside(final Type type,
                    final TokenContext tokenContext) {
       return inside(type, tokenContext.tokens(), tokenContext.index());
    }

    /**
     * Analyses the delimited sequence of the given type starting at the specified
     * index within the token array.
     * <p>
     * Returns an invalid {@link Sequence} if {@code type} is {@code null} or if the
     * token at {@code tokenIndex} does not match the expected opening delimiter.
     * </p>
     *
     * @param type       the expected delimiter type; may be {@code null}
     * @param tokens     the full array of tokens
     * @param tokenIndex the index of the expected opening delimiter token
     * @return the {@link Sequence} resulting from the analysis
     */
    private Sequence inside(final Type type,
                            final Token[] tokens,
                            final int tokenIndex) {
        if (type == null) {
            return new Sequence(new LinkedList<>(), tokenIndex, null, true, false);
        }
        if ((Type.parenthesis != type || !"(".equals(get(tokens, tokenIndex)))
                && (Type.curlingBracket != type || !"{".equals(get(tokens, tokenIndex)))
                && (Type.bracket != type || !"[".equals(get(tokens, tokenIndex)))) {
            return new Sequence(new LinkedList<>(), tokenIndex, null, true, false);
        }
        /** The result. */
        final Sequence result;
        if (Type.curlingBracket == type) {
            result = sequenceCurlingBracket(tokens, tokenIndex);
        }
        else {
            result = sequence(tokens, tokenIndex);
        }
        return result;
    }

    /**
     * Returns a string representation of this {@code SequenceService}.
     *
     * @return a string of the form {@code "SequenceService []"}
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(SequenceService.class.getSimpleName());
        result.append(" [");
        result.append("]");
        return result.toString();
    }

    /*
     *
     * SEQUENCE
     *
     */

    /**
     * Scans the token array from {@code tokenIndex} to find the matching closing
     * delimiter for a parenthesis, curly-brace, or bracket sequence (all three
     * delimiter types are tracked for proper nesting).
     *
     * @param tokens     the full array of tokens
     * @param tokenIndex the index of the opening delimiter token
     * @return the {@link Sequence} describing the result of the scan
     */
    private static Sequence sequence(final Token[] tokens,
                                     final int tokenIndex) {
        final LinkedList<Deep> deep = new LinkedList<>();
        final LinkedList<SequenceElement> sequence = new LinkedList<>();

        SourceLocation errorLocation = null;
        boolean done = false;
        boolean failed = false;
        int i = -1;

        int startIndex = -1;
        for (i = tokenIndex; i < tokens.length; i++) {
            final Token token = tokens[i];
            if (Token.tag(token).toString().startsWith("LITERAL_TEMPLATE")) {
                /** The continue. */
                continue;
            }
            // ()
            if ("(".equals(token.toString())) {
                deep.add(new Deep(Type.parenthesis, token.sourceLocation()));
                if (deep.size() == 2) {
                    startIndex = i;
                }
            }
            else if (")".equals(token.toString())) {
                if (deep.isEmpty() || Type.parenthesis != deep.removeLast().type()) {
                    errorLocation = token.sourceLocation();
                    failed = true;
                    /** The break. */
                    break;
                }
                if (deep.size() == 2) {
                    sequence.add(new SequenceElement(Type.parenthesis, startIndex, i));
                }
            }
            // {}
            else if ("{".equals(token.toString())) {
                deep.add(new Deep(Type.curlingBracket, token.sourceLocation()));
                if (deep.size() == 2) {
                    startIndex = i;
                }
            }
            else if ("}".equals(token.toString())) {
                if (deep.isEmpty() || Type.curlingBracket != deep.removeLast().type()) {
                    errorLocation = token.sourceLocation();
                    failed = true;
                    /** The break. */
                    break;
                }
                if (deep.size() == 2) {
                    sequence.add(new SequenceElement(Type.curlingBracket, startIndex, i));
                }
            }
            // []
            else if ("[".equals(token.toString())) {
                deep.add(new Deep(Type.bracket, token.sourceLocation()));
                if (deep.size() == 2) {
                    startIndex = i;
                }
            }
            else if ("]".equals(token.toString())) {
                if (deep.isEmpty() || Type.bracket != deep.removeLast().type()) {
                    errorLocation = token.sourceLocation();
                    failed = true;
                    /** The break. */
                    break;
                }
                if (deep.size() == 2) {
                    sequence.add(new SequenceElement(Type.bracket, startIndex, i));
                }
            }
            if (deep.size() == 0) {
                done = true;
                /** The break. */
                break;
            }
        }
        return new Sequence(sequence, i, errorLocation, failed, done);
    }

    /**
     * Scans the token array from {@code tokenIndex} to find the matching closing
     * curly brace, tracking only curly-brace nesting.
     *
     * @param tokens     the full array of tokens
     * @param tokenIndex the index of the opening {@code {}} token
     * @return the {@link Sequence} describing the result of the scan
     */
    private static Sequence sequenceCurlingBracket(final Token[] tokens,
                                                   final int tokenIndex) {
        final LinkedList<Deep> deep = new LinkedList<>();
        final LinkedList<SequenceElement> sequence = new LinkedList<>();

        SourceLocation errorLocation = null;
        boolean done = false;
        boolean failed = false;
        int i = -1;

        int startIndex = -1;
        for (i = tokenIndex; i < tokens.length; i++) {
            final Token token = tokens[i];
            if (Token.tag(token).toString().startsWith("LITERAL_TEMPLATE")) {
                /** The continue. */
                continue;
            }
            // {}
            if ("{".equals(token.toString())) {
                deep.add(new Deep(Type.curlingBracket, token.sourceLocation()));
                if (deep.size() == 2) {
                    startIndex = i;
                }
            }
            else if ("}".equals(token.toString())) {
                if (deep.isEmpty() || Type.curlingBracket != deep.removeLast().type()) {
                    errorLocation = token.sourceLocation();
                    failed = true;
                    /** The break. */
                    break;
                }
                if (deep.size() == 2) {
                    sequence.add(new SequenceElement(Type.curlingBracket, startIndex, i));
                }
            }
            if (deep.size() == 0) {
                done = true;
                /** The break. */
                break;
            }
        }
        return new Sequence(sequence, i, errorLocation, failed, done);
    }

    /*
     *
     * TOKEN
     *
     */

    /**
     * Returns the string representation of the token at the given index,
     * or {@code null} if the index is out of bounds.
     *
     * @param tokens the token array
     * @param index  the index to look up
     * @return the token's string value, or {@code null} if out of bounds
     */
    private static String get(final Token[] tokens,
                              final int index) {
        String result = null;
        if (index < tokens.length) {
            result = tokens[index].toString();
        }
        return result;
    }

    /*
     *
     * UTIL
     *
     */
//    private static Type typeOf(final String value) {
//        Type result = null;
//        if ("(".equals(value)) {
//            result = Type.parenthesis;
//        }
//        else if ("{".equals(value)) {
//            result = Type.curlingBracket;
//        }
//        else if ("[".equals(value)) {
//            result = Type.bracket;
//        }
//        return result;
//    }

    /*
     *
     * RECORD
     *
     */

    /**
     * Internal record tracking the nesting depth for a delimiter of the given type,
     * along with the source location of the opening token.
     *
     * @param type     the delimiter type
     * @param location the source location of the opening delimiter token
     */
    private static record Deep(Type type, SourceLocation location) {}

    /**
     * Represents an element of a delimited sequence, defined by its type and its start and end indices in the token array.
     *
     * @param type  the delimiter type of this sequence element
     * @param start the index of the opening delimiter token in the token array
     * @param end   the index of the closing delimiter token in the token array
     */
    static record SequenceElement(Type type, int start, int end) {}

    /**
     * Represents the result of analysing a delimited sequence in a token stream.
     *
     * @param sequence      the nested sequence elements found inside the outer delimiters
     * @param tokenIndex    the token array index reached at the end of the scan
     * @param errorLocation the source location of the first mismatched delimiter,
     *                      or {@code null} if no error occurred
     * @param failed        {@code true} if a mismatched delimiter was encountered
     * @param done          {@code true} if the outer closing delimiter was found
     */
    static record Sequence(LinkedList<SequenceElement> sequence,
                           int tokenIndex,
                           SourceLocation errorLocation,
                           boolean failed,
                           boolean done) {

        /**
         * Indicates whether the analysed sequence is valid, i.e. complete and error-free.
         *
         * @return {@code true} if the sequence is valid, {@code false} otherwise
         */
        boolean isValid() {
            return !failed && done;
        }

    }

}
