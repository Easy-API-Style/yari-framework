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
 * Represents a CSS pseudo-class selector (e.g. {@code :hover}, {@code :nth-child(2)}).
 * <p>
 * A pseudo-class selector is identified by a leading colon followed by an identifier
 * and targets elements based on their state or structural position in the document.
 * </p>
 */
@JsonPropertyOrder({"name", "sourceLocation"})
public class PseudoClassSelector implements PseudoSelector {

    private static final long serialVersionUID = 1L;

    /** The pseudo-class name. */
    @JsonProperty("name")
    private final Identifier name;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a {@code PseudoClassSelector} with the given identifier and no source location.
     *
     * @param value the identifier representing the pseudo-class name
     */
    public PseudoClassSelector(final Identifier value) {
        this(value, null);
    }

    /**
     * Creates a {@code PseudoClassSelector} with the given identifier and source location.
     * This constructor is used by Jackson for JSON deserialization.
     *
     * @param name           the identifier representing the pseudo-class name
     * @param sourceLocation the location in the source where this selector was parsed, may be {@code null}
     */
    @JsonCreator
    public PseudoClassSelector(@JsonProperty("name") final Identifier name,
                               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the identifier representing the pseudo-class name.
     *
     * @return the pseudo-class name identifier
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
     * Sets the parent AST node of this selector.
     *
     * @param parent the parent {@link AstNode} to assign
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
     * @return {@code true} if both nodes are {@code PseudoClassSelector} instances with equal names
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
        final PseudoClassSelector other = (PseudoClassSelector) astNode;
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
        if (object instanceof PseudoClassSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(PseudoClassSelector.class.getSimpleName());
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
