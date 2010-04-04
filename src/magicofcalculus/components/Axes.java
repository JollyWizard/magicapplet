//
//Axes.java
//
package magicofcalculus.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import static java.lang.Math.round;
import static java.lang.Math.ceil;
import java.util.ArrayList;
import java.awt.geom.Path2D;
import java.awt.Color;

import magicofcalculus.Component;
import magicofcalculus.DPoint;
import magicofcalculus.Function;
import magicofcalculus.Line;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;

/**
 * Class implementing Component
 * 
 * @author T Johnson
 * 
 */
public class Axes extends Component {

    /**
     * Class constructor
     * 
     * @param panel
     *            The Panel representing the Axes
     */
    public Axes(Panel panel) {
	super(panel);
	_xAxis = new Line(panel);
	_yAxis = new Line(panel);
	_xAxis.setVisible(true);
	_yAxis.setVisible(true);
    }

    public void setAxesInPanel(DPoint origin, int lengthOfXAxisInPanel,
	    int lengthOfYAxisInPanel) {
	repaint();
	_origin.setLocation(origin);
	_lengthOfXAxisInPanel = lengthOfXAxisInPanel;
	_lengthOfYAxisInPanel = lengthOfYAxisInPanel;
	_xAxis.setLine(_origin.x, _origin.y, _origin.x + _lengthOfXAxisInPanel,
		_origin.y);
	_yAxis.setLine(_origin.x, _origin.y, _origin.x, _origin.y
		- _lengthOfYAxisInPanel);
	setAffineTransform();
	repaint();
    }

    /**
     * Contains functionality from {@link #setAxesInPanel(int, int)} so that
     * annotations can be used
     */
    public void setPosition(DPoint pos) {
	_origin = pos;
	if (_lengthOfXAxisInPanel != 0 || _lengthOfYAxisInPanel != 0) {
	    refreshDrawValues();
	}
    }

    public DPoint getPosition() {
	return getOrigin();
    }

    /**
     * Contains functionality from {@link #setAxesInPanel(int, int)} so that
     * annotations can be used
     * 
     * @param lengthOfXAxisInPanel
     * @param lengthOfYAxisInPanel
     */
    public void setDimensions(int lengthOfXAxisInPanel, int lengthOfYAxisInPanel) {
	_lengthOfXAxisInPanel = lengthOfXAxisInPanel;
	_lengthOfYAxisInPanel = lengthOfYAxisInPanel;
	refreshDrawValues();
    }

    /**
     * recalculates the axes' drawing info so that it moves properly when
     * position or scale is changed.
     */
    public void refreshDrawValues() {
	if (_origin == null)
	    return;
	_xAxis.setLine(_origin.x, _origin.y, _origin.x + _lengthOfXAxisInPanel,
		_origin.y);
	_yAxis.setLine(_origin.x, _origin.y, _origin.x, _origin.y
		- _lengthOfYAxisInPanel);
	setAffineTransform();
	repaint();
    }

    public void setAxesLocal(int lengthOfXAxisLocal, int lengthOfYAxisLocal) {
	_lengthOfXAxisLocal = lengthOfXAxisLocal;
	_lengthOfYAxisLocal = lengthOfYAxisLocal;
	setAffineTransform();
    }

    /**
     * Accessor method for the _origin field
     * 
     * @return DPoint containing the axes origin
     */
    public DPoint getOrigin() {
	return _origin;
    }

    /**
     * Accessor for the _lengthOfXAxisInPanel field
     * 
     * @return The length of the X-Axis relative to the panel
     */
    public double getXAxisLengthInPanel() {
	return _lengthOfXAxisInPanel;
    }

    /**
     * Accessor for the _lengthOfYAxisInPanel field
     * 
     * @return The length of the Y-Axis relative to the panel
     */
    public double getYAxisLengthInPanel() {
	return _lengthOfYAxisInPanel;
    }

    /**
     * Accessor for the _lengthOfXAxisLocal field
     * 
     * @return Local length of the X-Axis
     */
    public double getXAxisLengthLocal() {
	return _lengthOfXAxisLocal;
    }

    public double getYAxisLengthLocal() {
	return _lengthOfYAxisLocal;
    }

    public DPoint transformLocalToPanel(DPoint point) {
	_at.transform(point, point);
	return point;
    }

    public DPoint transformPanelToLocal(DPoint point) {
	try {
	    _at.inverseTransform(point, point);
	}// to local coords
	catch (NoninvertibleTransformException e) {
	    MagicApplet.printError(this, "NoninvertibleTransformException");
	}
	return point;
    }

    public AffineTransform getATFromLocalToPanel() {
	return (AffineTransform) _at.clone();
    }

