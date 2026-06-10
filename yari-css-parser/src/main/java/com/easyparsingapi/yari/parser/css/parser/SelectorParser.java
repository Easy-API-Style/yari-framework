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
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.integer;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.nesting;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.universal;
import static com.easyparsingapi.yari.parser.css.parser.ExpressionParser.arithmetic;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.fragment;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.token;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.easyparsingapi.yari.parsec.OperatorTable;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.functors.MapInfix;
import com.easyparsingapi.yari.parsec.functors.MapOperator;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.css.ast.AttributeSelector;
import com.easyparsingapi.yari.parser.css.ast.ClassSelector;
import com.easyparsingapi.yari.parser.css.ast.CombinatorSelector;
import com.easyparsingapi.yari.parser.css.ast.CssError;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.css.ast.CssSelector;
import com.easyparsingapi.yari.parser.css.ast.ElementSelector;
import com.easyparsingapi.yari.parser.css.ast.IdSelector;
import com.easyparsingapi.yari.parser.css.ast.Identifier;
import com.easyparsingapi.yari.parser.css.ast.ListSelector;
import com.easyparsingapi.yari.parser.css.ast.NamespaceSelector;
import com.easyparsingapi.yari.parser.css.ast.NthPattern;
import com.easyparsingapi.yari.parser.css.ast.Operator;
import com.easyparsingapi.yari.parser.css.ast.PrefixSelector;
import com.easyparsingapi.yari.parser.css.ast.PseudoClassSelector;
import com.easyparsingapi.yari.parser.css.ast.PseudoElementSelector;
import com.easyparsingapi.yari.parser.css.ast.PseudoFunctionSelector;
import com.easyparsingapi.yari.parser.css.ast.QualifiedSelector;
import com.easyparsingapi.yari.parser.css.ast.AttributeSelector.Expression;
import com.easyparsingapi.yari.parser.css.lexer.CssTag;
import com.easyparsingapi.yari.parser.css.parser.CssConfig.Node;

/**
 * Provides parsers for all forms of CSS selectors, from atomic selectors
 * (element, class, id, attribute, pseudo-class, pseudo-element) up to
 * combinator expressions and comma-separated selector lists.
 */
public class SelectorParser {

    /** Not instantiable — all methods are static. */
    private SelectorParser() {}


    /*
     *
     * ATOMIC SELECTOR
     *
     */

    /**
     * Builds a parser that matches an element type selector (a bare word token).
     *
     * @return a parser producing an {@link ElementSelector}, labelled {@code "elementSelector"}
     */
    private static Parser<ElementSelector> elementSelector() {
        return fragment(CssTag.WORD)
                .map(Token::toString)
                .map(ElementSelector::new)
                .label("elementSelector");
    }

    /**
     * Builds a parser that matches a type selector: an element name, the universal
     * selector {@code *}, or the nesting selector {@code &}.
     *
     * @return a parser producing a {@link CssSelector}, labelled {@code "typeSelector"}
     */
    private static Parser<CssSelector> typeSelector() {
        return or(elementSelector(),
                  universal(),
                  nesting())
                .label("typeSelector");
    }

    /**
     * Builds a parser that matches a namespace-qualified selector of the form
     * {@code prefix|type}, where {@code prefix} is an optional identifier or
     * universal selector, and {@code type} is a type selector.
     *
     * @return a parser producing a {@link NamespaceSelector}, labelled {@code "namespaceSelector"}
     */
    private static Parser<NamespaceSelector> namespaceSelector() {
        return sequence(or(identifier(),
                           universal())
                         .optional(),
                        token("|")
                         .next(typeSelector()),
                        NamespaceSelector::new)
                .label("namespaceSelector");
    }

    /**
     * Builds a parser that matches a class selector of the form {@code .name}.
     *
     * @return a parser producing a {@link ClassSelector}, labelled {@code "classSelector"}
     */
    private static Parser<ClassSelector> classSelector() {
        return sequence(token("."),
                        identifier())
                .map(ClassSelector::new)
                .label("classSelector");
    }

    /**
     * Builds a parser that matches an ID selector of the form {@code #name}.
     *
     * @return a parser producing an {@link IdSelector}, labelled {@code "idSelector"}
     */
    private static Parser<IdSelector> idSelector() {
        return sequence(token("#"),
                        identifier())
                 .map(IdSelector::new)
                 .label("idSelector");
    }

