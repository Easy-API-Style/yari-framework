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

import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.array;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableCallExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableInvokedFunction;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableQualifiedExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableValue;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableValueWithChaining;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignment;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.atomic;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.awaitStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.blockProcedure;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.breakStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.callExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.classDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.commaSequence;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.continueStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.debuggerStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.deleteStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.destructuringArray;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.destructuringObject;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.doWhileStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.emptyStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.expression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.forStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.functionDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.ifStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.importFunctionCall;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.invokedFunction;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.javascriptExport;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.javascriptImport;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.labelStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.literalTemplate;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.newStatement;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.objectDeclaration;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.qualifiedExpression;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Parser.Reference;
import com.easyparsingapi.yari.parsec.Parser.ResultContext;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.location.SourceLocator;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptComment;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptTag;

/**
 * Parser configuration for the JavaScript parser.
 * Holds the full set of grammar rule references (one per {@link Node} kind),
 * the token list produced by the lexer, the source locator, and the list of
 * comments extracted during lexing.  Implements {@link ApiParser.Config} so it
 * can be handed directly to the generic {@code ApiParser} infrastructure.
 */
public class JavascriptConfig implements ApiParser.Config {

//    private static final Logger LOGGER = LoggerFactory.getLogger(JavascriptConfig.class);
    
    /**
     * Enumerates every grammar rule (node kind) supported by the JavaScript parser.
     * Each constant identifies one {@link com.easyparsingapi.yari.parsec.Parser.Reference}
     * that is lazily resolved during parsing.
     */
    public static enum Node {
        /** Atomic expression. */
        atomic,

        /** Block procedure (brace-delimited statement list). */
        blockProcedure,

        /** Literal template expression (back-tick string). */
        literalTemplate,

        /** Dynamic {@code import()} function call. */
        importFunctionCall,
        /** Static {@code import} declaration. */
        javascriptImport,
        /** {@code export} declaration. */
        javascriptExport,

        /** General expression. */
        expression,
        /** Qualified member-access expression. */
        qualifiedExpression,
        /** Assignable qualified member-access expression. */
        assignableQualifiedExpression,
        /** Call expression. */
        callExpression,
        /** Assignable call expression. */
        assignableCallExpression,
        /** Ternary conditional expression. */
        ternary,
        /** Comma-separated sequence expression. */
        commaSequence,
        /** Parenthesised sequence expression. */
        sequenceBetweenParenthesis,
        /** Immediately invoked function expression. */
        invokedFunction,

        /** {@code yield} expression. */
        yield,
        /** Assignment expression. */
        assignment,
        /** Assignable value expression. */
        assignableValue,
        /** Assignable value expression with optional chaining. */
        assignableValueWithChaining,
        /** Assignable invoked function expression. */
        assignableInvokedFunction,

        /** Array literal expression. */
        array,
        /** Spread expression ({@code ...expr}). */
        spread,
        /** Variable declaration ({@code var}, {@code let}, or {@code const}). */
        variableDeclaration,
        /** Array destructuring pattern. */
        destructuringArray,
        /** Object destructuring pattern. */
        destructuringObject,
        /** Object literal declaration. */
        objectDeclaration,
        /** Class declaration. */
        classDeclaration,
        /** Function declaration. */
        functionDeclaration,

        /** {@code "use strict"} directive. */
        useStrict,
        /** Empty statement ({@code ;}). */
        emptyStatement,
        /** Labelled statement. */
        labelStatement,
        /** {@code debugger} statement. */
        debuggerStatement,
        /** {@code await} statement. */
        awaitStatement,
        /** {@code if} / {@code else} statement. */
        ifStatement,
        /** {@code new} expression used as a statement. */
        newStatement,
        /** {@code return} statement. */
        returnStatement,
        /** {@code switch} statement. */
        switchStatment,
        /** {@code try} / {@code catch} / {@code finally} statement. */
        tryCatchFinally,
        /** {@code delete} statement. */
        deleteStatement,
        /** {@code throw} statement. */
        throwStatement,
        /** {@code do-while} statement. */
        doWhileStatement,
        /** {@code while} statement. */
        whileStatement,
        /** {@code for} statement. */
        forStatement,
        /** {@code continue} statement. */
        continueStatement,
        /** {@code break} statement. */
        breakStatement
    } 
    
    private final Map<Node, Reference<JavascriptNode>> nodeReferences = new HashMap<>();
    private final SequenceService sequenceService = new SequenceService();
    
    /** The tokens. */
    private List<Token> tokens;
    /** The comments. */
    private List<AstComment> comments;
    /** The sourceLocator. */
    private SourceLocator sourceLocator;
    
    /**
     * Creates a new {@code JavascriptConfig} and eagerly initialises all grammar rule references.
     */
    public JavascriptConfig() {
        super();
        this.initializeReference();
    }
    
