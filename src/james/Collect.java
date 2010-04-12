package james;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Collect {

    /**
     * Collects a set of all public field values (non-null) from the source
     * object
     * 
     * @param src
     * @return
     */
    public static Set<Object> fieldValues(Object src) {
	HashSet<Object> r = new HashSet<Object>();
	if (src == null)
	    return r;

	for (Field f : src.getClass().getFields()) {
	    Object t = null;
	    try {
		t = f.get(src);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    if (t != null)
		r.add(t);
	}
	return r;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> fieldValues(Object src, Class<T> type) {
	HashSet<T> r = new HashSet<T>();
	if (src == null)
	    return r;

	for (Field f : src.getClass().getFields()) {
	    if (type.isAssignableFrom(f.getType())) {
		T t = null;
		try {
		    t = (T) f.get(src);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		if (t != null)
		    r.add(t);
	    }
	}
	return r;
    }

    /**
     * Returns a map of public fields to non-null values;
     * 
     * @param target
     * @return
     */
    public static HashMap<Field, Object> fieldMap(Object target) {
	HashMap<Field, Object> r = new HashMap<Field, Object>();
	for (Field f : target.getClass().getFields()) {
	    Object o = null;
	    try {
		o = f.get(target);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    if (o == null)
		continue;
	    r.put(f, o);
	}
	return r;
    }

    /**
     * Returns a map of public fields to non-null values, excluding entries that
     * don't match the desired type
     * 
     * @param <T>
     *            The type that the field must be assignable from to be added to
     *            the map
     * @param src
     *            The objects whose fields will be processed
     * @param type
     *            The type that the field must be assignable from to be added to
     *            the map
     * @return The map filled with applicable values;
     */
    @SuppressWarnings("unchecked")
    public static <T> HashMap<Field, T> fieldMap(Object src, Class<T> type) {
	HashMap<Field, T> r = new HashMap<Field, T>();
	for (Field f : src.getClass().getFields()) {
	    if (!type.isAssignableFrom(f.getType()))
		continue;
	    T t = null;
	    try {
		t = (T) f.get(src);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    if (t == null)
		continue;
	    r.put(f, t);
	}
	return r;
    }
}
