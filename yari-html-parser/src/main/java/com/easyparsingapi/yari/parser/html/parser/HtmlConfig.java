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
package com.easyparsingapi.yari.parser.html.parser;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.easyparsingapi.yari.parser.xml.lexer.TagEntity;
import com.easyparsingapi.yari.parser.xml.lexer.TagEntity.Attribute;
import com.easyparsingapi.yari.parser.xml.lexer.TagEntity.Markup;
import com.easyparsingapi.yari.parser.xml.parser.XmlConfig;

/**
 * Configuration for the HTML parser, holding the sets of predicates that identify
 * JavaScript and CSS tags/attributes, as well as the underlying {@link XmlConfig}.
 *
 * <p>Instances are created via {@link #builder()} or retrieved as the pre-built
 * default through {@link #defaulConfig()}.
 */
public class HtmlConfig {

    private final Set<Function<TagEntity, Boolean>> javascriptTags = new HashSet<>();
    private final Set<Function<Attribute, Boolean>> javascriptAttributes = new HashSet<>();
    private final Set<Function<TagEntity, Boolean>> cssTags = new HashSet<>();
    private final Set<Function<Attribute, Boolean>> cssAttributes = new HashSet<>();

    private XmlConfig xmlConfig;

    private HtmlConfig() {
        super();
    }

    /**
     * Returns the set of predicates used to identify HTML tags whose content should
     * be treated as JavaScript.
     *
     * @return a mutable set of tag predicates for JavaScript blocks
     */
    public Set<Function<TagEntity, Boolean>> getJavascriptTags() {
        return javascriptTags;
    }

    /**
     * Returns the set of predicates used to identify HTML attributes whose value
     * should be treated as JavaScript.
     *
     * @return a mutable set of attribute predicates for JavaScript values
     */
    public Set<Function<Attribute, Boolean>> getJavascriptAttributes() {
        return javascriptAttributes;
    }

    /**
     * Returns the set of predicates used to identify HTML tags whose content should
     * be treated as CSS.
     *
     * @return a mutable set of tag predicates for CSS blocks
     */
    public Set<Function<TagEntity, Boolean>> getCssTags() {
        return cssTags;
    }

    /**
     * Returns the set of predicates used to identify HTML attributes whose value
     * should be treated as CSS.
     *
     * @return a mutable set of attribute predicates for CSS values
     */
    public Set<Function<Attribute, Boolean>> getCssAttributes() {
        return cssAttributes;
    }

    /**
     * Returns the underlying XML configuration derived from this HTML configuration.
     *
     * @return the {@link XmlConfig} used by the HTML parser
     */
    public XmlConfig getXmlConfig() {
        return xmlConfig;
    }

    void setXmlConfig(final XmlConfig xmlConfig) {
        this.xmlConfig = xmlConfig;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(HtmlConfig.class.getSimpleName());
        result.append(" [javascriptTags=");
        result.append(javascriptTags.size());
        result.append(", javascriptAttributes=");
        result.append(javascriptAttributes.size());
        result.append(", cssTags=");
        result.append(cssTags.size());
        result.append(", cssAttributes=");
        result.append(cssAttributes.size());
        result.append(", xmlConfig=");
        result.append(xmlConfig);
        result.append("]");
        return result.toString();
    }

    /*
     *
     * BUILDER
     *
     */
    /**
     * Creates a new {@link Builder} for constructing an {@link HtmlConfig} instance.
     *
     * @return a fresh builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Fluent builder for {@link HtmlConfig}.
     *
     * <p>Provides methods to register JavaScript/CSS tag and attribute predicates,
     * configure plain-text tags, and control unclosed-tag tolerance before
     * calling {@link #build()} to produce the final {@link HtmlConfig}.
     */
    public static class Builder {

        private final HtmlConfig htmlConfig = new HtmlConfig();
        private final XmlConfig.Builder xmlConfigBuilder = XmlConfig.builder();

        private Builder() {

        }

        /**
         * Registers a tag identified by namespace and local name as a JavaScript tag.
         *
         * @param namespace the XML namespace of the tag, or {@code null} for no namespace
         * @param name      the local name of the tag
         * @return this builder
         */
        public Builder javascriptTag(final String namespace,
                                     final String name) {
            return javascriptTag(new Markup(namespace, name));
        }

        /**
         * Registers a tag identified by its local name as a JavaScript tag.
         *
         * @param name the local name of the tag
         * @return this builder
         */
        public Builder javascriptTag(final String name) {
            return javascriptTag(TagEntity.newMarkup(name));
        }

