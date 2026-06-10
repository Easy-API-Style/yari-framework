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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Scanners;
import com.easyparsingapi.yari.parsec.internal.util.Checks;

/**
 * Encapsulates algorithm to recognize certain string pattern.
 * When fed with a character range, a {@link Pattern} object either fails to match,
 * or matches with the match length returned.
 * There is no error reported on where and what exactly failed.
 */
public abstract class Pattern {

    /**
     * Holds the context passed to a rule function, containing the source sequence and the
     * current matching range {@code [begin, end)}.
     *
     * @param src   the full source character sequence.
     * @param begin the index of the first character of the range to match (inclusive).
     * @param end   the index past the last character of the range to match (exclusive).
     */
    public static record Context(CharSequence src, int begin, int end) {}

    /**
     * Returned by {@link #match(CharSequence, int, int)} method when match fails.
     */
    public static final int MISMATCH = -1;

    /** Subclasses only. */
    protected Pattern() {
    }

    /**
     * Matches character range against the pattern.
     * The length of the range is {@code end - begin}.
     *
     * @param src   the source string.
     * @param begin the beginning index in the sequence.
     * @param end   the end index of the source string (exclusive). NOTE: the range is {@code [begin, end)}.
     * @return the number of characters matched. MISMATCH otherwise.
     */
    public abstract int match(CharSequence src, int begin, int end);

    /**
     * Creates a {@link Pattern} from an arbitrary rule function that receives a {@link Context}
     * and returns the number of characters matched, or {@link #MISMATCH} on failure.
     *
     * @param rule a function that accepts the current matching context and returns the match length
     *             or {@link #MISMATCH}.
     * @return a new Pattern backed by the given rule.
     */
    public static Pattern rule(Function<Context, Integer> rule) {
        return new Pattern() {
            @Override
            public int match(CharSequence src, int begin, int end) {
                return rule.apply(new Context(src, begin, end));
            }
        };
    }

    /**
     * Returns a {@link Pattern} object that sequentially matches the character range against {@code this} and then {@code next}.
     * If both succeeds, the entire match length is returned.
     *
     * @param next the next pattern to match.
     * @return the new Pattern object.
     */
    public final Pattern next(Pattern next) {
        return new SequencePattern(this, next);
    }

    /**
     * Returns a {@link Pattern} object that matches with 0 length even if {@code this} mismatches.
     *
     * @return a new Pattern that always succeeds, matching 0 characters on mismatch
     */
    public final Pattern optional() {
        return new OptionalPattern(this);
    }

    /**
     * Returns a {@link Pattern} object that matches this pattern for 0 or more times.
     * The total match length is returned.
     *
     * @return a new Pattern that greedily matches zero or more repetitions of this pattern
     */
    public final Pattern many() {
        return new ManyPattern(this);
    }

    /**
     * Returns {@link Pattern} object that matches this pattern for at least {@code min} times.
     * The total match length is returned.
     *
     * @param min the minimal number of times to match.
     * @return the new Pattern object.
     */
    public final Pattern atLeast(int min) {
        return new LowerBoundedPattern(Checks.checkMin(min), this);
    }

    /**
     * Returns a {@link Pattern} object that matches this pattern for 1 or more times.
     * The total match length is returned.
     *
     * @return a new Pattern that greedily matches one or more repetitions of this pattern
     */
    public final Pattern many1() {
        return atLeast(1);
    }

    /**
     * Returns {@link Pattern} object that matches this pattern for up to {@code max} times.
     * The total match length is returned.
     *
     * @param max the maximal number of times to match.
     * @return the new Pattern object.
     */
    public final Pattern atMost(int max) {
        return new UpperBoundedPattern(Checks.checkMax(max), this);
    }

    /**
     * Returns {@link Pattern} object that matches this pattern
     * for at least {@code min} times and up to {@code max} times.
     * The total match length is returned.
     *
     * @param min the minimal number of times to match.
     * @param max the maximal number of times to match.
     * @return the new Pattern object.
     */
    public final Pattern times(int min, int max) {
        return times(this, min, max);
    }

    /**
     * Returns a {@link Pattern} object that only matches if this pattern mismatches, 0 is returned otherwise.
     *
     * @return a new Pattern that succeeds with match length 0 when this pattern fails, and fails when this pattern succeeds
     */
    public final Pattern not() {
        return new NotPattern(this);
    }

    /**
     * Returns {@link Pattern} object that matches with match length 0 if this Pattern object matches.
     *
     * @return a new Pattern that succeeds with match length 0 without consuming input when this pattern matches
     */
    public final Pattern peek() {
        return new PeekPattern(this);
    }

    /**
     * Returns {@link Pattern} object that, if this pattern matches, matches the
     * remaining input against {@code consequence} pattern, or otherwise matches
     * against {@code alternative} pattern.
     *
     * @param consequence the pattern to apply to the remaining input when this pattern matches
     * @param alternative the pattern to apply when this pattern does not match
     * @return a new Pattern implementing the conditional matching logic
     */
    public final Pattern ifelse(Pattern consequence, Pattern alternative) {
        return ifElse(this, consequence, alternative);
    }

    /**
     * Returns {@link Pattern} object that matches the input against this pattern for {@code n} times.
     *
     * @param n the exact number of times to match this pattern; must be non-negative
     * @return a new Pattern that matches this pattern exactly {@code n} times
     */
    public final Pattern times(int n) {
        return new RepeatPattern(Checks.checkNonNegative(n, "n < 0"), this);
    }

