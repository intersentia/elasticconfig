package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.MultipleMappingParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.ObjectMappingParser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple ObjectMapping annotations
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MultipleMappingParserConfiguration(parser = ObjectMappingParser.class)
public @interface ObjectMappings {

    ObjectMapping[] value();
}