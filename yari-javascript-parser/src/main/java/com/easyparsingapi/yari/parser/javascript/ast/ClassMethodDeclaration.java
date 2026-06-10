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
 * AST node representing the declaration of a method in a JavaScript class.
 * <p>
 * A class method can be static, private, a generator, and/or asynchronous.
 * It is composed of a name, a {@link Signature} (list of parameters), and a
 * {@link Procedure} (method body).
 * </p>
 */
@JsonPropertyOrder({"staticMethod", "generator", "asynchronous", "privateMethod",
                    "name", "signature", "procedure", "sourceLocation"})
public class ClassMethodDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The staticMethod. */
    @JsonProperty("staticMethod")
    private final boolean staticMethod;
    /** The generator. */
    @JsonProperty("generator")
    private final boolean generator;
    /** The asynchronous. */
    @JsonProperty("asynchronous")
    private final boolean asynchronous;
    /** The privateMethod. */
    @JsonProperty("privateMethod")
    private final boolean privateMethod;
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
     * Constructs a class method declaration with no source location.
     *
     * @param staticMethod  {@code true} if the method is static
     * @param generator     {@code true} if the method is a generator
     * @param asynchronous  {@code true} if the method is asynchronous
     * @param privateMethod {@code true} if the method is private
     * @param name          node representing the method name
     * @param signature     signature (parameters) of the method
     * @param procedure     body of the method
     */
    public ClassMethodDeclaration(final boolean staticMethod,
                                  final boolean generator,
                                  final boolean asynchronous,
                                  final boolean privateMethod,
                                  final JavascriptNode name,
                                  final Signature signature,
                                  final Procedure procedure) {
        this(staticMethod, generator, asynchronous, privateMethod,
             /** Field. */
             name, signature, procedure, null);
    }

    /**
     * Constructs a class method declaration with all its properties, including the source location.
     * This constructor is used during JSON deserialization.
     *
     * @param staticMethod   {@code true} if the method is static
     * @param generator      {@code true} if the method is a generator
     * @param asynchronous   {@code true} if the method is asynchronous
     * @param privateMethod  {@code true} if the method is private
     * @param name           node representing the method name
     * @param signature      signature (parameters) of the method
     * @param procedure      body of the method
     * @param sourceLocation location of the method in the source, may be {@code null}
     */
    @JsonCreator
    public ClassMethodDeclaration(@JsonProperty("staticMethod") final boolean staticMethod,
                                  @JsonProperty("generator") final boolean generator,
                                  @JsonProperty("asynchronous") final boolean asynchronous,
                                  @JsonProperty("privateMethod") final boolean privateMethod,
                                  @JsonProperty("name") final JavascriptNode name,
                                  @JsonProperty("signature") final Signature signature,
                                  @JsonProperty("procedure") final Procedure procedure,
                                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.staticMethod = staticMethod;
        this.generator = generator;
        this.asynchronous = asynchronous;
        this.privateMethod = privateMethod;
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
     * Sets the parent node of this method declaration in the AST.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Indicates whether this method is declared static.
     *
     * @return {@code true} if the method is static
     */
    public boolean isStatic() {
        return staticMethod;
    }

    /**
     * Indicates whether this method is declared private.
     *
     * @return {@code true} if the method is private
     */
    public boolean isPirvate() {
        return privateMethod;
    }

    /**
     * Indicates whether this method is a generator (uses {@code function*}).
     *
     * @return {@code true} if the method is a generator
     */
    public boolean isGenerator() {
        return generator;
    }

    /**
     * Indicates whether this method is asynchronous (declared with {@code async}).
     *
     * @return {@code true} if the method is asynchronous
     */
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Returns the AST node representing the method name.
     *
     * @return the method name node
     */
    public JavascriptNode getName() {
        return name;
    }

    /**
     * Returns the signature (list of parameters) of the method.
     *
     * @return the method signature
     */
    public Signature getSignature() {
        return signature;
    }

    /**
     * Returns the body (procedure) of the method.
     *
     * @return the method body
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
        return Objects.hash(asynchronous, generator, name,
                            privateMethod, procedure, signature,
                            /** Field. */
                            staticMethod, sourceLocation);
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

    /**
     * Structurally compares this method declaration with another AST node,
     * without taking the source location into account.
     *
     * @param astNode the node to compare
     * @return {@code true} if both nodes represent the same method declaration
     */
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
        final ClassMethodDeclaration other = (ClassMethodDeclaration) astNode;
        return asynchronous == other.asynchronous
                && generator == other.generator
                && Objects.equals(name, other.name)
                && privateMethod == other.privateMethod
                && Objects.equals(procedure, other.procedure)
                && Objects.equals(signature, other.signature)
                && staticMethod == other.staticMethod;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ClassMethodDeclaration.class.getSimpleName());
        result.append(" [static=");
        result.append(staticMethod);
        result.append(", generator=");
        result.append(generator);
        result.append(", private=");
        result.append(privateMethod);
        result.append(", name=");
        result.append(name);
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
     * Signature of a JavaScript class method, representing the ordered list of its formal parameters.
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
         * Constructs a signature from a list of parameters, with no source location.
         *
         * @param parameters list of formal parameters of the method
         */
        public Signature(final List<JavascriptNode> parameters) {
            this(CollectionUtil.nullToEmpty(parameters), null);
        }

        /**
         * Constructs a signature with its parameters and source location.
         * This constructor is used during JSON deserialization.
         *
         * @param parameters     list of formal parameters of the method
         * @param sourceLocation location of the signature in the source, may be {@code null}
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
         * Sets the parent node of this signature in the AST.
         *
         * @param parent the parent node
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the number of parameters in the signature.
         *
         * @return the number of parameters
         */
        public int size() {
            return parameters.size();
        }

        /**
         * Indicates whether the signature has at least one parameter.
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
         * Returns the immutable list of parameters in the signature.
         *
         * @return the list of parameters
         */
        public List<JavascriptNode> getParameters() {
            return parameters;
        }

        /**
         * Returns the parameter at the specified index, or {@code null} if the index is out of bounds.
         *
         * @param index the zero-based index of the desired parameter
         * @return the parameter at that index, or {@code null} if the index is invalid
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

        /**
         * Structurally compares this signature with another AST node,
         * without taking the source location into account.
         *
         * @param astNode the node to compare
         * @return {@code true} if both nodes represent the same parameter list
         */
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
            final Signature other = (Signature) astNode;
            return Objects.equals(parameters, other.parameters);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(ClassMethodDeclaration.class.getSimpleName());
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
     * Body of a JavaScript class method, representing the ordered sequence of instructions that compose it.
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
         * Constructs a method body from a list of nodes, with no source location.
         *
         * @param nodes list of nodes (instructions) making up the method body
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Constructs a method body with its nodes and source location.
         * This constructor is used during JSON deserialization.
         *
         * @param nodes          list of nodes (instructions) making up the method body
         * @param sourceLocation location of the body in the source, may be {@code null}
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
         * Sets the parent node of this method body in the AST.
         *
         * @param parent the parent node
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
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

        /**
         * Structurally compares this method body with another AST node,
         * without taking the source location into account.
         *
         * @param astNode the node to compare
         * @return {@code true} if both nodes represent the same method body
         */
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
            final Procedure other = (Procedure) astNode;
            return Objects.equals(nodes, other.nodes);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(ClassMethodDeclaration.class.getSimpleName());
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
