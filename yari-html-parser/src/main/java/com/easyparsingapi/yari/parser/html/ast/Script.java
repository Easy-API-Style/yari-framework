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
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing a JavaScript script block embedded in an HTML document.
 * This node extends {@link Javascript} and implements {@link XmlNode} in order to be integrated
 * into the XML/HTML syntax tree while carrying the parsed JavaScript content.
 */
@JsonPropertyOrder({"nodes", "comments", "sourceLocation"})
public class Script extends Javascript implements XmlNode {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a Script node without source location information.
     *
     * @param nodes    the list of child JavaScript nodes of the script
     * @param comments the list of comments associated with the script
     */
    public Script(final List<JavascriptNode> nodes,
                  final List<AstComment> comments) {
        this(nodes, comments, null);
    }

    /**
     * Constructs a Script node with all the information required for its JSON deserialization.
     *
     * @param nodes          the list of child JavaScript nodes of the script
     * @param comments       the list of comments associated with the script
     * @param sourceLocation the location of the node in the source file, or {@code null} if unknown
     */
    @JsonCreator
    public Script(@JsonProperty("nodes") final List<JavascriptNode> nodes,
                  @JsonProperty("comments") final List<AstComment> comments,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(nodes, comments, sourceLocation);
    }

    @Override
    protected void astParent(final AstNode parent) {
        super.astParent(parent);
    }

}
