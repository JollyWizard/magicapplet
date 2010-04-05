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
import java.util.LinkedList;
import java.util.List;

import magicofcalculus.QuadCurve;
import magicofcalculus.components.Axes;

/**
 * A generic annotation to indicate that some sort of index should be applied to
 * a field or method
 * 
 * @author James Arlow
 */
public abstract class Map {

    public static <A extends Annotation, T> void Fill(Object src, List<T> list,
	    Class<A> aType, Class<T> listType) {
	LinkedHashMap<Integer, List<T>> values = Map.er.apply(aType, src,
		listType);
	for (Integer i : values.keySet()) {
	    System.out.println(i);
	    for (T t : values.get(i)) {
		System.out.println(t);
		list.add(t);
	    }
	}
    }

    /**
     * Indicates that a method can be processed by {@link Map.er}
     * 
     * @see {@link Map.er#process(Class, Class)} <br>
     *      {@link Map.er#process(Class, int, Class)}
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
     * {@link Map.Key}
     * 
     * @author James Arlow
     */
    public abstract static class er {

	/**
	 * Calls {@link Map.er#process(Class, int, Class)} with default key
	 * 
	 * @param <A>
	 * @param annotation
	 * @param cl
	 * @return
	 */
	public static <A extends Annotation> LinkedHashMap<Integer, List<Field>> process(
		Class<A> annotation, Class<?> cl) {
	    return process(annotation, Key.DEFAULT, cl);
	}

	/**
	 * Stores the maps of processed annotation classes by the class from
	 * which the fields were analyzed. The apply(Object) method relies on
	 * this cache to begin avoid code duplication. If a cache is not found
	 * then it calls the process method, which caches before it exits.
	 */
	private static HashMap<Class<? extends Annotation>, HashMap<Integer, HashMap<Class, LinkedHashMap<Integer, List<Field>>>>> mapCache = new HashMap<Class<? extends Annotation>, HashMap<Integer, HashMap<Class, LinkedHashMap<Integer, List<Field>>>>>();

	/**
	 * Safely deposits a map for Annotation and Map.Key by ensuring that all
	 * nested hashmaps are in place before performing the set operation.
	 * 
	 * @param annotation
	 * @param key
	 * @param objType
	 * @param map
	 * @return the input map so that it can be chained with return calls.
	 */
	private static LinkedHashMap<Integer, List<Field>> cacheAndReturn(
		Class<? extends Annotation> annotation, int key, Class objType,
		LinkedHashMap<Integer, List<Field>> map) {

	    // get the cache for annotationType:keys
	    HashMap<Integer, HashMap<Class, LinkedHashMap<Integer, List<Field>>>> keymap = mapCache
		    .get(annotation);
	    // create if not yet present
	    if (keymap == null) {
		keymap = new HashMap<Integer, HashMap<Class, LinkedHashMap<Integer, List<Field>>>>();
		mapCache.put(annotation, keymap);
	    }
	    // get the map of classes to indexed fields and for the current key
	    HashMap<Class, LinkedHashMap<Integer, List<Field>>> classmap = keymap
		    .get(key);
	    // create if missing.
	    if (classmap == null) {
		classmap = new HashMap<Class, LinkedHashMap<Integer, List<Field>>>();
		keymap.put(key, classmap);
	    }
	    // store class mapping for key in the end map
	    classmap.put(objType, map);

	    return map;
	}

