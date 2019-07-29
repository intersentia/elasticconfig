# elasticconfig
## Introduction
ElasticConfig Mapping Annotations is a Java project which allows you to automatically configure the analysis and mapping of an ElasticSearch index based on annotations.

## Usage
First create an annotated POJO representing your ElasticSearch type:
```java
@Filter(name = "delimiter_catenate", type = Filter.WORD_DELIMITER, properties = {
		@Property(key = "catenate_words", value = "true"),
		@Property(key = "catenate_numbers", value = "true")
})
@CustomAnalyzer(name = "default", tokenizer = Tokenizer.WHITESPACE, filters = {
		Filter.LOWERCASE, "delimiter_catenate", Filter.ASCII_FOLDING, Filter.PORTER_STEM
})
@CustomAnalyzer(name = "reference", tokenizer = Tokenizer.WHITESPACE, filters = {
		Filter.ASCII_FOLDING, Filter.LOWERCASE
})
@TextMapping(field = "search_field", index = true, store = false)
public class LibraryItem implements Serializable {
    public static final String NAME = "LibraryItem";

    @KeywordMapping(index = true, store = true)
    private String publicationId;
    @KeywordMapping(index = true, store = true, copyTo = "search_field")
    private String name;
    @TextMapping(index = false, store = true, analyzer = "reference")
    private String code;
    @TextMapping(index = false, store = false, copyTo = "search_field")
    private String description;

    // getters and setters
}
```

Then, use the AnalysisFactory and MappingFactory to complete your CreateIndexRequest:
```java
class Example {
    public static void main(String[] args) {
        CreateIndexRequest request = new CreateIndexRequest("library");
        request.settings(AnalysisFactory.createAnalysis(LibraryItem.class));
        request.mapping(LibraryItem.NAME, MappingFactory.createMapping(LibraryItem.class, true));
		CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
		if (!response.isAcknowledged) {
			throw new IllegalStateException("Index could not be created");
		}
    }
}
```

Alternatively, you can ask ElasticConfig to automatically create indices for all types in a certain package which are annotated with @Index:
```java
class Example {
    public static void main(String[] args) {
        ConfigurationScanner.scan("be.intersentia.elasticsearch").configure();
    }
}
```