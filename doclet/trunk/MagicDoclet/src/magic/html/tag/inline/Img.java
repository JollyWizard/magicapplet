package magic.html.tag.inline;

import magic.html.HTML;
import magic.html.XML;
import magic.html.attributes.StringAttribute;

@XML.close(true)
@XML.name("img")
public class Img extends HTML {

  @XML.Attribute.name("alt")
  public StringAttribute alt;

  @XML.Attribute.name("src")
  public StringAttribute path;

  public Img() {
    super();
  }

  public Img(String path, String alt) {
    super();
    this.path.set(path);
    this.alt.set(alt);
  }

  public void initXMLAttributes () {
    path = new StringAttribute();
    alt = new StringAttribute();
  }

}
