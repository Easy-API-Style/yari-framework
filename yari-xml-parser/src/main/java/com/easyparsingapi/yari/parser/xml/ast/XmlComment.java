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

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents an XML comment node ({@code <!-- ... -->}) in the AST.
 */
@JsonPropertyOrder({"comment", "sourceLocation"})
public class XmlComment implements AstComment, XmlNode {

    private static final long serialVersionUID = 1L;

    /** The comment text. */
    @JsonProperty("comment")
    private final String comment;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates an XmlComment with the given comment text and no source location.
     *
     * @param comment the comment text
     */
    public XmlComment(final String comment) {
        this(comment, null);
    }

    /**
     * Creates an XmlComment with the given comment text and source location.
     *
     * @param comment        the comment text
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public XmlComment(@JsonProperty("comment") final String comment,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.comment = comment;
        this.sourceLocation = sourceLocation;
    }

    /**
     * Returns the comment text.
     *
     * @return the comment string
     */
    public String getComment() {
        return comment;
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
     * Sets the parent node of this comment in the AST.
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
        final XmlComment other = (XmlComment) astNode;
        return Objects.equals(comment, other.comment);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(comment, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof XmlComment node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder result  = new StringBuilder();
        result .append(getClass().getSimpleName());
        result .append(" [comment=");
        result .append(comment);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result .append("]");
        return result .toString();
    }
    
}
