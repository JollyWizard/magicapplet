package james.Annotations.labels;

import james.Annotations.ComponentCaller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import magicofcalculus.components.Label;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Opaque {
    public boolean value();

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
