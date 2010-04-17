package magic.doclet.html.blocks;

import magic.html.HTML.classes;
import magic.html.tag.block.tables.Td;
import magic.html.tag.block.tables.Th;
import magic.html.tag.block.tables.Tr;

@classes("itemrow")
public class ItemRow extends Tr {
    //
    // @Td.index(1)
    // @Th.text("Action")
    // @classes("action")
    // public Td action;

  @Td.index(1)
  @Th.text("Description")
  @classes("description")
  public Td description;

  @Td.index(0)
  @Th.text("Name")
  @classes("name")
  public Td name;

  public ItemRow() {
    super();
  }

  public ItemRow(String name, String action, String desc) {
    super();
    this.name.addText(name);
    //this.action.addText(action);
    this.description.addText(desc);
  }

}
