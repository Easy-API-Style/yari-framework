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
 * CSS AST node representing a class selector (e.g. {@code .myStyle}).
 * <p>
 * A class selector targets HTML elements whose {@code class} attribute
 * contains the value identified by {@link #getClassName()}.
 * </p>
 */
@JsonPropertyOrder({"value", "sourceLocation"})
public class ClassSelector implements CssSelector {

    private static final long serialVersionUID = 1L;

    /** The class identifier. */
    @JsonProperty("value")
    private final Identifier value;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code ClassSelector} from its class identifier,
     * with no source location information.
     *
     * @param value the identifier representing the CSS class name
     */
    public ClassSelector(final Identifier value) {
        this(value, null);
    }

    /**
     * Constructs a {@code ClassSelector} from its class identifier
     * and its location in the CSS source.
     *
     * @param value          the identifier representing the CSS class name
     * @param sourceLocation the position of this node in the source, or {@code null} if unknown
     */
    @JsonCreator
    public ClassSelector(@JsonProperty("value") final Identifier value,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.value = value;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the identifier corresponding to the CSS class name targeted by this selector.
     *
     * @return the identifier of the class name
     */
    public Identifier getClassName() {
        return value;
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
     * Compares this class selector to another AST node for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare with
     * @return {@code true} if both nodes are of the same class and have equal class name identifiers
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
        final ClassSelector other = (ClassSelector) astNode;
        return Objects.equals(value, other.value) ;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(value, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof ClassSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(ClassSelector.class.getSimpleName());
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
