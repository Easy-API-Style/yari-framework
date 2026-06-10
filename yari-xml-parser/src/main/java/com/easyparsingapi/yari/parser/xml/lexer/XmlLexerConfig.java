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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.easyparsingapi.yari.parser.xml.lexer.TagEntity.Markup;

/**
 * Configuration for the XML lexer, including plain-text tag filters.
 */
public class XmlLexerConfig {

    /** Filters that determine which tags are lexed as plain text. */
    public final Set<Function<TagEntity, Boolean>> tagAsPlainTextFilters = new HashSet<>();

    /** Creates an XmlLexerConfig with default settings. */
    public XmlLexerConfig() {
        super();
    }
    
    /**
     * Returns an unmodifiable view of the plain-text tag filters.
     *
     * @return an unmodifiable set of plain-text tag filter functions
     */
    public Set<Function<TagEntity, Boolean>> tagAsPlainTextFilters() {
        return Collections.unmodifiableSet(tagAsPlainTextFilters);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(XmlLexerConfig.class.getSimpleName());
        result.append(" [tagAsPlainTextFilters=");
        result.append(tagAsPlainTextFilters.size());
        result.append("]");
        return result.toString();
    }

    /*
     * 
     * BUILDER
     * 
     */
    /**
     * Returns a new Builder for XmlLexerConfig.
     *
     * @return a new {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for constructing {@link XmlLexerConfig} instances with a fluent API.
     */
    public static class Builder {
        
        private final XmlLexerConfig config = new XmlLexerConfig();
        
        private Builder() {
            
        }
        
        /**
         * Registers the tag with the given namespace and name to be parsed as plain text.
         *
         * @param namespace the namespace prefix of the tag
         * @param tagName   the local name of the tag
         * @return this builder
         */
        public Builder tagAsPlainText(final String namespace,
                                      final String tagName) {
            return tagAsPlainText(new Markup(namespace, tagName));
        }
        
        /**
         * Registers the tag with the given name to be parsed as plain text.
         *
         * @param tagName the local name of the tag
         * @return this builder
         */
        public Builder tagAsPlainText(final String tagName) {
            return tagAsPlainText(TagEntity.newMarkup(tagName));
        }
        
        /**
         * Registers the given tag markup to be parsed as plain text.
         *
         * @param markup the markup identifying the tag to treat as plain text
         * @return this builder
         */
        public Builder tagAsPlainText(final Markup markup) {
            if (markup != null) {
                config.tagAsPlainTextFilters.add(tagEntity -> {
                    return tagEntity.markup().equals(markup);
                });
            }
            return this;
        }
        
        /**
         * Registers a custom filter that determines whether a tag should be parsed as plain text.
         *
         * @param filter a function that returns {@code true} for tags that should be treated as plain text
         * @return this builder
         */
        public Builder tagAsPlainText(final Function<TagEntity, Boolean> filter) {
            if (filter != null) {
                config.tagAsPlainTextFilters.add(filter);
            }
            return this;
        }
        
        /**
         * Builds and returns the configured XmlLexerConfig.
         *
         * @return the configured {@link XmlLexerConfig} instance
         */
        public XmlLexerConfig build() {
            return config;
        }

        @Override
        public String toString() {
            return Builder.class.getSimpleName() 
                     + "." + config.toString();
        }
        
    }
    
}
