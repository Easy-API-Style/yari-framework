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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a node in an Abstract Syntax Tree (AST).
 * Each node can have a parent, a list of children, source location information,
 * and type metadata. Provides utilities for recursive tree traversal in both
 * top-down (children) and bottom-up (parents) directions.
 */
public interface AstNode extends SourceLocalisable, TypeInfo, Serializable {

    /**
     * Returns a depth-first {@link Stream} of this node and all its descendants.
     *
     * @return a stream starting with this node followed by all descendant nodes
     */
    @JsonIgnore
    public default Stream<AstNode> astStream() {
        Stream<AstNode> result = Stream.of(this);
        for (final AstNode astNode : astChildren()) {
            result = Stream.concat(result, astNode.astStream());
        }
        return result;
    }

    /**
     * Returns {@code true} if this node has at least one child node.
     *
     * @return {@code true} if {@link #astChildren()} is non-empty, {@code false} otherwise
     */
    @JsonIgnore
    public default boolean hasChildren() {
        return !astChildren().isEmpty();
    }

    /**
     * Returns the direct children of this AST node.
     *
     * @return an ordered list of direct child nodes; never {@code null}
     */
    @JsonIgnore
    public List<AstNode> astChildren();

    /**
     * Walks all descendant nodes recursively, invoking {@code consumer} for each visited node.
     * The walk visits children in order and can be cancelled early by calling
     * {@link Handler#cancel()} inside the consumer.
     *
     * @param consumer a {@link Consumer} that receives a {@link Handler} for each visited node
     */
    public default void walkChildren(final Consumer<Handler> consumer) {
        final AtomicReference<Consumer<Handler>> walkRecursivelyReference = new AtomicReference<>();
        final Consumer<Handler> walkRecursively = h -> {
             for (final AstNode node : h.node().astChildren()) {
                 final List<AstNode> path = new ArrayList<>(h.path());
                 if(!path.contains(h.node())) {
                     path.add(h.node());
                     final Handler newHandler = new Handler(node, h.deep() + 1, path);
                     consumer.accept(newHandler);
                     if (newHandler.cancel) {
                         break;
                     }
                     walkRecursivelyReference.get()
                                             .accept(newHandler);
                 }
             }
        };
        walkRecursivelyReference.set(walkRecursively);
        walkRecursivelyReference.get()
                                .accept(new Handler(this, 0, List.of()));
    }

    /**
     * Returns the direct parent of this AST node, or {@code null} if this node is the root.
     *
     * @return the parent node, or {@code null} if none exists
     */
    @JsonIgnore
    public AstNode astParent();

    /**
     * Returns an ordered list of all ancestor nodes, starting from the immediate parent
     * up to the root of the tree.
     *
     * @return a list of ancestor nodes; empty if this node has no parent
     */
    public default List<AstNode> parents() {
        final List<AstNode> result = new ArrayList<>();
        AstNode parent = astParent();
        while (parent != null) {
            result.add(parent);
            parent = parent.astParent();
        }
        return result;
    }

    /**
     * Walks all ancestor nodes recursively, invoking {@code consumer} for each visited ancestor.
     * The walk travels upward through the tree and can be cancelled early by calling
     * {@link Handler#cancel()} inside the consumer.
     *
     * @param consumer a {@link Consumer} that receives a {@link Handler} for each visited ancestor
     */
    public default void walkParents(final Consumer<Handler> consumer) {
        final AtomicReference<Consumer<Handler>> walkRecursivelyReference = new AtomicReference<>();
        final Consumer<Handler> walkRecursively = h -> {
            final List<AstNode> path = new ArrayList<>(h.path());
            final AstNode parent = h.node().astParent();
            if(parent != null && !path.contains(h.node())) {
                path.add(h.node());
                final Handler newHandler = new Handler(parent, h.deep() + 1, path);
                consumer.accept(newHandler);
                if (!newHandler.cancel) {
                    walkRecursivelyReference.get()
                                            .accept(newHandler);
                }
            }
        };
        walkRecursivelyReference.set(walkRecursively);
        walkRecursivelyReference.get()
                                .accept(new Handler(this, 0, List.of()));
    }

    // equals
    @Override
    public boolean equals(final Object object);

    /**
     * Determines structural equality between this node and another {@link AstNode}.
     * Unlike {@link #equals(Object)}, implementations may compare AST-specific
     * properties independently of object identity.
     *
     * @param astNode the node to compare against; may be {@code null}
     * @return {@code true} if the two nodes are considered structurally equal
     */
    public boolean equalsNode(final AstNode astNode);

    /*
     *
     * CLASS
     *
     */
    /**
     * Carries contextual information about a node being visited during a tree walk.
     * Provides access to the current node, its depth in the traversal, the path of
     * ancestors traversed so far, and a mechanism to cancel the ongoing walk.
     */
    public static class Handler {

        private final AstNode node;
        private final int deep;
        private final List<AstNode> path;

        private boolean cancel;

        /**
         * Creates a new {@code Handler} for the given node at the specified depth.
         *
         * @param node the current AST node being visited
         * @param deep the zero-based depth of this node in the traversal
         * @param path the list of ancestor nodes that led to this node
         */
        public Handler(final AstNode node,
                       final int deep,
                       final List<AstNode> path) {
            super();
            this.node = node;
            this.deep = deep;
            this.path = path;
        }

        /**
         * Returns the zero-based depth of the current node within the traversal.
         *
         * @return the traversal depth of this node
         */
        public int deep() {
            return deep;
        }

        /**
         * Signals that the current tree walk should stop after processing this node.
         * No further siblings or ancestors will be visited once this method is called.
         */
        public void cancel() {
            this.cancel = true;
        }

        /**
         * Returns the AST node currently being visited.
         *
         * @return the current node; never {@code null}
         */
        public AstNode node() {
            return node;
        }

        /**
         * Returns the list of ancestor nodes that were traversed to reach the current node.
         *
         * @return an ordered list of ancestor nodes; empty if this is the starting node
         */
        public List<AstNode> path() {
            return path;
        }

        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(AstNode.class.getSimpleName());
            result.append(".");
            result.append(Handler.class.getSimpleName());
            result.append(" [deep=");
            result.append(deep);
            result.append(", stop=");
            result.append(cancel);
            result.append(", node=");
            result.append(node);
            result.append(", path=");
            result.append(path);
            result.append("]");
            return result.toString();
        }

    }

    /**
     * Collects all {@link AstNode} instances from the given attribute values.
     * Each attribute may be an {@link AstNode} directly or a {@link java.util.Collection} of {@link AstNode}s.
     *
     * @param attributes the attribute values to inspect, may be {@code null} or contain {@code null} entries
     * @return a mutable list of all {@link AstNode} instances found in the given attributes
     */
    static List<AstNode> childrenAttributes(final Object... attributes) {
        final List<AstNode> result = new ArrayList<>();
        if (attributes != null) {
            for (final Object attribute : attributes) {
                if (attribute instanceof Collection<?> collection) {
                    for (final Object value : collection) {
                        if (value instanceof AstNode node) {
                             result.add(node);
                        }
                    }
                }
                else {
                    if (attribute instanceof AstNode node) {
                        result.add(node);
                    }
                }
            }
        }
        return result;
    }

}
