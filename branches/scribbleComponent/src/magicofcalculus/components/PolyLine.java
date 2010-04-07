//
// PolyLine.java
//
package magicofcalculus.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.geom.Path2D;
import java.awt.BasicStroke;
import static java.lang.Math.round;
import java.awt.geom.AffineTransform;

import magicofcalculus.Component;
import magicofcalculus.DPoint;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;

public class PolyLine extends Component {

    public PolyLine(Panel panel) {
	super(panel);
    }
    public PolyLine(Panel panel, PolyLine pl) {
	super(panel);
	_pointList = pl.getPointList();
	resetPathFromPointList(_pointList);
    }
    
    public void setPolyLine(int numPoints, double[] xPoints, double[] yPoints) {
	if (numPoints>xPoints.length || numPoints>yPoints.length) return;
	_pointList.ensureCapacity(numPoints);
	for (int i=0; i<numPoints; i++) _pointList.add(new DPoint(xPoints[i],yPoints[i]));
	resetPathFromPointList(_pointList);
    }
    public void transform(AffineTransform at) {
	for (int i=0; i<_pointList.size(); i++) at.transform(_pointList.get(i),_pointList.get(i));
	resetPathFromPointList(_pointList);
    }

    public DPoint getFirstPoint() {
	if (_pointList.size()<1) return new DPoint();
	else return new DPoint(_pointList.get(0));
    }
    public DPoint getLastPoint() {
	if (_pointList.size()<1) return new DPoint();
	else return new DPoint(_pointList.get(_pointList.size()-1));
    }

    public ArrayList<DPoint> getPointList() {
	ArrayList<DPoint> newList = new ArrayList<DPoint>();
	newList.addAll(_pointList);
	return newList;
	}
    public ArrayList<DPoint> getPointList(double fromX, double toX) {
	ArrayList<DPoint> pointArray = new ArrayList<DPoint>();
	
	if (_pointList.size()<1) return pointArray;

	double firstX = _pointList.get(0).x;
	double lastX = _pointList.get(_pointList.size()-1).x;
	
	//limit testing
	if (toX<fromX || lastX<fromX || toX<firstX) return pointArray;
	if (fromX==lastX) {
	    pointArray.add(_pointList.get(_pointList.size()-1));
	    return pointArray;
	}
	if (toX==firstX) {
	    pointArray.add(_pointList.get(0));
	    return pointArray;
	}

	//at this point, we have fromX<=toX and fromX<lastX and firstX<toX
	
	//get the first point
	DPoint firstPoint = new DPoint();
	int indexOfSecondPoint=0;
	if (fromX<firstX) {
	    firstPoint.setLocation(_pointList.get(0));
	    indexOfSecondPoint=1;
	}
	else {
	    for (int i=0; i<_pointList.size(); i++) {
		if (fromX<_pointList.get(i).x) {
		    double fromY = _pointList.get(i-1).y + (fromX-_pointList.get(i-1).x)/(_pointList.get(i).x-_pointList.get(i-1).x)*(_pointList.get(i).y-_pointList.get(i-1).y);
		    firstPoint.setLocation(fromX,fromY);
//		    indexOfSecondPoint=i+1;
		    indexOfSecondPoint=i;//dev ?
		    break;
		}
	    }
	}
	pointArray.add(firstPoint);
	//at this point, we still have i<_pointList.size() becuase of the checking done above
	
	//load the intermediate points into the array while checking for the last point
	DPoint lastPoint = new DPoint();
	for (int i=indexOfSecondPoint; i<_pointList.size(); i++) {
	    if (_pointList.get(i).x<toX) pointArray.add(_pointList.get(i));
	    else {
		DPoint prevPoint = pointArray.get(pointArray.size()-1);
		double toY = prevPoint.y + (toX-prevPoint.x)/(_pointList.get(i).x-prevPoint.x)*(_pointList.get(i).y-prevPoint.y);
		lastPoint.setLocation(toX,toY);
		pointArray.add(lastPoint);//sb here
		break;
	    }
	}
//	pointArray.add(lastPoint);//bug

	return pointArray;
	
    }
    
    public void setDrawingInterval(double fromX, double toX) {
	repaint();
	_path.reset();
	ArrayList<DPoint> pointList = getPointList(fromX,toX);
	resetPathFromPointList(pointList);
	repaint();
    }
	    
    //From Component
    public boolean isHitBy(DPoint pt) {return _path.contains(pt);}   
    public void setPosition(DPoint pt) {
	double tx = pt.x-_pointList.get(0).x;
	double ty = pt.y-_pointList.get(0).y;
	transform(AffineTransform.getTranslateInstance(tx,ty));
    }
    public DPoint getPosition() {return new DPoint(_pointList.get(0));}
    public void draw(Graphics g){
	if (!_visible) return;
	Graphics2D g2 = (Graphics2D) g.create();
	g2.setStroke(new BasicStroke(_strokeWidth));
	g2.setColor(_color);
	g2.draw(_path);	
    }
    public void repaint() {
	if (_panel==null) return;
	_panel.repaint(MagicApplet.adjustPaintRect(_path.getBounds()));
    }
    
//-------------------------------------

    private void resetPathFromPointList(ArrayList<DPoint> pointList) {
	repaint();
	_path.reset();
	_path.moveTo(pointList.get(0).x,pointList.get(0).y);
	for (int i=1; i<pointList.size(); i++) {
	    _path.lineTo(pointList.get(i).x,pointList.get(i).y);
	}
	repaint();
    }

//--------------------------------------
    
    private ArrayList<DPoint> _pointList = new ArrayList<DPoint>();
    private Path2D.Double _path = new Path2D.Double();
}
//---------------------------------------------
//---------------------------------------------
