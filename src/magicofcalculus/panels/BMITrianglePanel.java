//
// BMITrianglePanel.java
//
package magicofcalculus.panels;

import james.annotations.drag.Drag;
import james.annotations.draw.Fill;
import james.annotations.draw.color;
import james.annotations.labels.Image;
import james.annotations.placement.Position;
import james.annotations.placement.zIndex;
import james.annotations.scenes.Scene;
import james.annotations.scenes.Scenes;
import james.annotations.visibility.Visible;

import java.awt.Color;

import magicofcalculus.DPoint;
import magicofcalculus.Function;
import magicofcalculus.Line;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;
import magicofcalculus.Function.LowerSemiCircle;
import magicofcalculus.components.Axes;
import magicofcalculus.components.Label;
import magicofcalculus.components.PolyLine;
import magicofcalculus.components.SecantTriangle;

/**
 * This is the zoomed in secantApproxPanel.
 * <p>
 * TODO add Mathematical description of what the panel actually demonstrates
 * <p>
 * 
 * @description A closer, smaller scale look at the secant triangle
 * @axes 1
 * @graph Quadratic Curve
 * @interactive The secant triangle can be resized by dragging and when it
 *              reaches minimum size the triangle changes color and the formula
 *              changes to infinitessimal form.
 * @author TJ Johnson
 * @documentation James Arlow<james.arlow@gmail.com>
 */
@Scenes( { @Scene(index = 0, description = "Start"), @Scene(index = 1) })
public class BMITrianglePanel extends Panel {

    /**
     * @name Axes
     * @description The Axes for this panels graph
     */
    @Visible
    @Fill(color = "red")
    @zIndex(layers.axes)
    public Axes _axes;

    /**
     * @name Curve Equation
     */
    @Drag
    @Image("28pt/CurveEquationLabel.gif")
    @Visible
    @zIndex(layers.label)
    @Position(x = 350, y = 4)
    public Label _curveEquationLabel;

    @Visible
    @Image("36pt/DeltaX.gif")
    @zIndex(layers.label)
    @Position(x = 0, y = 0)
    public Label _deltaXLabel;

    @Visible
    @Image("36pt/DeltaY.gif")
    @zIndex(layers.label)
    @Position(x = 0, y = 0)
    public Label _deltaYLabel;

    @zIndex(layers.label)
    @Visible
    @Drag
    @Image("32pt/DeltaYXFormula.gif")
    @Position(x = 53, y = 166)
    public Label _deltaYXFormulaLabel;

    @Image("36pt/Dx.gif")
    @zIndex(layers.label)
    @Position(x = 0, y = 0)
    public Label _dxLabel;

    @Image("32pt/DyDxFormula.gif")
    @zIndex(layers.label)
    @Position(x = 53, y = 166)
    public Label _dydxFormulaLabel;

    @Image("36pt/Dy.gif")
    @zIndex(layers.label)
    @Position(x = 0, y = 0)
    public Label _dyLabel;

    // ---------------------------------------

    @Visible
    @color("red")
    @zIndex(layers.graph)
    public PolyLine _lowerGraph;

    @Visible
    @Drag
    @color("blue")
    @zIndex(layers.triangle)
    public SecantTriangle _secantTriangle;

    @Visible
    @color("blue")
    @zIndex(layers.line)
    public Line _tangentLine;

    @Visible
    @color("red")
    @zIndex(layers.graph)
    public PolyLine _upperGraph;

    private Function.LowerSemiCircle _curveFunction = new Function.LowerSemiCircle();

    // Sync Param
    private double _xValuePanel = 0;

    private double _yValuePanel = 0;

    /**
     * Normal stuff.<br>
     * build coordinates<br>
     * intialize all components<br>
     * set SyncParamters<br>
     */
    public BMITrianglePanel() {
	super();

	DPoint origin = new DPoint(0, 450);
	int lengthOfAxisX = 450;
	int lengthOfAxisY = 450;
	int lengthOfAxisXLocal = 10;
	int lengthOfAxisYLocal = 10;

	_axes.setAxesInPanel(origin, lengthOfAxisX, lengthOfAxisY);
	_axes.setAxesLocal(lengthOfAxisXLocal, lengthOfAxisYLocal);

	double radius = 17.1;
	_curveFunction.setCenter(new DPoint(-4.44, radius - .8));
	_curveFunction.setRadius(radius + 1.2);
	_lowerGraph = _axes.getPolyLineFromFunction(NUM_INTERVALS, 0, 13,
		_curveFunction);
	_curveFunction.setRadius(radius);
	_upperGraph = _axes.getPolyLineFromFunction(NUM_INTERVALS, 0, 13,
		_curveFunction);

	_axes.setFillBetweenCurves(_upperGraph, _lowerGraph, 0, 600);

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
	_secantTriangle.setOutlineOnly(true);
	_secantTriangle.setStrokeWidth(2);
	_secantTriangle.setInnerThickness(36);

	_tangentLine.setLine(_secantTriangle.getTangentPoint(), _secantTriangle
		.getSecantPoint());
	_tangentLine.setStrokeWidth(2);
	_tangentLine.setLength(0, 1500);

	int groupId = createDragGroup();
	addToDragGroup(groupId, _deltaYXFormulaLabel);
	addToDragGroup(groupId, _dydxFormulaLabel);

	setSyncParams();
	syncComponents();
    }

    private static final int NUM_INTERVALS = 100;

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

    private void syncSecantTriangle() {
	_secantTriangle.setSecantPoint(new DPoint(_xValuePanel, _yValuePanel));
    }

    private void syncTangentLine() {
	_tangentLine.setLine(_secantTriangle.getTangentPoint(), new DPoint(
		_xValuePanel, _yValuePanel));
	_tangentLine.setLength(0, 1500);
    }

    /**
     * Simple screen descriptor change. Forward to next panel after first scene
     */
    @Override
    public void setScene(int scene) {
	super.setScene(scene);
	switch (scene) {
	case 1:
	    if (getTopLevelAncestor() == null)
		break;
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	}
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

    public static class layers {
	public static final int axes = 0;
	public static final int graph = 1;
	public static final int label = 3;

	public static final int line = 2;
	public static final int triangle = 4;
    }

}
