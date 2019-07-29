package be.intersentia.elasticsearch.configuration.annotation.mapping;

import be.intersentia.elasticsearch.configuration.parser.mapping.MappingParserConfiguration;
import be.intersentia.elasticsearch.configuration.parser.mapping.NestedMappingParser;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation marks an Object field as containing nested mapping annotations on the Object's fields.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(NestedMappings.class)
@MappingParserConfiguration(parser = NestedMappingParser.class)
public @interface NestedMapping {

    /**
     * The field() method allows you to specify the name of the field. By default, this is the same as the name of the
     * annotated Java field.
     */
    String field() default "DEFAULT";

    /**
     * Whether or not new properties should be added dynamically to an existing object. Accepts true (default), false
     * and strict.
     */
    DynamicOptions dynamic() default DynamicOptions.TRUE;
}