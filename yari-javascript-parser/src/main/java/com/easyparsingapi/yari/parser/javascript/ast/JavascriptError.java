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
package com.easyparsingapi.yari.parser.javascript.ast;

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstError;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstToken;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.error.ParseErrorDetail;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptToken;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node that represents a parse error encountered while processing JavaScript source code.
 * Implements both {@link JavascriptNode} and {@link AstError} so that error nodes can be
 * embedded in the AST alongside successfully parsed nodes.
 */
@JsonPropertyOrder({"tokens", "encountered",
                    "expected", "unexpected",
                    "failureMessage",
                    "sourceLocation" })
public class JavascriptError implements JavascriptNode, AstError {

    private static final long serialVersionUID = 1L; 
    
    /** The tokens. */
    @JsonProperty("tokens") 
    private final List<AstToken> tokens;
    /** The encountered. */
    @JsonProperty("encountered") 
    private final String encountered;
    /** The expected. */
    @JsonProperty("expected") 
    private final List<String> expected;
    /** The unexpected. */
    @JsonProperty("unexpected") 
    private final String unexpected;
    /** The failureMessage. */
    @JsonProperty("failureMessage") 
    private final String failureMessage;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code JavascriptError} node with full error detail.
     *
     * @param tokens         the tokens consumed up to the point of failure
     * @param encountered    a description of what was encountered at the failure point
     * @param expected       the list of token descriptions that were expected
     * @param unexpected     a description of the unexpected token, or {@code null}
     * @param failureMessage a human-readable summary of the parse failure
     * @param sourceLocation the source location at which the error occurred, or {@code null}
     */
    @JsonCreator
    public JavascriptError(@JsonProperty("tokens") final List<AstToken> tokens,
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
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes();
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this error node.
     *
     * @param parent the parent node to assign
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstToken> getTokens() {
        return tokens;
    }

    /** {@inheritDoc} */
    @Override
    public String getEncountered() {
        return encountered;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getExpected() {
        return expected;
    }

    /** {@inheritDoc} */
    @Override
    public String getUnexpected() {
        return unexpected;
    }

    /** {@inheritDoc} */
    @Override
    public String getFailureMessage() {
        return failureMessage;
    }

    /** {@inheritDoc} */
    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(encountered, expected, failureMessage, 
                            /** Field. */
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
    
    /** {@inheritDoc} */
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
        final JavascriptError other = (JavascriptError) astNode;
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
        result.append(JavascriptError.class.getSimpleName());
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
     * Factory method that creates a {@code JavascriptError} from a {@link ParseErrorDetail}.
     *
     * @param parseErrorDetail the error detail produced by the underlying parser
     * @param sourceLocation   the source location at which the error occurred, or {@code null}
     * @param tokens           the raw tokens consumed up to the failure point
     * @return a new {@code JavascriptError} instance populated with the given detail
     */
    public static JavascriptError newInstance(final ParseErrorDetail parseErrorDetail,
                                              final SourceLocation sourceLocation,
                                              final List<Token> tokens) {
        return new JavascriptError(JavascriptToken.toAstToken(tokens),
                                   parseErrorDetail.getEncountered(), 
                                   parseErrorDetail.getExpected(), 
                                   parseErrorDetail.getUnexpected(), 
                                   parseErrorDetail.getFailureMessage(),
                                   /** Field. */
                                   sourceLocation);
    }


}
