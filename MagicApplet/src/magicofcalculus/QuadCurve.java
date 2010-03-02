//
// QuadCurve.java
//
package magicofcalculus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.CubicCurve2D;

public class QuadCurve extends BezierCurve {

    public QuadCurve(Panel panel) {
	super(panel);
    }

    public void setCurve(double startX, double startY, double ctrlX, double ctrlY, double endX, double endY) {
	_quadCurve.setCurve(startX,startY,ctrlX,ctrlY,endX,endY);
    }

    //From BezierCurve
    public double[] getBezierPolyCoeffsX() {
	return new double[] {_quadCurve.x1, 2*(_quadCurve.ctrlx-_quadCurve.x1), _quadCurve.x2-2*_quadCurve.ctrlx+_quadCurve.x1};
    }
    public double[] getBezierPolyCoeffsY() {
	return new double[] {_quadCurve.y1, 2*(_quadCurve.ctrly-_quadCurve.y1), _quadCurve.y2-2*_quadCurve.ctrly+_quadCurve.y1};
    }
    
    public double getXAtParamValue(double t) {
	return (1-t)*(1-t)*_quadCurve.x1 + 2*t*(1-t)*_quadCurve.ctrlx + t*t*_quadCurve.x2;
    }
    public double getYAtParamValue(double t) {
	return (1-t)*(1-t)*_quadCurve.y1 + 2*t*(1-t)*_quadCurve.ctrly + t*t*_quadCurve.y2;
    }
    public DPoint getPointAtParamValue(double t){
	return new DPoint(getXAtParamValue(t),getYAtParamValue(t));
    }
    public double getSlopeAtParamValue(double t){
	
	double[] c = getBezierPolyCoeffsX();
	double[] d = getBezierPolyCoeffsY();
	
	double dc = 2*c[2]*t+c[1];
	double dd = 2*d[2]*t+d[1];
	
	if (dc==0.0) return Double.NaN;
	else return dd/dc;

    }

    public int getParamValuesAtX(double x, double[] t) {

	//Solve x(t)-x=0
	double[] c = getBezierPolyCoeffsX();
	c[0] = c[0]-x;
	double[] xRoots={0,0};
	int numberOfXRoots = QuadCurve2D.solveQuadratic(c,xRoots);//can be 0,1,or2

	//dev
//	String msg = "QuadCurve: # of x roots="+numberOfXRoots;
//	for (double r : xRoots) msg = msg.concat(" "+r);
//	MagicApplet.printDiagnostic(this,msg);
	
	//Load up the result array, and return.  t should be length 2,
	//but use the for loop just in case it's not.  
	//If not _infiniteLength, only return roots between 0 and 1.
	int numRootsReturned=0;
	for (int i=0; numRootsReturned<t.length && i<numberOfXRoots; i++) {
	    if (_infiniteLength || 0<=xRoots[i]&&xRoots[i]<=1) {
		t[numRootsReturned] = xRoots[i];
		numRootsReturned++;
	    }
	}
	return numRootsReturned;

    }
    public int getParamValuesAtY(double y, double[] t) {
	
	//Solve y(t)-y=0
	double[] d = getBezierPolyCoeffsY();
	d[0] = d[0]-y;
	double[] yRoots={0,0};
	int numberOfYRoots = QuadCurve2D.solveQuadratic(d,yRoots);

	//Load up the result array, and return.  t should be length 2,
	//but use the for loop just in case it's not.  
	//If not _infiniteLength, only return roots between 0 and 1.
	int numRootsReturned=0;
	for (int i=0; numRootsReturned<t.length && i<numberOfYRoots; i++) {
	    if (_infiniteLength || 0<=yRoots[i]&&yRoots[i]<=1) {
		t[numRootsReturned] = yRoots[i];
		numRootsReturned++;
	    }
	}
	return numRootsReturned;

    }

