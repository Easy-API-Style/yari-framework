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

import static com.easyparsingapi.yari.parsec.Parsers.or;
import static com.easyparsingapi.yari.parsec.Parsers.parseIf;
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.token;

import com.easyparsingapi.yari.core.util.TokenUtil;
import com.easyparsingapi.yari.parsec.OperatorTable;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Parsers.TokenContext;
import com.easyparsingapi.yari.parsec.functors.MapInfix;
import com.easyparsingapi.yari.parsec.functors.MapOperator;
import com.easyparsingapi.yari.parser.css.ast.CssError;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.css.ast.Function;
import com.easyparsingapi.yari.parser.css.ast.Identifier;
import com.easyparsingapi.yari.parser.css.ast.Infix;
import com.easyparsingapi.yari.parser.css.ast.Operator;
import com.easyparsingapi.yari.parser.css.ast.Parenthesis;
import com.easyparsingapi.yari.parser.css.ast.Prefix;
import com.easyparsingapi.yari.parser.css.lexer.CssTag;

/**
 * CSS expression parser handling arithmetic expressions and function calls.
 * <p>
 * This class provides composable parsers for binary (infix) operations,
 * unary (prefix) operations, parentheses, and CSS function calls,
 * relying on an operator table for precedence management.
 * </p>
 */
public class ExpressionParser {

    /** Not instantiable — all methods are static. */
    private ExpressionParser() {}


    /**
     * Builds an infix binary-operator parser for the given symbol.
     * The resulting parser maps a matched operator token into a {@link MapInfix} functor
     * that wraps the left and right operands in an {@link Infix} node.
     *
     * @param symbol the operator symbol to match (e.g. {@code "+"}, {@code "*"})
     * @return a parser producing a {@link MapInfix} functor for the binary operator
     */
    private static Parser<MapInfix<CssNode, CssNode, CssNode, Infix>> operator(final String symbol) {
        return token(symbol).map(Operator::new)
                            .map(operator -> MapInfix.map(operator, (left, right) -> new Infix(left, operator, right)));
    }

    /**
     * Builds a unary prefix-operator parser for the given symbol.
     * The resulting parser maps a matched operator token into a {@link MapOperator} functor
     * that wraps its operand in a {@link Prefix} node.
     *
     * @param symbol the operator symbol to match (e.g. {@code "-"}, {@code "+"})
     * @return a parser producing a {@link MapOperator} functor for the unary operator
     */
    private static Parser<MapOperator<CssNode, CssNode, CssNode>> prefix(final String symbol) {
        return token(symbol).map(Operator::new)
                            .map(prefix -> MapOperator.map(prefix, operand -> new Prefix(prefix, operand)));
    }

    /**
     * Builds a parser for arithmetic expressions composed of the given operand parser.
     * <p>
     * Supports the binary operators {@code +}, {@code -}, {@code |}, {@code *}, {@code /}
     * and the unary prefix operators {@code +} and {@code -}, with standard arithmetic
     * precedence. Parenthesised sub-expressions are also supported when the configuration
     * permits it.
     * </p>
     *
     * @param operand the parser for a single operand (leaf) of the arithmetic expression
     * @param config  the CSS parser configuration used to check whether parentheses are valid
     * @return a parser that produces a {@link CssNode} representing the arithmetic expression,
     *         labelled {@code "arithmetic"}
     */
    static Parser<CssNode> arithmetic(final Parser<CssNode> operand,
                                      final CssConfig config) {
        final Parser.Reference<CssNode> expressionReference = Parser.newReference();
        final Parser<CssNode> unit = or(operand,
                                        parseIf(c -> config.sequenceService().parenthesis(c),
                                                expressionReference.lazy()
                                                                   .between(token("("), token(")"))
                                                                   .map(Parenthesis::new)));
        final Parser<CssNode> operationTable = new OperatorTable<CssNode>()
                .infixl(operator("+"), 10)
                .infixl(operator("-"), 10)
                .infixl(operator("|"), 10)
                .infixl(operator("*"), 20)
                .infixl(operator("/"), 20)
                .prefix(prefix("-"), 30)
                .prefix(prefix("+"), 30)
                .buildMap(unit);
        expressionReference.set(operationTable);
        return expressionReference.lazy()
                                  .label("arithmetic");
    }

    /**
     * Builds a parser for a CSS function call, using a default identifier parser for the
     * function name and the given parser for each argument.
     *
     * @param parameter the parser for a single function argument
     * @param config    the CSS parser configuration used to verify the parenthesis sequence
     * @return a parser that produces a {@link Function} AST node, labelled {@code "function"}
     */
    static Parser<Function> function(final Parser<CssNode> parameter,
                                     final CssConfig config) {
        return function(identifier(), parameter, config);
    }

    /**
     * Builds a parser for a CSS function call using explicit parsers for both the
     * function name and each argument.
     * <p>
     * The parser only succeeds when the current token is an identifier immediately
     * followed (without whitespace) by an opening parenthesis that forms a valid
     * parenthesis sequence according to the configuration.
     * </p>
     *
     * @param name      the parser for the function name
     * @param parameter the parser for a single function argument
     * @param config    the CSS parser configuration used to verify the parenthesis sequence
     * @return a parser that produces a {@link Function} AST node, labelled {@code "function"}
     */
    static Parser<Function> function(final Parser<Identifier> name,
                                     final Parser<CssNode> parameter,
                                     final CssConfig config) {
        return parseIf(c -> {
                         boolean result = false;
                         final TokenContext tokenContext = c.tokenContext();
                         final Token[] tokens = tokenContext.tokens();
                         final int index = tokenContext.index();
                         final int nextIndex = index + 1;
                         if (nextIndex < tokens.length
                               && TokenUtil.isStuck(tokens[index], tokens[nextIndex])
                               && CssTag.WORD.equals(Token.tag(tokens[index]))
                               && "(".equals(tokens[nextIndex].toString())) {
                             result = config.sequenceService()
                                            .parenthesis(tokenContext, nextIndex);
                         }
                         return result;
                       },
                       sequence(name,
                                parameter.sepByBetween(CssError::newInstance,
                                                       token(","),
                                                       token("("),
                                                       token(")"))
                                         .map(Function.Signature::new),
                                Function::new))
                   .label("function");
    }

}
