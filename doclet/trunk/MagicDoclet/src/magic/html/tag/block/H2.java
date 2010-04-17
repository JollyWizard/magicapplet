package magic.html.tag.block;

import magic.html.HTML;
import magic.html.XML;

@XML.name("h2")
public class H2 extends HTML {

  public H2() {
    super();
  }

  public H2(String text) {
    super();
    addText(text);
  }

}
