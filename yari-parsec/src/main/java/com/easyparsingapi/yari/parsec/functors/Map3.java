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
 * Maps 3 objects of types {@code A}, {@code B} and {@code C} to an object of type {@code T}.
 *
 * @param <A> the type of the first input
 * @param <B> the type of the second input
 * @param <C> the type of the third input
 * @param <T> the type of the result
 */
@FunctionalInterface
public interface Map3<A, B, C, T> {

    /**
     * Maps {@code a}, {@code b} and {@code d} to the target object.
     *
     * @param a the first input value
     * @param b the second input value
     * @param d the third input value
     * @return the mapped result
     */
    T map(A a, B b, C d);
    
}
