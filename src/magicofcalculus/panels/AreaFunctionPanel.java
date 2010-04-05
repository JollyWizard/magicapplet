//
// AreaFunctionPanel.java
//
package magicofcalculus.panels;

import james.annotations.AxesProperties;
import james.annotations.PolyLineConfig;
import james.annotations.drag.Drag;
import james.annotations.draw.color;
import james.annotations.draw.Fill;
import james.annotations.labels.Image;
import james.annotations.placement.Dimensions;
import james.annotations.placement.Position;
import james.annotations.placement.Scale;
import james.annotations.scenes.Scene;
import james.annotations.scenes.Scenes;
import james.annotations.visibility.Visible;
import magicofcalculus.DPoint;
import magicofcalculus.Function;
import magicofcalculus.MagicApplet;
import magicofcalculus.Panel;
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
	@Scene(index = 2, description = "Before Movable Formulas"),
	@Scene(index = 3, description = "Movable Formulas Grouped"),
	@Scene(index = 4, description = "Formulas Ungrouped"),
	@Scene(index = 5, description = "Area Label and Regrouped") })
public class AreaFunctionPanel extends Panel {

    /**
     * <ol>
     * <li>setNumScenes(7);
     * <li>sets upperOrigin,lowerOrigin, lengthOfAxisXinPanel,
     * lengthOfAxisYinPanel,
     * <li>lengthOfAxisXLocal, lengthOfAxisYLocal
     * <li>creates and Configures _lowerAxes and _upperAxes
     * <li>setxxxArea relative to lowerOrigin: NextButton, BackButton</li>
     * <li>configures 2 shapes and 2 polylines
     * <li>configures 5 labels
     * <li>adds all components
     * <li>setLabelsOverThreeXSquaredLabel();
     * <li>_xCubedLabelsDragGroupId = createDragGroup();
     * <li>setSyncParams();
     * <li>syncComponents();
     * </ol>
     */
    public AreaFunctionPanel() {
	super();
	setNumScenes(7);

	_lowerPoint.setCenter(_lowerAxes.getOrigin());

	_upperPoint.setCenter(_upperAxes.getOrigin());

	_componentList.add(0, _lowerPoint);
	_componentList.add(0, _upperPoint);

	// setLabelsOverThreeXSquaredLabel();
	_xCubedLabelsDragGroupId = createDragGroup();
	setSyncParams();
    }

    /**
     * Modifies scene description and visibility via switch Last scene calls
     * MagicApplet.advancePlayer
     */
    @Override
    protected void setScene(int scene) {
	super.setScene(scene);
	switch (scene) {
	case 1:
	    if (_sceneAdvancing)
		((MagicApplet) getTopLevelAncestor()).advancePanel();
	    else
		((MagicApplet) getTopLevelAncestor()).reversePanel();
	    break;
	case 3:
	    // setLabelsOverThreeXSquaredLabel();
	    syncThreeLabel();
	    setXCubedLabelsGrouped(true);
	    break;
	case 4:
	    setXCubedLabelsGrouped(false);
	    break;
	case 5:
	    setXCubedLabelsGrouped(true);
	    syncComponents();
	    break;
	case 6:
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	}
    }

    /**
     * Syncs Lower point and upper point. Finds out which component has been
     * moved to the top of the component list because of drag.
     */
    protected void setSyncParams() {
	double oldXValuePanel = _xValuePanel;// save the old _xValuePanel for
	// repainting
	// set _xValuePanel
	if (_componentList.get(0) == _lowerPoint) {
	    _xValuePanel = _lowerPoint.getCenter().x;
	} else if (_componentList.get(0) == _upperPoint) {
	    _xValuePanel = _upperPoint.getCenter().x;
	}
	// make sure _xValuePanel is within limits
	if (_xValuePanel < _lowerAxes.getOrigin().x)
	    _xValuePanel = _lowerAxes.getOrigin().x;
	else if (_xValuePanel > _lowerAxes.getOrigin().x
		+ _lowerAxes.getXAxisLengthInPanel())
	    _xValuePanel = _lowerAxes.getOrigin().x
		    + _lowerAxes.getXAxisLengthInPanel();
	// repaint dragged over region
	if (oldXValuePanel < _xValuePanel)
	    repaint((int) oldXValuePanel - 1, 0,
		    (int) (_xValuePanel - oldXValuePanel) + 2, PANEL_HIEGHT);
	else
	    repaint((int) _xValuePanel - 1, 0,
		    (int) (oldXValuePanel - _xValuePanel) + 2, PANEL_HIEGHT);
    }

    /**
     * First syncs the two points, then syncs the labels.
     */
    protected void syncComponents() {
	syncThreeLabel();
    }

    /**
     * Sets the two points equal
     * 
     * @manual The two points will move in tandem.
     * @author T Johnson, James Arlow
     */
    public class DragPoint extends Drag.Handler {

