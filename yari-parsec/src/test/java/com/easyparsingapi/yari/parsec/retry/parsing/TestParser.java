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
package com.easyparsingapi.yari.parsec.retry.parsing;

import java.util.List;
import java.util.Set;

import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Scanners;
import com.easyparsingapi.yari.parsec.Terminals;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.Tokens.Tag;
import com.easyparsingapi.yari.parsec.error.ParseErrorDetail;
import com.easyparsingapi.yari.parsec.functors.Map3;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.pattern.Patterns;
import com.easyparsingapi.yari.parsec.util.ast.Ast;
import com.easyparsingapi.yari.parsec.util.ast.Error;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil.Result;

public class TestParser {
    
    static Parser<Void> delimiter = Scanners.WHITESPACES.skipMany();

    static Set<String> operator = Set.of("(", ")", ";", ",");
    
    static Parser<Fragment> comment = Scanners.lineComment("//")
                                              .source()
                                              .map(v -> Tokens.fragment(v, Tag.COMMENT));
    
    static Terminals terminals = Terminals.operators(operator)
                                          .words(Patterns.WORD
                                                         .notContain(operator)
                                                         .toScanner("WORD")
                                                         .source())
                                          .build();
    
    static Parser<?> tokenizer = Parsers.or(Scanners.INTEGER.map(v -> Tokens.fragment(v, "INTEGER")), 
                                            terminals.tokenizer());
    
    static Parser<List<Token>> lexer = ApiParser.lexer(tokenizer, delimiter);
    
    static Parser<Ast> id = Terminals.identifier()
                                     .map(Identifier::new)
                                     .label("identifier")
                                     .cast();
    
    static Parser<Ast> num = Terminals.fragment("INTEGER")
                                      .map(Number::new)
                                      .label("number")
                                      .cast();
    
    static Parser<Ast> token(String token) {
        return Terminals.RESERVED
                        .acceptIf(v -> token.equals(v))
                        .label("token[" + token + "]")
                        .map(Identifier::new)
                        .label("token[" + token + "]")
                        .cast();
    }
    
    static Parser<Ast> identifier(String identifier) {
        return Terminals.identifier()
                        .acceptIf(v -> identifier.equals(v))
                        .label("identifier[" + identifier + "]")
                        .map(Identifier::new)
                        .label("identifier[" + identifier + "]")
                        .cast();
    }

    static Map3<ParseErrorDetail, SourceLocation, List<Token>, Error>  error = (d, l, t) -> {
        return new Error(d.getFailureMessage(), t, l);
    };
    
    static <V>  Result<V> parse(Parser<V> parser, String source) {
        List<Token> tokens = ApiParser.lex(lexer, source);
        V ast = ApiParser.parse(parser, lexer, source);
        return new Result<>(tokens, ast);
    }
    
}
