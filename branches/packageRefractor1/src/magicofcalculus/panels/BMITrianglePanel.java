//
// BMITrianglePanel.java
//
package magicofcalculus.panels;

import java.awt.Color;

import magicofcalculus.Axes;
import magicofcalculus.DPoint;
import magicofcalculus.Function;
import magicofcalculus.Label;
import magicofcalculus.Line;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;
import magicofcalculus.PolyLine;
import magicofcalculus.SecantTriangle;
import magicofcalculus.Function.LowerSemiCircle;

/**
 * This is the zoomed in secantApproxPanel.
 * <p>
 * TODO add Mathematical description of what the panel actually demonstrates
 * <p>
 * 
 * @axes 1
 * @graph Quadratic Curve
 * @interactive The secant triangle can be resized by dragging and when it
 *              reaches minimum size the triangle changes color and the formula
 *              changes to infinitessimal form.
 * @author TJ Johnson
 * @documentation James Arlow<james.arlow@gmail.com>
 */
public class BMITrianglePanel extends Panel {

    /**
     * Normal stuff.<br>
     * build coordinates<br>
     * intialize all components<br>
     * set SyncParamters<br>
     */
    public BMITrianglePanel() {

	super();
	setNumScenes(2);

	DPoint origin = new DPoint(0, 450);
	int lengthOfAxisX = 450;
	int lengthOfAxisY = 450;
	int lengthOfAxisXLocal = 10;
	int lengthOfAxisYLocal = 10;

	_axes = new Axes(this);
	_axes.setAxesInPanel(origin, lengthOfAxisX, lengthOfAxisY);
	_axes.setAxesLocal(lengthOfAxisXLocal, lengthOfAxisYLocal);
	// _axes.setVisible(true);

	double radius = 17.1;
	_curveFunction.setCenter(new DPoint(-4.44, radius - .8));
	_curveFunction.setRadius(radius + 1.2);
	_lowerGraph = _axes.getPolyLineFromFunction(NUM_INTERVALS, 0, 13,
		_curveFunction);
	_curveFunction.setRadius(radius);
	_upperGraph = _axes.getPolyLineFromFunction(NUM_INTERVALS, 0, 13,
		_curveFunction);

	_lowerGraph.setVisible(true);
	_upperGraph.setVisible(true);
	_lowerGraph.setColor(Color.red);
	_upperGraph.setColor(Color.red);

	_axes.setFillUnderCurveColor(Color.red);
	_axes.setFillBetweenCurves(_upperGraph, _lowerGraph, 0, 600);
	_axes.setFillUnderCurveVisible(true);

	_secantTriangle = new SecantTriangle(this);
	double leftXLocal = 4;
	DPoint leftPointLocal = new DPoint(leftXLocal, _curveFunction
		.getYofX(leftXLocal));
	DPoint leftPointPanel = _axes.transformLocalToPanel(new DPoint(
		leftPointLocal));
	double rightXLocal = 12;
	DPoint rightPointLocal = new DPoint(rightXLocal, _curveFunction
		.getYofX(rightXLocal));
	DPoint rightPointPanel = _axes.transformLocalToPanel(new DPoint(
		rightPointLocal));
	_secantTriangle.setTriangle(leftPointPanel, rightPointPanel);
	_secantTriangle.setColor(Color.blue);
	_secantTriangle.setDraggable(true);
	_secantTriangle.setOutlineOnly(true);
	_secantTriangle.setStrokeWidth(2);
	_secantTriangle.setVisible(true);
	_secantTriangle.setInnerThickness(36);

	_tangentLine = new Line(this);
	_tangentLine.setLine(_secantTriangle.getTangentPoint(), _secantTriangle
		.getSecantPoint());
	_tangentLine.setColor(Color.blue);
	_tangentLine.setStrokeWidth(2);
	// _tangentLine.setVisible(true);
	// _tangentLine.setInfiniteLength(true);
	_tangentLine.setLength(0, 1500);

	_deltaXLabel = new Label(this);
	_deltaXLabel.setImage("36pt/DeltaX.gif");
	_deltaXLabel.setDisplayImage(true);
	_deltaXLabel.setPosition(0, 0);
	_deltaXLabel.setVisible(true);

	_deltaYLabel = new Label(this);
	_deltaYLabel.setImage("36pt/DeltaY.gif");
	_deltaYLabel.setDisplayImage(true);
	_deltaYLabel.setPosition(0, 0);
	_deltaYLabel.setVisible(true);

	_dxLabel = new Label(this);
	_dxLabel.setImage("36pt/Dx.gif");
	_dxLabel.setDisplayImage(true);
	_dxLabel.setPosition(0, 0);

	_dyLabel = new Label(this);
	_dyLabel.setImage("36pt/Dy.gif");
	_dyLabel.setDisplayImage(true);
	_dyLabel.setPosition(0, 0);

	_deltaYXFormulaLabel = new Label(this);
	_deltaYXFormulaLabel.setImage("32pt/DeltaYXFormula.gif");
	_deltaYXFormulaLabel.setDisplayImage(true);
	_deltaYXFormulaLabel.setDraggable(true);
	_deltaYXFormulaLabel.setPosition(53, 166);
	_deltaYXFormulaLabel.setVisible(true);

	_dydxFormulaLabel = new Label(this);
	_dydxFormulaLabel.setImage("32pt/DyDxFormula.gif");
	_dydxFormulaLabel.setDisplayImage(true);
	_dydxFormulaLabel.setDraggable(true);
	_dydxFormulaLabel.setPosition(53, 166);

	_curveEquationLabel = new Label(this);
	_curveEquationLabel.setDraggable(true);
	_curveEquationLabel.setDisplayImage(true);
	_curveEquationLabel.setImage("28pt/CurveEquationLabel.gif");
	_curveEquationLabel.setVisible(true);
	// _curveEquationLabel.setPosition(422,230);
	_curveEquationLabel.setPosition(411, 4);

	_componentList.add(0, _axes);
	_componentList.add(0, _lowerGraph);
	_componentList.add(0, _upperGraph);
	_componentList.add(0, _tangentLine);
	_componentList.add(0, _deltaXLabel);
	_componentList.add(0, _deltaYLabel);
	_componentList.add(0, _deltaYXFormulaLabel);
	_componentList.add(0, _dxLabel);
	_componentList.add(0, _dyLabel);
	_componentList.add(0, _dydxFormulaLabel);
	_componentList.add(0, _curveEquationLabel);
	_componentList.add(0, _secantTriangle);// needs to be on top because of
	// setSyncParams()

	int groupId = createDragGroup();
	addToDragGroup(groupId, _deltaYXFormulaLabel);
	addToDragGroup(groupId, _dydxFormulaLabel);

	setSyncParams();
	syncComponents();
    }

