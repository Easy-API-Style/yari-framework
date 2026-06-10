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

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Scanners;

/**
 * {@link Parser}s for testing purpose.
 */
final class TestParsers {

    static Parser<Character> isChar(char c) {
        return Scanners.isChar(c).retn(c);
    }

    static Parser<Character> areChars(String chars) {
        Parser<Character> parser = Parsers.constant(null);
        for (int i = 0; i < chars.length(); i++) {
            parser = parser.next(isChar(chars.charAt(i)));
        }
        return parser;
    }
    
}
