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
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstToken;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.xml.parser.XmlToken;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a generic parse error associated with one or more unexpected tokens.
 */
@JsonPropertyOrder({"tokens",
                    "message", "encountered",
                    "sourceLocation"})
public class TokenError implements XmlError, XmlNode {

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
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a TokenError without a source location.
     *
     * @param tokens      the list of unexpected tokens that caused the error
     * @param message     the error message
     * @param encountered the unexpected token text that was encountered
     */
    public TokenError(final List<AstToken> tokens,
                      final String message,
                      final String encountered) {
        this(tokens, message, encountered, null);
    }

    /**
     * Creates a TokenError with all fields.
     *
     * @param tokens         the list of unexpected tokens that caused the error
     * @param message        the error message
     * @param encountered    the unexpected token text that was encountered
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public TokenError(@JsonProperty("tokens") final List<AstToken> tokens,
                      @JsonProperty("message") final String message,
                      @JsonProperty("encountered") final String encountered,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.tokens = tokens;
        this.message = message;
        this.encountered = encountered;
        this.sourceLocation = sourceLocation;
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
    public List<String> getExpected() {
        return null;
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
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes();
    }

    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this error node in the AST.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, parent, sourceLocation, tokens);
    }

    @Override
    public boolean equalsNode(final AstNode astNode) {
        if (this == astNode) {
            return true;
        }
        if (!(astNode instanceof TokenError)) {
            return false;
        }
        final TokenError other = (TokenError) astNode;
        return Objects.equals(message, other.message)
                 && Objects.equals(sourceLocation, other.sourceLocation) 
                 && Objects.equals(tokens, other.tokens);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof TokenError node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
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
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
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
     * Creates a TokenError from a single unexpected token.
     *
     * @param message the error message
     * @param token   the unexpected token that caused the error
     * @return a new {@code TokenError} instance
     */
    public static TokenError newInstance(final String message,
                                         final Token token) {
        return new TokenError(List.of(XmlToken.toAstToken(token)),
                              message,
                              token.toString(),
                              token.sourceLocation());
    }
    
}
