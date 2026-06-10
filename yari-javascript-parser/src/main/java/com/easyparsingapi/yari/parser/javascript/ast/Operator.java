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
 * AST node representing a JavaScript operator (e.g. {@code +}, {@code ===}, {@code typeof}).
 * <p>
 * An operator is a leaf node identified solely by its symbol string. Use the
 * factory method {@link #symbol(String)} to create instances.
 * </p>
 */
@JsonPropertyOrder({"symbol", "sourceLocation"})
public class Operator implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The symbol. */
    @JsonProperty("symbol") 
    private final String symbol;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code Operator} without source location information.
     *
     * @param symbol the textual representation of the operator (e.g. {@code "+"})
     */
    public Operator(final String symbol) {
        this(symbol, null);
    }

    @JsonCreator
    private Operator(@JsonProperty("symbol") final String symbol,
                     @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.symbol = symbol;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
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
     * Sets the parent AST node of this node.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the textual symbol of this operator.
     *
     * @return the operator symbol string
     */
    public String getSymbol() {
        return symbol;
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
        return Objects.hash(symbol, sourceLocation);
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
        final Operator other = (Operator) obj;
        return Objects.equals(symbol, other.symbol);
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
     * Factory method that creates an {@code Operator} node for the given symbol.
     *
     * @param symbol the textual representation of the operator (e.g. {@code "&&"})
     * @return a new {@code Operator} node with the specified symbol
     */
    public static Operator symbol(final String symbol) {
        return new Operator(symbol);
    }

}
