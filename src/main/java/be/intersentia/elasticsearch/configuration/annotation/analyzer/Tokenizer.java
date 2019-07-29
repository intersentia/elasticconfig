package be.intersentia.elasticsearch.configuration.annotation.analyzer;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(Tokenizers.class)
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
