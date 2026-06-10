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
package com.easyparsingapi.yari.parser.javascript.sandbox;

import java.security.Signature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parser.javascript.ast.ArrayDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ArrowFunction;
import com.easyparsingapi.yari.parser.javascript.ast.Assignment;
import com.easyparsingapi.yari.parser.javascript.ast.Await;
import com.easyparsingapi.yari.parser.javascript.ast.Break;
import com.easyparsingapi.yari.parser.javascript.ast.Catch;
import com.easyparsingapi.yari.parser.javascript.ast.CatchInstance;
import com.easyparsingapi.yari.parser.javascript.ast.ClassDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ClassFieldDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ClassMethodDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ComputedValue;
import com.easyparsingapi.yari.parser.javascript.ast.Continue;
import com.easyparsingapi.yari.parser.javascript.ast.Debugger;
import com.easyparsingapi.yari.parser.javascript.ast.Decrement;
import com.easyparsingapi.yari.parser.javascript.ast.Delete;
import com.easyparsingapi.yari.parser.javascript.ast.DestructuringArray;
import com.easyparsingapi.yari.parser.javascript.ast.DestructuringObject;
import com.easyparsingapi.yari.parser.javascript.ast.DoWhile;
import com.easyparsingapi.yari.parser.javascript.ast.EmptyIteration;
import com.easyparsingapi.yari.parser.javascript.ast.Export;
import com.easyparsingapi.yari.parser.javascript.ast.FieldDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.Finally;
import com.easyparsingapi.yari.parser.javascript.ast.For;
import com.easyparsingapi.yari.parser.javascript.ast.FunctionCall;
import com.easyparsingapi.yari.parser.javascript.ast.FunctionDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.Getter;
import com.easyparsingapi.yari.parser.javascript.ast.Identifier;
import com.easyparsingapi.yari.parser.javascript.ast.If;
import com.easyparsingapi.yari.parser.javascript.ast.Import;
import com.easyparsingapi.yari.parser.javascript.ast.ImportFunctionCall;
import com.easyparsingapi.yari.parser.javascript.ast.Increment;
import com.easyparsingapi.yari.parser.javascript.ast.Infix;
import com.easyparsingapi.yari.parser.javascript.ast.InvokedFunction;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptComment;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptCondition;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptProcedure;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptSignature;
import com.easyparsingapi.yari.parser.javascript.ast.Label;
import com.easyparsingapi.yari.parser.javascript.ast.Literal;
import com.easyparsingapi.yari.parser.javascript.ast.MapExpression;
import com.easyparsingapi.yari.parser.javascript.ast.MethodDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.New;
import com.easyparsingapi.yari.parser.javascript.ast.ObjectDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.Operator;
import com.easyparsingapi.yari.parser.javascript.ast.ParameterWithValue;
import com.easyparsingapi.yari.parser.javascript.ast.Parenthesis;
import com.easyparsingapi.yari.parser.javascript.ast.Position;
import com.easyparsingapi.yari.parser.javascript.ast.Prefix;
import com.easyparsingapi.yari.parser.javascript.ast.QualifiedExpression;
import com.easyparsingapi.yari.parser.javascript.ast.Return;
import com.easyparsingapi.yari.parser.javascript.ast.Sequence;
import com.easyparsingapi.yari.parser.javascript.ast.Setter;
import com.easyparsingapi.yari.parser.javascript.ast.Spread;
import com.easyparsingapi.yari.parser.javascript.ast.StaticProcedure;
import com.easyparsingapi.yari.parser.javascript.ast.Switch;
import com.easyparsingapi.yari.parser.javascript.ast.Ternary;
import com.easyparsingapi.yari.parser.javascript.ast.This;
import com.easyparsingapi.yari.parser.javascript.ast.Throw;
import com.easyparsingapi.yari.parser.javascript.ast.Try;
import com.easyparsingapi.yari.parser.javascript.ast.VariableDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.While;
import com.easyparsingapi.yari.parser.javascript.ast.Yield;
import com.easyparsingapi.yari.parser.javascript.ast.VariableDeclaration.Variable;

