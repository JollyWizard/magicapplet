package magic.html.attributes;

import magic.html.XML;

public class IntegerAttribute extends XML.Attribute<Integer> {

  @Override
  public boolean declare () {
    return value != null;
  }

  public int get () {
    return value;
  }

  public void set (Integer i) {
    value = i;
  }

  @Override
  protected String getValueString () {
    return String.valueOf(value);
  }

}
