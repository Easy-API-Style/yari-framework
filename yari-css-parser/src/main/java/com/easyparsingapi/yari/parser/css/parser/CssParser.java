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

import static com.easyparsingapi.yari.parser.css.parser.RuleSetParser.properties;
import static com.easyparsingapi.yari.parser.css.parser.UnitParser.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.core.ast.AstResult;
import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parser.css.ast.Block;
import com.easyparsingapi.yari.parser.css.ast.Css;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.css.ast.CssUtil;
import com.easyparsingapi.yari.parser.css.lexer.CssLexer;

/**
 * Main entry point of the CSS parser, exposing static methods to parse
 * a complete stylesheet (unit) or a set of isolated CSS properties.
 * Parsing is performed in multiple passes in order to resolve nested blocks
 * concurrently.
 */
public class CssParser {

    /** Not instantiable — all methods are static. */
    private CssParser() {}


    private static final Logger LOGGER = LoggerFactory.getLogger(CssParser.class);

    /*
     *
     * UNIT
     *
     */

    /**
     * Parses a complete CSS string using the «unit» grammar and returns
     * the result as an AST along with the tokens produced by the lexer.
     *
     * @param css the string containing the CSS to parse
     * @return an {@link AstResult} wrapping the {@link Css} AST and the token list
     */
    public static AstResult<Css> parseUnit(final String css) {
        LOGGER.debug("parsing css...");
        final CssConfig cssConfig = new CssConfig();
        final Css cssAst = ApiParser.parse(unit(cssConfig),
                                           CssLexer.lexer(),
                                           cssConfig,
                                           /** The field. */
                                           css);
        applySubParsing(cssAst);
        return new AstResult<>(cssAst, cssConfig.getTokens());
    }

    /**
     * Parses a list of CSS tokens already produced by the lexer using the
     * «unit» grammar and returns the result as an AST.
     *
     * @param tokens the list of {@link Token} to parse
     * @return an {@link AstResult} wrapping the {@link Css} AST and the token list
     */
    public static AstResult<Css> parseUnit(final List<Token> tokens) {
        LOGGER.debug("[token] parsing css...");
        final CssConfig cssConfig = new CssConfig();
        final Css cssAst = ApiParser.parse(unit(cssConfig),
                                           tokens,
                                           /** The field. */
                                           cssConfig);
        applySubParsing(cssAst);
        return new AstResult<>(cssAst, cssConfig.getTokens());
    }

    /*
     *
     * PROPERTIES
     *
     */

    /**
     * Parses a CSS string using the «properties» grammar (CSS properties only,
     * without selectors) and returns the result as an AST.
     *
     * @param css the string containing the CSS properties to parse
     * @return an {@link AstResult} wrapping the {@link Css} AST and the token list
     */
    public static AstResult<Css> parseProperties(final String css) {
        LOGGER.debug("[property] parsing css...");
        final CssConfig cssConfig = new CssConfig();
        final Css cssAst = ApiParser.parse(properties(cssConfig),
                                           CssLexer.lexer(),
                                           cssConfig,
                                           /** The field. */
                                           css);
        applySubParsing(cssAst);
        return new AstResult<>(cssAst, cssConfig.getTokens());
    }

    /**
     * Parses a list of CSS tokens already produced by the lexer using the
     * «properties» grammar and returns the result as an AST.
     *
     * @param tokens the list of {@link Token} to parse
     * @return an {@link AstResult} wrapping the {@link Css} AST and the token list
     */
    public static AstResult<Css> parseProperties(final List<Token> tokens) {
        final CssConfig cssConfig = new CssConfig();
        final Css cssAst = ApiParser.parse(properties(cssConfig),
                                           tokens,
                                           /** The field. */
                                           cssConfig);
        applySubParsing(cssAst);
        return new AstResult<>(cssAst, cssConfig.getTokens());
    }

    private static Set<Block> filterCssBlock(final CssNode cssNode) {
        return cssNode.astStream()
                      .filter(n -> n instanceof Block)
                      .map(n -> (Block) n)
                      .filter(n -> n.getNodes() instanceof CssNodeList)
                      .collect(Collectors.toSet());
    }

    /*
     *
     * SUB PARSER
     *
     */
    private static void applySubParsing(final CssNode cssNode) {
       subParsing(filterCssBlock(cssNode), 1);
    }

    private static void subParsing(final Set<Block> blocks,
                                   final int deep) {
        if (!blocks.isEmpty()) {
            final Set<Block> newCssBlocks = ConcurrentHashMap.newKeySet();
            final int total = blocks.size();
            final List<CompletableFuture<?>> futures = new ArrayList<>();
            LOGGER.debug("parsing css step: deep={} element={}...", deep, total);
            for (final Block block : blocks) {
                futures.add(CompletableFuture.runAsync(() -> {
                    final CssNodeList blockList = (CssNodeList) block.getNodes();
                    final List<Token> tokens = blockList.tokens();

                    final CssConfig cssConfig = new CssConfig() {
                        /** {@inheritDoc} */
                        @Override
                        public List<Token> filter(final List<Token> tokens) {
                            return tokens;
                        }
                    };
                    final List<CssNode> cssNodes = ApiParser.parse(UnitParser.cssNodes(cssConfig),
                                                                   tokens,
                                                                   /** The field. */
                                                                   cssConfig);
                    blockList.addAll(cssNodes);
                    CssUtil.setAstParent(block);
                    for (final CssNode cssNode : cssNodes) {
                        newCssBlocks.addAll(filterCssBlock(cssNode));
                    }
                }));
            }
            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                             .join();
            subParsing(newCssBlocks, deep + 1);
        }
    }

}
