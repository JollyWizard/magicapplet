package james.Annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import magicofcalculus.Component;

/**
 * Binds AutoCaller class to ignore no-component fields and pre-generify methods
 * for use-case programming
 * 
 * @author James Arlow
 * 
 * @param <A>
 *            The annotation type to accept
 * @param <C>
 *            The Component type to accept.
 */
public abstract class ComponentCaller<A extends Annotation, C extends Component>
	implements AutoCaller<A> {

    public static boolean debug = false;

    @SuppressWarnings("unchecked")
    @Override
    public void call(A annote, Object o) {
	Class type = getType();
	if (o != null && type.isAssignableFrom(o.getClass())) {
	    C c = (C) o;

	    String[] pre = null;
	    if (debug)
		pre = getProperties(c);

	    call(annote, c);

	    String[] post = null;

	    if (debug) {
		String className = annote.annotationType().getSimpleName();
		System.out.print(className);
		post = getProperties(c);
		for (int i = 0; i < pre.length; i++) {
		    System.out.print("\t");
		    if (i > 0) {
			System.out.print('\t');
		    }
		    System.out.print(i < pre.length ? pre[i] : null);
		    System.out.print(" | ");
		    System.out.println(i < post.length ? post[i] : null);
		}
	    }
	}
    }

    public abstract void call(A annote, C c);

    public abstract String[] getProperties(C c);

    @SuppressWarnings("unchecked")
    public Class<C> getType() {
	Class base = getClass();
	while (base.getSuperclass() != Object.class
		&& base.getSuperclass() != ComponentCaller.class) {
	    base = base.getSuperclass();
	}
	Type r = ((ParameterizedType) base.getGenericSuperclass())
		.getActualTypeArguments()[1];
	if (r instanceof Class)
	    return (Class<C>) r;
	return null;
    }
}
