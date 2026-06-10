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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.location.SourceLocator;
import com.google.common.collect.Range;

/**
 * Service providing utility operations on a list of tokens produced by a parser,
 * including token lookup by index or source position, and source substring extraction.
 */
public class TokenService {

    private final List<Token> tokens;
    private final SourceLocator sourceLocator;
    private final Map<Integer, Range<Integer>> tokenIndexes = new TreeMap<>();

    /**
     * Constructs a {@code TokenService} for the given token list.
     * Builds an internal index mapping each token's position in the list to its
     * character range in the source, enabling fast lookup by character index.
     *
     * @param tokens the ordered list of tokens produced by the parser; must not be {@code null}
     */
    public TokenService(final List<Token> tokens) {
    	super();
        this.tokens = tokens;
        this.sourceLocator = !tokens.isEmpty()
                                  ? tokens.get(0).sourceLocator()
                                  : null;
        int index = 0;
        for (final Token token : tokens) {
            this.tokenIndexes.put(index,
            		              Range.closed(token.index(),
            		                           token.index() + token.length() - 1));
            index++;
        }
    }

    /*
     *
     * TOKEN
     *
     */

    /**
     * Returns the total number of tokens held by this service.
     *
     * @return the number of tokens
     */
    public int tokenSize() {
        return tokens.size();
    }

    /**
     * Returns the full list of tokens held by this service.
     *
     * @return an unmodifiable view or the backing list of tokens
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Returns the token at the given position in the token list.
     *
     * @param tokenIndex the zero-based index of the token in the token list
     * @return the {@link Token} at the specified position
     */
    public Token getToken(final int tokenIndex) {
        return tokens.get(tokenIndex);
    }

    /**
     * Returns the position of the given token in the token list.
     *
     * @param token the token whose index is requested
     * @return the zero-based index of the token, or {@code -1} if not found
     */
    public int getTokenIndexOf(final Token token) {
        return tokens.indexOf(token);
    }

    /**
     * Returns the token that covers the given character index in the source,
     * or {@code null} if no token covers that position.
     *
     * @param index the zero-based character index in the source
     * @return the {@link Token} covering the position, or {@code null}
     */
    public Token getTokenAt(final int index) {
        final Integer tokenIndex = getTokenIndexAt(index);
        return tokenIndex != null ? tokens.get(tokenIndex) : null;
    }

    private Integer getTokenIndexAt(final int index) {
        Integer result = null;
        for (final Entry<Integer, Range<Integer>> entry : tokenIndexes.entrySet()) {
            if (entry.getValue().contains(index)) {
                result = entry.getKey();
                break;
            }
        }
        return result;
    }

    /*
     *
     * SUBSTRING
     *
     */
    private String toString(final CharSequence charSequence) {
        return charSequence != null ? charSequence.toString() : null;
    }

    /**
     * Returns the entire source text as a {@link String}.
     *
     * @return the full source text
     */
    public String getSource() {
        return toString(sourceLocator.source());
    }

    /**
     * Extracts the source substring delimited by the given {@link SourceLocation}.
     *
     * @param sourceLocation the source location whose start and end positions delimit the substring
     * @return the extracted substring
     */
    public String substring(final SourceLocation sourceLocation) {
        return substring(sourceLocation.start(), sourceLocation.end());
    }

    /**
     * Extracts the source substring between two character indexes (inclusive).
     *
     * @param startIndex the zero-based start character index (inclusive)
     * @param endIndex   the zero-based end character index (inclusive)
     * @return the extracted substring
     */
    public String substring(final int startIndex, final int endIndex) {
        return toString(sourceLocator.substring(startIndex, endIndex));
    }

    /**
     * Extracts the source substring between two line/column positions (inclusive).
     *
     * @param startLine   the one-based start line
     * @param startColumn the one-based start column
     * @param endLine     the one-based end line
     * @param endColumn   the one-based end column
     * @return the extracted substring
     */
    public String substring(final int startLine, final int startColumn,
                            final int endLine, final int endColumn) {
        return toString(sourceLocator.substring(startLine, startColumn, endLine, endColumn));
    }

    /**
     * Extracts the source substring between two {@link SourceLocation.Position} values (inclusive).
     *
     * @param start the start position
     * @param end   the end position
     * @return the extracted substring
     */
    public String substring(final SourceLocation.Position start,
                            final SourceLocation.Position end) {
        return toString(sourceLocator.substring(start, end));
    }

    /**
     * Extracts a source substring of the given length starting at the specified line and column.
     *
     * @param line   the one-based starting line
     * @param column the one-based starting column
     * @param length the number of characters to extract
     * @return the extracted substring
     */
    public String substring(final int line,
                            final int column,
                            final int length) {
        return toString(sourceLocator.substring(line, column, length));
    }

    /*
     *
     * POSITION
     *
     */

    /**
     * Converts a zero-based character index into a {@link SourceLocation.Position} (line/column).
     *
     * @param index the zero-based character index in the source
     * @return the corresponding {@link SourceLocation.Position}
     */
    public SourceLocation.Position getPosition(final int index) {
        return sourceLocator.locate(index);
    }

    /**
     * Converts a {@link SourceLocation.Position} (line/column) into a zero-based character index.
     *
     * @param position the source position to convert
     * @return the corresponding zero-based character index
     */
    public int getIndex(final SourceLocation.Position position) {
        return sourceLocator.locate(position);
    }

    /*
     *
     * NODE
     *
     */

    /**
     * Returns all tokens that overlap the source location of the given node.
     *
     * @param node the source-localisable node whose tokens are requested
     * @return an ordered list of tokens covering the node's source range
     */
    public List<Token> getTokensOf(final SourceLocalisable node) {
        final List<Token> result = new ArrayList<>();
        final Integer startIndex = sourceLocator.locate(node.getSourceLocation().start());
        final Integer endIndex = sourceLocator.locate(node.getSourceLocation().end());
        final Integer start = getTokenIndexAt(startIndex);
        final Integer end = getTokenIndexAt(endIndex - 1);
        for (int i = start; i <= end; i++) {
            result.add(getToken(i));
        }
        return result;
    }

    /*
     *
     *
     *
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(TokenService.class.getSimpleName());
        result.append(" [tokens=");
        result.append(tokens.size());
        result.append("]");
        return result.toString();
    }

}
