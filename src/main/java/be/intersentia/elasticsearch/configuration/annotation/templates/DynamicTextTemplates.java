package be.intersentia.elasticsearch.configuration.annotation.templates;

import be.intersentia.elasticsearch.configuration.parser.MultipleTemplateParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.TextMappingParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple DynamicTextTemplate annotations.
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MultipleTemplateParserConfiguration(value = TextMappingParser.class)
public @interface DynamicTextTemplates {
    DynamicTextTemplate[] value() default {};
}