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

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;

/**
 * Lexer token tags for the XML lexer.
 */
public enum XmlTag {
    /** A carriage return or line feed character sequence. */
    RETURN_CARRIAGE,

    /** A CDATA section ({@code <![CDATA[...]]>}). */
    CDATA,
    /** A DOCTYPE declaration ({@code <!...>}). */
    DOCTYPE,
    /** An XML processing instruction prolog ({@code <?...?>}). */
    PROLOG,
    /** An XML comment ({@code <!--...-->}). */
    COMMENT,

    /** The opening token of a tag ({@code <name}). */
    BEGIN_TAG,
    /** An attribute name token. */
    ATTRIBUE_NAME,
    /** An equals sign separating an attribute name from its value. */
    EQUAL,
    /** An attribute value token (quoted string). */
    ATTRIBUE_VALUE,
    /** The {@code >} token that ends an opening tag. */
    END_TAG,
    /** The {@code />} token that ends a self-closing tag. */
    CLOSED_END_TAG,
    /** A closing tag token ({@code </name>}). */
    CLOSED_TAG,

    /** A plain text content token between tags. */
    TEXT,

    /** A lexer error token produced for unrecognised input. */
    ERROR;
    
    /**
     * Returns the {@link XmlTag} associated with the given token, or {@code null} if the token
     * does not carry an {@link XmlTag} fragment.
     *
     * @param token the lexer token whose tag is to be retrieved
     * @return the corresponding {@code XmlTag}, or {@code null}
     */
    public static XmlTag tag(final Token token) {
        XmlTag result = null;
        final Object value = token.value();
        if (value instanceof Fragment) {
            final Fragment fragment = (Fragment) value;
            result = (XmlTag) fragment.tag();
        }
        return result;
    }
    
}
