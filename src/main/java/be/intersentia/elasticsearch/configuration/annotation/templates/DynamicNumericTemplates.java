package be.intersentia.elasticsearch.configuration.annotation.templates;

import be.intersentia.elasticsearch.configuration.parser.mapping.MultipleTemplateParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.mapping.NumericMappingParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple DynamicNumericTemplate annotations.
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MultipleTemplateParserConfiguration(parser = NumericMappingParser.class)
public @interface DynamicNumericTemplates {
    DynamicNumericTemplate[] value() default {};
}