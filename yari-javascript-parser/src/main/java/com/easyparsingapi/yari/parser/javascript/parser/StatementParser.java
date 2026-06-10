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
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.javascript.parser.ExpressionParser.functionCallSignature;
import static com.easyparsingapi.yari.parser.javascript.parser.ExpressionParser.identifierOrQualifiedIdentifier;
import static com.easyparsingapi.yari.parser.javascript.parser.ExpressionParser.sequenceBetweenParenthesis;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableValueWithChaining;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.blockProcedure;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.classDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.commaSequence;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.doWhileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.expression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.forStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.functionDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.ifStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.switchStatment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.variableDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.whileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.LiteralParser.string;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.endStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.procedureOrStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.SeparatorParser.returnCarriage;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.phrase;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;
import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.statement;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.pattern.Patterns;
import com.easyparsingapi.yari.parser.javascript.ast.Await;
import com.easyparsingapi.yari.parser.javascript.ast.Break;
import com.easyparsingapi.yari.parser.javascript.ast.ClassDeclaration;
import com.easyparsingapi.yari.parser.javascript.ast.Continue;
import com.easyparsingapi.yari.parser.javascript.ast.Debugger;
import com.easyparsingapi.yari.parser.javascript.ast.Delete;
import com.easyparsingapi.yari.parser.javascript.ast.DoWhile;
import com.easyparsingapi.yari.parser.javascript.ast.Empty;
import com.easyparsingapi.yari.parser.javascript.ast.EmptyIteration;
import com.easyparsingapi.yari.parser.javascript.ast.EmptyStatement;
import com.easyparsingapi.yari.parser.javascript.ast.For;
import com.easyparsingapi.yari.parser.javascript.ast.If;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.Label;
import com.easyparsingapi.yari.parser.javascript.ast.New;
import com.easyparsingapi.yari.parser.javascript.ast.NewClass;
import com.easyparsingapi.yari.parser.javascript.ast.Operator;
import com.easyparsingapi.yari.parser.javascript.ast.Return;
import com.easyparsingapi.yari.parser.javascript.ast.Switch;
import com.easyparsingapi.yari.parser.javascript.ast.Throw;
import com.easyparsingapi.yari.parser.javascript.ast.UseStrict;
import com.easyparsingapi.yari.parser.javascript.ast.While;
import com.easyparsingapi.yari.parser.javascript.ast.Switch.CaseProcedure;
import com.easyparsingapi.yari.parser.javascript.ast.Switch.DefaultCase;
import com.easyparsingapi.yari.parser.javascript.ast.Switch.SwitchCase;
import com.easyparsingapi.yari.parser.javascript.ast.Switch.SwitchExpression;
import com.easyparsingapi.yari.parser.javascript.ast.Switch.SwitchProcedure;

/**
 * Package-private static utility class providing parsers for JavaScript statements:
 * {@code if}, {@code for}, {@code while}, {@code do-while}, {@code switch},
 * {@code return}, {@code throw}, {@code break}, {@code continue}, {@code label},
 * {@code new}, {@code delete}, {@code await}, {@code debugger}, {@code "use strict"},
 * and empty statements.
 */
class StatementParser {

    /** Not instantiable — all methods are static. */
    private StatementParser() {}
    
    /*
     * 
     * WHILE
     * 
     */
    private static Parser<JavascriptNode> whileCondition(final JavascriptConfig config) {
        return or(sequenceBetweenParenthesis(config, 2), 
                  parseIf(c -> config.sequenceService().parenthesis(c), 
                          or(config.parser(assignment),
                             config.parser(expression))
                                .between(JavascriptError::newInstance, token("("), token(")"))))
                .label("whileCondition");
    }
    
    static Parser<DoWhile> doWhileStatement(final JavascriptConfig config) {
        return sequence(token("do").next(procedureOrStatement(config).map(DoWhile.Procedure::new)),
                        token("while").next(whileCondition(config).map(DoWhile.Condition::new)),
                        DoWhile::new)
                .label("doWhileStatement");
    }
    
