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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.easyparsingapi.yari.parsec.ParseContext.Result;
import com.easyparsingapi.yari.parsec.functors.Map2;
import com.easyparsingapi.yari.parsec.functors.Map3;
import com.easyparsingapi.yari.parsec.functors.Map4;
import com.easyparsingapi.yari.parsec.functors.Map5;
import com.easyparsingapi.yari.parsec.functors.Map6;
import com.easyparsingapi.yari.parsec.functors.Map7;
import com.easyparsingapi.yari.parsec.functors.Map8;
import com.easyparsingapi.yari.parsec.internal.annotations.Private;
import com.easyparsingapi.yari.parsec.internal.util.Lists;
import com.easyparsingapi.yari.parsec.location.SourceLocator;
import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;

/**
 * Provides common {@link Parser} implementations.
 */
public class Parsers {

    private Parsers() {
    }

    /** {@link Parser} that succeeds only if EOF is met. Fails otherwise. */
    public static final Parser<?> EOF = eof("EOF");

    /**
     * A {@link Parser} that consumes a token. The token value is returned from the parser.
     */
    public static final Parser<Token> ANY_TOKEN =
        token(new TokenMap<Token>() {
            @Override
            public Token map(Token token) {
                return token;
            }

            @Override
            public String toString() {
                return "any_token";
            }
        });

    /**
     * A {@link Parser} that consumes a token and returns its raw value as an {@link Object}.
     */
    public static final Parser<Object> ANY_TOKEN_VALUE = token(new TokenMap<Object>() {
        @Override
        public Object map(Token token) {
            return token.value();
        }

        @Override
        public String toString() {
            return "any_token_value";
        }
    });

    /**
     * A {@link Parser} that returns the current location in the source.
     *
     * <p>
     * Because {@link com.easyparsingapi.yari.parsec.location.SourceLocation.Position#line()} and
     * {@link com.easyparsingapi.yari.parsec.location.SourceLocation.Position#column()} take amortized {@code log(n)} time,
     * it's more efficient to avoid calling them until the entire source has been parsed successfully.
     * In other words, avoid mapping over the position's line or column until parsing is complete.
     *
     * <p>
     * The index obtained from the source location can be read at any time.
     */
    public static final Parser<Position> SOURCE_LOCATION_POSITION = new Parser<Position>() {

        @Override
        boolean apply(ParseContext ctxt) {
            ctxt.setResult(ctxt.locator.locate(ctxt.getIndex()), ctxt.at, ctxt.at);
            return true;
        }

        @Override
        public String toString() {
            return "SOURCE_LOCATION_POSITION";
        }

    };

    /**
     * A {@link Parser} that always succeeds and returns the current character index in the source.
     */
    public static final Parser<Integer> INDEX = new Parser<Integer>() {

        @Override
        boolean apply(ParseContext ctxt) {
            ctxt.setResult(ctxt.getIndex(), ctxt.at, ctxt.at);
            return true;
        }

        @Override
        public String toString() {
            return "INDEX";
        }

    };

    @SuppressWarnings("rawtypes")
    private static final Parser ALWAYS = constant(null);

    @SuppressWarnings("rawtypes")
    private static final Parser NEVER = new Parser<Object>() {
        @Override
        boolean apply(ParseContext ctxt) {
            return false;
        }

        @Override
        public String toString() {
            return "never";
        }
    };

    static final Parser<Boolean> TRUE = constant(() -> true);
    static final Parser<Boolean> FALSE = constant(() -> false);

    /**
     * A {@link Parser} that always succeeds without consuming any input.
     *
     * @param <T> the result type
     * @return a parser that always succeeds
     */
    @SuppressWarnings("unchecked")
    public static <T> Parser<T> always() {
        return ALWAYS;
    }

    /**
     * A {@link Parser} that always fails without consuming any input.
     *
     * @param <T> the result type
     * @return a parser that always fails
     */
    @SuppressWarnings("unchecked")
    public static <T> Parser<T> never() {
        return NEVER;
    }

    /**
     * A {@link Parser} that succeeds only if EOF is met. Fails with {@code message} otherwise.
     */
    static Parser<?> eof(final String message) {
        return new Parser<Object>() {
            @Override
            boolean apply(ParseContext ctxt) {
                if (ctxt.isEof()) {
                    return true;
                }
                ctxt.missing(message);
                return false;
            }

            @Override
            public String toString() {
                return message;
            }
        };
    }

