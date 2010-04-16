package magic.doclet;

import java.util.HashMap;

import james.annotations.drag.Drag;
import james.annotations.scenes.Scene;
import james.annotations.visibility.Visible;

import magic.doclet.filesystem.IndexPageFile;
import magic.doclet.filesystem.PanelPageFile;
import magic.doclet.filesystem.SiteFolder;
import magic.doclet.html.blocks.PanelSummaryDiv;
import magicofcalculus.Component;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import static magic.doclet.Tools.*;

/**
 * @author James Arlow
 * @date today
 */
public class PanelDoc extends Doclet {

    public PanelDoc(RootDoc root) {
	siteRoot = new SiteFolder(Config.outputPath);
	cachePanels(root.classes());
	cacheIndexPage(magicAppletDoc);
	siteRoot.writeFile(indexPage);
	for (PanelPageFile pf : panelPages.values()) {
	    siteRoot.writeFile(pf);
	}
    }

    /**
     * Generates the information for the index page from the magic applet class
     * doc
     * 
     * @param playerDoc
     */
    public void cacheIndexPage(ClassDoc playerDoc) {
	indexPage.setTitle("Magic Applet: Discover the Magic of Calculus");
	MagicApplet player = new MagicApplet();
	player.init();
	for (Panel p : player._panelList) {
	    PanelSummaryDiv psd = new PanelSummaryDiv(panelPages.get(p
		    .getClass()));
	    psd.description.setText(getPanelDescription(p.getClass()));
	    indexPage.addToBody(psd);
	}
    }

    public SiteFolder siteRoot;

    public IndexPageFile indexPage = new IndexPageFile();

    /**
     * Gets cached when searching for panel docs, and is sent back into
     * cacheIndexPage(ClassDoc) when the panels are done(so page indexes are
     * available).
     */
    public ClassDoc magicAppletDoc;

    public HashMap<Class<? extends Panel>, PanelPageFile> panelPages = new HashMap<Class<? extends Panel>, PanelPageFile>();

    /**
     * The cache of each panel doc by panel class for later reference.
     */
    public HashMap<Class<? extends Panel>, ClassDoc> panelDocs = new HashMap<Class<? extends Panel>, ClassDoc>();

    /**
     * @return the description tag from a panels class level javadoc
     * 
     * @param panelClass
     * @return
     */
    public String getPanelDescription(Class<? extends Panel> panelClass) {
	ClassDoc cd = panelDocs.get(panelClass);
	StringBuilder sb = new StringBuilder("");
	for (Tag t : cd.tags("description")) {
	    sb.append(t.text());
	    sb.append("\n");
	}
	return sb.toString();
    }

    public static void main(String[] args) {
	// com.sun.tools.javadoc.Main.execute("magicApplet",
	// "magic.doclet.PanelDoc", new String[] {});
	String[] options = new String[] { "magicofcalculus", "james",
		"-subpackages", "magicofcalculus" };

	com.sun.tools.javadoc.Main.execute("magicApplet",
		"magic.doclet.PanelDoc", options);
    }

    public static boolean start(RootDoc root) {
	System.out.println("Starting:");
	new PanelDoc(root);
	return true;
    }

    /**
     * Caches information needed to write a panel page by analyzing the class
     * related to a ClassDoc known to be for a Panel class
     * 
     * @param doc
     *            A classdoc for a class that extends Panel
     */
    protected void cachePanelInfo(ClassDoc doc) {
	Class<? extends Panel> panelClass;
	panelClass = getRealClass(doc, Panel.class);
	if (panelClass == null) {
	    System.err.println("COULDN'T RETRIEVE CLASS: " + doc);
	    return;
	}
	if (panelClass == Panel.class)
	    return;
	Panel panel = null;
	try {
	    panel = panelClass.newInstance();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	panelDocs.put(panelClass, doc);
	// create panelPage
	panelPages.put(panelClass, new PanelPageFile(panelClass));

	// cache scene config info
	HashMap<Integer, SceneSummary> scenes = new HashMap<Integer, SceneSummary>();
	for (int i = 0; i <= panel.sceneConfig.last; i++) {
	    SceneSummary ss = new SceneSummary(i,
		    panel.sceneConfig.descriptions.get(i));
	    ss.prevBefore = panel.sceneConfig.prev.get(i);
	    ss.nextAfter = panel.sceneConfig.next.get(i);
	    Scene.Action sa = panel.sceneConfig.action.get(i);
	    if (sa != null)
		ss.actions_scene.add(sa.getClass());
	    scenes.put(i, ss);
	}

	// cache drag action info and visibility
	for (FieldDoc f : fieldsOfType(doc, Component.class)) {
	    ClassDoc dh = annotationValue(f, Drag.class, "action",
		    ClassDoc.class);

	    int[] ia = annotationValue(f, Visible.class, "value", int[].class);
	    if (ia != null)
		for (int i : ia) {
		    scenes.get(i).added.add(f);
		}

	    ia = annotationValue(f, Visible.class, "hidden", int[].class);
	    if (ia != null)
		for (int i : ia) {
		    scenes.get(i).added.add(f);
		}
	}

	for (int i : scenes.keySet()) {
	    panelPages.get(panelClass).addToBody(scenes.get(i).buildDiv());
	}
    }

    /**
     * My original testing method. Saved for reference.
     * 
     * @param panel
     */
    public static void writePanelTest(ClassDoc panel) {
	System.out.println(panel.simpleTypeName());
	for (FieldDoc f : panel.fields()) {
	    if (instanceOf(f.type(), Component.class)) {
		System.out.print(f.name());
		System.out.print("\t");
		System.out.println(f.type().simpleTypeName());
		ClassDoc cda = annotationValue(f, Drag.class, "action",
			ClassDoc.class);
		if (cda == null)
		    continue;
		Tag[] ta = cda.tags("manual");
		if (ta.length > 0)
		    System.out.println(ta[0].text());
	    }
	}
    }

    /**
     * Check all docs for panel classes and cache info.
     * 
     * @param classes
     *            the list of ClassDocs to check
     */
    protected void cachePanels(ClassDoc[] classes) {
	for (ClassDoc cd : classes) {
	    if (instanceOf(cd, MagicApplet.class)) {
		magicAppletDoc = cd;
	    }
	    if (instanceOf(cd, Panel.class))
		cachePanelInfo(cd);
	}
    }

}
