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
    public StringAttribute src;

    public Img() {
	super();
    }

    public Img(String path, String alt) {
	super();
	set(path, alt);
    }

    public void initXMLAttributes() {
	src = new StringAttribute();
	alt = new StringAttribute();
    }

    public void set(String path, String alt) {
	this.src.set(path);
	this.alt.set(alt);
    }

    public void copy(Img dataSource) {
	src.set(dataSource.src.get());
	alt.set(dataSource.alt.get());
    }

}
