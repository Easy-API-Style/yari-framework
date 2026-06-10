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

import static com.easyparsingapi.yari.parser.javascript.parser.UnitParser.functionNamesAsSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.easyparsingapi.yari.parsec.Parsers.ParsingContext;
import com.easyparsingapi.yari.parsec.Parsers.TokenContext;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.pattern.Pattern;
import com.easyparsingapi.yari.parsec.pattern.Patterns;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptTag;

/**
 * Package-private look-ahead service used by parsers to inspect the token stream without
 * consuming it.  Determines whether the upcoming tokens form a recognised bracket sequence,
 * function declaration, invoked function, or contain a specific pattern.
 */
class SequenceService {
    
    static enum Type { bracket, curlingBracket, parenthesis }
    private static enum InvokedFunctionPattern { pattern_1, pattern_2, assignable, none }
    
//    private final Map<Integer, Sequence> cacheSequencesByTokenIndex = new ConcurrentHashMap<>();
    
    /*
     * 
     * FUNCTION CALL
     * 
     */
    boolean functionCall(final ParsingContext currentContext,
                         final boolean optionalName) {
        boolean result = false;
        
        final TokenContext tokenContext = currentContext.tokenContext();
        final Token[] tokens = tokenContext.tokens();
        final int index = tokenContext.index();
        
        if (optionalName) {
            final Sequence sequence = inside(Type.parenthesis, tokens, index);
            if (sequence.isValid()) {
                result = true;
            }
        }
        if (!result) {
            Object nextTokenTag = tag(tokens, index);
            String nextToken = get(tokens, index);
            if (JavascriptTag.WORD.equals(nextTokenTag)
                    || (JavascriptTag.KEYWORD.equals(nextTokenTag) && functionNamesAsSet.contains(nextToken))) {
                final Sequence sequence = inside(Type.parenthesis, tokens, index + 1);
                if (sequence.isValid()) {
                    if ("async".equals(nextToken)) {
                        nextToken = get(tokens, sequence.tokenIndex() + 1);
                        if (!"=>".equals(nextToken)) {
                            // case: async ()
                            result = true;
                        }
                    }
                    else {
                        // case: _ ()
                        result = true;
                    }
                }
                else {
                    nextTokenTag = tag(tokens, index + 1);
                    nextToken = get(tokens, index + 1);
                    if (JavascriptTag.LITERAL_TEMPLATE_KEYWORD.equals(nextTokenTag) 
                            && "`".equals(nextToken)) {
                        // case: _ ``
                        result = true;
                    }
                }
            }
            if (!result) {
                final Sequence sequence_1 = inside(Type.parenthesis, tokens, index);
                if (sequence_1.isValid()) {
                    final Sequence sequence_2 = inside(Type.parenthesis, tokens, sequence_1.tokenIndex() + 1);
                    if (sequence_2.isValid()) {
                        // case: ()()
                        result = true;
                    }
                    else {
                        nextTokenTag = tag(tokens, sequence_1.tokenIndex() + 1);
                        nextToken = get(tokens, sequence_1.tokenIndex() + 1);
                        if (JavascriptTag.LITERAL_TEMPLATE_KEYWORD.equals(nextTokenTag) 
                                && "`".equals(nextToken)) {
                            // case: ()``
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }
    
    /*
     * 
     * FUNCTION
     * 
     */
    boolean isFunctionDeclaration(final ParsingContext currentContext) {
        final TokenContext tokenContext = currentContext.tokenContext();
        final Token[] tokens = tokenContext.tokens();
        final int index = tokenContext.index();
        return functionLength(tokens, index) >= 0;
    }
    
    boolean isArrowFunctionDeclaration(final ParsingContext currentContext) {
        final TokenContext tokenContext = currentContext.tokenContext();
        final Token[] tokens = tokenContext.tokens();
        final int index = tokenContext.index();
        return arrowFunctionLengthBeginning(tokens, index) >= 0;
    }
            
    int isFunctionDeclaration(final Token[] tokens, 
                              final int index,
                              final boolean strict) {
        int result = functionLength(tokens, index);
        if (result == -1) {
            if (strict) {
                result = arrowFunctionLength(tokens, index);
            }
            else {
                result = arrowFunctionLengthBeginning(tokens, index);
            }
        }
        return result;
    }

    private int arrowFunctionLengthBeginning(final Token[] tokens, final int index) {
        int result = -1;
        
        /** The startAt. */
        int startAt;
        /** The firstTokens. */
        List<String> firstTokens;
        if (result == -1) {
            // case: async ()=>...
            startAt = -1;
            firstTokens = subList(tokens, index, 2);
            if (firstTokens.contains("(")) {
                startAt = firstTokens.indexOf("(");
            }
            if (startAt >= 0) {
                final Sequence sequence = inside(Type.parenthesis, tokens, index + startAt);
                if (sequence.isValid()) {
                    if ("=>".equals(get(tokens, sequence.tokenIndex() + 1))) {
                        result = sequence.tokenIndex() + 1;
                    }
                }
            }
        }
        if (result == -1) {
            // case: async _ =>...
            startAt = -1;
            if ("async".equals(get(tokens, index))
                   && JavascriptTag.WORD.equals(tag(tokens, index + 1))
                   && "=>".equals(get(tokens, index + 2))) {
                result = index + 3;
            }
            else if (JavascriptTag.WORD.equals(tag(tokens, index))
                   && "=>".equals(get(tokens, index + 1))) {
                result = index + 2;
            }
        }
        return result;
    }

    private int arrowFunctionLength(final Token[] tokens, final int index) {
        int result = -1;
        
        /** The startAt. */
        int startAt;
        /** The firstTokens. */
        List<String> firstTokens;
        if (result == -1) {
            // case: async ()=>{}
            startAt = -1;
            firstTokens = subList(tokens, index, 2);
            if (firstTokens.contains("(")) {
                startAt = firstTokens.indexOf("(");
            }
            if (startAt >= 0) {
                Sequence sequence = inside(Type.parenthesis, tokens, index + startAt);
                if (sequence.isValid()) {
                    if ("=>".equals(get(tokens, sequence.tokenIndex() + 1))) {
                        sequence = inside(Type.curlingBracket, tokens, sequence.tokenIndex() + 2);
                        if (sequence.isValid()) {
                            result = sequence.tokenIndex();
                        }
                    }
                }
            }
        }
        if (result == -1) {
            // case: async ()=>()
            startAt = -1;
            firstTokens = subList(tokens, index, 2);
            if (firstTokens.contains("(")) {
                startAt = firstTokens.indexOf("(");
            }
            if (startAt >= 0) {
                Sequence sequence = inside(Type.parenthesis, tokens, index + startAt);
                if (sequence.isValid()) {
                    if ("=>".equals(get(tokens, sequence.tokenIndex() + 1))) {
                        sequence = inside(Type.parenthesis, tokens, sequence.tokenIndex() + 2);
                        if (sequence.isValid()) {
                            sequence = inside(Type.parenthesis, tokens, sequence.tokenIndex() + 1);
                        }
                        if (sequence.isValid()) {
                            result = sequence.tokenIndex();
                        }
                    }
                }
            }
        }
        if (result == -1) {
            // case: async _ => {}
            startAt = -1;
            if ("async".equals(get(tokens, index))
                    && JavascriptTag.WORD.equals(tag(tokens, index + 1))
                    && "=>".equals(get(tokens, index + 2))
                    && "{".equals(get(tokens, index + 3))) {
                startAt = index + 3;
                Sequence sequence = inside(Type.curlingBracket, tokens, startAt);
                if (sequence.isValid()) {
                    result = sequence.tokenIndex();
                }
            }
            else if (JavascriptTag.WORD.equals(tag(tokens, index))
                       && "=>".equals(get(tokens, index + 1))
                       && "{".equals(get(tokens, index + 2))) {
                startAt = index + 2;
                Sequence sequence = inside(Type.curlingBracket, tokens, startAt);
                if (sequence.isValid()) {
                    result = sequence.tokenIndex();
                }
            }
        }
        if (result == -1) {
            // case: async _ => ()
            startAt = -1;
            if ("async".equals(get(tokens, index))
                    && JavascriptTag.WORD.equals(tag(tokens, index + 1))
                    && "=>".equals(get(tokens, index + 2))
                    && "(".equals(get(tokens, index + 3))) {
                startAt = index + 3;
                Sequence sequence = inside(Type.parenthesis, tokens, startAt);
                if (sequence.isValid()) {
                    result = sequence.tokenIndex();
                }
            }
            else if (JavascriptTag.WORD.equals(tag(tokens, index))
                   && "=>".equals(get(tokens, index + 1))
                   && "(".equals(get(tokens, index + 2))) {
                startAt = index + 2;
                Sequence sequence = inside(Type.parenthesis, tokens, startAt);
                if (sequence.isValid()) {
                    result = sequence.tokenIndex();
                }
            }
        }
        return result;
    }

    private int functionLength(final Token[] tokens, final int index) {
        int result = -1;
        
        /** The startAt. */
        int startAt;
        /** The firstTokens. */
        List<String> firstTokens;
        if (result == -1) {
            // case: async function * _ (){}
            startAt = -1;
            firstTokens = subList(tokens, index, 5);
            if (firstTokens.contains("function")) {
                startAt = index + firstTokens.indexOf("(");
            }
            if (startAt > 0) {
                Sequence sequence = inside(Type.parenthesis, tokens, startAt);
                if (sequence.isValid()) {
                    sequence = inside(Type.curlingBracket, tokens, sequence.tokenIndex() + 1);
                }
                if (sequence.isValid()) {
                    result = sequence.tokenIndex();
                }
            }
        }
        return result;
    }
    
    /*
     * 
     * INVOKED FUNCTION
     * 
     */
    boolean assignableInvokedFunction(final ParsingContext currentContext) {
        final TokenContext tokenContext = currentContext.tokenContext();
        return assignableInvokedFunction(tokenContext.tokens(), tokenContext.index());
    }
    
    boolean assignableInvokedFunction(final Token[] tokens, 
                                      final int index) {
        boolean result = false;
        final int isFunction = isFunctionDeclaration(tokens, index, true);
        if (isFunction > 0) {
            final int newIndex = isFunction + 1;
            if ("(".equals(get(tokens, newIndex))) {
                Sequence sequence = inside(Type.parenthesis, tokens, newIndex);
                if (sequence.isValid()) {
                    result = true;
                }
            }
        }
        return result;
    }
    
    boolean invokedFunction_1(final ParsingContext currentContext) { 
        return invokedFunction(currentContext, InvokedFunctionPattern.pattern_1);
    }
    
    boolean invokedFunction_2(final ParsingContext currentContext) { 
        return invokedFunction(currentContext, InvokedFunctionPattern.pattern_2);
    }

    private final Pattern invokedFunctionPattern = Patterns.or(Patterns.string("function"), 
                                                               Patterns.string("=>"));
    
    private boolean invokedFunction(final ParsingContext currentContext, 
                                    final InvokedFunctionPattern invokedFunction) {
        return invokedFunction == invokedFunction(currentContext);
    }

    private InvokedFunctionPattern invokedFunction(final ParsingContext currentContext) {
        InvokedFunctionPattern result = InvokedFunctionPattern.none;
        if (lookFor(currentContext, Type.parenthesis, invokedFunctionPattern)) {
            final TokenContext tokenContext = currentContext.tokenContext();
            final Token[] tokens = tokenContext.tokens();
            final int index = tokenContext.index();
            
            final Sequence sequence = inside(Type.parenthesis, tokens, index);
            if (sequence.isValid()) {
                final boolean isSequence = lookFor(currentContext, Type.parenthesis, Patterns.isChar(','));
                if (!isSequence) {
                    // case: (function())
                    if(assignableInvokedFunction(tokens, index + 1)) {
                        result = InvokedFunctionPattern.pattern_1;
                    }
                    else {
                        // case: (function)()
                        final int isFunction = isFunctionDeclaration(tokens, index + 1, false);
                        if (isFunction > 0) {
                            final Sequence newSequence = inside(Type.parenthesis, tokens, sequence.tokenIndex() + 1);
                            if (newSequence.isValid()) {
                                result = InvokedFunctionPattern.pattern_2;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    /*
     * 
     * LOOK FOR 
     * 
     */
    static Pattern breakPattern() {
        return Patterns.or(Patterns.isChar(';'), 
                           Patterns.isChar(','), 
                           Patterns.string("=>"),
                           /** Field. */
                           Patterns.RETURN_CARRIAGE);
    }

    boolean lookFor(final ParsingContext currentContext, 
                    final Pattern pattern) {
        return lookFor(currentContext, pattern, breakPattern());
    }
    
    boolean lookFor(final ParsingContext currentContext, 
                    final String pattern) {
        return lookFor(currentContext, Patterns.string(pattern), breakPattern());
    }
    
    boolean lookFor(final ParsingContext currentContext, 
                    final String pattern,
                    final Pattern util) {
        return lookFor(currentContext, Patterns.string(pattern), util);
    }
    
    boolean lookFor(final ParsingContext currentContext, 
                    final Type inside,
                    final Pattern pattern) {
        boolean result = inside(currentContext.tokenContext(), inside);
        if (result) {
            result = lookFor(currentContext, 
                             currentContext.tokenContext().index() + 1, 
                             pattern, 
                             util(inside));
        }
        return result;
    }
    
    boolean lookFor(final ParsingContext currentContext, 
                    final Pattern pattern,
                    final Pattern util) {
        return lookFor(currentContext, 
                       currentContext.tokenContext().index(), 
                       pattern, 
                       /** Field. */
                       util);
    }
    
   private boolean lookFor(final ParsingContext currentContext, 
                           final int tokenIndex,
                           final Pattern pattern,
                           final Pattern util) {
        boolean result = false;
        final Token[] tokens = currentContext.tokenContext().tokens(); 
        int i = -1;
        for (i = tokenIndex; i < tokens.length; i++) {
            final Token token = tokens[i];
            final String tokenValue = token.toString();
            if ("(".equals(tokenValue)
                    || "[".equals(tokenValue)
                    || "{".equals(tokenValue)) {
                final int matchPattern = pattern.match(token.toString(), 0, token.length());
                if (matchPattern > 0) {
                    result = true;
                    /** The break. */
                    break;
                }
                final Sequence sequenceResult = inside(typeOf(tokenValue), tokens, i);
                if (!sequenceResult.isValid()) {
                    /** The break. */
                    break;
                }
                else {
                    i = sequenceResult.tokenIndex();
                }
            }
            else {
                final int matchUtil = util.match(token.toString(), 0, token.length());
                if (matchUtil > 0) {
                    /** The break. */
                    break;
                }
                final int matchPattern = pattern.match(token.toString(), 0, token.length());
                if (matchPattern > 0) {
                    result = true;
                    /** The break. */
                    break;
                }
            }
        }
        return result;
    }
    
   /*
    * 
    * INSIDE 
    * 
    */
    boolean bracket(final ParsingContext currentContext) {
        return bracket(currentContext.tokenContext());
    }
    
    boolean bracket(final TokenContext tokenContext) {
        return inside(Type.bracket, tokenContext).isValid();
    }
    
    boolean curlingBracket(final ParsingContext currentContext) {
        return curlingBracket(currentContext.tokenContext());
    }
    
    boolean curlingBracket(final TokenContext tokenContext) {
        return inside(Type.curlingBracket, tokenContext).isValid();
    }
    
    boolean parenthesis(final ParsingContext currentContext) {
        return parenthesis(currentContext.tokenContext());
    }
    
    boolean parenthesis(final TokenContext tokenContext) {
        return inside(Type.parenthesis, tokenContext).isValid();
    }
    
    boolean inside(final TokenContext tokenContext, Type type) {
        return inside(type, tokenContext).isValid();
    }
   
    Sequence inside(final Type type, 
                    final TokenContext tokenContext) {
       return inside(type, tokenContext.tokens(), tokenContext.index());
    }
    
    private static record Deep(Type type, SourceLocation location) {}
    
    private Sequence inside(final Type type,
                            final Token[] tokens, 
                            final int tokenIndex) {
        if (type == null) {
            return new Sequence(new LinkedList<>(), tokenIndex, null, true, false);
        }
        if ((Type.parenthesis != type || !"(".equals(get(tokens, tokenIndex)))
                && (Type.curlingBracket != type || !"{".equals(get(tokens, tokenIndex)))
                && (Type.bracket != type || !"[".equals(get(tokens, tokenIndex)))) {
            return new Sequence(new LinkedList<>(), tokenIndex, null, true, false);
        }
        return sequence(tokens, tokenIndex);
    }
    
    private static Sequence sequence(final Token[] tokens, 
                                     final int tokenIndex) {
        final LinkedList<Deep> deep = new LinkedList<>();
        final LinkedList<SequenceElement> sequence = new LinkedList<>();
        
        SourceLocation errorLocation = null;
        boolean done = false;
        boolean failed = false;
        int i = -1;
        
        int startIndex = -1;
        for (i = tokenIndex; i < tokens.length; i++) {
            final Token token = tokens[i];
            if (Token.tag(token).toString().startsWith("LITERAL_TEMPLATE")) {
                /** The continue. */
                continue;
            }
            // ()
            if ("(".equals(token.toString())) {
                deep.add(new Deep(Type.parenthesis, token.sourceLocation()));
                if (deep.size() == 2) {
                    startIndex = i;
                }
            }
            else if (")".equals(token.toString())) {
                if (deep.isEmpty() || Type.parenthesis != deep.removeLast().type()) {
                    errorLocation = token.sourceLocation();
                    failed = true;
                    /** The break. */
                    break;
                }
                if (deep.size() == 2) {
                    sequence.add(new SequenceElement(Type.parenthesis, startIndex, i));
                }
            }
            // {}
            else if ("{".equals(token.toString())) {
                deep.add(new Deep(Type.curlingBracket, token.sourceLocation()));
                if (deep.size() == 2) {
                    startIndex = i;
                }
            }
            else if ("}".equals(token.toString())) {
                if (deep.isEmpty() || Type.curlingBracket != deep.removeLast().type()) {
                    errorLocation = token.sourceLocation();
                    failed = true;
                    /** The break. */
                    break;
                }
                if (deep.size() == 2) {
                    sequence.add(new SequenceElement(Type.curlingBracket, startIndex, i));
                }
            }
            // []
            else if ("[".equals(token.toString())) {
                deep.add(new Deep(Type.bracket, token.sourceLocation()));
                if (deep.size() == 2) {
                    startIndex = i;
                }
            }
            else if ("]".equals(token.toString())) {
                if (deep.isEmpty() || Type.bracket != deep.removeLast().type()) {
                    errorLocation = token.sourceLocation();
                    failed = true;
                    /** The break. */
                    break;
                }
                if (deep.size() == 2) {
                    sequence.add(new SequenceElement(Type.bracket, startIndex, i));
                }
            }
            if (deep.size() == 0) {
                done = true;
                /** The break. */
                break;
            }
        }
        return new Sequence(sequence, i, errorLocation, failed, done);
    }
    
    /*
     * 
     * UTIL
     * 
     */
    private static Type typeOf(final String value) {
        Type result = null;
        if ("(".equals(value)) {
            result = Type.parenthesis;
        }
        else if ("{".equals(value)) {
            result = Type.curlingBracket;
        }
        else if ("[".equals(value)) {
            result = Type.bracket;
        }
        return result;
    }
    
    private static Pattern util(final Type type) {
        /** The result. */
        final Pattern result;
        if (Type.bracket == type) {
            result = Patterns.isChar(']');
        }
        else if (Type.curlingBracket == type) {
            result = Patterns.isChar('}');
        }
        else {
            result = Patterns.isChar(')');
        }
        return result;
    }
    
    /*
     * 
     * TOKEN
     * 
     */
    private static Object tag(final Token[] tokens, 
                              final int index) {
        Object result = null;
        if (index < tokens.length) {
            result = ((Tokens.Fragment) tokens[index].value()).tag();
        }
        return result;
    }
    
    private static List<String> subList(final Token[] tokens, 
                                        final int from, 
                                        final int length) {
        final List<String> result = new ArrayList<>();
        final int to = from + length - 1;
        if (to < tokens.length) {
            for (int i = from; i <= to; i++) {
                result.add(tokens[i].toString());
            }
        }
        return result;
    }
    
    private static String get(final Token[] tokens, 
                              final int index) {
        String result = null;
        if (index < tokens.length) {
            result = tokens[index].toString();
        }
        return result;
    }
    
    /*
     * 
     * RECORD
     * 
     */
    static record SequenceElement(Type type, int start, int end) {}
    
    static record Sequence(LinkedList<SequenceElement> sequence, 
                           int tokenIndex,
                           SourceLocation errorLocation,
                           boolean failed,
                           boolean done) {
        
        boolean isValid() {
            return !failed && done;
        }
        
    }
   
}
