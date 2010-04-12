package magic.html.tag.block.tables;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import magic.html.*;
import magic.html.attributes.IntegerAttribute;
import magic.html.attributes.EnumAttribute;

@XML.name("th")
public class Th extends Td {

  @XML.Attribute.name("colspan")
  public IntegerAttribute colspan;

  @XML.Attribute.name("rowspan")
  public IntegerAttribute rowspan;

  @XML.Attribute.name("scope")
  public EnumAttribute<Scope> scope;

  public Th() {
    super();
  }

  public Th(String text) {
    super();
    addText(text);
  }

  @Override
  public void initXMLAttributes () {
    super.initXMLAttributes();
    colspan = new IntegerAttribute();
    rowspan = new IntegerAttribute();
    scope = new EnumAttribute<Scope>(Scope.class);
  }

  public void setScopeCol () {
    scope.set(Scope.col);
  }

  public void setScopeRow () {
    scope.set(Scope.row);
  }

  public static @interface colspan {
    int value();
  }

  public static enum Scope implements
          EnumAttribute.DefaultEnum<Scope> {

    col,
    none,
    row;

    @Override
    public Scope getDefaultValue () {
      return none;
    }

  }

  @Retention(RetentionPolicy.RUNTIME)
  public static @interface text {
    String value();
  }

}
