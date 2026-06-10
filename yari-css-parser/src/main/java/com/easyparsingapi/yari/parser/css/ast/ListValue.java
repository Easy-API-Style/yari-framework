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
 * CSS node representing a list of values separated by a given separator (comma or space).
 * This node is used to model CSS properties whose value is a sequence of terms,
 * such as {@code font-family: Arial, sans-serif} (comma separator) or
 * {@code margin: 10px 20px} (space separator).
 */
@JsonPropertyOrder({"separator", "values", "sourceLocation"})
public class ListValue implements CssNode {

    private static final long serialVersionUID = 1L;

    /**
     * Type of separator used between the values of the list.
     * {@code comma} indicates a comma-separated list, {@code space} a space-separated one.
     */
    public static enum Separator {
        /** Comma-separated list. */
        comma,
        /** Space-separated list. */
        space
    }

    /** The value separator. */
    @JsonProperty("separator")
    private final Separator separator;
    /** The list of values. */
    @JsonProperty("values")
    private final List<CssNode> values;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code ListValue} without a source location.
     *
     * @param separator the separator used between values
     * @param values    the list of CSS nodes composing this list
     */
    public ListValue(final Separator separator,
                     final List<CssNode> values) {
        this(separator, values, null);
    }

    /**
     * Constructs a {@code ListValue} with all its properties (used by Jackson during JSON deserialization).
     *
     * @param separator      the separator used between values
     * @param values         the list of CSS nodes composing this list
     * @param sourceLocation the location in the original CSS source, or {@code null} if unknown
     */
    @JsonCreator
    public ListValue(@JsonProperty("separator") final Separator separator,
                     @JsonProperty("values") final List<CssNode> values,
                     @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.separator = separator;
        this.values = CollectionUtil.nullToEmpty(values);
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the separator used between the values of this list.
     *
     * @return the separator ({@code comma} or {@code space})
     */
    public Separator getSeparator() {
        return separator;
    }

    /**
     * Returns the number of values contained in this list.
     *
     * @return the number of CSS nodes in the list
     */
    public int size() {
        return values.size();
    }

    /**
     * Returns the value at the given index, or {@code null} if the index is out of bounds.
     *
     * @param index the zero-based index of the value to retrieve
     * @return the CSS node at position {@code index}, or {@code null} if the index is greater than or equal to {@link #size()}
     */
    public CssNode getValue(final int index) {
        CssNode result = null;
        if (index < size()) {
            result = values.get(index);
        }
        return result;
    }

    /**
     * Returns the complete list of CSS values of this node.
     *
     * @return the immutable list of CSS nodes
     */
    public List<CssNode> getValues() {
        return values;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(separator, values);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the syntax tree.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
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
        final ListValue other = (ListValue) astNode;
        return Objects.equals(separator, other.separator)
                 && Objects.equals(values, other.values);
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
        return Objects.hash(separator, values, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof ListValue node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ListValue.class.getSimpleName());
        result.append(" [separator=");
        result.append(separator);
        result.append(", values=");
        result.append(values.size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

    /*
     *
     * STATIC
     *
     */

    /**
     * Creates a {@code ListValue} from a separator and a list of typed CSS nodes.
     *
     * @param separator the separator to use between values
     * @param nodes     the list of source CSS nodes (subtype of {@link CssNode})
     * @return a new {@code ListValue} containing the provided nodes
     */
    public static ListValue of(final Separator separator,
                               final List<? extends CssNode> nodes) {
        return new ListValue(separator, nodes.stream().map(v -> (CssNode) v).toList());
    }

}