    public AffineTransform getATFromPanelToLocal() {
	AffineTransform clone = (AffineTransform) _at.clone();
	try {
	    clone.invert();
	} catch (NoninvertibleTransformException e) {
	    MagicApplet.printError(this, "NoninvertibleTransformException");
	    clone.setToIdentity();
	}
	return clone;
    }

    public PolyLine getPolyLineFromFunction(int numIntervals,
	    double leftXLocal, double rightXLocal, Function curveFunction) {

	PolyLine polyLine = new PolyLine(_panel);

	if (numIntervals < 1)
	    return polyLine;
	double deltaX = (rightXLocal - leftXLocal) / numIntervals;
	if (deltaX <= 0)
	    return polyLine;

	double[] xPoints = new double[numIntervals + 1];
	for (int i = 0; i < numIntervals + 1; i++)
	    xPoints[i] = leftXLocal + i * deltaX;
	double[] yPoints = curveFunction.getArrayOfYofX(xPoints);

	polyLine.setPolyLine(numIntervals + 1, xPoints, yPoints);
	polyLine.transform(_at);

	return polyLine;

    }

    /**
     * Modifies the visibility of the fill under the curve
     * 
     * @param set
     *            Boolean indicating if the fill should be visible or not
     */
    public void setFillUnderCurveVisible(boolean set) {
	_fillUnderCurveVisible = set;
    }

    /**
     * Sets the color of the fill under the curve
     * 
     * @param color
     *            Color to assign to the fill
     */
    public void setFillUnderCurveColor(Color color) {
	_fillUnderCurveColor = color;
	repaint();
    }

    /**
     * Renders the fill under the specified curve
     * 
     * @param curve
     *            Target curve
     * @param fromXInPanel
     *            Starting X coordinate
     * @param toXInPanel
     *            Ending X coordinate
     */
    public void setFillUnderCurve(PolyLine curve, double fromXInPanel,
	    double toXInPanel) {
	repaint();
	_underTheCurvePath.reset();
	ArrayList<DPoint> pointList = curve.getPointList(fromXInPanel,
		toXInPanel);
	_underTheCurvePath.moveTo(pointList.get(0).x, pointList.get(0).y);
	for (int i = 1; i < pointList.size(); i++) {
	    _underTheCurvePath.lineTo(pointList.get(i).x, pointList.get(i).y);
	}
	_underTheCurvePath.lineTo((int) round(toXInPanel),
		(int) round(_origin.y));
	_underTheCurvePath.lineTo((int) round(fromXInPanel),
		(int) round(_origin.y));
	_underTheCurvePath.closePath();
	repaint();
    }

    /**
     * Renders a fill between the specified curves between the specified
     * starting and ending x-coordinates
     * 
     * @param curve1
     *            First curve
     * @param curve2
     *            Second curve
     * @param fromXInPanel
     *            Starting x-coordinate
     * @param toXInPanel
     *            Ending x-coordinate
     */
    public void setFillBetweenCurves(PolyLine curve1, PolyLine curve2,
	    double fromXInPanel, double toXInPanel) {
	repaint();
	_underTheCurvePath.reset();
	ArrayList<DPoint> pointList1 = curve1.getPointList(fromXInPanel,
		toXInPanel);
	ArrayList<DPoint> pointList2 = curve2.getPointList(fromXInPanel,
		toXInPanel);
	_underTheCurvePath.moveTo(pointList1.get(0).x, pointList1.get(0).y);
	for (int i = 1; i < pointList1.size(); i++) {
	    _underTheCurvePath.lineTo(pointList1.get(i).x, pointList1.get(i).y);
	}
	for (int i = pointList2.size() - 1; i >= 0; i--) {
	    _underTheCurvePath.lineTo(pointList2.get(i).x, pointList2.get(i).y);
	}
	_underTheCurvePath.closePath();
	repaint();
    }

    public void setRiemannRectsVisible(boolean set) {
	_riemannRectsVisible = set;
    }

    public void setRiemannRightEndPoints(boolean set) {
	_riemannRightEndPoints = set;
    }

    public void setRiemannRects(double fromXLocal, double toXLocal,
	    double deltaXLocal, Function curveFunction) {

	repaint();
	_riemannRectsPath.reset();

	// make the list of x points
	if (deltaXLocal <= 0)
	    return;
	int numPoints = (int) ceil(((toXLocal - fromXLocal) / deltaXLocal)) + 1;
	if (numPoints <= 0)
	    return;
	double[] xList = new double[numPoints];
	for (int i = 0; i < numPoints - 1; i++) {
	    xList[i] = fromXLocal + i * deltaXLocal;
	}
	xList[numPoints - 1] = toXLocal;

	// draw the lines
	int isRightEndPoints = _riemannRightEndPoints ? 1 : 0;
	for (int i = 0; i < numPoints - 1; i++) {
	    _riemannRectsPath.moveTo(xList[i], 0);
	    double heightOfRect = curveFunction.getYofX(xList[i
		    + isRightEndPoints]);
	    // double heightOfRect =
	    // curveFunction.getYofX(xList[i+isRightEndPoints])-1.5/45;//dev
	    _riemannRectsPath.lineTo(xList[i], heightOfRect);
	    _riemannRectsPath.lineTo(xList[i + 1], heightOfRect);
	    _riemannRectsPath.lineTo(xList[i + 1], 0);// dev
	}
	_riemannRectsPath.moveTo(toXLocal, 0);
	_riemannRectsPath.lineTo(toXLocal, curveFunction.getYofX(toXLocal));

	_riemannRectsPath.transform(_at);
	repaint();
    }

