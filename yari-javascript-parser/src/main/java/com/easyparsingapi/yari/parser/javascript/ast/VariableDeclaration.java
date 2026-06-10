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
 * Represents a variable declaration statement in the AST
 * ({@code var}, {@code let}, or {@code const} followed by one or more {@link Variable}s).
 */
@JsonPropertyOrder({"type", "variableDeclarations", "sourceLocation"})
public class VariableDeclaration implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The type. */
    @JsonProperty("type") 
    private final Type type;
    /** The variableDeclarations. */
    @JsonProperty("variableDeclarations") 
    private final List<Variable> variableDeclarations;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code VariableDeclaration} without source-location information.
     *
     * @param type                 the declaration keyword ({@code var}, {@code let}, or {@code const})
     * @param variableDeclarations the list of declared variables
     */
    public VariableDeclaration(final Type type,
                               final List<Variable> variableDeclarations) {
        this(type, variableDeclarations, null);
    }

    /**
     * Constructs a {@code VariableDeclaration} with full source-location information.
     *
     * @param type                 the declaration keyword node
     * @param variableDeclarations the list of declared variables
     * @param sourceLocation       the source location of this node, or {@code null}
     */
    @JsonCreator
    public VariableDeclaration(@JsonProperty("type") final Type type,
                               @JsonProperty("variableDeclarations") final List<Variable> variableDeclarations,
                               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.type = type;
        this.variableDeclarations = CollectionUtil.nullToEmpty(variableDeclarations);
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(type, variableDeclarations);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }
    
    /**
     * Sets the parent AST node of this variable declaration.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the number of variables declared in this statement.
     *
     * @return the variable count
     */
    public int size() {
        return variableDeclarations.size();
    }

    /**
     * Returns {@code true} if a declaration keyword ({@link Type}) is present.
     *
     * @return {@code true} when a keyword node is available
     */
    public boolean hasType() {
        return getType() != null;
    }

    /**
     * Returns the declaration keyword node ({@code var}, {@code let}, or {@code const}),
     * or {@code null} if absent.
     *
     * @return the {@link Type} node, or {@code null}
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the declared variable at the given zero-based index, or {@code null} if out of range.
     *
     * @param index zero-based position of the desired variable
     * @return the {@link Variable} at that index, or {@code null}
     */
    public Variable getVariableDeclaration(final int index) {
        Variable result = null;
        if (index < size()) {
            result = variableDeclarations.get(index);
        }
        return result;
    }

    /**
     * Returns the ordered list of declared variables.
     *
     * @return the list of {@link Variable} nodes
     */
    public List<Variable> getVariableDeclarations() {
        return variableDeclarations;
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
        return Objects.hash(type, variableDeclarations, sourceLocation);
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
        final VariableDeclaration other = (VariableDeclaration) obj;
        return Objects.equals(type, other.type) 
                && Objects.equals(variableDeclarations, other.variableDeclarations);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(VariableDeclaration.class.getSimpleName());
        result.append(" [size=");
        result.append(size());
        if (hasType()) {
            result.append(", type=");
            result.append(getType());
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
     * Represents the declaration keyword ({@code var}, {@code let}, or {@code const})
     * in the AST as a string-valued node.
     */
    @JsonPropertyOrder({"value", "sourceLocation"})
    public static class Type implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The value. */
        @JsonProperty("value") 
        private final String value;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;
        
        /**
         * Constructs a {@code Type} without source-location information.
         *
         * @param value the keyword text ({@code "var"}, {@code "let"}, or {@code "const"})
         */
        public Type(final String value) {
            this(value, null);
        }

        /**
         * Constructs a {@code Type} with full source-location information.
         *
         * @param value          the keyword text
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Type(@JsonProperty("value") final String value,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.value = value;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(value);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }
        
        /**
         * Sets the parent AST node of this type keyword node.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the keyword text ({@code "var"}, {@code "let"}, or {@code "const"}).
         *
         * @return the keyword string
         */
        public String getValue() {
            return value;
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
            return Objects.hash(value, sourceLocation);
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
            final Type other = (Type) obj;
            return Objects.equals(value, other.value);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Variable.class.getSimpleName());
            result.append(".");
            result.append(Type.class.getSimpleName());
            result.append(" [value=");
            result.append(value);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }
    
    /**
     * Represents a single declared variable within a {@link VariableDeclaration},
     * carrying the binding name, an optional initialiser operator ({@code =}), and
     * an optional initial value expression.
     */
    @JsonPropertyOrder({"name", "operator", "value", "sourceLocation"})
    public static class Variable implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The name. */
        @JsonProperty("name") 
        protected final JavascriptNode name;
        /** The operator. */
        @JsonProperty("operator") 
        protected final Operator operator;
        /** The value. */
        @JsonProperty("value") 
        protected final JavascriptNode value;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;
        
        /**
         * Constructs a {@code Variable} without source-location information.
         *
         * @param name     the binding name (identifier or destructuring pattern)
         * @param operator the assignment operator ({@code =}), or {@code null}
         * @param value    the initial value expression, or {@code null}
         */
        public Variable(final JavascriptNode name,
                        final Operator operator,
                        final JavascriptNode value) {
            this(name, operator, value, null);
        }

        /**
         * Constructs a {@code Variable} with full source-location information.
         *
         * @param name           the binding name (identifier or destructuring pattern)
         * @param operator       the assignment operator, or {@code null}
         * @param value          the initial value expression, or {@code null}
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Variable(@JsonProperty("name") final JavascriptNode name,
                        @JsonProperty("operator") final Operator operator,
                        @JsonProperty("value") final JavascriptNode value,
                        @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.name = name;
            this.operator = operator;
            this.value = value;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(name, operator, value);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent AST node of this variable binding.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the binding name expression (an identifier or destructuring pattern).
         *
         * @return the name {@link JavascriptNode}
         */
        public JavascriptNode getName() {
            return name;
        }

        /**
         * Returns the assignment operator ({@code =}), or {@code null} if the variable has no initialiser.
         *
         * @return the {@link Operator}, or {@code null}
         */
        public Operator getOperator() {
            return operator;
        }

        /**
         * Returns {@code true} if this variable has an initialiser expression.
         *
         * @return {@code true} when an initial value is present
         */
        public boolean isInitialized() {
            return value != null;
        }

        /**
         * Returns the initial value expression, or {@code null} if there is no initialiser.
         *
         * @return the value {@link JavascriptNode}, or {@code null}
         */
        public JavascriptNode getValue() {
            return value;
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
            return Objects.hash(name, operator, value, sourceLocation);
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
            final Variable other = (Variable) obj;
            return Objects.equals(name, other.name)
                    && Objects.equals(operator, other.operator)
                    && Objects.equals(value, other.value);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Variable.class.getSimpleName());
            result.append(" [name=");
            result.append(name);
            if (isInitialized()) {
                result.append(", value=");
                result.append(value);
            }
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }
    
}
