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

import org.junit.Test;

import com.easyparsingapi.yari.parsec.location.SourceLocator;
import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;

/**
 * Unit test for {@link SourceLocator}.
 */
public class DefaultSourceLocatorTest {

    @Test
    public void testLocate_onlyOneLineBreakCharacter() {
        SourceLocator locator = new SourceLocator("\n");
        Position location = locator.locate(0);
        assertEquals(new Position(1, 1), location);
        assertEquals(location, locator.locate(0));
        assertEquals(new Position(1, 2), locator.locate(1));
    }

    @Test
    public void testLocate_emptySource() {
        SourceLocator locator = new SourceLocator("");
        Position location = locator.locate(0);
        assertEquals(new Position(1, 1), location);
    }
    
    @Test
    public void testLocate_lineBreakCharacterAndLine() {
        SourceLocator locator = new SourceLocator("\r\na");
        
        Position location_01 = locator.locate(0);
        Position location_02 = locator.locate(1);
        Position location_03 = locator.locate(2); 
        
        assertEquals(new Position(1, 1), location_01);
        assertEquals(location_01, locator.locate(0));
        assertEquals("\r", locator.substring(location_01, location_02));
        
        assertEquals(new Position(1, 2), location_02);
        assertEquals(location_02, locator.locate(1));
        assertEquals("\n", locator.substring(location_02, location_03));
        
        assertEquals(new Position(2, 1), location_03);
        assertEquals(location_03, locator.locate(2));
        assertEquals("a", locator.substring(location_03, new Position(10, 2)));
    }

}
