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
package com.easyparsingapi.yari.parsec.location;

import java.util.HashMap;
import java.util.Map;

import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;

/**
 * Locates the line and column number of a index in the source.
 *
 * <p>
 * It is <EM>not</EM> multi-thread safe.
 */
public class SourceLocator {

	 private final CharSequence source;
	 private final Map<Position, Integer> indexes = new HashMap<>();
	 private final Map<Integer, Position> sourcePositions = new HashMap<>();

    /**
     * Creates a {@link SourceLocator} object.
     *
     * @param source the source.
     */
	public SourceLocator(String source) {
		this(new StringBuffer(source));
	}

	/**
	 * Creates a {@link SourceLocator} from a {@link CharSequence} source and initialises the internal index mappings.
	 *
	 * @param source the character sequence to be indexed and located.
	 */
	public SourceLocator(CharSequence source) {
    	  super();
    	  this.source = source;
    	  this.initialize();
    }

    private void initialize() {
        int index = 0;
        int line = 1;
        int column = 1;
        for (final char c : source.toString().toCharArray()) {
            if (c == '\n') {
            	final Position sourcePostion = new Position(line, column);
				indexes.put(sourcePostion, index);
				sourcePositions.put(index, sourcePostion);
            	line++;
            	column = 0;
            }
            else {
				final Position sourcePostion = new Position(line, column);
				indexes.put(sourcePostion, index);
				sourcePositions.put(index, sourcePostion);
            }
            column++;
            index++;
        }
    }

    /**
     * Returns the underlying character sequence that this locator was built from.
     *
     * @return the source {@link CharSequence}.
     */
    public CharSequence source() {
    	return source;
    }

    /**
     * Returns the absolute index in the source that corresponds to the given {@link Position}.
     * If the exact position is not found, the method falls back to the preceding column on the same line.
     *
     * @param position the line/column position to look up.
     * @return the zero-based character index, or {@code null} if the position cannot be resolved.
     */
    public Integer locate(Position position) {
        Integer result = indexes.get(position);
        if (result == null) {
            result = indexes.get(new Position(position.line(), position.column() - 1));
        }
    	return result;
    }

    /**
     * Returns the {@link Position} (line and column) in the source that corresponds to the given absolute index.
     * If the index is beyond the end of the source, the position immediately after the last character is returned.
     * If the source is empty, position (1,1) is returned.
     *
     * @param index the zero-based character index in the source.
     * @return the corresponding {@link Position}, never {@code null}.
     */
    public Position locate(int index) {
    	final Position result;
		if (sourcePositions.isEmpty()) {
			result = new Position(1, 1);
		}
		else if (index >= source().length()) {
			final Position last = sourcePositions.get(source().length() - 1);
			result = new Position(last.line(), last.column() + 1);
		}
		else {
			result = sourcePositions.get(index);
		}
		return result;
    }

    /**
     * Returns the subsequence of the source delimited by the given absolute start and end indexes.
     * Returns an empty string when {@code startIndex} equals {@code endIndex} and is within bounds.
     *
     * @param startIndex the zero-based inclusive start index.
     * @param endIndex   the zero-based exclusive end index.
     * @return the corresponding {@link CharSequence} fragment.
     */
    public CharSequence substring(final int startIndex,
    		                      final int endIndex) {
        if (startIndex == endIndex
                && startIndex < source.length()) {
            return "";
        }
        return source.subSequence(startIndex, endIndex);
    }

    /**
     * Returns the subsequence of the source between the two given {@link Position}s.
     * If the end position is not found in the index, the substring extends to the end of the source.
     * Returns {@code null} if the start position cannot be resolved.
     *
     * @param start the inclusive start position.
     * @param end   the exclusive end position.
     * @return the corresponding {@link CharSequence} fragment, or {@code null} if {@code start} is unresolvable.
     */
    public CharSequence substring(final Position start,
    		                      final Position end) {
        final Integer startIndex = indexes.get(start);
        final Integer endIndex = indexes.get(end);
        final CharSequence result;
        if (startIndex != null && endIndex != null) {
        	result = substring(startIndex, endIndex);
        }
        else if (startIndex != null) {
        	result = substring(startIndex, source.length());
        }
        else {
        	result = null;
        }
        return result;
    }

    /**
     * Returns the subsequence of the source between the two positions expressed as individual line and column numbers.
     *
     * @param startLine   the line number of the start position (1-based).
     * @param startColumn the column number of the start position (1-based).
     * @param endLine     the line number of the end position (1-based).
     * @param endColumn   the column number of the end position (1-based).
     * @return the corresponding {@link CharSequence} fragment, or {@code null} if the start position is unresolvable.
     */
    public CharSequence substring(final int startLine, final int startColumn,
                                  final int endLine, final int endColumn) {
        return substring(new Position(startLine, startColumn),
                         new Position(endLine, endColumn));
    }

    /**
     * Returns a subsequence of the source starting at the given line and column, spanning the requested number of characters.
     * If the computed end index exceeds the source length, the subsequence extends to the end of the source.
     * Returns {@code null} if the start position is not present in the index.
     *
     * @param line   the line number of the start position (1-based).
     * @param column the column number of the start position (1-based).
     * @param length the number of characters to extract.
     * @return the corresponding {@link CharSequence} fragment, or {@code null} if the start position is unresolvable.
     */
    public CharSequence substring(final int line,
    		                      final int column,
    		                      final int length) {
        final Position startPosition = new Position(line, column);
        CharSequence result = null;
        if (indexes.containsKey(startPosition)) {
            final int start = indexes.get(startPosition);
            final int end = start + length;
            if (end < source.length()) {
                result = source.subSequence(start, end);
            }
            else {
                result = source.subSequence(start, source.length());
            }
        }
        return result;
    }

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(SourceLocator.class.getSimpleName());
		result.append(" [source=");
		result.append(source.length());
		result.append("]");
		return result.toString();
	}

}
