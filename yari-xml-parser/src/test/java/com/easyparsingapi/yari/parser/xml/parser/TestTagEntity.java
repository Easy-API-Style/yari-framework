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
package com.easyparsingapi.yari.parser.xml.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.easyparsingapi.yari.parser.xml.lexer.TagEntity;
import com.easyparsingapi.yari.parser.xml.lexer.TagEntity.Attribute;
import com.easyparsingapi.yari.parser.xml.lexer.TagEntity.Markup;

public class TestTagEntity {

    @Test
    public void test_01() {
        assertEquals(new Markup("abc", "name"), TagEntity.newMarkup("abc:name"));
    }
    
    @Test
    public void test_02() {
        assertEquals(new Markup(null, "name"), TagEntity.newMarkup("name"));
    }
    
    @Test
    public void test_03() {
        assertEquals(new Attribute(new Markup("abc", "name"), "blabla"), TagEntity.newAttribute("abc:name", "'blabla'"));
    }
    
    @Test
    public void test_04() {
        assertEquals(new Attribute(new Markup(null, "name"), "blabla"), TagEntity.newAttribute("name", "\"blabla\""));
    }

    @Test
    public void test_05() {
        assertEquals(new Attribute(new Markup(null, "name"), "blabla"), TagEntity.newAttribute("name", "blabla"));
    }
    
}
