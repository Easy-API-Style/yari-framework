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
 * Represents a computed value expression in the JavaScript AST, wrapping a {@link JavascriptNode}
 * expression whose value is evaluated at runtime (e.g. a computed property key {@code [expr]}).
 */
@JsonPropertyOrder({"expression", "sourceLocation"})
public class ComputedValue implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The expression. */
    @JsonProperty("expression")
    private final JavascriptNode expression;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a {@code ComputedValue} with the given expression and no source location.
     *
     * @param expression the JavaScript expression whose value is computed
     */
    public ComputedValue(final JavascriptNode expression) {
        this(expression, null);
    }

    /**
     * Creates a {@code ComputedValue} with the given expression and source location.
     *
     * @param expression     the JavaScript expression whose value is computed
     * @param sourceLocation the source location of this node in the original source code, may be {@code null}
     */
    @JsonCreator
    public ComputedValue(@JsonProperty("expression") final JavascriptNode expression,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.expression = expression;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
       public List<AstNode> astChildren() {
           return AstNode.childrenAttributes(expression);
       }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this node.
     *
     * @param parent the parent {@link AstNode} to assign to this node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the JavaScript expression wrapped by this computed value node.
     *
     * @return the inner {@link JavascriptNode} expression
     */
    public JavascriptNode getExpression() {
        return expression;
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
        return Objects.hash(expression, sourceLocation);
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
        final ComputedValue other = (ComputedValue) astNode;
        return Objects.equals(expression, other.expression);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ComputedValue.class.getSimpleName());
        result.append(" [expression=");
        result.append(expression);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
