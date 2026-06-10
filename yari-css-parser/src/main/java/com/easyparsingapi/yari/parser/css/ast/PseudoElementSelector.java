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
 * Represents a CSS pseudo-element selector (e.g. {@code ::before}, {@code ::after}).
 * A pseudo-element is identified by its name and is written with a double colon (::) in CSS3.
 */
@JsonPropertyOrder({"name", "sourceLocation"})
public class PseudoElementSelector implements PseudoSelector {

    private static final long serialVersionUID = 1L;

    /** The pseudo-element name. */
    @JsonProperty("name")
    private final Identifier name;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a pseudo-element selector with the given name and without a source location.
     *
     * @param value the identifier representing the name of the pseudo-element
     */
    public PseudoElementSelector(final Identifier value) {
        this(value, null);
    }

    /**
     * Constructs a pseudo-element selector with the given name and source location.
     *
     * @param name           the identifier representing the name of the pseudo-element
     * @param sourceLocation the location in the original CSS source, or {@code null} if unknown
     */
    @JsonCreator
    public PseudoElementSelector(@JsonProperty("name") final Identifier name,
                                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the identifier representing the name of the pseudo-element.
     *
     * @return the name of the pseudo-element
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
     * Sets the parent node of this selector in the syntax tree.
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
     * Compares this selector to another {@link AstNode} for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code PseudoElementSelector} instances with equal names
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
        final PseudoElementSelector other = (PseudoElementSelector) astNode;
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
        if (object instanceof PseudoElementSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(PseudoElementSelector.class.getSimpleName());
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