    /**
     * A {@link Parser} that always fails with {@code message}.
     *
     * @param <T>     the result type
     * @param message the failure message
     * @return a parser that always fails with the given message
     */
    public static <T> Parser<T> fail(final String message) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                ctxt.fail(message);
                return false;
            }

            @Override
            public String toString() {
                return message;
            }
        };
    }

    /**
     * A {@link Parser} that always succeeds and invokes {@code runnable} as a side effect.
     *
     * @param runnable the action to run on each parse attempt
     * @return a parser that always succeeds after running the given action
     */
    public static Parser<?> runnable(final Runnable runnable) {
        return new Parser<Object>() {
            @Override
            boolean apply(ParseContext ctxt) {
                runnable.run();
                return true;
            }

            @Override
            public String toString() {
                return runnable.toString();
            }
        };
    }

    /**
     * A {@link Parser} whose underlying parser is resolved at parse time by calling {@code parser}.
     *
     * <p>
     * This is useful for building recursive grammars or deferring parser construction until the
     * parse context is available.
     *
     * @param parser a {@link Supplier} that provides the actual {@link Parser} to run.
     * @param <T>    the result type of the parser.
     * @return a new {@link Parser} that delegates to the parser supplied at runtime.
     */
    public static <T> Parser<T> runtime(final Supplier<Parser<T>> parser) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                return parser.get().apply(ctxt);
            }

            @Override
            public String toString() {
                return parser.toString();
            }
        };
    }

    static Parser<?> context(final Consumer<ParseContext> action) {
        return new Parser<Object>() {
            @Override
            boolean apply(ParseContext ctxt) {
                action.accept(ctxt);
                return true;
            }

            @Override
            public String toString() {
                return action.toString();
            }
        };
    }

    /**
     * Represents a snapshot of the character source at a given position during parsing.
     *
     * @param source the full character sequence being parsed.
     * @param index  the current position in the source.
     */
    public static record SourceContext(CharSequence source, int index) {
        @Override
        public String toString() {
            return source.subSequence(index, source.length()).toString();
        }
    }

    /**
     * Represents a snapshot of the token stream at a given position during parsing.
     *
     * @param tokens the full array of tokens produced by the lexer.
     * @param index  the current position in the token array.
     */
    public static record TokenContext(Token[] tokens, int index) {
        @Override
        public String toString() {
            return "index=" + index + "\n" + Arrays.asList(tokens).subList(index, tokens.length - 1);
        }
    }

    /**
     * Aggregates all contextual information available during a parse step, including the source
     * locator, the character source context, and the token context.
     *
     * @param sourceLocator the locator used to resolve character positions into source locations.
     * @param sourceContext the current state of the character source.
     * @param tokenContext  the current state of the token stream.
     */
    public static record ParsingContext(SourceLocator sourceLocator,
                                        SourceContext sourceContext,
                                        TokenContext tokenContext) {}

    /**
     * A {@link Parser} that only runs {@code parser} when {@code condition} evaluates to
     * {@code true} given the current {@link ParsingContext}; fails immediately otherwise.
     *
     * <p>
     * When the parse context is not a {@link ParserState} (e.g. during a character-level parse),
     * the condition is ignored and {@code parser} is always run.
     *
     * @param condition a predicate that inspects the current parsing context to decide whether to
     *                  proceed.
     * @param parser    the parser to run when the condition holds.
     * @param <T>       the result type of the parser.
     * @return a new {@link Parser} that conditionally delegates to {@code parser}.
     */
    public static final <T> Parser<T> parseIf(final Function<ParsingContext, Boolean> condition,
                                              final Parser<T> parser) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                boolean result;
                if (ctxt instanceof ParserState parserState) {
                    SourceContext sourceContext = new SourceContext(ctxt.source, ctxt.getIndex());
                    TokenContext tokenContext = new TokenContext(parserState.getTokens(), ctxt.at);
                    if (condition.apply(new ParsingContext(ctxt.getSourceLocator(), sourceContext, tokenContext))) {
                        result = parser.apply(ctxt);
                    }
                    else {
                        result = false;
                    }
                }
                else {
                    result = parser.apply(ctxt);
                }
                return result;
            }

            @Override
            public String toString() {
                return "parseIf[" + parser.toString() + "]";
            }
        };
    }

    /**
     * Converts a parser of a collection of {@link Token} to a parser of an array of
     * {@code Token}.
     */
    static Parser<Token[]> tokens(final Parser<? extends Collection<Token>> parser) {
        return parser.map(list -> list.toArray(new Token[list.size()]));
    }

    /**
     * A {@link Parser} that takes as input the array of {@link Token} returned from {@code lexer},
     * and feeds the tokens as input into {@code parser}.
     *
     * <p>
     * It fails if either {@code lexer} or {@code parser} fails.
     *
     * @param lexer  the lexer object that returns an array of Tok objects.
     * @param parser the token level parser object.
     * @return the new Parser object.
     */
    static <T> Parser<T> nested(final Parser<Token[]> lexer,
                                final Parser<? extends T> parser,
                                final ApiParser.Config config) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                if (!lexer.apply(ctxt)) {
                    return false;
                }
                Token[] tokens = lexer.getReturn(ctxt);
                if (config != null) {
                    config.onTokens(ctxt.locator, Arrays.asList(tokens));
                    final List<Token> filterTokens = config.filter(Arrays.asList(tokens));
                    tokens = filterTokens.toArray(new Token[0]);
                }
                int endIndex;
                if (tokens.length > 0) {
                    Token lastToken = tokens[tokens.length - 1];
                    endIndex = lastToken.index() + lastToken.length();
                }
                else {
                    endIndex = 0;
                }
                ParserState parserState = new ParserState(ctxt.source,
                                                          tokens,
                                                          0,
                                                          ctxt.locator,
                                                          endIndex,
                                                          new Result(tokens, 0, tokens.length - 1));
                parserState.setConfig(config);
                ctxt.getTrace().startFresh(parserState);
                return ctxt.applyNested(parser, parserState);
            }

            @Override
            public String toString() {
                return parser.toString();
            }
        };
    }

    /******************** monadic combinators ******************* */

    /**
     * A {@link Parser} that always succeeds and returns the value supplied by {@code value}.
     *
     * @param <T>   the result type
     * @param value a supplier providing the constant value to return
     * @return a parser that always returns the supplied value without consuming input
     */
    public static <T> Parser<T> constant(final Supplier<T> value) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                T v = value != null ? value.get() : null;
                ctxt.mapResult(v, ctxt.at, ctxt.at);
                return true;
            }
            @Override
            public String toString() {
                return  value != null ? value.get().toString() : null;
            }
        };
    }

    /**
     * A {@link Parser} that runs 2 parsers sequentially and returns the result of the last one.
     *
     * @param <T> the result type
     * @param p1  the first parser
     * @param p2  the second parser whose result is returned
     * @return a parser that runs {@code p1} then {@code p2} and returns {@code p2}'s result
     */
    public static <T> Parser<T> sequence(Parser<?> p1, Parser<T> p2) {
        return sequence(p1, p2, InternalFunctors.<Object, T>lastOfTwo());
    }

    /**
     * A {@link Parser} that runs 3 parsers sequentially and returns the result of the last one.
     *
     * @param <T> the result type
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser whose result is returned
     * @return a parser that runs all three parsers in order and returns {@code p3}'s result
     */
    public static <T> Parser<T> sequence(Parser<?> p1, Parser<?> p2, Parser<T> p3) {
        return sequence(p1, p2, p3, InternalFunctors.<Object, Object, T>lastOfThree());
    }

    /**
     * A {@link Parser} that runs 4 parsers sequentially and returns the result of the last one.
     *
     * @param <T> the result type
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser
     * @param p4  the fourth parser whose result is returned
     * @return a parser that runs all four parsers in order and returns {@code p4}'s result
     */
    public static <T> Parser<T> sequence(Parser<?> p1, Parser<?> p2, Parser<?> p3, Parser<T> p4) {
        return sequence(p1, p2, p3, p4, InternalFunctors.<Object, Object, Object, T>lastOfFour());
    }

    /**
     * A {@link Parser} that runs 5 parsers sequentially and returns the result of the last one.
     *
     * @param <T> the result type
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser
     * @param p4  the fourth parser
     * @param p5  the fifth parser whose result is returned
     * @return a parser that runs all five parsers in order and returns {@code p5}'s result
     */
    public static <T> Parser<T> sequence(Parser<?> p1, Parser<?> p2, Parser<?> p3, Parser<?> p4, Parser<T> p5) {
        return sequence(p1, p2, p3, p4, p5, InternalFunctors.<Object, Object, Object, Object, T>lastOfFive());
    }

    /**
     * A {@link Parser} that sequentially runs {@code parsers} one by one
     * and collects the return values in an array.
     *
     * @param parsers the parsers to run in sequence
     * @return a parser that returns an array of the results of all parsers
     */
    public static Parser<Object[]> array(final Parser<?>... parsers) {
        return new Parser<Object[]>() {
            @Override
            boolean apply(ParseContext ctxt) {
                int begin = ctxt.at;
                Object[] ret = new Object[parsers.length];
                for (int i = 0; i < parsers.length; i++) {
                    Parser<?> parser = parsers[i];
                    if (!parser.apply(ctxt)) {
                        return false;
                    }
                    ret[i] = parser.getReturn(ctxt);
                }
                ctxt.setResult(ret, begin, ctxt.at);
                return true;
            }

            @Override
            public String toString() {
                return "array";
            }
        };
    }

    /**
     * A {@link Parser} that sequentially runs {@code parsers} one by one
     * and collects the return values in a {@link List}.
     *
     * @param <T>     the element type
     * @param parsers the parsers to run in sequence
     * @return a parser that returns a list of the results of all parsers
     */
    public static <T> Parser<List<T>> list(Iterable<? extends Parser<? extends T>> parsers) {
        final Parser<? extends T>[] array = toArray(parsers);
        return new Parser<List<T>>() {
            @Override
            boolean apply(ParseContext ctxt) {
                int begin = ctxt.at;
                ArrayList<T> list = Lists.arrayList(array.length);
                for (Parser<? extends T> parser : array) {
                    if (!parser.apply(ctxt)) {
                        return false;
                    }
                    list.add(parser.getReturn(ctxt));
                }
                ctxt.setResult(list, begin, ctxt.at);
                return true;
            }

            @Override
            public String toString() {
                return "list";
            }
        };
    }

    /**
     * Equivalent to {@link Parser#between(Parser, Parser)}.
     * Use this to list the parsers in the natural order.
     *
     * @param <T>    the result type
     * @param before the parser that must succeed before the main parser
     * @param parser the main parser whose result is returned
     * @param after  the parser that must succeed after the main parser
     * @return a parser that matches {@code before}, {@code parser}, {@code after} in order
     */
    public static <T> Parser<T> between(Parser<?> before,
                                        Parser<T> parser,
                                        Parser<?> after) {
        return parser.between(before, after);
    }

    /**
     * A {@link Parser} that runs {@code p1} and {@code p2} sequentially
     * and transforms the return values using {@code map}.
     *
     * @param <A> the result type of the first parser
     * @param <B> the result type of the second parser
     * @param <T> the result type of the mapping function
     * @param p1  the first parser
     * @param p2  the second parser
     * @param map the function that combines the two results
     * @return a parser that runs both parsers and combines their results with {@code map}
     */
    public static <A, B, T> Parser<T> sequence(final Parser<A> p1,
                                               final Parser<B> p2,
                                               final Map2<? super A, ? super B, ? extends T> map) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                boolean isSequence = "sequence".equals(map.toString());
                boolean isFollowedBy = "followedBy".equals(map.toString());

                int _begin = ctxt.at;

                Result firstResult = null;
                Result secondResult = null;

                // first
                boolean r1 = p1.apply(ctxt);
                if (!r1) {
                    return false;
                }
                firstResult = ctxt.getResult();
                A o1 = p1.getReturn(ctxt);

                // second
                boolean r2 = p2.apply(ctxt);
                if (!r2) {
                    return false;
                }
                secondResult = ctxt.getResult();
                B o2 = p2.getReturn(ctxt);

                int _end = ctxt.at;

                Integer begin;
                Integer end;
                if (isSequence) {
                    begin = secondResult.start();
                    end = secondResult.end();
                }
                else if (isFollowedBy) {
                    begin = firstResult.start();
                    end = firstResult.end();
                }
                else {
                    begin = _begin;
                    end = _end;
                }
                // map
                ctxt.mapResult(map.map(o1, o2), begin, end);
                return true;
            }

            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs 3 parsers sequentially and combines their results using {@code map}.
     *
     * @param <A> the result type of the first parser
     * @param <B> the result type of the second parser
     * @param <C> the result type of the third parser
     * @param <T> the result type of the mapping function
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser
     * @param map the function that combines the three results
     * @return a parser that runs all three parsers and combines their results with {@code map}
     */
    public static <A, B, C, T> Parser<T> sequence(final Parser<A> p1,
                                                  final Parser<B> p2,
                                                  final Parser<C> p3,
                                                  final Map3<? super A, ? super B, ? super C, ? extends T> map) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                boolean isSequence = "sequence".equals(map.toString());

                int begin = ctxt.at;
                boolean r1 = p1.apply(ctxt);
                if (!r1) {
                    return false;
                }
                A o1 = p1.getReturn(ctxt);
                boolean r2 = p2.apply(ctxt);
                if (!r2) {
                    return false;
                }
                B o2 = p2.getReturn(ctxt);
                if (isSequence) {
                    begin = ctxt.at;
                }
                boolean r3 = p3.apply(ctxt);
                if (!r3) {
                    return false;
                }
                C o3 = p3.getReturn(ctxt);
                int end = ctxt.at;
                ctxt.mapResult(map.map(o1, o2, o3), begin, end);
                return true;
            }

            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs 4 parsers sequentially and combines their results using {@code map}.
     *
     * @param <A> the result type of the first parser
     * @param <B> the result type of the second parser
     * @param <C> the result type of the third parser
     * @param <D> the result type of the fourth parser
     * @param <T> the result type of the mapping function
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser
     * @param p4  the fourth parser
     * @param map the function that combines the four results
     * @return a parser that runs all four parsers and combines their results with {@code map}
     */
    public static <A, B, C, D, T> Parser<T> sequence(final Parser<A> p1,
                                                     final Parser<B> p2,
                                                     final Parser<C> p3,
                                                     final Parser<D> p4,
                                                     final Map4<? super A, ? super B, ? super C, ? super D, ? extends T> map) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                boolean isSequence = "sequence".equals(map.toString());

                Integer begin = ctxt.at;
                boolean r1 = p1.apply(ctxt);
                if (!r1) {
                    return false;
                }
                A o1 = p1.getReturn(ctxt);
                boolean r2 = p2.apply(ctxt);
                if (!r2) {
                    return false;
                }
                B o2 = p2.getReturn(ctxt);
                boolean r3 = p3.apply(ctxt);
                if (!r3) {
                    return false;
                }
                C o3 = p3.getReturn(ctxt);
                if (isSequence) {
                    begin = ctxt.at;
                }
                boolean r4 = p4.apply(ctxt);
                if (!r4) {
                    return false;
                }
                D o4 = p4.getReturn(ctxt);
                int end = ctxt.at;
                ctxt.mapResult(map.map(o1, o2, o3, o4), begin, end);
                return true;
            }

            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs 5 parsers sequentially and combines their results using {@code map}.
     *
     * @param <A> the result type of the first parser
     * @param <B> the result type of the second parser
     * @param <C> the result type of the third parser
     * @param <D> the result type of the fourth parser
     * @param <E> the result type of the fifth parser
     * @param <T> the result type of the mapping function
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser
     * @param p4  the fourth parser
     * @param p5  the fifth parser
     * @param map the function that combines the five results
     * @return a parser that runs all five parsers and combines their results with {@code map}
     */
    public static <A, B, C, D, E, T> Parser<T> sequence(final Parser<A> p1,
                                                        final Parser<B> p2,
                                                        final Parser<C> p3,
                                                        final Parser<D> p4,
                                                        final Parser<E> p5,
                                                        final Map5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends T> map) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                boolean isSequence = "sequence".equals(map.toString());

                int begin = ctxt.at;
                boolean r1 = p1.apply(ctxt);
                if (!r1) {
                    return false;
                }
                A o1 = p1.getReturn(ctxt);
                boolean r2 = p2.apply(ctxt);
                if (!r2) {
                    return false;
                }
                B o2 = p2.getReturn(ctxt);
                boolean r3 = p3.apply(ctxt);
                if (!r3) {
                    return false;
                }
                C o3 = p3.getReturn(ctxt);
                boolean r4 = p4.apply(ctxt);
                if (!r4) {
                    return false;
                }
                D o4 = p4.getReturn(ctxt);
                if (isSequence) {
                     begin = ctxt.at;
                }
                boolean r5 = p5.apply(ctxt);
                if (!r5) {
                    return false;
                }
                E o5 = p5.getReturn(ctxt);
                int end = ctxt.at;
                ctxt.mapResult(map.map(o1, o2, o3, o4, o5), begin, end);
                return true;
            }

            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs 6 parsers sequentially and combines their results using {@code map}.
     *
     * @param <A> the result type of the first parser
     * @param <B> the result type of the second parser
     * @param <C> the result type of the third parser
     * @param <D> the result type of the fourth parser
     * @param <E> the result type of the fifth parser
     * @param <F> the result type of the sixth parser
     * @param <T> the result type of the mapping function
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser
     * @param p4  the fourth parser
     * @param p5  the fifth parser
     * @param p6  the sixth parser
     * @param map the function that combines the six results
     * @return a parser that runs all six parsers and combines their results with {@code map}
     */
    public static <A, B, C, D, E, F, T> Parser<T> sequence(final Parser<A> p1,
                                                           final Parser<B> p2,
                                                           final Parser<C> p3,
                                                           final Parser<D> p4,
                                                           final Parser<E> p5,
                                                           final Parser<F> p6,
                                                           final Map6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends T> map) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                int begin = ctxt.at;
                boolean r1 = p1.apply(ctxt);
                if (!r1) {
                    return false;
                }
                A o1 = p1.getReturn(ctxt);
                boolean r2 = p2.apply(ctxt);
                if (!r2) {
                    return false;
                }
                B o2 = p2.getReturn(ctxt);
                boolean r3 = p3.apply(ctxt);
                if (!r3) {
                    return false;
                }
                C o3 = p3.getReturn(ctxt);
                boolean r4 = p4.apply(ctxt);
                if (!r4) {
                    return false;
                }
                D o4 = p4.getReturn(ctxt);
                boolean r5 = p5.apply(ctxt);
                if (!r5) {
                    return false;
                }
                E o5 = p5.getReturn(ctxt);
                boolean r6 = p6.apply(ctxt);
                if (!r6) {
                    return false;
                }
                F o6 = p6.getReturn(ctxt);
                int end = ctxt.at;
                ctxt.mapResult(map.map(o1, o2, o3, o4, o5, o6), begin, end);
                return true;
            }

            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs 7 parsers sequentially and combines their results using {@code map}.
     *
     * @param <A> the result type of the first parser
     * @param <B> the result type of the second parser
     * @param <C> the result type of the third parser
     * @param <D> the result type of the fourth parser
     * @param <E> the result type of the fifth parser
     * @param <F> the result type of the sixth parser
     * @param <G> the result type of the seventh parser
     * @param <T> the result type of the mapping function
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser
     * @param p4  the fourth parser
     * @param p5  the fifth parser
     * @param p6  the sixth parser
     * @param p7  the seventh parser
     * @param map the function that combines the seven results
     * @return a parser that runs all seven parsers and combines their results with {@code map}
     */
    public static <A, B, C, D, E, F, G, T> Parser<T> sequence(final Parser<A> p1,
                                                              final Parser<B> p2,
                                                              final Parser<C> p3,
                                                              final Parser<D> p4,
                                                              final Parser<E> p5,
                                                              final Parser<F> p6,
                                                              final Parser<G> p7,
                                                              final Map7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends T> map) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                int begin = ctxt.at;
                boolean r1 = p1.apply(ctxt);
                if (!r1) {
                    return false;
                }
                A o1 = p1.getReturn(ctxt);
                boolean r2 = p2.apply(ctxt);
                if (!r2) {
                    return false;
                }
                B o2 = p2.getReturn(ctxt);
                boolean r3 = p3.apply(ctxt);
                if (!r3) {
                    return false;
                }
                C o3 = p3.getReturn(ctxt);
                boolean r4 = p4.apply(ctxt);
                if (!r4) {
                    return false;
                }
                D o4 = p4.getReturn(ctxt);
                boolean r5 = p5.apply(ctxt);
                if (!r5) {
                    return false;
                }
                E o5 = p5.getReturn(ctxt);
                boolean r6 = p6.apply(ctxt);
                if (!r6) {
                    return false;
                }
                F o6 = p6.getReturn(ctxt);
                boolean r7 = p7.apply(ctxt);
                if (!r7) {
                    return false;
                }
                G o7 = p7.getReturn(ctxt);
                int end = ctxt.at;
                ctxt.mapResult(map.map(o1, o2, o3, o4, o5, o6, o7), begin, end);
                return true;
            }

            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs 8 parsers sequentially and combines their results using {@code map}.
     *
     * @param <A> the result type of the first parser
     * @param <B> the result type of the second parser
     * @param <C> the result type of the third parser
     * @param <D> the result type of the fourth parser
     * @param <E> the result type of the fifth parser
     * @param <F> the result type of the sixth parser
     * @param <G> the result type of the seventh parser
     * @param <H> the result type of the eighth parser
     * @param <T> the result type of the mapping function
     * @param p1  the first parser
     * @param p2  the second parser
     * @param p3  the third parser
     * @param p4  the fourth parser
     * @param p5  the fifth parser
     * @param p6  the sixth parser
     * @param p7  the seventh parser
     * @param p8  the eighth parser
     * @param map the function that combines the eight results
     * @return a parser that runs all eight parsers and combines their results with {@code map}
     */
    public static <A, B, C, D, E, F, G, H, T> Parser<T> sequence(final Parser<A> p1,
                                                                 final Parser<B> p2,
                                                                 final Parser<C> p3,
                                                                 final Parser<D> p4,
                                                                 final Parser<E> p5,
                                                                 final Parser<F> p6,
                                                                 final Parser<G> p7,
                                                                 final Parser<H> p8,
                                                                 final Map8<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? extends T> map) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                int begin = ctxt.at;
                boolean r1 = p1.apply(ctxt);
                if (!r1) {
                    return false;
                }
                A o1 = p1.getReturn(ctxt);
                boolean r2 = p2.apply(ctxt);
                if (!r2) {
                    return false;
                }
                B o2 = p2.getReturn(ctxt);
                boolean r3 = p3.apply(ctxt);
                if (!r3) {
                    return false;
                }
                C o3 = p3.getReturn(ctxt);
                boolean r4 = p4.apply(ctxt);
                if (!r4) {
                    return false;
                }
                D o4 = p4.getReturn(ctxt);
                boolean r5 = p5.apply(ctxt);
                if (!r5) {
                    return false;
                }
                E o5 = p5.getReturn(ctxt);
                boolean r6 = p6.apply(ctxt);
                if (!r6) {
                    return false;
                }
                F o6 = p6.getReturn(ctxt);
                boolean r7 = p7.apply(ctxt);
                if (!r7) {
                    return false;
                }
                G o7 = p7.getReturn(ctxt);
                boolean r8 = p8.apply(ctxt);
                if (!r8) {
                    return false;
                }
                H o8 = p8.getReturn(ctxt);
                int end = ctxt.at;
                ctxt.mapResult(map.map(o1, o2, o3, o4, o5, o6, o7, o8), begin, end);
                return true;
            }

            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * A {@link Parser} that runs {@code parsers} sequentially and discards all return values.
     *
     * @param parsers the parsers to run in sequence
     * @return a parser that runs all given parsers in order
     */
    public static Parser<Object> sequence(final Parser<?>... parsers) {
        return new Parser<Object>() {
            @Override
            boolean apply(ParseContext ctxt) {
                for (Parser<?> p : parsers) {
                    if (!p.apply(ctxt)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String toString() {
                return "sequence";
            }
        };
    }

    /**
     * A {@link Parser} that runs {@code parsers} sequentially and discards all return values.
     *
     * @param parsers the iterable of parsers to run in sequence
     * @return a parser that runs all given parsers in order
     */
    public static Parser<Object> sequence(Iterable<? extends Parser<?>> parsers) {
        return sequence(toArray(parsers));
    }

    /**
     * A {@link Parser} that tries 2 alternative parsers, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     *
     * @param <T> the result type
     * @param p1  the first alternative
     * @param p2  the second alternative
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Parser<? extends T> p1,
                                   Parser<? extends T> p2) {
        return alt(p1, p2).cast();
    }

    /**
     * A {@link Parser} that tries 3 alternative parsers, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     *
     * @param <T> the result type
     * @param p1  the first alternative
     * @param p2  the second alternative
     * @param p3  the third alternative
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Parser<? extends T> p1,
                                   Parser<? extends T> p2,
                                   Parser<? extends T> p3) {
        return alt(p1, p2, p3).cast();
    }

    /**
     * A {@link Parser} that tries 4 alternative parsers, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     *
     * @param <T> the result type
     * @param p1  the first alternative
     * @param p2  the second alternative
     * @param p3  the third alternative
     * @param p4  the fourth alternative
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Parser<? extends T> p1,
                                   Parser<? extends T> p2,
                                   Parser<? extends T> p3,
                                   Parser<? extends T> p4) {
        return alt(p1, p2, p3, p4).cast();
    }

    /**
     * A {@link Parser} that tries 5 alternative parsers, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     *
     * @param <T> the result type
     * @param p1  the first alternative
     * @param p2  the second alternative
     * @param p3  the third alternative
     * @param p4  the fourth alternative
     * @param p5  the fifth alternative
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Parser<? extends T> p1,
                                   Parser<? extends T> p2,
                                   Parser<? extends T> p3,
                                   Parser<? extends T> p4,
                                   Parser<? extends T> p5) {
        return alt(p1, p2, p3, p4, p5).cast();
    }

    /**
     * A {@link Parser} that tries 6 alternative parsers, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     *
     * @param <T> the result type
     * @param p1  the first alternative
     * @param p2  the second alternative
     * @param p3  the third alternative
     * @param p4  the fourth alternative
     * @param p5  the fifth alternative
     * @param p6  the sixth alternative
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Parser<? extends T> p1,
                                   Parser<? extends T> p2,
                                   Parser<? extends T> p3,
                                   Parser<? extends T> p4,
                                   Parser<? extends T> p5,
                                   Parser<? extends T> p6) {
        return alt(p1, p2, p3, p4, p5, p6).cast();
    }

    /**
     * A {@link Parser} that tries 7 alternative parsers, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     *
     * @param <T> the result type
     * @param p1  the first alternative
     * @param p2  the second alternative
     * @param p3  the third alternative
     * @param p4  the fourth alternative
     * @param p5  the fifth alternative
     * @param p6  the sixth alternative
     * @param p7  the seventh alternative
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Parser<? extends T> p1,
                                   Parser<? extends T> p2,
                                   Parser<? extends T> p3,
                                   Parser<? extends T> p4,
                                   Parser<? extends T> p5,
                                   Parser<? extends T> p6,
                                   Parser<? extends T> p7) {
        return alt(p1, p2, p3, p4, p5, p6, p7).cast();
    }

    /**
     * A {@link Parser} that tries 8 alternative parsers, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     *
     * @param <T> the result type
     * @param p1  the first alternative
     * @param p2  the second alternative
     * @param p3  the third alternative
     * @param p4  the fourth alternative
     * @param p5  the fifth alternative
     * @param p6  the sixth alternative
     * @param p7  the seventh alternative
     * @param p8  the eighth alternative
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Parser<? extends T> p1,
                                   Parser<? extends T> p2,
                                   Parser<? extends T> p3,
                                   Parser<? extends T> p4,
                                   Parser<? extends T> p5,
                                   Parser<? extends T> p6,
                                   Parser<? extends T> p7,
                                   Parser<? extends T> p8) {
        return alt(p1, p2, p3, p4, p5, p6, p7, p8).cast();
    }

    /**
     * A {@link Parser} that tries 9 alternative parsers, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     *
     * @param <T> the result type
     * @param p1  the first alternative
     * @param p2  the second alternative
     * @param p3  the third alternative
     * @param p4  the fourth alternative
     * @param p5  the fifth alternative
     * @param p6  the sixth alternative
     * @param p7  the seventh alternative
     * @param p8  the eighth alternative
     * @param p9  the ninth alternative
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Parser<? extends T> p1,
                                   Parser<? extends T> p2,
                                   Parser<? extends T> p3,
                                   Parser<? extends T> p4,
                                   Parser<? extends T> p5,
                                   Parser<? extends T> p6,
                                   Parser<? extends T> p7,
                                   Parser<? extends T> p8,
                                   Parser<? extends T> p9) {
        return alt(p1, p2, p3, p4, p5, p6, p7, p8, p9).cast();
    }

    /**
     * A {@link Parser} that tries each alternative parser in {@code alternatives}, returning the first that succeeds.
     * Fallback happens regardless of partial match.
     * Different from {@link #alt(Parser[])}, it requires all alternative parsers to have type {@code T}.
     *
     * @param <T>          the result type
     * @param alternatives the array of alternative parsers to try in order
     * @return a parser that tries each alternative in order
     */
    @SafeVarargs
    public static <T> Parser<T> or(final Parser<? extends T>... alternatives) {
        if (alternatives.length == 0) {
            return never();
        }
        if (alternatives.length == 1) {
            return alternatives[0].cast();
        }
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                final Result result = ctxt.getResult();
                final int at = ctxt.at;
                final int step = ctxt.step;
                for (Parser<? extends T> p : alternatives) {
                    if (p.apply(ctxt)) {
                        return true;
                    }
                    ctxt.set(step, at, result);
                }
                return false;
            }

            @Override
            public String toString() {
                return "or";
            }

        };
    }

    /**
     * A {@link Parser} that tries each alternative parser in {@code alternatives}, returning the first that succeeds.
     *
     * @param <T>          the result type
     * @param alternatives the iterable of alternative parsers to try in order
     * @return a parser that tries each alternative in order
     */
    public static <T> Parser<T> or(Iterable<? extends Parser<? extends T>> alternatives) {
        return or(toArray(alternatives));
    }

    /**
     * Allows the overloads of "or()" to call the varargs version of "or" with no ambiguity.
     */
    private static Parser<Object> alt(Parser<?>... alternatives) {
        return or(alternatives);
    }

    /**
     * A {@link Parser} that runs both {@code p1} and {@code p2} and selects the longer match.
     * If both match the same length, the first one is favored.
     *
     * @param <T> the result type
     * @param p1  the first parser
     * @param p2  the second parser
     * @return a parser that returns the result of whichever parser consumed more input
     */
    public static <T> Parser<T> longer(Parser<? extends T> p1,
                                       Parser<? extends T> p2) {
        return longest(p1, p2);
    }

    /**
     * A {@link Parser} that runs every element of {@code parsers} and selects the longest match.
     * If two matches have the same length, the first one is favored.
     *
     * @param <T>     the result type
     * @param parsers the array of parsers to run
     * @return a parser that returns the result of the parser that consumed the most input
     */
    @SafeVarargs
    public static <T> Parser<T> longest(Parser<? extends T>... parsers) {
        if (parsers.length == 0) {
            return never();
        }
        if (parsers.length == 1) {
            return parsers[0].cast();
        }
        return new BestParser<T>(parsers, IntOrder.GT);
    }

    /**
     * A {@link Parser} that runs every element of {@code parsers} and selects the longest match.
     * If two matches have the same length, the first one is favored.
     *
     * @param <T>     the result type
     * @param parsers the iterable of parsers to run
     * @return a parser that returns the result of the parser that consumed the most input
     */
    public static <T> Parser<T> longest(Iterable<? extends Parser<? extends T>> parsers) {
        return longest(toArray(parsers));
    }

    /**
     * A {@link Parser} that runs both {@code p1} and {@code p2} and selects the shorter match.
     * If both match the same length, the first one is favored.
     *
     * @param <T> the result type
     * @param p1  the first parser
     * @param p2  the second parser
     * @return a parser that returns the result of whichever parser consumed less input
     */
    public static <T> Parser<T> shorter(Parser<? extends T> p1,
                                        Parser<? extends T> p2) {
        return shortest(p1, p2);
    }

    /**
     * A {@link Parser} that runs every element of {@code parsers} and selects the shortest match.
     * If two matches have the same length, the first one is favored.
     *
     * @param <T>     the result type
     * @param parsers the array of parsers to run
     * @return a parser that returns the result of the parser that consumed the least input
     */
    @SafeVarargs
    public static <T> Parser<T> shortest(Parser<? extends T>... parsers) {
        if (parsers.length == 0) {
            return never();
        }
        if (parsers.length == 1) {
            return parsers[0].cast();
        }
        return new BestParser<T>(parsers, IntOrder.LT);
    }

    /**
     * A {@link Parser} that runs every element of {@code parsers} and selects the shortest match.
     * If two matches have the same length, the first one is favored.
     *
     * @param <T>     the result type
     * @param parsers the iterable of parsers to run
     * @return a parser that returns the result of the parser that consumed the least input
     */
    public static <T> Parser<T> shortest(Iterable<? extends Parser<? extends T>> parsers) {
        return shortest(toArray(parsers));
    }

    /**
     * A {@link Parser} that always fails and reports that {@code name} is logically expected.
     *
     * @param <T>  the result type
     * @param name the name of the expected input
     * @return a parser that always fails with an "expected" error message
     */
    public static <T> Parser<T> expect(final String name) {
        return new Parser<T>() {
            @Override
            boolean apply(ParseContext ctxt) {
                ctxt.expected(name);
                return false;
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    /**
     * A {@link Parser} that always fails and reports that {@code name} is logically unexpected.
     *
     * @param <T>  the result type
     * @param name the name of the unexpected input
     * @return a parser that always fails with an "unexpected" error message
     */
    public static <T> Parser<T> unexpected(final String name) {
        return new Parser<T>() {
            @Override
            boolean apply(final ParseContext ctxt) {
                ctxt.unexpected(name);
                return false;
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    /**
     * Checks the current token with the {@code fromToken} object.
     * If the {@link TokenMap#map(Token)} method returns null, an unexpected token error occurs;
     * if the method returns a non-null value, the value is returned and the parser succeeds.
     *
     * @param <T>       the result type
     * @param fromToken the {@code TokenMap} used to map the current token to a result
     * @return a parser that succeeds when the current token is recognized by {@code fromToken}
     */
    public static <T> Parser<T> token(final TokenMap<? extends T> fromToken) {
        return new Parser<T>() {

            @Override
            boolean apply(final ParseContext ctxt) {
                if (ctxt.isEof()) {
                    ctxt.missing(fromToken);
                    return false;
                }
                Token token = ctxt.getToken();
                Object v = fromToken.map(token);
                if (v == null) {
                    ctxt.missing(fromToken);
                    return false;
                }
                ctxt.setResult(v, ctxt.at, ctxt.at);
                ctxt.next();
                return true;
            }

            @Override
            public String toString() {
                return fromToken.toString();
            }

        };
    }

    /**
     * Checks whether the current token value is of {@code type}, in which case,
     * the token value is returned and parse succeeds.
     *
     * @param <T>  the result type
     * @param type the expected token value type
     * @param name the name of what is logically expected
     * @return a parser that succeeds when the current token value is an instance of {@code type}
     */
    public static <T> Parser<T> tokenType(final Class<? extends T> type,
                                          final String name) {
        return token(new TokenMap<T>() {

            @Override
            public T map(Token token) {
                if (type.isInstance(token.value())) {
                    return type.cast(token.value());
                }
                return null;
            }

            @Override
            public String toString() {
                return name;
            }

        });
    }

    @Private
    static <T> Parser<T>[] toArrayWithIteration(Iterable<? extends Parser<? extends T>> parsers) {
        ArrayList<Parser<? extends T>> list = Lists.arrayList();
        for (Parser<? extends T> parser : parsers) {
            list.add(parser);
        }
        return toArray(list);
    }

    /**
     * We always convert {@link Iterable} to an array
     * to avoid the cost of creating a new {@Link java.util.Iterator} object
     * each time the parser runs.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Private
    static <T> Parser<T>[] toArray(Iterable<? extends Parser<? extends T>> parsers) {
        if (parsers instanceof Collection<?>) {
            return toArray((Collection) parsers);
        }
        return toArrayWithIteration(parsers);
    }

    @SuppressWarnings("unchecked")
    private static <T> Parser<T>[] toArray(Collection<? extends Parser<? extends T>> parsers) {
        return parsers.toArray(new Parser[parsers.size()]);
    }

}
