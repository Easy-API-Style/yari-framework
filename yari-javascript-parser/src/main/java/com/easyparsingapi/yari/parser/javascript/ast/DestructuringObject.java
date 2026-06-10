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
 * AST node representing a JavaScript object destructuring pattern (e.g. {@code { a, b: c = 1 }}).
 */
@JsonPropertyOrder({"fields", "sourceLocation"})
public class DestructuringObject implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The fields. */
    @JsonProperty("fields")
    private final List<JavascriptNode> fields;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code DestructuringObject} with no source location.
     *
     * @param fields the list of fields of the destructuring pattern
     */
    public DestructuringObject(final List<JavascriptNode> fields) {
        this(fields, null);
    }

    /**
     * Constructs a {@code DestructuringObject} with all its attributes.
     *
     * @param fields         the list of fields of the destructuring pattern
     * @param sourceLocation the location in the original source, may be {@code null}
     */
    @JsonCreator
    public DestructuringObject(@JsonProperty("fields") final List<JavascriptNode> fields,
                               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.fields = CollectionUtil.nullToEmpty(fields);
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(fields);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the AST.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the number of fields in this destructuring pattern.
     *
     * @return the number of fields
     */
    public int size() {
        return fields.size();
    }

    /**
     * Returns the list of fields in this destructuring pattern.
     *
     * @return the list of fields, never {@code null}
     */
    public List<JavascriptNode> getFields() {
        return fields;
    }

    /**
     * Returns the field at the given index, or {@code null} if the index is out of bounds.
     *
     * @param index the zero-based index of the desired field
     * @return the field at that index, or {@code null} if the index is greater than or equal to {@link #size()}
     */
    public JavascriptNode getField(final int index) {
        JavascriptNode result = null;
        if (index < fields.size()) {
            result = fields.get(index);
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
        return Objects.hash(fields, sourceLocation);
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
        final DestructuringObject other = (DestructuringObject) obj;
        return Objects.equals(fields, other.fields);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(DestructuringObject.class.getSimpleName());
        result.append(" [fields=");
        result.append(fields.size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

    /*
     *
     * CLASS
     *
     */
    /**
     * AST node representing an individual field in an object destructuring pattern
     * (e.g. {@code b: c = 1} where {@code b} is the key, {@code c} the binding, and {@code 1} the default value).
     */
    @JsonPropertyOrder({"id", "binding", "defaultValue", "sourceLocation"})
    public static class Field implements JavascriptNode {

        private static final long serialVersionUID = 1L;

        /** The id. */
        @JsonProperty("id")
        private final JavascriptNode id;
        /** The binding. */
        @JsonProperty("binding")
        private final JavascriptNode binding;
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
         * Constructs a {@code Field} without a source location.
         *
         * @param id           the node representing the field key
         * @param binding      the node representing the bound variable, may be {@code null}
         * @param defaultValue the node representing the default value, may be {@code null}
         */
        public Field(final JavascriptNode id,
                     final JavascriptNode binding,
                     final JavascriptNode defaultValue) {
            this(id, binding, defaultValue, null);
        }

        /**
         * Constructs a {@code Field} with all its attributes.
         *
         * @param id             the node representing the field key
         * @param binding        the node representing the bound variable, may be {@code null}
         * @param defaultValue   the node representing the default value, may be {@code null}
         * @param sourceLocation the location in the original source, may be {@code null}
         */
        @JsonCreator
        public Field(@JsonProperty("id") final JavascriptNode id,
                     @JsonProperty("binding") final JavascriptNode binding,
                     @JsonProperty("defaultValue") final JavascriptNode defaultValue,
                     @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.id = id;
            this.binding = binding;
            this.defaultValue = defaultValue;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }

        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(id, binding, defaultValue);
        }

        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent node of this field in the AST.
         *
         * @param parent the parent node
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the node representing the key of this destructuring field.
         *
         * @return the field identifier node
         */
        public JavascriptNode getId() {
            return id;
        }

        /**
         * Indicates whether this field has an explicit binding (a bound variable distinct from the key).
         *
         * @return {@code true} if a binding is defined, {@code false} otherwise
         */
        public boolean hasName() {
            return binding != null;
        }

        /**
         * Returns the node representing the variable bound to this field, or {@code null} if absent.
         *
         * @return the binding node, or {@code null}
         */
        public JavascriptNode getBinding() {
            return binding;
        }

        /**
         * Indicates whether this field has a default value.
         *
         * @return {@code true} if a default value is defined, {@code false} otherwise
         */
        public boolean hasDefaultValue() {
            return defaultValue != null;
        }

        /**
         * Returns the node representing the default value of this field, or {@code null} if absent.
         *
         * @return the default value node, or {@code null}
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
            return Objects.hash(binding, defaultValue, id, sourceLocation);
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
            final Field other = (Field) obj;
            return Objects.equals(binding, other.binding)
                    && Objects.equals(defaultValue, other.defaultValue)
                    && Objects.equals(id, other.id);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(DestructuringObject.class.getSimpleName());
            result.append(".");
            result.append(Field.class.getSimpleName());
            result.append(" [id=");
            result.append(id);
            result.append(", binding=");
            result.append(binding);
            if (hasDefaultValue()) {
                result.append(", defaultValue=");
                result.append(defaultValue);
            }
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }

}
