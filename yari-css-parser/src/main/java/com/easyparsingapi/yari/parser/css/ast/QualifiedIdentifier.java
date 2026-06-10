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

import java.util.LinkedList;
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
 * CSS node representing a qualified identifier composed of an ordered sequence of simple identifiers.
 * <p>
 * A qualified identifier allows expressing names of the form {@code namespace|element} or
 * multi-part paths within a CSS stylesheet.
 * </p>
 */
@JsonPropertyOrder({"identifiers", "sourceLocation"})
public class QualifiedIdentifier implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The identifiers. */
    @JsonProperty("identifiers")
    private final LinkedList<Identifier> identifiers;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a qualified identifier from a list of identifiers without a source location.
     *
     * @param selectors the list of identifiers constituting the qualified identifier; may be {@code null}
     */
    public QualifiedIdentifier(final List<Identifier> selectors) {
        this(selectors, null);
    }

    /**
     * Creates a qualified identifier from a list of identifiers and a source location.
     *
     * @param identifiers    the list of identifiers constituting the qualified identifier; may be {@code null}
     * @param sourceLocation the location in the CSS source from which this identifier originates; may be {@code null}
     */
    @JsonCreator
    public QualifiedIdentifier(@JsonProperty("identifiers") final List<Identifier> identifiers,
                               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.identifiers = new LinkedList<>(CollectionUtil.nullToEmpty(identifiers));
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the number of identifiers composing this qualified identifier.
     *
     * @return the number of identifiers
     */
    public int size() {
        return getIdentifiers().size();
    }

    /**
     * Returns the ordered list of identifiers composing this qualified identifier.
     *
     * @return the list of identifiers, never {@code null}
     */
    public LinkedList<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * Returns the identifier at the given position, or {@code null} if the index is out of bounds.
     *
     * @param index the zero-based index of the desired identifier
     * @return the identifier at position {@code index}, or {@code null} if {@code index >= size()}
     */
    public Identifier getIdentifier(final int index) {
        Identifier result = null;
        if (index < size()) {
            result = identifiers.get(index);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(identifiers);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this qualified identifier in the syntax tree.
     *
     * @param parent the parent node; may be {@code null}
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
     * Compares this node to another {@link AstNode} for structural equality, ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code QualifiedIdentifier} instances with equal identifier lists
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
        final QualifiedIdentifier other = (QualifiedIdentifier) astNode;
        return Objects.equals(identifiers, other.identifiers);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(identifiers, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof QualifiedIdentifier node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(QualifiedIdentifier.class.getSimpleName());
        result.append(" [identifiers=");
        result.append(size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