    /**
     * Returns {@link Pattern} object that matches if either {@code this} or {@code p2} matches.
     *
     * @param p2 the alternative pattern to try when this pattern does not match
     * @return a new Pattern that matches if either this pattern or {@code p2} matches
     */
    public final Pattern or(Pattern p2) {
        return new OrPattern(this, p2);
    }

    /**
     * Returns a scanner parser using {@code this} pattern.
     * Convenient short-hand for {@link Scanners#pattern}.
     *
     * @param name the name used to identify this scanner in error messages
     * @return a {@link Parser} that matches input using this pattern and produces no value
     */
    public final Parser<Void> toScanner(String name) {
        return Scanners.pattern(this, name);
    }

    /**
     * Returns a {@link Pattern} that matches like this pattern but truncates the result so that
     * it does not contain any of the given string values. When a forbidden value is found inside
     * the matched region, the match length is reduced to stop just before the first occurrence.
     *
     * @param value the first forbidden substring.
     * @param more  additional forbidden substrings (optional).
     * @return a new Pattern that excludes matches containing the specified values.
     */
    public final Pattern notContain(String value, String... more) {
    	Set<String> values = new HashSet<>();
    	values.add(value);
    	if (more != null) {
    		for (String v : more) {
    			values.add(v);
			}
    	}
    	return notContain(values);
    }

    /**
     * Returns a {@link Pattern} that matches like this pattern but truncates the result so that
     * it does not contain any of the given forbidden substrings. When a forbidden value is found
     * inside the matched region, the match length is reduced to stop just before that value.
     *
     * @param values the set of forbidden substrings.
     * @return a new Pattern that excludes matches containing any of the specified values.
     */
    public final Pattern notContain(Set<String> values) {
    	final Pattern currentPattern = this;
        return new Pattern() {
            @Override
            public int match(CharSequence src, int begin, int end) {
                int result = currentPattern.match(src, begin, end);
                if (result > 0) {
                	String valueResult = src.subSequence(begin, begin + result).toString();
                	for (String value : values) {
                		if (valueResult.contains(value)) {
                			result = Math.min(result, valueResult.indexOf(value));
                		}
                	}
                }
                return result;
            }

        };
    }

    private static Pattern ifElse(final Pattern cond,
                                  final Pattern consequence,
                                  final Pattern alternative) {
        return new Pattern() {
            @Override
            public int match(CharSequence src, int begin, int end) {
                final int conditionResult = cond.match(src, begin, end);
                if (conditionResult == MISMATCH) {
                    return alternative.match(src, begin, end);
                } else {
                    final int consequenceResult = consequence.match(src, begin + conditionResult, end);
                    if (consequenceResult == MISMATCH) {
                        return MISMATCH;
                    } else {
                        return conditionResult + consequenceResult;
                    }
                }
            }
        };
    }

    private static Pattern times(final Pattern pp,
                                 final int min,
                                 final int max) {
        Checks.checkMinMax(min, max);
        return new Pattern() {

            @Override
            public int match(CharSequence src, int begin, int end) {
                int minLen = RepeatPattern.matchRepeat(min, pp, src, end, begin, 0);
                if (MISMATCH == minLen) {
                    return MISMATCH;
                }
                return UpperBoundedPattern.matchSome(max - min, pp, src, end, begin + minLen, minLen);
            }

        };
    }

    /**
     * Returns a {@link Pattern} that first matches this pattern and then consumes characters
     * until the given escape string is encountered in the input.
     *
     * @param escapePattern the string that terminates the match.
     * @return a new Pattern combining this pattern with an {@code until} guard.
     */
    public final Pattern until(final String escapePattern) {
        return new SequencePattern(this, Patterns.until(escapePattern));
    }

    /**
     * Returns a {@link Pattern} that first matches this pattern and then consumes characters
     * until the given escape string is encountered, with optional end-of-file acceptance.
     *
     * @param escapePattern    the string that terminates the match.
     * @param acceptEndOfFile  if {@code true}, reaching end of input is treated as a successful match.
     * @return a new Pattern combining this pattern with an {@code until} guard.
     */
    public final Pattern until(final String escapePattern,
                               final boolean acceptEndOfFile) {
        return new SequencePattern(this, Patterns.until(escapePattern, acceptEndOfFile));
    }


    /**
     * Returns a {@link Pattern} that first matches this pattern and then consumes characters
     * until the given escape character is encountered in the input.
     *
     * @param escapePattern the character that terminates the match.
     * @return a new Pattern combining this pattern with an {@code until} guard.
     */
    public final Pattern until(final char escapePattern) {
        return new SequencePattern(this, Patterns.until(escapePattern));
    }

    /**
     * Returns a {@link Pattern} that first matches this pattern and then consumes characters
     * until the given escape character is encountered, allowing an optional escape character to
     * skip over occurrences of the terminator.
     *
     * @param escapePattern the character that terminates the match.
     * @param escapeChar    the character used to escape the terminator, or {@code null} if no escape is supported.
     * @return a new Pattern combining this pattern with an {@code until} guard.
     */
    public final Pattern until(final char escapePattern,
                               final Character escapeChar) {
        return new SequencePattern(this, Patterns.until(escapePattern, escapeChar));
    }

}
