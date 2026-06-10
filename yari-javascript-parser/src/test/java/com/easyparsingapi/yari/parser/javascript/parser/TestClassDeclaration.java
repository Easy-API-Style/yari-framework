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

public class TestClassDeclaration {
    
    @Test
    public void test_01(TestInfo testInfo) {
        String code = 
            "class Rectangle { "
          + "  constructor(height, width) { "
          + "    this.name = 'Rectangle'; "
          + "    this.height = height; "
          + "    this.width = width; "
          + "  } "
          + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String code = 
            "class FilledRectangle extends Rectangle { "
            + "  constructor(height, width, color) { "
            + "    super(height, width); "
            + "    this.name = 'Filled rectangle'; "
            + "    this.color = color; "
            + "  } "
            + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String code = "var Foo = class {}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String code = 
              "class A {"
            + "  static field = 'Inner y';"
            + "  static {"
            + "    var y = this.field;"
            + "  }"
            + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String code = 
            "class A {"
          + "  static fieldA = 'A.fieldA';"
          + "  static fieldB = 'A.fieldB';"
          + "}"
          + "class B extends A {"
          + "  static {"
          + "    let x = super.fieldA;"
          + "  }"
          + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_06(TestInfo testInfo) {
        String code = 
              "class ClassWithInstanceField {"
            + "  baseInstanceField = 'base field';"
            + "  anotherBaseInstanceField = this.baseInstanceField;"
            + "  baseInstanceMethod() { return 'base method output' }"
            + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_07(TestInfo testInfo) {
        String code = 
              "class ClassWithFancyMethods {"
            + "  *generatorMethod() { }"
            + "  async asyncMethod() { }"
            + "  async *asyncGeneratorMethod() { }"
            + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        String code = 
            "class ClassWithGetSet {"
          + "  #msg = 'hello world';"
          + "  get msg() {"
          + "   return this.#msg"
          + "  }"
          + "  set msg(x) {"
          + "    this.#msg = `hello ${x}`"
          + " }"
          + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_09(TestInfo testInfo) {
        String code = 
              "class ClassWithPrivateStaticField {"
            + "  static #PRIVATE_STATIC_FIELD;"
            + "  static publicStaticMethod() {"
            + "    ClassWithPrivateStaticField.#PRIVATE_STATIC_FIELD = 42;"
            + "    return ClassWithPrivateStaticField.#PRIVATE_STATIC_FIELD;"
            + "  }"
            + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        String code = 
            "class Base {\n"
          + "    static #privateStaticMethod1() {"
          + "        return 42;"
          + "    }"
          + "    static publicStaticMethod2() {"
          + "        return Base.#privateStaticMethod1();"
          + "    }"
          + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        String code = 
            "class ClassWithComputedFieldName {"
          + "    [`${PREFIX}Field`] = 'prefixed field'"
          + "}";
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_12(TestInfo testInfo) {
        String[] code = {
            "class ClassWithStaticField {",
            "  static baseStaticField = 'base static field'",
            "  static anotherBaseStaticField = this.baseStaticField",
            "",
            "  static baseStaticMethod() { return 'base static method output' }",
            "}"
        };
        assertAst(testInfo, code);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        String[] code = {
            "class ValidationError extends Error {",
            "  printCustomerMessage() {",
            "    return `Validation failed :-( (details: ${this.message})`;",
            "  }",
            "}"
        };
        assertAst(testInfo, code);
    }
    
}
