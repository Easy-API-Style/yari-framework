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

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.xml.ast.TagAbstract;
import com.easyparsingapi.yari.parser.xml.ast.TagFoot;
import com.easyparsingapi.yari.parser.xml.ast.TagHead;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents an HTML {@code <style>} tag node in the abstract syntax tree,
 * holding a {@link Style} body that contains the embedded CSS content.
 */
@JsonPropertyOrder({"head", "body", "foot", "sourceLocation"})
public class StyleTag extends TagAbstract<Style> implements XmlNode {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code StyleTag} with all components and an explicit parent node.
     *
     * @param head           the opening tag head (e.g. {@code <style ...>})
     * @param cssScript      the {@link Style} body containing the CSS content
     * @param foot           the closing tag foot (e.g. {@code </style>})
     * @param sourceLocation the location of this tag in the source file, or {@code null} if unknown
     * @param parent         the parent AST node to attach this tag to
     */
    public StyleTag(final TagHead head,
                    final Style cssScript,
                    final TagFoot foot,
                    final SourceLocation sourceLocation,
                    final AstNode parent) {
        this(head, cssScript, foot, sourceLocation);
        this.astParent(parent);
    }

    /**
     * Constructs a {@code StyleTag} without a source location.
     *
     * @param head  the opening tag head
     * @param style the {@link Style} body containing the CSS content
     * @param foot  the closing tag foot
     */
    public StyleTag(final TagHead head,
                    final Style style,
                    final TagFoot foot) {
        this(head, style, foot, null);
    }

    /**
     * Constructs a {@code StyleTag} from its JSON-deserialized components.
     *
     * @param head           the opening tag head
     * @param cssScript      the {@link Style} body containing the CSS content
     * @param foot           the closing tag foot
     * @param sourceLocation the location of this tag in the source file, or {@code null} if unknown
     */
    @JsonCreator
    public StyleTag(@JsonProperty("head") final TagHead head,
                    @JsonProperty("body") final Style cssScript,
                    @JsonProperty("foot") final TagFoot foot,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(head, cssScript, foot, sourceLocation);
    }

}
