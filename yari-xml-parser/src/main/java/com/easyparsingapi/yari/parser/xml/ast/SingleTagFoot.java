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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents the closing token of a self-closing (empty-element) XML tag (e.g., {@code />}).
 */
@JsonPropertyOrder({"name", "sourceLocation"})
public class SingleTagFoot extends TagFoot {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a SingleTagFoot by copying the name and source location from an existing {@link TagFoot}.
     *
     * @param tagFoot the tag foot to copy
     */
    public SingleTagFoot(final TagFoot tagFoot) {
        this(tagFoot.getName(),
             tagFoot.getSourceLocation());
    }

    /**
     * Creates a SingleTagFoot with the given name and source location.
     *
     * @param name           the tag name associated with this closing token
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public SingleTagFoot(@JsonProperty("name") final TagName name,
                         @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(name, sourceLocation);
    }
    
}
