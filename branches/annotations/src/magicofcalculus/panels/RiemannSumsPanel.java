//
// RiemannSumsPanel.java
//
package magicofcalculus.panels;

import james.annotations.scenes.Scene;
import james.annotations.scenes.Scenes;

import java.awt.Color;

import magicofcalculus.DPoint;
import magicofcalculus.Function;
import magicofcalculus.Line;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;
import magicofcalculus.Function.XSquaredPlusOne;
import magicofcalculus.components.Axes;
import magicofcalculus.components.Circle;
import magicofcalculus.components.Label;
import magicofcalculus.components.PolyLine;

/**
 * TODO add Mathematical description of what the panel actually demonstrates
 * <p>
 * 
 * @axes
 * @graph
 * @interactive
 * @author TJ Johnson
 * @documentation James Arlow<james.arlow@gmail.com>
 */
@Scenes( { @Scene(index = 0, description = "Start"),
	@Scene(index = 1, description = "You shouldn't be seeing this!"),
	@Scene(index = 2, description = "Post BMI"),
	@Scene(index = 3, description = "You shouldn't be seeing this!") })
public class RiemannSumsPanel extends Panel {

    public RiemannSumsPanel() {

	super();
	setNumScenes(4);// min 2

	DPoint origin = new DPoint(50, 400);
	int lengthOfAxisX = 350;
	int lengthOfAxisY = 350;
	int lengthOfAxisXLocal = 2;
	int lengthOfAxisYLocal = 5;
	_leftXLocal = .5;
	_rightXLocal = 1.5;

	_axes = new Axes(this);
	_axes.setAxesInPanel(origin, lengthOfAxisX, lengthOfAxisY);
	_axes.setAxesLocal(lengthOfAxisXLocal, lengthOfAxisYLocal);
	_axes.setFillUnderCurveColor(MagicApplet.GREEN);

	_leftXPanel = _axes.transformLocalToPanel(new DPoint(_leftXLocal, 0)).x;
	_rightXPanel = _axes.transformLocalToPanel(new DPoint(_rightXLocal, 0)).x;

	_graph = _axes.getPolyLineFromFunction(NUM_INTERVALS, 0,
		lengthOfAxisXLocal, _function);
	_graph.setColor(Color.red);
	_graph.setVisible(true);

	_axes.setFillUnderCurve(_graph, _leftXPanel, _rightXPanel);

	_leftPoint = new Circle(this);
	_leftPoint.setCenter(_leftXPanel, origin.y);
	_leftPoint.setColor(Color.red);
	_leftPoint.setVisible(true);

	_rightPoint = new Circle(this);
	_rightPoint.setCenter(_leftXPanel + (_rightXPanel - _leftXPanel) / 4,
		origin.y);
	_rightPoint.setColor(Color.blue);
	_rightPoint.setDraggable(true);
	_rightPoint.setVisible(true);
	// _rightPoint.setDiameter(25);

	_axes.setRiemannRectsVisible(true);
	// _axes.setRiemannRightEndPoints(true);
	_axes.setRiemannRightEndPoints(false);
	_axes.setVisible(true);
	_axes.setRiemannRectsColor(Color.blue);
	// _axes.setRiemannRectsStrokeWidth(4);
	_axes.setRiemannRectsStrokeWidth(1);

	_dxDimensionLabel = new Label(this);
	_dxDimensionLabel.setImage("28pt/DxDimension.gif");
	_dxDimensionLabel.setDisplayImage(true);
	_dxDimensionLabel.setPosition(187, 332);
	_dxDimensionLabel.setDraggable(true);// dev, for locating permanent
	// position

	_dxLine = new Line(this);
	double lineXValueLocal = 1.0;
	double lineYValueLocal = _function.getYofX(lineXValueLocal);
	_dxLine.setStrokeWidth(2.0);
	_dxLine.setLine(_axes.transformLocalToPanel(new DPoint(lineXValueLocal,
		0)), _axes.transformLocalToPanel(new DPoint(lineXValueLocal,
		lineYValueLocal)));

	_componentList.add(0, _graph);
	_componentList.add(0, _axes);
	_componentList.add(0, _leftPoint);
	_componentList.add(0, _dxDimensionLabel);
	_componentList.add(0, _dxLine);
	_componentList.add(0, _rightPoint);// does this need to be on top??

	// setScene(0);
	setSyncParams();
	syncComponents();
    }

    // From Panel
    protected void setScene(int scene) {
	super.setScene(scene);
	switch (scene) {
	case 0:
	    syncComponents();
	    break;
	case 1:
	    if (_sceneAdvancing)
		((MagicApplet) getTopLevelAncestor()).advancePanel();
	    else
		((MagicApplet) getTopLevelAncestor()).reversePanel();
	    break;
	case POST_BMI_SCENE:// 2
	    syncComponents();
	    break;
	case 3:
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	}
    }

    protected void setSyncParams() {

	boolean xValuePanelWasEqualToLeftXPanel = (_xValuePanel == _leftXPanel);

	if (_componentList.get(0) == _rightPoint) {
	    _xValuePanel = _rightPoint.getCenter().x;
	}

	// prevent from leaving _xValuePanel set to _leftXPanel+1
	if (_xValuePanel > _rightXPanel)
	    _xValuePanel = _rightXPanel;
	if (xValuePanelWasEqualToLeftXPanel) {
	    if (_xValuePanel == _leftXPanel + 1)
		_xValuePanel = _leftXPanel + 2;
	    else if (_xValuePanel < _leftXPanel)
		_xValuePanel = _leftXPanel;
	} else {
	    if (_xValuePanel <= _leftXPanel + 1)
		_xValuePanel = _leftXPanel;
	}

    }

    protected void syncComponents() {

	for (int i = 0; i < _componentList.size(); i++) {

	    if (_componentList.get(i) == _rightPoint) {
		_rightPoint.setCenter(_xValuePanel, _axes.getOrigin().y);
	    }

	    else if (_componentList.get(i) == _axes) {
		double xValueLocal = _axes.transformPanelToLocal(new DPoint(
			_xValuePanel, _axes.getOrigin().y)).x;
		double deltaXLocal = (xValueLocal - _leftXLocal);
		_axes.setRiemannRects(_leftXLocal, _rightXLocal, deltaXLocal,
			_function);

		if (_xValuePanel == _leftXPanel && getScene() >= POST_BMI_SCENE) {
		    _axes.setFillUnderCurveVisible(true);
		    _dxDimensionLabel.setVisible(true);
		    _dxLine.setVisible(true);
		} else {
		    _axes.setFillUnderCurveVisible(false);
		    _dxDimensionLabel.setVisible(false);
		    _dxLine.setVisible(false);
		}
	    }
	}
    }

    // ---------------------------------------

    private static final int NUM_INTERVALS = 100;
    private static final int POST_BMI_SCENE = 2;

    private Axes _axes = null;
    private PolyLine _graph = null;
    private Circle _leftPoint = null;
    private Circle _rightPoint = null;
    private Label _dxDimensionLabel = null;
    private Line _dxLine = null;

    Function _function = new Function.XSquaredPlusOne();

    // Sync Param
    private double _xValuePanel = 0;// in Panel coordinates

    private double _leftXLocal = 0;
    private double _rightXLocal = 0;
    private double _leftXPanel = 0;
    private double _rightXPanel = 0;

}
// ---------------------------------------
// ------------------------------------------------
