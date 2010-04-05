package james.annotations.visibility;

import james.annotations.Map;

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
     * Map.Key for show property.
     */
    public static final int show = 0;

    /**
     * Map.Key for hide property.
     */
    public static final int hide = 1;

    /**
     * Scenes at which the object becomes visible
     * <p>
     * Must be sorted or will not work properly.
     */
    @Map.Key(show)
    int[] value() default {0};

    /**
     * Scenes at which the object becomes hidden
     * <p>
     * Must be sorted or will not work properly.
     */
    @Map.Key(hide)
    int[] hidden() default {};

}
