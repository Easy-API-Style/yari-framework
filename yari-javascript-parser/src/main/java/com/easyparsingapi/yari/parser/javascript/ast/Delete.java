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
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing the {@code delete} operator in JavaScript,
 * which removes a property from an object or an element from an array.
 */
@JsonPropertyOrder({"deletedValue", "sourceLocation"})
public class Delete implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The deletedValue. */
    @JsonProperty("deletedValue")
    private final JavascriptNode deletedValue;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code Delete} node with the value to delete and no source location.
     *
     * @param deletedValue the node representing the target expression of the {@code delete} operator
     */
    public Delete(final JavascriptNode deletedValue) {
        this(deletedValue, null);
    }

    /**
     * Constructs a {@code Delete} node with the value to delete and the associated source location.
     *
     * @param deletedValue     the node representing the target expression of the {@code delete} operator
     * @param sourceLocation   the location in the source code corresponding to this node, may be {@code null}
     */
    @JsonCreator
    public Delete(@JsonProperty("deletedValue") final JavascriptNode deletedValue,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.deletedValue = deletedValue;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(deletedValue);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the AST.
     *
     * @param parent the parent node to associate with this node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the target expression of the {@code delete} operator.
     *
     * @return the node representing the value to delete
     */
    public JavascriptNode getDeletedValue() {
        return deletedValue;
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
        return Objects.hash(deletedValue, sourceLocation);
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
    public boolean equalsNode(final AstNode obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Delete other = (Delete) obj;
        return Objects.equals(deletedValue, other.deletedValue);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Delete.class.getSimpleName());
        result.append(" [deletedValue=");
        result.append(deletedValue);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
