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
 * CSS AST node representing a list of CSS selectors separated by a comma.
 * <p>
 * A {@code ListSelector} groups several {@link CssSelector} and corresponds
 * to the CSS construct {@code selector1, selector2, ...}.
 * </p>
 */
@JsonPropertyOrder({"selectors", "sourceLocation"})
public class ListSelector implements CssSelector {

    private static final long serialVersionUID = 1L;

    /** The list of selectors. */
    @JsonProperty("selectors")
    private final List<CssSelector> selectors;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code ListSelector} with the given selector list and without a source location.
     *
     * @param selectors the list of CSS selectors; {@code null} is treated as an empty list
     */
    public ListSelector(final List<CssSelector> selectors) {
        this(selectors, null);
    }

    /**
     * Constructs a {@code ListSelector} with the given selector list and source location.
     *
     * @param selectors      the list of CSS selectors; {@code null} is treated as an empty list
     * @param sourceLocation the location in the original source, may be {@code null}
     */
    @JsonCreator
    public ListSelector(@JsonProperty("selectors") final List<CssSelector> selectors,
                        @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.selectors = CollectionUtil.nullToEmpty(selectors);
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the number of selectors contained in this list.
     *
     * @return the number of selectors
     */
    public int size() {
        return selectors.size();
    }

    /**
     * Returns the selector at the given index, or {@code null} if the index is out of bounds.
     *
     * @param index the index of the selector to retrieve (zero-based)
     * @return the {@link CssSelector} at the given index, or {@code null} if the index is greater than or equal to {@link #size()}
     */
    public CssSelector getSelector(final int index) {
        CssSelector result = null;
        if (index < size()) {
            result = selectors.get(index);
        }
        return result;
    }

    /**
     * Returns the immutable list of CSS selectors of this node.
     *
     * @return the list of {@link CssSelector}, never {@code null}
     */
    public List<CssSelector> getSelectors() {
        return selectors;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(selectors);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the AST.
     *
     * @param parent the parent node, may be {@code null}
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

    /** {@inheritDoc} */
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
        final ListSelector other = (ListSelector) astNode;
        return Objects.equals(selectors, other.selectors);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(selectors, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof ListSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ListSelector.class.getSimpleName());
        result.append(" [selectors=");
        result.append(selectors.size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
