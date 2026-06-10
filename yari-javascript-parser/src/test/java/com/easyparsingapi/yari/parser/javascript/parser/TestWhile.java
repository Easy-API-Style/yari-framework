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

public class TestWhile {

    @Test
    public void test_1(TestInfo testInfo) {
        String code = "while (v == 10) "
                          + " var v_1";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_2(TestInfo testInfo) {
        String code = "while (fct_1(), fct_2(), fct_3()) "
                          + " var v_1";
        assertAst(testInfo, code);  
    } 
    
    @Test
    public void test_3(TestInfo testInfo) {
        String code = "while (fct_1(), fct_2(), fct_3());";
        assertAst(testInfo, code);  
    } 
    
    @Test
    public void test_4(TestInfo testInfo) {
        String code = "while (bibi !== 'roro') {"
                          + " var v_1"
                          + " if (toto) {"
                          + "  break"
                          + " }"
                          + "}";
        assertAst(testInfo, code);  
    } 

}