    /**
     * Returns the full, unfiltered token list produced by the lexer for the current source.
     *
     * @return the list of tokens, or {@code null} if no source has been processed yet
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Returns the source locator used to map token offsets to line/column positions.
     *
     * @return the {@link SourceLocator} for the current source, or {@code null} if not yet set
     */
    public SourceLocator getSourceLocator() {
        return sourceLocator;
    }

    /**
     * Returns the list of comments extracted from the current source during lexing.
     *
     * @return the list of {@link AstComment} instances found in the source
     */
    public List<AstComment> getComments() {
        return comments;
    }

    /**
     * Returns the shared {@link SequenceService} used by sub-parsers to handle
     * delimiter and separator sequences.
     *
     * @return the sequence service for this configuration
     */
    protected SequenceService sequenceService() {
        return sequenceService;
    }

    private List<AstComment> comments(final SourceLocator sourceLocator, 
                                      final List<Token> tokens) {
        final List<AstComment> result = new ArrayList<>();
        for (final Token token : tokens) {
            if (token.value() instanceof Fragment fragment) {
                JavascriptComment javascriptComment = null;
                if (JavascriptTag.BLOCK_COMMENT.equals(fragment.tag())) {
                    javascriptComment = new JavascriptComment(JavascriptComment.Type.block, fragment.text());
                }
                else if (JavascriptTag.LINE_COMMENT.equals(fragment.tag())) {
                    javascriptComment = new JavascriptComment(JavascriptComment.Type.line, fragment.text());
                }
                if (javascriptComment != null) {
                    final SourceLocation sourceLocation = new SourceLocation(sourceLocator.locate(token.index()), 
                                                                             sourceLocator.locate(token.index() + token.length()));
                    javascriptComment.setSourceLocation(sourceLocation);
                    result.add(javascriptComment);
                }
            }
        }
        return result;
    }
    
    private void initializeReference() {
        for (final Node node : Node.values()) {
            this.nodeReferences.put(node, Parser.newReference());
        }
        this.setNodeReference(atomic, AtomicParser.atomic());
        
        this.setNodeReference(blockProcedure, SeparatorParser.blockProcedure(this));
        
        this.setNodeReference(literalTemplate, LiteralParser.literalTemplate(this));
        
        this.setNodeReference(importFunctionCall, ImportParser.importFunctionCall(this));
        this.setNodeReference(javascriptImport, ImportParser.javascriptImport(this));
        this.setNodeReference(javascriptExport, ExportParser.javascriptExport(this));
        
        this.setNodeReference(expression, ExpressionParser.expression(this));
        this.setNodeReference(qualifiedExpression, ExpressionParser.qualifiedExpression(this, false));
        this.setNodeReference(assignableQualifiedExpression, ExpressionParser.qualifiedExpression(this, true));
        this.setNodeReference(callExpression, ExpressionParser.callExpression(this, false, true, false, true));
        this.setNodeReference(assignableCallExpression, ExpressionParser.callExpression(this, false, true, true, true));
        this.setNodeReference(ternary, ExpressionParser.ternary(this));
        this.setNodeReference(commaSequence, ExpressionParser.commaSequence(this));
        this.setNodeReference(sequenceBetweenParenthesis, ExpressionParser.sequenceBetweenParenthesis(this, 1));
        this.setNodeReference(invokedFunction, ExpressionParser.invokedFunction(this));
        this.setNodeReference(assignableInvokedFunction, ExpressionParser.assignableInvokedFunction(this));
        
        this.setNodeReference(yield, AssignmentParser.yield(this));
        this.setNodeReference(assignment, AssignmentParser.assignment(this));
        this.setNodeReference(assignableValue, AssignmentParser.assignableValue(this));
        this.setNodeReference(assignableValueWithChaining, AssignmentParser.assignableValueWithChaining(this));
        
        this.setNodeReference(spread, DeclarationParser.spread(this));
        this.setNodeReference(array, DeclarationParser.array(this));
        this.setNodeReference(variableDeclaration, DeclarationParser.variableDeclaration(this, false));
        this.setNodeReference(destructuringArray, DeclarationParser.destructuringArray(this));
        this.setNodeReference(objectDeclaration, DeclarationParser.objectDeclaration(this));
        this.setNodeReference(destructuringObject, DeclarationParser.destructuringObject(this));
        this.setNodeReference(classDeclaration, DeclarationParser.classDeclaration(this));
        this.setNodeReference(functionDeclaration, DeclarationParser.functionDeclaration(this));
        
        this.setNodeReference(useStrict, StatementParser.useStrict());
        this.setNodeReference(emptyStatement, StatementParser.emptyStatement());
        this.setNodeReference(returnStatement, StatementParser.returnStatement(this));
        this.setNodeReference(ifStatement, StatementParser.ifStatment(this));
        this.setNodeReference(labelStatement, StatementParser.labelStatement(this));
        this.setNodeReference(awaitStatement, StatementParser.awaitStatement(this));
        this.setNodeReference(newStatement, StatementParser.newStatement(this));
        this.setNodeReference(debuggerStatement, StatementParser.debuggerStatement());
        this.setNodeReference(switchStatment, StatementParser.switchStatment(this));
        this.setNodeReference(tryCatchFinally, SeparatorParser.tryCatchFinally(this));
        this.setNodeReference(deleteStatement, StatementParser.deleteStatement(this));
        this.setNodeReference(throwStatement, StatementParser.throwStatement(this));
        this.setNodeReference(doWhileStatement, StatementParser.doWhileStatement(this));
        this.setNodeReference(whileStatement, StatementParser.whileStatement(this));
        this.setNodeReference(forStatement, StatementParser.forStatement(this));
        this.setNodeReference(continueStatement, StatementParser.continueStatement());
        this.setNodeReference(breakStatement, StatementParser.breakStatement());
    }
    
