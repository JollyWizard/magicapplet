package magic.html.head;

public class Stylesheet extends Link {

  public Stylesheet() {
    super();
    rel.set("stylesheet");
    type.set("text/css");
  }

  public Stylesheet(String href) {
    this();
    this.href.set(href);
  }

}
