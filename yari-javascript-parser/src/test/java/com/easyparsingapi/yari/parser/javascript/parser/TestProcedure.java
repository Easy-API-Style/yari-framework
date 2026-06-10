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

public class TestProcedure {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = "{"
                    + " var v_1;"
                    + " var v_2;"
                    + " function fct_2(p_3) {"
                    + "  var v_3 = v_1 + v_2;"
                    + "  return v_3"
                    + " }"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "{"
                    + " var v_1 = 'null';"
                    + " {"
                    + "  var v_2;"
                    + "  var v_3;"
                    + "  v_2 = -1000.00"
                    + "  v_3 = 'roro';"
                    + " }"
                    + "}";
        assertAst(testInfo, code);
    }
    
}
