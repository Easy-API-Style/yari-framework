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

import java.util.ArrayList;
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
 * Represents a JavaScript {@code if} / {@code else if} / {@code else} statement in the AST.
 * The mandatory {@link IfBlock} holds the {@code if} condition and branch; zero or more
 * {@link ElseIfBlock}s hold the {@code else if} branches; the optional {@link ElseBlock}
 * holds the final {@code else} branch.
 */
@JsonPropertyOrder({"ifBlock", "elseIfBlocks", "elseBlock", "sourceLocation"})
public class If implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The ifBlock. */
    @JsonProperty("ifBlock") 
    private final IfBlock ifBlock;
    /** The elseIfBlocks. */
    @JsonProperty("elseIfBlocks") 
    private final List<ElseIfBlock> elseIfBlocks;
    /** The elseBlock. */
    @JsonProperty("elseBlock") 
    private final ElseBlock elseBlock;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code If} statement without source-location information.
     *
     * @param ifBlock      the mandatory {@code if} branch
     * @param elseIfBlocks the (possibly empty) list of {@code else if} branches
     * @param elseBlock    the optional {@code else} branch, or {@code null}
     */
    public If(final IfBlock ifBlock,
              final List<ElseIfBlock> elseIfBlocks,
              final ElseBlock elseBlock) {
        this(ifBlock, elseIfBlocks, elseBlock, null);
    }

    /**
     * Constructs an {@code If} statement with full source-location information.
     *
     * @param ifBlock        the mandatory {@code if} branch
     * @param elseIfBlocks   the (possibly empty) list of {@code else if} branches
     * @param elseBlock      the optional {@code else} branch, or {@code null}
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public If(@JsonProperty("ifBlock") final IfBlock ifBlock,
              @JsonProperty("elseIfBlocks") final List<ElseIfBlock> elseIfBlocks,
              @JsonProperty("elseBlock") final ElseBlock elseBlock,
              @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.ifBlock = ifBlock;
        this.elseIfBlocks = CollectionUtil.nullToEmpty(elseIfBlocks);
        this.elseBlock = elseBlock;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(ifBlock, elseIfBlocks, elseBlock);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this {@code If} statement.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the mandatory {@code if} branch of this statement.
     *
     * @return the {@link IfBlock}
     */
    public IfBlock getIfBlock() {
        return ifBlock;
    }

    /**
     * Returns the list of {@code else if} branches of this statement.
     *
     * @return a (possibly empty) list of {@link ElseIfBlock}s
     */
    public List<ElseIfBlock> getElseIfBlocks() {
        return elseIfBlocks;
    }

    /**
     * Returns the {@code else} branch of this statement, or {@code null} if absent.
     *
     * @return the {@link ElseBlock}, or {@code null}
     */
    public ElseBlock getElseBlock() {
        return elseBlock;
    }

    /**
     * Returns a combined list of all conditional blocks: the {@link IfBlock} followed
     * by all {@link ElseIfBlock}s, in source order.
     *
     * @return a new list containing the {@code if} block and all {@code else if} blocks
     */
    @JsonIgnore
    public List<IfBlock> getIfBlocks() {
        final List<IfBlock> result = new ArrayList<>();
        result.add(ifBlock);
        result.addAll(elseIfBlocks);
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
        return Objects.hash(elseBlock, elseIfBlocks, ifBlock, sourceLocation);
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
        final If other = (If) obj;
        return Objects.equals(elseBlock, other.elseBlock) 
                && Objects.equals(elseIfBlocks, other.elseIfBlocks)
                && Objects.equals(ifBlock, other.ifBlock);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(If.class.getSimpleName());
        result.append(" [ifBlock=");
        result.append(ifBlock);
        result.append(", elseIfBlocks=");
        result.append(elseIfBlocks);
        result.append(", elseBlock=");
        result.append(elseBlock);
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
     * Represents the boolean condition expression of an {@link IfBlock} or
     * {@link ElseIfBlock}.  Wraps a single {@link JavascriptNode} expression.
     */
    @JsonPropertyOrder({"expression", "sourceLocation"})
    public static class Condition implements JavascriptCondition {

        private static final long serialVersionUID = 1L; 
        
        /** The expression. */
        @JsonProperty("expression")
        private final JavascriptNode expression;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;

        /**
         * Constructs a {@code Condition} without source-location information.
         *
         * @param expression the condition expression node
         */
        public Condition(final JavascriptNode expression) {
            this(expression, null);
        }

        /**
         * Constructs a {@code Condition} with full source-location information.
         *
         * @param expression     the condition expression node
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Condition(@JsonProperty("expression") final JavascriptNode expression,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.expression = expression;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }

        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(expression);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this condition.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /** {@inheritDoc} */
        @Override
        public JavascriptNode getExpression() {
            return expression;
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
            return Objects.hash(expression, sourceLocation);
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
            final Condition other = (Condition) obj;
            return Objects.equals(expression, other.expression);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(If.class.getSimpleName());
            result.append(".");
            result.append(Condition.class.getSimpleName());
            result.append(" [expression=");
            result.append(expression);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }
    
    /**
     * Represents the primary {@code if} branch of a JavaScript {@link If} statement,
     * pairing a boolean {@link Condition} with the body ({@link JavascriptProcedure})
     * to execute when that condition evaluates to {@code true}.
     */
    @JsonPropertyOrder({"condition", "procedure", "sourceLocation"})
    public static class IfBlock implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The condition guarding this block. */
        @JsonProperty("condition")
        protected final Condition condition;
        /** The body executed when the condition is satisfied. */
        @JsonProperty("procedure")
        protected final JavascriptProcedure procedure;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The source location of this block in the original source text. */
        @JsonProperty("sourceLocation")
        protected SourceLocation sourceLocation;

        /**
         * Constructs an {@code IfBlock} without source-location information.
         *
         * @param condition the condition guarding this block
         * @param procedure the body executed when the condition is satisfied, or {@code null}
         */
        public IfBlock(final Condition condition,
                       final JavascriptProcedure procedure) {
            this(condition, procedure, null);
        }

        /**
         * Constructs an {@code IfBlock} with full source-location information.
         *
         * @param condition      the condition guarding this block
         * @param procedure      the body executed when the condition is satisfied, or {@code null}
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public IfBlock(@JsonProperty("condition") final Condition condition,
                       @JsonProperty("procedure") final JavascriptProcedure procedure,
                       @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.condition = condition;
            this.procedure = procedure;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(condition, procedure);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this {@code IfBlock}.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns {@code true} if this block has a non-null body.
         *
         * @return {@code true} when a procedure is present
         */
        public boolean hasProcedure() {
            return procedure != null;
        }

        /**
         * Returns the condition that guards this block.
         *
         * @return the {@link Condition}
         */
        public Condition getCondition() {
            return condition;
        }

        /**
         * Returns the body executed when the condition is satisfied.
         *
         * @return the {@link JavascriptProcedure}, or {@code null} if absent
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
            return Objects.hash(condition, procedure, sourceLocation);
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
            final IfBlock other = (IfBlock) obj;
            return Objects.equals(condition, other.condition)
                    && Objects.equals(procedure, other.procedure);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(If.class.getSimpleName());
            result.append(".");
            result.append(IfBlock.class.getSimpleName());
            result.append(" [condition=");
            result.append(condition);
            result.append(", procedure=");
            result.append(procedure);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }
    
    /**
     * Represents an {@code else if} branch of a JavaScript {@link If} statement.
     * Extends {@link IfBlock} with the same condition/procedure structure, but is
     * distinguished as a subordinate branch rather than the primary {@code if} block.
     */
    @JsonPropertyOrder({"condition", "procedure", "sourceLocation"})
    public static class ElseIfBlock extends IfBlock {

        private static final long serialVersionUID = 1L;

        /**
         * Constructs an {@code ElseIfBlock} by copying the condition and procedure
         * from an existing {@link IfBlock}.
         *
         * @param ifBlock the source block whose condition and procedure are reused
         */
        public ElseIfBlock(final IfBlock ifBlock) {
            this(ifBlock.getCondition(), ifBlock.getProcedure());
        }

        /**
         * Constructs an {@code ElseIfBlock} without source-location information.
         *
         * @param condition the condition guarding this {@code else if} branch
         * @param procedure the body executed when the condition is satisfied, or {@code null}
         */
        public ElseIfBlock(final Condition condition,
                           final JavascriptProcedure procedure) {
            this(condition, procedure, null);
        }

        /**
         * Constructs an {@code ElseIfBlock} with full source-location information.
         *
         * @param condition      the condition guarding this {@code else if} branch
         * @param procedure      the body executed when the condition is satisfied, or {@code null}
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public ElseIfBlock(@JsonProperty("condition") final Condition condition,
                           @JsonProperty("procedure") final JavascriptProcedure procedure,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super(condition, procedure, sourceLocation);
        }
        
        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(If.class.getSimpleName());
            result.append(".");
            result.append(IfBlock.class.getSimpleName());
            result.append(" [condition=");
            result.append(condition);
            result.append(", procedure=");
            result.append(procedure);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }

    /**
     * Represents the unconditional {@code else} branch of a JavaScript {@link If} statement.
     * Contains only an optional body ({@link JavascriptProcedure}) and its source location.
     */
    @JsonPropertyOrder({"procedure", "sourceLocation"})
    public static class ElseBlock implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
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
         * Constructs an {@code ElseBlock} without source-location information.
         *
         * @param procedure the body of the {@code else} branch, or {@code null}
         */
        public ElseBlock(final JavascriptProcedure procedure) {
            this(procedure, null);
        }

        /**
         * Constructs an {@code ElseBlock} with full source-location information.
         *
         * @param procedure      the body of the {@code else} branch, or {@code null}
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public ElseBlock(@JsonProperty("procedure") final JavascriptProcedure procedure,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.procedure = procedure;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(procedure);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this {@code ElseBlock}.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns {@code true} if this block has a non-null body.
         *
         * @return {@code true} when a procedure is present
         */
        public boolean hasProcedure() {
            return procedure != null;
        }

        /**
         * Returns the body of this {@code else} branch.
         *
         * @return the {@link JavascriptProcedure}, or {@code null} if absent
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
            return Objects.hash(procedure, sourceLocation);
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
            final ElseBlock other = (ElseBlock) obj;
            return Objects.equals(procedure, other.procedure);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(If.class.getSimpleName());
            result.append(".");
            result.append(ElseBlock.class.getSimpleName());
            result.append(" [procedure=");
            result.append(procedure);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }
    
    /**
     * Represents the body of an {@link IfBlock}, {@link ElseIfBlock}, or
     * {@link ElseBlock} as an ordered list of {@link JavascriptNode} statements.
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
         * @param nodes the list of statements forming the branch body
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Constructs a {@code Procedure} with full source-location information.
         *
         * @param nodes          the list of statements forming the branch body
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
            result.append(If.class.getSimpleName());
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
