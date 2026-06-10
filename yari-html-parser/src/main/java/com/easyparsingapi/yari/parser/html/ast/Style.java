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
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.css.ast.Css;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing an HTML {@code <style>} element containing embedded CSS.
 * <p>
 * This node extends {@link Css} in order to hold the parsed CSS syntax tree
 * and implements {@link XmlNode} to integrate into the parent HTML/XML tree.
 * </p>
 */
@JsonPropertyOrder({"nodes", "comments", "sourceLocation"})
public class Style extends Css implements XmlNode {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code Style} node without source location information.
     *
     * @param nodes    the list of child CSS nodes contained in the style element
     * @param comments the list of comments associated with this node
     */
    public Style(final List<CssNode> nodes,
                 final List<AstComment> comments) {
        this(nodes, comments, null);
    }

    /**
     * Constructs a {@code Style} node with all required information,
     * used in particular during JSON deserialization via Jackson.
     *
     * @param nodes          the list of child CSS nodes contained in the style element
     * @param comments       the list of comments associated with this node
     * @param sourceLocation the location of this node in the source file, or {@code null}
     */
    @JsonCreator
    public Style(@JsonProperty("nodes") final List<CssNode> nodes,
                 @JsonProperty("comments") final List<AstComment> comments,
                 @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(nodes, comments, sourceLocation);
    }

    @Override
    protected void astParent(final AstNode parent) {
        super.astParent(parent);
    }

}
