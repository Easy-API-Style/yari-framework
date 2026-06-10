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

public class TestIf {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = "if (p_1 && p_2) "
                    + " var v_1";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "if (p_1 && p_2) {"
                    + " var v_1;"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "if (p_1 && p_2) {"
                    + " var v_1;"
                    + " function fct_2(p_3) {"
                    + "  var v_2;"
                    + "  return v_3;"
                    + " }"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "if (p_1 && p_2) {"
                    + " var v_1 = 44;"
                    + "}"
                    + "else {"
                    + " var v_2 = 'bibi'"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "if (p_1 && p_2)   {}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "if (p_1 && p_2) { /* toto */}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "if (p_1 && p_2) {"
                    + " var v_1 = 44;"
                    + "}"
                    + "else {"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "if (p_1 > p_2) {"
                    + "/* toto */"
                    + " {"
                    + "  var v_1;"
                    + "  v_1 = 'yiyi';"
                    + " }"
                    + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "if (p_1 && p_2) {"
                    + " var v_1 = 44;"
                    + "}"
                    + "else if (p_1 && p_2 || p_3) {"
                    + " var v_2 = t < 55;"
                    + "} "
                    + "else "
                    + " v_3 = 'yiyi'";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "if (p_1 && p_2) {"
                    + " var v_1 = 44;"
                    + "}"
                    + "else if (p_1 && p_2 || p_3) ;"
                    + "else "
                    + " v_3 = 'yiyi'";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = "if (a=toto, b != 3) { }";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code =  
                "if(s=e?f:d, func[pp])"
                + "{"
                + "n=!0;"
                + "e.onreadystatechange="
                + "function(){4==e.readyState&&200==e.status&&2==e.responseText.length&&(iCookieExpire=7,o=e.responseText.toUpperCase(),i.SetCookie(i.CookieKey,o,iCookieExpire),i.CookieValue=o)};"
                + "try{e.open(\"GET\",t,!0),e.withCredentials=!0,e.send(null)}"
                + "catch(r){console.log(\"cerror1\")}"
                + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String[] code = {
                "if (a > 0) ",
                " result = \"positive\";",
                "else ",
                " result = \"NOT positive\";"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_14(TestInfo testInfo) {
        String[] code = {
            "if(Y==E)return P;if(Y==K)Y=67;else if(Y==67)Y=(D-7|12)<D&&(D+8^22)>=D?32:80;else if(Y==32)P=g,Y=80;else if(Y==80)Y=(D^7)>>4?E:4;"
        };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        String[] code = {
            "if(!kc(a))throw qb(\"int64\")"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_16(TestInfo testInfo) {
        String[] code = {
            "if(!/^\\s*(?:-?[1-9]\\d*|0)?\\s*$/.test(b))throw Error(String(b));"
        };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_17(TestInfo testInfo) {
        String[] code = {
            "if(!this.Ha){var b=a.value?a.value:new Uint8Array(0);if(b=this.Ja.decode(b,{stream:!a.done}))this.response=this.responseText+=b}a.done?QJb(this):PJb(this);"
        };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_18(TestInfo testInfo) {
        String[] code = {
                "if(this.Ph.Qv()instanceof \n Array){const h=this.Ph.Qv();h.length>0&&h[0]instanceof Uint8Array&&(this.Ja=!0,a=h)}"
        };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_19(TestInfo testInfo) {
        String[] code = {
                "if (!google.stvsc){google.ml(new Error('img_giir'),false,{'id':i,},);}"
        };
        assertAst(testInfo, code);
    }
    
}
