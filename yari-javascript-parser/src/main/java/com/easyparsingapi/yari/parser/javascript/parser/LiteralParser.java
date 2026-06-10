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
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.fragment;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.templateToken;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parser.javascript.ast.BigDecimal;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.Literal;
import com.easyparsingapi.yari.parser.javascript.ast.LiteralTemplate;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptTag;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;

/**
 * Package-private static utility class providing parsers for JavaScript literal values:
 * numbers (integer, decimal, hexadecimal, binary, octal, exponential, big integer),
 * booleans, strings (single-quote, double-quote, and HTML-entity variants),
 * regular expressions, and template literals.
 */
class LiteralParser {

    /** Not instantiable — all methods are static. */
    private LiteralParser() {}



    static Parser<JavascriptNode> anyLiteral() {
        return Parsers.or(trueOrFalse(),
                          singleQuoteString(),
                          singleQuoteStringEntityName(),
                          singleQuoteStringNumberCode(),
                          doubleQuoteString(),
                          doubleQuoteStringEntityName(),
                          doubleQuoteStringNumberCode(),
                          regExpString(),
                          bigInteger(),
                          base_10(),
                          octal(),
                          hexadecimal(),
                          binary(),
                          exponential(),
                          integer(),
                          decimal())
                      .label("anyLiteral");
    }
    
    static Parser<Literal> trueOrFalse() {
        return Parsers.or(token("true") , 
                          token("false"))
                      .map(value -> new Literal(Literal.Type.trueOrFalse, value))
                      .label("trueOrFalse");
    }
    
    static Parser<Literal> integer() {
        return fragment(JavascriptTag.INTEGER)
                 .map(value -> new Literal(Literal.Type.integer, value.toString()))
                 .label("integer");
    }
    
    static Parser<Literal> base_10() {
        return fragment(JavascriptTag.BASE_10)
                .map(value -> new Literal(Literal.Type.base_10, value.toString()))
                .label("base_10");
    }
    
    static Parser<Literal> hexadecimal() {
        return fragment(JavascriptTag.HEXADECIMAL)
                .map(value -> new Literal(Literal.Type.hexadecimal, value.toString()))
                .label("hexadecimal");
    }

    static Parser<Literal> binary() {
        return fragment(JavascriptTag.BINARY)
                .map(value -> new Literal(Literal.Type.binary, value.toString()))
                .label("binary");
    }
    
    static Parser<Literal> octal() {
        return fragment(JavascriptTag.OCTAL)
                .map(value -> new Literal(Literal.Type.octal, value.toString()))
                .label("octal");
    }

    static Parser<BigDecimal> bigInteger() {
        return or(base_10(),
                  octal(),
                  hexadecimal(),
                  binary(),
                  integer())
                .followedBy(identifier("n"))
                .map(BigDecimal::new)
                .label("bigInteger");
    }
    
    static Parser<Literal> exponential() {
        return fragment(JavascriptTag.EXPONENTIAL)
                .map(value -> new Literal(Literal.Type.exponential, value.toString()))
                .label("exponential");
    }
    
    static Parser<Literal> decimal() {
        return fragment(JavascriptTag.DECIMAL)
                .map(value -> new Literal(Literal.Type.decimal, value.toString()))
                .label("decimal");
    }
    
    private static String pad(final String value, String paddingString) {
        final int paddingLength = paddingString.length();
        return value.substring(paddingLength, value.length() - paddingLength);
    }
    
    static Parser<Literal> singleQuoteString() {
        return fragment(JavascriptTag.SINGLE_QUOTE_STRING)
                  .map(v -> new Literal(Literal.Type.singleQuoteString, pad(v.toString(), "'")))
                  .label("singleQuoteString");
    }
    
    static Parser<Literal> singleQuoteStringEntityName() {
        return fragment(JavascriptTag.SINGLE_QUOTE_STRING_ENTITY_NAME)
                  .map(v -> new Literal(Literal.Type.singleQuoteStringEntityName, pad(v.toString(), "&apos;")))
                  .label("singleQuoteStringEntityName");
    }
    
    static Parser<Literal> singleQuoteStringNumberCode() {
        return fragment(JavascriptTag.SINGLE_QUOTE_STRING_NUMBER_CODE)
                  .map(v -> new Literal(Literal.Type.singleQuoteStringNumberCode, pad(v.toString(), "&#39;")))
                  .label("singleQuoteStringNumberCode");
    }
    
    static Parser<Literal> doubleQuoteString() {
        return fragment(JavascriptTag.DOUBLE_QUOTE_STRING)
                  .map(v -> new Literal(Literal.Type.doubleQuoteString, pad(v.toString(), "\"")))
                  .label("doubleQuoteString");
    }
    
    static Parser<Literal> doubleQuoteStringEntityName() {
        return fragment(JavascriptTag.DOUBLE_QUOTE_STRING_ENTITY_NAME)
                  .map(v -> new Literal(Literal.Type.doubleQuoteStringEntityName, pad(v.toString(), "&quot;")))
                  .label("doubleQuoteStringEntityName");
    }
    
    static Parser<Literal> doubleQuoteStringNumberCode() {
        return fragment(JavascriptTag.DOUBLE_QUOTE_STRING_NUMBER_CODE)
                  .map(v -> new Literal(Literal.Type.doubleQuoteStringNumberCode, pad(v.toString(), "&#34;")))
                  .label("doubleQuoteStringNumberCode");
    }
    
    static Parser<Literal> regExpString() {
        return fragment(JavascriptTag.REGULAR_EXPRESSION)
                  .map(v -> new Literal(Literal.Type.regExpString, v.toString()))
                  .label("regExpString");
    }
    
    static Parser<Literal> string() {
        return Parsers.or(LiteralParser.singleQuoteString(),
                          LiteralParser.doubleQuoteString())
                      .label("string");
    }
    
    /*
     * 
     * TEMPLATE
     * 
     */
    @SuppressWarnings("unused")
    static Parser<LiteralTemplate> literalTemplate(final JavascriptConfig config) {
        return parseIf(c -> true,
                       or(fragment(JavascriptTag.LITERAL_TEMPLATE_ELEMENT)
                                    .map(Token::toString)
                                    .map(LiteralTemplate.Constant::new),
                          config.parser(Node.expression)
                                .between(JavascriptError::newInstance, 
                                         templateToken("${"), 
                                         templateToken("}"))
                                .map(LiteralTemplate.Variable::new))
                        .manyBetween(JavascriptError::newInstance, 
                                     fragment(JavascriptTag.LITERAL_TEMPLATE_ELEMENT).peek(), 
                                     templateToken("`"), 
                                     templateToken("`"))
                        .map(LiteralTemplate::new))
                .label("literalTemplate");
    }
    
}
