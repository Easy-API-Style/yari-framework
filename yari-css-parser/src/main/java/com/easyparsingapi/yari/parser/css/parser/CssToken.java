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
package com.easyparsingapi.yari.parser.css.parser;

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstToken;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a CSS token produced by the CSS parser, holding the raw text, its
 * syntactic tag and its location in the source file.
 */
@JsonPropertyOrder({"text", "tag", "sourceLocation"})
public class CssToken implements AstToken {

    /** The text. */
    @JsonProperty("text")
    private final String text;
    /** The tag. */
    @JsonProperty("tag")
    private final String tag;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private final SourceLocation sourceLocation;

    /**
     * Constructs a new {@code CssToken} with the given text, tag and source location.
     *
     * @param text           the raw textual content of the token
     * @param tag            the syntactic category (tag) of the token
     * @param sourceLocation the location of the token in the CSS source file
     */
    public CssToken(@JsonProperty("text") final String text,
                           @JsonProperty("tag") final String tag,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.text = text;
        this.tag = tag;
        this.sourceLocation = sourceLocation;
    }


    /**
     * Returns the raw textual content of this token.
     *
     * @return the token text
     */
    @Override
    public String text() {
        return text;
    }

    /**
     * Returns the syntactic category (tag) of this token.
     *
     * @return the token tag
     */
    @Override
    public String tag() {
        return tag;
    }

    /**
     * Returns the location of this token in the CSS source file.
     *
     * @return the source location
     */
    @Override
    public SourceLocation sourceLocation() {
        return sourceLocation;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(sourceLocation, tag, text);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CssToken other = (CssToken) obj;
        return Objects.equals(sourceLocation, other.sourceLocation)
                && Objects.equals(tag, other.tag)
                && Objects.equals(text, other.text);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(CssToken.class.getSimpleName());
        result.append(" [sourceLocation=");
        result.append(sourceLocation);
        result.append(", text=");
        result.append(text);
        result.append(", tag=");
        result.append(tag);
        result.append("]");
        return result.toString();
    }

    /*
     *
     * STATIC
     *
     */
    /**
     * Converts a list of generic {@link Token} objects into a list of {@link AstToken}
     * instances by wrapping each token's {@link Fragment} value into a {@code CssToken}.
     *
     * @param tokens the list of parser tokens to convert
     * @return a list of {@link AstToken} corresponding to the provided tokens
     */
    public static List<AstToken> toAstToken(final List<Token> tokens) {
        return tokens.stream()
                     .map(v -> (AstToken) new CssToken(((Fragment) v.value()).text(),
                                                              ((Fragment) v.value()).tag().toString(),
                                                              v.sourceLocation()))
                     .toList();
    }

}
