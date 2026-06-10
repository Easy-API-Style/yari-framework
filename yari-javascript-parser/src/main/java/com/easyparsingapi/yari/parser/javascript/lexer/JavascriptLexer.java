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
import static com.easyparsingapi.yari.parsec.pattern.Patterns.isChar;
import static com.easyparsingapi.yari.parsec.pattern.Patterns.lineComment;
import static com.easyparsingapi.yari.parsec.pattern.Patterns.many1;
import static com.easyparsingapi.yari.parsec.pattern.Patterns.sequence;
import static com.easyparsingapi.yari.parsec.pattern.Patterns.string;
import static com.easyparsingapi.yari.parser.javascript.lexer.LiteralTemplateLexer.LITERAL_TEMPLATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Terminals;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.Tokens.Tag;
import com.easyparsingapi.yari.parsec.pattern.CharPredicate;
import com.easyparsingapi.yari.parsec.pattern.CharPredicates;
import com.easyparsingapi.yari.parsec.pattern.Pattern;
import com.easyparsingapi.yari.parsec.pattern.Patterns;

/**
 * Static utility class that tokenises JavaScript source code into a flat list of
 * {@link com.easyparsingapi.yari.parsec.Token} instances.
 * The lexer handles all JavaScript literal types (strings, numbers, regular expressions,
 * template literals), operators, keywords, identifiers, comments, and line endings.
 */
public class JavascriptLexer {

    /** Not instantiable — all methods are static. */
    private JavascriptLexer() {}


//    private static final Logger LOGGER = LoggerFactory.getLogger(JavascriptLexer.class);
    
    private static final Set<String> OPERATORS = Set.of(
            ">>>=",
            "&gt;&gt;&gt;=",
            "&#62;&#62;&#62;=",
            "<<=",
            "&lt;&lt;=",
            "&#60&#60&#60;=",
            ">>=",
            "&gt;&gt=",
            "&#62;&#62;=",
            "&&=",
            "&amp;&amp;=",
            "&#38;&#38;=",
            "||=",
            "??=",
            "**=",
            "+=",
            "-=",
            "*=",
            "/=",
            "%=",
            "&=",
            "&amp;=",
            "&#38;=",
            "^=",
            "|=",
            
            ">>>",
            "&gt;&gt;&gt;",
            "&#62;&#62;&#62;",
            "<<<",
            "&lt;&lt;&lt;",
            "&#60;&#60;&#60;",
            ">>",
            "&gt;&gt;",
            "&#62;&#62;",
            "<<",
            "&lt;&lt;",
            "&#60;&#60;",
            "<=",
            "&lt;=",
            "&#60;=",
            ">=",
            "&gt;=",
            "&#62;=",
            "!==",
            "!=",
            "===",
            "==",
            "??",
            "||",
            "&&",
            "&amp;&amp;",
            "&#38;&#38;",
            
            "=>",
            
            "&lt;", "&#60;",
            "&gt;", "&#62;",
            "&amp;", "&#38;",
            
            "**",
            
            "%", "/", "*", "+", "-", "=",
            ":", ",", ";", ".", "~",
            ">", "<", "&", "|", "^", 
            "?", "!",
            "(", ")", "[", "]", "{", "}",
            "#"
    );
    
    /** Set of all JavaScript reserved keywords. */
    public static final Set<String> KEYWORDS = Set.of(
            "return", "function", 
            "await", "async",
            "const", "let", "var",
            "default", "import", "from", "export", "as",
            "switch", "case", 
            "if", "else",
            "true", "false",
            "for", "in", "of", "while", "do", "continue", "break",
            "typeof", "instanceof", 
            "class", "extends", "implements", "interface", "new", 
            "static", "delete",
            "try", "catch", "finally", "throw",
            "null", "this", "undefined", "NaN",
            "super", "constructor", 
            "yield", 
            "debugger",
            "void"
    );
    
    private static final Set<String> KEYWORDS_WHICH_START_STATEMENT = Set.of(
            "return", "function", 
            "await", "async",
            "const", "let", "var",
            "import","export", 
            "switch",
            "if",
            "for", "while", "do",
            "class", "new", 
            "delete",
            "try", "throw",
            "debugger"
    );
    
