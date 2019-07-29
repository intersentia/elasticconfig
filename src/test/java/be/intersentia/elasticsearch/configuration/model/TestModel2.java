package be.intersentia.elasticsearch.configuration.model;

import be.intersentia.elasticsearch.configuration.annotation.Index;
import be.intersentia.elasticsearch.configuration.annotation.analyzer.*;
import be.intersentia.elasticsearch.configuration.annotation.mapping.KeywordMapping;

@Index(value = "TestModel2", parent = "TestModel")
@Filter(name = "stop_dutch", type = Filter.STOP, properties = {
        @Property(key = "ignore_case", value = "true"),
        @Property(key = "stopwords_path", value="dutch.txt")
})
@CustomAnalyzer(name = "default", tokenizer = Tokenizer.WHITESPACE, filters = {
        Filter.ASCII_FOLDING, "stop_dutch", "stop_french", "delimiter_index",
        Filter.LOWERCASE, "stem_dutch", "stem_french", Filter.PORTER_STEM
})
public class TestModel2 {
    @KeywordMapping(index = true, store = true)
    private String documentId;
    @KeywordMapping(index = true, store = true)
    private String page;
}
