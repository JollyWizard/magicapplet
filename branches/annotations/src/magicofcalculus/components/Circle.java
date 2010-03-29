//
// Circle.java
//
package magicofcalculus.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import magicofcalculus.Component;
import magicofcalculus.DPoint;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;

public class Circle extends Component {

    public Circle(Panel panel) {
	super(panel);
    }

    public void setCenter(DPoint center) {
	repaint();
	_center.setLocation(center);
	resetShape();
	repaint();
    }

    public void setCenter(double x, double y) {
	setCenter(new DPoint(x, y));
    }

    public DPoint getCenter() {
	return _center;
    }

    public void setDiameter(double diameter) {
	repaint();
	_diameter = diameter;
	resetShape();
	repaint();
    }

    public double getDiameter() {
	return _diameter;
    }

    // From Component
    public void setPosition(DPoint p) {
	setCenter(p);
    }

    public DPoint getPosition() {
	return new DPoint(_center);
    }

    public void draw(Graphics g) {
	if (!_visible)
	    return;
	Graphics2D g2 = (Graphics2D) g.create();

	g2.setColor(_color);
	g2.draw(_ellipse);
	g2.fill(_ellipse);
    }

    public void repaint() {
	if (_panel == null)
	    return;
	_panel.repaint(MagicApplet.adjustPaintRect(_ellipse.getBounds()));
    }

    public boolean isHitBy(DPoint pt) {
	if (!_visible)
	    return false;
	return (_center.distance(pt) <= _diameter + HIT_MARGIN);
    }

    // ------------------------------------------------

    // private final static int HIT_MARGIN=5;
    private final static int HIT_MARGIN = 25;

    private DPoint _center = new DPoint();
    private double _diameter = 14;
    private Ellipse2D.Double _ellipse = new Ellipse2D.Double();;

    private void resetShape() {
	_ellipse.setFrame(_center.x - _diameter / 2, _center.y - _diameter / 2,
		_diameter, _diameter);
    }
}
// ------------------------------------------------

