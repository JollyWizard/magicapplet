package magic.html;

@XML.name("html")
public class Page extends HTML {

    public String path;

    public Body body;

    public Head head;

    public String toHTML() {
	newLine();
	tagLine(head);
	newLine();
	tagLine(body);
	return super.toHTML();
    }

}
