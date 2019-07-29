package be.intersentia.elasticsearch.configuration.annotation.analyzer;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(CustomNormalizers.class)
public @interface CustomNormalizer {
    String name();
    String[] charFilters() default {};
    String[] filters() default {};
}