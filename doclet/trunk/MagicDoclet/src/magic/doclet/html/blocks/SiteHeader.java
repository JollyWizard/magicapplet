package magic.doclet.html.blocks;

import magic.doclet.Config;
import magic.html.tag.block.Div;
import magic.html.tag.inline.Img;
import magic.html.tag.list.Li;
import magic.html.tag.list.Ul;

@magic.html.HTML.classes("header")
public class SiteHeader extends Div {

    @classes("title")
    public logoDiv logo;

    @classes("clear")
    public Div clear;

    public static class logoDiv extends TitleLink {

	@classes("logo")
	public Img img;

	public String toHTML() {
	    tagLine(img);
	    return super.toHTML();
	}

    }

    @classes("navmenu")
    public Ul menu;

    public SiteHeader() {
	super();
	setTitle("MAGIC APPLET");
	logo.img.set(Config.logoLocal, "Applet Logo");
    }

    /**
     * @param text
     * @see magic.doclet.html.blocks.TitleLink#setText(java.lang.String)
     */
    public void setTitle(String text) {
	logo.setText(text);
    }

    /**
     * @return
     * @see magic.html.tag.list.List#addLi()
     */
    public Li addMenuItem() {
	return menu.addLi();
    }

    /*
     * (non-Javadoc)
     * 
     * @see magic.html.HTML#toHTML()
     */
    @Override
    public String toHTML() {
	tagLine(logo);
	tagLine(menu);
	tagLine(clear);
	return super.toHTML();
    }

}
