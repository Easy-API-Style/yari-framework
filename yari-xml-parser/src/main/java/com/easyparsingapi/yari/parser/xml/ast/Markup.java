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
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;
import com.easyparsingapi.yari.parser.xml.lexer.XmlTag;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Abstract base class representing an XML markup name, optionally qualified with a namespace prefix.
 */
@JsonPropertyOrder({"namespace", "name", "sourceLocation"})
public abstract class Markup implements XmlIdentifier {
    
    private static final long serialVersionUID = 1L;
    
    /** The namespace part. */
    @JsonProperty("namespace")
    private final Namespace namespace;
    /** The local name part. */
    @JsonProperty("name")
    private final Name name;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    @JsonCreator
    Markup(@JsonProperty("namespace") final Namespace namespace,
           @JsonProperty("name") final Name name,
           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.namespace = namespace;
        this.name = name;
        this.sourceLocation = sourceLocation;
        XmlUtil.setAstParent(this);
    }
    
    @Override
    public String getValue() {
        final StringBuilder result = new StringBuilder();
        if (namespace() != null) {
            result.append(namespace().getValue());
            result.append(":");
        }
        result.append(name().getValue());
        return result.toString();
    }
    
    /**
     * Returns the namespace prefix part of this markup name, or {@code null} if none.
     *
     * @return the namespace, or {@code null}
     */
    public Namespace namespace() {
        return namespace;
    }