    /** Set of single-character keyword tokens. */
    public static Set<Character> characterKeywords =
        OPERATORS.stream()
                 .filter(v -> v.length() == 1)
                 .map(v -> v.charAt(0))
                 .collect(Collectors.toSet());
    
    /**
     * Returns the complete set of JavaScript reserved keywords recognised by the lexer.
     *
     * @return an unmodifiable set of keyword strings
     */
    public static Set<String> keywords() {
        return KEYWORDS;
    }

    /**
     * Returns the subset of keywords that can begin a statement.
     * This is used by the parser to determine whether an automatic semicolon should be inserted.
     *
     * @return an unmodifiable set of statement-starting keyword strings
     */
    public static Set<String> startingStatementKeywords() {
        return KEYWORDS_WHICH_START_STATEMENT;
    }
    
    /*
     * 
     * CUSTOM PATTERN
     * 
     */
    /** Set of characters that may precede a regular expression. */
    public static Set<Character> beforeRegularExpression =
            Set.of('\r', '\n',
                   '%', '/', '*', '+', '-', '=',
                   ':', ',', ';', '~',
                   '>', '<', '&', '|', '^', 
                   '?', '!',
                   '(', '[', '{');
    
    static final Pattern REGULAR_EXPRESSION_PATTERN = Pattern.rule(context -> {
        final CharSequence src = context.src();
        final int begin = context.begin();
        final int end = context.end();
        
        if (begin >= end) {
            return MISMATCH;
        }
        
        int nextIndex = begin + 1;
        if (src.charAt(begin) == '/'
                && nextIndex < src.length()
                && src.charAt(nextIndex) != '/') {
            
            int length = 1;
            boolean insideBracket = false;
            int parenthesisDeep = 0;
            boolean flags = false;
            
            for (int i = nextIndex; i < src.length(); i++) {
                /** Field. */
                length++;
                if (flags) {
                    if (!CharPredicates.among("dgimsuvy").isChar(src.charAt(i))) {
                        /** Field. */
                        length--;
                        /** The break. */
                        break;
                    }
                }
                else if (src.charAt(i) == '\\') {
                    /** Field. */
                    length++;
                    /** Field. */
                    i++;
                } 
                else if (!insideBracket && src.charAt(i) == '/') {
                    int n = i + 1;
                    if (n < src.length()
                          && (src.charAt(n) == '/' || src.charAt(n) == '=')) {
                        return MISMATCH;
                    }
                    flags = true;
                }
                else if (src.charAt(i) == '[') {
                    insideBracket = true;
                }
                else if (src.charAt(i) == ']') {
                    insideBracket = false;
                }
                else if (!insideBracket && src.charAt(i) == '(') {
                    /** Field. */
                    parenthesisDeep++;
                }
                else if (!insideBracket && src.charAt(i) == ')') {
                    /** Field. */
                    parenthesisDeep--;
                }
            }
            if (parenthesisDeep != 0) {
                return MISMATCH;
            }
            else if (insideBracket) {
                return MISMATCH;
            }
            for (int i = begin - 1; i >= 0; i--) {
                if (src.charAt(i) == ' ') {
                    /** The continue. */
                    continue;
                }
                else if (i - 1 >= 0 && Patterns.string("=>").match(src, i - 1, end) > 0) {
                    return length;
                }
                else if (i - 5 >= 0 && Patterns.string("return").match(src, i - 5, end) > 0) {
                    return length;
                }
                else if (beforeRegularExpression.contains(src.charAt(i))) {
                    return length;
                }
                else {
                    return MISMATCH;
                }
            }
            if (flags) {
                return length;
            }
        }
        // not regex
        return MISMATCH;
    });
    
    /*
     * 
     * PATTERN
     * 
     */
    private static final boolean checkUnderScore(final String value) {
        if (value.contains("__")
                || value.contains("_.")
                || value.contains("._")
                || value.startsWith("_")
                || value.endsWith("_")
                || "_".equals(value)) {
            return false;
        }
        if (value.startsWith("0")) {
            for (char c : value.toCharArray()) {
                if (c == '0') {
                    /** The continue. */
                    continue;
                }
                else if (c == '_') {
                    return false;
                }
                else {
                    /** The break. */
                    break;
                }
            }
        }
        return true;
    }
    
