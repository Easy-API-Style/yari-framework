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
import static com.easyparsingapi.yari.parser.css.parser.AtRuleParser.combinatorAtRuleParameter;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.atomic;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.doubleQuoteString;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.integer;
import static com.easyparsingapi.yari.parser.css.parser.AtomicParser.singleQuoteString;
import static com.easyparsingapi.yari.parser.css.parser.ExpressionParser.function;
import static com.easyparsingapi.yari.parser.css.parser.SelectorParser.nthPattern;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Parser.Reference;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.location.SourceLocator;
import com.easyparsingapi.yari.parser.css.ast.CssComment;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.css.ast.Function;
import com.easyparsingapi.yari.parser.css.ast.ListValue;
import com.easyparsingapi.yari.parser.css.ast.Parenthesis;
import com.easyparsingapi.yari.parser.css.ast.ListValue.Separator;
import com.easyparsingapi.yari.parser.css.lexer.CssTag;

/**
 * Central CSS parser configuration that initializes parser references
 * for at-rules, rule sets, selectors, as well as supported pseudo-elements,
 * pseudo-classes, and at-rule parameters.
 */
public class CssConfig implements ApiParser.Config {

//    private static final Logger LOGGER = LoggerFactory.getLogger(CssConfig.class);

    /**
     * Enumeration of the main CSS node types handled by the parser.
     */
    public static enum Node {
        /** At-rule node (e.g. {@code @media}, {@code @import}). */
        atRule,
        /** Rule-set node (selector + declaration block). */
        ruleSet,
        /** Standalone selector node. */
        selector
    }

    private final Map<Node, Reference<CssNode>> nodeReferences = new HashMap<>();
    private final SequenceService sequenceService = new SequenceService();

    private static Map<String, Parser<CssNode>> pseudoElementSelectorMap = new HashMap<>();
    private static Map<String, Parser<CssNode>> pseudoClassSelectorMap = new HashMap<>();

    private static Map<String, Parser<CssNode>> atRuleParameterMap = new HashMap<>();

    /** The tokens. */
    private List<Token> tokens;
    /** The comments. */
    private List<AstComment> comments;
    /** The sourceLocator. */
    private SourceLocator sourceLocator;

    /**
     * Constructs a new {@code CssConfig} instance and initializes all
     * parser references for the known CSS nodes.
     */
    public CssConfig() {
        super();
        this.initializeReference();
    }

    /**
     * Returns the list of tokens produced during the last call to {@link #onTokens}.
     *
     * @return the list of CSS tokens, or {@code null} if no tokenization has occurred yet
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Returns the list of comments extracted during the last call to {@link #onTokens}.
     *
     * @return the list of CSS comments, or {@code null} if no tokenization has occurred yet
     */
    public List<AstComment> getComments() {
        return comments;
    }

    /**
     * Returns the source locator associated with the last processed token stream.
     *
     * @return the current {@link SourceLocator}, or {@code null} if no tokenization has occurred yet
     */
    public SourceLocator getSourceLocator() {
        return sourceLocator;
    }

    /**
     * Returns the sequence service used to handle parenthesized constructs
     * and other structured CSS sequences.
     *
     * @return the {@link SequenceService} associated with this configuration
     */
    public SequenceService sequenceService() {
        return sequenceService;
    }

    /**
     * Returns the parser registered for the given CSS node type.
     *
     * @param <A>  the CSS node type produced by the parser
     * @param node the node type whose parser is requested
     * @return the parser associated with the node, accessed via its lazy reference
     */
    @SuppressWarnings("unchecked")
    protected <A extends CssNode> Parser<A> parser(final Node node) {
        return (Parser<A>) nodeReferences.get(node).lazy();
    }

    @SuppressWarnings("unchecked")
    private <A extends CssNode> void setNodeReference(final Node node, Parser<A> parser) {
        final Reference<A> reference = (Reference<A>) this.nodeReferences.get(node);
        reference.set(parser);
    }

    /**
     * Returns the parser registered for the given CSS pseudo-element function.
     *
     * @param function the pseudo-element function name (e.g. {@code "slotted"})
     * @return the corresponding parser, or {@code null} if none is registered for this name
     */
    protected Parser<CssNode> pseudoElementSelector(final String function) {
        return pseudoElementSelectorMap.get(function);
    }

