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
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parser.css.parser.AtomicPattern.DECIMAL;
import static com.easyparsingapi.yari.parser.css.parser.AtomicPattern.HEXADECIMAL;
import static com.easyparsingapi.yari.parser.css.parser.AtomicPattern.INTEGER;
import static com.easyparsingapi.yari.parser.css.parser.AtomicPattern.isValid;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.fragment;
import static com.easyparsingapi.yari.parser.css.parser.TermParser.token;

import java.util.Set;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.css.ast.Identifier;
import com.easyparsingapi.yari.parser.css.ast.Important;
import com.easyparsingapi.yari.parser.css.ast.Literal;
import com.easyparsingapi.yari.parser.css.ast.Nesting;
import com.easyparsingapi.yari.parser.css.ast.Universal;
import com.easyparsingapi.yari.parser.css.lexer.CssTag;

/**
 * Provides atomic CSS parsers for elementary values such as identifiers,
 * literals (strings, integers, decimals, hexadecimals), the universal selector,
 * the {@code !important} rule, and the nesting selector.
 */
public class AtomicParser {

    /** Not instantiable — all methods are static. */
    private AtomicParser() {}


    static Parser<CssNode> atomic() {
        return or(identifier(),
                  anyLiteral())
                .label("atomic");
    }

    static Parser<Literal> anyLiteral() {
        return or(singleQuoteString(),
                  doubleQuoteString(),
                  hexadecimal(),
                  integer(),
                  decimal())
                .label("anyLiteral");
    }

    static Parser<Literal> singleQuoteString() {
        return fragment(CssTag.SINGLE_QUOTE_STRING)
                  .map(v -> new Literal(Literal.Type.singleQuoteString, v.toString()))
                  .label("singleQuoteString");
    }

    static Parser<Literal> doubleQuoteString() {
        return fragment(CssTag.DOUBLE_QUOTE_STRING)
                   .map(v -> new Literal(Literal.Type.doubleQuoteString, v.toString()))
                   .label("doubleQuoteString");
    }

    static Parser<Literal> integer() {
        return fragment(CssTag.WORD)
                 .acceptIf(v -> isValid(v.toString(), INTEGER))
                 .map(value -> new Literal(Literal.Type.integer, value.toString()))
                 .label("integer");
    }

    static Parser<Literal> decimal() {
        return fragment(CssTag.DECIMAL)
                .acceptIf(v -> isValid(v.toString(), DECIMAL))
                .map(value -> new Literal(Literal.Type.decimal, value.toString()))
                .label("decimal");
    }

    static Parser<Literal> hexadecimal() {
        return sequence(token("#"),
                        fragment(CssTag.WORD).acceptIf(v -> isValid(v.toString(), HEXADECIMAL)))
                 .map(value -> new Literal(Literal.Type.hexadecimal, value.toString()))
                 .label("hexadecimal");
    }

    static Parser<Identifier> identifier() {
        return fragment(CssTag.WORD)
                  .map(v -> new Identifier(v.toString()))
                  .label("identifier");
    }

    static Parser<Identifier> identifier(final String... words) {
        return token(Set.of(words), CssTag.WORD, true)
                  .map(Token::toString)
                  .map(v -> new Identifier(v))
                  .label("identifier[" + String.join(", ", words) + "]");
    }

    /*
     *
     *
     *
     */
    @SuppressWarnings("unused")
    static Parser<Universal> universal() {
        return token("*").map(v -> new Universal())
                         .label("universal");
    }

    @SuppressWarnings("unused")
    static Parser<Important> important() {
        return sequence(token("!"), identifier("important"))
                  .map(v -> new Important())
                  .label("important");

    }

    @SuppressWarnings("unused")
    static Parser<Nesting> nesting() {
        return token("&").map(v -> new Nesting())
                         .label("nesting");
    }

}
