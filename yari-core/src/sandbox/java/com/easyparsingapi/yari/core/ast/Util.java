package com.easyparsingapi.yari.core.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.google.common.base.Strings;

public class Util {

    /*
     * 
     * COLLECTION
     * 
     */
    @SuppressWarnings("unchecked")
    public static <O> List<O> flat(final Collection<?> values) {
        final List<O> result = new ArrayList<>();
        for (final Object value : values) {
            if (value instanceof List<?>) {
                result.addAll(flat((List<?>) value));
            }
            else {
                result.add((O) value);
            }
        }
        return result;
    }
    
    public static Set<String> toStringValue(final Set<Character> values) {
        return values.stream().map(v -> String.valueOf(v)).collect(Collectors.toSet());
    }

    /*
     * 
     * FUNCTION
     * 
     */
    @SafeVarargs
    public static final <V> Function<V, Boolean> and(final Function<V, Boolean>... functions) {
        return value -> {
            boolean result = true;
            for (final Function<V, Boolean> function : functions) {
                if (!function.apply(value)) {
                    result = false;
                    break;
                }
            }
            return result;
        };
    }
    
    @SafeVarargs
    public static final <V> Function<V, Boolean> or(final Function<V, Boolean>... functions) {
        return value -> {
            boolean result = false;
            for (final Function<V, Boolean> function : functions) {
                if (function.apply(value)) {
                    result = true;
                    break;
                }
            }
            return result;
        };
    }
    
    /*
     * 
     * TOKEN
     * 
     */
    public static boolean isStuck(final Token... tokens) {
        return isStuck(CollectionUtil.toList(tokens));
    }
   
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

    public static Object tag(final Token[] tokens, final int index) {
        Object result = null;
        if (index < tokens.length) {
            result = ((Tokens.Fragment) tokens[index].value()).tag();
        }
        return result;
    }

    public static List<String> subList(final Token[] tokens, final int from, final int length) {
        final List<String> result = new ArrayList<>();
        final int to = from + length - 1;
        if (to < tokens.length) {
            for (int i = from; i <= to; i++) {
                result.add(tokens[i].toString());
            }
        }
        return result;
    }
    
}
