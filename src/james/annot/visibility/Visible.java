package james.annot.visibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates what scenes the component will become active or hidden.
 * 
 * @author James Arlow
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Visible {

    /**
     * Scenes at which the object becomes visible
     * <p>
     * Must be sorted or will not work properly.
     */
    int[] value() default {};

    /**
     * Scenes at which the object becomes hidden
     * <p>
     * Must be sorted or will not work properly.
     */
    int[] hidden() default {};

}
