/**
 * 
 */
package james.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import magicofcalculus.Function;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PolyLineConfig {
    int axes() default 0;

    int intervals();

    double leftXLocal() default 0;

    double rightXLocal() default 0;

    Class<? extends Function> function();

}