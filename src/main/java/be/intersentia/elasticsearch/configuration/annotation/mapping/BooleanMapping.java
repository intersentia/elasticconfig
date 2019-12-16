package be.intersentia.elasticsearch.configuration.annotation.mapping;


import be.intersentia.elasticsearch.configuration.parser.BooleanMappingParser;
import be.intersentia.elasticsearch.configuration.parser.MappingParserConfiguration;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines the field as requiring a Boolean mapping in ElasticSearch. A Boolean mapping is used to index
 * a boolean, but can also accept Strings and numbers which are interpreted as either true or false.
 *
 * For more details see: https://www.elastic.co/guide/en/elasticsearch/reference/5.2/boolean.html
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Repeatable(BooleanMappings.class)
@MappingParserConfiguration(parser = BooleanMappingParser.class)
public @interface BooleanMapping {

    /**
     * The field() method allows you to specify the name of the Elastic Search field. When annotating a Java Class you
     * are required to provide a value for this method. When annotating a Java Field this method is optional: by default
     * the value is he same as the name of the annotated Java Field.
     */
    String field() default "DEFAULT";

    /**
     * Individual fields can be boosted automatically — count more towards the relevance score — at query time, with the
     * boost parameter. Accepts a floating point number, defaults to 1.0.
     */
    float boost() default 1.0f;

    /**
     * The copy_to parameter allows you to create custom _all fields. In other words, the values of multiple fields can
     * be copied into a group field, which can then be queried as a single field. For instance, the first_name and
     * last_name fields can be copied to the full_name field.
     */
    String[] copyTo() default {};

    /**
     * Should the field be stored on disk in a column-stride fashion, so that it can later be used for sorting,
     * aggregations, or scripting? Accepts true (default) or false.
     */
    boolean docValues() default true;

    /**
     * Should the field be searchable? Accepts true (default) or false.
     */
    boolean index() default true;

    /**
     * The nullValue parameter allows you to replace explicit null values with the specified value so that it can be
     * indexed and searched. By default null values cannot be indexed or searched.
     */
    String nullValue() default "DEFAULT";

    /**
     * Whether the field value should be stored and retrievable separately from the _source field. Accepts true or
     * false (default).
     */
    boolean store() default false;
}