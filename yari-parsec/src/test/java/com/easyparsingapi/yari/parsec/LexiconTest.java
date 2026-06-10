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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.easyparsingapi.yari.parsec.Lexicon;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Terminals;

/**
 * Unit test for {@link Lexicon}.
 */
public class LexiconTest {

    @Test
    public void testWord() {
        Parser<?> tokenizer = Terminals.CharLiteral.SINGLE_QUOTE_TOKENIZER;
        Lexicon lexicon = new Lexicon(__ -> "foo", tokenizer);
        assertSame(tokenizer, lexicon.tokenizer);
        assertEquals("foo", lexicon.word("whatever"));
    }

    @Test
    public void testWord_throwsForNullValue() {
        Parser<?> tokenizer = Terminals.CharLiteral.SINGLE_QUOTE_TOKENIZER;
        Lexicon lexicon = new Lexicon(__ -> null, tokenizer);
        assertSame(tokenizer, lexicon.tokenizer);
        try {
            lexicon.word("whatever");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

}
