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
package com.easyparsingapi.yari.parsec;

/**
 * Translates the recognized string literal to a {@link String}.
 */
final class StringLiteralsTranslator {

    private static final char escapedChar(char c) {
        switch (c) {
        case 'r':
            return '\r';
        case 'n':
            return '\n';
        case 't':
            return '\t';
        default:
            return c;
        }
    }

    /**
     * Converts a double-quoted string literal token into its Java {@link String} value,
     * interpreting backslash escape sequences (e.g. {@code \n}, {@code \t}, {@code \r}).
     *
     * @param text the raw token text, including the surrounding double-quote characters
     * @return the unescaped string content without the enclosing quotes
     */
    static String tokenizeDoubleQuote(String text) {
        final int end = text.length() - 1;
        final StringBuilder buf = new StringBuilder();
        for (int i = 1; i < end; i++) {
            char c = text.charAt(i);
            if (c != '\\') {
                buf.append(c);
            } else {
                char c1 = text.charAt(++i);
                buf.append(escapedChar(c1));
            }
        }
        return buf.toString();
    }

    /**
     * Converts a single-quoted string literal token into its Java {@link String} value,
     * treating doubled single-quote characters ({@code ''}) as an escaped single quote.
     *
     * @param text the raw token text, including the surrounding single-quote characters
     * @return the unescaped string content without the enclosing quotes
     */
    static String tokenizeSingleQuote(String text) {
        int end = text.length() - 1;
        StringBuilder buf = new StringBuilder();
        for (int i = 1; i < end; i++) {
            char c = text.charAt(i);
            if (c != '\'') {
                buf.append(c);
            } else {
                buf.append('\'');
                i++;
            }
        }
        return buf.toString();
    }

}
