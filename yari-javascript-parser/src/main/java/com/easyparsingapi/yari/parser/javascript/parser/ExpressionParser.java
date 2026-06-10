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
import static com.easyparsingapi.yari.parser.javascript.lexer.JavascriptLexer.startingStatementKeywords;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.keyword;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.thisParser;
import static com.easyparsingapi.yari.parser.javascript.parser.DeclarationParser.functionDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.array;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableCallExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableInvokedFunction;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableQualifiedExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableValueWithChaining;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.atomic;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.awaitStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.blockProcedure;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.breakStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.classDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.continueStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.debuggerStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.deleteStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.doWhileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.expression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.forStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.functionDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.ifStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.importFunctionCall;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.invokedFunction;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.labelStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.literalTemplate;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.newStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.objectDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.returnStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.sequenceBetweenParenthesis;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.spread;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.switchStatment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.ternary;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.throwStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.tryCatchFinally;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.useStrict;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.variableDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.whileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.yield;
import static com.easyparsingapi.yari.parser.javascript.parser.LiteralParser.regExpString;
import static com.easyparsingapi.yari.parser.javascript.parser.LiteralParser.string;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.endStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.strictEndStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.SequenceService.breakPattern;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.phrase;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;
import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.functionNames;
import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.propertyNames;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.easyparsingapi.yari.parsec.OperatorTable;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.functors.MapInfix;
import com.easyparsingapi.yari.parsec.functors.MapOperator;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.pattern.Patterns;
import com.easyparsingapi.yari.parser.javascript.ast.Decrement;
import com.easyparsingapi.yari.parser.javascript.ast.Empty;
import com.easyparsingapi.yari.parser.javascript.ast.FunctionCall;
import com.easyparsingapi.yari.parser.javascript.ast.Increment;
import com.easyparsingapi.yari.parser.javascript.ast.Infix;
import com.easyparsingapi.yari.parser.javascript.ast.InvokedFunction;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.Literal;
import com.easyparsingapi.yari.parser.javascript.ast.LiteralTemplate;
import com.easyparsingapi.yari.parser.javascript.ast.MapExpression;
import com.easyparsingapi.yari.parser.javascript.ast.Operator;
import com.easyparsingapi.yari.parser.javascript.ast.Parenthesis;
import com.easyparsingapi.yari.parser.javascript.ast.Position;
import com.easyparsingapi.yari.parser.javascript.ast.Prefix;
import com.easyparsingapi.yari.parser.javascript.ast.QualifiedExpression;
import com.easyparsingapi.yari.parser.javascript.ast.QualifiedExpression.PrivateExpression;
import com.easyparsingapi.yari.parser.javascript.ast.QualifiedExpression.PrivateQualifier;
import com.easyparsingapi.yari.parser.javascript.ast.QualifiedExpression.Qualifier;
import com.easyparsingapi.yari.parser.javascript.ast.Sequence;
import com.easyparsingapi.yari.parser.javascript.ast.TagFunctionCall;
import com.easyparsingapi.yari.parser.javascript.ast.Ternary;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;
import com.easyparsingapi.yari.parser.javascript.parser.SequenceService.Type;

/**
 * Package-private static utility class providing parsers for JavaScript expressions:
 * infix/prefix/postfix operators, ternary, comma sequences, qualified member-access expressions,
 * call expressions, immediately-invoked function expressions, and parenthesised sub-expressions.
 */
class ExpressionParser {

    /** Not instantiable — all methods are static. */
    private ExpressionParser() {}


    
//    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionParser.class);
    
    private static LinkedHashSet<Node> expressionNodes = new LinkedHashSet<>();
    
    static {
        expressionNodes.add(ternary);                    
        expressionNodes.add(assignableQualifiedExpression);    
        expressionNodes.add(assignableCallExpression);  
        expressionNodes.add(assignableInvokedFunction);    
        expressionNodes.add(invokedFunction);      
        expressionNodes.add(functionDeclaration);   
        expressionNodes.add(literalTemplate);      
        expressionNodes.add(yield);   
        expressionNodes.add(newStatement);                 
        expressionNodes.add(awaitStatement);               
        expressionNodes.add(deleteStatement);           
        expressionNodes.add(importFunctionCall);           
        expressionNodes.add(objectDeclaration);            
        expressionNodes.add(classDeclaration);             
        expressionNodes.add(debuggerStatement);            
        expressionNodes.add(array);                        
        expressionNodes.add(atomic);                   
    }
 
