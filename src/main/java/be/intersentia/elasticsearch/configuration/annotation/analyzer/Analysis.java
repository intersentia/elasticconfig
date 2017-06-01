package be.intersentia.elasticsearch.configuration.annotation.analyzer;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by arnautssn on 20/04/2017.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Analysis {
    CharFilter[] charFilters() default {};
    Tokenizer[] tokenizers() default {};
    Filter[] filters() default {};
    Analyzer[] analyzers() default {};
    CustomAnalyzer[] customAnalyzers() default {};
    CustomNormalizer[] customNormalizers() default {};
}
