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
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.keyword;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.blockProcedure;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.breakStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.classDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.commaSequence;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.continueStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.doWhileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.expression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.forStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.functionDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.ifStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.labelStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.returnStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.switchStatment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.throwStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.tryCatchFinally;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.useStrict;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.variableDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.whileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.yield;
import static com.easyparsingapi.yari.parser.javascript.parser.LiteralParser.string;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.phrase;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;

import java.util.List;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parser.javascript.ast.Export;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.Export.ExportBlock;

/**
 * Package-private static utility class providing parsers for JavaScript {@code export} declarations,
 * including named exports, default exports, re-exports ({@code export ... from}),
 * and wildcard re-exports ({@code export * from}).
 */
class ExportParser {

    /** Not instantiable — all methods are static. */
    private ExportParser() {}

    static Parser<Export> javascriptExport(final JavascriptConfig config) {
        return or(allFromExport(),
                  fromExport(config),
                  exportDefault(config),
                  export(config),
                  listExport(config))
                .label("javascriptExport");
    }
    
    private static Parser<JavascriptNode> defaultExportExpression(final JavascriptConfig config) {
        return or(config.parser(assignment),
                  config.parser(expression),
                  config.parser(commaSequence),
                  config.parser(variableDeclaration),
                  config.parser(useStrict),
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
                  config.parser(labelStatement),
                  config.parser(blockProcedure))
               .label("defaultExportExpression");
    }

    private static Parser<Export> exportDefault(final JavascriptConfig config) {
        return phrase("export", "default")
                  .next(defaultExportExpression(config))
                  .map(v -> new Export(true, v))
                  .label("exportDefault");
    }
    
    private static Parser<Export> export(final JavascriptConfig config) {
        return token("export").next(or(config.parser(variableDeclaration),
                                       config.parser(functionDeclaration),
                                       config.parser(classDeclaration)))
                              .map(v -> new Export(false, v))
                              .label("export");
    }
    
    private static Parser<List<Export.ExportReference>> exportReference(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService().curlingBracket(c),
                       sequence(or(identifier(), 
                                   keyword("default")),
                                token("as").next(or(identifier(), 
                                                    keyword("default")))
                                           .optional(),
                                Export.ExportReference::new)
                         .sepBy(token(","))
                         .between(token("{"), token("}")));
    }
    
    private static Parser<Export> listExport(final JavascriptConfig config) {
        return token("export")
                .next(exportReference(config).map(ExportBlock::new))
                .map(v -> new Export(false, v))
                .label("listExport");
    }
    
    private static Parser<Export> allFromExport() {
        return phrase("export", "*")
                 .next(sequence(token("as").next(identifier()).optional(),
                                token("from").next(string()),
                                (alias, module) -> new Export.AllFrom(module, alias)))
                 .map(v -> new Export(false, v))
                 .label("allFromExport");
    }
    
    private static Parser<Export> fromExport(final JavascriptConfig config) {
        return token("export")
                .next(sequence(exportReference(config).map(ExportBlock::new),
                               token("from").next(string()),
                               (exportBlock, module) -> new Export.From(module, exportBlock)))
                .map(v -> new Export(false, v))
                .label("fromExport");
    }
    
}
