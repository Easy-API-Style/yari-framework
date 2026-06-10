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
import static com.easyparsingapi.yari.parser.javascript.parser.AssertUtil.assertAstError;

import java.nio.file.Path;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

//@Disabled
public class TestComplexFile {

    public static Path folder = Path.of("src/test/resources/com/easyparsingapi/yari/parser/javascript/test_complex_file");
    
    @Test
    public void test_01(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("file_01.js"));
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("file_02.js"));
    }

    @Test
    public void test_03(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("file_03.js"));
    }

    @Test
    public void test_04(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("file_04.js"));
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("file_05.js"));
    }

    @Test
    @Disabled
    public void test_06(TestInfo testInfo) {
        assertAstError(testInfo, folder.resolve("file_06.js"));
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        assertAst(testInfo, folder.resolve("file_07.js"));
    }
    
    @Test
    @Disabled
    public void test_08(TestInfo testInfo) {
        assertAstError(testInfo, folder.resolve("file_08.js"));
    }

}
