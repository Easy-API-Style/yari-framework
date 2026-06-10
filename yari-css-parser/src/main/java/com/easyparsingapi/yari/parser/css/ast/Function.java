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
package com.easyparsingapi.yari.parser.css.ast;

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
 * CSS syntax tree node representing a CSS function call,
 * composed of a name identifier and a signature containing its parameters.
 */
@JsonPropertyOrder({"name", "signature", "sourceLocation"})
public class Function implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The function name. */
    @JsonProperty("name")
    private final Identifier name;
    /** The function signature. */
    @JsonProperty("signature")
    private final Signature signature;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a CSS function with its name and signature, without location information.
     *
     * @param name      the identifier of the function name
     * @param signature the signature of the function containing its parameters
     */
    public Function(final Identifier name,
                    final Signature signature) {
        this(name, signature, null);
    }

    /**
     * Constructs a CSS function with its name, signature, and location in the source.
     *
     * @param name           the identifier of the function name
     * @param signature      the signature of the function containing its parameters
     * @param sourceLocation the location of this node in the source file
     */
    @JsonCreator
    public Function(@JsonProperty("name") final Identifier name,
                    @JsonProperty("signature") final Signature signature,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.signature = signature;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the identifier representing the name of this CSS function.
     *
     * @return the identifier of the function name
     */
    public Identifier getName() {
        return name;
    }

    /**
     * Returns the signature of this CSS function containing its parameters.
     *
     * @return the signature of the function
     */
    public Signature getSignature() {
        return signature;
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
     * Sets the parent node of this function in the syntax tree.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
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
        final Function other = (Function) astNode;
        return Objects.equals(name, other.name)
                 && Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(name, signature, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Function node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Function.class.getSimpleName());
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
     * Signature of a CSS function, grouping the ordered list of its parameters.
     */
    @JsonPropertyOrder({"parameters", "sourceLocation"})
    public static class Signature implements CssSignature {

        private static final long serialVersionUID = 1L;

        /** The list of parameter nodes. */
        @JsonProperty("parameters")
        private final List<CssNode> parameters;
        /** The parent AST node. */
        @JsonIgnore
        private AstNode parent;
        /** The source location. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;

        /**
         * Constructs a function signature with the list of its parameters, without a location.
         *
         * @param parameters the list of CSS nodes representing the parameters
         */
        public Signature(final List<CssNode> parameters) {
            this(parameters, null);
        }

        /**
         * Constructs a function signature with the list of its parameters and its location in the source.
         *
         * @param parameters     the list of CSS nodes representing the parameters
         * @param sourceLocation the location of this node in the source file
         */
        @JsonCreator
        public Signature(@JsonProperty("parameters") final List<CssNode> parameters,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.parameters = CollectionUtil.nullToEmpty(parameters);
            this.sourceLocation = sourceLocation;
            CssUtil.setAstParent(this);
        }

        /**
         * Returns the number of parameters present in this signature.
         *
         * @return the number of parameters, or 0 if no parameters are present
         */
        public int size() {
            return hasParameter() ? parameters.size() : 0;
        }

        /**
         * Indicates whether this signature contains at least one parameter.
         *
         * @return {@code true} if the parameter list is non-empty, {@code false} otherwise
         */
        public boolean hasParameter() {
            return !CollectionUtil.isEmpty(parameters);
        }

        /**
         * Returns the complete list of parameters of this signature.
         *
         * @return the list of CSS nodes representing the parameters
         */
        public List<CssNode> getParameters() {
            return parameters;
        }

        /**
         * Returns the parameter at the given index, or {@code null} if the index is out of bounds.
         *
         * @param index the (zero-based) index of the parameter to retrieve
         * @return the corresponding CSS node, or {@code null} if the index is invalid
         */
        public CssNode getParameter(final int index) {
            CssNode result = null;
            if (index < size()) {
                result = parameters.get(index);
            }
            return result;
        }

        /** {@inheritDoc} */
        @Override
        public Stream<CssNode> streamParameters() {
            return parameters.stream();
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
         * Sets the parent node of this signature in the syntax tree.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
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
            final Signature other = (Signature) astNode;
            return Objects.equals(parameters, other.parameters);
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(parameters, sourceLocation);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object object) {
            if (object instanceof Signature node) {
                return equalsNode(node)
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
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
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Function.class.getSimpleName());
            result.append(".");
            result.append(Signature.class.getSimpleName());
            result.append(" [parameters=");
            result.append(parameters);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }

}
