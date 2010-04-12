package magic.doclet;

import java.util.HashMap;

import james.annotations.drag.Drag;
import james.annotations.scenes.Scene;
import james.annotations.visibility.Visible;

import magicofcalculus.Component;
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
    writePanels(root.classes());
  }

  public static void main (String[] args) {
    //    com.sun.tools.javadoc.Main.execute("magicApplet",
    //            "magic.doclet.PanelDoc", new String[] {});
    String[] options =
            new String[] {
                "magicofcalculus",
                "james",
                "-subpackages",
                "magicofcalculus",
                "-subpackages",
                "james"
            };

    com.sun.tools.javadoc.Main.execute("magicApplet",
            "magic.doclet.PanelDoc", options);
  }

  public static boolean start (RootDoc root) {
    System.out.println("Starting:");
    new PanelDoc(root);
    return true;
  }

  /**
   * Writes a single ClassDoc known to be a panel
   * 
   * @param doc
   */
  protected static void writePanel (ClassDoc doc) {
    Class<? extends Panel> panelClass;
    panelClass = getRealClass(doc, Panel.class);
    if (panelClass == null) {
      System.err.println("COULDN'T RETRIEVE CLASS: " + doc);
      return;
    }
    if (panelClass == Panel.class) return;
    Panel panel = null;
    try {
      panel = panelClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    HashMap<Integer, SceneSummary> scenes =
            new HashMap<Integer, SceneSummary>();
    for (int i = 0; i <= panel.sceneConfig.last; i++) {
      SceneSummary ss =
              new SceneSummary(i, panel.sceneConfig.descriptions
                      .get(i));
      ss.prevBefore = panel.sceneConfig.prev.get(i);
      ss.nextAfter = panel.sceneConfig.next.get(i);
      Scene.Action sa = panel.sceneConfig.action.get(i);
      if (sa != null) ss.actions_scene.add(sa.getClass());
      scenes.put(i, ss);
    }

    for (FieldDoc f : fieldsOfType(doc, Component.class)) {
      ClassDoc dh =
              annotationValue(f, Drag.class, "action", ClassDoc.class);

      int[] ia =
              annotationValue(f, Visible.class, "value", int[].class);
      if (ia != null) for (int i : ia) {
        scenes.get(i).added.add(f);
      }

      ia = annotationValue(f, Visible.class, "hidden", int[].class);
      if (ia != null) for (int i : ia) {
        scenes.get(i).added.add(f);
      }
    }
    for (int i : scenes.keySet()) {
      System.out.println(scenes.get(i).buildDiv());
    }
  }

  /**
   * My original testing method. Saved for reference.
   * 
   * @param panel
   */
  public static void writePanelTest (ClassDoc panel) {
    System.out.println(panel.simpleTypeName());
    for (FieldDoc f : panel.fields()) {
      if (instanceOf(f.type(), Component.class)) {
        System.out.print(f.name());
        System.out.print("\t");
        System.out.println(f.type().simpleTypeName());
        ClassDoc cda =
                annotationValue(f, Drag.class, "action",
                        ClassDoc.class);
        if (cda == null) continue;
        Tag[] ta = cda.tags("manual");
        if (ta.length > 0) System.out.println(ta[0].text());
      }
    }
  }

  /**
   * Determine class type before forwarding to appropriate method
   * 
   * @param classes
   */
  protected void writePanels (ClassDoc[] classes) {
    for (ClassDoc cd : classes) {
      if (instanceOf(cd, Panel.class)) writePanel(cd);
      //  if (instanceOf(cd, AutoCaller.class)) System.err.println(cd);
    }
  }

}
