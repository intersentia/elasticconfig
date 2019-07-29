package be.intersentia.elasticsearch.configuration.annotation.analyzer;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(Analyzers.class)
public @interface Analyzer {
    public static String STANDARD = "standard";
    public static String SIMPLE = "simple";
    public static String WHITESPACE = "whitespace";
    public static String STOP = "stop";
    public static String KEYWORD = "keyword";
    public static String PATTERN = "pattern";
    public static String FINGERPRINT = "fingerprint";

    String name();
    String type();
    Property[] properties() default {};

    int maxTokenLength() default 255;
    String stopWords() default "\\_none_";
    String stopWordsPath() default "";

    String pattern() default "DEFAULT";
    String flags() default "DEFAULT";
    boolean lowercase() default true;
}