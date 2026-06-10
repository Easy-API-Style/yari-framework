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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.core.ast.AstResult;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.xml.ast.CData;
import com.easyparsingapi.yari.parser.xml.ast.DocType;
import com.easyparsingapi.yari.parser.xml.ast.Markup;
import com.easyparsingapi.yari.parser.xml.ast.Prolog;
import com.easyparsingapi.yari.parser.xml.ast.SingleTagFoot;
import com.easyparsingapi.yari.parser.xml.ast.SingleTagFootError;
import com.easyparsingapi.yari.parser.xml.ast.Tag;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute;
import com.easyparsingapi.yari.parser.xml.ast.TagAttributeError;
import com.easyparsingapi.yari.parser.xml.ast.TagBody;
import com.easyparsingapi.yari.parser.xml.ast.TagEmpty;
import com.easyparsingapi.yari.parser.xml.ast.TagFoot;
import com.easyparsingapi.yari.parser.xml.ast.TagHead;
import com.easyparsingapi.yari.parser.xml.ast.TagHeadError;
import com.easyparsingapi.yari.parser.xml.ast.TagName;
import com.easyparsingapi.yari.parser.xml.ast.Text;
import com.easyparsingapi.yari.parser.xml.ast.TokenError;
import com.easyparsingapi.yari.parser.xml.ast.UnclosedTagEmptyError;
import com.easyparsingapi.yari.parser.xml.ast.Xml;
import com.easyparsingapi.yari.parser.xml.ast.XmlComment;
import com.easyparsingapi.yari.parser.xml.ast.XmlNode;
import com.easyparsingapi.yari.parser.xml.ast.TagAttribute.Value.Type;
import com.easyparsingapi.yari.parser.xml.lexer.XmlTag;

/**
 * Main entry point of the XML parser.
 */
