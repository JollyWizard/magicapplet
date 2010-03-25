package james;

import james.Annotations.AxesProperties;
import james.Annotations.LabelProperties;
import james.Annotations.PolyLineConfig;
import james.Annotations.QuadCurveProperties;
import james.Annotations.Visibility;
import james.Annotations.placement.Position;
import james.Annotations.scenes.Config;

import java.awt.Color;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import magicofcalculus.Component;
import magicofcalculus.DPoint;
import magicofcalculus.Function;
import magicofcalculus.Panel;
import magicofcalculus.QuadCurve;
import magicofcalculus.components.Axes;
import magicofcalculus.components.Label;
import magicofcalculus.components.PolyLine;

public class Tools {

    /**
     * Constructs label and adds to panel.
     * 
     * @param panel
     *            the panel to add the LabelProperties to.
     * @param prop
     *            the properties annotation to add.
     * @return The constructed Label.
     */
    public static Label buildLabel(Panel panel, LabelProperties prop) {
	Label r = new Label(panel);
	if (prop == null)
	    return r;
	if (prop.text() != LabelProperties.Default) {
	    r.setText(prop.text());
	}
	if (prop.image() != LabelProperties.Default) {
	    r.setImage(prop.image());
	    r.setDisplayImage(prop.showImage());
	}
	r.setDraggable(prop.drag());
	r.setBgColor(panel.getBackground());
	r.setOpaque(prop.opaque());
	return r;
    }

    /**
     * initialize all the automated and annotation information of a panel.
     * 
     * @param p
     */
    public static void initializePanel(Panel p) {
	Config c = james.Annotations.scenes.Config.build(p.getClass());
	p.setSceneConfig(c);
	subComponents.buildSubComponents(p);
	buildAxes(p);
	buildQuadCurves(p);
	buildLabels(p);
	cacheVisibility(p);
	setPositions(p);
    }

    public static Class[] primitives = { int.class, short.class, long.class,
	    float.class, double.class, byte.class, char.class };

