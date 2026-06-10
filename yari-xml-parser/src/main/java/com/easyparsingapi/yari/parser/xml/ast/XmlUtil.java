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
 * Utility class for setting AST parent references on XML nodes.
 */
public class XmlUtil {

    /** Private constructor to prevent instantiation of this utility class. */
    private XmlUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);

    /**
     * Sets the parent reference on all immediate child nodes of the given node.
     *
     * @param node the AST node whose children will have their parent reference set
     */
    public static void setAstParent(final AstNode node) {
        for (final AstNode child : node.astChildren()) {
            switch (child) {
                case Collection<?> c -> c.stream()
                                         .filter(v -> v instanceof AstNode)
                                         .map(v -> (AstNode) v)
                                         .forEach(XmlUtil::setAstParent);
                case Xml c -> c.astParent(node);
                case Text c -> c.astParent(node);
                case TagAbstract<?> c -> c.astParent(node);
                case TagName c -> c.astParent(node);
                case TagHead c -> c.astParent(node);
                case TagBody c -> c.astParent(node);
                case TagFoot c -> c.astParent(node);
                case TagEmpty c -> c.astParent(node);
                case TagAttribute c -> c.astParent(node);
                case TagAttribute.Name c -> c.astParent(node);
                case TagAttribute.Value c -> c.astParent(node);
                case XmlComment c -> c.astParent(node);
                case Prolog c -> c.astParent(node);
                case CData c -> c.astParent(node);
                case Markup.Namespace c -> c.astParent(node);
                case Markup.Name c -> c.astParent(node);
                case TokenError c -> c.astParent(node);
                case DocType c -> c.astParent(node);
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
