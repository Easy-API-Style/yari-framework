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
 * Represents a CSS ID selector (e.g. {@code #myId}) in the abstract syntax tree.
 *
 * <p>An ID selector targets a single HTML element by its unique {@code id} attribute value.
 * The actual identifier (without the leading {@code #}) is held by an {@link Identifier} node.
 */
@JsonPropertyOrder({"value", "sourceLocation"})
public class IdSelector implements CssSelector {

    private static final long serialVersionUID = 1L;

    /** The id identifier. */
    @JsonProperty("value")
    private final Identifier value;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates an {@code IdSelector} with the given identifier and no source location.
     *
     * @param value the identifier representing the ID name (without the leading {@code #})
     */
    public IdSelector(final Identifier value) {
        this(value, null);
    }

    /**
     * Creates an {@code IdSelector} with the given identifier and source location.
     *
     * @param value          the identifier representing the ID name (without the leading {@code #})
     * @param sourceLocation the location of this selector in the source, or {@code null} if unknown
     */
    @JsonCreator
    public IdSelector(@JsonProperty("value") final Identifier value,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.value = value;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the identifier that holds the ID name of this selector.
     *
     * @return the {@link Identifier} node for this ID selector
     */
    public Identifier getId() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(value);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this selector.
     *
     * @param parent the parent {@link AstNode}, or {@code null} if this node has no parent
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
     * Checks structural equality between this node and another {@link AstNode}, comparing only the
     * identifier value and ignoring source location information.
     *
     * @param astNode the node to compare with
     * @return {@code true} if {@code astNode} is an {@code IdSelector} with the same identifier value
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
        final IdSelector other = (IdSelector) astNode;
        return Objects.equals(value, other.value) ;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(value, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof IdSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(IdSelector.class.getSimpleName());
        result.append(" [value=");
        result.append(value);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
