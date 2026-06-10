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
package com.easyparsingapi.yari.parser.css.parser;

import java.util.List;
import java.util.Set;

import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.core.util.TokenUtil;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parser.css.lexer.CssTag;

/**
 * Provides elementary parsers for CSS fragments and tokens,
 * used as basic building blocks in CSS syntactic analysis.
 */
public class TermParser {

    /** Not instantiable — all methods are static. */
    private TermParser() {}


    /*
     *
     * FRAGMENT
     *
     */

    /**
     * Builds a parser that matches any CSS fragment token, regardless of its tag.
     *
     * @return a parser that accepts any {@link Token} whose value is a {@link Fragment}
     */
    static Parser<Token> anyFragment() {
        return fragment(CssTag.values());
    }

    /**
     * Builds a parser that matches a CSS fragment token whose tag is one of the
     * specified {@link CssTag} values.
     *
     * @param cssTags the accepted CSS tags
     * @return a parser that accepts a {@link Token} with a matching fragment tag
     */
    static Parser<Token> fragment(final CssTag... cssTags) {
        return fragment(CollectionUtil.toSet(cssTags));
    }

    /**
     * Builds a parser that matches a CSS fragment token whose tag is <em>not</em> in
     * the specified set, i.e. any fragment except those tagged with the given values.
     *
     * @param cssTags the CSS tags to exclude
     * @return a parser that accepts a {@link Token} whose fragment tag is not in the exclusion set
     */
    static Parser<Token> notFragment(final CssTag... cssTags) {
        final Set<CssTag> notTags = CollectionUtil.toSet(cssTags);
        final Set<CssTag> tags = CollectionUtil.toSet(CssTag.values());
        tags.removeAll(notTags);
        return fragment(tags);
    }

    /**
     * Builds a parser that matches a CSS fragment token whose tag is contained in
     * the provided set.
     *
     * @param cssTags the set of accepted CSS tags; must not be empty
     * @return a parser that accepts a {@link Token} with a fragment tag in {@code cssTags},
     *         labelled {@code "fragment[<tags>]"}
     */
    static Parser<Token> fragment(final Set<CssTag> cssTags) {
        return Parsers.token(t -> {
            Token result = null;
            if (!CollectionUtil.isEmpty(cssTags)) {
                final Object value = t.value();
                if (value instanceof Fragment) {
                    final Fragment fragment = (Fragment) value;
                    if (cssTags.contains(fragment.tag())) {
                        result = t;
                    }
                }
            }
            return result;
        })
        .label("fragment" + fragmentLabel(cssTags));
    }

    /**
     * Builds a label string listing all tag names for the given set of CSS tags.
     *
     * @param cssTags the set of CSS tags to include in the label
     * @return a bracketed, comma-separated list of tag names, e.g. {@code "[WORD, NUMBER]"}
     */
    private static String fragmentLabel(final Set<CssTag> cssTags) {
        final StringBuilder result = new StringBuilder();
        result.append("[");
        result.append(String.join(", ", CssTag.toString(cssTags)));
        result.append("]");
        return result.toString();
    }

    /**
     * Builds a parser that matches a {@link CssTag#WORD} fragment whose text is one of
     * the specified words.
     *
     * @param words the expected word values
     * @return a parser that produces the matched word as a {@link String},
     *         labelled {@code "word"}
     */
    static Parser<String> word(final String... words) {
        return token(Set.of(words), CssTag.WORD, true)
                .map(Token::toString)
                .label("word");
    }

    /*
     *
     * TOKEN
     *
     */

    /**
     * Builds a parser that matches a sequence of operator tokens whose combined text
     * equals the concatenation of the given terms (a "phrase").
     *
     * @param terms the individual operator strings that compose the phrase
     * @return a parser that produces the concatenated phrase as a {@link String},
     *         labelled {@code "phrase[<terms>]"}
     */
    static Parser<String> phrase(final String... terms) {
        return phrase(CollectionUtil.toList(terms));
    }

