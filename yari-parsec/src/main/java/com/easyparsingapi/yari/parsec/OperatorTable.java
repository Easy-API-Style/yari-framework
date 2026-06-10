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

import java.util.Collections;
import java.util.List;

import com.easyparsingapi.yari.parsec.functors.MapInfix;
import com.easyparsingapi.yari.parsec.functors.MapOperator;
import com.easyparsingapi.yari.parsec.internal.annotations.Private;
import com.easyparsingapi.yari.parsec.internal.util.Lists;

/**
 * Builds {@link Parser} to parse expressions with operator-precedence grammar.
 * The operators and precedences are declared in this table.
 *
 * @param <T> the type of the expression nodes produced by the parser
 *
 * <p>Operators have precedences. The higher the precedence number, the higher the precedence.
 * For the same precedence, prefix &gt; postfix &gt; left-associative &gt; non-associative &gt; right-associative.
 *
 * <p>For example:
 * {@code
 *   Unary<Integer> negate = new Unary<Integer>() {... return -n; };
 *   Binary<Integer> plus = new Binary<Integer>() {... return a + b; };
 *   Binary<Integer> minus = new Binary<Integer>() {... return a - b; };
 *   ...
 *   Terminals terms = Terminals.operators("+", "-", "*", "/");
 *   Parser<Integer> calculator = new OperatorTable()
 *       .prefix(terms.token("-").retn(negate), 100)
 *       .infixl(terms.token("+").retn(plus), 10)
 *       .infixl(terms.token("-").retn(minus), 10)
 *       .infixl(terms.token("*").retn(multiply), 20)
 *       .infixl(terms.token("/").retn(divide), 20)
 *       .build(Terminals.IntegerLiteral.PARSER.map(stringToInteger));
 *   Parser<Integer> parser = calculator.from(
 *       terms.tokenizer().or(Terminals.IntegerLiteral.TOKENIZER), Scanners.WHITESPACES.optional());
 *   return parser.parse(text);
 * }
 */
public final class OperatorTable<T> {

    /** Creates an empty {@code OperatorTable}. */
    public OperatorTable() {}

    /** Describes operator associativity, in order of precedence. */
    enum Associativity {
        PREFIX, POSTFIX, LASSOC, NASSOC, RASSOC
    }

    private final List<Operator> operators = Lists.arrayList();

    static final class Operator implements Comparable<Operator> {

        final Parser<?> operator;
        final int precedence;
        final Associativity associativity;

        Operator(Parser<?> operator,
                 int precedence,
                 Associativity associativity) {
            this.operator = operator;
            this.precedence = precedence;
            this.associativity = associativity;
        }

