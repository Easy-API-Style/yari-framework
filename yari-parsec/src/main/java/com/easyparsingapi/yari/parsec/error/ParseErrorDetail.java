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
package com.easyparsingapi.yari.parsec.error;

import java.util.List;

import com.easyparsingapi.yari.parsec.Parsers;

/**
 * Describes details of a parsing error to support fine-grained error handling.
 * 
 */
public interface ParseErrorDetail {

    /**
     * Returns the 0-based index in the source where the error occurred.
     *
     * @return the error position index
     */
    int getIndex();

    /**
     * Returns the physical input encountered when the error occurred.
     *
     * @return the encountered input, or {@code null} if not available
     */
    String getEncountered();

    /**
     * Returns all tokens or constructs that were logically expected at the error position.
     *
     * @return the list of expected inputs
     */
    List<String> getExpected();

    /**
     * Returns what was logically unexpected at the error position, or {@code null} if none.
     *
     * @return the unexpected input, or {@code null}
     */
    String getUnexpected();

    /**
     * Returns the error message incurred by {@link Parsers#fail(String)},
     * or {@code null} if none.
     *
     * @return the failure message, or {@code null}
     */
    String getFailureMessage();
    
}
