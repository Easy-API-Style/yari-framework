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
package com.easyparsingapi.yari.parser.xml.ast;

import java.util.List;

import com.easyparsingapi.yari.core.ast.AstToken;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.xml.parser.XmlToken;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a parse error in an XML attribute.
 */
@JsonPropertyOrder({"name", "value", "sourceLocation",
                    "tokens", "message",
                    "encountered"})
public class TagAttributeError extends TagAttribute implements XmlError {

    private static final long serialVersionUID = 1L;

    /** The error tokens. */
    @JsonProperty("tokens")
    private final List<AstToken> tokens;
    /** The error message. */
    @JsonProperty("message")
    private final String message;
    /** The input encountered at the error position. */
    @JsonProperty("encountered")
    private final String encountered;
    
    /**
     * Creates a TagAttributeError without a source location.
     *
     * @param tokens      the list of unexpected tokens that caused the error
     * @param message     the error message
     * @param encountered the unexpected token text that was encountered
     * @param name        the attribute name, or {@code null} if unavailable
     * @param value       the attribute value, or {@code null} if unavailable
     */
    public TagAttributeError(final List<AstToken> tokens,
                             final String message,
                             final String encountered,
                             final Name name,
                             final Value value) {
        this(tokens, message, encountered, name, value, null);
    }

    /**
     * Creates a TagAttributeError with all fields.
     *
     * @param tokens         the list of unexpected tokens that caused the error
     * @param message        the error message
     * @param encountered    the unexpected token text that was encountered
     * @param name           the attribute name, or {@code null} if unavailable
     * @param value          the attribute value, or {@code null} if unavailable
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public TagAttributeError(@JsonProperty("tokens") final List<AstToken> tokens,
                             @JsonProperty("message") final String message,
                             @JsonProperty("encountered") final String encountered,
                             @JsonProperty("name") final Name name,
                             @JsonProperty("value") final Value value,
                             @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(name, value, sourceLocation);
        this.tokens = tokens;
        this.message = message;
        this.encountered = encountered;
    }

    @Override
    public List<AstToken> getTokens() {
        return tokens;
    }

    @Override
    public String getEncountered() {
        return encountered;
    }

    @Override
    @JsonIgnore
    public List<String> getExpected() {
        return getValue() != null 
                   ? List.of("token[=]") 
                   : List.of("attributeName");
    }

    @Override
    public String getUnexpected() {
        return null;
    }

    @Override
    public String getFailureMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof TagAttributeError)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder result  = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [message=");
        result.append(message);
        result.append(", tokens=");
        result.append(tokens);
        result.append(", encountered=");
        result.append(encountered);
        result.append(", name=");
        result.append(getName());
        result.append(", value=");
        result.append(getValue());
        if (getSourceLocation() != null) {
            result.append(", sourceLocation=");
            result.append(getSourceLocation());
        }
        result .append("]");
        return result .toString();
    }

    /*
     * 
     * STATIC
     * 
     */
    /**
     * Creates a TagAttributeError from a single unexpected token and a message, with no name or value.
     *
     * @param message the error message
     * @param token   the unexpected token that caused the error
     * @return a new {@code TagAttributeError} instance
     */
    public static TagAttributeError newInstance(final String message,
                                                final Token token) {
        return new TagAttributeError(List.of(XmlToken.toAstToken(token)),
                                     message,
                                     token.toString(),
                                     null,
                                     null,
                                     token.sourceLocation());
    }

    /**
     * Creates a TagAttributeError from a single unexpected token, a message, and a partial attribute value.
     *
     * @param message the error message
     * @param token   the unexpected token that caused the error
     * @param value   the partial attribute value parsed before the error
     * @return a new {@code TagAttributeError} instance
     */
    public static TagAttributeError newInstance(final String message,
                                                final Token token,
                                                final Value value) {
        return new TagAttributeError(List.of(XmlToken.toAstToken(token)),
                                     message,
                                     token.toString(),
                                     null,
                                     value,
                                     token.sourceLocation());
    }
     
 }