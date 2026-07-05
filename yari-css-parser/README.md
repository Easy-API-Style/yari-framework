# yari-css-parser

[![Website](https://img.shields.io/badge/website-easyparsingapi.com%2Fyari--css--parser-blue)](https://easyparsingapi.com/modules/css-parser.html) [![Email](https://img.shields.io/badge/email-easy.api.contact%40gmail.com-D14836)](mailto:easy.api.contact@gmail.com)

[![Release](https://img.shields.io/badge/release-1.0.2-brightgreen)](https://github.com/Easy-API-Style/yari-framework/releases/tag/1.0.2)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/java-25-orange)](https://openjdk.org/projects/jdk/25/)
[![Maven Central](https://img.shields.io/badge/maven--central-com.easyparsingapi%3Ayari--css--parser%3A1.0.2-blue)](https://central.sonatype.com/artifact/com.easyparsingapi/yari-css-parser/1.0.2)

Fault-tolerant CSS parser that produces a typed AST. Supports complete stylesheets, at-rules, complex selectors (pseudo-classes, pseudo-elements, combinators, nth-patterns…) and property values.

---

## Quick Start

### Full stylesheet

```java
String source = """
    body {
        margin: 0;
        font-family: Arial, sans-serif;
    }
    .title {
        color: #333;
        font-size: 1.5rem;
    }
    """;

AstResult<Css> result = CssParser.parseUnit(source);
Css css = result.unit();
```

### Properties only (no selector)

```java
AstResult<Css> result = CssParser.parseProperties("color: red; font-size: 14px;");
Css css = result.unit();
```

---

## AST

### Iterating nodes

```java
Css css = result.unit();

for (CssNode node : css.getNodes()) {
    if (node instanceof RuleSet ruleSet) {
        System.out.println("Selector : " + ruleSet.getSelector());
        System.out.println("Block    : " + ruleSet.getBlock());
    }
    if (node instanceof AtRule atRule) {
        System.out.println("At-rule : " + atRule.getName());
    }
}
```

### Recursive walk

```java
css.walkChildren(handler -> {
    AstNode node = handler.node();
    int depth    = handler.deep();

    if (node instanceof Property property) {
        System.out.println("  ".repeat(depth) + property);
    }
});
```

### Filtered stream

```java
css.astStream()
   .filter(n -> n instanceof ClassSelector)
   .map(n -> (ClassSelector) n)
   .map(ClassSelector::getClassName)
   .forEach(System.out::println);
```

---

## Main AST Node Types

| Type                      | Description                                          |
|---------------------------|------------------------------------------------------|
| `Css`                     | Root — complete stylesheet                           |
| `RuleSet`                 | Selector + declaration block                         |
| `AtRule`                  | At-rule (`@media`, `@keyframes`, `@import`…)        |
| `Property`                | CSS declaration (`font-size: 14px`)                 |
| `Block`                   | Block `{ … }`                                        |
| `ClassSelector`           | Class selector `.title`                              |
| `IdSelector`              | ID selector `#main`                                  |
| `ElementSelector`         | Tag selector `div`                                   |
| `AttributeSelector`       | Attribute selector `[type="text"]`                   |
| `PseudoClassSelector`     | Pseudo-class `:hover`, `:nth-child(2n+1)`            |
| `PseudoElementSelector`   | Pseudo-element `::before`                            |
| `PseudoFunctionSelector`  | Pseudo-function `:not(.hidden)`                      |
| `CombinatorSelector`      | Combinator `>`, `+`, `~`, ` `                        |
| `ListSelector`            | Selector list `h1, h2, h3`                           |
| `Identifier`              | CSS identifier                                       |
| `Literal`                 | Literal value (string, number, color…)               |
| `Function`                | CSS function `rgb(255,0,0)`, `calc(100% - 2rem)`     |
| `Important`               | `!important`                                         |
| `CssComment`              | Comment `/* … */`                                    |
| `CssError`                | Error node (fault-tolerant parsing)                  |

---

## Source Location

```java
AstResult<Css> result = CssParser.parseUnit(source);

result.unit().astStream()
   .filter(n -> n instanceof Property)
   .forEach(n -> {
       SourceLocation loc = n.getSourceLocation();
       System.out.printf("line %d col %d → line %d col %d%n",
           loc.start().line(), loc.start().column(),
           loc.end().line(),   loc.end().column());

       // Extract the source text of the node
       String text = result.substring(loc);
       System.out.println(text);
   });
```

---

## Comments

```java
Css css = result.unit();

// All comments
List<AstComment> comments = css.astComments();

// Comments before a specific node
css.getNodes().forEach(node ->
    css.astCommentsOf(node, AstUnit.Position.before)
       .forEach(c -> System.out.println("before " + node + ": " + c)));
```

---

## Complete Example — Extract all CSS classes

```java
String source = """
    .container > .header:hover {
        background: linear-gradient(to right, #f00, #00f);
    }
    @media (max-width: 768px) {
        .container { flex-direction: column; }
    }
    """;

AstResult<Css> result = CssParser.parseUnit(source);

result.unit()
      .astStream()
      .filter(n -> n instanceof ClassSelector)
      .map(n -> (ClassSelector) n)
      .map(ClassSelector::getClassName)
      .distinct()
      .forEach(System.out::println);
// → container
// → header
```
