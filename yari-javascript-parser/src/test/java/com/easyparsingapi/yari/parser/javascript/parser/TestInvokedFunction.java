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

public class TestInvokedFunction {
    
    @Test
    public void test_01(TestInfo testInfo) {
        final String code = "(async()=>console.log())()";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        final String code = "((a, b, c)=>console.log(await resolveAfter2Seconds()))(a, b, 20)";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        final String code =  
                "(async function(x) {"
              + "  let p_a = resolveAfter2Seconds(20);"
              + "  let p_b = resolveAfter2Seconds(30);"
              + "  return x + await p_a + await p_b;"
              + "})(10).then(v => {"
              + "  console.log(v);"
              + "})";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        final String code =  
                    "(() => {"
                  + "  for await (const num of asyncIterable) {"
                  + "    console.log(num);"
                  + "  }"
                  + "})()";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        final String code =  
                "(function(w,d,s,l,i) {"
              + " w[l]=w[l]||[];"
              + " w[l].push({'gtm.start': new Date().getTime(),event:'gtm.js'});"
              + " var f=d.getElementsByTagName(s)[0], j=d.createElement(s), dl=l!='dataLayer'?'&l='+l:'';"
              + " j.async=true;"
              + " j.src='https://www.googletagmanager.com/gtm.js?id='+i+dl;"
              + " f.parentNode.insertBefore(j,f);"
              + "}) (window, document, 'script', 'dataLayer', 'GTM-KJHB49');";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        final String code =  
                      "(function(i,s,o,g,r,a,m){"
                    + " i['GoogleAnalyticsObject']=r;"
                    + " i[r]=function(){ (i[r].q=i[r].q||[]).push(arguments) }"
                    + "})(window, document, 'script','https://www.google-analytics.com/analytics.js', 'ga');";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        final String code =  
              "(function(i,s,o,g,r,a,m){"
            + " i['GoogleAnalyticsObject']=r;"
            + " i[r]= i[r] || function(){ (i[r].q=i[r].q||[]).push(arguments), }"
            + "})(window, document, 'script','https://www.google-analytics.com/analytics.js', 'ga');";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        final String code = "(i = q && q).toto()";
        assertAst(testInfo, code);
    }

    @Test
    public void test_09(TestInfo testInfo) {
        final String code = "(function (p_1, p_2) { console.log('ok') }(v_1, v_2))";
        assertAst(testInfo, code);
    }

    @Test
    public void test_10(TestInfo testInfo) {
        final String code = "(function (p_1, p_2) { console.log('ok') } )(v_1, v_2)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_11(TestInfo testInfo) {
        final String code = "var maVariable = function (p) { t[r] = p }(100)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_12(TestInfo testInfo) {
        final String code = "var maVariable = function (p) { t[r] = p }(100)";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        final String code = "var maVariable = true && function (p) { t[r] = p }(100)";
        assertAst(testInfo, code);
    }

    @Test
    public void test_14(TestInfo testInfo) {
        final String code = "var maVariable = (0, function (p) { t[r] = p }(100))";
        assertAst(testInfo, code);
    }

    @Test
    public void test_15(TestInfo testInfo) {
        final String code = 
                  " var compteur = (function () {"
                + "        var i = 0; // propriété privée\n"
                + "        return {   // méthodes publiques\n"
                + "            obtenir: function () {"
                + "                alert(i);"
                + "            },"
                + "            mettre: function (valeur) {"
                + "                i = valeur;"
                + "            },"
                + "            incrementer: function () {"
                + "                alert(++i);"
                + "            }"
                + "        };"
                + "    })();";
        assertAst(testInfo, code);
    }
    
}
