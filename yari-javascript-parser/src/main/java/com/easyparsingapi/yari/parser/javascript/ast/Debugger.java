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
 * Represents a JavaScript {@code debugger} statement in the AST.
 * <p>
 * The {@code debugger} statement invokes any available debugging functionality
 * (e.g. setting a breakpoint). If no debugging functionality is available,
 * this statement has no effect.
 * </p>
 */
@JsonPropertyOrder({"sourceLocation"})
public class Debugger implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a {@code Debugger} node with no source location information.
     */
    public Debugger() {
        this(null);
    }

    /**
     * Creates a {@code Debugger} node with the given source location.
     *
     * @param sourceLocation the location of this node in the source code, or {@code null} if unknown
     */
    @JsonCreator
    public Debugger(@JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /**
     * Returns the list of AST children of this node.
     * <p>
     * A {@code debugger} statement has no child nodes.
     * </p>
     *
     * @return an empty list of {@link AstNode} children
     */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes();
    }

    /**
     * Returns the parent node of this node in the AST.
     *
     * @return the parent {@link AstNode}, or {@code null} if this node has no parent
     */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node in the AST.
     *
     * @param parent the parent {@link AstNode} to assign to this node
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
    public int hashCode() {
        return Objects.hash(sourceLocation);
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
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Debugger.class.getSimpleName());
        result.append(" [");
        if (sourceLocation != null) {
            result.append("sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
