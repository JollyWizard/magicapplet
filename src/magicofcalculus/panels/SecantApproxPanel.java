//
// SecantApproxPanel.java
//
package magicofcalculus.panels;

import james.SubComponent;
import james.annotations.Point;
import james.annotations.QuadCurveProperties;
import james.annotations.drag.Drag;
import james.annotations.draw.color;
import james.annotations.labels.Image;
import james.annotations.labels.Opaque;
import james.annotations.placement.Dimensions;
import james.annotations.placement.Position;
import james.annotations.placement.Scale;
import james.annotations.placement.zIndex;
import james.annotations.scenes.Scene;
import james.annotations.scenes.Scenes;
import james.annotations.visibility.Visible;

import java.awt.event.MouseEvent;

import magicofcalculus.DPoint;
import magicofcalculus.Line;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;
import magicofcalculus.QuadCurve;
import magicofcalculus.components.Axes;
import magicofcalculus.components.Circle;
import magicofcalculus.components.Label;
import magicofcalculus.components.SecantTriangle;

/**
 * TODO add Mathematical description of what the panel actually demonstrates
 * <p>
 * 
 * @description Walks through the analysis of infintessimal slope using the
 *              hypotenuse of a secant triangle
 * @axes 1
 * @graph Quadratic Curve
 * @interactive Points on the curve can be dragged. When they collide, they
 *              become an triangle that represents infinitessimal slope
 * @author TJ Johnson
 * @documentation James Arlow<james.arlow@gmail.com>
 */
@Scenes( {
	@Scene(index = 0, description = "Start"),
	@Scene(index = 1, description = "Curve"),
	@Scene(index = 2, description = "Curve formula"),
	@Scene(index = 3, description = "Tangent Point"),
	@Scene(index = 4, description = "Tangent Line"),
	@Scene(index = 5, description = "Secant Point"),
	@Scene(index = 6, description = "Attach Point and Line"),
	@Scene(index = 7, description = "Triangle w// labels"),
	@Scene(index = 8, description = "Slope Formula"),
	@Scene(index = 9, description = "Movable Formulas Grouped"),
	@Scene(index = 10, description = "Formulas Ungrouped"),
	@Scene(index = 11, description = "Slope Label and Regrouped"),
	@Scene(index = 12, description = "You shouldn't be seeing this!", next = true),
	@Scene(index = 13, description = "Post BMI"),
	@Scene(index = 14, description = "You shouldn't be seeing this!", next = true) })
public class SecantApproxPanel extends Panel {

    /**
     * @name Graph
     * @description the graph for this example
     */
    @Position(x = axis.originX, y = axis.originY)
    @Dimensions(width = axis.width, height = axis.height)
    @Scale(x = 0, y = 0)
    @Visible(1)
    @zIndex(layers.axes)
    public Axes _axes;

    /**
     * @name Curve
     * @description Quandrant I graph of x^3
     */
    @QuadCurveProperties(start = @Point(x = axis.originX, y = axis.originY), control = @Point(x = 300, y = axis.originY), end = @Point(x = axis.originX
	    + axis.width, y = axis.originY - axis.height))
    @Visible(1)
    @color("red")
    @zIndex(layers.graph)
    public QuadCurve _curve;

    /**
     * @name Curve Equation
     * @description x^3
     */
    @Drag
    @Image("28pt/CurveEquationLabel.gif")
    @Position(x = 160, y = 80)
    @Visible(2)
    @zIndex(layers.label)
    public Label _curveEquationLabel;

    /**
     * @name Curve Formula
     * @Description The Red Formula, for the quadritic curve on the grid
     */
    @Drag
    @Image("24pt/CurveFormulaLabel.gif")
    @Position(x = 454, y = 148)
    @Visible(9)
    @zIndex(layers.label)
    public Label _curveFormulaLabel;

    /**
     * @name Delta X
     * @description Triangle base as change in X
     */
    @Image("24pt/DeltaX.gif")
    @Visible(7)
    @zIndex(layers.label)
    public Label _deltaXLabel;

    /**
     * @name Delta Y
     * @description Triangle height as change in Y
     */
    @Image("24pt/DeltaY.gif")
    @Visible(7)
    @zIndex(layers.label)
    public Label _deltaYLabel;

    /**
     * @name Secant Triangle Slope
     * @description, shows how the slope is related to the hypotenuse of a
     *               secant Triangle
     */
    @Drag
    @Image("24pt/DeltaYXFormula.gif")
    @Position(x = 112, y = 183)
    @Visible(8)
    @zIndex(layers.label)
    public Label _deltaYXFormulaLabel;

