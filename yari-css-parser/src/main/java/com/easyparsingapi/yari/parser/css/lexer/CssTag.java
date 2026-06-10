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
package com.easyparsingapi.yari.parser.css.lexer;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;

/**
 * Enumeration of CSS lexer token tags used to classify the different kinds of
 * tokens produced during CSS lexical analysis.
 */
public enum CssTag {

    /** A string literal delimited by single-quote characters (e.g. {@code 'hello'}). */
    SINGLE_QUOTE_STRING,

    /** A string literal delimited by double-quote characters (e.g. {@code "hello"}). */
    DOUBLE_QUOTE_STRING,

    /** A hexadecimal numeric literal (e.g. {@code #ff0000}). */
    HEXADECIMAL,

    /** A decimal (floating-point) numeric literal (e.g. {@code 1.5}). */
    DECIMAL,

    /** An integer numeric literal (e.g. {@code 42}). */
    INTEGER,

    /** A line terminator, either {@code \n} (LF) or the {@code \r\n} (CRLF) sequence. */
    RETURN_CARRIAGE,

    /** A CSS operator or punctuation token (e.g. {@code :}, {@code ;}, {@code ~=}). */
    OPERATOR,

    /**
     * An identifier or word token, covering CSS identifiers, property names,
     * numeric values with units, URL content, and Unicode range notations.
     */
    WORD,

    /** A block comment delimited by {@code /*} and {@code *}{@code /}. */
    BLOCK_COMMENT;

    /**
     * Extracts the {@link CssTag} associated with the given token, or {@code null}
     * if the token's value is not a {@link Fragment} carrying a {@code CssTag}.
     *
     * @param token the token whose tag is to be retrieved
     * @return the {@code CssTag} of the token, or {@code null} if none is present
     */
    public static CssTag tag(final Token token) {
        CssTag result = null;
        final Object value = token.value();
        if (value instanceof Fragment) {
            final Fragment fragment = (Fragment) value;
            result = (CssTag) fragment.tag();
        }
        return result;
    }

    /**
     * Converts a collection of {@link CssTag} values to their string representations
     * and returns them as a {@link Set}.
     *
     * @param cssTags the collection of CSS tags to convert
     * @return a set of string representations of the given CSS tags
     */
    public static Set<String> toString(final Collection<CssTag> cssTags) {
        return cssTags.stream().map(CssTag::toString).collect(Collectors.toSet());
    }

}
