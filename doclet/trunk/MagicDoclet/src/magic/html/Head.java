package magic.html;

import java.util.LinkedList;
import java.util.List;

import magic.html.head.ScriptFile;
import magic.html.head.Stylesheet;
import magic.html.head.Title;

@XML.name("head")
public class Head extends HTML {

    public Title title;
    public List<Stylesheet> styles = new LinkedList<Stylesheet>();
    public List<ScriptFile> scripts = new LinkedList<ScriptFile>();

    public void setTitle(Object... title) {
	this.title.set(title);
    }

    public String toHTML() {
	tagLine(title);
	for (Stylesheet ss : styles) {
	    tagLine(ss);
	}
	for (ScriptFile sf : scripts) {
	    tagLine(sf);
	}
	return super.toHTML();
    }
}
