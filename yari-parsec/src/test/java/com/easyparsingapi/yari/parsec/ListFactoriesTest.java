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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.easyparsingapi.yari.parsec.ListFactory;

/**
 * Unit test for {@link ListFactories}.
 */
public class ListFactoriesTest {

    @Test
    public void testArrayListFactory() {
        ListFactory<Integer> intListFactory = ListFactory.arrayListFactory();
        ListFactory<String> stringListFactory = ListFactory.arrayListFactory();
        ArrayList<Integer> intList = (ArrayList<Integer>) intListFactory.newList();
        ArrayList<String> stringList = (ArrayList<String>) stringListFactory.newList();
        assertNotSame(intList, stringList);
        assertEquals(0, intList.size());
        assertEquals(0, stringList.size());
    }

    @Test
    public void testArrayListFactoryWithFirstElement() {
        ListFactory<Integer> intListFactory = ListFactory.arrayListFactoryWithFirstElement(1);
        ArrayList<Integer> list = (ArrayList<Integer>) intListFactory.newList();
        assertEquals(Arrays.asList(1), list);
    }
    
}
