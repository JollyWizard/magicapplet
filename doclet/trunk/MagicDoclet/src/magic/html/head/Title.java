package magic.html.head;

import magic.html.HTML;
import magic.html.XML;

@XML.name("title")
public class Title extends HTML {

    public void set(Object... title) {
	clear();
	for (Object o : title)
	    addText(o.toString());
    }

}
