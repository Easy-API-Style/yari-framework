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
import static com.easyparsingapi.yari.parser.css.parser.RuleSetParser.property;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.token;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Parsers.TokenContext;
import com.easyparsingapi.yari.parser.css.ast.Block;
import com.easyparsingapi.yari.parser.css.ast.Css;
import com.easyparsingapi.yari.parser.css.ast.CssError;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.css.parser.CssConfig.Node;
import com.easyparsingapi.yari.parser.css.parser.SequenceService.Sequence;
import com.easyparsingapi.yari.parser.css.parser.SequenceService.Type;

/**
 * Provides the base parsers used to analyse CSS units,
 * in particular blocks delimited by curly braces and sequences of CSS nodes.
 */
public class UnitParser {

    /** Not instantiable — all methods are static. */
    private UnitParser() {}


//    private static final Logger LOGGER = LoggerFactory.getLogger(UnitParser.class);

    /**
     * Error-recovery sentinel parser used by {@code manyUntilEof} combinators.
     * Matches the next {@code }}, {@code ;}, or the start of an at-rule ({@code @})
     * without consuming the at-rule token, so that the caller can resume parsing
     * at a known safe position after a syntax error.
     */
    static final Parser<?> handleErrorUntil =
        or(token("}"),
           token(";"),
           token("@").peek());

    /**
     * Builds a parser that recognises a sequence of top-level CSS nodes (at-rules,
     * rule sets, and property declarations), recovering from syntax errors at each
     * semicolon or closing brace.
     *
     * @param config the CSS parser configuration used to resolve at-rule and rule-set sub-parsers
     * @return a parser that produces a {@link List} of {@link CssNode} instances,
     *         labelled {@code "cssNodes"}
     */
    static Parser<List<CssNode>> cssNodes(final CssConfig config) {
        return or(config.<CssNode>parser(Node.atRule),
                  config.<CssNode>parser(Node.ruleSet),
                  property(config))
                .followedBy(token(";").optional())
                .manyUntilEof(CssError::newInstance, handleErrorUntil)
                .label("cssNodes");
    }

    /**
     * Builds a parser that recognises a CSS declaration block enclosed in curly braces
     * ({@code { ... }}).
     * <p>
     * The parser only succeeds when the opening {@code {}} forms a valid, complete
     * curly-brace sequence. The outer braces are consumed but excluded from the resulting
     * {@link Block}; if the block is empty (only braces), an empty list is produced.
     * </p>
     *
     * @param config the CSS parser configuration used to verify the curly-brace sequence
     * @return a parser that produces a {@link Block} AST node, labelled {@code "block"}
     */
    static Parser<Block> block(final CssConfig config) {
        final AtomicInteger lastIndex = new AtomicInteger();
        return parseIf(c -> {
                            final TokenContext tokenContext = c.tokenContext();
                            final Sequence sequence = config.sequenceService().inside(Type.curlingBracket, tokenContext);
                            final boolean result = sequence.isValid();
                            if (result) {
                                lastIndex.set(tokenContext.tokens()[sequence.tokenIndex()].index());
                            }
                            return result;
                       },
                       Parsers.ANY_TOKEN
                              .acceptIf(token -> token.index() <= lastIndex.get())
                              .many()
                              .map(tokens -> {
                                    List<CssNode> result = List.of();
                                    if (tokens.size() > 2) {
                                        tokens.removeFirst();
                                        tokens.removeLast();
                                        result = new CssNodeList(new ArrayList<>(tokens));
                                    }
                                    return result;
                               })
                               .map(Block::new))
                               .label("block");
    }

    /**
     * Builds a parser for a complete CSS stylesheet unit, recognising a sequence of
     * at-rules and rule sets and wrapping them in a {@link Css} node together with
     * any collected comments.
     * <p>
     * Syntax errors are recovered from using {@link #handleErrorUntil}.
     * </p>
     *
     * @param config the CSS parser configuration used to resolve at-rule and rule-set sub-parsers
     * @return a parser that produces a {@link Css} AST node, labelled {@code "unit"}
     */
    static Parser<Css> unit(final CssConfig config) {
        return or(config.<CssNode>parser(Node.atRule),
                  config.<CssNode>parser(Node.ruleSet))
                .followedBy(token(";").optional())
                .manyUntilEof(CssError::newInstance, handleErrorUntil)
                .map(nodes -> new Css(nodes, config.getComments()))
                .label("unit");
    }

}
