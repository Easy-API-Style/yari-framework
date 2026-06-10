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
package com.easyparsingapi.yari.parser.xml.ast;

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a text content node in an XML document.
 */
@JsonPropertyOrder({"text", "sourceLocation"})
public class Text implements XmlNode {

    private static final long serialVersionUID = 1L;

    /** The text content. */
    @JsonProperty("text")
    private final String text;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a Text node with the given content and no source location.
     *
     * @param text the text content
     */
    public Text(final String text) {
        this(text, null);
    }

    /**
     * Creates a Text node with the given content and source location.
     *
     * @param text           the text content
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public Text(@JsonProperty("text") final String text,
                @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.text = text;
        this.sourceLocation = sourceLocation;
        XmlUtil.setAstParent(this);
    }

    /**
     * Returns the text content.
     *
     * @return the text string
     */
    public String getText() {
        return text;
    }

    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes();
    }

    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this text node in the AST.
     *
     * @param parent the parent node
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    
    @Override
    public boolean equalsNode(final AstNode astNode) {
        if (this == astNode) {
            return true;
        }
        if (astNode == null) {
            return false;
        }
        if (getClass() != astNode.getClass()) {
            return false;
        }
        final Text other = (Text) astNode;
        return Objects.equals(text, other.text);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(text, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Text node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [text=");
        result.append(text);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }
    
}
