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
 * AST node representing a CSS at-rule (e.g. {@code @media}, {@code @import}, {@code @keyframes}).
 * <p>
 * An at-rule is composed of a name ({@link AtRuleName}), an optional signature
 * and an optional block ({@link Block}).
 * </p>
 */
@JsonPropertyOrder({"name", "signature", "block", "sourceLocation"})
public class AtRule implements CssNode {

    private static final long serialVersionUID = 1L;

    /** The name. */
    @JsonProperty("name")
    private final AtRuleName name;
    /** The signature. */
    @JsonProperty("signature")
    private final CssNode signature;
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
     * Constructs an at-rule without a source location.
     *
     * @param name      the name of the at-rule
     * @param signature the signature (prelude) of the at-rule, may be {@code null}
     * @param block     the block associated with the at-rule, may be {@code null}
     */
    public AtRule(final AtRuleName name,
                  final CssNode signature,
                  final Block block) {
        this(name, signature, block, null);
    }

    /**
     * Constructs an at-rule with all its properties, including the source location.
     * This constructor is used during JSON deserialization.
     *
     * @param name           the name of the at-rule
     * @param signature      the signature (prelude) of the at-rule, may be {@code null}
     * @param block          the block associated with the at-rule, may be {@code null}
     * @param sourceLocation the location in the original source, may be {@code null}
     */
    @JsonCreator
    public AtRule(@JsonProperty("name") final AtRuleName name,
                  @JsonProperty("signature") final CssNode signature,
                  @JsonProperty("block") final Block block,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.signature = signature;
        this.block = block;
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns the name of the at-rule.
     *
     * @return the name of the at-rule
     */
    public AtRuleName getName() {
        return name;
    }

    /**
     * Indicates whether the at-rule has a signature (prelude).
     *
     * @return {@code true} if the signature is non-null, {@code false} otherwise
     */
    public boolean hasSignature() {
        return signature != null;
    }

    /**
     * Returns the signature (prelude) of the at-rule.
     *
     * @return the signature, or {@code null} if absent
     */
    public CssNode getSignature() {
        return signature;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name, signature, block);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this at-rule in the AST.
     *
     * @param parent the parent node
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
     * Structurally compares this at-rule with another AST node,
     * ignoring the source location.
     *
     * @param astNode the node to compare
     * @return {@code true} if both nodes are structurally equivalent
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
        final AtRule other = (AtRule) astNode;
        return Objects.equals(name, other.name)
                && Objects.equals(signature, other.signature)
                && Objects.equals(block, other.block);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(name, signature, block, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof AtRule node) {
            return equalsNode(node)
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(AtRule.class.getSimpleName());
        result.append(" [name=");
        result.append(name);
        result.append(", signature=");
        result.append(signature);
        result.append(", block=");
        result.append(block != null ? block.size() : 0);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
