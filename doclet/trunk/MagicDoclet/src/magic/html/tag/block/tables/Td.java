package magic.html.tag.block.tables;

import james.annotations.Map;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import magic.html.HTML;
import magic.html.XML;

@XML.name("td")
public class Td extends HTML {

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface index {
	@Map.Key
	int value();
    }

    /*
     * (non-Javadoc)
     * 
     * @see magic.html.HTML#toHTML()
     */
    @Override
    public String toHTML() {
	return super.toHTML();
    }

}
