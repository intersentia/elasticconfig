package be.intersentia.elasticsearch.configuration.annotation.templates;

import be.intersentia.elasticsearch.configuration.parser.BooleanMappingParser;
import be.intersentia.elasticsearch.configuration.parser.MultipleTemplateParserConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple DynamicBooleanTemplate annotations.
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MultipleTemplateParserConfiguration(parser = BooleanMappingParser.class)
public @interface DynamicBooleanTemplates {
    DynamicBooleanTemplate[] value() default {};
}