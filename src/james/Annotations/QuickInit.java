package james.Annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class QuickInit {

    /**
     * Returns an instance of c, using constructor c(o.getClass()) or c() (if
     * the one-arg constructor is not found). Recursively performs the same
     * operation on all member fields using the created object as o.
     * <p>
     * NOTE: Fields will only be initialized if they have non-null values,
     * because otherwise they were initialized by the constructor.
     * 
     * @param <T>
     *            The input type, and that which will be returned
     * @param c
     *            The type to create
     * @param o
     *            The object to use as for a one-arg constructor if available.
     * @return An instance of the input type if it was created, else null;
     */
    @SuppressWarnings("unchecked")
    public static <T> T Build(Class<T> c, Object o) {
	T r = null;
	// First search for constructor that takes o as an arguement
	try {
	    if (o != null) {
		for (Constructor con : c.getConstructors()) {
		    if (!Modifier.isPublic(con.getModifiers())) continue;
		    Class[] params = con.getParameterTypes();
		    if (params.length != 1
			    || !params[0].isAssignableFrom(o.getClass())) {
			continue;
		    }
		    r = (T) con.newInstance(o);
		    break;
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	// Then perform default constructor search
	if (r == null && !c.isMemberClass()) {
	    try {
		if (c.getConstructor() != null) {
		    r = c.newInstance();
		}
	    } catch (Exception e) {
		System.err.println("DEFAULT CONSTRUCTOR INIT ERROR: " + c.getClass());
		e.printStackTrace();
	    }
	}

	// if failed skip field build
	if (r == null)
	    return r;

	// Then quick init all fields
	for (Field f : c.getFields()) {
	    int mod = f.getModifiers();
	    if (Modifier.isPublic(mod) && !Modifier.isFinal(mod))
		try {
		    // skip fields that already have a value
		    if (f.get(r) != null)
			continue;

		    Object fo = Build(f.getType(), r);
		    if (fo != null)
			f.set(r, fo);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	return r;
    }
}
