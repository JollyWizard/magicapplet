package james;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * If an annotation defines an inner class extending this type, the call method
 * will be called on any non-null fields that declare that annotation.
 * <p>
 * TODO implement pointer annotation for non inner classes to be applied at
 * annotationType level.
 * 
 * @author James Arlow
 * 
 * @param <T>
 *            The Annotation type. Not strictly necessary, but allows the
 *            implementation to avoid casting.
 */
public interface AutoCaller<T extends Annotation> {

    /**
     * The method that is called. The object is the value already pulled from
     * the field.
     * 
     * @param annote
     * @param o
     *            The object for which the annotation should be applied to.
     */
    public void call(T annote, Object o);

    /**
     * Utility methods for AutoCall inner classes of Annotation Types
     * */
    public static class m {

	/**
	 * AutoCalls on all non null fields for the input object.
	 * 
	 * @param o
	 *            The object to autocall anotation types for all fields.
	 */
	public static void autoCall(Object o) {
	    if (o == null)
		return;
	    for (Field f : o.getClass().getFields()) {
		autoCall(f, o);
	    }
	}

	/**
	 * Auto calls a single field with a single parent instance (to get the
	 * field value from).
	 * 
	 * @param f
	 * @param parent
	 */
	@SuppressWarnings("unchecked")
	public static void autoCall(Field f, Object parent) {
	    // Get field value
	    Object o = null;
	    try {
		o = f.get(parent);
	    } catch (Exception e1) {
		e1.printStackTrace();
	    }
	    // return if the value is null
	    if (o == null)
		return;

	    Annotation[] aa = f.getAnnotations();
	    for (Annotation a : aa) {
		Class[] inners = a.annotationType().getDeclaredClasses();
		for (Class<?> c : inners) {
		    if (AutoCaller.class.isAssignableFrom(c)) {
			try {
			    AutoCaller ac = (AutoCaller) c.newInstance();
			    ac.call(a, o);
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	}
    }
}
