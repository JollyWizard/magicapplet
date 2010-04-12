package magic.doclet.html.blocks;

import magic.html.HTML.classes;
import magic.html.tag.block.Div;
import magic.html.tag.block.H2;

@classes("changeset")
public class ChangeSetDiv extends Div {

  public String toHTML () {
    if (table.rows.size() == 0) return "";
    tagLine(title);
    tagLine(table);
    return super.toHTML();
  }

  @classes("title")
  public H2 title;

  @classes("items")
  public ItemTable table;

  public ChangeSetDiv() {
    super();
  }

}
