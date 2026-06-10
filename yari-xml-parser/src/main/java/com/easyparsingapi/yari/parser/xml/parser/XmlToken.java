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

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstToken;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST token for XML, wrapping a lexer token with its text.
 */
@JsonPropertyOrder({"text", "tag", "sourceLocation"})
public class XmlToken implements AstToken {

    @JsonProperty("text") 
    private final String text;
    @JsonProperty("tag") 
    private final String tag;
    @JsonProperty("sourceLocation") 
    private final SourceLocation sourceLocation;

    /**
     * Creates an XmlToken with the given text, tag name and source location.
     *
     * @param text           the raw token text
     * @param tag            the token tag name
     * @param sourceLocation the source location of this token
     */
    public XmlToken(@JsonProperty("text") final String text,
                    @JsonProperty("tag") final String tag,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.text = text;
        this.tag = tag;
        this.sourceLocation = sourceLocation;
    }
    
    @Override
    public String text() {
        return text;
    }

    @Override
    public String tag() {
        return tag;
    }

    @Override
    public SourceLocation sourceLocation() {
        return sourceLocation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceLocation, tag, text);
    }

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
        final XmlToken other = (XmlToken) obj;
        return Objects.equals(sourceLocation, other.sourceLocation)
                && Objects.equals(tag, other.tag)
                && Objects.equals(text, other.text);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(XmlToken.class.getSimpleName());
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
     * Converts a list of lexer tokens to a list of {@link AstToken}.
     *
     * @param tokens the list of lexer tokens to convert
     * @return a list of {@code AstToken} corresponding to the given tokens
     */
    public static List<AstToken> toAstToken(final List<Token> tokens) {
        return tokens.stream()
                     .map(v -> (AstToken) toAstToken(v))
                     .toList();
    }

    /**
     * Converts a single lexer token to an {@link XmlToken}.
     *
     * @param token the lexer token to convert
     * @return a new {@code XmlToken} wrapping the given token
     */
    public static XmlToken toAstToken(final Token token) {
        return new XmlToken(((Fragment) token.value()).text(), 
                            ((Fragment) token.value()).tag().toString(),
                            token.sourceLocation());
    }
    
}
