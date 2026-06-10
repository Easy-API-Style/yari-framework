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

import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.location.SourceLocator;

/**
 * Represents any token with a token value and the 0-based index in the source.
 */
public class Token {

    private final int index;
    private final int length;
    private final Object value;
    private final SourceLocator sourceLocator;

    /**
     * Creates a token with an associated source locator.
     *
     * @param index         the 0-based starting index of the token in the source
     * @param length        the number of characters spanned by the token
     * @param value         the token value (e.g. a {@link Tokens.Fragment} or a typed literal)
     * @param sourceLocator the {@link SourceLocator} used to convert character offsets to line/column positions,
     *                      or {@code null} if source location is not needed
     */
    public Token(int index,
    		     int length,
    		     Object value,
    		     SourceLocator sourceLocator) {
        this.index = index;
        this.length = length;
        this.value = value;
        this.sourceLocator = sourceLocator;
    }

    /**
     * Creates a token without a source locator.
     *
     * @param index  the 0-based starting index of the token in the source.
     * @param length the length of the token.
     * @param value  the token value.
     */
    protected Token(int index,
    		        int length,
    		        Object value) {
    	this(index, length, value, null);
    }

    /**
     * Returns the length of the token.
     *
     * @return the number of characters spanned by this token in the source
     */
    public int length() {
        return length;
    }

    /**
     * Returns the index of the token in the original source.
     *
     * @return the 0-based starting character index of this token in the source
     */
    public int index() {
        return index;
    }

    /**
     * Returns the token value.
     *
     * @return the parsed value associated with this token (e.g. a {@link Tokens.Fragment} or a typed literal)
     */
    public Object value() {
        return value;
    }

    /**
     * Returns the source locator associated with this token, or {@code null} if none was provided.
     *
     * @return the {@link SourceLocator} instance, or {@code null}.
     */
    public SourceLocator sourceLocator() {
		return sourceLocator;
	}

    /**
     * Computes and returns the {@link SourceLocation} of this token based on its index and length.
     * Returns {@code null} if no source locator is associated with this token.
     *
     * @return the {@link SourceLocation} spanning this token, or {@code null} if no source locator is set.
     */
    public SourceLocation sourceLocation() {
        if (sourceLocator != null) {
            return new SourceLocation(sourceLocator.locate(index),
                                      sourceLocator.locate(index + length));
        }
        return null;
    }

    @Override
	public int hashCode() {
		return java.util.Objects.hash(index, length, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		Token other = (Token) obj;
		return index == other.index
				 && length == other.length
				 && java.util.Objects.equals(value, other.value);
	}

	/** Returns the string representation of the token value. */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /*
     *
     * STATIC
     *
     */
    /**
     * Extracts the tag from a token whose value is a {@link Fragment}, or returns {@code null}
     * if the token is {@code null} or its value is not a {@link Fragment}.
     *
     * @param token the token to inspect, may be {@code null}.
     * @return the tag of the fragment value, or {@code null}.
     */
    public static Object tag(Token token) {
        Object result = null;
        if (token != null && token.value() instanceof Fragment fragment) {
            result = fragment.tag();
        }
        return result;
    }

}
