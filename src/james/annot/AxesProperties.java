/**
 * 
 */
package james.annot;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AxesProperties {
  int index() default 0;

  int localW() default 0;

  int localH() default 0;
}