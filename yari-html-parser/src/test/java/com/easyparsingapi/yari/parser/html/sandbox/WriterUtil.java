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
package com.easyparsingapi.yari.parser.html.sandbox;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parser.html.ast.Script;
import com.easyparsingapi.yari.parser.html.ast.ScriptAttributeValue;
import com.easyparsingapi.yari.parser.html.ast.Style;
import com.easyparsingapi.yari.parser.html.ast.StyleAttributeValue;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute;
import com.easyparsingapi.yari.parser.xml.ast.TagBody;
import com.easyparsingapi.yari.parser.xml.ast.TagComplex;
import com.easyparsingapi.yari.parser.xml.ast.TagEmpty;
import com.easyparsingapi.yari.parser.xml.ast.TagFoot;
import com.easyparsingapi.yari.parser.xml.ast.TagHead;
import com.easyparsingapi.yari.parser.xml.ast.Xml;
import com.easyparsingapi.yari.parser.xml.ast.XmlComment;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute.Value.Type;

public class WriterUtil {
    
    private static Map<Class<? extends SourceLocalisable>, Function<AstNode, String>> toCode = new HashMap<>();
    static {
        toCode.put(Xml.class, n -> {
            Xml node = Xml.class.cast(n);
            return String.join(" ", toCode(node.getNodes()));
        });
        
        toCode.put(TagComplex.class, n -> {
            TagComplex<?> node = TagComplex.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(toCode(node.getHead()));
            result.append(toCode(node.getBody()));
            result.append(toCode(node.getFoot()));
            return result.toString();
        });
        
        toCode.put(TagEmpty.class, n -> {
            TagEmpty node = TagEmpty.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("<");
            result.append(node.getName());
            if (node.hasAttribute()) {
                result.append(" ");
                result.append(String.join(" ", toCode(node.getAttributes())));
            }
            if (node.isClosed()) {
                result.append("/>");
            }
            else {
                result.append(">");
            }
            return result.toString();
        });
        
        toCode.put(TagHead.class, n -> {
            TagHead node = TagHead.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("<");
            result.append(node.getName());
            if (node.hasAttribute()) {
                result.append(" ");
                result.append(String.join(" ", toCode(node.getAttributes())));
            }
            result.append(">");
            return result.toString();
        });
        toCode.put(TagBody.class, n -> {
            TagBody node = TagBody.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(String.join("", toCode(node.getNodes())));
            return result.toString();
        });
        toCode.put(TagFoot.class, n -> {
            TagFoot node = TagFoot.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("</");
            result.append(node.getName());
            result.append(">");
            return result.toString();
        });
        
        toCode.put(TagAttribute.class, n -> {
            TagAttribute node = TagAttribute.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append(node.getName());
            if (node.hasValue()) {
                result.append("=");
                result.append(node.getValue());
            }
            return result.toString();
        });
        toCode.put(TagAttribute.Value.class, n -> {
            TagAttribute.Value node = TagAttribute.Value.class.cast(n);
            final StringBuilder result = new StringBuilder();
            if (Type.singleQuote == node.getType()) {
                result.append("'");
            }
            else {
                result.append("\"");
            }
            result.append(node.getValue());
            if (Type.singleQuote == node.getType()) {
                result.append("'");
            }
            else {
                result.append("\"");
            }
            return result.toString();
        });
        
        toCode.put(XmlComment.class, n -> {
            XmlComment node = XmlComment.class.cast(n);
            final StringBuilder result = new StringBuilder();
            result.append("< !--");
            result.append(node.getComment()
                              .replace("\r", "")
                              .replace(" \n", " ")
                              .replace("\n", ""));
            result.append("-->");
            return result.toString();
        });
        
        toCode.put(StyleAttributeValue.class, n -> {
            StyleAttributeValue node = StyleAttributeValue.class.cast(n);
            return String.join(";", toCode(node.getNodes()));
        });
        toCode.put(ScriptAttributeValue.class, n -> {
            ScriptAttributeValue node = ScriptAttributeValue.class.cast(n);
            return String.join(";", toCode(node.getNodes()));
        });
        
        toCode.put(Script.class, n -> {
            Script node = Script.class.cast(n);
            return String.join(";", toCode(node.getNodes()));
        });
        
        toCode.put(Style.class, n -> {
            Style node = Style.class.cast(n);
            return String.join(";", toCode(node.getNodes()));
        });
    }

    public static List<String> toCode(final Collection<? extends AstNode> astNodes) {
        return astNodes.stream()
                       .map(n -> toCode.get(n.getClass()).apply(n))
                       .collect(Collectors.toList());
    }

    public static String toCode(final AstNode astNode) {
        String result = null;
        if(astNode != null) {
            result = toCode.get(astNode.getClass()).apply(astNode);
        }
        return result;
    }

}
