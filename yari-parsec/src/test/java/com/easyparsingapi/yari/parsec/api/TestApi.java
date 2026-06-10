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
package com.easyparsingapi.yari.parsec.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.easyparsingapi.yari.parsec.ApiParser;
import com.easyparsingapi.yari.parsec.Parser;
import com.easyparsingapi.yari.parsec.Parsers;
import com.easyparsingapi.yari.parsec.Scanners;
import com.easyparsingapi.yari.parsec.Terminals;
import com.easyparsingapi.yari.parsec.Token;
import com.easyparsingapi.yari.parsec.Tokens;
import com.easyparsingapi.yari.parsec.Parser.ResultContext;
import com.easyparsingapi.yari.parsec.Tokens.Fragment;
import com.easyparsingapi.yari.parsec.Tokens.Tag;
import com.easyparsingapi.yari.parsec.error.ParserException;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.easyparsingapi.yari.parsec.util.ast.AssertUtil;
import com.easyparsingapi.yari.parsec.util.ast.Ast;
import com.easyparsingapi.yari.parsec.util.ast.Error;

public class TestApi {
	
	static class Identifier implements Ast {
	    
	    String value;
	    SourceLocation sourceLocation;
	    
	    public Identifier(String value) {
            super();
            this.value = value;
        }
	    
	    @Override
	    public List<Ast> astChildren() {
	        return List.of();
	    }

	    @Override
	    public SourceLocation getSourceLocation() {
	        return sourceLocation;
	    }
	    
        @Override
        public void setSourceLocation(SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }

        @Override
        public String toString() {
            return "Identifier [value=" + value + ", sourceLocation=" + sourceLocation + "]";
        }
        
	}
	
	static class Signature implements Ast {

	    List<Ast> parameters;
	    SourceLocation sourceLocation;
	    
        public Signature(List<Ast> parameters) {
            super();
            this.parameters = parameters;
        }
	    
        @Override
        public List<Ast> astChildren() {
            return Ast.childrenAttributes(parameters);
        }

        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }
        
        @Override
        public void setSourceLocation(SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }

