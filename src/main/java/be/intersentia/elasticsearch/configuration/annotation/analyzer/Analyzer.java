package be.intersentia.elasticsearch.configuration.annotation.analyzer;

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