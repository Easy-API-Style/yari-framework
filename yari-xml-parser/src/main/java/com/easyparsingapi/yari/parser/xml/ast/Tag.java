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

import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a complete XML element with a head, body and foot (e.g., {@code <tag>...</tag>}).
 */
public class Tag extends TagAbstract<TagBody> implements XmlNode {

    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a Tag with the given head, body and foot, and no source location.
     *
     * @param head the opening tag element
     * @param body the body containing child nodes
     * @param foot the closing tag element
     */
    public Tag(final TagHead head,
               final TagBody body,
               final TagFoot foot) {
        this(head, body, foot, null);
    }

    /**
     * Creates a Tag with all fields.
     *
     * @param head           the opening tag element
     * @param body           the body containing child nodes
     * @param foot           the closing tag element
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public Tag(@JsonProperty("head") final TagHead head,
               @JsonProperty("body") final TagBody body,
               @JsonProperty("foot") final TagFoot foot,
               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(head, body, foot, sourceLocation);
    }

}
