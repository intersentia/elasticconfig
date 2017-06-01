package be.intersentia.elasticsearch.configuration.annotation.analyzer;

/**
 * Created by arnautssn on 20/04/2017.
 */
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
