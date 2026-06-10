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

public class TestFor {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = "for (v in values) "
                          + " var v_1";
         assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "for (var i = 0; i < values.length; i++) "
                          + " var v_1";
         assertAst(testInfo, code);
    } 
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "for (fct_1(), var i = 0; fct_2(), i < values.length; fct_3(), i++) "
                          + " var v_1";
         assertAst(testInfo, code);
    } 
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "for (fct_1(), var i = 0; fct_2(), i < values.length; fct_3(), i++);";
         assertAst(testInfo, code);
    } 
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "for (var i = 0; i < values.length; i++) {"
                          + " var v_1"
                          + " continue"
                          + " if (toto) {"
                          + "  break"
                          + " }"
                          + "}";
         assertAst(testInfo, code);
    } 
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = 
                  "for await (const num of generatorWithRejectedPromises()) { "
                + " console.log(num);"
                + "}";
         assertAst(testInfo, code);
    } 
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "for ( let b = 4; ; current = n) {"
                    + " yield current;"
                    + "}";
         assertAst(testInfo, code);
    }

    @Test
    public void test_08(TestInfo testInfo) {
        String code = "for(let e=0+b,f=(c-b)%6+b;f<=c;e=f,f+=6)"
                + "{C>=4294967296&&(D+=Math.trunc(C/4294967296),D>>>=0,C>>>=0)}";
         assertAst(testInfo, code);
    }
    
}
