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
 * Represents a class field declaration in a JavaScript AST, consisting of a key
 * (the field name or computed expression) and an optional initialiser value.
 * Corresponds to constructs such as {@code fieldName = initialiserExpression} inside
 * a class body.
 */
@JsonPropertyOrder({"key", "value", "sourceLocation"})
public class FieldDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
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
     * Constructs a {@code FieldDeclaration} without source-location information.
     *
     * @param key   the field name or computed-property expression
     * @param value the initialiser expression, or {@code null} if absent
     */
    public FieldDeclaration(final JavascriptNode key,
                            final JavascriptNode value) {
        this(key, value, null);
    }

    /**
     * Constructs a {@code FieldDeclaration} with full source-location information.
     *
     * @param key            the field name or computed-property expression
     * @param value          the initialiser expression, or {@code null} if absent
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public FieldDeclaration(@JsonProperty("key") final JavascriptNode key,
                            @JsonProperty("value") final JavascriptNode value,
                            @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
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
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the field name or computed-property expression.
     *
     * @return the key node
     */
    public JavascriptNode getKey() {
        return key;
    }

    /**
     * Returns the initialiser expression, or {@code null} if no initialiser is present.
     *
     * @return the value node, or {@code null}
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
        return Objects.hash(key, value, sourceLocation);
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
        final FieldDeclaration other = (FieldDeclaration) obj;
        return Objects.equals(key, other.key)
                && Objects.equals(value, other.value);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(FieldDeclaration.class.getSimpleName());
        result.append(" [key=");
        result.append(key);
        result.append(", value=");
        result.append(value);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }
    
}