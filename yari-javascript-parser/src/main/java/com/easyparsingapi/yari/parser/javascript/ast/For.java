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
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a for loop statement in the JavaScript AST.
 */
@JsonPropertyOrder({"await", "iteration", "procedure", "sourceLocation"})
public class For implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /**
     * Enumerates the kinds of iteration a for loop can use.
     */
    public static enum IterationType {
        /** Standard for loop. */
        iteration,
        /** For-in or for-of loop. */
        listIteration
    }
    
    /** The await. */
    @JsonProperty("await") 
    private final boolean await;
    /** The iteration. */
    @JsonProperty("iteration") 
    private final JavascriptNode iteration;
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
     * Creates a for loop node without source location information.
     *
     * @param await     whether the for loop uses the await keyword
     * @param iteration the iteration expression of the for loop
     * @param procedure the body of the for loop
     */
    public For(final boolean await,
               final JavascriptNode iteration,
               final JavascriptProcedure procedure) {
        this(await, iteration, procedure, null);
    }

    /**
     * Creates a for loop node with all properties, including source location information.
     *
     * @param await          whether the for loop uses the await keyword
     * @param iteration      the iteration expression of the for loop
     * @param procedure      the body of the for loop
     * @param sourceLocation the source location of this node in the source code
     */
    @JsonCreator
    public For(@JsonProperty("await") final boolean await,
               @JsonProperty("iteration") final JavascriptNode iteration,
               @JsonProperty("procedure") final JavascriptProcedure procedure,
               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.await = await;
        this.iteration = iteration;
        this.procedure = procedure;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(iteration, procedure);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }
    
    /**
     * Sets the parent node of this for loop in the AST.
     *
     * @param parent the parent AST node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns true if this for loop uses the await keyword.
     *
     * @return {@code true} if the await keyword is present
     */
    public boolean hasAwait() {
        return await;
    }
    
    /**
     * Returns true if this for loop has a body.
     *
     * @return {@code true} if a procedure (loop body) is present
     */
    public boolean hasProcedure() {
        return procedure != null;
    }

    /**
     * Returns the iteration expression.
     *
     * @return the iteration expression of this for loop
     */
    public JavascriptNode getIteration() {
        return iteration;
    }

    /**
     * Returns the loop body.
     *
     * @return the procedure (body) of this for loop, or {@code null} if absent
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
        return Objects.hash(await, iteration, procedure, sourceLocation);
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
        final For other = (For) obj;
        return await == other.await
                && Objects.equals(iteration, other.iteration)
                && Objects.equals(procedure, other.procedure);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(For.class.getSimpleName());
        result.append(" [condition=");
        result.append(iteration);
        result.append(", procedure=");
        result.append(procedure);
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
     * Represents the header of a standard for loop, containing the initializer,
     * condition, and update expressions.
     */
    @JsonPropertyOrder({"firstExpression", "secondExpression", "thirdExpression", "sourceLocation"})
    public static class Iteration implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The firstExpression. */
        @JsonProperty("firstExpression") 
        private final JavascriptNode firstExpression;
        /** The secondExpression. */
        @JsonProperty("secondExpression") 
        private final JavascriptNode secondExpression;
        /** The thirdExpression. */
        @JsonProperty("thirdExpression") 
        private final JavascriptNode thirdExpression;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;

        /**
         * Creates an iteration header node without source location information.
         *
         * @param firstExpression  the initializer expression of the for loop
         * @param secondExpression the condition expression of the for loop
         * @param thirdExpression  the update expression of the for loop
         */
        public Iteration(final JavascriptNode firstExpression,
                         final JavascriptNode secondExpression,
                         final JavascriptNode thirdExpression) {
            this(firstExpression, secondExpression, thirdExpression, null);
        }

        /**
         * Creates an iteration header node with all properties, including source location information.
         *
         * @param firstExpression  the initializer expression of the for loop
         * @param secondExpression the condition expression of the for loop
         * @param thirdExpression  the update expression of the for loop
         * @param sourceLocation   the source location of this node in the source code
         */
        @JsonCreator
        public Iteration(@JsonProperty("firstExpression") final JavascriptNode firstExpression,
                         @JsonProperty("secondExpression") final JavascriptNode secondExpression,
                         @JsonProperty("thirdExpression") final JavascriptNode thirdExpression,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.firstExpression = firstExpression;
            this.secondExpression = secondExpression;
            this.thirdExpression = thirdExpression;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(firstExpression, secondExpression, thirdExpression);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent node of this iteration header in the AST.
         *
         * @param parent the parent AST node
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns true if this iteration has an initializer expression.
         *
         * @return {@code true} if the first (initializer) expression is present
         */
        public boolean hasFirstExpression() {
            return firstExpression != null;
        }

        /**
         * Returns the initializer expression of the for loop.
         *
         * @return the first expression, or {@code null} if absent
         */
        public JavascriptNode getFirstExpression() {
            return firstExpression;
        }

        /**
         * Returns true if this iteration has a condition expression.
         *
         * @return {@code true} if the second (condition) expression is present
         */
        public boolean hasSecondExpression() {
            return secondExpression != null;
        }

        /**
         * Returns the condition expression of the for loop.
         *
         * @return the second expression, or {@code null} if absent
         */
        public JavascriptNode getSecondExpression() {
            return secondExpression;
        }

        /**
         * Returns true if this iteration has an update expression.
         *
         * @return {@code true} if the third (update) expression is present
         */
        public boolean hasThirdExpression() {
            return thirdExpression != null;
        }

        /**
         * Returns the update expression of the for loop.
         *
         * @return the third expression, or {@code null} if absent
         */
        public JavascriptNode getThirdExpression() {
            return thirdExpression;
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
            return Objects.hash(firstExpression, secondExpression, thirdExpression, sourceLocation);
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
            final Iteration other = (Iteration) obj;
            return Objects.equals(firstExpression, other.firstExpression)
                    && Objects.equals(secondExpression, other.secondExpression)
                    && Objects.equals(thirdExpression, other.thirdExpression);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(For.class.getSimpleName());
            result.append(".");
            result.append(Iteration.class.getSimpleName());
            result.append(" [firstExpression=");
            result.append(firstExpression);
            result.append(", secondExpression=");
            result.append(secondExpression);
            result.append(", thirdExpression=");
            result.append(thirdExpression);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }
    
    /**
     * Represents the header of a for-in or for-of loop, containing the loop variable,
     * the operator (in/of), and the iterable expression.
     */
    @JsonPropertyOrder({"operator", "value", "values", "sourceLocation"})
    public static class ListIteration implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /**
         * Enumerates the operator keywords that can appear in a for-in or for-of loop.
         */
        public static enum Type {
            /** The {@code in} keyword, used in for-in loops. */
            in,
            /** The {@code of} keyword, used in for-of loops. */
            of
        }

        /** The value. */
        @JsonProperty("value") 
        private final JavascriptNode value;
        /** The operator. */
        @JsonProperty("operator") 
        private final Operator operator;
        /** The values. */
        @JsonProperty("values") 
        private final JavascriptNode values;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;

        /**
         * Creates a list iteration header node without source location information.
         *
         * @param value    the loop variable expression (left-hand side of in/of)
         * @param operator the operator node representing the {@code in} or {@code of} keyword
         * @param values   the iterable expression (right-hand side of in/of)
         */
        public ListIteration(final JavascriptNode value,
                             final Operator operator,
                             final JavascriptNode values) {
            this(value, operator, values, null);
        }

        /**
         * Creates a list iteration header node with all properties, including source location information.
         *
         * @param value          the loop variable expression (left-hand side of in/of)
         * @param operator       the operator node representing the {@code in} or {@code of} keyword
         * @param values         the iterable expression (right-hand side of in/of)
         * @param sourceLocation the source location of this node in the source code
         */
        @JsonCreator
        public ListIteration(@JsonProperty("value") final JavascriptNode value,
                             @JsonProperty("operator") final Operator operator,
                             @JsonProperty("values") final JavascriptNode values,
                             @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.operator = operator;
            this.value = value;
            this.values = values;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(value, operator, values);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }
        
        /**
         * Sets the parent node of this list iteration header in the AST.
         *
         * @param parent the parent AST node
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the type of this list iteration based on the operator symbol.
         *
         * @return {@link Type#in} if the operator is {@code in}, otherwise {@link Type#of}
         */
        public Type getType() {
            /** The result. */
            final Type result;
            if ("in".equals(operator.getSymbol())) {
                result = Type.in;
            }
            else {
                result = Type.of;
            }
            return result;
        }
        
        /**
         * Returns true if this list iteration uses the given operator type.
         *
         * @param type the operator type to test against
         * @return {@code true} if the loop uses the specified {@link Type}
         */
        public boolean is(final Type type) {
            return type == getType();
        }

        /**
         * Returns the loop variable expression (the left-hand side of in/of).
         *
         * @return the loop variable expression
         */
        public JavascriptNode getValue() {
            return value;
        }

        /**
         * Returns the operator node representing the {@code in} or {@code of} keyword.
         *
         * @return the operator node
         */
        public Operator getOperator() {
            return operator;
        }

        /**
         * Returns the iterable expression (the right-hand side of in/of).
         *
         * @return the iterable expression
         */
        public JavascriptNode getValues() {
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
            return Objects.hash(operator, value, values, sourceLocation);
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
            final ListIteration other = (ListIteration) obj;
            return Objects.equals(operator, other.operator)
                    && Objects.equals(value, other.value)
                    && Objects.equals(values, other.values);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(For.class.getSimpleName());
            result.append(".");
            result.append(ListIteration.class.getSimpleName());
            result.append(" [value=");
            result.append(value);
            result.append(", operator=");
            result.append(operator);
            result.append(", values=");
            result.append(values);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }
    
    /**
     * Represents the body of a for loop, containing the list of statements
     * to be executed on each iteration.
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
         * Creates a procedure node without source location information.
         *
         * @param nodes the list of statements forming the loop body
         */
        public Procedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Creates a procedure node with all properties, including source location information.
         *
         * @param nodes          the list of statements forming the loop body
         * @param sourceLocation the source location of this node in the source code
         */
        @JsonCreator
        public Procedure(@JsonProperty("nodes") final List<JavascriptNode> nodes,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.nodes = nodes;
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
         * Sets the parent node of this procedure in the AST.
         *
         * @param parent the parent AST node
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
            result.append(For.class.getSimpleName());
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
