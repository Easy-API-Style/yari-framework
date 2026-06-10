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

import static com.easyparsingapi.yari.parsec.Parsers.or;
import static com.easyparsingapi.yari.parsec.Parsers.parseIf;
import static com.easyparsingapi.yari.parser.javascript.lexer.JavascriptLexer.KEYWORDS;
import static com.easyparsingapi.yari.parser.javascript.lexer.JavascriptLexer.startingStatementKeywords;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.awaitStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.blockProcedure;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.breakStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.commaSequence;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.continueStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.doWhileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.emptyStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.expression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.forStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.ifStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.javascriptExport;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.javascriptImport;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.labelStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.returnStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.switchStatment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.throwStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.tryCatchFinally;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.useStrict;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.variableDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.whileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.yield;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.endStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.strictEndStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Parsers.TokenContext;
import com.easyparsingapi.yari.parser.javascript.ast.Identifier;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.Literal;
import com.easyparsingapi.yari.parser.javascript.ast.Null;
import com.easyparsingapi.yari.parser.javascript.ast.ObjectDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.This;
import com.easyparsingapi.yari.parser.javascript.ast.Yield;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;
import com.easyparsingapi.yari.parser.javascript.parser.SequenceService.Sequence;
import com.easyparsingapi.yari.parser.javascript.parser.SequenceService.Type;

/**
 * Package-private static utility class that assembles top-level unit parsers for complete
 * JavaScript programs or statement sequences from the configured sub-parsers.
 */
class UnitParser {

    /** Not instantiable — all methods are static. */
    private UnitParser() {}


    
//    private static final Logger LOGGER = LoggerFactory.getLogger(UnitParser.class);
    
    static final Set<String> notNames = 
        Set.of("null", "undefined");
    
    static final String[] propertyNames = 
        KEYWORDS.stream()
                .filter(v -> !notNames.contains(v))
                .collect(Collectors.toSet())
                .toArray(new String[0]);   

    static final Set<String> notFunctionNames =
        Set.of("null", "undefined", "default", "function");
    
    static final String[] functionNames = 
        KEYWORDS.stream()
                .filter(v -> !notFunctionNames.contains(v))
                .collect(Collectors.toSet())
                .toArray(new String[0]);   
    
    static final Set<String> functionNamesAsSet = 
        Set.of(functionNames);

    /*
     * 
     * STATEMENT
     * 
     */
    public static Parser<JavascriptNode> statementableExpression(final JavascriptConfig config) {
        return config.parser(expression)
                     .acceptIf(v -> !(v instanceof Identifier
                                         || v instanceof Literal
                                         || v instanceof Null
                                         || v instanceof This
                                         || v instanceof Yield
                                         || v instanceof ObjectDeclaration));
    }
    
    public static Parser<JavascriptNode> statement(final JavascriptConfig config) {
        return or(config.parser(useStrict),
                  config.parser(emptyStatement),
                  config.parser(variableDeclaration),
                  config.parser(awaitStatement),
                  config.parser(labelStatement),
                  config.parser(javascriptImport),
                  config.parser(javascriptExport),
                  config.parser(yield),
                  config.parser(returnStatement),
                  config.parser(switchStatment),
                  config.parser(tryCatchFinally),
                  config.parser(throwStatement),
                  config.parser(doWhileStatement),
                  config.parser(whileStatement),
                  config.parser(forStatement),
                  config.parser(continueStatement),
                  config.parser(breakStatement),
                  config.parser(ifStatement),
                  config.parser(commaSequence),
                  config.parser(assignment),
                  config.parser(blockProcedure),
                  statementableExpression(config));
    }

    public static Parser<JavascriptNode> arrowStatement(final JavascriptConfig config) {
        return or(config.parser(useStrict),
                  config.parser(emptyStatement),
                  config.parser(variableDeclaration),
                  config.parser(awaitStatement),
                  config.parser(labelStatement),
                  config.parser(javascriptImport),
                  config.parser(javascriptExport),
                  config.parser(yield),
                  config.parser(returnStatement),
                  config.parser(switchStatment),
                  config.parser(tryCatchFinally),
                  config.parser(throwStatement),
                  config.parser(doWhileStatement),
                  config.parser(whileStatement),
                  config.parser(forStatement),
                  config.parser(continueStatement),
                  config.parser(breakStatement),
                  config.parser(ifStatement),
                  config.parser(assignment),
                  config.parser(blockProcedure),
                  config.parser(expression));
    }
    
    static Parser<List<JavascriptNode>> javascriptNodes(final JavascriptConfig config) {
        return statement(config)
                   .followedBy(endStatement())
                   .manyUntilEof(JavascriptError::newInstance, 
                                 or(strictEndStatement(), 
                                    token(startingStatementKeywords()).peek()))
                   .label("javascriptNodes");
    }
    
    static Parser<List<JavascriptNode>> statementBlock(final JavascriptConfig config) {
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
                                  List<JavascriptNode> result = List.of();
                                  if (tokens.size() > 2) {
                                      tokens.removeFirst();
                                      tokens.removeLast();
                                      result = new Block(new ArrayList<>(tokens));
                                  }
                                  return result;
                              }))
                .label("statementBlock");
    }
    
    /*
     * 
     * UNIT
     * 
     */
    public static Parser<Javascript> unit(final JavascriptConfig config, 
                                          final Set<Node> nodes) {
        final Set<Parser<JavascriptNode>> parsers = new HashSet<>();
        for (final Node node : nodes) {
            parsers.add(config.parser(node));
        }
        return Parsers.or(parsers)
                      .followedBy(endStatement())
                      .manyUntilEof(JavascriptError::newInstance, 
                                    or(strictEndStatement(), 
                                       token(startingStatementKeywords()).peek()))
                      .map(v -> new Javascript(v, config.getComments()))
                      .label("unit[" + String.join(", ", nodes.stream().map(Node::name).sorted().toList()) + "]");
    }
    
    public static Parser<Javascript> unit(final JavascriptConfig config) {
        return statement(config)
                   .followedBy(endStatement())
                   .manyUntilEof(JavascriptError::newInstance, 
                                 or(strictEndStatement(), 
                                    token(startingStatementKeywords()).peek()))
                   .map(v -> new Javascript(v, config.getComments()))
                   .label("unit");
    }
    
}
