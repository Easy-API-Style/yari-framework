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
package com.easyparsingapi.yari.parsec.error;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;
import com.easyparsingapi.yari.parsec.util.ObjectTester;

/**
 * Unit test for {@link Location}.
 */
public class LocationTest {

    @Test
    public void testToString() {
        assertEquals("line 1 column 2", new Position(1, 2).toString());
    }

    @Test
    public void testEquals() {
        ObjectTester.assertEqual(new Position(1, 2), new Position(1, 2));
        ObjectTester.assertNotEqual(new Position(1, 2), new Position(2, 2), new Position(1, 1));
    }

}