    /**
     * Builds a parser that matches an attribute selector of the form
     * {@code [attr]}, {@code [attr=value]}, {@code [attr~=value]}, etc.,
     * including optional case-sensitivity flags ({@code s} or {@code i}).
     * The parser only succeeds when the bracket sequence is complete and valid.
     *
     * @param config the CSS parser configuration used to verify the bracket sequence
     * @return a parser producing an {@link AttributeSelector}, labelled {@code "attributeSelector"}
     */
    private static Parser<AttributeSelector> attributeSelector(final CssConfig config) {
        return  parseIf(c -> config.sequenceService().bracket(c),
                        sequence(identifier()
                                 .optional(),
                                or(token("~="),
                                   token("|="),
                                   token("^="),
                                   token("$="),
                                   token("*="),
                                   token("="))
                                 .map(Operator::new)
                                 .optional(),
                                atomic()
                                 .optional(),
                                or(identifier("s"),
                                   identifier("i"))
                                 .optional(),
                                Expression::new)
                         .<CssNode>cast()
                         .between(CssError::newInstance,
                                  token("["),
                                  token("]"))
                         .map(AttributeSelector::new))
                   .label("attributeSelector");
    }

    /**
     * Builds a parser that matches a pseudo-class selector of the form {@code :name}.
     *
     * @return a parser producing a {@link PseudoClassSelector}, labelled {@code "pseudoClassSelector"}
     */
    static Parser<PseudoClassSelector> pseudoClassSelector() {
       return sequence(token(":"),
                       identifier())
               .map(PseudoClassSelector::new)
               .label("pseudoClassSelector");
    }

    /**
     * Builds a parser that matches a pseudo-element selector of the form {@code ::name}.
     *
     * @return a parser producing a {@link PseudoElementSelector}, labelled {@code "pseudoElementSelector"}
     */
    private static Parser<PseudoElementSelector> pseudoElementSelector() {
        return sequence(token("::"),
                        identifier())
                .map(PseudoElementSelector::new)
                .label("pseudoElementSelector");
    }

    /*
     *
     * PSEUDO CLASS FUNCTION
     *
     */

    /**
     * Returns the appropriate argument parser for a pseudo-function selector based on the
     * type of the matched pseudo-selector.
     * <p>
     * If the pseudo-selector is a {@link PseudoElementSelector} or a
     * {@link PseudoClassSelector}, the configuration is consulted for a custom parser.
     * Otherwise, a parser accepting either a full selector or an arithmetic expression
     * is used as the default.
     * </p>
     *
     * @param cssSelector the pseudo-selector whose argument parser is to be resolved
     * @param config      the CSS parser configuration
     * @return a parser for the argument(s) of the pseudo-function
     */
    private static Parser<CssNode> signature(final CssSelector cssSelector,
                                             final CssConfig config) {
        Parser<CssNode> result = null;
        if (cssSelector instanceof PseudoElementSelector pseudoSelector) {
            result = config.pseudoElementSelector(pseudoSelector.getName().getValue());
        }
        else if (cssSelector instanceof PseudoClassSelector pseudoSelector) {
            result = config.pseudoClassSelector(pseudoSelector.getName().getValue());
        }
        if (result == null) {
            result = or(config.parser(Node.selector),
                        ExpressionParser.arithmetic(atomic(), config));
        }
        return result;
    }

    /**
     * Builds a parser that matches a pseudo-function selector such as
     * {@code :nth-child(...)} or {@code ::slotted(...)}.
     * <p>
     * The pseudo prefix ({@code :} or {@code ::}) is parsed first; the argument parser
     * is then resolved dynamically from the configuration based on the matched pseudo name.
     * </p>
     *
     * @param config the CSS parser configuration used to resolve the argument parser
     * @return a parser producing a {@link PseudoFunctionSelector},
     *         labelled {@code "pseudoFunctionSelector"}
     */
    private static Parser<PseudoFunctionSelector> pseudoFunctionSelector(final CssConfig config) {
        final AtomicReference<CssSelector> pseudoSelector = new AtomicReference<>();
        return sequence(or(pseudoElementSelector().result(r -> pseudoSelector.set(r)),
                           pseudoClassSelector().result(r -> pseudoSelector.set(r))),
                        parseIf(c -> config.sequenceService().parenthesis(c),
                                runtime(() -> signature(pseudoSelector.get(), config))
                                                 .between(CssError::newInstance, token("("), token(")"))
                                                 .map(PseudoFunctionSelector.Signature::new)),
                        PseudoFunctionSelector::new)
                    .label("pseudoFunctionSelector");
    }

