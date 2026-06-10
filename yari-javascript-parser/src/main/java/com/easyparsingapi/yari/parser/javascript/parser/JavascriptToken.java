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

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstToken;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * An immutable token produced by the JavaScript lexer, carrying its raw text,
 * tag name, and source location.  Implements {@link AstToken} so it can be
 * included in the AST token list.
 */
@JsonPropertyOrder({"text", "tag", "sourceLocation"})
public class JavascriptToken implements AstToken {

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
     * Creates a new {@code JavascriptToken}.
     *
     * @param text           the raw text of the token as it appears in the source
     * @param tag            the string representation of the lexer tag (e.g. {@code "KEYWORD"})
     * @param sourceLocation the source location of this token
     */
    public JavascriptToken(@JsonProperty("text") final String text,
                           @JsonProperty("tag") final String tag,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.text = text;
        this.tag = tag;
        this.sourceLocation = sourceLocation;
    }

    
    /**
     * Returns the raw text of this token as it appears in the source.
     *
     * @return the token text
     */
    @Override
    public String text() {
        return text;
    }

    /**
     * Returns the string name of the lexer tag that categorises this token.
     *
     * @return the tag name string
     */
    @Override
    public String tag() {
        return tag;
    }

    /**
     * Returns the source location of this token.
     *
     * @return the {@link SourceLocation}
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final JavascriptToken other = (JavascriptToken) obj;
        return Objects.equals(sourceLocation, other.sourceLocation)
                && Objects.equals(tag, other.tag)
                && Objects.equals(text, other.text);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(JavascriptToken.class.getSimpleName());
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
     * Converts a list of raw {@link Token} objects produced by the lexer into
     * a list of {@link AstToken} instances suitable for inclusion in the AST.
     *
     * @param tokens the raw token list from the lexer
     * @return the corresponding list of {@link AstToken} instances
     */
    public static List<AstToken> toAstToken(final List<Token> tokens) {
        return tokens.stream()
                     .map(v -> (AstToken) new JavascriptToken(((Fragment) v.value()).text(), 
                                                              ((Fragment) v.value()).tag().toString(),
                                                              v.sourceLocation()))
                     .toList();
    }
    
}
