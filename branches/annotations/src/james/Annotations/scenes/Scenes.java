package james.Annotations.scenes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stores the list of scenes for a panel as an annotation to the class header.
 * 
 * @author James Arlow
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scenes {
    /**
     * The list of Scene(s) for the class
     * 
     * @return
     */
    Scene[] value() default {};

}
