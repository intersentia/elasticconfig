package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.MultipleMappingParser;
import be.intersentia.elasticsearch.configuration.parser.NestedMappingParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple NestedMapping annotations
 */
@Target({FIELD})
@Retention(RUNTIME)
@MultipleMappingParser(NestedMappingParser.class)
public @interface NestedMappings {

    NestedMapping[] value();
}