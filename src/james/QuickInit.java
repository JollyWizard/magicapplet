package james;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import magicofcalculus.components.PolyLine;

public class QuickInit {

    /**
     * Builds all fields that QuickInit is capable of
     * 
     * @param o
     */
    public static void Build(Object o) {
	for (Field f : o.getClass().getFields()) {
	    Build(o, f);
	}
    }

    public static <T> T Build(Class<T> c) {
	return Build(c, (Object) null);
    }

    public static void Build(Object o, Field f) {
	// check parameters before trying
	if (o == null)
	    return;

	// check accessible
	int mod = f.getModifiers();
	if (Modifier.isFinal(mod) || !Modifier.isPublic(mod))
	    return;

	// make sure parent is correct
	if (!f.getDeclaringClass().isAssignableFrom(o.getClass()))
	    return;
	// check for null
	try {
	    if (f.get(o) != null)
		return;
	} catch (Exception e1) {
	    e1.printStackTrace();
	}

	// Build object and set if successful
	Object fo = Build(f.getType(), o);
	if (fo == null)
	    return;
	try {
	    f.set(o, fo);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

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
	int mod = c.getModifiers();
	if (c.isPrimitive() || Modifier.isAbstract(mod)
		|| !Modifier.isPublic(mod))
	    return null;
	T r = null;
	// First search for constructor that takes o as an arguement
	try {
	    if (o != null) {
		cons: for (Constructor con : c.getConstructors()) {
		    if (!Modifier.isPublic(con.getModifiers()))
			continue;
		    Class[] params = con.getParameterTypes();
		    for (Class cp : params) {
			if (!cp.isAssignableFrom(o.getClass()))
			    continue cons;
		    }
		    Object[] PARAMS = new Object[params.length];
		    for (int i = 0; i < PARAMS.length; i++)
			PARAMS[i] = o;

		    r = (T) con.newInstance(PARAMS);
		    break;
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	// Then perform default constructor search
	if (r == null
		&& (!c.isMemberClass() || Modifier.isStatic(c.getModifiers()))) {
	    try {
		if (c.getConstructor() != null) {
		    r = c.newInstance();
		}
	    } catch (NoSuchMethodException nsme) {
	    } catch (Exception e) {
		System.err.println("DEFAULT CONSTRUCTOR INIT ERROR: "
			+ c.getClass());
		e.printStackTrace();
	    }
	}

	// if failed skip field build
	if (r == null)
	    return r;

	// Build FIelds
	Build(r);

	return r;
    }
}
