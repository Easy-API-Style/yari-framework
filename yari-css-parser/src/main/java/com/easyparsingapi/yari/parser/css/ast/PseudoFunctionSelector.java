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
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing a CSS functional pseudo-selector (e.g. {@code :nth-child(2n+1)}),
 * composed of a base pseudo-selector and a signature containing the function parameter.
 */
@JsonPropertyOrder({"pseudoSelector", "signature", "sourceLocation"})
public class PseudoFunctionSelector implements PseudoSelector {

    private static final long serialVersionUID = 1L;

    /** The pseudo selector. */
    @JsonProperty("pseudoSelector")
    private final PseudoSelector pseudoSelector;
    /** The function signature. */
    @JsonProperty("signature")
    private final Signature signature;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code PseudoFunctionSelector} without a source location.
     *
     * @param pseudoSelector the base pseudo-selector (e.g. {@code :nth-child})
     * @param signature      the functional signature containing the parameter
     */
    public PseudoFunctionSelector(final PseudoSelector pseudoSelector,
                                  final Signature signature) {
        this(pseudoSelector, signature, null);
    }

    /**
     * Constructs a {@code PseudoFunctionSelector} with a source location, used during
     * JSON deserialization.
     *
     * @param pseudoSelector   the base pseudo-selector
     * @param signature        the functional signature containing the parameter
     * @param sourceLocation   the location in the original CSS source, or {@code null}
     */
    @JsonCreator
    public PseudoFunctionSelector(@JsonProperty("pseudoSelector") final PseudoSelector pseudoSelector,
                                  @JsonProperty("signature") final Signature signature,
                                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.pseudoSelector = pseudoSelector;
        this.signature = signature;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the base pseudo-selector of this functional selector.
     *
     * @return the base pseudo-selector
     */
    public CssSelector getPseudoSelector() {
        return pseudoSelector;
    }

    /**
     * Returns the functional signature of this pseudo-selector, containing its parameter.
     *
     * @return the signature of the functional pseudo-selector
     */
    public Signature getSignature() {
        return signature;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(pseudoSelector, signature);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the AST.
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

    /**
     * Compares this node to another {@link AstNode} ignoring source location,
     * by checking equality of the pseudo-selector and the signature.
     *
     * @param astNode the node to compare
     * @return {@code true} if both nodes are structurally equivalent
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
        final PseudoFunctionSelector other = (PseudoFunctionSelector) astNode;
        return Objects.equals(pseudoSelector, other.pseudoSelector)
                 && Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(pseudoSelector, signature, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof PseudoFunctionSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(PseudoFunctionSelector.class.getSimpleName());
        result.append(" [pseudoSelector=");
        result.append(pseudoSelector);
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
     * Represents the functional signature of a {@link PseudoFunctionSelector}, that is,
     * the list of parameters passed to the pseudo-selector function (e.g. {@code 2n+1}
     * in {@code :nth-child(2n+1)}).
     */
    @JsonPropertyOrder({"parameter", "sourceLocation"})
    public static class Signature implements CssSignature {

        private static final long serialVersionUID = 1L;

        /** The parameter. */
        @JsonProperty("parameter")
        private CssNode parameter;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;

        /**
         * Constructs a {@code Signature} without a source location.
         *
         * @param parameter the CSS node representing the function parameter
         */
        public Signature(final CssNode parameter) {
            this(parameter, null);
        }

        /**
         * Constructs a {@code Signature} with a source location, used during
         * JSON deserialization.
         *
         * @param parameter      the CSS node representing the function parameter
         * @param sourceLocation the location in the original CSS source, or {@code null}
         */
        @JsonCreator
        public Signature(@JsonProperty("parameter") final CssNode parameter,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.parameter = parameter;
            this.sourceLocation = sourceLocation;
            CssUtil.setAstParent(this);
        }

        /**
         * Returns the CSS node representing the parameter of this signature.
         *
         * @return the parameter of the signature
         */
        public CssNode getParameter() {
            return parameter;
        }

        /**
         * Returns a stream containing the single parameter of this signature.
         *
         * @return a {@link Stream} containing the parameter
         */
        @Override
        public Stream<CssNode> streamParameters() {
            return Stream.of(parameter);
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

        /**
         * Compares this signature to another {@link AstNode} ignoring source location,
         * by checking equality of the parameter.
         *
         * @param astNode the node to compare
         * @return {@code true} if both signatures are structurally equivalent
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
            return Objects.equals(parameter, other.parameter);
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(parameter, sourceLocation);
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
            result.append(PseudoFunctionSelector.class.getSimpleName());
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
