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
package com.easyparsingapi.yari.core.ast;

import java.util.List;

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.TokenService;
import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parsec.location.SourceLocation;

/**
 * Holds the result of a parsing operation, combining the root AST node with
 * the token stream and source-navigation services produced during parsing.
 *
 * @param <U> the concrete {@link AstNode} type that forms the root of the parsed tree
 */
public class AstResult<U extends AstNode> {

    private final U unit;
    private final TokenService tokenService;

    /**
     * Constructs an {@code AstResult} from a root AST node and the flat list of
     * tokens that were produced by the lexer.
     *
     * @param unit   the root node of the abstract syntax tree
     * @param tokens the ordered list of tokens covering the entire source input
     */
    public AstResult(final U unit,
                     final List<Token> tokens) {
        super();
        this.unit = unit;
        this.tokenService = new TokenService(tokens);
    }

    /**
     * Returns the root node of the abstract syntax tree produced by the parser.
     *
     * @return the root {@link AstNode}
     */
    public U unit() {
        return unit;
    }

    /*
     *
     * TOKEN
     *
     */

    /**
     * Returns the total number of tokens in the token stream.
     *
     * @return the number of tokens
     */
    public int tokenSize() {
        return tokenService.tokenSize();
    }

    /**
     * Returns the complete, ordered list of tokens that cover the source input.
     *
     * @return an unmodifiable list of {@link Token} objects
     */
    public List<Token> getTokens() {
        return tokenService.getTokens();
    }

    /**
     * Returns the token at the given index in the token stream.
     *
     * @param tokenIndex the zero-based index of the token to retrieve
     * @return the {@link Token} at {@code tokenIndex}
     */
    public Token getToken(final int tokenIndex) {
        return tokenService.getToken(tokenIndex);
    }

    /**
     * Returns the zero-based index of the given token within the token stream.
     *
     * @param token the {@link Token} whose index is to be found
     * @return the index of {@code token}, or {@code -1} if not present
     */
    public int getTokenIndexOf(final Token token) {
        return tokenService.getTokenIndexOf(token);
    }

    /**
     * Returns the token whose position in the source corresponds to the given
     * character index.
     *
     * @param index the character offset into the source
     * @return the {@link Token} covering position {@code index}
     */
    public Token getTokenAt(final int index) {
        return tokenService.getTokenAt(index);
    }

    /**
     * Returns all tokens that fall within the source range of the given node.
     *
     * @param node the {@link SourceLocalisable} node whose tokens are requested
     * @return the list of {@link Token} objects that belong to {@code node}
     */
    public List<Token> getTokensOf(final SourceLocalisable node) {
        return tokenService.getTokensOf(node);
    }

    /*
     *
     * SUBSTRING
     *
     */

    /**
     * Returns the full source text reconstructed from the token stream.
     *
     * @return the complete source string
     */
    public String getSource() {
        return tokenService.getSource();
    }

    /**
     * Returns the source substring covered by the given {@link SourceLocation}.
     *
     * @param sourceLocation the location range to extract
     * @return the source text within {@code sourceLocation}
     */
    public String substring(final SourceLocation sourceLocation) {
        return tokenService.substring(sourceLocation);
    }

    /**
     * Returns the source substring between two character offsets.
     *
     * @param startIndex the inclusive start character offset
     * @param endIndex   the exclusive end character offset
     * @return the source text between {@code startIndex} and {@code endIndex}
     */
    public String substring(final int startIndex, final int endIndex) {
        return tokenService.substring(startIndex, endIndex);
    }

    /**
     * Returns the source substring delimited by line/column coordinates.
     *
     * @param startLine   the one-based start line
     * @param startColumn the one-based start column
     * @param endLine     the one-based end line
     * @param endColumn   the one-based end column
     * @return the source text within the specified line/column range
     */
    public String substring(final int startLine, final int startColumn,
                            final int endLine, final int endColumn) {
        return tokenService.substring(startLine, startColumn, endLine, endColumn);
    }

    /**
     * Returns the source substring between two {@link SourceLocation.Position} objects.
     *
     * @param start the inclusive start position
     * @param end   the exclusive end position
     * @return the source text between {@code start} and {@code end}
     */
    public String substring(final SourceLocation.Position start,
                            final SourceLocation.Position end) {
        return tokenService.substring(start, end);
    }

    /**
     * Returns a source substring starting at the given line/column and spanning
     * the specified number of characters.
     *
     * @param line   the one-based line number of the start position
     * @param column the one-based column number of the start position
     * @param length the number of characters to extract
     * @return the source text of the requested length starting at {@code line}:{@code column}
     */
    public String substring(final int line,
                            final int column,
                            final int length) {
        return tokenService.substring(line, column, length);
    }

    /*
     *
     * POSITION
     *
     */

    /**
     * Converts a character offset into a {@link SourceLocation.Position} (line and column).
     *
     * @param index the zero-based character offset in the source
     * @return the corresponding {@link SourceLocation.Position}
     */
    public SourceLocation.Position getPosition(final int index) {
        return tokenService.getPosition(index);
    }

    /**
     * Converts a {@link SourceLocation.Position} (line and column) into a character offset.
     *
     * @param position the source position to convert
     * @return the zero-based character offset corresponding to {@code position}
     */
    public int getIndex(final SourceLocation.Position position) {
        return tokenService.getIndex(position);
    }

    /*
     *
     *
     *
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(AstResult.class.getSimpleName());
        result.append(" [tokens=");
        result.append(tokenService.tokenSize());
        result.append("]");
        return result.toString();
    }

}
