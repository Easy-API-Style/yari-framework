# yari-parsec

[![Website](https://img.shields.io/badge/website-easyparsingapi.com%2Fyari--parsec-blue)](https://easyparsingapi.com/modules/parsec.html)

[![Release](https://img.shields.io/badge/release-1.0.0-brightgreen)](https://github.com/Easy-API-Style/yari-framework/releases/tag/1.0.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/java-25-orange)](https://openjdk.org/projects/jdk/25/)
[![Maven Central](https://img.shields.io/badge/com.easyparsingapi%3Ayari--parsec%3A1.0.0-blue)](https://central.sonatype.com/artifact/com.easyparsingapi/yari-parsec/1.0.0)

Parser combinator library on which the entire Yari framework is built. Defines a grammar by composing small, well-typed parsers — `Parser<T>` — that scan characters, tokenize, build expressions with precedence/associativity, track source locations, and recover from errors.

---

## Resilient, fast, fully located — always return an AST, never throw

This is the most important design choice of `yari-parsec`, and the reason every parser in the Yari framework (`yari-css-parser`, `yari-html-parser`, `yari-javascript-parser`, `yari-xml-parser`) behaves the same way: **the parse always succeeds, in a single linear pass, with every node positioned in the source**.

> **Resilient** — broken input is captured in typed error nodes inside the AST, never raised as an exception. The parse always returns a usable tree.
>
> **Fast** — there is no global backtracking on failure. The parser commits to a decision, marks the failure locally, synchronizes on a known boundary, and moves on. One pass, minimal allocations.
>
> **Precisely located** — every AST node (real nodes *and* error nodes) carries a precise `SourceLocation` with the exact `(line, column)` of its start and end in the original source. Locations are computed once at lex-time by the `SourceLocator` and threaded through every combinator (`map`, `infixl`, `prefix`, `between`, `catchError`…), so no rule has to thread them manually.

When a grammar rule cannot resolve some part of the input, the parser does **not** throw and does **not** stop. Instead it:

1. **Records what it could not parse** — the exact `[start, end]` span where the grammar combination failed to resolve.
2. **Materializes an explicit error node in the AST** at that position, carrying the `ParseErrorDetail` and the tokens of the failed range.
3. **Resumes parsing** from a synchronization point (a known boundary like `;`, `}`, a closing tag, the next statement…) and continues matching the rest of the input normally.

This contract is built into the parser via `Parser.catchError(...)` (and its companions `between`, `manyUntil`, `manyBetween`, `sepByBetween` with recovery), which take three boundary parsers that drive the recovery loop:

| Boundary | Meaning |
|---|---|
| `retryParsingFrom` | When this matches, retry the failed rule from here (e.g. next statement, next selector, next declaration). |
| `stopParsingAt`    | When this matches, stop the recovery loop and emit the error node — the rule is considered "done with errors". |
| `failParsingAt`    | When this matches, give up and propagate the failure (used for catastrophic markers only). |

### Why this matters

| Benefit | Detail |
|---|---|
| **Resilient** | The parse cannot fail — broken input produces an error node, never an exception. Robust by construction, no try/catch ceremony at call sites. |
| **Fast** | A single linear pass — no global backtracking, no replay of consumed input. Failures synchronize locally on a boundary parser, so the cost of an error stays proportional to the size of the error region, not the size of the file. |
| **Precisely located** | Every node — including error nodes — carries an accurate `(startLine, startColumn) → (endLine, endColumn)` range, ready for IDE highlights, hover tooltips, hyperlinks to source, formatters, source-to-source rewriters, and refactoring tools. |
| **Tooling-friendly** | IDEs, linters, formatters and language servers get a usable, fully-positioned AST even on broken input — no try/catch around every keystroke. |
| **Total coverage** | Every byte of the source is accounted for: either inside a valid AST node or inside an error node. Nothing is silently dropped. |
| **Composability** | Error nodes implement the same `AstNode` contract as real nodes — they walk, stream, serialize and have a `SourceLocation` like any other node. |
| **Reporting** | Error nodes are real nodes — you can `astStream().filter(n -> n instanceof MyError)` to list every problem found, with precise locations. |

### How a Yari parser uses it (typical shape)

```java
// Inside a Yari parser, every list-like rule (statements, declarations,
// selectors, attributes...) is wrapped with catchError-style recovery:

Parser<List<Statement>> statements = statementParser
    .manyUntil(
        /* recover */    (detail, location, tokens) ->
                             new StatementError(detail, location, tokens),
        /* retry from */ statementSeparator,        // ';' — synchronize on the next stmt
        /* stop at    */ Parsers.never(),
        /* fail at    */ Parsers.never(),
        /* until      */ Parsers.EOF);
```

When the body of a statement is unparseable, the parser:
- consumes tokens until it sees `;`,
- builds a `StatementError` node spanning the bad tokens,
- inserts it into the list, and
- continues with the next statement.

The caller still gets `AstResult<MyUnit>` — never a `ParserException`.

### Resilience in action — error nodes via `catchError`

`catchError` is pure `yari-parsec`: it folds a parse failure into a value of *your* own type
instead of throwing, so a list-shaped rule keeps going and returns every item — good ones
and error ones alike. The result type stays whatever your grammar produces (here a `List`).

```java
// A self-contained grammar built entirely with yari-parsec.
// Each item is an integer; an unparseable item becomes an Err
// (via catchError), so the parse returns the whole list and never throws.

sealed interface Item {}
record Num(String text)                                                     implements Item {}
record Err(ParseErrorDetail detail, SourceLocation loc, List<Token> tokens) implements Item {}

Terminals terms = Terminals.operators(",");

Parser<Item> item = Terminals.IntegerLiteral.PARSER
    .<Item>map(Num::new)
    .catchError((detail, loc, tokens) -> new Err(detail, loc, tokens));

List<Item> items = ApiParser.parse(
    item.sepBy(terms.token(",")),
    Parsers.or(terms.tokenizer(), Terminals.IntegerLiteral.TOKENIZER),
    Scanners.WHITESPACES.skipMany(),
    "1, 2, oops, 4");                       // 'oops' is not an integer

// The parse succeeded — report every Err with its precise location.
items.stream()
     .filter(i -> i instanceof Err)
     .map(i -> (Err) i)
     .forEach(err -> System.out.printf("error at line %d:%d → %d:%d%n",
         err.loc().start().line(), err.loc().start().column(),
         err.loc().end().line(),   err.loc().end().column()));
```

### Precise locations, threaded for you

Combinators carry the matched `SourceLocation` along. `mapLocation` hands you the value
together with the exact span it matched — no manual offset bookkeeping.

```java
Terminals terms = Terminals.operators("+");

Parser<String> number = Terminals.IntegerLiteral.PARSER
    .mapLocation((text, loc) -> {
        System.out.printf("number %s at line %d:%d → %d:%d%n",
            text,
            loc.start().line(), loc.start().column(),
            loc.end().line(),   loc.end().column());
        return text;
    });

ApiParser.parse(
    number.sepBy(terms.token("+")),
    Parsers.or(terms.tokenizer(), Terminals.IntegerLiteral.TOKENIZER),
    Scanners.WHITESPACES.skipMany(),
    "10 + 200 + 3");
```

Need the matched text rather than its location? Use `withSource()` → `WithSource<T>` (value +
matched string) or `source()` → the matched substring as a `String`.

**TL;DR** — `yari-parsec` is built around the idea that a parser is a function `source → AST`, never `source → AST | exception`. The presence of error nodes in the tree carries the diagnostics; the structure of the tree carries everything else; the `SourceLocation` on every node lets tools point back to the exact bytes of the original input.

---

## Pinpoint exceptions — when recovery is not in place

The flip side of `catchError`: any rule that is **not** wrapped in error-recovery falls back to the standard combinator behaviour and throws a `ParserException`. This is on purpose — it is what makes designing a parser tractable.

When the exception is thrown, it carries the **exact** location and context where the grammar combination could not be resolved:

```java
try {
    T result = ApiParser.parse(myParser, tokenizer, delim, source);
} catch (ParserException e) {
    int                       line       = e.getLine();         // 1-based
    int                       column     = e.getColumn();       // 1-based
    SourceLocation.Position   position   = e.getLocation();
    ParseErrorDetail          detail     = e.getErrorDetail();

    int          index       = detail.getIndex();           // offset in the source
    String       encountered = detail.getEncountered();     // the actual input at the error
    List<String> expected    = detail.getExpected();        // every alternative the grammar tried
    String       unexpected  = detail.getUnexpected();      // what blocked the match (or null)
    String       message     = detail.getFailureMessage();  // explicit failure msg (or null)
}
```

The default rendered message looks like:

```
line 12 column 7
  expecting: "}", ";", IDENTIFIER
  unexpected: "@"
```

### Why this precision is valuable

| Use case | How the precise exception helps |
|---|---|
| **Designing a new grammar** | When a rule misfires during development, the message tells you exactly which alternative chain failed — you know whether the bug is in the rule itself, in the tokenizer, or in the operator table. |
| **Adding a new construct** | After adding a new keyword/literal/operator, a precise failure on a single fixture pinpoints the missed wiring (forgot `.or(...)`, wrong precedence, missing case) in seconds. |
| **Reproducing a user-reported issue** | A minimal failing input + the `(line, column)` from the exception is enough to add a regression test that locks the fix in place. |
| **Building tooling errors** | Even with full recovery enabled, the error nodes inside the AST carry the same `ParseErrorDetail` — the diagnostics are identical, only the delivery channel differs (exception vs. AST node). |

### Pinpoint mode + DEBUG

For the toughest grammar bugs, combine the precise exception with [DEBUG mode](#debug-mode-and-parsetree). The exception then carries a partial `ParseTree` showing every named rule that ran up to the failure — usually enough to spot the misrouted branch without instrumenting anything.

> ⚠️ **`debugParse` is development-only** — it logs every named rule traversal, which is expensive. Use it while designing a grammar or investigating a specific bug, never in production. (Note: `ApiParser.parseTree(...)` is *not* expensive — it runs like a normal parse and just returns a `ParseTree` instead of the AST.)

```java
try {
    ApiParser.debugParse(myParser, tokenizer, delim, source);
} catch (ParserException e) {
    System.err.println(e.getMessage());                  // pinpoint location
    e.getParseTree().walk(h -> System.err.printf("%s%s [%d..%d]%n",
        "  ".repeat(h.deep()),
        h.parseTree().getName(),
        h.parseTree().getBeginIndex(),
        h.parseTree().getEndIndex()));                   // path leading to the failure
}
```

---

## Table of Contents

- [Architecture](#architecture)
- [ApiParser — entry point](#apiparser--entry-point)
- [Pattern — character-level primitives](#pattern--character-level-primitives)
- [CharPredicate / CharPredicates](#charpredicate--charpredicates)
- [Scanners — character-level parsers](#scanners--character-level-parsers)
- [Parser&lt;T&gt; — combinators](#parsert--combinators)
- [Parsers — static factories](#parsers--static-factories)
- [Terminals — tokenization](#terminals--tokenization)
- [Token / Tokens / TokenMap](#token--tokens--tokenmap)
- [OperatorTable — precedence & associativity](#operatortable--precedence--associativity)
- [Parser.Reference — recursive grammars](#parserreference--recursive-grammars)
- [SourceLocation / SourceLocator](#sourcelocation--sourcelocator)
- [Functors — Map1…Map8, MapInfix, MapOperator](#functors--map1map8-mapinfix-mapoperator)
- [Error handling](#error-handling)
- [Debug mode and ParseTree](#debug-mode-and-parsetree)
- [WithSource](#withsource)

---

## Architecture

```
CharSequence source
        │
        ▼
   Pattern   ───────►  Scanners (character-level Parser<?>)
                            │
                            ▼
                       Terminals  ───►  List<Token>
                                            │
                                            ▼
                                      Parser<T>  ───►  T
```

Two parsing levels coexist:

| Level | Input | Built from |
|---|---|---|
| **Character level** | `CharSequence` | `Pattern` + `Scanners` |
| **Token level** | `List<Token>` | `Terminals` + `Parsers.token(...)` |

Connect them with `Parser.from(tokenizer, delim)` or the high-level [`ApiParser`](#apiparser--entry-point).

---

## ApiParser — entry point

`Parser.parse(...)` and `Parser.from(...)` are **`protected`**. End users do not call them directly — they call **`ApiParser`** static methods, which wire a token parser to a tokenizer + delimiter and run the parse.

```java
import com.easyparsingapi.yari.parsec.ApiParser;

// 1. Parse a CharSequence by composing a token-level parser with a tokenizer + delimiter
T result = ApiParser.parse(
    myTokenParser,                       // Parser<T> running on tokens
    terms.tokenizer(),                   // Parser<?> producing tokens
    Scanners.WHITESPACES.skipMany(),     // Parser<Void> delimiter
    source);                             // String

// 2. With a Config (token inspection / filtering / result mapping)
T result = ApiParser.parse(myTokenParser, tokenizer, delim, config, source);

// 3. Parse a pre-built token list directly
T result = ApiParser.parse(myTokenParser, tokens);
T result = ApiParser.parse(myTokenParser, tokens, config);

// 4. Build a reusable lexer
Parser<List<Token>> lexer = ApiParser.lexer(terms.tokenizer(), delim);
List<Token> tokens = ApiParser.lex(terms.tokenizer(), delim, source);

// 5. Re-tokenize a Token's content (useful for embedded sub-languages)
List<Token> sub = ApiParser.lex(lexer, parentToken);

// 6. Debug parse → returns the result AND captures a ParseTree on failure
T result = ApiParser.debugParse(myTokenParser, tokenizer, delim, source);

// 7. Get a full ParseTree (success or failure)
ParseTree tree = ApiParser.parseTree(myTokenParser, tokenizer, delim, source);
```

### `ApiParser.Config`

Customize the pipeline:

```java
ApiParser.Config config = new ApiParser.Config() {
    @Override
    public void onTokens(SourceLocator locator, List<Token> tokens) {
        // inspect tokens between lex and parse
    }
    @Override
    public List<Token> filter(List<Token> tokens) {
        // default: removes COMMENT tokens
        return tokens;
    }
    @Override
    public Function<Parser.ResultContext, Object> onMap() {
        // intercept and remap every parser result
        return ctx -> ctx.value();
    }
};
```

---

## Pattern — character-level primitives

`Pattern` is the lowest-level building block — a pure character matcher that returns the **match length** or `Pattern.MISMATCH (-1)`. Patterns compose; they convert to scanners with `.toScanner(name)`.

### Built-in constants (`Patterns.*`)

```java
Patterns.NEVER         // never matches
Patterns.ALWAYS        // matches empty input
Patterns.ANY_CHAR      // any single character
Patterns.EOF           // matches end of input
Patterns.RETURN_CARRIAGE
Patterns.ESCAPED       // \X — backslash + any
Patterns.INTEGER
Patterns.STRICT_DECIMAL
Patterns.FRACTION
Patterns.DECIMAL
Patterns.WORD          // identifier-shaped
Patterns.DEC_INTEGER   // non-zero-leading decimal
Patterns.OCT_INTEGER   // 0-leading octal
Patterns.HEX_INTEGER   // 0x… hex
Patterns.SCIENTIFIC_NOTATION
Patterns.REGEXP_PATTERN
Patterns.REGEXP_MODIFIERS
```

### Pattern factories (`Patterns.*`)

```java
Patterns.isChar('a')                    // single char
Patterns.isChar(CharPredicates.IS_DIGIT)
Patterns.range('a', 'z')                // char range
Patterns.among("+-*/")                  // char set
Patterns.string("foo")                  // literal
Patterns.stringCaseInsensitive("if")
Patterns.notString("end")               // matches any char that isn't the start of "end"
Patterns.hasAtLeast(3)                  // at least 3 chars remaining
Patterns.hasExact(5)                    // exactly 5 chars remaining
Patterns.lineComment("//")              // line comment scanner pattern
Patterns.regex(java.util.regex.Pattern.compile("[A-Z]+"))
```

### Pattern combinators

```java
Pattern p = Patterns.isChar('a')
    .next(Patterns.isChar('b'))         // sequence
    .optional()                          // ?
    .many()                              // *
    .many1();                            // +

Pattern p2 = pattern.atLeast(3);         // {3,}
Pattern p3 = pattern.atMost(5);          // {0,5}
Pattern p4 = pattern.times(2, 4);        // {2,4}
Pattern p5 = pattern.times(3);           // {3}
Pattern p6 = pattern.not();              // negation
Pattern p7 = pattern.peek();             // lookahead, length 0 on success
Pattern p8 = pattern.or(other);          // alternative
Pattern p9 = pattern.ifelse(then, alt);  // conditional
```

### Repetition with `CharPredicate`

```java
Patterns.repeat(3, CharPredicates.IS_DIGIT)        // exactly 3 digits
Patterns.atLeast(1, CharPredicates.IS_ALPHA)       // 1+ letters
Patterns.atMost(5, CharPredicates.IS_HEX_DIGIT)    // 0..5 hex
Patterns.many(CharPredicates.IS_WHITESPACE)        // 0..*
Patterns.many1(CharPredicates.IS_LETTER)           // 1..*
Patterns.times(2, 4, CharPredicates.IS_DIGIT)      // {2,4}
```

### Higher-level matchers

```java
Patterns.and(p1, p2, p3)            // all must match — longest wins
Patterns.or(p1, p2, p3)             // first match wins
Patterns.sequence(p1, p2, p3)       // p1 then p2 then p3
Patterns.longest(p1, p2)            // pick longest match
Patterns.shortest(p1, p2)           // pick shortest match
pattern.notContain("end", "stop")   // matches as long as none of these substrings are reached
pattern.until('"', true)            // match up to (and including) escaped delimiter
```

### Pattern → Scanner

```java
Parser<Void> scanner = Patterns.WORD.toScanner("identifier");
```

---

## CharPredicate / CharPredicates

`CharPredicate` is a `char → boolean` test, used by Patterns and Scanners.

### Constants

```java
CharPredicates.NEVER, CharPredicates.ALWAYS
CharPredicates.IS_DIGIT          // 0-9
CharPredicates.IS_HEX_DIGIT      // 0-9 a-f A-F
CharPredicates.IS_UPPER_CASE     // A-Z
CharPredicates.IS_LOWER_CASE     // a-z
CharPredicates.IS_LETTER         // letter (locale-aware)
CharPredicates.IS_ALPHA          // letter or digit
CharPredicates.IS_ALPHA_         // letter, digit, or _
CharPredicates.IS_ALPHA_NUMERIC
CharPredicates.IS_ALPHA_NUMERIC_
CharPredicates.IS_WHITESPACE
CharPredicates.IS_END_LINE       // \n or \r
```

### Factories

```java
CharPredicates.isChar('x')
CharPredicates.notChar('"')
CharPredicates.range('a', 'z')
CharPredicates.notRange('0', '9')
CharPredicates.among("+-*/")
CharPredicates.notAmong(" \t\n")
CharPredicates.not(predicate)
CharPredicates.and(p1, p2, p3)
CharPredicates.or(p1, p2, p3)
```

---

## Scanners — character-level parsers

Pre-built `Parser<?>` instances that recognize lexical fragments. All scanners run on `CharSequence` and return `Void`, `String`, or a structured value.

### Whitespace and comments

| Scanner | Recognizes |
|---|---|
| `Scanners.WHITESPACES` | one or more whitespace chars |
| `Scanners.RETURN_CARRIAGE` | `\r\n` or `\n` |
| `Scanners.ANY_CHAR` | any single character (fails at EOF) |
| `Scanners.JAVA_LINE_COMMENT` | `// …` until end of line |
| `Scanners.JAVA_BLOCK_COMMENT` | `/* … */` |
| `Scanners.SQL_LINE_COMMENT` | `-- …` |
| `Scanners.SQL_BLOCK_COMMENT` | `/* … */` |
| `Scanners.HASKELL_LINE_COMMENT` | `-- …` |
| `Scanners.HASKELL_BLOCK_COMMENT` | `{- … -}` |
| `Scanners.JAVA_DELIMITER` | whitespace + Java comments |
| `Scanners.SQL_DELIMITER` | whitespace + SQL comments |
| `Scanners.HASKELL_DELIMITER` | whitespace + Haskell comments |

### Literals

| Scanner | Recognizes | Returns |
|---|---|---|
| `Scanners.IDENTIFIER` | letter then letter/digit/underscore | `String` |
| `Scanners.INTEGER` | one or more digits | `String` |
| `Scanners.DECIMAL` | `[0-9]+(\.[0-9]+)?` | `String` |
| `Scanners.DEC_INTEGER` | non-zero-leading integer | `String` |
| `Scanners.OCT_INTEGER` | `0` then octal digits | `String` |
| `Scanners.HEX_INTEGER` | `0x` then hex digits | `String` |
| `Scanners.SCIENTIFIC_NOTATION` | `1.23e-4` style | `String` |
| `Scanners.DOUBLE_QUOTE_STRING` | `"…"` with `\` escapes | `String` (includes quotes) |
| `Scanners.SINGLE_QUOTE_STRING` | `'…'` with `''` doubling | `String` (includes quotes) |
| `Scanners.SINGLE_QUOTE_CHAR` | `'.'` C-style char | `String` |

### Factory methods

```java
Scanners.string("->")                            // exact match, Void
Scanners.stringCaseInsensitive("if")             // case-insensitive
Scanners.isChar('=')                             // exact char
Scanners.isChar(CharPredicates.IS_DIGIT)         // predicate-based char
Scanners.notChar('"')                            // any char except '"'
Scanners.among("+-*/")                           // any char in the set
Scanners.notAmong(" \t\n")                       // any char not in the set
Scanners.many(CharPredicates.IS_ALPHA)           // 0..* matching predicate
Scanners.many1(CharPredicates.IS_DIGIT)          // 1..* matching predicate
Scanners.pattern(Patterns.DECIMAL, "decimal")    // wrap a Pattern
Scanners.lineComment("#")                        // custom line comment
Scanners.blockComment("(*", "*)")                // custom block comment
Scanners.nestableBlockComment("/*", "*/")        // nestable block comment
Scanners.quoted('[', ']')                        // [ … ] with content
Scanners.nestedScanner(outer, inner)             // run inner inside outer's match
```

---

## Parser&lt;T&gt; — combinators

`Parser<T>` is the central type. Combinators below return new `Parser<…>` and are non-destructive.

### Sequencing

```java
p1.next(p2)                 // p1 then p2, returns p2's value
p1.next(t -> mkParser(t))   // monadic bind — next parser depends on previous value
p1.followedBy(p2)           // p1 then p2, returns p1's value, discards p2
p1.notFollowedBy(p2)        // p1, then negative lookahead on p2
p1.between(open, close)     // open then p1 then close — keeps p1's value
```

### Transformation

| Combinator | Behavior |
|---|---|
| `p.map(f)` | apply `f` to result, **preserves** source location |
| `p.apply(f)` | apply `f` to result, **replaces** source location with the matched span |
| `p.mapLocation((v, loc) -> …)` | combine the value with its `SourceLocation` |
| `p.retn(value)` | discard `p`'s value, return `value` |
| `p.cast()` | unchecked type cast — use only when you know the actual type |

### Choice / fallback

```java
p1.or(p2, p3, …)            // try p1, then p2, then p3 (no commitment on partial match)
p1.otherwise(fallback)      // run fallback only if p1 matched zero input
Parsers.or(p1, p2, …)       // same as p1.or(...) — up to 9 overloads + Iterable
Parsers.longest(p1, p2, …)  // try all, pick longest match
Parsers.shortest(p1, p2, …) // try all, pick shortest match
```

### Repetition

```java
p.many()                    // 0..*    → Parser<List<T>>
p.many1()                   // 1..*    → Parser<List<T>>
p.atLeast(min)              // min..*  → Parser<List<T>>
p.times(n)                  // exactly n → Parser<List<T>>
p.times(min, max)           // min..max → Parser<List<T>>
p.skipMany()                // 0..*, value discarded → Parser<Void>
p.skipMany1()               // 1..*, value discarded → Parser<Void>
p.skipAtLeast(min)          // → Parser<Void>
p.skipTimes(n)              // → Parser<Void>
p.skipTimes(min, max)       // → Parser<Void>
```

### Separator-based repetition

```java
p.sepBy(sep)                // 0..*    separated by sep
p.sepBy1(sep)               // 1..*    separated by sep
p.endBy(sep)                // 0..*    each terminated by sep
p.endBy1(sep)               // 1..*    each terminated by sep
p.sepEndBy(sep)             // 0..*    separated and optionally terminated
p.sepEndBy1(sep)            // 1..*    separated and optionally terminated
p.until(end)                // collect matches until `end` succeeds (end is not consumed)
```

### Optional

```java
p.optional()                          // returns null if p doesn't match
p.optional(() -> defaultValue)        // supplied default on zero-match
p.asOptional()                        // returns Optional<T>  (Optional.empty() if absent)
```

### Lookahead / backtracking

```java
p.peek()                    // run p without consuming input
p.not()                     // succeed only if p fails — consumes nothing
p.not("identifier")         // same, with a custom name for error messages
p.atomic()                  // make p all-or-nothing — undo any partial consumption on failure
```

### Error labeling

```java
p.label("if-expression")    // names this parser in error messages and ParseTree nodes
```

### Operator-precedence helpers (low-level)

For most cases use [`OperatorTable`](#operatortable--precedence--associativity). These primitives back it:

```java
p.prefix(operatorParser)    // op p
p.postfix(operatorParser)   // p op*  (left recursion safe)
p.infixl(operatorParser)    // p (op p)*   left-associative
p.infixr(operatorParser)    // p (op p)*   right-associative
p.infixn(operatorParser)    // p (op p)?   non-associative
```

### Token-level wrapping

```java
p.token()                   // wrap the matched range in a Token — used inside a tokenizer
p.source()                  // return the matched substring as String
p.withSource()              // return WithSource<T> = (value, matched string)
```

---

## Parsers — static factories

```java
// Constants
Parsers.EOF                          // succeeds at end of input
Parsers.ANY_TOKEN                    // any token at token level
Parsers.ANY_TOKEN_VALUE              // any token's value
Parsers.SOURCE_LOCATION_POSITION     // current position
Parsers.INDEX                        // current char/token index

// Trivial parsers
Parsers.always()                     // always succeeds, returns null
Parsers.never()                      // always fails
Parsers.fail("custom message")       // fail with message
Parsers.constant(() -> value)        // succeed, return supplied value
Parsers.runnable(() -> sideEffect()) // run side effect, succeed
Parsers.runtime(() -> buildParser()) // defer parser construction

// Composition
Parsers.sequence(p1, p2)             // run sequentially — 2 to 5 arg overloads
Parsers.sequence(p1, p2, (a, b) -> …) // sequence + map result with Map2 / Map3 / …
Parsers.list(iterableOfParsers)      // run all, collect List<T>
Parsers.array(p1, p2, …)             // run all, collect Object[]

// Bracketing
Parsers.between(open, body, close)   // open body close → body's value

// Choice — see Parser.or above
Parsers.or(p1, p2, …)                // 2..9 arg overloads + Iterable
Parsers.longest(p1, p2, …)
Parsers.shortest(p1, p2, …)

// Errors
Parsers.expect("comma")              // raises an "expecting <name>" error
Parsers.unexpected("EOF")            // raises an "unexpected <name>" error

// Token-level
Parsers.token(tokenMap)              // succeed if tokenMap maps the next token
Parsers.tokenType(MyTokenType.class, "MyTokenType") // match by Java type

// Conditional
Parsers.parseIf(ctx -> condition(ctx), parser)      // run parser only if predicate holds
```

The `ParsingContext` exposed to `parseIf` contains `SourceLocator`, `SourceContext(source, index)`, and `TokenContext(tokens, index)`.

---

## Terminals — tokenization

A `Terminals` instance describes the operators and keywords of a language, and exposes a `tokenizer()` that turns source into a token stream.

```java
Terminals terms = Terminals.operators("+", "-", "*", "/", "(", ")")
                            .words(Scanners.IDENTIFIER)              // what counts as a "word"
                            .keywords("if", "else", "while")         // case-sensitive keywords
                            .build();

Parser<Void> delim = Scanners.WHITESPACES.skipMany();

// At token level, match by literal name
Parser<Token> plus  = terms.token("+");
Parser<Token> ifKw  = terms.token("if");

// Match a sequence of tokens as one
Parser<Token> arrow = terms.phrase("-", ">");

// The tokenizer
Parser<?> tokenizer = terms.tokenizer();

// Identifier — succeeds on any non-reserved word
Parser<String> ident = Terminals.identifier();

// Reserved word check
Parser<String> reserved = Terminals.RESERVED;
```

Case-insensitive variant:

```java
Terminals terms = Terminals.operators(operators)
                            .words(Scanners.IDENTIFIER)
                            .caseInsensitiveKeywords("select", "from", "where")
                            .build();
```

### Built-in literal tokenizers

These are ready-to-use `Parser<?>` pairs (`TOKENIZER`, `PARSER`) — combine the tokenizer into your lexer, and use the parser at token level.

```java
Terminals.IntegerLiteral.TOKENIZER       Terminals.IntegerLiteral.PARSER    // → String
Terminals.DecimalLiteral.TOKENIZER       Terminals.DecimalLiteral.PARSER    // → String
Terminals.LongLiteral.DEC_TOKENIZER      Terminals.LongLiteral.PARSER       // → Long
Terminals.LongLiteral.OCT_TOKENIZER
Terminals.LongLiteral.HEX_TOKENIZER
Terminals.LongLiteral.TOKENIZER          // any of the three above
Terminals.ScientificNumberLiteral.TOKENIZER  Terminals.ScientificNumberLiteral.PARSER
Terminals.StringLiteral.DOUBLE_QUOTE_TOKENIZER  Terminals.StringLiteral.PARSER  // → String
Terminals.StringLiteral.SINGLE_QUOTE_TOKENIZER
Terminals.CharLiteral.SINGLE_QUOTE_TOKENIZER    Terminals.CharLiteral.PARSER    // → Character
Terminals.Identifier.TOKENIZER           Terminals.Identifier.PARSER        // → String
```

Compose tokenizers with `Parsers.or(...)`:

```java
Parser<?> tokenizer = Parsers.or(
    terms.tokenizer(),
    Terminals.IntegerLiteral.TOKENIZER,
    Terminals.StringLiteral.DOUBLE_QUOTE_TOKENIZER);
```

### Fragment matching by tag

```java
// Parser that matches any token whose value is a Fragment with tag == Tokens.Tag.IDENTIFIER
Parser<String> any = Terminals.fragment(Tokens.Tag.IDENTIFIER);

// Match multiple tags
Parser<String> ident = Terminals.fragment(Tokens.Tag.IDENTIFIER, Tokens.Tag.RESERVED);
```

---

## Token / Tokens / TokenMap

### `Token`

```java
Token t = ...;
int    index     = t.index();          // start offset in source
int    length    = t.length();         // character length
Object value     = t.value();          // logical value (Fragment, String, Long, …)
SourceLocator    = t.sourceLocator();
SourceLocation   = t.sourceLocation(); // computed from index + length

// Extract the Fragment tag (or null)
Object tag = Token.tag(t);
```

### `Tokens.Fragment`

A `Fragment` is the standard token value carrying a textual fragment plus a tag.

```java
Tokens.Fragment frag = Tokens.fragment("foo", Tokens.Tag.IDENTIFIER);
String text = frag.text();
Object tag  = frag.tag();

// Tag enum used by built-in tokenizers
enum Tokens.Tag { RESERVED, IDENTIFIER, INTEGER, DECIMAL, COMMENT }

// Convenience builders
Tokens.reserved("if")
Tokens.identifier("foo")
Tokens.integerLiteral("42")
Tokens.decimalLiteral("3.14")
Tokens.scientificNotation("1.5", "-3")  // → ScientificNotation { significand, exponent }
```

### `TokenMap<T>`

Functional interface — receives a token, returns a value or `null` if the token is unrecognized.

```java
TokenMap<MyValue> map = token -> {
    if (token.value() instanceof MyValue v) return v;
    return null;
};
Parser<MyValue> parser = Parsers.token(map);
```

---

## OperatorTable — precedence & associativity

Build expression parsers without manually wiring `infixl`/`infixr`/`prefix` chains.

```java
import com.easyparsingapi.yari.parsec.functors.MapOperator;
import com.easyparsingapi.yari.parsec.functors.MapInfix;

Terminals terms = Terminals.operators("+", "-", "*", "/", "(", ")");

Parser<Integer> NUMBER = Terminals.IntegerLiteral.PARSER.map(Integer::parseInt);

Parser<Integer> atom = NUMBER.or(
    Parsers.between(terms.token("("), NUMBER, terms.token(")")));

Parser<Integer> expr = new OperatorTable<Integer>()
    .prefix(terms.token("-").retn(MapOperator.map("-", (Integer n) -> -n)), 100)
    .infixl(terms.token("*").retn(MapInfix.map("*", (Integer a, Integer b) -> a * b)), 20)
    .infixl(terms.token("/").retn(MapInfix.map("/", (Integer a, Integer b) -> a / b)), 20)
    .infixl(terms.token("+").retn(MapInfix.map("+", (Integer a, Integer b) -> a + b)), 10)
    .infixl(terms.token("-").retn(MapInfix.map("-", (Integer a, Integer b) -> a - b)), 10)
    .buildMap(atom);

int result = ApiParser.parse(expr,
    Parsers.or(terms.tokenizer(), Terminals.IntegerLiteral.TOKENIZER),
    Scanners.WHITESPACES.skipMany(),
    "2 + 3 * 4");                // → 14
```

**Precedence rules**:
- Higher precedence number = tighter binding.
- For equal precedence, fixity ordering is: **prefix > postfix > left-assoc > non-assoc > right-assoc**.

**API**:

```java
OperatorTable<T>
    .prefix(Parser<MapOperator<T,T,T>>, int prec)
    .postfix(Parser<MapOperator<T,T,T>>, int prec)
    .infixl(Parser<MapInfix<O,T,T,T>>, int prec)
    .infixr(Parser<MapInfix<O,T,T,T>>, int prec)
    .infixn(Parser<MapInfix<O,T,T,T>>, int prec)
    .buildMap(Parser<? extends T> operand);   // → Parser<T>
```

---

## Parser.Reference — recursive grammars

`Parser.Reference<T>` is a mutable cell that lets a grammar reference itself before construction is complete. **Never** introduce left recursion — use `postfix` / `OperatorTable` instead.

```java
Parser.Reference<Expr> ref = Parser.newReference();

Parser<Expr> atom = NUMBER.or(
    Parsers.between(terms.token("("), ref.lazy(), terms.token(")")));

ref.set(new OperatorTable<Expr>()
    .infixl(terms.token("+").retn(MapInfix.map("+", Add::new)), 10)
    .buildMap(atom));

Parser<Expr> parser = ref.get();
```

`ref.lazy()` returns a parser that defers to whatever `ref.set(...)` was given. Forgetting to call `set()` throws at parse time.

---

## SourceLocation / SourceLocator

### `SourceLocation`

```java
SourceLocation loc = node.getSourceLocation();
SourceLocation.Position s = loc.start();    // (line, column) — 1-based
SourceLocation.Position e = loc.end();

int line   = s.line();
int column = s.column();
```

### `SourceLocator`

Built once per parse — converts between offsets and positions, and extracts substrings.

```java
SourceLocator locator = new SourceLocator(source);

Integer offset = locator.locate(new SourceLocation.Position(3, 5));  // offset of line 3, col 5
SourceLocation.Position pos = locator.locate(42);                    // position at offset 42

CharSequence sub = locator.substring(10, 30);                  // by offsets
CharSequence sub = locator.substring(startPos, endPos);        // by positions
CharSequence sub = locator.substring(1, 1, 5, 10);             // line/col → line/col
CharSequence sub = locator.substring(3, 5, 8);                 // from (line, col) over 8 chars
```

### `SourceLocalisable`

Interface implemented by AST nodes — `getSourceLocation()`, `setSourceLocation(...)`, `hasSourceLocation()`, plus `compareTo` (by start position).

---

## Functors — Map1…Map8, MapInfix, MapOperator

Plain functional interfaces — `Map1<A, T>` through `Map8<...>` — used by `Parsers.sequence(...)` and others to combine multiple parser results.

```java
Parsers.sequence(p1, p2, (a, b) -> new Pair<>(a, b));         // Map2
Parsers.sequence(p1, p2, p3, (a, b, c) -> new Triple<>(a,b,c)); // Map3
// ... up to Map8
```

`MapInfix<O, L, R, I>` and `MapOperator<O, V, R>` are the typed shapes accepted by `OperatorTable.infix*` / `prefix` / `postfix`. They capture both the **operator value** and the **mapping**, which is what lets `infixl` / `infixr` track source locations across the whole expression.

```java
MapInfix.map("+", (Integer l, Integer r) -> l + r);
MapOperator.map("-", (Integer v) -> -v);
```

---

## Error handling

### `ParserException`

Thrown by `ApiParser.parse(...)` on failure.

```java
try {
    T result = ApiParser.parse(myParser, tokenizer, delim, source);
} catch (ParserException e) {
    int line   = e.getLine();
    int column = e.getColumn();
    SourceLocation.Position loc = e.getLocation();
    ParseErrorDetail detail = e.getErrorDetail();
    ParseTree partial = e.getParseTree();    // null unless DEBUG / parseTree was used
}
```

### `ParseErrorDetail`

```java
int    where      = detail.getIndex();          // offset
String got        = detail.getEncountered();    // input at the error
List<String> exp  = detail.getExpected();       // expected alternatives
String unexpected = detail.getUnexpected();     // or null
String message    = detail.getFailureMessage(); // explicit fail message or null
```

### `catchError` — produce error nodes inline

Instead of throwing, fold parse errors into a recovery node and keep going.

```java
import com.easyparsingapi.yari.parsec.functors.Map3;

Map3<ParseErrorDetail, SourceLocation, List<Token>, MyNode> recover =
    (detail, location, tokens) -> new MyErrorNode(detail, location, tokens);

Parser<MyNode> resilient = myParser.catchError(recover);

// With explicit retry / stop / fail synchronization parsers
Parser<MyNode> tuned = myParser.catchError(
    recover,
    /* retryParsingFrom */ terms.token(";"),
    /* stopParsingAt    */ terms.token("end"),
    /* failParsingAt    */ terms.token("FATAL"));
```

Companion combinators that use the same error-recovery contract:

```java
p.between(recover, before, after);             // single instance + recovery
p.manyUntil(recover, retryFrom, end);          // many, recovering, until end
p.manyUntilEof(recover);                       // many up to EOF, recovering
p.manyBetween(recover, before, after);         // many inside brackets, recovering
p.sepByBetween(recover, sep, before, after);   // separated list inside brackets
```

---

## Debug mode and ParseTree

There are two distinct tools here — don't confuse them.

| Tool | What it does | Cost | When to use |
|---|---|---|---|
| `ApiParser.parseTree(...)` | Runs the parser like a normal parse, but returns a `ParseTree` (structural view of named rules) **instead of** the AST. | Comparable to a normal `parse(...)` — no significant overhead. | Anytime you need a structural snapshot instead of, or in addition to, the AST. |
| `ApiParser.debugParse(...)` | Runs the parser in `Parser.Mode.DEBUG` — produces the AST **and** attaches a partial `ParseTree` on `ParserException`, **and** logs every named rule traversal. | **Expensive** — verbose tracing/logging on every named rule, including failed alternatives. | **Development only** — designing a new grammar, investigating a specific parsing bug. |

> ⚠️ **`debugParse` is development-only — never run it on production traffic.** The verbose tracing it emits on every named rule (including alternatives that backtrack) defeats the linear-pass performance of `Parser.Mode.PRODUCTION`.
>
> `parseTree`, on the other hand, is fine to call anywhere a normal `parse` would be — it just returns a different shape.

### Why `debugParse` is expensive

| In `PRODUCTION` (used by `parse` and `parseTree`) | In `DEBUG` (used by `debugParse`) |
|---|---|
| Failed alternatives are forgotten the moment they backtrack. | Every named rule entry is traced — including failed alternatives. |
| Minimal logging. | Each rule traversal is logged with depth, indices, and value. |
| One linear pass, suitable for production. | Logging + retained tracing references = significantly slower and heavier. |

### When to use `debugParse`

| Scenario | Use `debugParse`? |
|---|---|
| Production parsing of user content | **No** — use `ApiParser.parse(...)`. |
| High-throughput pipelines, language-server hot paths | **No** — performance regression. |
| Designing a new grammar rule | **Yes** — see the recursion of every named combinator. |
| Investigating a reported parsing bug on a specific input | **Yes** — get the partial tree on the exception. |
| Writing a regression test for a fixed grammar bug | **No** — assert on the AST or the error node instead. |

### Usage

```java
// Get a ParseTree instead of the AST — safe to use anywhere a normal parse runs
ParseTree tree = ApiParser.parseTree(myParser, tokenizer, delim, source);

// Verbose debug parse — development only
try {
    ApiParser.debugParse(myParser, tokenizer, delim, source);
} catch (ParserException e) {
    ParseTree partial = e.getParseTree();
    partial.walk(h -> System.out.printf("%s%s [%d..%d] -> %s%n",
        "  ".repeat(h.deep()),
        h.parseTree().getName(),
        h.parseTree().getBeginIndex(),
        h.parseTree().getEndIndex(),
        h.parseTree().getValue()));
}
```

Only `Parser.label(...)`-named nodes appear in the tree — that's why naming your rules with `.label("if-expression")`, `.label("selector")`, etc. is essential when you want the trace to be readable.

### `ParseTree` API

```java
String name     = tree.getName();
int    begin    = tree.getBeginIndex();
int    end      = tree.getEndIndex();
Object value    = tree.getValue();
List<ParseTree> children = tree.getChildren();

tree.walk(handler -> {
    ParseTree node    = handler.parseTree();
    int       depth   = handler.deep();
    List<ParseTree> parents = handler.parents();
});
```

---

## WithSource

When you need both the parsed value and the matched source string:

```java
Parser<WithSource<Integer>> p = NUMBER.withSource();

WithSource<Integer> ws = ApiParser.parse(p, tokenizer, delim, source);
Integer value  = ws.getValue();
String  text   = ws.getSource();          // never null
```

`Parser.source()` returns only the matched substring (`Parser<String>`).
