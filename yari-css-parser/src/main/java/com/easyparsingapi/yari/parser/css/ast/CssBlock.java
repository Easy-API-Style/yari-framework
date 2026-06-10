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
package com.easyparsingapi.yari.parser.css.ast;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents a CSS block node that contains an ordered list of child {@link CssNode} elements.
 * <p>
 * A block groups multiple CSS nodes (e.g. declarations inside a rule-set or nested rules inside
 * an at-rule). Implementations must supply the backing list via {@link #getNodes()};
 * all other methods are provided as default implementations on top of that list.
 * </p>
 */
public interface CssBlock extends CssNode {

    /**
     * Returns the number of child nodes contained in this block.
     *
     * @return the size of the node list, or {@code 0} if the block is empty
     */
    public default int size() {
        return getNodes().size();
    }

    /**
     * Returns the child node at the specified position, or {@code null} if the index is
     * out of bounds.
     *
     * @param index zero-based position of the desired node
     * @return the {@link CssNode} at {@code index}, or {@code null} when {@code index >= size()}
     */
    public default CssNode getNode(final int index) {
        CssNode result = null;
        if (index < size()) {
            result = getNodes().get(index);
        }
        return result;
    }

    /**
     * Indicates whether this block contains at least one child node.
     *
     * @return {@code true} if the node list is non-empty, {@code false} otherwise
     */
    public default boolean hasNode() {
        return !getNodes().isEmpty();
    }

    /**
     * Returns a sequential {@link Stream} over the child nodes of this block.
     *
     * @return a stream of {@link CssNode} elements in list order
     */
    public default Stream<CssNode> streamNodes() {
        return getNodes().stream();
    }

    /**
     * Returns the ordered list of child nodes that make up the content of this block.
     *
     * @return a non-null, possibly empty list of {@link CssNode} elements
     */
    public List<CssNode> getNodes();

}
