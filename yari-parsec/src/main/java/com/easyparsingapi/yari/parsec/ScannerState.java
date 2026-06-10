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

import java.util.List;

import com.easyparsingapi.yari.parsec.error.ParserException;
import com.easyparsingapi.yari.parsec.location.SourceLocator;

/**
 * Parser state for scanner.
 */
class ScannerState extends ParseContext {
	
    private final int end;

    ScannerState(CharSequence source) {
        this(source, 0, new SourceLocator(source));
    }

    ScannerState(CharSequence source,
                 int from, 
                 SourceLocator locator) {
        super(source, from, locator);
        this.end = source.length();
    }

    ScannerState(CharSequence source,
                 int from, 
                 int end, 
                 SourceLocator locator) {
        super(source, from, locator);
        this.end = end;
    }
    
    static ScannerState newScannerState(List<Token> tokens) {
        if (tokens.size() == 0) {
            System.out.println();
        }
        Token first = tokens.get(0);
        Token last = tokens.get(tokens.size() - 1);
        SourceLocator sourceLocator = first.sourceLocator();
        ScannerState scannerState = new ScannerState(sourceLocator.source(),
                                                     first.index(), 
                                                     last.index() + last.length(),
                                                     sourceLocator);
        return scannerState;
    }

    /**
     * Creates a ScannerState with an explicit end boundary and a pre-existing result value.
     *
     * @param source         the source string
     * @param from           from where do we start to scan?
     * @param end            till where do we stop scanning? (exclusive)
     * @param locator        the locator for mapping index to line and column number
     * @param originalResult the original result value
     */
    ScannerState(CharSequence source,
                 int from, 
                 int end, 
                 SourceLocator locator, 
                 Result originalResult) {
        super(source, originalResult, from, locator);
        this.end = end;
    }

    @Override
    char peekChar() {
        return source.charAt(at);
    }

    @Override
    boolean isEof() {
        return end == at;
    }

    @Override
    int toIndex(int pos) {
        return pos;
    }

    @Override
    String getInputName(int pos) {
        if (pos >= end) {
            return EOF;
        }
        return Character.toString(source.charAt(pos));
    }

    @Override
    CharSequence characters() {
        return source;
    }

    @Override
    Token getToken() {
        throw new IllegalStateException("Parser not on token level");
    }

    final <T> T run(Parser<T> parser) {
        if (!applyWithExceptionWrapped(parser)) {
            final ParserException parserException = new ParserException(renderError(), locator.locate(errorIndex()));
            parserException.setParseTree(buildErrorParseTree());
            throw parserException;
        }
        return parser.getReturn(this);
    }

    private boolean applyWithExceptionWrapped(Parser<?> parser) {
        try {
            return parser.apply(this);
        }
        catch (RuntimeException e) {
            if (e instanceof ParserException) {
            	 throw (ParserException) e;
            }
            final ParserException wrapper = new ParserException(e, null, locator.locate((getIndex())));
            // Use the successful parse tree because we are interrupted abruptly by an exception
            // So no need to take the "farthest error path".
            wrapper.setParseTree(buildParseTree());
            throw wrapper;
        }
    }

}
