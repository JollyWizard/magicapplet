package magic.doclet.html.blocks;

import magic.html.HTML.classes;
import magic.html.tag.block.Div;
import magic.html.tag.block.H2;
import magic.html.tag.inline.Img;

@classes("scene")
public class SceneDiv extends Div {

  @classes("title")
  public H2 title;

  @classes("screenshot")
  public Img screenshot;

  @classes("changes")
  public Changes changes;

  @classes("actions")
  public Actions actions;

  public static class Actions extends Div {
    @classes("title")
    public H2 title;

    public ActionSetDiv scene;

    public ActionSetDiv drag;

    public Actions() {
      super();
      title.addText("Actions");
      scene.title.setText("Set Scene");
      drag.title.setText("Drag");
    }

    public String toHTML () {
      if (scene.table.rows.size() == 0 && drag.table.rows.size() == 0)
        return "";
      tagLine(title);
      tagLine(scene);
      tagLine(drag);
      return super.toHTML();
    }

    public void setId (String id) {
      setId(id + "_actions");
      scene.setId(id + "_actions_scene");
      scene.setId(id + "_actions_drag");
    }

  }

  public static class Changes extends Div {

    @classes("title")
    public H2 title;

    public ChangeSetDiv added;

    public ChangeSetDiv removed;

    public Changes() {
      super();
      title.setText("Changes");
      added.title.setText("Added");
      removed.title.setText("Removed");
    }

    public String toHTML () {
      if (added.table.rows.size() == 0
              && removed.table.rows.size() == 0) return "";
      tagLine(title);
      tagLine(added);
      tagLine(removed);
      return super.toHTML();
    }

    /* (non-Javadoc)
     * @see magic.html.HTML#setId(java.lang.String)
     */
    @Override
    public void setId (String id) {
      super.setId(id + "_changes");
      added.setId(id + "_added");
      removed.setId(id + "_removed");
    }

  }

  public void setId (String id) {
    super.setId(id);
    changes.setId(id);
  }

  public String toHTML () {
    tagLine(title);
    tagLine(screenshot);
    tagLine(changes);
    tagLine(actions);
    return super.toHTML();
  }

  public static void main (String[] args) {
    SceneDiv s = new SceneDiv();
    s.setId("scene1");
    ActionRow ar = new ActionRow();
    ar.name.setText("Action1");
    ar.description.setText("DESCRIPTION");
    s.actions.drag.table.add(ar);

    s.changes.added.table.add("item 1", "item 2", "item 2");
    s.changes.removed.table.add("item 1", "item 2", "item 2");
    System.out.println(s.toHTML());
  }

}
