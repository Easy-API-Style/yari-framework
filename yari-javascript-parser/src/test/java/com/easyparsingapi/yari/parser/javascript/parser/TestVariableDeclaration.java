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

public class TestVariableDeclaration {
	
    @Test
    public void test_01(TestInfo testInfo) {
        String code = "var vv = 33,kk;let cc;\n let  bibi = 'ggg'; const roro";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = "const {"
                    + "  a,"
                    + "  b: { c: d },"
                    + "} = obj;";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_O3(TestInfo testInfo) {
        String code = "let normalArray = [].slice.call(arguments)";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = "const valA = nullValue ?? \"default for A\"";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = 
                  "const obj = {"
                + "  [items]: \"Hello\""
                + "}";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = "var a=t[u]={exports:{}}";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = "const parsedURL = /^(\\w+):\\/\\/([^\\/]+)\\/(.*)$/.exec(url);";
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = "var _i = 0, _Object$keys = Object.keys(protectedAudienceMethods);";
        assertAst(testInfo, code);  
    } 
    
    @Test
    public void test_09(TestInfo testInfo) {
        String[] code = {
         "var uniqueIdentifier =",
         " source.uniqueId +",
         " source.name +",
         " '_' +",
         " (Array.isArray(args) ? args.join('_') : '');"
        };
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String[] code = {
         "const b=c=>c;"
        };
        assertAst(testInfo, code);  
    }

    @Test
    public void test_11(TestInfo testInfo) {
        String[] code = {
          "var fsB=/(url\\()([^)]*)(\\))/g,uST=/(^\\/[^\\/])|(^#)|(^[\\w-\\d]*:)/"
        };
        assertAst(testInfo, code);  
    }

    @Test
    public void test_12(TestInfo testInfo) {
        String[] code = {
          "var mustCancel = function mustCancel(value) {",
          "        if (canceled) {",
          "          return canceled;",
          "        }",
          "        canceled =",
          "          value !== undefined &&",
          "          constantValue !== undefined &&",
          "          typeof value !== typeof constantValue &&",
          "          value !== null;",
          "        return canceled;",
          "      };"
        };
        assertAst(testInfo, code);  
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String[] code = {
          "const{bvAttrs:e,computedHref:t,computedRel:n,disabled:r,target:i,routerTag:o,isRouterLink:a}=this"
        };
        assertAst(testInfo, code);  
    }
    
}
