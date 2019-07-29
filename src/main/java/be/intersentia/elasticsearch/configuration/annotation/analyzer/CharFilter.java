package be.intersentia.elasticsearch.configuration.annotation.analyzer;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(CharFilters.class)
public @interface CharFilter {
    public static String HTML_STRIP = "html_strip";
    public static String MAPPING = "mapping";
    public static String PATTERN_REPLACE = "pattern_replace";

    String name();
    String type();
    Property[] properties() default {};

    String[] escapedTags() default {};

    String[] mappings() default {};
    String mappingsPath() default "DEFAULT";

    String pattern() default "DEFAULT";
    String replacement() default "DEFAULT";
    String flags() default "DEFAULT";
}
