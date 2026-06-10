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
package com.easyparsingapi.yari.parser.css.ast;

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstError;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstToken;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.error.ParseErrorDetail;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.css.parser.CssToken;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing a CSS parsing error, containing information
 * about the encountered tokens, expected/unexpected values, and the failure message.
 */
@JsonPropertyOrder({"tokens", "encountered",
                    "expected", "unexpected",
                    "failureMessage",
                    "sourceLocation" })
public class CssError implements CssNode, AstError {

    private static final long serialVersionUID = 1L;

    /** The error tokens. */
    @JsonProperty("tokens")
    private final List<AstToken> tokens;
    /** The input encountered at the error position. */
    @JsonProperty("encountered")
    private final String encountered;
    /** The list of expected tokens. */
    @JsonProperty("expected")
    private final List<String> expected;
    /** The unexpected input. */
    @JsonProperty("unexpected")
    private final String unexpected;
    /** The failure message. */
    @JsonProperty("failureMessage")
    private final String failureMessage;

    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;

    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code CssError} instance with all the details of the parsing error.
     *
     * @param tokens          list of AST tokens associated with the error
     * @param encountered     the value encountered at the time of the error
     * @param expected        the list of expected values at the time of the error
     * @param unexpected      the unexpected value that caused the error
     * @param failureMessage  the message describing the parsing failure
     * @param sourceLocation  the location in the CSS source where the error occurred
     */
    @JsonCreator
    public CssError(@JsonProperty("tokens") final List<AstToken> tokens,
                    @JsonProperty("encountered") String encountered,
                    @JsonProperty("expected") List<String> expected,
                    @JsonProperty("unexpected") String unexpected,
                    @JsonProperty("failureMessage") String failureMessage,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.tokens = CollectionUtil.nullToEmpty(tokens);
        this.encountered = encountered;
        this.expected = CollectionUtil.nullToEmpty(expected);
        this.unexpected = unexpected;
        this.failureMessage = failureMessage;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the list of child AST nodes. A {@code CssError} node has no structural children.
     *
     * @return an empty list
     */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes();
    }

    /**
     * Returns the parent node of this error node in the AST.
     *
     * @return the parent {@link AstNode}, or {@code null} if this is a root node
     */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this AST node in the syntax tree.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the list of AST tokens associated with this parsing error.
     *
     * @return a non-null list of {@link AstToken} elements involved in the error
     */
    @Override
    public List<AstToken> getTokens() {
        return tokens;
    }

    /**
     * Returns the input value that was encountered at the error position.
     *
     * @return the encountered value, or {@code null} if not available
     */
    @Override
    public String getEncountered() {
        return encountered;
    }

    /**
     * Returns the list of values that were expected at the error position.
     *
     * @return a non-null list of expected value descriptions
     */
    @Override
    public List<String> getExpected() {
        return expected;
    }

    /**
     * Returns the unexpected value that triggered the parsing error.
     *
     * @return the unexpected value, or {@code null} if not available
     */
    @Override
    public String getUnexpected() {
        return unexpected;
    }

    /**
     * Returns the human-readable message describing the parsing failure.
     *
     * @return the failure message, or {@code null} if not available
     */
    @Override
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * Returns the location in the CSS source where this error occurred.
     *
     * @return the source location, or {@code null} if not available
     */
    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    /**
     * Sets the location in the CSS source where this error occurred.
     *
     * @param sourceLocation the source location to assign, or {@code null}
     */
    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(encountered, expected, failureMessage,
                            /** The field. */
                            sourceLocation, tokens, unexpected);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof AstNode node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /**
     * Compares this error node to another AST node for structural equality,
     * considering all error fields including source location.
     *
     * @param astNode the node to compare with
     * @return {@code true} if both nodes have the same class and all error fields are equal
     */
    @Override
    public boolean equalsNode(final AstNode astNode) {
        if (this == astNode) {
            return true;
        }
        if (astNode == null) {
            return false;
        }
        if (getClass() != astNode.getClass()) {
            return false;
        }
        final CssError other = (CssError) astNode;
        return Objects.equals(encountered, other.encountered)
                && Objects.equals(expected, other.expected)
                && Objects.equals(failureMessage, other.failureMessage)
                && Objects.equals(sourceLocation, other.sourceLocation)
                && Objects.equals(tokens, other.tokens)
                && Objects.equals(unexpected, other.unexpected);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [\n   sourceLocation=");
        result.append(sourceLocation);
        result.append("\n   tokens=");
        result.append(String.join(" ", tokens.stream().map(v -> v.text()).toList()));
        result.append("\n   tokens=");
        result.append(tokens);
        result.append("\n   encountered=");
        result.append(encountered);
        result.append("\n   expected=");
        result.append(expected);
        result.append("\n   unexpected=");
        result.append(unexpected);
        result.append("\n   failureMessage=");
        result.append(failureMessage);
        result.append("\n]");
        return result.toString();
    }

    /*
     *
     * STATIC
     *
     */
    /**
     * Creates a new {@code CssError} instance from the details of a parsing error
     * and the location in the CSS source.
     *
     * @param parseErrorDetail the details of the parsing error (encountered value, expected values, unexpected value, message)
     * @param sourceLocation   the location in the CSS source where the error occurred
     * @param tokens           the list of raw tokens associated with the error
     * @return a new {@code CssError} instance corresponding to the parsing error
     */
    public static CssError newInstance(final ParseErrorDetail parseErrorDetail,
                                       final SourceLocation sourceLocation,
                                       final List<Token> tokens) {
        return new CssError(CssToken.toAstToken(tokens),
                            parseErrorDetail.getEncountered(),
                            parseErrorDetail.getExpected(),
                            parseErrorDetail.getUnexpected(),
                            parseErrorDetail.getFailureMessage(),
                            /** The field. */
                            sourceLocation);
    }

}
