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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.easyparsingapi.yari.parsec.Parser.Mode;
import com.easyparsingapi.yari.parsec.Parser.ResultContext;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.Tokens.Tag;
import com.easyparsingapi.yari.parsec.location.SourceLocalisable;
import com.easyparsingapi.yari.parsec.location.SourceLocator;

/**
 * Main entry point of the Parsec framework API, exposing static methods
 * for parsing, debug-mode parsing, syntax tree construction, and tokenisation
 * (lexing) of text sources.
 */
public class ApiParser {

    /** Not instantiable — all methods are static. */
    private ApiParser() {}

	/*
	 *
	 * Config
	 *
	 */
	private static Config DEFAULT_CONFIG = new Config() {};

	/**
	 * Returns the default configuration used when no explicit configuration
	 * is supplied to the parsing and lexing methods.
	 *
	 * @return the default {@link Config} instance
	 */
	public static Config defaultConfig() {
		return DEFAULT_CONFIG;
	}

	/**
	 * Configuration interface for the parsing pipeline, allowing customisation
	 * of token processing (filtering, mapping, callback) before and after analysis.
	 */
	public static interface Config {

		/**
		 * Callback invoked after tokenisation, allowing inspection or tracing
		 * of the token list produced by the lexer.
		 *
		 * @param sourceLocator the source locator associated with the analysed text
		 * @param tokens        the list of tokens produced by the lexer
		 */
		public default void onTokens(final SourceLocator sourceLocator,
				                     final List<Token> tokens) {
		}

		/**
		 * Returns the transformation function applied to each parsing result.
		 * By default, if the result implements {@link SourceLocalisable}, its source
		 * location is automatically populated.
		 *
		 * @return a function transforming a {@link ResultContext} into the final value
		 */
		public default Function<ResultContext, Object> onMap() {
		    return c -> {
	            if (c.value() instanceof SourceLocalisable sourceLocalisable) {
	                sourceLocalisable.setSourceLocation(c.sourceLocation());
	            }
	            return c.value();
	        };
		}

		/**
		 * Filters the token list before parsing by removing tokens of type
		 * comment ({@link Tag#COMMENT}).
		 *
		 * @param tokens the raw list of tokens produced by the lexer
		 * @return a new token list with comments removed
		 */
		public default List<Token> filter(final List<Token> tokens) {
			final List<Token> result = new ArrayList<>();
			for (final Token token : tokens) {
				boolean doAdd = true;
				if (token.value() instanceof Fragment fragment) {
					if (Tag.COMMENT.equals(fragment.tag())) {
						doAdd = false;
					}
				}
				if (doAdd) {
					result.add(token);
				}
			}
			return result;
		}

	}

	/*
     *
     * DEBUG PARSER
     *
     */

	/**
	 * Parses the source in debug mode using a tokenizer and a delimiter,
	 * with the supplied configuration. Debug mode produces detailed traces
	 * of the parsing process.
	 *
	 * @param <T>       the type of the result produced by the parser
	 * @param parser    the main parser to apply on the tokens
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @param config    the parsing pipeline configuration
	 * @param source    the source string to parse
	 * @return the parsing result of type {@code T}
	 */
    public static <T> T debugParse(Parser<T> parser,
                                   Parser<?> tokenizer,
                                   Parser<Void> delimiter,
                                   Config config,
                                   String source) {
        return parser.from(tokenizer, delimiter, config).parse(source, Mode.DEBUG);
    }

	/**
	 * Parses the source in debug mode using a tokenizer and a delimiter,
	 * with the default configuration.
	 *
	 * @param <T>       the type of the result produced by the parser
	 * @param parser    the main parser to apply on the tokens
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @param source    the source string to parse
	 * @return the parsing result of type {@code T}
	 */
    public static <T> T debugParse(Parser<T> parser,
                                   Parser<?> tokenizer,
                                   Parser<Void> delimiter,
                                   String source) {
        return debugParse(parser, tokenizer, delimiter, DEFAULT_CONFIG, source);
    }

	/**
	 * Parses the source in debug mode using a standalone lexer and an explicit
	 * configuration.
	 *
	 * @param <T>    the type of the result produced by the parser
	 * @param parser the main parser to apply on the tokens
	 * @param lexer  the lexing parser producing a collection of tokens
	 * @param config the parsing pipeline configuration
	 * @param source the source string to parse
	 * @return the parsing result of type {@code T}
	 */
    public static <T> T debugParse(Parser<T> parser,
                                   Parser<? extends Collection<Token>> lexer,
                                   Config config,
                                   String source) {
        return parser.from(lexer, config).parse(source, Mode.DEBUG);
    }

