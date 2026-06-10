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
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing a dynamic {@code import()} function call in JavaScript.
 * Wraps a single-parameter {@link Signature} node that holds the module specifier.
 */
@JsonPropertyOrder({"signature", "sourceLocation"})
public class ImportFunctionCall implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
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
     * Constructs an {@code ImportFunctionCall} without source location information.
     *
     * @param signature the signature holding the module specifier parameter
     */
    public ImportFunctionCall(final Signature signature) {
        this(signature, null);
    }

    /**
     * Constructs an {@code ImportFunctionCall} with full source location information.
     *
     * @param signature      the signature holding the module specifier parameter
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public ImportFunctionCall(@JsonProperty("signature") final Signature signature,
                              @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.signature = signature;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(signature);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this dynamic import call in the AST.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the signature holding the module specifier parameter of this
     * dynamic {@code import()} call.
     *
     * @return the {@link Signature} of this import call
     */
    public ImportFunctionCall.Signature getSignature() {
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
        return Objects.hash(signature, sourceLocation);
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
        final ImportFunctionCall other = (ImportFunctionCall) obj;
        return Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ImportFunctionCall.class.getSimpleName());
        result.append(" [signature=");
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
     * Represents the single-parameter signature of a dynamic {@code import()} call,
     * holding the module specifier expression.
     */
    @JsonPropertyOrder({"parameter", "sourceLocation"})
    public static class Signature implements JavascriptSignature {

        private static final long serialVersionUID = 1L; 
        
        /** The parameter. */
        @JsonProperty("parameter") 
        private final JavascriptNode parameter;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;
        
        /**
         * Constructs a {@code Signature} without source location information.
         *
         * @param parameter the module specifier expression node
         */
        public Signature(final JavascriptNode parameter) {
            this(parameter, null);
        }

        /**
         * Constructs a {@code Signature} with full source location information.
         *
         * @param parameter      the module specifier expression node
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Signature(@JsonProperty("parameter") final JavascriptNode parameter,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.parameter = parameter;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(parameter);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent node of this signature in the AST.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /** {@inheritDoc} */
        @Override
        public Stream<JavascriptNode> streamParameters() {
            return Stream.of(parameter);
        }

        /**
         * Returns the module specifier expression passed to the dynamic {@code import()} call.
         *
         * @return the parameter expression node
         */
        public JavascriptNode getParameter() {
            return parameter;
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
            return Objects.hash(parameter, sourceLocation);
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
            return Objects.equals(parameter, other.parameter);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(ImportFunctionCall.class.getSimpleName());
            result.append(".");
            result.append(Signature.class.getSimpleName());
            result.append(" [parameter=");
            result.append(parameter);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }
    
}