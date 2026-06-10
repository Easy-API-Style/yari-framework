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

/**
 * Interface implemented by elements that can be associated with a source location.
 * <p>
 * A source-localisable element optionally carries a {@link SourceLocation} that
 * indicates where in the original source text the element was produced.
 * The interface also provides a natural ordering over its implementations based
 * on that location.
 * </p>
 */
public interface SourceLocalisable extends Comparable<SourceLocalisable> {

    /**
     * Comparator that orders {@link SourceLocalisable} instances by their
     * {@link SourceLocation}, placing instances with a {@code null} location first.
     */
    static final Comparator<SourceLocalisable> COMPARATOR =
        Comparator.comparing(SourceLocalisable::getSourceLocation,
                             Comparator.nullsFirst(Comparator.naturalOrder()));

    /**
     * Returns {@code true} if this element has an associated source location.
     *
     * @return {@code true} when {@link #getSourceLocation()} returns a non-null value
     */
    public default boolean hasSourceLocation() {
        return getSourceLocation() != null;
    }

    /**
     * Returns the source location associated with this element, or {@code null} if none.
     *
     * @return the {@link SourceLocation} of this element, or {@code null}
     */
    public SourceLocation getSourceLocation();

    /**
     * Associates a source location with this element.
     *
     * @param sourceLocation the {@link SourceLocation} to set, or {@code null} to clear it
     */
    public void setSourceLocation(final SourceLocation sourceLocation);

    @Override
    public default int compareTo(final SourceLocalisable SourceLocalisable) {
        return COMPARATOR.compare(this, SourceLocalisable);
    }

}
