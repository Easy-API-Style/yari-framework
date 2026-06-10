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
package com.easyparsingapi.yari.parser.css.ast;

import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstComment;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a CSS comment node in the abstract syntax tree.
 * A CSS comment contains the raw comment text and its location in the source.
 */
@JsonPropertyOrder({"comment", "sourceLocation"})
public class CssComment implements AstComment {

    private static final long serialVersionUID = 1L;

    /** The comment text. */
    @JsonProperty("comment")
    private final String comment;
    /** The source location. */
    @JsonProperty("sourceLocation")
    private SourceLocation sourceLocation;

    /**
     * Creates a CSS comment node with the given comment text and no source location.
     *
     * @param comment the raw text of the CSS comment
     */
    public CssComment(final String comment) {
        this(comment, null);
    }

    /**
     * Creates a CSS comment node with the given comment text and source location.
     *
     * @param comment        the raw text of the CSS comment
     * @param sourceLocation the location of this comment in the source file, or {@code null} if unknown
     */
    @JsonCreator
    public CssComment(@JsonProperty("comment") final String comment,
                      @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.comment = comment;
        this.sourceLocation = sourceLocation;
    }

    /**
     * Returns the raw text of this CSS comment.
     *
     * @return the comment text
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
        return Objects.hash(comment, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final CssComment other = (CssComment) object;
        return Objects.equals(comment, other.comment)
                 && Objects.equals(sourceLocation, other.sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result  = new StringBuilder();
        result .append(CssComment.class.getSimpleName());
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
