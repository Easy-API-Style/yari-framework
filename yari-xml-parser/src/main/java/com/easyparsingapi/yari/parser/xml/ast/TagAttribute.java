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
package com.easyparsingapi.yari.parser.xml.ast;

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.xml.ast.Markup.Name;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents an XML attribute consisting of a name and an optional value.
 */
@JsonPropertyOrder({"name", "value", "sourceLocation"})
public class TagAttribute implements XmlNode {

    private static final long serialVersionUID = 1L; 
    
    /** The attribute name. */
    @JsonProperty("name")
    private final Name name;
    /** The attribute value. */
    @JsonProperty("value")
    private final Value value;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Creates a TagAttribute with a name, value, source location and explicit parent node.
     *
     * @param name           the attribute name
     * @param value          the attribute value, or {@code null} if absent
     * @param sourceLocation the location of this node in the source document
     * @param parent         the parent node in the AST
     */
    public TagAttribute(final Name name,
                        final Value value,
                        final SourceLocation sourceLocation,
                        final AstNode parent) {
        this(name, value, sourceLocation);
        this.astParent(parent);
    }

    /**
     * Creates a TagAttribute with a name and value and no source location.
     *
     * @param name  the attribute name
     * @param value the attribute value, or {@code null} if absent
     */
    public TagAttribute(final Name name,
                        final Value value) {
        this(name, value, null);
    }

    /**
     * Creates a TagAttribute with only a name (no value, no source location).
     *
     * @param name the attribute name
     */
    public TagAttribute(final Name name) {
        this(name, null, null);
    }

    /**
     * Creates a TagAttribute with a name, optional value and source location.
     *
     * @param name           the attribute name
     * @param value          the attribute value, or {@code null} if absent
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public TagAttribute(@JsonProperty("name") final Name name,
                        @JsonProperty("value") final Value value,
                        @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.value = value;
        this.sourceLocation = sourceLocation;
        XmlUtil.setAstParent(this);
    }
    
    /**
     * Returns the name of this attribute.
     *
     * @return the attribute name
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns {@code true} if this attribute has a value.
     *
     * @return {@code true} if the value is non-null
     */
    public boolean hasValue() {
        return value != null;
    }

    /**
     * Returns the value of this attribute, or {@code null} if absent.
     *
     * @return the attribute value, or {@code null}
     */
    public Value getValue() {
        return value;
    }

    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name, value);
    }

    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this attribute.
     *
     * @param parent the parent node in the AST
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

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
        final TagAttribute other = (TagAttribute) astNode;
        return Objects.equals(name, other.name)
                 && Objects.equals(value, other.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, value, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof TagAttribute node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [name=");
        result.append(name);
        result.append(", value=");
        result.append(value);
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
     * Represents the name of an XML attribute, optionally qualified with a namespace prefix.
     */
    @JsonPropertyOrder({"namespace", "name", "sourceLocation"})
    public static class Name extends Markup {

        private static final long serialVersionUID = 1L;

        /**
         * Creates an attribute Name with the given namespace and local name, and no source location.
         *
         * @param namespace the namespace prefix, or {@code null} if none
         * @param name      the local name part
         */
        public Name(final Namespace namespace,
                    final Name name) {
            this(namespace, name, null);
        }

        /**
         * Creates an attribute Name with the given namespace, local name and source location.
         *
         * @param namespace      the namespace prefix, or {@code null} if none
         * @param name           the local name part
         * @param sourceLocation the location of this node in the source document
         */
        @JsonCreator
        public Name(@JsonProperty("namespace") final Namespace namespace,
                    @JsonProperty("name") final Name name,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super(namespace, name, sourceLocation);
        }
        
    }
    
    /**
     * Represents the value of an XML attribute, including its quote style.
     */
    @JsonPropertyOrder({"value", "type", "sourceLocation"})
    public static class Value implements XmlIdentifier {

        private static final long serialVersionUID = 1L;

        /**
         * The quoting style used for an attribute value.
         */
        public static enum Type {
            /** Attribute value delimited by single quotes ({@code '...'}) . */
            singleQuote,
            /** Attribute value delimited by double quotes ({@code "..."}). */
            doubleQuote
        }

        /** The quote type. */
        @JsonProperty("type")
        private final Type type;
        /** The attribute value text. */
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
         * Creates a Value with the given type and string content, and no source location.
         *
         * @param type  the quoting style used for this value
         * @param value the attribute value string
         */
        public Value(final Type type,
                     final String value) {
            this(type, value, null);
        }

        /**
         * Creates a Value with all fields.
         *
         * @param type           the quoting style used for this value
         * @param value          the attribute value string
         * @param sourceLocation the location of this node in the source document
         */
        @JsonCreator
        public Value(@JsonProperty("type") final Type type,
                     @JsonProperty("value") final String value,
                     @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.type = type;
            this.value = value;
            this.sourceLocation = sourceLocation;
            XmlUtil.setAstParent(this);
        }

        /**
         * Returns the quoting style used for this attribute value.
         *
         * @return the quote type
         */
        public Type getType() {
            return type;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes();
        }

        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this value.
         *
         * @param parent the parent node in the AST
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }

        @Override
        public void setSourceLocation(final SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }
        
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
            final Value other = (Value) astNode;
            return Objects.equals(value, other.value);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(value, sourceLocation);
        }

        @Override
        public boolean equals(final Object object) {
            if (object instanceof Value node) {
                return equalsNode(node) 
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
        }
        
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(TagAttribute.class.getSimpleName());
            result.append(".");
            result.append(Value.class.getSimpleName());
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
    
}
