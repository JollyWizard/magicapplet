package magic.doclet.html.blocks;

import magic.html.HTML.classes;
import magic.html.tag.block.Div;
import magic.html.tag.block.H2;
import magic.html.tag.block.tables.Table;

@classes("actionset")
public class ActionSetDiv extends Div {

  @classes("title")
  public H2 title;

  public ActionTable table;

  public String toHTML () {
    if (table.rows.size() == 0) return "";
    tagLine(title);
    tagLine(table);
    int i = 0;
    for (ActionRow ar : table.rows)
      ar.setId(id.get() + ++i);
    return super.toHTML();
  }

  public static class ActionTable extends Table<ActionRow> {

    public ActionTable() {
      super(ActionRow.class);
    }

  }

}
