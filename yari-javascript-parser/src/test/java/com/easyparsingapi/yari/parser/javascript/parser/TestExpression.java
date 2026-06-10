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
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.array;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableCallExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableInvokedFunction;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableQualifiedExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.atomic;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.awaitStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.classDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.debuggerStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.deleteStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.functionDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.importFunctionCall;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.invokedFunction;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.newStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.objectDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.ternary;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.yield;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestExpression {

    private void assertJavascript(TestInfo testInfo, String... code) {
        assertAstExpression(testInfo, 
                            Set.of(ternary,              
                                   assignableQualifiedExpression,   
                                   assignableCallExpression, 
                                   assignableInvokedFunction, 
                                   invokedFunction,           
                                   functionDeclaration,
                                   yield,   
                                   newStatement,              
                                   awaitStatement,            
                                   deleteStatement,           
                                   importFunctionCall,        
                                   objectDeclaration,         
                                   classDeclaration,          
                                   debuggerStatement,         
                                   array,                     
                                   atomic),                
                            code);
    }

    @Test
    public void test_01(TestInfo testInfo) {
        String code = "toto+(3+(4.9*bibi))";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "toto";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "(toto)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "toto+(3+(-4.9*bibi))";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "2-5";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "2-(-5)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "2-(-5.6)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "2-(- a)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "2+(--a)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "--a";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = "2+(a--)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = "a--";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = "a>=b";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_14(TestInfo testInfo) {
        String code = "t > 55";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        String code = "typeof arguments";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_16(TestInfo testInfo) {
        String code = "x + await p_a + await p_b";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_17(TestInfo testInfo) {
        String code = "2.0 / 0     // Infinity\n";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_18(TestInfo testInfo) {
        String code = "(function(x) {  })(10).toto().titi";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_20(TestInfo testInfo) { 
        String code = "[ a, b , [c, d], f]";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_21(TestInfo testInfo) {
        String code = "({}) instanceof Object";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_22(TestInfo testInfo) {
        String code = "a() + c()";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_35(TestInfo testInfo) {
        String code = "(value + 30) * 6.6";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_36(TestInfo testInfo) {
        String code = "toto";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_37(TestInfo testInfo) {
        try {
            String code = "(toto) (bibi)";
            assertJavascript(testInfo, code);
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 8, error=EmptyParseError [index=7, encountered=(, expected=[token[KEYWORD][**], token[KEYWORD][*], token[KEYWORD][/], token[KEYWORD][%], token[KEYWORD][-], phrase[- -], token[KEYWORD][+], phrase[+ +], token[KEYWORD][^], token[KEYWORD][|], token[KEYWORD][&], token[KEYWORD][&amp;], token[KEYWORD][>>>], token[KEYWORD][&gt;&gt;&gt;], token[KEYWORD][&#62;&#62;&#62;], token[KEYWORD][<<<], token[KEYWORD][&lt;&lt;&lt;], token[KEYWORD][&#60;&#60;&#60;], token[KEYWORD][>>], token[KEYWORD][&gt;&gt;], token[KEYWORD][&#62;&#62;], token[KEYWORD][<<], token[KEYWORD][&lt;&lt;], token[KEYWORD][&#60;&#60;], token[KEYWORD][<=], token[KEYWORD][&lt;=], token[KEYWORD][&#60;=], token[KEYWORD][>=], token[KEYWORD][&gt;=], token[KEYWORD][&#62;=], token[KEYWORD][+], token[KEYWORD][-], token[KEYWORD][in], token[KEYWORD][instanceof], token[KEYWORD][<], token[KEYWORD][&lt;], token[KEYWORD][&#60;], token[KEYWORD][>], token[KEYWORD][&gt;], token[KEYWORD][&#62;], token[KEYWORD][!==], token[KEYWORD][!=], token[KEYWORD][===], token[KEYWORD][==], token[KEYWORD][??], token[KEYWORD][||], token[KEYWORD][&&], token[KEYWORD][&amp;&amp;], token[KEYWORD][&#38;&#38;], token[KEYWORD][;], fragment[RETURN_CARRIAGE]]]]",
                         e.toString());
        }
    }
    
    @Test
    public void test_38(TestInfo testInfo) {
        String code = "window.XMLHttpRequest&&(e=new XMLHttpRequest,\"withCredentials\"in e)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_39(TestInfo testInfo) {
        String code = "/ab+c/i;";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_40(TestInfo testInfo) {
        String code = "v || /(\\w+)\\s(\\w+)/";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_41(TestInfo testInfo) {
        String code = "c===void 0&&(c=d.length)";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_42(TestInfo testInfo) {
        String code = "function(D){return\"\"+D}(z).join(\"A\")";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_43(TestInfo testInfo) {
        String code = "function(D){return\"\"+D}(z)(Array(Math.random()*7824|0))";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_44(TestInfo testInfo) {
        String code = "(function(D){return\"\"+D}(z))(A)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_45(TestInfo testInfo) {
        String code = "(function(D){return\"\"+D})(z)(A)";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_46(TestInfo testInfo) {
        String code = "(function(D){return\"\"+D}(z))[A]";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_47(TestInfo testInfo) {
        String code = "(function(D){return\"\"+D})(z)[A]";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_48(TestInfo testInfo) {
        String code = "typeof Symbol===\"function\"&&typeof Symbol()===\"symbol\"";
        assertJavascript(testInfo, code);
    }
    
    @Test
    public void test_49(TestInfo testInfo) {
        String code = "a?c&&Symbol.for&&a?Symbol.for(a):a!=null?Symbol(a):Symbol():b";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_50(TestInfo testInfo) {
        String code = "C>=4294967296&&(D+=Math.trunc(C/4294967296),D>>>=0,C>>>=0)";
        assertJavascript(testInfo, code);
    }

    @Test
    public void test_51(TestInfo testInfo) {
        String code = "Number.NaN === NaN";
        assertJavascript(testInfo, code);
    }
    
}
