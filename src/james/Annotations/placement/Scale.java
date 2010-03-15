package james.Annotations.placement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Properties about how a rectangular area should be scaled.
 * 
 * @author JiMinitaur
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Scale {
    /**
     * The width of the x axis in units
     */
    double x();

    /**
     * The width of the y axis in units
     */
    double y();

    /**
     * The minimum value on the x axis
     * 
     * @default 0
     */
    double leftX() default 0;

    /**
     * The minimum value on the y axis
     * 
     * @default 0
     */
    double leftY() default 0;

    /**
     * Decides whether or not he origin will be centered in the area on the x
     * axis. If so, the scale of the negative and positive sides may be
     * different.
     * @default false
     */
    boolean centerX() default false;

    /**
     * Decides whether or not he origin will be centered in the area on the y
     * axis. If so, the scale of the negative and positive sides may be
     * different.
     * 
     * @default false
     */
    boolean centerY() default false;
}
