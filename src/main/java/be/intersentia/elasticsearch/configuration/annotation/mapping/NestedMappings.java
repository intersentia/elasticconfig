package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.mapping.MultipleMappingParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.mapping.NestedMappingParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple NestedMapping annotations
 */
@Target({FIELD})
@Retention(RUNTIME)
@MultipleMappingParserConfiguration(parser = NestedMappingParser.class)
public @interface NestedMappings {

    NestedMapping[] value();
}