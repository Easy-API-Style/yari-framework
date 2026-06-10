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
 * AST node representing a JavaScript {@code catch} block, composed of an
 * optional signature (exception parameter) and a handler body.
 */
@JsonPropertyOrder({"values", "sourceLocation"})
public class Catch implements JavascriptNode {

    private static final long serialVersionUID = 1L;

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
     * Creates a {@code catch} node with no source location information.
     *
     * @param signature the catch block signature (exception parameter), may be {@code null}
     * @param procedure the catch block body containing the instructions to execute
     */
    public Catch(final Signature signature,
                 final Procedure procedure) {
        this(signature, procedure, null);
    }

    /**
     * Creates a {@code catch} node with all its properties, used during JSON deserialization.
     *
     * @param signature      the catch block signature (exception parameter), may be {@code null}
     * @param procedure      the catch block body containing the instructions to execute
     * @param sourceLocation the location of this node in the source, may be {@code null}
     */
    @JsonCreator
    public Catch(@JsonProperty("signature") final Signature signature,
                 @JsonProperty("procedure") final Procedure procedure,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
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
     * Sets the parent node of this node in the AST.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Indicates whether this {@code catch} block has a signature (exception parameter).
     *
     * @return {@code true} if the signature is present, {@code false} otherwise
     */
    public boolean hasSignature() {
        return signature != null;
    }

    /**
     * Returns the signature of the {@code catch} block, that is, the exception parameter.
     *
     * @return the signature, or {@code null} if absent
     */
    public Signature getSignature() {
        return signature;
    }

    /**
     * Returns the body of the {@code catch} block containing the instructions to execute.
     *
     * @return the body of the catch block
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
        return Objects.hash(procedure, signature, sourceLocation);
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
        final Catch other = (Catch) obj;
        return Objects.equals(procedure, other.procedure)
                && Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Catch.class.getSimpleName());
        result.append(" [signature=");
        result.append(signature);
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
     * AST node representing the signature of a {@code catch} block, that is,
     * the exception parameter declared between parentheses.
     */
    @JsonPropertyOrder({"parameter", "sourceLocation"})
    public static class Signature  implements JavascriptSignature {

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
         * Creates a {@code catch} block signature with no source location information.
         *
         * @param parameter the node representing the exception parameter
         */
        public Signature(final JavascriptNode parameter) {
            this(parameter, null);
        }

        /**
         * Creates a {@code catch} block signature with all its properties, used during JSON deserialization.
         *
         * @param parameter      the node representing the exception parameter
         * @param sourceLocation the location of this node in the source, may be {@code null}
         */
        @JsonCreator
        public Signature(@JsonProperty("parameter")  final JavascriptNode parameter,
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
         * @param parent the parent node
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
         * Returns the node representing the exception parameter of the signature.
         *
         * @return the exception parameter
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
            result.append(Catch.class.getSimpleName());
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

    /**
     * AST node representing the body of a {@code catch} block, containing the list
     * of instructions to execute when the exception is caught.
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
         * Creates a {@code catch} block body with no source location information.
         *
         * @param nodes the list of instruction nodes making up the block body
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Creates a {@code catch} block body with all its properties, used during JSON deserialization.
         *
         * @param nodes          the list of instruction nodes making up the block body
         * @param sourceLocation the location of this node in the source, may be {@code null}
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
         * Sets the parent node of this block body in the AST.
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
            result.append(Catch.class.getSimpleName());
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
