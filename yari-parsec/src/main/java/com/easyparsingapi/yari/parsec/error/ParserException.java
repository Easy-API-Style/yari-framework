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
package com.easyparsingapi.yari.parsec.error;

import com.easyparsingapi.yari.parsec.ParseTree;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.location.SourceLocation.Position;

/**
 * Is thrown when any grammar error happens or any exception is thrown during parsing.
 */
public class ParserException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
	/** The parse error detail. */
	private final ParseErrorDetail error;
    /** The position in the source where the error occurred. */
    private final Position location;
    /** The parse tree at the error location, if available. */
    private ParseTree parseTree = null;

    /**
     * Creates a {@link ParserException} object.
     * 
     * @param details  the {@link ParseErrorDetail} that describes the error details.
     * @param location the error location.
     */
    public ParserException(ParseErrorDetail details,
    		               Position location) {
        super(toErrorMessage(null, details, location));
        this.error = details;
        this.location = location;
    }

    /**
     * Creates a {@link ParserException} object.
     * 
     * @param cause    the exception that causes this.
     * @param details  the {@link ParseErrorDetail} that describes the error details.
     * @param location the location.
     */
    public ParserException(Throwable cause, 
                           ParseErrorDetail details,
                           Position location) {
        super(toErrorMessage(cause.getMessage(), details, location), cause);
        this.error = details;
        this.location = location;
    }

    /**
     * Returns the detailed description of the error, or {@code null} if none.
     *
     * @return the {@link ParseErrorDetail} associated with this exception, or {@code null} if not available
     */
    public ParseErrorDetail getErrorDetail() {
        return error;
    }

    /**
     * Returns the parse tree until the parse error happened,
     * when {@link Parser#parseTree parseTree()} was invoked.
     * {@code null} if absent.
     *
     * @return the partial {@link ParseTree} captured at the point of failure, or {@code null} if not available
     */
    public ParseTree getParseTree() {
        return parseTree;
    }

    /**
     * Sets the partial parse tree captured up to the point where the error occurred.
     *
     * @param parseTree the {@link ParseTree} to associate with this exception, may be {@code null}.
     */
    public void setParseTree(ParseTree parseTree) {
        this.parseTree = parseTree;
    }

    private static String toErrorMessage(String message,
                                         ParseErrorDetail details,
                                         Position location) {
        StringBuilder buf = new StringBuilder();
        if (message != null && message.length() > 0) {
            buf.append(message).append('\n');
        }
        buf.append(ErrorReporter.toString(details, location));
        return buf.toString();
    }

    /**
     * Returns the location of the error.
     *
     * @return the {@link Position} in the source where the error occurred
     */
    public Position getLocation() {
        return location;
    }

    /**
     * Returns the line where the error occurred.
     *
     * @return the 1-based line number in the source where the error was detected
     */
    public final int getLine() {
        return location.line();
    }

    /**
     * Returns the column where the error occurred.
     *
     * @return the 1-based column number in the source where the error was detected
     */
    public final int getColumn() {
        return location.column();
    }

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append(ParserException.class.getSimpleName());
		result.append(" [location=");
		result.append(location);
		result.append(", error=");
        result.append(error);
		if (parseTree != null) {
			result.append(", parseTree=");
			result.append(parseTree);
		}
		result.append("]");
		return result.toString();
	}
    
}
