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
package com.easyparsingapi.yari.parser.javascript.parser;

import static com.easyparsingapi.yari.parsec.Parsers.or;
import static com.easyparsingapi.yari.parsec.Parsers.parseIf;
import static com.easyparsingapi.yari.parsec.Parsers.sequence;
import static com.easyparsingapi.yari.parser.javascript.parser.AtomicParser.identifier;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.assignableValueWithChaining;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.callExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.destructuringArray;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.destructuringObject;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.expression;
import static com.easyparsingapi.yari.parser.javascript.parser.JavascriptConfig.Node.qualifiedExpression;
import static com.easyparsingapi.yari.parser.javascript.parser.TermParser.token;

import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.pattern.Pattern;
import com.easyparsingapi.yari.parsec.pattern.Patterns;
import com.easyparsingapi.yari.parser.javascript.ast.Assignment;
import com.easyparsingapi.yari.parser.javascript.ast.JavascriptNode;
import com.easyparsingapi.yari.parser.javascript.ast.Operator;
import com.easyparsingapi.yari.parser.javascript.ast.Parenthesis;
import com.easyparsingapi.yari.parser.javascript.ast.Yield;
import com.easyparsingapi.yari.parser.javascript.parser.SequenceService.Type;

/**
 * Utility class providing parsers for JavaScript assignment expressions,
 * including simple assignments, compound assignments, chained assignments,
 * and {@code yield} expressions.
 * <p>
 * This class is not instantiable; all members are static.
 * </p>
 */
public class AssignmentParser {

    /** Private constructor — this utility class must not be instantiated. */
    private AssignmentParser() {
    }

     static Parser<Yield> yield(final JavascriptConfig config) {
         return sequence(token("yield").next(token("*").succeeds()), 
                         config.parser(expression).optional(),
                         Yield::new)
                   .label("yield");
     }
     
     static Parser<JavascriptNode> assignableKey(final JavascriptConfig config) {
        return or(config.parser(destructuringArray),
                  config.parser(destructuringObject),
                  config.parser(qualifiedExpression),
                  config.parser(callExpression),
                  identifier())
                .label("assignableKey");
     }
     
     static Parser<JavascriptNode> assignableValueWithChaining(final JavascriptConfig config) {
         return or(chaining(config),
                   assignableValue(config))
                 .label("assignableValueWithChaining");
     }

     static Parser<JavascriptNode> assignableValue(final JavascriptConfig config) {
         return config.parser(expression)
                      .label("assignableValue");
     }

     static Parser<Assignment.Chaining> chaining(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService()
                                  .lookFor(c, 
                                           assignmentOperators, 
                                           SequenceService.breakPattern()), 
                       assignableValue(config)
                            .sepBy1(operator())
                            .acceptIf(v -> v.size() >= 2)
                            .map(Assignment.Chaining::new)
                            .label("chaining"));
    }
     
    static Pattern equalOperator = 
       Patterns.isChar('=')
               .next(Patterns.and(Patterns.isChar('>').not(), 
                                  Patterns.isChar('=').not()));
     
   /** Pattern matching all assignment operator symbols. */
   public static Pattern assignmentOperators =
       Patterns.or(Patterns.string(">>>="),
                   Patterns.string("&gt;&gt;&gt;="),
                   Patterns.string("&#62;&#62;&#62;="),
                   Patterns.string("<<="),
                   Patterns.string("&lt;&lt;="),
                   Patterns.string("&#60&#60&#60;="),
                   Patterns.string(">>="),
                   Patterns.string("&gt;&gt="),
                   Patterns.string("&#62;&#62;="),
                   Patterns.string("&&="),
                   Patterns.string("&amp;&amp;="),
                   Patterns.string("&#38;&#38;="),
                   Patterns.string("||="),
                   Patterns.string("??="),
                   Patterns.string("**="),
                   Patterns.string("+="),
                   Patterns.string("-="),
                   Patterns.string("*="),
                   Patterns.string("/="),
                   Patterns.string("%="),
                   Patterns.string("&="),
                   Patterns.string("&amp;="),
                   Patterns.string("&#38;="),
                   Patterns.string("^="),
                   Patterns.string("|="),
                   /** Field. */
                   equalOperator);
    
    private static Parser<Operator> operator() {
        return or(token(">>>="),
                  token("&gt;&gt;&gt;="),
                  token("&#62;&#62;&#62;="),
                  
                  token("<<="),
                  token("&lt;&lt;="),
                  token("&#60;&#60;&#60;="),
                  
                  token(">>="),
                  token("&gt;&gt;="),
                  token("&#62;&#62;="),
                  
                  token("&&="),
                  token("&amp;&amp;="),
                  token("&#38;&#38;="),
                  
                  token("||="),
                  token("??="),
                  
                  token("**="),
                  token("+="),
                  token("-="),
                  token("*="),
                  token("/="),
                  token("%="),
                  
                  token("&="),
                  token("&amp;="),
                  token("&#38;="),
                  
                  token("^="),
                  token("|="),
                 
                  token("="))
                .map(symbol -> Operator.symbol(symbol));
    }
    
    private static Parser<Parenthesis> assignmentDestructuringObject(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService()
                                  .lookFor(c, Type.parenthesis, equalOperator),
                       sequence(config.parser(destructuringObject),
                                operator(),
                                config.parser(assignableValueWithChaining), 
                                Assignment::new)
                        .between(token("("), token(")"))
                        .map(Parenthesis::new))
                 .label("assignmentDestructuringObject");
    }
    
    private static Parser<Assignment> defaultAssignment(final JavascriptConfig config) {
        return parseIf(c -> config.sequenceService()
                                  .lookFor(c, assignmentOperators),
                       sequence(assignableKey(config), 
                                operator(),
                                config.parser(assignableValueWithChaining), 
                                Assignment::new))
                 .label("assignment");
    }
    
    static Parser<JavascriptNode> assignment(final JavascriptConfig config) {
        return or(assignmentDestructuringObject(config), 
                  defaultAssignment(config))
                .label("assignment").cast();
    }
    
}
