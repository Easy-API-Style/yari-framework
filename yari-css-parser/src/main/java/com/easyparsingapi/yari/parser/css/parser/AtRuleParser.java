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
import static com.easyparsingapi.yari.parsec.Parsers.runtime;
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.atomic;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.css.parser.ExpressionParser.function;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.token;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.word;
import static com.easyparsingapi.yari.parser.css.parser.UnitParser.block;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.easyparsingapi.yari.parsec.OperatorTable;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parser.Reference;
import com.easyparsingapi.yari.parsec.functors.MapInfix;
import com.easyparsingapi.yari.parsec.functors.MapOperator;
import com.easyparsingapi.yari.parsec.location.SourceLocator;
import com.easyparsingapi.yari.parser.css.ast.AtRule;
import com.easyparsingapi.yari.parser.css.ast.AtRuleName;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.css.ast.Function;
import com.easyparsingapi.yari.parser.css.ast.Identifier;
import com.easyparsingapi.yari.parser.css.ast.Infix;
import com.easyparsingapi.yari.parser.css.ast.ListParameter;
import com.easyparsingapi.yari.parser.css.ast.Operator;
import com.easyparsingapi.yari.parser.css.ast.Parenthesis;
import com.easyparsingapi.yari.parser.css.ast.Prefix;
import com.easyparsingapi.yari.parser.css.ast.QualifiedIdentifier;
import com.easyparsingapi.yari.parser.css.ast.Range;
import com.easyparsingapi.yari.parser.css.parser.CssConfig.Node;

/**
 * Provides parsers for CSS at-rules, including their names, parameters, and combinators.
 *
 * <p>This class builds composable {@link Parser} instances that recognise CSS at-rule
 * constructs such as {@code @media}, {@code @supports}, and {@code @import}, handling
 * infix expressions, range comparisons, qualified identifiers, and logical combinators
 * ({@code and}, {@code or}, {@code not}, {@code only}).</p>
 */
public class AtRuleParser {

    /** Not instantiable — all methods are static. */
    private AtRuleParser() {}

    /*
     *
     * OPERAND
     *
     */
    static Parser<Infix> atRuleInfix() {
        return sequence(identifier(),
                        or(token("<="),
                           token(">="),
                           token("<"),
                           token(">"))
                         .map(Operator::new),
                        identifier(),
                        Infix::new)
                 .label("atRuleInfix");
    }

    static Parser<Range> atRuleRange() {
        return sequence(identifier(),
                        or(token("<="),
                           token(">="),
                           token("<"),
                           token(">"))
                         .map(Operator::new),
                        identifier(),
                        or(token("<="),
                           token(">="),
                           token("<"),
                           token(">"))
                         .map(Operator::new),
                        identifier(),
                        Range::new)
                .label("atRuleRange");
    }

    static Parser<QualifiedIdentifier> qualifiedIdentifier(final CssConfig config) {
        final Reference<QualifiedIdentifier> reference = Parser.newReference();
        final Parser<Identifier> element = identifier();
        reference.set(sequence(element,
                               token(".").next(reference.lazy()).optional(),
                               (left, right) -> {
            final List<Identifier> identifiers = new ArrayList<>();
            identifiers.add(left);
            if (right != null) {
                identifiers.addAll(right.getIdentifiers());
            }
            return new QualifiedIdentifier(identifiers);
        })
        .acceptIf(v -> {
            final LinkedList<Identifier> identifiers = v.getIdentifiers();
            if (identifiers.size() < 2) {
                return true;
            }
            final SourceLocator sourceLocator = config.getSourceLocator();
            final int endIndex = sourceLocator.locate(identifiers.get(identifiers.size() - 2)
                                                                 .getSourceLocation()
                                                                 .end());
            final int startIndex = sourceLocator.locate(identifiers.getLast()
                                                                   .getSourceLocation()
                                                                   .start());
            return (endIndex + 1) == startIndex;
        }));
        return reference.lazy()
                        .acceptIf(v -> v.size() >= 2)
                        .label("qualifiedIdentifier");
    }

    /*
     *
     * PARAMETER
     *
     */
    private static Parser<MapInfix<Object, CssNode, CssNode, Infix>> whitespace() {
        return word("and", "or")
                .not()
                .map(operator
                        -> MapInfix.map(operator, (left, right) -> new Infix(left,
                                                                             Operator.empty(left.getSourceLocation().end()),
                                                                             /** The field. */
                                                                             right)));
    }

    private static Parser<MapInfix<CssNode, CssNode, CssNode, Infix>> operator(final String symbol) {
        return word(symbol)
                 .map(Operator::new)
                 .map(operator -> MapInfix.map(operator, (left, right) -> new Infix(left, operator, right)));
    }

