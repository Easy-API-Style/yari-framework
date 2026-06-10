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
package com.easyparsingapi.yari.parsec.internal.util;

import static java.util.Arrays.asList;

/**
 * Internal utility for {@link String} operation.
 */
public final class StringUtil {

    /** Not instantiable — all methods are static. */
    private StringUtil() {
    }

    /**
     * Joins {@code objects} with {@code delim} as the delimiter.
     *
     * @param delim   the delimiter string inserted between consecutive elements
     * @param objects the array of objects whose string representations are joined
     * @return the joined string, or an empty string if {@code objects} is empty
     */
    public static String join(String delim,
                              Object[] objects) {
        // Do not use varargs to prevent some silly compiler warnings.
        if (objects.length == 0) {
            return "";
        }
        return join(new StringBuilder(), delim, objects).toString();
    }

    /**
     * Joins {@code objects} with {@code delim} as the delimiter,
     * appending the result to {@code builder}.
     *
     * @param builder the {@link StringBuilder} to append the joined text to
     * @param delim   the delimiter string inserted between consecutive elements
     * @param objects the array of objects whose string representations are joined
     * @return {@code builder} with the joined text appended
     */
    public static StringBuilder join(StringBuilder builder,
                                     String delim,
                                     Object[] objects) {
        return join(builder, delim, asList(objects));
    }

    /**
     * Joins {@code objects} with {@code delim} as the delimiter,
     * appending the result to {@code builder}.
     *
     * @param builder the {@link StringBuilder} to append the joined text to
     * @param delim   the delimiter string inserted between consecutive elements
     * @param objects the iterable of objects whose string representations are joined
     * @return {@code builder} with the joined text appended
     */
    public static StringBuilder join(StringBuilder builder,
                                     String delim,
                                     Iterable<?> objects) {
        int i = 0;
        for (Object obj : objects) {
            if (i++ > 0) {
                builder.append(delim);
            }
            builder.append(obj);
        }
        return builder;
    }

    /**
     * Replaces all line separators ({@code \r} and {@code \n}) in {@code value} with spaces,
     * and collapses consecutive whitespace into a single space.
     *
     * @param value the character sequence to process, or {@code null}
     * @return the processed string with line separators replaced by spaces, or {@code null} if {@code value} is {@code null}
     */
    public static String replaceLineSeparatorBySpace(final CharSequence value) {
        return value != null
                 ? replaceLineSeparatorBySpace(value.toString())
                 : null;
    }

    /**
     * Replaces all line separators ({@code \r} and {@code \n}) in {@code value} with spaces,
     * and collapses consecutive whitespace into a single space.
     *
     * @param value the string to process, or {@code null}
     * @return the processed string with line separators replaced by spaces, or {@code null} if {@code value} is {@code null}
     */
    public static String replaceLineSeparatorBySpace(final String value) {
        return value != null
                 ? value.replaceAll("\r", "") .replaceAll("\n", " ") .replaceAll("\\s+", " ")
                 : null;
    }

}