    @SuppressWarnings("unchecked")
    private <A extends JavascriptNode> void setNodeReference(final Node node, Parser<A> parser) {
        final Reference<A> reference = (Reference<A>) this.nodeReferences.get(node);
        reference.set(parser);
    }

    /**
     * Returns a lazily resolved parser for the given grammar rule node.
     *
     * @param node the grammar rule whose parser should be retrieved
     * @return a {@link Parser} that resolves the reference for the given node
     */
    protected Parser<JavascriptNode> parser(final Node node) {
        return this.nodeReferences.get(node).lazy();
    }
    
    /** {@inheritDoc} */
    @Override
    public void onTokens(final SourceLocator sourceLocator,
                         final List<Token> tokens) {
        this.sourceLocator = sourceLocator;
        this.tokens = tokens;
        this.comments = comments(sourceLocator, tokens);
    }
    
    /** {@inheritDoc} */
    @Override
    public Function<ResultContext, Object> onMap() {
        return c -> {
            if (c.value() instanceof SourceLocalisable sourceLocalisable) {
                sourceLocalisable.setSourceLocation(c.sourceLocation());
            }
            return c.value();
        };
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Token> filter(final List<Token> tokens) {
        List<Token> result = removeComment(tokens);
        result = removeReturnCarriage(result, Way.before);
        result = removeReturnCarriage(result, Way.after);
        return result;
    }
    
    private static enum Way { before, after }
    
    private static Set<Object> reservedTags =
        Set.of(JavascriptTag.KEYWORD,
               JavascriptTag.LITERAL_TEMPLATE_KEYWORD);
    
    private static Set<String> exceptKeywords =
        Set.of("debugger",
               "continue",
               "break",
               "throw",
               "return");
    
    private static List<Token> removeReturnCarriage(final List<Token> tokens, 
                                                    final Way way) {
        final List<Token> input = new ArrayList<>(tokens);
        if (Way.before == way) {
            Collections.reverse(input);
        }
        final List<Token> result = new ArrayList<>();
        String keyword = null; 
        int index = 0;
        for (final Token token : input) {
            if (token.value() instanceof Fragment fragment) {
                boolean doAdd = true;
                if (reservedTags.contains(fragment.tag())) {
                    keyword = fragment.text(); 
                }
                else if (keyword != null 
                           && JavascriptTag.RETURN_CARRIAGE.equals(fragment.tag())) {
                    Token _token = null;
                    if (Way.after == way) {
                        final int indexBefore = index - 1;
                        if (indexBefore >= 0) {
                            _token = input.get(indexBefore);
                        }
                    }
                    else {
                        final int indexAfter= index + 1;
                        if (indexAfter < input.size()) {
                            _token = input.get(indexAfter);
                        }
                    }
                    if (_token != null && exceptKeywords.contains(_token.toString())) {
                        result.add(token);
                        continue;
                    }
                    doAdd = false;
                }
                else {
                    keyword = null; 
                }
                if (doAdd) {
                    result.add(token);
                }
            }
            index++;
        }
        if (Way.before == way) {
            Collections.reverse(result);
        }
        return result;
    }

    private static List<Token> removeComment(final List<Token> tokens) {
        final List<Token> result = new ArrayList<>();
        for (final Token token : tokens) {
            if (token.value() instanceof Fragment fragment) {
                boolean doAdd = true;
                if (JavascriptTag.BLOCK_COMMENT.equals(fragment.tag())
                        || JavascriptTag.LINE_COMMENT.equals(fragment.tag())) {
                    doAdd = false;
                }
                if (doAdd) {
                    result.add(token);
                }
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(JavascriptConfig.class.getSimpleName());
        result.append(" [tokens=");
        result.append(tokens != null ? tokens.size() : -1);
        result.append("]");
        return result.toString();
    }
    
}
