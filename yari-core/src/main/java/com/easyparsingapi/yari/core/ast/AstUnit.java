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
package com.easyparsingapi.yari.core.ast;

import java.util.List;

/**
 * Represents a syntactic unit in the AST that can carry associated comments.
 * An {@code AstUnit} extends {@link AstNode} and provides access to the comments
 * attached to itself or to any of its child nodes at specific relative positions.
 */
public interface AstUnit extends AstNode {

    /**
     * Defines the position of a comment relative to a given {@link AstNode}.
     * <ul>
     *   <li>{@code before} — the comment appears before the node.</li>
     *   <li>{@code between} — the comment appears between two adjacent tokens of the node.</li>
     *   <li>{@code after} — the comment appears after the node.</li>
     * </ul>
     */
    public static enum Position {
        /** The comment appears before the node. */
        before,
        /** The comment appears between two adjacent tokens of the node. */
        between,
        /** The comment appears after the node. */
        after
    }

    /**
     * Returns all comments associated with this unit.
     *
     * @return a list of {@link AstComment} instances belonging to this unit; never {@code null}.
     */
    public List<AstComment> astComments();

    /**
     * Returns the comments associated with a specific child node at the given positions.
     * If no positions are specified, comments at all positions are returned.
     *
     * @param node      the child {@link AstNode} whose comments are requested.
     * @param positions the relative positions to filter by; if empty, all positions are included.
     * @return a list of {@link AstComment} instances matching the specified criteria; never {@code null}.
     */
    public List<AstComment> astCommentsOf(final AstNode node,
                                          final Position... positions);

}
