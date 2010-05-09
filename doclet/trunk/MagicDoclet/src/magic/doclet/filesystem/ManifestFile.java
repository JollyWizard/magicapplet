package magic.doclet.filesystem;

import magic.html.tag.block.tables.Table;
import magic.html.tag.block.tables.Td;
import magic.html.tag.block.tables.Th;
import magic.html.tag.block.tables.Tr;

public class ManifestFile extends PageFile {

    public Table<ItemRow> pages = new Table<ItemRow>(ItemRow.class);

    public Table<ItemRow> images = new Table<ItemRow>(ItemRow.class);

    public ManifestFile() {
	super("manifest");
	page.body.addTag(pages);
	page.body.addTag(images);
    }

    public static class ItemRow extends Tr {

	@Th.text("Name")
	@Td.index(0)
	public Td name;

	@Th.text("File")
	@Td.index(1)
	public Td file;

	@Th.text("Description")
	@Td.index(2)
	public Td description;
    }

}
