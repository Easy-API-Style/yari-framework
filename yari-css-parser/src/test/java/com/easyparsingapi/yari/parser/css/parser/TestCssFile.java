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
package com.easyparsingapi.yari.parser.css.parser;

import static com.easyparsingapi.yari.parser.css.parser.AssertUtil.assertAst;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestCssFile {
    
    private static final Path resources = Paths.get("src/test/resources/com/easyparsingapi/yari/parser/css/test_css_file");
    
    @Test
    public void test_01(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_01.css"));
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_02.css"));
    }

    @Test
    public void test_03(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_03.css"));
    }

    @Test
    public void test_04(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_04.css"));
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_05.css"));
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_06.css"));
    }

    @Test
    public void test_07(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_07.css"));
    }

    @Test
    public void test_08(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_08.css"));
    }

    @Test
    public void test_09(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_09.css"));
    }

    @Test
    public void test_10(TestInfo testInfo) {
        assertAst(testInfo, resources.resolve("test_10.css"));
    }
    
}
