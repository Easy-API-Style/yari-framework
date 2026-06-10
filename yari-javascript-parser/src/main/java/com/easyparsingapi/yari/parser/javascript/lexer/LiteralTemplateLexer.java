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
package com.easyparsingapi.yari.parser.javascript.lexer;

import static com.easyparsingapi.yari.parsec.pattern.Pattern.MISMATCH;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.pattern.Pattern;
import com.easyparsingapi.yari.parsec.pattern.Patterns;

/**
 * Static utility class that tokenises JavaScript template literals (back-tick strings)
 * into a flat list of {@link com.easyparsingapi.yari.parsec.Token} instances.
 * Template-literal variable segments ({@code ${...}}) are expanded recursively by
 * delegating back to {@link JavascriptLexer}, which handles nested template literals.
 */
public class LiteralTemplateLexer {

    /** Not instantiable — all methods are static. */
    private LiteralTemplateLexer() {}

    private static class LiteralTemplateLexerContext {
        
        /** The insideVariable. */
        boolean insideVariable;
        
        boolean insideVariable() {
            return insideVariable;
        }
        
        void insideVariable(boolean insideVariable) {
            this.insideVariable = insideVariable;
        }
        
    }
    
    static final Parser<String> variableLiteralTemplate(final LiteralTemplateLexerContext lexerContext) {
       return Pattern.rule(context -> {
            if (lexerContext.insideVariable()) {
                final CharSequence src = context.src();
                final int begin = context.begin();
                final int end = context.end();
                
                if (begin >= end) {
                    return MISMATCH;
                }
                
                int length = 0;
                final LinkedList<Type> deep = new LinkedList<>();
                deep.add(Type.curlingBracket);
                for (int i = begin; i < src.length(); i++) {
                    /** Field. */
                    length++;
                    if (src.charAt(i) == '\\') {
                        /** Field. */
                        length++;
                        /** Field. */
                        i++;
                    } 
                    else if (src.charAt(i) == '{') {
                        deep.add(Type.curlingBracket);
                        /** Field. */
                        length++;
                        /** Field. */
                        i++;
                    }
                    else if (src.charAt(i) == '}') {
                        deep.removeLast();
                        if (deep.isEmpty()) {
                            return notZero(length - 1);
                        }
                    }
                }
            }
            return MISMATCH;
        })
        .toScanner(JavascriptTag.LITERAL_TEMPLATE_VARIABLE.name())
        .source();
    }
    
    static final Parser<String> startVariableLiteralTemplate(final LiteralTemplateLexerContext lexerContext) {
        return Pattern.rule(context -> {
            final CharSequence src = context.src();
            final int begin = context.begin();
            final int end = context.end();
            
            if (begin >= end) {
                return MISMATCH;
            }
            int nextIndex = begin + 1;
            if (nextIndex < end
                    && src.charAt(begin) == '$'
                    && src.charAt(nextIndex) == '{') {
                lexerContext.insideVariable(true);
                return 2; 
            }
            return MISMATCH;
        })
        .toScanner("START_VARIABLE_LITERAL_TEMPLATE")
        .source();
    }
    
    static final Parser<String> endVariableLiteralTemplate(final LiteralTemplateLexerContext lexerContext) {
        return Pattern.rule(context -> {
            if (lexerContext.insideVariable()) {
                final CharSequence src = context.src();
                final int begin = context.begin();
                final int end = context.end();
                
                if (begin >= end) {
                    return MISMATCH;
                }
                if (src.charAt(begin) == '}')  {
                    lexerContext.insideVariable(false);
                    return 1; 
                }
            }
            return MISMATCH;
        })
        .toScanner("END_VARIABLE_LITERAL_TEMPLATE") 
        .source();
    };
    
    private static enum Type { backtick, curlingBracket }

    static final Parser<String> LITERAL_TEMPLATE = Pattern.rule(context -> {
        final CharSequence src = context.src();
        final int begin = context.begin();
        final int end = context.end();
        
        if (begin >= end) {
            return MISMATCH;
        }
        if (src.charAt(begin) == '`') {
            int length = 0;
            final LinkedList<Type> deep = new LinkedList<>();
            
            for (int i = begin; i < src.length(); i++) {
                /** Field. */
                length++;
                if (src.charAt(i) == '\\') {
                    /** Field. */
                    length++;
                    /** Field. */
                    i++;
                } 
                else if (src.charAt(i) == '`') {
                    if (deep.isEmpty()) {
                        deep.add(Type.backtick);
                    }
                    else {
                        Type lastType = deep.getLast();
                        if (Type.curlingBracket.equals(lastType)) {
                            deep.add(Type.backtick);
                        }
                        else  if (Type.backtick.equals(lastType)) {
                            deep.removeLast();
                        }
                    }
                    if (deep.isEmpty()) {
                        return length;
                    }
                }
                else if (Patterns.string("${").match(src, i, end) > 0) {
                    if (deep.isEmpty()) {
                        return MISMATCH;
                    }
                    else {
                        Type lastType = deep.getLast();
                        if (Type.backtick.equals(lastType)) {
                            deep.add(Type.curlingBracket);
                            /** Field. */
                            length++;
                            /** Field. */
                            i++;
                        }
                        else {
                            return MISMATCH;
                        }
                    }
                }
                else if (src.charAt(i) == '}') {
                    if (deep.isEmpty()) {
                        return MISMATCH;
                    }
                    else {
                        Type lastType = deep.getLast();
                        if (Type.curlingBracket.equals(lastType)) {
                            deep.removeLast();
                        }
                    }
                }
            }
        }
        return MISMATCH;
    })
    .toScanner(JavascriptTag.LITERAL_TEMPLATE.name())
    .source();

