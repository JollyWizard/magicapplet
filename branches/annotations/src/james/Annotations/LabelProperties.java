package james.Annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Holds initialization properties for Labels defined as fields.
 * 
 * @author JiMinitaur
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LabelProperties {
  String text() default Default;

  String image() default Default;

  boolean showImage() default true;

  boolean drag() default true;

  boolean opaque() default false;

  public final static String Default = "!_default";
}