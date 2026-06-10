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
 * AST node representing a bracket-based member access expression (map-style lookup),
 * such as {@code obj[key]} in JavaScript.
 * <p>
 * The node holds the target expression ({@code name}) and the bracket accessor
 * ({@link Bracket}) that contains the key expression.
 * </p>
 */
@JsonPropertyOrder({"name", "bracket", "sourceLocation"})
public class MapExpression implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The name. */
    @JsonProperty("name")
    private final JavascriptNode name;
    /** The bracket. */
    @JsonProperty("bracket")
    private final Bracket bracket;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code MapExpression} without source location information.
     *
     * @param name    the expression to the left of the bracket accessor
     * @param bracket the bracket accessor containing the key expression
     */
    public MapExpression(final JavascriptNode name,
                         final Bracket bracket) {
        this(name, bracket, null);
    }
    
    /**
     * Constructs a {@code MapExpression} with all components.
     *
     * @param name           the expression to the left of the bracket accessor
     * @param bracket        the bracket accessor containing the key expression
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public MapExpression(@JsonProperty("name") final JavascriptNode name,
                      @JsonProperty("bracket") final Bracket bracket,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.bracket = bracket;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name, bracket);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this node.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the target expression of the bracket access (the left-hand side).
     *
     * @return the target expression node
     */
    public JavascriptNode getName() {
        return name;
    }

    /**
     * Returns the bracket accessor that contains the key expression.
     *
     * @return the {@link Bracket} node
     */
    public Bracket getBracket() {
        return bracket;
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
        return Objects.hash(bracket, name, sourceLocation);
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
        final MapExpression other = (MapExpression) obj;
        return Objects.equals(bracket, other.bracket) 
                && Objects.equals(name, other.name);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(MapExpression.class.getSimpleName());
        result.append(" [name=");
        result.append(name);
        result.append(", bracket=");
        result.append(bracket);
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
     * AST node representing the bracket part of a map expression ({@code [key]}),
     * encapsulating the key expression used for the lookup.
     */
    @JsonPropertyOrder({"parameter", "sourceLocation"})
    public static class Bracket implements JavascriptNode {

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
         * Constructs a {@code Bracket} without source location information.
         *
         * @param parameter the key expression inside the brackets
         */
        public Bracket(final JavascriptNode parameter) {
            this(parameter, null);
        }

        /**
         * Constructs a {@code Bracket} with all components.
         *
         * @param parameter      the key expression inside the brackets
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public Bracket(@JsonProperty("parameter") final JavascriptNode parameter,
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
         * Sets the parent AST node of this node.
         *
         * @param parent the parent node
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the key expression inside the brackets.
         *
         * @return the key expression node
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
            final Bracket other = (Bracket) obj;
            return Objects.equals(parameter, other.parameter);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(MapExpression.class.getSimpleName());
            result.append(".");
            result.append(Bracket.class.getSimpleName());
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
    
}