    /**
     * Set the color of the ReimannRects
     * 
     * @param color
     *            Color
     */
    public void setRiemannRectsColor(Color color) {
	_riemannRectsColor = color;
	repaint();
    }

    /**
     * Set the width of the ReimannRects Stroke
     * 
     * @param width
     *            Stroke width
     */
    public void setRiemannRectsStrokeWidth(double width) {
	repaint();
	_riemannRectsStrokeWidth = (float) width;
	repaint();
    }

    /**
     * Set the stroke width
     * 
     * @param width
     *            Width
     */
    public void setStrokeWidth(double width) { // from component
	repaint();
	_strokeWidth = (float) width;
	_xAxis.setStrokeWidth(_strokeWidth);
	_yAxis.setStrokeWidth(_strokeWidth);
	repaint();
    }

    /**
     * Renders the Axes
     */
    public void draw(Graphics g) {
	Graphics2D g2 = (Graphics2D) g.create();
	if (_visible) {
	    g2.setStroke(new BasicStroke(_strokeWidth));
	    g2.setColor(_color);
	    _xAxis.draw(g2);
	    _yAxis.draw(g2);
	}
	if (_fillUnderCurveVisible) {
	    g2.setColor(_fillUnderCurveColor);
	    g2.fill(_underTheCurvePath);
	    g2.draw(_underTheCurvePath);// dev
	}
	if (_riemannRectsVisible) {
	    g2.setStroke(new BasicStroke(_riemannRectsStrokeWidth));
	    g2.setColor(_riemannRectsColor);
	    g2.draw(_riemannRectsPath);

	    // for solid bars
	    g2.fill(_riemannRectsPath);
	    g2.setColor(Color.black);
	    g2.draw(_riemannRectsPath);

	    // if (!_outlineOnly) g2.fill(_riemannRectsPath);
	    // if (_innerThickness>0.0) g2.fill(_innerThicknessPath);
	}
    }

    /**
     * Repaints the component
     */
    public void repaint() {
	if (_panel == null)
	    return;
	_xAxis.repaint();
	_yAxis.repaint();
    }

    /**
     * Determines if the specified point hits the x or y axis
     * 
     * @param pt
     *            Point to test
     */
    public boolean isHitBy(DPoint pt) {
	if (!_visible)
	    return false;
	return _xAxis.isHitBy(pt) || _yAxis.isHitBy(pt);
    }

    // ------------------------------------------------

    private void setAffineTransform() {
	if (_lengthOfXAxisLocal == 0.0 || _lengthOfYAxisLocal == 0.0) {
	    _at.setToIdentity();
	    return;
	}
	double sx = _lengthOfXAxisInPanel / _lengthOfXAxisLocal;
	double sy = -_lengthOfYAxisInPanel / _lengthOfYAxisLocal;
	double tx = _origin.x;
	double ty = _origin.y;

	_at.setToTranslation(tx, ty);// then translate
	_at.scale(sx, sy);// scale first, build the transform in reverse order
    }

    // ------------------------------------------------

    private Line _xAxis = null;

    private Line _yAxis = null;

    private DPoint _origin = new DPoint();

    private int _lengthOfXAxisLocal = 0;

    private int _lengthOfYAxisLocal = 0;

    private int _lengthOfXAxisInPanel = 0;

    private int _lengthOfYAxisInPanel = 0;

    // goes from the Local coordinates of the Axes to the coordinates of it's
    // Panel
    private AffineTransform _at = new AffineTransform();// Local to Panel

    private boolean _fillUnderCurveVisible = false;

    private Color _fillUnderCurveColor = Color.black;

    /**
     * @return the _fillUnderCurveColor
     */
    public Color getFillUnderCurveColor() {
	return _fillUnderCurveColor;
    }

    private Path2D.Double _underTheCurvePath = new Path2D.Double();

    private boolean _riemannRectsVisible = false;

    private boolean _riemannRightEndPoints = false;

    private Color _riemannRectsColor = Color.black;

    private float _riemannRectsStrokeWidth = 1.0f;

    private Path2D.Double _riemannRectsPath = new Path2D.Double();

}
// ------------------------------------------------
