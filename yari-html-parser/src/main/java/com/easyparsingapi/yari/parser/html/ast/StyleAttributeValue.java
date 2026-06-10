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
package com.easyparsingapi.yari.parser.html.ast;

import java.util.List;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstUnit;
import com.easyparsingapi.yari.core.ast.service.CommentService;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.css.ast.CssBlock;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents the value of an HTML {@code style} attribute, parsed as a CSS block.
 * <p>
 * This node bridges the HTML AST and the CSS AST by implementing both
 * {@link CssBlock} and {@link XmlNode}, allowing inline style declarations
 * to be traversed and manipulated as CSS nodes within an HTML document.
 * </p>
 */
@JsonPropertyOrder({"value", "type", "nodes", "comments", "sourceLocation"})
public class StyleAttributeValue extends TagAttribute.Value
                                 implements CssBlock, AstUnit, XmlNode {

    private static final long serialVersionUID = 1L;

    /** The parsed CSS nodes. */
    @JsonProperty("nodes")
    private final List<CssNode> nodes;
    /** The comments found in the style. */
    @JsonProperty("comments")
    private final List<AstComment> comments;
    /** Service for querying comments by node. */
    @JsonIgnore
    private CommentService commentService;

    /**
     * Constructs a {@code StyleAttributeValue} without source location information.
     *
     * @param type     the type of the attribute value
     * @param value    the raw string value of the style attribute
     * @param nodes    the list of CSS nodes parsed from the style attribute
     * @param comments the list of comments associated with this node
     */
    public StyleAttributeValue(final Type type,
                               final String value,
                               final List<CssNode> nodes,
                               final List<AstComment> comments) {
        this(type, value, nodes, comments, null);
    }

    /**
     * Constructs a {@code StyleAttributeValue} with full source location information.
     * This constructor is used by Jackson for JSON deserialization.
     *
     * @param type           the type of the attribute value
     * @param value          the raw string value of the style attribute
     * @param nodes          the list of CSS nodes parsed from the style attribute
     * @param comments       the list of comments associated with this node
     * @param sourceLocation the source location of this node in the original source
     */
    @JsonCreator
    public StyleAttributeValue(@JsonProperty("type") final Type type,
                               @JsonProperty("value") final String value,
                               @JsonProperty("nodes") final List<CssNode> nodes,
                               @JsonProperty("comments") final List<AstComment> comments,
                               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(type, value, sourceLocation);
        this.nodes = nodes;
        this.comments = comments;
        HtmlUtil.setAstParent(this);
    }

    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(nodes);
    }

    @Override
    public List<CssNode> getNodes() {
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
    protected void astParent(final AstNode parent) {
        super.astParent(parent);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(StyleAttributeValue.class.getSimpleName());
        result.append(" [type=");
        result.append(getType());
        result.append(", value=");
        result.append(getValue());
        result.append(", nodes=");
        result.append(nodes.size());
        if (comments != null && !comments.isEmpty()) {
            result.append(", comments=");
            result.append(comments.size());
        }
        if (getSourceLocation() != null) {
            result.append(", sourceLocation=");
            result.append(getSourceLocation());
        }
        result.append("]");
        return result.toString();
    }

}
