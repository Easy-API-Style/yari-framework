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
 * AST node representing a JavaScript increment expression ({@code ++} operator),
 * which can appear in prefix or postfix position.
 * Holds the operator position, the operator symbol, and the operand expression.
 */
@JsonPropertyOrder({"position", "operator", "operand", "sourceLocation"})
public class Increment implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The position. */
    @JsonProperty("position")
    private final Position position;
    /** The operator. */
    @JsonProperty("operator")
    private final Operator operator;
    /** The operand. */
    @JsonProperty("operand")
    private final JavascriptNode operand;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code Increment} node without source location information.
     *
     * @param position the position of the operator (prefix or postfix)
     * @param operator the {@code ++} operator symbol
     * @param operand  the expression being incremented
     */
    public Increment(final Position position,
                     final Operator operator,
                     final JavascriptNode operand) {
        this(position, operator, operand, null);
    }

    /**
     * Constructs an {@code Increment} node with full source location information.
     *
     * @param position       the position of the operator (prefix or postfix)
     * @param operator       the {@code ++} operator symbol
     * @param operand        the expression being incremented
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Increment(@JsonProperty("position") final Position position,
                     @JsonProperty("operator") final Operator operator,
                     @JsonProperty("operand") final JavascriptNode operand,
                     @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.position = position;
        this.operator = operator;
        this.operand = operand;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
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
     * Sets the parent node of this increment node in the AST.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the position (prefix or postfix) of the {@code ++} operator.
     *
     * @return the operator {@link Position}
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the {@code ++} operator symbol of this increment expression.
     *
     * @return the {@link Operator}
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the operand expression being incremented.
     *
     * @return the operand {@link JavascriptNode}
     */
    public JavascriptNode getOperand() {
        return operand;
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
        return Objects.hash(operand, operator, position, sourceLocation);
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
        final Increment other = (Increment) obj;
        return Objects.equals(operand, other.operand)
                && Objects.equals(operator, other.operator)
                && position == other.position;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Increment.class.getSimpleName());
        result.append(" [position=");
        result.append(position);
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
