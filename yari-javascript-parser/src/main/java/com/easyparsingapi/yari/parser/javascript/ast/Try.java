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

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a {@code try / catch / finally} construct in the AST.
 * The try body is a {@link Procedure}; zero or more {@link Catch} blocks may follow,
 * and an optional {@link Finally} block may close the construct.
 */
@JsonPropertyOrder({"procedure", "catches", "finallyBlock", "sourceLocation"})
public class Try implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The procedure. */
    @JsonProperty("procedure") 
    private final Procedure procedure;
    /** The catches. */
    @JsonProperty("catches") 
    private final List<Catch> catches;
    /** The finallyBlock. */
    @JsonProperty("finallyBlock") 
    private final Finally finallyBlock;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code Try} without source-location information.
     *
     * @param procedure    the body of the {@code try} block
     * @param catches      the list of {@link Catch} clauses (may be empty)
     * @param finallyBlock the optional {@link Finally} block, or {@code null}
     */
    public Try(final Procedure procedure,
               final List<Catch> catches,
               final Finally finallyBlock) {
        this(procedure, catches, finallyBlock, null);
    }

    /**
     * Constructs a {@code Try} with full source-location information.
     *
     * @param procedure      the body of the {@code try} block
     * @param catches        the list of {@link Catch} clauses (may be empty)
     * @param finallyBlock   the optional {@link Finally} block, or {@code null}
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Try(@JsonProperty("procedure") final Procedure procedure,
               @JsonProperty("catches") final List<Catch> catches,
               @JsonProperty("finallyBlock") final Finally finallyBlock,
               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.procedure = procedure;
        this.catches = CollectionUtil.nullToEmpty(catches);
        this.finallyBlock = finallyBlock;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(procedure, catches, finallyBlock);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this try construct.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the body of the {@code try} block.
     *
     * @return the {@link Procedure} containing the try statements
     */
    public Procedure getProcedure() {
        return procedure;
    }

    /**
     * Returns {@code true} if at least one {@code catch} clause is present.
     *
     * @return {@code true} when one or more catch clauses exist
     */
    public boolean hasCatch() {
        return !catches.isEmpty();
    }

    /**
     * Returns the list of {@code catch} clauses.
     *
     * @return the ordered list of {@link Catch} nodes
     */
    public List<Catch> getCatches() {
        return catches;
    }

    /**
     * Returns the {@code catch} clause at the given zero-based index, or {@code null} if out of range.
     *
     * @param index zero-based position of the desired catch clause
     * @return the {@link Catch} at that index, or {@code null}
     */
    public Catch getCatch(final int index) {
        Catch result = null;
        if (index < catches.size()) {
            result = catches.get(index);
        }
        return result;
    }

    /**
     * Returns {@code true} if a {@code finally} block is present.
     *
     * @return {@code true} when a finally block exists
     */
    public boolean hasFinally() {
        return finallyBlock != null;
    }

    /**
     * Returns the {@code finally} block, or {@code null} if absent.
     *
     * @return the {@link Finally} node, or {@code null}
     */
    public Finally getFinally() {
        return finallyBlock;
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
        return Objects.hash(catches, finallyBlock, procedure, sourceLocation);
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
        final Try other = (Try) obj;
        return Objects.equals(catches, other.catches) 
                && Objects.equals(finallyBlock, other.finallyBlock)
                && Objects.equals(procedure, other.procedure);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Try.class.getSimpleName());
        result.append(" [procedure=");
        result.append(procedure.size());
        result.append(", catches=");
        result.append(catches);
        result.append(", finally=");
        result.append(finallyBlock);
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
     * The body of the {@code try} block as an ordered list of statements.
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
         * @param nodes the list of statements forming the try body
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Constructs a {@code Procedure} with full source-location information.
         *
         * @param nodes          the list of statements forming the try body
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
         * Sets the parent AST node of this try body.
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
            result.append(Try.class.getSimpleName());
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
