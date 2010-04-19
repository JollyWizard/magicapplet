package magic.doclet.html.blocks;

import magic.doclet.filesystem.PanelPageFile;
import magic.html.HTML.classes;
import magic.html.tag.block.Div;
import magic.html.tag.inline.Img;

@classes("panelsummary")
public class PanelSummaryDiv extends Div {

    public PanelSummaryDiv(PanelPageFile ppf) {
	titlelink.setLink(ppf);
	titlelink.setText(ppf.panelClass.getSimpleName());
	SceneDiv firstScene = ppf.getScene(1);
	if (firstScene != null) {
	    screenshot.copy(firstScene.screenshot);
	}
    }

    @classes("screenshot")
    public Img screenshot;

    public TitleLink titlelink;

    @classes("description")
    public Div description;

    /*
     * (non-Javadoc)
     * 
     * @see magic.html.HTML#toHTML()
     */
    @Override
    public String toHTML() {
	tagLine(titlelink);
	tagLine(screenshot);
	tagLine(description);
	return super.toHTML();
    }

}