    private static Parser<MapOperator<CssNode, CssNode, CssNode>> prefix(final String symbol) {
        return word(symbol)
                 .map(Operator::new)
                 .map(prefix -> MapOperator.map(prefix, operand -> new Prefix(prefix, operand)));
    }

    private static Parser<CssNode> defaultParameter(final CssConfig config) {
        final Parser.Reference<Function> function = Parser.newReference();
        function.set(function(combinatorAtRuleParameter(or(function.lazy(),
                                                           atRuleRange(),
                                                           atRuleInfix(),
                                                           RuleSetParser.property(config),
                                                           qualifiedIdentifier(config),
                                                           parseIf(c -> config.sequenceService().parenthesis(c),
                                                                   config.parser(Node.selector)
                                                                         .between(token("("), token(")"))
                                                                         .map(Parenthesis::new)),
                                                           SelectorParser.pseudoClassSelector(),
                                                           atomic()), config),
                              /** The field. */
                              config));
        return or(function.lazy(),
                  atRuleRange(),
                  atRuleInfix(),
                  RuleSetParser.property(config),
                  qualifiedIdentifier(config),
                  parseIf(c -> config.sequenceService().parenthesis(c),
                          config.parser(Node.selector)
                                .between(token("("), token(")"))
                                .map(Parenthesis::new)),
                  SelectorParser.pseudoClassSelector(),
                  atomic())
                .label("defaultParameter");
    }

    private static Parser<CssNode> atRuleParameter(final AtRuleName atRuleName,
                                                   final CssConfig config) {
        Parser<CssNode> result = config.atRuleParameter(atRuleName.getName().getValue());
        if (result == null) {
            result = defaultParameter(config);
        }
        return result;
    }

    static Parser<CssNode> combinatorAtRuleParameter(final AtRuleName atRuleName,
                                                     final CssConfig config) {
        final Parser<CssNode> operand = atRuleParameter(atRuleName, config);
        return combinatorAtRuleParameter(operand, config)
                        .label("combinatorAtRuleParameter[AtRuleName]");
    }

    static Parser<CssNode> combinatorAtRuleParameter(final Parser<CssNode> operand,
                                                     final CssConfig config) {
        final Parser.Reference<CssNode> reference = Parser.newReference();
        final Parser<CssNode> unit = or(operand,
                                        parseIf(c -> config.sequenceService().parenthesis(c),
                                                reference.lazy()
                                                         .between(token("("), token(")"))
                                                         .map(Parenthesis::new)));
        final Parser<CssNode> operatorTable = new OperatorTable<CssNode>()
                .infixl(operator("and"), 30)
                .infixl(operator("or"), 20)
                .infixl(whitespace(), 30)
                .prefix(prefix("not"), 40)
                .prefix(prefix("only"), 40)
                .buildMap(unit);
        reference.set(operatorTable);
        return reference.lazy()
                        .label("combinatorAtRuleParameter");
    }

    private static Parser<CssNode> listParameterOrParameter(final Parser<CssNode> element) {
        return element.sepBy(token(","))
                      .map(cssNodes -> {
                          /** The result. */
                          final CssNode result;
                          if (cssNodes.size() == 1) {
                              result = cssNodes.get(0);
                          }
                          else {
                              result = new ListParameter(cssNodes);
                          }
                          return result;
                      })
                      .label("listParameterOrParameter");
    }

    /*
     *
     * AT RULE
     *
     */
    private static Parser<AtRuleName> atRuleName() {
        return sequence(token("@"),
                        identifier())
                .map(AtRuleName::new)
                .label("atRuleName");
    }

    /**
     * Returns a parser that recognises a complete CSS at-rule.
     *
     * <p>An at-rule consists of an at-rule name (e.g. {@code @media}), an optional
     * parameter expression (which may itself be a comma-separated list of combinator
     * expressions), and an optional block delimited by braces.  The parameter parser
     * is resolved lazily at parse time so that custom parameter parsers registered
     * in {@code config} are honoured for each specific at-rule name.</p>
     *
     * @param config the CSS parser configuration used to resolve custom at-rule
     *               parameter parsers and parser-level services
     * @return a {@link Parser} that produces an {@link AtRule} AST node
     */
    public static Parser<AtRule> atRule(final CssConfig config) {
        final AtomicReference<AtRuleName> atRuleName = new AtomicReference<>();
        return sequence(atRuleName().result(v -> atRuleName.set(v)),
                        runtime(() -> listParameterOrParameter(combinatorAtRuleParameter(atRuleName.get(), config)).optional()),
                        block(config).optional(),
                        AtRule::new)
                  .label("atRule");
   }

}
