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
package com.easyparsingapi.yari.parser.javascript.parser;

import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.unit;

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
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptProcedure;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptUtil;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptLexer;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;

/**
 * Static utility class that provides the public entry points for parsing JavaScript
 * source code into an AST.
 * All parsing methods lex the source (or accept a pre-built token list), run the
 * grammar, and then apply a second-pass sub-parser to lazily expand procedure bodies.
 */
public class JavascriptParser {



    /** Not instantiable — all methods are static. */
    private JavascriptParser() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(JavascriptParser.class);
    
    static <N extends JavascriptNode> N parse(final Parser<N> parser,
                                              final JavascriptConfig javascriptConfig,
                                              final String javascript) {
        final N result = ApiParser.parse(parser,
                                         JavascriptLexer.lexer(), 
                                         javascriptConfig, 
                                         /** Field. */
                                         javascript);
        applySubParsing(result);
        return result;
    }
    
    /*
     * 
     * EXPRESSION
     * 
     */
    static JavascriptNode parseExpression(final String javascript,
                                          final Set<Node> nodes) {
        LOGGER.debug("[expression] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig(); 
        final JavascriptNode result = ApiParser.parse(ExpressionParser.expression(javascriptConfig, nodes),
                                                      JavascriptLexer.lexer(), 
                                                      javascriptConfig, 
                                                      /** Field. */
                                                      javascript);
        applySubParsing(result);
        return result;
    }

    static AstResult<JavascriptNodeUnit> parseUnitExpression(final String javascript,
                                                             final Set<Node> nodes) {
        LOGGER.debug("[expression] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig(); 
        final JavascriptNode javascriptNode = ApiParser.parse(ExpressionParser.expression(javascriptConfig, nodes)
                                                                              .followedBy(SeparatorParser.endStatement()),
                                                              JavascriptLexer.lexer(),  
                                                              javascriptConfig, 
                                                              /** Field. */
                                                              javascript);
        applySubParsing(javascriptNode);
        return new AstResult<>(new JavascriptNodeUnit(javascriptNode, javascriptConfig.getComments()),
                               javascriptConfig.getTokens());
    }

    /**
     * Parses a JavaScript source string as an expression and returns the result.
     *
     * @param javascript the JavaScript source string containing an expression
     * @return an {@link AstResult} wrapping the parsed {@link Javascript} expression node
     *         together with the token list
     */
    public static AstResult<Javascript> parseExpression(final String javascript) {
        LOGGER.debug("[unitExpression] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig();
        final Javascript javascriptAst = ApiParser.parse(ExpressionParser.unitExpression(javascriptConfig),
                                                         JavascriptLexer.lexer(),
                                                         javascriptConfig,
                                                         /** Field. */
                                                         javascript);
        applySubParsing(javascriptAst);
        return new AstResult<>(javascriptAst, javascriptConfig.getTokens());
    }

    /**
     * Parses a pre-tokenised list of tokens as an expression and returns the result.
     *
     * @param tokens the pre-built token list representing a JavaScript expression
     * @return an {@link AstResult} wrapping the parsed {@link Javascript} expression node
     *         together with the token list
     */
    public static AstResult<Javascript> parseExpression(final List<Token> tokens) {
        LOGGER.debug("[unitExpression][token] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig();
        final Javascript javascriptAst = ApiParser.parse(ExpressionParser.unitExpression(javascriptConfig),
                                                         tokens,
                                                         /** Field. */
                                                         javascriptConfig);
        applySubParsing(javascriptAst);
        return new AstResult<>(javascriptAst, javascriptConfig.getTokens());
    }
    
    /*
     * 
     * NODE
     * 
     */
    static JavascriptNode parse(final String javascript,
                                final Node node) {
        LOGGER.debug("[node] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig(); 
        final JavascriptNode result = ApiParser.parse(javascriptConfig.parser(node),
                                                      JavascriptLexer.lexer(), 
                                                      javascriptConfig, 
                                                      /** Field. */
                                                      javascript);
        applySubParsing(result);
        return result;
    }
    
    static Javascript parse(final String javascript,
                            final Set<Node> nodes) {
        LOGGER.debug("[nodes] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig();
        final Javascript result = ApiParser.parse(unit(javascriptConfig, nodes),
                                                  JavascriptLexer.lexer(), 
                                                  javascriptConfig, 
                                                  /** Field. */
                                                  javascript);
        applySubParsing(result);
        return result;
    }

    /**
     * Parses a pre-tokenised list of tokens as a complete JavaScript program.
     *
     * @param tokens the pre-built token list representing a full JavaScript source
     * @return the root {@link Javascript} AST node for the parsed program
     */
    public static Javascript parse(final List<Token> tokens) {
        LOGGER.debug("[token] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig();
        final Javascript result = ApiParser.parse(unit(javascriptConfig),
                                                  /** Field. */
                                                  tokens);
        applySubParsing(result);
        return result;
    }

    /**
     * Parses a JavaScript source string as a complete program.
     *
     * @param javascript the raw JavaScript source code to parse
     * @return the root {@link Javascript} AST node for the parsed program
     */
    public static Javascript parse(final String javascript) {
        LOGGER.debug("parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig();
        final Javascript result = ApiParser.parse(unit(javascriptConfig),
                                                  JavascriptLexer.lexer(),
                                                  javascriptConfig,
                                                  /** Field. */
                                                  javascript);
        applySubParsing(result);
        return result;
    }
 
    /*
     * 
     * UNIT
     * 
     */
    static AstResult<JavascriptNodeUnit> parseUnit(final String javascript,
                                                   final Node node) {
        LOGGER.debug("[unit][node] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig(); 
        final JavascriptNode javascriptNode = ApiParser.parse(javascriptConfig.parser(node),
                                                              JavascriptLexer.lexer(), 
                                                              javascriptConfig, 
                                                              /** Field. */
                                                              javascript);
        applySubParsing(javascriptNode);
        return new AstResult<>(new JavascriptNodeUnit(javascriptNode, javascriptConfig.getComments()),
                               javascriptConfig.getTokens());
    }
    
    static AstResult<Javascript> parseUnit(final String javascript, 
                                           final Set<Node> nodes) {
        LOGGER.debug("[unit][nodes] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig();
        final Javascript javascriptAst = ApiParser.parse(unit(javascriptConfig, nodes),
                                                         JavascriptLexer.lexer(),  
                                                         javascriptConfig, 
                                                         /** Field. */
                                                         javascript);
        applySubParsing(javascriptAst);
        return new AstResult<>(javascriptAst, javascriptConfig.getTokens());
    }
    
    /**
     * Parses a JavaScript source string as a complete program and returns the result
     * together with the token list.
     *
     * @param javascript the raw JavaScript source code to parse
     * @return an {@link AstResult} wrapping the root {@link Javascript} AST node
     *         together with the token list
     */
    public static AstResult<Javascript> parseUnit(final String javascript) {
        LOGGER.debug("[unit] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig();
        final Javascript javascriptAst = ApiParser.parse(unit(javascriptConfig),
                                                         JavascriptLexer.lexer(),
                                                         javascriptConfig,
                                                         /** Field. */
                                                         javascript);
        applySubParsing(javascriptAst);
        return new AstResult<>(javascriptAst, javascriptConfig.getTokens());
    }

    /**
     * Parses a pre-tokenised list of tokens as a complete JavaScript program and returns
     * the result together with the token list.
     *
     * @param tokens the pre-built token list representing a full JavaScript source
     * @return an {@link AstResult} wrapping the root {@link Javascript} AST node
     *         together with the token list
     */
    public static AstResult<Javascript> parseUnit(final List<Token> tokens) {
        LOGGER.debug("[unit][tokens] parsing javascript...");
        final JavascriptConfig javascriptConfig = new JavascriptConfig();
        final Javascript javascriptAst = ApiParser.parse(unit(javascriptConfig),
                                                         tokens,
                                                         /** Field. */
                                                         javascriptConfig);
        applySubParsing(javascriptAst);
        return new AstResult<>(javascriptAst, javascriptConfig.getTokens());
    }
    
    /*
     * 
     * SUB PARSER
     * 
     */
    private static Set<JavascriptProcedure> filterJavascriptProcedure(final JavascriptNode javascriptNode) {
        return javascriptNode.astStream()
                             .filter(n -> n instanceof JavascriptProcedure)
                             .map(n -> (JavascriptProcedure) n)
                             .filter(n -> n.getNodes() instanceof Block)
                             .collect(Collectors.toSet());
    }
    
    private static void applySubParsing(final JavascriptNode javascriptNode) { 
       subParsing(filterJavascriptProcedure(javascriptNode), 1);
    }
    
    private static void subParsing(final Set<JavascriptProcedure> javascriptProcedures,
                                   final int deep) {
        if (!javascriptProcedures.isEmpty()) {
            final Set<JavascriptProcedure> newJavascriptProcedures = ConcurrentHashMap.newKeySet();
            final int total = javascriptProcedures.size();
            
            final List<CompletableFuture<?>> futures = new ArrayList<>();
            LOGGER.debug("parsing javascript step: deep={} element={}...", deep, total);
            for (final JavascriptProcedure javascriptProcedure : javascriptProcedures) {
                // future
                futures.add(CompletableFuture.runAsync(() -> {
                    final Block parallelList = (Block) javascriptProcedure.getNodes();
                    final List<Token> tokens = parallelList.tokens();
                    final JavascriptConfig javascriptConfig = new JavascriptConfig() {
                        /** {@inheritDoc} */
                        @Override
                        public List<Token> filter(final List<Token> tokens) {
                            return tokens;
                        }
                    };
                    final List<JavascriptNode> javascriptNodes = ApiParser.parse(UnitParser.javascriptNodes(javascriptConfig),
                                                                                 tokens,
                                                                                 /** Field. */
                                                                                 javascriptConfig);
                    parallelList.addAll(javascriptNodes);
                    JavascriptUtil.setAstParent(javascriptProcedure);
                    for (final JavascriptNode javascriptNode : javascriptNodes) {
                        newJavascriptProcedures.addAll(filterJavascriptProcedure(javascriptNode));
                    }
                }));
            }
            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                             .join();
            subParsing(newJavascriptProcedures, deep + 1);
        }
    }
    
}
