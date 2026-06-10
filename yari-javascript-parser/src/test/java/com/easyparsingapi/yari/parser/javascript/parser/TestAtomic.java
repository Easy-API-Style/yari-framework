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
import static com.easyparsingapi.yari.parser.javascript.parser.AssertUtil.assertAstExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.atomic;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;

public class TestAtomic {

    private void assertJavascript(TestInfo testInfo, String... code) {
        assertAst(testInfo, Set.of(Node.atomic), code);
    }

    private void assertExpression(TestInfo testInfo, String... code) {
        assertAstExpression(testInfo, Set.of(atomic), code);
    }
    
    @Test
    public void test_01(TestInfo testInfo) {
        String code = "toto 67 9.001 9. .7";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "toto 67 9.001 9. .7 \'rr ff dd\'   \"tt bb \"";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "-67";
        assertExpression(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "&#39;toto 67 9.001&#39; 9. .7 &apos;rr ff dd&apos;   \"tt bb \"";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "&#34;toto 67 9.001&#34; 9. .7 &quot;rr ff dd&quot;   \"tt bb \"";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "-9.001";
        assertExpression(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "-9.";
        assertExpression(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "-.7";
        assertExpression(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "123456789123456789n";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "123456789123456789n 0o7777777777777777n 0x123456789ABCDEF1n 0b0101010101110101n";
        assertAst(testInfo, code);
    }
    
}
