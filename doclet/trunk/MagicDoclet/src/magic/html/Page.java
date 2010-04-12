package magic.html;

import magic.html.tag.*;

@XML.name("html")
public class Page extends XML {
  
  public Body body = new Body();

  public Head head = new Head();

  @Override
  public void initXMLAttributes () {
    
  }

}
