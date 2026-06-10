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
import java.util.stream.Stream;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing an invoked (called) JavaScript function expression,
 * pairing the function reference ({@link Definition}) with its argument list ({@link Signature}).
 */
@JsonPropertyOrder({"definition", "signature", "sourceLocation"})
public class InvokedFunction implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The definition. */
    @JsonProperty("definition") 
    private final Definition definition;
    /** The signature. */
    @JsonProperty("signature") 
    private final Signature signature;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code InvokedFunction} node without source location information.
     *
     * @param definition the function reference being invoked
     * @param signature  the argument list
     */
    public InvokedFunction(final Definition definition,
                           final Signature signature) {
        this(definition, signature, null);
    }

    /**
     * Constructs an {@code InvokedFunction} node with full source location information.
     *
     * @param definition     the function reference being invoked
     * @param signature      the argument list
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public InvokedFunction(@JsonProperty("definition") final Definition definition,
                           @JsonProperty("signature") final Signature signature,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.definition = definition;
        this.signature = signature;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(definition, signature);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this invoked function node in the AST.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the definition (function reference) of this invocation.
     *
     * @return the {@link Definition} node
     */
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Returns the argument list of this function invocation.
     *
     * @return the {@link Signature} node
     */
    public Signature getSignature() {
        return signature;
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
        return Objects.hash(definition, signature, sourceLocation, sourceLocation);
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
        final InvokedFunction other = (InvokedFunction) astNode;
        return Objects.equals(definition, other.definition) 
                && Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(InvokedFunction.class.getSimpleName());
        result.append(" [definition=");
        result.append(definition);
        result.append(", signature=");
        result.append(signature);
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
     * Represents the function reference (callee) of an {@link InvokedFunction} node.
     * Wraps the expression that resolves to the callable function.
     */
    @JsonPropertyOrder({"function", "sourceLocation"})
    public static class Definition implements JavascriptNode  {

        private static final long serialVersionUID = 1L; 
        
        /** The function. */
        @JsonProperty("function") 
        private final JavascriptNode function;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;
        
        /**
         * Constructs a {@code Definition} without source location information.
         *
         * @param function the expression resolving to the callable function
         */
        public Definition(final JavascriptNode function) {
            this(function, null);
        }

        /**
         * Constructs a {@code Definition} with full source location information.
         *
         * @param function       the expression resolving to the callable function
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Definition(@JsonProperty("function") final JavascriptNode function,
                          @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.function = function;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(function);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent node of this definition node in the AST.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the expression that resolves to the callable function.
         *
         * @return the function expression {@link JavascriptNode}
         */
        public JavascriptNode getFunction() {
            return function;
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
            return Objects.hash(function, sourceLocation);
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
            final Definition other = (Definition) obj;
            return Objects.equals(function, other.function);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(InvokedFunction.class.getSimpleName());
            result.append(".");
            result.append(Definition.class.getSimpleName());
            result.append(" [function=");
            result.append(function);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }
    
    /**
     * Represents the argument list of an {@link InvokedFunction} node,
     * holding an ordered list of argument expression nodes.
     */
    @JsonPropertyOrder({"parameters", "sourceLocation"})
    public static class Signature implements JavascriptSignature {

        private static final long serialVersionUID = 1L; 
        
        /** The parameters. */
        @JsonProperty("parameters") 
        private final List<JavascriptNode> parameters;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;
        
        /**
         * Constructs a {@code Signature} without source location information.
         *
         * @param parameters the list of argument expression nodes
         */
        public Signature(final List<JavascriptNode> parameters) {
            this(parameters, null);
        }

        /**
         * Constructs a {@code Signature} with full source location information.
         *
         * @param parameters     the list of argument expression nodes
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Signature(@JsonProperty("parameters") final List<JavascriptNode> parameters,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.parameters = CollectionUtil.nullToEmpty(parameters);
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(parameters);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent node of this signature node in the AST.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the number of arguments in this signature.
         *
         * @return the argument count
         */
        public int size() {
            return parameters.size();
        }

        /**
         * Returns {@code true} if this signature contains at least one argument.
         *
         * @return {@code true} when one or more arguments are present
         */
        public boolean hasParameter() {
            return !parameters.isEmpty();
        }

        /** {@inheritDoc} */
        @Override
        public Stream<JavascriptNode> streamParameters() {
            return parameters.stream();
        }

        /**
         * Returns the ordered list of argument expression nodes.
         *
         * @return the list of argument {@link JavascriptNode}s
         */
        public List<JavascriptNode> getParameters() {
            return parameters;
        }

        /**
         * Returns the argument at the given zero-based index,
         * or {@code null} if the index is out of range.
         *
         * @param index zero-based position of the desired argument
         * @return the argument {@link JavascriptNode} at that index, or {@code null}
         */
        public JavascriptNode getParameter(final int index) {
            JavascriptNode result = null;
            if (index < size()) {
                result = parameters.get(index);
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
            return Objects.hash(parameters, sourceLocation);
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
            final Signature other = (Signature) obj;
            return Objects.equals(parameters, other.parameters);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(InvokedFunction.class.getSimpleName());
            result.append(".");
            result.append(Signature.class.getSimpleName());
            result.append(" [parameters=");
            result.append(parameters.size());
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }
    
}
