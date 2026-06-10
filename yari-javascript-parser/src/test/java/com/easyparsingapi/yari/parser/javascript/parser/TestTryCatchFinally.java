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

public class TestTryCatchFinally {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = 
                  "try { "
                + "    fct_1();"
                + "} catch (e if e instanceof TypeError) { "
                + "    fct_2(); "
                + "} catch (e if e instanceof RangeError) { "
                + "    fct_3(); "
                + "} catch (e if e instanceof EvalError) { "
                + "    fct_4();  "
                + "} catch (e) { "
                + "    fct_5();"
                + "} ";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = 
                  "try { "
                + "    fct_1();"
                + "} catch (e if e instanceof TypeError) { "
                + "    fct_2(); "
                + "} catch (e if e instanceof RangeError) { "
                + "    fct_3(); "
                + "} catch (e if e instanceof EvalError) { "
                + "    fct_4();  "
                + "} catch (e) { "
                + "    fct_5();"
                + "} ";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "try{const {pvc:l}=_.z(await _.z(this.wa));l==null||l()}finally{_.z()}";
        assertAst(testInfo, code);
    }
    
}
