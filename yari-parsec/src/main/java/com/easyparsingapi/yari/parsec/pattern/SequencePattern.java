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

class SequencePattern extends Pattern {
    private final Pattern[] patterns;

    SequencePattern(Pattern... patterns) {
        this.patterns = patterns;
    }

    /**
     * Attempts to match the sub-patterns sequentially against the source.
     * Each sub-pattern must match starting from the position where the previous one stopped;
     * if any of them fails, the method immediately returns {@code MISMATCH}.
     *
     * @param src   the character sequence in which to perform the match
     * @param begin the start index (inclusive) in {@code src}
     * @param end   the end index (exclusive) in {@code src}
     * @return the total matched length if all sub-patterns match,
     *         or {@code MISMATCH} if any of them fails
     */
    @Override
    public int match(final CharSequence src, final int begin, final int end) {
        int current = begin;
        for (Pattern pattern : patterns) {
            int l = pattern.match(src, current, end);
            if (l == MISMATCH)
                return l;
            current += l;
        }
        return current - begin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Pattern pattern : patterns) {
            sb.append(pattern);
        }
        return sb.toString();
    }

}
