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
package com.easyparsingapi.yari.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Utility class providing helper methods for working with Java collections.
 */
public class CollectionUtil {

    /** Not instantiable — utility class. */
    private CollectionUtil() {}

    /**
     * Returns {@code true} if the given collection is {@code null} or contains no elements.
     *
     * @param values the collection to check, may be {@code null}
     * @return {@code true} if {@code values} is {@code null} or empty, {@code false} otherwise
     */
    public static boolean isEmpty(final Collection<?> values) {
        return values == null || values.isEmpty();
    }

    /**
     * Returns the given list if it is non-{@code null}, or an empty {@link ArrayList} otherwise.
     *
     * @param <O>    the element type
     * @param values the list to check, may be {@code null}
     * @return {@code values} if non-{@code null}, or a new empty {@link ArrayList}
     */
    public static <O> List<O> nullToEmpty(final List<O> values) {
        return values == null ? new ArrayList<>() : values;
    }

    /**
     * Creates a mutable {@link List} from the given varargs elements.
     * Returns an empty list if {@code values} is {@code null}.
     *
     * @param <O>    the element type
     * @param values the elements to include in the list
     * @return a new mutable {@link List} containing all provided elements, or an empty list if {@code null}
     */
    @SafeVarargs
    public static <O> List<O> toList(final O... values) {
        if (values != null) {
            return Lists.newArrayList(values);
        }
        return new ArrayList<>();
    }

    /**
     * Creates a mutable {@link Set} from the given varargs elements.
     * Returns an empty set if {@code values} is {@code null}.
     *
     * @param <O>    the element type
     * @param values the elements to include in the set
     * @return a new mutable {@link Set} containing all provided elements, or an empty set if {@code null}
     */
    @SafeVarargs
    public static <O> Set<O> toSet(final O... values) {
        if (values != null) {
            return Sets.newHashSet(values);
        }
        return new HashSet<>();
    }

}