    /**
     * @name Delta X
     * @description numerator of the infitessimal slope
     */
    @Drag
    @Image("12pt/dx.gif")
    @Position(x = 251, y = 356)
    @zIndex(layers.label)
    public Label _dxLabel;

    // ---------------------------------------

    /**
     * @name Infinite Slope
     * @description The formula for slope at a distance infinitly small.
     */
    @Drag
    @Image("24pt/DydxFormula.gif")
    @Position(x = 112, y = 183)
    @zIndex(layers.label)
    public Label _dydxFormulaLabel;

    @color(src = MagicApplet.class, index = MagicApplet._GREEN, mode = color.Mode.field)
    @zIndex(layers.triangle)
    public SecantTriangle _dydxTriangle;

    /**
     * @name Delta Y
     * @description Denominator of the infitessimal slope
     */
    @Drag
    @Image("12pt/dy.gif")
    @Position(x = 270, y = 341)
    @zIndex(layers.label)
    public Label _dyLabel;

    /**
     * @name Unknown Formula
     * @Description Unknown (As we have not yet discovered the great mysteries
     *              of calculus)
     */
    @Drag
    @Image("24pt/QuestionMarkFormula.gif")
    @Position(x = 112, y = 183)
    @zIndex(layers.label)
    public Label _questionMarkFormulaLabel;

    /**
     * @name Secant Point
     * @description The upper blue point.
     */
    @Drag(action = PostBMI_Collider.class)
    @Visible(3)
    @color("blue")
    @zIndex(layers.points)
    public Circle _secantPoint;

    /**
     * @name Secant Triangle
     * @description Right Triangle based on secant line between points.
     */
    @Visible(6)
    @Drag
    @color("blue")
    @zIndex(layers.triangle)
    public SecantTriangle _secantTriangle;

    /**
     * @name slope Formula Label
     * @Description Identifies the formula label.
     */
    @Drag
    @Image("24pt/SlopeFormulaLabel.gif")
    @Position(x = 453, y = 214)
    @Visible(11)
    @zIndex(layers.label)
    public Label _slopeFormulaLabel;

    /**
     * @name Tangent Line
     * @description The line between the two points
     */
    @Drag
    @Visible(4)
    @color("blue")
    @zIndex(layers.line)
    public Line _tangentLine;

    /**
     * @name Tangent Point
     * @description The lower red point on the curve. At the center of the
     *              tangent line
     */
    @Visible(5)
    @color("red")
    @zIndex(layers.points)
    public Circle _tangentPoint;

    // ---------------------------------------

    /**
     * Old groupable labels moved to agrregate label Position taken from
     * {@link SecantApproxPanel#_xCubedLabel}
     * 
     * @name Changeable Formula
     * @description A Formula that changes between x^2 & x^3, debending on drag
     *              locations.
     */
    @Drag
    @Position(x = 694, y = 100)
    @Visible(9)
    @zIndex(layers.label)
    public x23Label _x23;

    /**
     * @name X^3
     * @description the forumala for something or other;
     */
    @Drag
    @Image("32pt/XCubedLabel.gif")
    @Position(x = 694, y = 129)
    @Visible(9)
    @zIndex(layers.label)
    public Label _xCubedLabel;

    /**
     * point used to share info between actions
     */
    private DPoint _mousePt = new DPoint();

    // Sync params
    /**
     * Seems to be a temporary value for syncing mouse values.
     */
    private double _tValue = .9;

    // / ////////////////////////////////////////////////

    private final double _tValueAtTangentPoint = .5;

    /**
     * <ol>
     * <li>Sets scene count
     * <li>Initializes all components
     * <li>adds components to list
     * <li>creates a drag group
     * </ol>
     * 
     * <pre>
     * addToDragGroup(groupId, _deltaYXFormulaLabel);
     * addToDragGroup(groupId, _dydxFormulaLabel);
     * addToDragGroup(groupId, _questionMarkFormulaLabel);
     * </pre>
     */
    public SecantApproxPanel() {
	super();

	_tangentPoint.setCenter(_curve
		.getPointAtParamValue(_tValueAtTangentPoint));

	double length = 700;// in pixels
	double tValueAtSecantPoint = _tValue;
	double slope = Line.getSlope(_tangentPoint.getCenter(), _curve
		.getPointAtParamValue(tValueAtSecantPoint));
	_tangentLine.setLine(_tangentPoint.getCenter(), slope, length);
	_tangentLine.setPivotDrag(_tangentPoint.getCenter());

	_secantPoint
		.setCenter(_curve.getPointAtParamValue(tValueAtSecantPoint));

	_secantTriangle.setTriangle(_tangentPoint.getCenter(), _secantPoint
		.getCenter());
	_secantTriangle.setOutlineOnly(true);

	final int offset = 7;
	_dydxTriangle.setTriangle(_tangentPoint.getCenter().getTranslation(
		-offset, offset), _tangentPoint.getCenter().getTranslation(
		offset, -offset));

	int groupId = createDragGroup();
	addToDragGroup(groupId, _deltaYXFormulaLabel);
	addToDragGroup(groupId, _dydxFormulaLabel);
	addToDragGroup(groupId, _questionMarkFormulaLabel);
    }

