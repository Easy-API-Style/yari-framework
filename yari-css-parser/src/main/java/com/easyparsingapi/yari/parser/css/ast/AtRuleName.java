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
 * Represents the name part of a CSS at-rule (e.g. {@code media} in {@code @media}).
 * <p>
 * The name is stored as an {@link Identifier} node and is serialized under the
 * JSON property {@code "name"}.
 * </p>
 */
@JsonPropertyOrder({"name", "sourceLocation"})
public class AtRuleName implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The name. */
    @JsonProperty("name")
    private final Identifier name;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code AtRuleName} with no source location.
     *
     * @param name the identifier representing the at-rule name; must not be {@code null}
     */
    public AtRuleName(final Identifier name) {
        this(name, null);
    }

    /**
     * Constructs an {@code AtRuleName} with the given identifier and source location.
     * <p>
     * This constructor is used by Jackson for JSON deserialization.
     * </p>
     *
     * @param name           the identifier representing the at-rule name
     * @param sourceLocation the source location of this node in the original CSS source, or {@code null}
     */
    @JsonCreator
    public AtRuleName(@JsonProperty("name") final Identifier name,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the identifier that represents the at-rule name.
     *
     * @return the at-rule name identifier
     */
    public Identifier getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this node.
     *
     * @param parent the parent node to assign
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
     * Compares this node to another {@link AstNode} for structural equality,
     * considering only the {@code name} field and ignoring source location.
     *
     * @param astNode the node to compare with
     * @return {@code true} if both nodes have the same class and equal {@code name}; {@code false} otherwise
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
        final AtRuleName other = (AtRuleName) astNode;
        return Objects.equals(name, other.name) ;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(name, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof AtRuleName node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(AtRuleName.class.getSimpleName());
        result.append(" [name=");
        result.append(name);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
