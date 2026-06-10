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
 * Represents a field declaration inside a JavaScript class body, capturing whether
 * the field is static and/or private, its key expression, and its optional initializer value.
 */
@JsonPropertyOrder({"staticField", "privateField", "key", "value", "sourceLocation"})
public class ClassFieldDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The staticField. */
    @JsonProperty("staticField")
    private final boolean staticField;
    /** The privateField. */
    @JsonProperty("privateField")
    private final boolean privateField;
    /** The key. */
    @JsonProperty("key")
    private final JavascriptNode key;
    /** The value. */
    @JsonProperty("value")
    private final JavascriptNode value;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a {@code ClassFieldDeclaration} without a source location.
     *
     * @param staticField  {@code true} if the field is declared with the {@code static} modifier
     * @param privateField {@code true} if the field is declared as a private field (using {@code #} syntax)
     * @param key          the node representing the field name / key expression
     * @param value        the node representing the field initializer, or {@code null} if absent
     */
    public ClassFieldDeclaration(final boolean staticField,
                                 final boolean privateField,
                                 final JavascriptNode key,
                                 final JavascriptNode value) {
        this(staticField, privateField, key, value, null);
    }

    /**
     * Creates a {@code ClassFieldDeclaration} with all properties, including source location.
     * This constructor is used by Jackson for JSON deserialization.
     *
     * @param staticField    {@code true} if the field is declared with the {@code static} modifier
     * @param privateField   {@code true} if the field is declared as a private field (using {@code #} syntax)
     * @param key            the node representing the field name / key expression
     * @param value          the node representing the field initializer, or {@code null} if absent
     * @param sourceLocation the location of this node in the source code, or {@code null} if unknown
     */
    @JsonCreator
    public ClassFieldDeclaration(@JsonProperty("staticField") final boolean staticField,
                                 @JsonProperty("privateField") final boolean privateField,
                                 @JsonProperty("key") final JavascriptNode key,
                                 @JsonProperty("value") final JavascriptNode value,
                                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.staticField = staticField;
        this.privateField = privateField;
        this.key = key;
        this.value = value;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(key, value);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this field declaration.
     *
     * @param parent the parent node in the AST hierarchy
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns {@code true} if this field is declared with the {@code static} modifier.
     *
     * @return {@code true} for static fields, {@code false} otherwise
     */
    public boolean isStatic() {
        return staticField;
    }

    /**
     * Returns {@code true} if this field is declared as a private field (using {@code #} syntax).
     *
     * @return {@code true} for private fields, {@code false} otherwise
     */
    public boolean isPirvate() {
        return privateField;
    }

    /**
     * Returns the node representing the field name / key expression.
     *
     * @return the key node of this field declaration
     */
    public JavascriptNode getKey() {
        return key;
    }

    /**
     * Returns {@code true} if this field declaration has an initializer value.
     *
     * @return {@code true} if the field has an initializer, {@code false} otherwise
     */
    public boolean isInitialized() {
        return value != null;
    }

    /**
     * Returns the node representing the field initializer expression, or {@code null} if the field
     * has no initializer.
     *
     * @return the initializer value node, or {@code null}
     */
    public JavascriptNode getValue() {
        return value;
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
        return Objects.hash(key, privateField, staticField, value, sourceLocation);
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
        final ClassFieldDeclaration other = (ClassFieldDeclaration) obj;
        return Objects.equals(key, other.key)
                && privateField == other.privateField
                && staticField == other.staticField
                && Objects.equals(value, other.value);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ClassFieldDeclaration.class.getSimpleName());
        result.append(" [static=");
        result.append(staticField);
        result.append(", private=");
        result.append(privateField);
        result.append(", key=");
        result.append(key);
        if (isInitialized()) {
            result.append(", value=");
            result.append(value);
        }
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
