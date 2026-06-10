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

import static com.easyparsingapi.yari.core.ast.AstUnit.Position.after;
import static com.easyparsingapi.yari.core.ast.AstUnit.Position.before;
import static com.easyparsingapi.yari.core.ast.AstUnit.Position.between;
import static com.easyparsingapi.yari.parser.javascript.parser.AssertUtil.toSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstResult;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptComment;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptParser;

public class TestCommentService {

    @Test
    public void test_01(TestInfo testInfo) {
        String[] code = {
            "if (p_1 && p_2) {", 
            "   var v_1;", 
            "}"};
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(toSource(code));
        assertEquals(0, javascriptUnit.unit().astComments().size());
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String[] code = {
            "// test 0",
            "// test 1",
            "if/* test 3 */ (/* test 4 */ p_1 /* test 2 */ && /* test 5 */ p_2) /* test 6 */ /* test 11 */{", 
            "   // test 7",
            "   var /* test 8 */ v_1; /* test 9 */", 
            "} // test 10"};
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(AssertUtil.toSource(code));
        Javascript javascript = javascriptUnit.unit();
        List<AstNode> nodes = javascript.astStream().toList();
        assertEquals(12, javascript.astComments().size());
        // javascript
        assertEquals(List.of("// test 0", "// test 1"), 
                     toString(javascript.astCommentsOf(nodes.get(0), before)));
        // if
        assertEquals(List.of("// test 0", "// test 1"),
                     toString(javascript.astCommentsOf(nodes.get(1), before)));
        // if.block
        assertEquals(List.of("// test 0", "// test 1"),
                     toString(javascript.astCommentsOf(nodes.get(2), before)));
        // if.condition
        assertEquals(List.of("/* test 3 */"),
                     toString(javascript.astCommentsOf(nodes.get(3), before)));
        // infix
        assertEquals(List.of("/* test 4 */"),
                     toString(javascript.astCommentsOf(nodes.get(4), before)));
        // infix p_1  
        assertEquals(List.of("/* test 4 */"),
                     toString(javascript.astCommentsOf(nodes.get(5), before)));
        // infix Operator
        assertEquals(List.of("/* test 2 */"),
                     toString(javascript.astCommentsOf(nodes.get(6), before)));
        // infix p_2  
        assertEquals(List.of("/* test 5 */"),
                     toString(javascript.astCommentsOf(nodes.get(7), before)));
        // If.Procedure
        assertEquals(List.of("/* test 6 */", "/* test 11 */"),
                     toString(javascript.astCommentsOf(nodes.get(8), before)));
        // VariableDeclaration var v_1
        assertEquals(List.of("// test 7"),
                     toString(javascript.astCommentsOf(nodes.get(9), before)));
        // VariableDeclaration.Type var
        assertEquals(List.of("// test 7"),
                     toString(javascript.astCommentsOf(nodes.get(10), before)));
        // VariableDeclaration.Variable v_1
        assertEquals(List.of("/* test 8 */"),
                     toString(javascript.astCommentsOf(nodes.get(11), before)));
        // VariableDeclaration.Variable.Identifier v_1
        assertEquals(List.of("/* test 8 */"),
                     toString(javascript.astCommentsOf(nodes.get(12), before)));
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String[] code = {
            "// test 0",
            "// test 1",
            "if/* test 3 */ (/* test 4 */ p_1 /* test 2 */ && /* test 5 */ p_2 /* test 12 */ )/* test 6 */ /* test 11 */{", 
            "   // test 7",
            "   var /* test 8 */ v_1; /* test 9 */", 
            "   // test 15",
            "   let /* test 13 */ v_2  /* test 16 */; /* test 14 */", 
            "} // test 10"};
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(AssertUtil.toSource(code));
        Javascript javascript = javascriptUnit.unit();
        List<AstNode> nodes = javascript.astStream().toList();
        assertEquals(17, javascript.astComments().size());
        // javascript
        assertEquals(List.of("// test 10"), 
                     toString(javascript.astCommentsOf(nodes.get(0), after)));
        // if
        assertEquals(List.of("// test 10"),
                     toString(javascript.astCommentsOf(nodes.get(1), after)));
        // if.block
        assertEquals(List.of("// test 10"),
                     toString(javascript.astCommentsOf(nodes.get(2), after)));
        // if.condition
        assertEquals(List.of("/* test 6 */", "/* test 11 */"),
                     toString(javascript.astCommentsOf(nodes.get(3), after)));
        // infix
        assertEquals(List.of("/* test 12 */"),
                     toString(javascript.astCommentsOf(nodes.get(4), after)));
        // infix p_1  
        assertEquals(List.of("/* test 2 */"),
                     toString(javascript.astCommentsOf(nodes.get(5), after)));
        // infix Operator
        assertEquals(List.of("/* test 5 */"),
                     toString(javascript.astCommentsOf(nodes.get(6), after)));
        // infix p_2  
        assertEquals(List.of("/* test 12 */"),
                     toString(javascript.astCommentsOf(nodes.get(7), after)));
        // If.Procedure
        assertEquals(List.of("// test 10"),
                     toString(javascript.astCommentsOf(nodes.get(8), after)));
        // VariableDeclaration var v_1
        assertEquals(List.of("/* test 9 */", "// test 15"),
                     toString(javascript.astCommentsOf(nodes.get(9), after)));
        // VariableDeclaration.Type var
        assertEquals(List.of("/* test 8 */"),
                     toString(javascript.astCommentsOf(nodes.get(10), after)));
        // VariableDeclaration.Variable v_1
        assertEquals(List.of("/* test 9 */", "// test 15"),
                     toString(javascript.astCommentsOf(nodes.get(11), after)));
        // VariableDeclaration.Variable.Identifier v_1
        assertEquals(List.of("/* test 9 */", "// test 15"),
                     toString(javascript.astCommentsOf(nodes.get(12), after)));
        // VariableDeclaration var v_2
        assertEquals(List.of("/* test 16 */", "/* test 14 */"),
                     toString(javascript.astCommentsOf(nodes.get(13), after)));
        // VariableDeclaration.Type let
        assertEquals(List.of("/* test 13 */"),
                     toString(javascript.astCommentsOf(nodes.get(14), after)));
        // VariableDeclaration.Variable v_2
        assertEquals(List.of("/* test 16 */", "/* test 14 */"),
                     toString(javascript.astCommentsOf(nodes.get(15), after)));
        // VariableDeclaration.Variable.Identifier v_2
        assertEquals(List.of("/* test 16 */", "/* test 14 */"),
                     toString(javascript.astCommentsOf(nodes.get(16), after)));
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String[] code = {
            "// test 0",
            "// test 1",
            "if/* test 3 */ (/* test 4 */ p_1 /* test 2 */ && /* test 5 */ p_2 /* test 12 */ )/* test 6 */ /* test 11 */{", 
            "   // test 7",
            "   var /* test 8 */ v_1; /* test 9 */", 
            "   // test 15",
            "   let /* test 13 */ v_2  /* test 16 */; /* test 14 */", 
            "} // test 10"};
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(AssertUtil.toSource(code));
        Javascript javascript = javascriptUnit.unit();
        List<AstNode> nodes = javascript.astStream().toList();
        assertEquals(17, javascript.astComments().size());
        // javascript
        assertEquals(List.of("/* test 3 */", "/* test 4 */", "/* test 2 */", 
                             "/* test 5 */", "/* test 12 */", "/* test 6 */", 
                             "/* test 11 */", "// test 7", "/* test 8 */", 
                             "/* test 9 */", "// test 15", "/* test 13 */", 
                             "/* test 16 */", "/* test 14 */"), 
                     toString(javascript.astCommentsOf(nodes.get(0), between)));
        // if
        assertEquals(List.of("/* test 3 */", "/* test 4 */", "/* test 2 */", 
                             "/* test 5 */", "/* test 12 */", "/* test 6 */", 
                             "/* test 11 */", "// test 7", "/* test 8 */", 
                             "/* test 9 */", "// test 15", "/* test 13 */", 
                             "/* test 16 */", "/* test 14 */"),
                     toString(javascript.astCommentsOf(nodes.get(1), between)));
        // if.block
        assertEquals(List.of("/* test 3 */", "/* test 4 */", "/* test 2 */", 
                             "/* test 5 */", "/* test 12 */", "/* test 6 */", 
                             "/* test 11 */", "// test 7", "/* test 8 */", 
                             "/* test 9 */", "// test 15", "/* test 13 */", 
                             "/* test 16 */", "/* test 14 */"),
                     toString(javascript.astCommentsOf(nodes.get(2), between)));
        // if.condition
        assertEquals(List.of("/* test 4 */", "/* test 2 */", 
                             "/* test 5 */", "/* test 12 */"),
                     toString(javascript.astCommentsOf(nodes.get(3), between)));
        // infix
        assertEquals(List.of("/* test 2 */", "/* test 5 */"),
                     toString(javascript.astCommentsOf(nodes.get(4), between)));
        // infix p_1  
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(5), between)));
        // infix Operator
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(6), between)));
        // infix p_2  
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(7), between)));
        // If.Procedure
        assertEquals(List.of("// test 7", "/* test 8 */", 
                             "/* test 9 */", "// test 15", "/* test 13 */", 
                             "/* test 16 */", "/* test 14 */"),
                     toString(javascript.astCommentsOf(nodes.get(8), between)));
        // VariableDeclaration var v_1
        assertEquals(List.of("/* test 8 */"),
                     toString(javascript.astCommentsOf(nodes.get(9), between)));
        // VariableDeclaration.Type var
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(10), between)));
        // VariableDeclaration.Variable v_1
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(11), between)));
        // VariableDeclaration.Variable.Identifier v_1
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(12), between)));
        // VariableDeclaration var v_2
        assertEquals(List.of("/* test 13 */"),
                     toString(javascript.astCommentsOf(nodes.get(13), between)));
        // VariableDeclaration.Type let
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(14), between)));
        // VariableDeclaration.Variable v_2
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(15), between)));
        // VariableDeclaration.Variable.Identifier v_2
        assertEquals(List.of(),
                     toString(javascript.astCommentsOf(nodes.get(16), between)));
    }
    
    private static List<String> toString(List<AstComment> astComments) {
        return astComments.stream()
                          .map(v -> (JavascriptComment) v)
                          .map(v -> v.getComment())
                          .toList();
    }
    
}
