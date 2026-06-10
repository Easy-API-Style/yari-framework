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

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstUnit;
import com.easyparsingapi.yari.core.ast.service.CommentService;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Root node of the abstract syntax tree (AST) of a CSS stylesheet.
 * <p>
 * This class represents the top-level unit produced by the CSS parser.
 * It contains the ordered list of CSS nodes ({@link CssNode}) as well as the
 * comments associated with the stylesheet.
 * </p>
 */
@JsonPropertyOrder({"nodes", "comments", "sourceLocation"})
public class Css implements AstUnit, CssBlock {

    private static final long serialVersionUID = 1L;

    /** The top-level CSS nodes. */
    @JsonProperty("nodes")
    private final List<CssNode> nodes;
    /** The comments found in the stylesheet. */
    @JsonProperty("comments")
    final List<AstComment> comments;
    /** Service for querying comments by node. */
    @JsonIgnore
    private CommentService commentService;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Constructs a root CSS node without a source location.
     *
     * @param nodes    list of child CSS nodes in the stylesheet
     * @param comments list of comments present in the stylesheet
     */
    public Css(final List<CssNode> nodes,
               final List<AstComment> comments) {
        this(nodes, comments, null);
    }

    /**
     * Constructs a root CSS node with all its properties.
     * <p>
     * This constructor is used by Jackson during JSON deserialization.
     * </p>
     *
     * @param nodes          list of child CSS nodes in the stylesheet
     * @param comments       list of comments present in the stylesheet
     * @param sourceLocation position of this node in the original source, or {@code null}
     */
    @JsonCreator
    public Css(@JsonProperty("nodes") final List<CssNode> nodes,
               @JsonProperty("comments") final List<AstComment> comments,
               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.nodes = CollectionUtil.nullToEmpty(nodes);
        this.comments = CollectionUtil.nullToEmpty(comments);
        this.sourceLocation = sourceLocation;
        CssUtil.setAstParent(this);
    }

    /**
     * Returns all comments present in this CSS stylesheet.
     *
     * @return a non-null list of {@link AstComment} elements
     */
    @Override
    public List<AstComment> astComments() {
        return comments;
    }

    /**
     * Returns the comments associated with the given AST node at the specified positions.
     *
     * @param node      the AST node for which to retrieve comments
     * @param positions the positions relative to the node (e.g. before, after) to filter by;
     *                  if none are given, all associated comments are returned
     * @return a list of {@link AstComment} elements associated with the node at the given positions
     */
    @Override
    public List<AstComment> astCommentsOf(final AstNode node,
                                          final Position... positions) {
        return commentService().astCommentsOf(node, positions);
    }

    /**
     * Returns the direct child AST nodes of this CSS unit (i.e., the top-level CSS nodes).
     *
     * @return a non-null list of child {@link AstNode} elements
     */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(nodes);
    }

    /**
     * Returns the parent AST node of this CSS unit.
     *
     * @return the parent {@link AstNode}, or {@code null} if this is the root
     */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this CSS unit in the AST.
     *
     * @param parent the parent node to associate with this unit
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the ordered list of top-level CSS nodes in this stylesheet.
     *
     * @return a non-null, possibly empty list of {@link CssNode} elements
     */
    @Override
    public List<CssNode> getNodes() {
        return nodes;
    }

    /**
     * Returns the source location of this CSS unit (typically spanning the whole stylesheet).
     *
     * @return the source location, or {@code null} if not available
     */
    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    /**
     * Sets the source location of this CSS unit.
     *
     * @param sourceLocation the source location to assign, or {@code null}
     */
    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    /**
     * Compares this CSS unit to another AST node for structural equality,
     * considering only the list of child nodes and ignoring comments and source location.
     *
     * @param astNode the node to compare with
     * @return {@code true} if both nodes are of the same class and have equal child node lists
     */
    @Override
    public boolean equalsNode(AstNode astNode) {
        if (this == astNode) {
            return true;
        }
        if (astNode == null) {
            return false;
        }
        if (getClass() != astNode.getClass()) {
            return false;
        }
        final Css other = (Css) astNode;
        return Objects.equals(nodes, other.nodes);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(nodes, comments, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Css node) {
            return equalsNode(node)
                      && Objects.equals(comments, node.astComments())
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    private CommentService commentService() {
        if (this.commentService == null) {
            this.commentService = new CommentService(this);
        }
        return this.commentService;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [cssNodes=");
        result.append(nodes.size());
        if (comments != null && !comments.isEmpty()) {
            result.append(", comments=");
            result.append(comments.size());
        }
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
