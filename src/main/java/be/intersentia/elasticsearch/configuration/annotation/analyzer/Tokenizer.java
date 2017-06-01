package be.intersentia.elasticsearch.configuration.annotation.analyzer;

/**
 * Created by arnautssn on 20/04/2017.
 */
public @interface Tokenizer {
    public static String STANDARD = "standard";
    public static String LETTER = "letter";
    public static String LOWERCASE = "lowercase";
    public static String WHITESPACE = "whitespace";
    public static String UAX_URL_EMAIL = "uax_url_email";
    public static String CLASSIC = "classic";
    public static String THAI = "thai";
    public static String NGRAM = "ngram";
    public static String EDGE_NGRAM = "edge_ngram";
    public static String KEYWORD = "keyword";
    public static String PATTERN = "pattern";
    public static String PATH_HIERARCHY = "path_hierarchy";

    String name();
    String type();
    Property[] properties() default {};
}
