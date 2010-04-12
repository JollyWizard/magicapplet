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
  public boolean declare () {
    return value != null && value != "";
  }

  public String get () {
    return value == null ? "" : getValue();
  }

  public void set (String s) {
    setValue(s);
  }

  @Override
  protected String getValueString () {
    return '"' + value + '"';
  }

}
