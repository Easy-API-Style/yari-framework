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
package com.easyparsingapi.yari.parser.html.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstError;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstResult;
import com.easyparsingapi.yari.core.ast.AstUnit;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.TokenService;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parser.html.ast.Html;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.easyparsingapi.yari.parser.xml.lexer.XmlLexer;
import com.easyparsingapi.yari.parser.xml.lexer.XmlLexerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.base.Strings;

public class AssertUtil {
    
    private static boolean write = false;
    private static boolean pretty = true;
    
    public static void write() {
        write = true;
    }
    
    public static void write(boolean write) {
        AssertUtil.write = write;
    }
    
    public static Path targetFolder = Path.of("src/test/resources/com/easyparsingapi/yari/parser/html");
    
    private static final ObjectMapper JSON_OBJECT_MAPPER = JsonMapper.builder()
                                                                     .build();
    
    record Target(Path folder, String file) {}
    
    /*
     * 
     * HTML
     * 
     */
    public static void assertHtmlAst(final TestInfo testInfo,
                                     final HtmlConfig htmlConfig, 
                                     final String... source) {
        try {
            AstResult<Html> unit = HtmlParser.parseUnit(toSource(source), htmlConfig);
            assertAst(unit.unit(), unit.getTokens(), toTarget(testInfo), true);
        } 
        catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void assertHtmlAstError(final TestInfo testInfo,
                                          final HtmlConfig htmlConfig, 
                                          final String... source) {
        try {
            AstResult<Html> unit = HtmlParser.parseUnit(toSource(source), htmlConfig);
            assertAst(unit.unit(), unit.getTokens(), toTarget(testInfo), false);
        } 
        catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public static void assertHtmlAst(final TestInfo testInfo,
                                     final Path file) {
        try {
            AstResult<Html> unit = HtmlParser.parseUnit(Files.readString(file), 
                                                        HtmlConfig.defaulConfig());
            assertAst(unit.unit(), unit.getTokens(), toTarget(testInfo), true);
        } 
        catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public static void assertHtmlAstError(final TestInfo testInfo,
                                          final Path file) {
        try {
            AstResult<Html> unit = HtmlParser.parseUnit(Files.readString(file), 
                                                        HtmlConfig.defaulConfig());
            assertAst(unit.unit(), unit.getTokens(), toTarget(testInfo), false);
        } 
        catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /*
     * 
     * TOKEN
     * 
     */
    public static void assertHtmlToken(final TestInfo testInfo,
                                       final Path file) {
        try {
            List<Token> tokens = XmlLexer.lex(XmlLexerConfig.builder()
                                                            .tagAsPlainText("script")
                                                            .tagAsPlainText("style")
                                                            .build(),
                                             Files.readString(file));
            assertToken(tokens, toTarget(testInfo));
        } 
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void assertToken(final List<Token> tokens,
                                    final Target target) {
        try {
            final Path folderPath = target.folder(); 
            if (write) {
                delete(folderPath);
            }
            Files.createDirectories(folderPath);
            final Path tokenFile = folderPath.resolve(target.file() + "_token.csv");
            if (Files.exists(tokenFile)) {
                final List<String> expectedTokens = Files.readAllLines(tokenFile);
                final List<String> actualTokens = tokens(tokens);
                assertEquals(expectedTokens, actualTokens);
            }
            else {
                Files.write(tokenFile, tokens(tokens));
            }
        } 
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /*
     * 
     * 
     * 
     */
    private static void assertAst(final AstNode actualAstNode, 
                                  final List<Token> tokens,
                                  final Target target,
                                  final boolean failedIfError) {
        try {
            final List<AstError> errors = actualAstNode.astStream()
                                                 .parallel()
                                                 .filter(n -> n instanceof AstError)
                                                 .map(n -> (AstError) n)
                                                 .toList();
            if (failedIfError) {
                if (!errors.isEmpty()) {
                    fail(System.lineSeparator() 
                       + "-> " + String.join(System.lineSeparator() + "-> ", 
                                             errors.stream()
                                                   .map(AstError::toString)
                                                   .toList()));
                }
            }
            else {
                if (errors.isEmpty()) {
                    fail("ast error not found");
                }
            }
            
            final Path folderPath = target.folder(); 
            if (write) {
                delete(folderPath);
            }
            Files.createDirectories(folderPath);
            // node
            final Path xmlFile = folderPath.resolve(target.file() + "_xml.json");
            if (Files.exists(xmlFile)) {
                final XmlNode expectedAstNode = JSON_OBJECT_MAPPER.reader().readValue(xmlFile.toFile(), XmlNode.class);
                final List<AstNode> expectedNodes = expectedAstNode.astStream().toList();
                final List<AstNode> actualNodes = actualAstNode.astStream().toList();
                if (expectedNodes.size() == actualNodes.size()) {
                    for (int i = expectedNodes.size() - 1; i >= 0; i--) {
                        assertEquals(expectedNodes.get(i), actualNodes.get(i),
                                     "\n" + i +
                                     "\n" + expectedNodes.get(i) + 
                                     "\n" + actualNodes.get(i) +
                                     "\n" );
                        if (i == 0) {
                            assertNull("expected parent != null " + i + " " + expectedNodes.get(i),
                                          expectedNodes.get(i).astParent());
                            assertNull("actual parent != null " + i + " " + actualNodes.get(i),
                                       actualNodes.get(i).astParent());
                        }
                        else {
                            assertNotNull("expected parent == null " + i + " " + expectedNodes.get(i),
                                          expectedNodes.get(i).astParent());
                            assertNotNull("actual parent == null " + i + " " + actualNodes.get(i),
                                          actualNodes.get(i).astParent());
                        }
                    }
                    for (int i = 0; i < actualNodes.size(); i++) {
                        AstNode parent = actualNodes.get(i);
                        for(AstNode child : parent.astChildren()) {
                            assertTrue("actual wrong parent " 
                                         + i + " " + actualNodes.get(i)
                                         + " -> " + child.astParent(), 
                                       parent == child.astParent());
                        }
                    }
                }
                else {
                    assertEquals(expectedNodes.size(), actualNodes.size());
                }
                assertEquals(expectedAstNode, actualAstNode);
            }
            else {
                if (pretty) {
                    JSON_OBJECT_MAPPER.writer()
                                      .withDefaultPrettyPrinter()
                                      .writeValue(xmlFile.toFile(), actualAstNode);
                }
                else {
                    JSON_OBJECT_MAPPER.writer()
                                      .writeValue(xmlFile.toFile(), actualAstNode);
                }
            }
            // token
            final Path tokenFile = folderPath.resolve(target.file() + "_token.csv");
            if (Files.exists(tokenFile)) {
                final List<String> expectedTokens = Files.readAllLines(tokenFile);
                final List<String> actualTokens = tokens(tokens);
                assertEquals(expectedTokens, actualTokens);
            }
            else {
                Files.write(tokenFile, tokens(tokens));
            }
            // location
            final Path locationFile = folderPath.resolve(target.file() + "_location.txt");
            final TokenService tokenService = new TokenService(tokens);
            if (Files.exists(locationFile)) {
                final List<String> expectedLocations = Files.readAllLines(locationFile);
                final List<String> actualLocations = location(actualAstNode, tokenService);
                assertEquals(String.join("\n", expectedLocations), 
                             String.join("\n", actualLocations));
            }
            else {
                Files.write(locationFile, location(actualAstNode, tokenService));
            }
            // tree
            final Path treeFile = folderPath.resolve(target.file() + "_tree.txt");
            if (Files.exists(treeFile)) {
                final List<String> expectedLocations = Files.readAllLines(treeFile);
                final List<String> actualLocations = tree(actualAstNode, tokenService);
                assertEquals(String.join("\n", expectedLocations), 
                             String.join("\n", actualLocations));
            }
            else {
                Files.write(treeFile, tree(actualAstNode, tokenService));
            }
            // toString
            final Path toStringFile = folderPath.resolve(target.file() + ".txt");
            if (Files.exists(toStringFile)) {
                final List<String> expectedLocations = Files.readAllLines(toStringFile);
                final List<String> actualLocations = toString(actualAstNode, tokenService);
                assertEquals(String.join("\n", expectedLocations), 
                             String.join("\n", actualLocations));
            }
            else {
                Files.write(toStringFile, toString(actualAstNode, tokenService));
            }
        } 
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> tokens(final List<Token> tokens) {
        final List<String> result = new ArrayList<>();
        for (final Token token : tokens) {
            final StringBuilder line = new StringBuilder();
            line.append(token.index());
            line.append(";");
            line.append(token.length());
            line.append(";");
            final Fragment fragment = (Fragment) token.value();
            line.append(fragment.tag());
            line.append(";");
            line.append(clean(fragment.text()));
            result.add(line.toString());
        }
        return result;
    }

    private static List<String> tree(final AstNode astNode, 
                                     final TokenService tokenService) {
        final List<String> result = new ArrayList<>();
        result.add(Strings.repeat(" ", 0) 
                 + "[" + 0 + ", " + astNode.getClass().getSimpleName() + "] " 
                 + clean(tokenService.substring(astNode.getSourceLocation())));
        astNode.walkChildren(h -> {
            AstNode n = h.node();
            result.add(Strings.repeat(" ", h.deep()) 
                       + "[" + h.deep() + ", " + n.getClass().getSimpleName() + "] " 
                       + clean(tokenService.substring(n.getSourceLocation())));
        });
        return result;
    }
    
    private static List<String> toString(final AstNode astNode, 
                                         final TokenService tokenService) {
        final List<String> result = new ArrayList<>();
        if (astNode instanceof AstUnit astUnit) {
            for (final AstComment astComment : astUnit.astComments()) {
                result.add(clean(astComment.toString()));
            }
        }
        else {
            result.add(clean(astNode.toString()));
        }
        astNode.astStream().forEach(n -> {
            result.add(clean(n.toString()));
        });
        return result;
    }
    
    private static List<String> location(final AstNode astNode, 
                                         final TokenService tokenService) {
        final List<String> result = new ArrayList<>();
        if (astNode instanceof AstUnit astUnit) {
            for (final AstComment astComment : astUnit.astComments()) {
                result.add(clean(astComment.getSourceLocation().toString()) + " [" + astNode.getClass().getSimpleName() + "]");
                result.add(clean(tokenService.substring(astComment.getSourceLocation())));
                result.add("---------------------------");
            }
        }
        else {
            result.add(clean(tokenService.substring(astNode.getSourceLocation())) + " [" + astNode.getClass().getSimpleName() + "]");
            result.add("---------------------------");
        }
        astNode.astStream().forEach(n -> {
            result.add(clean(n.getSourceLocation().toString()) + " [" + n.getClass().getSimpleName() + "]");
            result.add(clean(tokenService.substring(n.getSourceLocation())));
            result.add("---------------------------");
        });
        return result;
    }
    
    private static String clean(final String value) {
        if (value != null) {
            return value.replace("\n", "\\\\n")
                        .replace("\r", "")
                        .replaceAll("\\s+", " ");
        }
        return null;
    }
    
    private static Target toTarget(final TestInfo testInfo) {
        String className = camelCaseToSnake(testInfo.getTestClass().get().getSimpleName());
        String methodName = testInfo.getTestMethod().get().getName();
        Path folder = targetFolder.resolve(className)
                                  .resolve(methodName);
        String file = className.replace("test_", "") +  methodName.replace("test", "");
        return new Target(folder, file);
    }
    
    public static String toSource(final String... source) {
        final List<String> result = new ArrayList<>();
        for (final String v : source) {
            result.add(v.replace("\r", ""));
        }
        return String.join("\n", result);
    }
    
    public static String camelCaseToSnake(String input) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (char c : input.toCharArray()) {
            if (i == 0) {
                result.append(Character.toLowerCase(c));
            }
            else if (Character.isUpperCase(c)) {
                result.append("_").append(Character.toLowerCase(c));
            } 
            else {
                result.append(c);
            }
            i++;
        }
        return result.toString();
    }
    
    public static void delete(final Path path) throws IOException {
        if (path != null && Files.exists(path)) {
            try (final Stream<Path> paths = Files.walk(path)) {
                paths.sorted(Comparator.reverseOrder())
                     .map(Path::toFile)
                     .forEach(File::delete);
            }
            while (Files.exists(path)) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {}
            }
        }
    }

}
