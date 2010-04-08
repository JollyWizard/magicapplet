package james;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Collect {

    @SuppressWarnings("unchecked")
    public static <T> Set<T> getFieldValues(Object src, Class<T> type) {
	HashSet<T> r = new HashSet<T>();
	if (src == null)
	    return r;

	for (Field f : src.getClass().getFields()) {
	    if (Modifier.isPublic(f.getModifiers())
		    && type.isAssignableFrom(f.getType())) {
		T t = null;
		try {
		    t = (T) f.get(src);
		} catch (Exception e) {

		}
		if (t != null)
		    r.add(t);
	    }
	}

	return r;
    }
}
