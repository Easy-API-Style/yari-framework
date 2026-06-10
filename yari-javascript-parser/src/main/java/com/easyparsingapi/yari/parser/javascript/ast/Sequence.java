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
 * Represents a comma-separated sequence of expressions in the AST
 * (e.g. {@code a, b, c} as used in the comma operator or parenthesised sequences).
 */
@JsonPropertyOrder({"nodes", "sourceLocation"})
public class Sequence implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The nodes. */
    @JsonProperty("nodes")
    private final List<JavascriptNode> nodes;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code Sequence} without source-location information.
     *
     * @param nodes the list of expression nodes in the sequence
     */
    public Sequence(final List<JavascriptNode> nodes) {
        this(nodes, null);
    }

    /**
     * Constructs a {@code Sequence} with full source-location information.
     *
     * @param nodes          the list of expression nodes in the sequence
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Sequence(@JsonProperty("nodes") final List<JavascriptNode> nodes,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.nodes = nodes;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
       public List<AstNode> astChildren() {
           return AstNode.childrenAttributes(nodes);
       }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }
    
    /**
     * Sets the parent AST node of this sequence.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the number of expressions in this sequence.
     *
     * @return the element count
     */
    public int size() {
        return nodes.size();
    }

    /**
     * Returns the first expression in this sequence.
     *
     * @return the first {@link JavascriptNode}
     */
    public JavascriptNode getFirst() {
        return nodes.getFirst();
    }

    /**
     * Returns the last expression in this sequence.
     *
     * @return the last {@link JavascriptNode}
     */
    public JavascriptNode getLast() {
        return nodes.getLast();
    }

    /**
     * Returns the ordered list of expressions in this sequence.
     *
     * @return the list of {@link JavascriptNode} elements
     */
    public List<JavascriptNode> getNodes() {
        return nodes;
    }

    /**
     * Returns the expression at the given zero-based index, or {@code null} if out of range.
     *
     * @param index zero-based position of the desired element
     * @return the {@link JavascriptNode} at that index, or {@code null}
     */
    public JavascriptNode getNode(final int index) {
        JavascriptNode result = null;
        if (index < nodes.size()) {
            result = nodes.get(index);
        }
        return result;
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
        return Objects.hash(nodes, sourceLocation);
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
        final Sequence other = (Sequence) obj;
        return Objects.equals(nodes, other.nodes);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Sequence.class.getSimpleName());
        result.append(" [nodes=");
        result.append(nodes.size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
