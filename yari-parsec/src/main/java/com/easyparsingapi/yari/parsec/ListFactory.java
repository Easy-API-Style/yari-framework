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
package com.easyparsingapi.yari.parsec;

import java.util.ArrayList;
import java.util.List;

import com.easyparsingapi.yari.parsec.internal.util.Lists;

/**
 * Creates a {link List}.
 */
abstract class ListFactory<T> {

    /** Creates a new list. */
    abstract List<T> newList();

    /** Returns a {@link ListFactory} that creates an empty {@link ArrayList}. */
    @SuppressWarnings("unchecked")
    static <T> ListFactory<T> arrayListFactory() {
        return ARRAY_LIST_FACTORY;
    }

    /**
     * Returns a {@link ListFactory} that creates an {@link ArrayList} instance 
     * with {@code first} as the first element.
     */
    static <T> ListFactory<T> arrayListFactoryWithFirstElement(final T first) {
        return new ListFactory<T>() {
            @Override
            List<T> newList() {
                ArrayList<T> list = Lists.arrayList();
                list.add(first);
                return list;
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private static final ListFactory ARRAY_LIST_FACTORY = new ListFactory<Object>() {
        @Override
        List<Object> newList() {
            return Lists.arrayList();
        }
    };
    
}
