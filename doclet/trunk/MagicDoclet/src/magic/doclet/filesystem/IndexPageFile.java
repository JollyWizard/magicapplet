package magic.doclet.filesystem;

import magic.doclet.html.blocks.SiteHeader;
import magic.html.head.Stylesheet;

public class IndexPageFile extends PageFile {

    public SiteHeader header = new SiteHeader();

    public IndexPageFile() {
	super("index");
	page.head.styles.add(new Stylesheet("styles/main.css"));
	page.head.styles.add(new Stylesheet("styles/header.css"));
	page.head.styles.add(new Stylesheet("styles/home.css"));
	page.body.addTag(header);
    }

}
