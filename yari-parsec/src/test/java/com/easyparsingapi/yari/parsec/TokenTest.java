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

import org.junit.Test;

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.util.ObjectTester;

/**
 * Unit test for {@link Token}.
 */
public class TokenTest {

    @Test
    public void testLength() {
        assertEquals(1, new Token(0, 1, null).length());
    }

    @Test
    public void testIndex() {
        assertEquals(1, new Token(1, 2, null).index());
    }

    @Test
    public void testValue() {
        assertEquals("value", new Token(1, 2, "value").value());
    }

    @Test
    public void testToString() {
        assertEquals("value", new Token(1, 2, "value").toString());
        assertEquals("null", new Token(1, 2, null).toString());
    }

    @Test
    public void testEquals() {
        ObjectTester.assertEqual(new Token(1, 2, "value"), new Token(1, 2, "value"));
        ObjectTester.assertEqual(new Token(1, 2, null), new Token(1, 2, null));
        ObjectTester.assertNotEqual(new Token(1, 2, "value"), 
                                    new Token(2, 2, "value"),
                                    new Token(1, 3, "value"),
                                    new Token(1, 2, "value2"), 
                                    new Token(1, 2, null));
    }

}
