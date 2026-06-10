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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.easyparsingapi.yari.parsec.InternalFunctors;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.TokenMap;
import com.easyparsingapi.yari.parsec.functors.Map2;
import com.easyparsingapi.yari.parsec.functors.Map3;
import com.easyparsingapi.yari.parsec.functors.Map4;
import com.easyparsingapi.yari.parsec.functors.Map5;

/**
 * Unit test for {@link InternalFunctors}.
 */
public class InternalFunctorsTest {

    @Test
    public void testTokenWithSameValue() {
        Integer i = 10;
        TokenMap<Token> fromToken = InternalFunctors.tokenWithSameValue(i);
        assertEquals("10", fromToken.toString());
        assertNull(fromToken.map(new Token(1, 1, "foo")));
        assertNull(fromToken.map(new Token(1, 1, 2)));
        assertNull(fromToken.map(new Token(1, 1, null)));
        Token token = new Token(1, 1, i);
        assertSame(token, fromToken.map(token));
    }

    @Test
    public void testFirstOfTwo() {
    	Map2<String, Integer, String> map = InternalFunctors.firstOfTwo();
        assertEquals("followedBy", map.toString());
        assertEquals("one", map.map("one", 2));
    }

    @Test
    public void testLastOfTwo() {
    	Map2<Integer, String, String> map = InternalFunctors.lastOfTwo();
        assertEquals("sequence", map.toString());
        assertEquals("two", map.map(1, "two"));
    }

    @Test
    public void testLastOfThree() {
        Map3<Integer, String, String, String> map = InternalFunctors.lastOfThree();
        assertEquals("sequence", map.toString());
        assertEquals("three", map.map(1, "two", "three"));
    }

    @Test
    public void testLastOfFour() {
        Map4<Integer, String, String, String, String> map = InternalFunctors.lastOfFour();
        assertEquals("sequence", map.toString());
        assertEquals("four", map.map(1, "two", "three", "four"));
    }

    @Test
    public void testLastOfFive() {
        Map5<Integer, String, String, String, String, String> map = InternalFunctors.lastOfFive();
        assertEquals("sequence", map.toString());
        assertEquals("five", map.map(1, "two", "three", "four", "five"));
    }

}