    /**
     * Builds a parser for an {@code An+B} (nth) pattern, as used in pseudo-class
     * functions such as {@code :nth-child} and {@code :nth-of-type}.
     * <p>
     * Accepts integers, the keywords {@code even} and {@code odd}, step expressions
     * such as {@code 2n}, {@code -3n}, or {@code n}, and an optional
     * {@code of &lt;selector&gt;} clause.
     * </p>
     *
     * @param config the CSS parser configuration used for arithmetic and selector sub-parsers
     * @return a parser that produces an {@link NthPattern} AST node
     */
    static Parser<NthPattern> nthPattern(final CssConfig config) {
        return sequence(arithmetic(or(integer(),
                                      identifier("even", "odd"),
                                      fragment(CssTag.WORD)
                                        .acceptIf(v -> v.toString().matches("\\d+n|-\\d+n|-n|n"))
                                        .map(v -> new Identifier(v.toString()))),
                                   config),
                        identifier("of").next(config.<CssSelector>parser(Node.selector)).optional(),
                        /** The field. */
                        NthPattern::new);
    }

    /*
     *
     * COMBINATOR
     *
     */

    /**
     * Builds a parser for a combinator selector expression, handling descendant (whitespace),
     * child ({@code >}), adjacent sibling ({@code +}), and general sibling ({@code ~}) combinators,
     * as well as prefix forms of the same operators.
     * <p>
     * The parser rejects degenerate trees where a {@link PrefixSelector} would appear on the
     * right-hand side of a combinator or as the operand of another prefix.
     * </p>
     *
     * @param config the CSS parser configuration used for attribute and pseudo-function sub-parsers
     * @return a parser that produces a {@link CssSelector}, labelled {@code "combinatorSelector"}
     */
    static Parser<CssSelector> combinatorSelector(final CssConfig config) {
        final Parser<CssSelector> operand = or(pseudoFunctionSelector(config),
                                               pseudoElementSelector(),
                                               pseudoClassSelector(),
                                               attributeSelector(config),
                                               classSelector(),
                                               idSelector(),
                                               namespaceSelector(),
                                               typeSelector());
        final Parser.Reference<CssSelector> expressionReference = Parser.newReference();
        final Parser<CssSelector> operationTable = new OperatorTable<CssSelector>()
           .infixl(whitespace(), 10)
           .infixl(operator(">"), 10)
           .infixl(operator("+"), 10)
           .infixl(operator("~"), 10)
           .prefix(prefix("+"), 20)
           .prefix(prefix(">"), 20)
           .prefix(prefix("~"), 20)
           .buildMap(operand);
        expressionReference.set(operationTable);
        return expressionReference
                 .lazy()
                 .acceptIf(v -> {
                     boolean result = true;
                     if (v instanceof CombinatorSelector combinatorSelector) {
                         result = !(combinatorSelector.getRightSelector() instanceof PrefixSelector);
                     }
                     else if (v instanceof PrefixSelector prefixSelector) {
                         result = !(prefixSelector.getSelector() instanceof PrefixSelector);
                     }
                     return result;
                 })
                 .label("combinatorSelector");
    }

