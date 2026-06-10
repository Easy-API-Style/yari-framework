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
 * Utility functions for any object.
 */
public final class Objects {

    /** Not instantiable — all methods are static. */
    private Objects() {}

    /**
     * Returns the hash code of {@code obj}, or {@code 0} if {@code obj} is {@code null}.
     *
     * @param obj the object to hash, may be {@code null}
     * @return the hash code, or {@code 0} for {@code null}
     */
    public static int hashCode(Object obj) {
        return obj == null ? 0 : obj.hashCode();
    }

    /**
     * Compares {@code o1} and {@code o2} for equality.
     * Returns {@code true} if both are {@code null} or if {@code o1.equals(o2)}.
     *
     * @param o1 the first object, may be {@code null}
     * @param o2 the second object, may be {@code null}
     * @return {@code true} if the two objects are equal or both {@code null}
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    /**
     * Checks whether {@code obj} is one of the elements of {@code array} (by reference equality).
     *
     * @param obj   the object to look for
     * @param array the array of candidates
     * @return {@code true} if {@code obj} is found in {@code array}
     */
    public static boolean in(Object obj, Object... array) {
        for (Object expected : array) {
            if (obj == expected) {
                return true;
            }
        }
        return false;
    }
    
}
