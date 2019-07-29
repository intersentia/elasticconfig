package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.mapping.MappingParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.mapping.NumericMappingParser;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines the field as requiring a Numeric mapping in ElasticSearch. A Numeric mapping is used to index
 * a variety of numbers.
 *
 * For more details see: https://www.elastic.co/guide/en/elasticsearch/reference/5.2/number.html
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Repeatable(NumericMappings.class)
@MappingParserConfiguration(parser = NumericMappingParser.class)
public @interface NumericMapping {

    /**
     * The field() method allows you to specify the name of the Elastic Search field. When annotating a Java Class you
     * are required to provide a value for this method. When annotating a Java Field this method is optional: by default
     * the value is he same as the name of the annotated Java Field.
     */
    String field() default "DEFAULT";

    /**
     * The type of Numeric mapping.
     */
    NumericType type() default NumericType.DEFAULT;

    /**
     * Try to convert strings to numbers and truncate fractions for integers. Accepts true (default) and false.
     */
    boolean coerce() default true;

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
     * If true, malformed numbers are ignored. If false (default), malformed numbers throw an exception and reject the
     * whole document.
     */
    boolean ignoreMalformed() default false;

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

    /**
     * The following numeric types are supported by ElasticSearch.
     */
    enum NumericType {
        /**
         * By default, a best effort conversion will be made from the Java field type to an
         * ElasticSearch NumericType.
         */
        DEFAULT,

        /*A signed 64-bit integer with a minimum value of -263 and a maximum value of 263-1.*/
        LONG,

        /*A signed 32-bit integer with a minimum value of -231 and a maximum value of 231-1.*/
        INTEGER,

        /*A signed 16-bit integer with a minimum value of -32,768 and a maximum value of 32,767.*/
        SHORT,

        /*A signed 8-bit integer with a minimum value of -128 and a maximum value of 127.*/
        BYTE,

        /*A double-precision 64-bit IEEE 754 floating point.*/
        DOUBLE,

        /*A single-precision 32-bit IEEE 754 floating point.*/
        FLOAT,

        /*A half-precision 16-bit IEEE 754 floating point.*/
        HALF_FLOAT,

        /*A floating point that is backed by a long and a fixed scaling factor.*/
        SCALED_FLOAT
    }
}