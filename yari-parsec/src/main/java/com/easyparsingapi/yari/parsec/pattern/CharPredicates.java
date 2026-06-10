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
package com.easyparsingapi.yari.parsec.pattern;

import com.easyparsingapi.yari.parsec.internal.util.StringUtil;

/**
 * Provides common {@link CharPredicate} implementations.
 */
public class CharPredicates {

    private CharPredicates() {
    }
    
    /** A {@link CharPredicate} that returns true if the character is a line terminator ({@code '\n'} or {@code '\r'}). */
    public static final CharPredicate IS_END_LINE = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return c == '\n' || c == '\r';
        }

        @Override
        public String toString() {
            return "[0-9a-fA-F]";
        }
    };

    /** A {@link CharPredicate} that always returns false. */
    public static final CharPredicate NEVER = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return false;
        }

        @Override
        public String toString() {
            return "none";
        }
    };

    /** A {@link CharPredicate} that always returns true. */
    public static final CharPredicate ALWAYS = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return true;
        }

        @Override
        public String toString() {
            return "any character";
        }
    };

    /**
     * A {@link CharPredicate} that returns true 
     * if the character is a digit or within the range of {@code [a-f]} or {@code [A-F]}.
     */
    public static final CharPredicate IS_HEX_DIGIT = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
        }

        @Override
        public String toString() {
            return "[0-9a-fA-F]";
        }
    };

    /**
     * A {@link CharPredicate} that returns true 
     * if {@link Character#isUpperCase(char)} returns true.
     */
    public static final CharPredicate IS_UPPER_CASE = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return Character.isUpperCase(c);
        }

        @Override
        public String toString() {
            return "uppercase";
        }
    };

    /**
     * A {@link CharPredicate} that returns true 
     * if {@link Character#isLowerCase(char)} returns true.
     */
    public static final CharPredicate IS_LOWER_CASE = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return Character.isLowerCase(c);
        }

        @Override
        public String toString() {
            return "lowercase";
        }
    };

    /**
     * A {@link CharPredicate} that returns true 
     * if {@link Character#isWhitespace(char)} returns true.
     */
    public static final CharPredicate IS_WHITESPACE = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return Character.isWhitespace(c);
        }

        @Override
        public String toString() {
            return "whitespace";
        }
    };

    /**
     * A {@link CharPredicate} that returns true
     * if the character is an alpha character.
     */
    public static final CharPredicate IS_ALPHA = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return c <= 'z' && c >= 'a' || c <= 'Z' && c >= 'A';
        }

        @Override
        public String toString() {
            return "[a-zA-Z]";
        }
    };

    /**
     * A {@link CharPredicate} that returns true 
     * if it is an alpha character or the underscore character {@code _}.
     */
    public static final CharPredicate IS_ALPHA_ = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return c == '_' || c <= 'z' && c >= 'a' || c <= 'Z' && c >= 'A';
        }

        @Override
        public String toString() {
            return "[a-zA-Z_]";
        }
    };

    /**
     * A {@link CharPredicate} that returns true 
     * if {@link Character#isLetter(char)} returns true.
     */
    public static final CharPredicate IS_LETTER = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return Character.isLetter(c);
        }

        @Override
        public String toString() {
            return "letter";
        }
    };

    /**
     * A {@link CharPredicate} that returns true 
     * if it is an alphanumeric character, or an underscore character.
     */
    public static final CharPredicate IS_ALPHA_NUMERIC = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9';
        }

        @Override
        public String toString() {
            return "[0-9a-zA-Z]";
        }
    };

    /**
     * A {@link CharPredicate} that returns true 
     * if it is an alphanumeric character, or an underscore character.
     */
    public static final CharPredicate IS_ALPHA_NUMERIC_ = new CharPredicate() {
        @Override
        public boolean isChar(char c) {
            return c == '_' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9';
        }

        @Override
        public String toString() {
            return "[0-9a-zA-Z_]";
        }
    };

    /**
     * A {@link CharPredicate} that returns {@code true} if the character is equal to {@code c}.
     *
     * @param c the character to match
     * @return a predicate that matches the given character
     */
    public static CharPredicate isChar(final char c) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char x) {
                return x == c;
            }

            @Override
            public String toString() {
                return Character.toString(c);
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if the character is not equal to {@code c}.
     *
     * @param c the character to exclude
     * @return a predicate that matches any character except the given one
     */
    public static CharPredicate notChar(final char c) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char x) {
                return x != c;
            }

            @Override
            public String toString() {
                return "^" + Character.toString(c);
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if the character is within the range {@code [a, b]}.
     *
     * @param a the lower bound of the range (inclusive)
     * @param b the upper bound of the range (inclusive)
     * @return a predicate that matches characters in the given range
     */
    public static CharPredicate range(final char a, final char b) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                return c >= a && c <= b;
            }

            @Override
            public String toString() {
                return "[" + a + '-' + b + "]";
            }
        };
    }

    /** A {@link CharPredicate} that returns true 
     * if the character is a digit. */
    public static final CharPredicate IS_DIGIT = range('0', '9');

    /**
     * A {@link CharPredicate} that returns {@code true} if the character is not within the range {@code [a, b]}.
     *
     * @param a the lower bound of the excluded range (inclusive)
     * @param b the upper bound of the excluded range (inclusive)
     * @return a predicate that matches characters outside the given range
     */
    public static CharPredicate notRange(final char a, final char b) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                return !(c >= a && c <= b);
            }

            @Override
            public String toString() {
                return "[^" + a + '-' + b + "]";
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if the character is equal to any character in {@code chars}.
     *
     * @param chars the string of accepted characters
     * @return a predicate that matches any character contained in the given string
     */
    public static CharPredicate among(final String chars) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                return chars.indexOf(c) >= 0;
            }

            @Override
            public String toString() {
                return '[' + chars + ']';
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if the character is not equal to any character in {@code chars}.
     *
     * @param chars the string of excluded characters
     * @return a predicate that matches any character not contained in the given string
     */
    public static CharPredicate notAmong(final String chars) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                return chars.indexOf(c) < 0;
            }

            @Override
            public String toString() {
                return "^[" + chars + ']';
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if {@code predicate} evaluates to {@code false}.
     *
     * @param predicate the predicate to negate
     * @return a predicate that is the logical negation of the given predicate
     */
    public static CharPredicate not(final CharPredicate predicate) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                return !predicate.isChar(c);
            }

            @Override
            public String toString() {
                return "^" + predicate;
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if both {@code predicate1} and {@code predicate2} evaluate to {@code true}.
     *
     * @param predicate1 the first predicate
     * @param predicate2 the second predicate
     * @return a predicate that is the logical conjunction of the two given predicates
     */
    public static CharPredicate and(final CharPredicate predicate1, final CharPredicate predicate2) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                return predicate1.isChar(c) && predicate2.isChar(c);
            }

            @Override
            public String toString() {
                return predicate1 + " and " + predicate2;
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if either {@code predicate1} or {@code predicate2} evaluates to {@code true}.
     *
     * @param predicate1 the first predicate
     * @param predicate2 the second predicate
     * @return a predicate that is the logical disjunction of the two given predicates
     */
    public static CharPredicate or(final CharPredicate predicate1, final CharPredicate predicate2) {
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                return predicate1.isChar(c) || predicate2.isChar(c);
            }

            @Override
            public String toString() {
                return predicate1 + " or " + predicate2;
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if all predicates in {@code predicates} evaluate to {@code true}.
     *
     * @param predicates the predicates to combine with a logical AND
     * @return a predicate that is the conjunction of all given predicates
     */
    public static CharPredicate and(final CharPredicate... predicates) {
        if (predicates.length == 0) {
            return ALWAYS;
        } else if (predicates.length == 1) {
            return predicates[0];
        }
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                for (int i = 0; i < predicates.length; i++) {
                    if (!predicates[i].isChar(c)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String toString() {
                return StringUtil.join(" and ", predicates);
            }
        };
    }

    /**
     * A {@link CharPredicate} that returns {@code true} if any predicate in {@code predicates} evaluates to {@code true}.
     *
     * @param predicates the predicates to combine with a logical OR
     * @return a predicate that is the disjunction of all given predicates
     */
    public static CharPredicate or(final CharPredicate... predicates) {
        if (predicates.length == 0) {
            return NEVER;
        } else if (predicates.length == 1) {
            return predicates[0];
        }
        return new CharPredicate() {
            @Override
            public boolean isChar(char c) {
                for (int i = 0; i < predicates.length; i++) {
                    if (predicates[i].isChar(c)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString() {
                return StringUtil.join(" or ", predicates);
            }
        };
    }
    
}
