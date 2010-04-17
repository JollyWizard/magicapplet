package magic.html.tag.list;

import java.util.LinkedList;

import magic.html.HTML;

public class List extends HTML {

    public LinkedList<Li> listItems = new LinkedList<Li>();

    public Li addLi() {
	Li li = new Li();
	listItems.add(li);
	return li;
    }

    /*
     * (non-Javadoc)
     * 
     * @see magic.html.HTML#toHTML()
     */
    @Override
    public String toHTML() {
	newLine();
	for (Li li : listItems)
	    tagLine(li);
	newLine();
	return super.toHTML();
    }

}