    /**
     * The scene where the line becomes a secant of the two points
     */
    private static final int ATTACH_SCENE = 6;

    private static final boolean MAKE_TANGENT_DISAPPEAR = false;

    /**
     * The scene when the panel is returned to from BMI_Panel
     */
    private static final int POST_BMI_SCENE = 13;

    /**
     * The scene where the slope formula becomes visible
     */
    private static final int SLOPE_FORMULA_VISIBLE_SCENE = 8;

    /**
     * Overrides from panel.
     * <p>
     * Stores DPoint in field _mousePoint Calls super
     */
    public void mouseDragged(MouseEvent mouse) {
	_mousePt = new DPoint(mouse.getPoint());
	super.mouseDragged(mouse);
    }

    /**
     * Overrides from panel.
     * <p>
     * Stores DPoint in field _mousePoint
     * <p>
     * ensures that the secantPoint is draggable, even if it isn't visible.
     * 
     */
    @Override
    public void mousePressed(MouseEvent mouse) {
	_mousePt = new DPoint(mouse.getPoint());// capture mouse point for use
	// in syncComponents()

	// make it so that the _secantPoint is always selected from underneath
	// all other components
	boolean secantPointWasVisible = _secantPoint.isVisible();
	_secantPoint.setVisible(true);
	if (_secantPoint.isHitBy(_mousePt))
	    _componentList.bringToTopOfZOrder(_secantPoint);
	super.mousePressed(mouse);
	// put this down here so so it stays hittable during
	// super.mousePressed() which selects the first hittable component
	_secantPoint.setVisible(secantPointWasVisible);
    }

    /**
     * Overrides from panel.
     * <p>
     * Stores DPoint in field _mousePoint Calls super
     */
    public void mouseReleased(MouseEvent mouse) {
	_mousePt = new DPoint(mouse.getPoint());
	super.mouseReleased(mouse);
    }

    /**
     * Sets the 3 label for x's power to a relative position for the formula to
     * look correct.
     * 
     * @deprecated
     */
    private void setLabelsOverXCubedLabel() {
	DPoint bottomLeftCornerOfXCubedLabel = _xCubedLabel
		.getBottomLeftCorner();
	DPoint _x23Pos = _x23.getPosition(bottomLeftCornerOfXCubedLabel);
	_x23._xLabel.setPosition(_x23Pos);

	double pctWidthOverlap = 5;
	double pctHeightOverlap = 38;
	double xPos = _x23._xLabel.getPosition().x
		+ (1 - pctWidthOverlap / 100) * _x23._xLabel.getWidth();
	double yPos = _x23._xLabel.getPosition().y + (pctHeightOverlap / 100)
		* _x23._xLabel.getHeight();
	_x23._threeLabel.setImage("32pt/ThreeLabel.gif");
	_x23._threeLabel.setBottomLeftCorner(new DPoint(xPos, yPos));
	_x23._twoLabel.setBottomLeftCorner(_x23._threeLabel
		.getBottomLeftCorner());
	_componentList.bringToTopOfZOrder(_x23._xLabel);
	_componentList.bringToTopOfZOrder(_x23._threeLabel);
    }

    /**
     * _
     */
    private void setSyncParamsFromSecantPoint() {
	_tValue = _curve
		.getParamValueAtPointNearestTo(_secantPoint.getCenter());
    }

    private void setSyncParamsFromSecantTriangle() {
	double[] tArray = { 0, 0 };
	int numParamValues = _curve.getParamValuesAtX(_secantTriangle
		.getCornerPoint().x, tArray);
	if (numParamValues > 0) {
	    _tValue = tArray[0];
	}// use the first param value that comes up.
    }

