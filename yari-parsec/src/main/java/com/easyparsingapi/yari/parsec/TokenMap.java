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
package com.easyparsingapi.yari.parsec;

/**
 * Maps a {@link Token} to an object of type {@code T}, or {@code null} if the token isn't recognized.
 *
 * @param <T> the type of the mapped result
 */
@FunctionalInterface
public interface TokenMap<T> {

    /**
     * Transforms {@code token} to an instance of {@code T}.
     * {@code null} is returned if the token isn't recognized.
     *
     * @param token the token to transform
     * @return the mapped result, or {@code null} if the token is not recognized
     */
    T map(Token token);
    
}
