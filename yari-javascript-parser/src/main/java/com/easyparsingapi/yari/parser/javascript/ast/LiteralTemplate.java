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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a JavaScript template literal (back-tick string) in the AST.
 * The literal is modelled as an ordered list of {@link Constant} (plain text segments)
 * and {@link Variable} (interpolated expression segments) elements.
 */
@JsonPropertyOrder({"elements", "sourceLocation"})
public class LiteralTemplate implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The elements. */
    @JsonProperty("elements")
    private final List<JavascriptNode> elements;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code LiteralTemplate} without source-location information.
     *
     * @param elements the ordered list of constant and variable segments
     */
    public LiteralTemplate(final List<JavascriptNode> elements) {
        this(elements, null);
    }

    /**
     * Constructs a {@code LiteralTemplate} with full source-location information.
     *
     * @param elements       the ordered list of constant and variable segments
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public LiteralTemplate(@JsonProperty("elements") final List<JavascriptNode> elements,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.elements = CollectionUtil.nullToEmpty(elements);
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(elements);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }
    
    /**
     * Sets the parent AST node of this template literal.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the number of elements (constant + variable segments) in this template literal.
     *
     * @return the element count
     */
    public int size() {
        return elements.size();
    }

    /**
     * Returns the ordered list of elements (constant text and interpolated expressions)
     * that make up this template literal.
     *
     * @return the list of {@link JavascriptNode} elements
     */
    public List<JavascriptNode> getElements() {
        return elements;
    }

    /**
     * Returns the element at the given zero-based index, or {@code null} if out of range.
     *
     * @param index zero-based position of the desired element
     * @return the {@link JavascriptNode} at that index, or {@code null}
     */
    public JavascriptNode getValue(final int index) {
        JavascriptNode result = null;
        if (index < elements.size()) {
            result = elements.get(index);
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
        return Objects.hash(elements, sourceLocation);
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
        final LiteralTemplate other = (LiteralTemplate) obj;
        return Objects.equals(elements, other.elements);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(LiteralTemplate.class.getSimpleName());
        result.append(" [elements=");
        result.append(elements.size());
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
     * Represents a plain text segment of a template literal (the parts outside {@code ${ }}).
     */
    @JsonPropertyOrder({"value", "sourceLocation"})
    public static class Constant implements JavascriptNode {

        private static final long serialVersionUID = 1L;

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
         * Constructs a {@code Constant} segment without source-location information.
         *
         * @param value the raw text of this constant segment
         */
        public Constant(final String value) {
            super();
            this.value = value;
        }

        /**
         * Constructs a {@code Constant} segment with full source-location information.
         *
         * @param value          the raw text of this constant segment
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Constant(@JsonProperty("value") final String value,
                        @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
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
         * Sets the parent AST node of this constant segment.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the raw text of this constant segment.
         *
         * @return the constant string value
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
            final Constant other = (Constant) obj;
            return Objects.equals(value, other.value);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(LiteralTemplate.class.getSimpleName());
            result.append(".");
            result.append(Constant.class.getSimpleName());
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
     * Represents an interpolated expression segment of a template literal (the {@code ${ expr }} parts).
     */
    @JsonPropertyOrder({"value", "sourceLocation"})
    public static class Variable implements JavascriptNode {

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
         * Constructs a {@code Variable} segment without source-location information.
         *
         * @param value the interpolated expression node
         */
        public Variable(final JavascriptNode value) {
            this(value, null);
        }

        /**
         * Constructs a {@code Variable} segment with full source-location information.
         *
         * @param value          the interpolated expression node
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Variable(@JsonProperty("value") final JavascriptNode value,
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
         * Sets the parent AST node of this variable segment.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the interpolated expression node for this segment.
         *
         * @return the {@link JavascriptNode} representing the interpolated expression
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
            final Variable other = (Variable) obj;
            return Objects.equals(value, other.value);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(LiteralTemplate.class.getSimpleName());
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

}
