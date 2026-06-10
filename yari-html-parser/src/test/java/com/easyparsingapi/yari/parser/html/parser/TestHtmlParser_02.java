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
package com.easyparsingapi.yari.parser.html.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parser.html.parser.HtmlConfig;

public class TestHtmlParser_02 {
    
    HtmlConfig htmlConfig = HtmlConfig.builder()
                                      .javascriptTag("script")
                                      .javascriptAttribute("onload")
                                      .cssTag("style")
                                      .cssAttribute("style")
                                      .acceptUnclosedTag(true)
                                      .build();
    
    @Test
    public void test_01(TestInfo testInfo) {
        String code = """
                <test> 
                    <style> 
                       < bbbb
                       > tttt
                    </style>
                    <div> 
                      aaaa
                    </div>  
                </test>    
             """;
        AssertUtil.assertHtmlAstError(testInfo, htmlConfig, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = """
                <test> 
                    <script att='yo'> 
                       < bbbb
                       > tttt
                    </script>
                    <div> 
                      aaaa
                    </div>  
                </test>    
             """;
        AssertUtil.assertHtmlAstError(testInfo, htmlConfig, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = """
                <test> 
                    <script> 
                       function fct() {
                          return 'hello'
                       }
                    </script>
                    <div> 
                      aaaa
                    </div>  
                </test>    
             """;
        AssertUtil.assertHtmlAst(testInfo, htmlConfig, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        HtmlConfig htmlConfig = HtmlConfig.builder()
                                          .javascriptTag(t-> {
                                              boolean result = false;
                                              if ("script".equals(t.markup().name())) {
                                                  if (t.attributes().size() == 0) {
                                                      result = true;
                                                  }
                                              }
                                              return result;
                                          })
                                          .acceptUnclosedTag(true)
                                          .build();
        String code = """
                <test> 
                    <script> 
                       function fct() {
                          return 'hello'
                       }
                    </script>
                    <script att="no"> 
                         <div> 
                           aaaa
                         </div>  
                    </script>
                    <div> 
                      bbbb
                    </div>  
                </test>    
             """;
        AssertUtil.assertHtmlAst(testInfo, htmlConfig, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        HtmlConfig htmlConfig = HtmlConfig.builder()
                                          .tagAsPlainText(t-> {
                                              boolean result = false;
                                              if ("script".equals(t.markup().name())) {
                                                  if (t.attributes().size() == 1
                                                          && "att".equals(t.attributes().get(0).markup().name())
                                                          && "no".equals(t.attributes().get(0).value())) {
                                                      result = true;
                                                  }
                                              }
                                              return result;
                                          })
                                          .acceptUnclosedTag(true)
                                          .build();
        String code = """
                <test> 
                    <script> 
                        <div> 
                           aaaa
                         </div>  
                    </script>
                    <script att="no"> 
                         <div> 
                           ccc
                         </div>  
                    </script>
                    <div> 
                      bbbb
                    </div>  
                </test>    
             """;
        AssertUtil.assertHtmlAst(testInfo, htmlConfig, code);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        HtmlConfig htmlConfig = HtmlConfig.builder()
                                          .tagAsPlainText(t-> {
                                              boolean result = false;
                                              if ("script".equals(t.markup().name())) {
                                                  if (t.attributes().size() == 1
                                                          && "att".equals(t.attributes().get(0).markup().name())
                                                          && "no".equals(t.attributes().get(0).value())) {
                                                      result = true;
                                                  }
                                              }
                                              return result;
                                          })
                                          .acceptUnclosedTag(true)
                                          .build();
        String code = """
                <test> 
                    <script> 
                        <div> 
                           aaaa
                         </div>  
                    </script>
                    <script att="no"/> 
                    <div> 
                      bbbb
                    </div>  
                </test>    
             """;
        AssertUtil.assertHtmlAst(testInfo, htmlConfig, code);
    }
    
}
