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
package com.easyparsingapi.yari.parser.javascript.ast;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.core.ast.AstNode;

/**
 * Internal utility that wires up parent references in the AST after a node is constructed.
 * Every AST node holds a back-pointer to its parent; this class sets those pointers by
 * dispatching over the concrete type of each child via a pattern-matching switch.
 */
public class JavascriptUtil {



    /** Not instantiable — all methods are static. */
    private JavascriptUtil() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(JavascriptUtil.class);

    /**
     * Recursively sets the parent reference on every direct child of {@code node}.
     * Children that are collections are unwrapped before the parent is assigned.
     * Unrecognised child types are logged at WARN level.
     *
     * @param node the node whose children should receive a parent back-pointer
     */
    public static void setAstParent(final AstNode node) {
        for (final AstNode child : node.astChildren()) {
            switch (child) {
                case Collection<?> c -> c.stream()
                                         .filter(v -> v instanceof AstNode)
                                         .map(v -> (AstNode) v)
                                         .forEach(JavascriptUtil::setAstParent);
                case AbstractWhile c -> c.astParent(node);
                case UseStrict c -> c.astParent(node);
                case EmptyStatement c -> c.astParent(node);
                case BlockProcedure c -> c.astParent(node);
                case ArrayDeclaration c -> c.astParent(node);
                case ArrowFunction c -> c.astParent(node);
                case ArrowFunction.Signature c -> c.astParent(node);
                case ArrowFunction.Procedure c -> c.astParent(node);
                case Assignment c -> c.astParent(node);
                case Assignment.Chaining c -> c.astParent(node);
                case Await c -> c.astParent(node);
                case BigDecimal c -> c.astParent(node);
                case Break c -> c.astParent(node);
                case Catch c -> c.astParent(node);
                case Catch.Signature c -> c.astParent(node);
                case Catch.Procedure c -> c.astParent(node);
                case CatchInstance c -> c.astParent(node);
                case ClassDeclaration c -> c.astParent(node);
                case ClassDeclaration.StaticFieldDeclaration c -> c.astParent(node);
                case ClassFieldDeclaration c -> c.astParent(node);
                case ClassMethodDeclaration c -> c.astParent(node);
                case ClassMethodDeclaration.Signature c -> c.astParent(node);
                case ClassMethodDeclaration.Procedure c -> c.astParent(node);
                case Sequence c -> c.astParent(node);
                case ComputedValue c -> c.astParent(node);
                case Continue c -> c.astParent(node);
                case Debugger c -> c.astParent(node);
                case Decrement c -> c.astParent(node);
                case Delete c -> c.astParent(node);
                case DestructuringArray c -> c.astParent(node);
                case DestructuringArray.Value c -> c.astParent(node);
                case DestructuringObject c -> c.astParent(node);
                case DestructuringObject.Field c -> c.astParent(node);
                case Empty c -> c.astParent(node);
                case Export c -> c.astParent(node);
                case Export.AllFrom c -> c.astParent(node);
                case Export.ExportBlock c -> c.astParent(node);
                case Export.ExportReference c -> c.astParent(node);
                case Export.From c -> c.astParent(node);
                case DoWhile.Procedure c -> c.astParent(node);
                case DoWhile.Condition c -> c.astParent(node);
                case While.Procedure c -> c.astParent(node);
                case While.Condition c -> c.astParent(node);
                case Finally c -> c.astParent(node);
                case Finally.Procedure c -> c.astParent(node);
                case FieldDeclaration c -> c.astParent(node);
                case For c -> c.astParent(node);
                case For.Iteration c -> c.astParent(node);
                case For.ListIteration c -> c.astParent(node);
                case For.Procedure c -> c.astParent(node);
                case FunctionCall c -> c.astParent(node);
                case FunctionCall.Signature c -> c.astParent(node);
                case FunctionDeclaration c -> c.astParent(node);
                case FunctionDeclaration.Signature c -> c.astParent(node);
                case FunctionDeclaration.Procedure c -> c.astParent(node);
                case Getter c -> c.astParent(node);
                case Getter.Procedure c -> c.astParent(node);
                case Identifier c -> c.astParent(node);
                case If c -> c.astParent(node);
                case If.Condition c -> c.astParent(node);
                case If.IfBlock c -> c.astParent(node);
                case If.Procedure c -> c.astParent(node);
                case If.ElseBlock c -> c.astParent(node);
                case Import c -> c.astParent(node);
                case Import.ImportBlock c -> c.astParent(node);
                case Import.ImportReference c -> c.astParent(node);
                case ImportFunctionCall c -> c.astParent(node);
                case ImportFunctionCall.Signature c -> c.astParent(node);
                case Increment c -> c.astParent(node);
                case Infix c -> c.astParent(node);
                case InvokedFunction c -> c.astParent(node);
                case InvokedFunction.Signature c -> c.astParent(node);
                case InvokedFunction.Definition c -> c.astParent(node);
                case Javascript c -> c.astParent(node);
                case Label c -> c.astParent(node);
                case Literal c -> c.astParent(node);
                case LiteralTemplate c -> c.astParent(node);
                case LiteralTemplate.Constant c -> c.astParent(node);
                case LiteralTemplate.Variable c -> c.astParent(node);
                case MapExpression c -> c.astParent(node);
                case MapExpression.Bracket c -> c.astParent(node);
                case MethodDeclaration c -> c.astParent(node);
                case MethodDeclaration.Signature c -> c.astParent(node);
                case MethodDeclaration.Procedure c -> c.astParent(node);
                case NaN c -> c.astParent(node);
                case New c -> c.astParent(node);
                case NewClass c -> c.astParent(node);
                case New.Signature c -> c.astParent(node);
                case Null c -> c.astParent(node);
                case ObjectDeclaration c -> c.astParent(node);
                case Operator c -> c.astParent(node);
                case ParameterWithValue c -> c.astParent(node);
                case Parenthesis c -> c.astParent(node);
                case Prefix c -> c.astParent(node);
                case QualifiedExpression c -> c.astParent(node);
                case QualifiedExpression.Qualifier c -> c.astParent(node);
                case QualifiedExpression.PrivateExpression c -> c.astParent(node);
                case Return c -> c.astParent(node);
                case Setter c -> c.astParent(node);
                case Setter.Signature c -> c.astParent(node);
                case Setter.Procedure c -> c.astParent(node);
                case Spread c -> c.astParent(node);
                case StaticProcedure c -> c.astParent(node);
                case StaticProcedure.Procedure c -> c.astParent(node);
                case Switch c -> c.astParent(node);
                case Switch.SwitchCase c -> c.astParent(node);
                case Switch.SwitchExpression c -> c.astParent(node);
                case Switch.SwitchProcedure c -> c.astParent(node);
                case Switch.CaseProcedure c -> c.astParent(node);
                case Switch.DefaultCase c -> c.astParent(node);
                case TagFunctionCall c -> c.astParent(node);
                case Ternary c -> c.astParent(node);
                case This c -> c.astParent(node);
                case Throw c -> c.astParent(node);
                case Try c -> c.astParent(node);
                case Try.Procedure c -> c.astParent(node);
                case Undefined c -> c.astParent(node);
                case VariableDeclaration c -> c.astParent(node);
                case VariableDeclaration.Type c -> c.astParent(node);
                case VariableDeclaration.Variable c -> c.astParent(node);
                case Yield c -> c.astParent(node);
                case JavascriptError c -> c.astParent(node);
                default -> LOGGER.warn("Unrecognized type of {}", child);
            }
        }
    }

    /**
     * Sets the parent reference of a root {@link Javascript} node to the given {@code parent}.
     *
     * @param javascript the root AST node to update
     * @param parent     the parent node to assign
     */
    public static void setAstParent(final Javascript javascript, final AstNode parent) {
        javascript.astParent(parent);
    }
    
}
