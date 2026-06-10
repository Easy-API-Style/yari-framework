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
 * AST node representing a CSS selector prefixed by a unary operator (e.g. {@code :not(...)}, {@code ::before}).
 * <p>
 * A {@code PrefixSelector} is composed of an operator applied as a prefix and an operand selector.
 * </p>
 */
@JsonPropertyOrder({"operator", "operand", "sourceLocation"})
public class PrefixSelector implements CssSelector {

    private static final long serialVersionUID = 1L;

    /** The prefix operator. */
    @JsonProperty("operator")
    private final Operator operator;
    /** The selector. */
    @JsonProperty("operand")
    private final CssSelector selector;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code PrefixSelector} without source location information.
     *
     * @param operator the unary operator applied as a prefix
     * @param operand  the selector to which the operator is applied
     */
    public PrefixSelector(final Operator operator,
                          final CssSelector operand) {
        this(operator, operand, null);
    }

    /**
     * Constructs a {@code PrefixSelector} with all its properties, used during JSON deserialization.
     *
     * @param operator       the unary operator applied as a prefix
     * @param selector       the selector to which the operator is applied
     * @param sourceLocation the location in the original CSS source, may be {@code null}
     */
    @JsonCreator
    public PrefixSelector(@JsonProperty("operator") final Operator operator,
                          @JsonProperty("operand") final CssSelector selector,
                          @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.operator = operator;
        this.selector = selector;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the unary operator prefixing this selector.
     *
     * @return the operator of this node
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the operand selector to which the operator is applied.
     *
     * @return the operand selector
     */
    public CssSelector getSelector() {
        return selector;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(operator, selector);
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
     * Checks structural equality between this node and another AST node, regardless of source location.
     *
     * @param astNode the AST node to compare
     * @return {@code true} if both nodes have the same operator and the same operand selector
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
        final PrefixSelector other = (PrefixSelector) astNode;
        return Objects.equals(operator, other.operator)
                && Objects.equals(selector, other.selector);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(operator, selector, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof PrefixSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(PrefixSelector.class.getSimpleName());
        result.append(" [operator=");
        result.append(operator);
        result.append(", operand=");
        result.append(selector);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
