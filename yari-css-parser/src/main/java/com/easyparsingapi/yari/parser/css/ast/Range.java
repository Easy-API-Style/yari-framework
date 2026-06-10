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
 * AST node representing a CSS range of the form {@code <beforeOperand> <beforeOperator> <operand> <afterOperator> <afterOperand>},
 * used in particular in media queries and dimension selectors.
 */
@JsonPropertyOrder({"beforeOperand", "beforeOperator",
                    "operand",
                    "afterOperand", "afterOperand",
                    "sourceLocation"})
public class Range implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The beforeOperand. */
    @JsonProperty("beforeOperand")
    private final Identifier beforeOperand;
    /** The beforeOperator. */
    @JsonProperty("beforeOperator")
    private final Operator beforeOperator;
    /** The operand. */
    @JsonProperty("operand")
    private final Identifier operand;
    /** The afterOperator. */
    @JsonProperty("afterOperator")
    private final Operator afterOperator;
    /** The afterOperand. */
    @JsonProperty("afterOperand")
    private final Identifier afterOperand;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code Range} node without source location information.
     *
     * @param beforeOperand  the identifier positioned before the left operator
     * @param beforeOperator the left comparison operator
     * @param operand        the central identifier of the range
     * @param afterOperator  the right comparison operator
     * @param afterOperand   the identifier positioned after the right operator
     */
    public Range(final Identifier beforeOperand,
                 final Operator beforeOperator,
                 final Identifier operand,
                 final Operator afterOperator,
                 final Identifier afterOperand) {
        this(beforeOperand, beforeOperator, operand, afterOperator, afterOperand, null);
    }

    /**
     * Constructs a {@code Range} node with all its components and its location in the source.
     *
     * @param beforeOperand  the identifier positioned before the left operator
     * @param beforeOperator the left comparison operator
     * @param operand        the central identifier of the range
     * @param afterOperator  the right comparison operator
     * @param afterOperand   the identifier positioned after the right operator
     * @param sourceLocation the location of this node in the source file, or {@code null}
     */
    @JsonCreator
    public Range(@JsonProperty("beforeOperand") final Identifier beforeOperand,
                 @JsonProperty("beforeOperator") final Operator beforeOperator,
                 @JsonProperty("operand") final Identifier operand,
                 @JsonProperty("afterOperator") final Operator afterOperator,
                 @JsonProperty("afterOperand") final Identifier afterOperand,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.beforeOperand = beforeOperand;
        this.beforeOperator = beforeOperator;
        this.operand = operand;
        this.afterOperator = afterOperator;
        this.afterOperand = afterOperand;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the comparison operator positioned to the left of the central operand.
     *
     * @return the left operator of the range
     */
    public Operator getOperatorBefore() {
        return beforeOperator;
    }

    /**
     * Returns the comparison operator positioned to the right of the central operand.
     *
     * @return the right operator of the range
     */
    public Operator getOperatorAfter() {
        return afterOperator;
    }


    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(beforeOperand, beforeOperator,
                                          operand,
                                          /** The field. */
                                          afterOperator, afterOperand);
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
     * Compares this range node to another {@link AstNode} for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code Range} instances with equal operands and operators
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
        final Range other = (Range) astNode;
        return Objects.equals(beforeOperand, other.beforeOperand)
                 && Objects.equals(afterOperator, other.afterOperator)
                 && Objects.equals(beforeOperator, other.beforeOperator)
                 && Objects.equals(operand, other.operand)
                 && Objects.equals(afterOperator, other.afterOperator)
                 && Objects.equals(afterOperand, other.afterOperand);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(beforeOperand, beforeOperator,
                            operand,
                            afterOperator, afterOperand,
                            getSourceLocation());
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Range node) {
            return equalsNode(node)
                      && Objects.equals(getSourceLocation(), node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Range.class.getSimpleName());
        result.append(" [beforeOperand=");
        result.append(beforeOperand);
        result.append(", beforeOperator=");
        result.append(beforeOperator);
        result.append(", operand=");
        result.append(operand);
        result.append(", afterOperator=");
        result.append(afterOperator);
        result.append(", afterOperand=");
        result.append(afterOperand);
        if (getSourceLocation() != null) {
            result.append(", sourceLocation=");
            result.append(getSourceLocation());
        }
        result.append("]");
        return result.toString();
    }

}