    public double getParamValueAtPointNearestTo(DPoint pt) {

	// let (a,b) be the given point
	double a = pt.x;
	double b = pt.y;

	//get the Bezier polynomial coefficients, and modify the constant term
	double[] c = getBezierPolyCoeffsX();
	c[0] = c[0]-a;
	double[] d = getBezierPolyCoeffsY();
	d[0] = d[0]-b;
	
	//set up and solve the cubic polynomial
	double[] coeffs = { c[0]*c[1]               + d[0]*d[1] ,
			    c[1]*c[1] + 2*c[0]*c[2] + d[1]*d[1] + 2*d[0]*d[2] ,
			    3*c[1]*c[2]             + 3*d[1]*d[2] ,
			    2*c[2]*c[2]             + 2*d[2]*d[2]
			    };
	double[] roots = {0,0,0};
	int numberOfRoots = CubicCurve2D.solveCubic(coeffs,roots);

        //Set t to the only root.  There will always be at least one root since we're
	//solving a cubic.
	//dev (!) need to deal with multiple roots in general.  But for now, this works 
	//with this particular curve as long as the mouse is close to the curve.
	double t=roots[0];
	if (!_infiniteLength) {
	    if (t<0) t=0;
	    if (t>1) t=1;
	}

	return t;
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
	Line line = new Line(_panel);
	double t = getParamValueAtPointNearestTo(tangentPoint);
	double slope = getSlopeAtParamValue(t);
	line.setLine(tangentPoint,slope,length);
	return line;
    }
    public int getParamValuesAtIntersections(Line line, double[] intersectionParamValues) {

	DPoint lineStartPt = line.getPointAtParamValue(0);
	double m = line.getSlope();

	double[] c = getBezierPolyCoeffsX();
	c[0] = c[0]-lineStartPt.x;
	double[] d = getBezierPolyCoeffsY();
	d[0] = d[0]-lineStartPt.y;
	double[] coeffs = {d[0]-m*c[0],d[1]-m*c[1],d[2]-m*c[2]};
	double[] roots={0,0};
	int numberOfRoots = QuadCurve2D.solveQuadratic(coeffs,roots);

//	for (int i=0; i<intersectionParamValues.length && i<numberOfRoots; i++) 
//	    intersectionParamValues[i]=roots[i];
//	return numberOfRoots;
	int numRootsReturned=0;
	for (int i=0; numRootsReturned<intersectionParamValues.length && i<numberOfRoots; i++) {
	    if (_infiniteLength || 0<=roots[i]&&roots[i]<=1) {
		intersectionParamValues[numRootsReturned] = roots[i];
		numRootsReturned++;
	    }
	}
	return numRootsReturned;

    }
    public int getIntersectionPoints(Line line, DPoint[] intersectionPoints) {
	double[] paramValues={0,0};
	int numberOfIntersections = getParamValuesAtIntersections(line,paramValues);
	for (int i=0; i<intersectionPoints.length && i<numberOfIntersections; i++) 
	    intersectionPoints[i].setLocation(getPointAtParamValue(paramValues[i]));
	return numberOfIntersections;
    }
    
    //From Component
    public void draw(Graphics g){
	if (!_visible) return;
	Graphics2D g2 = (Graphics2D) g.create();
	g2.setStroke(new BasicStroke(_strokeWidth));
	g2.setColor(_color);
	g2.draw(_quadCurve);
	//dev,this line "closes the shape", it's why we can't use _quadCurve.contains(pt) in isSelectedBy()
	//g2.drawLine((int)_quadCurve.x1,(int)_quadCurve.y1,(int)_quadCurve.x2,(int)_quadCurve.y2);
    }
    public void repaint() {
      if (_panel==null) return;
      _panel.repaint(MagicApplet.adjustPaintRect(_quadCurve.getBounds()));
    }
    public boolean isHitBy(DPoint pt) {
	if (!_visible) return false;
	return (getDistanceTo(pt)<=HIT_MARGIN);
    }

//------------------------------------------------
    
    private final static int HIT_MARGIN=5;
//  private final static int DRAG_ZONE=20;
    private static int DRAG_ZONE=-1;//no drag zone

    private QuadCurve2D.Double _quadCurve = new QuadCurve2D.Double();

}
//------------------------------------------------
//------------------------------------------------



