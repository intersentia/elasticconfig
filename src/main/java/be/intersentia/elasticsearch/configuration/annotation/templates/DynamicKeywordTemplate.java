package be.intersentia.elasticsearch.configuration.annotation.templates;

import be.intersentia.elasticsearch.configuration.annotation.mapping.KeywordMapping;
import be.intersentia.elasticsearch.configuration.parser.KeywordMappingParser;
import be.intersentia.elasticsearch.configuration.parser.TemplateParserConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines a dynamic template with a Keyword mapping in ElasticSearch.
 * @see DynamicTemplate
 * @see KeywordMapping
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@TemplateParserConfiguration(parser = KeywordMappingParser.class)
public @interface DynamicKeywordTemplate {
    DynamicTemplate template();
    KeywordMapping mapping();
}