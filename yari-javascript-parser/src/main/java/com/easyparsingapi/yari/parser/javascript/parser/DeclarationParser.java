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

import static com.easyparsingapi.yari.parsec.Parsers.or;
import static com.easyparsingapi.yari.parsec.Parsers.parseIf;
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parser.javascript.parser.AssignmentParser.assignableKey;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.keyword;
import static com.easyparsingapi.yari.parser.javascript.parser.ExpressionParser.identifierOrQualifiedIdentifier;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableValue;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableValueWithChaining;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.callExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.destructuringArray;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.destructuringObject;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.expression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.spread;
import static com.easyparsingapi.yari.parser.javascript.parser.LiteralParser.integer;
import static com.easyparsingapi.yari.parser.javascript.parser.LiteralParser.string;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.arrowProcedureOrStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.endStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.procedure;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.staticProcedure;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.strictEndStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.phrase;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;
import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.functionNames;
import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.propertyNames;

import java.util.List;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parser.javascript.ast.ArrayDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ArrowFunction;
import com.easyparsingapi.yari.parser.javascript.ast.ClassDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ClassFieldDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ClassMethodDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ComputedValue;
import com.easyparsingapi.yari.parser.javascript.ast.DestructuringArray;
import com.easyparsingapi.yari.parser.javascript.ast.DestructuringObject;
import com.easyparsingapi.yari.parser.javascript.ast.Empty;
import com.easyparsingapi.yari.parser.javascript.ast.FieldDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.FunctionDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.Getter;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.MethodDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.ObjectDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.Operator;
import com.easyparsingapi.yari.parser.javascript.ast.ParameterWithValue;
import com.easyparsingapi.yari.parser.javascript.ast.Setter;
import com.easyparsingapi.yari.parser.javascript.ast.Spread;
import com.easyparsingapi.yari.parser.javascript.ast.VariableDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.FunctionDeclaration.Procedure;
import com.easyparsingapi.yari.parser.javascript.ast.VariableDeclaration.Variable;

/**
 * Package-private static utility class providing parsers for JavaScript declaration constructs:
 * variable declarations ({@code var}/{@code let}/{@code const}), arrays, objects,
 * destructuring patterns, class declarations, function declarations, and spread expressions.
 */
class DeclarationParser {

    /** Not instantiable — all methods are static. */
    private DeclarationParser() {}
    
    static Parser<Spread> spread(final JavascriptConfig config) {
        return phrase(".", ".", ".").next(or(config.parser(destructuringArray),
                                             config.parser(destructuringObject),
                                             config.parser(expression),
                                             identifier()))
                                    .map(Spread::new)
                                    .label("spread");
    }
    
