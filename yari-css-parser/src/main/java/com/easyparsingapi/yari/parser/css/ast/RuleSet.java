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
 * CSS AST node representing a rule set, composed of a selector
 * and a declaration block.
 */
@JsonPropertyOrder({"selector", "block", "sourceLocation"})
public class RuleSet implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The selector. */
    @JsonProperty("selector")
    private final CssSelector selector;
    /** The block. */
    @JsonProperty("block")
    private final Block block;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a {@code RuleSet} with a selector and a block, without a source location.
     *
     * @param selector the CSS selector associated with this rule set
     * @param block    the CSS declaration block
     */
    public RuleSet(final CssSelector selector,
                   final Block block) {
        this(selector, block, null);
    }

    /**
     * Constructs a {@code RuleSet} with a selector, a block, and a source location.
     *
     * @param selector       the CSS selector associated with this rule set
     * @param block          the CSS declaration block
     * @param sourceLocation the location in the original source, may be {@code null}
     */
    @JsonCreator
    public RuleSet(@JsonProperty("selector") final CssSelector selector,
                   @JsonProperty("block") final Block block,
                   @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.selector = selector;
        this.block = block;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the CSS selector of this rule set.
     *
     * @return the CSS selector
     */
    public CssSelector getSelector() {
        return selector;
    }

    /**
     * Returns the CSS declaration block of this rule set.
     *
     * @return the declaration block
     */
    public Block getBlock() {
        return block;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(selector, block);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this rule set in the AST.
     *
     * @param parent the parent node to assign
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
     * Compares this rule set to another {@link AstNode} for structural equality,
     * ignoring source location.
     *
     * @param astNode the node to compare; may be {@code null}
     * @return {@code true} if both nodes are {@code RuleSet} instances with equal selector and block
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
        final RuleSet other = (RuleSet) astNode;
        return Objects.equals(selector, other.selector)
                && Objects.equals(block, other.block);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(selector, block, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof RuleSet node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(RuleSet.class.getSimpleName());
        result.append(" [selector=");
        result.append(selector);
        result.append(", block=");
        result.append(block.size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
