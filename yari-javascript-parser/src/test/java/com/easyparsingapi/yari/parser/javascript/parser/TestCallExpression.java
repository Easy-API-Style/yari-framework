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

public class TestCallExpression {

	@Test
	public void test_01(TestInfo testInfo) {
		String code = "look()[10]";
		assertAst(testInfo, code);
	}

	@Test
	public void test_02(TestInfo testInfo) {
		String code = "look(20*30)()[10][value]";
		assertAst(testInfo, code);
	}

	@Test
	public void test_03(TestInfo testInfo) {
		String code = "look(20*30, 'yo', ++count)()[10][(value + 30) * 6.6]";
		assertAst(testInfo, code);
	}

	@Test
	public void test_04(TestInfo testInfo) {
		String code = "look[10][value + 30 * 6.6]()";
		assertAst(testInfo, code);
	}

	@Test
	public void test_05(TestInfo testInfo) {
		String code = "look[nn][hh.uu]";
		assertAst(testInfo, code);
	}

	@Test
	public void test_06(TestInfo testInfo) {
		String code = "toto[nn.ll][rr]";
		assertAst(testInfo, code);
	}
	
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "toto(n, k)[nn.ll](bibi)";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "toto[nn.ll](n, k)";
        assertAst(testInfo, code);
    }

	@Test
	public void test_10(TestInfo testInfo) {
		String code = "look(count--)['ok']";
		assertAst(testInfo, code);
	}

	@Test
	public void test_11(TestInfo testInfo) {
		String code = "look(true)[false]";
		assertAst(testInfo, code);
	}

    @Test
    public void test_12(TestInfo testInfo) {
        String code = "look[(value + 30) * 6.6]";
        assertAst(testInfo, code);
    }

    @Test
    public void test_13(TestInfo testInfo) {
        String code = "look(+h,a[h])";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_14(TestInfo testInfo) {
        String code = "var K=class{constructor(a,b,c){this.o=Ec(a,b,c)}toJSON(){return zc(this)}clone(){return Ic(this,a,b)?Jc(this,a,!0):new this.constructor(Hc(a,b,!1))}}";
        assertAst(testInfo, code);
    }

    @Test
    public void test_15(TestInfo testInfo) {
        String[] code = { "String.raw`Salut\\n${2 + 3}!`" };
        assertAst(testInfo, code);
    }

    @Test
    public void test_16(TestInfo testInfo) {
        String[] code = {
            "console.log(`string text line 1 ",
            "             string text line 2`);" };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_17(TestInfo testInfo) {
        String[] code = { "myTag`That ${person} is a ${age}.`" };
        assertAst(testInfo, code);
    }

    @Test
    public void test_18(TestInfo testInfo) {
        String[] code = { "console.log.bind(1, 2)`Hello`" };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_19(TestInfo testInfo) {
        String[] code = { "new Function(\"console.log(arguments)\")`Hello`" };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_20(TestInfo testInfo) {
        String[] code = { "recursive`Hello``World`" };
        assertAst(testInfo, code);
    }

    @Test
    public void test_21(TestInfo testInfo) {
        String[] code = { "addOnDisposeCallback(async()=>{})" };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_22(TestInfo testInfo) {
        String[] code = { "addOnDisposeCallback(this.async())" };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_23(TestInfo testInfo) {
        String[] code = { "_.eva((0,_.dva)`IE0Oi${a.Fa.length}cESSm${o9a((f=d)!=null?f:\"unknown\")}OWXEXe`)" };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_24(TestInfo testInfo) {
        String code = "look(...a&&o&&!Object(s.t)(o,\"a\"))";
        assertAst(testInfo, code);
    }
   
}