    private void setSyncParamsFromTangentLine() {

	double[] intersectionParamValues = { 0, 0 };
	int numberOfIntersections = _curve.getParamValuesAtIntersections(
		_tangentLine, intersectionParamValues);
	if (numberOfIntersections > 1) {// pick out the one that's not the
	    // _tangentPoint
	    double epsilon = .001;
	    int i = 0;
	    if (_curve.getPointAtParamValue(intersectionParamValues[0])
		    .distance(_tangentPoint.getCenter()) < epsilon)
		i = 1;
	    _tValue = intersectionParamValues[i];
	}

    }

    private void syncDeltaXLabel() {
	DPoint curvePt = _curve.getPointAtParamValue(_tValue);
	double xPos = (_tangentPoint.getCenter().x + curvePt.x) / 2
		- _deltaXLabel.getRect().width / 2;
	double yPos = _tangentPoint.getCenter().y;
	_deltaXLabel.setPosition(xPos, yPos);
    }

    private void syncDeltaYLabel() {
	DPoint curvePt = _curve.getPointAtParamValue(_tValue);
	double xPos = curvePt.x;
	double yPos = (_tangentPoint.getCenter().y + curvePt.y) / 2
		- _deltaYLabel.getRect().height / 2;
	_deltaYLabel.setPosition(xPos, yPos);
    }

    private void syncSecantPoint() {
	if (0 <= _tValue && _tValue <= 1) {
	    _secantPoint.setVisible(true);
	    _secantPoint.setCenter(_curve.getPointAtParamValue(_tValue));
	} else {
	    _secantPoint.setVisible(false);
	}
    }

    private void syncSecantTriangle() {
	DPoint curvePt = _curve.getPointAtParamValue(_tValue);
	_secantTriangle.setSecantPoint(curvePt);
    }

    private void syncTangentLine() {
	DPoint curvePt = _curve.getPointAtParamValue(_tValue);
	double slope = Line.getSlope(curvePt.x, curvePt.y, _tangentPoint
		.getCenter().x, _tangentPoint.getCenter().y);
	if (Double.isInfinite(slope) || Double.isNaN(slope))
	    slope = 0.0;
	_tangentLine.setLine(_tangentPoint.getCenter(), slope, _tangentLine
		.getLength());
    }

    /**
     * sets visibility and descriptions via switch
     * <p>
     */
    public void setScene(int scene) {

	super.setScene(scene);

	switch (scene) {
	case 7:
	    syncComponents();// to place labels on triangle in case no move has
	    // happened since Attach Point and Line
	    break;
	case 9:
	    // setLabelsOverXCubedLabel();
	    break;
	case 10:
	    // TODO Migrated to _x23label
	    // setXCubedLabelsGrouped(false);
	    break;
	case 11:
	    syncComponents();// dev, in case coming back here from scene 13 with
	    // green line
	    break;
	case 12:
	    if (getTopLevelAncestor() == null)
		break;
	    if (_sceneAdvancing)
		((MagicApplet) getTopLevelAncestor()).advancePanel();
	    else
		((MagicApplet) getTopLevelAncestor()).reversePanel();
	    break;
	case 14:
	    if (getTopLevelAncestor() == null)
		break;
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	default:
	    break;
	}
    }

    protected void setSyncParams() {
	if (_componentList.get(0) == _secantPoint) {
	    setSyncParamsFromSecantPoint();
	} else if (_componentList.get(0) == _tangentLine) {
	    setSyncParamsFromTangentLine();
	} else if (_componentList.get(0) == _secantTriangle) {
	    setSyncParamsFromSecantTriangle();
	}

	if (_tValue < _tValueAtTangentPoint)
	    _tValue = _tValueAtTangentPoint;
    }

    /**
     * No action if scene is less that field: ATTACH_SCENE <br>
     * Check for point collision and switch to the secant triangle if necessary <br>
     * Calls all methods of name syncXXX
     * <p>
     * Comment Copied from setScene method call:
     * 
     * <pre>
     * // to place labels on triangle in case no move has
     * // happened since Attach Point and Line
     * </pre>
     */
    protected void syncComponents() {

	if (getScene() < ATTACH_SCENE)
	    return;

	// go thru the movable components
	for (int i = 0; i < _componentList.size(); i++) {
	    if (_componentList.get(i) == _secantPoint) {
		syncSecantPoint();
	    } else if (_componentList.get(i) == _tangentLine) {
		syncTangentLine();
	    } else if (_componentList.get(i) == _secantTriangle) {
		syncSecantTriangle();
	    } else if (_componentList.get(i) == _deltaXLabel) {
		syncDeltaXLabel();
	    } else if (_componentList.get(i) == _deltaYLabel) {
		syncDeltaYLabel();
	    }
	    // else if (_componentList.get(i) == _threeLabel) {
	    // syncThreeLabel();
	    // }
	}

	boolean pointsOnTopOfEachOther = _tValue == _curve
		.getParamValueAtPointNearestTo(_tangentPoint.getCenter());

	if (pointsOnTopOfEachOther) {
	} else {
	    _tangentLine.setVisible(true);
	    _secantPoint.setVisible(true);
	    _tangentPoint.setVisible(true);
	    _dydxTriangle.setVisible(false);
	    _dxLabel.setVisible(false);
	    _dyLabel.setVisible(false);

	    if (getScene() >= SLOPE_FORMULA_VISIBLE_SCENE)
		_deltaYXFormulaLabel.setVisible(true);
	    _dydxFormulaLabel.setVisible(false);
	    _questionMarkFormulaLabel.setVisible(false);

	    if (_secantTriangle.isVisible()) {
		_deltaXLabel.setVisible(true);
		_deltaYLabel.setVisible(true);
	    }
	}
    }

