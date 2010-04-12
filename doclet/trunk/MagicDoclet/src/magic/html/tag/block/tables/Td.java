package magic.html.tag.block.tables;

import james.annotations.Map;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import magic.html.*;

@XML.name("td")
public class Td extends HTML {
  
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface index {
    @Map.Key
    int value();
  }

}
