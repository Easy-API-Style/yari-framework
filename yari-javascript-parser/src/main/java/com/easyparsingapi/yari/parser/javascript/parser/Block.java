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
package com.easyparsingapi.yari.parser.javascript.parser;

import java.util.ArrayList;
import java.util.List;

import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An intermediate parse result that accumulates a sequence of {@link JavascriptNode}
 * statements together with the raw {@link com.easyparsingapi.yari.parsec.Token} list
 * from which they were produced.
 * <p>
 * Instances are created and consumed internally by the parser; they are not part
 * of the public AST.
 * </p>
 */
public class Block extends ArrayList<JavascriptNode> {
    
    private static final long serialVersionUID = 1L;
    
    /** The tokens. */
    @JsonIgnore
    final List<Token> tokens;

    Block(final List<Token> tokens) {
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
        return Block.class.getSimpleName() + "." + super.toString();
    }
    
}