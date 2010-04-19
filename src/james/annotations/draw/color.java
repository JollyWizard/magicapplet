package james.annotations.draw;

import james.annotations.ComponentCaller;

import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import magicofcalculus.Component;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface color {

    public String value() default "";

    public Class src() default Object.class;

    public int index() default 0;

    public int red() default 0;

    public int green() default 0;

    public int blue() default 0;

    public Mode mode() default Mode.name;

    public static enum Mode {
	name, field, rgb;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Index {
	int value();
    }

    public static class ComponentSetter extends
	    ComponentCaller<color, Component> {

	@Override
	public void call(color annote, Component c) {
	    java.awt.Color co = null;
	    Mode m = annote.mode();
	    if (m == Mode.name)
		co = getColor(annote.value());
	    if (m == Mode.field)
		co = getColor(annote.src(), annote.index());
	    if (m == Mode.rgb) {
		co = new java.awt.Color(annote.red(), annote.green(), annote
			.blue());
	    }
	    if (co != null)
		c.setColor(co);
	}

	public java.awt.Color getColor(Class src, int index) {
	    java.awt.Color r = null;
	    for (Field f : src.getFields()) {
		if (java.awt.Color.class.isAssignableFrom(f.getType())) {
		    Index ind = f.getAnnotation(Index.class);
		    if (ind == null)
			continue;
		    if (ind.value() == index) {
			try {
			    r = (java.awt.Color) f.get(null);
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	    return r;
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
