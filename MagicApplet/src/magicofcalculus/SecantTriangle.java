//
// SecantTriangle.java
//
package magicofcalculus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;
import java.awt.geom.Path2D;

public class SecantTriangle extends Component {

    public SecantTriangle(Panel panel) {
	super(panel);
	_innerThicknessPath.setWindingRule(Path2D.WIND_EVEN_ODD);
    }
    
    public void setTriangle(DPoint tangentPoint, DPoint secantPoint) {
	setTangentPoint(tangentPoint);
	setSecantPoint(secantPoint);
    }
    public void setTangentPoint(DPoint tangentPoint) {
	repaint();
	_tangentPoint.setLocation(tangentPoint);
	resetShape();
	repaint();
    }
    public void setSecantPoint(DPoint secantPoint) {
	repaint();
	_secantPoint.setLocation(secantPoint);
	resetShape();
	repaint();
    }
    public DPoint getSecantPoint() {return new DPoint(_secantPoint);}
    public DPoint getTangentPoint() {return new DPoint(_tangentPoint);}
    public DPoint getCornerPoint() {return new DPoint(_secantPoint.getX(),_tangentPoint.getY());}

    public void setOutlineOnly(boolean set) {
	repaint();
	_outlineOnly=set;
	repaint();
    }
    public void setInnerThickness(double thickness) {
	repaint();
	_innerThickness = thickness;
//	if (_innerThickness<0.0) {_innerThickness=0.0; return;}
	resetShape();
	repaint();
    }
    
    //From Component
    public void setPosition(DPoint p) {//only sets the x coordinate of the _secantPoint
	repaint();
	_secantPoint.x=p.x;
	resetShape();
	repaint();
    }
    public DPoint getPosition() {return getCornerPoint();}
    public void draw(Graphics g){
	if (!_visible) return;
	
	Graphics2D g2 = (Graphics2D) g.create();
	g2.setStroke(new BasicStroke(_strokeWidth));
	g2.setColor(_color);
	g2.draw(_triangle);	
	if (!_outlineOnly) g2.fill(_triangle);
	if (_innerThickness>0.0) g2.fill(_innerThicknessPath);
	
    }
    public void repaint() {
	if (_panel==null) return;
	_panel.repaint(MagicApplet.adjustPaintRect(_triangle.getBounds(),(int)ceil(getStrokeWidth()/1)));
    }
    public boolean isHitBy(DPoint pt) {
	if (!_visible) return false;
//	return (_triangle.distance(pt)<=HIT_MARGIN);
	return (_triangle.contains(pt));	
	//dev
//	boolean hit = (_triangle.contains(pt));
//	MagicApplet.printDiagnostic(this,"Hit: "+hit);
//	return hit;

    }   

//------------------------------------------------

    private void resetShape() {
	_triangle.setTriangle(_tangentPoint,_secantPoint,getCornerPoint());
	
	_innerThicknessPath.reset();
	if (_innerThickness>0.0) {
	    _innerThicknessPath.moveTo(getCornerPoint().x,getCornerPoint().y);
	    _innerThicknessPath.lineTo(getTangentPoint().x,getTangentPoint().y);
	    _innerThicknessPath.lineTo(getSecantPoint().x,getSecantPoint().y);
	    _innerThicknessPath.closePath();
	    double a = getCornerPoint().x-getTangentPoint().x;
	    double b = getCornerPoint().y-getSecantPoint().y;
	    double c = (a+sqrt(a*a+b*b))/b*_innerThickness;
	    double d = (b+sqrt(a*a+b*b))/a*_innerThickness;
	    if (a>c+_innerThickness && b>d+_innerThickness) {
		_innerThicknessPath.moveTo(getCornerPoint().x-_innerThickness,getCornerPoint().y-_innerThickness);
		_innerThicknessPath.lineTo(getTangentPoint().x+c,getTangentPoint().y-_innerThickness);
		_innerThicknessPath.lineTo(getSecantPoint().x-_innerThickness,getSecantPoint().y+d);
		_innerThicknessPath.closePath();
	    }
	}
	
    }
    
//------------------------------------------------

    //private final static int HIT_MARGIN=5;

    private TriangleShape _triangle = new TriangleShape();
    private DPoint _tangentPoint = new DPoint();
    private DPoint _secantPoint = new DPoint();

    private boolean _outlineOnly=false;
    
    private double _innerThickness=0.0;
    private Path2D.Double _innerThicknessPath = new Path2D.Double();
    
}
//------------------------------------------------
//------------------------------------------------
