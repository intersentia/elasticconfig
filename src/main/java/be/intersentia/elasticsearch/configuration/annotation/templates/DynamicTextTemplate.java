package be.intersentia.elasticsearch.configuration.annotation.templates;

import be.intersentia.elasticsearch.configuration.annotation.mapping.TextMapping;
import be.intersentia.elasticsearch.configuration.parser.mapping.TemplateParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.mapping.TextMappingParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines a dynamic template with a Text mapping in ElasticSearch.
 * @see DynamicTemplate
 * @see TextMapping
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@TemplateParserConfiguration(parser = TextMappingParser.class)
public @interface DynamicTextTemplate {
    DynamicTemplate template();
    TextMapping mapping();
}