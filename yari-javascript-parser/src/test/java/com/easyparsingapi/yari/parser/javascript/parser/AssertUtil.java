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
package com.easyparsingapi.yari.parser.javascript.parser;

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
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstResult;
import com.easyparsingapi.yari.core.ast.AstUnit;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.TokenService;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptError;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node;
import com.google.common.base.Strings;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class AssertUtil {
    
    private static boolean write = false;
    private static boolean pretty = true;
    
    public static void write() {
        write = true;
    }
    
    public static void write(boolean write) {
        AssertUtil.write = write;
    }
    
    public static Path targetFolder = Path.of("src/test/resources/com/easyparsingapi/yari/parser/javascript");
    
    private static final ObjectMapper JSON_OBJECT_MAPPER = JsonMapper.builder()
                                                                     .build();
    
    record Target(Path folder, String file) {}

    public static void assertAst(final Target target,
                                 final String... source) {
        final AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(toSource(source));
        assertAst(javascriptUnit.unit(), javascriptUnit.getTokens(), target, true);
    }

    public static void assertAst(final Target target,
                                 final Node node,
                                 final String... source) {
         final AstResult<JavascriptNodeUnit> unit = JavascriptParser.parseUnit(toSource(source), node);
         assertAst(unit.unit(), unit.getTokens(), target, true);
    }

    public static void assertAst(final Target target,
                                 final List<String> source) {
        assertAst(target, String.join("\n", source));
    }

    public static void assertAst(final Target target,
                                 final Path file) {
        try {
            AstResult<Javascript> unit = JavascriptParser.parseUnit(Files.readString(file));
            assertAst(unit.unit(), unit.getTokens(), target, true);
        } 
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void assertAst(final TestInfo testInfo,
                                 final Path file) {
        try {
            AstResult<Javascript> unit = JavascriptParser.parseUnit(Files.readString(file));
            assertAst(unit.unit(), unit.getTokens(), toTarget(testInfo), true);
        } 
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void assertAstError(final TestInfo testInfo,
                                      final Path file) {
        try {
            AstResult<Javascript> unit = JavascriptParser.parseUnit(Files.readString(file));
            assertAst(unit.unit(), unit.getTokens(), toTarget(testInfo), false);
        } 
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void assertAstExpression(final TestInfo testInfo,
                                           final Set<Node> nodes,
                                           final String... source) {
        final AstResult<JavascriptNodeUnit> unit = JavascriptParser.parseUnitExpression(toSource(source), nodes);
        assertAst(unit.unit(), unit.getTokens(), toTarget(testInfo), true);
    }
    
    public static void assertAst(final TestInfo testInfo,
                                 final Set<Node> nodes,
                                 final String... source) {
        final AstResult<Javascript> unit = JavascriptParser.parseUnit(toSource(source), nodes);
        assertAst(unit.unit(), unit.getTokens(), toTarget(testInfo), true);
    }
    
    public static void assertAst(final TestInfo testInfo,
                                 final Node node,
                                 final String... source) {
        assertAst(toTarget(testInfo), node, source);
    }
    
    public static void assertAst(final TestInfo testInfo, 
                                 final String... source) {
        assertAst(toTarget(testInfo), source);
    }
    
    public static void assertAstError(final TestInfo testInfo,
                                      final Node node,
                                      final String... source) {
        final AstResult<JavascriptNodeUnit> javascriptUnit = JavascriptParser.parseUnit(toSource(source), node);
        assertAst(javascriptUnit.unit(), javascriptUnit.getTokens(), toTarget(testInfo), false);
    }
    
    public static void assertAstError(final TestInfo testInfo,
                                      final String... source) {
        final AstResult<Javascript> javascriptUnit = JavascriptParser.parseUnit(toSource(source));
        assertAst(javascriptUnit.unit(), javascriptUnit.getTokens(), toTarget(testInfo), false);
    }
    
    private static void assertAst(final JavascriptNode javascriptNode, 
                                  final List<Token> tokens,
                                  final Target target,
                                  final boolean failedIfError) {
        try {
            final List<JavascriptError> javascriptErrors = javascriptNode.astStream()
                                                                         .parallel()
                                                                         .filter(n -> n instanceof JavascriptError)
                                                                         .map(n -> (JavascriptError) n)
                                                                         .toList();
            if (failedIfError) {
                if (!javascriptErrors.isEmpty()) {
                    fail(System.lineSeparator() 
                       + "-> " + String.join(System.lineSeparator() + "-> ", 
                                             javascriptErrors.stream().map(JavascriptError::toString).toList()));
                }
            }
            else {
                if (javascriptErrors.isEmpty()) {
                    fail("ast error not found");
                }
            }
            
            final Path folderPath = target.folder(); 
            if (write) {
                delete(folderPath);
            }
            Files.createDirectories(folderPath);
            // javascript
            final Path javascriptFile = folderPath.resolve(target.file() + "_javascript.json");
            if (Files.exists(javascriptFile)) {
                final JavascriptNode expectedJavascript = JSON_OBJECT_MAPPER.reader()
                                                                            .forType(JavascriptNode.class)
                                                                            .readValue(javascriptFile.toFile());
                final List<AstNode> expectedJavascriptNodes = expectedJavascript.astStream().toList();
                final List<AstNode> actualJavascriptNodes = javascriptNode.astStream().toList();
                if (expectedJavascriptNodes.size() == actualJavascriptNodes.size()) {
                    for (int i = expectedJavascriptNodes.size() - 1; i >= 0; i--) {
                        assertEquals(expectedJavascriptNodes.get(i), actualJavascriptNodes.get(i));
                        if (i == 0) {
                            assertNull("expected parent != null " + i + " " + expectedJavascriptNodes.get(i),
                                       expectedJavascriptNodes.get(i).astParent());
                            assertNull("actual parent != null " + i + " " + actualJavascriptNodes.get(i),
                                       actualJavascriptNodes.get(i).astParent());
                        }
                        else {
                            assertNotNull("expected parent == null " + i + " " + expectedJavascriptNodes.get(i),
                                          expectedJavascriptNodes.get(i).astParent());
                            assertNotNull("actual parent == null " + i + " " + actualJavascriptNodes.get(i),
                                          actualJavascriptNodes.get(i).astParent());
                        }
                    }
                    for (int i = 0; i < actualJavascriptNodes.size(); i++) {
                        AstNode parent = actualJavascriptNodes.get(i);
                        for(AstNode child : parent.astChildren()) {
                            if (parent != child.astParent()) {
                                System.out.println();
                            }
                            assertTrue("actual wrong parent " 
                                         + i + " " + actualJavascriptNodes.get(i)
                                         + " -> " + child.astParent(), 
                                       parent == child.astParent());
                        }
                    }
                }
                else {
                    assertEquals(expectedJavascriptNodes.size(), actualJavascriptNodes.size());
                }
                assertEquals(expectedJavascript, javascriptNode);
            }
            else {
                if (pretty) {
                    JSON_OBJECT_MAPPER.writer()
                                      .withDefaultPrettyPrinter()
                                      .writeValue(javascriptFile.toFile(), javascriptNode);
                }
                else {
                    JSON_OBJECT_MAPPER.writer()
                                      .writeValue(javascriptFile.toFile(), javascriptNode);
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
                final List<String> actualLocations = location(javascriptNode, tokenService);
                assertEquals(String.join("\n", expectedLocations), 
                             String.join("\n", actualLocations));
            }
            else {
                Files.write(locationFile, location(javascriptNode, tokenService));
            }
            // tree
            final Path treeFile = folderPath.resolve(target.file() + "_tree.txt");
            if (Files.exists(treeFile)) {
                final List<String> expectedLocations = Files.readAllLines(treeFile);
                final List<String> actualLocations = tree(javascriptNode, tokenService);
                assertEquals(String.join("\n", expectedLocations), 
                             String.join("\n", actualLocations));
            }
            else {
                Files.write(treeFile, tree(javascriptNode, tokenService));
            }
            // toString
            final Path toStringFile = folderPath.resolve(target.file() + ".txt");
            if (Files.exists(toStringFile)) {
                final List<String> expectedLocations = Files.readAllLines(toStringFile);
                final List<String> actualLocations = toString(javascriptNode, tokenService);
                assertEquals(String.join("\n", expectedLocations), 
                             String.join("\n", actualLocations));
            }
            else {
                Files.write(toStringFile, toString(javascriptNode, tokenService));
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

    private static List<String> tree(final JavascriptNode javascriptNode, 
                                     final TokenService tokenService) {
        final List<String> result = new ArrayList<>();
        result.add(Strings.repeat(" ", 0) 
                 + "[" + 0 + ", " + javascriptNode.getClass().getSimpleName() + "] " 
                 + clean(tokenService.substring(javascriptNode.getSourceLocation())));
        javascriptNode.walkChildren(h -> {
            AstNode n = h.node();
            result.add(Strings.repeat(" ", h.deep()) 
                       + "[" + h.deep() + ", " + n.getClass().getSimpleName() + "] " 
                       + clean(tokenService.substring(n.getSourceLocation())));
        });
        return result;
    }
    
    private static List<String> toString(final JavascriptNode javascriptNode, 
                                         final TokenService tokenService) {
        final List<String> result = new ArrayList<>();
        if (javascriptNode instanceof AstUnit astUnit) {
            for (final AstComment astComment : astUnit.astComments()) {
                result.add(clean(astComment.toString()));
            }
        }
        else {
            result.add(clean(javascriptNode.toString()));
        }
        javascriptNode.astStream().forEach(n -> {
            result.add(clean(n.toString()));
        });
        return result;
    }
    
    private static List<String> location(final JavascriptNode javascriptNode, 
                                         final TokenService tokenService) {
        final List<String> result = new ArrayList<>();
        if (javascriptNode instanceof AstUnit astUnit) {
            for (final AstComment astComment : astUnit.astComments()) {
                result.add(clean(astComment.getSourceLocation().toString()) + " [" + javascriptNode.getClass().getSimpleName() + "]");
                result.add(clean(tokenService.substring(astComment.getSourceLocation())));
                result.add("---------------------------");
            }
        }
        else {
            result.add(clean(tokenService.substring(javascriptNode.getSourceLocation())) + " [" + javascriptNode.getClass().getSimpleName() + "]");
            result.add("---------------------------");
        }
        javascriptNode.astStream().forEach(n -> {
            result.add(clean(n.getSourceLocation().toString()) + " [" + n.getClass().getSimpleName() + "]");
            result.add(clean(tokenService.substring(n.getSourceLocation())));
            result.add("---------------------------");
        });
        return result;
    }
    
    public static String clean(final String value) {
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
