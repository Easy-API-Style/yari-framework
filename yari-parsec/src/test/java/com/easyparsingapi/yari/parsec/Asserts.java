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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Assert;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Scanners;
import com.easyparsingapi.yari.parsec.error.ParserException;

/**
 * Extra assertions.
 */
public final class Asserts {

    public static void assertFailure(Parser.Mode mode,
    		                         Parser<?> parser,
    		                         String source, 
    		                         int line, 
    		                         int column) {
        try {
            parser.parse(source, mode);
            Assert.fail();
        }
        catch (ParserException e) {
            assertEquals(line, e.getLine());
            assertEquals(column, e.getColumn());
        }
    }

    public static void assertFailure(Parser.Mode mode,
    		                         Parser<?> parser,
    		                         String source, 
    		                         int line, 
    		                         int column,
            String expectedMessage) {
        try {
            parser.parse(source, mode);
            Assert.fail();
        } catch (ParserException e) {
            assertTrue(e.getMessage(), e.getMessage().contains(expectedMessage));
            assertEquals(line, e.getLine());
            assertEquals(column, e.getColumn());
        }
    }

    public static void assertFailure(Parser<?> parser, 
    		                         String source, 
    		                         int line,
    		                         int column,
    		                         String expectedMessage) {
        try {
            parser.parse(source);
            Assert.fail();
        } 
        catch (ParserException e) {
            assertTrue(e.getMessage(), e.getMessage().contains(expectedMessage));
            assertEquals(line, e.getLine());
            assertEquals(column, e.getColumn());
        }
    }

    public static void assertFailure(Parser.Mode mode, 
    		                         Parser<?> parser, 
    		                         String source,
    		                         int line,
    		                         int column,
    		                         Class<? extends Throwable> cause) {
        try {
            parser.parse(source, mode);
            Assert.fail();
        } 
        catch (ParserException e) {
            assertEquals(line, e.getLine());
            assertEquals(column, e.getColumn());
            assertTrue(cause.isInstance(e.getCause()));
        }
    }

    public static void assertParser(Parser.Mode mode,
    		                        Parser<?> parser,
    		                        String source,
    		                        Object value,
    		                        String rest) {
        assertEquals(value, parser.followedBy(Scanners.string(rest)).parse(source, mode));
    }

    public static void assertArrayEquals(Object[] actual,
    		                             Object... expected) {
        assertEquals(Arrays.asList(expected), Arrays.asList(actual));
    }

    static void assertScanner(Parser.Mode mode, 
    		                  Parser<Void> scanner, 
    		                  String source, 
    		                  String remaining) {
        assertNull(scanner.followedBy(Scanners.string(remaining)).parse(source, mode));
    }

    static void assertStringScanner(Parser.Mode mode,
    		                        Parser<String> scanner,
    		                        String source, 
    		                        String remaining) {
        assertEquals(source.substring(0, source.length() - remaining.length()), 
        		     scanner.followedBy(Scanners.string(remaining)).parse(source, mode));
    }

    static void assertStringScanner(Parser.Mode mode, 
    		                        Parser<String> scanner,
    		                        String source) {
        assertEquals(source, scanner.parse(source, mode));
    }
    
}
