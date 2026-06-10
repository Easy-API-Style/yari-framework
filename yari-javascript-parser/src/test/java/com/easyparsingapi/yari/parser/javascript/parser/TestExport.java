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

public class TestExport {
	
    @Test
    public void test_01(TestInfo testInfo) {
        String code = "export let name1, name2,   nameN";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "export const name1 = 12, name2='toto', name3={fifi:'riri', ruru:'zuzu'}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "export var name1 = 12, name2, name3={fifi:'riri', ruru:'zuzu'}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "export function functionName(){ }";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "export class ClassName{ }";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "export const { name1, name2: bar } = o";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "export const [ name1, name2 ] = array";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "export default function () { }";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "export default function toto() { }";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "export { variable1 as name1, variable2 as name2, nameN }";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = "export { name1 as default, name2 }";
        assertAst(testInfo, code);
    }
 //export * from './test'
    @Test
    public void test_13(TestInfo testInfo) {
        String code = "export * from './test'";
        assertAst(testInfo, code);    }
    
    @Test
    public void test_14(TestInfo testInfo) {
        String code = "export * as name1  from './test'"; 
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        String code = "export { import1 as name1, import2 as name2, default, nameN } from './test'";
        assertAst(testInfo, code);
    }
    
}
