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
package com.easyparsingapi.yari.parsec.retry.parsing;

import java.util.List;

import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.util.ast.Ast;

public class Number implements Ast {
    
    private String value;
    private SourceLocation sourceLocation;
    
    public Number(String value) {
        super();
        this.value = value;
    }
    
    @Override
    public List<Ast> astChildren() {
        return List.of();
    }

    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }
    
    @Override
    public void setSourceLocation(SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    @Override
    public String toString() {
        return "Identifier [value=" + value + ", sourceLocation=" + sourceLocation + "]";
    }
    
}