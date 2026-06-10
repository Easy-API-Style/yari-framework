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

import static com.easyparsingapi.yari.parser.javascript.parser.AssertUtil.assertAstError;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestCatchError {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = "function toto(v_1, v_2) { var { x_1 = v_1 + v_2; return x_1; }";
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "function toto((v_1, v_2) {\n var x_1 = v_1 + v_2;\n  return x_1;\n }";
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = 
            "function toto(v_1, v_2) ({(\n"
          + " var { x_1 = v_1 + v_2;\n [ return x_1;\n"
          + " }";
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "function toto(v_1, v_2) {\n var x_1 = v_1 + v_2;\n } return x_1;\n }";
        assertAstError(testInfo, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        String code = "var vv = 33,kk;xxxxxxxxx qqq fffff;let cc;dd rrr fff \n let  bibi = 'ggg';var const roro";
        assertAstError(testInfo, code);  
    }

    @Test
    public void test_06(TestInfo testInfo) {
        String code = "function toto(v_1, v_2) { var ! x_1 = v_1 + v_2; return x_1; }";
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "{a: aa = 10, b: bb = 5}";
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "rrrrrr sssssssss;  vv = 33,kk \n eeeeee bbbb";
        assertAstError(testInfo, code);    
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code =  "if(hh =oo, jj+, e = 3) {}";
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code =  "toto(rr, jj+, 4+5)";
        assertAstError(testInfo, code);
    }
    
}
