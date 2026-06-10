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
package com.easyparsingapi.yari.parser.xml.ast;

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
 * Represents the root node of a parsed XML document, containing top-level nodes and comments.
 */
@JsonPropertyOrder({"nodes", "comments", "sourceLocation"})
public class Xml implements AstUnit, XmlNodeContainer {

    private static final long serialVersionUID = 1L;

    /** The top-level XML nodes. */
    @JsonProperty("nodes")
    private final List<XmlNode> nodes;
    /** The comments found in the document. */
    @JsonProperty("comments")
    private final List<AstComment> comments;
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
     * Creates an Xml root node with the given child nodes and comments, and no source location.
     *
     * @param nodes    the list of top-level XML nodes
     * @param comments the list of AST comments collected during parsing
     */
    public Xml(final List<XmlNode> nodes,
               final List<AstComment> comments) {
        this(nodes, comments, null);
    }

    /**
     * Creates an Xml root node with all fields.
     *
     * @param nodes          the list of top-level XML nodes
     * @param comments       the list of AST comments collected during parsing
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public Xml(@JsonProperty("nodes") final List<XmlNode> nodes,
               @JsonProperty("comments") final List<AstComment> comments,
               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.nodes = CollectionUtil.nullToEmpty(nodes);
        this.comments = CollectionUtil.nullToEmpty(comments);
        this.sourceLocation = sourceLocation;
        XmlUtil.setAstParent(this);
    }
    
    @Override
    public List<XmlNode> getNodes() {
        return nodes;
    }

    @Override
    public List<AstComment> astComments() {
        return comments;
    }

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
    
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(nodes);
    }

    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this XML root in the AST.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    
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
        final Xml other = (Xml) astNode;
        return Objects.equals(nodes, other.nodes);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nodes, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Xml node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [nodes=");
        result.append(size());
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }
    
}
