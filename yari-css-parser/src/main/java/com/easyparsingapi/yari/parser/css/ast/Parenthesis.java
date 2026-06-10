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

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * CSS node representing an expression enclosed in parentheses in the syntax tree.
 */
@JsonPropertyOrder({"operand", "sourceLocation"})
public class Parenthesis implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The parenthesized operand. */
    @JsonProperty("operand")
    private CssNode operand;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a Parenthesis node containing the specified operand, without a source location.
     *
     * @param operand the CSS node representing the expression enclosed in parentheses
     */
    public Parenthesis(final CssNode operand) {
        this(operand, null);
    }

    /**
     * Constructs a Parenthesis node containing the specified operand and its associated source location.
     *
     * @param operand        the CSS node representing the expression enclosed in parentheses
     * @param sourceLocation the location in the original source, may be {@code null}
     */
    @JsonCreator
    public Parenthesis(@JsonProperty("operand") final CssNode operand,
                       @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.operand = operand;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the CSS node contained within the parentheses.
     *
     * @return the operand encapsulated by this node
     */
    public CssNode getOperand() {
        return operand;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(operand);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the syntax tree.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
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

    /**
     * Compares this parenthesis node to another {@link AstNode} for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code Parenthesis} instances with equal operands
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
        final Parenthesis other = (Parenthesis) astNode;
        return Objects.equals(operand, other.operand);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(operand, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Parenthesis node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Parenthesis.class.getSimpleName());
        result.append(" [operand=");
        result.append(operand);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
