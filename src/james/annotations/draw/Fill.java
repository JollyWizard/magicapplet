package james.annotations.draw;

import james.annotations.ComponentCaller;
import magicofcalculus.components.Axes;

public @interface Fill {
    boolean value() default true;

    String color() default "black";

    public static class SetAxes extends ComponentCaller<Fill, Axes> {

	@Override
	public void call(Fill annote, Axes c) {
	    c.setFillUnderCurveVisible(annote.value());
	    java.awt.Color co = color.ComponentSetter.getColor(annote.color());
	    if (co != null) {
		c.setFillUnderCurveColor(co);
	    }
	}

	@Override
	public String[] getProperties(Axes c) {
	    return new String[] { c.getFillUnderCurveColor().toString() };
	}
    }
}