        /**
         * Registers a tag identified by a {@link Markup} descriptor as a JavaScript tag
         * and marks its content as plain text in the underlying XML configuration.
         *
         * @param tagName the markup descriptor of the tag
         * @return this builder
         */
        public Builder javascriptTag(final Markup tagName) {
            htmlConfig.javascriptTags.add(v -> v.markup().equals(tagName));
            xmlConfigBuilder.tagAsPlainText(tagName);
            return this;
        }

        /**
         * Registers a custom predicate to identify JavaScript tags.
         * Matching tags will also have their content treated as plain text.
         *
         * @param filter a predicate that returns {@code true} for tags containing JavaScript
         * @return this builder
         */
        public Builder javascriptTag(final Function<TagEntity, Boolean> filter) {
            htmlConfig.javascriptTags.add(filter);
            xmlConfigBuilder.tagAsPlainText(filter);
            return this;
        }

        /**
         * Registers an attribute identified by namespace and local name as a JavaScript attribute.
         *
         * @param namespace the XML namespace of the attribute, or {@code null} for no namespace
         * @param name      the local name of the attribute
         * @return this builder
         */
        public Builder javascriptAttribute(final String namespace,
                                           final String name) {
            return javascriptAttribute(new Markup(namespace, name));
        }

        /**
         * Registers an attribute identified by its local name as a JavaScript attribute.
         *
         * @param name the local name of the attribute
         * @return this builder
         */
        public Builder javascriptAttribute(final String name) {
            return javascriptAttribute(TagEntity.newMarkup(name));
        }

        /**
         * Registers an attribute identified by a {@link Markup} descriptor as a JavaScript attribute.
         *
         * @param tagName the markup descriptor of the attribute
         * @return this builder
         */
        public Builder javascriptAttribute(final Markup tagName) {
            htmlConfig.javascriptAttributes.add(v -> v.markup().equals(tagName));
            return this;
        }

        /**
         * Registers a custom predicate to identify attributes whose value contains JavaScript.
         *
         * @param filter a predicate that returns {@code true} for JavaScript-bearing attributes
         * @return this builder
         */
        public Builder javascriptAttribute(final Function<Attribute, Boolean> filter) {
            htmlConfig.javascriptAttributes.add(filter);
            return this;
        }

        /**
         * Registers a tag identified by namespace and local name as a CSS tag.
         *
         * @param namespace the XML namespace of the tag, or {@code null} for no namespace
         * @param name      the local name of the tag
         * @return this builder
         */
        public Builder cssTag(final String namespace,
                              final String name) {
            return cssTag(new Markup(namespace, name));
        }

        /**
         * Registers a tag identified by its local name as a CSS tag.
         *
         * @param name the local name of the tag
         * @return this builder
         */
        public Builder cssTag(final String name) {
            return cssTag(TagEntity.newMarkup(name));
        }

        /**
         * Registers a tag identified by a {@link Markup} descriptor as a CSS tag
         * and marks its content as plain text in the underlying XML configuration.
         *
         * @param tagName the markup descriptor of the tag
         * @return this builder
         */
        public Builder cssTag(final Markup tagName) {
            htmlConfig.cssTags.add(v -> v.markup().equals(tagName));
            xmlConfigBuilder.tagAsPlainText(tagName);
            return this;
        }

        /**
         * Registers a custom predicate to identify CSS tags.
         * Matching tags will also have their content treated as plain text.
         *
         * @param filter a predicate that returns {@code true} for tags containing CSS
         * @return this builder
         */
        public Builder cssTag(final Function<TagEntity, Boolean> filter) {
            htmlConfig.cssTags.add(filter);
            xmlConfigBuilder.tagAsPlainText(filter);
            return this;
        }

        /**
         * Registers an attribute identified by namespace and local name as a CSS attribute.
         *
         * @param namespace the XML namespace of the attribute, or {@code null} for no namespace
         * @param name      the local name of the attribute
         * @return this builder
         */
        public Builder cssAttribute(final String namespace,
                                    final String name) {
            return cssAttribute(new Markup(namespace, name));
        }

        /**
         * Registers an attribute identified by its local name as a CSS attribute.
         *
         * @param name the local name of the attribute
         * @return this builder
         */
        public Builder cssAttribute(final String name) {
            return cssAttribute(TagEntity.newMarkup(name));
        }

