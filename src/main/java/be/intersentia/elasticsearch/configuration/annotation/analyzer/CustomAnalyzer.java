package be.intersentia.elasticsearch.configuration.annotation.analyzer;

public @interface CustomAnalyzer {
    String name();
    String[] charFilters() default {};
    String tokenizer() default Tokenizer.STANDARD;
    String[] filters() default {};
    int positionIncrementGap() default 100;
}