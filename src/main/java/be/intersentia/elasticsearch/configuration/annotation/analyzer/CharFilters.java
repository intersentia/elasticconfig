package be.intersentia.elasticsearch.configuration.annotation.analyzer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation encapsulates multiple CharFilter annotations
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface CharFilters {

    CharFilter[] value();
}