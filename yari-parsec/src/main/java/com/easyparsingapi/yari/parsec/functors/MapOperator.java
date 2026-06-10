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

import java.util.function.Function;

/**
 * Associates an operator token with a mapping function, allowing the operator to be retrieved
 * alongside the transformation it represents.
 *
 * @param <O> the type of the operator
 * @param <V> the input type of the mapping function
 * @param <R> the result type of the mapping function
 */
public class MapOperator<O, V, R> implements Function<V, R> {

    private final O operator;
    private final Function<V, R> map;

    private MapOperator(final O operator,
                        final Function<V, R> map) {
        super();
        this.operator = operator;
        this.map = map;
    }

    @Override
    public R apply(V arg0) {
        return map.apply(arg0);
    }

    /**
     * Returns the operator token associated with this mapping function.
     *
     * @return the operator
     */
    public O operator() {
        return operator;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(MapOperator.class.getSimpleName());
        result.append(" [operator=");
        result.append(operator);
        result.append(", map=");
        result.append(map);
        result.append("]");
        return result.toString();
    }

    /*
     *
     * STATIC
     *
     */

    /**
     * Creates a new {@code MapOperator} binding the given operator token to the given mapping function.
     *
     * @param <A>      the type of the operator
     * @param <B>      the input type of the mapping function
     * @param <C>      the result type of the mapping function
     * @param operator the operator token to associate with the function
     * @param map      the mapping function
     * @return a new {@code MapOperator} wrapping the operator and function
     */
    public static <A, B, C> MapOperator<A, B, C> map(final A operator, final Function<B, C> map) {
        return new MapOperator<>(operator, map);
    }

}
