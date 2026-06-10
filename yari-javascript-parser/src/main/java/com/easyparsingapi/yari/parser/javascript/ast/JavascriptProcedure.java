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
package com.easyparsingapi.yari.parser.javascript.ast;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents a JavaScript AST node that contains an ordered sequence of statements,
 * such as a function body, block statement, or the top-level compilation unit.
 * Provides convenience methods for querying and streaming the child statement nodes.
 */
public interface JavascriptProcedure extends JavascriptNode {

    /**
     * Returns the number of statements in this procedure body.
     *
     * @return the statement count
     */
    public default int size() {
        return getNodes().size();
    }

    /**
     * Returns {@code true} if this procedure body contains at least one statement.
     *
     * @return {@code true} when one or more statements are present
     */
    public default boolean hasNode() {
        return !getNodes().isEmpty();
    }

    /**
     * Returns a sequential stream over the statements in this procedure body.
     *
     * @return a {@link Stream} of {@link JavascriptNode} elements
     */
    public default Stream<JavascriptNode> streamNodes() {
        return getNodes().stream();
    }

    /**
     * Returns the statement at the given zero-based index, or {@code null} if the
     * index is out of range.
     *
     * @param index the zero-based position of the desired statement
     * @return the statement node, or {@code null}
     */
    public default JavascriptNode getNode(final int index) {
        JavascriptNode result = null;
        if (index < size()) {
            result = getNodes().get(index);
        }
        return result;
    }
    
    /**
     * Returns the list of statements in this procedure block.
     *
     * @return the list of {@link JavascriptNode} statements
     */
    public List<JavascriptNode> getNodes();
    
}
