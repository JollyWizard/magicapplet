//
// BezierCurve.java
//
// Direct subclasses: Line, QuadCurve
//
package magicofcalculus;

import java.awt.Graphics;

public class BezierCurve extends Component implements DragMaster {

    public BezierCurve(Panel panel) {
	super(panel);
    }

    public void setInfiniteLength(boolean set) {_infiniteLength=set;}
    public boolean isInfiniteLength() {return _infiniteLength;}

    public double[] getBezierPolyCoeffsX() {
	return null;
    }
    public double[] getBezierPolyCoeffsY() {
	return null;
    }
    
    public double getXAtParamValue(double t) {
	return 0;
    }
    public double getYAtParamValue(double t) {
	return 0;
    }
    public DPoint getPointAtParamValue(double t){//no need to override
	return new DPoint(getXAtParamValue(t),getYAtParamValue(t));
    }
    public double getSlopeAtParamValue(double t){
	return 0;
    }

    public int getParamValuesAtX(double x, double[] t) {
	return 0;
    }
    public int getParamValuesAtY(double y, double[] t) {
	return 0;
    }

    public double getParamValueAtPointNearestTo(DPoint pt) {
	return 0;
    }
    public DPoint getPointNearestTo(DPoint pt) {
	double t = getParamValueAtPointNearestTo(pt);
	return getPointAtParamValue(t);
    }
    public double getDistanceTo(DPoint pt) {
	DPoint nearestPoint = getPointNearestTo(pt);
	return nearestPoint.distance(pt);
    }
    
    public Line getTangentLine(DPoint tangentPoint, double length) {
	return null;
    }
    public int getParamValuesAtIntersections(Line line, double[] intersectionParamValues) {
	return 0;
    }
    public int getIntersectionPoints(Line line, DPoint[] intersectionPoints) {
	double[] paramValues={0,0,0,0,0};
	int numberOfIntersections = getParamValuesAtIntersections(line,paramValues);
	for (int i=0; i<intersectionPoints.length && i<numberOfIntersections; i++) 
	    intersectionPoints[i].setLocation(getPointAtParamValue(paramValues[i]));
	return numberOfIntersections;
    }
    
    //From Component
    public void draw(Graphics g){
    }
    public void repaint() {
    }
    public boolean isHitBy(DPoint pt) {
	if (!_visible) return false;
	return (getDistanceTo(pt)<=HIT_MARGIN);
    }

    //From DragMaster
    public boolean getDragDestination(DPoint mousePoint, DPoint dragDestination) {

	DPoint nearestPoint = getPointNearestTo(mousePoint);
	if (DRAG_ZONE<0 || nearestPoint.distance(mousePoint)<=DRAG_ZONE) {
	    dragDestination.setLocation(nearestPoint);
	    return true;
	}
	return false;
    }

//------------------------------------------------
    
    private final static int HIT_MARGIN=5;
//  private final static int DRAG_ZONE=20;
    private static int DRAG_ZONE=-1;//no drag zone

    protected boolean _infiniteLength=false;

}
//------------------------------------------------
//------------------------------------------------



