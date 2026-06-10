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
 * Represents a JavaScript function declaration (or function expression) in the AST.
 * Captures whether the function is a generator ({@code function*}), asynchronous
 * ({@code async function}), or anonymous, together with its optional name,
 * parameter list ({@link Signature}), and body ({@link Procedure}).
 */
@JsonPropertyOrder({"generator", "asynchronous", "name", "signature", "procedure", "sourceLocation"})
public class FunctionDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The generator. */
    @JsonProperty("generator") 
    private final boolean generator;
    /** The asynchronous. */
    @JsonProperty("asynchronous") 
    private final boolean asynchronous;
    /** The name. */
    @JsonProperty("name") 
    private final Identifier name;
    /** The signature. */
    @JsonProperty("signature") 
    private final Signature signature;
    /** The procedure. */
    @JsonProperty("procedure") 
    private final Procedure procedure;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code FunctionDeclaration} without source-location information.
     *
     * @param generator    {@code true} if this is a generator function ({@code function*})
     * @param asynchronous {@code true} if this is an async function ({@code async function})
     * @param name         the function name, or {@code null} for anonymous functions
     * @param signature    the parameter list
     * @param procedure    the function body
     */
    public FunctionDeclaration(final boolean generator,
                               final boolean asynchronous,
                               final Identifier name,
                               final Signature signature,
                               final Procedure procedure) {
        this(generator, asynchronous, name, signature, procedure, null);
    }

    /**
     * Constructs a {@code FunctionDeclaration} with full source-location information.
     *
     * @param generator      {@code true} if this is a generator function ({@code function*})
     * @param asynchronous   {@code true} if this is an async function ({@code async function})
     * @param name           the function name, or {@code null} for anonymous functions
     * @param signature      the parameter list
     * @param procedure      the function body
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public FunctionDeclaration(@JsonProperty("generator") final boolean generator,
                               @JsonProperty("asynchronous") final boolean asynchronous,
                               @JsonProperty("name") final Identifier name,
                               @JsonProperty("signature") final Signature signature,
                               @JsonProperty("procedure") final Procedure procedure,
                                @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.generator = generator;
        this.asynchronous = asynchronous;
        this.name = name;
        this.signature = signature;
        this.procedure = procedure;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
     public List<AstNode> astChildren() {
         return AstNode.childrenAttributes(name, signature, procedure);
     }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }
    
    /**
     * Sets the parent AST node of this function declaration.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns {@code true} if this is a generator function ({@code function*}).
     *
     * @return {@code true} for generator functions
     */
    public boolean isGenerator() {
        return generator;
    }

    /**
     * Returns {@code true} if this is an asynchronous function ({@code async function}).
     *
     * @return {@code true} for async functions
     */
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Returns {@code true} if this function has no name (i.e. is a function expression
     * without an identifier).
     *
     * @return {@code true} when the function is anonymous
     */
    public boolean isAnonymous() {
        return name == null;
    }

    /**
     * Returns the function name identifier, or {@code null} if the function is anonymous.
     *
     * @return the name identifier, or {@code null}
     */
    public Identifier getName() {
        return name;
    }

    /**
     * Returns the parameter list of this function.
     *
     * @return the {@link Signature} containing the formal parameters
     */
    public Signature getSignature() {
        return signature;
    }

    /**
     * Returns the body of this function.
     *
     * @return the {@link Procedure} containing the function statements
     */
    public Procedure getProcedure() {
        return procedure;
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
        return Objects.hash(asynchronous, generator, name, procedure, signature, sourceLocation);
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
        final FunctionDeclaration other = (FunctionDeclaration) obj;
        return asynchronous == other.asynchronous
                && generator == other.generator
                && Objects.equals(name, other.name)
                && Objects.equals(procedure, other.procedure)
                && Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(FunctionDeclaration.class.getSimpleName());
        result.append(" [asynchronous=");
        result.append(asynchronous);
        result.append(", generator=");
        result.append(generator);
        if (!isAnonymous()) {
            result.append(", name=");
            result.append(name);
        }
        result.append(", signature=");
        result.append(signature.size());
        result.append(", procedure=");
        result.append(procedure.size());
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
     * Represents the formal parameter list of a {@link FunctionDeclaration}.
     * Each element is a {@link JavascriptNode} that may be a plain identifier,
     * a default-value assignment, a rest parameter, or a destructuring pattern.
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
         * @param parameters the list of formal parameter nodes
         */
        public Signature(final List<JavascriptNode> parameters) {
           this(parameters, null);
        }

        /**
         * Constructs a {@code Signature} with full source-location information.
         *
         * @param parameters     the list of formal parameter nodes
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
         * Returns the number of formal parameters in this signature.
         *
         * @return the parameter count
         */
        public int size() {
            return parameters.size();
        }

        /**
         * Returns {@code true} if this signature contains at least one parameter.
         *
         * @return {@code true} when one or more parameters are present
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
         * Returns the ordered list of formal parameter nodes.
         *
         * @return an unmodifiable list of parameter {@link JavascriptNode}s
         */
        public List<JavascriptNode> getParameters() {
            return parameters;
        }
        
        /**
         * Returns the formal parameter at the given zero-based index,
         * or {@code null} if the index is out of range.
         *
         * @param index zero-based position of the desired parameter
         * @return the parameter {@link JavascriptNode} at that index, or {@code null}
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
            result.append(FunctionDeclaration.class.getSimpleName());
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
    
    /**
     * Represents the body of a {@link FunctionDeclaration} as an ordered list of
     * {@link JavascriptNode} statements.
     */
    @JsonPropertyOrder({"nodes", "sourceLocation"})
    public static class Procedure implements JavascriptProcedure {

        private static final long serialVersionUID = 1L; 
        
        /** The nodes. */
        @JsonProperty("nodes") 
        private final List<JavascriptNode> nodes;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;
        
        /**
         * Constructs a {@code Procedure} without source-location information.
         *
         * @param nodes the list of statements forming the function body
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Constructs a {@code Procedure} with full source-location information.
         *
         * @param nodes          the list of statements forming the function body
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Procedure(@JsonProperty("nodes") final List<JavascriptNode> nodes,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.nodes = CollectionUtil.nullToEmpty(nodes);
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(nodes);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }
        
        /**
         * Sets the parent AST node of this {@code Procedure}.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /** {@inheritDoc} */
        @Override
        public int size() {
            return nodes.size();
        }
        
        /** {@inheritDoc} */
        @Override
        public boolean hasNode() {
            return !nodes.isEmpty();
        }
        
        /** {@inheritDoc} */
        @Override
        public Stream<JavascriptNode> streamNodes() {
            return nodes.stream();
        }
        
        /** {@inheritDoc} */
        @Override
        public List<JavascriptNode> getNodes() {
            return nodes;
        }
        
        /** {@inheritDoc} */
        @Override
        public JavascriptNode getNode(final int index) {
            JavascriptNode result = null;
            if (index < size()) {
                result = nodes.get(index);
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
            return Objects.hash(nodes, sourceLocation);
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
            final Procedure other = (Procedure) obj;
            return Objects.equals(nodes, other.nodes);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(FunctionDeclaration.class.getSimpleName());
            result.append(".");
            result.append(Procedure.class.getSimpleName());
            result.append(" [nodes=");
            result.append(nodes.size());
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }

}