        @Override
        public String toString() {
            return "Signature [parameters=" + parameters + ", sourceLocation=" + sourceLocation + "]";
        }
	    
	}
	
	static class Block implements Ast {
	    
	    List<Ast> declarations;
	    SourceLocation sourceLocation;
        
        public Block(List<Ast> declarations) {
            super();
            this.declarations = declarations;
        }

        @Override
        public List<Ast> astChildren() {
            return Ast.childrenAttributes(declarations);
        }

        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }

        @Override
        public void setSourceLocation(SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }

        @Override
        public String toString() {
            return "Block [declarations=" + declarations + ", sourceLocation=" + sourceLocation + "]";
        }
	    
	}
	
	static class Procedure implements Ast {
	    
	    Identifier name;
	    Signature signature;
        Block block;
        SourceLocation sourceLocation;
        
        public Procedure(Identifier name, Signature signature, Block block) {
            super();
            this.name = name;
            this.signature = signature;
            this.block = block;
        }

        @Override
        public List<Ast> astChildren() {
            return Ast.childrenAttributes(name, signature, block);
        }

        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }
        
        @Override
        public void setSourceLocation(SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }

        @Override
        public String toString() {
            return "Procedure ["
                    + "name=" + name + ", "
                    + "signature=" + signature + ", "
                    + "block=" + block + ", "
                    + "sourceLocation=" + sourceLocation 
                    + "]";
        }
        
	}
	
	static class Declaration implements Ast {
	    
	    Identifier type;
	    Identifier name;
	    Identifier value;
	    SourceLocation sourceLocation;
	    
	    public Declaration(Identifier type, Identifier name, Identifier value) {
            super();
            this.type = type;
            this.name = name;
            this.value = value;
        }

        @Override
        public List<Ast> astChildren() {
            return Ast.childrenAttributes(type, name, value);
        }

        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }
        
        @Override
        public void setSourceLocation(SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }

        @Override
        public String toString() {
            return "Declaration ["
                 + "type=" + type + ", "
                 + "name=" + name + ", "
                 + "value=" + value + ", "
                 + "sourceLocation=" + sourceLocation 
                 + "]";
        }
	    
	}
	
   static class Program implements Ast {
        
        List<Ast> statements;
        SourceLocation sourceLocation;
        
        public Program(List<Ast> statements) {
            super();
            this.statements = statements;
        }

        @Override
        public List<Ast> astChildren() {
            return Ast.childrenAttributes(statements);
        }

        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }

        @Override
        public void setSourceLocation(SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }

        @Override
        public String toString() {
            return "Program [statements=" + statements + ", sourceLocation=" + sourceLocation + "]";
        }
        
    }
	
	static Parser<Void> delimiter = Scanners.WHITESPACES.skipMany();
	
	static Parser<Fragment> comment = Scanners.lineComment("//")
	                                          .source()
	                                          .map(v -> Tokens.fragment(v, Tag.COMMENT));
	
	static Terminals terminals = Terminals.operators("=", "{", "}", "(", ")", ";", ",", "*", ";", "?", "#")
                                          .words(Scanners.IDENTIFIER)
                                          .keywords("function", "String", "int")
                                          .build();
	static Parser<?> tokenizer = Parsers.or(comment, terminals.tokenizer());

	// 
	static Parser<Declaration> declaration = 
		Parsers.sequence(Parsers.or(terminals.token("String"), terminals.token("int"))
				                .map(Object::toString)
				                .map(Identifier::new),
				         Terminals.identifier()
				                  .map(Identifier::new),
				         terminals.token("=")
				                  .next(Terminals.identifier().map(Identifier::new)),
				         Declaration::new);
	
	static Parser<Procedure> procedure_1 = 
		Parsers.sequence(terminals.token("function").next(Terminals.identifier().map(Identifier::new)),
		                 Terminals.identifier()
		                          .map(Identifier::new)
		                          .<Ast>cast()
		                          .sepBy(terminals.token(","))
		                          .between(terminals.token("("), terminals.token(")"))
		                          .map(Signature::new),
				         declaration.followedBy(terminals.token(";"))
				                    .<Ast>cast()
				                    .many()
				                    .between(terminals.token("{"), terminals.token("}"))
				                    .map(Block::new),
				         (n, p, b) -> new Procedure(n, p, b));
	
	static Parser<Procedure> procedure_2 = 
	        Parsers.sequence(terminals.token("function")
	                                  .next(Terminals.identifier().map(Identifier::new)),
	                         Terminals.identifier()
	                                  .map(Identifier::new)
	                                  .<Ast>cast()
	                                  .sepByBetween((a, b, c) -> new Error(a.getFailureMessage(), c, b), 
	                                                terminals.token(","),  
	                                                Parsers.never(),
	                                                Parsers.or(terminals.token(";"), 
	                                                           terminals.token("{").peek()),
	                                                terminals.token(","), 
	                                                terminals.token("("),
	                                                terminals.token(")"))
	                                  .map(Signature::new),
	                         declaration.followedBy(terminals.token(";").optional())
	                                    .<Ast>cast()
	                                    .manyBetween((a, b, c) -> new Error(a.getFailureMessage(), c, b), 
	                                                 terminals.token(";"),  
	                                                 Parsers.never(),
	                                                 Parsers.never(),
	                                                 terminals.token("{"), 
	                                                 terminals.token("}"))
	                                    .map(Block::new),
	                         (n, p, b) -> new Procedure(n, p, b));
	
	static Parser<Ast> program = Parsers.or(declaration, procedure_2)
	                                    .followedBy(terminals.token(";").optional());
	
	final ApiParser.Config config = new ApiParser.Config() {

		@Override
		public Function<ResultContext, Object> onMap() {
			return v -> {
				if (v.value() instanceof Ast ast) {
				    ast.setSourceLocation(v.sourceLocation());
				}
				return v.value();
			};
		}
		
	};
	
	@Test
	public void test_01(TestInfo testInfo) {
	    List<String> source = new ArrayList<>();
	    source.add("function toto(p_1, p_2, p_3, p_4) {");
	    source.add("    int bibi = roro;");
	    source.add("    // comment");
	    source.add("    String fifi = lolo;");
	    source.add("}");
		List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
		Procedure actual = ApiParser.parse(procedure_1, tokens, config);
		AssertUtil.assertAst(testInfo, tokens, actual);
	}
	
	@Test
	public void test_02(TestInfo testInfo) {
	    List<String> source = new ArrayList<>();
        source.add("function toto(p_1, p_2, p_3, p_4) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
		Procedure actual = ApiParser.parse(procedure_1, tokenizer, delimiter, config, AssertUtil.toSource(source));
		AssertUtil.assertAst(testInfo, tokens, actual);
	}

    @Test
    public void test_03(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1, p_2, p_3, p_4) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        Procedure actual = ApiParser.parse(procedure_2, tokens, config);
        AssertUtil.assertAst(testInfo, tokens, actual);
    }
    
    @Test
    public void test_04(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1, p_2, p_3, p_4) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        Procedure actual = ApiParser.parse(procedure_2, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAst(testInfo, tokens, actual);
    }

    @Test
    public void test_07(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1, p_2, p_3 p_4) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        Procedure actual = ApiParser.parse(procedure_2, tokens, config);
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }
    
    @Test
    public void test_08(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1, p_2, p_3 p_4 {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        
        try {
            Procedure actual = ApiParser.parse(procedure_2, tokenizer, delimiter, config, AssertUtil.toSource(source));
            fail("Exception Expected!");
        }
        catch (ParserException e) {
            assertEquals("ParserException [location=line 1 column 33, error=EmptyParseError [index=32, encountered={, expected=[], unexpected=or]]",
                         e.toString());
            assertTrue(true);
        }
        catch (Exception e) {
            fail("wrong exception thrown");
        }
    }

    @Test
    public void test_09(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1, p_2 * aa, p_3, p_4) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        Procedure actual = ApiParser.parse(procedure_2, tokens, config);
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }
    
    @Test
    public void test_10(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1, p_2 * aa, p_3, p_4) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        Procedure actual = ApiParser.parse(procedure_2, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }
    
    @Test
    public void test_11(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1, *, p_3, p_4) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        Procedure actual = ApiParser.parse(procedure_2, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_12(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1,, p_4 {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("};");
        source.add("int bibi = roro;");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntilEof(Error::newInstance, 
                                                             terminals.token(";"))
                                                .map(Program::new);
        
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }
    
    @Test
    public void test_13(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro;");
        source.add("int ?");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntil(Error::newInstance, 
                                                          terminals.token(";"), 
                                                          terminals.token("?"), 
                                                          Parsers.never(),
                                                          Parsers.EOF)
                                               .map(Program::new);
        
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_14(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("int  dd ff;");
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro;");
        source.add("int ?");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntil(Error::newInstance, 
                                                          terminals.token(";"), 
                                                          terminals.token("?").peek(), 
                                                          Parsers.never(),
                                                          Parsers.never())
                                               .map(Program::new)
                                               .followedBy(terminals.token("?"));
        
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }
    
    @Test
    public void test_15(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("int  dd ff;");
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro;");
        source.add("int ?");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntilEof(Error::newInstance, 
                                                             terminals.token(";"))
                                               .map(Program::new);
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_16(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("int  dd ff;");
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro;");
        source.add("int ?");
        
        Parser<Program> programParser = program.manyUntil(Error::newInstance, 
                                                          terminals.token(";"), 
                                                          Parsers.never(), 
                                                          terminals.token("?"),
                                                          Parsers.EOF)
                                               .map(Program::new)
                                               .followedBy(terminals.token("?"));
        try {
            Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
            fail("Exception Expected!");
        }
        catch (ParserException e) {
            assertEquals("ParserException [location=line 5 column 6, error=EmptyParseError [index=65, encountered=EOF, expected=[], unexpected=?]]",
                         e.toString());
            assertTrue(true);
        }
        catch (Exception e) {
            fail("wrong exception thrown");
        }
    }

    @Test
    public void test_17(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("int  dd ff;");
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro;");
        source.add("int ?");
        
        Parser<Program> programParser = program.manyUntil(Error::newInstance, 
                                                          terminals.token(";"), 
                                                          Parsers.never(), 
                                                          Parsers.EOF,
                                                          Parsers.never())
                                               .map(Program::new);
        try {
            Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
            fail("Exception Expected!");
        }
        catch (ParserException e) {
            assertEquals("ParserException [location=line 5 column 6, error=EmptyParseError [index=65, encountered=EOF, expected=[], unexpected=EOF]]",
                         e.toString());
            assertTrue(true);
        }
        catch (Exception e) {
            fail("wrong exception thrown");
        }
    }

    @Test
    public void test_18(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("int  dd ff;");
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro;");
        
        Parser<Program> programParser = program.manyUntilEof(Error::newInstance, 
                                                             terminals.token(";"),
                                                             Parsers.EOF)
                                               .map(Program::new);
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_19(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("int  dd ff;");
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro;");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntilEof(Error::newInstance, 
                                                             terminals.token(";"))
                                               .map(Program::new);
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_20(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro;");
        source.add("int ?");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntil(Error::newInstance, 
                                                          terminals.token(";"), 
                                                          terminals.token("?"), 
                                                          Parsers.never(),
                                                          Parsers.EOF)
                                               .map(Program::new);
        
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_21(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String fifi = lolo;");
        source.add("int dd ff;");
        source.add("int bibi = roro");
        source.add("int ?");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntilEof(Error::newInstance, 
                                                             terminals.token(";"))
                                               .map(Program::new);
        
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_22(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String fifi = lolo;");
        source.add("function toto(p_1, p_2 * aa, p_3, p_4) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        source.add("int bibi = roro");
        source.add("int ff = aa");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntilEof(Error::newInstance, 
                                                             terminals.token(";"))
                                               .map(Program::new);
        
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_23(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("function toto(p_1, p_2 * aa, p_3, p_4, #) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        Procedure actual = ApiParser.parse(procedure_2, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_24(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String fifi = lolo;");
        source.add("function toto(p_1, p_2 * aa, p_3, p_4, #) {");
        source.add("    int bibi = roro;");
        source.add("    // comment");
        source.add("    String fifi = lolo;");
        source.add("}");
        source.add("int bibi = roro");
        source.add("int bibi ? roro");
        source.add("int ff = aa");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntilEof(Error::newInstance, 
                                                             terminals.token(";"))
                                               .map(Program::new);
        
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_25(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("int dd ff;");
        source.add("int gg;");
        source.add("int ?");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> programParser = program.manyUntilEof(Error::newInstance, 
                                                             terminals.token(";"))
                                               .map(Program::new);
        
        Program actual = ApiParser.parse(programParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }
    
    @Test
    public void test_26(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String dd= ss");
        source.add("int dd String ff= a");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> declarationParser = declaration.<Ast>cast()
                                                       .catchError(Error::newInstance, 
                                                                   terminals.token("String").peek())
                                                       .many()
                                                       .map(Program::new);
        
        Ast actual = ApiParser.parse(declarationParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }

    @Test
    public void test_27(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String dd= ss");
        source.add("int dd # String ff= a");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> declarationParser = declaration.<Ast>cast()
                                                       .catchError(Error::newInstance, 
                                                                   terminals.token("String").peek(),
                                                                   Parsers.never(),
                                                                   terminals.token("#"))
                                                       .many()
                                                       .map(Program::new);
        try {
            Ast actual = ApiParser.parse(declarationParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
            fail("Exception Expected!");
        }
        catch (ParserException e) {
            assertEquals("ParserException [location=line 2 column 10, error=EmptyParseError [index=23, encountered=String, expected=[], unexpected=#]]",
                         e.toString());
            assertTrue(true);
        }
        catch (Exception e) {
            fail("wrong exception thrown");
        }
    }

    @Test
    public void test_28(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String dd= ss");
        source.add("String dd String ff= a");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> declarationParser = declaration.<Ast>cast()
                                                       .catchError(Error::newInstance, 
                                                                   terminals.token("String").peek())
                                                       .many()
                                                       .map(Program::new);
        
        Ast actual = ApiParser.parse(declarationParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }
    
    @Test
    public void test_29(TestInfo testInfo) {
        List<String> source = new ArrayList<>();
        source.add("String dd= ss;");
        source.add("String dd; String ff= a");
        
        List<Token> tokens = ApiParser.lex(tokenizer, delimiter, AssertUtil.toSource(source));
        
        Parser<Program> declarationParser = declaration.<Ast>cast()
                                                       .catchError(Error::newInstance, 
                                                                   Parsers.or(terminals.token(";"), 
                                                                              terminals.token("String").peek()))
                                                       .followedBy(terminals.token(";").optional())
                                                       .many()
                                                       .map(Program::new);
        
        Ast actual = ApiParser.parse(declarationParser, tokenizer, delimiter, config, AssertUtil.toSource(source));
        AssertUtil.assertAstError(testInfo, tokens, actual);
    }
    
}
