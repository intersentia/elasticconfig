package be.intersentia.elasticsearch.configuration.annotation.templates;

import be.intersentia.elasticsearch.configuration.annotation.mapping.DateMapping;
import be.intersentia.elasticsearch.configuration.parser.DateMappingParser;
import be.intersentia.elasticsearch.configuration.parser.TemplateParserConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines a dynamic template with a Date mapping in ElasticSearch.
 * @see DynamicTemplate
 * @see DateMapping
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@TemplateParserConfiguration(parser = DateMappingParser.class)
public @interface DynamicDateTemplate {
    DynamicTemplate template();
    DateMapping mapping();
}