	@Override
	public void action(Object... params) {
	    // _lowerPoint
	    _lowerPoint.setCenter(_xValuePanel, _lowerAxes.getOrigin().y);

	    // _upperPoint
	    // on the upper x axis in Panel coords
	    DPoint point = new DPoint(_xValuePanel, _upperAxes.getOrigin().y);
	    _upperAxes.transformPanelToLocal(point);
	    // on curve in local coords
	    point.y = new Function.IntegralOfSinXOverX().getYofX(point.x);
	    _upperAxes.transformLocalToPanel(point);
	    _upperPoint.setCenter(point);

	    // others
	    _upperGraph.setDrawingInterval(_upperAxes.getOrigin().x,
		    _xValuePanel);
	    _lowerAxes.setFillUnderCurve(_lowerGraph, _lowerAxes.getOrigin().x,
		    _xValuePanel);
	    _lowerAxes.setFillUnderCurveVisible(_xValuePanel != _lowerAxes
		    .getOrigin().x);
	}

    }

    // ---------------------------------------

    // Movable Algebra
    /**
     * Syncs the formula label
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
     * Syncs the x power label to the x variable label.
     */
    private void syncThreeLabel() {
	if (_threeLabel.getPosition().x < _xLabel.getPosition().x)
	    _threeLabel.setImage("32pt/ThreeLabelBig.gif");
	else
	    _threeLabel.setImage("32pt/ThreeLabel.gif");
    }

    /**
     * sets a second set of labels on top of the first set. I assume its a test
     * method.
     * 
     * @deprecated
     */
    private void setLabelsOverThreeXSquaredLabel() {
	DPoint pos = _threeXSquaredLabel.getPosition();// 694,195
	_threeLabel.setPosition(pos.getTranslation(0, 10));// 694,205
	_xLabel.setPosition(pos.getTranslation(19, 19));// 713,214
	_twoLabel.setPosition(pos.getTranslation(42, 3));// 736,198
	_componentList.bringToTopOfZOrder(_xLabel);
	_componentList.bringToTopOfZOrder(_threeLabel);
    }

    // ---------------------------------------

    /**
     * The number of intervals used to map the integral function
     */

    public static final double AXIS_X = 50;

    public static final int AXIS_W = 300;

    public static final int AXIS_H = 200;

    public static final int localX = 10;

    public static final int localY = 2;

    @Visible
    @AxesProperties(index = 0)
    @Scale(x = localX, y = localY)
    @Dimensions(width = AXIS_W, height = AXIS_H)
    @Fill(color = "blue")
    @Position(x = AXIS_X, y = 450)
    public Axes _lowerAxes;

    @Visible
    @AxesProperties(index = 1, localW = localX, localH = localY)
    @Position(x = AXIS_X, y = 225)
    @Fill(color = "red")
    @Dimensions(width = AXIS_W, height = AXIS_H)
    public Axes _upperAxes;

    @Visible
    @Drag(action = DragPoint.class)
    @color("blue")
    public Circle _lowerPoint;

    @Visible
    @Drag(action = DragPoint.class)
    @color("red")
    public Circle _upperPoint;

    @Visible
    @PolyLineConfig(axes = 0, function = Function.SinXOverX.class, intervals = NUM_INTERVALS, rightXLocal = 10)
    @color("black")
    public PolyLine _lowerGraph;

    @Visible
    @PolyLineConfig(axes = 1, function = Function.IntegralOfSinXOverX.class, intervals = NUM_INTERVALS, rightXLocal = 10)
    @color("red")
    public PolyLine _upperGraph;

    private static final int NUM_INTERVALS = 100;

    @Visible
    @Image("36pt/FLabel.gif")
    @Position(x = 145, y = 295)
    public Label _fLabel;

    @Visible
    @Image("36pt/ALabel.gif")
    @Position(x = 145, y = 113)
    public Label _aLabel;

    @Visible(value = 3)
    @Image("24pt/CurveFormulaLabel.gif")
    @Position(x = 411, y = 287)
    public Label _curveFormulaLabel;

    @Visible(value = 5)
    @Image("24pt/AreaFormulaLabel.gif")
    @Position(x = 454, y = 148)
    public Label _areaFormulaLabel;

    @Image("32pt/ThreeXSquaredLabel.gif")
    @Position(x = 638, y = 271)
    @Visible(value = 3)
    public Label _threeXSquaredLabel;

    @Image("32pt/XLabel.gif")
    @Visible(value = 3)
    public Label _xLabel;

    @Image("32pt/TwoLabel.gif")
    @Visible(value = 3)
    public Label _twoLabel;

    @Image("32pt/ThreeLabel.gif")
    @Visible(value = 3)
    public Label _threeLabel;

    private boolean _groupXCubedLabels = true;
    private int _xCubedLabelsDragGroupId = -1;

    // Sync Param
    private double _xValuePanel = 0;// in Panel coordinates

}
