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
package com.easyparsingapi.yari.parsec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.easyparsingapi.yari.parsec.ParseContext.Result;

/**
 * Represents the syntactical structure of the input being parsed.
 */
public final class ParseTree {

    private final String name;
    private final int beginIndex;
    private final int endIndex;
    private final Object value;
    private final List<ParseTree> children;

    ParseTree(String name,
              int beginIndex,
              int endIndex,
              Object value,
              List<ParseTree> children) {
        this.name = name;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.value = value;
        this.children = Collections.unmodifiableList(children);
    }

    /**
     * Returns the node name, which is specified in {@link Parser#label}.
     *
     * @return the name of this parse tree node
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the index in source where this node starts.
     *
     * @return the inclusive start index of this node in the source
     */
    public int getBeginIndex() {
        return beginIndex;
    }

    /**
     * Returns the index in source where this node ends.
     *
     * @return the exclusive end index of this node in the source
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Returns the parsed value of this node, or {@code null} if it's a failed node.
     *
     * @return the parsed value associated with this node, or {@code null} if absent
     */
    public Object getValue() {
    	Object result = null;
    	if (value instanceof Result r) {
    		result = r.geValue();
    	}
    	else {
    		result = value;
    	}
        return result;
    }

    /**
     * Returns the immutable list of child nodes that correspond to
     * {@link Parser#label labeled} parsers syntactically enclosed inside parent
     * parser.
     *
     * @return an unmodifiable list of child {@link ParseTree} nodes
     */
    public List<ParseTree> getChildren() {
        return children;
    }

    /**
     * Carries contextual information about a {@link ParseTree} node during a
     * {@link ParseTree#walk} traversal, including the node itself, its depth in
     * the tree, and the ordered list of its ancestor nodes.
     *
     * @param parseTree the current node being visited
     * @param deep      the zero-based depth of the current node relative to the
     *                  root node passed to {@link ParseTree#walk}
     * @param parents   the ordered list of ancestor nodes from the walk root down
     *                  to (but not including) the current node
     */
    public static record Handler(ParseTree parseTree,
                                 int deep,
                                 List<ParseTree> parents) {}

    /**
     * Performs a depth-first traversal of this node's children, invoking the
     * given action for each descendant node.
     * <p>
     * The action is called with a {@link Handler} that provides the visited node,
     * its depth relative to {@code this}, and the list of its ancestors. The root
     * node ({@code this}) is never passed to the action; only its descendants are.
     * </p>
     *
     * @param action the callback to invoke for each descendant node; must not be
     *               {@code null}
     */
    public void walk(Consumer<Handler> action) {
        walkRecursive(action, new Handler(this, 0, List.of()));
    }

    private static void walkRecursive(Consumer<Handler> action,
                                      Handler handler) {
        List<ParseTree> children = handler.parseTree().getChildren();
        if (children != null) {
            List<ParseTree> parents = new ArrayList<>(handler.parents());
            parents.add(handler.parseTree());
            int deep = handler.deep() + 1;
            for (ParseTree child : children) {
                Handler newHandler = new Handler(child, deep, parents);
                action.accept(newHandler);
                walkRecursive(action, newHandler);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getName());
        result.append("[start:");
        result.append(getBeginIndex());
        result.append(", end:");
        result.append(getEndIndex());
        result.append(", value:");
        result.append(getValue());
        result.append("]");
        walk(h -> {
            result.append(System.lineSeparator());
            result.append(com.google.common.base.Strings.repeat(" ", h.deep() * 2));
            result.append(h.parseTree().getName());
            result.append("[start:");
            result.append(h.parseTree().getBeginIndex());
            result.append(", end:");
            result.append(h.parseTree().getEndIndex());
            result.append(", value:");
            result.append(h.parseTree().getValue());
            result.append("]");
        });
        return result.toString();
    }

}
