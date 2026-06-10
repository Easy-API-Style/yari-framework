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
 * Represents a {@code yield} expression in the AST.
 * The {@code generator} flag distinguishes {@code yield*} (delegate) from plain {@code yield}.
 */
@JsonPropertyOrder({"generator", "operand", "sourceLocation"})
public class Yield implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The generator. */
    @JsonProperty("generator") 
    private final boolean generator;
    /** The operand. */
    @JsonProperty("operand") 
    private final JavascriptNode operand;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code Yield} without source-location information.
     *
     * @param generator {@code true} for {@code yield*} (delegate)
     * @param operand   the yielded expression, or {@code null} for a bare {@code yield}
     */
    public Yield(final boolean generator,
                 final JavascriptNode operand) {
        this(generator, operand, null);
    }

    /**
     * Constructs a {@code Yield} with full source-location information.
     *
     * @param generator      {@code true} for {@code yield*} (delegate)
     * @param operand        the yielded expression, or {@code null} for a bare {@code yield}
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Yield(@JsonProperty("generator") final boolean generator,
                 @JsonProperty("operand") final JavascriptNode operand,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.generator = generator;
        this.operand = operand;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(operand);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this yield expression.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns {@code true} if this is a delegating {@code yield*} expression.
     *
     * @return {@code true} for {@code yield*}
     */
    public boolean isGenerator() {
        return generator;
    }

    /**
     * Returns {@code true} if this yield expression has an operand.
     *
     * @return {@code true} when an operand expression is present
     */
    public boolean hasOperand() {
        return operand != null;
    }

    /**
     * Returns the yielded operand expression, or {@code null} for a bare {@code yield}.
     *
     * @return the operand {@link JavascriptNode}, or {@code null}
     */
    public JavascriptNode getOperand() {
        return operand;
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
        return Objects.hash(generator, operand, sourceLocation);
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
        final Yield other = (Yield) obj;
        return generator == other.generator 
                && Objects.equals(operand, other.operand);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Yield.class.getSimpleName());
        result.append(" [generator=");
        result.append(generator);
        result.append(", operand=");
        result.append(operand);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
