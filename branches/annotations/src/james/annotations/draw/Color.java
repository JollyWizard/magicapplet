package james.annotations.draw;

import james.ComponentCaller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import magicofcalculus.Component;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Color {

    public String value();

    public static class ComponentSetter extends
	    ComponentCaller<Color, Component> {

	@Override
	public void call(Color annote, Component c) {
	    java.awt.Color co = getColor(annote.value());
	    if (co != null)
		c.setColor(co);

	}

	@Override
	public String[] getProperties(Component c) {
	    return new String[] { c.getColor().toString() };
	}

	public static java.awt.Color getColor(String s) {
	    Field f;
	    try {
		f = java.awt.Color.class.getField(s);
		if (Modifier.isStatic(f.getModifiers())
			&& f.getType().equals(java.awt.Color.class)) {
		    return (java.awt.Color) f.get(null);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    java.awt.Color r = null;
	    try {
		r = java.awt.Color.decode(s);
	    } catch (Exception e) {
	    }
	    return r;
	}
    }

}