    private void pseudoElementSelector(final String function,
                                       final Parser<? extends CssNode> parser) {
        pseudoElementSelectorMap.put(function, parser.cast());
    }

    /**
     * Returns the parser registered for the given CSS pseudo-class function.
     *
     * @param function the pseudo-class function name (e.g. {@code "nth-child"})
     * @return the corresponding parser, or {@code null} if none is registered for this name
     */
    protected Parser<CssNode> pseudoClassSelector(final String function) {
        return pseudoClassSelectorMap.get(function);
    }

    private void pseudoClassSelectorMap(final String function,
                                        final Parser<? extends CssNode> parser) {
        pseudoClassSelectorMap.put(function, parser.cast());
    }

    /**
     * Returns the parser registered for the parameter of the given CSS at-rule.
     *
     * @param rule the at-rule name (e.g. {@code "media"}, {@code "keyframes"})
     * @return the corresponding parser, or {@code null} if none is registered for this name
     */
    protected Parser<CssNode> atRuleParameter(final String rule) {
        return atRuleParameterMap.get(rule);
    }

    private void atRuleParameter(final String rule,
                                 final Parser<? extends CssNode> parser) {
        atRuleParameterMap.put(rule, parser.cast());
    }

    private void atRuleParameter(final Set<String> rules) {
        for (final String rule : rules) {
            atRuleParameterMap.put(rule, Parsers.never().label("atRuleParameter_" + rule).cast());
        }
    }

    /**
     * Callback invoked by the parsing infrastructure after tokenization.
     * Stores the source locator and full token list, and extracts CSS block
     * comments into a separate list.
     *
     * @param sourceLocator the locator used to map token offsets to source positions
     * @param tokens        the full list of tokens produced by the lexer
     */
    @Override
    public void onTokens(final SourceLocator sourceLocator,
                         final List<Token> tokens) {
        this.sourceLocator = sourceLocator;
        this.tokens = tokens;
        this.comments = comments(sourceLocator, tokens);
    }

    /**
     * Filters the raw token list before parsing by removing block comments and
     * carriage-return tokens.
     *
     * @param tokens the full token list to filter
     * @return a new list containing only the tokens relevant to the CSS grammar
     */
    @Override
    public List<Token> filter(final List<Token> tokens) {
        return clean(tokens);
    }

    private static List<Token> clean(final List<Token> tokens) {
        final List<Token> result = new ArrayList<>();
        for (final Token token : tokens) {
            if (token.value() instanceof Fragment fragment) {
                boolean doAdd = true;
                if (CssTag.BLOCK_COMMENT.equals(fragment.tag())) {
                    doAdd = false;
                }
                else if (CssTag.RETURN_CARRIAGE.equals(fragment.tag())) {
                    doAdd = false;
                }
                if (doAdd) {
                    result.add(token);
                }
            }
        }
        return result;
    }

    private List<AstComment> comments(final SourceLocator sourceLocator,
                                      final List<Token> tokens) {
        final List<AstComment> result = new ArrayList<>();
        for (final Token token : tokens) {
            if (token.value() instanceof Fragment fragment) {
                CssComment cssComment = null;
                if (CssTag.BLOCK_COMMENT.equals(fragment.tag())) {
                    cssComment = new CssComment(fragment.text());
                }
                if (cssComment != null) {
                    final SourceLocation sourceLocation = new SourceLocation(sourceLocator.locate(token.index()),
                                                                             sourceLocator.locate(token.index() + token.length()));
                    cssComment.setSourceLocation(sourceLocation);
                    result.add(cssComment);
                }
            }
        }
        return result;
    }