    private void setSyncParamsFromSecantTriangle() {
	_xValuePanel = _secantTriangle.getCornerPoint().x;
	if (_xValuePanel < 282)
	    _xValuePanel = 282;
	if (_xValuePanel > 550)
	    _xValuePanel = 550;
	DPoint curvePt = new DPoint(_xValuePanel, _axes.getOrigin().y);// on the
	// x axis
	// in
	// Panel
	// coords
	_axes.transformPanelToLocal(curvePt);// on the x axis in Local coords
	curvePt.y = _curveFunction.getYofX(curvePt.x);// on curve in Local
	// coords
	_axes.transformLocalToPanel(curvePt);// on curve in Panel coords
	_yValuePanel = curvePt.y;
    }

    private void syncSecantTriangle() {
	_secantTriangle.setSecantPoint(new DPoint(_xValuePanel, _yValuePanel));
    }

    private void syncTangentLine() {
	_tangentLine.setLine(_secantTriangle.getTangentPoint(), new DPoint(
		_xValuePanel, _yValuePanel));
	_tangentLine.setLength(0, 1500);
    }

    /**
     * Groups label movements.
     */
    private void syncDeltaYXLabels() {
	DPoint secantPoint = new DPoint(_xValuePanel, _yValuePanel);
	double xPos = (_secantTriangle.getTangentPoint().x + secantPoint.x) / 2
		- _deltaXLabel.getRect().width / 2;
	double yPos = _secantTriangle.getTangentPoint().y;
	_deltaXLabel.setPosition(xPos, yPos);
	_dxLabel.setPosition(xPos, yPos);

	xPos = secantPoint.x;
	yPos = (_secantTriangle.getTangentPoint().y + secantPoint.y) / 2
		- _deltaXLabel.getRect().height / 2;
	_deltaYLabel.setPosition(xPos, yPos);
	_dyLabel.setPosition(xPos, yPos);
    }

