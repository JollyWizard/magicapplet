package magic.html.tag.block.tables;

import james.annotations.Map;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import magic.html.HTML;
import magic.html.XML;


@XML.name("tr")
public class Tr extends HTML {

    public LinkedList<Td> columns = new LinkedList<Td>();

    public Tr(Td... cols) {
	super();
	for (Td td : cols) {
	    columns.add(td);
	}
    }

    /**
     * Generates column Headers from annotations on fields;
     * 
     * @param cl
     * @return
     */
    public static Tr columnHeaders(Class<? extends Tr> cl) {
	if (cl == null || cl.equals(Tr.class))
	    return null;
	Tr r = new Tr();
	HashMap<Integer, List<Field>> fieldMap = Map.er.process(Td.index.class,
		cl);
	for (List<Field> lf : fieldMap.values()) {
	    for (Field f : lf) {
		Th.text title = f.getAnnotation(Th.text.class);
		Th header = new Th(title == null ? "" : title.value());
		Th.colspan colspan = f.getAnnotation(Th.colspan.class);
		if (colspan != null)
		    header.colspan.set(colspan.value());
		header.setScopeCol();
		r.columns.add(header);
	    }
	}
	return r;
    }

    public String toHTML() {
	for (Td td : columns) {
	    addTag(td);
	}
	HashMap<Integer, List<Td>> fieldMap = Map.er.apply(Td.index.class,
		this, Td.class);
	for (Integer i : fieldMap.keySet())
	    for (Td td : fieldMap.get(i))
		contents.add(i, td);
	return super.toHTML();
    }
}
