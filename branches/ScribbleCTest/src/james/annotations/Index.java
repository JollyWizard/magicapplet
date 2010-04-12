package james.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A generic annotation to indicate that some sort of index should be applied to
 * a field or method
 * 
 * @author James Arlow
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
public @interface Index {
    public int value();

    /**
     * Indicates that a method can be processed by {@link Index.er}
     * 
     * @see {@link Index.er#process(Class, Class)} <br>
     *      {@link Index.er#process(Class, int, Class)}
     * @author James Arlow
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Key {
	/**
	 * Index.er can process multiple value methods if desired, but it needs
	 * to know this value. If one is not assigned, it will be assumed 0.
	 * Multiple methods with the same index will lead to unpredictable
	 * results.
	 * 
	 * @return
	 */
	int value() default DEFAULT;

	public static final int DEFAULT = 0;
    }

    /**
     * Process int Annotation methods to return an sorted index map between @see
     * {@link Index.Key}
     * 
     * @author James Arlow
     */
    public abstract static class er {

	/**
	 * Calls {@link Index.er#process(Class, int, Class)} with default key
	 * 
	 * @param <A>
	 * @param annotation
	 * @param cl
	 * @return
	 */
	public static <A extends Annotation> LinkedHashMap<A, Field> process(
		Class<A> annotation, Class<?> cl) {
	    return process(annotation, Key.DEFAULT, cl);
	}

	/**
	 * Processes a class to return a sorted map, based on {@link Index.Key}
	 * Annotations
	 * 
	 * @param <A>
	 *            The annotation type that will be mapped
	 * @param annotation
	 *            the annotation class
	 * @param key
	 *            the Index.Key value to match
	 * @param cl
	 *            the class to map fields from
	 * @return a LinkedHashMap containing all the fields in the indexing
	 *         order
	 */
	public static <A extends Annotation> LinkedHashMap<A, Field> process(
		Class<A> annotation, int key, Class<?> cl) {
	    // build return list && return if invalid class to process
	    // parameter;
	    LinkedHashMap<A, Field> r = new LinkedHashMap<A, Field>();
	    if (cl == null)
		return r;

	    // grab all field annotations
	    HashMap<A, Field> cache = new HashMap<A, Field>();
	    for (Field f : cl.getFields()) {
		A a = f.getAnnotation(annotation);
		if (a == null)
		    continue;
		cache.put(a, f);
		System.out.println(f.getName() + " | " + f.getType());
	    }

	    // if no annotations found return;
	    if (cache.size() == 0)
		return r;

	    // find index method
	    Method ivalue = null;
	    for (Method m : annotation.getDeclaredMethods()) {
		Index.Key ik = m.getAnnotation(Index.Key.class);
		if (ik == null)
		    continue;
		if (ik.value() == key) {
		    ivalue = m;
		    break;
		}
	    }
	    
	    // if no index method found return;
	    if (ivalue == null)
		return r;

	    // find range and cache values
	    Integer lowest = null, highest = null, value = null;
	    HashMap<A, Integer> values = new HashMap<A, Integer>();

	    for (A a : cache.keySet()) {
		try {
		    value = null;
		    Object o = ivalue.invoke(a);
		    if (o instanceof Integer)
			value = (Integer) o;
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		if (value == null)
		    continue;
		if (lowest == null || value < lowest)
		    lowest = value;
		if (highest == null || value > highest)
		    highest = value;
		values.put(a, value);
	    }

	    System.out.println(lowest);
	    // add to linked HashMap in order
	    ArrayList<A> as = new ArrayList<A>();

	    for (int i = lowest; i <= highest; i++) {

		// empty remove list
		as.clear();

		// add matching entries to return and add to remove list
		for (A a : values.keySet()) {
		    value = values.get(a);
		    if (value == i) {
			r.put(a, cache.get(a));
			as.add(a);
		    }
		}

		// removed processed entries from cache
		for (A a : as)
		    cache.remove(a);
	    }

	    return r;
	}
    }

}
