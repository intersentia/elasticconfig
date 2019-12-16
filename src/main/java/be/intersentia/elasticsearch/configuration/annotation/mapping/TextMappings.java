package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.MultipleMappingParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.TextMappingParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple TextMapping annotations
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MultipleMappingParserConfiguration(parser = TextMappingParser.class)
public @interface TextMappings {

    TextMapping[] value();
}