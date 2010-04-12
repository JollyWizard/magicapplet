package magic.html.contents;

import java.util.HashMap;

public class Text implements HtmlContents {

  StringBuilder builder = new StringBuilder();

  /**
   * Creates the Text object, and appends all Strings.
   * @param strings
   */
  public Text(Object... strings) {
    for (Object o : strings)
      builder.append(o);
  }

  /**
   * A mapping of HTML entities that require escape sequences.
   */
  public static HashMap<String, String> entities =
          new HashMap<String, String>();

  public static String escape (char c) {
    return escape(String.valueOf(c));
  }

  public static String escape (String rawValue) {
    String r = entities.get(rawValue);
    if (r != null) return r;
    return rawValue;
  }

  /**
   * Adds standard entities.
   */
  static {
    entities.put("<", "&lt;");
    entities.put(">", "&gt;");
    entities.put("&", "&amp;");
  }

  public void append (Object... objs) {
    for (Object o : objs) {
      builder.append(o);
    }
  }

  /**
   * Resets the length of the StringBuilder to 0.
   */
  public void clear () {
    builder.setLength(0);
  }

  /**
   * Prints the text as html-safe (entity escaped) String literal
   */
  @Override
  public String toHTML () {
    StringBuilder r = new StringBuilder();
    for (char c : builder.toString().toCharArray()) {
      r.append(escape(c));
    }
    return r.toString();
  }

  public String toString() {
    return toHTML();
  }
  
}