    /**
     * sets initial Position of Components from @Position annotation
     */
    public static void setPositions(Object o) {
	fields: for (Field f : o.getClass().getFields()) {

	    if (!Component.class.isAssignableFrom(f.getType()))
		continue fields;

	    // if no @Position skip
	    Position pos = f.getAnnotation(Position.class);
	    if (pos == null)
		continue fields;

	    /**
	     * Get component from field
	     */
	    Component com = null;
	    try {
		com = (Component) f.get(o);
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    // if not null then set position
	    if (com != null)
		com.setPosition(pos.x(), pos.y());
	}
    }

    /**
     * Populates Fields of the panel with labels.
     * 
     * @param p
     */
    public static void buildLabels(Object o) {

	// Panel must be used to initialize the components, so the type of
	// object must be tested before we begin
	Panel p = null;
	if (o instanceof Panel)
	    p = (Panel) o;
	else if (o instanceof Component)
	    p = ((Component) o)._panel;

	fields: for (Field f : o.getClass().getFields()) {
	    if (f.getType().equals(Label.class)) {
		try {
		    if (f.get(o) == null) {
			LabelProperties lp = f
				.getAnnotation(LabelProperties.class);
			f.set(o, buildLabel(p, lp));
			Label l = (Label) f.get(o);
			if (l == null)
			    continue fields;

			// Add to draw list
			if (o instanceof Panel) {
			    ((Panel) o)._componentList.add(l);
			} else if (o instanceof SubComponent) {
			    ((SubComponent) o).members.add(l);
			    l.setVisible(true);
			}

			// Diagnostic
			System.out.println("LABEL BUILT: "
				+ o.getClass().getName()
				+ f.getAnnotation(LabelProperties.class)
					.image());
		    } else
			System.err.println("CANNOT ACCESS PRIVATE LABEL: " + f);
		} catch (IllegalAccessException iae) {
		    System.err.println(iae.getMessage());
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    /**
     * Builds all Axes and PolyLines possible from config parameters and adds
     * them to the component list.
     * 
     * @param p
     */
    public static void buildAxes(Panel p) {
	if (axesIndexes.get(p) == null)
	    axesIndexes.put(p, new HashMap<Integer, Axes>());

	List<Field> axisFields = new ArrayList<Field>();
	List<Field> polyLineFields = new ArrayList<Field>();

	Class c = p.getClass();
	do {
	    Field[] fa = c.getFields();

	    for (Field f : fa) {

		if (f.getType().equals(Axes.class)) {
		    axisFields.add(f);
		} else if (f.getType().equals(PolyLine.class)) {
		    polyLineFields.add(f);
		}

	    }
	    if (c != Panel.class)
		c = c.getSuperclass();
	} while (c != Panel.class);
	for (Field f : axisFields) {
	    if (Modifier.isPublic(f.getModifiers())) {
		buildAxis(p, f);
	    } else
		System.err.println("PRIVATE AXES FIELD: "
			+ f.getDeclaringClass() + ":" + f.getName());
	}
	for (Field f : polyLineFields) {
	    if (Modifier.isPublic(f.getModifiers())) {
		buildPolyLine(p, f);
	    } else
		System.err.println("PRIVATE POLYLINE FIELD: "
			+ f.getDeclaringClass() + ":" + f.getName());
	}
    }

    public static void buildQuadCurve(Panel p, Field f) {
	QuadCurve qc = new QuadCurve(p);
	QuadCurveProperties qp = f.getAnnotation(QuadCurveProperties.class);
	if (qp == null)
	    return;
	qc.setCurve(qp.start().x(), qp.start().y(), qp.control().x(), qp
		.control().y(), qp.end().x(), qp.end().y());
	try {
	    f.set(p, qc);
	    p._componentList.add(qc);
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
    }

    public static void buildQuadCurves(Panel p) {
	Class c = p.getClass();
	do {
	    Field[] fa = c.getFields();

	    for (Field f : fa) {
		if (Modifier.isPublic(f.getModifiers())) {
		    buildQuadCurve(p, f);
		} else {
		    System.err.println("PRIVATE QuadCurve FIELD: "
			    + f.getDeclaringClass() + ":" + f.getName());
		}
	    }
	    if (c != Panel.class)
		c = c.getSuperclass();
	} while (c != Panel.class);
    }

    /**
     * Builds an axis in a target panel using a field which has configuration
     * annotations. Returns null if @AxesProperties is not found.
     * <ol>
     * <li>Constructs axes
     * <li>adds to components
     * <li>caches index
     * <li>configures position in panel
     * </ol>
     * 
     * @param p
     *            the panel
     * @param f
     *            the field for which the index should be set.
     */
    public static void buildAxis(Panel p, Field f) {
	AxesProperties ap = f.getAnnotation(AxesProperties.class);
	if (ap == null)
	    return;
	try {
	    Axes a = new Axes(p);
	    a.setAxesInPanel(new DPoint(ap.origin().x(), ap.origin().y()), ap
		    .width(), ap.height());
	    a.setAxesLocal(ap.localW(), ap.localH());
	    axesIndexes.get(p).put(ap.index(), a);
	    f.set(p, a);
	    p._componentList.add(a);
	} catch (Exception e) {
	    System.err.println("COULDN'T CONSTRUCT AXIS: "
		    + p.getClass().getSimpleName() + ":" + f.getName());
	    System.err.println(e.getMessage());
	}
    }

    /**
     * Stores axis indexes so that multi-graph panels can link to correct
     * graphs.
     */
    public static HashMap<Panel, HashMap<Integer, Axes>> axesIndexes = new HashMap<Panel, HashMap<Integer, Axes>>();

    public static Color getColor(String s) {
	Field f;
	try {
	    f = Color.class.getField(s);
	    if (f != null && Modifier.isStatic(f.getModifiers())
		    && f.getType().equals(Color.class)) {
		return (Color) f.get(null);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Builds a polyline from a config annotation and adds to panel _component
     * list
     * 
     * @param p
     * @param f
     */
    public static void buildPolyLine(Panel p, Field f) {
	PolyLineConfig plc = f.getAnnotation(PolyLineConfig.class);
	if (plc == null)
	    return;
	Axes a = axesIndexes.get(p).get(plc.axes());
	PolyLine pl = getPolyLineFromFunction(a, plc);
	try {
	    f.set(p, pl);
	    p._componentList.add(pl);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Builds a polyline from an axes and a config annotation
     * 
     * @param config
     * @return
     */
    public static PolyLine getPolyLineFromFunction(Axes axes,
	    PolyLineConfig config) {
	Function function;
	try {
	    function = config.function().newInstance();
	    PolyLine r = axes.getPolyLineFromFunction(config.intervals(),
		    config.leftXLocal(), config.rightXLocal(), function);
	    Color c = james.Tools.getColor(config.color());
	    r.setColor(c);
	    return r;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static boolean mouseDiagnostic = false;

    public static void cacheVisibility(Panel panel) {
	List<Field> fl = getFieldsWith(panel.getClass(), Visibility.class);
	for (Field f : fl) {
	    panel.sceneVisibility.put(f, f.getAnnotation(Visibility.class));
	}
    }

    public static List<Field> getFieldsWith(Class c,
	    Class<? extends Annotation> cl) {
	LinkedList<Field> r = new LinkedList<Field>();
	for (Field f : c.getFields()) {
	    if (f.isAnnotationPresent(cl)) {
		r.add(f);
	    }
	}
	return r;
    }

    /**
     * Holds methods for dealing with subComponents
     * 
     * @author James Arlow
     */
    public static class subComponents {
	/**
	 * Initializes sub component fields (but not fields)
	 * 
	 * @param p
	 */
	public static void buildSubComponents(Panel p) {

	    for (Field f : p.getClass().getFields()) {
		Class c = f.getType();
		if (SubComponent.class.isAssignableFrom(c)) {

		    SubComponent sc = null;
		    try {
			// Find Constructor and execute
			if (Modifier.isStatic(c.getModifiers())) {
			    sc = (SubComponent) f.getType().getConstructor(
				    Panel.class).newInstance(p);
			} else {
			    sc = (SubComponent) f.getType().getConstructor(
				    p.getClass(), Panel.class)
				    .newInstance(p, p);
			}

			if (sc != null) {
			    f.set(p, sc);

			    // Add to Draw List
			    p._componentList.add(sc);

			    buildLabels(sc);
			    setPositions(sc);
			}

		    } catch (Exception e) {
			System.err.println(e.getMessage());
		    }
		}
	    }
	}
    }

}