        /** Higher precedence first. For tie, compares associativity. */
        @Override
        public int compareTo(Operator that) {
            if (precedence > that.precedence) {
                return -1;
            }
            if (precedence < that.precedence) {
                return 1;
            }
            return associativity.compareTo(that.associativity);
        }

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append(Operator.class.getSimpleName());
			result.append(" [operator=");
			result.append(operator);
			result.append(", precedence=");
			result.append(precedence);
			result.append(", associativity=");
			result.append(associativity);
			result.append("]");
			return result.toString();
		}

    }

    /**
     * Adds a prefix unary operator.
     *
     * @param parser     the parser for the operator.
     * @param precedence the precedence number.
     * @return this.
     */
    public OperatorTable<T> prefix(Parser<? extends MapOperator<? super T, ? super T, ? extends T>> parser,
                                    int precedence) {
        operators.add(new Operator(parser, precedence, Associativity.PREFIX));
        return this;
    }

    /**
     * Adds a postfix unary operator.
     *
     * @param parser     the parser for the operator.
     * @param precedence the precedence number.
     * @return this.
     */
    public OperatorTable<T> postfix(Parser<? extends MapOperator<? super T, ? super T, ? extends T>> parser,
                                    int precedence) {
        operators.add(new Operator(parser, precedence, Associativity.POSTFIX));
        return this;
    }

    /**
     * Adds an infix left-associative binary operator.
     *
     * @param parser     the parser for the operator.
     * @param precedence the precedence number.
     * @return this.
     */
    public OperatorTable<T> infixl(Parser<? extends MapInfix<? super T, ? super T, ? super T, ? extends T>> parser,
                                   int precedence) {
        operators.add(new Operator(parser, precedence, Associativity.LASSOC));
        return this;
    }

    /**
     * Adds an infix right-associative binary operator.
     *
     * @param parser     the parser for the operator.
     * @param precedence the precedence number.
     * @return this.
     */
    public OperatorTable<T> infixr(Parser<? extends MapInfix<? super T, ? super T, ? super T, ? extends T>> parser,
                                   int precedence) {
        operators.add(new Operator(parser, precedence, Associativity.RASSOC));
        return this;
    }

    /**
     * Adds an infix non-associative binary operator.
     *
     * @param parser     the parser for the operator.
     * @param precedence the precedence number.
     * @return this.
     */
    public OperatorTable<T> infixn(Parser<? extends MapInfix<? super T, ? super T, ? super T, ? extends T>> parser,
                                   int precedence) {
        operators.add(new Operator(parser, precedence, Associativity.NASSOC));
        return this;
    }

    /**
     * Builds a {@link Parser} based on information in this {@link OperatorTable}.
     *
     * @param operand parser for the operands.
     * @return the expression parser.
     */
    Parser<T> build(Parser<? extends T> operand) {
        return buildExpressionParser(operand, operators());
    }

    /**
     * Builds a {@link Parser} based on information in this {@link OperatorTable} and applies an
     * identity mapping to the result, ensuring the returned parser carries the correct type.
     *
     * @param operand parser for the operands.
     * @return the expression parser with an identity map applied to its output.
     */
    public Parser<T> buildMap(Parser<? extends T> operand) {
        return buildExpressionParser(operand, operators()).map(v -> v);
    }

    @Private
    Operator[] operators() {
        Collections.sort(operators);
        return operators.toArray(new Operator[operators.size()]);
    }

    /**
     * Builds a {@link Parser} based on information described by {@link OperatorTable}.
     *
     * @param term parser for the terminals.
     * @param ops  the operators.
     * @return the expression parser.
     */
    static <T> Parser<T> buildExpressionParser(final Parser<? extends T> term,
                                               final Operator... ops) {
        if (ops.length == 0) {
            return term.cast();
        }
        int begin = 0;
        int precedence = ops[0].precedence;
        Associativity associativity = ops[0].associativity;
        int end = 0;
        Parser<T> result = term.cast();
        for (int i = 1; i < ops.length; i++) {
            Operator operator = ops[i];
            end = i;
            if (operator.precedence == precedence
                    && operator.associativity == associativity) {
                continue;
            }
            end = i;
            Parser<?> p = slice(ops, begin, end);
            result = build(p, associativity, result);
            begin = i;
            precedence = ops[i].precedence;
            associativity = ops[i].associativity;
        }
        if (end != ops.length) {
            end = ops.length;
            associativity = ops[begin].associativity;
            Parser<?> p = slice(ops, begin, end);
            result = build(p, associativity, result);
        }
        return result;
    }

    private static Parser<?> slice(Operator[] ops,
                                   int begin,
                                   int end) {
        Parser<?>[] result = new Parser<?>[end - begin];
        for (int i = 0; i < result.length; i++) {
            result[i] = ops[i + begin].operator;
        }
        return Parsers.or(result);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <T> Parser<T> build(Parser operator,
                                       Associativity associativity,
                                       Parser<T> operand) {
        switch (associativity) {
        case PREFIX:
            return operand.prefix(operator);
        case POSTFIX:
            return operand.postfix(operator);
        case LASSOC:
            return operand.infixl(operator);
        case RASSOC:
            return operand.infixr(operator);
        case NASSOC:
            return operand.infixn(operator);
        default:
            throw new AssertionError();
        }
    }

}
