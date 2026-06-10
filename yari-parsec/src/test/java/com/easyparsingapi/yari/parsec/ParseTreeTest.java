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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.easyparsingapi.yari.parsec.ParseTree;

public class ParseTreeTest {

    @Test
    public void noChildren() {
        Assert.assertEquals("foo[start:0, end:1000, value:123]", tree("foo", 123).toString());
    }

    @Test
    public void oneChild() {
        Assert.assertEquals("foo[start:0, end:1000, value:123]\n"
                          + "  bar[start:0, end:1000, value:x]", 
                            tree("foo", 123, tree("bar", "x")).toString());
    }

    @Test
    public void oneChildren() {
        Assert.assertEquals("foo[start:0, end:1000, value:123]\n"
                          + "  bar[start:0, end:1000, value:x]\n"
                          + "  baz[start:0, end:1000, value:y]",
                            tree("foo", 123, tree("bar", "x"), tree("baz", "y")).toString());
    }

    private static ParseTree tree(String name, Object value, ParseTree... children) {
        return new ParseTree(name, 0, 1000, value, Arrays.asList(children));
    }
    
}
