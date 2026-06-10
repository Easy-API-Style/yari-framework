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
 * Represents a CSS combinator selector linking two selectors via an operator
 * (descendant, direct child, adjacent sibling, or general sibling).
 */
@JsonPropertyOrder({"leftSelector", "operator", "rightSelector", "type", "sourceLocation"})
public class CombinatorSelector implements CssSelector {

    private static final long serialVersionUID = 1L;

    /**
     * Enumeration of the supported CSS combinator types.
     */
    public static enum Type {
        /** Descendant combinator (whitespace). */
        descendant,
        /** Child combinator ({@code >}). */
        child,
        /** Adjacent sibling combinator ({@code +}). */
        adjacentSibling,
        /** General sibling combinator ({@code ~}). */
        generalSibling
    }

    /** The left selector. */
    @JsonProperty("leftSelector")
    private final CssSelector leftSelector;
    /** The combinator operator. */
    @JsonProperty("operator")
    private final Operator operator;
    /** The right selector. */
    @JsonProperty("rightSelector")
    private final CssSelector rightSelector;
    /** The combinator type. */
    @JsonProperty("type")
    private final Type type;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code CombinatorSelector} without source location information.
     *
     * @param leftSelector  the selector on the left side of the combinator
     * @param operator      the combinator operator (e.g. {@code >}, {@code +}, {@code ~} or space)
     * @param rightSelector the selector on the right side of the combinator
     */
    public CombinatorSelector(final CssSelector leftSelector,
                              final Operator operator,
                              final CssSelector rightSelector) {
        this(leftSelector, operator, rightSelector, null);
    }

    /**
     * Constructs a {@code CombinatorSelector} with all its attributes, used during
     * JSON deserialization.
     *
     * @param leftSelector   the selector on the left side of the combinator
     * @param operator       the combinator operator (e.g. {@code >}, {@code +}, {@code ~} or space)
     * @param rightSelector  the selector on the right side of the combinator
     * @param sourceLocation the location in the source file, may be {@code null}
     */
    @JsonCreator
    public CombinatorSelector(@JsonProperty("leftSelector") final CssSelector leftSelector,
                              @JsonProperty("operator") final Operator operator,
                              @JsonProperty("rightSelector") final CssSelector rightSelector,
                              @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.leftSelector = leftSelector;
        this.operator = operator;
        this.rightSelector = rightSelector;
        this.type = toType(operator);
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Indicates whether this combinator selector is of the specified type.
     *
     * @param type the type to check
     * @return {@code true} if this combinator matches the given type, {@code false} otherwise
     */
    public boolean is(final Type type) {
        return this.type == type;
    }

    /**
     * Returns the type of this combinator selector.
     *
     * @return the type of the combinator
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the selector on the left side of the combinator.
     *
     * @return the left selector
     */
    public CssSelector getLeftSelector() {
        return leftSelector;
    }

    /**
     * Returns the combinator operator.
     *
     * @return the combinator operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the selector on the right side of the combinator.
     *
     * @return the right selector
     */
    public CssSelector getRightSelector() {
        return rightSelector;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(leftSelector, operator, rightSelector);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this combinator selector.
     *
     * @param parent the parent node in the AST
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
     * Compares this combinator selector to another AST node for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare with
     * @return {@code true} if both nodes are of the same class and have equal operator,
     *         left selector, and right selector
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
        final CombinatorSelector other = (CombinatorSelector) astNode;
        return Objects.equals(operator, other.operator)
                 && Objects.equals(leftSelector, other.leftSelector)
                 && Objects.equals(rightSelector, other.rightSelector);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(operator, leftSelector, rightSelector, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof CombinatorSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(CombinatorSelector.class.getSimpleName());
        result.append(" [leftSelector=");
        result.append(leftSelector);
        result.append(", operator=");
        result.append(operator);
        result.append(", rightSelector=");
        result.append(rightSelector);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

    /*
     *
     * STATIC
     *
     */
    static Type toType(final Operator operator) {
        if (operator != null) {
            /** The result. */
            final Type result;
            if (">".equals(operator.getSymbol())) {
                result = Type.child;
            }
            else if ("+".equals(operator.getSymbol())) {
                result = Type.adjacentSibling;
            }
            else if ("~".equals(operator.getSymbol())) {
                result = Type.generalSibling;
            }
            else {
                result = Type.descendant;
            }
            return result;
        }
        return null;
    }

}
