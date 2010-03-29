package james.Annotations.placement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import magicofcalculus.Component;
import james.Annotations.ComponentCaller;

/**
 * Indicates two dimensional position defaulting to (0,0)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Position {

    /**
     * The x position
     * 
     * @default 0
     */
    double x() default 0;

    /**
     * The y position
     * 
     * @default 0
     */
    double y() default 0;

    public static class SetPosition extends
	    ComponentCaller<Position, Component> {

	@Override
	public void call(Position annote, Component c) {
	    c.setPosition(annote.x(), annote.y());
	}

	@Override
	public String[] getProperties(Component c) {
	    return new String[] { c.getPosition().toString() };
	}

    }

}
