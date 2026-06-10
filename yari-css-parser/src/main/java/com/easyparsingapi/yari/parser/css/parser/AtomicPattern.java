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

import static com.easyparsingapi.yari.parsec.pattern.Patterns.isChar;
import static com.easyparsingapi.yari.parsec.pattern.Patterns.many1;
import static com.easyparsingapi.yari.parsec.pattern.Patterns.or;
import static com.easyparsingapi.yari.parsec.pattern.Patterns.sequence;

import com.easyparsingapi.yari.parsec.pattern.CharPredicate;
import com.easyparsingapi.yari.parsec.pattern.Pattern;

/**
 * Provides atomic parsing patterns for common CSS token types such as integers,
 * decimal numbers, and hexadecimal values.
 */
public class AtomicPattern {

    /** Not instantiable — all methods are static. */
    private AtomicPattern() {}


    private static final CharPredicate IS_DIGIT = c -> Character.isDigit(c);

    private static final Pattern DECIMAL_STRICT =
            sequence(many1(IS_DIGIT),
                     isChar('.'),
                     many1(IS_DIGIT));

    private static final Pattern DECIMAL_EMPTY_RIGHT =
            sequence(many1(IS_DIGIT),
                     isChar('.'));

    private static final Pattern DECIMAL_EMPTY_LEFT =
            sequence(isChar('.'),
                     many1(IS_DIGIT));

    static final Pattern DECIMAL =
            or(DECIMAL_STRICT,
               DECIMAL_EMPTY_RIGHT,
               /** The field. */
               DECIMAL_EMPTY_LEFT);

    static final Pattern INTEGER = many1(IS_DIGIT);

    /**
     * Predicate that matches any character that is a valid hexadecimal digit
     * (0-9, a-f, A-F).
     */
    public static final CharPredicate IS_HEXADECIMAL = c -> Character.isDigit(c)
                                                         || c == 'A' || c == 'a'
                                                         || c == 'B' || c == 'b'
                                                         || c == 'C' || c == 'c'
                                                         || c == 'D' || c == 'd'
                                                         || c == 'E' || c == 'e'
                                                         || c == 'F' || c == 'f';

    static final Pattern HEXADECIMAL = many1(IS_HEXADECIMAL);

    static boolean isValid(final String value,
                           final Pattern pattern) {
        return pattern.match(value, 0, value.length()) > 0;
    }

}
