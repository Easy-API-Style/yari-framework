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

/**
 * Parses any nestable comment pattern.
 */
final class NestableBlockCommentScanner extends Parser<Void> {
    
    private final Parser<?> openQuote;
    private final Parser<?> closeQuote;
    private final Parser<?> commented;

    NestableBlockCommentScanner(Parser<?> openQuote, 
                                Parser<?> closeQuote, 
                                Parser<?> commented) {
        this.openQuote = openQuote;
        this.closeQuote = closeQuote;
        this.commented = commented;
    }

    @Override
    boolean apply(final ParseContext ctxt) {
    	int begin = ctxt.at;
        if (!openQuote.apply(ctxt))
            return false;
        for (int level = 1; level > 0;) {
            final int step = ctxt.step;
            final int at = ctxt.at;
            if (closeQuote.apply(ctxt)) {
                if (at == ctxt.at) {
                    throw new IllegalStateException("closing comment scanner not consuming input.");
                }
                level--;
                continue;
            }
            if (openQuote.apply(ctxt)) {
                if (at == ctxt.at) {
                    throw new IllegalStateException("opening comment scanner not consuming input.");
                }
                level++;
                continue;
            }
            if (!ctxt.stillThere(at, step))
                return false;
            if (commented.apply(ctxt)) {
                if (at == ctxt.at) {
                    throw new IllegalStateException("commented scanner not consuming input.");
                }
                continue;
            }
            return false;
        }
        ctxt.setResult(null, begin, ctxt.at);
        return true;
    }

    @Override
    public String toString() {
        return "nestable block comment";
    }
    
}