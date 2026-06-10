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
package com.easyparsingapi.yari.core.ast;

import java.util.List;

import com.easyparsingapi.yari.parsec.location.SourceLocation;

/**
 * Represents a parse error node in the abstract syntax tree, providing information
 * about what was encountered, what was expected, and which tokens are involved.
 */
public interface AstError extends AstNode {

    /**
     * Returns the list of tokens associated with this parse error.
     *
     * @return the tokens involved in this error, never {@code null}
     */
    public List<AstToken> getTokens();

    /**
     * Returns a description of the input that was actually encountered during parsing.
     *
     * @return the encountered input string, or {@code null} if not applicable
     */
    public String getEncountered();

    /**
     * Returns the list of input strings or token types that were expected at the error location.
     *
     * @return the list of expected alternatives, never {@code null}
     */
    public List<String> getExpected();

    /**
     * Returns a description of the unexpected input that triggered this parse error.
     *
     * @return the unexpected input string, or {@code null} if not applicable
     */
    public String getUnexpected();

    /**
     * Returns a human-readable message describing the parse failure.
     *
     * @return the failure message, or {@code null} if not available
     */
    public String getFailureMessage();

    /**
     * Returns the source location of this error node, spanning from the start of the first
     * token to the end of the last token. Returns {@code null} if no tokens are associated
     * with this error.
     *
     * @return the source location covering all error tokens, or {@code null} if there are none
     */
    @Override
    public default SourceLocation getSourceLocation() {
        SourceLocation result = null;
        if (!getTokens().isEmpty()) {
            result = new SourceLocation(getTokens().getFirst().sourceLocation().start(),
                                        getTokens().getLast().sourceLocation().end());
        }
        return result;
    }

}
