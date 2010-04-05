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
import james.annotations.placement.Dimensions;
import james.annotations.placement.Position;
import james.annotations.placement.Scale;
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
	setNumScenes(NUM_SCENES);
	// setNumScenes(1);//dev

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
    public void mouseDragged(MouseEvent mouse) {
	_mousePt = new DPoint(mouse.getPoint());
	super.mouseDragged(mouse);
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
     * sets visibility and descriptions via switch
     * <p>
     */
    protected void setScene(int scene) {

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
	    _slopeFormulaLabel.setVisible(true);
	    syncComponents();// dev, in case coming back here from scene 13 with
	    // green line
	    break;
	case 12:
	    if (_sceneAdvancing)
		((MagicApplet) getTopLevelAncestor()).advancePanel();
	    else
		((MagicApplet) getTopLevelAncestor()).reversePanel();
	    break;
	case 14:
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
     * Class to handle the action where the components collide after the mystic
     * secrets of calculus have been revealed
     * 
     * @manual When the two points collide the Formula will change to reveal the
     *         mystical workings of the calculus
     * 
     * @author James Arlow
     */
    public class PostBMI_Collider extends Drag.Handler {
	public void action(Object... Params) {
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

    // ---------------------------------------

    /**
     * _
     */
    private void setSyncParamsFromSecantPoint() {
	_tValue = _curve
		.getParamValueAtPointNearestTo(_secantPoint.getCenter());
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

    private void setSyncParamsFromSecantTriangle() {
	double[] tArray = { 0, 0 };
	int numParamValues = _curve.getParamValuesAtX(_secantTriangle
		.getCornerPoint().x, tArray);
	if (numParamValues > 0) {
	    _tValue = tArray[0];
	}// use the first param value that comes up.
    }

    private void syncSecantPoint() {
	if (0 <= _tValue && _tValue <= 1) {
	    _secantPoint.setVisible(true);
	    _secantPoint.setCenter(_curve.getPointAtParamValue(_tValue));
	} else {
	    _secantPoint.setVisible(false);
	}
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

    private void syncSecantTriangle() {
	DPoint curvePt = _curve.getPointAtParamValue(_tValue);
	_secantTriangle.setSecantPoint(curvePt);
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

    // ---------------------------------------

    /**
     * The scene where the line becomes a secant of the two points
     */
    private static final int ATTACH_SCENE = 6;

    /**
     * The scene where the slope formula becomes visible
     */
    private static final int SLOPE_FORMULA_VISIBLE_SCENE = 8;
    /**
     * The scene when the panel is returned to from BMI_Panel
     */
    private static final int POST_BMI_SCENE = 13;

    private static final boolean MAKE_TANGENT_DISAPPEAR = false;

    // private static final boolean MAKE_TANGENT_DISAPPEAR = true;

    private static final int NUM_SCENES = 15;

    // ////////////////////////////////////////////////
    public static final int originX = 50;

    public static final int originY = 450;

    public static final int width = 350;

    public static final int height = 400;

    @Position(x = originX, y = originY)
    @Dimensions(width = width, height = height)
    @Scale(x = 0, y = 0)
    @Visible(1)
    public Axes _axes;

    @QuadCurveProperties(start = @Point(x = originX, y = originY), control = @Point(x = 300, y = originY), end = @Point(x = originX
	    + width, y = originY - height))
    @Visible(1)
    @color("red")
    public QuadCurve _curve;

    @Visible(5)
    @color("red")
    public Circle _tangentPoint;

    @Drag
    @Visible(4)
    @color("blue")
    public Line _tangentLine;

    @Drag(action = PostBMI_Collider.class)
    @Visible(3)
    @color("blue")
    public Circle _secantPoint;

    @Visible(6)
    @Drag
    @color("blue")
    public SecantTriangle _secantTriangle;

    @Drag
    @Image("28pt/CurveEquationLabel.gif")
    @Position(x = 160, y = 80)
    @Visible(2)
    public Label _curveEquationLabel;

    @Image("24pt/DeltaX.gif")
    @Visible(7)
    public Label _deltaXLabel;

    @Image("24pt/DeltaY.gif")
    @Visible(7)
    public Label _deltaYLabel;

    @Drag
    @Image("24pt/DeltaYXFormula.gif")
    @Position(x = 112, y = 183)
    @Visible(8)
    public Label _deltaYXFormulaLabel;

    @Drag
    @Image("24pt/DydxFormula.gif")
    @Position(x = 112, y = 183)
    public Label _dydxFormulaLabel;

    @Drag
    @Image("24pt/QuestionMarkFormula.gif")
    @Position(x = 112, y = 183)
    public Label _questionMarkFormulaLabel;

    @color(src = MagicApplet.class, index = MagicApplet._GREEN, mode = color.Mode.field)
    public SecantTriangle _dydxTriangle;

    @Drag
    @Image("12pt/dx.gif")
    @Position(x = 251, y = 356)
    public Label _dxLabel;

    @Drag
    @Image("12pt/dy.gif")
    @Position(x = 270, y = 341)
    public Label _dyLabel;

    @Drag
    @Image("24pt/CurveFormulaLabel.gif")
    @Position(x = 454, y = 148)
    @Visible(9)
    public Label _curveFormulaLabel;

    @Drag
    @Image("24pt/SlopeFormulaLabel.gif")
    @Position(x = 453, y = 214)
    @Visible(11)
    public Label _slopeFormulaLabel;

    @Drag
    @Image("32pt/XCubedLabel.gif")
    @Position(x = 694, y = 129)
    @Visible(9)
    public Label _xCubedLabel;

    /**
     * Old groupable labels moved to agrregate label Position taken from
     * {@link SecantApproxPanel#_xCubedLabel}
     */
    @Drag
    @Position(x = 694, y = 129)
    @Visible(9)
    public x23Label _x23;

    /**
     * addToDragGroup(_xCubedLabelsDragGroupId, _xLabel);
     * addToDragGroup(_xCubedLabelsDragGroupId, _twoLabel);
     * addToDragGroup(_xCubedLabelsDragGroupId, _threeLabel);
     */
    public class x23Label extends SubComponent {
	public x23Label(Panel panel) {
	    super(panel);
	}

	@Image("32pt/XLabel.gif")
	public Label _xLabel;

	@Image("32pt/TwoLabel.gif")
	public Label _twoLabel;

	@Image(value = "32pt/ThreeLabel.gif")
	public Label _threeLabel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see magicofcalculus.Component#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean set) {
	    super.setVisible(set);
	    System.out.println("_X23 visible = true;");
	}

    }

    // Sync params
    /**
     * Seems to be a temporary value for syncing mouse values.
     */
    private double _tValue = .9;
    private final double _tValueAtTangentPoint = .5;

    private DPoint _mousePt = new DPoint();
}
// ---------------------------------------
// ------------------------------------------------
