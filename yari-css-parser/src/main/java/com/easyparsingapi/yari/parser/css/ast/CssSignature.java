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

import java.util.stream.Stream;

/**
 * Represents a CSS node that carries a signature, i.e. a named construct
 * (such as a function or at-rule) whose definition includes a list of
 * parameters accessible as child {@link CssNode} elements.
 */
public interface CssSignature extends CssNode {

    /**
     * Returns a sequential stream over the parameter nodes that make up this signature.
     *
     * @return a {@link Stream} of {@link CssNode} representing the parameters of this signature
     */
    public Stream<CssNode> streamParameters();

}
