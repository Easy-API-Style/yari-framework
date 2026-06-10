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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parser.xml.ast.XmlComment;
import com.easyparsingapi.yari.parser.xml.lexer.XmlLexer;
import com.easyparsingapi.yari.parser.xml.lexer.XmlTag;

class XmlContext {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlContext.class);
    
    private static Set<XmlTag> unusedHtmlTags = 
        Set.of(XmlTag.RETURN_CARRIAGE);
    
    private final XmlConfig xmlConfig;
    
    private final List<Token> tokens;
    private final List<Token> cleanedTokens = new ArrayList<>();
    private List<AstComment> comments = new ArrayList<>();
    
    XmlContext(final String html, 
               final XmlConfig xmlConfig) {
        this(XmlLexer.lex(xmlConfig.xmlLexerConfig, html), xmlConfig);
    }
    
    XmlContext(final List<Token> tokens, 
               final XmlConfig xmlConfig) {
        super();
        this.tokens = tokens; 
        this.xmlConfig = xmlConfig; 
        for (final Token token : tokens) {
            final XmlTag htmlTag = XmlTag.tag(token);
            if (!unusedHtmlTags.contains(htmlTag)) {
                cleanedTokens.add(token);
            }
            else if (XmlTag.COMMENT == htmlTag) {
                final XmlComment xmlComment = new XmlComment(token.toString());
                xmlComment.setSourceLocation(token.sourceLocation());
                comments.add(xmlComment);
                cleanedTokens.add(token);
            }
        }
    }
    
    List<Token> getTokens() {
        return tokens;
    }
    
    List<Token> getCleanedTokens() {
        return cleanedTokens;
    }

    List<AstComment> getComments() {
        return comments;
    }
    
    boolean acceptUnclosedTag() {
        return xmlConfig.acceptUnclosedTag;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(XmlContext.class.getSimpleName());
        result.append(" [tokens=");
        result.append(cleanedTokens.size());
        result.append("]");
        return result.toString();
    }
    
}
