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

/**
 * Maps 7 objects to an object of type {@code T}.
 *
 * @param <A> the type of the first input
 * @param <B> the type of the second input
 * @param <C> the type of the third input
 * @param <D> the type of the fourth input
 * @param <E> the type of the fifth input
 * @param <F> the type of the sixth input
 * @param <G> the type of the seventh input
 * @param <T> the type of the result
 */
@FunctionalInterface
public interface Map7<A, B, C, D, E, F, G, T> {
    /**
     * Combines the seven provided values into a single result of type {@code T}.
     *
     * @param a the first input value
     * @param b the second input value
     * @param c the third input value
     * @param d the fourth input value
     * @param e the fifth input value
     * @param f the sixth input value
     * @param g the seventh input value
     * @return the result produced by combining all seven input values
     */
    T map(A a, B b, C c, D d, E e, F f, G g);
}
