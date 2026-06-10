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

import com.easyparsingapi.yari.parsec.location.SourceLocation;

/**
 * Represents a token node in an Abstract Syntax Tree (AST), carrying the raw text,
 * a classification tag, and its location in the source.
 */
public interface AstToken extends TypeInfo {

    /**
     * Returns the raw text content of this token as it appears in the source.
     *
     * @return the literal text of this token
     */
    public String text();

    /**
     * Returns the tag that classifies the type or role of this token (e.g. keyword, identifier, operator).
     *
     * @return the classification tag of this token
     */
    public String tag();

    /**
     * Returns the location of this token within the source file.
     *
     * @return the source location (line, column, offset) of this token
     */
    public SourceLocation sourceLocation();

}
