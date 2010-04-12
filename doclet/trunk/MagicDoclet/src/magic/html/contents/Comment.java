package magic.html.contents;

/**
 * Represents an HTML Comment that can be inserted
 * 
 * @author James Arlow
 */
public class Comment implements HtmlContents {

  public String contents;

  public Comment(String... contents) {
    StringBuilder sb = new StringBuilder();
    for (String s : contents)
      sb.append(s);
    this.contents = sb.toString();
  }

  public String toHTML () {
    return "<!-- " + contents + " -->";
  }

}
