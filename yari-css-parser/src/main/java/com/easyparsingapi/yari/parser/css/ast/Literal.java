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
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * CSS AST node representing a literal value, such as a character string,
 * an integer, a decimal, or a hexadecimal number.
 */
@JsonPropertyOrder({"type", "value", "sourceLocation"})
public class Literal implements CssNode {

    private static final long serialVersionUID = 1L;

    /**
     * Enumeration of the supported CSS literal types.
     */
    public static enum Type {
        /** String delimited by double quotes. */
        doubleQuoteString,
        /** String delimited by single quotes. */
        singleQuoteString,
        /** Integer value. */
        integer,
        /** Decimal value. */
        decimal,
        /** Hexadecimal value. */
        hexadecimal

    }

    /** The literal type. */
    @JsonProperty("type")
    private final Type type;
    /** The literal text value. */
    @JsonProperty("value")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String value;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a literal with its type and value, without a source location.
     *
     * @param type  the type of the literal
     * @param value the textual value of the literal
     */
    public Literal(final Type type,
                   final String value) {
        this(type, value, null);
    }

    /**
     * Constructs a literal with its type, value, and source location.
     *
     * @param type           the type of the literal
     * @param value          the textual value of the literal
     * @param sourceLocation the location of the literal in the source, may be {@code null}
     */
    @JsonCreator
    public Literal(@JsonProperty("type") final Type type,
                   @JsonProperty("value") final String value,
                   @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.type = type;
        this.value = value;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Indicates whether this literal is of the specified type.
     *
     * @param type the type to test
     * @return {@code true} if this literal's type matches the given type
     */
    public boolean isType(final Type type) {
        return this.type == type;
    }

    /**
     * Returns the type of this literal.
     *
     * @return the literal type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the textual value of this literal.
     *
     * @return the literal value, or {@code null} if not defined
     */
    public String getValue() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes();
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this literal in the AST.
     *
     * @param parent the parent node to associate with this literal
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
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

    /**
     * Compares this literal to another {@link AstNode} for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code Literal} instances with equal type and value
     */
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
        final Literal other = (Literal) astNode;
        return Objects.equals(type, other.type)
                && Objects.equals(value, other.value);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(type, value, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Literal node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Literal.class.getSimpleName());
        result.append(" [type=");
        result.append(type);
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
