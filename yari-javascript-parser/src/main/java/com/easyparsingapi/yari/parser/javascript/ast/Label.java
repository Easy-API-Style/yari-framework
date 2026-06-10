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
 * Represents a labelled statement in the AST (e.g. {@code myLabel: for (...)}).
 * Holds the label identifier and the statement to which the label is attached.
 */
@JsonPropertyOrder({"name", "statement", "sourceLocation"})
public class Label implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The name. */
    @JsonProperty("name")
    private final Identifier name;
    /** The statement. */
    @JsonProperty("statement")
    private final JavascriptNode statement;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code Label} without source-location information.
     *
     * @param name      the label identifier
     * @param statement the statement attached to this label
     */
    public Label(final Identifier name,
                 final JavascriptNode statement) {
        this(name, statement, null);
    }

    /**
     * Constructs a {@code Label} with full source-location information.
     *
     * @param name           the label identifier
     * @param statement      the statement attached to this label
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Label(@JsonProperty("name") final Identifier name,
                 @JsonProperty("statement") final JavascriptNode statement,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.statement = statement;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name, statement);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this label.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the label identifier.
     *
     * @return the {@link Identifier} used as the label name
     */
    public Identifier getName() {
        return name;
    }

    /**
     * Returns the statement attached to this label.
     *
     * @return the labelled {@link JavascriptNode} statement
     */
    public JavascriptNode getStatement() {
        return statement;
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
        return Objects.hash(name, statement, sourceLocation);
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
        final Label other = (Label) obj;
        return Objects.equals(name, other.name) 
                && Objects.equals(statement, other.statement);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Label.class.getSimpleName());
        result.append(" [name=");
        result.append(name);
        result.append(", statement=");
        result.append(statement);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
