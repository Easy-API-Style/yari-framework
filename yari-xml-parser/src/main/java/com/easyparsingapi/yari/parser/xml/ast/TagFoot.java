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
 * Represents the closing tag element of an XML element (e.g., {@code </tag>}).
 */
@JsonPropertyOrder({"name", "sourceLocation"})
public class TagFoot implements XmlNode {
    
    private static final long serialVersionUID = 1L;
    
    /** The closing tag name. */
    @JsonProperty("name")
    private final TagName name;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Creates a TagFoot with the given name and no source location.
     *
     * @param name the tag name of this closing element
     */
    public TagFoot(final TagName name) {
        super();
        this.name = name;
    }

    /**
     * Creates a TagFoot with the given name and source location.
     *
     * @param name           the tag name of this closing element
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public TagFoot(@JsonProperty("name") final TagName name,
                   @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.name = name;
        this.sourceLocation = sourceLocation;
        XmlUtil.setAstParent(this);
    }

    /**
     * Returns the closing tag name.
     *
     * @return the tag name
     */
    public TagName getName() {
        return name;
    }

    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(name);
    }

    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this closing tag element.
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
        final TagFoot other = (TagFoot) astNode;
        return Objects.equals(name, other.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof TagFoot node) {
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
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }
    
}