    private void initializeReference() {
        for (final Node node : Node.values()) {
            this.nodeReferences.put(node, Parser.newReference());
        }
        this.setNodeReference(Node.atRule, AtRuleParser.atRule(this));
        this.setNodeReference(Node.ruleSet, RuleSetParser.ruleSet(this));
        this.setNodeReference(Node.selector, SelectorParser.selector(this));

        // pseudo element function
        pseudoElementSelector("highlight", identifier().label("pseudoElementFunction_highlight"));
        pseudoElementSelector("part", identifier().many()
                                                  .map(v -> ListValue.of(Separator.space, v))
                                                  .label("pseudoElementFunction_part"));
        pseudoElementSelector("scroll-button", parser(Node.selector).label("pseudoElementFunction_scroll-button"));
        pseudoElementSelector("slotted", parser(Node.selector).label("pseudoElementFunction_slotted"));
        pseudoElementSelector("view-transition-group", parser(Node.selector).label("pseudoElementFunction_view-transition-group"));
        pseudoElementSelector("view-transition-image-pair", parser(Node.selector).label("pseudoElementFunction_view-transition-image-pair"));
        pseudoElementSelector("view-transition-old", parser(Node.selector).label("pseudoElementFunction_view-transition-old"));
        pseudoElementSelector("view-transition-new", parser(Node.selector).label("pseudoElementFunction_view-transition-new"));

        // pseudo class function
        pseudoClassSelectorMap("dir", identifier().label("pseudoClassFunction_dir"));
        pseudoClassSelectorMap("-moz-locale-dir", identifier().label("pseudoClassFunction_-moz-locale-dir"));
        pseudoClassSelectorMap("has", parser(Node.selector).label("pseudoClassFunction_has"));
        pseudoClassSelectorMap("heading", integer().sepBy(token(","))
                                                   .map(v -> ListValue.of(Separator.comma, v))
                                                   .label("pseudoClassFunction_heading"));
        pseudoClassSelectorMap("host", parser(Node.selector).label("pseudoClassFunction_host"));
        pseudoClassSelectorMap("host-context", parser(Node.selector).label("pseudoClassFunction_host-context"));
        pseudoClassSelectorMap("is", parser(Node.selector).label("pseudoClassFunction_is"));
        pseudoClassSelectorMap("lang", or(singleQuoteString(),
                                          doubleQuoteString(),
                                          identifier())
                                        .sepBy(token(","))
                                        .map(v -> ListValue.of(Separator.comma, v))
                                        .label("pseudoClassFunction_lang"));
        pseudoClassSelectorMap("not", parser(Node.selector).label("pseudoClassFunction_not"));
        pseudoClassSelectorMap("nth-child", nthPattern(this).label("pseudoClassFunction_nth-child"));
        pseudoClassSelectorMap("nth-last-child", nthPattern(this).label("pseudoClassFunction_nth-last-child"));
        pseudoClassSelectorMap("nth-of-type", nthPattern(this).label("pseudoClassFunction_nth-of-type"));
        pseudoClassSelectorMap("nth-last-of-type", nthPattern(this).label("pseudoClassFunction_nth-last-of-type"));
        pseudoClassSelectorMap("state", identifier().label("pseudoClassFunction_state"));

        // at rule parameter
        // @charset
        atRuleParameter("charset", or(singleQuoteString(),
                                      doubleQuoteString())
                                    .label("atRuleParameter_charset"));
        // @color-profile
        atRuleParameter("color-profile", identifier().label("atRuleParameter_color-profile"));
        // @container
        final Parser.Reference<Function> containerFunction = Parser.newReference();
        containerFunction.set(function(combinatorAtRuleParameter(or(containerFunction.lazy(),
                                                                    AtRuleParser.atRuleRange(),
                                                                    AtRuleParser.atRuleInfix(),
                                                                    RuleSetParser.property(this),
                                                                    atomic()), this),
                                       /** The field. */
                                       this));
        atRuleParameter("container", or(containerFunction.lazy(),
                                        AtRuleParser.atRuleRange(),
                                        AtRuleParser.atRuleInfix(),
                                        RuleSetParser.property(this),
                                        atomic())
                                      .label("atRuleParameter_container"));
        // @counter-style
        atRuleParameter("counter-style", identifier().label("atRuleParameter_counter-style"));
        // @document
        atRuleParameter("document", function(or(singleQuoteString(),
                                                doubleQuoteString()),
                                             this)
                                       .label("atRuleParameter_document"));
        // @font-face
        atRuleParameter("font-face", Parsers.never().label("atRuleParameter_font-face").cast());
        // @font-feature-values
        atRuleParameter("font-feature-values", atomic().label("atRuleParameter_font-feature-values"));
        final Set<String> fontFeatureValuesRules =
                Set.of("swash",
                       "annotation",
                       "ornaments",
                       "character-variant");
        atRuleParameter(fontFeatureValuesRules);
        // @font-palette-values
        atRuleParameter("font-palette-values", atomic().label("atRuleParameter_font-palette-values"));
        // @function
        atRuleParameter("function", or(RuleSetParser.functionValue(this),
                                       atomic())
                                    .label("atRuleParameter_function"));
        // @import
        final Parser.Reference<Function> importFunction = Parser.newReference();
        importFunction.set(function(combinatorAtRuleParameter(or(importFunction.lazy(),
                                                                 AtRuleParser.atRuleRange(),
                                                                 AtRuleParser.atRuleInfix(),
                                                                 RuleSetParser.property(this),
                                                                 AtRuleParser.qualifiedIdentifier(this),
                                                                 atomic()), this),
                                    /** The field. */
                                    this));
        atRuleParameter("import", or(importFunction.lazy(),
                                     AtRuleParser.atRuleRange(),
                                     AtRuleParser.atRuleInfix(),
                                     RuleSetParser.property(this),
                                     AtRuleParser.qualifiedIdentifier(this),
                                     atomic())
                                   .label("atRuleParameter_import"));
        // @keyframes
        atRuleParameter("keyframes", atomic().label("atRuleParameter_keyframes"));
        // @layer
        atRuleParameter("layer", or(AtRuleParser.qualifiedIdentifier(this),
                                    atomic())
                                  .label("atRuleParameter_layer"));
        // @media
        atRuleParameter("media", or(AtRuleParser.atRuleRange(),
                                    AtRuleParser.atRuleInfix(),
                                    RuleSetParser.property(this),
                                    atomic())
                                  .label("atRuleParameter_media"));
        // @supports
        final Parser.Reference<Function> supportsFunction = Parser.newReference();
        supportsFunction.set(function(combinatorAtRuleParameter(or(importFunction.lazy(),
                                                                   AtRuleParser.atRuleRange(),
                                                                   AtRuleParser.atRuleInfix(),
                                                                   RuleSetParser.property(this),
                                                                   AtRuleParser.qualifiedIdentifier(this),
                                                                   parser(Node.selector),
                                                                   atomic()), this),
                                       /** The field. */
                                       this));
        atRuleParameter("supports", or(function(identifier("font-tech",
                                                           "font-format"),
                                                identifier().cast(),
                                                this),
                                       supportsFunction.lazy(),
                                       AtRuleParser.atRuleRange(),
                                       AtRuleParser.atRuleInfix(),
                                       RuleSetParser.property(this),
                                       AtRuleParser.qualifiedIdentifier(this),
                                       atomic())
                                     .label("atRuleParameter_supports"));
        // @namespace
        atRuleParameter("namespace", or(function(atomic(), this),
                                        atomic())
                                      .label("atRuleParameter_namespace"));
        // @page
        atRuleParameter("page", or(SelectorParser.pseudoClassSelector(),
                                   atomic())
                                 .label("atRuleParameter_page"));
        final Set<String> pageRules =
            Set.of("top-left-corner",
                   "top-left",
                   "top-center",
                   "top-right",
                   "top-right-corner",
                   "bottom-left-corner",
                   "bottom-left",
                   "bottom-center",
                   "bottom-right",
                   "bottom-right-corner",
                   "left-top",
                   "left-middle",
                   "left-bottom",
                   "right-top",
                   "right-middle",
                   "right-bottom");
        atRuleParameter(pageRules);
        // @position-try
        atRuleParameter("position-try", atomic().label("atRuleParameter_position-try"));
        // @property
        atRuleParameter("property", atomic().label("atRuleParameter_property"));
        // @scope
        atRuleParameter("scope", or(parseIf(c -> sequenceService().parenthesis(c),
                                            parser(Node.selector).between(token("("), token(")"))
                                                                 .map(Parenthesis::new)),
                                    atomic())
                                 .label("atRuleParameter_scope"));
        // @starting-style
        atRuleParameter("starting-style", Parsers.never().label("atRuleParameter_starting-style").cast());
        // @view-transition
        atRuleParameter("view-transition", Parsers.never().label("atRuleParameter_view-transition").cast());
    }

    /**
     * Returns a brief string representation of this configuration including
     * the number of tokens held, or {@code -1} if no tokenization has been performed yet.
     *
     * @return a human-readable description of this {@code CssConfig}
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(CssConfig.class.getSimpleName());
        result.append(" [tokens=");
        result.append(tokens != null ? tokens.size() : -1);
        result.append("]");
        return result.toString();
    }

}
