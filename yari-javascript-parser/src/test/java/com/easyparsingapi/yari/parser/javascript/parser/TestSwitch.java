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

public class TestSwitch {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = 
                  "switch (expr) {"
                + "  case 'Oranges':"
                + "    console.log('Oranges are $0.59 a pound.');"
                + "    break;"
                + "  case 'Cherries':"
                + "    console.log('Cherries are $3.00 a pound.');"
                + "  case 'Mangoes':"
                + "  case 'Papayas':"
                + "    c + 5;"
                + "    break;"
                + "  default:"
                + "    console.log('Sorry, we are out of ' + expr + '.');}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "switch(typeof a){case \"string\":kc(a);var b=hc(Number(a));if(ec(b))a=String(b)}";
        assertAst(testInfo, code);
    }
    
    
    
}
