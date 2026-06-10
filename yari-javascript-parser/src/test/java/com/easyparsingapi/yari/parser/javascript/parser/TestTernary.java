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

import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;

public class TestTernary {

	private void assertJavascript(TestInfo testInfo, String... code) {
		assertAst(testInfo, Node.ternary, code);
	}
	
	@Test
	public void test_01(TestInfo testInfo) {
		String code = "isMember ? '$2.00' : '$10.00'";
		assertJavascript(testInfo, code);
	}
	
	@Test
	public void test_02(TestInfo testInfo) {
		String code = "condition1 ? value1"
				    + " : condition2 ? value2"
				    + " : condition3 ? value3"
				    + " : value4";
		assertJavascript(testInfo, code);
	}
	
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "condition.v(e) ? value1 : value2";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "\"undefined\"!=typeof self ? self: \"undefined\"!=typeof window ? window: {}";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        String code = 
           "(offset=document.cookie.indexOf(o),-1!=offset)"
         + "    ? ("
         + "        offset+=o.length,"
         + "        end=document.cookie.indexOf(\";\",offset),"
         + "        -1==end&&(end=document.cookie.length),"
         + "        unescape(document.cookie.substring(offset,end))"
         + "     )"
         + "    : void 0";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        String code = "b===c?b!==0||1/b===1/c:b!==b&&c!==c";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        String code = "b===1?function(D){return E.createScript(D)}:function(D){return\"\"+D}(z)(Array(Math.random()*7824|0)).join(\"\\n\")";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "b===c?b!==0||1 / b===1 / c:b!==b&&c!==c";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_09(TestInfo testInfo) {
        String code = "b===c?b!==0||1 /b===1/ c:b!==b&&c!==c";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_10(TestInfo testInfo) {
        String code = "Ic(this,a,b)?Jc(this,a,!0):new this.constructor(Hc(a,b,!1))";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_11(TestInfo testInfo) {
        String code = "status===1?(a,push(()=>{Tg(f)})):status===2";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = "a.Ha?(0,_.cKb)(b):bKb(b)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = "a.Ha ?.9:.5";
        assertJavascript(testInfo, code);
    }
    
}
