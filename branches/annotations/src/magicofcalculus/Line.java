//
// Line.java
//
package magicofcalculus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;

import magicofcalculus.components.BezierCurve;
import static java.lang.Math.sqrt;

public class Line extends BezierCurve {

    public Line(Panel panel) {
	super(panel);
	_line = new Line2D.Double();
    }

    public void setLine(double x0, double y0, double x1, double y1) {
	repaint();
	_line.setLine(x0,y0,x1,y1);
	repaint();
    }
    public void setLine(DPoint p0, DPoint p1) {
	repaint();
	_line.setLine(p0.x,p0.y,p1.x,p1.y);
	repaint();
    }
    public void setLine(DPoint midPoint, double slope, double length) {
	repaint();
	_line.setLine(midPoint.x,midPoint.y,midPoint.x+1,midPoint.y+slope);//the length of this line is sqrt(1+m^2)
	DPoint endPoint = getPointAtParamValue((length/2)/(sqrt(1+slope*slope)));
	_line.setLine(midPoint,endPoint);//the length of the line is now length/2
	DPoint startPoint = getPointAtParamValue(-1);
	_line.setLine(startPoint,endPoint);
	repaint();
    }
    
    public void setLength(double tValueAtMidPoint, double length){
	if (getLength()==0.0) return;
	double tForHalfLength = length/2/getLength();
	double tStart = tValueAtMidPoint-tForHalfLength;
	double tEnd = tValueAtMidPoint+tForHalfLength;
	setLine(getPointAtParamValue(tStart),getPointAtParamValue(tEnd));
    }
    public double getLength() {
	return sqrt((_line.x1-_line.x2)*(_line.x1-_line.x2)+(_line.y1-_line.y2)*(_line.y1-_line.y2));
    }
    public double getSlope() {
	return getSlope(_line.x1,_line.y1,_line.x2,_line.y2);
    }
    public static double getSlope(double x1, double y1, double x2, double y2) {
	double dx = x1-x2;
	double dy = y1-y2;
	if (dx==0.0) {
	    if (dy!=0.0) return Double.POSITIVE_INFINITY;
	    if (dy==0.0) return Double.NaN;
	}
	return dy/dx;
    }
    public static double getSlope(DPoint p1, DPoint p2) {return getSlope(p1.x,p1.y,p2.x,p2.y);}
        
    public void setPivotDrag(DPoint pivotPoint) {
	_pivoted = true;
	_pivotPoint.setLocation(pivotPoint);
    }
    public void unsetPivotDrag() {
	_pivoted = false;
    }

    //From BezierCurve
    public DPoint getPointNearestTo(DPoint pt) {
	double c[]={ pt.x-_line.x1, _line.x2-_line.x1 };//modified Bezier coefficients
	double d[]={ pt.y-_line.y1, _line.y2-_line.y1 };
	double t = (c[0]*c[1]+d[0]*d[1])/(c[1]*c[1]+d[1]*d[1]);
	return getPointAtParamValue(t);
    }
    public double getDistanceTo(DPoint pt) {
	if (_infiniteLength) return _line.ptLineDist(pt);
	else return _line.ptSegDist(pt);
    }
    public DPoint getPointAtParamValue(double t){
	double x = (1-t)*_line.x1+t*_line.x2;
	double y = (1-t)*_line.y1+t*_line.y2;
	return new DPoint(x,y);
    }
    
    //From Component
    public void setPosition(DPoint p) {//use the midpoint of the line
	DPoint currentPos = getPosition();
	double translateX = p.x-currentPos.x;
	double translateY = p.y-currentPos.y;
	setLine(_line.x1+translateX,_line.y1+translateY,_line.x2+translateX,_line.y2+translateY);
    }
    public DPoint getPosition() {//returns the midpoint of the line
	int midX = (int)((_line.x1+_line.x2)/2);
	int midY = (int)((_line.y1+_line.y2)/2);
	return new DPoint(midX,midY);
    }
    public void draw(Graphics g){
	if (!_visible) return;
	Graphics2D g2 = (Graphics2D) g.create();
	g2.setStroke(new BasicStroke(_strokeWidth));
	g2.setColor(_color);
	if (_infiniteLength) {
	    g2.draw(_line);//dev
	}
	else g2.draw(_line);
    }
    public void repaint() {
      if (_panel==null) return;
      _panel.repaint(MagicApplet.adjustPaintRect(_line.getBounds()));
    }
    public boolean isHitBy(DPoint pt) {
	if (!_visible) return false;
	return (getDistanceTo(pt)<=HIT_MARGIN);
    }

    public void dragTo(DPoint mousePoint) {

	if (!isDraggable()) return;
	if (!_pivoted) {super.dragTo(mousePoint); return;}

	// do the pivoted drag
	if (_dragMaster==null) {//drag using the _dragHandle
	    DPoint linePt = getPointNearestTo(mousePoint);
	    linePt.translate(mousePoint.x-_dragHandle.x,mousePoint.y-_dragHandle.y);
	    double slope = getSlope(linePt.x,linePt.y,_pivotPoint.x,_pivotPoint.y);
	    setLine(_pivotPoint,slope,getLength());
	    _dragHandle=mousePoint;
	}
	else {//drag using the _dragMaster
	    DPoint dragDest = new DPoint(mousePoint);
	    if (!_dragMaster.getDragDestination(mousePoint,dragDest)) return;
	    double slope = getSlope(dragDest.x,dragDest.y,_pivotPoint.x,_pivotPoint.y);
	    setLine(_pivotPoint,slope,getLength());
	}
	
    }

    
//------------------------------------------------

    private final static int HIT_MARGIN=5;
    private final static int DRAG_ZONE=20;
//    private static int DRAG_ZONE=-1;//no drag zone

    private Line2D.Double _line;

    private boolean _pivoted=false;
    private DPoint _pivotPoint=new DPoint();
}
//------------------------------------------------



