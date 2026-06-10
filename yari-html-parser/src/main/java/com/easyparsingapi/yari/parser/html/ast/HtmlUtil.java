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
package com.easyparsingapi.yari.parser.html.ast;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parser.css.ast.Css;
import com.easyparsingapi.yari.parser.css.ast.CssUtil;
import com.easyparsingapi.yari.parser.css.ast.Property;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptUtil;

/**
 * Utility class providing helper methods for manipulating HTML AST nodes,
 * including recursive parent-node assignment across mixed HTML, CSS and JavaScript subtrees.
 */
public class HtmlUtil {

    /** Not instantiable — all methods are static. */
    private HtmlUtil() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlUtil.class);

    /**
     * Recursively sets the parent reference on every child node of the given AST node.
     * <p>
     * Each direct child is dispatched by type: collections are iterated, {@link Script} and
     * {@link Style} nodes have their parent set directly, {@link Css} and {@link Property} nodes
     * are delegated to {@link CssUtil#setAstParent}, {@link Javascript} nodes are delegated to
     * {@link JavascriptUtil#setAstParent}, and any unrecognised type is logged as a warning.
     * </p>
     *
     * @param node the AST node whose children should have their parent reference initialised
     */
    public static void setAstParent(final AstNode node) {
        for (final AstNode child : node.astChildren()) {
            switch (child) {
                case Collection<?> c -> c.stream()
                                         .filter(v -> v instanceof AstNode)
                                         .map(v -> (AstNode) v)
                                         .forEach(HtmlUtil::setAstParent);
                case Script c -> c.astParent(node);
                case Style c -> c.astParent(node);
                case Css c -> CssUtil.setAstParent(c, node);
                case Property c -> CssUtil.setAstParent(c, node);
                case Javascript c -> JavascriptUtil.setAstParent(c, node);
                default -> {
                    LOGGER.warn("Unrecognized type of {}", child.getClass().getName());
                }
            }
        }
    }

}
