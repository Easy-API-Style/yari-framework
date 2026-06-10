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

import static com.easyparsingapi.yari.parsec.pattern.Pattern.MISMATCH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.pattern.CharPredicate;
import com.easyparsingapi.yari.parsec.pattern.Pattern;
import com.easyparsingapi.yari.parsec.pattern.Patterns;
import com.easyparsingapi.yari.parser.xml.lexer.TagEntity.Markup;

/**
 * Lexer for XML source text, producing a list of typed tokens.
 */
public class XmlLexer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlLexer.class);
    
    private final List<Fragment> fragments = new ArrayList<>();
    private final AtomicReference<String> insideTag = new AtomicReference<>();
    private final AtomicReference<String> tagName = new AtomicReference<>();
    
    private final Set<Function<TagEntity, Boolean>> tagAsPlainTextFilters;
    
    private XmlLexer(final Set<Function<TagEntity, Boolean>> tagAsPlainTextFilters) {
        super();
        this.tagAsPlainTextFilters = tagAsPlainTextFilters;
    }
    
    /*
     * 
     * WHITE
     * 
     */
    private static final CharPredicate IS_WHITE_SPACE = 
        c -> c != '\r' 
              && c != '\n' 
              && Character.isWhitespace(c);
        
    private static final Parser<String> RETURN_CARRIAGE = 
        Patterns.or(Patterns.sequence(Patterns.isChar('\r'), Patterns.isChar('\n')), 
                    Patterns.isChar('\n'))
                .toScanner(XmlTag.RETURN_CARRIAGE.name()) 
                .source(); 
    
    private static final Parser<String> WHITE_SPACE = 
        Patterns.many(IS_WHITE_SPACE)
                .toScanner("whiteSpace")
                .source();   
        
    /*
     * 
     * WORD
     * 
     */    
    private static final Set<Character> OPERATORS =
        Set.of('"', '\'', 
               '#', '$', '@', '~',
               '|','&', 
               '?', '!',
               '^', '`', 
               '\\', '/', 
               '*', '+', '=', '%',
               '(', ')',
               ',', ';',
               '<', '>',
               '[', ']',
               '{', '}');
    
    private static final CharPredicate IS_XML_CHARACTER = 
        c -> !OPERATORS.contains(c)
                && !Character.isWhitespace(c);
          
    private static final Pattern XML_TAG = 
        Patterns.isChar(IS_XML_CHARACTER)
                .many1();

    private static boolean checkElementName(final char c) {
        return !Character.isDigit(c) && c != '-';
    }
    
    private static final Pattern TEXT_PATTERN = Pattern.rule(context -> {
        final CharSequence src = context.src();
        final int begin = context.begin();
        final int end = context.end();
        
        if (begin >= end) {
            return MISMATCH;
        }
        int length = 0;
        for (int i = begin; i < src.length(); i++) {
            length++;
            final char c = src.charAt(i);
            if (c != '<' && c != '>') {
                continue;
            }
            else {
                length--;
                for (int ii = (begin + length - 1); ii >= 0; ii--) {
                    final char cc = src.charAt(ii);
                    if (Character.isWhitespace(cc)) {
                        length--;
                    }
                    else {
                        break;
                    }
                }
//                System.out.println(src.subSequence(begin, begin + length) + "|");
                return length;
            }
        }
        // if EOF
        return length;
    });
    
    private static final Parser<String> TEXT_PARSER = 
        TEXT_PATTERN.toScanner(XmlTag.TEXT.name())
                    .source();
    
    /*
     * 
     * ERROR
     * 
     */
    private static final Parser<String> ERROR_PARSER = 
      Patterns.sequence(Patterns.or(Patterns.string("</"),
                                    Patterns.string("<")), 
                        Patterns.or(Patterns.isChar(IS_XML_CHARACTER).not().peek(),
                                    Patterns.isChar(c -> Character.isDigit(c)).peek(),
                                    Patterns.isChar(c -> c == '-').peek()))
              .toScanner(XmlTag.ERROR.name())
              .source();
    
    /*
     * 
     * PROLOG
     * 
     */
    private static final Parser<String> COMMENT = 
        Patterns.sequence(Patterns.string("<!--").until("-->"), 
                          Patterns.string("-->"))
                .toScanner(XmlTag.COMMENT.name())
                .source();
    
    private static final Parser<String> CDATA = 
        Patterns.sequence(Patterns.string("<![CDATA[").until("]]>"), 
                          Patterns.string("]]>"))
                .toScanner(XmlTag.CDATA.name())
                .source();

    private static final Parser<String> DOCTYPE = 
        Patterns.sequence(Patterns.string("<!").until(">"), 
                          Patterns.isChar('>'))
                .toScanner(XmlTag.DOCTYPE.name())
                .source();
    
    private static final Parser<String> PROLOG = 
        Patterns.sequence(Patterns.string("<?").until("?>"), 
                          Patterns.string("?>"))
                .toScanner(XmlTag.PROLOG.name())
                .source();
    
    /*
     * 
     * PLAIN TEXT
     * 
     */
    private static TagEntity toTagEntity(final List<Fragment> fragments) {
        final Markup tagName = TagEntity.newMarkup(fragments.removeFirst().text().substring(1));
        
        final List<TagEntity.Attribute> tagAttributes = new ArrayList<>();
        final ListIterator<Fragment> iterator = fragments.listIterator();
        while (iterator.hasNext()) {
            final Map<XmlTag, Fragment> map = new HashMap<>();
            Fragment fragment = iterator.next();
            if (XmlTag.ATTRIBUE_NAME.equals(fragment.tag())) {
                // name OK
                map.put(XmlTag.ATTRIBUE_NAME, fragment);
                if (iterator.hasNext()) {
                    fragment = iterator.next();
                    if (XmlTag.EQUAL.equals(fragment.tag())) {
                        // equal OK
                        map.put(XmlTag.EQUAL, fragment);
                        if (iterator.hasNext()) {
                            fragment = iterator.next();
                            if (XmlTag.ATTRIBUE_VALUE.equals(fragment.tag())) {
                                // value OK
                                map.put(XmlTag.ATTRIBUE_VALUE, fragment);
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
                final Fragment attributeName = map.get(XmlTag.ATTRIBUE_NAME);
                final Fragment attributeValue = map.get(XmlTag.ATTRIBUE_VALUE);
                String tagAttributeValue = null;
                if (attributeValue != null) {
                    tagAttributeValue = attributeValue.text();
                }
                final TagEntity.Attribute tagAttribute = TagEntity.newAttribute(attributeName.text(), 
                                                                                tagAttributeValue);
                tagAttributes.add(tagAttribute);
            }
        }
        return new TagEntity(tagName, tagAttributes);
    }

    private final Parser<String> asPlainText(final Set<Function<TagEntity, Boolean>> tagAsPlainTextFunctions) { 
        return Pattern.rule(context -> {
            final CharSequence src = context.src();
            final int begin = context.begin();
            final int end = context.end();
            
            if (begin >= end) {
                return MISMATCH;
            }
            // check rules
            boolean accept = false;
            if (insideTag.get() != null 
                    && !fragments.isEmpty()) {
                for (final Function<TagEntity, Boolean> tagAsPlainTextFunction : tagAsPlainTextFunctions) {
                    accept = tagAsPlainTextFunction.apply(toTagEntity(new ArrayList<>(fragments)));
                    if (accept) {
                        break;
                    }
                }
                fragments.clear();
            }
            if (!accept) {
                return MISMATCH;
            }
            //
            final String endTag = "</" + insideTag.get() + ">";
            final char[] escapePatternAsArray = endTag.toCharArray();
            insideTag.set(null);
            
            int length = MISMATCH;
            for (int i = begin; i < src.length(); i++) {
                boolean match = true;
                for (int ii = 0; ii < escapePatternAsArray.length; ii++) {
                    char c = escapePatternAsArray[ii];
                    if (src.charAt(i + ii) != c) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    length = i - begin;
                    break;
                }
            }
            if (length == MISMATCH) {
                return MISMATCH;
            }
            //
            if (src.subSequence(begin,  begin + length)
                   .toString()
                   .isBlank()) {
                return MISMATCH;
            }
            // 
            for (int ii = (begin + length - 1); ii >= 0; ii--) {
                final char cc = src.charAt(ii);
                if (Character.isWhitespace(cc)) {
                    length--;
                }
                else {
                    break;
                }
            }
//            System.out.println(src.subSequence(begin, begin + length) + "|");
            return length;
        })
        .toScanner("asPlainText")
        .source();
    }
    
    /*
     * 
     * TAG
     * 
     */
    private static final Parser<String> BEGIN_TAG = 
        Patterns.sequence(Patterns.isChar('<'), XML_TAG)
                .toScanner(XmlTag.BEGIN_TAG.name())
                .source()
                .acceptIf(v -> checkElementName(v.charAt(1)));
    
    private static final Parser<String> END_TAG = 
        Patterns.isChar('>')
                .toScanner(XmlTag.END_TAG.name())
                .source();
    
    private static final Parser<String> CLOSED_END_TAG = 
        Patterns.string("/>")
                .toScanner(XmlTag.CLOSED_END_TAG.name())
                .source();
    
    private static final Parser<String> CLOSED_TAG = 
        Patterns.sequence(Patterns.string("</"),
                          XML_TAG, 
                          Patterns.isChar('>'))
                .toScanner(XmlTag.CLOSED_TAG.name())
                .source()
                .acceptIf(v -> checkElementName(v.charAt(2)));
    
    /*
     * 
     * ATTRIBUTE
     * 
     */
    private static final Pattern SINGLE_QUOTE_STRING = 
        Patterns.sequence(Patterns.isChar('\'').until('\''), 
                          Patterns.isChar('\'')); 

    private static final Pattern DOUBLE_QUOTE_STRING = 
        Patterns.sequence(Patterns.isChar('"').until('"'), 
                          Patterns.isChar('"')); 
    
    private boolean isInsideTagHead() {
        return tagName.get() != null;
    }
    
    private final Pattern isInsideTagHead = 
        Pattern.rule(c -> isInsideTagHead() ? 0 : MISMATCH);
    
    private final Parser<String> attributeNameParser = 
        Patterns.sequence(isInsideTagHead, XML_TAG)
                .toScanner(XmlTag.ATTRIBUE_NAME.name())
                .source()
                .acceptIf(v -> checkElementName(v.charAt(0)));
    
    private final Parser<String> equalParser = 
        Patterns.sequence(isInsideTagHead, Patterns.isChar('='))
                .toScanner(XmlTag.EQUAL.name())
                .source();
    
    private final Parser<String> attributeValueParser = 
        Patterns.sequence(isInsideTagHead, 
                          Patterns.or(SINGLE_QUOTE_STRING, 
                                      DOUBLE_QUOTE_STRING)) 
                .toScanner(XmlTag.EQUAL.name())
                .source();

    /*
     * 
     * TOKENIZER
     * 
     */
    private final Parser<Fragment> xmlTokenizer() {  
       final Parser<Fragment> asPlainText;
       if (!tagAsPlainTextFilters.isEmpty()) {
           asPlainText = asPlainText(tagAsPlainTextFilters).map(hit -> fragment(hit, XmlTag.TEXT));
       }
       else {
           asPlainText = Parsers.never();
       }
       return Parsers.or(RETURN_CARRIAGE.map(hit -> Tokens.fragment(hit, XmlTag.RETURN_CARRIAGE)),
                         CDATA.map(hit -> fragment(hit, XmlTag.CDATA)),
                         COMMENT.map(hit -> fragment(hit, XmlTag.COMMENT)),
                         DOCTYPE.map(hit -> fragment(hit, XmlTag.DOCTYPE)),
                         PROLOG.map(hit -> fragment(hit, XmlTag.PROLOG)),
                         asPlainText,
                         BEGIN_TAG.map(hit -> fragment(hit, XmlTag.BEGIN_TAG)),
                         attributeNameParser.map(hit -> fragment(hit, XmlTag.ATTRIBUE_NAME)),
                         equalParser.map(hit -> fragment(hit, XmlTag.EQUAL)),
                         attributeValueParser.map(hit -> fragment(hit, XmlTag.ATTRIBUE_VALUE)),
                         END_TAG.map(hit -> fragment(hit, XmlTag.END_TAG)),
                         CLOSED_END_TAG.map(hit -> fragment(hit, XmlTag.CLOSED_END_TAG)),
                         CLOSED_TAG.map(hit -> fragment(hit, XmlTag.CLOSED_TAG)),
                         ERROR_PARSER.map(hit -> fragment(hit, XmlTag.ERROR)),
                         TEXT_PARSER.map(hit -> fragment(hit, XmlTag.TEXT)));
    }
    
    private final Fragment fragment(final String hit, 
                                    final XmlTag xmlTag) {
        XmlTag xmlTagResult = xmlTag;
        if (XmlTag.BEGIN_TAG == xmlTag) {
            tagName.set(hit.substring(1));
            insideTag.set(null);
            fragments.clear();
        }
        else if (XmlTag.END_TAG == xmlTag
                    || XmlTag.CLOSED_END_TAG == xmlTag
                    || XmlTag.CLOSED_TAG == xmlTag
                    || XmlTag.ERROR == xmlTag
                    || XmlTag.CDATA == xmlTag
                    || XmlTag.COMMENT == xmlTag
                    || XmlTag.DOCTYPE == xmlTag
                    || XmlTag.PROLOG == xmlTag
                    || XmlTag.TEXT == xmlTag) {
            if (tagName.get() != null
                    && XmlTag.END_TAG == xmlTag) {
                insideTag.set(tagName.get());
            }
            else {
                insideTag.set(null);
                fragments.clear();
            }
            tagName.set(null);
        }
        final Fragment fragment = Tokens.fragment(hit, xmlTagResult);
        if (XmlTag.ATTRIBUE_NAME == xmlTag
                || XmlTag.EQUAL == xmlTag
                || XmlTag.ATTRIBUE_VALUE == xmlTag
                || XmlTag.BEGIN_TAG == xmlTag) {
            fragments.add(fragment);
        }
        return fragment;
    }
    
    /*
     * 
     * LEXER
     * 
     */
    private static Parser<Void> delimitedWhiteSpace() {
        return WHITE_SPACE.skipMany();
    }
    
    /**
     * Returns a parser that tokenizes XML input using the given configuration.
     *
     * @param xmlLexerConfig the lexer configuration to apply
     * @return a parser that produces a list of XML tokens
     */
    public static Parser<List<Token>> lexer(final XmlLexerConfig xmlLexerConfig) {
        final XmlLexer xmlLexer = new XmlLexer(xmlLexerConfig.tagAsPlainTextFilters());
        return ApiParser.lexer(xmlLexer.xmlTokenizer(), delimitedWhiteSpace());
    }
    
    /**
     * Tokenizes the given XML source using the given configuration.
     *
     * @param xmlLexerConfig the lexer configuration to apply
     * @param xml            the XML source text to tokenize
     * @return the list of tokens produced from the XML source
     */
    public static List<Token> lex(final XmlLexerConfig xmlLexerConfig,
                                  final String xml) {
        return ApiParser.lex(XmlLexer.lexer(xmlLexerConfig), xml);
    }
    
}
