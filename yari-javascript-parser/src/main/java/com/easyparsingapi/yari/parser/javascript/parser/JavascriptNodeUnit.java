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
package com.easyparsingapi.yari.parser.javascript.parser;

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstUnit;
import com.easyparsingapi.yari.core.ast.service.CommentService;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A wrapper that pairs a single parsed {@link JavascriptNode} with the list of
 * comments extracted from the same source unit.
 * Implements both {@link AstUnit} (for comment access) and {@link JavascriptNode}
 * (so it can be treated as a node in the AST).
 */
@JsonPropertyOrder({"node", "comments", "sourceLocation"})
public class JavascriptNodeUnit implements AstUnit, JavascriptNode {

    private static final long serialVersionUID = 1L;
    
    /** The node. */
    @JsonProperty("node")
    private final JavascriptNode node;
    /** The commentService. */
    @JsonIgnore
    private CommentService commentService;
    /** The comments. */
    @JsonProperty("comments")
    private final List<AstComment> comments;
    
    /**
     * Constructs a {@code JavascriptNodeUnit} pairing the given node with its comments.
     *
     * @param node     the parsed {@link JavascriptNode}
     * @param comments the list of comments from the same source unit
     */
    @JsonCreator
    JavascriptNodeUnit(@JsonProperty("node") final JavascriptNode node,
                       @JsonProperty("comments") final List<AstComment> comments) {
        super();
        this.node = node;
        this.comments = CollectionUtil.nullToEmpty(comments);
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
        return AstNode.childrenAttributes(node);
    }

    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public SourceLocation getSourceLocation() {
        return node.getSourceLocation();
    }

    /** {@inheritDoc} */
    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        node.setSourceLocation(sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equalsNode(final AstNode astNode) {
        return node.equalsNode(astNode);
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(comments, node);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JavascriptNodeUnit other = (JavascriptNodeUnit) obj;
        return Objects.equals(comments, other.comments) 
                && Objects.equals(node, other.node);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(JavascriptNodeUnit.class.getSimpleName());
        result.append(" [javascriptNode=");
        result.append(node);
        result.append(", comments=");
        result.append(comments);
        result.append("]");
        return result.toString();
    }
    
}
