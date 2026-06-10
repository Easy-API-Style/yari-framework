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

public class TestGeneratorFunction {

    @Test
    public void test_01(TestInfo testInfo) {
        String[] code = {
            "function* generator(i) {",
            "  yield i;",
            "  yield i + 10;",
            "}"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_02(TestInfo testInfo) {
        String[] code = {
            "const someObj = {",
            "  *generator() {",
            "    yield \"a\";",
            "    yield \"b\";",
            "  },",
            "};"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_03(TestInfo testInfo) {
        String[] code = {
            "class Foo {",
            "  *generator() {",
            "    yield 1;",
            "    yield 2;",
            "    yield 3;",
            "  }",
            "}"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_04(TestInfo testInfo) {
        String[] code = {
            "class Foo {",
            "  *[Symbol.iterator]() {",
            "    yield 1;",
            "    yield 2;",
            "  }",
            "}"
        };
        assertAst(testInfo, code);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        String[] code = {
            "const SomeObj = {",
            "  *[Symbol.iterator]() {",
            "    yield \"a\";",
            "    yield \"b\";",
            "  },",
            "};"
        };
        assertAst(testInfo, code);
    }
    
}