    /**
     * Builds an infix operator parser that handles the implicit descendant combinator
     * (whitespace between selectors with no explicit combinator token).
     * <p>
     * When the two selectors are adjacent (no column gap), the right-hand selector is
     * folded into the left-hand side as a {@link QualifiedSelector}; otherwise a
     * descendant {@link CombinatorSelector} with an empty operator is produced.
     * </p>
     *
     * @return a parser producing a {@link MapInfix} functor for the whitespace combinator
     */
    private static Parser<MapInfix<Object, CssSelector, CssSelector, CssSelector>> whitespace() {
        return token(">", "+", "~")
                .not()
                .map(o -> MapInfix.map(o, (left, right) -> {
                  /** The result. */
                  final CssSelector result;
                  final int endColumn = left.getSourceLocation().end().column() + 1;
                  final int startColumn = right.getSourceLocation().start().column();
                  if (endColumn == startColumn) {
                      if (left instanceof PrefixSelector prefixSelector) {
                          final List<CssSelector> selectors = new ArrayList<>();
                          if (prefixSelector.getSelector() instanceof QualifiedSelector qualifiedSelector) {
                              selectors.addAll(qualifiedSelector.getSelectors());
                          }
                          else {
                              selectors.add(prefixSelector.getSelector());
                          }
                          selectors.add(right);
                          final QualifiedSelector qualifiedSelector = new QualifiedSelector(selectors);
                          qualifiedSelector.setSourceLocation(new SourceLocation(selectors.getFirst().getSourceLocation().start(),
                                                                                 selectors.getLast().getSourceLocation().end()));
                          result = new PrefixSelector(prefixSelector.getOperator(), qualifiedSelector);
                      }
                      else if (left instanceof CombinatorSelector combinatorSelector) {
                          final List<CssSelector> selectors = new ArrayList<>();
                          if (combinatorSelector.getRightSelector() instanceof QualifiedSelector qualifiedSelector) {
                              selectors.addAll(qualifiedSelector.getSelectors());
                          }
                          else {
                              selectors.add(combinatorSelector.getRightSelector());
                          }
                          selectors.add(right);
                          final QualifiedSelector qualifiedSelector = new QualifiedSelector(selectors);
                          qualifiedSelector.setSourceLocation(new SourceLocation(selectors.getFirst().getSourceLocation().start(),
                                                                                 selectors.getLast().getSourceLocation().end()));
                          result = new CombinatorSelector(combinatorSelector.getLeftSelector(), combinatorSelector.getOperator(), qualifiedSelector);
                      }
                      else if (left instanceof QualifiedSelector qualifiedSelector) {
                          final List<CssSelector> selectors = qualifiedSelector.getSelectors();
                          selectors.add(right);
                          final QualifiedSelector _qualifiedSelector = new QualifiedSelector(selectors);
                          _qualifiedSelector.setSourceLocation(new SourceLocation(selectors.getFirst().getSourceLocation().start(),
                                                                                  selectors.getLast().getSourceLocation().end()));
                          result = _qualifiedSelector;
                      }
                      else {
                          final List<CssSelector> selectors = new ArrayList<>();
                          selectors.add(left);
                          selectors.add(right);
                          result = new QualifiedSelector(selectors);
                      }
                  }
                  else {
                      result = new CombinatorSelector(left, Operator.empty(left.getSourceLocation().end()), right);
                  }
                  return result;
                }));
    }

    /**
     * Builds an explicit infix combinator parser for the given symbol
     * ({@code >}, {@code +}, or {@code ~}).
     * The resulting functor wraps the left and right selectors in a
     * {@link CombinatorSelector} node.
     *
     * @param symbol the combinator symbol to match
     * @return a parser producing a {@link MapInfix} functor for the explicit combinator
     */
    private static Parser<MapInfix<Object, CssSelector, CssSelector, CssSelector>> operator(final String symbol) {
        return token(symbol).map(Operator::new)
                            .map(operator -> MapInfix.map(operator, (left, right) -> new CombinatorSelector(left, operator, right)));
    }

    /**
     * Builds a unary prefix combinator parser for the given symbol
     * ({@code >}, {@code +}, or {@code ~}).
     * The resulting functor wraps the operand selector in a {@link PrefixSelector} node.
     *
     * @param symbol the combinator symbol to match
     * @return a parser producing a {@link MapOperator} functor for the prefix combinator
     */
    private static Parser<MapOperator<CssNode, CssSelector, CssSelector>> prefix(final String symbol) {
        return token(symbol).map(Operator::new)
                            .map(prefix -> MapOperator.map(prefix, operand -> new PrefixSelector(prefix, operand)));
    }

    /*
     *
     * SELECTOR
     *
     */

    /**
     * Builds a parser that recognises a complete CSS selector, which may be a single
     * combinator selector or a comma-separated list of selectors.
     * <p>
     * A single selector is returned as-is; multiple selectors are wrapped in a
     * {@link ListSelector}.
     * </p>
     *
     * @param config the CSS parser configuration used for combinator sub-parsers
     * @return a parser that produces a {@link CssSelector} AST node
     */
    static Parser<CssSelector> selector(final CssConfig config) {
        return combinatorSelector(config).sepBy(token(","))
                                         .map(v -> v.size() == 1
                                                      ? v.get(0)
                                                      : new ListSelector(v));
    }

}
