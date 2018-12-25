package site.zido.elise.task.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface EliseHelper {
    String regex() default "";
}
