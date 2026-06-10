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

public class Block implements Ast {
    
    private List<Ast> elements;
    private SourceLocation sourceLocation;
    
    public Block(List<Ast> elements) {
        super();
        this.elements = elements;
    }

    public List<Ast> getElements() {
        return elements;
    }

    public void add(Ast element) {
        this.elements.add(element);
    }

    public void addAll(List<Ast> elements) {
        this.elements.addAll(elements);
    }

    @Override
    public List<Ast> astChildren() {
        return Ast.childrenAttributes(elements);
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
        return "Block [elements=" + elements + ", sourceLocation=" + sourceLocation + "]";
    }
    
}