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
package com.easyparsingapi.yari.parser.css.parser;

import java.util.ArrayList;
import java.util.List;

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parser.css.ast.CssNode;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A list of CSS AST nodes produced during parsing, paired with the source
 * {@link Token} list that generated them.
 *
 * <p>This list is used as a deferred container: blocks whose content has not yet
 * been sub-parsed hold their raw tokens here until the next parsing pass
 * consumes and clears them via {@link #tokens()}.</p>
 */
public class CssNodeList extends ArrayList<CssNode> {

    private static final long serialVersionUID = 1L;

    /** The tokens. */
    @JsonIgnore
    final List<Token> tokens;

    CssNodeList(final List<Token> tokens) {
        super();
        this.tokens = tokens;
    }

    List<Token> tokens() {
        final List<Token> result = new ArrayList<>(tokens);
        tokens.clear();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return CssNodeList.class.getSimpleName() + "." + super.toString();
    }

}
