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

public class TestArrowFunction {

    @Test
    public void test_01(TestInfo testInfo) {
        String code =  """
           (a) => {
              return a + 100;
        }""";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "(a) => a + 100";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "a => a + 100";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "(a, b) => a + b + 100";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "() => a + b + 100";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "let bob = a => b + 100;";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "(a = 400, b=20, c) => a + b + 100";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "(a, b, ...r) => a + b + 100";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "() => ({foo: \"a\"})";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        assertAst(testInfo, "(function (a) {",
                            "   return a + 100;",
                            "});");
    }

    @Test
    public void test_11(TestInfo testInfo) {
        assertAst(testInfo, "(a) => {",
                            "   return a + 100;",
                            "};");
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        assertAst(testInfo, "(a) => a + 100;");
    }

    @Test
    public void test_13(TestInfo testInfo) {
        assertAst(testInfo, "a => a + 100;");
    }

    @Test
    public void test_14(TestInfo testInfo) {
        assertAst(testInfo, "const func = () => ({ foo: 1 });");
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        assertAst(testInfo, "([a, b] = [10, 20]) => console.log(this.i, this);");
    }
    
    @Test
    public void test_16(TestInfo testInfo) {
        assertAst(testInfo, "()=>{Tg(this,this,d,e,f)}");
    }
    
}
