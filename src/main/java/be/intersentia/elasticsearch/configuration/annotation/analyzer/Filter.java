package be.intersentia.elasticsearch.configuration.annotation.analyzer;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(Filters.class)
public @interface Filter {
    public static String STANDARD = "standard";
    public static String ASCII_FOLDING = "asciifolding";
    public static String FLATTEN_GRAPH = "flatten_graph";
    public static String LENGTH = "length";
    public static String LOWERCASE = "lowercase";
    public static String UPPERCASE = "uppercase";
    public static String NGRAM = "nGram";
    public static String EDGE_NGRAM = "edgeNGram";
    public static String PORTER_STEM = "porter_stem";
    public static String SHINGLE = "shingle";
    public static String STOP = "stop";
    public static String WORD_DELIMITER = "word_delimiter";
    public static String STEMMER = "stemmer";
    public static String STEMMER_OVERRIDE = "stemmer_override";
    public static String KEYWORD_MARKER = "keyword_marker";
    public static String KEYWORD_REPEAT = "keyword_repeat";
    public static String KSTEM = "kstem";
    public static String SNOWBALL = "snowball";
    public static String PHONETIC = "phonetic";
    public static String SYNONYM = "synonym";
    public static String SYNONYM_GRAPH = "synonym_graph";
    public static String COMPOUND_WORD_HYPHENATAION = "hyphenation_decompounder";
    public static String COMPOUND_WORD_DICTIONARY = "dictionary_decompounder";
    public static String REVERSE = "reverse";
    public static String ELISION = "elisions";
    public static String TRUNCATE = "truncate ";
    public static String UNIQUE = "unique";
    public static String PATTERN_CAPTURE = "pattern_capture";
    public static String PATTERN_REPLACE = "pattern_replace";
    public static String TRIM = "trim";
    public static String LIMIT_TOKEN_COUNT = "limit";
    public static String HUNSPELL_STEM = "hunspell";
    public static String COMMON_GRAMS = "common_grams";
    public static String NORMALIZATION = "normalization";
    public static String CJK_WIDTH = "cjk_width";
    public static String CJK_BIGRAM = "cjk_bigram ";
    public static String DELIMITED_PAYLOAD = "delimited_payload_filter";
    public static String KEEP_WORDS = "keep";
    public static String CLASSIC = "classic";
    public static String APOSTROPHE = "apostrophe";
    public static String DECIMAL_DIGITS = "decimal_digit";
    public static String FINGERPRINT = "fingerprint ";
    public static String MINHASH = "min_hash";

    String name();
    String type();
    Property[] properties() default {};
}
