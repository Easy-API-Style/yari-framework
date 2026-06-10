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
 * Represents a CSS nth-pattern used in pseudo-class selectors such as
 * {@code :nth-child(An+B of selector)}, combining an expression (the An+B part)
 * with an optional {@code of} selector filter.
 */
@JsonPropertyOrder({"expression", "of", "sourceLocation"})
public class NthPattern implements PseudoSelector {

    private static final long serialVersionUID = 1L;

    /** The nth expression. */
    @JsonProperty("expression")
    private final CssNode expression;
    /** The optional selector filter. */
    @JsonProperty("of")
    private final CssSelector of;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a new {@code NthPattern} with the given expression and optional selector,
     * without source location information.
     *
     * @param expression the An+B expression node (e.g. {@code 2n+1})
     * @param of         the optional CSS selector following the {@code of} keyword, or {@code null}
     */
    public NthPattern(final CssNode expression,
                      final CssSelector of) {
        this(expression, of, null);
    }

    /**
     * Creates a new {@code NthPattern} with the given expression, optional selector,
     * and source location. This constructor is used for JSON deserialization.
     *
     * @param expression     the An+B expression node (e.g. {@code 2n+1})
     * @param of             the optional CSS selector following the {@code of} keyword, or {@code null}
     * @param sourceLocation the source location of this node in the original CSS source, or {@code null}
     */
    @JsonCreator
    public NthPattern(@JsonProperty("expression") final CssNode expression,
                      @JsonProperty("of") final CssSelector of,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.expression = expression;
        this.of = of;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the An+B expression node of this nth-pattern.
     *
     * @return the expression node, never {@code null}
     */
    public CssNode getExpression() {
        return expression;
    }

    /**
     * Returns the optional CSS selector that follows the {@code of} keyword in this nth-pattern.
     *
     * @return the {@code of} selector, or {@code null} if none was specified
     */
    public CssNode ofSelector() {
        return of;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(expression, of);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this nth-pattern.
     *
     * @param parent the parent {@link AstNode}, or {@code null} to clear the parent reference
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
     * Compares this nth-pattern to another {@link AstNode} for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code NthPattern} instances with equal expression and {@code of} selector
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
        final NthPattern other = (NthPattern) astNode;
        return Objects.equals(expression, other.expression)
                && Objects.equals(of, other.of);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(expression, of, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof NthPattern node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(NthPattern.class.getSimpleName());
        result.append(" [expression=");
        result.append(expression);
        result.append(", of=");
        result.append(of);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
