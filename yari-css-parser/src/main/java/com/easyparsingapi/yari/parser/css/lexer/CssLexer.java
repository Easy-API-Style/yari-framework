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
package com.easyparsingapi.yari.parser.css.lexer;

import static com.easyparsingapi.yari.parsec.pattern.Pattern.MISMATCH;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.pattern.CharPredicate;
import com.easyparsingapi.yari.parsec.pattern.Pattern;
import com.easyparsingapi.yari.parsec.pattern.Patterns;
import com.easyparsingapi.yari.parser.css.parser.AtomicPattern;

/**
 * Lexer for CSS source text, responsible for tokenising a raw CSS string or token
 * into a list of typed {@link Token} instances (words, operators, strings, comments, etc.).
 */
public class CssLexer {

    /**
     * Creates a new {@code CssLexer} instance with an empty look-back token buffer.
     */
    public CssLexer() {
        super();
    }

//    private static final Logger LOGGER = LoggerFactory.getLogger(CssLexer.class);

    private final List<String> previousTokens = new ArrayList<>(2);

    private static final List<String> OPERATORS = List.of(
        "~=", "|=", "^=", "$=", "*=", "<=", ">=", "::",

        ":", ";", ",", ".",
        "#", "@", ">", "<",
        "{", "}", "(", ")", "[", "]",
        "^", "+",  "*", "/", "-",

        "&", "!",

        "|", "$", "=", "~"
    );

    /**
     * Set of single-character operator characters derived from {@code OPERATORS},
     * excluding the {@code '-'} character which has dual use as a word constituent
     * (e.g. in property names and negative numeric values).
     */
    private static Set<Character> characterKeywords =
        OPERATORS.stream()
                 .filter(v -> v.length() == 1)
                 .map(v -> v.charAt(0))
                 .filter(v -> v != '-')
                 .collect(Collectors.toSet());

    /*
     *
     * PATTERN
     *
     */

    /**
     * Predicate that matches characters eligible to appear inside a CSS word token.
     * A character qualifies when it is not whitespace, not a single-character operator,
     * not a double-quote, not a single-quote, and not a {@code '+'}.
     */
    private static final CharPredicate IS_ALPHA_NUMERIC =
        c -> !Character.isWhitespace(c)
                && !characterKeywords.contains(c)
                && c != '"'
                && c != '\''
                && c != '+';

    /**
     * Predicate that matches horizontal whitespace characters, i.e. whitespace that is
     * neither a carriage-return ({@code \r}) nor a line-feed ({@code \n}).
     */
    private static final CharPredicate IS_WHITE_SPACE =
        c -> c != '\r'
              && c != '\n'
              && Character.isWhitespace(c);

    private boolean isUrlMode() {
        final int previousTokenLength = previousTokens.size();
        return previousTokenLength >= 2
                   && "(".equals(previousTokens.get(previousTokenLength - 1))
                   && "url".equals(previousTokens.get(previousTokenLength - 2));
    }

    private boolean isPropertyMode(final CharSequence src,
                                   final int begin) {
        final char first = src.charAt(begin);
        final int nextIndex = begin + 1;
        return Character.isDigit(first)
                  || (nextIndex < src.length()
                         && first == '.'
                         && Character.isDigit(src.charAt(nextIndex)))
                  || (nextIndex < src.length()
                          && first == '-'
                          && src.charAt(nextIndex) == '.')
                  || (nextIndex < src.length()
                          && first == '-'
                          && Character.isDigit(src.charAt(nextIndex)));
    }

    private boolean isUnitcodeMode(final CharSequence src,
                                   final int begin) {
        final char first = src.charAt(begin);
        final int nextIndex = begin + 1;
        return first == 'U'
                && nextIndex < src.length()
                && src.charAt(nextIndex) == '+';
    }

