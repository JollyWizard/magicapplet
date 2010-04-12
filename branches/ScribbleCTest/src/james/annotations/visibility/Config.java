package james.annotations.visibility;

import james.annotations.Map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import magicofcalculus.Component;
import magicofcalculus.Panel;

public class Config {

    public HashMap<Component, HashMap<Integer, Boolean>> show = new HashMap<Component, HashMap<Integer, Boolean>>();

    public Panel parent;

    public Config(Panel parent) {
	this.parent = parent;
    }

    /**
     * Creates the visibility cache based on annotation information.
     */
    public void build() {
	LinkedHashMap<Integer, List<Component>> shows = Map.er.apply(
		Visible.class, parent, Component.class, Visible.show);
	LinkedHashMap<Integer, List<Component>> hides = Map.er.apply(
		Visible.class, parent, Component.class, Visible.hide);

	// stores all annotated collections
	HashSet<Component> all = new HashSet<Component>();

	// find the highest scene listed in an annotation
	int highest = -1;
	for (Integer i : shows.keySet()) {
	    if (i > highest)
		highest = i;
	    for (Component c : shows.get(i))
		all.add(c);
	}
	for (Integer i : hides.keySet()) {
	    if (i > highest)
		highest = i;
	    for (Component c : shows.get(i))
		all.add(c);
	}

	// for each annotated component create a cache listing for showing and
	// hiding
	for (Component c : all) {
	    show.put(c, new HashMap<Integer, Boolean>());
	}

	LinkedList<Component> showing = new LinkedList<Component>();
	LinkedList<Component> hiding = new LinkedList<Component>();
	// because default @Visibles go to 0, all will be assumed hidden, till
	// told otherwise
	hiding.addAll(all);
	for (int i = 0; i <= highest; i++) {
	    // set newly shown items to be shown until told otherwise
	    List<Component> temp = shows.get(i);
	    if (temp != null) {
		showing.addAll(temp);
		hiding.removeAll(temp);
	    }
	    // set newly hidden items to be hidden until noted otherwise
	    temp = hides.get(i);
	    if (temp != null) {
		showing.removeAll(temp);
		hiding.removeAll(temp);
	    }
	    // for all now showing cache true;
	    for (Component c : showing) {
		show.get(c).put(i, true);
	    }
	    // for all now hiding cache false;
	    for (Component c : hiding) {
		show.get(c).put(i, false);
	    }
	}
    }

    public void setScene(int index) {
	for (Component c : show.keySet()) {
	    Boolean _show = show.get(c).get(index);
	    if (_show != null) {
		c.setVisible(_show);
	    }
	}
    }
}
