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
package com.easyparsingapi.yari.parsec.util.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parsec.location.SourceLocation;

public interface Ast extends SourceLocalisable {
    
    public default Stream<Ast> astStream() {
        Stream<Ast> result = Stream.of(this);
        for (final Ast astNode : astChildren()) {
            result = Stream.concat(result, astNode.astStream());
        }
        return result;
    }
    
    public List<Ast> astChildren();
    
    @Override
    public SourceLocation getSourceLocation();
    
    @Override
    public void setSourceLocation(SourceLocation sourceLocation);
    
    public static List<Ast> childrenAttributes(final Object... attributes) {
        final List<Ast> result = new ArrayList<>();
        if (attributes != null) {
            for (final Object attribute : attributes) {
                if (attribute instanceof Collection<?> collection) {
                    for (final Object value : collection) {
                        if (value instanceof Ast node) {
                             result.add(node);
                        }
                    }
                }
                else {
                    if (attribute instanceof Ast node) {
                        result.add(node);
                    }
                }
            }
        }
        return result;
    }
    
}