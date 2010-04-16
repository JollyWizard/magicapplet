package magic.doclet.filesystem;

import magic.doclet.html.blocks.SceneDiv;
import magic.html.contents.HtmlContents;
import magic.html.head.Stylesheet;
import magicofcalculus.Panel;

public class PanelPageFile extends PageFile {

    public Class<? extends Panel> panelClass;

    public PanelPageFile(Class<? extends Panel> panelClass) {
	super(panelClass.getSimpleName());
	this.panelClass = panelClass;
	page.head.styles.add(new Stylesheet("styles/main.css"));
	setTitle("MagicApplet | Panel " + panelClass.getSimpleName());
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
