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
 * Represents a {@code switch} statement in the AST.
 * Holds a {@link SwitchExpression} (the discriminant) and a {@link SwitchProcedure}
 * containing the {@link SwitchCase} and {@link DefaultCase} clauses.
 */
@JsonPropertyOrder({"switchExpression", "switchProcedure", "sourceLocation"})
public class Switch implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The switchExpression. */
    @JsonProperty("switchExpression") 
    private final SwitchExpression switchExpression;
    /** The switchProcedure. */
    @JsonProperty("switchProcedure") 
    private final SwitchProcedure switchProcedure;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code Switch} without source-location information.
     *
     * @param switchExpression the discriminant expression
     * @param switchProcedure  the case/default clauses
     */
    public Switch(final SwitchExpression switchExpression,
                  final SwitchProcedure switchProcedure) {
        this(switchExpression, switchProcedure, null);
    }

    /**
     * Constructs a {@code Switch} with full source-location information.
     *
     * @param switchExpression the discriminant expression
     * @param switchProcedure  the case/default clauses
     * @param sourceLocation   the source location of this node, or {@code null}
     */
    @JsonCreator
    public Switch(@JsonProperty("switchExpression") final SwitchExpression switchExpression,
                  @JsonProperty("switchProcedure") final SwitchProcedure switchProcedure,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.switchExpression = switchExpression;
        this.switchProcedure = switchProcedure;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
      public List<AstNode> astChildren() {
          return AstNode.childrenAttributes(switchExpression, switchProcedure);
      }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this switch statement.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the discriminant expression of this switch statement.
     *
     * @return the {@link SwitchExpression}
     */
    public SwitchExpression getSwitchExpression() {
        return switchExpression;
    }

    /**
     * Returns the procedure containing all case and default clauses.
     *
     * @return the {@link SwitchProcedure}
     */
    public SwitchProcedure getSwitchProcedure() {
        return switchProcedure;
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
        return Objects.hash(switchExpression, switchProcedure, sourceLocation);
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
        final Switch other = (Switch) obj;
        return Objects.equals(switchExpression, other.switchExpression)
                && Objects.equals(switchProcedure, other.switchProcedure);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Switch.class.getSimpleName());
        result.append(" [switchExpression=");
        result.append(switchExpression);
        if (switchProcedure != null) {
            result.append(", switchProcedure=");
            result.append(switchProcedure.size());
        }
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
     * Wraps the discriminant expression of the {@code switch} statement.
     */
    @JsonPropertyOrder({"expression", "sourceLocation"})
    public static class SwitchExpression implements JavascriptNode {

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
         * Constructs a {@code SwitchExpression} without source-location information.
         *
         * @param expression the discriminant expression
         */
        public SwitchExpression(final JavascriptNode expression) {
            this(expression, null);
        }

        /**
         * Constructs a {@code SwitchExpression} with full source-location information.
         *
         * @param expression     the discriminant expression
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public SwitchExpression(@JsonProperty("expression") final JavascriptNode expression,
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
         * Sets the parent AST node of this switch expression.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the discriminant expression of the {@code switch} statement.
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
            final SwitchExpression other = (SwitchExpression) obj;
            return Objects.equals(expression, other.expression);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(SwitchExpression.class.getSimpleName());
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
     * Represents a single {@code case <value>:} clause of a {@code switch} statement.
     */
    @JsonPropertyOrder({"caseValue", "caseProcedure", "sourceLocation"})
    public static class SwitchCase implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The caseValue. */
        @JsonProperty("caseValue") 
        private final JavascriptNode caseValue;
        /** The caseProcedure. */
        @JsonProperty("caseProcedure") 
        private final CaseProcedure caseProcedure;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;

        /**
         * Constructs a {@code SwitchCase} without source-location information.
         *
         * @param caseValue     the case discriminant expression
         * @param caseProcedure the statements for this case (may be {@code null})
         */
        public SwitchCase(final JavascriptNode caseValue,
                          final CaseProcedure caseProcedure) {
            this(caseValue, caseProcedure, null);
        }

        /**
         * Constructs a {@code SwitchCase} with full source-location information.
         *
         * @param caseValue      the case discriminant expression
         * @param caseProcedure  the statements for this case (may be {@code null})
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public SwitchCase(@JsonProperty("caseValue") final JavascriptNode caseValue,
                          @JsonProperty("caseProcedure") final CaseProcedure caseProcedure,
                          @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.caseValue = caseValue;
            this.caseProcedure = caseProcedure;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
          public List<AstNode> astChildren() {
              return AstNode.childrenAttributes(caseValue, caseProcedure);
          }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this case clause.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns {@code true} if this case has a body.
         *
         * @return {@code true} when a {@link CaseProcedure} is present
         */
        public boolean hasCaseProcedure() {
            return caseProcedure != null;
        }

        /**
         * Returns the case discriminant expression.
         *
         * @return the case value {@link JavascriptNode}
         */
        public JavascriptNode getCaseValue() {
            return caseValue;
        }

        /**
         * Returns the body of this case clause, or {@code null} if the clause has no body.
         *
         * @return the {@link CaseProcedure}, or {@code null}
         */
        public CaseProcedure getCaseProcedure() {
            return caseProcedure;
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
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(caseProcedure, caseValue, sourceLocation);
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
            final SwitchCase other = (SwitchCase) obj;
            return Objects.equals(caseProcedure, other.caseProcedure) 
                    && Objects.equals(caseValue, other.caseValue);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(SwitchCase.class.getSimpleName());
            result.append(" [caseValue=");
            result.append(caseValue);
            result.append(", caseProcedure=");
            result.append(caseProcedure);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }
    
    /**
     * Represents the {@code default:} clause of a {@code switch} statement.
     */
    @JsonPropertyOrder({"caseProcedure", "sourceLocation"})
    public static class DefaultCase implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The caseProcedure. */
        @JsonProperty("caseProcedure") 
        private final CaseProcedure caseProcedure;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;

        /**
         * Constructs a {@code DefaultCase} without source-location information.
         *
         * @param caseProcedure the body of the default clause
         */
        public DefaultCase(final CaseProcedure caseProcedure) {
            this(caseProcedure, null);
        }

        /**
         * Constructs a {@code DefaultCase} with full source-location information.
         *
         * @param caseProcedure  the body of the default clause
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public DefaultCase(@JsonProperty("caseProcedure") final CaseProcedure caseProcedure,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.caseProcedure = caseProcedure;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
          public List<AstNode> astChildren() {
              return AstNode.childrenAttributes(caseProcedure);
          }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this default clause.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the body of this default clause.
         *
         * @return the {@link CaseProcedure}
         */
        public CaseProcedure getCaseProcedure() {
            return caseProcedure;
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
            return Objects.hash(caseProcedure, sourceLocation);
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
            final DefaultCase other = (DefaultCase) obj;
            return Objects.equals(caseProcedure, other.caseProcedure);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(DefaultCase.class.getSimpleName());
            result.append(" [caseProcedure=");
            result.append(caseProcedure);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }
    
    /**
     * Contains the ordered list of {@link SwitchCase} clauses and the optional {@link DefaultCase}
     * of a {@code switch} statement.
     */
    @JsonPropertyOrder({"switchCases", "defaultCase", "sourceLocation"})
    public static class SwitchProcedure implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The switchCases. */
        @JsonProperty("switchCases") 
        private List<SwitchCase> switchCases;
        /** The defaultCase. */
        @JsonProperty("defaultCase") 
        private DefaultCase defaultCase;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;
                
        /**
         * Constructs a {@code SwitchProcedure} without source-location information.
         *
         * @param switchCases the list of case clauses
         * @param defaultCase the optional default clause, or {@code null}
         */
        public SwitchProcedure(final List<SwitchCase> switchCases,
                               final DefaultCase defaultCase) {
            this(switchCases, defaultCase, null);
        }

        /**
         * Constructs a {@code SwitchProcedure} with full source-location information.
         *
         * @param switchCases    the list of case clauses
         * @param defaultCase    the optional default clause, or {@code null}
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public SwitchProcedure(@JsonProperty("switchCases") final List<SwitchCase> switchCases,
                               @JsonProperty("defaultCase") final DefaultCase defaultCase,
                               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.switchCases = CollectionUtil.nullToEmpty(switchCases);
            this.defaultCase = defaultCase;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
          public List<AstNode> astChildren() {
              return AstNode.childrenAttributes(switchCases, defaultCase);
          }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this switch procedure.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the number of case clauses.
         *
         * @return the case count
         */
        public int size() {
            return switchCases.size();
        }

        /**
         * Returns the ordered list of case clauses.
         *
         * @return the list of {@link SwitchCase} nodes
         */
        public List<SwitchCase> getSwitchCases() {
            return switchCases;
        }

        /**
         * Returns the case clause at the given zero-based index, or {@code null} if out of range.
         *
         * @param index zero-based position of the desired case
         * @return the {@link SwitchCase}, or {@code null}
         */
        public SwitchCase getSwitchCase(final int index) {
            SwitchCase result = null;
            if (index < size()) {
                result = switchCases.get(index);
            }
            return result;
        }

        /**
         * Returns {@code true} if a {@code default} clause is present.
         *
         * @return {@code true} when a default case exists
         */
        public boolean hasDefaultCase() {
            return defaultCase != null;
        }

        /**
         * Returns the {@code default} clause, or {@code null} if absent.
         *
         * @return the {@link DefaultCase}, or {@code null}
         */
        public DefaultCase getDefaultCase() {
            return defaultCase;
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
            return Objects.hash(defaultCase, switchCases, sourceLocation);
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
            final SwitchProcedure other = (SwitchProcedure) obj;
            return Objects.equals(defaultCase, other.defaultCase)
                    && Objects.equals(switchCases, other.switchCases);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(SwitchProcedure.class.getSimpleName());
            result.append(" [switchCases=");
            result.append(switchCases.size());
            result.append(", defaultCase=");
            result.append(hasDefaultCase());
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }
    
    /**
     * The statement list body of a {@link SwitchCase} or {@link DefaultCase} clause.
     */
    @JsonPropertyOrder({"nodes", "sourceLocation"})
    public static class CaseProcedure implements JavascriptProcedure {

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
         * Constructs a {@code CaseProcedure} without source-location information.
         *
         * @param nodes the list of statements for this case body
         */
        public CaseProcedure(final List<JavascriptNode> nodes) {
            this(nodes, null);
        }

        /**
         * Constructs a {@code CaseProcedure} with full source-location information.
         *
         * @param nodes          the list of statements for this case body
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public CaseProcedure(@JsonProperty("nodes") final List<JavascriptNode> nodes,
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
         * Sets the parent AST node of this case procedure.
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
            final CaseProcedure other = (CaseProcedure) obj;
            return Objects.equals(nodes, other.nodes);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(CaseProcedure.class.getSimpleName());
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
