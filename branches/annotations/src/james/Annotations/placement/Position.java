package james.Annotations.placement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * Indicates two dimensional position defaulting to (0,0)
 */
public @interface Position {

  /**
   * The x position
   * @default 0
   */
  double x() default 0;
  /**
   * The y position
   * @default 0
   */
  double y() default 0;
  
}
