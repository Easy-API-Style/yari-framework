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
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing a JavaScript procedure block, that is, an ordered sequence
 * of JavaScript nodes forming the body of a block of instructions.
 */
@JsonPropertyOrder({"procedure", "sourceLocation"})
public class BlockProcedure implements JavascriptNode, JavascriptProcedure {

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
     * Constructs a {@code BlockProcedure} with the given list of nodes and no source location.
     *
     * @param nodes the list of JavaScript nodes composing the block; may be {@code null} (will be normalized to an empty list)
     */
    public BlockProcedure(final List<JavascriptNode> nodes) {
        this(nodes, null);
    }

    /**
     * Constructs a {@code BlockProcedure} with the given list of nodes and source location.
     *
     * @param nodes          the list of JavaScript nodes composing the block; may be {@code null} (will be normalized to an empty list)
     * @param sourceLocation the location in the original source, or {@code null} if unknown
     */
    @JsonCreator
    public BlockProcedure(@JsonProperty("nodes") final List<JavascriptNode> nodes,
                     @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.nodes = CollectionUtil.nullToEmpty(nodes);
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
     * Sets the parent node of this block in the AST.
     *
     * @param parent the parent node, or {@code null} if this node is the root
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /** {@inheritDoc} */
    @Override
    public List<JavascriptNode> getNodes() {
        return nodes;
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
        final BlockProcedure other = (BlockProcedure) obj;
        return Objects.equals(nodes, other.nodes);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName());
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
