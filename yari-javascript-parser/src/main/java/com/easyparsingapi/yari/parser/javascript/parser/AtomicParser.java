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
import static com.easyparsingapi.yari.parser.javascript.parser.LiteralParser.anyLiteral;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.fragment;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;

import java.util.Set;

import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parser.javascript.ast.Identifier;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.NaN;
import com.easyparsingapi.yari.parser.javascript.ast.Null;
import com.easyparsingapi.yari.parser.javascript.ast.This;
import com.easyparsingapi.yari.parser.javascript.ast.Undefined;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptTag;

/**
 * Package-private static utility class providing parsers for atomic JavaScript expressions:
 * identifiers, keywords used as identifiers, and the {@code this} keyword.
 */
class AtomicParser {

    /** Not instantiable — all methods are static. */
    private AtomicParser() {}

    static Parser<JavascriptNode> atomic() {
        return or(anyLiteral(),
                  identifier(),
                  thisParser(), 
                  nullParser(),
                  undefined(),
                  NaN())
                .label("atomic");
    }
    
    static Parser<Identifier> identifier() {
        return fragment(JavascriptTag.WORD)
                  .map(v -> new Identifier(v.toString()))
                  .label("identifier");
    }
    
    static Parser<Identifier> identifier(final String... terms) {
        final Set<String> _terms = CollectionUtil.toSet(terms);
        return fragment(JavascriptTag.WORD)
                  .acceptIf(v -> _terms.contains(v.toString()))
                  .map(v -> new Identifier(v.toString()))
                  .label("identifier[" + String.join(", ", terms) + "]");
    }
    
    static Parser<Identifier> keyword(final String... keyword) {
        return token(keyword)
                 .map(v -> new Identifier(v.toString()))
                 .label("keyword");
    }
    
    static Parser<Identifier> keyword(final Set<String> keywords) {
        return token(keywords)
                  .map(v -> new Identifier(v))
                  .label("keyword");
    }
    
    @SuppressWarnings("unused")
    static Parser<This> thisParser() {
        return keyword("this").map(v -> new This());
    }
    
    @SuppressWarnings("unused")
    static Parser<Null> nullParser() {
        return keyword("null").map(v -> new Null());
    }
    
    @SuppressWarnings("unused")
    static Parser<Undefined> undefined() {
        return keyword("undefined").map(v -> new Undefined());
    }
    
    @SuppressWarnings("unused")
    static Parser<NaN> NaN() {
        return keyword("NaN").map(v -> new NaN());
    }
    
}
