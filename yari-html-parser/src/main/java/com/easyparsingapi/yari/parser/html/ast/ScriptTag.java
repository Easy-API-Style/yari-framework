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
 * AST node representing an HTML {@code <script>} tag containing a block of JavaScript code.
 * This tag is composed of a tag head, a {@link Script} body, and a tag foot.
 */
@JsonPropertyOrder({"head", "body", "foot", "sourceLocation"})
public class ScriptTag extends TagAbstract<Script> implements XmlNode {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a complete {@code ScriptTag} with a source location and a parent node.
     *
     * @param head           the tag head (e.g. {@code <script ...>})
     * @param script         the body containing the JavaScript code
     * @param foot           the tag foot (e.g. {@code </script>})
     * @param sourceLocation the location of the tag in the source file, may be {@code null}
     * @param parent         the parent node in the AST
     */
    public ScriptTag(final TagHead head,
                     final Script script,
                     final TagFoot foot,
                     final SourceLocation sourceLocation,
                     final AstNode parent) {
        this(head, script, foot, sourceLocation);
        this.astParent(parent);
    }

    /**
     * Constructs a {@code ScriptTag} without source location.
     *
     * @param head       the tag head (e.g. {@code <script ...>})
     * @param javascript the body containing the JavaScript code
     * @param foot       the tag foot (e.g. {@code </script>})
     */
    public ScriptTag(final TagHead head,
                     final Script javascript,
                     final TagFoot foot) {
        this(head, javascript, foot, null);
    }

    /**
     * Primary constructor used for JSON deserialization.
     *
     * @param head           the tag head (e.g. {@code <script ...>})
     * @param script         the body containing the JavaScript code
     * @param foot           the tag foot (e.g. {@code </script>})
     * @param sourceLocation the location of the tag in the source file, may be {@code null}
     */
    @JsonCreator
    public ScriptTag(@JsonProperty("head") final TagHead head,
                     @JsonProperty("body") final Script script,
                     @JsonProperty("foot") final TagFoot foot,
                     @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(head, script, foot, sourceLocation);
    }

}
