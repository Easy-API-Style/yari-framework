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

public class TestAttributeSelector {

    @Test
    public void test_01(TestInfo testInfo) {
        String code =  """
           a[title] {
                color: purple;
           }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_02(TestInfo testInfo) {
        String code =  """
           a[href="https://example.org"]
           {
                color: green;
           }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_03(TestInfo testInfo) {
        String code =  """
           a[href|="example"] {
              font-size: 2em;
           }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String code =  """
           a[href$=".org"] {
             font-style: italic;
           }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        String code =  """
           a[class~="logo"] {
               padding: 2px;
           }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        String code =  """
          a[href^="insensitive" i] {
             color: cyan;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        String code =  """
          a[href*="cAsE" s] {
            color: pink;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_08(TestInfo testInfo) {
        String code =  """
          div[data-lang="zh-TW"] {
             color: purple;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_09(TestInfo testInfo) {
        String code =  """
          ol[type="a" s] {
              list-style-type: lower-alpha;
              background: lime;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_10(TestInfo testInfo) {
        String code =  """
          div:not([lang]) {
              font-style: italic;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_11(TestInfo testInfo) {
        String code =  """
          ol[type="a"]:first-child {
            list-style-type: lower-alpha;
            background: red;
          }
        """;
        assertAst(testInfo, code);
    }

    @Test
    public void test_12(TestInfo testInfo) {
        String code =  """
          a[href^="https://"][href$=".org"]
          {
             color: green;
          }
        """;
        assertAst(testInfo, code);
    }
    
}
