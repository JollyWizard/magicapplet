package magic.doclet.filesystem;

import magic.doclet.html.blocks.SceneDiv;
import magic.doclet.html.blocks.SiteHeader;
import magic.html.contents.HtmlContents;
import magic.html.head.Stylesheet;
import magicofcalculus.Panel;

public class PanelPageFile extends PageFile {

    public Class<? extends Panel> panelClass;

    public SiteHeader header = new SiteHeader();

    public PanelPageFile(Class<? extends Panel> panelClass) {
	super(panelClass.getSimpleName());
	this.panelClass = panelClass;
	page.head.styles.add(new Stylesheet("styles/main.css"));
	page.head.styles.add(new Stylesheet("styles/header.css"));
	setTitle("MagicApplet | Panel " + panelClass.getSimpleName());
	page.body.addTag(header);
    }

    public SceneDiv getScene(int i) {
	for (HtmlContents hc : page.body) {
	    if (hc instanceof SceneDiv) {
		SceneDiv sd = (SceneDiv) hc;
		if (sd.isScene(i))
		    return sd;
	    }
	}
	return null;
    }

}