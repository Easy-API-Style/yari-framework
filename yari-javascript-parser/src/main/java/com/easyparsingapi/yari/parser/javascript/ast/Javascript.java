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
 * The root AST node of a parsed JavaScript source file.
 * Implements both {@link JavascriptProcedure} (providing the top-level statement list)
 * and {@link AstUnit} (providing access to the associated comments).
 */
@JsonPropertyOrder({"nodes", "comments", "sourceLocation"})
public class Javascript implements JavascriptProcedure, AstUnit {

    private static final long serialVersionUID = 1L; 
    
    /** The nodes. */
    @JsonProperty("nodes")
    private final List<JavascriptNode> nodes;
    /** The comments. */
    @JsonProperty("comments")
    private final List<AstComment> comments;
    /** The commentService. */
    @JsonIgnore
    private CommentService commentService;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code Javascript} root node without source location information.
     *
     * @param nodes    the top-level statement nodes of the source file
     * @param comments the comments associated with this source unit
     */
    public Javascript(final List<JavascriptNode> nodes,
                      final List<AstComment> comments) {
        this(nodes, comments, null);
    }
    
    /**
     * Constructs a {@code Javascript} root node with full source location information.
     *
     * @param nodes          the top-level statement nodes of the source file
     * @param comments       the comments associated with this source unit
     * @param sourceLocation the source location of the entire compilation unit, or {@code null}
     */
    @JsonCreator
    public Javascript(@JsonProperty("nodes") final List<JavascriptNode> nodes,
                      @JsonProperty("comments") final List<AstComment> comments,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.nodes = CollectionUtil.nullToEmpty(nodes);
        this.comments = CollectionUtil.nullToEmpty(comments);
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstComment> astComments() {
        return comments;
    }

    /** {@inheritDoc} */
    @Override
    public List<AstComment> astCommentsOf(final AstNode node, 
                                          final Position... positions) {
        return commentService().astCommentsOf(node, positions);
    }

    private CommentService commentService() {
        if (this.commentService == null) {
            this.commentService = new CommentService(this);
        }
        return this.commentService;
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(nodes);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this root node.
     *
     * @param parent the parent node to assign
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /** {@inheritDoc} */
    @Override
    public List<JavascriptNode> getNodes() {
        return nodes;
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
        final Javascript other = (Javascript) astNode;
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
        if (object instanceof Javascript node) {
            return equalsNode(node) 
                      && Objects.equals(comments, node.astComments())
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [nodes=");
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