    private static final CharPredicate IS_BINARY = c -> c == '0' || c == '1' || c == '_';
    
    private static final CharPredicate IS_HEXADECIMAL = c -> Character.isDigit(c) 
                                                               || c == 'A' || c == 'a'
                                                               || c == 'B' || c == 'b'
                                                               || c == 'C' || c == 'c'
                                                               || c == 'D' || c == 'd'
                                                               || c == 'E' || c == 'e'
                                                               || c == 'F' || c == 'f'
                                                               || c == '_';
    
    private static final CharPredicate IS_ALPHA_NUMERIC = c -> !Character.isWhitespace(c) 
                                                                  && c != '\"' 
                                                                  && c != '\'' 
                                                                  && c != '`'
                                                                  && !characterKeywords.contains(c);
    
    private static final CharPredicate IS_DIGIT = c -> Character.isDigit(c) || c == '_';
    
    private static final CharPredicate IS_WHITE_SPACE = c -> c != '\r'
                                                                && c != '\n' 
                                                                && Character.isWhitespace(c);
    
    /** Parser matching a carriage return or CRLF sequence. */
    public static final Parser<String> RETURN_CARRIAGE = Patterns.or(Patterns.sequence(Patterns.isChar('\r'), Patterns.isChar('\n')),
                                                                     Patterns.isChar('\n'))
                                                                 .toScanner(JavascriptTag.RETURN_CARRIAGE.name()).source();
    
    private static final Parser<String> DOUBLE_QUOTE_STRING = 
            sequence(isChar('"').until('"'), 
                     isChar('"'))
                .toScanner(JavascriptTag.DOUBLE_QUOTE_STRING.name())
                .source(); 
    
    private static final Parser<String> DOUBLE_QUOTE_STRING_ENTITY_NAME = 
            sequence(string("&quot;").until("&quot;"), 
                     string("&quot;"))
                .toScanner(JavascriptTag.DOUBLE_QUOTE_STRING.name())
                .source(); 
    
    private static final Parser<String> DOUBLE_QUOTE_STRING_NUMBER_CODE = 
            sequence(string("&#34;").until("&#34;"), 
                     string("&#34;"))
                .toScanner(JavascriptTag.DOUBLE_QUOTE_STRING.name())
                .source(); 
    
    private static final Parser<String> SINGLE_QUOTE_STRING = 
            sequence(isChar('\'').until('\''), 
                     isChar('\''))
               .toScanner(JavascriptTag.SINGLE_QUOTE_STRING.name())
               .source(); 
    
    private static final Parser<String> SINGLE_QUOTE_STRING_ENTITY_NAME = 
            sequence(string("&apos;").until("&apos;"), 
                     string("&apos;"))
               .toScanner(JavascriptTag.SINGLE_QUOTE_STRING.name())
               .source(); 
    
    private static final Parser<String> SINGLE_QUOTE_STRING_NUMBER_CODE = 
            sequence(string("&#39;").until("&#39;"), 
                     string("&#39;"))
               .toScanner(JavascriptTag.SINGLE_QUOTE_STRING.name())
               .source(); 
    
    private static final Parser<String> REGULAR_EXPRESSION = 
            REGULAR_EXPRESSION_PATTERN.toScanner(JavascriptTag.REGULAR_EXPRESSION.name())
                              .source(); 
    
    private static final Parser<String> INTEGER =
            many1(IS_DIGIT).toScanner(JavascriptTag.INTEGER.name())
                           .source()
                           .acceptIf(JavascriptLexer::checkUnderScore);
    
    private static final Parser<String> HEXADECIMAL =
            sequence(isChar('0'), 
                     isChar('x').or(isChar('X')),
                     many1(IS_HEXADECIMAL))
              .toScanner(JavascriptTag.HEXADECIMAL.name())
              .source()
              .acceptIf(JavascriptLexer::checkUnderScore);
    
    private static final Parser<String> BINARY =
            sequence(isChar('0'), 
                     isChar('b').or(isChar('B')),
                     many1(IS_BINARY))
              .toScanner(JavascriptTag.BINARY.name())
              .source()
              .acceptIf(JavascriptLexer::checkUnderScore);
    
