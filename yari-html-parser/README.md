# yari-html-parser

[![Website](https://img.shields.io/badge/website-easyparsingapi.com%2Fyari--html--parser-blue)](https://easyparsingapi.com/modules/html-parser.html)

[![Release](https://img.shields.io/badge/release-1.0.0-brightgreen)](https://github.com/Easy-API-Style/yari-framework/releases/tag/1.0.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/java-25-orange)](https://openjdk.org/projects/jdk/25/)
[![Maven Central](https://img.shields.io/badge/com.easyparsingapi%3Ayari--html--parser%3A1.0.0-blue)](https://central.sonatype.com/artifact/com.easyparsingapi/yari-html-parser/1.0.0)

[![yari-xml-parser](https://img.shields.io/badge/yari--xml--parser-blue?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxNiAxNiI+PHBhdGggZD0iTTggMUwxIDQuNXY3TDggMTVsNy0zLjV2LTdMOCAxeiIgZmlsbD0ibm9uZSIgc3Ryb2tlPSJ3aGl0ZSIgc3Ryb2tlLXdpZHRoPSIxLjMiLz48cGF0aCBkPSJNMSA0LjVMOCA4bDctMy41TTggOHY3IiBmaWxsPSJub25lIiBzdHJva2U9IndoaXRlIiBzdHJva2Utd2lkdGg9IjEuMyIvPjwvc3ZnPg==)](../yari-xml-parser/README.md)

HTML parser that builds on the XML parser and additionally processes embedded JavaScript and CSS (e.g. `<script>`, `<style>` tags, `onclick`, `style` attributes). The result is a unified AST where each sub-language is parsed into its own tree.

---

## Quick Start

### Default configuration

The default configuration automatically recognizes:
- JS tags: `<script>` (with no `type`, or `type="text/javascript"` / `type="module"`)
- CSS tags: `<style>`
- CSS attributes: `style`
- Unclosed tags are tolerated (`acceptUnclosedTag(true)`)

> To parse JavaScript event attributes such as `onclick`, register them explicitly with
> `javascriptAttribute("onclick")` — see the custom configuration below.

```java
String source = """
    <!DOCTYPE html>
    <html>
      <head>
        <style>
          body { margin: 0; }
        </style>
      </head>
      <body>
        <script>
          const x = 42;
          console.log(x);
        </script>
        <button onclick="alert('hello')">Click</button>
      </body>
    </html>
    """;

AstResult<Html> result = HtmlParser.parseUnit(source, HtmlConfig.defaulConfig());
Html html = result.unit();
```

---

## `HtmlConfig` — Configuration

### Custom configuration

```java
HtmlConfig config = HtmlConfig.builder()
    // Tags whose content is JavaScript
    .javascriptTag("script")
    .javascriptTag("module")
    // Attributes whose value is JavaScript
    .javascriptAttribute("onclick")
    .javascriptAttribute("onchange")
    .javascriptAttribute("onload")
    // Tags whose content is CSS
    .cssTag("style")
    // Attributes whose value is CSS
    .cssAttribute("style")
    // Tags treated as plain text (not parsed)
    .tagAsPlainText("pre")
    .tagAsPlainText("code")
    // Tolerate unclosed tags
    .acceptUnclosedTag(true)
    .build();

AstResult<Html> result = HtmlParser.parseUnit(source, config);
```

---

## AST

`Html` extends `Xml`: all XML structure is available plus HTML-specific nodes.

### Walking the tree

```java
Html html = result.unit();

html.walkChildren(handler -> {
    AstNode node = handler.node();

    if (node instanceof Tag tag) {
        System.out.println(tag.getHead().getName());
    }
    if (node instanceof ScriptTag scriptTag) {
        // Parsed JavaScript content — Script extends Javascript
        Script js = scriptTag.getBody();
        System.out.println("Script with " + js.getNodes().size() + " statements");
    }
    if (node instanceof StyleTag styleTag) {
        // Parsed CSS content — Style extends Css
        Style css = styleTag.getBody();
        System.out.println("Style with " + css.getNodes().size() + " rules");
    }
});
```

### HTML-specific node types

| Type                    | Description                                                  |
|-------------------------|--------------------------------------------------------------|
| `Html`                  | Root — complete HTML document                                |
| `ScriptTag`             | `<script>` tag with embedded JavaScript AST                  |
| `StyleTag`              | `<style>` tag with embedded CSS AST                          |
| `Script`                | JavaScript content of a tag or attribute                     |
| `Style`                 | CSS content of a tag or attribute                            |
| `ScriptAttributeValue`  | JS attribute value (`onclick="..."`) with its JS AST         |
| `StyleAttributeValue`   | CSS attribute value (`style="..."`) with its CSS AST         |

Inherited XML nodes (`Tag`, `TagHead`, `TagBody`, `TagFoot`, `Text`, `XmlComment`, etc.) are also available — see the [yari-xml-parser](../yari-xml-parser/README.md) README.

---

## Source Location

```java
html.astStream()
    .filter(n -> n instanceof ScriptTag)
    .forEach(n -> {
        SourceLocation loc = n.getSourceLocation();
        System.out.printf("Script: line %d → line %d%n",
            loc.start().line(), loc.end().line());
    });
```

---

## Error Recovery — Malformed Pages

The parser is **fault-tolerant** and **never throws** on malformed HTML. Real-world pages
are rarely well-formed, so it always returns a complete, usable tree and marks the broken
spots instead of aborting. It recovers from, among others:

- **Unclosed tags** — an element opened but never closed (e.g. `<li>First<li>Second`, or a bare `<br>` / `<img>`).
- **Closing-only tags** — a closing tag with no matching opening tag (e.g. a stray `</section>`).
- **Broken nesting** — overlapping or mis-ordered tags.

With `acceptUnclosedTag(true)` (the default in `defaulConfig()`) unclosed tags are accepted
silently; any genuinely unrecoverable fragment becomes an `XmlError` node carrying its
source location and a failure message — never an exception.

```java
// A deliberately malformed page:
//  • <li> items are never closed   (unclosed tags)
//  • a stray </section> has no opening tag   (closing-only tag)
String malformed = """
    <ul>
      <li>First
      <li>Second
    </ul>
    </section>
    <p>Trailing text
    """;

// defaulConfig() already sets acceptUnclosedTag(true)
AstResult<Html> result = HtmlParser.parseUnit(malformed, HtmlConfig.defaulConfig());
Html html = result.unit();

// The whole document still parsed into a usable tree — no exception thrown
System.out.println(html.getNodes().size() + " top-level nodes");

// Inspect the recovered error markers
html.astStream()
    .filter(n -> n instanceof XmlError)
    .forEach(n -> System.out.println("error at " + n.getSourceLocation()));
```

> To reject malformed input instead, build a config with `acceptUnclosedTag(false)`:
> unclosed tags then surface as `XmlError` nodes too, so you can validate strictly while
> still never having to catch an exception.

---

## Complete Example — Extract all scripts and styles

```java
String source = """
    <html>
      <head><style>.btn { color: red; }</style></head>
      <body>
        <script>function greet(name) { return "Hello " + name; }</script>
        <button style="font-size:14px" onclick="greet('world')">Go</button>
      </body>
    </html>
    """;

AstResult<Html> result = HtmlParser.parseUnit(source, HtmlConfig.defaulConfig());

// Inline scripts — Script extends Javascript, so getNodes() are JS statements
result.unit().astStream()
    .filter(n -> n instanceof Script)
    .map(n -> (Script) n)
    .forEach(s -> System.out.println("JS: " + s.getNodes().size() + " statements"));

// Inline styles — Style extends Css, so getNodes() are CSS rules
result.unit().astStream()
    .filter(n -> n instanceof Style)
    .map(n -> (Style) n)
    .forEach(s -> System.out.println("CSS: " + s.getNodes().size() + " rules"));
```