	/**
	 * Parses the source in debug mode using a standalone lexer and the default
	 * configuration.
	 *
	 * @param <T>    the type of the result produced by the parser
	 * @param parser the main parser to apply on the tokens
	 * @param lexer  the lexing parser producing a collection of tokens
	 * @param source the source string to parse
	 * @return the parsing result of type {@code T}
	 */
    public static <T> T debugParse(Parser<T> parser,
                                   Parser<? extends Collection<Token>> lexer,
                                   String source) {
        return debugParse(parser, lexer, DEFAULT_CONFIG, source);
    }

	/*
	 *
	 * PARSER
	 *
	 */

	/**
	 * Parses the source using a tokenizer, a delimiter and an explicit configuration,
	 * and returns the parsing result.
	 *
	 * @param <T>       the type of the result produced by the parser
	 * @param parser    the main parser to apply on the tokens
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @param config    the parsing pipeline configuration
	 * @param source    the source string to parse
	 * @return the parsing result of type {@code T}
	 */
	public static <T> T parse(Parser<T> parser,
			                  Parser<?> tokenizer,
			                  Parser<Void> delimiter,
			                  Config config,
			                  String source) {
		return parser.from(tokenizer, delimiter, config).parse(source);
	}

	/**
	 * Parses the source using a tokenizer and a delimiter, with the default
	 * configuration.
	 *
	 * @param <T>       the type of the result produced by the parser
	 * @param parser    the main parser to apply on the tokens
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @param source    the source string to parse
	 * @return the parsing result of type {@code T}
	 */
	public static <T> T parse(Parser<T> parser,
			                  Parser<?> tokenizer,
			                  Parser<Void> delimiter,
			                  String source) {
		return parse(parser, tokenizer, delimiter, DEFAULT_CONFIG, source);
	}

	/**
	 * Parses the source using a standalone lexer and an explicit configuration.
	 *
	 * @param <T>    the type of the result produced by the parser
	 * @param parser the main parser to apply on the tokens
	 * @param lexer  the lexing parser producing a collection of tokens
	 * @param config the parsing pipeline configuration
	 * @param source the source string to parse
	 * @return the parsing result of type {@code T}
	 */
	public static <T> T parse(Parser<T> parser,
			                  Parser<? extends Collection<Token>> lexer,
			                  Config config,
			                  String source) {
		return parser.from(lexer, config).parse(source);
	}

	/**
	 * Parses the source using a standalone lexer and the default configuration.
	 *
	 * @param <T>    the type of the result produced by the parser
	 * @param parser the main parser to apply on the tokens
	 * @param lexer  the lexing parser producing a collection of tokens
	 * @param source the source string to parse
	 * @return the parsing result of type {@code T}
	 */
	public static <T> T parse(Parser<T> parser,
			                  Parser<? extends Collection<Token>> lexer,
			                  String source) {
		return parse(parser, lexer, DEFAULT_CONFIG, source);
	}

	/**
	 * Parses an already-produced token list with an explicit configuration,
	 * without performing any additional tokenisation.
	 *
	 * @param <T>    the type of the result produced by the parser
	 * @param parser the main parser to apply on the tokens
	 * @param tokens the list of tokens to parse
	 * @param config the parsing pipeline configuration
	 * @return the parsing result of type {@code T}
	 */
	public static <T> T parse(Parser<T> parser,
			                  List<Token> tokens,
			                  Config config) {
		return parser.from(tokens, config).parse(tokens);
	}

	/**
	 * Parses an already-produced token list with the default configuration,
	 * without performing any additional tokenisation.
	 *
	 * @param <T>    the type of the result produced by the parser
	 * @param parser the main parser to apply on the tokens
	 * @param tokens the list of tokens to parse
	 * @return the parsing result of type {@code T}
	 */
	public static <T> T parse(Parser<T> parser,
			                  List<Token> tokens) {
		return parse(parser, tokens, DEFAULT_CONFIG);
	}

	/*
	 *
	 * PARSER TREE
	 *
	 */

	/**
	 * Builds the syntax tree of the source using a tokenizer, a delimiter and an
	 * explicit configuration.
	 *
	 * @param parser    the parser defining the grammar
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @param config    the parsing pipeline configuration
	 * @param source    the source string to parse
	 * @return the syntax tree ({@link ParseTree}) resulting from parsing
	 */
	public static ParseTree parseTree(Parser<?> parser,
			                          Parser<?> tokenizer,
			                          Parser<Void> delimiter,
			                          Config config,
			                          String source) {
		return parser.from(tokenizer, delimiter, config).parseTree(source);
	}

	/**
	 * Builds the syntax tree of the source using a tokenizer and a delimiter,
	 * with the default configuration.
	 *
	 * @param parser    the parser defining the grammar
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @param source    the source string to parse
	 * @return the syntax tree ({@link ParseTree}) resulting from parsing
	 */
	public static ParseTree parseTree(Parser<?> parser,
			                          Parser<?> tokenizer,
			                          Parser<Void> delimiter,
			                          String source) {
		return parseTree(parser, tokenizer, delimiter, DEFAULT_CONFIG, source);
	}

