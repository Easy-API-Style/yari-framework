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
 * Represents the binding clause of a JavaScript {@code catch} block, holding the caught exception
 * identifier and an optional {@code instanceof} guard expression.
 */
@JsonPropertyOrder({"exception", "instanceofException", "sourceLocation"})
public class CatchInstance implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /** The exception. */
    @JsonProperty("exception")
    private final Identifier exception;
    /** The instanceofException. */
    @JsonProperty("instanceofException")
    private final Infix instanceofException;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a {@code CatchInstance} without source location information.
     *
     * @param exception           the identifier bound to the caught exception
     * @param instanceofException an optional {@code instanceof} infix expression used as a type guard, or {@code null}
     */
    public CatchInstance(final Identifier exception,
                         final Infix instanceofException) {
        this(exception, instanceofException, null);
    }

    /**
     * Creates a {@code CatchInstance} with all properties, used as the Jackson deserialization entry point.
     *
     * @param exception           the identifier bound to the caught exception
     * @param instanceofException an optional {@code instanceof} infix expression used as a type guard, or {@code null}
     * @param sourceLocation      the source location of this node in the original source code, or {@code null}
     */
    @JsonCreator
    public CatchInstance(@JsonProperty("exception") final Identifier exception,
                         @JsonProperty("instanceofException") final Infix instanceofException,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.exception = exception;
        this.instanceofException = instanceofException;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(exception, instanceofException);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this catch instance.
     *
     * @param parent the parent {@link AstNode} to assign
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the identifier bound to the caught exception.
     *
     * @return the exception {@link Identifier}
     */
    public Identifier getException() {
        return exception;
    }

    /**
     * Returns the optional {@code instanceof} infix expression used as a type guard on the caught exception.
     *
     * @return the {@link Infix} guard expression, or {@code null} if none is defined
     */
    public Infix getInstanceofException() {
        return instanceofException;
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
        return Objects.hash(exception, instanceofException, sourceLocation);
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
        final CatchInstance other = (CatchInstance) obj;
        return Objects.equals(exception, other.exception)
                && Objects.equals(instanceofException, other.instanceofException);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(CatchInstance.class);
        result.append(" [exception=");
        result.append(exception);
        result.append(", instanceofException=");
        result.append(instanceofException);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
