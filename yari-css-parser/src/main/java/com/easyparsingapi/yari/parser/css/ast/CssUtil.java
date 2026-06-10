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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.core.ast.AstNode;

/**
 * Utility class providing helper methods for manipulating CSS AST nodes,
 * in particular for setting parent references across the node hierarchy.
 */
public class CssUtil {

    /** Not instantiable — all methods are static. */
    private CssUtil() {}


    private static final Logger LOGGER = LoggerFactory.getLogger(CssUtil.class);

    /**
     * Recursively sets the parent reference of every child node in the subtree
     * rooted at the given node, dispatching on each concrete CSS AST node type.
     * Unrecognised child types are logged as warnings.
     *
     * @param node the root AST node whose descendants will have their parent set
     */
    public static void setAstParent(final AstNode node) {
        for (final AstNode child : node.astChildren()) {
            switch (child) {
                case Collection<?> c -> c.stream()
                                         .filter(v -> v instanceof AstNode)
                                         .map(v -> (AstNode) v)
                                         .forEach(CssUtil::setAstParent);
                case AtRuleName c -> c.astParent(node);
                case AttributeSelector c -> c.astParent(node);
                case AttributeSelector.Expression c -> c.astParent(node);
                case ClassSelector c -> c.astParent(node);
                case CombinatorSelector c -> c.astParent(node);
                case Important c -> c.astParent(node);
                case AtRule c -> c.astParent(node);
                case RuleSet c -> c.astParent(node);
                case Css c -> c.astParent(node);
                case Function c -> c.astParent(node);
                case Function.Signature c -> c.astParent(node);
                case Identifier c -> c.astParent(node);
                case IdSelector c -> c.astParent(node);
                case Infix c -> c.astParent(node);
                case ListValue c -> c.astParent(node);
                case Literal c -> c.astParent(node);
                case NamespaceSelector c -> c.astParent(node);
                case Nesting c -> c.astParent(node);
                case Operator c -> c.astParent(node);
                case Parenthesis c -> c.astParent(node);
                case Prefix c -> c.astParent(node);
                case PrefixSelector c -> c.astParent(node);
                case PseudoClassSelector c -> c.astParent(node);
                case PseudoElementSelector c -> c.astParent(node);
                case PseudoFunctionSelector c -> c.astParent(node);
                case PseudoFunctionSelector.Signature    c -> c.astParent(node);
                case QualifiedSelector c -> c.astParent(node);
                case Block c -> c.astParent(node);
                case Property c -> c.astParent(node);
                case ListSelector c -> c.astParent(node);
                case ElementSelector c -> c.astParent(node);
                case Universal c -> c.astParent(node);
                case ListParameter c -> c.astParent(node);
                case QualifiedIdentifier c -> c.astParent(node);
                case CssError c -> c.astParent(node);
                case NthPattern c -> c.astParent(node);
                case Range c -> c.astParent(node);
                default -> LOGGER.warn("Unrecognized type of {}", child);
            }
        }
    }

    /**
     * Sets the parent reference of the given {@link Css} node to the specified parent.
     *
     * @param css    the CSS root node whose parent will be set
     * @param parent the AST node to assign as parent
     */
    public static void setAstParent(final Css css, final AstNode parent) {
        css.astParent(parent);
    }

    /**
     * Sets the parent reference of the given {@link Property} node to the specified parent.
     *
     * @param property the CSS property node whose parent will be set
     * @param parent   the AST node to assign as parent
     */
    public static void setAstParent(final Property property, final AstNode parent) {
        property.astParent(parent);
    }

}
