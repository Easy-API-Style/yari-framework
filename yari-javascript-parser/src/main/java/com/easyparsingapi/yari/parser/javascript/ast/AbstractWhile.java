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

import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract class representing an AST node of type while loop in JavaScript.
 * It groups the iteration condition and the associated block of instructions (procedure),
 * and serves as a common base for the while and do-while variants.
 */
public abstract class AbstractWhile implements JavascriptNode {

    private static final long serialVersionUID = 1L;

    /**
     * The iteration condition evaluated at each loop iteration.
     */
    @JsonProperty("condition")
    protected final JavascriptCondition condition;

    /**
     * The block of instructions executed as long as the condition is true.
     */
    @JsonProperty("procedure")
    protected final JavascriptProcedure procedure;

    /** The parent. */
    @JsonIgnore
    private AstNode parent;

    /**
     * The position in the original source corresponding to this node.
     */
    @JsonProperty("sourceLocation")
    protected SourceLocation sourceLocation;

    /**
     * Constructs an AbstractWhile node with no source location information.
     *
     * @param condition the loop iteration condition
     * @param procedure the block of instructions to execute
     */
    public AbstractWhile(final JavascriptCondition condition,
                         final JavascriptProcedure procedure) {
        this(condition, procedure, null);
    }

    /**
     * Constructs an AbstractWhile node with all its constituent elements.
     *
     * @param condition      the loop iteration condition
     * @param procedure      the block of instructions to execute
     * @param sourceLocation the position in the original source, may be {@code null}
     */
    @JsonCreator
    public AbstractWhile(@JsonProperty("condition") final JavascriptCondition condition,
                         @JsonProperty("procedure") final JavascriptProcedure procedure,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.condition = condition;
        this.procedure = procedure;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this while-loop node in the AST.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Indicates whether this node has an associated block of instructions.
     *
     * @return {@code true} if the procedure is non-null, {@code false} otherwise
     */
    public boolean hasProcedure() {
        return procedure != null;
    }

    /**
     * Returns the iteration condition of this loop.
     *
     * @return the iteration condition
     */
    public JavascriptCondition getCondition() {
        return condition;
    }

    /**
     * Returns the block of instructions associated with this loop.
     *
     * @return the block of instructions, or {@code null} if absent
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
        return Objects.hash(condition, procedure, sourceLocation, sourceLocation);
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
        final AbstractWhile other = (AbstractWhile) astNode;
        return Objects.equals(condition, other.condition)
                && Objects.equals(procedure, other.procedure);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [condition=");
        result.append(condition);
        result.append(", procedure=");
        result.append(procedure);
        result.append(", parent=");
        result.append(astParent());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
