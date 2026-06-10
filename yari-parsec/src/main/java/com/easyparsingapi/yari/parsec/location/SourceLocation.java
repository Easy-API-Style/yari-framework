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
package com.easyparsingapi.yari.parsec.location;

import java.util.Comparator;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents the location of a source element in a source file, defined by a start position and an end position.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"start", "end"})
public class SourceLocation implements Comparable<SourceLocation> {

    private final static Comparator<SourceLocation> COMPARATOR =
        Comparator.comparing(SourceLocation::start)
                  .thenComparing(SourceLocation::end);

    /** The start position (inclusive) of this source location. */
    @JsonProperty("start")
    public final Position start;

    /** The end position (inclusive) of this source location. */
    @JsonProperty("end")
    public final Position end;

    /**
     * Creates a new {@code SourceLocation} with the given start and end positions.
     *
     * @param start the start position of the source location
     * @param end   the end position of the source location
     */
    @JsonCreator
    public SourceLocation(@JsonProperty("start") final Position start,
                          @JsonProperty("end") final Position end) {
        super();
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the start position (inclusive) of this source location.
     *
     * @return the start {@link Position}
     */
    public Position start() {
        return start;
    }

    /**
     * Returns the end position (inclusive) of this source location.
     *
     * @return the end {@link Position}
     */
    public Position end() {
        return end;
    }

    @Override
    public int compareTo(final SourceLocation sourceLocation) {
        return COMPARATOR.compare(this, sourceLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(end, start);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SourceLocation other = (SourceLocation) obj;
        return Objects.equals(end, other.end)
                && Objects.equals(start, other.start);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("SourceLocation [start=");
        result.append(start);
        result.append(", end=");
        result.append(end);
        result.append("]");
        return result.toString();
    }

    /*
     *
     * CLASS
     *
     */

    /**
     * Represents a position in a source file, identified by a line number and a column number.
     */
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonPropertyOrder({"line", "column"})
    public static class Position implements Comparable<Position> {

        private final static Comparator<Position> COMPARATOR =
            Comparator.comparing(Position::line)
                      .thenComparing(Position::column);

        @JsonProperty("line")
        private final int line;
        @JsonProperty("column")
        private final int column;

        /**
         * Creates a new {@code Position} with the given line and column numbers.
         *
         * @param line   the line number (1-based)
         * @param column the column number (1-based)
         */
        @JsonCreator
        public Position(@JsonProperty("line") final int line,
                        @JsonProperty("column") final int column) {
            super();
            this.line = line;
            this.column = column;
        }

        /**
         * Returns the line number of this position (1-based).
         *
         * @return the 1-based line number
         */
        public int line() {
            return line;
        }

        /**
         * Returns the column number of this position (1-based).
         *
         * @return the 1-based column number
         */
        public int column() {
            return column;
        }

        @Override
        public int compareTo(final Position position) {
            return COMPARATOR.compare(this, position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(column, line);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Position other = (Position) obj;
            return column == other.column && line == other.line;
        }

        @Override
        public String toString() {
             final StringBuilder result = new StringBuilder();
             result.append("line ");
             result.append(line);
             result.append(" column ");
             result.append(column);
             return result.toString();
        }

    }

}
