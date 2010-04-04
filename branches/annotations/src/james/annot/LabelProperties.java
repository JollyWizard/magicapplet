package james.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Holds initialization properties for Labels defined as fields.
 * 
 * @deprecated move all functionality to QuickInit and AutoCall
 * @author JiMinitaur
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LabelProperties {
    String text() default Default;

    /**
     * @deprecated moved to autocall
     * @return
     */
    String image() default Default;

    /**
     * @deprecated, moved to autocall TODO add implementation to Image class
     *              caller
     * 
     * @return
     */
    boolean showImage() default true;

    /**
     * @deprecated moved to autocall
     * @return
     */
    boolean opaque() default false;

    public final static String Default = "!_default";
}