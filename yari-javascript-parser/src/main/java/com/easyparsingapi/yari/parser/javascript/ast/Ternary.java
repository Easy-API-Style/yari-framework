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
 * Represents a ternary conditional expression in the AST
 * ({@code condition ? ifPart : elsePart}).
 */
@JsonPropertyOrder({"condition", "ifPart", "elsePart", "sourceLocation"})
public class Ternary implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The condition. */
    @JsonProperty("condition")
    private final JavascriptNode condition;
    /** The ifPart. */
    @JsonProperty("ifPart")
    private final JavascriptNode ifPart;
    /** The elsePart. */
    @JsonProperty("elsePart")
    private final JavascriptNode elsePart;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code Ternary} without source-location information.
     *
     * @param condition the boolean condition expression
     * @param ifPart    the expression evaluated when the condition is truthy
     * @param elsePart  the expression evaluated when the condition is falsy
     */
    public Ternary(final JavascriptNode condition,
                   final JavascriptNode ifPart,
                   final JavascriptNode elsePart) {
        this(condition, ifPart, elsePart, null);
    }

    /**
     * Constructs a {@code Ternary} with full source-location information.
     *
     * @param condition      the boolean condition expression
     * @param ifPart         the expression evaluated when the condition is truthy
     * @param elsePart       the expression evaluated when the condition is falsy
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Ternary(@JsonProperty("condition") final JavascriptNode condition,
                   @JsonProperty("ifPart") final JavascriptNode ifPart,
                   @JsonProperty("elsePart") final JavascriptNode elsePart,
                   @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.condition = condition;
        this.ifPart = ifPart;
        this.elsePart = elsePart;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(condition, ifPart, elsePart);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this ternary expression.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the condition expression.
     *
     * @return the condition {@link JavascriptNode}
     */
    public JavascriptNode getCondition() {
        return condition;
    }

    /**
     * Returns the expression evaluated when the condition is truthy.
     *
     * @return the truthy-branch {@link JavascriptNode}
     */
    public JavascriptNode getIfPart() {
        return ifPart;
    }

    /**
     * Returns the expression evaluated when the condition is falsy.
     *
     * @return the falsy-branch {@link JavascriptNode}
     */
    public JavascriptNode getElsePart() {
        return elsePart;
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
        return Objects.hash(condition, elsePart, ifPart, sourceLocation);
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
        final Ternary other = (Ternary) obj;
        return Objects.equals(condition, other.condition)
                && Objects.equals(elsePart, other.elsePart)
                && Objects.equals(ifPart, other.ifPart);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Ternary.class.getSimpleName());
        result.append(" [condition=");
        result.append(condition);
        result.append(", ifPart=");
        result.append(ifPart);
        result.append(", elsePart=");
        result.append(elsePart);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }
    
}
