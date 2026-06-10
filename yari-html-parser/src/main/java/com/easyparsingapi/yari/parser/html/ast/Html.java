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
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.xml.ast.Xml;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Root node of the abstract syntax tree (AST) representing an HTML document.
 * Extends {@link Xml} to inherit the XML structure while representing
 * the specifics of an HTML document.
 */
@JsonPropertyOrder({"nodes","comments", "sourceLocation"})
public class Html extends Xml {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a root HTML node without source location information.
     *
     * @param nodes    the list of child nodes making up the HTML document
     * @param comments the list of comments present in the HTML document
     */
    public Html(final List<XmlNode> nodes,
                final List<AstComment> comments) {
        this(nodes, comments, null);
    }

    /**
     * Constructs a root HTML node with all required information,
     * used during JSON deserialization.
     *
     * @param nodes          the list of child nodes making up the HTML document
     * @param comments       the list of comments present in the HTML document
     * @param sourceLocation the location of the document in the source code, may be {@code null}
     */
    @JsonCreator
    public Html(@JsonProperty("nodes") final List<XmlNode> nodes,
                @JsonProperty("comments") final List<AstComment> comments,
                @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(nodes, comments, sourceLocation);
    }

}
