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
 * An XML node that contains a list of child XML nodes.
 */
public interface XmlNodeContainer extends XmlNode {

    /**
     * Returns true if this container has at least one child node.
     *
     * @return {@code true} if the child node list is non-empty
     */
    public default boolean hasNode() {
        return !CollectionUtil.isEmpty(getNodes());
    }

    /**
     * Returns the number of child nodes.
     *
     * @return the size of the child node list
     */
    public default int size() {
        return getNodes().size();
    }

    /**
     * Returns the child node at the given index, or {@code null} if the index is out of range.
     *
     * @param index the zero-based index of the child node to retrieve
     * @return the child node at {@code index}, or {@code null} if {@code index >= size()}
     */
    public default XmlNode getNode(final int index) {
        XmlNode result = null;
        if (index < size()) {
            result = getNodes().get(index);
        }
        return result;
    }

    /**
     * Returns the list of child XML nodes.
     *
     * @return the list of child nodes
     */
    public List<XmlNode> getNodes();

}
