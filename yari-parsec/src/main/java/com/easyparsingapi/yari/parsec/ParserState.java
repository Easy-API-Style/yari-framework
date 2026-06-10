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

import java.util.ArrayList;
import java.util.List;

import com.easyparsingapi.yari.parsec.location.SourceLocator;

/**
 * Represents {@link ParseContext} for token level parsing.
 */
class ParserState extends ParseContext {

    private static final String USED_ON_TOKEN_INPUT = "Cannot scan characters on tokens."
            + "\nThis normally happens when you are using a character-level parser on token input."
            + " For example: Scanners.string(foo).from(tokenizer).parse(text) will result in this error"
            + " because scanner works on characters while it's used as a token-level parser.";

    private final Token[] input;
    // in case a terminating eof token is not explicitly created, the implicit one is used.
    private final int endIndex;
    
    ParserState(CharSequence source, 
	            Token[] input, 
	            int at, 
	            SourceLocator locator, 
	            int endIndex, 
	            Result result) {
	    super(source, result, at, locator);
	    this.input = input;
	    this.endIndex = endIndex;
	}

    @Override
    boolean isEof() {
        return at >= input.length;
    }

    @Override
    int toIndex(int pos) {
        if (pos >= input.length) {
            return endIndex;
        }
        return input[pos].index();
    }

    @Override
    Token getToken() {
        return at < input.length ? input[at] : null;
    }
    
    Token getToken(int index) {
        return index < input.length ? input[index] : null;
    }
    
    Token[] getTokens() {
        return input;
    }
    
    List<Token> getTokens(int from, int to) {
    	List<Token> result = new ArrayList<>();
    	if (from <= to) {
    		from = from < 0 ? 0 : from;
    		to = to > input.length - 1 ?  input.length - 1 : to;
    		for (int i = from; i <= to; i++) {
    			result.add(input[i]);
    		}
    	}
        return result;
    }

    @Override
    char peekChar() {
        throw new IllegalStateException(USED_ON_TOKEN_INPUT);
    }

    @Override
    CharSequence characters() {
        throw new IllegalStateException(USED_ON_TOKEN_INPUT);
    }

    @Override
    String getInputName(int pos) {
        if (pos >= input.length) {
            return EOF;
        }
        return input[pos].toString();
    }
	
}