    private static final Parser<String> BASE_10 = 
            sequence(isChar('0').many1(), 
                     many1(IS_DIGIT))
              .toScanner(JavascriptTag.BASE_10.name())
              .source()
              .acceptIf(JavascriptLexer::checkUnderScore);
    
   private static final Parser<String> OCTAL =
           sequence(isChar('0'), 
                    isChar('o').or(isChar('O')),
                    many1(IS_DIGIT))
              .toScanner(JavascriptTag.OCTAL.name())
              .source()
              .acceptIf(JavascriptLexer::checkUnderScore);
   
    private static final Parser<String> PREFIX_EXPONENTIAL = 
            sequence(isChar('e').or(isChar('E')),
                     many1(IS_DIGIT))
              .toScanner(JavascriptTag.EXPONENTIAL.name())
              .source()
              .acceptIf(JavascriptLexer::checkUnderScore);
    
    private static final Parser<String> DECIMAL_STRICT = 
            sequence(many1(IS_DIGIT),
                     isChar('.'),
                     many1(IS_DIGIT))
               .toScanner("DECIMAL_STRICT")
               .source();
    
    private static final Parser<String> DECIMAL_EMPTY_RIGHT =
            sequence(many1(IS_DIGIT),
                    isChar('.'))
               .toScanner("DECIMAL_EMPTY_RIGHT")
               .source();
    
    private static final Parser<String> DECIMAL_EMPTY_LEFT =
            sequence(isChar('.'),
                     many1(IS_DIGIT))
               .toScanner("DECIMAL_EMPTY_LEFT")
               .source();
    
    private static final Parser<String> DECIMAL = 
            Parsers.or(DECIMAL_STRICT, 
                       DECIMAL_EMPTY_RIGHT,
                       DECIMAL_EMPTY_LEFT)
                   .acceptIf(JavascriptLexer::checkUnderScore);
    
    private static final Parser<String> EXPONENTIAL = 
            Parsers.or(Parsers.sequence(DECIMAL, PREFIX_EXPONENTIAL,
                                        (decimal, exponential) -> decimal + exponential),
                       Parsers.sequence(INTEGER, PREFIX_EXPONENTIAL,
                                        (integer, exponential) -> integer + exponential));
  
    private static final Parser<String> LINE_COMMENT =
            lineComment("//").toScanner(JavascriptTag.LINE_COMMENT.name())
                             .source();
    
    private static final Parser<String> BLOCK_COMMENT = 
            sequence(string("/*").until("*/"), 
                     string("*/"))
              .toScanner(JavascriptTag.BLOCK_COMMENT.name())
              .source();
    
    private static final Parser<String> WORD = 
            isChar(IS_ALPHA_NUMERIC).many1()
                                    .notContain(OPERATORS)
                                    .toScanner(JavascriptTag.WORD.name())
                                    .source();
    
