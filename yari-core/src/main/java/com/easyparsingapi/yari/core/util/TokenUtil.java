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
package com.easyparsingapi.yari.core.util;

import java.util.List;

import com.easyparsingapi.yari.parsec.Token;
import com.google.common.base.Strings;

/**
 * Utility class providing helper methods for working with {@link Token} instances.
 */
public class TokenUtil {

    /** Not instantiable — utility class. */
    private TokenUtil() {}

    /**
     * Determines whether the given tokens are contiguous (stuck together) in the source,
     * i.e. each token immediately follows the previous one with no gap.
     *
     * @param tokens the tokens to check, in source order
     * @return {@code true} if all consecutive token pairs are adjacent with no gap, {@code false} otherwise
     */
    public static boolean isStuck(final Token... tokens) {
        return isStuck(CollectionUtil.toList(tokens));
    }

    /**
     * Determines whether the given list of tokens are contiguous (stuck together) in the source,
     * i.e. each token immediately follows the previous one with no gap.
     *
     * @param tokens the list of tokens to check, in source order
     * @return {@code true} if all consecutive token pairs are adjacent with no gap, {@code false} otherwise
     */
    public static boolean isStuck(final List<Token> tokens) {
        boolean result = true;
        Token previous = null;
        for (final Token token : tokens) {
            if (previous != null) {
                if (previous.index() + previous.length() != token.index()) {
                    result = false;
                    break;
                }
            }
            previous = token;
        }
        return result;
    }

    /**
     * Reconstructs the source text represented by the given list of tokens, preserving
     * the original whitespace gaps between tokens based on their source indices.
     *
     * @param tokens the list of tokens to convert to a string, in source order
     * @return the reconstructed source string with whitespace gaps restored between tokens
     */
    public static String toString(final List<Token> tokens) {
        final StringBuilder result = new StringBuilder();
        if (!CollectionUtil.isEmpty(tokens)) {
            Token previous = null;
            for (final Token token : tokens) {
                if (previous != null) {
                    result.append(Strings.repeat(" ", token.index() - previous.index() - previous.length()));
                }
                result.append(token.toString());
                previous = token;
            }
        }
        return result.toString();
    }

}
