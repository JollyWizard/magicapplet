package james.annot.drag;

import james.ComponentCaller;
import james.QuickInit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import magicofcalculus.Component;

/**
 * Indicates that a field should be draggable. Defaults to true but can be used
 * to disable for classes that default to true on their own;
 * 
 * @default true
 * @author James Arlow
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Drag {
    boolean value() default true;

    Class<? extends Handler> action() default Handler.class;

    /**
     * A class that is called when a drag action is performed.
     * 
     * @author James Arlow
     * 
     */
    public abstract class Handler {
	public abstract void action(Object... params);
    }

    /**
     * The <code>AutoCaller</code> class that handles drag operations
     * 
     * @author James Arlow
     * 
     */
    public static class SetDraggable extends ComponentCaller<Drag, Component> {

	@Override
	public void call(Drag annote, Component c) {
	    c.setDraggable(annote.value());
	    if (annote.action() != Handler.class && c._panel != null) {
		Handler h = QuickInit.Build(annote.action(), c._panel);
		c.dMaster2 = h;
	    }
	}

	@Override
	public String[] getProperties(Component c) {
	    return new String[] { String.valueOf(c.isDraggable()),
		    String.valueOf(c.dMaster2) };
	}

    }
}
