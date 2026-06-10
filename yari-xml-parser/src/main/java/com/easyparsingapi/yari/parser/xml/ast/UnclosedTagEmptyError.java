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
 * Represents a parse error for an empty XML tag that is missing its self-closing {@code />} terminator.
 */
@JsonPropertyOrder({"name", "attributes",
                    "sourceLocation",
                    "tokens", "message"})
public class UnclosedTagEmptyError extends TagEmpty implements XmlError {

    private static final long serialVersionUID = 1L;

    /** The error tokens. */
    @JsonProperty("tokens")
    private final List<AstToken> tokens;
    /** The error message. */
    @JsonProperty("message")
    private final String message;

    /**
     * Creates an UnclosedTagEmptyError without a source location.
     *
     * @param tokens     the list of unexpected tokens that caused the error
     * @param message    the error message
     * @param name       the partially parsed tag name
     * @param attributes the partially parsed attributes
     */
    public UnclosedTagEmptyError(final List<AstToken> tokens,
                                 final String message,
                                 final TagName name,
                                 final List<TagAttribute> attributes) {
        this(tokens, message, name, attributes, null);
    }

    /**
     * Creates an UnclosedTagEmptyError with all fields.
     *
     * @param tokens         the list of unexpected tokens that caused the error
     * @param message        the error message
     * @param name           the partially parsed tag name
     * @param attributes     the partially parsed attributes
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public UnclosedTagEmptyError(@JsonProperty("tokens") final List<AstToken> tokens,
                                 @JsonProperty("message") final String message,
                                 @JsonProperty("name") final TagName name,
                                 @JsonProperty("attributes") final List<TagAttribute> attributes,
                                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(false, name, attributes, sourceLocation);
        this.tokens = tokens;
        this.message = message;
    }

    @Override
    public List<AstToken> getTokens() {
        return tokens;
    }

    @Override
    @JsonIgnore
    public String getEncountered() {
        return ">";
    }

    @Override
    @JsonIgnore
    public List<String> getExpected() {
        return List.of("token[/>]");
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
        if (!(obj instanceof UnclosedTagEmptyError)) {
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
        result.append(", name=");
        result.append(getName());
        result.append(", attributes=");
        result.append(getAttributes().size());
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
     * Creates an UnclosedTagEmptyError from a list of tokens, a name, attributes and a source location.
     *
     * @param message        the error message
     * @param tokens         the list of unexpected tokens that caused the error
     * @param name           the partially parsed tag name
     * @param attributes     the partially parsed attributes
     * @param sourceLocation the location of the error in the source document
     * @return a new {@code UnclosedTagEmptyError} instance
     */
    public static UnclosedTagEmptyError newInstance(final String message,
                                                    final List<Token> tokens,
                                                    final TagName name,
                                                    final List<TagAttribute> attributes,
                                                    final SourceLocation sourceLocation) {
        return new UnclosedTagEmptyError(XmlToken.toAstToken(tokens),
                                         message,
                                         name,
                                         attributes,
                                         sourceLocation);
    }
     
 }