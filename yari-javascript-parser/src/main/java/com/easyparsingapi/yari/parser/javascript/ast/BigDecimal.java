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

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing a BigDecimal literal in a JavaScript source file.
 * <p>
 * This node wraps a {@link Literal} that holds the textual representation of
 * the big-decimal value and optionally carries its {@link SourceLocation}
 * within the parsed source.
 * </p>
 */
@JsonPropertyOrder({"literal", "sourceLocation"})
public class BigDecimal implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The literal. */
    @JsonProperty("literal")
    private final Literal literal;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code BigDecimal} node with the given literal and no source location.
     *
     * @param literal the literal that holds the big-decimal value text; may be {@code null}
     */
    public BigDecimal(final Literal literal) {
        this(literal, null);
    }

    /**
     * Constructs a {@code BigDecimal} node with the given literal and source location.
     * <p>
     * This constructor is used by Jackson during JSON deserialization.
     * </p>
     *
     * @param literal        the literal that holds the big-decimal value text; may be {@code null}
     * @param sourceLocation the location of this node in the source file; may be {@code null}
     */
    @JsonCreator
    public BigDecimal(@JsonProperty("literal") final Literal literal,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.literal = literal;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(literal);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this node.
     *
     * @param parent the parent {@link AstNode}; may be {@code null}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns {@code true} if this node has a non-null literal (i.e. a label value).
     *
     * @return {@code true} if the literal is present, {@code false} otherwise
     */
    public boolean hasLabel() {
        return literal != null;
    }

    /**
     * Returns the literal that holds the big-decimal value text of this node.
     *
     * @return the {@link Literal}, or {@code null} if none was provided
     */
    public Literal getLiteral() {
        return literal;
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
        return Objects.hash(literal, sourceLocation);
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
    public boolean equalsNode(final AstNode obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BigDecimal other = (BigDecimal) obj;
        return Objects.equals(literal, other.literal);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(BigDecimal.class.getSimpleName());
        result.append(" [");
        if (literal != null) {
            result.append("literal=");
            result.append(literal);
        }
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
