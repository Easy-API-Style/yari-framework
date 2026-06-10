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
 * Represents a CSS namespace-qualified selector of the form {@code namespace|element},
 * where a namespace prefix is applied to an element selector.
 */
@JsonPropertyOrder({"namespace", "selector", "sourceLocation"})
public class NamespaceSelector implements CssSelector {

    private static final long serialVersionUID = 1L;

    /** The namespace part. */
    @JsonProperty("namespace")
    private final CssNode namespace;
    /** The element part. */
    @JsonProperty("selector")
    private final CssNode element;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a {@code NamespaceSelector} without source location information.
     *
     * @param namespace the namespace prefix node (may be {@code null} for the wildcard namespace)
     * @param selector  the element selector node to which the namespace applies
     */
    public NamespaceSelector(final CssNode namespace,
                             final CssNode selector) {
        this(namespace, selector, null);
    }

    /**
     * Creates a {@code NamespaceSelector} with full source location information.
     *
     * @param namespace      the namespace prefix node (may be {@code null} for the wildcard namespace)
     * @param element        the element selector node to which the namespace applies
     * @param sourceLocation the source location of this selector in the original CSS source, or {@code null}
     */
    @JsonCreator
    public NamespaceSelector(@JsonProperty("namespace") final CssNode namespace,
                             @JsonProperty("selector") final CssNode element,
                             @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.namespace = namespace;
        this.element = element;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the namespace prefix node of this selector.
     *
     * @return the namespace prefix node, or {@code null} if no prefix is specified
     */
    public CssNode getNamespace() {
        return namespace;
    }

    /**
     * Returns the element selector node qualified by the namespace.
     *
     * @return the element selector node
     */
    public CssNode getElement() {
        return element;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(namespace, element);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this selector.
     *
     * @param parent the parent {@link AstNode} in the abstract syntax tree
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
     * Compares this node to another {@link AstNode} for structural equality,
     * ignoring source location information.
     *
     * @param astNode the node to compare against
     * @return {@code true} if both nodes have equal namespace and element children
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
        final NamespaceSelector other = (NamespaceSelector) astNode;
        return Objects.equals(namespace, other.namespace)
                 && Objects.equals(element, other.element);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(namespace, element, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof NamespaceSelector node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(NamespaceSelector.class.getSimpleName());
        result.append(" [namespace=");
        result.append(namespace);
        result.append(", element=");
        result.append(element);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
