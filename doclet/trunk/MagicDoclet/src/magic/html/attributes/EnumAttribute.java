package magic.html.attributes;

import magic.html.XML.Attribute;

public class EnumAttribute<E extends Enum<E> & EnumAttribute.DefaultEnum<E>>
        extends Attribute {

  public EnumAttribute(Class<E> Class) {
    super();
  }

  @Override
  public boolean declare () {
    if (value == null) return false;
    if (value instanceof DefaultEnum) { return value != ((DefaultEnum) value)
            .getDefaultValue(); }
    return false;
  }

  public void set (E value) {
    this.value = value;
  }

  @Override
  protected String getValueString () {
    return '"' + value.toString() + '"';
  }

  public static interface DefaultEnum<E extends Enum<E>> {
    public E getDefaultValue ();
  }

}
