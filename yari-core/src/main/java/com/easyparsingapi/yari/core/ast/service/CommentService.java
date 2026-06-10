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
package com.easyparsingapi.yari.core.ast.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.ast.AstUnit;
import com.easyparsingapi.yari.core.ast.service.CommentService;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

/**
 * Service responsible for resolving the {@link AstComment} nodes that are
 * positionally associated with a given {@link AstNode} within an
 * {@link AstUnit} (before, after, or between the node's source boundaries).
 */
public class CommentService {

    private final ListMultimap<Position, SourceLocalisable> sortedNodes = MultimapBuilder.treeKeys().arrayListValues().build();
    private final List<Position> sortedPositions;

    /**
     * Builds the internal position index from all AST nodes and comments
     * declared in the given compilation unit.
     *
     * @param astUnit the compilation unit whose nodes and comments are indexed
     */
    public CommentService(final AstUnit astUnit) {
        super();
        astUnit.astStream().forEach(v -> {
            sortedNodes.put(v.getSourceLocation().start(), v);
            sortedNodes.put(v.getSourceLocation().end(), v);
        });
        astUnit.astComments().forEach(v -> {
            sortedNodes.put(v.getSourceLocation().start(), v);
            sortedNodes.put(v.getSourceLocation().end(), v);
        });
        sortedPositions = new ArrayList<>(sortedNodes.keySet());
    }

    /**
     * Returns the list of {@link AstComment} nodes that are adjacent to
     * {@code astNode} at the requested relative positions.
     *
     * <p>For each requested {@link AstUnit.Position}:
     * <ul>
     *   <li>{@code before} — comments that appear immediately before the start
     *       of {@code astNode}, with no intervening non-comment nodes.</li>
     *   <li>{@code after} — comments that appear immediately after the end of
     *       {@code astNode}, with no intervening non-comment nodes.</li>
     *   <li>{@code between} — comments located between the start and the end
     *       of {@code astNode} (i.e. inside it).</li>
     * </ul>
     *
     * @param astNode   the node whose surrounding comments are requested;
     *                  returns an empty list if {@code null}
     * @param positions the relative positions to search; returns an empty list
     *                  if {@code null} or empty
     * @return an ordered, deduplicated list of matching {@link AstComment} nodes
     */
    public List<AstComment> astCommentsOf(final AstNode astNode,
                                          final AstUnit.Position... positions) {
        final Set<AstComment> result = new TreeSet<>();
        if (astNode != null && positions != null) {
            final Set<AstUnit.Position> allPositions = CollectionUtil.toSet(positions);
            if (allPositions.contains(AstUnit.Position.before)) {
                final int index = sortedPositions.indexOf(astNode.getSourceLocation().start());
                final ListIterator<Position> iterator = sortedPositions.listIterator(index);
                while (iterator.hasPrevious()) {
                    final Position previous = iterator.previous();
                    final List<SourceLocalisable> sourceLocalisables = sortedNodes.get(previous);
                    boolean doBreak = false;
                    for (SourceLocalisable sourceLocalisable : sourceLocalisables) {
                        if (sourceLocalisable instanceof AstComment) {
                            result.add((AstComment) sourceLocalisable);
                        }
                        else {
                            doBreak = true;
                            break;
                        }
                    }
                    if (doBreak) {
                        break;
                    }
                }
            }
            if (allPositions.contains(AstUnit.Position.after)) {
                final int index = sortedPositions.indexOf(astNode.getSourceLocation().end()) + 1;
                final ListIterator<Position> iterator = sortedPositions.listIterator(index);
                while (iterator.hasNext()) {
                    final Position next = iterator.next();
                    final Collection<SourceLocalisable> sourceLocalisables = sortedNodes.get(next);
                    boolean doBreak = false;
                    for (SourceLocalisable sourceLocalisable : sourceLocalisables) {
                        if (sourceLocalisable instanceof AstComment) {
                            result.add((AstComment) sourceLocalisable);
                        }
                        else {
                            doBreak = true;
                            break;
                        }
                    }
                    if (doBreak) {
                        break;
                    }
                }
            }
            if (allPositions.contains(AstUnit.Position.between)) {
                final int index = sortedPositions.indexOf(astNode.getSourceLocation().start());
                final ListIterator<Position> iterator = sortedPositions.listIterator(index + 1);
                while (iterator.hasNext()) {
                    final Position next = iterator.next();
                    final Collection<SourceLocalisable> sourceLocalisables = sortedNodes.get(next);
                    boolean doBreak = false;
                    for (SourceLocalisable sourceLocalisable : sourceLocalisables) {
                        if (sourceLocalisable instanceof AstComment) {
                            result.add((AstComment) sourceLocalisable);
                        }
                        else if (sourceLocalisable == astNode) {
                            doBreak = true;
                            break;
                        }
                    }
                    if (doBreak) {
                        break;
                    }
                }
            }
        }
        return new ArrayList<>(result);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(CommentService.class.getSimpleName());
        result.append(" [nodes=");
        result.append(sortedNodes.values().size());
        result.append("]");
        return result.toString();
    }

    /*
     *
     * CLASS
     *
     */
    record Limit(Position lower, Position upper) {

        public boolean isBetween(final AstComment astComment) {
            final SourceLocation sourceLocation = astComment.getSourceLocation();
            final int compareLower = sourceLocation.start().compareTo(lower);
            final int compareUpper = upper.compareTo(sourceLocation.end());
            return compareLower >= 0  && compareUpper >= 0;
        }

    }

}
