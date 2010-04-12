package magic.html.tag.block.tables;

import java.util.LinkedList;

import magic.html.*;

@XML.name("table")
public class Table<R extends Tr> extends HTML {

  /**
   * The list of rows in the table.
   */
  public LinkedList<R> rows = new LinkedList<R>();

  private Class<R> rowType;

  public Table(Class<R> rowType) {
    super();
    this.rowType = rowType;
  }

  public void add (R row) {
    rows.add(row);
  }

  /**
   * Adds generated header
   */
  @Override
  public String toHTML () {
    Tr headers = Tr.columnHeaders(rowType);
    if (headers != null) {
      tagLine(headers);
    }
    for (R row : rows) {
      tagLine(row);
    }
    return super.toHTML();
  }

}
