package be.intersentia.elasticsearch.configuration.annotation.templates;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation allows you to define custom mappings that can be applied to dynamically added fields based on:
 * - the datatype detected by Elasticsearch, with match_mapping_type.
 * - the name of the field, with match and unmatch or match_pattern.
 * - the full dotted path to the field, with path_match and path_unmatch.
 *
 * The original field name {name} and the detected datatype {dynamic_type} template variables can be used in the mapping
 * specification as placeholders.
 */
@Retention(RUNTIME)
public @interface DynamicTemplate {
    String name();
    String matchMappingType() default "DEFAULT";
    String match() default "DEFAULT";
    String unMatch() default "DEFAULT";
    String pathMatch() default "DEFAULT";
    String pathUnMatch() default "DEFAULT";
}