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
 * AST node representing a JavaScript assignment expression (e.g. {@code a = b}, {@code x += 1}).
 * <p>
 * An assignment is composed of a left operand, an assignment operator, and a right operand.
 * </p>
 */
@JsonPropertyOrder({"leftOperand", "operator", "rightOperand", "sourceLocation"})
public class Assignment implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The leftOperand. */
    @JsonProperty("leftOperand")
    private final JavascriptNode leftOperand;
    /** The operator. */
    @JsonProperty("operator")
    private final Operator operator;
    /** The rightOperand. */
    @JsonProperty("rightOperand")
    private final JavascriptNode rightOperand;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an assignment node with no source location.
     *
     * @param leftOperand  the left operand of the assignment
     * @param operator     the assignment operator
     * @param rightOperand the right operand of the assignment
     */
    public Assignment(final JavascriptNode leftOperand,
                      final Operator operator,
                      final JavascriptNode rightOperand) {
        this(leftOperand, operator, rightOperand, null);
    }

    /**
     * Constructs an assignment node with a source location.
     *
     * @param leftOperand    the left operand of the assignment
     * @param operator       the assignment operator
     * @param rightOperand   the right operand of the assignment
     * @param sourceLocation the location of the node in the source file, or {@code null} if unknown
     */
    @JsonCreator
    public Assignment(@JsonProperty("leftOperand") final JavascriptNode leftOperand,
                      @JsonProperty("operator") final Operator operator,
                      @JsonProperty("rightOperand") final JavascriptNode rightOperand,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(leftOperand, operator, rightOperand);
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

    /**
     * Returns the left operand of the assignment.
     *
     * @return the left operand
     */
    public JavascriptNode getLeftOperand() {
        return leftOperand;
    }

    /**
     * Returns the assignment operator.
     *
     * @return the assignment operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the right operand of the assignment.
     *
     * @return the right operand
     */
    public JavascriptNode getRightOperand() {
        return rightOperand;
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
        return Objects.hash(leftOperand, operator, rightOperand, sourceLocation);
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
        final Assignment other = (Assignment) obj;
        return Objects.equals(leftOperand, other.leftOperand)
                && Objects.equals(operator, other.operator)
                && Objects.equals(rightOperand, other.rightOperand);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Assignment.class.getSimpleName());
        result.append(" [leftOperand=");
        result.append(leftOperand);
        result.append(", operator=");
        result.append(operator);
        result.append(", rightOperand=");
        result.append(rightOperand);
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
     * AST node representing a chained JavaScript assignment (e.g. {@code a = b = c}).
     * <p>
     * Groups an ordered list of nodes assigned sequentially.
     * </p>
     */
    @JsonPropertyOrder({"values", "sourceLocation"})
    public static class Chaining implements JavascriptNode {

        private static final long serialVersionUID = 1L;

        /** The values. */
        @JsonProperty("values")
        private final List<JavascriptNode> values;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;

        /**
         * Constructs a chained assignment node with no source location.
         *
         * @param values the ordered list of nodes composing the chain
         */
        public Chaining(final List<JavascriptNode> values) {
            this(values, null);
        }

        /**
         * Constructs a chained assignment node with a source location.
         *
         * @param values         the ordered list of nodes composing the chain
         * @param sourceLocation the location of the node in the source file, or {@code null} if unknown
         */
        @JsonCreator
        public Chaining(@JsonProperty("values") final List<JavascriptNode> values,
                        @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.values = CollectionUtil.nullToEmpty(values);
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }

        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(values);
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

        /**
         * Returns the ordered list of nodes composing the assignment chain.
         *
         * @return the list of nodes in the chain
         */
        public List<JavascriptNode> getValues() {
            return values;
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
            return Objects.hash(values, sourceLocation);
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
            final Chaining other = (Chaining) obj;
            return Objects.equals(values, other.values);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Chaining.class.getSimpleName());
            result.append(".");
            result.append(Assignment.class.getSimpleName());
            result.append(" [values=");
            result.append(values.size());
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }

}
