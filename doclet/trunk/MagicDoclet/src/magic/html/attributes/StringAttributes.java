package magic.html.attributes;

import java.util.LinkedList;
import java.util.List;

import magic.html.XML;

public class StringAttributes extends XML.Attribute<List<String>> {

  /**
   * Builds the list
   */
  public StringAttributes() {
    super(new LinkedList<String>());
  }

  /**
   * Adds a class String to the list if it is not already present
   * 
   * @param s
   *          the class String to add
   * @return true if the item was added, false if it was already present.
   */
  public boolean add (String s) {
    boolean already = false;
    for (String v : value) {
      if (s == v) {
        already = true;
        break;
      }
    }
    if (!already) {
      value.add(s);
    }
    return (!already);
  }

  /**
   * if no classes are present, this element will not be written, else they will
   * be listed per toDeclaration();
   */
  @Override
  public boolean declare () {
    return value.size() != 0;
  }

  public String get (int i) {
    if (i < 0 || i > value.size()) return "";
    return value.get(i);
  }

  public String[] getAll () {
    return value.toArray(new String[value.size()]);
  }
  
  @Override
  protected String getValueString () {
    StringBuilder sb = new StringBuilder();
    sb.append('"');
    for (String s : value) {
      sb.append(s);
      sb.append(" ");
    }
    sb.append('"');
    return sb.toString();
  }

}
