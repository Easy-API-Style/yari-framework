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
 * AST node representing a JavaScript {@code do...while} loop,
 * composed of a block of instructions executed at least once (procedure)
 * followed by a condition evaluated at each iteration.
 */
@JsonPropertyOrder({"procedure", "condition", "sourceLocation"})
public class DoWhile extends AbstractWhile {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code do...while} node with no source location.
     *
     * @param procedure the block of instructions to execute
     * @param condition the loop stop condition
     */
    public DoWhile(final JavascriptProcedure procedure,
                   final JavascriptCondition condition) {
        super(condition, procedure);
    }

    /**
     * Constructs a {@code do...while} node with a source location (used by Jackson).
     *
     * @param procedure      the block of instructions to execute
     * @param condition      the loop stop condition
     * @param sourceLocation the location of the node in the source
     */
    @JsonCreator
    public DoWhile(@JsonProperty("procedure") final JavascriptProcedure procedure,
                   @JsonProperty("condition") final JavascriptCondition condition,
                   @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(condition, procedure, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(procedure, condition);
    }

    /*
     *
     * CLASS
     *
     */
    /**
     * Represents the condition of a {@code do...while} loop,
     * encapsulating the boolean expression evaluated after each iteration.
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
         * Constructs a condition without a source location.
         *
         * @param expression the boolean expression of the condition
         */
        public Condition(final JavascriptNode expression) {
            this(expression, null);
        }

        /**
         * Constructs a condition with a source location (used by Jackson).
         *
         * @param expression     the boolean expression of the condition
         * @param sourceLocation the location of the node in the source
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
         * Sets the parent node of this condition in the AST.
         *
         * @param parent the parent node
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
            result.append(DoWhile.class.getSimpleName());
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
     * Represents the instruction block (body) of a {@code do...while} loop,
     * containing the list of JavaScript nodes to execute at each iteration.
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
         * Constructs an instruction block without a source location.
         *
         * @param nodes the list of instructions in the block
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Constructs an instruction block with a source location (used by Jackson).
         *
         * @param nodes          the list of instructions in the block
         * @param sourceLocation the location of the node in the source
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
         * Sets the parent node of this block in the AST.
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
            result.append(DoWhile.class.getSimpleName());
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
