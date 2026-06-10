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
package com.easyparsingapi.yari.parsec.retry.parsing;

import static com.easyparsingapi.yari.parsec.Parsers.always;
import static com.easyparsingapi.yari.parsec.Parsers.never;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.error;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.id;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.identifier;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.num;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.parse;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.token;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parsec.util.ast.AssertUtil;
import com.easyparsingapi.yari.parsec.util.ast.Ast;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil.Result;

public class TestCatchError {
    
  @Test
  public void test_01(TestInfo testInfo) {
      Result<Ast> result = parse(id.many().map(Block::new),
                                 "aa bb cc");
      AssertUtil.assertAst(testInfo, result);
  }
  
  @Test
  public void test_02(TestInfo testInfo) {
      Result<Ast> result = parse(id.catchError(error, always(), never(), never())
                                   .many()
                                   .map(Block::new),
                                 "aa bb cc 11 55 vv");
      AssertUtil.assertAstError(testInfo, result);
  }

  @Test
  public void test_03(TestInfo testInfo) {
      Result<Ast> result = parse(id.catchError(error, always(), num, never())
                                   .many()
                                   .map(Block::new),
                                 "aa bb cc 11 55 vv");
      AssertUtil.assertAstError(testInfo, result);
  }

  @Test
  public void test_04(TestInfo testInfo) {
      try {
          Result<Ast> result = parse(id.catchError(error, always(), never(), num)
                                       .many()
                                       .map(Block::new),
                                     "aa bb cc 11 55 vv");
          fail("Exception Expected!");
      }
      catch (Exception e) {
          assertEquals("ParserException [location=line 1 column 13, error=EmptyParseError [index=12, encountered=55, expected=[], unexpected=number]]",
                       e.toString());
      }
  }

  @Test
  public void test_05(TestInfo testInfo) {
      try {
          Result<Ast> result = parse(id.catchError(error, always(), never(), num.peek())
                                       .many()
                                       .map(Block::new),
                                     "aa bb cc 11 55 vv");
          fail("Exception Expected!");
      }
      catch (Exception e) {
          assertEquals("ParserException [location=line 1 column 10, error=EmptyParseError [index=9, encountered=11, expected=[identifier]]]",
                       e.toString());
      }
  }
  
  @Test
  public void test_06(TestInfo testInfo) {
      Result<Ast> result = parse(id.catchError(error, identifier("ee"), never(), never())
                                   .many()
                                   .map(Block::new),
                                 "aa bb cc 11 55 vv dd ee uu ii");
      AssertUtil.assertAstError(testInfo, result);
  }

  @Test
  public void test_07(TestInfo testInfo) {
      Result<Ast> result = parse(id.catchError(error, identifier("ee").peek(), never(), never())
                                   .many()
                                   .map(Block::new),
                                 "aa bb cc 11 55 vv dd ee uu ii");
      AssertUtil.assertAstError(testInfo, result);
  }

  @Test
  public void test_08(TestInfo testInfo) {
      Result<Ast> result = parse(id.catchError(error, identifier("ee").peek(), never(), never())
                                   .many()
                                   .map(Block::new),
                                 "aa bb cc 11 55 vv dd ee uu ii");
      AssertUtil.assertAstError(testInfo, result);
  }

  @Test
  public void test_09(TestInfo testInfo) {
      Result<Ast> result = parse(id.catchError(error, always(), num, num.peek())
                                   .many()
                                   .map(Block::new),
                                 "aa bb cc 11 55 vv");
      AssertUtil.assertAstError(testInfo, result);
  }
  
  @Test
  public void test_10(TestInfo testInfo) {
      try {
          parse(id.catchError(error, always(), token(","), token(";"))
                  .many()
                  .map(Block::new),
                "aa bb cc 11, 55 ; vv");
          fail("Exception Expected!");
      }
      catch (Exception e) {
          assertEquals("ParserException [location=line 1 column 19, error=EmptyParseError [index=18, encountered=vv, expected=[], unexpected=token[;]]]",
                       e.toString());
      }
  }
  
  @Test
  public void test_11(TestInfo testInfo) {
      Result<Ast> result = parse(id.catchError(error, always(), token(","), never())
                                   .many()
                                   .map(Block::new),
                                 "aa bb cc 11, 55 ; vv");
      AssertUtil.assertAstError(testInfo, result);
  }

  @Test
  public void test_12(TestInfo testInfo) {
      Result<Ast> result = parse(id.catchError(error, id.peek(), token(","), never())
                                   .many()
                                   .map(Block::new),
                                 "aa bb cc 11, 55 ; vv");
      AssertUtil.assertAstError(testInfo, result);
  }

  @Test
  public void test_13(TestInfo testInfo) {
      try {
          parse(id.catchError(error, id.peek(), never(), token(";"))
                  .many()
                  .map(Block::new),
              "aa bb cc 11, 55 ; vv");
          fail("Exception Expected!");
      }
      catch (Exception e) {
          assertEquals("ParserException [location=line 1 column 19, error=EmptyParseError [index=18, encountered=vv, expected=[], unexpected=token[;]]]",
                       e.toString());
      }
  }
    
}
