package james.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypedCaller<A extends Annotation, T> implements
	AutoCaller<A> {

    @SuppressWarnings("unchecked")
    @Override
    public void call(A annote, Object o) {
	if (getType().isAssignableFrom(o.getClass())) {
	    callTarget(annote, (T) o);
	}
    }

    public abstract void callTarget(A a, T target);

    @SuppressWarnings("unchecked")
    public Class<T> getType() {
	Class base = getClass();
	while (base.getSuperclass() != Object.class
		&& base.getSuperclass() != ComponentCaller.class) {
	    base = base.getSuperclass();
	}
	Type r = ((ParameterizedType) base.getGenericSuperclass())
		.getActualTypeArguments()[1];
	if (r instanceof Class)
	    return (Class<T>) r;
	return null;
    }

}
