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

/**
 * Parses a list of pattern started with a delimiter, 
 * separated and optionally ended by the delimiter.
 */
final class DelimitedParser<T> extends Parser<List<T>> {
    
    private final Parser<T> parser;
    private final Parser<?> delim;
    private final ListFactory<T> listFactory;

    DelimitedParser(Parser<T> p, 
                    Parser<?> delim, 
                    ListFactory<T> listFactory) {
        this.parser = p;
        this.delim = delim;
        this.listFactory = listFactory;
    }

    @Override
    final boolean apply(final ParseContext ctxt) {
    	int begin = ctxt.at;
        final List<T> result = listFactory.newList();
        for (;;) {
            final int step0 = ctxt.step;
            final int at0 = ctxt.at;
            boolean r = ctxt.applyAsDelimiter(delim);
            if (!r) {
                ctxt.setResult(result, begin, ctxt.at);
                ctxt.setAt(step0, at0);
                return true;
            }
            final int step1 = ctxt.step;
            final int at1 = ctxt.at;
            r = parser.apply(ctxt);
            if (!r) {
                ctxt.setResult(result, begin, ctxt.at);
                ctxt.setAt(step1, at1);
                return true;
            }
            if (at0 == ctxt.at) { // infinite loop
                ctxt.setResult(result, begin, ctxt.at);
                return true;
            }
            result.add(parser.getReturn(ctxt));
        }
    }

    @Override
    public String toString() {
        return "delimited";
    }
    
}
