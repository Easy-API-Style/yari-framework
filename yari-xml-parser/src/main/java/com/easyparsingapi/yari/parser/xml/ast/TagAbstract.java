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
 * Abstract base class for XML elements that have a head, body and foot structure.
 *
 * @param <B> the type of the body node
 */
@JsonPropertyOrder({"head", "body", "foot", "sourceLocation"})
public abstract class TagAbstract<B extends XmlNode> implements TagComplex<B> {
    
    private static final long serialVersionUID = 1L;
    
    /** The tag head. */
    @JsonProperty("head")
    private final TagHead head;
    /** The tag body. */
    @JsonProperty("body")
    private final B body;
    /** The tag foot. */
    @JsonProperty("foot")
    private final TagFoot foot;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Creates a TagAbstract with the given head, body and foot, and no source location.
     *
     * @param head the opening tag element
     * @param body the body node
     * @param foot the closing tag element
     */
    public TagAbstract(final TagHead head,
                       final B body,
                       final TagFoot foot) {
        this(head, body, foot, null);
    }

    /**
     * Creates a TagAbstract with all fields.
     *
     * @param head           the opening tag element
     * @param body           the body node
     * @param foot           the closing tag element
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public TagAbstract(@JsonProperty("head") final TagHead head,
                       @JsonProperty("body") final B body,
                       @JsonProperty("foot") final TagFoot foot,
                       @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.head = head;
        this.body = body;
        this.foot = foot;
        this.sourceLocation = sourceLocation;
        XmlUtil.setAstParent(this);
    }
    
    @Override
    @JsonIgnore
    public TagName getName() {
        return getHead().getName();
    }

    @Override
    @JsonIgnore
    public List<TagAttribute> getAttributes() {
        return getHead().getAttributes();
    }
    
    @Override
    public TagHead getHead() {
        return head;
    }
    
    @Override
    public B getBody() {
        return body;
    }
    
    @Override
    public TagFoot getFoot() {
        return foot;
    }

    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(head, body, foot);
    }

    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this tag in the AST.
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
        final TagAbstract<?> other = (TagAbstract<?>) astNode;
        return Objects.equals(head, other.head)
                  && Objects.equals(body, other.body)
                  && Objects.equals(foot, other.foot);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(head, body, foot, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof TagAbstract<?> node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(" [head=");
        result.append(head);
        result.append(", body=");
        result.append(body);
        result.append(", foot=");
        result.append(foot);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

}
