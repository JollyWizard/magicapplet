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

  /**
   * If parameter = LabelProperties.Default, then the position values where
   * not declared.
   * TODO migrate this to separate functionality
   */
  Point pos() default @Point(parameter = Default);

  boolean showImage() default true;

  boolean drag() default true;

  boolean opaque() default false;

  public final static String Default = "!_default";
}