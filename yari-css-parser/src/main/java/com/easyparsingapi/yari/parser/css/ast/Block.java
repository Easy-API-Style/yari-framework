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
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a CSS block delimited by curly braces, containing an ordered list of child CSS nodes.
 */
@JsonPropertyOrder({"nodes", "sourceLocation"})
public class Block implements CssBlock {

    private static final long serialVersionUID = 1L;

    /** The block content nodes. */
    @JsonProperty("nodes")
    private final List<CssNode> nodes;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a CSS block with the provided list of child nodes and no source location.
     *
     * @param nodes the list of CSS nodes contained in this block; {@code null} is treated as an empty list
     */
    public Block(final List<CssNode> nodes) {
        this(nodes, null);
    }

    /**
     * Creates a CSS block with the provided list of child nodes and source location.
     *
     * @param nodes          the list of CSS nodes contained in this block; {@code null} is treated as an empty list
     * @param sourceLocation the location in the original source, or {@code null} if unknown
     */
    @JsonCreator
    public Block(@JsonProperty("nodes") final List<CssNode> nodes,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.nodes = CollectionUtil.nullToEmpty(nodes);
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<CssNode> getNodes() {
        return nodes;
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
     * Sets the parent node of this block in the syntax tree.
     *
     * @param parent the parent node to associate with this block
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
     * Compares this block to another AST node for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare with
     * @return {@code true} if both nodes are of the same class and contain equal node lists
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
        final Block other = (Block) astNode;
        return Objects.equals(nodes, other.nodes);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(nodes, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Block node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Block.class.getSimpleName());
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
