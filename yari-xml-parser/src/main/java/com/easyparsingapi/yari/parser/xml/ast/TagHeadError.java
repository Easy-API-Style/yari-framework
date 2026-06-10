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
 * Represents a parse error that occurred while parsing the opening tag of an XML element.
 */
@JsonPropertyOrder({"name", "attributes", "sourceLocation",
                    "tokens", "message",
                    "encountered"})
public class TagHeadError extends TagHead implements XmlError {

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
     * Creates a TagHeadError without a source location.
     *
     * @param tokens      the list of unexpected tokens that caused the error
     * @param message     the error message
     * @param encountered the unexpected token text that was encountered
     * @param name        the partially parsed tag name, or {@code null} if unavailable
     * @param attributes  the partially parsed attributes, or {@code null} if unavailable
     */
    public TagHeadError(final List<AstToken> tokens,
                        final String message,
                        final String encountered,
                        final TagName name,
                        final List<TagAttribute> attributes) {
        this(tokens, message, encountered, name, attributes, null);
    }

    /**
     * Creates a TagHeadError with all fields.
     *
     * @param tokens         the list of unexpected tokens that caused the error
     * @param message        the error message
     * @param encountered    the unexpected token text that was encountered
     * @param name           the partially parsed tag name, or {@code null} if unavailable
     * @param attributes     the partially parsed attributes, or {@code null} if unavailable
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public TagHeadError(@JsonProperty("tokens") final List<AstToken> tokens,
                        @JsonProperty("message") final String message,
                        @JsonProperty("encountered") final String encountered,
                        @JsonProperty("name") final TagName name,
                        @JsonProperty("attributes") final List<TagAttribute> attributes,
                        @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(name, attributes, sourceLocation);
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
        return List.of("token[/>]", "token[>]");
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
        if (!(obj instanceof TagHeadError)) {
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
     * Creates a TagHeadError from a single unexpected token with no attributes.
     *
     * @param message the error message
     * @param token   the unexpected token that caused the error
     * @return a new {@code TagHeadError} instance
     */
    public static TagHeadError newInstance(final String message,
                                           final Token token) {
        return new TagHeadError(List.of(XmlToken.toAstToken(token)),
                                message,
                                token.toString(),
                                Markup.toTagName(token),
                                null,
                                token.sourceLocation());
    }

    /**
     * Creates a TagHeadError from a list of tokens and partially parsed attributes.
     *
     * @param message    the error message
     * @param tokens     the list of unexpected tokens that caused the error
     * @param attributes the partially parsed attribute list
     * @return a new {@code TagHeadError} instance
     */
    public static TagHeadError newInstance(final String message,
                                           final List<Token> tokens,
                                           final List<TagAttribute> attributes) {
        return new TagHeadError(XmlToken.toAstToken(tokens),
                                message,
                                tokens.getLast().toString(),
                                Markup.toTagName(tokens.getFirst()),
                                attributes,
                                new SourceLocation(tokens.getFirst().sourceLocation().start(),
                                                   tokens.getLast().sourceLocation().end()));
    }
     
 }