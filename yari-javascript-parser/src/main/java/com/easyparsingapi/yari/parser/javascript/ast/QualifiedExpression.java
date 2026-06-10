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
import java.util.function.Consumer;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a chain of member-access expressions in the AST (e.g. {@code a.b.c} or {@code a?.b}).
 * The chain is modelled as a leading expression followed by one or more {@link Qualifier} steps,
 * each carrying the access operator ({@code .} or {@code ?.}) and the next expression.
 */
@JsonPropertyOrder({"qualifiers", "expression", "sourceLocation"})
public class QualifiedExpression implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The qualifiers. */
    @JsonProperty("qualifiers") 
    private final List<Qualifier> qualifiers;
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
     * Constructs a {@code QualifiedExpression} without source-location information.
     *
     * @param qualifiers the list of qualifier steps
     * @param expression the final (rightmost) expression in the chain
     */
    public QualifiedExpression(final List<Qualifier> qualifiers,
                               final JavascriptNode expression) {
        this(qualifiers, expression, null);
    }

    /**
     * Constructs a {@code QualifiedExpression} with full source-location information.
     *
     * @param qualifiers     the list of qualifier steps
     * @param expression     the final (rightmost) expression in the chain
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public QualifiedExpression(@JsonProperty("qualifiers") final List<Qualifier> qualifiers,
                               @JsonProperty("expression") final JavascriptNode expression,
                               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.qualifiers = CollectionUtil.nullToEmpty(qualifiers);
        this.expression = expression;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(qualifiers, expression);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this qualified expression.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the number of qualifier steps in this chain.
     *
     * @return the qualifier count
     */
    public int qualifierSize() {
        return qualifiers.size();
    }

    /**
     * Returns the ordered list of qualifier steps.
     *
     * @return the list of {@link Qualifier} objects
     */
    public List<Qualifier> getQualifiers() {
        return qualifiers;
    }

    /**
     * Returns the final (rightmost) expression in the chain.
     *
     * @return the terminal {@link JavascriptNode}
     */
    public JavascriptNode getExpression() {
        return expression;
    }

    /**
     * Returns the qualifier step at the given zero-based index, or {@code null} if out of range.
     *
     * @param index zero-based position of the desired qualifier
     * @return the {@link Qualifier} at that index, or {@code null}
     */
    public Qualifier getQualifier(final int index) {
        Qualifier result = null;
        if (index >= 0 && index < qualifiers.size()) {
            result = qualifiers.get(index);
        }
        return result;
    }

    /**
     * Returns all expressions in the chain in order — one per qualifier plus the terminal expression.
     *
     * @return a list of {@link JavascriptNode} objects representing each segment of the chain
     */
    public List<JavascriptNode> getExpressions() {
        final List<JavascriptNode> result = new ArrayList<>();
        for (final Qualifier qualifier : qualifiers) {
            result.add(qualifier.getExpression());
        }
        result.add(expression);
        return result;
    }

    /**
     * Returns the expression at the given zero-based position in the flattened expression list,
     * or {@code null} if out of range.
     *
     * @param index zero-based position in the flattened expression list
     * @return the {@link JavascriptNode} at that position, or {@code null}
     */
    public JavascriptNode getExpression(final int index) {
        JavascriptNode result = null;
        if (index >= 0 && index < qualifiers.size() + 1) {
            List<JavascriptNode> expressions = getExpressions();
            result = expressions.get(index);
        }
        return result;
    }

    /**
     * Walks the entire qualifier chain, invoking the consumer for each (qualifier, operator, expression) triple.
     *
     * @param consumer the callback to invoke for each step
     */
    public void walk(final Consumer<Handler> consumer) {
        walk(0, consumer);
    }

    /**
     * Walks the qualifier chain starting from the given index,
     * invoking the consumer for each (qualifier, operator, expression) triple.
     *
     * @param index    the zero-based index of the first qualifier to visit
     * @param consumer the callback to invoke for each step
     */
    public void walk(final int index, final Consumer<Handler> consumer) {
        if (index >= 0 && index < qualifiers.size() + 1) {
            final List<Qualifier> qualifiers = getQualifiers();
            final List<JavascriptNode> expressions = getExpressions();
            for (int i = index; i < qualifiers.size(); i++) {
                final Qualifier qualifier = qualifiers.get(i);
                JavascriptNode expression = expressions.get(i + 1);
                consumer.accept(new Handler(qualifier.getExpression(), 
                                            qualifier.getOperator(), 
                                            /** Field. */
                                            expression));
            }
        }
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
        return Objects.hash(expression, qualifiers, sourceLocation);
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
        final QualifiedExpression other = (QualifiedExpression) obj;
        return Objects.equals(expression, other.expression)
                && Objects.equals(qualifiers, other.qualifiers);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(QualifiedExpression.class.getSimpleName());
        result.append(" [qualifiers=");
        result.append(qualifiers.size());
        result.append(", expression=");
        result.append(expression);
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
     * Represents a single step in a member-access chain, carrying the left-hand expression
     * and the access operator ({@code .} or {@code ?.}).
     */
    @JsonPropertyOrder({"expression", "operator", "sourceLocation"})
    public static class Qualifier implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The expression. */
        @JsonProperty("expression") 
        private final JavascriptNode expression;
        /** The operator. */
        @JsonProperty("operator") 
        private final Operator operator;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;

        /**
         * Constructs a {@code Qualifier} without source-location information.
         *
         * @param expression the left-hand expression of this step
         * @param operator   the access operator ({@code .} or {@code ?.})
         */
        public Qualifier(final JavascriptNode expression,
                         final Operator operator) {
            this(expression, operator, null);
        }

        /**
         * Constructs a {@code Qualifier} with full source-location information.
         *
         * @param expression     the left-hand expression of this step
         * @param operator       the access operator
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Qualifier(@JsonProperty("expression") final JavascriptNode expression,
                         @JsonProperty("operator") final Operator operator,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.expression = expression;
            this.operator = operator;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(expression, operator);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this qualifier step.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the left-hand expression of this qualifier step.
         *
         * @return the expression {@link JavascriptNode}
         */
        public JavascriptNode getExpression() {
            return expression;
        }

        /**
         * Returns the access operator of this qualifier step.
         *
         * @return the {@link Operator}
         */
        public Operator getOperator() {
            return operator;
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
            return Objects.hash(expression, operator, sourceLocation);
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
            final Qualifier other = (Qualifier) obj;
            return Objects.equals(expression, other.expression)
                    && Objects.equals(operator, other.operator);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(getClass().getSimpleName());
            result.append(".");
            result.append(Qualifier.class.getSimpleName());
            result.append(" [operator=");
            result.append(operator);
            result.append(", expression=");
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
     * A {@link Qualifier} variant that represents a private-field access step (using {@code #name}).
     */
    @JsonPropertyOrder({"expression", "operator", "sourceLocation"})
    public static class PrivateQualifier extends Qualifier {

        private static final long serialVersionUID = 1L; 
        
        /**
         * Constructs a {@code PrivateQualifier} without source-location information.
         *
         * @param expression the private-field name expression
         * @param operator   the access operator
         */
        public PrivateQualifier(final JavascriptNode expression,
                                final Operator operator) {
            this(expression, operator, null);
        }

        /**
         * Constructs a {@code PrivateQualifier} with full source-location information.
         *
         * @param expression     the private-field name expression
         * @param operator       the access operator
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public PrivateQualifier(@JsonProperty("expression") final JavascriptNode expression,
                                @JsonProperty("operator") final Operator operator,
                                @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super(expression, operator, sourceLocation);
        }
        
    }
    
    /**
     * Wraps a private-field identifier expression (e.g. {@code #name}) within a qualified chain.
     */
    @JsonPropertyOrder({"expression", "operator", "sourceLocation"})
    public static class PrivateExpression implements JavascriptNode {

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
         * Constructs a {@code PrivateExpression} without source-location information.
         *
         * @param expression the private-field identifier expression
         */
        public PrivateExpression(final JavascriptNode expression) {
            this(expression, null);
        }

        /**
         * Constructs a {@code PrivateExpression} with full source-location information.
         *
         * @param expression     the private-field identifier expression
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public PrivateExpression(@JsonProperty("expression") final JavascriptNode expression,
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
         * Sets the parent AST node of this private expression.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the private-field identifier expression wrapped by this node.
         *
         * @return the expression {@link JavascriptNode}
         */
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
            return Objects.hash(expression);
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
        public boolean equalsNode(AstNode obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            PrivateExpression other = (PrivateExpression) obj;
            return Objects.equals(expression, other.expression);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(getClass().getSimpleName());
            result.append(".");
            result.append(PrivateExpression.class.getSimpleName());
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
     * A read-only view of one step in the qualifier chain, carrying the qualifier expression,
     * the access operator, and the next expression.  Passed to the {@link #walk} callback.
     */
    public static class Handler {

        /** The qualifier. */
        private final JavascriptNode qualifier;
        /** The operator. */
        private final Operator operator;
        /** The expression. */
        private final JavascriptNode expression;

        private Handler(final JavascriptNode qualifier,
                        final Operator operator,
                        final JavascriptNode expression) {
            super();
            this.qualifier = qualifier;
            this.operator = operator;
            this.expression = expression;
        }

        /**
         * Returns the left-hand expression of this qualifier step.
         *
         * @return the qualifier {@link JavascriptNode}
         */
        public JavascriptNode qualifier() {
            return qualifier;
        }

        /**
         * Returns the access operator of this qualifier step.
         *
         * @return the {@link Operator}
         */
        public Operator operator() {
            return operator;
        }

        /**
         * Returns the right-hand expression that follows this qualifier step.
         *
         * @return the next {@link JavascriptNode} in the chain
         */
        public JavascriptNode expression() {
            return expression;
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(expression, operator, qualifier);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Handler other = (Handler) obj;
            return Objects.equals(expression, other.expression) 
                    && Objects.equals(operator, other.operator)
                    && Objects.equals(qualifier, other.qualifier);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(QualifiedExpression.class.getSimpleName());
            result.append(".");
            result.append(Handler.class.getSimpleName());
            result.append(" [qualifier=");
            result.append(qualifier);
            result.append(", operator=");
            result.append(operator);
            result.append(", expression=");
            result.append(expression);
            result.append("]");
            return result.toString();
        }

    }

}
