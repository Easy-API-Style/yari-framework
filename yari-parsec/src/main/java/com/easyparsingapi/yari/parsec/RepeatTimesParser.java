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

import java.util.Collection;
import java.util.List;

final class RepeatTimesParser<T> extends Parser<List<T>> {
    
    private final Parser<? extends T> parser;
    private final int min;
    private final int max;
    private final ListFactory<T> listFactory;

    RepeatTimesParser(Parser<? extends T> parser,
                      int min, 
                      int max) {
        this(parser, min, max, ListFactory.<T>arrayListFactory());
    }

    RepeatTimesParser(Parser<? extends T> parser, 
                      int min, 
                      int max, 
                      ListFactory<T> listFactory) {
        this.parser = parser;
        this.min = min;
        this.max = max;
        this.listFactory = listFactory;
    }

    @Override
    boolean apply(ParseContext ctxt) {
    	int begin = ctxt.at;
        List<T> result = listFactory.newList();
        if (!ctxt.repeat(parser, min, result))
            return false;
        if (repeatAtMost(max - min, result, ctxt)) {
            ctxt.setResult(result, begin, ctxt.at);
            return true;
        }
        return false;
    }

    private boolean repeatAtMost(int times, 
                                 Collection<T> collection, 
                                 ParseContext ctxt) {
        for (int i = 0; i < times; i++) {
            int physical = ctxt.at;
            int logical = ctxt.step;
            if (!parser.apply(ctxt)) {
                ctxt.setAt(logical, physical);
                return true;
            }
            collection.add(parser.getReturn(ctxt));
        }
        return true;
    }

    @Override
    public String toString() {
        return "times";
    }
    
}