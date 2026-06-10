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
 * AST node representing a JavaScript object literal declaration, e.g. {@code { a: 1, b: 2 }}.
 * <p>
 * Holds an ordered list of property nodes that form the object's key-value pairs.
 * </p>
 */
@JsonPropertyOrder({"properties", "sourceLocation"})
public class ObjectDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The properties. */
    @JsonProperty("properties") 
    private final List<JavascriptNode> properties;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs an {@code ObjectDeclaration} without source location information.
     *
     * @param properties the list of property nodes; {@code null} is treated as empty
     */
    public ObjectDeclaration(final List<JavascriptNode> properties) {
        this(properties, null);
    }

    /**
     * Constructs an {@code ObjectDeclaration} with all components.
     *
     * @param properties     the list of property nodes; {@code null} is treated as empty
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public ObjectDeclaration(@JsonProperty("properties") final List<JavascriptNode> properties,
                             @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.properties = CollectionUtil.nullToEmpty(properties);
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(properties);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this node.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the number of properties in this object literal.
     *
     * @return the property count
     */
    public int size() {
        return properties.size();
    }

    /**
     * Returns whether this object literal contains at least one property.
     *
     * @return {@code true} if the property list is not empty
     */
    public boolean hasProperty() {
        return !properties.isEmpty();
    }

    /**
     * Returns the list of property nodes.
     *
     * @return the list of property nodes
     */
    public List<JavascriptNode> getProperties() {
        return properties;
    }

    /**
     * Returns the property at the specified index, or {@code null} if the index
     * is out of bounds.
     *
     * @param index the zero-based index of the property
     * @return the property node, or {@code null} if the index is out of range
     */
    public JavascriptNode getProperty(final int index) {
        JavascriptNode result = null;
        if (index < size()) {
            result = properties.get(index);
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
        return Objects.hash(properties, sourceLocation);
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
        final ObjectDeclaration other = (ObjectDeclaration) obj;
        return Objects.equals(properties, other.properties);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ObjectDeclaration.class.getSimpleName());
        result.append(" [properties=");
        result.append(size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }
    
}