    static final Parser<String> literalTemplateBacktick(final LiteralTemplateLexerContext lexerContext) {
        return Pattern.rule(context -> {
            if (!lexerContext.insideVariable()) {
                final CharSequence src = context.src();
                final int begin = context.begin();
                final int end = context.end();
                
                if (begin >= end) {
                    return MISMATCH;
                }
                if (src.charAt(begin) == '`')  {
                    return 1;
                }
            }
            return MISMATCH;
        })
        .toScanner("LITERAL_TEMPLATE_BACKTICK")
        .source();
    }

    static final Parser<String> literalTemplateElement(final LiteralTemplateLexerContext lexerContext) {
        return Pattern.rule(context -> {
            final CharSequence src = context.src();
            final int begin = context.begin();
            final int end = context.end();
            
            if (begin >= end) {
                return MISMATCH;
            }
            int length = 0;
            for (int i = begin; i < src.length(); i++) {
                /** Field. */
                length++;
                if (src.charAt(i) == '\\') {
                    /** Field. */
                    length++;
                    /** Field. */
                    i++;
                } 
                else if (src.charAt(i) == '`') {
                    return notZero(length - 1);
                } 
                else if (Patterns.string("${").match(src, i, end) > 0) {
                    return notZero(length - 1);
                }
            }
            return MISMATCH;
        })
        .toScanner(JavascriptTag.LITERAL_TEMPLATE_ELEMENT.name())
        .source();
    }
    
    private static int notZero(final int value) {
        return value == 0 ? 1 : value;
    }
    
    private static final Parser<Fragment> literalTemplateTokenizer() {
        final LiteralTemplateLexerContext lexerContext = new LiteralTemplateLexerContext();
        return Parsers.or(startVariableLiteralTemplate(lexerContext).map(hit -> Tokens.fragment(hit, JavascriptTag.LITERAL_TEMPLATE_KEYWORD)),
                          endVariableLiteralTemplate(lexerContext).map(hit -> Tokens.fragment(hit, JavascriptTag.LITERAL_TEMPLATE_KEYWORD)),
                          variableLiteralTemplate(lexerContext).map(hit -> Tokens.fragment(hit, JavascriptTag.LITERAL_TEMPLATE_VARIABLE)),
                          literalTemplateBacktick(lexerContext).map(hit -> Tokens.fragment(hit, JavascriptTag.LITERAL_TEMPLATE_KEYWORD)),
                          literalTemplateElement(lexerContext).map(hit -> Tokens.fragment(hit, JavascriptTag.LITERAL_TEMPLATE_ELEMENT)));
    }
    
    private static Parser<Void> delimitedWhiteSpace() {
        return Parsers.always();
    }

    /**
     * Returns a parser that tokenises a template literal source string into a list of tokens,
     * expanding nested variable segments recursively.
     *
     * @return a {@link Parser} that produces a {@link List} of {@link Token} instances
     */
    public static Parser<List<Token>> lexer() {
        return ApiParser.lexer(literalTemplateTokenizer(), delimitedWhiteSpace())
                        .map(LiteralTemplateLexer::lexRecursively);
    }

    /**
     * Tokenises the given template literal source string into a flat list of tokens.
     * Nested {@code ${...}} variable segments are expanded recursively.
     *
     * @param literalTemplate the raw template literal source (back-tick string)
     * @return the ordered list of {@link Token} instances
     */
    public static List<Token> lex(final String literalTemplate) {
        final List<Token> tokens = ApiParser.lex(literalTemplateTokenizer(), delimitedWhiteSpace(), literalTemplate);
        return lexRecursively(tokens);
    }

    /**
     * Re-tokenises an existing token whose text represents a template literal.
     * Nested {@code ${...}} variable segments are expanded recursively.
     *
     * @param literalTemplate the token whose text should be re-lexed as a template literal
     * @return the ordered list of {@link Token} instances
     */
    public static List<Token> lex(final Token literalTemplate) {
        final List<Token> tokens = ApiParser.lex(literalTemplateTokenizer(), delimitedWhiteSpace(), literalTemplate);
        return lexRecursively(tokens);
    }

    private static List<Token> lexRecursively(final List<Token> tokens) {
        final List<Token> result = new ArrayList<>();
        for (final Token token : tokens) {
            if (JavascriptTag.LITERAL_TEMPLATE_VARIABLE.equals(Token.tag(token))) {
                final List<Token> jsTokens = JavascriptLexer.lex(token);
                for (final Token jsToken : jsTokens) {
                    if (JavascriptTag.LITERAL_TEMPLATE.equals(Token.tag(jsToken))) {
                        result.addAll(lex(jsToken));
                    }
                    else {
                        result.add(jsToken);
                    }
                }
            }
            else {
                result.add(token);
            }
        }
        return result;
    }
    
}
