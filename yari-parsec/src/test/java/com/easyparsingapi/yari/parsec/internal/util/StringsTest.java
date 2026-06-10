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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.easyparsingapi.yari.parsec.internal.util.StringUtil;

/**
 * Unit test for {@link StringUtil}.
 */
public class StringsTest {

    @Test
    public void testJoin() {
        assertEquals("", StringUtil.join(", ", new Object[0]));
        assertEquals("1", StringUtil.join(", ", new Object[] { 1 }));
        assertEquals("1, 2", StringUtil.join(", ", new Object[] { 1, 2 }));
    }

    @Test
    public void testJoin_withStringBuilder() {
        assertEquals("", StringUtil.join(new StringBuilder(), ", ", new Object[0]).toString());
        assertEquals("1", StringUtil.join(new StringBuilder(), ", ", new Object[] { 1 }).toString());
        assertEquals("1, 2", StringUtil.join(new StringBuilder(), ", ", new Object[] { 1, 2 }).toString());
    }
}
