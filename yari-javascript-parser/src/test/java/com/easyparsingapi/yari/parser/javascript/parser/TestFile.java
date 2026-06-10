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
package com.easyparsingapi.yari.parser.javascript.parser;

import static com.easyparsingapi.yari.parser.javascript.parser.AssertUtil.assertAst;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestFile {
    
    public static Path folder = Path.of("src/test/resources/com/easyparsingapi/yari/parser/javascript/test_file");

    @Test
    public void test_01(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_assignment.js"));
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_class.js"));
    }

    @Test
    public void test_03(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_comma.js"));
    }

    @Test
    public void test_04(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_condition.js"));
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_destructuring.js"));
    }

    @Test
    public void test_06(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_function.js"));
    }

    @Test
    public void test_07(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_functionCall.js"));
    }

    @Test
    public void test_08(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_generator.js"));
    }

    @Test
    public void test_09(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_label.js"));
    }

    @Test
    public void test_10(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_parameter.js"));
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_qualified.js"));
    }

    @Test
    public void test_12(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_regex.js"));
    }

    @Test
    public void test_13(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_switch.js"));
    }

    @Test
    public void test_14(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_symbol.js"));
    }

    @Test
    public void test_15(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_tryCatch.js"));
    }

    @Test
    public void test_16(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_variable.js"));
    }

    @Test
    public void test_17(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_boolean.js"));
    }

    @Test
    public void test_18(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("test_literal.js"));
    }
    
}
