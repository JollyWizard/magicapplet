package james.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.LinkedList;

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
    public void call(T annote, Object target);

    /**
     * Utility methods for AutoCall inner classes of Annotation Types
     * */
    public static class m {

	/**
	 * Applys all autocallers from class to the object, then all fields to
	 * non-null field values
	 * 
	 * @param o
	 *            The object to autocall anotation types for all fields.
	 */
	public static void autoCall(Object o, Class...whitelist) {
	    if (o == null)
		return;

	    // first process class level annotations. Starting at bottom class
	    // and working up
	    LinkedList<Class> classes = new LinkedList<Class>();
	    Class c = o.getClass();
	    while (c != Object.class && c != null) {
		classes.add(c);
		c = c.getSuperclass();
	    }
	    
	    for (int i = 0; i < classes.size(); i++)
		autoCall(classes.get(i), o, whitelist);

	    // then process field level annotations
	    for (Field f : o.getClass().getFields()) {
		autoCall(f, o, whitelist);
	    }
	}

	/**
	 * For each annotation on the element, if the annotation has an inner
	 * class of type AutoCaller, call {@link AutoCaller#call(T, Object)};
	 * 
	 * @param ae
	 *            the annotated element to retrieve annotations from,
	 *            generally this a class or field for which the element
	 *            belongs.
	 * @param o
	 *            the object which is passed as the target to the
	 *            AutoCallers#call() method
	 * @param whitelist
	 *            if these arguements are included, only Annotations of
	 *            these types will be processed.
	 */
	@SuppressWarnings("unchecked")
	public static void autoCall(AnnotatedElement ae, Object o,
		Class... whitelist) {
	    Annotation[] aa = ae.getAnnotations();
	    annotations: for (Annotation a : aa) {
		if (whitelist.length != 0) {
		    for (Class<? extends Annotation> c : whitelist)
			if (!c.isAssignableFrom(a.getClass()))
			    continue annotations;
		}
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

	/**
	 * Auto calls a single field with a single parent instance (to get the
	 * field value from).
	 * 
	 * @param f
	 *            The field to call the object on.
	 * @param parent
	 *            the object from whom the field value should be retrieved
	 *            before calling the annotation setter
	 */
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

	    // if the value was retrieved, apply the fields annotations to it
	    autoCall((AnnotatedElement) f, o);

	}
    }
}