	/**
	 * Processes a class to return a sorted map, based on {@link Map.Key}
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
	public static <A extends Annotation> LinkedHashMap<Integer, List<Field>> process(
		Class<A> annotation, int key, Class<?> cl) {
	    // build return list && return if invalid class to process
	    // parameter;
	    LinkedHashMap<Integer, List<Field>> r = new LinkedHashMap<Integer, List<Field>>();
	    if (cl == null)
		return cacheAndReturn(annotation, key, cl, r);

	    // grab all field annotations
	    HashMap<Field, A> fieldCache = new HashMap<Field, A>();
	    for (Field f : cl.getFields()) {
		A a = f.getAnnotation(annotation);
		if (a == null)
		    continue;
		fieldCache.put(f, a);
		// System.out.println(f.getName() + "\t|\t" +
		// fieldCache.get(f));
	    }

	    // if no annotations found return;
	    if (fieldCache.size() == 0)
		return cacheAndReturn(annotation, key, cl, r);

	    // find index method
	    Method ivalue = null;
	    for (Method m : annotation.getDeclaredMethods()) {
		Map.Key ik = m.getAnnotation(Map.Key.class);
		if (ik == null)
		    continue;
		if (ik.value() == key) {
		    ivalue = m;
		    break;
		}
	    }

	    // if no index method found return;
	    if (ivalue == null)
		return cacheAndReturn(annotation, key, cl, r);

	    if (ivalue.getReturnType() != int[].class) {
		System.err.println("Map Key not int[]: " + ivalue.getName());
	    }

	    // find range and cache values
	    Integer lowest = null, highest = null;
	    int[] value = null;
	    HashMap<A, int[]> values = new HashMap<A, int[]>();

	    for (A a : fieldCache.values()) {
		try {
		    value = null;
		    Object o = ivalue.invoke(a);
		    if (o instanceof int[])
			value = (int[]) o;
		} catch (Exception e) {
		    e.printStackTrace();
		}

		if (value == null) {
		    System.err.println("SKIPPING: " + a);
		    continue;
		}

		for (int i : value) {
		    if (lowest == null || i < lowest)
			lowest = i;
		    if (highest == null || i > highest)
			highest = i;
		    values.put(a, value);
		}
	    }
	    // At this point the lowest and highest value have been found
	    // and all the int[]s have been cached with the annotations in the
	    // values map. annotations:fields are cached in the fieldCache.

	    // If no values were found return;
	    if (lowest == null || highest == null)
		return cacheAndReturn(annotation, key, cl, r);

	    // check the keys iteratively from the start to the end.
	    for (int i = lowest; i <= highest; i++) {
		// check the by fields because annotation equality is finniky
		for (Field f : fieldCache.keySet()) {
		    // get the value via the annotation key from field cache
		    value = values.get(fieldCache.get(f));
		    // iterate each value
		    for (int j : value) {
			// if the current value is in list
			if (j == i) {
			    // create a new list if not present
			    if (r.get(j) == null)
				r.put(j, new LinkedList<Field>());
			    // add the field to the index list.
			    r.get(j).add(f);
			}
		    }
		}
	    }

	    return cacheAndReturn(annotation, key, cl, r);
	}

	/**
	 * Checks the HashMap nesting when retrieving a cached map. If at any
	 * point the nesting has not already been initialized, the map will be
	 * processed which will then ensure that it is.
	 * 
	 * @param aType
	 * @param key
	 * @param objectType
	 * @return
	 */
	public static LinkedHashMap<Integer, List<Field>> getMapCache(
		Class<? extends Annotation> aType, int key, Class objectType) {
	    HashMap<Integer, HashMap<Class, LinkedHashMap<Integer, List<Field>>>> hm = mapCache
		    .get(aType);
	    if (hm == null) {
		process(aType, key, objectType);
		hm = mapCache.get(aType);
	    }

	    HashMap<Class, LinkedHashMap<Integer, List<Field>>> keymap = hm
		    .get(key);
	    if (keymap == null) {
		process(aType, key, objectType);
		keymap = hm.get(key);
	    }

	    LinkedHashMap<Integer, List<Field>> r = keymap.get(objectType);
	    if (r == null) {
		process(aType, key, objectType);
		r = keymap.get(objectType);
	    }
	    return r;
	}

	public static <A extends Annotation, V> LinkedHashMap<Integer, List<V>> apply(
		Class<A> annotation, Object obj, Class<V> valueType) {
	    return apply(annotation, obj, valueType, Key.DEFAULT);
	}

	@SuppressWarnings("unchecked")
	public static <A extends Annotation, V> LinkedHashMap<Integer, List<V>> apply(
		Class<A> annotation, Object obj, Class<V> valueType, int key) {

	    // preconditions
	    if (obj == null)
		return null;
	    Class c = obj.getClass();

	    // return value
	    LinkedHashMap<Integer, List<V>> r = new LinkedHashMap<Integer, List<V>>();

	    // get the cache for the key, will be created if doesn't exist.
	    LinkedHashMap<Integer, List<Field>> fieldMap = getMapCache(
		    annotation, key, c);

	    for (Integer i : fieldMap.keySet()) {
		for (Field f : fieldMap.get(i)) {
		    if (valueType.isAssignableFrom(f.getType())) {
			try {
			    if (r.get(i) == null)
				r.put(i, new LinkedList<V>());
			    r.get(i).add((V) f.get(obj));
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    }
		}
	    }

	    return r;
	}
    }

}
