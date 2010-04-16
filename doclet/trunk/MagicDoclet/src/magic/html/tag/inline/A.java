package magic.html.tag.inline;

import magic.doclet.filesystem.PageFile;
import magic.html.HTML;
import magic.html.XML;
import magic.html.attributes.StringAttribute;

@XML.name("a")
public class A extends HTML {

    @XML.Attribute.name("href")
    public StringAttribute href;

    @XML.Attribute.name("title")
    public StringAttribute title;

    public void setLink(PageFile pf) {
	href.set(pf.path);
    }

    public void setLink(PageFile pf, String id) {
	href.set(pf.path, "#", id);
    }

    public void setTitle(String title) {
	this.title.set(title);
    }

    public void setText(Object... text) {
	addText(text);
    }

}
