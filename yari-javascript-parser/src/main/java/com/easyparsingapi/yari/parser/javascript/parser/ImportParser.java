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
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.expression;
import static com.easyparsingapi.yari.parser.javascript.parser.LiteralParser.string;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.phrase;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;

import java.util.Map;
import java.util.Map.Entry;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parser.javascript.ast.Identifier;
import com.easyparsingapi.yari.parser.javascript.ast.Import;
import com.easyparsingapi.yari.parser.javascript.ast.ImportFunctionCall;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.Import.ImportBlock;
import com.easyparsingapi.yari.parser.javascript.ast.Import.ImportReference;

/**
 * Package-private static utility class providing parsers for JavaScript {@code import}
 * declarations and the dynamic {@code import()} function call.
 */
class ImportParser {

    /** Not instantiable — all methods are static. */
    private ImportParser() {}

    static Parser<Import> javascriptImport(final JavascriptConfig config) {
        final Parser<Import> importFromParser =
            sequence(token("import").next(or(parseIf(c -> config.sequenceService().curlingBracket(c),
                                                     sequence(or(identifier(), token("default").map(v -> new Identifier(v))),
                                                              token("as").next(or(identifier(), token("default").map(v -> new Identifier(v))))
                                                                         .optional(),
                                                              ImportReference::new)
                                                        .sepBy(token(","))
                                                        .between(token("{"), token("}"))
                                                        .map(ImportBlock::new)), 
                                             phrase("*", "as")
                                                .next(identifier())
                                                .map(v -> Map.entry("FULL", v)),
                                             identifier()
                                                .map(v -> Map.entry("DEFAULT", v)))
                                           .sepBy(token(","))),
                     token("from").next(string()),
                     (importList, moduleName) -> {
                         Identifier defaultExportName = null;
                         Identifier fullExportName = null;
                         ImportBlock exportBlock = null;
                         for (final Object value : importList) {
                             if (value instanceof ImportBlock) {
                                 exportBlock = (ImportBlock) value;
                             }
                             else if (value instanceof Entry<?, ?> entry) {
                                 final String key = (String) entry.getKey();
                                 if ("FULL".equals(key)) {
                                     fullExportName = (Identifier) entry.getValue();
                                 }
                                 else if ("DEFAULT".equals(key)) {
                                     defaultExportName = (Identifier) entry.getValue();
                                 }
                             }
                         }
                         return new Import(moduleName, defaultExportName, fullExportName, exportBlock);
                     });
        final Parser<Import> importParser = token("import")
                                              .next(string())
                                              .map(v -> new Import(v, null, null, null));
        return or(importFromParser, importParser).label("javascriptImport");
    }

    static Parser<ImportFunctionCall> importFunctionCall(final JavascriptConfig config) {
        return token("import").next(parseIf(c -> config.sequenceService().parenthesis(c), 
                                            config.parser(expression)
                                                  .between(JavascriptError::newInstance, 
                                                           token("("), 
                                                           token(")"))
                                                  .map(ImportFunctionCall.Signature::new)))
                              .map(ImportFunctionCall::new)
                              .label("importFunctionCall");
    }
    
}