    /**
     * Simple screen descriptor change. Forward to next panel after first scene
     */
    @Override
    protected void setScene(int scene) {
	super.setScene(scene);
	String sceneDescrip = "no scene";
	switch (scene) {
	// case 0:
	// sceneDescrip = "Next: Tangent Line";
	// _tangentLine.setVisible(false);
	// break;
	case 0:
	    // _tangentLine.setVisible(true);
	    sceneDescrip = "Start";
	    break;
	case 1:
	    sceneDescrip = "You shouldn't be seeing this!";
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	}
	setSceneString(sceneDescrip);

    }

    /**
     * if the top component is the secant triangle, call
     * setSyncParamsFromSecantTriangle();
     */
    protected void setSyncParams() {
	if (_componentList.get(0) == _secantTriangle) {
	    setSyncParamsFromSecantTriangle();
	}
    }

    /**
     * calls sync methods using weird for loop. TODO invesigate this for loop,
     * it's used elsewhere <br>
     * If x slider value is at max left (constant in code) then perform action
     * code
     */
    protected void syncComponents() {
	for (int i = 0; i < _componentList.size(); i++) {
	    if (_componentList.get(i) == _secantTriangle) {
		syncSecantTriangle();
	    } else if (_componentList.get(i) == _tangentLine) {
		syncTangentLine();
	    } else if (_componentList.get(i) == _deltaXLabel) {
		syncDeltaYXLabels();
	    }
	}

	if (_xValuePanel == 282) {
	    _deltaXLabel.setVisible(false);
	    _dxLabel.setVisible(true);
	    _deltaYLabel.setVisible(false);
	    _dyLabel.setVisible(true);
	    _secantTriangle.setColor(MagicApplet.GREEN);
	    _deltaYXFormulaLabel.setVisible(false);
	    _dydxFormulaLabel.setVisible(true);
	} else {
	    _deltaXLabel.setVisible(true);
	    _dxLabel.setVisible(false);
	    _deltaYLabel.setVisible(true);
	    _dyLabel.setVisible(false);
	    _secantTriangle.setColor(Color.blue);
	    _deltaYXFormulaLabel.setVisible(true);
	    _dydxFormulaLabel.setVisible(false);
	}
    }

    // ---------------------------------------

    private static final int NUM_INTERVALS = 100;

    private Axes _axes = null;
    private PolyLine _lowerGraph = null;
    private PolyLine _upperGraph = null;
    private SecantTriangle _secantTriangle = null;
    private Line _tangentLine = null;

    private Label _deltaXLabel = null;
    private Label _deltaYLabel = null;
    private Label _dxLabel = null;
    private Label _dyLabel = null;
    private Label _deltaYXFormulaLabel = null;
    private Label _dydxFormulaLabel = null;
    private Label _curveEquationLabel = null;

    private Function.LowerSemiCircle _curveFunction = new Function.LowerSemiCircle();

    // Sync Param
    private double _xValuePanel = 0;
    private double _yValuePanel = 0;

}
// ---------------------------------------
// ------------------------------------------------
/*
 * _deltaXLabel _deltaYLabel _deltaYXFormulaLabel _dxLabel _dyLabel
 * _dydxFormulaLabel
 */