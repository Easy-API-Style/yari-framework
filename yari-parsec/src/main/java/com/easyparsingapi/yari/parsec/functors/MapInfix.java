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

import java.util.function.BiFunction;

/**
 * A {@link BiFunction} wrapper that associates an infix operator with a mapping function applied to a left and right operand.
 * <p>
 * After each invocation of {@link #apply(Object, Object)}, the last left operand, right operand,
 * and computed infix result are retained and accessible via the corresponding accessor methods.
 * </p>
 *
 * @param <O> the type of the infix operator
 * @param <L> the type of the left operand
 * @param <R> the type of the right operand
 * @param <I> the type of the infix result
 */
public class MapInfix<O, L, R, I> implements BiFunction<L, R, I> {

    private final O operator;
    private final BiFunction<L, R, I> map;

    private L left;
    private R right;
    private I infix;

    private MapInfix(final O operator,
                     final BiFunction<L, R, I> map) {
        super();
        this.operator = operator;
        this.map = map;
    }

    @Override
    public I apply(L left, R right) {
        this.left = left;
        this.right = right;
        this.infix = map.apply(left, right);
        return this.infix;
    }

    /**
     * Returns the left operand used in the most recent call to {@link #apply(Object, Object)}.
     *
     * @return the last left operand, or {@code null} if {@code apply} has not been called yet
     */
    public L left() {
        return left;
    }

    /**
     * Returns the infix operator associated with this instance.
     *
     * @return the infix operator
     */
    public O operator() {
        return operator;
    }

    /**
     * Returns the right operand used in the most recent call to {@link #apply(Object, Object)}.
     *
     * @return the last right operand, or {@code null} if {@code apply} has not been called yet
     */
    public R right() {
        return right;
    }

    /**
     * Returns the infix result computed during the most recent call to {@link #apply(Object, Object)}.
     *
     * @return the last computed infix result, or {@code null} if {@code apply} has not been called yet
     */
    public I infix() {
        return infix;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(MapInfix.class.getSimpleName());
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
     * Creates a new {@code MapInfix} instance bound to the given operator and mapping function.
     *
     * @param <O>      the type of the infix operator
     * @param <L>      the type of the left operand
     * @param <R>      the type of the right operand
     * @param <I>      the type of the infix result
     * @param operator the infix operator to associate with this instance
     * @param map      the {@link BiFunction} used to compute the infix result from the two operands
     * @return a new {@code MapInfix} instance
     */
    public static <O, L, R, I> MapInfix<O, L, R, I> map(final O operator,
                                                        final BiFunction<L, R, I> map) {
        return new MapInfix<>(operator, map);
    }

}
