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

import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;

public class TestFunctionDeclaration {
    
    private void assertJavascript(TestInfo testInfo, String... code) {
        assertAst(testInfo, Node.functionDeclaration, code);
    }

    @Test
    public void test_01(TestInfo testInfo) {
        String code = "function fct_1() {"
                    + " var v_1;"
                    + " function fct_2(p_3) {"
                    + "  var v_2;"
                    + "  return v_3;"
                    + " }"
                    + "}";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "function /* df */ fct_1(p_1, p_2) {\n"
                    + " var v_1;//ttt\n"
                    + " function /* df\n"
                    + "cxvfx\n"
                    + "*/fct_2(p_3) {\n"
                    + "  //eeeeeee\n"
                    + "  //aaaaaaa\n"
                    + "  v_2 = 'l_1'\n"
                    + " }\n"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "function fct_1(p_1, p_2) {"
                    + " var v_1;"
                    + " function fct_2(p_3) {"
                    + "  var v_2;"
                    + "  return v_3;"
                    + " }"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "var f = function (p_1, p_2) {"
                    + " var v_1;"
                    + " function fct_2(p_3) {"
                    + "  var v_2;"
                    + "  return v_3;"
                    + " }"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = 
                  "function* generator(i) {"
                + "  yield i"
                + "  yield i + 10;"
                + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = 
                "function* creerID() { "
              + " var index = 0; "
              + " while (true) { "
              + "  yield index++; "
              + " } "
              + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = 
              "async function asyncCall() { "
            + " console.log('calling'); "
            + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = 
                "function func(a, b, c = 9) { "
              + "  console.log(a + \" \" + b); "
              + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = 
            "function greet(name, greeting, message = greeting + ' ' + name) {"
          + "  return [name, greeting, message]"
          + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = 
                "function myFunc() {"
              + "  var x = 0;"
              + "  return (x += 1, x); "
              + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String[] code = {
                "function* logGenerator() {",
                "  console.log(0);",
                "  console.log(1, yield);",
                "  console.log(2, yield);",
                "  console.log(3, yield);",
                "}"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_12(TestInfo testInfo) {
        String[] code = { "a=>l.get(a)" };
        assertAst(testInfo, code);
    }

    @Test
    public void test_13(TestInfo testInfo) {
        String[] code = { "function(a){return/^[\\s\\xa0]*([\\s\\S]*?)[\\s\\xa0]*$/.exec(a)[1]};" };
        assertAst(testInfo, code);
    }

    @Test
    public void test_14(TestInfo testInfo) {
        String[] code = { 
                "function mc(a)"
                + "{"
                        
                + "if(!kc(a))throw qb(\"int64\");"
                + "switch(typeof a)"
                + "{"
                + "case \"string\":"
                + "kc(a);"
                + "var b=hc(Number(a));"
                + "C>=4294967296&&(D+=Math.trunc(C/4294967296),D>>>=0,C>>>=0)"

                + "case \"bigint\":"
                + "b=dc(64,a);"
                + "if(!/^\\s*(?:-?[1-9]\\d*|0)?\\s*$/.test(b))throw Error(String(b));"
                + "}"
                
                + "}",
                };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        String[] code = { 
           "function sanitize(x) {",
           "  if (isNaN(x)) {",
           "    return NaN;",
           "  }",
           "  return x;",
           "}" };
        assertAst(testInfo, code);
    }
    
}
