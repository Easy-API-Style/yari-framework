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
package com.easyparsingapi.yari.parser.xml.parser;

import java.util.Set;
import java.util.function.Function;

import com.easyparsingapi.yari.parser.xml.lexer.TagEntity;
import com.easyparsingapi.yari.parser.xml.lexer.XmlLexerConfig;
import com.easyparsingapi.yari.parser.xml.lexer.TagEntity.Markup;

/**
 * Configuration for the XML parser, including lexer settings and parser-level options.
 */
public class XmlConfig {

    /** The lexer configuration used to tokenize the XML input. */
    public XmlLexerConfig xmlLexerConfig;
    /** Whether the parser should accept empty tags that are not explicitly self-closed with {@code />}. */
    public boolean acceptUnclosedTag;

    /**
     * Creates an XmlConfig with default settings.
     */
    public XmlConfig() {
        super();
    }
    
    /**
     * Returns whether unclosed tags are accepted during parsing.
     *
     * @return {@code true} if the parser accepts tags that are not explicitly self-closed
     */
    public boolean acceptUnclosedTag() {
        return acceptUnclosedTag;
    }

    /**
     * Returns the set of filters that determine which tags are parsed as plain text.
     *
     * @return the set of plain-text tag filter functions
     */
    public Set<Function<TagEntity, Boolean>> tagAsPlainTextFilters() {
        return xmlLexerConfig.tagAsPlainTextFilters();
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(XmlConfig.class.getSimpleName());
        result.append(" [acceptUnclosedTag=");
        result.append(acceptUnclosedTag);
        result.append(", tagAsPlainTextFilters=");
        result.append(xmlLexerConfig.tagAsPlainTextFilters.size());
        result.append("]");
        return result.toString();
    }

    /*
     *
     * BUILDER
     *
     */
    /**
     * Returns a new Builder for XmlConfig.
     *
     * @return a new {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder for constructing {@link XmlConfig} instances with a fluent API.
     */
    public static class Builder {

        private final XmlLexerConfig.Builder XmlLexerConfigBuilder = XmlLexerConfig.builder();
        private final XmlConfig xmlConfig = new XmlConfig();

        private Builder() {
            
        }
        
        /**
         * Sets whether unclosed tags are accepted.
         *
         * @param acceptUnclosedTag {@code true} to accept tags not explicitly self-closed
         * @return this builder
         */
        public Builder acceptUnclosedTag(final boolean acceptUnclosedTag) {
            xmlConfig.acceptUnclosedTag = acceptUnclosedTag;
            return this;
        }
        
        /**
         * Registers the given tag markup to be parsed as plain text.
         *
         * @param tagName the markup identifying the tag to treat as plain text
         * @return this builder
         */
        public Builder tagAsPlainText(final Markup tagName) {
            XmlLexerConfigBuilder.tagAsPlainText(tagName);
            return this;
        }
        
        /**
         * Registers the tag with the given namespace and name to be parsed as plain text.
         *
         * @param namespace the namespace prefix of the tag
         * @param name      the local name of the tag
         * @return this builder
         */
        public Builder tagAsPlainText(final String namespace,
                                      final String name) {
            XmlLexerConfigBuilder.tagAsPlainText(namespace, name);
            return this;
        }
        
        /**
         * Registers the tag with the given name to be parsed as plain text.
         *
         * @param name the local name of the tag
         * @return this builder
         */
        public Builder tagAsPlainText(final String name) {
            XmlLexerConfigBuilder.tagAsPlainText(name);
            return this;
        }
        
        /**
         * Registers a custom filter that determines whether a tag should be parsed as plain text.
         *
         * @param filter a function that returns {@code true} for tags that should be treated as plain text
         * @return this builder
         */
        public Builder tagAsPlainText(final Function<TagEntity, Boolean> filter) {
            XmlLexerConfigBuilder.tagAsPlainText(filter);
            return this;
        }
        
        /**
         * Builds and returns the configured XmlConfig.
         *
         * @return the configured {@link XmlConfig} instance
         */
        public XmlConfig build() {
            xmlConfig.xmlLexerConfig = XmlLexerConfigBuilder.build();
            return xmlConfig;
        }

        @Override
        public String toString() {
            return Builder.class.getSimpleName() 
                     + "." + xmlConfig.toString();
        }
        
    }
    
}
