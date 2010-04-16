package magic.html.head;

import magic.html.*;
import magic.html.attributes.StringAttribute;

@XML.close(true)
@XML.name("link")
public class Link extends HTML {

  @XML.Attribute.name("rel")
  public StringAttribute rel;

  @XML.Attribute.name("type")
  public StringAttribute type;

  @XML.Attribute.name("href")
  public StringAttribute href;

}