    private static final Parser<Fragment> JAVASCRIPT_TOKENIZER = 
        Parsers.or(RETURN_CARRIAGE.map(hit -> Tokens.fragment(hit, JavascriptTag.RETURN_CARRIAGE)),
                   LINE_COMMENT.map(hit -> Tokens.fragment(hit, JavascriptTag.LINE_COMMENT)), 
                   BLOCK_COMMENT.map(hit -> Tokens.fragment(hit, JavascriptTag.BLOCK_COMMENT)),
                   BINARY.map(hit -> Tokens.fragment(hit, JavascriptTag.BINARY)),
                   EXPONENTIAL.map(hit -> Tokens.fragment(hit, JavascriptTag.EXPONENTIAL)),
                   OCTAL.map(hit -> Tokens.fragment(hit, JavascriptTag.OCTAL)),
                   HEXADECIMAL.map(hit -> Tokens.fragment(hit, JavascriptTag.HEXADECIMAL)),
                   DECIMAL.map(hit -> Tokens.fragment(hit, JavascriptTag.DECIMAL)),
                   BASE_10.map(hit -> Tokens.fragment(hit, JavascriptTag.BASE_10)),
                   INTEGER.map(hit -> Tokens.fragment(hit, JavascriptTag.INTEGER)),
                   SINGLE_QUOTE_STRING.map(hit -> Tokens.fragment(hit, JavascriptTag.SINGLE_QUOTE_STRING)),
                   SINGLE_QUOTE_STRING_ENTITY_NAME.map(hit -> Tokens.fragment(hit, JavascriptTag.SINGLE_QUOTE_STRING_ENTITY_NAME)),
                   SINGLE_QUOTE_STRING_NUMBER_CODE.map(hit -> Tokens.fragment(hit, JavascriptTag.SINGLE_QUOTE_STRING_NUMBER_CODE)),
                   DOUBLE_QUOTE_STRING.map(hit -> Tokens.fragment(hit, JavascriptTag.DOUBLE_QUOTE_STRING)),
                   DOUBLE_QUOTE_STRING_ENTITY_NAME.map(hit -> Tokens.fragment(hit, JavascriptTag.DOUBLE_QUOTE_STRING_ENTITY_NAME)),
                   DOUBLE_QUOTE_STRING_NUMBER_CODE.map(hit -> Tokens.fragment(hit, JavascriptTag.DOUBLE_QUOTE_STRING_NUMBER_CODE)),
                   LITERAL_TEMPLATE.map(hit -> Tokens.fragment(hit, JavascriptTag.LITERAL_TEMPLATE)),
                   REGULAR_EXPRESSION.map(hit -> Tokens.fragment(hit, JavascriptTag.REGULAR_EXPRESSION)),
                   Terminals.operators(OPERATORS)
                            .words(WORD)
                            .keywords(KEYWORDS)
                            .build()
                            .tokenizer()
                            .map(v -> {
                                Object result = v;
                                if (v instanceof Fragment fragment) {
                                    if (fragment.tag() == Tag.RESERVED) {
                                        result = Tokens.fragment(fragment.text(), JavascriptTag.KEYWORD);
                                    }
                                    else if (fragment.tag() == Tag.IDENTIFIER) {
                                        result = Tokens.fragment(fragment.text(), JavascriptTag.WORD);
                                    }
                                }
                                return result;
                            })
                            .cast());
    
    private static final Parser<Void> WHITE_SPACE = many1(IS_WHITE_SPACE).toScanner("whiteSpace");
    
    /*
     * 
     * LEXER
     * 
     */
    private static Parser<Void> delimitedWhiteSpace() {
        return WHITE_SPACE.skipMany();
    }
    
    private static Parser<Fragment> tokenizer() {
        return JAVASCRIPT_TOKENIZER;
    }

    /**
     * Returns a parser that tokenises a complete JavaScript source string into a list of tokens.
     * Template-literal tokens are expanded recursively by a nested lexer pass.
     *
     * @return a {@link Parser} that produces a {@link List} of {@link Token} instances
     */
    public static Parser<List<Token>> lexer() {
        return ApiParser.lexer(tokenizer(), delimitedWhiteSpace())
                        .map(JavascriptLexer::lexRecursively);
    }

    /**
     * Tokenises the given JavaScript source string into a flat list of tokens.
     * Template-literal tokens are expanded recursively.
     *
     * @param javascript the raw JavaScript source code to tokenise
     * @return the ordered list of {@link Token} instances produced by the lexer
     */
    public static List<Token> lex(final String javascript) {
        final List<Token> tokens = ApiParser.lex(JAVASCRIPT_TOKENIZER, delimitedWhiteSpace(), javascript);
        return lexRecursively(tokens);
    }

    /**
     * Re-tokenises the text of an existing token (e.g. a template-literal sub-token)
     * and returns the resulting flat list of tokens.
     *
     * @param javascript the token whose text should be re-lexed
     * @return the ordered list of {@link Token} instances produced by the lexer
     */
    public static List<Token> lex(final Token javascript) {
        final List<Token> tokens = ApiParser.lex(JAVASCRIPT_TOKENIZER, delimitedWhiteSpace(), javascript);
        return lexRecursively(tokens);
    }

    private static List<Token> lexRecursively(final List<Token> tokens) {
        final List<Token> result = new ArrayList<>();
        for (final Token token : tokens) {
            if (JavascriptTag.LITERAL_TEMPLATE.equals(Token.tag(token))) {
                result.addAll(LiteralTemplateLexer.lex(token));
            }
            else {
                result.add(token);
            }
        }
        return result;
    }
    
}
