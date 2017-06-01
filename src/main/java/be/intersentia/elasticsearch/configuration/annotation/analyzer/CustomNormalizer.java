package be.intersentia.elasticsearch.configuration.annotation.analyzer;

public @interface CustomNormalizer {
    String name();
    String[] charFilters() default {};
    String[] filters() default {};
}