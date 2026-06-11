# yari-javascript-parser

[![Website](https://img.shields.io/badge/website-easyparsingapi.com%2Fyari--javascript--parser-blue)](https://easyparsingapi.com/modules/javascript-parser.html) [![Email](https://img.shields.io/badge/email-easy.api.contact%40gmail.com-D14836)](mailto:easy.api.contact@gmail.com)

[![Release](https://img.shields.io/badge/release-1.0.0-brightgreen)](https://github.com/Easy-API-Style/yari-framework/releases/tag/1.0.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/java-25-orange)](https://openjdk.org/projects/jdk/25/)
[![Maven Central](https://img.shields.io/badge/maven--central-com.easyparsingapi%3Ayari--javascript--parser%3A1.0.0-blue)](https://central.sonatype.com/artifact/com.easyparsingapi/yari-javascript-parser/1.0.0)

Fault-tolerant JavaScript parser that produces a typed AST. Supports functions, classes, ES6 modules (import/export), async/await, destructuring, template literals, generators, and more.

**Built for speed:** the parser was designed for high throughput and stays fast even on very large scripts (bundles, minified files), with linear scaling and low allocation overhead.

---

## Quick Start

```java
String source = """
    import { readFile } from 'fs/promises';

    const MAX = 100;

    async function process(path) {
        const data = await readFile(path, 'utf8');
        return data.split('\\n').length;
    }

    export default process;
    """;

// Full result including tokens
AstResult<Javascript> result = JavascriptParser.parseUnit(source);
Javascript js = result.unit();

// Simple parse
Javascript js2 = JavascriptParser.parse(source);

// Expression only (parseExpression returns an AstResult<Javascript>)
Javascript expr = JavascriptParser.parseExpression("a + b * c").unit();
```

---

## AST

### Iterating nodes

```java
Javascript js = result.unit();

for (JavascriptNode node : js.getNodes()) {
    switch (node) {
        case Import imp            -> System.out.println("import from: " + imp.getModuleName());
        case VariableDeclaration v -> System.out.println("variable: " + v.getType());
        case FunctionDeclaration f -> System.out.println("function: " + f.getSignature());
        case ClassDeclaration c    -> System.out.println("class: " + c.getClassName());
        case Export e              -> System.out.println("export");
        default                    -> {}
    }
}
```

### Recursive walk

```java
js.walkChildren(handler -> {
    AstNode node = handler.node();
    int depth    = handler.deep();

    if (node instanceof FunctionCall call) {
        System.out.println("  ".repeat(depth) + "call: " + call);
    }
});
```

---

## Main AST Node Types

### Declarations

| Type                     | Description                                       |
|--------------------------|---------------------------------------------------|
| `VariableDeclaration`    | `var`, `let`, `const`                             |
| `FunctionDeclaration`    | `function`, `async function`, `function*`         |
| `ClassDeclaration`       | `class Foo extends Bar { … }`                     |
| `ArrowFunction`          | `(x) => x * 2`                                   |
| `MethodDeclaration`      | Method inside an object or class                  |
| `ClassMethodDeclaration` | Class method (with `static`, `get`, `set`)        |
| `ClassFieldDeclaration`  | Class field `#private = 0`                        |

### Control flow

| Type       | Description                               |
|------------|-------------------------------------------|
| `If`       | `if (cond) { … } else { … }`            |
| `For`      | `for`, `for…in`, `for…of`               |
| `While`    | `while (cond) { … }`                    |
| `DoWhile`  | `do { … } while (cond)`                 |
| `Switch`   | `switch (x) { case … }`                 |
| `Try`      | `try { … } catch (e) { … } finally`     |
| `Return`   | `return value`                            |
| `Break`    | `break [label]`                           |
| `Continue` | `continue [label]`                        |
| `Throw`    | `throw new Error()`                       |

### Expressions

| Type                   | Description                                      |
|------------------------|--------------------------------------------------|
| `Infix`                | Binary expression `a + b`, `a === b`             |
| `Prefix`               | Unary expression `!x`, `typeof x`                |
| `Ternary`              | `cond ? a : b`                                   |
| `Assignment`           | `x = 1`, `x += 1`                               |
| `FunctionCall`         | `foo(a, b)`                                      |
| `QualifiedExpression`  | `obj.prop`, `arr[0]`                             |
| `NewClass`             | `new Foo(args)`                                  |
| `Await`                | `await promise`                                  |
| `Yield`                | `yield value`                                    |
| `Spread`               | `...items`                                       |
| `Sequence`             | `a, b, c`                                        |
| `Parenthesis`          | `(expr)`                                         |

### Literals

| Type               | Description                              |
|--------------------|------------------------------------------|
| `Literal`          | String, number, boolean, regex           |
| `LiteralTemplate`  | Template literal `` `hello ${name}` ``   |
| `ArrayDeclaration` | `[1, 2, 3]`                              |
| `ObjectDeclaration`| `{ key: value }`                         |
| `Null`             | `null`                                   |
| `Undefined`        | `undefined`                              |
| `NaN`              | `NaN`                                    |
| `This`             | `this`                                   |

### Modules

| Type                | Description                                        |
|---------------------|----------------------------------------------------|
| `Import`            | `import { x } from 'module'`                      |
| `Export`            | `export default`, `export { x }`                  |
| `ImportFunctionCall`| Dynamic `import('module')`                        |

### Destructuring

| Type                   | Description                          |
|------------------------|--------------------------------------|
| `DestructuringArray`   | `const [a, b] = arr`                 |
| `DestructuringObject`  | `const { x, y } = obj`              |

---

## Examples

### List imports

```java
Javascript js = JavascriptParser.parse(source);

js.getNodes().stream()
  .filter(n -> n instanceof Import)
  .map(n -> (Import) n)
  .forEach(imp -> {
      System.out.println("module: " + imp.getModuleName());
      if (imp.hasDefault()) {
          System.out.println("  default: " + imp.getDefaultName());
      }
      if (imp.hasImportBlock()) {
          imp.getImportBlock().getImportReferences().forEach(ref ->
              System.out.println("  { " + ref.getName()
                  + (ref.hasAlias() ? " as " + ref.getAlias() : "") + " }"));
      }
  });
```

### List functions

```java
js.astStream()
  .filter(n -> n instanceof FunctionDeclaration)
  .map(n -> (FunctionDeclaration) n)
  .forEach(fn -> {
      String name   = fn.isAnonymous() ? "<anonymous>" : fn.getSignature().toString();
      boolean async = fn.isAsynchronous();
      boolean gen   = fn.isGenerator();
      System.out.printf("%s%s%s%n",
          async ? "async " : "",
          gen   ? "function* " : "function ",
          name);
  });
```

### List variables

```java
js.astStream()
  .filter(n -> n instanceof VariableDeclaration)
  .map(n -> (VariableDeclaration) n)
  .forEach(decl -> {
      String kind = decl.getType().toString(); // var / let / const
      decl.getVariableDeclarations().forEach(v ->
          System.out.printf("%s %s%s%n",
              kind, v.getName(),
              v.isInitialized() ? " = " + v.getValue() : ""));
  });
```

### List classes and their members

```java
js.astStream()
  .filter(n -> n instanceof ClassDeclaration)
  .map(n -> (ClassDeclaration) n)
  .forEach(cls -> {
      System.out.println("class " + cls.getClassName()
          + (cls.isExtended() ? " extends " + cls.getExtendedName() : ""));
      cls.getProperties().forEach(p -> System.out.println("  " + p));
  });
```

---

## Comments

```java
AstResult<Javascript> result = JavascriptParser.parseUnit(source);
Javascript js = result.unit();

// All comments
List<AstComment> all = js.astComments();

// Comments before a node
js.getNodes().forEach(node ->
    js.astCommentsOf(node, AstUnit.Position.before)
      .forEach(c -> System.out.println("before " + node + ": " + c)));
```

---

## Source Location

```java
AstResult<Javascript> result = JavascriptParser.parseUnit(source);

result.unit().astStream()
      .filter(n -> n instanceof FunctionDeclaration)
      .forEach(n -> {
          SourceLocation loc = n.getSourceLocation();
          System.out.printf("function line %d → %d%n",
              loc.start().line(), loc.end().line());
          System.out.println(result.substring(loc));
      });
```
