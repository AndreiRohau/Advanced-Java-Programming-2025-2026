# ws-28-json-xml-2

SAX, StAX, DOM, and JAXB parsing examples that process `employee-example-ns.xml` from resources.

## SAX features

- Namespace-aware SAX parsing (`factory.setNamespaceAware(true)`)
- Secure parser configuration (`FEATURE_SECURE_PROCESSING`)
- Disabling external entities for safer XML handling
- Handling XML attributes (`employee id`)
- Capturing CDATA section events via `LexicalHandler`

## StAX features

- Pull-based parsing with `XMLStreamReader`
- Namespace-aware element handling via `getLocalName()`
- Safe XML input factory configuration (DTD and external entities disabled)
- Attribute extraction (`id` from `employee`)
- Event statistics (`START_ELEMENT`, `END_ELEMENT`, `CHARACTERS`, `CDATA`)

Note: some StAX implementations report CDATA as regular character events.

## DOM features

- Tree-based parsing with `DocumentBuilder`
- Namespace-aware traversal (`getElementsByTagNameNS`)
- Safe parser configuration (secure processing, DTD/entities disabled)
- Attribute extraction (`id` from `employee`)
- Node statistics (`ELEMENT_NODE`, `TEXT_NODE`, `CDATA_SECTION_NODE`)

## JAXB features

- Annotation-driven XML binding with `@XmlRootElement`, `@XmlElement`, and `@XmlAttribute`
- Namespace-aware binding for `employee-example-ns.xml`
- Automatic object graph unmarshalling (XML -> Java DTOs)
- Marshalling preview generation (Java DTOs -> XML)
- Mapping bound DTOs to shared domain model (`uz.itpu.model`)

## Run tests

```cmd
mvn test
```

## Run demo main

```cmd
mvn -q -DskipTests package
java -cp target/classes uz.itpu.pt1.sax.MainSax
java -cp target/classes uz.itpu.pt1.stax.MainStax
java -cp target/classes uz.itpu.pt1.dom.MainDom
java -cp target/classes uz.itpu.pt2.JAXB.MainJaxb
```