    static Parser<While> whileStatement(final JavascriptConfig config) {
        return sequence(token("while").next(whileCondition(config).map(While.Condition::new)),
                        or(token(";").map(v -> null),
                           procedureOrStatement(config).map(While.Procedure::new)),
                        While::new)
                .label("whileStatement");
    }
    
    /*
     * 
     * FOR
     * 
     */
    private static Parser<For.ListIteration> forListIteration(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService()
                                  .lookFor(c, 
                                           Patterns.or(Patterns.string("in"), 
                                                       Patterns.string("of")),
                                           Patterns.isChar(')')),
                       sequence(or(config.parser(variableDeclaration), identifier()),
                                or(token("in"), token("of")).map(v -> Operator.symbol(v.toString())),  
                                config.parser(expression), 
                                For.ListIteration::new))
                .label("forListIteration");
    }
    
    private static Parser<For.Iteration> forIteration(final JavascriptConfig config) {
        return or(config.parser(commaSequence), 
                  config.parser(assignment),
                  config.parser(expression),
                  config.parser(variableDeclaration))
                .optional(() -> new Empty())
                .catchError(JavascriptError::newInstance, token(";"), token(")"), Parsers.never())
                .sepBy1(token(";"))
                .acceptIf(v -> v.size() == 3)
                .map(javascriptNodes -> {
                    final JavascriptNode first = javascriptNodes.get(0);
                    final JavascriptNode second = javascriptNodes.get(1);
                    final JavascriptNode third = javascriptNodes.get(2);
                    return new For.Iteration(first, second, third);
                })
                .label("forIteration");
    }
    
    static Parser<For> forStatement(final JavascriptConfig config) {
        return sequence(token("for").next(token("await").succeeds()),
                        or(phrase("(", ")").map(v -> new EmptyIteration()),
                           parseIf(c -> config.sequenceService().parenthesis(c),
                                   or(forListIteration(config),
                                      forIteration(config))
                                    .between(JavascriptError::newInstance,
                                             token("("), 
                                             token(")")))),
                        or(token(";").map(v -> null),
                           procedureOrStatement(config).map(For.Procedure::new)),
                        (await, iteration, procedure) -> new For(await, iteration, procedure))
                  .label("forStatement");
    }
    
    static Parser<Continue> continueStatement() {
        return token("continue").next(identifier().optional())
                                .map(Continue::new)
                                .label("continueStatement");
    }
    
    static Parser<Break> breakStatement() {
        return token("break").next(identifier().optional())
                             .map(Break::new)
                             .label("breakStatement");
    }
    
    /*
     * 
     * IF
     * 
     */
     static Parser<If> ifStatment(final JavascriptConfig config) {
        final Parser<If.IfBlock> ifBlockParser = 
            sequence(token("if").next(or(sequenceBetweenParenthesis(config, 2), 
                                         parseIf(c -> config.sequenceService().parenthesis(c),
                                                 or(config.parser(assignment),
                                                    config.parser(expression))
                                                       .between(JavascriptError::newInstance, 
                                                                token("("), 
                                                                token(")"))))
                                      .map(If.Condition::new)),
                     or(token(";").map(v -> null),
                        procedureOrStatement(config).map(If.Procedure::new)),
                If.IfBlock::new);
        final Parser<If.ElseIfBlock> elseIfBlockParser = 
            token("else").next(ifBlockParser)
                         .map(If.ElseIfBlock::new);
        final Parser<If.ElseBlock> elseBlockParser = 
            token("else").next(or(token(";").map(v -> null),
                                  procedureOrStatement(config)
                                 .map(If.Procedure::new)))
                         .map(If.ElseBlock::new);
       return sequence(ifBlockParser,
                       elseIfBlockParser.many(),
                       elseBlockParser.optional(),
                       If::new)
                .label("ifStatment");
    }
    
    /*
     * 
     * SWITCH
     * 
     */
     static Parser<Switch> switchStatment(final JavascriptConfig config) {
         return sequence(token("switch").next(or(sequenceBetweenParenthesis(config, 2),
                                                 parseIf(c -> config.sequenceService().parenthesis(c), 
                                                         or(config.parser(assignment),
                                                            config.parser(expression),
                                                            config.parser(variableDeclaration))
                                                          .between(JavascriptError::newInstance,
                                                                   token("("), 
                                                                   token(")"))))
                                               .map(SwitchExpression::new)),
                         parseIf(c -> config.sequenceService().curlingBracket(c),
                                 sequence(sequence(token("case").next(config.parser(expression).followedBy(token(":"))),
                                                   statement(config).followedBy(endStatement()).many().map(CaseProcedure::new),
                                                   SwitchCase::new)
                                              .many(),
                                          phrase("default", ":")
                                            .next(statement(config).followedBy(endStatement()).many().map(CaseProcedure::new))
                                            .map(DefaultCase::new)
                                            .optional(),
                                          SwitchProcedure::new)
                                       .optional()
                                       .between(token("{"), token("}"))),
                         Switch::new)
                    .label("switchStatment");
     }
     
    /*
     * 
     * OTHER
     * 
     */
    static Parser<UseStrict> useStrict() {
        return string()
                 .acceptIf(v -> {
                     boolean result = false;
                     final String[] split = v.getValue().split("\\s+");
                     if (split.length == 2
                             && split[0].equals("use")
                             && split[1].equals("strict")) {
                         result = true;
                     }
                     return result;
                 })
                 .map(v -> new UseStrict())
                 .label("useStrict");
    }
    
    static Parser<EmptyStatement> emptyStatement() {
        return or(token(";"), returnCarriage())
                .map(v -> new EmptyStatement())
                .label("emptyStatement");
    }
    
    static Parser<Debugger> debuggerStatement() {
        return token("debugger").map(v -> new Debugger())
                                .label("debuggerStatement");
    }
    
    static Parser<Return> returnStatement(final JavascriptConfig config) {
        return token("return").next(or(config.parser(commaSequence),
                                       config.parser(assignableValueWithChaining))
                                     .optional())
                              .map(Return::new)
                              .label("returnStatement");
    }
    
    static Parser<Await> awaitStatement(final JavascriptConfig config) {
        return token("await").next(config.parser(expression))
                             .map(Await::new)
                             .label("awaitStatement");
    }
    
    static Parser<Label> labelStatement(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService().lookFor(c, Patterns.isChar(':')), 
                       sequence(identifier().followedBy(token(":")),
                                or(config.parser(forStatement),
                                   config.parser(ifStatement),
                                   config.parser(whileStatement),
                                   config.parser(switchStatment),
                                   config.parser(doWhileStatement),
                                   config.parser(functionDeclaration),
                                   config.parser(blockProcedure)),
                                   Label::new))
                   .label("labelStatement");
    }

    static Parser<Throw> throwStatement(final JavascriptConfig config) {
        return token("throw").next(or(config.parser(commaSequence),
                                      config.parser(assignment),
                                      config.parser(expression)))
                             .map(Throw::new)
                             .label("throwStatement");
    }

    static Parser<Delete> deleteStatement(final JavascriptConfig config) {
        return token("delete")
                 .next(config.parser(expression))
                 .map(Delete::new)
                 .label("deleteStatement");
    }
    
    private static Parser<NewClass> newClass(final JavascriptConfig config) {
        return token("new").next(config.parser(classDeclaration))
                           .<ClassDeclaration>cast()
                           .map(NewClass::new)
                  .label("newClass");
    }

    private static Parser<New> newInstance(final JavascriptConfig config) {
        return sequence(token("new").next(identifierOrQualifiedIdentifier()),
                        functionCallSignature(config)
                           .map(New.Signature::new)
                           .optional(),
                        New::new)
                  .label("newStatement");
    }
    
    static Parser<JavascriptNode> newStatement(final JavascriptConfig config) {
        return or(newClass(config),
                  newInstance(config))
                .label("newStatement");
    }
    
}
