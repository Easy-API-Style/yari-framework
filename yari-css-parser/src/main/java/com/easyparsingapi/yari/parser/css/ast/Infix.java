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
 * CSS AST node representing an infix expression composed of a left operand,
 * a binary operator, and a right operand (e.g. {@code a + b} or {@code x * y}).
 */
@JsonPropertyOrder({"leftOperand", "operator", "rightOperand", "sourceLocation"})
public class Infix implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The left operand. */
    @JsonProperty("leftOperand")
    private final CssNode leftOperand;
    /** The infix operator. */
    @JsonProperty("operator")
    private final Operator operator;
    /** The right operand. */
    @JsonProperty("rightOperand")
    private final CssNode rightOperand;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code Infix} node without source location information.
     *
     * @param leftOperand  the left operand of the expression
     * @param operator     the binary operator linking the two operands
     * @param rightOperand the right operand of the expression
     */
    public Infix(final CssNode leftOperand,
                 final Operator operator,
                 final CssNode rightOperand) {
        this(leftOperand, operator, rightOperand, null);
    }

    /**
     * Constructs an {@code Infix} node with all its components, including the source location.
     *
     * @param leftOperand    the left operand of the expression
     * @param operator       the binary operator linking the two operands
     * @param rightOperand   the right operand of the expression
     * @param sourceLocation the position of this node in the CSS source, may be {@code null}
     */
    @JsonCreator
    public Infix(@JsonProperty("leftOperand") final CssNode leftOperand,
                 @JsonProperty("operator") final Operator operator,
                 @JsonProperty("rightOperand") final CssNode rightOperand,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the left operand of the infix expression.
     *
     * @return the left operand
     */
    public CssNode getLeftOperand() {
        return leftOperand;
    }

    /**
     * Returns the binary operator of the infix expression.
     *
     * @return the operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the right operand of the infix expression.
     *
     * @return the right operand
     */
    public CssNode getRightOperand() {
        return rightOperand;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(leftOperand, operator, rightOperand, sourceLocation);
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
     * Checks structural equality between this node and another {@link AstNode},
     * comparing the operator and both operands without considering the source location.
     *
     * @param astNode the node to compare
     * @return {@code true} if both nodes are structurally equivalent
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
        final Infix other = (Infix) astNode;
        return Objects.equals(operator, other.operator)
                && Objects.equals(leftOperand, other.leftOperand)
                && Objects.equals(rightOperand, other.rightOperand);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(operator, leftOperand, rightOperand, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Infix node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Infix.class.getSimpleName());
        result.append(" [leftOperand=");
        result.append(leftOperand);
        result.append(", operator=");
        result.append(operator);
        result.append(", rightOperand=");
        result.append(rightOperand);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
