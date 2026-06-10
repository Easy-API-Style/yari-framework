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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestImport {
	
    @Test
    public void test_01(TestInfo testInfo) {
        String code = "import defaultExport from \"module-name\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "import * as name from \"module-name\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "import { export1 } from \"module-name\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "import { export1 as alias1 } from 'module-name'";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "import { export1 , export2 } from \"module-name\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_6(TestInfo testInfo) {
        String code = "import { export1 , export2 as alias2 } from \"module-name\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "import defaultExport, { export1, export2 as alias2 } from \"module-name\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "import defaultExport, * as name from \"module-name\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "import \"module-name\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "import(\"module-name\")";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = "var promise = import(\"module-name\")";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = "let module = await import('/modules/mon-module.js');";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = "import('/modules/mon-module.js').then((module) => { })";
        assertAst(testInfo, code);
    }

}
