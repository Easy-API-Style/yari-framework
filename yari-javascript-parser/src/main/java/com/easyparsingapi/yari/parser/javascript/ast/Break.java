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
 * AST node representing a {@code break} statement in JavaScript,
 * optionally associated with a target label.
 */
@JsonPropertyOrder({"label", "sourceLocation"})
public class Break implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The label. */
    @JsonProperty("label")
    private final Identifier label;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a {@code break} node with no source location.
     *
     * @param label the target label of the {@code break} statement, or {@code null} if absent
     */
    public Break(final Identifier label) {
        this(label, null);
    }

    /**
     * Creates a {@code break} node with an optional label and a source location.
     *
     * @param label          the target label of the {@code break} statement, or {@code null} if absent
     * @param sourceLocation the location of the node in the source, or {@code null} if unknown
     */
    @JsonCreator
    public Break(@JsonProperty("label") final Identifier label,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.label = label;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(label);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the AST.
     *
     * @param parent the parent node, or {@code null} if this node is the root
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Indicates whether the {@code break} statement has a target label.
     *
     * @return {@code true} if a label is associated, {@code false} otherwise
     */
    public boolean hasLabel() {
        return label != null;
    }

    /**
     * Returns the target label of the {@code break} statement.
     *
     * @return the label, or {@code null} if no label is associated
     */
    public Identifier getLabel() {
        return label;
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
        return Objects.hash(label, sourceLocation);
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
        final Break other = (Break) obj;
        return Objects.equals(label, other.label);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Break.class.getSimpleName());
        result.append(" [");
        if (label != null) {
            result.append("label=");
            result.append(label);
        }
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
