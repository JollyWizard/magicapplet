package james.annot;

public @interface Point {
  double x() default 0;

  double y() default 0;

  String parameter() default "";
}
