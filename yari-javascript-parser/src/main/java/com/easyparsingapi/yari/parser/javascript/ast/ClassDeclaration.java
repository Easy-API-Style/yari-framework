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
 * AST node representing a JavaScript class declaration ({@code class} keyword).
 * <p>
 * A class declaration can be named or anonymous, extend another class,
 * and contain a set of properties (methods, fields, etc.).
 * </p>
 */
@JsonPropertyOrder({"className", "extendedName", "properties", "sourceLocation"})
public class ClassDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The className. */
    @JsonProperty("className")
    private final Identifier className;
    /** The extendedName. */
    @JsonProperty("extendedName")
    private final JavascriptNode extendedName;
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
     * Constructs a class declaration with no source location.
     *
     * @param className    identifier of the class name, or {@code null} if the class is anonymous
     * @param extendedName node representing the inherited parent class, or {@code null} if none
     * @param properties   list of class members (methods, fields, etc.)
     */
    public ClassDeclaration(final Identifier className,
                            final JavascriptNode extendedName,
                            final List<JavascriptNode> properties) {
        this(className, extendedName, properties, null);
    }

    /**
     * Constructs a class declaration with all its properties, including the source location.
     *
     * @param className      identifier of the class name, or {@code null} if the class is anonymous
     * @param extendedName   node representing the inherited parent class, or {@code null} if none
     * @param properties     list of class members (methods, fields, etc.)
     * @param sourceLocation location in the original source, or {@code null}
     */
    @JsonCreator
    public ClassDeclaration(@JsonProperty("className") final Identifier className,
                            @JsonProperty("extendedName") final JavascriptNode extendedName,
                            @JsonProperty("properties") final List<JavascriptNode> properties,
                            @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.className = className;
        this.extendedName = extendedName;
        this.properties = CollectionUtil.nullToEmpty(properties);
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(className, extendedName, properties);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this class declaration in the AST.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Indicates whether the class is anonymous, that is, declared without a name.
     *
     * @return {@code true} if the class name is absent, {@code false} otherwise
     */
    public boolean isAnonymous() {
        return className == null;
    }

    /**
     * Returns the identifier representing the class name.
     *
     * @return the class name identifier, or {@code null} if the class is anonymous
     */
    public Identifier getClassName() {
        return className;
    }

    /**
     * Indicates whether the class extends another class (presence of an {@code extends} clause).
     *
     * @return {@code true} if a parent class is declared, {@code false} otherwise
     */
    public boolean isExtended() {
        return extendedName != null;
    }

    /**
     * Returns the node representing the inherited parent class.
     *
     * @return the node of the {@code extends} clause, or {@code null} if no inheritance is defined
     */
    public JavascriptNode getExtendedName() {
        return extendedName;
    }

    /**
     * Returns the number of members (properties) of the class.
     *
     * @return the number of properties contained in the class declaration
     */
    public int size() {
        return properties.size();
    }

    /**
     * Indicates whether the class has at least one member (property).
     *
     * @return {@code true} if the property list is not empty, {@code false} otherwise
     */
    public boolean hasProperty() {
        return !properties.isEmpty();
    }

    /**
     * Returns the list of members (properties) of the class.
     *
     * @return non-null list of member nodes of the class
     */
    public List<JavascriptNode> getProperties() {
        return properties;
    }

    /**
     * Returns the class member at the given index.
     *
     * @param index zero-based position of the desired member
     * @return the member node at that index, or {@code null} if the index is out of bounds
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
        return Objects.hash(className, extendedName, properties, sourceLocation);
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
        final ClassDeclaration other = (ClassDeclaration) obj;
        return Objects.equals(className, other.className)
                && Objects.equals(extendedName, other.extendedName)
                && Objects.equals(properties, other.properties);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ClassDeclaration.class.getSimpleName());
        result.append(" [className=");
        result.append(className);
        if (isExtended()) {
            result.append(", extendedName=");
            result.append(extendedName);
        }
        result.append(", properties=");
        result.append(size());
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
     * AST node representing the declaration of a static field in a JavaScript class.
     * <p>
     * A static field is defined by a key (field name) and an optional value.
     * </p>
     */
    @JsonPropertyOrder({"key", "value", "sourceLocation"})
    public static class StaticFieldDeclaration implements JavascriptNode {

        private static final long serialVersionUID = 1L;

        /** The member key. */
        @JsonProperty("key")
        protected final JavascriptNode key;
        /** The member value. */
        @JsonProperty("value")
        protected final JavascriptNode value;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;

        /**
         * Constructs a static field declaration with no source location.
         *
         * @param key   node representing the name (key) of the static field
         * @param value node representing the initial value of the field, or {@code null}
         */
        public StaticFieldDeclaration(final JavascriptNode key,
                                      final JavascriptNode value) {
            this(key, value, null);
        }

        /**
         * Constructs a static field declaration with all its properties.
         *
         * @param key            node representing the name (key) of the static field
         * @param value          node representing the initial value of the field, or {@code null}
         * @param sourceLocation location in the original source, or {@code null}
         */
        @JsonCreator
        public StaticFieldDeclaration(@JsonProperty("key") final JavascriptNode key,
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
         * Sets the parent node of this static field in the AST.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the node representing the name (key) of the static field.
         *
         * @return the key node of the static field
         */
        public JavascriptNode getKey() {
            return key;
        }

        /**
         * Returns the node representing the initial value of the static field.
         *
         * @return the value node, or {@code null} if no value is defined
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
            final StaticFieldDeclaration other = (StaticFieldDeclaration) obj;
            return Objects.equals(key, other.key)
                    && Objects.equals(value, other.value);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(ObjectDeclaration.class.getSimpleName());
            result.append(".");
            result.append(StaticFieldDeclaration.class.getSimpleName());
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

}
