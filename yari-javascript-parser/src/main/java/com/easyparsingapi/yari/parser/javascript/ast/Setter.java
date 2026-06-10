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
 * Represents a {@code set} accessor declaration in the AST
 * (e.g. {@code set foo(value) { ... }} in a class or object literal).
 * Holds the property name, a single-parameter {@link Signature}, and a {@link Procedure} body.
 */
@JsonPropertyOrder({"name", "signature", "procedure", "sourceLocation"})
public class Setter implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
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
     * Constructs a {@code Setter} without source-location information.
     *
     * @param name      the property name expression
     * @param signature the single-parameter signature
     * @param procedure the setter body
     */
    public Setter(final JavascriptNode name,
                  final Signature signature,
                  final Procedure procedure) {
        this(name, signature, procedure, null);
    }

    /**
     * Constructs a {@code Setter} with full source-location information.
     *
     * @param name           the property name expression
     * @param signature      the single-parameter signature
     * @param procedure      the setter body
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Setter(@JsonProperty("name") final JavascriptNode name,
                  @JsonProperty("signature") final Signature signature,
                  @JsonProperty("procedure") final Procedure procedure,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
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
     * Sets the parent AST node of this setter declaration.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the property name expression of this setter.
     *
     * @return the name {@link JavascriptNode}
     */
    public JavascriptNode getName() {
        return name;
    }

    /**
     * Returns the single-parameter signature of this setter.
     *
     * @return the {@link Signature}
     */
    public Signature getSignature() {
        return signature;
    }

    /**
     * Returns the body of this setter.
     *
     * @return the {@link Procedure}
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
        return Objects.hash(name, procedure, signature, sourceLocation);
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
        final Setter other = (Setter) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(procedure, other.procedure)
                && Objects.equals(signature, other.signature);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Setter.class.getSimpleName());
        result.append(" [name=");
        result.append(name);
        result.append(", signature=");
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
    
    /**
     * The single-parameter signature of a {@link Setter} accessor.
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
         * Constructs a {@code Signature} without source-location information.
         *
         * @param parameter the single setter parameter node
         */
        public Signature(final JavascriptNode parameter) {
            this(parameter, null);
        }

        /**
         * Constructs a {@code Signature} with full source-location information.
         *
         * @param parameter      the single setter parameter node
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
         * Sets the parent AST node of this signature.
         *
         * @param parent the parent {@link AstNode}
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
         * Returns the single setter parameter.
         *
         * @return the parameter {@link JavascriptNode}
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
            result.append(Setter.class.getSimpleName());
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
     * The body of a {@link Setter} accessor as an ordered list of statements.
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
         * @param nodes the list of statements forming the setter body
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Constructs a {@code Procedure} with full source-location information.
         *
         * @param nodes          the list of statements forming the setter body
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
         * Sets the parent AST node of this setter body.
         *
         * @param parent the parent {@link AstNode}
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
            result.append(Setter.class.getSimpleName());
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
