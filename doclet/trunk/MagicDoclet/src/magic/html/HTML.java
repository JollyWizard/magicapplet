package magic.html;

import james.annotations.AutoCaller;
import james.annotations.TypedCaller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import magic.html.attributes.StringAttribute;
import magic.html.attributes.StringAttributes;
import magic.html.contents.Comment;
import magic.html.contents.HtmlContents;
import magic.html.contents.Text;

@XML.close(false)
public abstract class HTML extends XML implements HtmlContents,
	Iterable<HtmlContents> {

    @XML.Attribute.name("class")
    public StringAttributes classes;

    @XML.Attribute.name("id")
    public StringAttribute id;

    public void setId(String id) {
	this.id.set(id);
    }

    public void clear() {
	contents.clear();
    }

    public void setText(String text) {
	contents.clear();
	addText(text);
    }

    public Iterators iterators = new Iterators();

    public ContentList contents = new ContentList();

    public HTML() {
	super();
	AutoCaller.m.autoCall(this, HTML.classes.class);
    }

    public void initHTMLfields() {

    }

    public void addTag(HTML tag) {
	contents.add(tag);
    }

    public Text addText(Object... s) {
	Text r = new Text(s);
	contents.add(r);
	return r;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<HtmlContents> iterator() {
	return iterators.newIterator(this);
    }

    public Text newLine(Object... s) {
	Text r = new Text('\n');
	r.append(s);
	contents.add(r);
	return r;
    }

    public Text tagLine(HTML tag) {
	contents.add(tag);
	Text r = new Text("\n\t");
	contents.add(r);
	return r;
    }

    /*
     * (non-Javadoc)
     * 
     * @see magic.html.contents.HtmlContents#toHTML()
     */
    @Override
    public String toHTML() {
	StringBuilder r = new StringBuilder();
	boolean first = true;
	boolean closed;
	if (close != null) {
	    r.append(open());
	    closed = close;
	} else {
	    closed = contents.size() == 0;
	    r.append(open(closed));
	}
	if (close == null || !close) {
	    if (contents.size() != 0) {
		for (HtmlContents hc : contents) {
		    r.append(hc.toHTML());
		    first = false;
		}
	    }
	}
	if (!closed)
	    r.append(close());
	r.append(new Comment(name, " ", classes.toDeclaration()).toHTML());
	return r.toString();
    }

    public String toString() {
	return toHTML();
    }

    public static @interface Anchor {
	int index();

	Relative value();
    }

    /**
     * Default CSS classes to be applied in header or on field members.
     * 
     * @author James Arlow
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target( { ElementType.TYPE, ElementType.FIELD })
    public static @interface classes {
	String[] value();

	public static class apply extends TypedCaller<classes, HTML> {

	    @Override
	    public void callTarget(classes a, HTML target) {
		for (String s : a.value()) {
		    target.classes.add(s);
		}
	    }

	}
    }

    public class ContentList extends LinkedList<HtmlContents> {

	@Override
	public boolean add(HtmlContents e) {
	    iterators.notifyAdd(size() - 1);
	    return super.add(e);
	}

	@Override
	public void add(int index, HtmlContents element) {
	    super.add(index, element);
	    iterators.notifyAdd(index);
	}

	public HtmlContents remove(HtmlContents e) {
	    int index = -1;
	    for (int i = 0; i < size(); i++)
		if (get(i) == e) {
		    index = i;
		    break;
		}
	    if (index != -1)
		return remove(index);
	    return null;
	}

	@Override
	public HtmlContents remove(int index) {
	    iterators.notifyRemove(index);
	    return super.remove(index);
	}
    }

    /**
     * An iterator that allows for:each browsing of HtmlContents. Synced through
     * the HTML.Iterators to ensure that modifications are propogated to
     * multiple iterators.
     * 
     * @author James Arlow
     */
    public static class HtmlIterator implements Iterator<HtmlContents> {

	public HtmlContents current;

	public HTML source;

	int index;

	boolean removed;

	public HtmlIterator(HTML src) {
	    source = src;
	    index = -1;
	}

	@Override
	public boolean hasNext() {
	    return index + 1 < source.contents.size();
	}

	@Override
	public HtmlContents next() {
	    if (!hasNext())
		throw new NoSuchElementException();
	    removed = false;
	    return source.contents.get(++index);
	}

	public void notifyAdd(int index2) {
	    if (index2 <= index) {
		index++;
	    }
	}

	public void notifyRemove(int index2) {
	    if (index2 <= index) {
		index--;
	    }
	}

	@Override
	public void remove() {
	    if (removed || index == -1)
		throw new IllegalStateException();
	    source.contents.remove(index);
	    removed = true;
	}
    }

    /**
     * Caches iterators, so that if a modification is made during the iteration
     * process, the indexes will stay in sync.
     * 
     * @author James Arlow
     */
    public class Iterators {

	private List<SoftReference<HtmlIterator>> deadIterators = new LinkedList<SoftReference<HtmlIterator>>();

	private List<SoftReference<HtmlIterator>> openIterators = new LinkedList<SoftReference<HtmlIterator>>();

	private void removeIterators() {
	    for (SoftReference<HtmlIterator> ref : deadIterators) {
		openIterators.remove(ref);
	    }
	    deadIterators.clear();
	}

	protected HtmlIterator newIterator(HTML parent) {
	    HtmlIterator r = new HtmlIterator(parent);
	    SoftReference<HtmlIterator> ref = new SoftReference<HtmlIterator>(r);
	    openIterators.add(ref);
	    return r;
	}

	protected void notifyAdd(int index) {
	    boolean deleteDead = false;
	    for (SoftReference<HtmlIterator> ref : openIterators) {
		HtmlIterator r = ref.get();
		if (r == null) {
		    deadIterators.add(ref);
		    deleteDead = true;
		} else
		    r.notifyAdd(index);

	    }
	    if (deleteDead)
		removeIterators();
	}

	protected void notifyRemove(int index) {
	    boolean deleteDead = false;
	    for (SoftReference<HtmlIterator> ref : openIterators) {
		HtmlIterator r = ref.get();
		if (r == null) {
		    deadIterators.add(ref);
		    deleteDead = true;
		} else
		    r.notifyRemove(index);

	    }
	    if (deleteDead)
		removeIterators();
	}
    }

    public static enum Relative {
	after, before;
    }

}