    private static Parser<ComputedValue> computedValue(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService().bracket(c),
                       config.parser(expression)
                             .between(JavascriptError::newInstance,
                                      token("["), 
                                      token("]"))
                             .map(ComputedValue::new))
                 .label("computedValue");
    }
    
    /*
     * 
     * DESTRUCTURING ARRAY
     * 
     */
    private static Parser<DestructuringArray.Value> destructuringArrayValue(final JavascriptConfig config) {
        return sequence(or(config.parser(callExpression),
                           config.parser(destructuringArray),
                           config.parser(destructuringObject), 
                           string(),
                           identifier()), 
                        token("=").next(config.parser(expression))
                          .optional(),
                        DestructuringArray.Value::new)
                 .label("destructuringArrayValue");
    }
    
    static Parser<DestructuringArray> destructuringArray(final JavascriptConfig config) { 
        return parseIf(c -> config.sequenceService().bracket(c),
                       or(spread(config),
                          destructuringArrayValue(config))
                       .optional(() -> new Empty())
                       .sepBy(token(","))
                       .between(TermParser.token("["), TermParser.token("]"))
                       .map(DestructuringArray::new))
                 .label("destructuringArray");
    }
    
    /*
     * 
     * DESTRUCTURING OBJECT
     * 
     */
    private static Parser<DestructuringObject.Field> destructuringObjectField(final JavascriptConfig config) {
        return sequence(or(computedValue(config), 
                           string(),
                           identifier()), 
                        token(":").next(or(config.parser(destructuringArray),
                                           config.parser(destructuringObject),
                                           config.parser(callExpression),
                                           identifier()))
                                  .optional(),
                        token("=").next(config.parser(expression))
                                 .optional(),
                        DestructuringObject.Field::new)
                .label("destructuringObjectField");
    }
    
    static Parser<DestructuringObject> destructuringObject(final JavascriptConfig config) { 
        return parseIf(c -> config.sequenceService().curlingBracket(c),
                       or(spread(config),
                          destructuringObjectField(config))
                            .optional(() -> new Empty())
                        .sepByBetween(JavascriptError::newInstance, token(","), token("{"), token("}"))
                        .acceptIf(v -> v.size() >= 1)
                        .map(DestructuringObject::new))
                 .label("destructuringObject");
    }
    
    /*
     * 
     * SIGANTURE
     * 
     */
    private static Parser<ParameterWithValue> parameterWithValueDeclaration(final JavascriptConfig config) {
        return sequence(or(config.parser(destructuringArray),
                           config.parser(destructuringObject),
                           identifier()), 
                        token("=").next(config.parser(expression)),
                        ParameterWithValue::new)
                   .label("parameterWithValueDeclaration");
    }
    
    private static Parser<JavascriptNode> parameterDeclaration(final JavascriptConfig config) { 
       return or(spread(config),
                 parameterWithValueDeclaration(config),
                 config.parser(destructuringArray),
                 config.parser(destructuringObject),
                 identifier())
               .label("parameterDeclaration");
    }
    
    private static Parser<List<JavascriptNode>> signatureDeclaration(final JavascriptConfig config) { 
        return parseIf(c -> config.sequenceService().parenthesis(c),
                       parameterDeclaration(config)
                         .sepByBetween(JavascriptError::newInstance, 
                                       token(","), 
                                       token("("), 
                                       token(")")))
                 .label("signatureDeclaration");
    }
    
    /*
     * 
     * METHOD
     * 
     */
    private static Parser<Getter> getter(final JavascriptConfig config) {
        return sequence(identifier("get").next(or(computedValue(config),
                                                  identifier()))
                                         .followedBy(phrase("(", ")")), 
                        procedure(config).map(Getter.Procedure::new),
                        Getter::new)
                   .label("getter");
    }
    
    private static Parser<Setter> setter(final JavascriptConfig config) {
        return sequence(identifier("set").next(or(computedValue(config), 
                                                  identifier())), 
                        parseIf(c -> config.sequenceService().parenthesis(c),
                                parameterDeclaration(config)
                                   .between(JavascriptError::newInstance, 
                                            token("("), 
                                            token(")"))
                                   .map(Setter.Signature::new)),
                        procedure(config)
                            .map(Setter.Procedure::new),
                        Setter::new)
                    .label("setter");
    }
    
    /*
     * 
     * ARRAY
     * 
     */
    @SuppressWarnings("unused")
    public static Parser<ArrayDeclaration> array(final JavascriptConfig config) { 
        return or(phrase("[", "]").map(v -> new ArrayDeclaration(List.of())),
                  parseIf(c -> config.sequenceService().bracket(c),
                          or(spread(config), 
                             config.parser(expression))
                           .optional(() -> new Empty())
                           .sepByBetween(JavascriptError::newInstance,
                                         token(","),
                                         token("["),
                                         token("]"))
                           .map(ArrayDeclaration::new)))
                .label("array");
    }
    
    /*
     * 
     * OBJECT
     * 
     */
    private static Parser<MethodDeclaration> objectMethodDeclaration(final JavascriptConfig config) { 
        return sequence(token("async").asOptional(),
                        token("*").asOptional(),
                        or(computedValue(config),
                           identifier(),
                           keyword(functionNames)),
                        signatureDeclaration(config).map(MethodDeclaration.Signature::new),
                        procedure(config).map(MethodDeclaration.Procedure::new),
                        (asyncToken, generatorToken, methodName, signature, procedure) -> 
                           new MethodDeclaration(generatorToken.isPresent(), 
                                                 asyncToken.isPresent(),
                                                 methodName, 
                                                 signature, 
                                                 procedure))
                 .label("objectMethodDeclaration");
    }

    private static Parser<FieldDeclaration> objectFieldDeclaration(final JavascriptConfig config) { 
        return sequence(or(string(),
                           integer(),
                           computedValue(config),
                           identifier(),
                           keyword(propertyNames)),
                        token(":").next(config.parser(assignableValue)),
                        FieldDeclaration::new)
                   .label("objectFieldDeclaration");
    }
    
    @SuppressWarnings("unused")
    static Parser<ObjectDeclaration> objectDeclaration(final JavascriptConfig config) {
        return or(phrase("{", "}").map(v -> new ObjectDeclaration(List.of())),
                  parseIf(c -> config.sequenceService().curlingBracket(c),
                          or(objectFieldDeclaration(config),
                             getter(config),
                             setter(config),
                             objectMethodDeclaration(config),
                             identifier(),
                             config.parser(spread))
                          .optional(() -> new Empty())
                      .sepByBetween(JavascriptError::newInstance, token(","), token("{"), token("}"))
                      .map(ObjectDeclaration::new)))
                .label("objectDeclaration");
    }    
    
    /*
     * 
     * CLASS
     * 
     */
    private static Parser<ClassFieldDeclaration> classFieldDeclaration(final JavascriptConfig config) { 
        return sequence(token("static").succeeds(),
                        token("#").succeeds(),
                        or(computedValue(config), 
                           identifier()),
                        token("=").next(config.parser(assignableValueWithChaining)).optional(),
                        (staticToken, isPrivate, key, value) -> new ClassFieldDeclaration(staticToken, isPrivate, key, value))
                 .label("classFieldDeclaration");
    }
    
    private static Parser<ClassMethodDeclaration> classMethodDeclaration(final JavascriptConfig config) { 
        return sequence(token("static").succeeds(),
                        token("async").succeeds(),
                        token("*").succeeds(),
                        token("#").succeeds(),
                        or(computedValue(config), 
                           identifier(),
                           keyword(functionNames)),
                        signatureDeclaration(config).map(ClassMethodDeclaration.Signature::new),
                        procedure(config).map(ClassMethodDeclaration.Procedure::new),
                        (staticToken, asyncToken, generatorToken, isPrivate, methodName, signature, procedure)
                            -> new ClassMethodDeclaration(staticToken,
                                                          generatorToken, 
                                                          asyncToken,
                                                          isPrivate,
                                                          methodName, 
                                                          signature, 
                                                          procedure))
                    .label("classMethodDeclaration");
    }   
    
    static Parser<ClassDeclaration> classDeclaration(final JavascriptConfig config) {
        return sequence(token("class").next(identifier().optional()),
                        token("extends").next(identifierOrQualifiedIdentifier())
                                        .optional(),
                        parseIf(c -> config.sequenceService().curlingBracket(c),
                                or(getter(config),
                                   setter(config),
                                   classMethodDeclaration(config),
                                   classFieldDeclaration(config),
                                   staticProcedure(config))
                                 .followedBy(endStatement())
                                 .manyBetween(JavascriptError::newInstance,
                                              strictEndStatement(),
                                              token("{"),
                                              token("}"))),
                      ClassDeclaration::new)
                  .label("classDeclaration");
    }
    
    /*
     * 
     * FUNCTION
     * 
     */
    static Parser<FunctionDeclaration> function(final JavascriptConfig config) {
        return sequence(token("async").succeeds(),
                        token("function").next(token("*").succeeds()),
                        or(identifier(), keyword("of")).optional(),
                        signatureDeclaration(config).map(FunctionDeclaration.Signature::new),
                        procedure(config).map(Procedure::new),
                        (async, generator, functionName, signature, procedure) 
                          -> new FunctionDeclaration(generator, 
                                                     async,
                                                     functionName, 
                                                     signature, 
                                                     procedure))
                  .label("function");
    }
    
    @SuppressWarnings("unused")
    static Parser<ArrowFunction> arrowFunction(final JavascriptConfig config) {
        return sequence(token("async").succeeds(),
                        or(identifier().<JavascriptNode>cast().map(List::of),  
                           phrase("(", ")").map(v -> List.<JavascriptNode>of()),
                           signatureDeclaration(config))
                          .map(ArrowFunction.Signature::new),
                        token("=>")
                          .next(arrowProcedureOrStatement(config).map(ArrowFunction.Procedure::new)),
                        (async, signature, procedure) -> new ArrowFunction(async, signature, procedure))
                   .label("arrowFunction");
    }
    
    static Parser<JavascriptNode> functionDeclaration(final JavascriptConfig config) {
        return or(parseIf(c -> config.sequenceService().isFunctionDeclaration(c), function(config)),
                  parseIf(c -> config.sequenceService().isArrowFunctionDeclaration(c), arrowFunction(config)))
                .label("functionDeclaration");
    }

    /*
     * 
     * VARIABLE
     * 
     */
    @SuppressWarnings("unused")
    static Parser<VariableDeclaration> variableDeclaration(final JavascriptConfig config, 
                                                           final boolean single) {
        final Parser<Variable> variableWithValue = 
            sequence(assignableKey(config),
                     or(token("=").map(v -> Operator.symbol("=")), 
                        phrase("?", "?").map(v -> Operator.symbol("??"))),
                     config.parser(assignableValueWithChaining),
                     Variable::new);
        final Parser<Variable> variable = assignableKey(config).map(name -> new Variable(name, null, null));
        return sequence(token("var", "let", "const").map(v -> new VariableDeclaration.Type(v)),
                        single 
                            ? or(variableWithValue, variable).map(List::of) 
                            : or(variableWithValue, variable).sepBy1(token(",")),
                        VariableDeclaration::new)
                   .label("variableDeclaration"); 
    }    
    
}
