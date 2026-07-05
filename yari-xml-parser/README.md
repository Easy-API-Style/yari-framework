# yari-xml-parser

[![Website](https://img.shields.io/badge/website-easyparsingapi.com%2Fyari--xml--parser-blue)](https://easyparsingapi.com/modules/xml-parser.html) [![Email](https://img.shields.io/badge/email-easy.api.contact%40gmail.com-D14836)](mailto:easy.api.contact@gmail.com)

[![Release](https://img.shields.io/badge/release-1.0.2-brightgreen)](https://github.com/Easy-API-Style/yari-framework/releases/tag/1.0.2)
[![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0)
[![Java](https://img.shields.io/badge/java-25-orange)](https://openjdk.org/projects/jdk/25/)
[![Maven Central](https://img.shields.io/badge/maven--central-com.easyparsingapi%3Ayari--xml--parser%3A1.0.2-blue)](https://central.sonatype.com/artifact/com.easyparsingapi/yari-xml-parser/1.0.2)

Fault-tolerant XML parser that produces a typed AST. Supports the full XML structure: simple and complex tags (head + body + foot), self-closing tags, comments, CDATA, DOCTYPE, prolog, and namespaces.

---

## Quick Start

```java
String source = """
    <?xml version="1.0" encoding="UTF-8"?>
    <catalog>
        <!-- Available books -->
        <book id="1" lang="fr">
            <title>Le Petit Prince</title>
            <author>Saint-Exupéry</author>
        </book>
        <book id="2" lang="en">
            <title>1984</title>
            <author>Orwell</author>
        </book>
    </catalog>
    """;

AstResult<Xml> result = XmlParser.parseUnit(source, XmlConfig.builder().build());
Xml xml = result.unit();
```

---

## `XmlConfig` — Configuration

```java
XmlConfig config = XmlConfig.builder()
    // Tolerate unclosed tags
    .acceptUnclosedTag(true)
    // Treat the content of a tag as plain text (not parsed)
    .tagAsPlainText("script")
    .tagAsPlainText("pre")
    .build();

AstResult<Xml> result = XmlParser.parseUnit(source, config);
```

---

## AST

### Iterating nodes

```java
Xml xml = result.unit();

for (XmlNode node : xml.getNodes()) {
    switch (node) {
        case Tag tag         -> System.out.println("tag: " + tag.getHead().getName());
        case Text text       -> System.out.println("text: " + text);
        case XmlComment c    -> System.out.println("comment: " + c);
        case Prolog prolog   -> System.out.println("XML prolog");
        case DocType docType -> System.out.println("DOCTYPE");
        case CData cdata     -> System.out.println("CDATA");
        default              -> {}
    }
}
```

### Recursive walk

```java
xml.walkChildren(handler -> {
    AstNode node = handler.node();
    int depth    = handler.deep();

    if (node instanceof Tag tag) {
        System.out.println("  ".repeat(depth) + tag.getHead().getName());
    }
});
```

---

## Main AST Node Types

### Tag structure

| Type              | Description                                                          |
|-------------------|----------------------------------------------------------------------|
| `Xml`             | Root — complete XML document                                         |
| `Tag`             | Complete tag: `<foo>...</foo>`                                       |
| `TagHead`         | Tag opening: `<foo attr="val"`                                      |
| `TagBody`         | Tag body: content between `<foo>` and `</foo>`                      |
| `TagFoot`         | Tag closing: `</foo>`                                                |
| `TagEmpty`        | Self-closing tag: `<br/>`, `<img src="..."/>`                        |
| `TagSimple`       | Tag with no body or attributes                                       |
| `TagComplex`      | Tag with body and/or attributes                                      |
| `TagAttribute`    | Attribute `name="value"`                                             |
| `TagName`         | Tag name, with optional namespace                                    |

### Other nodes

| Type              | Description                                                          |
|-------------------|----------------------------------------------------------------------|
| `Text`            | Text content between tags                                            |
| `XmlComment`      | Comment `<!-- … -->`                                                 |
| `CData`           | CDATA section `<![CDATA[…]]>`                                        |
| `Prolog`          | Prolog `<?xml version="1.0"?>`                                       |
| `DocType`         | Declaration `<!DOCTYPE …>`                                           |
| `Markup`          | Tag or attribute identifier                                          |
| `XmlIdentifier`   | Qualified XML identifier (with namespace)                            |
| `XmlError`        | Error node (fault-tolerant parsing)                                  |

---

## Examples

### Read tag attributes

```java
xml.astStream()
   .filter(n -> n instanceof Tag)
   .map(n -> (Tag) n)
   .forEach(tag -> {
       TagHead head = tag.getHead();
       System.out.println("Tag: " + head.getName());
       head.getAttributes().forEach(attr ->
           System.out.printf("  %s = %s%n", attr.getName(), attr.getValue()));
   });
```

### Find a tag by name

```java
xml.astStream()
   .filter(n -> n instanceof Tag)
   .map(n -> (Tag) n)
   .filter(t -> "book".equals(t.getHead().getName().toString()))
   .forEach(book -> {
       System.out.println("Book found:");
       book.getBody().getNodes().forEach(child ->
           System.out.println("  " + child));
   });
```

### Extract all text content

```java
String fullText = xml.astStream()
    .filter(n -> n instanceof Text)
    .map(Object::toString)
    .collect(Collectors.joining(" "));
```

### Access comments

```java
List<AstComment> comments = xml.astComments();

xml.getNodes().forEach(node ->
    xml.astCommentsOf(node, AstUnit.Position.before)
       .forEach(c -> System.out.println("before <" + node + ">: " + c)));
```

---

## Source Location

```java
AstResult<Xml> result = XmlParser.parseUnit(source, config);

result.unit().astStream()
      .filter(n -> n instanceof Tag)
      .forEach(n -> {
          SourceLocation loc = n.getSourceLocation();
          System.out.printf("tag line %d col %d → line %d col %d%n",
              loc.start().line(), loc.start().column(),
              loc.end().line(),   loc.end().column());
          System.out.println(result.substring(loc));
      });
```

---

## Complete Example — Transform an XML catalogue

```java
String source = """
    <catalog>
        <book id="1"><title>Le Petit Prince</title></book>
        <book id="2"><title>1984</title></book>
    </catalog>
    """;

AstResult<Xml> result = XmlParser.parseUnit(source, XmlConfig.builder().build());

record Book(String id, String title) {}

List<Book> books = result.unit().astStream()
    .filter(n -> n instanceof Tag t
              && "book".equals(t.getHead().getName().toString()))
    .map(n -> (Tag) n)
    .map(tag -> {
        String id = tag.getHead().getAttributes().stream()
            .filter(a -> "id".equals(a.getName().toString()))
            .map(a -> a.getValue().getValue())
            .findFirst().orElse("");
        String title = tag.getBody().getNodes().stream()
            .filter(c -> c instanceof Tag t
                      && "title".equals(t.getHead().getName().toString()))
            .map(c -> ((Tag) c).getBody().getNodes().toString())
            .findFirst().orElse("");
        return new Book(id, title);
    })
    .toList();

books.forEach(System.out::println);
```
