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
 * Represents a CSS property declaration composed of a name (identifier) and a value node in the AST.
 */
@JsonPropertyOrder({"name", "value", "sourceLocation"})
public class Property implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The property name. */
    @JsonProperty("name")
    private final Identifier name;
    /** The property value. */
    @JsonProperty("value")
    private final CssNode value;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a {@code Property} with the given name and value, without source location information.
     *
     * @param name  the identifier representing the property name
     * @param value the CSS node representing the property value
     */
    public Property(final Identifier name,
                    final CssNode value) {
        this(name, value, null);
    }

    /**
     * Creates a {@code Property} with the given name, value, and source location.
     *
     * @param name           the identifier representing the property name
     * @param value          the CSS node representing the property value
     * @param sourceLocation the source location of this property in the original source, or {@code null}
     */
    @JsonCreator
    public Property(@JsonProperty("name") final Identifier name,
                    @JsonProperty("value") final CssNode value,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.value = value;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the identifier representing the name of this CSS property.
     *
     * @return the property name identifier
     */
    public Identifier getName() {
        return name;
    }

    /**
     * Returns the CSS node representing the value of this CSS property.
     *
     * @return the property value node
     */
    public CssNode getValue() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name, value);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this property.
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
     * Compares this property to another AST node for structural equality, ignoring source location.
     *
     * @param astNode the AST node to compare with
     * @return {@code true} if both nodes are {@code Property} instances with equal name and value
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
        final Property other = (Property) astNode;
        return Objects.equals(name, other.name)
                && Objects.equals(value, other.value);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(name, value, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Property node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Property.class.getSimpleName());
        result.append(" [name=");
        result.append(name);
        result.append(", value=");
        result.append(value);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