public class XmlParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlParser.class);
    
    private static record TagHeadToken(Token token, int index) {}
    
    private final XmlContext xmlContext;
    private final List<Token> xmlTokens;
   
    private final LinkedList<Token> attributes = new LinkedList<>();
    private final LinkedList<XmlNode> children = new LinkedList<>();
    
    private final Map<TagHead, List<Token>> tagHeads = new HashMap<>();
    private TagHeadToken tagHeadToken;

    private XmlParser(final XmlContext xmlContext) {
        super();
        this.xmlContext = xmlContext;
        this.xmlTokens = xmlContext.getCleanedTokens();
    }

    private void checkIfTagHeadIsOpened(final Token currentXmlToken) {
        if (tagHeadToken != null) {
            final List<TagAttribute> tagAttributes = toTagAttribute(attributes);
            if (!tagAttributes.isEmpty()) {
                final TagHeadError tagHeadError = TagHeadError.newInstance("tag head not ending error",
                                                                           xmlTokens.subList(xmlTokens.indexOf(tagHeadToken.token()), 
                                                                                             xmlTokens.indexOf(currentXmlToken)), 
                                                                           tagAttributes);
                children.set(tagHeadToken.index(), tagHeadError);
            }
            tagHeadToken = null;
        }
    }
    
    private Xml parse() {
        for (final Token currentXmlToken : xmlTokens) {
            final XmlTag xmlTag = XmlTag.tag(currentXmlToken);
            /*
             * ERROR
             */
            if (XmlTag.ERROR == xmlTag) {
                checkIfTagHeadIsOpened(currentXmlToken);
                children.add(TokenError.newInstance("token error", currentXmlToken));
            }
            /*
             * NODE
             */
            else if (XmlTag.CDATA == xmlTag) {
                checkIfTagHeadIsOpened(currentXmlToken);
                children.add(new CData(currentXmlToken.toString(),
                                       currentXmlToken.sourceLocation()));
            }
            else if (XmlTag.DOCTYPE == xmlTag) {
                checkIfTagHeadIsOpened(currentXmlToken);
                children.add(new DocType(currentXmlToken.toString(),
                                         currentXmlToken.sourceLocation()));
            }
            else if (XmlTag. PROLOG == xmlTag) {
                checkIfTagHeadIsOpened(currentXmlToken);
                children.add(new Prolog(currentXmlToken.toString(), 
                                        currentXmlToken.sourceLocation()));
            }
            else if (XmlTag.COMMENT == xmlTag) {
                checkIfTagHeadIsOpened(currentXmlToken);
                children.add(new XmlComment(currentXmlToken.toString(),
                                            currentXmlToken.sourceLocation()));
            }
            else if (XmlTag.TEXT == xmlTag) {
                checkIfTagHeadIsOpened(currentXmlToken);
                children.add(new Text(currentXmlToken.toString(),
                                      currentXmlToken.sourceLocation()));
            }
            /*
             * HEAD
             */
            else if (XmlTag.BEGIN_TAG == xmlTag) {
                checkIfTagHeadIsOpened(currentXmlToken);
                tagHeadToken = new TagHeadToken(currentXmlToken, children.size());
                children.add(TagHeadError.newInstance("tag head not ending error", currentXmlToken));
            }
            else if (XmlTag.ATTRIBUE_NAME == xmlTag) {
                if (tagHeadToken != null) {
                    attributes.add(currentXmlToken);
                }
                else {
                    children.add(TokenError.newInstance("attribute name error", currentXmlToken));
                }
            }
            else if (XmlTag.EQUAL == xmlTag) {
                if (tagHeadToken != null) {
                    attributes.add(currentXmlToken);
                }
                else {
                    children.add(TokenError.newInstance("attribute equal error", currentXmlToken));
                }
            }
            else if (XmlTag.ATTRIBUE_VALUE == xmlTag) {
                if (tagHeadToken != null) {
                    attributes.add(currentXmlToken);
                }
                else {
                    children.add(TokenError.newInstance("attribute value error", currentXmlToken));
                }
            }
            else if (XmlTag.END_TAG == xmlTag) {
                if (tagHeadToken != null) {
                    final List<TagAttribute> tagAttributes = toTagAttribute(attributes);
                    final TagName tagName = Markup.toTagName(tagHeadToken.token());
                    final TagHead tagHead = new TagHead(tagName, 
                                                        tagAttributes, 
                                                        new SourceLocation(tagHeadToken.token().sourceLocation().start(),
                                                                           currentXmlToken.sourceLocation().end()));
                    children.set(tagHeadToken.index(), tagHead);
                    final int firstTag = xmlTokens.indexOf(tagHeadToken.token());
                    final int lastTag = xmlTokens.indexOf(currentXmlToken);
                    tagHeads.put(tagHead, xmlTokens.subList(firstTag, lastTag));
                    tagHeadToken = null;
                }
                else {
                    children.add(TokenError.newInstance("tag head ending error", currentXmlToken));
                }
            }
            /*
             * SIMPLE TAG
             */
            else if (XmlTag.CLOSED_END_TAG == xmlTag) {
                if (tagHeadToken != null) {
                    final List<TagAttribute> tagAttributes = toTagAttribute(attributes);
                    final TagName tagName = Markup.toTagName(tagHeadToken.token());
                    final TagEmpty tagEmpty = new TagEmpty(true, 
                                                           tagName, 
                                                           tagAttributes, 
                                                           new SourceLocation(tagHeadToken.token().sourceLocation().start(),
                                                                              currentXmlToken.sourceLocation().end()));
                    children.set(tagHeadToken.index(), tagEmpty);
                    tagHeadToken = null;
                }
                else {
                    children.add(TokenError.newInstance("tag head closing error", currentXmlToken));
                }
            }
            /*
             * COMPLEX TAG
             */
            else if (XmlTag.CLOSED_TAG == xmlTag) {
                checkIfTagHeadIsOpened(currentXmlToken);
                // tag foot
                final TagName tagFootName = Markup.toTagName(currentXmlToken);
                final TagFoot tagFootFound = new TagFoot(tagFootName, currentXmlToken.sourceLocation());
                // look for tag head
                final List<XmlNode> currentTagChildren = new ArrayList<>();
                final ListIterator<XmlNode> childrenIterator = children.listIterator(children.size());
                TagHead tagHeadFound = null;
                while (childrenIterator.hasPrevious()) {
                    final XmlNode previousXmlNode = childrenIterator.previous();
                    if (previousXmlNode instanceof TagHead tagHead) {
                        if (tagFootName.getValue().equals(tagHead.getName().getValue())) {
                            tagHeadFound = tagHead;
                            break;
                        }
                    }
                    currentTagChildren.add(previousXmlNode);
                }
                // tag head found
                if (tagHeadFound != null) {
                    Collections.reverse(currentTagChildren);
                    children.removeAll(currentTagChildren);
                    children.remove(tagHeadFound);
                    final List<XmlNode> tagNodeChildren = new ArrayList<>();
                    for (final XmlNode tagChild : currentTagChildren) {
                        if (tagChild instanceof TagHeadError tagHeadError) {
                            tagNodeChildren.add(tagHeadError);
                        }
                        else if (tagChild instanceof TagHead tagHead) {
                            if (xmlContext.acceptUnclosedTag()) { 
                                final TagEmpty unclosedTag = new TagEmpty(false, 
                                                                          tagHead.getName(), 
                                                                          tagHead.getAttributes(),
                                                                          tagHead.getSourceLocation());
                                tagNodeChildren.add(unclosedTag);
                            }
                            else {
                                tagNodeChildren.add(UnclosedTagEmptyError.newInstance("unclosed tag not allowed",
                                                                                      tagHeads.get(tagHead),
                                                                                      tagHead.getName(), 
                                                                                      tagHead.getAttributes(),
                                                                                      tagHead.getSourceLocation()));
                            }
                        }
                        else {
                            tagNodeChildren.add(tagChild);
                        }
                    }
                    final TagBody tagBody = tagNodeChildren.isEmpty() 
                                               ? null 
                                               : new TagBody(tagNodeChildren,
                                                             new SourceLocation(tagNodeChildren.getFirst().getSourceLocation().start(),
                                                                                tagNodeChildren.getLast().getSourceLocation().end()));
                    final Tag tag = new Tag(tagHeadFound, 
                                            tagBody, 
                                            tagFootFound, 
                                            new SourceLocation(tagHeadFound.getSourceLocation().start(), 
                                                               tagFootFound.getSourceLocation().end()));
                    children.add(tag);
                }
                // no TagHeader found
                else {
                    if (xmlContext.acceptUnclosedTag()) {
                        children.add(new SingleTagFoot(tagFootFound));
                    }
                    else {
                        children.add(SingleTagFootError.newInstance("tag foot error", 
                                                                    currentXmlToken,
                                                                    tagFootFound.getName(),
                                                                    tagFootFound.getSourceLocation()));
                    }
                }
            }
        }
        // unit xml
        final List<XmlNode> unitChildren = new ArrayList<>();
        for (final XmlNode child : children) {
            if (child instanceof TagHeadError tagHeadError) {
                unitChildren.add(tagHeadError);
            }
            else if (child instanceof TagHead tagHead) {
                if (xmlContext.acceptUnclosedTag()) { 
                    final TagEmpty tagEmpty = new TagEmpty(false, 
                                                           tagHead.getName(), 
                                                           tagHead.getAttributes(),
                                                           tagHead.getSourceLocation());
                    unitChildren.add(tagEmpty);
                }
                else {
                    unitChildren.add(tagHead);
                }
            }
            else {
                unitChildren.add(child);
            }
        }
        return new Xml(unitChildren,
                       xmlContext.getComments(),
                       new SourceLocation(xmlTokens.getFirst().sourceLocation().start(), 
                                          xmlTokens.getLast().sourceLocation().end()));
    }

    private static List<TagAttribute> toTagAttribute(final LinkedList<Token> attributes) {
        final List<TagAttribute> result = new ArrayList<>();
        
        final ListIterator<Token> iterator = attributes.listIterator();
        while (iterator.hasNext()) {
            final Map<XmlTag, Token> map = new HashMap<>();
            Token token = iterator.next();
            if (XmlTag.ATTRIBUE_NAME.equals(Token.tag(token))) {
                // name OK
                map.put(XmlTag.ATTRIBUE_NAME, token);
                if (iterator.hasNext()) {
                    token = iterator.next();
                    if (XmlTag.EQUAL.equals(Token.tag(token))) {
                        // equal OK
                        map.put(XmlTag.EQUAL, token);
                        if (iterator.hasNext()) {
                            token = iterator.next();
                            if (XmlTag.ATTRIBUE_VALUE.equals(Token.tag(token))) {
                                // value OK
                                map.put(XmlTag.ATTRIBUE_VALUE, token);
                            }
                            else {
                                iterator.previous();
                            }
                        }
                    }
                    else {
                        iterator.previous();
                    }
                }
                final Token attributeName = map.get(XmlTag.ATTRIBUE_NAME);
                final Token attributeValue = map.get(XmlTag.ATTRIBUE_VALUE);

                final TagAttribute.Name tagAttributeName = Markup.toTagAttributeName(attributeName);
                TagAttribute.Value tagAttributeValue = null;
                if (attributeValue != null) {
                    final String value = attributeValue.toString();
                    final Type type = value.startsWith("'") ? Type.singleQuote : Type.doubleQuote;
                    tagAttributeValue = new TagAttribute.Value(type,
                                                               value.substring(1, value.length() - 1), 
                                                               attributeValue.sourceLocation());
                }
                final TagAttribute tagAttribute = new TagAttribute(tagAttributeName, tagAttributeValue);
                if (tagAttributeValue != null) {
                    tagAttribute.setSourceLocation(new SourceLocation(tagAttributeName.getSourceLocation().start(), 
                                                                      tagAttributeValue.getSourceLocation().end()));
                }
                else {
                    tagAttribute.setSourceLocation(tagAttributeName.getSourceLocation());
                }
                result.add(tagAttribute);
            }
            if (map.isEmpty()) {
                final XmlTag xmlTag = (XmlTag) Token.tag(token);
                if (XmlTag.EQUAL.equals(xmlTag)) {
                    result.add(TagAttributeError.newInstance("tag attribute equal error", token));
                }
                else {
                    final String value = token.toString();
                    final Type type = value.startsWith("'") ? Type.singleQuote : Type.doubleQuote;
                    TagAttribute.Value tagAttributeValue = new TagAttribute.Value(type,
                                                                                  value.substring(1, value.length() - 1), 
                                                                                  token.sourceLocation());
                    result.add(TagAttributeError.newInstance("tag attribute value error", token, tagAttributeValue));
                }
            }
        }
        attributes.clear();
        return result;
    }
    
    /*
     * 
     * STATIC
     * 
     */
    /**
     * Parses a list of tokens into an XML AST.
     *
     * @param tokens    the list of lexer tokens representing the XML input
     * @param xmlConfig the parser configuration to apply
     * @return the parse result containing the XML AST and the original token list
     */
    public static AstResult<Xml> parseUnit(final List<Token> tokens,
                                           final XmlConfig xmlConfig) {
        LOGGER.debug("[START][token] parsing xml...");
        final XmlContext xmlContext = new XmlContext(tokens, xmlConfig);
        final Xml xmlAst = parseUnit(xmlContext);
        return new AstResult<>(xmlAst, xmlContext.getTokens());
    }
    
    /**
     * Parses an XML string into an AST.
     *
     * @param xml       the raw XML source text to parse
     * @param xmlConfig the parser configuration to apply
     * @return the parse result containing the XML AST and the token list
     */
    public static AstResult<Xml> parseUnit(final String xml,
                                           final XmlConfig xmlConfig) {
        LOGGER.debug("parsing xml...");
        final XmlContext xmlContext = new XmlContext(xml, xmlConfig);
        final Xml xmlAst = parseUnit(xmlContext);
        return new AstResult<>(xmlAst, xmlContext.getTokens());
    }
    
    private static Xml parseUnit(final XmlContext xmlContext) {
        final XmlParser xmlParser = new XmlParser(xmlContext);
        return xmlParser.parse();
    }

}
