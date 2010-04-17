package magic.doclet.filesystem;

import magic.doclet.html.blocks.SiteHeader;

public class IndexPageFile extends PageFile {

    public IndexPageFile() {
	super("index");
	page.body.addTag(new SiteHeader());
    }

}
