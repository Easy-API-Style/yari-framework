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

import static com.easyparsingapi.yari.parsec.Parsers.EOF;
import static com.easyparsingapi.yari.parsec.Parsers.always;
import static com.easyparsingapi.yari.parsec.Parsers.never;
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.error;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.id;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.num;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.parse;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.token;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil;
import com.easyparsingapi.yari.parsec.util.ast.Ast;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil.Result;

public class TestSepByBetween {
    
    @Test
    public void test_01(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, always(), 
                                                   token(","), token("("), token(")"))
                                     .map(Block::new),
                                   "(aa, 11, 33, cc, 22, zz)");
        AssertUtil.assertAstError(testInfo, result);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, 
                                                   token(","), token("("), token(")"))
                                     .map(Block::new),
                                   "(aa, bb, 11, 33, cc, 22, zz, vv)");
        AssertUtil.assertAstError(testInfo, result);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        Result<Ast> result = 
            parse(sequence(id.sepByBetween(error, id.peek(), 
                                           token(","), token("("), token(")"))
                             .map(Block::new),
                           num.many()
                              .map(Block::new)
                              .optional(), 
                           (a, b) -> {
                                  if (b != null) {
                                      a.add(b);
                                  }
                                return a;
                            }),
                            "(aa, 11, 33, cc, 22, zz) 11 22");
        AssertUtil.assertAstError(testInfo, result);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, never(), 
                                                   token(","), token("("), token(")"))
                                     .map(Block::new),
                                   "(aa, 11, cc, 22)");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, token(";"),
                                                   token(","), token("("), token(")"))
                                     .map(Block::new),
                                   "(aa, 11, ;cc, 22)");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, Parsers.or(token(";"), 
                                                                     token(","),
                                                                     id.peek()),
                                                   token(","), token("("), token(")"))
                                     .map(Block::new),
                                   "(aa, 11 ;,cc)");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        Result<Ast> result = parse(sequence(id.sepByBetween(error, always(), 
                                                           token(","), token("("), token(")"))
                                              .map(Block::new),
                                            num.many().map(Block::new).optional(), 
                                            (a, b) -> {
                                               if (b != null) {
                                                   a.add(b);
                                               }
                                               return a;
                                            }),
                                  "(aa, 11, cc) 11 22");
        AssertUtil.assertAstError(testInfo, result);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, token(","), token("("), token(")"))
                                     .map(Block::new),
                                  "(aa, 22");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_09(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, token(","), token("("), token(")"))
                                     .map(Block::new),
                                   "(aa, bb");
        AssertUtil.assertAst(testInfo, result);
    }

    @Test
    public void test_10(TestInfo testInfo) {
        try {
            parse(id.sepByBetween(error, always(), EOF,
                                  token(","), token("("), token(")"))
                    .map(Block::new),
                  "(aa, 22");
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 8, error=EmptyParseError [index=7, encountered=EOF, expected=[], unexpected=EOF]]",
                         e.toString());
        }
    }

    @Test
    public void test_11(TestInfo testInfo) {
        try {
            parse(id.sepByBetween(error, always(), never(), EOF,
                                  token(","), token("("), token(")")),
                  "(aa, 22");
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 8, error=EmptyParseError [index=7, encountered=EOF, expected=[], unexpected=EOF]]",
                         e.toString());
        }
    }

    @Test
    public void test_12(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, always(), never(), never(), 
                                                  token(","), token("("), token(")"))
                                     .map(Block::new),
                                  "(aa, 22");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_13(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, token(","), token("("), token(")"))
                                     .map(Block::new),
                                  "(22)");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_14(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, token(","), token("("), token(")"))
                                     .map(Block::new),
                                  "()");
        AssertUtil.assertAst(testInfo, result);
    }

    @Test
    public void test_15(TestInfo testInfo) {
        try {
            parse(id.sepByBetween(error, always(), token(";"), 
                                  token(","), token("("), token(")")),
                  "(aa, vv, ; dd)");
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 12, error=EmptyParseError [index=11, encountered=dd, expected=[], unexpected=token[;]]]",
                         e.toString());
        }
    }

    @Test
    public void test_16(TestInfo testInfo) {
        try {
            parse(id.sepByBetween(error, always(), token(";"), 
                                  token(","), token("("), token(")")),
                  "aa, vv, dd)");
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 1, error=EmptyParseError [index=0, encountered=aa, expected=[token[(], token[(]]]]",
                         e.toString());
        }
    }

    @Test
    public void test_17(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, token(","), token("("), token(")"))
                                     .map(Block::new),
                                   "(ff)");
        AssertUtil.assertAst(testInfo, result);
    }
    
    @Test
    public void test_18(TestInfo testInfo) {
        Result<Ast> result = parse(id.sepByBetween(error, id.peek(),
                                                   token(","), token("("), token(")"))
                                     .map(Block::new),
                                   "(aa, 11, ;,cc)");
        AssertUtil.assertAstError(testInfo, result);
    }

}
