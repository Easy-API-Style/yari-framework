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

public class TestAssignment {

    @Test
    public void test_01(TestInfo testInfo) {
        String code = "v='toto'";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "v = 'toto'";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "v += 1000";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "v-=bibi[2]";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = "v ||= bibi(2)";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "v >>>= bibi.popo.fafa";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "gogo[gg] **= bibi.popo.fafa";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "vv = 33,kk";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = "b >>>= 2";
        assertAst(testInfo, code);    
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = "this.nom = 'Polygone'";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = "x = [1, 2, 3, 4, 5]";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String code = "x = y = z";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String code = "x = y = z()";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_14(TestInfo testInfo) {
        String code = "a = true &amp;&amp; false ";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        String code = "a = true &#38;&#38; false ";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_16(TestInfo testInfo) {
        String code = "bar /= 2 // bibi\n; bar /= 2 // mimi \n";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_17(TestInfo testInfo) {
        String code = "bar /= 2; bar /= 2";
        assertAst(testInfo, code);
    }

    @Test
    public void test_18(TestInfo testInfo) {
        String code = "i[r]=function(){ (i[r].q=i[r].q||[]).push(arguments) }";
        assertAst(testInfo, code);
    }

    @Test
    public void test_19(TestInfo testInfo) {
        String code = "t=/.*pcmag\\.com$/.test(location.hostname)?\"//geo.pcmag.com/loc/country.php\":\"//geo.ziffdavis.com/loc/country.php\"";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_20(TestInfo testInfo) {
        String code = "s=e?f:d";
        assertAst(testInfo, code);
    }

    @Test
    public void test_21(TestInfo testInfo) {
        String code = "v += (1000)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_22(TestInfo testInfo) {
        String code = "({ a, b } = obj)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_23(TestInfo testInfo) {
        String code = "({ a: a1, b: b1 } = obj)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_24(TestInfo testInfo) {
        String code = "({ a: a1 = aDefault, b = bDefault } = obj)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_25(TestInfo testInfo) {
        String code = "({ a, b, ...rest } = obj)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_26(TestInfo testInfo) {
        String code = "({ a: a1, b: b1, ...rest } = obj)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_27(TestInfo testInfo) {
        String code = "ha=/\\[native code\\]/";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_28(TestInfo testInfo) {
        String code = "[...Object.values({Za:1,Ya:2,Xa:4,gb:8,mb:16,eb:32,Pa:64,Va:128,Ta:256,lb:512,Ua:1024,Wa:2048,fb:4096,ab:8192})] = a";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_29(TestInfo testInfo) {
        String code = "g=(h=(l=n[wb])!=null?l:n[wb]=new uc)[f]";
        assertAst(testInfo, code);
    }

    @Test
    public void test_30(TestInfo testInfo) {
        String code = "e=d.Na(...e)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_31(TestInfo testInfo) {
        String code = "var window=this;";
        assertAst(testInfo, code);
    }

    @Test
    public void test_32(TestInfo testInfo) {
        String code = "nHb=/^[a-z0-9-_/]+(callback:\\d+)?$/i;";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_33(TestInfo testInfo) {
        String code = "d=d.replace(/([\"' :.[\\],=])/g,\"\\$1\")";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_34(TestInfo testInfo) {
        String[] code = {
            "this.response=this.responseText+=b"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_35(TestInfo testInfo) {
        String[] code = {
            "v={type:Boolean,default:!1}"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_36(TestInfo testInfo) {
        String[] code = {
            "v={type:String,default:void 0}"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_37(TestInfo testInfo) {
        String[] code = {
            "v = 2.0 / -0.0"
        };
        assertAst(testInfo, code);
    }
    
}
