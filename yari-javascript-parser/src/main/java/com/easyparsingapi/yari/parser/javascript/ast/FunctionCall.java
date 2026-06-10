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
 * Represents a function (or method) call expression in a JavaScript AST, combining
 * the callee expression ({@code name}) with its argument list ({@link Signature}).
 * Covers forms such as {@code foo()}, {@code obj.method(a, b)}, and
 * {@code (expr)(args)}.
 */
@JsonPropertyOrder({"name", "signature", "sourceLocation"})
public class FunctionCall implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The name. */
    @JsonProperty("name") 
    private final JavascriptNode name;
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
     * Constructs a {@code FunctionCall} without source-location information.
     *
     * @param name      the callee expression
     * @param signature the argument list
     */
    public FunctionCall(final JavascriptNode name,
                        final Signature signature) {
        this(name, signature, null);
    }

    /**
     * Constructs a {@code FunctionCall} with full source-location information.
     *
     * @param name           the callee expression
     * @param signature      the argument list
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public FunctionCall(@JsonProperty("name") final JavascriptNode name,
                        @JsonProperty("signature") final Signature signature,
                        @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.signature = signature;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name, signature);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }
    
    /**
     * Sets the parent AST node of this function call.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the callee expression of this function call.
     *
     * @return the callee node
     */
    public JavascriptNode getName() {
        return name;
    }

    /**
     * Returns the argument list (signature) of this function call.
     *
     * @return the {@link Signature} containing the call arguments
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
        return Objects.hash(name, signature, sourceLocation);
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
        final FunctionCall other = (FunctionCall) obj;
        return Objects.equals(name, other.name) 
                && Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(FunctionCall.class.getSimpleName());
        result.append(" [name=");
        result.append(name);
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
     * Represents the argument list of a {@link FunctionCall}, holding an ordered
     * sequence of argument expressions passed to the callee.
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
         * Constructs a {@code Signature} without source-location information.
         *
         * @param parameters the list of argument expressions
         */
        public Signature(final List<JavascriptNode> parameters) {
            this(parameters, null);
        }

        /**
         * Constructs a {@code Signature} with full source-location information.
         *
         * @param parameters     the list of argument expressions
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
         * Sets the parent AST node of this {@code Signature}.
         *
         * @param parent the parent {@link AstNode}
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
         * Returns all argument expressions in call order.
         *
         * @return an unmodifiable list of argument nodes
         */
        public List<JavascriptNode> getParameters() {
            return parameters;
        }

        /**
         * Returns the argument at the given zero-based index, or {@code null} if the
         * index is out of range.
         *
         * @param index the zero-based position of the desired argument
         * @return the argument node, or {@code null}
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
            result.append(FunctionCall.class.getSimpleName());
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
