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

public class TestDestructuring {
    
    @Test
    public void test_01(TestInfo testInfo) {
        String code = "[a, b] = [10, 20]";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "[a, b, ...rest] = [10, 20, 30, 40, 50]";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "({ a, b } = { a: 10, b: 20 })";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "({a, b, ...rest} = {a: 10, b: 20, c: 30, d: 40})";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "[firstElement, secondElement] = list";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "const [firstElement, secondElement] = list;";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "[a=5, b=7] = [1]";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "[a, b] = f()";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "const {p: foo, q: bar} = o";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "{a = 2 , b}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = "({a = 2 , b} = {a: 3})";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = "let {[key]: foo} = {z: 'bar'}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = "({a: aa = 10, b: bb = 5} = {a: 3})";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_14(TestInfo testInfo) {
        String code = "const {a: aa = 10, b: bb = 5} = {a: 3}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        String code = "[a, , b] = f()";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_16(TestInfo testInfo) {
        String code = "[arr[2], arr[1]] = [arr[1], arr[2]]";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_17(TestInfo testInfo) {
        String code = "[a, ...b, ] = [1, 2, 3]";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_18(TestInfo testInfo) {
        String code = "let { [clef]: toto } = { z: \"truc\" };";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_19(TestInfo testInfo) {
        String code = 
                  "drawChart({"
                + "  coords: { x: 18, y: 30 },"
                + "  radius: 30,"
                + "});";
        assertAst(testInfo, code);
    }

    @Test
    public void test_20(TestInfo testInfo) {
        String code = "[a, b, ...rest] = array";
        assertAst(testInfo, code);
    }

    @Test
    public void test_21(TestInfo testInfo) {
        String code = "[a, b, ...{ pop, push }] = array";
        assertAst(testInfo, code);
    }

    @Test
    public void test_22(TestInfo testInfo) {
        String code = "[a = aDefault, b] = array";
        assertAst(testInfo, code);
    }

    @Test
    public void test_23(TestInfo testInfo) {
        String code = "[a, b, ...[c, d]] = array";
        assertAst(testInfo, code);
    }

    @Test
    public void test_25(TestInfo testInfo) {
        String[] code = {
            "let {",
            "  title: englishTitle, // rename",
            "  translations: [",
            "    {",
            "       title: localeTitle, // rename",
            "    },",
            "  ],",
            "} = metadata;"
        };
        assertAst(testInfo, code);
    }
    
}