    /**
     * Builds a parser that matches a sequence of operator tokens whose combined text
     * equals the concatenation of the given list of terms.
     *
     * @param terms the list of individual operator strings that compose the phrase
     * @return a parser that produces the concatenated phrase as a {@link String},
     *         labelled {@code "phrase[<terms>]"}
     */
    static Parser<String> phrase(final List<String> terms) {
        return Parsers.list(terms.stream()
                      .map(v -> token(CssTag.OPERATOR, true, v))
                      .toList())
                      .map(TokenUtil::toString)
                      .label("phrase[" + String.join(", ", terms) + "]");
    }

    /**
     * Builds a parser that matches an operator token whose text is one of the given terms.
     *
     * @param terms the accepted operator strings
     * @return a parser that produces the matched operator text as a {@link String}
     */
    static Parser<String> token(final String... terms) {
        return token(CollectionUtil.toSet(terms));
    }

    /**
     * Builds a parser that matches an operator token whose text is contained in the given set.
     *
     * @param terms the set of accepted operator strings
     * @return a parser that produces the matched operator text as a {@link String}
     */
    static Parser<String> token(final Set<String> terms) {
        return token(terms, CssTag.OPERATOR, true)
                .map(Token::toString);
    }

    /**
     * Builds a parser that matches an operator token whose text is <em>not</em> one of
     * the given terms.
     *
     * @param terms the operator strings to exclude
     * @return a parser that produces the matched operator text as a {@link String}
     */
    static Parser<String> notToken(final String... terms) {
        return notToken(CollectionUtil.toSet(terms));
    }

    /**
     * Builds a parser that matches an operator token whose text is <em>not</em> in the
     * given set.
     *
     * @param terms the set of operator strings to exclude
     * @return a parser that produces the matched operator text as a {@link String}
     */
    static Parser<String> notToken(final Set<String> terms) {
        return token(terms, CssTag.OPERATOR, false)
                .map(Token::toString);
    }

    /**
     * Builds a parser that matches a fragment token of the given CSS tag whose text is
     * one of the specified terms (convenience varargs overload).
     *
     * @param cssTag  the required CSS tag of the fragment
     * @param contain {@code true} to accept tokens whose text is in {@code terms};
     *                {@code false} to accept tokens whose text is <em>not</em> in {@code terms}
     * @param terms   the operator strings to match against
     * @return a parser that produces the matched {@link Token}
     */
    private final static Parser<Token> token(final CssTag cssTag,
                                             final boolean contain,
                                             final String... terms) {
        return token(CollectionUtil.toSet(terms), cssTag, contain);
    }

    /**
     * Builds a parser that matches a fragment token of the given CSS tag, accepting or
     * rejecting it depending on whether its text is in the provided set.
     *
     * @param terms   the set of operator strings to match against
     * @param cssTag  the required CSS tag of the fragment
     * @param contain {@code true} to accept tokens whose text is in {@code terms};
     *                {@code false} to accept tokens whose text is <em>not</em> in {@code terms}
     * @return a parser that produces the matched {@link Token},
     *         labelled {@code "token[not][<tag>][<terms>]"}
     */
    static Parser<Token> token(final Set<String> terms,
                               final CssTag cssTag,
                               final boolean contain) {
        return Parsers.token(token -> {
            Token result = null;
            final Object value = token.value();
            if (value instanceof Fragment fragment
                    && fragment.tag() == cssTag) {
                if (!CollectionUtil.isEmpty(terms)) {
                    if (contain == terms.contains(fragment.text())) {
                        result = token;
                    }
                }
            }
            return result;
        })
        .label("token" + tokenLabel(contain, cssTag, terms));
    }

    /**
     * Builds a label string for a token parser, indicating the containment mode,
     * CSS tag, and the set of operator strings.
     *
     * @param contain {@code true} for an inclusion filter, {@code false} for an exclusion filter
     * @param cssTag  the CSS tag used by the parser
     * @param terms   the set of operator strings used by the parser
     * @return a descriptive label string, e.g. {@code "[not][OPERATOR][+,-]"}
     */
    private static String tokenLabel(final boolean contain,
                                     final CssTag cssTag,
                                     final Set<String> terms) {
        final StringBuilder result = new StringBuilder();
        if (!contain) {
            result.append("[not]");
        }
        result.append("[");
        result.append(cssTag.name());
        result.append("]");
        result.append("[");
        result.append(String.join(",", terms));
        result.append("]");
        return result.toString();
    }

}
