package magic.doclet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MemberDoc;
import com.sun.javadoc.Type;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;

public class Tools {

  public static Set<FieldDoc> fieldsOfType (ClassDoc source,
          Class type) {
    HashSet<FieldDoc> r = new HashSet<FieldDoc>();
    for (FieldDoc f : source.fields()) {
      if (instanceOf(f.type(), type)) r.add(f);
    }
    return r;
  }

  @SuppressWarnings("unchecked")
  public static <T> Class<? extends T> getRealClass (ClassDoc doc,
          Class<T> type) {
    Class c = getRealClass(doc);
    if (c == null) return null;
    if (type.isAssignableFrom(c)) return (Class<? extends T>) c;
    return null;
  }

  public static Class getRealClass (ClassDoc doc) {
    Class c = null;
    try {
      c = Class.forName(doc.qualifiedTypeName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return c;
  }

  /**
   * Retrieves the value of the given annotation instance documentation with the
   * given key/method name
   * 
   * @param ad
   *          The AnnotationDesc to poll the methods for.
   * @param name
   *          The name of the value method to retrieve that value for
   * @return
   */
  public static Object annotationValue (AnnotationDesc ad, String name) {
    if (ad == null) return null;
    for (ElementValuePair evp : ad.elementValues()) {
      if (evp.element().name().equals(name))
        return evp.value().value();
    }
    return null;
  }

  /**
   * Retrieves the annotation value with the given method name. Will return null
   * if the class is wrong to ensure variable assignment. If you are unsure what
   * the value type will be, use the two parameter version.
   * 
   * @param <T>
   *          The type that the annotation value should match.
   * @param ad
   *          The AnnotationDesc to poll the methods for.
   * @param name
   *          The name of the value method to retrieve that value for
   * @param type
   *          the type of the Annotations method value which is to be retrieved.
   * @return The cast value if found and is of the correct type, else null.
   */
  @SuppressWarnings("unchecked")
  public static <T> T annotationValue (AnnotationDesc ad,
          String name, Class<T> type) {
    if (ad == null) return null;
    for (ElementValuePair evp : ad.elementValues()) {
      if (evp.element().name().equals(name)) {
        //retrieve the value of the value.  I know right.
        Object value = evp.value().value();

        //Primitives and Strings will be directly matchable
        if (type.isAssignableFrom(value.getClass()))
          return (T) value;

        try {
          //For array Types, the Array will have to be generated.
          //It'll either work or it won't so give up on any one exception.
          if (value instanceof AnnotationValue[]) {
            if (!type.isArray()) return null;
            AnnotationValue[] ava = ((AnnotationValue[]) value);
            Class comp = type.getComponentType();
            Object r = Array.newInstance(comp, ava.length);
            int i = 0;
            for (AnnotationValue av : ava) {
              Array.set(r, i++, av.value());
            }
            return (T) r;
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  /**
   * Finds the specified annotation from the member and returns the specified
   * value, if applicable.
   * 
   * @param <T>
   *          The type that the annotation value should match.
   * @param md
   *          the MemberDoc to search for annotation.
   * @param a
   *          the annotation type to find a match for.
   * @param name
   *          The name of the value method to retrieve that value for
   * @param type
   *          the type of the Annotations method value which is to be retrieved.
   * @return null if either the annotation or the value are not found or cant be
   *         case, else an instance of T from the member.
   */
  public static <T> T annotationValue (MemberDoc md,
          Class<? extends Annotation> a, String name, Class<T> type) {
    return annotationValue(getAnnotation(md, a), name, type);
  }

  /**
   * Returns the AnnotationDesc for the given annotation type assigned to the
   * MemberType (either Field, Class, or Method).
   * 
   * @param md
   *          the MemberDoc to search for annotation.
   * @param a
   *          the annotation type to find a match for.
   * @return null if not found else the AnnotationDesc being polled for.
   */
  public static AnnotationDesc getAnnotation (MemberDoc md,
          Class<? extends Annotation> a) {
    for (AnnotationDesc ad : md.annotations()) {
      if (instanceOf(ad.annotationType(), a))
        return (AnnotationDesc) ad;
    }
    return null;
  }

  /**
   * Checks the com.sun.javadoc.type value and returns true if it is equal to
   * the com.sun.reflect value of the input class, or in the instance of a
   * ClassDoc type, if the type extends or implements the input class
   * 
   * @param t
   *          the javadoc type value
   * @param c
   *          the java to check for
   * @return true if the class is equal or a superclass/superinterface of the
   *         javadoc type.
   */
  public static boolean instanceOf (Type t, Class c) {
    if (t == null) return false;
    if (t.qualifiedTypeName() == c.getCanonicalName()) return true;
    ClassDoc cd = t.asClassDoc();
    if (cd != null) {
      while (cd != null) {
        if (cd.qualifiedName().equals(c.getCanonicalName()))
          return true;
        for (ClassDoc iface : cd.interfaces())
          if (iface.qualifiedName().equals(c.getCanonicalName()))
            return true;
        cd = cd.superclass();
      }
    }
    return false;
  }

  /**
   * Checks the java class against the javadoc type to see if they are equal by
   * package and class name.
   * 
   * @param t
   * @param c
   * @return
   */
  public static boolean sameClass (Type t, Class c) {
    if (t == null) return false;
    if (t.typeName().equals(c.getCanonicalName())) return true;
    return false;
  }

}
