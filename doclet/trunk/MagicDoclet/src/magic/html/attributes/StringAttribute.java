package magic.html.attributes;

import magic.html.XML;

public class StringAttribute extends XML.Attribute<String> {

    public StringAttribute() {
	super();
    }

    public StringAttribute(String s) {
	super(s);
    }

    @Override
    public boolean declare() {
	return value != null && value != "";
    }

    public String get() {
	return value == null ? "" : getValue();
    }

    public void set(Object... strings) {
	for (Object o : strings)
	    setValue((getValue() == null ? "" : getValue()) + o.toString());
    }

    @Override
    protected String getValueString() {
	return '"' + value + '"';
    }

}
