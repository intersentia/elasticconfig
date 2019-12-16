package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.JoinMappingParser;
import be.intersentia.elasticsearch.configuration.parser.MappingParserConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The JoinMapping annotation defines parent/child relations within documents of the same index. The relations array
 * defines a set of possible relations within the documents, each relation being a parent name and a child name.
 * For more information see: https://www.elastic.co/guide/en/elasticsearch/reference/6.0/parent-join.html
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MappingParserConfiguration(parser = JoinMappingParser.class)
public @interface JoinMapping {

    /**
     * The field() method allows you to specify the name of the join field.
     */
    String field() default "DEFAULT";

    /**
     * The defined relations.
     */
    Relation[] relations();
}