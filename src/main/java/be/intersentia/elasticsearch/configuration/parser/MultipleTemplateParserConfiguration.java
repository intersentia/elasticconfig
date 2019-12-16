package be.intersentia.elasticsearch.configuration.parser;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used on the ElasticSearch Mapping annotations, and configures which AbstractMappingParser can
 * parser the annotation.
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface MultipleTemplateParserConfiguration {
    Class<? extends AbstractMappingParser> parser();
}