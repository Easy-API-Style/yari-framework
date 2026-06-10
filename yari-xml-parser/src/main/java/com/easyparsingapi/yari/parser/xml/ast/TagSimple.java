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

import java.util.List;

import com.easyparsingapi.yari.core.util.CollectionUtil;

/**
 * Represents a simple XML tag that has a name and an optional list of attributes.
 */
public interface TagSimple extends XmlNode {

    /**
     * Returns the tag name.
     *
     * @return the tag name
     */
    public TagName getName();

    /**
     * Returns true if this tag has at least one attribute.
     *
     * @return {@code true} if the attribute list is non-empty
     */
    public default boolean hasAttribute() {
        return !CollectionUtil.isEmpty(getAttributes());
    }

    /**
     * Returns the list of attributes of this tag.
     *
     * @return the list of tag attributes
     */
    public List<TagAttribute> getAttributes();

}
