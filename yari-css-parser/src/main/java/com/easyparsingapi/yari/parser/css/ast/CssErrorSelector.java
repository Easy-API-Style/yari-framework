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

import java.util.List;

import com.easyparsingapi.yari.core.ast.AstToken;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.error.ParseErrorDetail;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parser.css.parser.CssToken;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CSS AST error node representing an invalid or unrecognized selector encountered during parsing.
 */
public class CssErrorSelector extends CssError implements CssSelector {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code CssErrorSelector} from JSON deserialization data.
     *
     * @param tokens          the list of AST tokens associated with the erroneous selector
     * @param encountered     the symbol encountered during the parsing error
     * @param expected        the list of expected symbols at that position
     * @param unexpected      the unexpected symbol that caused the error
     * @param failureMessage  the message describing the cause of the failure
     * @param sourceLocation  the position in the source where the error occurred
     */
    @JsonCreator
    public CssErrorSelector(@JsonProperty("tokens") final List<AstToken> tokens,
                            @JsonProperty("encountered") String encountered,
                            @JsonProperty("expected") List<String> expected,
                            @JsonProperty("unexpected") String unexpected,
                            @JsonProperty("failureMessage") String failureMessage,
                            @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super(tokens, encountered, expected, unexpected, failureMessage, sourceLocation);
    }

    /*
     *
     * STATIC
     *
     */

    /**
     * Creates a new {@code CssErrorSelector} instance from the details of a parsing error.
     *
     * @param parseErrorDetail the details of the parsing error (encountered symbol, expected symbols, failure message)
     * @param sourceLocation   the position in the source where the error occurred
     * @param tokens           the list of raw tokens involved in the error
     * @return a new {@code CssErrorSelector} representing the erroneous selector
     */
    public static CssErrorSelector newInstanceSelector(final ParseErrorDetail parseErrorDetail,
                                               final SourceLocation sourceLocation,
                                               final List<Token> tokens) {
        return new CssErrorSelector(CssToken.toAstToken(tokens),
                                    parseErrorDetail.getEncountered(),
                                    parseErrorDetail.getExpected(),
                                    parseErrorDetail.getUnexpected(),
                                    parseErrorDetail.getFailureMessage(),
                                    /** The field. */
                                    sourceLocation);
    }

}
