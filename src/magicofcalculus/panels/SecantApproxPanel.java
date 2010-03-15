//
// SecantApproxPanel.java
//
package magicofcalculus.panels;

import james.Annotations.scenes.Scene;
import james.Annotations.scenes.Scenes;

import java.awt.Color;
import java.awt.event.MouseEvent;

import magicofcalculus.Component;
import magicofcalculus.DPoint;
import magicofcalculus.DragMaster;
import magicofcalculus.Line;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;
import magicofcalculus.QuadCurve;
import magicofcalculus.components.Axes;
import magicofcalculus.components.Circle;
import magicofcalculus.components.Label;
import magicofcalculus.components.SecantTriangle;
import static java.lang.Math.abs;

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
	@Scene(index = 7, description = "Triangle w labels"),
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

	DPoint origin = new DPoint(50, 450);
	int lengthOfAxisX = 350;
	int lengthOfAxisY = 400;

	_axes = new Axes(this);
	_axes.setAxesInPanel(origin, lengthOfAxisX, lengthOfAxisY);

	_curve = new QuadCurve(this);
	_curve.setCurve(origin.x, origin.y, 300, origin.y, origin.x
		+ lengthOfAxisX, origin.y - lengthOfAxisY);
	_curve.setColor(Color.red);

	_curveEquationLabel = new Label(this);
	_curveEquationLabel.setPosition(160, 80);
	_curveEquationLabel.setDraggable(true);
	_curveEquationLabel.setDisplayImage(true);
	_curveEquationLabel.setImage("28pt/CurveEquationLabel.gif");

	_tangentPoint = new Circle(this);
	_tangentPoint.setCenter(_curve
		.getPointAtParamValue(_tValueAtTangentPoint));
	_tangentPoint.setColor(Color.red);

	double length = 700;// in pixels
	_tangentLine = new Line(this);
	double tValueAtSecantPoint = _tValue;
	double slope = Line.getSlope(_tangentPoint.getCenter(), _curve
		.getPointAtParamValue(tValueAtSecantPoint));
	_tangentLine.setLine(_tangentPoint.getCenter(), slope, length);
	_tangentLine.setDraggable(true);
	_tangentLine.setPivotDrag(_tangentPoint.getCenter());
	_tangentLine.setColor(Color.blue);

	_secantPoint = new Circle(this);
	_secantPoint
		.setCenter(_curve.getPointAtParamValue(tValueAtSecantPoint));
	_secantPoint.setColor(Color.blue);
	_secantPoint.setDraggable(true);
	_secantPoint.setDragMaster((DragMaster) _curve);

	_secantTriangle = new SecantTriangle(this);
	_secantTriangle.setTriangle(_tangentPoint.getCenter(), _secantPoint
		.getCenter());
	_secantTriangle.setColor(Color.blue);
	_secantTriangle.setDraggable(true);
	_secantTriangle.setOutlineOnly(true);

	_deltaXLabel = new Label(this);
	_deltaXLabel.setImage("24pt/DeltaX.gif");
	_deltaXLabel.setDisplayImage(true);
	_deltaXLabel.setPosition(0, 0);

	_deltaYLabel = new Label(this);
	_deltaYLabel.setImage("24pt/DeltaY.gif");
	_deltaYLabel.setDisplayImage(true);
	_deltaYLabel.setPosition(0, 0);

	_deltaYXFormulaLabel = new Label(this);
	_deltaYXFormulaLabel.setImage("24pt/DeltaYXFormula.gif");
	_deltaYXFormulaLabel.setDisplayImage(true);
	_deltaYXFormulaLabel.setDraggable(true);
	_deltaYXFormulaLabel.setPosition(112, 183);

	_dydxFormulaLabel = new Label(this);
	_dydxFormulaLabel.setImage("24pt/DydxFormula.gif");
	_dydxFormulaLabel.setDisplayImage(true);
	_dydxFormulaLabel.setDraggable(true);
	_dydxFormulaLabel.setPosition(112, 183);

	_questionMarkFormulaLabel = new Label(this);
	_questionMarkFormulaLabel.setImage("24pt/QuestionMarkFormula.gif");
	_questionMarkFormulaLabel.setDisplayImage(true);
	_questionMarkFormulaLabel.setDraggable(true);
	_questionMarkFormulaLabel.setPosition(112, 183);

	_dydxTriangle = new SecantTriangle(this);
	_dydxTriangle.setColor(MagicApplet.GREEN);
	final int offset = 7;
	_dydxTriangle.setTriangle(_tangentPoint.getCenter().getTranslation(
		-offset, offset), _tangentPoint.getCenter().getTranslation(
		offset, -offset));

	_dxLabel = new Label(this);
	_dxLabel.setImage("12pt/dx.gif");
	_dxLabel.setDisplayImage(true);
	_dxLabel.setPosition(251, 356);
	_dxLabel.setDraggable(true);// dev

	_dyLabel = new Label(this);
	_dyLabel.setImage("12pt/dy.gif");
	_dyLabel.setDisplayImage(true);
	_dyLabel.setPosition(270, 341);
	_dyLabel.setDraggable(true);// dev

	_curveFormulaLabel = new Label(this);
	_curveFormulaLabel.setImage("24pt/CurveFormulaLabel.gif");
	_curveFormulaLabel.setDisplayImage(true);
	_curveFormulaLabel.setDraggable(true);
	_curveFormulaLabel.setPosition(new DPoint(454, 148));

	_slopeFormulaLabel = new Label(this);
	_slopeFormulaLabel.setImage("24pt/SlopeFormulaLabel.gif");
	_slopeFormulaLabel.setDisplayImage(true);
	_slopeFormulaLabel.setDraggable(true);
	_slopeFormulaLabel.setPosition(new DPoint(453, 214));

	_xCubedLabel = new Label(this);
	_xCubedLabel.setImage("32pt/XCubedLabel.gif");
	_xCubedLabel.setDisplayImage(true);
	_xCubedLabel.setDraggable(true);
	_xCubedLabel.setPosition(694, 129);

	_xLabel = new Label(this);
	_xLabel.setImage("32pt/XLabel.gif");
	_xLabel.setDisplayImage(true);
	_xLabel.setDraggable(true);

	_threeLabel = new Label(this);
	_threeLabel.setImage("32pt/ThreeLabel.gif");
	_threeLabel.setDisplayImage(true);
	_threeLabel.setDraggable(true);
	_threeLabel.setOpaque(true);
	_threeLabel.setBgColor(getBackgroundColor());
	_threeLabel.setSize((int) _threeLabel.getWidth() + 5, (int) _threeLabel
		.getHeight());

	_twoLabel = new Label(this);
	_twoLabel.setImage("32pt/TwoLabel.gif");
	_twoLabel.setDisplayImage(true);
	_twoLabel.setDraggable(true);

	_componentList.add(0, _axes);// add to beginning of list
	_componentList.add(0, _curve);
	_componentList.add(0, _curveEquationLabel);
	_componentList.add(0, _tangentPoint);
	_componentList.add(0, _tangentLine);
	_componentList.add(0, _secantPoint);
	_componentList.add(0, _secantTriangle);
	_componentList.add(0, _deltaXLabel);
	_componentList.add(0, _deltaYLabel);
	_componentList.add(0, _deltaYXFormulaLabel);
	_componentList.add(0, _dydxFormulaLabel);
	_componentList.add(0, _questionMarkFormulaLabel);
	_componentList.add(0, _dydxTriangle);
	_componentList.add(0, _dxLabel);
	_componentList.add(0, _dyLabel);
	_componentList.add(0, _curveFormulaLabel);
	_componentList.add(0, _slopeFormulaLabel);
	_componentList.add(0, _xCubedLabel);
	_componentList.add(0, _xLabel);
	_componentList.add(0, _twoLabel);
	_componentList.add(0, _threeLabel);

	setLabelsOverXCubedLabel();
	_xCubedLabelsDragGroupId = createDragGroup();

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
     * 
     */
    protected void setScene(int scene) {

	super.setScene(scene);

	switch (scene) {
	case 0:
	    for (Component comp : _componentList)
		comp.setVisible(false);
	    _tangentLine.setColor(Color.blue);
	    break;
	case 1:
	    _axes.setVisible(true);
	    _curve.setVisible(true);
	    _curveEquationLabel.setVisible(false);
	    break;
	case 2:
	    _curveEquationLabel.setVisible(true);
	    _tangentPoint.setVisible(false);
	    break;
	case 3:
	    _tangentPoint.setVisible(true);
	    _tangentLine.setVisible(false);
	    break;
	case 4:
	    _tangentLine.setVisible(true);
	    _secantPoint.setVisible(false);
	    break;
	case 5:
	    _secantPoint.setVisible(true);
	    break;
	case ATTACH_SCENE:// 6
	    _secantTriangle.setVisible(false);
	    _deltaXLabel.setVisible(false);
	    _deltaYLabel.setVisible(false);
	    break;
	case 7:
	    _secantTriangle.setVisible(true);
	    _deltaXLabel.setVisible(true);
	    _deltaYLabel.setVisible(true);
	    syncComponents();// to place labels on triangle in case no move has
	    // happened since Attach Point and Line
	    _deltaYXFormulaLabel.setVisible(false);
	    break;
	case 8:// SLOPE_FORMULA_VISIBLE_SCENE
	    _deltaYXFormulaLabel.setVisible(true);
	    _curveFormulaLabel.setVisible(false);
	    _xCubedLabel.setVisible(false);
	    _xLabel.setVisible(false);
	    _twoLabel.setVisible(false);
	    _threeLabel.setVisible(false);
	    break;
	case 9:
	    _curveFormulaLabel.setVisible(true);
	    _xCubedLabel.setVisible(true);
	    _xLabel.setVisible(true);
	    _twoLabel.setVisible(true);
	    _threeLabel.setVisible(true);
	    setLabelsOverXCubedLabel();
	    setXCubedLabelsGrouped(true);
	    break;
	case 10:
	    setXCubedLabelsGrouped(false);
	    _slopeFormulaLabel.setVisible(false);
	    setXCubedLabelsGrouped(false);
	    break;
	case 11:
	    _slopeFormulaLabel.setVisible(true);
	    setXCubedLabelsGrouped(true);
	    syncComponents();// dev, in case coming back here from scene 13 with
	    // green line
	    break;
	case 12:
	    if (_sceneAdvancing)
		((MagicApplet) getTopLevelAncestor()).advancePanel();
	    else
		((MagicApplet) getTopLevelAncestor()).reversePanel();
	    break;
	case POST_BMI_SCENE:// 13
	    break;
	case 14:
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	default:
	    break;
	// don't forget to set NUM_SCENES
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
	boolean pointsOnTopOfEachOther = _tValue == _curve
		.getParamValueAtPointNearestTo(_tangentPoint.getCenter());

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
	    } else if (_componentList.get(i) == _threeLabel) {
		syncThreeLabel();
	    }
	}

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
		_tangentLine.setColor(Color.blue);
		double slope = Line.getSlope(_mousePt.x, _mousePt.y,
			_tangentPoint.getCenter().x,
			_tangentPoint.getCenter().y);
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
	} else {
	    _tangentLine.setColor(Color.blue);
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

    private void syncThreeLabel() {
	if (_threeLabel.getPosition().x < _xLabel.getPosition().x)
	    _threeLabel.setImage("32pt/ThreeLabelBig.gif");
	else
	    _threeLabel.setImage("32pt/ThreeLabel.gif");
    }

    /**
     * Adds the labels for the xcubed information to the drag group or clear
     * drag group if false.
     * 
     * @param set
     */
    private void setXCubedLabelsGrouped(boolean set) {
	_groupXCubedLabels = set;
	if (_groupXCubedLabels) {
	    addToDragGroup(_xCubedLabelsDragGroupId, _xLabel);
	    addToDragGroup(_xCubedLabelsDragGroupId, _twoLabel);
	    addToDragGroup(_xCubedLabelsDragGroupId, _threeLabel);
	} else
	    clearDragGroup(_xCubedLabelsDragGroupId);
    }

    /**
     * Sets the 3 label for x's power to a relative position for the formula to
     * look correct.
     */
    private void setLabelsOverXCubedLabel() {
	DPoint bottomLeftCornerOfXCubedLabel = _xCubedLabel
		.getBottomLeftCorner();
	_xLabel.setBottomLeftCorner(bottomLeftCornerOfXCubedLabel);
	double pctWidthOverlap = 5;
	double pctHeightOverlap = 38;
	double xPos = _xLabel.getPosition().x + (1 - pctWidthOverlap / 100)
		* _xLabel.getWidth();
	double yPos = _xLabel.getPosition().y + (pctHeightOverlap / 100)
		* _xLabel.getHeight();
	_threeLabel.setImage("32pt/ThreeLabel.gif");
	_threeLabel.setBottomLeftCorner(new DPoint(xPos, yPos));
	_twoLabel.setBottomLeftCorner(_threeLabel.getBottomLeftCorner());
	_componentList.bringToTopOfZOrder(_xLabel);
	_componentList.bringToTopOfZOrder(_threeLabel);
    }

    // ---------------------------------------

    private static final boolean MAKE_TANGENT_DISAPPEAR = false;
    // private static final boolean MAKE_TANGENT_DISAPPEAR = true;

    private static final int NUM_SCENES = 15;
    /**
     * The scene where the line becomes a secant of the two points
     */
    private static final int ATTACH_SCENE = 6;
    private static final int SLOPE_FORMULA_VISIBLE_SCENE = 8;
    /**
     * THe scene when the panel is returned to from BMI_Panel
     */
    private static final int POST_BMI_SCENE = 13;

    private boolean _groupXCubedLabels = true;
    private int _xCubedLabelsDragGroupId = -1;

    private Axes _axes = null;
    private QuadCurve _curve = null;
    private Label _curveEquationLabel = null;
    private Circle _tangentPoint = null;
    private Line _tangentLine = null;
    private Circle _secantPoint = null;

    private SecantTriangle _secantTriangle = null;
    private Label _deltaXLabel = null;
    private Label _deltaYLabel = null;
    private Label _deltaYXFormulaLabel = null;
    private Label _dydxFormulaLabel = null;
    private Label _questionMarkFormulaLabel = null;

    private SecantTriangle _dydxTriangle = null;
    private Label _dxLabel = null;
    private Label _dyLabel = null;

    private Label _curveFormulaLabel = null;
    private Label _slopeFormulaLabel = null;
    private Label _xCubedLabel = null;
    private Label _xLabel = null;
    private Label _twoLabel = null;
    private Label _threeLabel = null;

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
