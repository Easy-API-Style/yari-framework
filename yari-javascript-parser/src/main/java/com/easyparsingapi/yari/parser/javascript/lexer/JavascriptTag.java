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
package com.easyparsingapi.yari.parser.javascript.lexer;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;

/**
 * Enumeration of all token tag constants recognised by the JavaScript lexer.
 * Each constant identifies a specific syntactic category that a
 * {@link com.easyparsingapi.yari.parsec.Tokens.Fragment} may belong to.
 */
public enum JavascriptTag {

    /** Single-quoted string literal using the {@code '} character. */
    SINGLE_QUOTE_STRING,
    /** Single-quoted string literal encoded as an HTML entity name ({@code &apos;}). */
    SINGLE_QUOTE_STRING_ENTITY_NAME,
    /** Single-quoted string literal encoded as an HTML numeric character reference ({@code &#39;}). */
    SINGLE_QUOTE_STRING_NUMBER_CODE,
    /** Double-quoted string literal using the {@code "} character. */
    DOUBLE_QUOTE_STRING,
    /** Double-quoted string literal encoded as an HTML entity name ({@code &quot;}). */
    DOUBLE_QUOTE_STRING_ENTITY_NAME,
    /** Double-quoted string literal encoded as an HTML numeric character reference ({@code &#34;}). */
    DOUBLE_QUOTE_STRING_NUMBER_CODE,

    /** Regular expression literal (e.g. {@code /pattern/flags}). */
    REGULAR_EXPRESSION,

    /** Back-tick template literal (the whole raw token, before inner expansion). */
    LITERAL_TEMPLATE,
    /** Keyword token found inside a template literal expression ({@code ${...}}). */
    LITERAL_TEMPLATE_KEYWORD,
    /** Static text segment of a template literal. */
    LITERAL_TEMPLATE_ELEMENT,
    /** Expression variable segment of a template literal ({@code ${expr}}). */
    LITERAL_TEMPLATE_VARIABLE,

    /** Decimal integer literal. */
    INTEGER,
    /** Base-10 numeric literal with a leading zero(s). */
    BASE_10,
    /** Hexadecimal numeric literal (prefix {@code 0x} or {@code 0X}). */
    HEXADECIMAL,
    /** Binary numeric literal (prefix {@code 0b} or {@code 0B}). */
    BINARY,
    /** Octal numeric literal (prefix {@code 0o} or {@code 0O}). */
    OCTAL,
    /** Exponential (scientific-notation) numeric literal. */
    EXPONENTIAL,
    /** Decimal (floating-point) numeric literal. */
    DECIMAL,
    /** Reserved JavaScript keyword token. */
    KEYWORD,
    /** Identifier or non-reserved word token. */
    WORD,

    /** Carriage-return or CRLF line-ending token. */
    RETURN_CARRIAGE,

    /** Single-line comment ({@code // ...}). */
    LINE_COMMENT,
    /** Block comment ({@code /* ... *&#47;}). */
    BLOCK_COMMENT;

    /**
     * Extracts the {@code JavascriptTag} of the given token, or {@code null} if the token
     * does not carry a {@link com.easyparsingapi.yari.parsec.Tokens.Fragment} value.
     *
     * @param token the token whose tag should be extracted
     * @return the {@code JavascriptTag} of the token, or {@code null}
     */
    public static JavascriptTag tag(final Token token) {
        JavascriptTag result = null;
        if (token != null) {
            final Object value = token.value();
            if (value instanceof Fragment) {
                final Fragment fragment = (Fragment) value;
                result = (JavascriptTag) fragment.tag();
            }
        }
        return result;
    }
    
    /**
     * Converts a collection of {@code JavascriptTag} values to a set of their string names.
     *
     * @param javascriptTags the collection of tags to convert
     * @return a {@link Set} containing the {@link #toString()} representation of each tag
     */
    public static Set<String> toString(final Collection<JavascriptTag> javascriptTags) {
        return javascriptTags.stream().map(JavascriptTag::toString).collect(Collectors.toSet());
    }
    
}
