package magic.doclet;

import static magic.doclet.Tools.annotationValue;
import static magic.doclet.Tools.fieldsOfType;
import static magic.doclet.Tools.getRealClass;
import static magic.doclet.Tools.instanceOf;
import james.annotations.drag.Drag;
import james.annotations.scenes.Scene;
import james.annotations.visibility.Visible;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import magic.doclet.filesystem.IndexPageFile;
import magic.doclet.filesystem.ManifestFile;
import magic.doclet.filesystem.PageFile;
import magic.doclet.filesystem.PanelPageFile;
import magic.doclet.filesystem.SiteFolder;
import magic.doclet.html.blocks.PanelSummaryDiv;
import magic.doclet.html.blocks.SceneDiv;
import magic.doclet.screenshots.PanelScreenShot;
import magic.html.HTML;
import magic.html.contents.HtmlContents;
import magic.html.tag.inline.A;
import magic.html.tag.inline.Img;
import magic.html.tag.list.Li;
import magicofcalculus.Component;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

/**
 * This is the doclet class for generating the project manual.
 * <p>
 * The main method starts the javadoc utility programatically.
 * <p>
 * Doclets contain a static method {@link #start(RootDoc)} which the javadoc
 * utility will call once it is done prepro0cessing the class files. The RootDoc
 * is a container for all ClassDoc and PackageDoc (s) generated during the
 * preprocessing phase.
 * <p>
 * When the static method is called it creates a new instance of this class,
 * which then processes the information from the docs and generates the file.
 * <p>
 * In order to avoid reprocessing all the runtime configurations from the
 * ClassDocs this implementation uses instances of the actual class to retrieve
 * the information as it is generated for runtime behavior. It also uses those
 * instances to generate screenshots via programmatic control of the panels
 * behavior.
 * <p>
 * For memory issues see project wiki entry on doclet configuration
 * 
 * @author James Arlow
 * @date March-April, 2010
 */
public class PanelDoc extends Doclet {

    public PanelDoc(RootDoc root) {
	siteRoot = new SiteFolder(Config.outputPath);

	Class c = getClass();
	if (Config.freshstart) {
	    siteRoot.copyResource(c, SiteFolder.images, Config.logoPath);
	    siteRoot.copyResource(c, SiteFolder.styles, Config.homeCSS);
	    siteRoot.copyResource(c, SiteFolder.styles, Config.mainCSS);
	    siteRoot.copyResource(c, SiteFolder.styles, Config.headerCSS);
	}

	cachePanels(root.classes());
	cacheIndexPage(magicAppletDoc);
	buildMenus();

	siteRoot.writeFile(indexPage);

	generateManifest();
	siteRoot.writeFile(manifest);
	for (PanelPageFile pf : panelPages.values()) {
	    siteRoot.writeFile(pf);
	}
	// write screenshots
	for (HashMap<Integer, SceneSummary> hm : panelScenes.values()) {
	    for (SceneSummary ss : hm.values()) {
		ss.screenshot.write(siteRoot);
	    }
	}
    }

    public ManifestFile manifest = new ManifestFile();

    public void generateManifest() {
	ManifestFile.ItemRow row = new ManifestFile.ItemRow();
	row.name.addText("Home");
	row.description.addText("Root Page with Panel Navigation");
	row.file.addText("index.html");
	manifest.pages.add(row);
	for (Class c : panelPages.keySet()) {
	    row = new ManifestFile.ItemRow();
	    row.name.addText(c.getSimpleName());
	    row.description.addText("Panel page for ", c.getSimpleName());
	    row.file.addText(panelPages.get(c).path);
	    manifest.pages.add(row);
	}
	for (Class c : panelScenes.keySet()) {
	    for (HashMap<Integer, SceneSummary> hm : panelScenes.values()) {
		for (Integer i : hm.keySet()) {
		    row = new ManifestFile.ItemRow();
		    row.name.addText(c.getSimpleName() + " Screen "
			    + hm.get(i).index);
		    row.description.addText("Screenshot for Scene "
			    + hm.get(i).index);
		    row.file.addText(hm.get(i).screenshot.path);
		    manifest.images.add(row);
		}
	    }
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
	for (Panel p : applet._panelList) {
	    PanelSummaryDiv psd = new PanelSummaryDiv(panelPages.get(p
		    .getClass()));

	    psd.description.setText(getPanelDescription(p.getClass()));
	    indexPage.addToBody(psd);
	}
    }

    public MagicApplet applet = new MagicApplet();
    {
	applet.init();
    }

    public void buildMenus() {
	LinkedList<Li> menuItems = new LinkedList<Li>();
	HashSet<Panel> already = new HashSet<Panel>();

	Li home = new Li();
	A homeLink = new A();
	homeLink.setLink(indexPage);
	homeLink.setText("HOME");
	home.addTag(homeLink);
	menuItems.add(home);

	for (Panel p : applet._panelList) {
	    Li menuItem = new Li();
	    if (!already.contains(p)) {
		A link = new A();
		link.addText(p.getClass().getSimpleName());
		link.setLink(panelPages.get(p.getClass()));
		menuItem.addTag(link);
		menuItems.add(menuItem);
	    }
	    already.add(p);
	}

	for (Li li : menuItems) {
	    for (PanelPageFile ppf : panelPages.values()) {
		ppf.header.menu.listItems.add(li);
	    }
	    indexPage.header.menu.listItems.add(li);
	}

	for (PanelPageFile ppf : panelPages.values()) {
	    ppf.header.logo.setLink(indexPage);
	}
	indexPage.header.logo.setLink(indexPage);

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

    public HashMap<Class<? extends Panel>, HashMap<Integer, SceneSummary>> panelScenes = new HashMap<Class<? extends Panel>, HashMap<Integer, SceneSummary>>();

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

	// cache panel info
	panelDocs.put(panelClass, doc);
	panelPages.put(panelClass, new PanelPageFile(panelClass));

	HashMap<Integer, SceneSummary> scenes = new HashMap<Integer, SceneSummary>();
	panelScenes.put(panelClass, scenes);

	// cache scene config info
	for (int i = 0; i <= panel.sceneConfig.last; i++) {
	    SceneSummary ss = new SceneSummary(i,
		    panel.sceneConfig.descriptions.get(i));
	    ss.prevBefore = panel.sceneConfig.prev.get(i);
	    ss.nextAfter = panel.sceneConfig.next.get(i);
	    Scene.Action sa = panel.sceneConfig.action.get(i);
	    if (sa != null)
		ss.actions_scene.add(sa.getClass());

	    // generate screenshot

	    ss.screenshot = new PanelScreenShot(panel, i);

	    // cache scene
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
