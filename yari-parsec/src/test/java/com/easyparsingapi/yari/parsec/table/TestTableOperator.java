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
package com.easyparsingapi.yari.parsec.table;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.OperatorTable;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Scanners;
import com.easyparsingapi.yari.parsec.Terminals;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.functors.MapInfix;
import com.easyparsingapi.yari.parsec.functors.MapOperator;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil;
import com.easyparsingapi.yari.parsec.util.ast.Ast;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil.Result;

public class TestTableOperator {

    private static final Terminals TERMINALS = 
            Terminals.operators("+", "-", "*", "/", "(", ")")
                     .words(Scanners.IDENTIFIER)
                     .build();

    static final Parser<Void> IGNORED = Scanners.WHITESPACES.skipMany();
    
    static Parser<List<Token>> lexer = ApiParser.lexer(TERMINALS.tokenizer(), IGNORED);

    static Parser<Ast> token(String... names) {
        return TERMINALS.token(names)
                        .map(Token::toString)
                        .map(Identifier::new)
                        .label("token[" + String.join(", ", names) + "]")
                        .cast();
    }

    static Parser<Ast> identifier() {
        return Terminals.identifier()
                        .map(Identifier::new)
                        .label("identifier")
                        .cast();
    }

    static Parser<MapInfix<Ast, Ast, Ast, Infix>> infix(String name) {
        return token(name).map(o -> MapInfix.map(o, (l, r) -> new Infix(l, o, r)));
    }

    static Parser<MapOperator<Ast, Ast, Prefix>> prefix(String name) {
        return token(name).map(opartor -> MapOperator.map(opartor, operand -> new Prefix(opartor, operand)));
    }

    static Parser<Ast> calculator_left() {
        Parser.Reference<Ast> ref = Parser.newReference();
        Parser<Ast> unit =Parsers.or(ref.lazy()
                                        .between(token("("), token(")"))
                                        .map(v -> new Parenthesis(v)),
                                     identifier());
        Parser<Ast> parser = new OperatorTable<Ast>()
                .infixl(infix("+"), 10)
                .infixl(infix("-"), 10)
                .infixl(infix("*"), 20)
                .infixl(infix("/"), 20)
                .prefix(prefix("-"), 30)
                .buildMap(unit);
        ref.set(parser);
        return parser;
    }

    static Parser<Ast> calculator_right() {
        Parser.Reference<Ast> ref = Parser.newReference();
        Parser<Ast> unit =Parsers.or(ref.lazy()
                                        .between(token("("), token(")"))
                                        .map(v -> new Parenthesis(v)),
                                     identifier());
        Parser<Ast> parser = new OperatorTable<Ast>()
                .infixr(infix("+"), 10)
                .infixr(infix("-"), 10)
                .infixr(infix("*"), 20)
                .infixr(infix("/"), 20)
                .prefix(prefix("-"), 30)
                .buildMap(unit);
        ref.set(parser);
        return parser;
    }

    static Parser<MapInfix<Ast, Ast, Ast, Infix>> infixOptional(String name) {
        return token(name).optional().map(o -> MapInfix.map(o, (l, r) -> new Infix(l, o, r)));
    }
    
    static Parser<Ast> calculator_none() {
        Parser.Reference<Ast> ref = Parser.newReference();
        Parser<Ast> unit =Parsers.or(ref.lazy()
                                        .between(token("("), token(")"))
                                        .map(v -> new Parenthesis(v)),
                                     identifier());
        Parser<Ast> parser = new OperatorTable<Ast>()
                .infixn(infixOptional("-"), 10)
                .infixn(infixOptional("*"), 20)
                .buildMap(unit);
        ref.set(parser);
        return parser;
    }

    static <V>  Result<V> parse(Parser<V> parser, String source) {
        List<Token> tokens = ApiParser.lex(lexer, source);
        V ast = ApiParser.parse(parser, lexer, source);
        return new Result<>(tokens, ast);
    }

    @Test
    public void test_01(TestInfo testInfo) {
        Result<Ast> result = parse(calculator_left(), "a+b");
        AssertUtil.assertAst(testInfo, result);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        Result<Ast> result = parse(calculator_left(), "a+(b*c)");
        AssertUtil.assertAst(testInfo, result);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        try {
            Result<Ast> result = parse(calculator_left(), "a(b*c)");
            fail("Exception Expected!");
        }
        catch (Exception e) {
            assertEquals("ParserException [location=line 1 column 2, error=EmptyParseError [index=1, encountered=(, expected=[token[*], token[/], token[+], token[-]]]]",
                         e.toString());
        }
    }

    @Test
    public void test_04(TestInfo testInfo) {
        Result<Ast> result = parse(calculator_left(), "-(b*c)");
        AssertUtil.assertAst(testInfo, result);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        Result<Ast> result = parse(calculator_right(), "a+(b*s)*(e/(f+s))*s");
        AssertUtil.assertAst(testInfo, result);
    }

    @Test
    public void test_06(TestInfo testInfo) {
        Result<Ast> result = parse(calculator_left(), "a+(b*s)*(e/(f+s))*s");
        AssertUtil.assertAst(testInfo, result);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        Result<Ast> result = parse(calculator_none(), "a  (b * s (f * s(f * s)) )(e(f * s)) (s - d)");
        AssertUtil.assertAst(testInfo, result);
    }

}
