package james.Annotations.scenes;

import java.util.HashMap;
import java.util.LinkedList;

import magicofcalculus.Panel;

/**
 * Stores the processed configuration information for the scenes from
 * {@link Scenes#value()}.
 * 
 * @author James Arlow
 */
public class Config {

    /**
     * The index of the last scene (0 inclusive). Defaults to -1 if no scenes
     */
    public int last = -1;
    /**
     * The cache of scene descriptions
     */
    public LinkedList<String> descriptions = new LinkedList<String>();
    /**
     * If a scene has true mapped to its index, next panel
     */
    public HashMap<Integer, Boolean> next = new HashMap<Integer, Boolean>();
    /**
     * If a scene has true mapped to its index, last panel
     */
    public HashMap<Integer, Boolean> prev = new HashMap<Integer, Boolean>();

    /**
     * Analyzes Scenes Annotation and builds config information.
     * 
     * @param p
     */
    public static Config build(Class<? extends Panel> c) {
	// return value
	Config r = new Config();

	Scenes ss = c.getAnnotation(Scenes.class);

	// TODO add error conditions (Exceptions)
	if (ss == null || ss.value().length == 0)
	    return r;

	// Extract Scene[] from Scenes
	Scene[] sa = ss.value();

	// figure out the number of scenes
	int highest = -1;
	for (Scene s : sa) {
	    if (s.index() > highest)
		highest = s.index();
	}
	r.last = highest;

	Scene lastScene = null;
	Scene currentScene;
	all: for (int i = 0; i <= highest; i++) {

	    // first find if current scene has a marker
	    // if not then the last one will be used.
	    currentScene = null;
	    find: for (Scene s : sa) {
		if (s.index() == i) {
		    currentScene = s;
		    break find;
		}
	    }

	    /*
	     * Use currentScene, lastScene, or "" based on availability
	     */
	    if (currentScene != null
		    && currentScene.description() != Scene.DEFAULT) {
		// if current scene found
		r.descriptions.add(currentScene.description());
		lastScene = currentScene;
		continue all;
	    } else if (lastScene != null
		    && lastScene.description() != Scene.DEFAULT) {
		// if last scene is available
		r.descriptions.add(lastScene.description());
		continue all;
	    } else {
		// or just ""
		r.descriptions.add("");
		continue all;
	    }
	}

	// Cache panel redirects
	for (int i = sa.length - 1; i > -1; i--) {
	    Scene s = sa[i];
	    if (s.next() == true) {
		r.next.put(s.index(), true);
	    }
	    if (s.prev() == true) {
		r.prev.put(s.index(), true);
	    }
	}
	// 0 always prev panels, last always next panels.
	// TODO check to see what problems this might cause.
	r.prev.put(0, true);
	r.next.put(r.last, true);

	// DEBUG
	System.out.println(c.getName());
	int d = 0;
	for (int i = 0; i < r.descriptions.size(); i++) {
	    System.out.print(d++ + "\t" + r.descriptions.get(i));
	    System.out.print("\t"
		    + (r.prev.get(i) != null && r.prev.get(i) == true ? "prev"
			    : ""));
	    System.out.print("\t"
		    + (r.next.get(i) != null && r.next.get(i) == true ? "next"
			    : ""));
	    System.out.println("");
	}
	System.out.println();

	return r;
    }

}
