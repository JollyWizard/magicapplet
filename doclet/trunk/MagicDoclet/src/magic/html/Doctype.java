package magic.html;

import magic.html.contents.HtmlContents;

public enum Doctype implements HtmlContents {

    transitional(
	    "HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"  \"http://www.w3.org/TR/html4/loose.dtd\""), xStrict(
	    "html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");

    String string;

    Doctype(String declaration) {
	string = declaration;
    }

    public String toHTML() {
	return "<!DOCTYPE " + string + ">";
    }

}
