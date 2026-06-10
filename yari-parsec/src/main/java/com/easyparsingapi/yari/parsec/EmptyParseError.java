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
package com.easyparsingapi.yari.parsec;

import java.util.List;

import com.easyparsingapi.yari.parsec.error.ParseErrorDetail;

/**
 * Empty implementation of {@link ParseErrorDetail} for subclasses to override.
 */
class EmptyParseError implements ParseErrorDetail {

    private final int index;
    private final String encountered;

    EmptyParseError(int index, String encountered) {
        this.index = index;
        this.encountered = encountered;
    }

    @Override
    public final int getIndex() {
        return index;
    }

    @Override
    public final String getEncountered() {
        return encountered;
    }

    @Override
    public List<String> getExpected() {
        return List.of();
    }

    @Override
    public String getUnexpected() {
        return null;
    }

    @Override
    public String getFailureMessage() {
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(EmptyParseError.class.getSimpleName());
        result.append(" [index=");
        result.append(index);
        result.append(", encountered=");
        result.append(encountered);
        if (getExpected() != null) {
            result.append(", expected=");
            result.append(getExpected());
        }
        if (getUnexpected() != null) {
            result.append(", unexpected=");
            result.append(getUnexpected());
        }
        if (getFailureMessage() != null) {
            result.append(", failureMessage=");
            result.append(getFailureMessage());
        }
        result.append("]");
        return result.toString();
    }

}
