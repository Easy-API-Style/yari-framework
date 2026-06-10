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
package com.easyparsingapi.yari.parsec.pattern;

class OptionalPattern extends Pattern {

    private final Pattern pattern;

    OptionalPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Attempts to match the underlying pattern against the source sequence.
     * If the pattern does not match ({@code MISMATCH}), returns {@code 0} instead of
     * {@code MISMATCH}, making the pattern optional (matches zero characters).
     *
     * @param src   the source character sequence in which to perform the match
     * @param begin the start index (inclusive) in {@code src}
     * @param end   the end index (exclusive) in {@code src}
     * @return the number of matched characters, or {@code 0} if the pattern does not match
     */
    @Override
    public int match(CharSequence src, int begin, int end) {
        int l = pattern.match(src, begin, end);
        return (l == MISMATCH) ? 0 : l;
    }

    @Override
    public String toString() {
        return pattern + "?";
    }

}
