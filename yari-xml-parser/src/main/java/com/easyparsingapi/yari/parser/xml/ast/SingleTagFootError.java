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
 * Represents a parse error in the closing token of a self-closing XML tag.
 */
@JsonPropertyOrder({"name", "sourceLocation",
                    "tokens", "message"})
public class SingleTagFootError extends TagFoot implements XmlError {

    private static final long serialVersionUID = 1L;

    /** The error tokens. */
    @JsonProperty("tokens")
    private final List<AstToken> tokens;
    /** The error message. */
    @JsonProperty("message")
    private final String message;
    
    /**
     * Creates a SingleTagFootError with the given tokens, message and name, and no source location.
     *
     * @param tokens  the list of unexpected tokens that caused the error
     * @param message the error message
     * @param name    the tag name associated with this closing token
     */
    public SingleTagFootError(final List<AstToken> tokens,
                              final String message,
                              final TagName name) {
        this(tokens, message, name, null);
    }

    /**
     * Creates a SingleTagFootError with all fields.
     *
     * @param tokens         the list of unexpected tokens that caused the error
     * @param message        the error message
     * @param name           the tag name associated with this closing token
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public SingleTagFootError(@JsonProperty("tokens") final List<AstToken> tokens,
                              @JsonProperty("message") final String message,
                              @JsonProperty("name") final TagName name,
                              @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(name, sourceLocation);
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
    @JsonIgnore
    public String getUnexpected() {
        return "token[</]";
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
        if (!(obj instanceof SingleTagFootError)) {
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
     * Creates a SingleTagFootError from a single unexpected token, a message, a name, and a source location.
     *
     * @param message        the error message
     * @param token          the unexpected token that caused the error
     * @param name           the tag name associated with the closing token
     * @param sourceLocation the location of the error in the source document
     * @return a new {@code SingleTagFootError} instance
     */
    public static SingleTagFootError newInstance(final String message,
                                                 final Token token,
                                                 final TagName name,
                                                 final SourceLocation sourceLocation) {
        return new SingleTagFootError(List.of(XmlToken.toAstToken(token)),
                                      message,
                                      name,
                                      sourceLocation);
    }
     
 }