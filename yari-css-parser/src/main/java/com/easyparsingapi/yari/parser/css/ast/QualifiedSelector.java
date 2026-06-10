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
 * Represents a qualified CSS selector composed of an ordered list of simple selectors.
 *
 * <p>A qualified selector combines several {@link CssSelector} (for example a type selector
 * followed by class or attribute selectors) to form a more precise matching rule.</p>
 */
@JsonPropertyOrder({"selectors", "sourceLocation"})
public class QualifiedSelector implements CssSelector {

    private static final long serialVersionUID = 1L;

    /** The selectors. */
    @JsonProperty("selectors")
    private final List<CssSelector> selectors;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code QualifiedSelector} from a list of selectors, without a source location.
     *
     * @param selectors the list of selectors composing this qualified selector; may be {@code null}
     */
    public QualifiedSelector(final List<CssSelector> selectors) {
        this(selectors, null);
    }

    /**
     * Constructs a {@code QualifiedSelector} from a list of selectors and a source location.
     *
     * @param selectors      the list of selectors composing this qualified selector; may be {@code null}
     * @param sourceLocation the location in the source file; may be {@code null}
     */
    @JsonCreator
    public QualifiedSelector(@JsonProperty("selectors") final List<CssSelector> selectors,
                             @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.selectors = CollectionUtil.nullToEmpty(selectors);
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the number of simple selectors composing this qualified selector.
     *
     * @return the number of selectors
     */
    public int size() {
        return getSelectors().size();
    }

    /**
     * Returns the list of simple selectors composing this qualified selector.
     *
     * @return the list of {@link CssSelector}, never {@code null}
     */
    public List<CssSelector> getSelectors() {
        return selectors;
    }

    /**
     * Returns the simple selector at the given index, or {@code null} if the index is out of bounds.
     *
     * @param index the index of the desired selector (zero-based)
     * @return the {@link CssSelector} at that index, or {@code null} if the index is greater than or equal to {@link #size()}
     */
    public CssSelector getSelector(final int index) {
        CssSelector result = null;
        if (index < size()) {
            result = selectors.get(index);
        }
        return result;
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
     * Sets the parent node of this selector in the abstract syntax tree.
     *
     * @param parent the parent {@link AstNode}; may be {@code null}
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
     * Compares this node to another {@link AstNode} based solely on selectors,
     * regardless of source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are of the same type and have equal selector lists
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
        final QualifiedSelector other = (QualifiedSelector) astNode;
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
        if (object instanceof QualifiedSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(QualifiedSelector.class.getSimpleName());
        result.append(" [selectors=");
        result.append(size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