    /**
     * Low-level {@link Pattern} that recognises a CSS word token.
     * <p>
     * The matching strategy adapts to the current lexer state:
     * <ul>
     *   <li><b>URL mode</b> – active immediately after {@code url(}; consumes everything
     *       up to (but not including) the closing {@code ')'} character.</li>
     *   <li><b>Unicode range mode</b> – active when the input starts with {@code U+};
     *       consumes the {@code U+} prefix followed by any combination of hexadecimal
     *       digits, {@code '?'} wildcards, and {@code '-'} range separators.</li>
     *   <li><b>Property/numeric mode</b> – active when the word begins with a digit,
     *       {@code '.'}, or a {@code '-'} followed by a digit or dot; the {@code '.'}
     *       character is treated as a word constituent to support decimal literals.</li>
     *   <li><b>Default identifier mode</b> – consumes characters that satisfy
     *       {@link #IS_ALPHA_NUMERIC}, honouring CSS escape sequences ({@code '\'}).</li>
     * </ul>
     * Returns {@code MISMATCH} when no valid word can be recognised at the current position.
     */
    final Pattern wordPattern = Pattern.rule(context -> {
        final CharSequence src = context.src();
        final int begin = context.begin();
        final int end = context.end();

        if (begin >= end) {
            return MISMATCH;
        }

        if (isUrlMode()) {
            int length = 0;
            for (int i = begin; i < src.length(); i++) {
                final char c = src.charAt(i);
                /** The field. */
                length++;
                if (c == ')') {
//                    System.out.println(src.subSequence(begin, begin + length) + "|");
                    if (length > 1) {
                        /** The field. */
                        length--;
                    }
                    else {
                        return MISMATCH;
                    }
                    return length;
                }
            }
            // if ')' not found
            return MISMATCH;
        }
        else if (isUnitcodeMode(src, begin)) {
            int length = 2;
            for (int i = begin + 2; i < src.length(); i++) {
                /** The field. */
                length++;
                final char c = src.charAt(i);
                if (c == '?'
                        || c == '-'
                        || AtomicPattern.IS_HEXADECIMAL.isChar(c)) {
                    /** The continue. */
                    continue;
                }
                else {
//                    System.out.println(src.subSequence(begin, begin + length) + "|");
                    /** The field. */
                    length--;
                    return length;
                }
            }
            // if EOF
            return length;
        }
        else {
            final boolean propertyMode = isPropertyMode(src, begin);

            char first = src.charAt(begin);
            if (propertyMode && first == '.') {
                // do nothing
            }
            else if (!IS_ALPHA_NUMERIC.isChar(first)) {
                return MISMATCH;
            }

            int length = 0;
            for (int i = begin; i < src.length(); i++) {
                /** The field. */
                length++;
                final char c = src.charAt(i);
                if (propertyMode && c == '.') {
                    /** The continue. */
                    continue;
                }
                else if (c == '\\') {
                    /** The field. */
                    length++;
                    /** The field. */
                    i++;
                }
                else if (!IS_ALPHA_NUMERIC.isChar(c)) {
                    /** The field. */
                    length--;
                    if (length == 1 && "-".equals(src.subSequence(begin, begin + 1))) {
                        return MISMATCH;
                    }
//                    System.out.println(src.subSequence(begin, begin + length) + "|");
                    return length;
                }
            }
            // if EOF
            return length;
        }
    });

    private final Parser<String> word = wordPattern.toScanner(CssTag.WORD.name())
                                                   .source();

    /**
     * Parser that matches any CSS operator token (e.g. {@code ~=}, {@code ::}, {@code ;}, {@code +}).
     * The operators are tried in declaration order, longest-match first.
     */
    public static final Parser<String> OPERATOR =
        Patterns.or(OPERATORS.stream()
                             .map(Patterns::string)
                             .toList()
                             .toArray(Pattern[]::new))
                .toScanner(CssTag.OPERATOR.name())
                .source();

    /**
     * Parser that matches a line terminator, accepting both {@code \r\n} (CRLF) and {@code \n} (LF).
     */
    public static final Parser<String> RETURN_CARRIAGE =
        Patterns.or(Patterns.sequence(Patterns.isChar('\r'),
                                      Patterns.isChar('\n')),
                    Patterns.isChar('\n'))
                .toScanner(CssTag.RETURN_CARRIAGE.name())
                .source();

