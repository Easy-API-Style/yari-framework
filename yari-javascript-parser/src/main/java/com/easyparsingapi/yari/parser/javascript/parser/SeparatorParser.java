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

import static com.easyparsingapi.yari.parsec.Parsers.EOF;
import static com.easyparsingapi.yari.parsec.Parsers.or;
import static com.easyparsingapi.yari.parsec.Parsers.parseIf;
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.fragment;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;
import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.arrowStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.statement;
import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.statementBlock;

import java.util.List;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.pattern.Patterns;
import com.easyparsingapi.yari.parser.javascript.ast.BlockProcedure;
import com.easyparsingapi.yari.parser.javascript.ast.Catch;
import com.easyparsingapi.yari.parser.javascript.ast.CatchInstance;
import com.easyparsingapi.yari.parser.javascript.ast.Finally;
import com.easyparsingapi.yari.parser.javascript.ast.Infix;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.Operator;
import com.easyparsingapi.yari.parser.javascript.ast.StaticProcedure;
import com.easyparsingapi.yari.parser.javascript.ast.Try;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptTag;

/**
 * Package-private static utility class providing parsers for JavaScript statement separators
 * (semicolons, return carriages), block delimiters, and structured constructs such as
 * {@code try/catch/finally} and {@code static { }}.
 */
class SeparatorParser {

    /** Not instantiable — all methods are static. */
    private SeparatorParser() {}
    
    static Parser<?> returnCarriage() {
        return fragment(JavascriptTag.RETURN_CARRIAGE);
    }

    static Parser<?> strictEndStatement() {
        return or(token(";"),
                  returnCarriage().many())
                .label("strictEndStatement");
    }

    static Parser<?> endStatement() {
        return or(token(";"),
                  returnCarriage().many(),
                  EOF)
                .optional()
                .label("endStatement");
    }
    
    static Parser<List<JavascriptNode>> procedure(final JavascriptConfig config) {
        return statementBlock(config).label("procedure");
    }
    
    static Parser<List<JavascriptNode>> procedureOrStatement(final JavascriptConfig config) {
       return or(statementBlock(config),
                 statement(config).followedBy(endStatement()).map(List::of))
               .label("procedureOrStatement");
    }

    static Parser<List<JavascriptNode>> arrowProcedureOrStatement(final JavascriptConfig config) {
       return or(statementBlock(config),
                 arrowStatement(config).followedBy(endStatement()).map(List::of))
               .label("procedureOrStatement");
    }
    
    static Parser<StaticProcedure> staticProcedure(final JavascriptConfig config) {
        return token("static").next(procedure(config).map(StaticProcedure.Procedure::new)) 
                              .map(StaticProcedure::new)
                              .label("staticProcedure");
    }

    static Parser<BlockProcedure> blockProcedure(final JavascriptConfig config) {
        return statementBlock(config)
                   .map(BlockProcedure::new)
                   .label("blockProcedure");
    }
    
    static Parser<Catch.Signature> catchSignature(final JavascriptConfig config) {
        final Parser<CatchInstance> catchInstanceParser = 
           parseIf(c -> config.sequenceService().lookFor(c, Patterns.string("if")), 
                   sequence(identifier(),
                            sequence(token("if").next(identifier()),
                                     token("instanceof").map(v -> Operator.symbol("instanceof")),
                                     identifier(),
                                     (error, opeartor, typeError) -> new Infix(error, opeartor, typeError)),
                               CatchInstance::new));
        return or(catchInstanceParser, 
                  identifier())
                .between(JavascriptError::newInstance, 
                         token("("), 
                         token(")"))
                .map(Catch.Signature::new)
                .label("catchSignature");
    }
    
    static Parser<Try> tryCatchFinally(final JavascriptConfig config) {
        return sequence(token("try").next(procedure(config).map(Try.Procedure::new)),
                        sequence(token("catch").next(catchSignature(config).optional()),
                                 procedure(config).map(Catch.Procedure::new),
                                 Catch::new).many(),
                        token("finally").next(procedure(config).map(Finally.Procedure::new)).map(Finally::new).optional(),
                        Try::new)
                 .label("tryCatchFinally");
    }
    
}
