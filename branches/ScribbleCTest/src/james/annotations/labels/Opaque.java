package james.annotations.labels;

import james.annotations.ComponentCaller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import magicofcalculus.components.Label;

/**
 * Sets opacity for images
 * @author James Arlow
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Opaque {
    public boolean value() default true;

    public static class SetOpaque extends ComponentCaller<Opaque, Label> {

	@Override
	public void call(Opaque annote, Label c) {
	    c.setOpaque(annote.value());
	}

	@Override
	public String[] getProperties(Label c) {
	    //TODO expose opacity for debugging info
	    return new String[] {};
	}
    }
}
