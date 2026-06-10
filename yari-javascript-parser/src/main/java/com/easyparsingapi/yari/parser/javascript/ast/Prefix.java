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
 * Represents a prefix unary expression in the AST (e.g. {@code -x}, {@code !x}, {@code ~x}).
 * Combines a prefix {@link Operator} with its operand expression.
 */
@JsonPropertyOrder({"operator", "operand", "sourceLocation"})
public class Prefix implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
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
     * Constructs a {@code Prefix} without source-location information.
     *
     * @param operator the prefix operator
     * @param operand  the operand expression
     */
    public Prefix(final Operator operator,
                  final JavascriptNode operand) {
        this(operator, operand, null);
    }

    /**
     * Constructs a {@code Prefix} with full source-location information.
     *
     * @param operator       the prefix operator
     * @param operand        the operand expression
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Prefix(@JsonProperty("operator") final Operator operator,
                  @JsonProperty("operand") final JavascriptNode operand,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
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
     * Sets the parent AST node of this prefix expression.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the prefix operator of this expression.
     *
     * @return the {@link Operator}
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the operand of this prefix expression.
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
        return Objects.hash(operand, operator, sourceLocation);
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
        final Prefix other = (Prefix) astNode;
        return Objects.equals(operand, other.operand) 
                 && Objects.equals(operator, other.operator);
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
