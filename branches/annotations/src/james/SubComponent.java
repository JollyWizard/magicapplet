package james;

import james.annotations.Map;
import james.annotations.labels.Opaque;
import james.annotations.placement.zIndex;

import java.awt.Graphics;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import magicofcalculus.Component;
import magicofcalculus.DPoint;
import magicofcalculus.Panel;

/**
 * TODO Add local visibility methods and initializers
 * 
 * @author James Arlow
 * 
 */
public class SubComponent extends Component {

    public DPoint upperLeftCorner = new DPoint(0, 0);

    public LinkedList<Component> members = new LinkedList<Component>();

    public DPoint lowerLeftCorner = new DPoint(0, 0);

    /**
     * @param panel
     *            The parent panel.
     */
    public SubComponent(Panel panel) {
	super(panel);
	QuickInit.Build(this, panel);
	AutoCaller.m.autoCall(this);
	LinkedHashMap<Integer, List<Component>> all = Map.er.apply(
		zIndex.class, this, Component.class);
	for (List<Component> cl : all.values())
	    for (Component c : cl) {
		members.add(0,c);
		c.setVisible(true);
	    }
    }

    /**
     * Translates the graphics context into a local context and draws all
     * members to it using their local positions.
     */
    @Override
    public void draw(Graphics g) {
	if (!_visible)
	    return;
	DPoint origin = getPosition();
	Graphics subg = g.create();
	subg.translate((int) origin.x, (int) origin.y);

	for (int i = members.size() - 1; i >= 0; i--) {
	    Component c = members.get(i);
	    if (c != null)
		c.draw(subg);
	}
    }

    /**
     * @param target
     * @return true if target is a member component
     */
    public boolean hasComponent(Component target) {
	for (Component c : members) {
	    if (c == target)
		return true;
	}
	return false;
    }

    /**
     * Gets the position of the component in the Subcomponents parents
     * coordinate space.
     * 
     * @param target
     * @return
     */
    public DPoint getPosition(Component target) {
	DPoint r = target.getPosition();
	DPoint local = getPosition();
	r.translate(local.x, local.y);
	return r;
    }

    /**
     * Translates a local point by pos and sets targets position.
     * 
     * @param target
     * @param pos
     */
    public void setRelativePosition(Component target, DPoint pos) {
	DPoint local = getPosition();
	local.translate(pos.x, pos.y);
	target.setPosition(pos);
    }

    /**
     * Sets the position reference equal to the input parameter
     */
    public void setPosition(DPoint pos) {
	upperLeftCorner = pos;
    }

    /**
     * Returns a copy of the local Position (Upper left corner)
     */
    public DPoint getPosition() {
	return new DPoint(upperLeftCorner.x, upperLeftCorner.y);
    }

    /**
     * Returns the position translated into the local coordinate space.
     * 
     * @param extern
     *            The point from the external coordinate system to translate
     *            into local coordinates
     * @return The local translation of the external point
     */
    public DPoint getPosition(DPoint extern) {
	DPoint r = getPosition();
	r.x = r.x - extern.x;
	r.y = r.y - extern.y;
	return r;
    }

}
