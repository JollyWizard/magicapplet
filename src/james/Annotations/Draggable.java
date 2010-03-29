package james.Annotations;

import james.Annotations.placement.Position;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import magicofcalculus.Component;

/**
 * Indicates that a field should be draggable. Defaults to true but can be used
 * to disable for classes that default to true;
 * 
 * @default true
 * @author James Arlow
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Draggable {
    boolean value() default true;

    public static class SetDraggable extends
	    ComponentCaller<Draggable, Component> {

	@Override
	public void call(Draggable annote, Component c) {
	    c.setDraggable(annote.value());
	}

	@Override
	public String[] getProperties(Component c) {
	    return new String[] { String.valueOf(c.isDraggable()) };
	}

    }
}