    static Parser<JavascriptNode> expression(final JavascriptConfig config, 
                                             final Set<Node> nodes) {
        final List<Parser<JavascriptNode>> parsers = new ArrayList<>();
        for (final Node node : expressionNodes) {
            if (nodes.contains(node)) {
                parsers.add(config.parser(node));
            }
        }
        return expression(config, or(parsers));
    }
    
    /*
     * 
     * INFIX
     * 
     */
    static Parser<Javascript> unitExpression(final JavascriptConfig config) {
        return or(commaSequence(config),
                  expression(config))
                   .followedBy(endStatement())
                   .manyUntilEof(JavascriptError::newInstance, 
                                 or(strictEndStatement(), 
                                    token(startingStatementKeywords()).peek()))
                   .map(v -> new Javascript(v, config.getComments()))
                   .label("unitExpression");
    }
    
    static Parser<JavascriptNode> expression(final JavascriptConfig config) {
        return expression(config,
                          or(config.parser(ternary),
                             config.parser(assignableQualifiedExpression),
                             config.parser(assignableCallExpression),
                             config.parser(assignableInvokedFunction),
                             config.parser(invokedFunction),
                             config.parser(functionDeclaration),
                             config.parser(literalTemplate),
                             config.parser(yield),
                             config.parser(newStatement),
                             config.parser(awaitStatement),
                             config.parser(deleteStatement),
                             config.parser(importFunctionCall),
                             config.parser(objectDeclaration),
                             config.parser(classDeclaration),
                             config.parser(debuggerStatement),
                             config.parser(array),
                             config.parser(atomic)))
                .label("expression");
    }
    
    private static Parser<JavascriptNode> expression(final JavascriptConfig config, 
                                                     final Parser<JavascriptNode> operandParser) {
        final Parser.Reference<JavascriptNode> reference = Parser.newReference();
        final Parser<JavascriptNode> unit = 
           or(operandParser,
              parseIf(c -> config.sequenceService().parenthesis(c), 
                      reference.lazy()
                               .between(token("("), token(")"))
                               .map(Parenthesis::new))
                 .label("expression[parenthesis]"),
                      config.parser(sequenceBetweenParenthesis));
        final Parser<JavascriptNode> operatorTable = new OperatorTable<JavascriptNode>()
                .infixl(operator(">>>"), 70)
                .infixl(operator("&gt;&gt;&gt;"), 70)
                .infixl(operator("&#62;&#62;&#62;"), 70)
              
                .infixl(operator("<<<"), 70)
                .infixl(operator("&lt;&lt;&lt;"), 70)
                .infixl(operator("&#60;&#60;&#60;"), 70)
             
                .infixl(operator(">>"), 70)
                .infixl(operator("&gt;&gt;"), 70)
                .infixl(operator("&#62;&#62;"), 70)
                
                .infixl(operator("<<"), 70)
                .infixl(operator("&lt;&lt;"), 70)
                .infixl(operator("&#60;&#60;"), 70)
                
                .infixl(operator("<="), 70)
                .infixl(operator("&lt;="), 70)
                .infixl(operator("&#60;="), 70)
                
                .infixl(operator(">="), 70)
                .infixl(operator("&gt;="), 70)
                .infixl(operator("&#62;="), 70)
                
                .infixl(operator("<"), 40)
                .infixl(operator("&lt;"), 40)
                .infixl(operator("&#60;"), 40)
                
                .infixl(operator(">"), 40)
                .infixl(operator("&gt;"), 40)
                .infixl(operator("&#62;"), 40)
                
                .infixl(operator("!=="), 40)
                .infixl(operator("!="), 40)
                .infixl(operator("==="), 40)
                .infixl(operator("=="), 40)
                
                .infixl(operator("??"), 40)
                .infixl(operator("||"), 40)
                .infixl(operator("&&"), 40)
                .infixl(operator("&amp;&amp;"), 40)
                .infixl(operator("&#38;&#38;"), 40)
                
                .prefix(prefix("!"), 90)
                
                .prefix(decrement(Position.prefix), 90)
                .postfix(decrement(Position.suffix), 90)
                
                .prefix(increment(Position.prefix), 90)
                .postfix(increment(Position.suffix), 90)
                
                .infixl(operator("**"), 110)
                .infixl(operator("*"), 100)
                .infixl(operator("/"), 100)
                .infixl(operator("%"), 100)
                .infixl(operator("+"), 60)
                .infixl(operator("-"), 60)
                
                .prefix(negative(), 120)
                .prefix(positive(), 120)
              
                .infixl(operator("^"), 80)
                .infixl(operator("|"), 80)
                .infixl(operator("&"), 80)
                .infixl(operator("&amp;"), 80)
                .prefix(prefix("~"), 80)
              
                .infixl(word("in"), 50)
                .infixl(word("instanceof"), 50)
                
                .prefix(prefix("typeof"), 90)
                .prefix(prefix("void"), 90)
                .buildMap(unit);
        
        reference.set(operatorTable);
        return operatorTable;
    }

