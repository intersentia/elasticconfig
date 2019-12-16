package be.intersentia.elasticsearch.configuration.annotation.templates;

import be.intersentia.elasticsearch.configuration.annotation.mapping.NumericMapping;
import be.intersentia.elasticsearch.configuration.parser.NumericMappingParser;
import be.intersentia.elasticsearch.configuration.parser.TemplateParserConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines a dynamic template with a Numeric mapping in ElasticSearch.
 * @see DynamicTemplate
 * @see NumericMapping
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@TemplateParserConfiguration(parser = NumericMappingParser.class)
public @interface DynamicNumericTemplate {
    DynamicTemplate template();
    NumericMapping mapping();
}