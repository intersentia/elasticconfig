package be.intersentia.elasticsearch.configuration.annotation.templates;

import be.intersentia.elasticsearch.configuration.annotation.mapping.BooleanMapping;
import be.intersentia.elasticsearch.configuration.parser.mapping.BooleanMappingParser;
import be.intersentia.elasticsearch.configuration.parser.mapping.TemplateParserConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines a dynamic template with a Boolean mapping in ElasticSearch.
 * @see DynamicTemplate
 * @see BooleanMapping
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@TemplateParserConfiguration(parser = BooleanMappingParser.class)
public @interface DynamicBooleanTemplate {
    DynamicTemplate template();
    BooleanMapping mapping();
}