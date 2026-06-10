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
package com.easyparsingapi.yari.parser.css.parser;

import static com.easyparsingapi.yari.parser.css.parser.AssertUtil.assertAst;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestPseudoFucntion {

    @Test
    public void test_01(TestInfo testInfo) {
        String code =  """
           :dir(rtl) {
               background-color: red;
           }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code =  """
           h1:has(+ p) {
             margin-bottom: 0;
           }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code =  """
          :heading(1, 3) {
            color: tomato;
          }
          :heading(2, 4) {
            color: slateblue;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String code =  """
            :host(h1) {
              color: red;
            }
            
            :host(#shadow-dom-host) {
              border: 2px dashed blue;
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code =  """
            ol {
              list-style-type: upper-alpha;
              color: darkblue;
            }
            
            :is(ol, ul, menu:unsupported) :is(ol, ul) {
              color: green;
            }
            
            :is(ol, ul) :is(ol, ul) ol {
              list-style-type: lower-greek;
              color: chocolate;
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code =  """
              :lang("nl", "de") {
                color: green;
              }
        
              /* Omitting quotes & case-insensitive matching */
              :lang(EN, FR) {
                color: blue;
              }
        
              /* Wildcard matching a language range */
              :lang("*-Latn") {
                color: red;
              }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        String code =  """
              p:not(.irrelevant) {
                font-weight: bold;
              }
        
              p > strong,
              p > b.important {
                color: crimson;
              }
        
              p > :not(strong, b.important) {
                color: darkmagenta;
              }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_08(TestInfo testInfo) {
        String code =  """
            p:nth-child(n)  {
              font-weight: bold;
            }
            li:nth-child(-n + 3) {
              border: 2px solid orange;
              margin-bottom: 1px;
            }
            li:nth-child(even) {
              background-color: lightyellow;
            }
            li:nth-child(even of .noted) {
              background-color: tomato;
              border-bottom-color: seagreen;
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code =  """
            .premier span:nth-child(1),
            .deuxieme span:nth-child(-n+3),
            .troisieme span:nth-of-type(2n+1)
            .quatrieme span:nth-of-type(4n) {
              background-color: tomato;
            }
        """;
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code =  """
          span:nth-last-of-type(2) {
            background-color: lime;
          }
          p.fancy:nth-of-type(2n + 1) {
            text-decoration: underline;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_11(TestInfo testInfo) {
        String code =  """
          labeled-checkbox:state(checked) {
            border: solid;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_12(TestInfo testInfo) {
        String code =  """
          :host(:state(checked))::before {
             content: "[x]";
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_13(TestInfo testInfo) {
        String code =  """
           :where(ol, ul, menu:unsupported) :where(ol, ul) {
              color: green;
           }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_14(TestInfo testInfo) {
        String code =  """
           ::highlight(rainbow-color-1) {
              color: violet;
              text-decoration: underline;
           }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_15(TestInfo testInfo) {
        String code =  """
            tabbed-custom-element::part(tab):hover {
              background-color: black;
              color: white;
            }
            
            tabbed-custom-element::part(tab active) {
              border-color: blue !important;
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_16(TestInfo testInfo) {
        String code =  """
            ul::scroll-button(*):disabled {
              opacity: 0.2;
              cursor: unset;
            }
            ul::scroll-button(left) {
                content: "◄";
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_17(TestInfo testInfo) {
        String code =  """
            ::slotted(.content) {
               background-color: aqua;
            }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_18(TestInfo testInfo) {
        String code =  """
                :root::view-transition-group(*) {
                  position: absolute;
                  top: 0;
                  left: 0;
                
                  animation-duration: 0.25s;
                  animation-fill-mode: both;
                }
                ::view-transition-image-pair(root) {
                  isolation: auto;
                }
                :root::view-transition-old(*),
                :root::view-transition-new(*) {
                  position: absolute;
                  inset-block-start: 0;
                  inline-size: 100%;
                  block-size: auto;
                
                  animation-duration: inherit;
                  animation-fill-mode: inherit;
                  animation-delay: inherit;
                }
                ::view-transition-new(figure-caption) {
                  height: auto;
                  right: 0;
                  left: auto;
                  transform-origin: right center;
                }
        """;
        assertAst(testInfo, code);
    }

}
