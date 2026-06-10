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
import static com.easyparsingapi.yari.parsec.Parsers.never;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.error;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.id;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.parse;
import static com.easyparsingapi.yari.parsec.retry.parsing.TestParser.token;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parsec.util.ast.AssertUtil;
import com.easyparsingapi.yari.parsec.util.ast.Ast;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil.Result;

public class TestBetween {
    
    @Test
    public void test_01(TestInfo testInfo) {
        Result<Ast> result = parse(id.between(error, token("("), token(")")), "(dd)");
        AssertUtil.assertAst(testInfo, result);
    }

    @Test
    public void test_02(TestInfo testInfo) {
        Result<Ast> result = parse(id.between(error, token("("), token(")")), "(22)");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_03(TestInfo testInfo) {
        Result<Ast> result = parse(id.between(error, token("("), token(")")), "()");
        assertNull(result.ast());
    }

    @Test
    public void test_04(TestInfo testInfo) {
        Result<Ast> result = parse(id.between(error, token("("), token(")")), "(aa");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_05(TestInfo testInfo) {
        Result<Ast> result = parse(id.between(error, EOF, never(), token("("), token(")")), "(aa");
        AssertUtil.assertAstError(testInfo, result);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        try {
            parse(id.between(error, never(), EOF, token("("), token(")")), "(aa");
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 4, error=EmptyParseError [index=3, encountered=EOF, expected=[token[)], token[)]]]]",
                         e.toString());
        }
    }

    @Test
    public void test_07(TestInfo testInfo) {
        try {
            parse(id.between(error, token(";"), token("("), token(")")), "(a;b)");
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 4, error=EmptyParseError [index=3, encountered=b, expected=[], unexpected=token[;]]]",
                         e.toString());
        }
    }

    @Test
    public void test_08(TestInfo testInfo) {
        try {
            parse(id.between(error, never(), EOF, token("("), token(")")), "aa)");
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 1, error=EmptyParseError [index=0, encountered=aa, expected=[token[(], token[(]]]]",
                         e.toString());
        }
    }
    
}