    private static final Parser<String> DOUBLE_QUOTE_STRING =
        Patterns.sequence(Patterns.isChar('"').until('"'),
                          Patterns.isChar('"'))
                .toScanner(CssTag.DOUBLE_QUOTE_STRING.name())
                .source();

    private static final Parser<String> SINGLE_QUOTE_STRING =
        Patterns.sequence(Patterns.isChar('\'').until('\''),
                          Patterns.isChar('\''))
                .toScanner(CssTag.SINGLE_QUOTE_STRING.name())
                .source();

    private static final Parser<String> BLOCK_COMMENT =
        Patterns.sequence(Patterns.string("/*").until("*/"),
                          Patterns.string("*/"))
                .toScanner(CssTag.BLOCK_COMMENT.name())
                .source();

    private final Parser<Fragment> cssTokenizer() {
        return Parsers.or(RETURN_CARRIAGE.map(hit -> Tokens.fragment(hit, CssTag.RETURN_CARRIAGE)),
                          SINGLE_QUOTE_STRING.map(hit -> Tokens.fragment(hit, CssTag.SINGLE_QUOTE_STRING)),
                          DOUBLE_QUOTE_STRING.map(hit -> Tokens.fragment(hit, CssTag.DOUBLE_QUOTE_STRING)),
                          BLOCK_COMMENT.map(hit -> Tokens.fragment(hit, CssTag.BLOCK_COMMENT)),
                          word.map(hit -> Tokens.fragment(hit, CssTag.WORD)),
                          OPERATOR.map(hit -> Tokens.fragment(hit, CssTag.OPERATOR)))
                        .result(t -> {
                            if (!CssTag.RETURN_CARRIAGE.equals(t.tag())
                                    && !CssTag.BLOCK_COMMENT.equals(t.tag())) {
                                if (previousTokens.size() == 2) {
                                    previousTokens.removeFirst();
                                }
                                previousTokens.add(t.text());
                            }
                        });
    }

    private static final Parser<String> WHITE_SPACE = Patterns.many1(IS_WHITE_SPACE).toScanner("whiteSpace").source();

    /*
     *
     * LEXER
     *
     */

    /**
     * Returns a parser that skips any combination of line terminators, horizontal
     * whitespace, and {@code /* ... *}{@code /} block comments.
     * <p>
     * Intended for use as the token delimiter in contexts where blank lines and
     * multi-line comments should be silently consumed between tokens.
     *
     * @return a {@code Parser<Void>} that consumes zero or more comment/whitespace sequences
     */
    static Parser<Void> delimitedComment() {
        return Parsers.or(RETURN_CARRIAGE, WHITE_SPACE, BLOCK_COMMENT).skipMany();
    }

    /**
     * Returns a parser that skips zero or more horizontal whitespace characters
     * (spaces and tabs), without consuming line terminators or comments.
     *
     * @return a {@code Parser<Void>} that consumes zero or more horizontal whitespace characters
     */
    static Parser<Void> delimitedWhiteSpace() {
        return WHITE_SPACE.skipMany();
    }

    /**
     * Builds and returns a stateful CSS lexer parser that produces a list of {@link Token} instances
     * from a CSS source, skipping horizontal whitespace between tokens.
     *
     * @return a parser that tokenises a full CSS source into a {@code List<Token>}
     */
    public static Parser<List<Token>> lexer() {
        final CssLexer cssLexer = new CssLexer();
        return ApiParser.lexer(cssLexer.cssTokenizer(), delimitedWhiteSpace());
    }

    /**
     * Tokenises the given CSS source string into a list of {@link Token} instances.
     *
     * @param css the raw CSS source text to lex
     * @return an ordered list of tokens produced by the CSS lexer
     */
    public static List<Token> lex(final String css) {
        return ApiParser.lex(lexer(), css);
    }

    /**
     * Tokenises the text content of the given {@link Token} as CSS, returning the resulting token list.
     *
     * @param css a token whose text content is treated as a CSS source to re-lex
     * @return an ordered list of tokens produced by the CSS lexer
     */
    public static List<Token> lex(final Token css) {
        return ApiParser.lex(lexer(), css);
    }

}
