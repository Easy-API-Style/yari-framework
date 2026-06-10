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
 * Represents the qualified name of an XML tag, optionally prefixed with a namespace.
 */
@JsonPropertyOrder({"namespace", "name", "sourceLocation"})
public class TagName extends Markup {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a TagName with the given namespace and local name, and no source location.
     *
     * @param namespace the namespace prefix, or {@code null} if none
     * @param name      the local name part
     */
    public TagName(final Namespace namespace,
                   final Name name) {
        this(namespace, name, null);
    }

    /**
     * Creates a TagName with the given namespace, local name and source location.
     *
     * @param namespace      the namespace prefix, or {@code null} if none
     * @param name           the local name part
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public TagName(@JsonProperty("namespace") final Namespace namespace,
                   @JsonProperty("name") final Name name,
                   @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(namespace, name, sourceLocation);
    }
    
}