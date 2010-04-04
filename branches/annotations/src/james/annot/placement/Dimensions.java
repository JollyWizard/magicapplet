package james.annot.placement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import magicofcalculus.components.Axes;
import james.ComponentCaller;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dimensions {

    double width() default 0;

    double height() default 0;

    public static class SetAxes extends ComponentCaller<Dimensions, Axes> {

	@Override
	public void call(Dimensions annote, Axes c) {
	    c.setDimensions((int) annote.width(), (int) annote.height());
	}

	@Override
	public String[] getProperties(Axes c) {
	    return new String[] {};
	}

    }

}
