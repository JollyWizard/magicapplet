package magic.doclet.screenshots;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import magic.doclet.filesystem.SiteFolder;
import magicofcalculus.Panel;
import wade.SavableGraphics;

public class PanelScreenShot extends SavableGraphics {

    public String path = "images/";

    public PanelScreenShot(Panel p, int scene) {
	super(800, 600);
	p.setScene(scene);
	Graphics2D g = getGraphics2D();
	g.setBackground(new Color(0xdd, 0xdd, 0xdd));
	g.fillRect(0, 0, 800, 600);
	p.paintComponent(g);
	path += p.getClass().getSimpleName() + "_scene" + (scene + 1) + ".png";
    }

    public boolean write(SiteFolder sf) {
	try {
	    WriteImage(sf.getRelativePath(path.replace("/", "\\")),
		    SGF_TYPE_PNG);
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

}
