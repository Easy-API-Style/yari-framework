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

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.parsec.ParseContext.ErrorType;
import com.easyparsingapi.yari.parsec.ParseContext.Result;
import com.easyparsingapi.yari.parsec.error.ParseErrorDetail;
import com.easyparsingapi.yari.parsec.error.ParserException;
import com.easyparsingapi.yari.parsec.functors.Map2;
import com.easyparsingapi.yari.parsec.functors.Map3;
import com.easyparsingapi.yari.parsec.functors.MapInfix;
import com.easyparsingapi.yari.parsec.functors.MapOperator;
import com.easyparsingapi.yari.parsec.internal.annotations.Private;
import com.easyparsingapi.yari.parsec.internal.util.Checks;
import com.easyparsingapi.yari.parsec.internal.util.StringUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.google.common.base.Strings;

/**
 * Defines grammar and encapsulates parsing logic.
 * A {@link Parser} takes as input a {@link CharSequence} source
 * and parses it when the {@link #parse(CharSequence)} method is called.
 * A value of type {@code T} will be returned if parsing succeeds,
 * or a {@link ParserException} is thrown to indicate parsing error.
 * For example:
 *  <pre>{@code
 *   Parser<String> scanner = Scanners.IDENTIFIER;
 *   assertEquals("foo", scanner.parse("foo"));
 * }</pre>
 *
 * <p> {@code Parser}s run either on character level to scan the source,
 * or on token level to parse a list of {@link Token} objects returned from another parser.
 * This other parser that returns the list of tokens for token level parsing is hooked up
 * via the {@link #from(Parser, Parser)} or {@link #from(Parser)} method.
 *
 * <p>The following are important naming conventions used throughout the library:
 *
 * <ul>
 * <li>A character level parser object that recognizes a single lexical word is called a scanner.
 * <li>A scanner that translates the recognized lexical word into a token is called a tokenizer.
 * <li>A character level parser object that does lexical analysis and returns a list of {@link Token} is called a lexer.
 * <li>All {@code index} parameters are 0-based indexes in the original source.
 * </ul>
 *
 * To debug a complex parser that fails in un-obvious way, pass {@link Mode#DEBUG} mode to {@link #parse(CharSequence, Mode)}
 * and inspect the result in {@link ParserException#getParseTree()}.
 * All {@link #label labeled} parsers will generate a node in the exception's parse tree,
 * with matched indices in the source.
 *
 * @param <T> the type of value produced by this parser
 */
