package magic.html.head;

import magic.html.HTML;
import magic.html.XML;
import magic.html.attributes.StringAttribute;

@XML.name("script")
public class ScriptFile extends HTML {

  @XML.Attribute.name("src")
  public StringAttribute src;

  @XML.Attribute.name("language")
  public StringAttribute language;

  @XML.Attribute.name("type")
  public StringAttribute type;
  
}
