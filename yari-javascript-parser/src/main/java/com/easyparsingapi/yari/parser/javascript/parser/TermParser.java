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

import java.util.List;
import java.util.Set;

import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.core.util.TokenUtil;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptTag;

/**
 * Package-private static utility class providing low-level token and fragment parsers
 * that form the building blocks for all higher-level JavaScript parsers.
 * Handles matching of fragments by tag, keyword tokens by text, and multi-token phrases.
 */
class TermParser {

    /** Not instantiable — all methods are static. */
    private TermParser() {}


    
    /*
     * 
     * FRAGMENT
     * 
     */
    final static Parser<Token> anyFragment() {
        return fragment(JavascriptTag.values());
    }
    
    final static Parser<Token> fragment(final JavascriptTag... javascriptTags) {
        return fragment(CollectionUtil.toSet(javascriptTags));
    }
    
    final static Parser<Token> notFragment(final JavascriptTag... javascriptTags) {
        final Set<JavascriptTag> notTags = CollectionUtil.toSet(javascriptTags);
        final Set<JavascriptTag> tags = CollectionUtil.toSet(JavascriptTag.values());
        tags.removeAll(notTags);
        return fragment(tags);
    }
    
    final static Parser<Token> fragment(final Set<JavascriptTag> javascriptTags) {
        return Parsers.token(t -> {
            Token result = null;
            if (!CollectionUtil.isEmpty(javascriptTags)) {
                final Object value = t.value();
                if (value instanceof Fragment fragment) {
                    if (javascriptTags.contains(fragment.tag())) {
                        result = t;
                    }
                }
            }
            return result;
        })
        .label("fragment" + fragmentLabel(javascriptTags));
    }

    private static String fragmentLabel(final Set<JavascriptTag> javascriptTags) {
        final StringBuilder result = new StringBuilder();
        result.append("[");
        result.append(String.join(", ", JavascriptTag.toString(javascriptTags)));
        result.append("]");
        return result.toString();
    }
    
    /*
     * 
     * TOKEN TEMPLATE
     * 
     */
    final static Parser<String> templateToken(final String... terms) {
        return token(CollectionUtil.toSet(terms), 
                     JavascriptTag.LITERAL_TEMPLATE_KEYWORD,
                     true)
                .map(Token::toString);
    }
    
    /*
     * 
     * TOKEN
     * 
     */
    final static Parser<String> phrase(final String... terms) {
        return phrase(CollectionUtil.toList(terms));
    }
    
    final static Parser<String> phrase(final List<String> terms) {
        return Parsers.list(terms.stream()
                      .map(v -> token(JavascriptTag.KEYWORD, true, v))
                      .toList())
                      .map(TokenUtil::toString)
                      .label("phrase[" + String.join(" ", terms) + "]");
    }
    
    final static Parser<String> token(final String... terms) {
        return token(CollectionUtil.toSet(terms));
    }
    
    final static Parser<String> token(final Set<String> terms) {
        return token(terms, JavascriptTag.KEYWORD, true)
                .map(Token::toString);
    }
    
    final static Parser<String> notToken(final String... terms) {
        return notToken(CollectionUtil.toSet(terms));
    }
    
    final static Parser<String> notToken(final Set<String> terms) {
        return token(terms, JavascriptTag.KEYWORD, false)
                .map(Token::toString);
    }
    
    private final static Parser<Token> token(final JavascriptTag javascriptTag,
                                             final boolean contain,
                                             final String... terms) {
        return token(CollectionUtil.toSet(terms), javascriptTag, contain);
    }
    
    private final static Parser<Token> token(final Set<String> terms,
                                             final JavascriptTag javascriptTag,
                                             final boolean contain) {
        return Parsers.token(token -> {
            Token result = null;
            final Object value = token.value();
            if (value instanceof Fragment fragment
                    && fragment.tag() == javascriptTag) {
                if (!CollectionUtil.isEmpty(terms)) {
                    if (contain == terms.contains(fragment.text())) {
                        result = token;
                    }
                }
            }
            return result;
        })
        .label("token" + tokenLabel(contain, javascriptTag, terms));
    }

    private static String tokenLabel(final boolean contain, 
                                     final JavascriptTag javascriptTag,
                                     final Set<String> terms) {
        final StringBuilder result = new StringBuilder();
        if (!contain) {
            result.append("[not]");
        }
        result.append("[");
        result.append(javascriptTag.name());
        result.append("]");
        result.append("[");
        result.append(String.join(",", terms));
        result.append("]");
        return result.toString();
    }
    
}
