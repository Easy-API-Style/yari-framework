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

import java.util.ArrayList;

/**
 * Internal utility to work with {@link java.util.List}.
 */
public final class Lists {

    /** Not instantiable — all methods are static. */
    private Lists() {}

    /**
     * Returns a new empty {@link ArrayList}.
     *
     * @param <T> the element type
     * @return a new empty {@link ArrayList}
     */
    public static <T> ArrayList<T> arrayList() {
        return new ArrayList<T>();
    }

    /**
     * Returns a new {@link ArrayList} with enough capacity to hold {@code expectedElements}.
     *
     * @param <T>              the element type
     * @param expectedElements the expected number of elements
     * @return a new {@link ArrayList} pre-sized for the expected number of elements
     */
    public static <T> ArrayList<T> arrayList(int expectedElements) {
        return new ArrayList<T>(capacity(expectedElements));
    }

    private static int capacity(int expectedElements) {
        return (int) Math.min(5L + expectedElements + (expectedElements / 10), Integer.MAX_VALUE);
    }
    
}
