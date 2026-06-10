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
package com.easyparsingapi.yari.parser.xml.lexer;

import java.util.ArrayList;
import java.util.List;

import com.easyparsingapi.yari.parser.xml.ast.Tag;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute;
import com.easyparsingapi.yari.parser.xml.ast.Markup.Namespace;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute.Value;

/**
 * A lightweight data record representing a parsed XML tag with its markup name and attribute list,
 * used internally by the lexer before AST nodes are constructed.
 *
 * @param markup     the markup name of the tag
 * @param attributes the list of attributes on the tag
 */
public record TagEntity(Markup markup, List<Attribute> attributes) {

    /**
     * A key-value pair representing a single attribute of a parsed tag.
     *
     * @param markup the attribute name (namespace + local name)
     * @param value  the attribute value string, or {@code null} if absent
     */
    public static record Attribute(Markup markup, String value) {}

    /**
     * A namespace-qualified name for a tag or attribute.
     *
     * @param namespace the namespace prefix, or {@code null} if none
     * @param name      the local name
     */
    public static record Markup(String namespace, String name) {
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            if (namespace != null) {
                result.append(namespace);
                result.append(":");
            }
            result.append(name);
            return result.toString();
        }
    }

    /*
     * 
     * STATIC
     * 
     */
    /**
     * Converts a {@link Tag} AST node into a {@link TagEntity} record.
     *
     * @param tag the tag AST node to convert
     * @return a new {@code TagEntity} reflecting the tag's name and attributes
     */
    public static TagEntity toTagEntity(final Tag tag) {
        final Namespace namespace = tag.getName().namespace();
        final Markup markup = new Markup(namespace != null 
                                              ? namespace.getValue()
                                              : null, 
                                         tag.getName().name().getValue());
        final List<Attribute> attributes = new ArrayList<>();
        for (final TagAttribute tagAttribute : tag.getAttributes()) {
            final Namespace attributeNamespace = tagAttribute.getName().namespace();
            final Markup name = new Markup(attributeNamespace != null 
                                               ? attributeNamespace.getValue() 
                                               : null, 
                                           tagAttribute.getName().name().getValue());
            final Value attributeValue = tagAttribute.getValue();
            attributes.add(new Attribute(name, 
                                         attributeValue != null 
                                            ? attributeValue.getValue() 
                                            : null));
        }
        return new TagEntity(markup, attributes);
    }
    
    /**
     * Converts a {@link TagAttribute} AST node into an {@link Attribute} record.
     *
     * @param tagAttribute the attribute AST node to convert
     * @return a new {@code Attribute} reflecting the attribute's name and value
     */
    public static Attribute toAttribute(final TagAttribute tagAttribute) {
        final Namespace namespace = tagAttribute.getName().namespace();
        final Markup name = new Markup(namespace != null 
                                           ? namespace.getValue() 
                                           : null, 
                                       tagAttribute.getName().name().getValue());
        final Value attributeValue = tagAttribute.getValue();
        return new Attribute(name, 
                             attributeValue != null 
                                 ? attributeValue.getValue() 
                                 : null);
    }
    
    /**
     * Creates a {@link Markup} record from a raw name string, splitting on {@code :} if a namespace prefix is present.
     *
     * @param name the raw name string, optionally namespace-qualified (e.g., {@code ns:element})
     * @return a new {@code Markup} with the parsed namespace and local name
     */
    public static Markup newMarkup(final String name) {
        if (name.contains(":")) {
            final int index = name.indexOf(":");
            return new Markup(name.substring(0, index), 
                              name.substring(index + 1, name.length()));
        }
        else {
            return new Markup(null, name);
        }
    }
    
    /**
     * Creates an {@link Attribute} record from a raw name and value string,
     * stripping surrounding quotes from the value if present.
     *
     * @param name  the raw attribute name, optionally namespace-qualified
     * @param value the raw attribute value string (may be quoted or {@code null})
     * @return a new {@code Attribute} with the parsed name and unquoted value
     */
    public static Attribute newAttribute(final String name,
                                         final String value) {
        String attributeValue = null;
        if (value != null) {
            if (value.startsWith("\"") 
                   && value.endsWith("\"")) {
                attributeValue = value.substring(1, value.length() - 1);
            }
            else if (value.startsWith("'") 
                        && value.endsWith("'")) {
                attributeValue = value.substring(1, value.length() - 1);
            }
            else {
                attributeValue = value;
            }
        }
       return new Attribute(newMarkup(name), attributeValue);
    }
    
}
