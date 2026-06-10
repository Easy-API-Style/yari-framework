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
package com.easyparsingapi.yari.parser.javascript.ast;

import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node that represents a JavaScript comment (either single-line or block).
 * Implements {@link AstComment} to integrate with the generic comment infrastructure.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonPropertyOrder({"type", "comment", "sourceLocation"})
public class JavascriptComment implements AstComment {

    private static final long serialVersionUID = 1L; 
    
    /**
     * Enumerates the kinds of JavaScript comment that can appear in source code.
     */
    public static enum Type {
        /** Single-line comment. */
        line,
        /** Block comment. */
        block
    }
    
    /** The type. */
    @JsonProperty("type") 
    private final Type type;
    /** The comment. */
    @JsonProperty("comment") 
    private final String comment;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs a {@code JavascriptComment} without source location information.
     *
     * @param type    the kind of comment ({@link Type#line} or {@link Type#block})
     * @param comment the raw text of the comment
     */
    public JavascriptComment(final Type type,
                             final String comment) {
        this(type, comment, null);
    }
    
    /**
     * Constructs a {@code JavascriptComment} with full source location information.
     *
     * @param type           the kind of comment ({@link Type#line} or {@link Type#block})
     * @param comment        the raw text of the comment
     * @param sourceLocation the source location of this comment, or {@code null}
     */
    @JsonCreator
    public JavascriptComment(@JsonProperty("type") final Type type,
                             @JsonProperty("comment") final String comment,
                             @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.type = type;
        this.comment = comment;
        this.sourceLocation = sourceLocation;
    }
    
    /**
     * Returns the type of this comment.
     *
     * @return {@link Type#line} for a single-line comment, {@link Type#block} for a block comment
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the raw text of this comment.
     *
     * @return the comment text as it appears in the source
     */
    public String getComment() {
        return comment;
    }
    
    /** {@inheritDoc} */
    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(comment, sourceLocation, type);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JavascriptComment other = (JavascriptComment) obj;
        return Objects.equals(comment, other.comment) 
                && Objects.equals(sourceLocation, other.sourceLocation)
                && type == other.type;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result  = new StringBuilder();
        result .append(JavascriptComment.class.getSimpleName());
        result .append(" [type=");
        result .append(type);
        result .append(", comment=");
        result .append(comment);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result .append("]");
        return result .toString();
    }
    
}
