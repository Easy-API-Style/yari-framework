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
 * AST node representing a JavaScript arrow function, optionally asynchronous.
 * An arrow function is composed of a signature (list of parameters) and a body (procedure).
 */
@JsonPropertyOrder({"asynchronous", "signature", "procedure", "sourceLocation"})
public class ArrowFunction implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The asynchronous. */
    @JsonProperty("asynchronous")
    private final boolean asynchronous;
    /** The signature. */
    @JsonProperty("signature")
    private final JavascriptSignature signature;
    /** The procedure. */
    @JsonProperty("procedure")
    private final JavascriptProcedure procedure;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an arrow function with no source location.
     *
     * @param asynchronous {@code true} if the arrow function is declared {@code async}
     * @param signature    the signature containing the function parameters
     * @param procedure    the function body
     */
    public ArrowFunction(final boolean asynchronous,
                         final JavascriptSignature signature,
                         final JavascriptProcedure procedure) {
        this(asynchronous, signature, procedure, null);
    }

    /**
     * Constructs an arrow function with all its properties, used by Jackson during deserialization.
     *
     * @param asynchronous   {@code true} if the arrow function is declared {@code async}
     * @param signature      the signature containing the function parameters
     * @param procedure      the function body
     * @param sourceLocation the location of this node in the source file, may be {@code null}
     */
    @JsonCreator
    public ArrowFunction(@JsonProperty("asynchronous") final boolean asynchronous,
                         @JsonProperty("signature") final JavascriptSignature signature,
                         @JsonProperty("procedure")final JavascriptProcedure procedure,
                         @JsonProperty("sourceLocation")final SourceLocation sourceLocation) {
        super();
        this.asynchronous = asynchronous;
        this.signature = signature;
        this.procedure = procedure;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(signature, procedure);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this arrow function in the AST.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Indicates whether the arrow function is declared asynchronous.
     *
     * @return {@code true} if the function is {@code async}, {@code false} otherwise
     */
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Returns the signature of the arrow function, containing the list of its parameters.
     *
     * @return the signature of the arrow function
     */
    public JavascriptSignature getSignature() {
        return signature;
    }

    /**
     * Returns the body (procedure) of the arrow function.
     *
     * @return the body of the arrow function
     */
    public JavascriptProcedure getProcedure() {
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
        return Objects.hash(asynchronous, procedure, signature, sourceLocation);
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
        final ArrowFunction other = (ArrowFunction) obj;
        return asynchronous == other.asynchronous
                && Objects.equals(procedure, other.procedure)
                && Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ArrowFunction.class.getSimpleName());
        result.append(" [asynchronous=");
        result.append(asynchronous);
        result.append(", signature=");
        result.append(signature);
        result.append(", procedure=");
        result.append(procedure);
        if (sourceLocation != null) {
             result.append(", sourceLocation=");
             result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

    /**
     * AST node representing the signature of an arrow function, that is, the list of its parameters.
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
         * Constructs an arrow function signature with no source location.
         *
         * @param parameters the list of function parameters
         */
        public Signature(final List<JavascriptNode> parameters) {
           this(parameters, null);
        }

        /**
         * Constructs an arrow function signature with all its properties, used by Jackson during deserialization.
         *
         * @param parameters     the list of function parameters
         * @param sourceLocation the location of this node in the source file, may be {@code null}
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
         * Indicates whether the signature contains at least one parameter.
         *
         * @return {@code true} if the parameter list is non-empty, {@code false} otherwise
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
         * Returns the list of parameters in the signature.
         *
         * @return the list of parameters
         */
        public List<JavascriptNode> getParameters() {
            return parameters;
        }

        /**
         * Returns the parameter at the given index, or {@code null} if the index is out of bounds.
         *
         * @param index the zero-based index of the parameter to retrieve
         * @return the parameter at the specified index, or {@code null} if the index is invalid
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
            result.append(ArrowFunction.class.getSimpleName());
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
     * AST node representing the body (procedure) of an arrow function, containing the list of statements or expressions.
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
         * Constructs an arrow function body with no source location.
         *
         * @param nodes the list of nodes making up the function body
         */
        public Procedure(final List<JavascriptNode> nodes) {
           this(nodes, null);
        }

        /**
         * Constructs an arrow function body with all its properties, used by Jackson during deserialization.
         *
         * @param nodes          the list of nodes making up the function body
         * @param sourceLocation the location of this node in the source file, may be {@code null}
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
         * Sets the parent node of this function body in the AST.
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
            result.append(ArrowFunction.class.getSimpleName());
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
