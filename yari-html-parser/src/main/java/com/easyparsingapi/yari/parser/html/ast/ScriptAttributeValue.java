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
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptProcedure;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * HTML attribute value whose content is JavaScript code, represented
 * as an AST node combining the {@link AstUnit},
 * {@link JavascriptProcedure} and {@link XmlNode} interfaces.
 */
@JsonPropertyOrder({"value", "type", "nodes", "comments", "sourceLocation"})
public class ScriptAttributeValue extends TagAttribute.Value
                                  implements AstUnit, JavascriptProcedure, XmlNode {

    private static final long serialVersionUID = 1L;

    /** The parsed JavaScript nodes. */
    @JsonProperty("nodes")
    private final List<JavascriptNode> nodes;
    /** The comments found in the script. */
    @JsonProperty("comments")
    private final List<AstComment> comments;
    /** Service for querying comments by node. */
    @JsonIgnore
    private CommentService commentService;

    /**
     * Constructs a {@code ScriptAttributeValue} without source location information.
     *
     * @param type     the type of the attribute value
     * @param value    the raw textual representation of the value
     * @param nodes    the list of JavaScript nodes produced by parsing the value
     * @param comments the list of comments associated with this value
     */
    public ScriptAttributeValue(final Type type,
                                final String value,
                                final List<JavascriptNode> nodes,
                                final List<AstComment> comments) {
        this(type, value, nodes, comments, null);
    }

    /**
     * Constructs a {@code ScriptAttributeValue} with all its properties, used
     * in particular by Jackson deserialization.
     *
     * @param type           the type of the attribute value
     * @param value          the raw textual representation of the value
     * @param nodes          the list of JavaScript nodes produced by parsing the value
     * @param comments       the list of comments associated with this value
     * @param sourceLocation the location of this element in the original source, or {@code null}
     */
    @JsonCreator
    public ScriptAttributeValue(@JsonProperty("type") final Type type,
                                @JsonProperty("value") final String value,
                                @JsonProperty("nodes") final List<JavascriptNode> nodes,
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
    public List<JavascriptNode> getNodes() {
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
        result.append(ScriptAttributeValue.class.getSimpleName());
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
