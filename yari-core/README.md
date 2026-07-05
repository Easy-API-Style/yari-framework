# yari-core

[![Website](https://img.shields.io/badge/website-easyparsingapi.com%2Fyari--core-blue)](https://easyparsingapi.com/modules/core.html) [![Email](https://img.shields.io/badge/email-easy.api.contact%40gmail.com-D14836)](mailto:easy.api.contact@gmail.com)

[![Release](https://img.shields.io/badge/release-1.0.2-brightgreen)](https://github.com/Easy-API-Style/yari-framework/releases/tag/1.0.2)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/java-25-orange)](https://openjdk.org/projects/jdk/25/)
[![Maven Central](https://img.shields.io/badge/maven--central-com.easyparsingapi%3Ayari--core%3A1.0.2-blue)](https://central.sonatype.com/artifact/com.easyparsingapi/yari-core/1.0.2)

Common interfaces and abstractions shared by all Yari framework modules. This module defines the Abstract Syntax Tree (AST) contract, source location system, and base utilities.

---

## Key Concepts

### `AstNode` — AST Node

The central interface implemented by all AST nodes. Provides tree navigation (parents, children), source location, and polymorphic JSON serialization.

```java
// Walk all descendants
astNode.walkChildren(handler -> {
    AstNode current = handler.node();
    int depth       = handler.deep();
    // stop the walk early
    handler.cancel();
});

// Walk up toward the root
astNode.walkParents(handler -> {
    AstNode ancestor = handler.node();
});

// Depth-first stream of all descendants
astNode.astStream()
       .filter(n -> n instanceof Identifier)
       .forEach(System.out::println);

// Direct children
List<AstNode> children = astNode.astChildren();

// Parent node
AstNode parent = astNode.astParent();

// All ancestors (immediate parent → root)
List<AstNode> ancestors = astNode.parents();
```

---

### `AstUnit` — Node with Comments

Extends `AstNode` for nodes that carry associated comments (file roots, blocks, etc.).

```java
// All comments in the unit
List<AstComment> all = unit.astComments();

// Comments of a child node filtered by position
List<AstComment> before  = unit.astCommentsOf(childNode, Position.before);
List<AstComment> between = unit.astCommentsOf(childNode, Position.between);
List<AstComment> after   = unit.astCommentsOf(childNode, Position.after);
```

**Comment positions:**

| Value     | Description                                            |
|-----------|--------------------------------------------------------|
| `before`  | The comment appears before the node                   |
| `between` | The comment appears between two tokens of the node    |
| `after`   | The comment appears after the node                    |

---

### `AstResult<T>` — Parse Result

Wraps the root AST node and exposes token and source services.

```java
AstResult<Xml> result = XmlParser.parseUnit(source, config);

// Root node
Xml root = result.getNode();

// Tokens
List<Token> tokens     = result.getTokens();
List<Token> nodeTokens = result.getTokensOf(someNode);

// Source
String fullSource = result.getSource();
String excerpt    = result.substring(node.getSourceLocation());

// Offset ↔ line/column conversion
SourceLocation.Position pos = result.getPosition(42);
int offset                  = result.getIndex(pos);
```

---

### `TypeInfo` — Polymorphic Serialization

`TypeInfo` is a marker interface (empty body) carrying three Jackson annotations that every AST node inherits via `AstNode extends TypeInfo`. Together they enable lossless JSON round-tripping of arbitrary AST trees through a single base type, without any custom deserializer.

```java
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(
    use      = JsonTypeInfo.Id.CLASS,        // discriminator = fully-qualified Java class name
    include  = JsonTypeInfo.As.PROPERTY,     // emitted as a JSON property
    property = "@class")                     // …named "@class"
public interface TypeInfo {}
```

**What each annotation does:**

| Annotation | Effect |
|---|---|
| `@JsonTypeInfo(use=CLASS, include=PROPERTY, property="@class")` | Writes the fully-qualified class name into a `"@class"` field on every node. On read, Jackson uses it to instantiate the concrete subtype — so a `List<AstNode>` of mixed concrete types round-trips correctly. |
| `@JsonInclude(NON_EMPTY)` | Skips `null` values, empty strings, empty `Collection`s, empty `Map`s, and absent `Optional`s. Keeps the JSON compact and deterministic — e.g. a `Tag` with no attributes won't emit `"attributes": []`. |
| `@JsonIgnoreProperties(ignoreUnknown=true)` | Tolerates unknown fields on read — forward-compatible: new fields added in a later version of the library don't break old consumers. |

**Why a marker interface and not per-class annotations?**

Every AST node (`AstNode`, `AstUnit`, `AstComment`, `AstError`, every concrete tag/statement/selector class…) extends or implements `TypeInfo` via `AstNode`. The annotations are inherited once and propagate to the entire tree — there is **no class to add `@JsonTypeInfo` to** when you write a new node type. Implement `AstNode` and serialization works.

**Producing JSON:**

```java
ObjectMapper mapper = new ObjectMapper();

AstResult<Xml> result = XmlParser.parseUnit(source, XmlConfig.builder().build());
String json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(result.getNode());
```

```json
{
  "@class": "com.easyparsingapi.yari.parser.xml.ast.Xml",
  "nodes": [
    {
      "@class": "com.easyparsingapi.yari.parser.xml.ast.Tag",
      "head": {
        "@class": "com.easyparsingapi.yari.parser.xml.ast.TagHead",
        "name": { "@class": "com.easyparsingapi.yari.parser.xml.ast.TagName", ... },
        "attributes": [ ... ]
      },
      "body": { "@class": "com.easyparsingapi.yari.parser.xml.ast.TagBody", ... },
      "foot": { "@class": "com.easyparsingapi.yari.parser.xml.ast.TagFoot", ... },
      "sourceLocation": { "start": {"line": 1, "column": 1}, "end": {"line": 3, "column": 7} }
    }
  ]
}
```

Notice:
- The discriminator `"@class"` is the **first** property of each object — concrete type is known before any field is read.
- `sourceLocation` is preserved (it's part of every node).
- A `Tag` with no attributes would omit `"attributes"` entirely (thanks to `NON_EMPTY`).

**Reading JSON back into a typed tree:**

```java
// Read as the base interface — Jackson resolves the concrete class from "@class"
AstNode root = mapper.readValue(json, AstNode.class);

// Same JSON, read directly as the concrete root type
Xml xml = mapper.readValue(json, Xml.class);

// Pattern-match descendants like any other AST
xml.astStream()
   .filter(n -> n instanceof Tag tag && "book".equals(tag.getHead().getName().toString()))
   .forEach(System.out::println);
```

**Caveats:**

- `"@class"` carries fully-qualified Java class names — **the receiver must have the same node classes on its classpath, in the same packages, with compatible field shapes**. Rename or move a node class and existing serialized trees will no longer deserialize.
- `JsonTypeInfo.Id.CLASS` instantiates by reflection. **Never deserialize untrusted input** with this mapper as-is — it's a classic gadget-chain vector. For external/untrusted JSON, configure a `PolymorphicTypeValidator` allowing only `com.easyparsingapi.yari.**` packages, or expose a sanitized DTO layer.
- `NON_EMPTY` means absent fields after a round-trip should be treated as "empty", not "missing" — always use the typed accessors (`astChildren()`, `astComments()`) which return non-null collections rather than reading the JSON directly.