    /**
     * Returns the local name part of this markup name.
     *
     * @return the local name
     */
    public Name name() {
        return name;
    }

    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(namespace, name);
    }

    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this markup element.
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
        final Markup other = (Markup) astNode;
        return Objects.equals(namespace, other.namespace) 
                 && Objects.equals(name, other.name) ;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Markup node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [value=");
        result.append(getValue());
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
     * Represents the namespace prefix part of a markup name (e.g., {@code ns} in {@code ns:element}).
     */
    @JsonPropertyOrder({"value", "sourceLocation"})
    public static class Namespace implements XmlIdentifier {

        private static final long serialVersionUID = 1L;

        /** The namespace string value. */
        @JsonProperty("value")
        private final String value;
        /** The parent AST node. */
        @JsonIgnore
        private AstNode parent;
        /** The source location. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;

        /**
         * Creates a Namespace with the given value and no source location.
         *
         * @param value the namespace prefix string
         */
        public Namespace(final String value) {
            this(value, null);
        }

        /**
         * Creates a Namespace with the given value and source location.
         *
         * @param value          the namespace prefix string
         * @param sourceLocation the location of this node in the source document
         */
        @JsonCreator
        public Namespace(@JsonProperty("value") final String value,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.value = value;
            this.sourceLocation = sourceLocation;
            XmlUtil.setAstParent(this);
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
         * Sets the parent AST node of this namespace element.
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
            final Namespace other = (Namespace) astNode;
            return Objects.equals(value, other.value);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(value, sourceLocation);
        }

        @Override
        public boolean equals(final Object object) {
            if (object instanceof Namespace node) {
                return equalsNode(node) 
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
        }
        
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(TagName.class.getSimpleName());
            result.append(".");
            result.append(Namespace.class.getSimpleName());
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
    
    /**
     * Represents the local name part of a markup name (e.g., {@code element} in {@code ns:element}).
     */
    @JsonPropertyOrder({"value", "sourceLocation"})
    public static class Name implements XmlIdentifier {

        private static final long serialVersionUID = 1L;

        /** The local name string value. */
        @JsonProperty("value")
        private final String value;
        /** The parent AST node. */
        @JsonIgnore
        private AstNode parent;
        /** The source location. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;

        /**
         * Creates a Name with the given value and no source location.
         *
         * @param value the local name string
         */
        public Name(final String value) {
            this(value, null);
        }

        /**
         * Creates a Name with the given value and source location.
         *
         * @param value          the local name string
         * @param sourceLocation the location of this node in the source document
         */
        @JsonCreator
        public Name(@JsonProperty("name") final String value,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.value = value;
            this.sourceLocation = sourceLocation;
            XmlUtil.setAstParent(this);
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
         * Sets the parent AST node of this name element.
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
            final Name other = (Name) astNode;
            return Objects.equals(value, other.value);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(value, sourceLocation);
        }

        @Override
        public boolean equals(final Object object) {
            if (object instanceof Name node) {
                return equalsNode(node) 
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
        }
        
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(TagName.class.getSimpleName());
            result.append(".");
            result.append(Name.class.getSimpleName());
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
    
    /*
     * 
     * STATIC
     * 
     */
    private static record NewInstance(Namespace namespace, 
                                      Name name,
                                      SourceLocation sourceLocation) {}
    
    private static NewInstance newInstance(final Token token) {
        final SourceLocation sourceLocationTagHeadToken = token.sourceLocation();
        
        final String fullName;
        final int line;
        final int startColumn;
        final int endColumn;
        final SourceLocation fullSourceLocation;
        
        final XmlTag xmlTag = (XmlTag) Token.tag(token);
        if (XmlTag.ATTRIBUE_NAME.equals(xmlTag)) {
            fullName = token.toString();
            line = sourceLocationTagHeadToken.start().line();
            startColumn = sourceLocationTagHeadToken.start().column();
            endColumn = sourceLocationTagHeadToken.end().column();
            fullSourceLocation = new SourceLocation(new Position(line, startColumn),
                                                    new Position(line, endColumn));
        }
        else if (XmlTag.BEGIN_TAG.equals(xmlTag)) {
            fullName = token.toString().substring(1);
            line = sourceLocationTagHeadToken.start().line();
            startColumn = sourceLocationTagHeadToken.start().column() + 1;
            endColumn = sourceLocationTagHeadToken.end().column();
            fullSourceLocation = new SourceLocation(new Position(line, startColumn),
                                                    new Position(line, endColumn));
        }
        else {
            fullName = token.toString().substring(2, token.toString().length() - 1);
            line = sourceLocationTagHeadToken.start().line();
            startColumn = sourceLocationTagHeadToken.start().column() + 2;
            endColumn = sourceLocationTagHeadToken.end().column() - 1;
            fullSourceLocation = new SourceLocation(new Position(line, startColumn),
                                                    new Position(line, endColumn));
        }
       
        Namespace namespace = null;
        Name name = null;
        if (fullName.contains(":")) {
            final int index = fullName.indexOf(":");
            final String _namespace = fullName.substring(0, index);
            namespace = new Namespace(_namespace, new SourceLocation(new Position(line, startColumn), 
                                                                     new Position(line, startColumn + _namespace.length())));
            final String _name = fullName.substring(index + 1, fullName.length());
            name = new Name(_name, new SourceLocation(new Position(line, startColumn + _namespace.length() + 1), 
                                                      new Position(line, endColumn)));
        }
        else {
            name = new Name(fullName, fullSourceLocation);
        }
        return new NewInstance(namespace, name, fullSourceLocation);
    }
    
    /**
     * Creates a {@link TagName} from the given lexer token.
     *
     * @param token the lexer token representing a tag name
     * @return a new {@code TagName} parsed from the token
     */
    public static TagName toTagName(final Token token) {
        final NewInstance newInstance = newInstance(token);
        return new TagName(newInstance.namespace,
                           newInstance.name,
                           newInstance.sourceLocation);
    }

    /**
     * Creates a {@link TagAttribute.Name} from the given lexer token.
     *
     * @param token the lexer token representing an attribute name
     * @return a new {@code TagAttribute.Name} parsed from the token
     */
    public static TagAttribute.Name toTagAttributeName(final Token token) {
        final NewInstance newInstance = newInstance(token);
        return new TagAttribute.Name(newInstance.namespace, 
                                     newInstance.name, 
                                     newInstance.sourceLocation);
    }

}
