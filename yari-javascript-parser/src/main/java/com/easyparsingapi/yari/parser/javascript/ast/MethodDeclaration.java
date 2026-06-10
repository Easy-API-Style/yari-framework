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
 * AST node representing a JavaScript method declaration inside a class body.
 * <p>
 * Captures whether the method is a generator ({@code function*}) or asynchronous
 * ({@code async}), together with its name, parameter signature, and body procedure.
 * </p>
 */
@JsonPropertyOrder({"generator", "asynchronous", "name",
                    "signature", "procedure",
                    "sourceLocation"})
public class MethodDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The generator. */
    @JsonProperty("generator") 
    private final boolean generator;
    /** The asynchronous. */
    @JsonProperty("asynchronous") 
    private final boolean asynchronous;
    /** The name. */
    @JsonProperty("name") 
    private final JavascriptNode name;
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
     * Constructs a {@code MethodDeclaration} without source location information.
     *
     * @param generator    {@code true} if the method is a generator function
     * @param asynchronous {@code true} if the method is declared with {@code async}
     * @param name         the name of the method
     * @param signature    the parameter list of the method
     * @param procedure    the body of the method
     */
    public MethodDeclaration(final boolean generator,
                             final boolean asynchronous,
                             final JavascriptNode name,
                             final Signature signature,
                             final Procedure procedure) {
        this(generator, asynchronous, name, signature, procedure, null);
    }
    
    /**
     * Constructs a {@code MethodDeclaration} with all components.
     *
     * @param generator      {@code true} if the method is a generator function
     * @param asynchronous   {@code true} if the method is declared with {@code async}
     * @param name           the name of the method
     * @param signature      the parameter list of the method
     * @param procedure      the body of the method
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public MethodDeclaration(@JsonProperty("generator") final boolean generator,
                             @JsonProperty("asynchronous") final boolean asynchronous,
                             @JsonProperty("name") final JavascriptNode name,
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
     * Sets the parent AST node of this node.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns whether this method is a generator function.
     *
     * @return {@code true} if the method uses the {@code function*} syntax
     */
    public boolean isGenerator() {
        return generator;
    }

    /**
     * Returns whether this method is declared as asynchronous.
     *
     * @return {@code true} if the method is declared with {@code async}
     */
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Returns the name node of the method.
     *
     * @return the method name node
     */
    public JavascriptNode getName() {
        return name;
    }

    /**
     * Returns the signature (parameter list) of the method.
     *
     * @return the {@link Signature} node
     */
    public Signature getSignature() {
        return signature;
    }

    /**
     * Returns the procedure (body) of the method.
     *
     * @return the {@link Procedure} node
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
        return Objects.hash(asynchronous, generator, 
                           name, procedure, signature, 
                           /** Field. */
                           sourceLocation);
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
        final MethodDeclaration other = (MethodDeclaration) obj;
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
        result.append(MethodDeclaration.class.getSimpleName());
        result.append(" [generator=");
        result.append(generator);
        result.append(", asynchronous=");
        result.append(asynchronous);
        result.append(", name=");
        result.append(name);
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
     * AST node representing the parameter list (signature) of a method declaration.
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
         * @param parameters the list of parameter nodes; {@code null} is treated as empty
         */
        public Signature(final List<JavascriptNode> parameters) {
            this(parameters, null);
        }

        /**
         * Constructs a {@code Signature} with all components.
         *
         * @param parameters     the list of parameter nodes; {@code null} is treated as empty
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
         * Sets the parent AST node of this node.
         *
         * @param parent the parent node
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the number of parameters in this signature.
         *
         * @return the parameter count
         */
        public int size() {
            return parameters.size();
        }

        /**
         * Returns whether this signature contains at least one parameter.
         *
         * @return {@code true} if the parameter list is not empty
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
         * Returns the list of parameter nodes.
         *
         * @return the list of parameter nodes
         */
        public List<JavascriptNode> getParameters() {
            return parameters;
        }

        /**
         * Returns the parameter at the specified index, or {@code null} if the index
         * is out of bounds.
         *
         * @param index the zero-based index of the parameter
         * @return the parameter node, or {@code null} if the index is out of range
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
            result.append(MethodDeclaration.class.getSimpleName());
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
     * AST node representing the body (procedure) of a method declaration,
     * containing the list of statement nodes enclosed in braces.
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
         * Constructs a {@code Procedure} without source location information.
         *
         * @param nodes the list of statement nodes in the method body; {@code null} is treated as empty
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Constructs a {@code Procedure} with all components.
         *
         * @param nodes          the list of statement nodes in the method body; {@code null} is treated as empty
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
         * Sets the parent AST node of this node.
         *
         * @param parent the parent node
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the number of statement nodes in this procedure body.
         *
         * @return the statement count
         */
        public int size() {
            return nodes.size();
        }

        /** {@inheritDoc} */
        @Override
        public List<JavascriptNode> getNodes() {
            return nodes;
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
            result.append(MethodDeclaration.class.getSimpleName());
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
