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
package com.easyparsingapi.yari.parser.css.parser;

import static com.easyparsingapi.yari.parser.css.parser.AssertUtil.assertAstError;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestCssError {

    @Test
    public void test_01(TestInfo testInfo) {
        String code =  """
           li:has(+ li:last-of-type > + div) {
             color: red;
             font-weight: bold;
           }
        """;
        assertAstError(testInfo, code);
    }

    @Test
    public void test_02(TestInfo testInfo) {
        String code =  """
           a[title + 3] {
                color: purple;
           }
        """;
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code =  """
            table.browserref tr:nth-child(odd) {
                background-color: #ffffff;
            
            table.browserref tr.fixzebra {
                background-color: #E7E9EB;
            }
            
            table.browserref th {
                height: 44px;
                background-repeat: no-repeat;
                background-position: center center;
                border: 1px solid #d4d4d4;
                background-color: #ffffff;
                font-weight: normal;
                color: #555555;
                padding: 11px 5px 11px 5px;
                vertical-align: middle;
            }
        """;
        assertAstError(testInfo, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String code =  """
            table.browserref tr:nth-child(odd) {
                background-color: #ffffff;
            }
            
            table.browserref tr.fixzebra {
                background-color: #E7E9EB;
            
            
            @font-face {
                font-display: auto;
                font-family: tablet-gothic;
                font-style: normal;
                font-weight: 800;
            }
        """;
        assertAstError(testInfo, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        String code =  """
            .sb86 {
                background: url() no-repeat;
                background-size: ;
            }
        """;
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code =  """
            .sb86 {
                background: ;
            }
        """;
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code =  """
            .sb86 {
                background: vv(;
            }
        """;
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code =  """
            .sb86 {
                background: vv(;
                font-weight: 800;
            }
            .sb87 {
               font-weight: 800;
            }
        """;
        assertAstError(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code =  """
            .sb86 {
                background: url(;
            }
        """;
        assertAstError(testInfo, code);
    }
    
}
