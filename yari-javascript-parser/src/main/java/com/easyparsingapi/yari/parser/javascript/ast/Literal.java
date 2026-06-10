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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a JavaScript literal value in the AST: numbers in various bases,
 * boolean values, strings (single-quote, double-quote, and their HTML-entity variants),
 * and regular expressions.
 * Each literal carries a {@link Type} tag and its raw string {@code value}.
 */
@JsonPropertyOrder({"type", "value", "sourceLocation"})
public class Literal implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /**
     * Enumerates all JavaScript literal kinds recognised by the parser.
     */
    public static enum Type {
        /** Decimal integer literal (e.g. {@code 42}). */
        integer,
        /** Octal-style base-10 literal (e.g. {@code 012}). */
        base_10,
        /** BigInt literal (e.g. {@code 42n}). */
        bigInteger,
        /** Hexadecimal literal (e.g. {@code 0xFF}). */
        hexadecimal,
        /** Binary literal (e.g. {@code 0b1010}). */
        binary,
        /** Octal literal (e.g. {@code 0o17}). */
        octal,
        /** Exponential / scientific-notation literal (e.g. {@code 1e3}). */
        exponential,
        /** Decimal (floating-point) literal (e.g. {@code 3.14}). */
        decimal,
        /** Boolean literal ({@code true} or {@code false}). */
        trueOrFalse,
        /** Double-quoted string literal. */
        doubleQuoteString,
        /** Double-quoted string using HTML entity name encoding ({@code &quot;}). */
        doubleQuoteStringEntityName,
        /** Double-quoted string using HTML numeric character reference ({@code &#34;}). */
        doubleQuoteStringNumberCode,
        /** Single-quoted string literal. */
        singleQuoteString,
        /** Single-quoted string using HTML entity name encoding ({@code &apos;}). */
        singleQuoteStringEntityName,
        /** Single-quoted string using HTML numeric character reference ({@code &#39;}). */
        singleQuoteStringNumberCode,
        /** Regular-expression literal (e.g. {@code /foo/gi}). */
        regExpString
    }

    /** The type. */
    @JsonProperty("type")
    private final Type type;
    /** The value. */
    @JsonProperty("value")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String value;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code Literal} without source-location information.
     *
     * @param type  the literal kind
     * @param value the raw string value of the literal
     */
    public Literal(final Type type,
                   final String value) {
        this(type, value, null);
    }

    /**
     * Constructs a {@code Literal} with full source-location information.
     *
     * @param type           the literal kind
     * @param value          the raw string value of the literal
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Literal(@JsonProperty("type") final Type type,
                   @JsonProperty("value") final String value,
                   @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.type = type;
        this.value = value;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
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
     * Sets the parent AST node of this literal.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns {@code true} if the literal value is an instance of the given class.
     *
     * @param clazz the class to test against
     * @return {@code true} if {@code value} is an instance of {@code clazz}
     */
    public boolean isInstanceOf(final Class<?> clazz) {
        return clazz.isInstance(value);
    }

    /**
     * Returns {@code true} if this literal is of the given type.
     *
     * @param type the type to compare with
     * @return {@code true} when this literal's type matches
     */
    public boolean isType(final Type type) {
        return this.type == type;
    }

    /**
     * Returns the type of this literal.
     *
     * @return the {@link Type} enum constant identifying the literal kind
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the raw string value of this literal as it appears in the source.
     *
     * @return the literal value string
     */
    public String getValue() {
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
        return Objects.hash(type, value, sourceLocation);
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
        final Literal other = (Literal) obj;
        return type == other.type 
                && Objects.equals(value, other.value);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Literal.class.getSimpleName());
        result.append(" [value=");
        result.append(value);
        result.append(" ,type=");
        result.append(getType());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
