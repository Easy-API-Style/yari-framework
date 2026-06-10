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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents an XML processing instruction prolog node (e.g., {@code <?xml ... ?>}).
 */
@JsonPropertyOrder({"value", "sourceLocation"})
public class Prolog implements AstComment, XmlNode {

    private static final long serialVersionUID = 1L;
    
    /** The raw prolog text. */
    @JsonProperty("value")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String value;
    /** The parent AST node. */
    @JsonIgnore
    private AstNode parent;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;
    
    /**
     * Creates a Prolog node with the given value and no source location.
     *
     * @param value the raw text content of the prolog
     */
    public Prolog(final String value) {
        this(value, null);
    }

    /**
     * Creates a Prolog node with the given value and source location.
     *
     * @param value          the raw text content of the prolog
     * @param sourceLocation the location of this node in the source document
     */
    @JsonCreator
    public Prolog(@JsonProperty("value") final String value,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.value = value;
        this.sourceLocation = sourceLocation;
    }
    
    /**
     * Returns the raw text content of this prolog node.
     *
     * @return the prolog value
     */
    public String getValue() {
        return value;
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
     * Sets the parent AST node of this node.
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
        final Prolog other = (Prolog) astNode;
        return Objects.equals(value, other.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value, sourceLocation);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Prolog node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder result  = new StringBuilder();
        result .append(getClass().getSimpleName());
        result .append(" [value=");
        result .append(value);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result .append("]");
        return result .toString();
    }
    
}