    @SuppressWarnings("unused")
    private static Parser<MapInfix<JavascriptNode, JavascriptNode, JavascriptNode, Infix>> operator(final String symbol) {
        return token(symbol).map(v -> Operator.symbol(symbol))
                            .map(operator -> MapInfix.map(operator, (left, right) -> new Infix(left, operator, right)));
    } 
    
    @SuppressWarnings("unused")
    private static Parser<MapInfix<JavascriptNode, JavascriptNode, JavascriptNode, Infix>> word(final String symbol) {
         return token(symbol).map(v -> Operator.symbol(symbol))
                             .map(operator -> MapInfix.map(operator, (left, right) -> new Infix(left, operator, right)));
    } 
    
    @SuppressWarnings("unused")
    private static Parser<MapOperator<JavascriptNode, JavascriptNode, Increment>> increment(final Position position) {
        return phrase("+", "+").map(v -> Operator.symbol("++"))
                               .map(operator -> MapOperator.map(operator, operand -> new Increment(position, operator, operand)));
    }
    
    @SuppressWarnings("unused")
    private static Parser<MapOperator<JavascriptNode, JavascriptNode, Decrement>> decrement(final Position position) {
        return phrase("-", "-").map(v -> Operator.symbol("--"))
                               .map(operator -> MapOperator.map(operator, operand -> new Decrement(position, operator, operand)));
    }
    
    @SuppressWarnings("unused")
    private static Parser<MapOperator<JavascriptNode, JavascriptNode, JavascriptNode>> negative() {
        return token("-").map(v -> Operator.symbol("-"))
                         .map(operator -> MapOperator.map(operator, operand ->  {
            return new Prefix(operator, operand);
        }));
    }
    
    @SuppressWarnings("unused")
    private static Parser<MapOperator<JavascriptNode, JavascriptNode, JavascriptNode>> positive() {
        return token("+").map(v -> Operator.symbol("+"))
                         .map(operator -> MapOperator.map(operator, operand ->  {
            /** The result. */
            final JavascriptNode result;
            if (operand instanceof Literal 
                    && ((Literal) operand).isType(Literal.Type.integer)) {
                final Literal literal = (Literal) operand;
                result = new Literal(Literal.Type.integer, "-" + literal.getValue());
            }
            else if (operand instanceof Literal 
                        && ((Literal) operand).isType(Literal.Type.decimal)) {
                final Literal literal = (Literal) operand;
                result = new Literal(Literal.Type.decimal, "-" + literal.getValue());
            }
            else {
                result = new Prefix(operator, operand);
            }
            return result;
        }));
    }
    
    @SuppressWarnings("unused")
    private static Parser<MapOperator<JavascriptNode, JavascriptNode, JavascriptNode>> prefix(final String keyword) {
        return token(keyword).map(v -> Operator.symbol(keyword))
                             .map(operator -> MapOperator.map(operator, operand -> new Prefix(operator, operand)));
    }
    
    /*
     * 
     * TERNARY
     * 
     */
    private static Parser<JavascriptNode> ternaryExpression(final JavascriptConfig config) {
        return or(config.parser(assignment),
                  expression(config,
                             or(config.parser(assignableQualifiedExpression),
                                config.parser(assignableCallExpression),
                                config.parser(assignableInvokedFunction),
                                config.parser(invokedFunction),
                                config.parser(functionDeclaration),
                                config.parser(yield),
                                config.parser(newStatement),
                                config.parser(awaitStatement),
                                config.parser(deleteStatement),
                                config.parser(importFunctionCall),
                                config.parser(objectDeclaration),
                                config.parser(classDeclaration),
                                config.parser(debuggerStatement),
                                config.parser(array),
                                config.parser(atomic))),
                  config.parser(variableDeclaration),
                  config.parser(blockProcedure))
               .label("ternaryExpression");
    }
    
