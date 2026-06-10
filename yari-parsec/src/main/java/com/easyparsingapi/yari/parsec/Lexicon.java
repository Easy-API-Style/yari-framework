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
package com.easyparsingapi.yari.parsec;

import java.util.function.Function;

import com.easyparsingapi.yari.parsec.internal.annotations.Private;
import com.easyparsingapi.yari.parsec.internal.util.Checks;
import com.easyparsingapi.yari.parsec.internal.util.StringUtil;

/**
 * A {@link Lexicon} is a group of lexical words that can be tokenized by a single tokenizer.
 */
class Lexicon {

    /** Maps lexical word name to token value. */
    final Function<String, Object> words;

    /** The scanner that recognizes any of the lexical word. */
    final Parser<?> tokenizer;

    Lexicon(Function<String, Object> words,
            Parser<?> tokenizer) {
        this.words = words;
        this.tokenizer = tokenizer;
    }

    /**
     * Returns the tokenizer that tokenizes all terminals (operators, keywords, identifiers etc.) managed in this instance.
     *
     * @return the {@link Parser} that produces tokens for all terminals defined in this lexicon
     */
    public Parser<?> tokenizer() {
        return tokenizer;
    }

    /**
     * A {@link Parser} that recognizes a sequence of tokens identified by {@code tokenNames}, as an atomic step.
     *
     * @param tokenNames the ordered names of the tokens that form the phrase
     * @return a {@link Parser} that matches the exact sequence of named tokens and returns the joined phrase string
     */
    public Parser<?> phrase(String... tokenNames) {
        Parser<?>[] wordParsers = new Parser<?>[tokenNames.length];
        for (int i = 0; i < tokenNames.length; i++) {
            wordParsers[i] = token(tokenNames[i]);
        }
        String phrase = StringUtil.join(" ", tokenNames);
        return Parsers.sequence(wordParsers).atomic().retn(phrase).label(phrase);
    }

    /**
     * A {@link Parser} that recognizes a token identified by any of {@code tokenNames}.
     *
     * @param tokenNames the names of the acceptable tokens; the parser succeeds if the next token matches any one of them
     * @return a {@link Parser} that matches and returns the first token whose value equals any of the named tokens
     */
    public Parser<Token> token(String... tokenNames) {
        if (tokenNames.length == 0) {
            return Parsers.never();
        }
        @SuppressWarnings("unchecked")
        Parser<Token>[] ps = new Parser[tokenNames.length];
        for (int i = 0; i < tokenNames.length; i++) {
            ps[i] = Parsers.token(InternalFunctors.tokenWithSameValue(word(tokenNames[i])));
        }
        return Parsers.or(ps);
    }

    /**
     * A {@link Parser} that recognizes the token identified by {@code tokenName}.
     *
     * @param tokenName the name of the token to match
     * @return a {@link Parser} that matches and returns the token whose value equals {@code tokenName}
     */
    public Parser<Token> token(String tokenName) {
        return Parsers.token(InternalFunctors.tokenWithSameValue(word(tokenName)));
    }

    /**
     * Gets the token value identified by the token text. 
     * This text is the operator or the keyword.
     * 
     * @param name the token text.
     * @return the token object.
     * @exception IllegalArgumentException if the token object does not exist.
     */
    @Private
    Object word(String name) {
        Object p = words.apply(name);
        Checks.checkArgument(p != null, "token %s unavailable", name);
        return p;
    }

    /**
     * Returns a {@link Lexicon} instance that's a union of {@code this} and {@code that}.
     */
    Lexicon union(Lexicon that) {
        return new Lexicon(fallback(words, that.words), Parsers.or(tokenizer, that.tokenizer));
    }

    /**
     * Returns a {@link Function} that delegates to {@code function} 
     * and falls back to {@code defaultFunction} for null return values.
     */
    static <F, T> Function<F, T> fallback(Function<F, T> function, Function<? super F, ? extends T> defaultFunction) {
        return from -> {
            T result = function.apply(from);
            return result == null ? defaultFunction.apply(from) : result;
        };
    }
    
}
