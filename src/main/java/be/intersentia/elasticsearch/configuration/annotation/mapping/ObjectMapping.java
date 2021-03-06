package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.MappingParser;
import be.intersentia.elasticsearch.configuration.parser.ObjectMappingParser;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation defines the field as requiring an Object mapping in ElasticSearch. An Object mapping enables mapping
 * hierarchical JSON documents: the document may contain inner objects which, in turn, may contain inner objects
 * themselves.
 *
 * For more details see: https://www.elastic.co/guide/en/elasticsearch/reference/current/object.html
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Repeatable(ObjectMappings.class)
@MappingParser(ObjectMappingParser.class)
public @interface ObjectMapping {

    /**
     * The field() method allows you to specify the name of the Elastic Search field. When annotating a Java Class you
     * are required to provide a value for this method. When annotating a Java Field this method is optional: by default
     * the value is he same as the name of the annotated Java Field.
     */
    String field() default "DEFAULT";

    /**
     * Whether or not new properties should be added dynamically to an existing object. Accepts true (default), false
     * and strict.
     */
    DynamicOptions dynamic() default DynamicOptions.TRUE;

    /**
     * Whether the JSON value given for the object field should be parsed and indexed (true, default) or completely
     * ignored (false).
     */
    boolean enabled() default true;

    /**
     * Whether or not the field value should be included in the _all field? Defaults to false if index is set to false,
     * or if a parent object field sets includeInAll to false. Otherwise defaults to true.
     * @deprecated Deleted in ElasticSearch 7.0
     */
    @Deprecated
    OptionalBoolean includeInAll() default OptionalBoolean.DEFAULT;
}