public class WriterUtil {

    private static Map<Class<? extends SourceLocalisable>, Function<AstNode, String>> toCode = new HashMap<>();
    static {
        toCode.put(Javascript.class, n -> {
            return String.join(";", toCode(Javascript.class.cast(n).getNodes()));
        });
        toCode.put(ArrayDeclaration.class, n -> {
            final StringBuilder result = new StringBuilder();
            result.append("[");
            result.append(String.join(", ", toCode(ArrayDeclaration.class.cast(n).getValues())));
            result.append("]");
            return result.toString();
        });
        toCode.put(ClassFieldDeclaration.class, n -> {
            ClassFieldDeclaration node = ClassFieldDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.isStatic()) {
                result.append("static ");
            }
            result.append(toCode(node.getKey()));
            if (node.isInitialized()) {
                result.append(" = ");
                result.append(toCode(node.getValue()));
            }
            return result.toString();
        });
        toCode.put(ClassMethodDeclaration.class, n -> {
            ClassMethodDeclaration node = ClassMethodDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.isStatic()) {
                result.append("static ");
            }
            if (node.isAsynchronous()) {
                result.append("async ");
            }
            if (node.isGenerator()) {
                result.append("*");
            }
            result.append(toCode(node.getName()));
            result.append(toCode(node.getSignature()));
            result.append(" ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(ComputedValue.class, n -> {
            ComputedValue node = ComputedValue.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("[");
            if (node.getExpression() != null) {
                result.append(toCode(node.getExpression()));
            }
            result.append("]");
            return result.toString();
        });
        toCode.put(ParameterWithValue.class, n -> {
            ParameterWithValue node = ParameterWithValue.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getIdentifier()));
            result.append(" = ");
            result.append(toCode(node.getDefaultValue()));
            return result.toString();
        });
        toCode.put(DestructuringArray.class, n -> {
            DestructuringArray node = DestructuringArray.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("[");
            result.append(String.join(", ", toCode(node.getValues())));
            result.append("]");
            return result.toString();
        });
        toCode.put(DestructuringArray.Value.class, n -> {
            DestructuringArray.Value node = DestructuringArray.Value.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getId()));
            if (node.hasDefaultValue()) {
                result.append(" = ");
                result.append(toCode(node.getDefaultValue()));
            }
            return result.toString();
        });
        toCode.put(DestructuringObject.class, n -> {
            DestructuringObject node = DestructuringObject.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (!CollectionUtil.isEmpty(node.getFields())) {
                result.append("{ ");
                result.append(String.join(", ", toCode(node.getFields())));
                result.append(" }");
            } else {
                result.append("{ }");
            }
            return result.toString();
        });
        toCode.put(DestructuringObject.Field.class, n -> {
            DestructuringObject.Field node = DestructuringObject.Field.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getId()));
            if (node.hasName()) {
                result.append(": ");
                result.append(toCode(node.getBinding()));
            }
            if (node.hasDefaultValue()) {
                result.append(" = ");
                result.append(toCode(node.getDefaultValue()));
            }
            return result.toString();
        });
        toCode.put(EmptyIteration.class, n -> {
            return "";
        });
        toCode.put(FieldDeclaration.class, n -> {
            FieldDeclaration node = FieldDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getKey()));
            result.append(": ");
            result.append(toCode(node.getValue()));
            return result.toString();
        });
        toCode.put(Getter.class, n -> {
            Getter node = Getter.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("get ");
            result.append(toCode(node.getName()));
            result.append("() ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(Setter.class, n -> {
            Setter node = Setter.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("set ");
            result.append(toCode(node.getName()));
            result.append(toCode(node.getSignature()));
            result.append(" ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(Identifier.class, n -> {
            Identifier node = Identifier.class.cast(n);
            return node.getValue();
        });
        toCode.put(Infix.class, n -> {
            Infix node = Infix.class.cast(n);
            return String.join(" ", toCode(node.getLeftOperand()), 
                                    toCode(node.getOperator()), 
                                    toCode(node.getRightOperand()));
        });
        toCode.put(JavascriptCondition.class, n -> {
            JavascriptCondition node = JavascriptCondition.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("(");
            if (node.getExpression() != null) {
                result.append(toCode(node.getExpression()));
            }
            result.append(")");
            return result.toString();
        });
        toCode.put(Literal.class, n -> {
            Literal node = Literal.class.cast(n);
            return String.valueOf(node.getValue());
        });
        toCode.put(MapExpression.class, n -> {
            MapExpression node = MapExpression.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getName()));
            result.append(toCode(node.getBracket()));
            return result.toString();
        });
        toCode.put(MapExpression.Bracket.class, n -> {
            MapExpression.Bracket node = MapExpression.Bracket.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("[");
            if (node.getParameter() != null) {
                result.append(toCode(node.getParameter()));
            }
            result.append("]");
            return result.toString();
        });
        toCode.put(MethodDeclaration.class, n -> {
            MethodDeclaration node = MethodDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.isAsynchronous()) {
                result.append("async ");
            }
            if (node.isGenerator()) {
                result.append("*");
            }
            result.append(toCode(node.getName()));
            result.append(toCode(node.getSignature()));
            result.append(" ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(ObjectDeclaration.class, n -> {
            ObjectDeclaration node = ObjectDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.getProperties().isEmpty()) {
                result.append("{ }");
            } 
            else {
                result.append("{ ");
                result.append(String.join(", ", toCode(node.getProperties())));
                result.append(" }");
            }
            return result.toString();
        });
        toCode.put(Operator.class, n -> {
            Operator node = Operator.class.cast(n);
            return node.getSymbol();
        });
        toCode.put(Parenthesis.class, n -> {
            Parenthesis node = Parenthesis.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("(");
            result.append(toCode(node.getOperand()));
            result.append(")");
            return result.toString();
        });
        toCode.put(Prefix.class, n -> {
            Prefix node = Prefix.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getOperator()));
            if (!"-".equals(node.getOperator().getSymbol()) && !"!".equals(node.getOperator().getSymbol())) {
                result.append(" ");
            }
            result.append(toCode(node.getOperand()));
            return result.toString();
        });
        toCode.put(QualifiedExpression.class, n -> {
            QualifiedExpression node = QualifiedExpression.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(String.join("", toCode(node.getQualifiers())));
            result.append(toCode(node.getExpression()));
            return result.toString();
        });
        toCode.put(QualifiedExpression.Qualifier.class, n -> {
            QualifiedExpression.Qualifier node = QualifiedExpression.Qualifier.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getExpression()));
            result.append(toCode(node.getOperator()));
            return result.toString();
        });
        toCode.put(Spread.class, n -> {
            Spread node = Spread.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("...");
            result.append(toCode(node.getValue()));
            return result.toString();
        });
        toCode.put(This.class, n -> {
            return "this";
        });
        toCode.put(EmptyIteration.class, n -> {
            return "";
        });
        toCode.put(JavascriptProcedure.class, n -> {
            JavascriptProcedure node = JavascriptProcedure.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.getNodes().isEmpty()) {
                result.append("{ }");
            }
            else {
                result.append("{ ");
                result.append(String.join("; ", toCode(node.getNodes())));
                result.append(" }");
            }
            return result.toString();
        });
        toCode.put(JavascriptSignature.class, n -> {
            JavascriptSignature node = JavascriptSignature.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.streamParameters().count() == 0) {
                result.append("()");
            }
            else {
                result.append("(");
                result.append(String.join(", ", toCode(node.streamParameters().toList())));
                result.append(")");
            }
            return result.toString();
        });
        toCode.put(Catch.class, n -> {
            Catch node = Catch.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("catch");
            result.append(toCode(node.getSignature()));
            result.append(" ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(CatchInstance.class, n -> {
            CatchInstance node = CatchInstance.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getException()));
            result.append(" if ");
            result.append(toCode(node.getInstanceofException()));
            return result.toString();
        });
        toCode.put(Sequence.class, n -> {
            Sequence node = Sequence.class.cast(n);
            final List<String> result = new ArrayList<>();
            int index = 0;
            for (final JavascriptNode expression : node.getNodes()) {
                String expressionAsString = toCode(expression);
                if (index != 0 && !expressionAsString.isBlank()) {
                    expressionAsString = " " + expressionAsString;
                }
                result.add(expressionAsString);
                index++;
            }
            return String.join(",", result);
        });
        toCode.put(JavascriptComment.class, n -> {
            JavascriptComment node = JavascriptComment.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("/* ");
            result.append(node.getComment()
                              .replace("\r", "")
                              .replace(" \n", " ")
                              .replace("\n", ""));
            result.append(" */");
            return result.toString();
        });
        toCode.put(Finally.class, n -> {
            Finally node = Finally.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("finally ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(StaticProcedure.class, n -> {
            StaticProcedure node = StaticProcedure.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("static ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(Try.class, n -> {
            Try node = Try.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("try ");
            result.append(toCode(node.getProcedure()));
            if (node.hasCatch()) {
                result.append(" ");
                result.append(String.join(" ", toCode(node.getCatches())));
            }
            if (node.hasFinally()) {
                result.append(" ");
                result.append(toCode(node.getFinally()));
            }
            return result.toString();
        });
        toCode.put(ArrowFunction.class, n -> {
            ArrowFunction node = ArrowFunction.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.isAsynchronous()) {
                result.append("async ");
            }
            result.append(toCode(node.getSignature()));
            result.append(" => ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(Assignment.class, n -> {
            Assignment node = Assignment.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(String.join(" ", toCode(node.getLeftOperand()), 
                                           toCode(node.getOperator()), 
                                           toCode(node.getRightOperand())));
            return result.toString();
        });
        toCode.put(Assignment.Chaining.class, n -> {
            Assignment.Chaining node = Assignment.Chaining.class.cast(n);
            return String.join(" = ", toCode(node.getValues()));
        });
        toCode.put(Await.class, n -> {
            Await node = Await.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("await ");
            result.append(toCode(node.applyOn()));
            return result.toString();
        });
        toCode.put(Break.class, n -> {
            Break node = Break.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("break");
            if (node.hasLabel()) {
                result.append(" ");
                result.append(toCode(node.getLabel()));
            }
            return result.toString();
        });
        toCode.put(ClassDeclaration.class, n -> {
            ClassDeclaration node = ClassDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("class");
            if (!node.isAnonymous()) {
                result.append(" ");
                result.append(toCode(node.getClassName()));
            }
            if (node.isExtended()) {
                result.append(" extends ");
                result.append(toCode(node.getExtendedName()));
            }
            result.append(" ");
            if (node.getProperties().isEmpty()) {
                result.append("{ }");
            } else {
                result.append("{ ");
                result.append(String.join("; ", toCode(node.getProperties())));
                result.append(" }");
            }
            return result.toString();
        });
        toCode.put(ClassDeclaration.StaticFieldDeclaration.class, n -> {
            ClassDeclaration.StaticFieldDeclaration node = ClassDeclaration.StaticFieldDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("static ");
            result.append(toCode(node.getKey()));
            result.append(" = ");
            result.append(toCode(node.getValue()));
            return result.toString();
        });
        toCode.put(Continue.class, n -> {
            return "continue";
        });
        toCode.put(Debugger.class, n -> {
            return "debugger";
        });
        toCode.put(Decrement.class, n -> {
            Decrement node = Decrement.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (Position.prefix == node.getPosition()) {
                result.append("--");
            }
            result.append(toCode(node.getOperand()));
            if (Position.suffix == node.getPosition()) {
                result.append("--");
            }
            return result.toString();
        });
        toCode.put(Delete.class, n -> {
            Delete node = Delete.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("delete ");
            result.append(toCode(node.getDeletedValue()));
            return result.toString();
        });
        toCode.put(DoWhile.class, n -> {
            DoWhile node = DoWhile.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("do ");
            result.append(toCode(node.getProcedure()));
            result.append(" ");
            result.append("while ");
            result.append(toCode(node.getCondition()));
            return result.toString();
        });
        toCode.put(Export.class, n -> {
            Export node = Export.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("export ");
            if (node.isDefault()) {
                result.append("default ");
            }
            result.append(toCode(node.getDefinition()));
            return result.toString();
        });
        toCode.put(Export.ExportBlock.class, n -> {
            Export.ExportBlock node = Export.ExportBlock.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (!node.getExportReferences().isEmpty()) {
                result.append("{ ");
                result.append(String.join(", ", toCode(node.getExportReferences())));
                result.append(" }");
            } else {
                result.append("{ }");
            }
            return result.toString();
        });
        toCode.put(Export.ExportReference.class, n -> {
            Export.ExportReference node = Export.ExportReference.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getName()));
            if (node.hasAlias()) {
                result.append(" as ");
                result.append(toCode(node.getAlias()));
            }
            return result.toString();
        });
        toCode.put(Export.AllFrom.class, n -> {
            Export.AllFrom node = Export.AllFrom.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("*");
            if (node.hasAlias()) {
                result.append(" as ");
                result.append(toCode(node.getAlias()));
            }
            result.append(" from ");
            result.append(toCode(node.getModuleName()));
            return result.toString();
        });
        toCode.put(Export.From.class, n -> {
            Export.From node = Export.From.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getExportBlock()));
            result.append(" from ");
            result.append(toCode(node.getModuleName()));
            return result.toString();
        });
        toCode.put(For.class, n -> {
            For node = For.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("for ");
            if (node.hasAwait()) {
                result.append("await ");
            }
            result.append(toCode(node.getIteration()));
            if (node.getProcedure() != null) {
                result.append(" ");
                result.append(toCode(node.getProcedure()));
            }
            return result.toString();
        });
        toCode.put(For.Iteration.class, n -> {
            For.Iteration node = For.Iteration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("(");
            if (node.hasFirstExpression()) {
                result.append(toCode(node.getFirstExpression()));
            }
            result.append("; ");
            if (node.hasSecondExpression()) {
                result.append(toCode(node.getSecondExpression()));
            }
            result.append("; ");
            if (node.hasThirdExpression()) {
                result.append(toCode(node.getThirdExpression()));
            }
            result.append(")");
            return result.toString();
        });
        toCode.put(For.ListIteration.class, n -> {
            For.ListIteration node = For.ListIteration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("(");
            result.append(toCode(node.getValue()));
            result.append(" ");
            result.append(toCode(node.getOperator()));
            result.append(" ");
            result.append(toCode(node.getValues()));
            result.append(")");
            return result.toString();
        });
        toCode.put(FunctionCall.class, n -> {
            FunctionCall node = FunctionCall.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getName()));
            result.append(toCode(node.getSignature()));
            return result.toString();
        });
        toCode.put(FunctionDeclaration.class, n -> {
            FunctionDeclaration node = FunctionDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.isAsynchronous()) {
                result.append("async ");
            }
            if (node.isGenerator()) {
                result.append("function*");
            }
            else {
                result.append("function");
            }
            if (!node.isAnonymous()) {
                result.append(" ");
                result.append(toCode(node.getName()));
            }
            result.append(toCode(node.getSignature()));
            result.append(" ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(If.class, n -> {
            If node = If.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getIfBlock()));
            if (node.getElseBlock() != null) {
                result.append(" ");
                result.append(toCode(node.getElseBlock()));
            }
            return result.toString();
        });
        toCode.put(If.IfBlock.class, n -> {
            If.IfBlock node = If.IfBlock.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("if ");
            result.append(toCode(node.getCondition()));
            if (node.getProcedure() != null) {
                result.append(" ");
                result.append(toCode(node.getProcedure()));
            }
            else {
                result.append(";");
            }
            return result.toString();
        });
        toCode.put(If.ElseBlock.class, n -> {
            If.ElseBlock node = If.ElseBlock.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("else ");
            result.append(toCode(node.getProcedure()));
            return result.toString();
        });
        toCode.put(Import.class, n -> {
            Import node = Import.class.cast(n);
            final List<String> imports = new ArrayList<>();
            if (node.hasDefault()) {
                imports.add(toCode(node.getDefaultName()));
            }
            if (node.hasAlias()) {
                imports.add("* as " + toCode(node.getAlias()));
            }
            if (node.hasImportBlock()) {
                imports.add(toCode(node.getImportBlock()));
            }
            final StringBuilder result = new StringBuilder();
            result.append("import ");
            if (!imports.isEmpty()) {
                result.append(String.join(", ", imports));
                result.append(" from ");
            }
            result.append(toCode(node.getModuleName()));
            return result.toString();
        });
        toCode.put(Import.ImportBlock.class, n -> {
            Import.ImportBlock node = Import.ImportBlock.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (!node.getImportReferences().isEmpty()) {
                result.append("{ ");
                result.append(String.join(", ", toCode(node.getImportReferences())));
                result.append(" }");
            } else {
                result.append("{ }");
            }
            return result.toString();
        });
        toCode.put(Import.ImportReference.class, n -> {
            Import.ImportReference node = Import.ImportReference.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getName()));
            if (node.hasAlias()) {
                result.append(" as ");
                result.append(toCode(node.getAlias()));
            }
            return result.toString();
        });
        toCode.put(ImportFunctionCall.class, n -> {
            ImportFunctionCall node = ImportFunctionCall.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("import");
            result.append(toCode(node.getSignature()));
            return result.toString();
        });
        toCode.put(Increment.class, n -> {
            Increment node = Increment.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (Position.prefix == node.getPosition()) {
                result.append("++");
            }
            result.append(toCode(node.getOperand()));
            if (Position.suffix == node.getPosition()) {
                result.append("++");
            }
            return result.toString();
        });
        toCode.put(InvokedFunction.class, n -> {
            InvokedFunction node = InvokedFunction.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getDefinition()));
            result.append(toCode(node.getSignature()));
            return result.toString();
        });
        toCode.put(InvokedFunction.Definition.class, n -> {
            InvokedFunction.Definition node = InvokedFunction.Definition.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("(");
            if (node.getFunction() != null) {
                result.append(toCode(node.getFunction()));
            }
            result.append(")");
            return result.toString();
        });
        toCode.put(JavascriptError.class, n -> {
            JavascriptError node = JavascriptError.class.cast(n);
            final StringBuilder result = new StringBuilder();
//            result.append("/`*");
//            result.append(TokenUtil.toString(node.getTokens()));
//            result.append("*/");
            return result.toString();
        });
        toCode.put(Label.class, n -> {
            Label node = Label.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getName()));
            result.append(": ");
            result.append(toCode(node.getStatement()));
            return result.toString();
        });
        toCode.put(VariableDeclaration.class, n -> {
            VariableDeclaration node = VariableDeclaration.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (node.hasType()) {
                result.append(toCode(node.getType()));
                result.append(" ");
            }
            final List<String> variables = new ArrayList<>();
            for (final Variable variableDeclaration : node.getVariableDeclarations()) {
                final StringBuilder variable = new StringBuilder();
                variable.append(toCode(variableDeclaration.getName()));
                if (variableDeclaration.getValue() != null) {
                    variable.append(" = ");
                    variable.append(toCode(variableDeclaration.getValue()));
                }
                variables.add(variable.toString());
            }
            result.append(String.join(", ", variables));
            return result.toString();
        });
        toCode.put(New.class, n -> {
            New node = New.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("new ");
            result.append(toCode(node.getName()));
            if (node.hasSignature()) {
                result.append(toCode(node.getSignature()));
            }
            return result.toString();
        });
        toCode.put(Return.class, n -> {
            Return node = Return.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("return");
            if (node.hasReturnValue()) {
                result.append(" ");
                result.append(toCode(node.getReturnValue()));
            }
            return result.toString();
        });
        toCode.put(Switch.class, n -> {
            Switch node = Switch.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("switch ");
            result.append(toCode(node.getSwitchExpression()));
            result.append(": ");
            result.append(toCode(node.getSwitchProcedure()));
            return result.toString();
        });
        toCode.put(Switch.SwitchExpression.class, n -> {
            Switch.SwitchExpression node = Switch.SwitchExpression.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("(");
            if (node.getExpression() != null) {
                result.append(toCode(node.getExpression()));
            }
            result.append(")");
            return result.toString();
        });
        toCode.put(Switch.SwitchCase.class, n -> {
            Switch.SwitchCase node = Switch.SwitchCase.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("case ");
            result.append(toCode(node.getCaseValue()));
            result.append(":");
            if (node.hasCaseProcedure()) {
                result.append(" ");
                result.append(toCode(node.getCaseProcedure()));
            }
            return result.toString();
        });
        toCode.put(Switch.DefaultCase.class, n -> {
            Switch.DefaultCase node = Switch.DefaultCase.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("default: ");
            result.append(toCode(node.getCaseProcedure()));
            return result.toString();
        });
        toCode.put(Switch.SwitchProcedure.class, n -> {
            Switch.SwitchProcedure node = Switch.SwitchProcedure.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("{ ");
            result.append(String.join("; ", toCode(node.getSwitchCases())));
            if (node.hasDefaultCase()) {
                result.append("; ");
                result.append(toCode(node.getDefaultCase()));
            }
            result.append(" }");
            return result.toString();
        });
        toCode.put(Switch.CaseProcedure.class, n -> {
            Switch.CaseProcedure node = Switch.CaseProcedure.class.cast(n);
            return String.join("; ", toCode(node.getNodes()));
        });
        toCode.put(Ternary.class, n -> {
            Ternary node = Ternary.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getCondition()));
            result.append(" ? ");
            result.append(toCode(node.getIfPart()));
            result.append(" : ");
            result.append(toCode(node.getElsePart()));
            return result.toString();
        });
        toCode.put(Throw.class, n -> {
            Throw node = Throw.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("throw ");
            result.append(toCode(node.getThrownValue()));
            return result.toString();
        });
        toCode.put(Variable.class, n -> {
            Variable node = Variable.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getName()));
            if (node.isInitialized()) {
                result.append(" ");
                result.append(toCode(node.getOperator()));
                result.append(" ");
                result.append(toCode(node.getValue()));
            }
            return result.toString();
        });
        toCode.put(VariableDeclaration.Type.class, n -> {
            VariableDeclaration.Type node = VariableDeclaration.Type.class.cast(n);
            return node.getValue();
        });
        toCode.put(While.class, n -> {
            While node = While.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("while ");
            result.append(toCode(node.getCondition()));
            if (node.getProcedure() != null) {
                result.append(" ");
                result.append(toCode(node.getProcedure()));
            }
            return result.toString();
        });
        toCode.put(Yield.class, n -> {
            Yield node = Yield.class.cast(n);
              final StringBuilder result = new StringBuilder();
                result.append("yield ");
                result.append(toCode(node.getOperand()));
                return result.toString();
        });
    }

    public static List<String> toCode(final Collection<? extends AstNode> astNodes) {
        return astNodes.stream()
                       .map(n -> toCode.get(n.getClass()).apply(n))
                       .collect(Collectors.toList());
    }

    public static String toCode(final AstNode astNode) {
        String result = null;
        if(astNode != null) {
            if (astNode instanceof JavascriptProcedure) {
                result = toCode.get(JavascriptProcedure.class).apply(astNode);
            }
            else if (astNode instanceof JavascriptSignature) {
                result = toCode.get(Signature.class).apply(astNode);
            }
            else if (astNode instanceof JavascriptCondition) {
                result = toCode.get(JavascriptCondition.class).apply(astNode);
            }
            else {
                result = toCode.get(astNode.getClass()).apply(astNode);
            }
        }
        return result;
    }

}
