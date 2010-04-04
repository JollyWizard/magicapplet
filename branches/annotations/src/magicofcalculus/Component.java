//
// Component.java
//
// Subclasses: Axes, BezierCurve, Circle, Label, (Line), PolyLine, (QuadCurve), SecantTriangle, Rect
//
package magicofcalculus;

import james.annotations.drag.Drag;

import java.awt.Graphics;
import java.awt.Color;

public class Component extends Object {

    /**
     * @param panel
     *            The root level container used to perform concise repaints
     */
    public Component(Panel panel) {
	_panel = panel;
    }

    public boolean isVisible() {
	return _visible;
    }

    public void setVisible(boolean set) {
	_visible = set;
	repaint();
    }

    public boolean isSelectable() {
	return _selectable || _draggable;
    }// if a component is draggable, then it's also selectable

    public void setSelectable(boolean set) {
	_selectable = set;
    }

    public boolean isDraggable() {
	return _draggable;
    }

    public void setDraggable(boolean set) {
	_draggable = set;
    }

    public void setPosition(DPoint p) {
    }// override this one

    public final void setPosition(double x, double y) {
	setPosition(new DPoint(x, y));
    }// not this one

    public DPoint getPosition() {
	return new DPoint();
    }

    public void setColor(Color color) {
	_color = color;
	repaint();
    }

    public void setStrokeWidth(double width) {
	repaint();
	_strokeWidth = (float) width;
	repaint();
    }

    public double getStrokeWidth() {
	return _strokeWidth;
    }

    public void draw(Graphics g) {
    }

    public void repaint() {
    }

    // support for dragging
    public boolean isHitBy(DPoint pt) {
	return false;
    }

    public void startDrag(DPoint mousePoint) {
	if (!isDraggable())
	    return;
	_dragHandle = mousePoint;
    }

    public Drag.Handler dMaster2;

    public void dragTo(DPoint mousePoint) {
	// this provides common drag operations, (1) a simple drag that uses a
	// _dragHandle,
	// and (2) a drag that asks a _dragMaster where to go. Override this in
	// dervied classes for anything fancier.

	if (!isDraggable())
	    return;

	if (_dragMaster == null) {// drag using the _dragHandle
	    DPoint pos = getPosition();
	    pos.translate(mousePoint.x - _dragHandle.x, mousePoint.y
		    - _dragHandle.y);
	    setPosition(pos);
	    _dragHandle = mousePoint;
	} else {// drag using the _dragMaster
	    DPoint dragDest = new DPoint();
	    if (!_dragMaster.getDragDestination(mousePoint, dragDest))
		return;
	    setPosition(dragDest);
	}

	if (dMaster2 != null)
	    dMaster2.action();

    }

    public void stopDrag() {
    }

    public void setDragMaster(DragMaster dragMaster) {
	_dragMaster = dragMaster;
    }// call setDragMaster(null) to remove a dragMaster

    // From DragMaster
    // public boolean getDragDestination(DPoint mousePoint, DPoint
    // dragDestination) {return false;}
    // dev, why doesn't this work? why doesn't this get overridden?

    // ------------------------------------------------

    /**
     * The root level container used to perform concise repaints
     */
    public Panel _panel = null;

    protected DPoint _dragHandle = null;

    protected boolean _visible = false;

    protected boolean _selectable = false;

    protected boolean _draggable = false;

    /**
     * <p>
     * 
     * @deprecated
     */
    protected DragMaster _dragMaster = null;

    protected Color _color = Color.black;

    /**
     * @return the _color
     */
    public Color getColor() {
	return _color;
    }

    protected float _strokeWidth = 3.5f;
}
// ------------------------------------------------
