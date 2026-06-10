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
 * CSS node representing an ordered list of CSS parameters.
 *
 * <p>This node groups several {@link CssNode} children (the parameters) into a
 * sequence whose order is preserved. It is used, for example, to represent
 * the arguments of a CSS function or the values of a composite property.</p>
 */
@JsonPropertyOrder({"parameters", "sourceLocation"})
public class ListParameter implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The list of parameter nodes. */
    @JsonProperty("parameters")
    private final List<CssNode> parameters;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code ListParameter} with the provided parameter list and without
     * a source location.
     *
     * @param parameters list of CSS nodes constituting the parameters; may be
     *                   {@code null}, in which case an empty list is used
     */
    public ListParameter(final List<CssNode> parameters) {
        this(parameters, null);
    }

    /**
     * Constructs a {@code ListParameter} with the provided parameter list and source location.
     *
     * @param parameters     list of CSS nodes constituting the parameters; may be
     *                       {@code null}, in which case an empty list is used
     * @param sourceLocation location in the original source, or {@code null}
     */
    @JsonCreator
    public ListParameter(@JsonProperty("parameters") final List<CssNode> parameters,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.parameters = CollectionUtil.nullToEmpty(parameters);
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the number of parameters contained in this node.
     *
     * @return the number of parameters
     */
    public int size() {
        return parameters.size();
    }

    /**
     * Returns the parameter at the specified index, or {@code null} if the index is
     * out of bounds.
     *
     * @param index zero-based index of the parameter to retrieve
     * @return the corresponding {@link CssNode}, or {@code null} if {@code index >= size()}
     */
    public CssNode getParameter(final int index) {
        CssNode result = null;
        if (index < size()) {
            result = parameters.get(index);
        }
        return result;
    }

    /**
     * Returns the complete list of parameters of this node.
     *
     * @return unmodifiable list of {@link CssNode} parameters
     */
    public List<CssNode> getParameters() {
        return parameters;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(parameters);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this {@code ListParameter} in the syntax tree.
     *
     * @param parent the parent node, or {@code null} if this node is the root
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
        final ListParameter other = (ListParameter) astNode;
        return Objects.equals(parameters, other.parameters);
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
        return Objects.hash(parameters, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof ListParameter node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ListParameter.class.getSimpleName());
        result.append(" [parameters=");
        result.append(parameters.size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
