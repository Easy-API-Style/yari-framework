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
 * AST node representing a JavaScript binary infix expression
 * (e.g. {@code a + b}, {@code x === y}).
 * Holds the left operand, the operator symbol, and the right operand.
 */
@JsonPropertyOrder({"leftOperand", "operator", "rightOperand", "sourceLocation"})
public class Infix implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The leftOperand. */
    @JsonProperty("leftOperand")
    private final JavascriptNode leftOperand;
    /** The operator. */
    @JsonProperty("operator")
    private final Operator operator;
    /** The rightOperand. */
    @JsonProperty("rightOperand")
    private final JavascriptNode rightOperand;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Constructs an {@code Infix} node without source location information.
     *
     * @param leftOperand  the left-hand side expression
     * @param operator     the binary operator symbol
     * @param rightOperand the right-hand side expression
     */
    public Infix(final JavascriptNode leftOperand,
                 final Operator operator,
                 final JavascriptNode rightOperand) {
        this(leftOperand, operator, rightOperand, null);
    }

    /**
     * Constructs an {@code Infix} node with full source location information.
     *
     * @param leftOperand    the left-hand side expression
     * @param operator       the binary operator symbol
     * @param rightOperand   the right-hand side expression
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Infix(@JsonProperty("leftOperand") final JavascriptNode leftOperand,
                 @JsonProperty("operator") final Operator operator,
                 @JsonProperty("rightOperand") final JavascriptNode rightOperand,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(leftOperand, operator, rightOperand);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this infix expression in the AST.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the left-hand side operand of this binary expression.
     *
     * @return the left operand {@link JavascriptNode}
     */
    public JavascriptNode getLeftOperand() {
        return leftOperand;
    }

    /**
     * Returns the binary operator symbol of this infix expression.
     *
     * @return the {@link Operator}
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the right-hand side operand of this binary expression.
     *
     * @return the right operand {@link JavascriptNode}
     */
    public JavascriptNode getRightOperand() {
        return rightOperand;
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
        return Objects.hash(leftOperand, operator, rightOperand, sourceLocation);
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
        final Infix other = (Infix) obj;
        return Objects.equals(leftOperand, other.leftOperand)
                && Objects.equals(operator, other.operator)
                && Objects.equals(rightOperand, other.rightOperand);
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
