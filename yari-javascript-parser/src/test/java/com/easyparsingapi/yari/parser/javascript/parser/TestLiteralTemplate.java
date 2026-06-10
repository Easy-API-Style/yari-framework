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

public class TestLiteralTemplate {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = 
            "`header ${\n"
          + "  isLargeScreen() ? \"\" : `icon-${item.isCollapsed ? \"expander\" : \"collapser\"}`\n"
          + "}`";
        assertAst(testInfo, code);
    }

    @Test
    public void test_02(TestInfo testInfo) {
        String[] code = { "function tf(a){return`/youtubei/v1/${sf(a)}`}" };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "`${e}${`/${\"youtubei\"}/${a.config_.innertubeApiVersion}/${b}`}`";
        assertAst(testInfo, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String code = "Lf(`${e}${`/${\"youtubei\"}/${a.config_.innertubeApiVersion}/${b}`}`)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        String code = "stack =`${stackFunction} ${stackURL}${stackLine}${stackCol}`.trim();";
        assertAst(testInfo, code);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        String code = "`{${Object.keys(a).map(b=>{const c=_.yw(a[b]);return`${b}:${c}`}).join(\",\")}}`";
        assertAst(testInfo, code);
    }
    
}
