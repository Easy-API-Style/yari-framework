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
 * Provides common token values.
 */
public class Tokens {

    /**
     * Returns a {@link Fragment} tagged with {@code tag}.
     *
     * @param text the fragment text.
     * @param tag  the tag representing the fragment's semantics.
     * @return a new {@link Fragment} with the given text and tag.
     */
    public static Fragment fragment(String text, Object tag) {
        return new Fragment(text, tag);
    }

    /**
     * Returns a {@link Fragment} tagged as {@link Tag#RESERVED}.
     * 
     * @param name the reserved word.
     * @return the token value.
     */
    public static Fragment reserved(String name) {
        return fragment(name, Tag.RESERVED);
    }

    /**
     * Returns a {@link Fragment} tagged as {@link Tag#IDENTIFIER}.
     * 
     * @param name the identifier.
     * @return the token value.
     */
    public static Fragment identifier(String name) {
        return fragment(name, Tag.IDENTIFIER);
    }

    /**
     * Returns a {@link Fragment} tagged as {@link Tag#DECIMAL}.
     * 
     * @param s the decimal string representation.
     * @return the token value.
     */
    public static Fragment decimalLiteral(String s) {
        return fragment(s, Tag.DECIMAL);
    }

    /**
     * Returns a {@link Fragment} tagged as {@link Tag#INTEGER}.
     * 
     * @param s the integer string representation.
     * @return the token value.
     */
    public static Fragment integerLiteral(String s) {
        return fragment(s, Tag.INTEGER);
    }

    /**
     * Returns a {@link ScientificNotation} with {@code significand}
     * before the 'e' or 'E' and {@code exponent} after.
     *
     * @param significand the mantissa part of the scientific notation literal.
     * @param exponent    the exponent part of the scientific notation literal.
     * @return a new {@link ScientificNotation} instance.
     */
    public static ScientificNotation scientificNotation(String significand, String exponent) {
        return new ScientificNotation(significand, exponent);
    }

    /**
     * Represents a fragment tagged according to its semantics. 
     * It's a convenience class so that you don't have 
     * to create many classes each for a different token. 
     * Instead, you could just use {@code new fragment(text, "token1")} 
     * to uniquely identify a token by the "token1" tag.
     */
    public static class Fragment {
        
        private final String text;
        private final Object tag;

        /** Called by {@code Tokens.fragment()}. */
        private Fragment(String text, Object tag) {
            this.text = text;
            this.tag = tag;
        }

        /**
         * Returns the text of the token value.
         *
         * @return the raw text string that was scanned for this fragment
         */
        public String text() {
            return text;
        }

        /**
         * Returns the tag of the token value.
         *
         * @return the tag object that identifies the semantic category of this fragment
         */
        public Object tag() {
            return tag;
        }

        boolean equalFragment(Fragment that) {
            return tag.equals(that.tag) && text.equals(that.text);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Fragment) {
                return equalFragment((Fragment) obj);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return tag.hashCode() * 31 + text.hashCode();
        }

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Represents a scientific notation with a significand (mantissa) and an
     * exponent. Both are represented with a {@link String} to avoid number range issue.
     */
    public static class ScientificNotation {

        /** The significand (mantissa) before the "E". */
        public final String significand;

        /** The exponent after the "E". */
        public final String exponent; // we leave the range check to the semantics analysis

        /** Called by {@code Tokens.scientificNotation()}. */
        private ScientificNotation(String mantissa, String exp) {
            this.significand = mantissa;
            this.exponent = exp;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ScientificNotation) {
                ScientificNotation that = (ScientificNotation) obj;
                return significand.equals(that.significand) && exponent.equals(that.exponent);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return significand.hashCode() * 31 + exponent.hashCode();
        }

        @Override
        public String toString() {
            return significand + "E" + exponent;
        }

    }

    /** Pre-built {@link Fragment} token tags. */
    public enum Tag {

        /** Reserved word */
        RESERVED,

        /** Regular identifier */
        IDENTIFIER,

        /** Integral number literal */
        INTEGER,

        /** Decimal number literal */
        DECIMAL,
        
        /** Comment literal */
        COMMENT
    }

    private Tokens() {
    }
    
}