    private static Parser<JavascriptNode> ternaryStatement(final JavascriptConfig config) {
        return or(config.parser(assignment),
                  config.parser(expression),
                  config.parser(variableDeclaration),
                  config.parser(returnStatement),
                  config.parser(switchStatment),
                  config.parser(tryCatchFinally),
                  config.parser(throwStatement),
                  config.parser(doWhileStatement),
                  config.parser(whileStatement),
                  config.parser(forStatement),
                  config.parser(continueStatement),
                  config.parser(breakStatement),
                  config.parser(ifStatement),
                  config.parser(blockProcedure))
               .label("ternaryStatement");
    }
    
    static Parser<Ternary> ternary(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService()
                                  .lookFor(c, 
                                           Patterns.isChar('?').next(Patterns.isChar('.').not()),
                                           breakPattern()),
                       sequence(ternaryExpression(config),
                                token("?").notFollowedBy(token("."))
                                          .next(ternaryStatement(config)), 
                                token(":").next(ternaryStatement(config)), 
                                Ternary::new)
                        .label("ternary"));
    }
    
    /*
     * 
     * SEQUENCE
     * 
     */
    private static Parser<JavascriptNode> sequenceExpression(final JavascriptConfig config) {
        return or(config.parser(assignment),
                  config.parser(expression),
                  config.parser(spread),
                  config.parser(variableDeclaration),
                  config.parser(useStrict),
                  config.parser(returnStatement),
                  config.parser(switchStatment),
                  config.parser(tryCatchFinally),
                  config.parser(throwStatement),
                  config.parser(doWhileStatement),
                  config.parser(whileStatement),
                  config.parser(forStatement),
                  config.parser(continueStatement),
                  config.parser(breakStatement),
                  config.parser(ifStatement),
                  config.parser(labelStatement),
                  config.parser(blockProcedure))
               .label("sequenceExpression");
    }
    
    static Parser<Sequence> commaSequence(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService()
                                  .lookFor(c, 
                                           Patterns.isChar(','), 
                                           Patterns.isChar(';')),
                       sequenceExpression(config)
                        .optional(() -> new Empty())
                        .sepBy1(token(","))
                        .acceptIf(v -> v.size() > 1)
                        .map(Sequence::new)
                        .label("commaSequence"));
    }

    static Parser<Sequence> sequenceBetweenParenthesis(final JavascriptConfig config, 
                                                       final int min) {
        return parseIf(c -> min > 1
                              ? config.sequenceService().lookFor(c, Type.parenthesis, Patterns.isChar(',')) 
                              : config.sequenceService().parenthesis(c), 
                       sequenceExpression(config)
                          .optional(() -> new Empty())
                          .sepByBetween(JavascriptError::newInstance, token(","), token("("), token(")"))
                          .acceptIf(v -> v.size() >= min)
                          .apply(v -> {
                              final Sequence commaSequence = new Sequence(v);
                              commaSequence.setSourceLocation(new SourceLocation(v.getFirst().getSourceLocation().start(),
                                                                                 v.getLast().getSourceLocation().end()));
                              return commaSequence;
                          })
                          .label("sequenceBetweenParenthesis"));
    }
    
    /*
     * 
     * QUALIFIED
     * 
     */
    static Parser<QualifiedExpression> qualifiedIdentifier() {
        return sequence(sequence(or(identifier(), 
                                    thisParser(),
                                    keyword(propertyNames)),
                                 token(".").map(Operator::new),
                                 Qualifier::new).many1(), 
                        or(identifier(), 
                           keyword(propertyNames)),
                        QualifiedExpression::new);
    }

    static Parser<JavascriptNode> identifierOrQualifiedIdentifier() {
        return or(qualifiedIdentifier(),
                  or(identifier(), 
                     thisParser(),
                     keyword(propertyNames)));
    }
    
    @SuppressWarnings("unused")
    static Parser<QualifiedExpression> qualifiedExpression(final JavascriptConfig config, 
                                                           final boolean acceptAssignableInvokedFunction) {
        final Parser<Operator> operatorParser =
                or(token(".").map(v ->  Operator.symbol(v.toString())),
                   phrase("?", ".").map(v -> Operator.symbol("?.")));
        final Parser<Qualifier> rootQualifierParser =
            sequence(token("#").succeeds(),
                     or(callExpression(config, false, true, acceptAssignableInvokedFunction, true),
                        config.parser(invokedFunction),
                        acceptAssignableInvokedFunction 
                          ? config.parser(assignableInvokedFunction) 
                          : Parsers.never(),
                        config.parser(array),
                        config.parser(literalTemplate),
                        config.parser(newStatement),
                        config.parser(importFunctionCall),
                        string(),
                        regExpString(),
                        identifier(),
                        thisParser(),
                        keyword(propertyNames),
                        config.parser(sequenceBetweenParenthesis)), 
                     operatorParser,
                     (isPrivate, expression, operator) -> isPrivate 
                                                             ? new PrivateQualifier(expression, operator) 
                                                             : new Qualifier(expression, operator));
        
        final Parser.Reference<JavascriptNode> callExpressionWithOptionalName = Parser.newReference();
        callExpressionWithOptionalName.set(callExpression(config, true, false, false, false));
        final Parser<Qualifier> qualifierParser =
                sequence(token("#").succeeds(),
                         or(callExpressionWithOptionalName.lazy(),
                            keyword(propertyNames),
                            identifier()), 
                         operatorParser,
                         (isPrivate, expression, operator) -> isPrivate 
                                                                 ? new PrivateQualifier(expression, operator) 
                                                                 : new Qualifier(expression, operator));
        
        final Parser<QualifiedExpression> qualifiedExpressionParser =
               sequence(rootQualifierParser,
                        qualifierParser.many(),
                        sequence(token("#").succeeds(),
                                 or(callExpressionWithOptionalName.lazy(),
                                    keyword(propertyNames),
                                    identifier()),
                                    (isPrivate, expression) -> isPrivate 
                                                                  ? new PrivateExpression(expression) 
                                                                  : expression),
                        (rootQualifier, nextQualifiers, expression) -> {
                            final List<Qualifier> qualifiers = new ArrayList<>();
                            qualifiers.add(rootQualifier);
                            qualifiers.addAll(nextQualifiers);
                            return new QualifiedExpression(qualifiers, expression);
                        });
        
        return parseIf(c -> config.sequenceService()
                                  .lookFor(c, Patterns.or(Patterns.isChar('.'), 
                                                          Patterns.string("?."))),
                       qualifiedExpressionParser) 
                  .label("qualifiedExpression");
    }
    
    /*
     * 
     * CALL SIGNATURE
     * 
     */
    @SuppressWarnings("unused")
    static Parser<List<JavascriptNode>> functionCallSignature(final JavascriptConfig config) {
        return or(phrase("(", ")").map(v -> List.<JavascriptNode>of()), 
                  parseIf(c -> config.sequenceService().parenthesis(c),
                          or(config.parser(assignment),
                             config.parser(spread),
                             config.parser(expression))
                            .optional(() -> new Empty())
                                .sepByBetween(JavascriptError::newInstance, 
                                              token(","), 
                                              token("("), 
                                              token(")")))
                .label("functionCallSignature"));
    }
    
    /*
     * 
     * INVOKED FUNCTION
     * 
     */
    static Parser<JavascriptNode> assignableInvokedFunction(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService().assignableInvokedFunction(c),
                       sequence(functionDeclaration(config).map(InvokedFunction.Definition::new),
                                functionCallSignature(config).map(InvokedFunction.Signature::new),
                                (definition, signature) -> {
                                    /** The result. */
                                    final JavascriptNode result;
                                    if (signature != null) {
                                        result = new InvokedFunction(definition, signature);
                                    }
                                    else {
                                        result = definition.getFunction();
                                    }
                                    return result;
                                }))
                    .label("assignableInvokedFunction");
    }
    
    private static Parser<JavascriptNode> invokedFunction_1(final JavascriptConfig config) {
        return sequence(functionDeclaration(config).map(InvokedFunction.Definition::new),
                        functionCallSignature(config).map(InvokedFunction.Signature::new),
                        InvokedFunction::new)
                   .between(token("("), token(")"))
                   .map(v -> v)
                   .label("invokedFunction_1")
                   .cast();
    }
    
    private static Parser<JavascriptNode> invokedFunction_2(final JavascriptConfig config) {
        return sequence(functionDeclaration(config).between(token("("), token(")"))
                                                   .map(InvokedFunction.Definition::new),
                        functionCallSignature(config).map(InvokedFunction.Signature::new),
                        InvokedFunction::new)
                   .label("invokedFunction_2")
                   .cast();
    }

    static Parser<JavascriptNode> invokedFunction(final JavascriptConfig config) {
        return or(parseIf(c -> config.sequenceService().invokedFunction_1(c), 
                          invokedFunction_1(config)),
                  parseIf(c -> config.sequenceService().invokedFunction_2(c), 
                          invokedFunction_2(config)))
                .label("invokedFunction");
    }
    
    /*
     * 
     * CALL EXPRESSION
     * 
     */
    private static Parser<JavascriptNode> functionCall(final JavascriptConfig config, 
                                                       final boolean optionalName) {
        return parseIf(c -> config.sequenceService().functionCall(c, optionalName),
                       sequence(or(identifier(),
                                   keyword(functionNames),
                                   sequenceBetweenParenthesis(config, 2))
                                 .optional(() -> new Empty())
                                 .acceptIf(v -> optionalName || !(v instanceof Empty)),
                                or(functionCallSignature(config).map(FunctionCall.Signature::new), 
                                   config.parser(literalTemplate)),
                                (n, s) -> {
                                    /** The result. */
                                    JavascriptNode result;
                                    if (s instanceof FunctionCall.Signature signature) {
                                        result = new FunctionCall(n, signature);
                                    }
                                    else {
                                        result = new TagFunctionCall(n, (LiteralTemplate) s);
                                    }
                                    return result;
                                }))
                  .label("functionCall");
    }
    
    private static Parser<MapExpression.Bracket> mapExpressionBracket(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService().bracket(c),
                       config.parser(assignableValueWithChaining)
                             .between(JavascriptError::newInstance,
                                      token("["),
                                      token("]"))
                             .map(MapExpression.Bracket::new)
                             .label("mapExpressionBracket"));
    }
    
    private static Parser<MapExpression> mapExpression(final JavascriptConfig config, 
                                                       final boolean optionalName) {
        return parseIf(c -> config.sequenceService().lookFor(c, Patterns.isChar('[')),
                       sequence(or(identifier(), 
                                   config.parser(sequenceBetweenParenthesis))
                                 .optional(() -> new Empty())
                                 .acceptIf(v -> optionalName || !(v instanceof Empty)),
                                mapExpressionBracket(config),
                                MapExpression::new))
                 .label("mapExpression");
    }
    
    static Parser<JavascriptNode> callExpression(final JavascriptConfig config,  
                                                 final boolean optionalName,
                                                 final boolean acceptInvokedFunction,
                                                 final boolean acceptAssignableInvokedFunction,
                                                 final boolean acceptNewStatement) {
        return sequence(or(acceptInvokedFunction 
                              ? config.parser(invokedFunction)
                              : Parsers.never(),
                           acceptAssignableInvokedFunction 
                              ? config.parser(assignableInvokedFunction) 
                              : Parsers.never(),
                           acceptNewStatement 
                              ? config.parser(newStatement)
                              : Parsers.never(),
                           functionCall(config, optionalName),
                           mapExpression(config, optionalName)),
                        or(functionCallSignature(config).map(FunctionCall.Signature::new),
                           mapExpressionBracket(config),
                           config.parser(literalTemplate))
                         .many(),
                        (root, calls) -> {
                            JavascriptNode result = root;
                            final SourceLocation.Position start = root.getSourceLocation().start();
                            for (final JavascriptNode call : calls) {
                                if (call instanceof MapExpression.Bracket bracket) {
                                    result = new MapExpression(result, bracket);
                                    result.setSourceLocation(new SourceLocation(start, bracket.getSourceLocation().end()));
                                }
                                else if (call instanceof FunctionCall.Signature signature) {
                                    result = new FunctionCall(result, signature);
                                    result.setSourceLocation(new SourceLocation(start, signature.getSourceLocation().end()));
                                }
                                else if (call instanceof LiteralTemplate literalTemplate) {
                                    result = new TagFunctionCall(result, literalTemplate);
                                    result.setSourceLocation(new SourceLocation(start, literalTemplate.getSourceLocation().end()));
                                }
                            }
                            return result;
                        })
                 .label("callExpression" 
                           + (acceptInvokedFunction ? "[invokedFunction]" : "")
                           + (acceptAssignableInvokedFunction ? "[assignableInvokedFunction]" : ""));
    }

    /*
     * 
     * PARENTHESIS
     * 
     */
    static Parser<Parenthesis> parenthesis(final JavascriptConfig config,
                                           final Parser<JavascriptNode> parser) {
        return parseIf(c -> config.sequenceService().parenthesis(c),
                       parser.between(token("("), token(")"))
                             .map(Parenthesis::new)
                             .label("parenthesis"));
    }
    
    static Parser<JavascriptNode> parenthesisOptional(final Parser<JavascriptNode> parser, 
                                                      final JavascriptConfig config) {
        return or(parenthesis(config, parser), parser).label("parenthesisOptional");
    }

}