    /**
     * Constants related to axis placement
     */
    public static class axis {
	public static final int height = 400;

	public static final int originX = 50;

	public static final int originY = 450;

	public static final int width = 350;
    }

    /**
     * Midpoints for zindex layers
     * 
     * @author James Arlow
     * 
     */
    public static class layers {
	public static final int axes = 10;
	public static final int graph = 0;
	public static final int label = 40;
	public static final int line = 20;
	public static final int points = 30;
	public static final int triangle = 60;
    }

    /**
     * Class to handle the action where the components collide after the mystic
     * secrets of calculus have been revealed
     * 
     * @manual When the two points collide the Formula will change to reveal the
     *         mystical workings of the calculus
     * 
     * @author James Arlow
     */
    public class PostBMI_Collider implements Drag.Handler {
	public void action() {
	    boolean pointsOnTopOfEachOther = _tValue == _curve
		    .getParamValueAtPointNearestTo(_tangentPoint.getCenter());

	    if (pointsOnTopOfEachOther) {
		if (getScene() >= POST_BMI_SCENE) {
		    _tangentLine.setColor(MagicApplet.GREEN);
		    _tangentLine.setLine(_tangentPoint.getCenter(), _curve
			    .getSlopeAtParamValue(_tValueAtTangentPoint),
			    _tangentLine.getLength());
		    _tangentLine.setVisible(true);

		    _secantPoint.setVisible(false);
		    _tangentPoint.setVisible(false);
		    _dydxTriangle.setVisible(true);
		    _dxLabel.setVisible(true);
		    _dyLabel.setVisible(true);

		    _deltaYXFormulaLabel.setVisible(false);
		    _questionMarkFormulaLabel.setVisible(false);
		    _dydxFormulaLabel.setVisible(true);
		    _deltaXLabel.setVisible(false);
		    _deltaYLabel.setVisible(false);
		} else {
		    double slope = Line.getSlope(_mousePt.x, _mousePt.y,
			    _tangentPoint.getCenter().x, _tangentPoint
				    .getCenter().y);
		    _tangentLine.setLine(_tangentPoint.getCenter(), slope,
			    _tangentLine.getLength());
		    _tangentLine.setVisible(!MAKE_TANGENT_DISAPPEAR);

		    _secantPoint.setVisible(true);
		    _tangentPoint.setVisible(true);
		    _dydxTriangle.setVisible(false);
		    _dxLabel.setVisible(false);
		    _dyLabel.setVisible(false);

		    _deltaXLabel.setVisible(false);
		    _deltaYLabel.setVisible(false);
		    _deltaYXFormulaLabel.setVisible(false);
		    _dydxFormulaLabel.setVisible(false);
		    if (getScene() >= SLOPE_FORMULA_VISIBLE_SCENE)
			_questionMarkFormulaLabel.setVisible(true);
		}
	    }

	}
    }

    /**
     * addToDragGroup(_xCubedLabelsDragGroupId, _xLabel);
     * addToDragGroup(_xCubedLabelsDragGroupId, _twoLabel);
     * addToDragGroup(_xCubedLabelsDragGroupId, _threeLabel);
     */
    public static class x23Label extends SubComponent {
	@Drag
	@Opaque
	@Image("32pt/ThreeLabel.gif")
	@zIndex(2)
	@Position(x = 20, y = -15)
	public Label _threeLabel;

	@Drag
	@Image("32pt/TwoLabel.gif")
	@Position(x = 20, y = -15)
	@zIndex(1)
	public Label _twoLabel;

	@Drag
	@Image("32pt/XLabel.gif")
	@zIndex(0)
	public Label _xLabel;

	public x23Label(Panel p) {
	    super(p);
	}

    }
}
