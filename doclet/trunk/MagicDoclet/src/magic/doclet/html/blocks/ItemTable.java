package magic.doclet.html.blocks;

import magic.html.tag.block.tables.Table;

public class ItemTable extends Table<ItemRow> {

  public ItemTable() {
    super(ItemRow.class);
  }
  
  public void add(String name, String action, String desc) {
    rows.add(new ItemRow(name, action, desc));
  }
  
}
