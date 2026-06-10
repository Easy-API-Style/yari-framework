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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;

public class TestQualifiedExpression {
	
	private void assertJavascript(TestInfo testInfo, String... code) {
		assertAst(testInfo, Node.qualifiedExpression, code);
	}
	
    @Test
    public void test_01(TestInfo testInfo) {
        String code = "toto.bibi.momo";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "toto[nn][hh.uu].bb.gg[rr]";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "bb.ll.tab[ff][tt].l";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "tutu().toto[nn.ll][rr]";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "tutu(n, k, a.b.c).toto[nn.ll][rr]";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "this.nom";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "import('/modules/mon-module.js').then(module)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    @Disabled
    public void test_11(TestInfo testInfo) {
        String code = "Promise.resolve({ value, done })";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = "/^(\\w+)\\:\\/\\/([^\\/]+)\\/(.*)$/.exec(url)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = "[].slice.call(arguments)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_14(TestInfo testInfo) {
        String code = "client.details?.address?.city";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        String code = "someInterface.customMethod?.()";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_16(TestInfo testInfo) {
        String code = "myMap.get(\"bar\")?.name";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_17(TestInfo testInfo) {
        String code = "obj?.['prop' + 'Name']";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_18(TestInfo testInfo) {
        String code = "import.meta.url";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_19(TestInfo testInfo) {
        String code = "obj?.['prop' + 'Name'].last.ok";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_20(TestInfo testInfo) {
        String code = "this.info((4+5))";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_21(TestInfo testInfo) {
        String code = "this.info((value))";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_22(TestInfo testInfo) {
        String code = "this.info(((value)))";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_23(TestInfo testInfo) {
        String code = "[].push(arguments)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_24(TestInfo testInfo) {
        String code = "(i[r].q=i[r].q||[]).push(arguments)";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_25(TestInfo testInfo) {
        String code = "w[l].push({'gtm.start': new Date().getTime(),event:'gtm.js'})";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_26(TestInfo testInfo) {
        String code = "new Date().getTime()";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_27(TestInfo testInfo) {
        String code = "/.*pcmag\\\\.com$/.test(location.hostname)";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_28(TestInfo testInfo) {
        String code = "/^([a-z0-9._-]+)@([a-z0-9._-]+)\\.([a-z]{2,6})$/.test(email)";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_29(TestInfo testInfo) {
        String code = "window.gapi.load(\"\",{callback:window[\"gapi_onload\"]})";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_30(TestInfo testInfo) {
        String code = "\"tube ytsubscribe zoomableimage\".split(\" \")";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_31(TestInfo testInfo) {
        String code = "stackTrace.split('\\n')";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_32(TestInfo testInfo) {
        String code = "Object.defineProperty(window.customElements,\"get\",{value:a=>l.get(a),configurable:!0,writable:!0})";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_33(TestInfo testInfo) {
        String code = "Promise.resolve({ value, done })";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_34(TestInfo testInfo) {
        String code = "e.subarray(c,c=10240)";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_35(TestInfo testInfo) {
        String code = "n.push({...e,value:{...e.value,data:[e.value.data]}})";
        assertJavascript(testInfo, code);
    }
    
}
