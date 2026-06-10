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
 * AST node representing a function parameter with a default value,
 * e.g. {@code x = 42} in {@code function foo(x = 42) {}}.
 * <p>
 * Holds the parameter identifier and the expression that provides its default value.
 * </p>
 */
@JsonPropertyOrder({"identifier", "defaultValue", "sourceLocation"})
public class ParameterWithValue implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The identifier. */
    @JsonProperty("identifier") 
    private final JavascriptNode identifier;
    /** The defaultValue. */
    @JsonProperty("defaultValue") 
    private final JavascriptNode defaultValue;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code ParameterWithValue} without source location information.
     *
     * @param identifier   the parameter name node
     * @param defaultValue the default value expression
     */
    public ParameterWithValue(final JavascriptNode identifier,
                              final JavascriptNode defaultValue) {
        this(identifier, defaultValue, null);
    }

    /**
     * Constructs a {@code ParameterWithValue} with all components.
     *
     * @param identifier     the parameter name node
     * @param defaultValue   the default value expression
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public ParameterWithValue(@JsonProperty("identifier") final JavascriptNode identifier,
                              @JsonProperty("defaultValue") final JavascriptNode defaultValue,
                              @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.identifier = identifier;
        this.defaultValue = defaultValue;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(identifier, defaultValue);
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
     * Returns the parameter identifier node.
     *
     * @return the identifier node
     */
    public JavascriptNode getIdentifier() {
        return identifier;
    }

    /**
     * Returns the default value expression for this parameter.
     *
     * @return the default value node
     */
    public JavascriptNode getDefaultValue() {
        return defaultValue;
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
        return Objects.hash(defaultValue, identifier, sourceLocation);
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
        final ParameterWithValue other = (ParameterWithValue) obj;
        return Objects.equals(defaultValue, other.defaultValue) 
                && Objects.equals(identifier, other.identifier);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ParameterWithValue.class.getSimpleName());
        result.append(" [identifier=");
        result.append(identifier);
        result.append(", defaultValue=");
        result.append(defaultValue);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
