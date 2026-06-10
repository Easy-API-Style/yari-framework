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
package com.easyparsingapi.yari.parser.javascript.ast;

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
 * AST node representing the declaration of a JavaScript array (array literal),
 * containing an ordered list of {@link JavascriptNode} elements.
 */
@JsonPropertyOrder({"values", "sourceLocation"})
public class ArrayDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The values. */
    @JsonProperty("values")
    private final List<JavascriptNode> values;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an array declaration with the specified values and no source location.
     *
     * @param values the list of array elements; if {@code null}, an empty list is used
     */
    public ArrayDeclaration(final List<JavascriptNode> values) {
        this(values, null);
    }

    /**
     * Constructs an array declaration with the specified values and source location.
     *
     * @param values         the list of array elements; if {@code null}, an empty list is used
     * @param sourceLocation the location of the array in the source, may be {@code null}
     */
    @JsonCreator
    public ArrayDeclaration(@JsonProperty("values") final List<JavascriptNode> values,
                            @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.values = CollectionUtil.nullToEmpty(values);
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
       public List<AstNode> astChildren() {
           return AstNode.childrenAttributes(values);
       }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this array declaration in the AST.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the number of elements contained in the array.
     *
     * @return the number of elements
     */
    public int size() {
        return values.size();
    }

    /**
     * Indicates whether the array contains at least one element.
     *
     * @return {@code true} if the array is not empty, {@code false} otherwise
     */
    public boolean hasValue() {
        return !values.isEmpty();
    }

    /**
     * Returns the list of all elements in the array.
     *
     * @return the immutable list of elements
     */
    public List<JavascriptNode> getValues() {
        return values;
    }

    /**
     * Returns the array element at the given index, or {@code null} if the index is out of bounds.
     *
     * @param index the zero-based index of the element to retrieve
     * @return the element at the specified index, or {@code null} if the index is greater than or equal to the array size
     */
    public JavascriptNode getValue(final int index) {
        JavascriptNode result = null;
        if (index < values.size()) {
            result = values.get(index);
        }
        return result;
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
        return Objects.hash(values, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof AstNode node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
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
        final ArrayDeclaration other = (ArrayDeclaration) astNode;
        return Objects.equals(values, other.values);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ArrayDeclaration.class.getSimpleName());
        result.append(" [values=");
        result.append(values.size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
