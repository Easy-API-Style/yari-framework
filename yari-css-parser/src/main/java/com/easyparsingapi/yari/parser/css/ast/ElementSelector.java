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
 * CSS AST node representing an HTML element selector (e.g. {@code div}, {@code p}, {@code span}).
 */
@JsonPropertyOrder({"name", "sourceLocation"})
public class ElementSelector implements CssSelector {

    private static final long serialVersionUID = 1L;

    /** The element name. */
    @JsonProperty("name")
    private final String name;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code ElementSelector} with the given element name and no source location.
     *
     * @param name the name of the HTML element targeted by this selector (e.g. {@code "div"})
     */
    public ElementSelector(final String name) {
        this(name, null);
    }

    /**
     * Constructs an {@code ElementSelector} with the given element name and source location.
     *
     * @param name           the name of the HTML element targeted by this selector (e.g. {@code "div"})
     * @param sourceLocation the position in the CSS source from which this selector was parsed, or {@code null}
     */
    @JsonCreator
    public ElementSelector(@JsonProperty("name") final String name,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the name of the HTML element targeted by this selector.
     *
     * @return the element name (e.g. {@code "div"}, {@code "p"})
     */
    public String getName() {
        return name;
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
     * Sets the parent node of this selector in the AST.
     *
     * @param parent the parent node to associate with this selector
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
     * Compares this selector with another AST node, ignoring the source location.
     * Two {@code ElementSelector} instances are considered equal if they have the same element name.
     *
     * @param astNode the AST node to compare
     * @return {@code true} if both nodes represent the same element selector
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
        final ElementSelector other = (ElementSelector) astNode;
        return Objects.equals(name, other.name);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(name, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof ElementSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ElementSelector.class.getSimpleName());
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
