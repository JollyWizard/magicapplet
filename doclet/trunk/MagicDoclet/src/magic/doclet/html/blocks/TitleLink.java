package magic.doclet.html.blocks;

import magic.doclet.filesystem.PageFile;
import magic.html.HTML.classes;
import magic.html.tag.block.H2;
import magic.html.tag.inline.A;

@classes("titlelink")
public class TitleLink extends H2 {

    public A link;

    /**
     * @param pf
     * @param id
     * @see magic.html.tag.inline.A#setLink(magic.doclet.filesystem.PageFile,
     *      java.lang.String)
     */
    public void setLink(PageFile pf, String id) {
	link.setLink(pf, id);
    }

    /**
     * @param pf
     * @see magic.html.tag.inline.A#setLink(magic.doclet.filesystem.PageFile)
     */
    public void setLink(PageFile pf) {
	link.setLink(pf);
    }

    /**
     * @param title
     * @see magic.html.tag.inline.A#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
	link.setTitle(title);
    }

    public void setText(String text) {
	this.setText((Object) text);
    }

    /**
     * @param text
     * @see magic.html.tag.inline.A#setText(java.lang.Object[])
     */
    public void setText(Object... text) {
	link.setText(text);
    }

    /*
     * (non-Javadoc)
     * 
     * @see magic.html.HTML#toHTML()
     */
    @Override
    public String toHTML() {
	addTag(link);
	return super.toHTML();
    }

}
