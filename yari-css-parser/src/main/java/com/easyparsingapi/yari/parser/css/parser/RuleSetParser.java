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
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.atomic;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.important;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.universal;
import static com.easyparsingapi.yari.parser.css.parser.ExpressionParser.arithmetic;
import static com.easyparsingapi.yari.parser.css.parser.ExpressionParser.function;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.token;
import static com.easyparsingapi.yari.parser.css.parser.UnitParser.block;
import static com.easyparsingapi.yari.parser.css.parser.UnitParser.handleErrorUntil;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parser.Reference;
import com.easyparsingapi.yari.parser.css.ast.Css;
import com.easyparsingapi.yari.parser.css.ast.CssError;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.css.ast.CssSelector;
import com.easyparsingapi.yari.parser.css.ast.Function;
import com.easyparsingapi.yari.parser.css.ast.ListValue;
import com.easyparsingapi.yari.parser.css.ast.Property;
import com.easyparsingapi.yari.parser.css.ast.RuleSet;
import com.easyparsingapi.yari.parser.css.ast.ListValue.Separator;
import com.easyparsingapi.yari.parser.css.parser.CssConfig.Node;

/**
 * Provides parsers for CSS rule sets, including property declarations and their values.
 * This class assembles the combinators needed to parse a complete CSS rule set
 * (selector + declaration block) as well as individual properties and their value expressions.
 */
public class RuleSetParser {

    /** Not instantiable — all methods are static. */
    private RuleSetParser() {}


//    private static final Logger LOGGER = LoggerFactory.getLogger(RuleSetParser.class);

    /*
     *
     * PROPERTY
     *
     */

    /**
     * Builds a parser that recognises a CSS function value, supporting recursive
     * function calls and arithmetic expressions inside the argument list.
     *
     * @param config the CSS parser configuration used to resolve sub-parsers
     * @return a {@link Parser} that produces a {@link Function} AST node
     */
    static Parser<Function> functionValue(final CssConfig config) {
        final Reference<Function> fucntionValueReference = Parser.newReference();
        fucntionValueReference.set(function(or(arithmetic(spaceListValueOrValue(or(fucntionValueReference.lazy(),
                                                                                   atomic())),
                                                          config),
                                               universal())
                                              .optional(),
                                            /** The field. */
                                            config));
        return fucntionValueReference.lazy()
                                     .label("functionValue");
    }

    /**
     * Builds a parser that recognises one or more comma-separated values,
     * wrapping them in a {@link ListValue} with {@link Separator#comma} when there are
     * multiple values, or returning the single value directly.
     *
     * @param element the parser for a single value element
     * @return a parser that produces either a single {@link CssNode} or a
     *         comma-separated {@link ListValue}, labelled {@code "commaListValueOrValue"}
     */
    private static Parser<CssNode> commaListValueOrValue(final Parser<CssNode> element) {
        return element.sepBy(token(","))
                      .map(cssNodes -> {
                          /** The result. */
                          final CssNode result;
                          if (cssNodes.size() == 1) {
                              result = cssNodes.get(0);
                          }
                          else {
                              result = new ListValue(Separator.comma, cssNodes);
                          }
                          return result;
                      })
                      .acceptIf(v -> {
                          boolean result = true;
                          if (v instanceof ListValue listValue) {
                              result = !listValue.getValues().isEmpty();
                          }
                          return result;
                      })
                      .label("commaListValueOrValue");
    }

    /**
     * Builds a parser that recognises one or more whitespace-separated values,
     * wrapping them in a {@link ListValue} with {@link Separator#space} when there are
     * multiple values, or returning the single value directly.
     *
     * @param element the parser for a single value element
     * @return a parser that produces either a single {@link CssNode} or a
     *         space-separated {@link ListValue}, labelled {@code "spaceListValueOrValue"}
     */
    private static Parser<CssNode> spaceListValueOrValue(final Parser<CssNode> element) {
        return element.many()
                      .map(cssNodes -> {
                          /** The result. */
                          final CssNode result;
                          if (cssNodes.size() == 1) {
                              result = cssNodes.get(0);
                          }
                          else {
                              result = new ListValue(Separator.space, cssNodes);
                          }
                          return result;
                      })
                      .acceptIf(v -> {
                          boolean result = true;
                          if (v instanceof ListValue listValue) {
                              result = !listValue.getValues().isEmpty();
                          }
                          return result;
                      })
                      .label("spaceListValueOrValue");
    }

    /**
     * Builds a parser for a complete CSS property value expression.
     * <p>
     * The value may be a function call, the {@code !important} flag, an atomic value,
     * or an arithmetic expression, and may consist of a comma-separated list of
     * space-separated components.
     * </p>
     *
     * @param config the CSS parser configuration used to resolve function and arithmetic sub-parsers
     * @return a parser that produces a {@link CssNode} representing the property value,
     *         labelled {@code "propertyValue"}
     */
    private static Parser<CssNode> propertyValue(final CssConfig config) {
        final Parser<CssNode> element = or(functionValue(config),
                                           important(),
                                           atomic());
        return commaListValueOrValue(spaceListValueOrValue(arithmetic(element, config)))
                .label("propertyValue");
    }

    /**
     * Builds a parser that recognises a single CSS property declaration (name + value).
     *
     * @param config the CSS parser configuration used to resolve value sub-parsers
     * @return a {@link Parser} that produces a {@link Property} AST node
     */
    static Parser<Property> property(final CssConfig config) {
        return sequence(identifier(),
                        token(":").next(propertyValue(config)),
                        Property::new)
                  .label("property");
    }

    /**
     * Builds a parser that recognises a sequence of CSS property declarations forming
     * a declaration block, tolerating syntax errors by recovering until the next
     * semicolon or end of input.
     *
     * @param config the CSS parser configuration used to resolve property sub-parsers
     * @return a {@link Parser} that produces a {@link Css} node containing all parsed declarations
     */
    static Parser<Css> properties(final CssConfig config) {
        return property(config)
                .<CssNode>cast()
                .followedBy(token(";").optional())
                .manyUntilEof(CssError::newInstance, handleErrorUntil)
                .map(nodes -> new Css(nodes, config.getComments()))
                .label("unit");
    }

    /*
     *
     * RULE SET
     *
     */

    /**
     * Builds a parser that recognises a complete CSS rule set, consisting of a
     * selector followed by a declaration block enclosed in braces.
     *
     * @param config the CSS parser configuration used to resolve the selector and block sub-parsers
     * @return a {@link Parser} that produces a {@link RuleSet} AST node
     */
    static Parser<RuleSet> ruleSet(final CssConfig config) {
        return sequence(config.<CssSelector>parser(Node.selector),
                        block(config),
                        RuleSet::new)
                   .label("ruleSet");
    }

}
