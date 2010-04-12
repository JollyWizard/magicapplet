package magic.doclet;

public class HtmlWriter {

  StringBuilder sb = new StringBuilder();

  public HtmlWriter() {
  }

  public void append (Object... appends) {
    for (Object o : appends) {
      if (o != null) sb.append(o.toString());
      sb.append(" ");
    }
  }

  public String classes (String... classes) {
    StringBuilder divb = new StringBuilder();
    if (classes.length > 0) {
      divb.append(" class=\"");
      for (String s : classes) {
        divb.append(s);
        divb.append(" ");
      }
      divb.append("\"");
    }
    return divb.toString();
  }

  public void closeBody () {
    closeTag("body");
  }

  public void closeDiv () {
    closeTag("div");
  }

  public void closeHeader () {
    closeTag("head");
  }

  public void closeP () {
    closeTag("p");
  }

  public void closeTag (String tag) {
    sb.append("</");
    sb.append(tag);
    sb.append(">\n");
  }

  public void heading (int index, String text, String... classes) {
    openTag("h" + index, classes(classes));
    sb.append(text);
    closeTag("h");
  }

  public void img (String url, String alt) {
    openTag("img", "src=\"" + url + '\"', "alt=\"" + alt + '\"');
  }

  public void link (String url, String title, String text) {
    openTag("a", "href=" + url + "\"", "title=" + title + "\"");
    sb.append(text);
    closeTag("a");
  }

  public void openTag (String... tags) {
    sb.append('<');
    for (String tag : tags) {
      sb.append(tag);
      sb.append(" ");
    }
    sb.append('>');
  }

  public void startBody () {
    openTag("body");
  }

  public void startDiv (String... classes) {
    openTag("div", classes(classes));
  }

  public void startHeader () {
    openTag("head");
  }

  public void startP (String... classes) {
    openTag("p", classes(classes));
  }

  /**
   * Appends a self closing tag: &lt;tags[0] ... tags[n] /&gt;</pre>
   * 
   * @param tags
   */
  public void tag (String... tags) {
    sb.append("<");
    for (String tag : tags) {
      sb.append(tag);
      sb.append(" ");
    }
    sb.append(" />\n");
  }

}
