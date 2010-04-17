package magic.doclet.html.blocks;

import magic.html.tag.block.Div;
import magic.html.tag.list.Li;
import magic.html.tag.list.Ul;

@magic.html.HTML.classes("header")
public class SiteHeader extends Div {

    @classes("pagetitle")
    public TitleLink title;

    @classes("logo")
    public Div logo;

    @classes("navmenu")
    public Ul menu;

    public SiteHeader() {
	super();
	setTitle("MAGIC APPLET");
    }

    /**
     * @param text
     * @see magic.doclet.html.blocks.TitleLink#setText(java.lang.String)
     */
    public void setTitle(String text) {
	title.setText(text);
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
	tagLine(title);
	tagLine(menu);
	return super.toHTML();
    }

}
