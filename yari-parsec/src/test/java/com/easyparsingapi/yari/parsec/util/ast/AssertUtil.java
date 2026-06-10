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
package com.easyparsingapi.yari.parsec.util.ast;

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

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.TokenService;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;

public class AssertUtil {
    
    public static record Target(Path folder, String file) {}
    public static record Result<V>(List<Token> tokens, V ast) {}
    
    private static boolean write = true;
    
    public static void write() {
        write = true;
    }
    
    public static Path targetFolder = Path.of("src/test/resources/com/easyparsingapi/yari/parsec/api");
    
    public static void assertAstError(final TestInfo testInfo,
                                      final Result<Ast> result) {
        assertAst(testInfo, result.ast(), result.tokens(), false);
    }
    
    public static void assertAst(final TestInfo testInfo,
                                 final Result<Ast> result) {
        assertAst(testInfo, result.ast(), result.tokens(), true);
    }
    
    public static void assertAstError(final TestInfo testInfo,
                                      final List<Token> tokens,
                                      final Ast ast) {
        assertAst(testInfo, ast, tokens, false);
    }
    
    public static void assertAst(final TestInfo testInfo,
                                 final List<Token> tokens,
                                 final Ast ast) {
        assertAst(testInfo, ast, tokens, true);
    }
    
    private static void assertAst(final TestInfo testInfo,
                                  final Ast ast, 
                                  final List<Token> tokens,
                                  final boolean failedIfError) {
        try {
            final List<Error> errors = ast.astStream()
                                          .filter(n -> n instanceof Error)
                                          .map(n -> (Error) n)
                                          .toList();
            if (failedIfError) {
                if (!errors.isEmpty()) {
                    fail(System.lineSeparator() + "-> " + String.join(System.lineSeparator() + "-> ", errors.stream().map(Error::toString).toList()));
                }
            }
            else {
                if (errors.isEmpty()) {
                    fail("ast error not found");
                }
            }
            final Target target = toTarget(testInfo);
            final Path folderPath = target.folder(); 
            if (write) {
                delete(folderPath);
            }
            Files.createDirectories(folderPath);
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
                final List<String> actualLocations = location(ast, tokenService);
                assertEquals(String.join("\n", expectedLocations), 
                             String.join("\n", actualLocations));
            }
            else {
                Files.write(locationFile, location(ast, tokenService));
            }
            // tree
            final Path treeFile = folderPath.resolve(target.file() + "_tree.txt");
            if (Files.exists(treeFile)) {
                final List<String> expectedLocations = Files.readAllLines(treeFile);
                final List<String> actualLocations = tree(ast, tokenService);
                assertEquals(String.join("\n", expectedLocations), 
                             String.join("\n", actualLocations));
            }
            else {
                Files.write(treeFile, tree(ast, tokenService));
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

    private static List<String> tree(final Ast ast, 
                                     final TokenService tokenService) {
        
        final List<String> result = new ArrayList<>();
        ast.astStream().forEach(n -> {
            result.add("[" + n.getClass().getSimpleName() + "] " 
                       + clean(tokenService.substring(n.getSourceLocation())));
        });
        return result;
    }
    
    private static List<String> location(final Ast ast, 
                                         final TokenService tokenService) {
        final List<String> result = new ArrayList<>();
        ast.astStream().forEach(n -> {
            result.add(clean(n.toString()));
            result.add(clean(tokenService.substring(n.getSourceLocation())));
            result.add("---------------------------");
        });
        return result;
    }
    
    private static String clean(final String value) {
        if (value != null) {
            return value.replace("\n", "\\\\n").replace("\r", "");
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

    public static String toSource(final List<String> source) {
        final List<String> result = new ArrayList<>();
        for (final String v : source) {
            result.add(v.replace("\r", ""));
        }
        return String.join("\n", result);
    }
    
    private static void delete(final Path path) throws IOException {
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

}
