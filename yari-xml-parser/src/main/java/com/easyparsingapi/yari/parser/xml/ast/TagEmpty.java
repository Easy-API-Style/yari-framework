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
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents an XML empty element that may or may not be explicitly closed with {@code />}.
 */
@JsonPropertyOrder({"closed", "name", "attributes", "sourceLocation"})
public class TagEmpty implements TagSimple, TagWithAttribute {
    
    private static final long serialVersionUID = 1L;
    
    /** Whether the tag is self-closing. */
    @JsonProperty("closed")
    private final boolean closed;
    /** The tag name. */
    @JsonProperty("name")
    private final TagName name;
    /** The tag attributes. */
    @JsonProperty("attributes")
    private final List<TagAttribute> attributes;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Creates a TagEmpty with the given attributes and no source location.
     *
     * @param closed     {@code true} if the tag is explicitly closed with {@code />}
     * @param name       the tag name
     * @param attributes the list of attributes on this tag
     */
    public TagEmpty(final boolean closed,
                    final TagName name,
                    final List<TagAttribute> attributes) {
        this(closed, name, attributes, null);
    }

    /**
     * Creates a TagEmpty with all fields.
     *
     * @param closed         {@code true} if the tag is explicitly closed with {@code />}
     * @param name           the tag name
     * @param attributes     the list of attributes on this tag
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public TagEmpty(@JsonProperty("closed") final boolean closed,
                    @JsonProperty("name") final TagName name,
                    @JsonProperty("attributes") final List<TagAttribute> attributes,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.closed = closed;
        this.name = name;
        this.attributes = CollectionUtil.nullToEmpty(attributes);
        this.sourceLocation = sourceLocation;
        XmlUtil.setAstParent(this);
    }
    
    /**
     * Returns true if this self-closing tag ends with />.
     *
     * @return {@code true} if the tag ends with {@code />}
     */
    public boolean isClosed() {
        return closed;
    }
    
    @Override
    public TagName getName() {
        return name;
    }
    
    @Override
    public List<TagAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name, attributes);
    }

    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this empty tag.
     *
     * @param parent the parent node in the AST
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
        final TagEmpty other = (TagEmpty) astNode;
        return Objects.equals(name, other.name)
                 && Objects.equals(closed, other.closed)
                 && Objects.equals(attributes, other.attributes);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, attributes, closed, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof TagEmpty node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [name=");
        result.append(name);
        result.append(", attributes=");
        result.append(getAttributes().size());
        result.append(", closed=");
        result.append(closed);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
