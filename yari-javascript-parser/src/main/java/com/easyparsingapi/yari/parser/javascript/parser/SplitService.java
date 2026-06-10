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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptLexer;

/**
 * Utility service that splits a JavaScript source string (or token list) into top-level
 * bracket sections ({@code ()}, {@code {}}, {@code []}), each carrying its own nested
 * sub-sections for further analysis.
 */
public class SplitService {

    /** Not instantiable — all methods are static. */
    private SplitService() {}

    /**
     * Enumerates the three bracket types that delimit a {@link Section}.
     */
    public static enum Type {
        /** Square-bracket section ({@code [ ... ]}). */
        bracket,
        /** Curly-bracket section ({@code { ... }}). */
        curlingBracket,
        /** Parenthesis section ({@code ( ... )}). */
        parenthesis
    }

    /**
     * The start and end tokens that delimit a top-level bracket section in the token stream.
     *
     * @param start the opening bracket token
     * @param end   the closing bracket token
     */
    public static record Position(Token start,
                                  Token end) {

        /**
         * Returns the source location of the opening bracket token.
         *
         * @return the start {@link SourceLocation}
         */
        SourceLocation startSourceLocation() {
            return start.sourceLocation();
        }

        /**
         * Returns the token-stream index of the opening bracket.
         *
         * @return the start index
         */
        int startIndex() {
            return start.index();
        }

        /**
         * Returns the source location of the closing bracket token.
         *
         * @return the end {@link SourceLocation}
         */
        SourceLocation endSourceLocation() {
            return end.sourceLocation();
        }

        /**
         * Returns the token-stream index immediately after the closing bracket.
         *
         * @return the exclusive end index
         */
        int endIndex() {
            return end.index() + end.length();
        }

    }

    /**
     * A top-level bracket section in the token stream, carrying the bracket type,
     * the tokens contained within the brackets, the position of the delimiters,
     * and any nested sub-sections.
     *
     * @param type     the bracket type of this section
     * @param tokens   the tokens inside the brackets (excluding the delimiters)
     * @param position the source position of the opening and closing brackets
     * @param sections the nested bracket sections found within this section
     */
    public static record Section(Type type,
                                 List<Token> tokens,
                                 Position position,
                                 List<Section> sections) {

    }

    /**
     * The root result of splitting a JavaScript source, containing all top-level
     * tokens and the top-level bracket sections extracted from them.
     *
     * @param tokens   the full ordered token list produced by the lexer
     * @param sections the top-level bracket sections extracted from the token list
     */
    public static record Root(List<Token> tokens,
                              List<Section> sections) {}

    /**
     * Lexes the given JavaScript source string and splits the resulting token list
     * into top-level bracket sections.
     *
     * @param javascript the raw JavaScript source to split
     * @return a {@link Root} containing the full token list and the top-level sections
     */
    public static Root split(final String javascript) {
        final List<Token> tokens = ApiParser.lex(JavascriptLexer.lexer(), javascript);
        final List<Section> sections = split(tokens);
        return new Root(tokens, sections);
    }
    
    private static List<Section> split(final List<Token> tokens) {
        final List<Section> result = new ArrayList<>();
        
        final LinkedList<Type> deep = new LinkedList<>();
        Type type = null;
        Token startToken = null;
        List<Token> sectionTokens = null;
        for (final Token token : tokens) {
            if (Token.tag(token).toString().startsWith("LITERAL_TEMPLATE")) {
                /** The continue. */
                continue;
            }
            // ()
            if ("(".equals(token.toString())) {
                if (deep.isEmpty()) {
                    type = Type.parenthesis;
                    startToken = token;
                    sectionTokens = new ArrayList<>();
                }
                deep.add(Type.parenthesis);
            }
            else if (")".equals(token.toString())) {
                if (deep.isEmpty()) {
                    System.err.println(token);
                }
                else if (Type.parenthesis != deep.getLast()) {
                    System.err.println(token);
                }
                else {
                    deep.removeLast();
                }
            }
            // {}
            else if ("{".equals(token.toString())) {
                if (deep.isEmpty()) {
                    type = Type.curlingBracket;
                    startToken = token;
                    sectionTokens = new ArrayList<>();
                }
                deep.add(Type.curlingBracket);
            }
            else if ("}".equals(token.toString())) {
                if (deep.isEmpty()) {
                    System.err.println(token);
                }
                else if (Type.curlingBracket != deep.getLast()) {
                    System.err.println(token);
                }
                else {
                    deep.removeLast();
                }
            }
            // []
            else if ("[".equals(token.toString())) {
                if (deep.isEmpty()) {
                    type = Type.bracket;
                    startToken = token;
                    sectionTokens = new ArrayList<>();
                }
                deep.add(Type.bracket);
            }
            else if ("]".equals(token.toString())) {
                if (deep.isEmpty()) {
                    System.err.println(token);
                }
                else if (Type.bracket != deep.getLast()) {
                    System.err.println(token);
                }
                else {
                    deep.removeLast();
                }
            }
            if (type != null) {
                sectionTokens.add(token);
            }
            if (type != null && deep.isEmpty()) {
                sectionTokens.removeFirst();
                sectionTokens.removeLast();
                result.add(new Section(type, 
                                       sectionTokens, 
                                       new Position(startToken, token), 
                                       split(sectionTokens)));
                type = null;
                sectionTokens = null;
                startToken = null;
            }
        }
        if (!deep.isEmpty()) {
            sectionTokens.clear();
        }
        return result;
    }
    
}
