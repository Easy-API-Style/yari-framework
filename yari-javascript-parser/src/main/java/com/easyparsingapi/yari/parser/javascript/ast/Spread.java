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
 * Represents a spread expression in the AST ({@code ...expr}).
 * Used in array literals, function arguments, and object literals.
 */
@JsonPropertyOrder({"value", "sourceLocation"})
public class Spread implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
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
     * Constructs a {@code Spread} without source-location information.
     *
     * @param value the expression being spread
     */
    public Spread(final JavascriptNode value) {
        this(value, null);
    }

    /**
     * Constructs a {@code Spread} with full source-location information.
     *
     * @param value          the expression being spread
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Spread(@JsonProperty("value") final JavascriptNode value,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.value = value;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(value);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this spread expression.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the expression being spread.
     *
     * @return the spread {@link JavascriptNode}
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
        return Objects.hash(value, sourceLocation);
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
        final Spread other = (Spread) obj;
        return Objects.equals(value, other.value);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Spread.class.getSimpleName());
        result.append(" [value=");
        result.append(value);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
