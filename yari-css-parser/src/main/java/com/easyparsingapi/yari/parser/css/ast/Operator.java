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
import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * CSS AST node representing an operator, defined by its textual symbol.
 *
 * <p>A CSS operator is typically a special character such as {@code ,}, {@code /},
 * {@code +}, etc., used as a separator or combinator in CSS values and selectors.</p>
 */
@JsonPropertyOrder({"symbol", "sourceLocation"})
public class Operator implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The operator symbol string. */
    @JsonProperty("symbol")
    private final String symbol;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an operator with the given symbol and without a source location.
     *
     * @param symbol the textual symbol of the operator; {@code null} is treated as an empty string
     */
    public Operator(final String symbol) {
        this(symbol, null);
    }

    /**
     * Constructs an operator with the given symbol and associated source location.
     *
     * @param symbol         the textual symbol of the operator; {@code null} is treated as an empty string
     * @param sourceLocation the location in the original CSS source, may be {@code null}
     */
    @JsonCreator
    public Operator(@JsonProperty("symbol") final String symbol,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.symbol = symbol != null ? symbol : "";
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the textual symbol of this operator.
     *
     * @return the symbol of the operator, never {@code null}
     */
    public String getSymbol() {
        return symbol;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes();
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this operator in the AST.
     *
     * @param parent the parent node, may be {@code null}
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
     * Compares this operator to another {@link AstNode} for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code Operator} instances with equal symbols
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
        final Operator other = (Operator) astNode;
        return Objects.equals(symbol, other.symbol);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(symbol, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Operator node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Operator.class.getSimpleName());
        result.append(" [symbol=");
        result.append("'");
        result.append(symbol);
        result.append("'");
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

    /**
     * Creates an empty operator (empty symbol) positioned at the given source location.
     *
     * @param position the position in the CSS source used as both start and end of the location
     * @return a new {@code Operator} with an empty symbol and a source location pointing to {@code position}
     */
    public static Operator empty(final Position position) {
        final Operator result = new Operator("");
        result.setSourceLocation(new SourceLocation(position, position));
        return result;
    }

}
