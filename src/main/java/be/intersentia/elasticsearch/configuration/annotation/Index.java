package be.intersentia.elasticsearch.configuration.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Optional;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
public @interface Index {
    String index() default "EMPTY";
    String parent() default "";
    boolean disableDynamicProperties() default true;
    boolean disableAllField() default false;
}
