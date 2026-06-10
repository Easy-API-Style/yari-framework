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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstResult;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptParser;

public class TestAstNodeWalk {

    @Test
    public void test_01(TestInfo testInfo) {
        String[] code = { 
                "if (p_1 && p_2) {", 
                "   var v_1;", 
                "   function fct_2(p_3) {", 
                "     var v_2;", 
                "     return v_3;", 
                "   }", 
                "}"
        };
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(AssertUtil.toSource(code));
        Javascript javascript = javascriptUnit.unit();
        
        List<String> expected = new ArrayList<>();
        expected.add("[1] if (p_1 && p_2) {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[2] if (p_1 && p_2) {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[3] (p_1 && p_2)");
        expected.add("[4] p_1 && p_2");
        expected.add("[5] p_1");
        expected.add("[5] &&");
        expected.add("[5] p_2");
        expected.add("[3] {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[4] var v_1");
        expected.add("[5] var");
        expected.add("[5] v_1");
        expected.add("[6] v_1");
        expected.add("[4] function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }");
        expected.add("[5] fct_2");
        expected.add("[5] (p_3)");
        expected.add("[6] p_3");
        expected.add("[5] {\\\\n var v_2;\\\\n return v_3;\\\\n }");
        expected.add("[6] var v_2");
        expected.add("[7] var");
        expected.add("[7] v_2");
        expected.add("[8] v_2");
        expected.add("[6] return v_3");
        expected.add("[7] v_3");
        
        List<String> actual = new ArrayList<>();
        javascript.walkChildren(h -> {
            final String value = "[" + h.deep() + "] " + AssertUtil.clean(javascriptUnit.substring(h.node().getSourceLocation()));
            actual.add(value);
//            System.out.println("expected.add(\"" + value + "\");");
        });
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_02(TestInfo testInfo) {
        String[] code = { 
                "if (p_1 && p_2) {", 
                "   var v_1;", 
                "   function fct_2(p_3) {", 
                "     var v_2;", 
                "     return v_3;", 
                "   }", 
                "}"
        };
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(AssertUtil.toSource(code));
        Javascript javascript = javascriptUnit.unit();
        
        List<String> expected = new ArrayList<>();
        expected.add("[1] if (p_1 && p_2) {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[2] if (p_1 && p_2) {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[3] (p_1 && p_2)");
        expected.add("[4] p_1 && p_2");
        expected.add("[3] {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[4] var v_1");
        
        List<String> actual = new ArrayList<>();
        javascript.walkChildren(h -> {
            if (h.deep() == 4) {
                h.cancel();
            }
            final String value = "[" + h.deep() + "] " + AssertUtil.clean(javascriptUnit.substring(h.node().getSourceLocation()));
            actual.add(value);
        });
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_03(TestInfo testInfo) {
        String[] code = { 
                "if (p_1 && p_2) {", 
                "   var v_1;", 
                "   function fct_2(p_3) {", 
                "     var v_2;", 
                "     return v_3;", 
                "   }", 
                "}"
        };
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(AssertUtil.toSource(code));
        Javascript javascript = javascriptUnit.unit();
        
        List<String> expected = new ArrayList<>();
        expected.add("[1] Javascript");
        expected.add("[2] Javascript::If");
        expected.add("[3] Javascript::If::IfBlock");
        expected.add("[4] Javascript::If::IfBlock::Condition");
        expected.add("[5] Javascript::If::IfBlock::Condition::Infix");
        expected.add("[5] Javascript::If::IfBlock::Condition::Infix");
        expected.add("[5] Javascript::If::IfBlock::Condition::Infix");
        expected.add("[3] Javascript::If::IfBlock");
        expected.add("[4] Javascript::If::IfBlock::Procedure");
        expected.add("[5] Javascript::If::IfBlock::Procedure::VariableDeclaration");
        expected.add("[5] Javascript::If::IfBlock::Procedure::VariableDeclaration");
        expected.add("[6] Javascript::If::IfBlock::Procedure::VariableDeclaration::Variable");
        expected.add("[4] Javascript::If::IfBlock::Procedure");
        expected.add("[5] Javascript::If::IfBlock::Procedure::FunctionDeclaration");
        expected.add("[5] Javascript::If::IfBlock::Procedure::FunctionDeclaration");
        expected.add("[6] Javascript::If::IfBlock::Procedure::FunctionDeclaration::Signature");
        expected.add("[5] Javascript::If::IfBlock::Procedure::FunctionDeclaration");
        expected.add("[6] Javascript::If::IfBlock::Procedure::FunctionDeclaration::Procedure");
        expected.add("[7] Javascript::If::IfBlock::Procedure::FunctionDeclaration::Procedure::VariableDeclaration");
        expected.add("[7] Javascript::If::IfBlock::Procedure::FunctionDeclaration::Procedure::VariableDeclaration");
        expected.add("[8] Javascript::If::IfBlock::Procedure::FunctionDeclaration::Procedure::VariableDeclaration::Variable");
        expected.add("[6] Javascript::If::IfBlock::Procedure::FunctionDeclaration::Procedure");
        expected.add("[7] Javascript::If::IfBlock::Procedure::FunctionDeclaration::Procedure::Return");

        List<String> actual = new ArrayList<>();
        javascript.walkChildren(h -> {
            final String value = "[" + h.deep() + "] " + String.join("::", h.path().stream().map(v -> v.getClass().getSimpleName()).toList());
            actual.add(value);
            System.out.println("expected.add(\"" + value + "\");");
        });
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        String[] code = { 
                "if (p_1 && p_2) {", 
                "   var v_1;", 
                "   function fct_2(p_3) {", 
                "     var v_2;", 
                "     return v_3;", 
                "   }", 
                "}"
        };
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(AssertUtil.toSource(code));
        Javascript javascript = javascriptUnit.unit();
        AtomicReference<AstNode> node = new AtomicReference<>();
        
        javascript.walkChildren(h -> {
            if (h.deep() == 7) {
                node.set(h.node());
                h.cancel();
            }
        });
        List<String> expected = new ArrayList<>();
        expected.add("[1] return v_3");
        expected.add("[2] {\\\\n var v_2;\\\\n return v_3;\\\\n }");
        expected.add("[3] function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }");
        expected.add("[4] {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[5] if (p_1 && p_2) {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[6] if (p_1 && p_2) {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        expected.add("[7] if (p_1 && p_2) {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        
        List<String> actual = new ArrayList<>();
        node.get().walkParents(h -> {
            final String value = "[" + h.deep() + "] " + AssertUtil.clean(javascriptUnit.substring(h.node().getSourceLocation()));
            actual.add(value);
            System.out.println("expected.add(\"" + value + "\");");
        });
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_05(TestInfo testInfo) {
        String[] code = { 
                "if (p_1 && p_2) {", 
                "   var v_1;", 
                "   function fct_2(p_3) {", 
                "     var v_2;", 
                "     return v_3;", 
                "   }", 
                "}"
        };
        AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(AssertUtil.toSource(code));
        Javascript javascript = javascriptUnit.unit();
        AtomicReference<AstNode> node = new AtomicReference<>();
        
        javascript.walkChildren(h -> {
            if (h.deep() == 7) {
                node.set(h.node());
                h.cancel();
            }
        });
        List<String> expected = new ArrayList<>();
        expected.add("[1] return v_3");
        expected.add("[2] {\\\\n var v_2;\\\\n return v_3;\\\\n }");
        expected.add("[3] function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }");
        expected.add("[4] {\\\\n var v_1;\\\\n function fct_2(p_3) {\\\\n var v_2;\\\\n return v_3;\\\\n }\\\\n}");
        
        List<String> actual = new ArrayList<>();
        node.get().walkParents(h -> {
            if (h.deep() == 4) {
                h.cancel();
            }
            final String value = "[" + h.deep() + "] " + AssertUtil.clean(javascriptUnit.substring(h.node().getSourceLocation()));
            actual.add(value);
            System.out.println("expected.add(\"" + value + "\");");
        });
        assertEquals(expected, actual);
    }
    
}
