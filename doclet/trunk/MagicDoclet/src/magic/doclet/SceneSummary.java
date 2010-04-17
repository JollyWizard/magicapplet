package magic.doclet;

import james.annotations.scenes.Scene;
import james.annotations.scenes.Scene.Action;

import java.util.LinkedList;
import java.util.List;

import magic.doclet.html.blocks.ActionRow;
import magic.doclet.html.blocks.ItemRow;
import magic.doclet.html.blocks.SceneDiv;
import magic.doclet.screenshots.PanelScreenShot;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Tag;

public class SceneSummary {

    public int index;

    public String name;

    public PanelScreenShot screenshot;

    public Boolean nextAfter;

    public Boolean prevBefore;

    public List<FieldDoc> added = new LinkedList<FieldDoc>();

    public List<FieldDoc> removed = new LinkedList<FieldDoc>();

    public List<Class<? extends Scene.Action>> actions_scene = new LinkedList<Class<? extends Scene.Action>>();

    public List<ClassDoc> actions_drag = new LinkedList<ClassDoc>();

    /**
     * @param index
     *            the scene index (in java) the named index of the panel will be
     *            equal to index+1 (for normal people)
     * @param name
     *            The scene name/description
     */
    public SceneSummary(int index, String name) {
	this.index = index + 1;
	this.name = name;
    }

    public String toString() {
	return index + " | " + name + "( show: " + added.size() + " | hide: "
		+ removed.size() + " ) ";
    }

    public SceneDiv buildDiv() {
	SceneDiv r = new SceneDiv();
	r.setId(index);
	r.title.setText(name);
	r.screenshot.set(screenshot.path, "Screenshot for scene " + index);
	for (int i = 0; i < added.size(); i++)
	    r.changes.added.table.add(buildItemRow(added, i));
	for (int i = 0; i < removed.size(); i++)
	    r.changes.removed.table.add(buildItemRow(removed, i));
	for (int i = 0; i < actions_scene.size(); i++)
	    r.actions.scene.table.add(buildActionRow(actions_scene, i));
	for (int i = 0; i < actions_drag.size(); i++)
	    r.actions.scene.table.add(buildDragActionRow(i));
	return r;
    }

    private ActionRow buildActionRow(List<Class<? extends Action>> actions,
	    int i) {
	Class<? extends Action> action = actions.get(i);
	ActionRow r = new ActionRow();
	r.name.setText(action.getSimpleName());
	r.description.setText("description goes here");
	return r;
    }

    private ActionRow buildDragActionRow(int i) {
	ClassDoc action = actions_drag.get(i);
	ActionRow r = new ActionRow();
	r.name.setText(action.name());
	r.description.setText("description goes here");
	return r;
    }

    public ItemRow buildItemRow(List<FieldDoc> lf, int index) {
	FieldDoc f = lf.get(index);
	String name = f.name();
	for (Tag t : f.tags("name")) {
	    name = t.text();
	}
	String action = null;
	// add action anchor here.
	String description = "";
	for (Tag t : f.tags("description"))
	    description = t.text();

	return new ItemRow(f.name(), action == null ? "action" : action,
		description);
    }

}
