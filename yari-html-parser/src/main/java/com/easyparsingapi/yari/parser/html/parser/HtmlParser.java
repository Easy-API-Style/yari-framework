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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstResult;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parser.css.ast.Css;
import com.easyparsingapi.yari.parser.css.lexer.CssLexer;
import com.easyparsingapi.yari.parser.css.parser.CssParser;
import com.easyparsingapi.yari.parser.html.ast.Html;
import com.easyparsingapi.yari.parser.html.ast.Script;
import com.easyparsingapi.yari.parser.html.ast.ScriptAttributeValue;
import com.easyparsingapi.yari.parser.html.ast.ScriptTag;
import com.easyparsingapi.yari.parser.html.ast.Style;
import com.easyparsingapi.yari.parser.html.ast.StyleAttributeValue;
import com.easyparsingapi.yari.parser.html.ast.StyleTag;
import com.easyparsingapi.yari.parser.javascript.ast.Javascript;
import com.easyparsingapi.yari.parser.javascript.lexer.JavascriptLexer;
import com.easyparsingapi.yari.parser.javascript.parser.JavascriptParser;
import com.easyparsingapi.yari.parser.xml.ast.Tag;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute;
import com.easyparsingapi.yari.parser.xml.ast.TagWithAttribute;
import com.easyparsingapi.yari.parser.xml.ast.Xml;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.easyparsingapi.yari.parser.xml.ast.XmlNodeContainer;
import com.easyparsingapi.yari.parser.xml.lexer.TagEntity;
import com.easyparsingapi.yari.parser.xml.lexer.XmlTag;
import com.easyparsingapi.yari.parser.xml.lexer.TagEntity.Attribute;
import com.easyparsingapi.yari.parser.xml.parser.XmlParser;

/**
 * HTML parser that produces an enriched {@link Html} AST by recursively parsing
 * JavaScript and CSS content embedded in HTML tags and attributes.
 */
public class HtmlParser {

