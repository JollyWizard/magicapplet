package magic.doclet.html.blocks;

import magic.html.HTML.classes;
import magic.html.tag.block.tables.Td;
import magic.html.tag.block.tables.Th;
import magic.html.tag.block.tables.Tr;

@classes("action")
public class ActionRow extends Tr {

  @Td.index(0)
  public Td name;

  @Th.text("Description")
  @Td.index(1)
  public Td description;

}
