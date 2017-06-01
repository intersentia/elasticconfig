package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.mapping.DateMappingParser;
import be.intersentia.elasticsearch.configuration.parser.mapping.MappingParserConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines the field as requiring a Date mapping in ElasticSearch. A Date mapping is used to index
 * Date objects, Strings containing formatted dates, e.g. "2015-01-01" or "2015/01/01 12:10:30", Long numbers
 * representing milliseconds-since-the-epoch or Integer numbers representing seconds-since-the-epoch.
 *
 * For more details see: https://www.elastic.co/guide/en/elasticsearch/reference/5.2/date.html
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@MappingParserConfiguration(parser = DateMappingParser.class)
public @interface DateMapping {

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
     * The date format(s) that can be parsed. Defaults to strict_date_optional_time||epoch_millis.
     */
    String format() default "DEFAULT";

    /**
     * The locale to use when parsing dates since months do not have the same names and/or abbreviations in all
     * languages. The default is the ROOT locale,
     */
    String locale() default "DEFAULT";

    /**
     * If true, malformed numbers are ignored. If false (default), malformed numbers throw an exception and reject the
     * whole document.
     */
    boolean ignoreMalformed() default false;

    /**
     * Whether or not the field value should be included in the _all field? Defaults to false if index is set to false,
     * or if a parent object field sets includeInAll to false. Otherwise defaults to true.
     */
    OptionalBoolean includeInAll() default OptionalBoolean.DEFAULT;

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