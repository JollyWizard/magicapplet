package magic.doclet.filesystem;

import magic.html.HTML;
import magic.html.Page;

/**
 * Acts as a container for HTML contents which are intended to be written to a
 * file.
 * <p>
 * Two variables are stored, the magic.html.Page and the path String which
 * represents the relative URL (including file name) of the file.
 * 
 * @author James Arlow
 * 
 */
public class PageFile {

    /**
     * The actual HTML contents (type: magic.html.page)
     */
    public Page page = new Page();

    /**
     * The path name relative to the root of the file.
     */
    public String path;

    public PageFile(String name) {
	page = new Page();
	path = name + ".html";
    }

    /**
     * @return the HTML contents as a string (from &lt;html&gt; to &lt;/html&gt;
     *         inclusive)
     */
    public String getContents() {
	return page.toHTML();
    }

    public void addToBody(HTML contents) {
	page.body.tagLine(contents);
    }

    public void setTitle(Object... title) {
	page.head.setTitle(title);
    }

}
