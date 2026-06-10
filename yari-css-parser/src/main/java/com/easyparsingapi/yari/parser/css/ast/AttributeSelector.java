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

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a CSS attribute selector of the form {@code [attr]}, {@code [attr=value]},
 * {@code [attr~=value]}, etc., as defined in the CSS Selectors specification.
 */
@JsonPropertyOrder({"expression", "sourceLocation"})
public class AttributeSelector implements CssSelector  {

    private static final long serialVersionUID = 1L;

    /** The attribute expression. */
    @JsonProperty("expression")
    private final CssNode expression;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code AttributeSelector} with the given expression and no source location.
     *
     * @param expression the expression describing the content of the attribute selector
     */
    public AttributeSelector(@JsonProperty("expression") CssNode expression) {
        this(expression, null);
    }

    /**
     * Constructs an {@code AttributeSelector} with the given expression and source location.
     *
     * @param expression      the expression describing the content of the attribute selector
     * @param sourceLocation  the position of this node in the CSS source, or {@code null}
     */
    @JsonCreator
    public AttributeSelector(@JsonProperty("expression") CssNode expression,
                             @JsonProperty("sourceLocation") SourceLocation sourceLocation) {
        super();
        this.expression = expression;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the CSS expression contained in this attribute selector.
     *
     * @return the CSS node representing the selector expression
     */
    public CssNode getExpression() {
        return expression;
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
     * Sets the parent node of this selector in the syntax tree.
     *
     * @param parent the parent node to associate with this selector
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
     * Compares this attribute selector to another AST node for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare with
     * @return {@code true} if both nodes are of the same class and have equal expressions
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
        final AttributeSelector other = (AttributeSelector) astNode;
        return Objects.equals(expression, other.expression);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(expression, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof AttributeSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Expression.class.getSimpleName());
        result.append(" [sourceLocation=");
        result.append(sourceLocation);
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
     * Represents the content of a CSS attribute selector, comprising the attribute name,
     * the optional comparison operator, the associated value, and the case-sensitivity option.
     */
    @JsonPropertyOrder({"operator", "name", "value", "caseOption", "sourceLocation"})
    public static class Expression implements CssNode {

        private static final long serialVersionUID = 1L;

        /**
         * Case-sensitivity option applicable to attribute value comparison.
         */
        public static enum Case {
            /** Case-insensitive comparison (indicated by {@code i} flag). */
            insensitive,
            /** Case-sensitive comparison (indicated by {@code s} flag). */
            sensitive
        }

        /** The attribute name. */
        @JsonProperty("name")
        private final Identifier name;
        /** The attribute operator. */
        @JsonProperty("operator")
        private final Operator operator;
        /** The attribute value. */
        @JsonProperty("value")
        private final CssNode value;
        /** The case sensitivity option. */
        @JsonProperty("caseOption")
        private final Identifier caseOption;
        /** The parent AST node. */
        @JsonIgnore
        private AstNode parent;
        /** The source location. */
        @JsonProperty("sourceLocation")
        private SourceLocation sourceLocation;

        /**
         * Constructs an {@code Expression} without a source location.
         *
         * @param name        the identifier of the attribute name
         * @param operator    the comparison operator, or {@code null} if absent
         * @param value       the value to compare, or {@code null} if absent
         * @param caseOption  the case option ({@code "i"} or {@code "s"}), or {@code null}
         */
        public Expression(final Identifier name,
                          final Operator operator,
                          final CssNode value,
                          final Identifier caseOption) {
            this(name, operator, value, caseOption, null);
        }

        /**
         * Constructs an {@code Expression} with all its components and the source location.
         *
         * @param name            the identifier of the attribute name
         * @param operator        the comparison operator, or {@code null} if absent
         * @param value           the value to compare, or {@code null} if absent
         * @param caseOption      the case option ({@code "i"} or {@code "s"}), or {@code null}
         * @param sourceLocation  the position of this node in the CSS source, or {@code null}
         */
        @JsonCreator
        public Expression(@JsonProperty("name") final Identifier name,
                          @JsonProperty("operator") final Operator operator,
                          @JsonProperty("value") final CssNode value,
                          @JsonProperty("caseOption") final Identifier caseOption,
                          @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.name = name;
            this.operator = operator;
            this.value = value;
            this.caseOption = caseOption;
            this.sourceLocation = sourceLocation;
            CssUtil.setAstParent(this);
        }

        /**
         * Returns the identifier of the attribute name targeted by this selector.
         *
         * @return the identifier representing the attribute name
         */
        public Identifier getName() {
            return name;
        }

        /**
         * Returns the comparison operator of this attribute selector.
         *
         * @return the CSS operator (e.g. {@code =}, {@code ~=}, {@code |=}), or {@code null} if absent
         */
        public Operator getOperator() {
            return operator;
        }

        /**
         * Indicates whether this attribute selector has a comparison operator and therefore a value.
         *
         * @return {@code true} if an operator is present, {@code false} otherwise
         */
        public boolean hasValue() {
            return operator != null;
        }

        /**
         * Returns the CSS value to compare against the attribute.
         *
         * @return the CSS node representing the value, or {@code null} if absent
         */
        public CssNode getValue() {
            return value;
        }

        /**
         * Indicates whether this attribute selector matches the specified case option.
         * In the absence of an explicit case option, the comparison is considered case-sensitive.
         *
         * @param caseOption the case option to test ({@link Case#sensitive} or {@link Case#insensitive})
         * @return {@code true} if the effective case option matches {@code caseOption}
         */
        public boolean is(final Case caseOption) {
            /** The result. */
            final boolean result;
            if (hasCase()) {
                final String caseValue = this.caseOption.getValue();
                if ("s".equals(caseValue)
                        && Case.sensitive == caseOption) {
                    result = true;
                }
                else if ("i".equals(caseValue)
                             && Case.insensitive == caseOption) {
                    result = true;
                }
                else {
                    result = false;
                }
            }
            else {
                result = Case.sensitive == caseOption;
            }
            return result;
        }

        /**
         * Indicates whether an explicit case option is present in this attribute selector.
         *
         * @return {@code true} if a case option is defined, {@code false} otherwise
         */
        public boolean hasCase() {
            return caseOption != null;
        }

        /**
         * Returns the identifier representing the case option of this attribute selector.
         *
         * @return the identifier of the case option ({@code "i"} or {@code "s"}), or {@code null}
         */
        public Identifier getCase() {
            return caseOption;
        }

        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(name, operator, value, caseOption);
        }

        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent node of this expression in the syntax tree.
         *
         * @param parent the parent node to associate with this expression
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
            final Expression other = (Expression) astNode;
            return Objects.equals(name, other.name)
                     && Objects.equals(operator, other.operator)
                     && Objects.equals(value, other.value)
                     && Objects.equals(caseOption, other.caseOption);
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(operator, name, value, caseOption, sourceLocation);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object object) {
            if (object instanceof Expression node) {
                return equalsNode(node)
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Expression.class.getSimpleName());
            result.append(" [name=");
            result.append(name);
            result.append(", operator=");
            result.append(operator);
            result.append(", value=");
            result.append(value);
            result.append(", caseOption=");
            result.append(caseOption);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }

    }

}
