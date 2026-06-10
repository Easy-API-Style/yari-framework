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
package com.easyparsingapi.yari.parser.xml.parser;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class TestXmlParser_01 {
    
    private static Path folder = Path.of("src/test/resources/com/easyparsingapi/yari/parser/xml/html/file");
    
    private static final XmlConfig XML_CONFIG = XmlConfig.builder()
                                                         .acceptUnclosedTag(true)
                                                         .tagAsPlainText("script")
                                                         .tagAsPlainText("style")
                                                         .build();
    
    @Test
    public void test_01(TestInfo testInfo) {
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG, folder.resolve("test_01.html"));
    }

    @Test
    public void test_02(TestInfo testInfo) {
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG, folder.resolve("test_02.html"));
    }

    @Test
    public void test_03(TestInfo testInfo) {
        AssertUtil.assertXmlAstError(testInfo, XML_CONFIG, folder.resolve("test_03.html"));
    }

    @Test
    public void test_04(TestInfo testInfo) {
        AssertUtil.assertXmlAstError(testInfo, XML_CONFIG, folder.resolve("test_04.html"));
    }

    @Test
    public void test_05(TestInfo testInfo) {
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG, folder.resolve("test_05.html"));
    }

    @Test
    public void test_06(TestInfo testInfo) {
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG, folder.resolve("test_06.html"));
    }

    @Test
    public void test_07(TestInfo testInfo) {
        AssertUtil.assertXmlAst(testInfo, XML_CONFIG, folder.resolve("test_07.html"));
    }
    
}
