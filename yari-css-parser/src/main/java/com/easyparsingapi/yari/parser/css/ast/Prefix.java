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
 * CSS AST node representing a prefixed expression, composed of an operator and its operand.
 */
@JsonPropertyOrder({"operator", "operand", "sourceLocation"})
public class Prefix implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The prefix operator. */
    @JsonProperty("operator")
    private final Operator operator;
    /** The operand. */
    @JsonProperty("operand")
    private final CssNode operand;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a Prefix node without a source location.
     *
     * @param operator the prefix operator
     * @param operand  the CSS operand node to which the operator is applied
     */
    public Prefix(final Operator operator,
                  final CssNode operand) {
        this(operator, operand, null);
    }

    /**
     * Constructs a Prefix node with all its properties, used during JSON deserialization.
     *
     * @param operator       the prefix operator
     * @param operand        the CSS operand node to which the operator is applied
     * @param sourceLocation the location in the original source, may be {@code null}
     */
    @JsonCreator
    public Prefix(@JsonProperty("operator") final Operator operator,
                  @JsonProperty("operand") final CssNode operand,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.operator = operator;
        this.operand = operand;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the prefix operator of this node.
     *
     * @return the prefix operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the CSS operand node to which the operator is applied.
     *
     * @return the operand node
     */
    public CssNode getOperand() {
        return operand;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(operator, operand);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the AST.
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
     * Compares this prefix node to another {@link AstNode} for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code Prefix} instances with equal operator and operand
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
        final Prefix other = (Prefix) astNode;
        return Objects.equals(operator, other.operator)
                && Objects.equals(operand, other.operand);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(operator, operand, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Prefix node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Prefix.class.getSimpleName());
        result.append(" [operator=");
        result.append(operator);
        result.append(", operand=");
        result.append(operand);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