public abstract class Parser<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    /**
     * Represents the lifecycle state of a parser execution.
     */
    public static enum State {
        /** The parser has not yet started. */
        BEGIN,
        /** The parser completed successfully. */
        SUCCESS,
        /** The parser failed. */
        FAILED
    }

    /**
     * Holds the result of a parse operation together with its position information in the source.
     *
     * @param value          the parsed value
     * @param startIndex     the 0-based start index in the source
     * @param endIndex       the 0-based end index (exclusive) in the source
     * @param sourceLocation the source location corresponding to the matched range
     */
    public static record ResultContext(Object value,
    	                               int startIndex,
    	                               int endIndex,
    	                               SourceLocation sourceLocation) {}

    /**
     * constructor
     */
    public Parser() { }

    /**
   * An atomic mutable reference to {@link Parser} used in recursive grammars.
   *
   * <p>For example, the following is a recursive grammar for a simple calculator: <pre>   {@code
   *   Terminals terms = Terminals.operators("(", ")", "+", "-");
   *   Parser.Reference<Integer> ref = Parser.newReference();
   *   Parser<Integer> literal = Terminals.IntegerLiteral.PARSER.map(new Function<String, Integer>() {
   *      ...
   *      return Integer.parseInt(s);
   *   });
   *   Parser.Reference<Integer> parenthesized =  // recursion in rule E = (E)
   *       Parsers.between(terms.token("("), ref.lazy(), terms.token(")"));
   *   ref.set(new OperatorTable()
   *       .infixl(terms.token("+").retn(plus), 10)
   *       .infixl(terms.token("-").retn(minus), 10)
   *       .build(literal.or(parenthesized)));
   *   return ref.get();
   * }</pre>
   * Note that a left recursive grammar will result in {@code StackOverflowError}.
   * Use appropriate parser built-in parser combinators to avoid left-recursion.
   * For instance, many left recursive grammar rules can be thought
   * as logically equivalent to postfix operator rules. In such case,
   * either {@link OperatorTable} or {@link Parser#postfix} can be used to work around left recursion.
   * The following is a left recursive parser for array types in the form of "T[]" or "T[][]":
   * <pre>{@code
   *   Terminals terms = Terminals.operators("[", "]");
   *   Parser.Reference<Type> ref = Parser.newReference();
   *   ref.set(Parsers.or(leafTypeParser,
   *       Parsers.sequence(ref.lazy(), terms.phrase("[", "]"), new Unary<Type>() {...})));
   *   return ref.get();
   * }</pre>
   * And it will fail. A correct implementation is:  <pre>   {@code
   *   Terminals terms = Terminals.operators("[", "]");
   *   return leafTypeParer.postfix(terms.phrase("[", "]").retn(new Unary<Type>() {...}));
   * }</pre>
   * A not-so-obvious example, is to parse the {@code expr ? a : b} ternary operator.
   * It too is a left recursive grammar.
   * And un-intuitively it can also be thought as a postfix operator.
   * Basically, we can parse "? a : b" as a whole into a unary operator
   * that accepts the condition expression as input and outputs the full ternary expression:
   * <pre>{@code
   *   Parser<Expr> ternary(Parser<Expr> expr) {
   *     return expr.postfix(
   *       Parsers.sequence(
   *           terms.token("?"), expr, terms.token(":"), expr,
   *           (unused, then, unused, orelse) -> cond ->
   *               new TernaryExpr(cond, then, orelse)));
   *   }
   * }</pre>
   *
   * @param <T> the type of value produced by the referenced parser
   */
    @SuppressWarnings("serial")
    public static final class Reference<T> extends AtomicReference<Parser<T>> {

        /** Creates a new uninitialized {@code Reference}. */
        public Reference() {}

        /** The lazy parser delegate. */
        private final Parser<T> lazy = new Parser<T>() {

            @Override
            boolean apply(ParseContext ctxt) {
                return deref().apply(ctxt);
            }

            private Parser<T> deref() {
                Parser<T> p = get();
                Checks.checkNotNullState(p, "Uninitialized lazy parser reference. "
                                          + "Did you forget to call set() on the reference?");
                return p;
            }

            @Override
            public String toString() {
                return "lazy";
            }

        };

        /**
         * A {@link Parser} that delegates to the parser object referenced by {@code this} during parsing time.
         *
         * @return a lazy parser that resolves the referenced parser at parse time
         */
        public Parser<T> lazy() {
            return lazy;
        }

    }

    /**
     * Creates a new instance of {@link Reference}.
     * Used when your grammar is recursive (many grammars are).
     *
     * @param <T> the result type of the referenced parser
     * @return a new, empty {@link Reference} instance
     */
    public final static <T> Reference<T> newReference() {
        return new Reference<T>();
    }

    /**
     * A {@link Parser} that executes {@code this} and, if it succeeds, returns {@code value}.
     *
     * @param <R>   the result type
     * @param value the constant value to return on success
     * @return a parser that always returns {@code value} when {@code this} succeeds
     */
    public final <R> Parser<R> retn(R value) {
        return next(Parsers.constant(() -> value));
    }

    /**
     * A {@link Parser} that sequentially executes {@code this} and then {@code parser},
     * returning the result of {@code parser}.
     *
     * @param <R>    the result type
     * @param parser the parser to run after {@code this}
     * @return a parser that runs {@code this} then {@code parser} and returns {@code parser}'s result
     */
    public final <R> Parser<R> next(Parser<R> parser) {
        return Parsers.sequence(this, parser, InternalFunctors.<Object, R>lastOfTwo());
    }

    /**
     * A {@link Parser} that executes {@code this}, then maps its result to another {@link Parser}
     * via {@code map} and executes that parser as the next step.
     *
     * @param <To> the result type of the next parser
     * @param map  a function that takes this parser's result and returns the next parser to run
     * @return a parser that chains {@code this} with a dynamically chosen next parser
     */
    public final <To> Parser<To> next(final Function<? super T, ? extends Parser<? extends To>> map) {
        return new Parser<To>() {
            @Override
            boolean apply(ParseContext ctxt) {
                return Parser.this.apply(ctxt) && runNext(ctxt);
            }

            @Override
            public String toString() {
                return map.toString();
            }

            private boolean runNext(ParseContext state) {
                T from = Parser.this.getReturn(state);
                return map.apply(from).apply(state);
            }
        };
    }

    /**
     * A {@link Parser} that matches this parser zero or many times until {@code parser} succeeds.
     * The input matched by {@code parser} is not consumed.
     * All results matched by this parser are collected in a list and returned.
     *
     * @param parser the termination parser; when it succeeds, repetition stops
     * @return a parser that collects all matches of this parser until {@code parser} succeeds
     */
    public final Parser<List<T>> until(Parser<?> parser) {
        return parser.not().next(this).many().followedBy(parser.peek());
    }

    /**
     * A {@link Parser} that attempts to parse with {@code this} and, on failure, applies
     * the {@code handleError} function to produce a recovery value, then resumes parsing
     * by scanning forward according to the provided control parsers.
     *
     * @param handleError       a function invoked on parse failure to build a recovery value
     *                          from the error detail, source location, and consumed tokens
     * @param retryParsingFrom  a parser whose match signals that normal parsing may resume
     * @param stopParsingAt     a parser whose match causes recovery to stop gracefully
     * @param failParsingAt     a parser whose match causes recovery to fail with an error
     * @return a fault-tolerant parser that recovers from errors using {@code handleError}
     */
    public final Parser<T> catchError(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                      final Parser<?> retryParsingFrom,
                                      final Parser<?> stopParsingAt,
                                      final Parser<?> failParsingAt) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                final Result result = ctxt.getResult();
                final int step = ctxt.step;
                final int at = ctxt.at;

                if (!Parser.this.apply(ctxt)) {
                    // back
                    ctxt.set(step, at, result);
                    // render error
                    final int begin = ctxt.at;
                    final ParseErrorDetail parseErrorDetail = ctxt.renderError();
                    // stop
                    if (ctxt.withErrorSuppressed(stopParsingAt)) {
                        int end = ctxt.at;
                        T error = error(parseErrorDetail, handleError, ctxt, begin, end);
                        ctxt.setResult(error, begin, end);
                        return true;
                    }
                    // fail
                    if (ctxt.withErrorSuppressed(failParsingAt)) {
                        ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                        return false;
                    }
                    // EOF -> stop
                    if (ctxt.isEof()) {
                        int end = ctxt.at;
                        T error = error(parseErrorDetail, handleError, ctxt, begin, end);
                        ctxt.mapResult(error, begin, end);
                        return true;
                    }
                    // catch error
                    else {
                        ctxt.next();
                        boolean failed = false;
                        while (true) {
                            // stop
                            if (ctxt.withErrorSuppressed(stopParsingAt)) {
                                break;
                            }
                            // fail
                            else if (ctxt.withErrorSuppressed(failParsingAt)) {
                                failed = true;
                                break;
                            }
                            // retry
                            else if (!ctxt.withErrorSuppressed(retryParsingFrom)) {
                                if (ctxt.isEof()) {
                                    break;
                                }
                                else {
                                    ctxt.next();
                                }
                            }
                            // EOF
                            else if (ctxt.isEof()) {
                                // stop
                                if (ctxt.withErrorSuppressed(stopParsingAt)) {
                                    break;
                                }
                                // failed
                                else if (ctxt.withErrorSuppressed(failParsingAt)) {
                                    failed = true;
                                    break;
                                }
                                break;
                            }
                            else {
                                break;
                            }
                        }
                        int end = ctxt.at;
                        T error = error(parseErrorDetail, handleError, ctxt, begin, end);
                        ctxt.mapResult(error, begin, end);
                        if (failed) {
                            ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                            return false;
                        }
                    }
                }
                return true;
            }
            @Override
            public String toString() {
                return "catchError";
            }
        };
    }

    /**
     * A {@link Parser} that attempts to parse with {@code this} and, on failure, applies
     * {@code handleError} to produce a recovery value, then resumes from the next token.
     *
     * @param handleError a function invoked on parse failure to build a recovery value
     * @return a fault-tolerant parser that recovers from errors using {@code handleError}
     */
    public final Parser<T> catchError(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError) {
        return catchError(handleError, Parsers.always(), Parsers.never(), Parsers.never());
    }

    /**
     * A {@link Parser} that attempts to parse with {@code this} and, on failure, applies
     * {@code handleError} to produce a recovery value, scanning forward until {@code retryParsingFrom} matches.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @return a fault-tolerant parser that recovers from errors using {@code handleError}
     */
    public final Parser<T> catchError(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                      final Parser<?> retryParsingFrom) {
        return catchError(handleError, retryParsingFrom, Parsers.never(), Parsers.never());
    }

    /**
     * A {@link Parser} that attempts to parse with {@code this} and, on failure, applies
     * {@code handleError} to produce a recovery value, scanning forward until {@code retryParsingFrom}
     * matches or failing hard when {@code failParsingAt} matches.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param failParsingAt    a parser whose match causes recovery to fail with an error
     * @return a fault-tolerant parser that recovers from errors using {@code handleError}
     */
    public final Parser<T> catchError(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                      final Parser<?> retryParsingFrom,
                                      final Parser<?> failParsingAt) {
        return catchError(handleError, retryParsingFrom, Parsers.never(), failParsingAt);
    }

    /**
     * A {@link Parser} that runs {@code this} between {@code before} and {@code after} delimiters,
     * recovering from parse errors using {@code handleError} and scanning forward according to
     * the provided control parsers.
     *
     * @param handleError   a function invoked on parse failure to build a recovery value
     * @param stopParsingAt a parser whose match causes recovery to stop gracefully
     * @param failParsingAt a parser whose match causes recovery to fail with an error
     * @param before        a parser matching the opening delimiter
     * @param after         a parser matching the closing delimiter
     * @return a fault-tolerant parser that runs {@code this} between the two delimiters
     */
    public final Parser<T> between(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                   final Parser<?> stopParsingAt,
                                   final Parser<?> failParsingAt,
                                   final Parser<?> before,
                                   final Parser<?> after) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                return betweenRecursive(handleError,
                                        stopParsingAt, failParsingAt,
                                        before, after,
                                        true, new AtomicInteger(), ctxt);
            }

            @Override
            public String toString() {
                return "between[catchError]";
            }
        };
    }

    /**
     * A {@link Parser} that runs {@code this} between {@code before} and {@code after} delimiters,
     * recovering from parse errors using {@code handleError}.
     *
     * @param handleError a function invoked on parse failure to build a recovery value
     * @param before      a parser matching the opening delimiter
     * @param after       a parser matching the closing delimiter
     * @return a fault-tolerant parser that runs {@code this} between the two delimiters
     */
    public final Parser<T> between(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                   final Parser<?> before,
                                   final Parser<?> after) {
        return between(handleError, Parsers.never(), Parsers.never(), before, after);
    }

    /**
     * A {@link Parser} that runs {@code this} between {@code before} and {@code after} delimiters,
     * recovering from parse errors using {@code handleError} and failing hard when {@code failParsingAt} matches.
     *
     * @param handleError   a function invoked on parse failure to build a recovery value
     * @param failParsingAt a parser whose match causes recovery to fail with an error
     * @param before        a parser matching the opening delimiter
     * @param after         a parser matching the closing delimiter
     * @return a fault-tolerant parser that runs {@code this} between the two delimiters
     */
    public final Parser<T> between(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                   final Parser<?> failParsingAt,
                                   final Parser<?> before,
                                   final Parser<?> after) {
        return between(handleError, Parsers.never(), failParsingAt, before, after);
    }

    private boolean betweenRecursive(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                     final Parser<?> stopParsingAt,
                                     final Parser<?> failParsingAt,
                                     final Parser<?> before,
                                     final Parser<?> after,
                                     final boolean firstTime,
                                     final AtomicInteger beginAt,
                                     final ParseContext ctxt) {
        final Result result = ctxt.getResult();
        if (firstTime) {
            if (before.apply(ctxt)) {
                beginAt.set(ctxt.at);
            }
            else {
                return false;
            }
        }
        if (!Parser.this.optional()
                        .followedBy(after)
                        .apply(ctxt)) {
            // render error
            final ParseErrorDetail parseErrorDetail = ctxt.renderError();

            // stop
            if (ctxt.withErrorSuppressed(stopParsingAt)) {
                int end = ctxt.at;
                T error = error(parseErrorDetail, handleError, ctxt, beginAt.get(), end);
                ctxt.mapResult(error, beginAt.get(), end);
                return true;
            }
            // fail
            if (ctxt.withErrorSuppressed(failParsingAt)) {
                ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                return false;
            }
            // EOF -> stop
            if (ctxt.isEof()) {
                int end = ctxt.at;
                T error = error(parseErrorDetail, handleError, ctxt, beginAt.get(), end);
                ctxt.mapResult(error, beginAt.get(), end);
                return true;
            }
            // catch error
            else {
                // next
                ctxt.next();
                boolean stop = false;
                boolean failed = false;
                while (true) {
                    // after
                    if (ctxt.withErrorSuppressed(after.peek())) {
                        break;
                    }
                    // stop
                    else if (ctxt.withErrorSuppressed(stopParsingAt)) {
                        stop = true;
                        break;
                    }
                    // fail
                    else if (ctxt.withErrorSuppressed(failParsingAt)) {
                        failed = true;
                        break;
                    }
                    else if (ctxt.isEof()) {
                        // stop
                        if (ctxt.withErrorSuppressed(stopParsingAt)) {
                            stop = true;
                            break;
                        }
                        // fail
                        else if (ctxt.withErrorSuppressed(failParsingAt)) {
                            failed = true;
                            break;
                        }
                        stop = true;
                        break;
                    }
                    else {
                        ctxt.next();
                    }
                }
                // error instance
                int end = ctxt.at;
                T error = error(parseErrorDetail, handleError, ctxt, beginAt.get(), end);
                ctxt.mapResult(error, beginAt.get(), end);
                if (stop) {
                    return true;
                }
                else if (failed) {
                    ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                    return false;
                }
                else {
                    return betweenRecursive(handleError,
                                            stopParsingAt,
                                            failParsingAt,
                                            before,
                                            after,
                                            false,
                                            beginAt,
                                            ctxt);
                }
            }
        }
        if (!firstTime) {
            if (ctxt instanceof ParserState) {
                ctxt.mapResult(result.geValue(),
                               beginAt.get(),
                               result.end());
            }
        }
        return true;
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} until {@code until} succeeds,
     * recovering from parse errors using {@code handleError} and scanning forward according to
     * the provided control parsers. The successfully parsed values and recovery values are
     * collected in a {@link List}.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param stopParsingAt    a parser whose match causes the repetition to stop gracefully
     * @param failParsingAt    a parser whose match causes recovery to fail with an error
     * @param until            a parser whose match terminates the repetition
     * @return a fault-tolerant parser that repeatedly matches and collects results until {@code until} succeeds
     */
    public final Parser<List<T>> manyUntil(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                           final Parser<?> retryParsingFrom,
                                           final Parser<?> stopParsingAt,
                                           final Parser<?> failParsingAt,
                                           final Parser<?> until) {
        return new Parser<List<T>>() {
            @Override
            boolean apply(ParseContext ctxt) {
                return manyUntilRecursive(handleError,
                                          retryParsingFrom,
                                          stopParsingAt, failParsingAt,
                                          until,
                                          new ArrayList<>(), true, new AtomicInteger(), ctxt);
            }

            @Override
            public String toString() {
                return "manyUntil[catchError]";
            }
        };
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} until {@code until} succeeds,
     * recovering from parse errors using {@code handleError} and resuming from {@code retryParsingFrom}.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param until            a parser whose match terminates the repetition
     * @return a fault-tolerant parser that repeatedly matches and collects results until {@code until} succeeds
     */
    public final Parser<List<T>> manyUntil(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                           final Parser<?> retryParsingFrom,
                                           final Parser<?> until) {
        return manyUntil(handleError, retryParsingFrom, Parsers.never(), Parsers.never(), until);
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} until end-of-file,
     * recovering from parse errors using {@code handleError} with automatic retry from the next token.
     *
     * @param handleError a function invoked on parse failure to build a recovery value
     * @return a fault-tolerant parser that repeatedly matches and collects results until end-of-file
     */
    public final Parser<List<T>> manyUntilEof(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError) {
        return manyUntil(handleError, Parsers.always(), Parsers.never(), Parsers.never(), Parsers.EOF);
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} until end-of-file,
     * recovering from parse errors using {@code handleError} and resuming from {@code retryParsingFrom}.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @return a fault-tolerant parser that repeatedly matches and collects results until end-of-file
     */
    public final Parser<List<T>> manyUntilEof(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                              final Parser<?> retryParsingFrom) {
        return manyUntil(handleError, retryParsingFrom, Parsers.never(), Parsers.never(), Parsers.EOF);
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} until end-of-file,
     * recovering from parse errors using {@code handleError} and scanning forward according to
     * the provided control parsers.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param stopParseAt      a parser whose match causes the repetition to stop gracefully
     * @param failParsingAt    a parser whose match causes recovery to fail with an error
     * @return a fault-tolerant parser that repeatedly matches and collects results until end-of-file
     */
    public final Parser<List<T>> manyUntilEof(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                              final Parser<?> retryParsingFrom,
                                              final Parser<?> stopParseAt,
                                              final Parser<?> failParsingAt) {
        return manyUntil(handleError, retryParsingFrom, stopParseAt, failParsingAt, Parsers.EOF);
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} until end-of-file,
     * recovering from parse errors using {@code handleError}, resuming from {@code retryParsingFrom},
     * and failing hard when {@code failParsingAt} matches.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param failParsingAt    a parser whose match causes recovery to fail with an error
     * @return a fault-tolerant parser that repeatedly matches and collects results until end-of-file
     */
    public final Parser<List<T>> manyUntilEof(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                              final Parser<?> retryParsingFrom,
                                              final Parser<?> failParsingAt) {
        return manyUntil(handleError, retryParsingFrom, Parsers.never(), failParsingAt, Parsers.EOF);
    }

    @SuppressWarnings("unchecked")
    private boolean manyUntilRecursive(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                       final Parser<?> retryParsingFrom,
                                       final Parser<?> stopParsingAt,
                                       final Parser<?> failParsingAt,
                                       final Parser<?> until,
                                       final List<T> values,
                                       final boolean firstTime,
                                       final AtomicInteger beginAt,
                                       final ParseContext ctxt) {
        if (firstTime) {
            values.clear();
            beginAt.set(ctxt.at);
        }
        //
        if (!Parser.this.until(until)
                        .apply(ctxt)) {
            // values
            if (ctxt instanceof ParserState) {
                final Result result = ctxt.getResult();
                values.addAll((List<T>) result.geValue());
            }
            // render error
            final int begin = ctxt.at;
            final ParseErrorDetail parseErrorDetail = ctxt.renderError();

            // stop
            if (ctxt.withErrorSuppressed(stopParsingAt)) {
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                return true;
            }
            // fail
            if (ctxt.withErrorSuppressed(failParsingAt)) {
                ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                return false;
            }
            // EOF -> stop
            if (ctxt.isEof()) {
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                return true;
            }
            // catch error
            else {
                ctxt.next();
                boolean stop = false;
                boolean failed = false;
                while (true) {
                    // stop
                    if (ctxt.withErrorSuppressed(stopParsingAt)) {
                        stop = true;
                        break;
                    }
                    // fail
                    else if (ctxt.withErrorSuppressed(failParsingAt)) {
                        failed = true;
                        break;
                    }
                    // until
                    else if (ctxt.withErrorSuppressed(until.peek())) {
                        break;
                    }
                    // retry
                    else if (!ctxt.withErrorSuppressed(retryParsingFrom)) {
                        if (ctxt.isEof()) {
                            break;
                        }
                        else {
                            ctxt.next();
                        }
                    }
                    else if (ctxt.isEof()) {
                        // stop
                        if (ctxt.withErrorSuppressed(stopParsingAt)) {
                            stop = true;
                            break;
                        }
                        // fail
                        else if (ctxt.withErrorSuppressed(failParsingAt)) {
                            failed = true;
                            break;
                        }
                        else if (ctxt.withErrorSuppressed(until.peek())) {
                            break;
                        }
                        stop = true;
                        break;
                    }
                    else {
                        break;
                    }
                }
                // error instance
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                // error caught
                if (stop) {
                    return true;
                }
                else if (failed) {
                    ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                    return false;
                }
                else {
                    return manyUntilRecursive(handleError,
                                              retryParsingFrom,
                                              stopParsingAt,
                                              failParsingAt,
                                              until,
                                              values,
                                              false,
                                              beginAt,
                                              ctxt);
                }
            }
        }
        // if apply == true
        if (!firstTime) {
            if (ctxt instanceof ParserState) {
                final Result result = ctxt.getResult();
                values.addAll((List<T>) result.geValue());
                ctxt.mapResult(values, beginAt.get(), ctxt.at);
            }
        }
        return true;
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} for the content between {@code before}
     * and {@code after} delimiters, recovering from parse errors using {@code handleError} and
     * scanning forward according to the provided control parsers.
     * The successfully parsed values and recovery values are collected in a {@link List}.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param stopParsingAt    a parser whose match causes the repetition to stop gracefully
     * @param failParsingAt    a parser whose match causes recovery to fail with an error
     * @param before           a parser matching the opening delimiter
     * @param after            a parser matching the closing delimiter
     * @return a fault-tolerant parser that repeatedly matches between the delimiters
     */
    public final Parser<List<T>> manyBetween(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                             final Parser<?> retryParsingFrom,
                                             final Parser<?> stopParsingAt,
                                             final Parser<?> failParsingAt,
                                             final Parser<?> before,
                                             final Parser<?> after) {
        return new Parser<List<T>>() {
            @Override
            boolean apply(ParseContext ctxt) {
                return manyBetweenRecursive(handleError,
                                            retryParsingFrom, stopParsingAt, failParsingAt,
                                            before, after,
                                            new ArrayList<>(), true, new AtomicInteger(), ctxt);
            }
            @Override
            public String toString() {
                return "manyBetween[catchError]";
            }
        };

    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} for the content between {@code before}
     * and {@code after} delimiters, recovering from parse errors using {@code handleError}.
     *
     * @param handleError a function invoked on parse failure to build a recovery value
     * @param before      a parser matching the opening delimiter
     * @param after       a parser matching the closing delimiter
     * @return a fault-tolerant parser that repeatedly matches between the delimiters
     */
    public final Parser<List<T>> manyBetween(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                             final Parser<?> before,
                                             final Parser<?> after) {
        return manyBetween(handleError,
                           Parsers.always(),
                           Parsers.never(),
                           Parsers.never(),
                           before,
                           after);
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} for the content between {@code before}
     * and {@code after} delimiters, recovering from parse errors using {@code handleError} and
     * resuming from {@code retryParsingFrom}.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param before           a parser matching the opening delimiter
     * @param after            a parser matching the closing delimiter
     * @return a fault-tolerant parser that repeatedly matches between the delimiters
     */
    public final Parser<List<T>> manyBetween(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                             final Parser<?> retryParsingFrom,
                                             final Parser<?> before,
                                             final Parser<?> after) {
        return manyBetween(handleError,
                           retryParsingFrom,
                           Parsers.never(),
                           Parsers.never(),
                           before,
                           after);
    }

    /**
     * A {@link Parser} that repeatedly applies {@code this} for the content between {@code before}
     * and {@code after} delimiters, recovering from parse errors using {@code handleError},
     * resuming from {@code retryParsingFrom}, and failing hard when {@code failParsingAt} matches.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param failParsingAt    a parser whose match causes recovery to fail with an error
     * @param before           a parser matching the opening delimiter
     * @param after            a parser matching the closing delimiter
     * @return a fault-tolerant parser that repeatedly matches between the delimiters
     */
    public final Parser<List<T>> manyBetween(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                             final Parser<?> retryParsingFrom,
                                             final Parser<?> failParsingAt,
                                             final Parser<?> before,
                                             final Parser<?> after) {
        return manyBetween(handleError,
                           retryParsingFrom,
                           Parsers.never(),
                           failParsingAt,
                           before,
                           after);
    }

    @SuppressWarnings("unchecked")
    private boolean manyBetweenRecursive(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                         final Parser<?> retryParsingFrom,
                                         final Parser<?> stopParsingAt,
                                         final Parser<?> failParsingAt,
                                         final Parser<?> before,
                                         final Parser<?> after,
                                         final List<T> values,
                                         final boolean firstTime,
                                         final AtomicInteger beginAt,
                                         final ParseContext ctxt) {
        if (firstTime) {
            values.clear();
            beginAt.set(ctxt.at);
        }
        if (firstTime
                && !before.peek().apply(ctxt)) {
            return false;
        }
        else if (firstTime
                    ? !Parser.this.many()
                                  .between(before, after)
                                  .apply(ctxt)
                    : !Parser.this.many()
                                  .followedBy(after)
                                  .apply(ctxt)) {
            // values
            if (ctxt instanceof ParserState) {
                final Result result = ctxt.getResult();
                values.addAll((List<T>) result.geValue());
            }
            // render error
            final int begin = ctxt.at;
            final ParseErrorDetail parseErrorDetail = ctxt.renderError();
            // stop
            if (ctxt.withErrorSuppressed(stopParsingAt)) {
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                return true;
            }
            // failed
            if (ctxt.withErrorSuppressed(failParsingAt)) {
                ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                return false;
            }
            // EOF -> stop
            if (ctxt.isEof()) {
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                return true;
            }
            // catch error
            else {
                ctxt.next();
                boolean stop = false;
                boolean failed = false;
                while (true) {
                    // stop
                    if (ctxt.withErrorSuppressed(stopParsingAt)) {
                        stop = true;
                        break;
                    }
                    // failed
                    else if (ctxt.withErrorSuppressed(failParsingAt)) {
                        failed = true;
                        break;
                    }
                    // after
                    else if (ctxt.withErrorSuppressed(after.peek())) {
                        break;
                    }
                    // retry
                    else if (!ctxt.withErrorSuppressed(retryParsingFrom)) {
                        if (ctxt.isEof()) {
                            break;
                        }
                        else {
                            ctxt.next();
                        }
                    }
                    // EOF
                    else if (ctxt.isEof()) {
                        // stop
                        if (ctxt.withErrorSuppressed(stopParsingAt)) {
                            stop = true;
                            break;
                        }
                        // failed
                        else if (ctxt.withErrorSuppressed(failParsingAt)) {
                            failed = true;
                            break;
                        }
                        // after
                        else if (ctxt.withErrorSuppressed(after.peek())) {
                            break;
                        }
                        stop = true;
                        break;
                    }
                    else {
                        break;
                    }
                }
                // error instance
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                // error caught
                if (stop) {
                    return true;
                }
                else if (failed) {
                    ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                    return false;
                }
                else {
                    return manyBetweenRecursive(handleError,
                                                retryParsingFrom,
                                                stopParsingAt,
                                                failParsingAt,
                                                before,
                                                after,
                                                values,
                                                false,
                                                beginAt,
                                                ctxt);
                }
            }
        }
        // if apply == true
        if (!firstTime) {
            if (ctxt instanceof ParserState) {
                final Result result = ctxt.getResult();
                values.addAll((List<T>) result.geValue());
                ctxt.mapResult(values, beginAt.get(), ctxt.at);
            }
        }
        return true;
    }

    /**
     * A {@link Parser} that runs {@code this} separated by {@code separator} between
     * {@code before} and {@code after} delimiters, recovering from parse errors using
     * {@code handleError} and scanning forward according to the provided control parsers.
     * The successfully parsed values and recovery values are collected in a {@link List}.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param stopParsingAt    a parser whose match causes the repetition to stop gracefully
     * @param failParsingAt    a parser whose match causes recovery to fail with an error
     * @param separator        a parser matching the element separator
     * @param before           a parser matching the opening delimiter
     * @param after            a parser matching the closing delimiter
     * @return a fault-tolerant parser that matches separated elements between the delimiters
     */
    public final Parser<List<T>> sepByBetween(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                              final Parser<?> retryParsingFrom,
                                              final Parser<?> stopParsingAt,
                                              final Parser<?> failParsingAt,
                                              final Parser<?> separator,
                                              final Parser<?> before,
                                              final Parser<?> after) {
        return new Parser<List<T>>() {
            @Override
            boolean apply(ParseContext ctxt) {
                return sepByBetweenRecursive(handleError,
                                             retryParsingFrom,
                                             stopParsingAt,
                                             failParsingAt,
                                             separator,
                                             before,
                                             after,
                                             new ArrayList<>(),
                                             true,
                                             new AtomicInteger(),
                                             ctxt);
            }
            @Override
            public String toString() {
                return "sepByBetween[catchError]";
            }
        };
    }

    /**
     * A {@link Parser} that runs {@code this} separated by {@code separator} between
     * {@code before} and {@code after} delimiters, recovering from parse errors using {@code handleError}.
     *
     * @param handleError a function invoked on parse failure to build a recovery value
     * @param separator   a parser matching the element separator
     * @param before      a parser matching the opening delimiter
     * @param after       a parser matching the closing delimiter
     * @return a fault-tolerant parser that matches separated elements between the delimiters
     */
    public final Parser<List<T>> sepByBetween(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                              final Parser<?> separator,
                                              final Parser<?> before,
                                              final Parser<?> after) {
        return sepByBetween(handleError, separator, Parsers.never(), Parsers.never(), separator, before, after);
    }

    /**
     * A {@link Parser} that runs {@code this} separated by {@code separator} between
     * {@code before} and {@code after} delimiters, recovering from parse errors using
     * {@code handleError} and resuming from {@code retryParsingFrom}.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param separator        a parser matching the element separator
     * @param before           a parser matching the opening delimiter
     * @param after            a parser matching the closing delimiter
     * @return a fault-tolerant parser that matches separated elements between the delimiters
     */
    public final Parser<List<T>> sepByBetween(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                              final Parser<?> retryParsingFrom,
                                              final Parser<?> separator,
                                              final Parser<?> before,
                                              final Parser<?> after) {
        return sepByBetween(handleError, retryParsingFrom, Parsers.never(), Parsers.never(), separator, before, after);
    }

    /**
     * A {@link Parser} that runs {@code this} separated by {@code separator} between
     * {@code before} and {@code after} delimiters, recovering from parse errors using
     * {@code handleError}, resuming from {@code retryParsingFrom}, and failing hard when
     * {@code failParsingAt} matches.
     *
     * @param handleError      a function invoked on parse failure to build a recovery value
     * @param retryParsingFrom a parser whose match signals that normal parsing may resume
     * @param failParsingAt    a parser whose match causes recovery to fail with an error
     * @param separator        a parser matching the element separator
     * @param before           a parser matching the opening delimiter
     * @param after            a parser matching the closing delimiter
     * @return a fault-tolerant parser that matches separated elements between the delimiters
     */
    public final Parser<List<T>> sepByBetween(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                              final Parser<?> retryParsingFrom,
                                              final Parser<?> failParsingAt,
                                              final Parser<?> separator,
                                              final Parser<?> before,
                                              final Parser<?> after) {
        return sepByBetween(handleError, retryParsingFrom, Parsers.never(), failParsingAt, separator, before, after);
    }

    @SuppressWarnings("unchecked")
    private boolean sepByBetweenRecursive(final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                          final Parser<?> retryParsingFrom,
                                          final Parser<?> stopParsingAt,
                                          final Parser<?> failParsingAt,
                                          final Parser<?> separator,
                                          final Parser<?> before,
                                          final Parser<?> after,
                                          final List<T> values,
                                          final boolean firstTime,
                                          final AtomicInteger beginAt,
                                          final ParseContext ctxt) {
        if (firstTime) {
            values.clear();
            beginAt.set(ctxt.at);
        }
        if (firstTime
                && !before.peek().apply(ctxt)) {
            return false;
        }
        else if (firstTime
                    ? !Parser.this.sepBy(separator)
                                  .between(before, after)
                                  .apply(ctxt)
                    : !Parser.this.sepBy(separator)
                                  .followedBy(after)
                                  .apply(ctxt)) {
            // get values
            if (ctxt instanceof ParserState) {
                final Result result = ctxt.getResult();
                values.addAll((List<T>) result.geValue());
            }
            // render error
            final int begin = ctxt.at;
            final ParseErrorDetail parseErrorDetail = ctxt.renderError();

            // stop
            if (ctxt.withErrorSuppressed(stopParsingAt)) {
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                return true;
            }
            // fail
            if (ctxt.withErrorSuppressed(failParsingAt)) {
                ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                return false;
            }
            // EOF -> stop
            if (ctxt.isEof()) {
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                return true;
            }
            // catch error
            else {
                // next
                ctxt.next();
                boolean stop = false;
                boolean failed = false;
                while (true) {
                    // stop
                    if (ctxt.withErrorSuppressed(stopParsingAt)) {
                        stop = true;
                        break;
                    }
                    // fail
                    else if (ctxt.withErrorSuppressed(failParsingAt)) {
                        failed = true;
                        break;
                    }
                    // after
                    else if (ctxt.withErrorSuppressed(after.peek())) {
                        break;
                    }
                    // retry
                    else if (!ctxt.withErrorSuppressed(retryParsingFrom)) {
                        if (ctxt.isEof()) {
                            break;
                        }
                        else {
                            ctxt.next();
                        }
                    }
                    // EOF
                    else if (ctxt.isEof()) {
                        // stop
                        if (ctxt.withErrorSuppressed(stopParsingAt)) {
                            stop = true;
                            break;
                        }
                        // fail
                        else if (ctxt.withErrorSuppressed(failParsingAt)) {
                            failed = true;
                            break;
                        }
                        else if (ctxt.withErrorSuppressed(after.peek())) {
                            break;
                        }
                        stop = true;
                        break;
                    }
                    else {
                        break;
                    }
                }
                // error instance
                mapResult(values, parseErrorDetail, handleError, ctxt, beginAt, begin);
                if (stop) {
                    return true;
                }
                else if (failed) {
                    ctxt.raise(ErrorType.UNEXPECTED, failParsingAt);
                    return false;
                }
                else {
                    return sepByBetweenRecursive(handleError,
                                                 retryParsingFrom,
                                                 stopParsingAt,
                                                 failParsingAt,
                                                 separator,
                                                 before,
                                                 after,
                                                 values,
                                                 false,
                                                 beginAt,
                                                 ctxt);
                }
            }
        }
        // if apply == true
        if (!firstTime) {
            if (ctxt instanceof ParserState) {
                final Result result = ctxt.getResult();
                values.addAll((List<T>) result.geValue());
                ctxt.mapResult(values, beginAt.get(), ctxt.at);
            }
        }
        return true;
    }

    private static <T> void mapResult(final List<T> values,
                                      final ParseErrorDetail parseErrorDetail,
                                      final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                                      final ParseContext ctxt,
                                      final AtomicInteger beginAt,
                                      final int begin) {
        int end = ctxt.at;
        T error = error(parseErrorDetail, handleError, ctxt, begin, end);
        if (error != null) {
            values.add(error);
        }
        ctxt.mapResult(values, beginAt.get(), end);
    }

    private static <T> T error(final ParseErrorDetail parseErrorDetail,
                               final Map3<ParseErrorDetail, SourceLocation, List<Token>, ? extends T> handleError,
                               final ParseContext ctxt,
                               final int begin,
                               final int end) {
        T result = null;
        if (ctxt instanceof ParserState parserState) {
            final List<Token> tokens = parserState.getTokens(begin, end - 1);
            if (!tokens.isEmpty()) {
                final Token first = tokens.getFirst();
                final Token last = tokens.getLast();
                final SourceLocation sourceLocation = new SourceLocation(ctxt.getSourcePosition(first.index()),
                                                                         ctxt.getSourcePosition(last.index() + last.length()));
                result = handleError.map(parseErrorDetail, sourceLocation, tokens);
            }
        }
        return result;
    }

    /**
     * A {@link Parser} that sequentially executes {@code this} and then {@code parser},
     * whose return value is ignored.
     *
     * @param parser the parser to run after {@code this}, whose result is discarded
     * @return a parser that returns this parser's result and requires {@code parser} to follow
     */
    public final Parser<T> followedBy(Parser<?> parser) {
        return Parsers.sequence(this, parser, InternalFunctors.<T, Object>firstOfTwo());
    }

    /**
     * A {@link Parser} that succeeds if {@code this} succeeds
     * and the pattern recognized by {@code parser} is not following.
     *
     * @param parser the parser that must not match after {@code this}
     * @return a parser that succeeds only when {@code parser} does not follow
     */
    public final Parser<T> notFollowedBy(Parser<?> parser) {
        return followedBy(parser.not());
    }

    /**
     * {@code p.many()} is equivalent to {@code p*} in EBNF.
     * The return values are collected and returned in a {@link List}.
     *
     * @return a parser that matches zero or more times and collects all results
     */
    public final Parser<List<T>> many() {
        return atLeast(0);
    }

    /**
     * {@code p.skipMany()} is equivalent to {@code p*} in EBNF.
     * The return values are discarded.
     *
     * @return a parser that matches zero or more times and discards all results
     */
    public final Parser<Void> skipMany() {
        return skipAtLeast(0);
    }

    /**
     * {@code p.many1()} is equivalent to {@code p+} in EBNF.
     * The return values are collected and returned in a {@link List}.
     *
     * @return a parser that matches one or more times and collects all results
     */
    public final Parser<List<T>> many1() {
        return atLeast(1);
    }

    /**
     * {@code p.skipMany1()} is equivalent to {@code p+} in EBNF.
     * The return values are discarded.
     *
     * @return a parser that matches one or more times and discards all results
     */
    public final Parser<Void> skipMany1() {
        return skipAtLeast(1);
    }

    /**
     * A {@link Parser} that runs {@code this} parser greedily for at least {@code min} times.
     * The return values are collected and returned in a {@link List}.
     *
     * @param min the minimum number of times to match
     * @return a parser that matches at least {@code min} times
     */
    public final Parser<List<T>> atLeast(int min) {
        return new RepeatAtLeastParser<T>(this, Checks.checkMin(min));
    }

    /**
     * A {@link Parser} that runs {@code this} parser greedily
     * for at least {@code min} times and ignores the return values.
     *
     * @param min the minimum number of times to match
     * @return a parser that matches at least {@code min} times and discards all results
     */
    public final Parser<Void> skipAtLeast(int min) {
        return new SkipAtLeastParser(this, Checks.checkMin(min));
    }

    /**
     * A {@link Parser} that sequentially runs {@code this}
     * for {@code n} times and ignores the return values.
     *
     * @param n the exact number of times to run
     * @return a parser that matches exactly {@code n} times and discards all results
     */
    public final Parser<Void> skipTimes(int n) {
        return skipTimes(n, n);
    }

    /**
     * A {@link Parser} that runs {@code this}
     * for {@code n} times and collects the return values in a {@link List}.
     *
     * @param n the exact number of times to run
     * @return a parser that matches exactly {@code n} times and collects all results
     */
    public final Parser<List<T>> times(int n) {
        return times(n, n);
    }

    /**
     * A {@link Parser} that runs {@code this} parser
     * for at least {@code min} times and up to {@code max} times.
     * The return values are collected and returned in a {@link List}.
     *
     * @param min the minimum number of times to match
     * @param max the maximum number of times to match
     * @return a parser that matches between {@code min} and {@code max} times
     */
    public final Parser<List<T>> times(int min, int max) {
        Checks.checkMinMax(min, max);
        return new RepeatTimesParser<T>(this, min, max);
    }

    /**
     * A {@link Parser} that runs {@code this} parser
     * for at least {@code min} times and up to {@code max} times,
     * with all the return values ignored.
     *
     * @param min the minimum number of times to match
     * @param max the maximum number of times to match
     * @return a parser that matches between {@code min} and {@code max} times and discards results
     */
    public final Parser<Void> skipTimes(int min, int max) {
        Checks.checkMinMax(min, max);
        return new SkipTimesParser(this, min, max);
    }

    /**
     * A {@link Parser} that runs {@code this} parser and transforms the return value using {@code map}.
     *
     * @param <R> the result type after mapping
     * @param map the function to apply to the parsed value
     * @return a parser that transforms its result with {@code map}
     */
    public final <R> Parser<R> map(final Function<? super T, ? extends R> map) {
        return new Parser<R>() {
            @Override
            boolean apply(final ParseContext ctxt) {
                int begin = ctxt.at;
                final boolean ok = Parser.this.apply(ctxt);
                if (ok) {
                    ctxt.mapResult(map.apply(Parser.this.getReturn(ctxt)), begin, ctxt.at);
                }
                return ok;
            }
            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs {@code this} and transforms the return value using {@code map},
     * setting the result without preserving the source range information of the original parse.
     *
     * @param map the mapping function to apply to the parsed value
     * @param <R> the type of the mapped result
     * @return a new parser that applies {@code map} to the result of {@code this}
     */
    public final <R> Parser<R> apply(final Function<? super T, ? extends R> map) {
        return new Parser<R>() {
            @Override
            boolean apply(final ParseContext ctxt) {
                int begin = ctxt.at;
                final boolean ok = Parser.this.apply(ctxt);
                if (ok) {
                    ctxt.setResult(map.apply(Parser.this.getReturn(ctxt)), begin, ctxt.at);
                }
                return ok;
            }
            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs {@code this} and transforms the return value using {@code map},
     * providing both the parsed value and the corresponding {@link SourceLocation} to the mapping function.
     *
     * @param map a function accepting the parsed value and its source location, returning the mapped result
     * @param <R> the type of the mapped result
     * @return a new parser that applies {@code map} to the result and source location of {@code this}
     */
    public final <R> Parser<R> mapLocation(final Map2<? super T, SourceLocation, ? extends R> map) {
        return new Parser<R>() {
            @Override
            boolean apply(final ParseContext ctxt) {
                int begin = ctxt.at;
                final boolean ok = Parser.this.apply(ctxt);
                if (ok) {
                    ctxt.mapResult(map.map(Parser.this.getReturn(ctxt), ctxt.getSourceLocation()),
                                   begin,
                                   ctxt.at);
                }
                return ok;
            }
            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * {@code p1.or(p2)} is equivalent to {@code p1 | p2} in EBNF.
     *
     * @param alternative the alternative parser to run if this fails
     * @return a parser that tries this parser first, then {@code alternative}
     */
    public final Parser<T> or(Parser<? extends T> alternative) {
        return Parsers.or(this, alternative);
    }

    /**
     * {@code a.otherwise(fallback)} runs {@code fallback} when {@code a} matches zero input.
     * This is different from {@code a.or(alternative)}
     * where {@code alternative} is run whenever {@code a} fails to match.
     *
     * <p>One should usually use {@link #or}.
     *
     * @param fallback the parser to run if {@code this} matches no input
     * @return a parser that falls back to {@code fallback} when this consumes no input
     */
    public final Parser<T> otherwise(Parser<? extends T> fallback) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                final Result result = ctxt.getResult();
                final int at = ctxt.at;
                final int step = ctxt.step;
                if (Parser.this.apply(ctxt)) {
                    return true;
                }
                if (ctxt.errorIndex() > at) {
                    return false;
                }
                ctxt.set(step, at, result);
                return fallback.apply(ctxt);
            }
            @Override
            public String toString() {
                return "otherwise";
            }
        };
    }

    /**
     * {@code p.asOptional()} is equivalent to {@code p?} in EBNF.
     * {@code Optional.empty()} is the result when {@code this} fails with no partial match.
     * Note that {@link Optional} prohibits nulls so make sure {@code this} does not result in {@code null}.
     *
     * @return a parser that wraps its result in {@link Optional}, or returns {@code Optional.empty()} on failure
     */
    public final Parser<Optional<T>> asOptional() {
        return map(Optional::of).optional(() -> Optional.empty());
    }

    /**
     * A {@link Parser} that returns {@code null}
     * if {@code this} fails with no partial match.
     *
     * @return a parser that always succeeds, returning {@code null} on failure
     */
    public final Parser<T> optional() {
        return optional(null);
    }

    /**
     * A {@link Parser} that returns the value supplied by {@code defaultValue}
     * if {@code this} fails with no partial match.
     *
     * @param defaultValue a supplier invoked to produce the fallback value when {@code this} fails
     * @return a new parser that always succeeds, returning the parsed value or the supplied default
     */
    public final Parser<T> optional(Supplier<T> defaultValue) {
        return Parsers.or(this, Parsers.constant(defaultValue))
                      .map(v -> v);
    }

    /**
     * A {@link Parser} that fails if {@code this} succeeds.
     * Any input consumption is undone.
     *
     * @return a parser that succeeds only when {@code this} fails
     */
    public final Parser<?> not() {
        return not(toString());
    }

    /**
     * A {@link Parser} that fails if {@code this} succeeds.
     * Any input consumption is undone.
     *
     * @param unexpected the name of what is not expected
     * @return a parser that succeeds only when {@code this} fails, reporting {@code unexpected}
     */
    public final Parser<?> not(String unexpected) {
        return peek().ifelse(Parsers.unexpected(unexpected), Parsers.always());
    }

    /**
     * A {@link Parser} that runs {@code this} and undoes any input consumption if it succeeds.
     *
     * @return a parser that looks ahead without consuming input
     */
    public final Parser<T> peek() {
        return new Parser<T>() {
            @Override
            public Parser<T> label(String name) {
                return Parser.this.label(name).peek();
            }
            @Override
            boolean apply(ParseContext ctxt) {
                int step = ctxt.step;
                int at = ctxt.at;
                boolean ok = Parser.this.apply(ctxt);
                if (ok) {
                    ctxt.setAt(step, at);
                }
                return ok;
            }
            @Override
            public String toString() {
                return "peek";
            }
        };
    }

    /**
     * A {@link Parser} that undoes any partial match if {@code this} fails.
     * In other words, the parser either fully matches, or matches none.
     *
     * @return a parser that commits fully or not at all
     */
    public final Parser<T> atomic() {
        return new Parser<T>() {
            @Override
            public Parser<T> label(String name) {
                return Parser.this.label(name).atomic();
            }
            @Override
            boolean apply(ParseContext ctxt) {
                int at = ctxt.at;
                int step = ctxt.step;
                boolean r = Parser.this.apply(ctxt);
                if (r) {
                    ctxt.step = step + 1;
                } else {
                    ctxt.setAt(step, at);
                }
                return r;
            }
            @Override
            public String toString() {
                return Parser.this.toString();
            }
        };
    }

    /**
     * A {@link Parser} that returns {@code true}
     * if {@code this} succeeds, {@code false} otherwise.
     *
     * @return a parser that always succeeds and returns whether {@code this} matched
     */
    public final Parser<Boolean> succeeds() {
        return ifelse(Parsers.TRUE, Parsers.FALSE);
    }

    /**
     * A {@link Parser} that returns {@code true}
     * if {@code this} fails, {@code false} otherwise.
     *
     * @return a parser that always succeeds and returns whether {@code this} failed
     */
    public final Parser<Boolean> fails() {
        return ifelse(Parsers.FALSE, Parsers.TRUE);
    }

    /**
     * A {@link Parser} that runs {@code consequence} if {@code this} succeeds,
     * or {@code alternative} otherwise.
     *
     * @param <R>         the result type
     * @param consequence the parser to run when {@code this} succeeds
     * @param alternative the parser to run when {@code this} fails
     * @return a parser that branches on whether {@code this} succeeds
     */
    public final <R> Parser<R> ifelse(Parser<? extends R> consequence,
                                      Parser<? extends R> alternative) {
        return ifelse(__ -> consequence, alternative);
    }

    /**
     * A {@link Parser} that maps the result of {@code this} to a {@link Parser} via {@code consequence}
     * and runs it if {@code this} succeeds, or runs {@code alternative} otherwise.
     *
     * @param <R>         the result type
     * @param consequence a function mapping this parser's result to the next parser to run
     * @param alternative the parser to run when {@code this} fails
     * @return a parser that branches on whether {@code this} succeeds
     */
    public final <R> Parser<R> ifelse(final Function<? super T, ? extends Parser<? extends R>> consequence,
                                      final Parser<? extends R> alternative) {
        return new Parser<R>() {
            @Override
            boolean apply(ParseContext ctxt) {
                final Result result = ctxt.getResult();
                final int step = ctxt.step;
                final int at = ctxt.at;
                if (ctxt.withErrorSuppressed(Parser.this)) {
                    Parser<? extends R> parser = consequence.apply(Parser.this.getReturn(ctxt));
                    return parser.apply(ctxt);
                }
                ctxt.set(step, at, result);
                return alternative.apply(ctxt);
            }
            @Override
            public String toString() {
                return "ifelse";
            }
        };
    }

    /**
     * A {@link Parser} that runs {@code this} and succeeds only if the parsed value satisfies
     * the given {@code condition}; fails (without consuming input) otherwise.
     *
     * @param condition a predicate applied to the parsed value; the parser succeeds when it returns {@code true}
     * @return a new parser that filters results of {@code this} by the given condition
     */
    public final Parser<T> acceptIf(final Function<? super T, Boolean> condition) {
        final AtomicReference<T> result = new AtomicReference<>();
        return _acceptIf(condition)
                   .result(v -> result.set(v))
                   .ifelse(Parsers.always(),
                           Parsers.never())
                   .map(v -> result.get());
    }

    private final Parser<T> _acceptIf(final Function<? super T, Boolean> condition) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                if (ctxt.withErrorSuppressed(Parser.this)) {
                    return condition.apply(Parser.this.getReturn(ctxt));
                }
                return false;
            }
            @Override
            public String toString() {
                return "acceptIf[" + Parser.this + "]";
            }
        };
    }

    /**
     * A {@link Parser} that runs {@code this} and, on success, passes the parsed value to
     * {@code action} as a side-effect before returning the value unchanged.
     *
     * @param action a consumer invoked with the parsed value when parsing succeeds
     * @return a new parser that executes {@code action} on success and preserves the original result
     */
    public final  Parser<T> result(final Consumer<? super T> action) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                boolean ok = Parser.this.apply(ctxt);
                if (ok) {
                    action.accept(Parser.this.getReturn(ctxt));
                }
                return ok;
            }
            @Override
            public String toString() {
                return "result[" + Parser.this + "]";
            }
        };
    }

    /**
     * A {@link Parser} that reports an error about {@code name} expected,
     * if {@code this} fails with no partial match.
     *
     * @param name the label used in error messages
     * @return a parser with a descriptive label for error reporting
     */
    public Parser<T> label(final String name) {
        return new Parser<T>() {
            @Override
            public Parser<T> label(String overrideName) {
                return Parser.this.label(overrideName);
            }
            @Override
            boolean apply(ParseContext ctxt) {
                return ctxt.applyNewNode(Parser.this, name);
            }
            @Override
            public String toString() {
                return name;
            }
        };
    }

    /**
     * Casts {@code this} to a {@link Parser} of type {@code R}.
     * Use it only if you know the parser actually returns a value of type {@code R}.
     *
     * @param <R> the target type
     * @return this parser cast to {@code Parser<R>}
     */
    @SuppressWarnings("unchecked")
    public final <R> Parser<R> cast() {
        return (Parser<R>) this;
    }

    /**
     * A {@link Parser} that runs {@code this} between {@code before} and {@code after}.
     * The return value of {@code this} is preserved.
     *
     * <p>
     * Equivalent to {@link Parsers#between(Parser, Parser, Parser)},
     * which preserves the natural order of the parsers in the argument list,
     * but is a bit more verbose.
     *
     * @param before the parser matching the opening delimiter
     * @param after  the parser matching the closing delimiter
     * @return a parser that matches {@code before}, {@code this}, {@code after} and returns this result
     */
    public final Parser<T> between(Parser<?> before,
                                   Parser<?> after) {
        return before.next(followedBy(after));
    }

    /**
     * A {@link Parser} that runs {@code this} 1 or more times separated by {@code delim}.
     * The return values are collected in a {@link List}.
     *
     * @param delim the separator parser
     * @return a parser that matches one or more occurrences separated by {@code delim}
     */
    public final Parser<List<T>> sepBy1(Parser<?> delim) {
        final Parser<T> afterFirst = delim.asDelimiter().next(this);
        return next((Function<T, Parser<List<T>>>) firstValue -> new RepeatAtLeastParser<T>(afterFirst, 0, ListFactory.arrayListFactoryWithFirstElement(firstValue)));
    }

    /**
     * A {@link Parser} that runs {@code this} 0 or more times separated by {@code delim}.
     * The return values are collected in a {@link List}.
     *
     * @param delim the separator parser
     * @return a parser that matches zero or more occurrences separated by {@code delim}
     */
    public final Parser<List<T>> sepBy(Parser<?> delim) {
        return Parsers.or(sepBy1(delim), EmptyListParser.<T>instance());
    }

    /**
     * A {@link Parser} that runs {@code this} for 0 or more times delimited and terminated by {@code delim}.
     * The return values are collected in a {@link List}.
     *
     * @param delim the delimiter and terminator parser
     * @return a parser that matches zero or more occurrences each followed by {@code delim}
     */
    public final Parser<List<T>> endBy(Parser<?> delim) {
        return followedBy(delim).many();
    }

    /**
     * A {@link Parser} that runs {@code this} for 1 or more times delimited and terminated by {@code delim}.
     * The return values are collected in a {@link List}.
     *
     * @param delim the delimiter and terminator parser
     * @return a parser that matches one or more occurrences each followed by {@code delim}
     */
    public final Parser<List<T>> endBy1(Parser<?> delim) {
        return followedBy(delim).many1();
    }

    /**
     * A {@link Parser} that runs {@code this} for 1 ore more times separated
     * and optionally terminated by {@code delim}.
     * For example: {@code "foo;foo;foo"} and {@code "foo;foo;"}
     * both matches {@code foo.sepEndBy1(semicolon)}.
     * The return values are collected in a {@link List}.
     *
     * @param delim the separator/terminator parser
     * @return a parser that matches one or more occurrences separated and optionally terminated by {@code delim}
     */
    public final Parser<List<T>> sepEndBy1(final Parser<?> delim) {
        return next(first -> new DelimitedParser<T>(this, delim, ListFactory.arrayListFactoryWithFirstElement(first)));
    }

    /**
     * A {@link Parser} that runs {@code this} for 0 ore more times separated
     * and optionally terminated by {@code
     * delim}. For example: {@code "foo;foo;foo"} and {@code "foo;foo;"}
     * both matches {@code foo.sepEndBy(semicolon)}.
     * The return values are collected in a {@link List}.
     *
     * @param delim the separator/terminator parser
     * @return a parser that matches zero or more occurrences separated and optionally terminated by {@code delim}
     */
    public final Parser<List<T>> sepEndBy(Parser<?> delim) {
        return Parsers.or(sepEndBy1(delim), EmptyListParser.<T>instance());
    }

    /**
     * A {@link Parser} that runs {@code op} for 0 or more times greedily,
     * then runs {@code this}.
     * The {@link Function} objects returned from {@code op} are applied
     * from right to left to the return value of {@code p}.
     *
     * {@code p.prefix(op)} is equivalent to {@code op* p} in EBNF.
     *
     * @param op the prefix operator parser returning a unary function
     * @return a parser that applies zero or more prefix operators to the result of {@code this}
     */
    public final Parser<T> prefix(Parser<? extends Function<? super T, ? extends T>> op) {
        final AtomicReference<ParseContext> ctxt = new AtomicReference<>();
        return Parsers.context(c -> ctxt.set(c))
                      .next(Parsers.sequence(op.many(), this, (a, b) -> applyPrefixOperators(ctxt.get(), a, b)));
    }

   /**
    * A {@link Parser} that runs {@code this} and then runs {@code op} for 0 or more times greedily.
    * The {@link Function} objects returned from {@code op} are applied from left to right
    * to the return value of p.
    *
    * <p>This is the preferred API to avoid {@code StackOverflowError} in left-recursive parsers.
    * For example, to parse array types in the form of "T[]" or "T[][]", the following
    * left recursive grammar will fail:
    * <pre>{@code
    *   Terminals terms = Terminals.operators("[", "]");
    *   Parser.Reference<Type> ref = Parser.newReference();
    *   ref.set(Parsers.or(leafTypeParser,
    *                      Parsers.sequence(ref.lazy(),
    *                      terms.phrase("[", "]"),
    *                      new Unary<Type>() {...})));
    *   return ref.get();
    * }</pre>
    *
    * A correct implementation is:
    * <pre>{@code
    *   Terminals terms = Terminals.operators("[", "]");
    *   return leafTypeParer.postfix(terms.phrase("[", "]").retn(new Unary<Type>() {...}));
    * }</pre>
    *
    * A not-so-obvious example, is to parse the {@code expr ? a : b} ternary operator.
    * It too is a left recursive grammar.
    * And un-intuitively it can also be thought as a postfix operator.
    * Basically, we can parse "? a : b" as a whole into a unary operator
    * that accepts the condition expression as input and outputs the full ternary expression:
    * <pre>{@code
    *   Parser<Expr> ternary(Parser<Expr> expr) {
    *     return expr.postfix(
    *       Parsers.sequence(terms.token("?"),
    *                        expr,
    *                        terms.token(":"),
    *                        expr,
    *                        (unused, then, unused, orelse)
    *                               -> cond
    *                               -> new TernaryExpr(cond, then, orelse)));
    *   }
    * }</pre>
    * {@link OperatorTable} also handles left recursion transparently.
    *
    * {@code p.postfix(op)} is equivalent to {@code p op*} in EBNF.
    *
    * @param op the postfix operator parser returning a unary function
    * @return a parser that applies zero or more postfix operators to the result of {@code this}
    */
    public final Parser<T> postfix(Parser<? extends Function<? super T, ? extends T>> op) {
        final AtomicReference<ParseContext> ctxt = new AtomicReference<>();
        return Parsers.context(c -> ctxt.set(c))
                      .next(Parsers.sequence(this, op.many(), (a,b) -> applyPostfixOperators(ctxt.get(), a, b)));
    }

    /**
     * A {@link Parser} that parses non-associative infix operator.
     * Runs {@code this} for the left operand, and then runs {@code op}
     * and {@code this} for the operator and the right operand optionally.
     * The {@link BiFunction} objects returned from {@code op} are applied
     * to the return values of the two operands, if any.
     *
     * {@code p.infixn(op)} is equivalent to {@code p (op p)?} in EBNF.
     *
     * @param op the infix operator parser returning a binary function
     * @return a parser that optionally applies a non-associative infix operator
     */
    public final Parser<T> infixn(Parser<? extends BiFunction<? super T, ? super T, ? extends T>> op) {
        return next(a -> {
            Parser<T> shift = Parsers.sequence(op, this, (m2, b) -> m2.apply(a, b));
            return shift.or(Parsers.constant(() -> a));
        }).map(v -> v);
    }

    /**
     * A {@link Parser} for left-associative infix operator.
     * Runs {@code this} for the left operand, and then runs {@code operator}
     * and {@code this} for the operator and the right operand for 0 or more times greedily.
     * The {@link BiFunction} objects returned from {@code operator} are applied
     * from left to right to the return values of {@code this}, if any.
     *
     * For example:
     * {@code a + b + c + d} is evaluated as {@code (((a + b)+c)+d)}.
     *
     * {@code p.infixl(op)} is equivalent to {@code p (op p)*} in EBNF.
     *
     * @param operator the left-associative infix operator parser returning a binary function
     * @return a parser that applies zero or more left-associative infix operators
     */
    public final Parser<T> infixl(Parser<? extends BiFunction<? super T, ? super T, ? extends T>> operator) {
        final AtomicReference<ParseContext> ctxt = new AtomicReference<>();
        final Map2<BiFunction<? super T, ? super T, ? extends T>, T, Function<? super T, ? extends T>> rightToLeft =
            (op, r) -> l -> {
                final T infix = op.apply(l, r);
                if (l instanceof SourceLocalisable left
                        && left.hasSourceLocation()
                        && r instanceof SourceLocalisable right
                        && right.hasSourceLocation()) {
                    ctxt.get().mapResult(infix,
                                         left.getSourceLocation().start(),
                                         right.getSourceLocation().end());
                }
                return infix;
            };
        return next(first -> Parsers.context(c -> ctxt.set(c))
                                    .next(Parsers.sequence(operator, this, rightToLeft)
        		                                 .many()
                                                 .map(maps -> applyInfixOperators(first, maps))))
        		 .map(v -> v);
    }

    /**
     * A {@link Parser} for right-associative infix operator.
     * Runs {@code this} for the left operand, and then runs {@code op}
     * and {@code this} for the operator and the right operand for 0 or more times greedily.
     * The {@link BiFunction} objects returned from {@code op} are applied from right
     * to left to the return values of {@code this}, if any.
     *
     * For example:
     * {@code a + b + c + d} is evaluated as {@code a + (b + (c + d))}.
     *
     * {@code p.infixr(op)} is equivalent to {@code p (op p)*} in EBNF.
     *
     * @param operator the right-associative infix operator parser returning a binary function
     * @return a parser that applies zero or more right-associative infix operators
     */
    public final Parser<T> infixr(Parser<? extends BiFunction<? super T, ? super T, ? extends T>> operator) {
        final AtomicReference<ParseContext> ctxt = new AtomicReference<>();
        final Parser<Rhs<T>> rhs = Parsers.sequence(operator, this, Rhs<T>::new);
        return Parsers.sequence(Parsers.context(c -> ctxt.set(c)).next(this),
                                rhs.many(),
                                (f, r) -> applyInfixrOperators(f, r, ctxt.get()))
                      .map(v -> v);
    }

    /**
     * A {@link Parser} that runs {@code this} and wraps the return value in a {@link Token}.
     *
     * <p>
     * It is normally not necessary to call this method explicitly.
     * {@link #lexer(Parser)} and {@link #from(Parser, Parser)}
     * both do the conversion automatically.
     *
     * @return a parser that wraps its result in a {@link Token}
     */
    public final Parser<Token> token() {
        return new Parser<Token>() {
            @Override
            boolean apply(ParseContext ctxt) {
                int begin = ctxt.getIndex();
                int b = ctxt.at;
                if (!Parser.this.apply(ctxt)) {
                    return false;
                }
                int len = ctxt.getIndex() - begin;
                Token token = new Token(begin, len, ctxt.getResult().geValue(), ctxt.getSourceLocator());
                ctxt.setResult(token, b, ctxt.at);
                return true;
            }

            @Override
            public String toString() {
                return Parser.this.toString();
            }
        };
    }

    /**
     * A {@link Parser} that returns the matched string in the original source.
     *
     * @return a parser that returns the raw source text matched by {@code this}
     */
    public final Parser<String> source() {
        return new Parser<String>() {
            @Override
            boolean apply(ParseContext ctxt) {
                int begin = ctxt.getIndex();
                int b = ctxt.at;
                if (!Parser.this.apply(ctxt)) {
                    return false;
                }
                ctxt.setResult(ctxt.source.subSequence(begin, ctxt.getIndex()).toString(),
                               b,
                               ctxt.at);
                return true;
            }

            @Override
            public String toString() {
                return "source";
            }
        };
    }

    /**
     * A {@link Parser} that returns both the parsed object and the matched source string.
     *
     * @return a parser that returns its result paired with the matched source text
     */
    public final Parser<WithSource<T>> withSource() {
        return new Parser<WithSource<T>>() {
            @Override
            boolean apply(ParseContext ctxt) {
                int begin = ctxt.getIndex();
                int b = ctxt.at;
                if (!Parser.this.apply(ctxt)) {
                    return false;
                }
                String source = ctxt.source.subSequence(begin, ctxt.getIndex()).toString();
                @SuppressWarnings("unchecked")
                WithSource<T> withSource = new WithSource<T>((T) ctxt.getResult().geValue(), source);
                ctxt.setResult(withSource, b, ctxt.at);
                return true;
            }

            @Override
            public String toString() {
                return Parser.this.toString();
            }
        };
    }

    /**
     * A {@link Parser} that takes as input the {@link Token} collection returned by {@code lexer},
     * and runs {@code this} to parse the tokens.
     * Most parsers should use the simpler {@link #from(Parser, Parser)} instead.
     * {@code this} must be a token level parser.
     *
     * @param lexer a parser that produces the token collection
     * @return a parser that runs {@code this} on the tokens produced by {@code lexer}
     */
    protected final Parser<T> from(Parser<? extends Collection<Token>> lexer) {
        return _from(lexer, null);
    }

    /**
     * A {@link Parser} that takes as input the {@link Token} collection returned by {@code lexer},
     * and runs {@code this} to parse the tokens using the given {@code config}.
     *
     * @param lexer  a parser that produces the token collection from character-level input
     * @param config optional API parser configuration to apply during token-level parsing
     * @return a parser that runs {@code this} on the tokens produced by {@code lexer}
     */
    protected final Parser<T> from(Parser<? extends Collection<Token>> lexer, ApiParser.Config config) {
        return _from(lexer, config);
    }

    private final Parser<T> _from(Parser<? extends Collection<Token>> lexer, ApiParser.Config config) {
        return Parsers.nested(Parsers.tokens(lexer), followedBy(Parsers.EOF), config);
    }

    /**
     * A {@link Parser} that uses the provided pre-built token list as input
     * and runs {@code this} to parse those tokens using the given {@code config}.
     *
     * @param tokens a pre-built list of tokens to parse
     * @param config optional API parser configuration to apply during token-level parsing
     * @return a parser that runs {@code this} on the provided token list
     */
    protected final Parser<T> from(List<Token> tokens, ApiParser.Config config) {
        Parser<Token[]> tokenParser = Parsers.constant(() -> tokens.toArray(Token[]::new));
        return Parsers.nested(tokenParser, followedBy(Parsers.always()), config);
    }

    /**
     * A {@link Parser} that takes as input the tokens returned by {@code tokenizer} delimited by {@code delim},
     * and runs {@code this} to parse the tokens.
     * A common misunderstanding is that {@code tokenizer} has to be a parser of {@link Token}.
     * It doesn't need to be because {@code Terminals} already takes care of wrapping your logical token objects
     * into physical {@code Token} with correct source location information tacked on for free.
     * Your token object can literally be anything, as long as your token level parser can recognize it later.
     *
     * <p>
     * The following example uses {@code Terminals.tokenizer()}:
     *
     * <pre>
     * Terminals terminals = ...;
     * return parser.from(terminals.tokenizer(), Scanners.WHITESPACES.optional()).parse(str);
     * </pre>
     *
     * And tokens are optionally delimited by whitespaces.
     * <p>
     * Optionally, you can skip comments using an alternative scanner than {@code WHITESPACES}:
     *
     * <pre>{@code
     *   Terminals terminals = ...;
     *   Parser<?> delim = Parsers.or(
     *       Scanners.WHITESPACE,
     *       Scanners.JAVA_LINE_COMMENT,
     *       Scanners.JAVA_BLOCK_COMMENT).skipMany();
     *   return parser.from(terminals.tokenizer(), delim).parse(str);
     * }</pre>
     *
     * <p>
     * In both examples, it's important to make sure the delimiter scanner can accept empty string
     * (either through {@link #optional} or {@link #skipMany}),
     * unless adjacent operator characters shouldn't be parsed as separate operators.
     * i.e. "((" as two left parenthesis operators.
     *
     * {@code this} must be a token level parser.
     *
     * @param tokenizer a parser that recognizes individual tokens in the character stream
     * @param delim     a parser that recognizes the delimiter between tokens
     * @return a parser that tokenizes input with {@code tokenizer}/{@code delim} then runs {@code this}
     */
    protected final Parser<T> from(Parser<?> tokenizer,
                                   Parser<Void> delim) {
        return from(tokenizer, delim, null);
    }

    /**
     * A {@link Parser} that takes as input the tokens produced by {@code tokenizer} delimited by
     * {@code delim}, and runs {@code this} to parse the tokens using the given {@code config}.
     *
     * @param tokenizer a parser that recognizes individual tokens in the character stream
     * @param delim     a parser that recognizes the delimiter between tokens (e.g. whitespace)
     * @param config    optional API parser configuration to apply during token-level parsing
     * @return a parser that tokenizes input then runs {@code this} on the resulting tokens
     */
    protected final Parser<T> from(Parser<?> tokenizer,
                                   Parser<Void> delim,
                                   ApiParser.Config config) {
        return from(tokenizer.lexer(delim), config);
    }

    /**
     * A {@link Parser} that greedily runs {@code this} repeatedly,
     * and ignores the pattern recognized by {@code delim} before and after each occurrence.
     * The result tokens are wrapped in {@link Token} and are collected
     * and returned in a {@link List}.
     *
     * <p>
     * It is normally not necessary to call this method explicitly.
     * {@link #from(Parser, Parser)} is more convenient for simple uses
     * that just need to connect a token level parser with a lexer that produces the tokens.
     * When more flexible control over the token list is needed, for example,
     * to parse indentation sensitive language, a pre-processor of the token list may be needed.
     *
     * {@code this} must be a tokenizer that returns a token value.
     *
     * @param delim a parser that recognizes ignored delimiters between tokens
     * @return a parser that greedily tokenizes input and returns the resulting token list
     */
    protected Parser<List<Token>> lexer(Parser<?> delim) {
        return delim.optional(null).next(token().sepEndBy(delim));
    }

    /**
     * As a delimiter, the parser's error is considered lenient and will only be reported
     * if no other meaningful error is encountered.
     * The delimiter's logical step is also considered 0,
     * which means it won't ever stop repetition combinators such as {@link #many}.
     */
    final Parser<T> asDelimiter() {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                return ctxt.applyAsDelimiter(Parser.this);
            }

            @Override
            public String toString() {
                return Parser.this.toString();
            }
        };
    }

    /**
     * Parses {@code source} and returns the parsed result.
     *
     * @param source the character sequence to parse
     * @return the parsed value
     */
    protected final T parse(CharSequence source) {
        return parse(source, Mode.PRODUCTION);
    }

    /**
     * Parses source read from {@code readable} and returns the parsed result.
     *
     * @param readable the source to read from
     * @return the parsed value
     * @throws IOException if an I/O error occurs while reading from {@code readable}
     */
    protected final T parse(Readable readable) throws IOException {
        return parse(read(readable));
    }

    /**
     * Parses the given pre-built token list using {@code this} token-level parser.
     *
     * @param tokens the list of tokens to parse
     * @return the parsed value of type {@code T}
     */
    protected final T parse(List<Token> tokens) {
        return ScannerState.newScannerState(tokens).run(this);
    }

    /**
     * Parses {@code source} under the given {@code mode}.
     *
     * For example:
     * <pre>
     *   try {
     *     parser.parse(text, Mode.DEBUG);
     *   }
     *   catch (ParserException e) {
     *     ParseTree parseTree = e.getParseTree();
     *     ...
     *   }
     * </pre>
     *
     * @param source the character sequence to parse
     * @param mode   the parsing mode (e.g. production or debug)
     * @return the parsed value
     */
    protected final T parse(CharSequence source, Mode mode) {
        return mode.run(this, source);
    }

    /**
     * Parses {@code source} and returns a {@link ParseTree} corresponding
     * to the syntactical structure of the input.
     * Only {@link #label labeled} parser nodes are represented in the parse tree.
     *
     * <p>
     * If parsing failed, {@link ParserException#getParseTree()} can be inspected
     * for the parse tree at error location.
     *
     * @param source the character sequence to parse
     * @return the parse tree built from the matched labeled parsers
     */
    protected final ParseTree parseTree(CharSequence source) {
        ScannerState state = new ScannerState(source);
        state.enableTrace("root");
        state.run(this.followedBy(Parsers.EOF));
        return state.buildParseTree();
    }

    /**
     * Parses the given pre-built token list and returns a {@link ParseTree} corresponding
     * to the syntactical structure of the tokens.
     * Only {@link #label labeled} parser nodes are represented in the parse tree.
     *
     * @param tokens the list of tokens to parse
     * @return the parse tree built from the matched labeled parsers
     */
    protected final ParseTree parseTree(List<Token> tokens) {
        ScannerState state = ScannerState.newScannerState(tokens);
        state.enableTrace("root");
        state.run(this);
        return state.buildParseTree();
    }

    /**
     * Defines the mode that a parser should be run in.
     */
    protected enum Mode {
        /** Default mode.
         * Used for production.
         */
        PRODUCTION {
            @Override
            <T> T run(Parser<T> parser, CharSequence source) {
                return new ScannerState(source).run(parser.followedBy(Parsers.EOF));
            }
        },

        /**
         * Debug mode.
         * {@link ParserException#getParseTree} can be used to inspect partial parse result.
         */
        DEBUG {
            @Override
            <T> T run(Parser<T> parser, CharSequence source) {
                ScannerState state = new ScannerState(source);
                state.enableTrace("root");
                T result = state.run(parser.followedBy(Parsers.EOF));
                ParseTree parseTree = state.getTrace().getCurrentNode().toParseTree();
                LOGGER.info("### [Mode.DEBUG] value ###");
                parseTree.walk(h -> {
                    LOGGER.info("{}{}[start:{}, end:{}] -> {}",
                                Strings.repeat(" ", h.deep() * 2),
                                h.parseTree().getName(),
                                h.parseTree().getBeginIndex(),
                                h.parseTree().getEndIndex(),
                                h.parseTree().getValue());
                });
                LOGGER.info("### [Mode.DEBUG] source ###");
                parseTree.walk(h -> {
                    LOGGER.info("{}{}[start:{}, end:{}] -> {}",
                                Strings.repeat(" ", h.deep() * 2),
                                h.parseTree().getName(),
                                h.parseTree().getBeginIndex(),
                                h.parseTree().getEndIndex(),
                                StringUtil.replaceLineSeparatorBySpace(source.subSequence(h.parseTree().getBeginIndex(),
                                                                                          h.parseTree().getEndIndex())));
                });
                return result;
            }
        };

        abstract <T> T run(Parser<T> parser, CharSequence source);
    }

    abstract boolean apply(ParseContext ctxt);

    /**
     * Copies all content from {@code from} to {@code to}.
     */
    @Private
    static StringBuilder read(Readable from) throws IOException {
        StringBuilder builder = new StringBuilder();
        CharBuffer buf = CharBuffer.allocate(2048);
        for (;;) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            buf.flip();
            builder.append(buf, 0, r);
        }
        return builder;
    }

    @SuppressWarnings("unchecked")
    final T getReturn(ParseContext ctxt) {
        return (T) ctxt.getResult().geValue();
    }

    private static <T> T applyPrefixOperators(ParseContext ctxt,
                                              List<? extends Function<? super T, ? extends T>> ms,
                                              T a) {
        for (int i = ms.size() - 1; i >= 0; i--) {
            Function<? super T, ? extends T> m = ms.get(i);
            T _a = a;
            a = m.apply(a);
            if (m instanceof MapOperator<?, ?, ?> mapOperator
                    && mapOperator.operator() instanceof SourceLocalisable operator
                    && operator.hasSourceLocation()
                    && _a instanceof SourceLocalisable right
                    && right.hasSourceLocation()) {
                ctxt.mapResult(a,
                               operator.getSourceLocation().start(),
                               right.getSourceLocation().end());
            }
        }
        return a;
    }

    private static <T> T applyPostfixOperators(ParseContext ctxt,
                                               T a,
                                               Iterable<? extends Function<? super T, ? extends T>> ms) {
        for (Function<? super T, ? extends T> m : ms) {
            T _a = a;
            a = m.apply(a);
            if (m instanceof MapOperator<?, ?, ?> mapOperator
                    && mapOperator.operator() instanceof SourceLocalisable operator
                    && operator.hasSourceLocation()
                    && _a instanceof SourceLocalisable left
                    && left.hasSourceLocation()) {
                ctxt.mapResult(a,
                               left.getSourceLocation().start(),
                               operator.getSourceLocation().end());
            }
        }
        return a;
    }

    private static <T> T applyInfixOperators(T initialValue,
                                             List<? extends Function<? super T, ? extends T>> functions) {
        T result = initialValue;
        for (Function<? super T, ? extends T> function : functions) {
            result = function.apply(result);
        }
        return result;
    }

    // 1+ 1+ 1+ ..... 1
    private static final class Rhs<T> {

        final BiFunction<? super T, ? super T, ? extends T> op;
        final T rhs;

        Rhs(BiFunction<? super T, ? super T, ? extends T> op, T rhs) {
            this.op = op;
            this.rhs = rhs;
        }

        @Override
        public String toString() {
            return op + " " + rhs;
        }

    }

    private static <T> T applyInfixrOperators(T first,
                                              List<Rhs<T>> rhss,
                                              ParseContext ctxt) {
        if (rhss.isEmpty()) {
            return first;
        }
        int lastIndex = rhss.size() - 1;
        T o2 = rhss.get(lastIndex).rhs;
        for (int i = lastIndex; i > 0; i--) {
            T o1 = rhss.get(i - 1).rhs;
            o2 = rhss.get(i).op.apply(o1, o2);
            if (rhss.get(i).op instanceof MapInfix<?, ?, ?, ?> mapInfix) {
                if (mapInfix.left() instanceof SourceLocalisable left
                        && left.hasSourceLocation()
                        && mapInfix.right() instanceof SourceLocalisable right
                        && right.hasSourceLocation()) {
                    ctxt.mapResult(mapInfix.infix(),
                                   left.getSourceLocation().start(),
                                   right.getSourceLocation().end());
                }
            }
        }
        T result = rhss.get(0).op.apply(first, o2);
        if (rhss.get(0).op instanceof MapInfix<?, ?, ?, ?> mapInfix) {
            if (mapInfix.left() instanceof SourceLocalisable left
                    && left.hasSourceLocation()
                    && mapInfix.right() instanceof SourceLocalisable right
                    && right.hasSourceLocation()) {
                ctxt.mapResult(mapInfix.infix(),
                               left.getSourceLocation().start(),
                               right.getSourceLocation().end());
            }
        }
        return result;
    }

}