    /** Not instantiable — all methods are static. */
    private HtmlParser() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlParser.class);

    /**
     * Parses a complete HTML unit and returns an {@link AstResult} containing the {@link Html} AST.
     * <p>
     * The parser first delegates XML analysis to {@link XmlParser}, then identifies tags and
     * attributes containing JavaScript or CSS according to the provided configuration. The content of
     * these tags and attributes is then parsed with the dedicated parsers ({@link JavascriptParser},
     * {@link CssParser}) and the AST nodes are replaced by their typed equivalents
     * ({@link ScriptTag}, {@link StyleTag}, {@link ScriptAttributeValue}, {@link StyleAttributeValue}).
     * </p>
     *
     * @param html       the HTML source to parse
     * @param htmlConfig the configuration describing which tags and attributes contain
     *                   JavaScript or CSS
     * @return an {@link AstResult} containing the {@link Html} AST and the consolidated list of all tokens
     */
    public static AstResult<Html> parseUnit(final String html,
                                            final HtmlConfig htmlConfig) {
        final AstResult<Xml> xml = XmlParser.parseUnit(html, htmlConfig.getXmlConfig());
        final Xml xmlUnit = xml.unit();
        final List<XmlNode> nodes = new ArrayList<>(xmlUnit.getNodes());
        final List<AstComment> allComments = new ArrayList<>(xmlUnit.astComments());
        final List<Token> allTokens = new ArrayList<>(xml.getTokens());

        final List<Tag> javascriptTags = new ArrayList<>();
        final List<Tag> cssTags = new ArrayList<>();

        final List<TagAttribute> javascriptAttributes = new ArrayList<>();
        final List<TagAttribute> cssAttributes = new ArrayList<>();

        xmlUnit.astStream().forEach(n -> {
            if (n instanceof Tag tag) {
                final TagEntity tagEntity = TagEntity.toTagEntity(tag);
                for (final Function<TagEntity, Boolean> filter : htmlConfig.getJavascriptTags()) {
                    if (filter.apply(tagEntity)) {
                        javascriptTags.add(tag);
                        break;
                    }
                }
                for (final Function<TagEntity, Boolean> filter : htmlConfig.getCssTags()) {
                    if (filter.apply(tagEntity)) {
                        cssTags.add(tag);
                        break;
                    }
                }
            }
            else if (n instanceof TagAttribute tagAttribute) {
                final Attribute attribute = TagEntity.toAttribute(tagAttribute);
                for (final Function<Attribute, Boolean> filter : htmlConfig.getJavascriptAttributes()) {
                    if (filter.apply(attribute)) {
                        javascriptAttributes.add(tagAttribute);
                        break;
                    }
                }
                for (final Function<Attribute, Boolean> filter : htmlConfig.getCssAttributes()) {
                    if (filter.apply(attribute)) {
                        cssAttributes.add(tagAttribute);
                        break;
                    }
                }
            }
        });
        if (!javascriptTags.isEmpty()) {
            LOGGER.debug("parsing html javascriptTags={}...", javascriptTags.size());
        }
        for (final Tag tag : javascriptTags) {
            final List<Token> tokens = xml.getTokensOf(tag);
            final Token textToken = firstToken(tokens, XmlTag.TEXT);
            if (textToken != null) {
                final List<Token> javascriptTokens = JavascriptLexer.lex(textToken);
                if (!javascriptTokens.isEmpty()) {
                    final AstResult<Javascript> javascriptAst = JavascriptParser.parseUnit(javascriptTokens);
                    final Javascript javascript = javascriptAst.unit();
                    allComments.addAll(javascript.astComments());
                    final Script script = new Script(javascript.getNodes(),
                                                     javascript.astComments(),
                                                     javascript.getSourceLocation());
                    final XmlNodeContainer parent = (XmlNodeContainer) tag.astParent();
                    final ScriptTag scriptTag = new ScriptTag(tag.getHead(),
                                                              script,
                                                              tag.getFoot(),
                                                              tag.getSourceLocation(),
                                                              parent);
                    final int tagIndex = parent.getNodes().indexOf(tag);
                    parent.getNodes().set(tagIndex, scriptTag);
                    final int tokenIndex = allTokens.indexOf(textToken);
                    allTokens.remove(tokenIndex);
                    allTokens.addAll(tokenIndex, javascriptTokens);
                }
            }
        }
        if (!javascriptAttributes.isEmpty()) {
            LOGGER.debug("parsing html javascriptAttributes={}...", javascriptAttributes.size());
        }
        for (final TagAttribute tagAttribute : javascriptAttributes) {
            final List<Token> tokens = xml.getTokensOf(tagAttribute.getValue());
            final Token textToken = firstToken(tokens, XmlTag.ATTRIBUE_VALUE);
            if (textToken != null) {
                final List<Token> javascriptTokens = JavascriptLexer.lex(unpadToken(textToken));
                if (!javascriptTokens.isEmpty()) {
                    final AstResult<Javascript> javascriptAst = JavascriptParser.parseExpression(javascriptTokens);
                    final Javascript javascript = javascriptAst.unit();
                    allComments.addAll(javascript.astComments());
                    final TagWithAttribute parent = (TagWithAttribute) tagAttribute.astParent();
                    final TagAttribute.Value value = tagAttribute.getValue();
                    final ScriptAttributeValue scriptAttributeValue =
                        new ScriptAttributeValue(value.getType(),
                                                 value.getValue(),
                                                 javascript.getNodes(),
                                                 javascript.astComments(),
                                                 javascript.getSourceLocation());
                    final TagAttribute newTagAttribute = new TagAttribute(tagAttribute.getName(),
                                                                          scriptAttributeValue,
                                                                          tagAttribute.getSourceLocation(),
                                                                          parent);
                    final int tagIndex = parent.getAttributes().indexOf(tagAttribute);
                    parent.getAttributes().set(tagIndex, newTagAttribute);
                    final int tokenIndex = allTokens.indexOf(textToken);
                    allTokens.remove(tokenIndex);
                    allTokens.addAll(tokenIndex, javascriptTokens);
                }
            }
        }
        if (!cssTags.isEmpty()) {
            LOGGER.debug("parsing html cssTags={}...", cssTags.size());
        }
        for (final Tag tag : cssTags) {
            final List<Token> tokens = xml.getTokensOf(tag);
            final Token textToken = firstToken(tokens, XmlTag.TEXT);
            if (textToken != null) {
                final List<Token> cssTokens = CssLexer.lex(textToken);
                if (!cssTokens.isEmpty()) {
                    final AstResult<Css> cssAst = CssParser.parseUnit(cssTokens);
                    final Css css = cssAst.unit();
                    allComments.addAll(css.astComments());
                    final Style style = new Style(css.getNodes(),
                                                  css.astComments(),
                                                  css.getSourceLocation());
                    final XmlNodeContainer parent = (XmlNodeContainer) tag.astParent();
                    final StyleTag styleTag = new StyleTag(tag.getHead(),
                                                           style,
                                                           tag.getFoot(),
                                                           tag.getSourceLocation(),
                                                           parent);
                    final int tagIndex = parent.getNodes().indexOf(tag);
                    parent.getNodes().set(tagIndex, styleTag);
                    final int tokenIndex = allTokens.indexOf(textToken);
                    allTokens.remove(tokenIndex);
                    allTokens.addAll(tokenIndex, cssTokens);
                }
            }
        }
        if (!cssAttributes.isEmpty()) {
            LOGGER.debug("parsing html cssAttributes={}...", cssAttributes.size());
        }
        for (final TagAttribute tagAttribute : cssAttributes) {
            final List<Token> tokens = xml.getTokensOf(tagAttribute.getValue());
            final Token textToken = firstToken(tokens, XmlTag.ATTRIBUE_VALUE);
            if (textToken != null) {
                final List<Token> cssTokens = CssLexer.lex(unpadToken(textToken));
                if (!cssTokens.isEmpty()) {
                    final AstResult<Css> cssAst = CssParser.parseProperties(cssTokens);
                    final Css css = cssAst.unit();
                    allComments.addAll(css.astComments());
                    final TagWithAttribute parent = (TagWithAttribute) tagAttribute.astParent();
                    final TagAttribute.Value value = tagAttribute.getValue();
                    final StyleAttributeValue styleAttributeValue =
                        new StyleAttributeValue(value.getType(),
                                                value.getValue(),
                                                css.getNodes(),
                                                css.astComments(),
                                                css.getSourceLocation());
                    final TagAttribute newTagAttribute = new TagAttribute(tagAttribute.getName(),
                                                                          styleAttributeValue,
                                                                          tagAttribute.getSourceLocation(),
                                                                          parent);
                    final int tagIndex = parent.getAttributes().indexOf(tagAttribute);
                    parent.getAttributes().set(tagIndex, newTagAttribute);
                    final int tokenIndex = allTokens.indexOf(textToken);
                    allTokens.remove(tokenIndex);
                    allTokens.addAll(tokenIndex, cssTokens);
                }
            }
        }
        final Html htmlUnit = new Html(nodes, allComments, xmlUnit.getSourceLocation());
        return new AstResult<>(htmlUnit, allTokens);
    }

    private static Token firstToken(final List<Token> tokens,
                                    final XmlTag xmlTag) {
        return tokens.stream()
                     .filter(t -> xmlTag.equals(Token.tag(t)))
                     .findFirst()
                     .orElse(null);
    }

    private static Token unpadToken(final Token token) {
        final Fragment fragment = (Fragment) token.value();
        final Fragment value = Tokens.fragment(fragment.text().substring(1, fragment.text().length() - 1),
                                               fragment.tag());
        return new Token(token.index(),
                         token.length(),
                         value,
                         token.sourceLocator());
    }

}
