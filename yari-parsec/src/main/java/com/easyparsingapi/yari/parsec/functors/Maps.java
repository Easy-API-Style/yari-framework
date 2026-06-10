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
package com.easyparsingapi.yari.parsec.functors;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Provides common implementations of {@link Map} interface and the variants.
 */
public final class Maps {

    /**
     * The {@link UnaryOperator} that maps a {@link String} to lower case using {@link Locale#US}.
     */
    public static UnaryOperator<String> TO_LOWER_CASE = toLowerCase(Locale.US);

    /**
     * Returns a {@link UnaryOperator} that maps a {@link String} to lower case using {@code locale}.
     *
     * @param locale the locale to use for the conversion
     * @return a {@link UnaryOperator} that converts strings to lower case
     */
    public static UnaryOperator<String> toLowerCase(final Locale locale) {
        return new UnaryOperator<String>() {
            @Override
            public String apply(String s) {
                return s.toLowerCase(locale);
            }

            @Override
            public String toString() {
                return "toLowerCase";
            }
        };
    }

    /**
     * The {@link UnaryOperator} that maps a {@link String} to upper case using {@link Locale#US}.
     */
    public static UnaryOperator<String> TO_UPPER_CASE = toUpperCase(Locale.US);

    /**
     * Returns a {@link UnaryOperator} that maps a {@link String} to upper case using {@code locale}.
     *
     * @param locale the locale to use for the conversion
     * @return a {@link UnaryOperator} that converts strings to upper case
     */
    public static UnaryOperator<String> toUpperCase(Locale locale) {
        return new UnaryOperator<String>() {
            @Override
            public String apply(String s) {
                return s.toUpperCase(locale);
            }

            @Override
            public String toString() {
                return "toUpperCase";
            }
        };
    }

    /**
     * Returns a function that maps the string representation of an enum to its
     * corresponding enum value by calling {@link Enum#valueOf(Class, String)}.
     *
     * @param <E>      the enum type
     * @param enumType the class of the enum
     * @return a function that converts a string to its matching enum constant
     */
    public static <E extends Enum<E>> Function<String, E> toEnum(Class<E> enumType) {
        return new Function<String, E>() {
            @Override
            public E apply(String name) {
                return Enum.valueOf(enumType, name);
            }

            @Override
            public String toString() {
                return "-> " + enumType.getName();
            }
        };
    }

    private Maps() {
    }
    
}