        /**
         * Registers an attribute identified by a {@link Markup} descriptor as a CSS attribute.
         *
         * @param tagName the markup descriptor of the attribute
         * @return this builder
         */
        public Builder cssAttribute(final Markup tagName) {
            htmlConfig.cssAttributes.add(v -> v.markup().equals(tagName));
            return this;
        }

        /**
         * Registers a custom predicate to identify attributes whose value contains CSS.
         *
         * @param filter a predicate that returns {@code true} for CSS-bearing attributes
         * @return this builder
         */
        public Builder cssAttribute(final Function<Attribute, Boolean> filter) {
            htmlConfig.cssAttributes.add(filter);
            return this;
        }

        /**
         * Marks the tag identified by the given {@link Markup} descriptor so that its
         * content is parsed as plain text (raw characters) rather than as XML markup.
         *
         * @param tagName the markup descriptor of the tag to treat as plain text
         * @return this builder
         */
        public Builder tagAsPlainText(final Markup tagName) {
            xmlConfigBuilder.tagAsPlainText(tagName);
            return this;
        }

        /**
         * Marks the tag identified by namespace and local name so that its content is
         * parsed as plain text rather than as XML markup.
         *
         * @param namespace the XML namespace of the tag, or {@code null} for no namespace
         * @param name      the local name of the tag
         * @return this builder
         */
        public Builder tagAsPlainText(final String namespace,
                                      final String name) {
            xmlConfigBuilder.tagAsPlainText(namespace, name);
            return this;
        }

        /**
         * Marks the tag identified by its local name so that its content is parsed as
         * plain text rather than as XML markup.
         *
         * @param name the local name of the tag
         * @return this builder
         */
        public Builder tagAsPlainText(final String name) {
            xmlConfigBuilder.tagAsPlainText(name);
            return this;
        }

        /**
         * Marks tags matching the given predicate so that their content is parsed as
         * plain text rather than as XML markup.
         *
         * @param filter a predicate that returns {@code true} for tags to treat as plain text
         * @return this builder
         */
        public Builder tagAsPlainText(final Function<TagEntity, Boolean> filter) {
            xmlConfigBuilder.tagAsPlainText(filter);
            return this;
        }

        /**
         * Configures whether the parser should accept unclosed (void/self-closing) tags
         * without raising an error.
         *
         * @param acceptUnclosedTag {@code true} to tolerate unclosed tags, {@code false} to reject them
         * @return this builder
         */
        public Builder acceptUnclosedTag(final boolean acceptUnclosedTag) {
            xmlConfigBuilder.acceptUnclosedTag(acceptUnclosedTag);
            return this;
        }

        /**
         * Builds and returns the configured {@link HtmlConfig} instance.
         *
         * @return the fully built {@link HtmlConfig}
         */
        public HtmlConfig build() {
            htmlConfig.setXmlConfig(xmlConfigBuilder.build());
            return htmlConfig;
        }

        @Override
        public String toString() {
            return Builder.class.getSimpleName()
                     + "." + htmlConfig.toString();
        }

    }

    /*
     *
     * STATIC
     *
     */
    /**
     * Returns the default {@link HtmlConfig}, pre-configured to handle standard HTML:
     * {@code <script>} tags (with no {@code type} attribute, or with
     * {@code type="text/javascript"} / {@code type="module"}) are treated as JavaScript,
     * {@code <style>} tags and {@code style} attributes are treated as CSS, and
     * unclosed tags are accepted.
     *
     * @return default HTML configuration
     */
    public static HtmlConfig defaulConfig() {
        return  HtmlConfig.builder()
                          .javascriptTag(tag -> {
                              boolean result = false;
                              Markup markup = tag.markup();
                              if (markup.namespace() == null
                                      && "script".equals(markup.name())) {
                                  if (tag.attributes().isEmpty()) {
                                      result = true;
                                  }
                                  else {
                                      boolean typeFound = false;
                                      for (Attribute attribute : tag.attributes()) {
                                          Markup attributeName = attribute.markup();
                                          if ("type".equals(attributeName.name())) {
                                              typeFound = true;
                                              if ("text/javascript".equals(attribute.value())
                                                      || "module".equals(attribute.value())) {
                                                  result = true;
                                                  break;
                                              }
                                          }
                                      }
                                      if (!typeFound) {
                                          result = true;
                                      }
                                  }
                              }
                              return result;
                          })
                          .cssTag("style")
                          .cssAttribute("style")
                          .acceptUnclosedTag(true)
                          .tagAsPlainText("script")
                          .build();
    }

}
