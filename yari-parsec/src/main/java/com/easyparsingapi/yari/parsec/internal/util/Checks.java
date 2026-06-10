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

/**
 * Common facilities to check precondition, postcondition and invariants.
 */
public final class Checks {

    /** Not instantiable — all methods are static. */
    private Checks() {}

    /**
     * Checks that {@code value} is not null.
     *
     * @param <T>   the type of the value
     * @param value the value to check
     * @return the value itself if non-null
     * @throws NullPointerException if {@code value} is null
     */
    public static <T> T checkNotNull(T value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

    /**
     * Checks that an argument satisfies requirement.
     *
     * @param condition the condition that has to be true
     * @param message   the error message if {@code condition} is false
     *
     * @throws IllegalArgumentException if {@code condition} is false
     */
    public static void checkArgument(boolean condition,
                                     String message)
                               throws IllegalArgumentException {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that an argument satisfies requirement.
     *
     * @param condition the condition that has to be true
     * @param message   the error message if {@code condition} is false
     * @param args      the arguments to the error message
     *
     * @throws IllegalArgumentException if {@code condition} is false
     */
    public static void checkArgument(boolean condition, 
                                     String message, 
                                     Object... args)
                           throws IllegalArgumentException {
        if (!condition) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * Checks a certain state.
     *
     * @param condition the condition of the state that has to be true
     * @param message   the error message if {@code condition} is false
     *
     * @throws IllegalStateException if {@code condition} is false
     */
    public static void checkState(boolean condition,
                                  String message) throws IllegalStateException {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Checks a certain state.
     *
     * @param condition the condition of the state that has to be true
     * @param message   the error message if {@code condition} is false
     * @param args      the arguments to the error message
     *
     * @throws IllegalStateException if {@code condition} is false
     */
    public static void checkState(boolean condition, 
                                  String message, 
                                  Object... args) throws IllegalStateException {
        if (!condition) {
            throw new IllegalStateException(String.format(message, args));
        }
    }

    /**
     * Checks that {@code object} is not null.
     *
     * @param object  the object that cannot be null
     * @param message the error message if {@code object} is null
     *
     * @throws IllegalStateException if {@code object} is null
     */
    public static void checkNotNullState(Object object,
                                         String message) {
        checkState(object != null, message);
    }

    /**
     * Checks that {@code object} is not null.
     *
     * @param object  the object that cannot be null
     * @param message the error message if {@code condition} is false
     * @param args    the arguments to the error message
     *
     * @throws IllegalStateException if {@code object} is null
     */
    public static void checkNotNullState(Object object,
                                         String message, 
                                         Object... args) {
        checkState(object != null, message, args);
    }

    /**
     * Checks that neither {@code min} nor {@code max} is negative and that {@code min &lt;= max}.
     *
     * @param min the minimum value to check
     * @param max the maximum value to check
     */
    public static void checkMinMax(int min, int max) {
        checkMin(min);
        checkMax(max);
        checkArgument(min <= max, "min > max");
    }

    /**
     * Checks that {@code min} is not negative.
     *
     * @param min the minimum value to check
     * @return {@code min} if non-negative
     */
    public static int checkMin(int min) {
        checkNonNegative(min, "min < 0");
        return min;
    }

    /**
     * Checks that {@code max} is not negative.
     *
     * @param max the maximum value to check
     * @return {@code max} if non-negative
     */
    public static int checkMax(int max) {
        checkNonNegative(max, "max < 0");
        return max;
    }

    /**
     * Checks that {@code n} is not negative, or throws an {@link IllegalArgumentException} with {@code message}.
     *
     * @param n       the value to check
     * @param message the error message if {@code n} is negative
     * @return {@code n} if non-negative
     */
    public static int checkNonNegative(int n, String message) {
        checkArgument(n >= 0, message);
        return n;
    }
    
}
