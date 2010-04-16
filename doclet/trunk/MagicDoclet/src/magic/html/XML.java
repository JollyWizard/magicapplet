package magic.html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import james.Collect;
import james.QuickInit;
import james.annotations.AutoCaller;
import james.annotations.TypedCaller;

public abstract class XML {

    /**
     * The tag name i.e. &lt;name &gt;
     */
    public String name;

    /**
     * Indicates that a tag should always(true) or never(false) close itself.
     */
    protected Boolean close;

    /**
     * Default Constructor. Initializes names and attributes @see
     * {@link #initXMLAttributes()
     */
    public XML() {
	QuickInit.Build(this);
	AutoCaller.m.autoCall(this, XML.name.class, XML.close.class);
	initXMLAttributes();
	HashMap<Field, Attribute> hm = Collect.fieldMap(this,
		XML.Attribute.class);
	for (Field f : hm.keySet())
	    AutoCaller.m.autoCall(f, this);
    }

    public String close() {
	return "</" + name + ">";
    }

    /**
     * Ensures that all XML attributes are initialized before their field
     * Annotations are AutoCalled
     */
    public void initXMLAttributes() {
    }

    /**
     * Creates an opening tag that is not self closing:
     * <p>
     * "&lt;name attribute1="x" attribute2=3 ... &gt;"
     * <p>
     * equivalent to {@link #open(boolean)} : (false)
     * 
     * @return the string for the tag that is generated
     */
    public String open() {
	return open(close);
    }

    /**
     * Creates an opening tag (tag with attributes) that self closes if close =
     * true
     * 
     * @param close
     *            true if the tag should self close (lone tag)
     * @return <pre>
     * if (close)
     *  "&lt;name attribute1="x" attribute2=3 ... /&gt;"
     * else 
     *  "&lt;name attribute1="x" attribute2=3 ... &gt;"
     * </pre>
     */
    public String open(Boolean close) {
	StringBuilder r = new StringBuilder();
	// Open tag in the standard fashion, with "<name "
	r.append("<");
	r.append(name);
	r.append(" ");
	// collect and print attributes values
	Set<Attribute> attrs = Collect.fieldValues(this, XML.Attribute.class);
	for (Attribute a : attrs) {
	    r.append(a.toDeclaration());
	    r.append(" ");
	}
	// single line tag if close is true
	if (close != null && close)
	    r.append("/");
	r.append(">");
	return r.toString();
    }

    /**
     * @return the name
     */
    protected String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    protected void setName(String name) {
	this.name = name;
    }

    /**
     * Acts as a buffer to XML tag attributes. So the can be set, analyzed, and
     * written without.
     * 
     * @author James Arlow
     * @param <VALUETYPE>
     *            The type of value that the annotation stores (so that only
     *            appropriate values are set before writing)
     */
    public abstract static class Attribute<VALUETYPE> {

	public String name;

	protected VALUETYPE value;

	public Attribute() {
	    AutoCaller.m.autoCall(this);
	}

	/**
	 * Creates the Attribute and sets the default value
	 * 
	 * @param value
	 */
	public Attribute(VALUETYPE value) {
	    this.value = value;
	}

	public abstract boolean declare();

	public String toDeclaration() {
	    if (!declare())
		return "";
	    StringBuilder r = new StringBuilder();
	    r.append(name);
	    r.append("=");
	    r.append(getValueString());
	    return r.toString();
	}

	/**
	 * @return the value
	 */
	protected VALUETYPE getValue() {
	    return value;
	}

	protected abstract String getValueString();

	/**
	 * @param value
	 *            the value to set
	 */
	protected void setValue(VALUETYPE value) {
	    this.value = value;
	}

	/**
	 * An annotation to set the default attribute name for a class or field
	 * XML.Attribute
	 * 
	 * @author James Arlow
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.TYPE, ElementType.FIELD })
	public static @interface name {
	    public String value();

	    public static class Apply extends
		    TypedCaller<Attribute.name, Attribute> {
		@SuppressWarnings("unchecked")
		@Override
		public void callTarget(name annote, Attribute target) {
		    target.name = annote.value();
		}
	    }
	}

    }

    /**
     * An annotation to set the default tag name for an XML tag class.
     * 
     * @author James Arlow
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface name {
	public String value();

	public static class Apply extends TypedCaller<name, XML> {

	    @Override
	    public void callTarget(name annote, XML target) {
		target.name = annote.value();
	    }

	}
    }

    /**
     * indicates that the tag should always(true) or never(false) close itself
     * when generating an open tag.
     * 
     * @author James Arlow
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target( { ElementType.TYPE, ElementType.FIELD })
    public static @interface close {
	/**
	 * indicates that the tag should always(true) or never(false) close as a
	 * single tag.
	 */
	public boolean value() default true;

	public static class Apply extends TypedCaller<close, XML> {

	    @Override
	    public void callTarget(close a, XML target) {
		target.close = a.value();
	    }
	}
    }

}