	/**
	 * Builds the syntax tree of the source using a standalone lexer and an explicit
	 * configuration.
	 *
	 * @param parser the parser defining the grammar
	 * @param lexer  the lexing parser producing a collection of tokens
	 * @param config the parsing pipeline configuration
	 * @param source the source string to parse
	 * @return the syntax tree ({@link ParseTree}) resulting from parsing
	 */
	public static ParseTree parseTree(Parser<?> parser,
			                          Parser<? extends Collection<Token>> lexer,
			                          Config config,
			                          String source) {
		return parser.from(lexer, config).parseTree(source);
	}

	/**
	 * Builds the syntax tree of the source using a standalone lexer and the default
	 * configuration.
	 *
	 * @param parser the parser defining the grammar
	 * @param lexer  the lexing parser producing a collection of tokens
	 * @param source the source string to parse
	 * @return the syntax tree ({@link ParseTree}) resulting from parsing
	 */
	public static ParseTree parseTree(Parser<?> parser,
			                          Parser<? extends Collection<Token>> lexer,
			                          String source) {
		return parseTree(parser, lexer, DEFAULT_CONFIG, source);
	}

	/**
	 * Builds the syntax tree from an already-produced token list with an explicit
	 * configuration.
	 *
	 * @param parser the parser defining the grammar
	 * @param tokens the list of tokens to parse
	 * @param config the parsing pipeline configuration
	 * @return the syntax tree ({@link ParseTree}) resulting from parsing
	 */
	public static ParseTree parseTree(Parser<?> parser,
			                          List<Token> tokens,
			                          Config config) {
		return parser.from(tokens, config).parseTree(tokens);
	}

	/**
	 * Builds the syntax tree from an already-produced token list with the default
	 * configuration.
	 *
	 * @param parser the parser defining the grammar
	 * @param tokens the list of tokens to parse
	 * @return the syntax tree ({@link ParseTree}) resulting from parsing
	 */
	public static ParseTree parseTree(Parser<?> parser,
			                          List<Token> tokens) {
		return parseTree(parser, tokens, DEFAULT_CONFIG);
	}

    /*
     *
     * LEXER
     *
     */

	/**
	 * Creates a lexing parser (lexer) from a tokenizer and a delimiter, allowing
	 * the lexer to be reused across multiple analyses.
	 *
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @return a parser producing a {@link List} of {@link Token}
	 */
    public static Parser<List<Token>> lexer(Parser<?> tokenizer,
                                            Parser<Void> delimiter) {
        return tokenizer.lexer(delimiter);
    }

	/**
	 * Tokenises the source string using a tokenizer and a delimiter, and returns
	 * the resulting token list.
	 *
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @param source    the source string to tokenise
	 * @return the list of {@link Token} produced by the lexer
	 */
	public static List<Token> lex(Parser<?> tokenizer,
                                  Parser<Void> delimiter,
                                  String source) {
		return tokenizer.lexer(delimiter).parse(source);
	}

	/**
	 * Tokenises the source string using an already-built standalone lexer, and
	 * returns the resulting token list.
	 *
	 * @param lexer  the lexing parser producing a list of tokens
	 * @param source the source string to tokenise
	 * @return the list of {@link Token} produced by the lexer
	 */
	public static List<Token> lex(Parser<List<Token>> lexer,
			                      String source) {
		return lexer.parse(source);
	}

	/**
	 * Tokenises the text content of an existing token using a tokenizer and a
	 * delimiter, adjusting the indices of the produced tokens so that they
	 * correspond to the parent token's position in the original source.
	 *
	 * @param tokenizer the tokenisation parser transforming the source into tokens
	 * @param delimiter the parser defining the separators to ignore between tokens
	 * @param token     the token whose text content is to be re-tokenised
	 * @return the list of {@link Token} produced with corrected indices
	 */
    public static List<Token> lex(Parser<?> tokenizer,
                                  Parser<Void> delimiter,
                                  Token token) {
        List<Token> tokens = tokenizer.lexer(delimiter).parse(token.toString());
        return fixTokenIndex(token, tokens);
    }

	/**
	 * Tokenises the text content of an existing token using a standalone lexer,
	 * adjusting the indices of the produced tokens so that they correspond to the
	 * parent token's position in the original source.
	 *
	 * @param lexer the lexing parser producing a list of tokens
	 * @param token the token whose text content is to be re-tokenised
	 * @return the list of {@link Token} produced with corrected indices
	 */
    public static List<Token> lex(Parser<List<Token>> lexer,
                                  Token token) {
        List<Token> tokens = lexer.parse(token.toString());
        return fixTokenIndex(token, tokens);
    }

    private static List<Token> fixTokenIndex(Token token, List<Token> tokens) {
        int startIndex = token.index();
        List<Token> result = new ArrayList<>();
        for (Token t : tokens) {
            result.add(new Token(startIndex + t.index(), t.length(), t.value(), token.sourceLocator()));
        }
        return result;
    }

}
