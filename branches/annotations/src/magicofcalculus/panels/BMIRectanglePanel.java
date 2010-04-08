//
// BMIRectanglePanel.java
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
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;
import magicofcalculus.components.Axes;
import magicofcalculus.components.Circle;
import magicofcalculus.components.Label;
import magicofcalculus.components.PolyLine;

/**
 * This is the zoomed in version of the RiemensSumsPanel
 * <p>
 * TODO add Mathematical description of what the panel actually demonstrates
 * <p>
 * 
 * @axes
 * @graph
 * @interactive
 * @author TJ Johnson
 * @documentation James Arlow<james.arlow@gmail.com>
 */
@Scenes( { @Scene(index = 0, description = "Start", next = true),
	@Scene(index = 1) })
public class BMIRectanglePanel extends Panel {

    /**
     * <ul>
     * <li>super
     * <li>2 scenes
     * <li>Sets axes position and scale
     * <li>intializes graph settings, points and labels.
     * <li>setSyncParams();
     * <li>syncComponents();
     * </ul>
     */
    public BMIRectanglePanel() {

	super();

	DPoint origin = new DPoint(0, 450);
	int lengthOfAxisX = 450;
	int lengthOfAxisY = 800;
	int lengthOfAxisXLocal = 10;
	int lengthOfAxisYLocal = 800 / 45;
	_leftXLocal = 0;
	_rightXLocal = 10;

	_axes.setAxesInPanel(origin, lengthOfAxisX, lengthOfAxisY);
	_axes.setAxesLocal(lengthOfAxisXLocal, lengthOfAxisYLocal);
	_axes.setRiemannRectsVisible(true);
	// _axes.setRiemannRightEndPoints(true);
	_axes.setRiemannRightEndPoints(false);

	// _axes.setRiemannRectsStrokeWidth(55.0/2);
	_axes.setRiemannRectsStrokeWidth(1);

	setNextButtonArea(origin.x + 50, origin.y + 20, lengthOfAxisX,
		Panel.PANEL_HIEGHT - origin.y - 20);

	_leftXPanel = _axes.transformLocalToPanel(new DPoint(_leftXLocal, 0)).x;
	_rightXPanel = _axes.transformLocalToPanel(new DPoint(_rightXLocal, 0)).x;

	_leftStopXPanel = 55;
	double startXPanel = 193;
	double h = 1;
	double slope = .9;
	_linearFunction.setLinearFunction(new DPoint(0, h), slope);
	_lowerGraph = _axes.getPolyLineFromFunction(NUM_INTERVALS, 0, 10,
		_linearFunction);
	_linearFunction.setLinearFunction(new DPoint(0, h + 1.2), slope);
	// _linearFunction.setLinearFunction(new
	// DPoint(0,h+slope*_leftStopXPanel/45),slope);
	_upperGraph = _axes.getPolyLineFromFunction(NUM_INTERVALS, 0, 10,
		_linearFunction);

	_lowerGraph.setStrokeWidth(1);
	_upperGraph.setStrokeWidth(1);

	_axes.setFillBetweenCurves(_upperGraph, _lowerGraph, 0,
		Panel.PANEL_WIDTH);

	_leftPoint.setCenter(_leftXPanel, origin.y);

	// _rightPoint.setCenter(_leftXPanel+.25*(_rightXPanel-_leftXPanel),origin.y);
	_rightPoint.setCenter(startXPanel, origin.y);
	_rightPoint.setDiameter(2);
	// _rightPoint.setHitMargin(25);

	setSyncParams();
    }

    // From Panel
    /**
     * Sets scene description using switch or forwards scene
     */
    protected void setScene(int scene) {
	super.setScene(scene);
	switch (scene) {
	case 1:
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	}
    }

    /**
     * Makes sure draggin points don't leave their bounds
     */
    protected void setSyncParams() {
	if (_componentList.get(0) == _rightPoint) {
	    _xValuePanel = _rightPoint.getCenter().x;
	}
	// if (_xValuePanel<_leftXPanel) _xValuePanel=_leftXPanel;
	if (_xValuePanel < _leftStopXPanel)
	    _xValuePanel = _leftStopXPanel;
	if (_xValuePanel > _rightXPanel)
	    _xValuePanel = _rightXPanel;
    }

    // ---------------------------------------

    /**
     * The number of intervals use when drawing the curve.
     */
    private static final int NUM_INTERVALS = 100;

    @color("blue")
    @Fill(color = "red")
    @zIndex(LAYERS.axes)
    public Axes _axes;

    /**
     * constants for zIndex Layers.
     * 
     * @author James Arlow
     * 
     */
    public static final class LAYERS {

	public static final int axes = 2;

	public static final int graph = 1;

	public static final int points = 3;

	public static final int label = 4;
    }

    @Visible
    @zIndex(LAYERS.graph)
    @color("red")
    public PolyLine _lowerGraph;

    @Visible
    @zIndex(LAYERS.graph)
    @color("red")
    public PolyLine _upperGraph;

    @Visible
    @zIndex(LAYERS.points)
    @color("red")
    public Circle _leftPoint;

    @Visible
    @zIndex(LAYERS.points)
    @Drag(action = MaxLeft.class)
    @color("blue")
    public Circle _rightPoint;

    /**
     * Handles the action when the components are dragged
     * <p>
     * Enables the change in rectangle display properties when the maximum left
     * drag position occurs.
     * 
     * @manual If the point is dragged to its maximum left position, the
     *         rectangle indicates a review of the situation
     * 
     * @author James Arlow
     * 
     */
    public class MaxLeft extends Drag.Handler {

	@Override
	public void action(Object... params) {
	    for (int i = 0; i < _componentList.size(); i++) {

		if (_componentList.get(i) == _rightPoint) {
		    _rightPoint.setCenter(_xValuePanel, _axes.getOrigin().y);
		}

		else if (_componentList.get(i) == _axes) {
		    double xValueLocal = _axes
			    .transformPanelToLocal(new DPoint(_xValuePanel,
				    _axes.getOrigin().y)).x;
		    double deltaXLocal = (xValueLocal - _leftXLocal);
		    _axes.setRiemannRects(_leftXLocal, _rightXLocal,
			    deltaXLocal, _linearFunction);
		    if (_xValuePanel == _leftStopXPanel) {
			_axes.setRiemannRectsColor(MagicApplet.GREEN);
			_dxLabel.setVisible(true);
		    } else {
			_axes.setRiemannRectsColor(Color.blue);
			_dxLabel.setVisible(false);
		    }
		}
	    }
	}

    }

    @Visible
    @zIndex(LAYERS.label)
    @Drag
    @Image("36pt/DxDimension outside.gif")
    @Position(x = 173, y = 320)
    public Label _dxLabel;

    public Function.LinearFunction _linearFunction = new Function.LinearFunction();

    // Sync Param
    private double _xValuePanel = 193;// in Panel coordinates

    private double _leftXLocal = 0;
    private double _rightXLocal = 0;
    private double _leftXPanel = 0;
    private double _rightXPanel = 0;
    private double _leftStopXPanel = 0;

}
// ---------------------------------------
// ------------------------------------------------
