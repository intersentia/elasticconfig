package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.DateMappingParser;
import be.intersentia.elasticsearch.configuration.parser.MultipleMappingParserConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple DateMapping annotations
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MultipleMappingParserConfiguration(parser = DateMappingParser.class)
public @interface DateMappings {

    DateMapping[] value();
}