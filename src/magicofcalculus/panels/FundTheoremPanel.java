//
// AreaFunctionPanel.java
//
package magicofcalculus.panels;

import james.Tools;
import james.annotations.AutoCaller;
import james.annotations.drag.Drag;
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
import magicofcalculus.components.RectComponent;
import magicofcalculus.components.SecantTriangle;

/**
 * <p>
 * TODO add Mathematical description of what the panel actually demonstrates
 * <p>
 * 
 * @description the fundamental theoreom of calculus.
 * @axes 2
 * @graph simple curves
 * @interactive The slider points on the two area triangles are synced together
 * @author TJ Johnson
 * @documentation James Arlow<james.arlow@gmail.com>
 */
@Scenes( { @Scene(index = 0, description = "Start"),
	@Scene(index = 1, description = "Lower Equation"),
	@Scene(index = 2, description = "Slope Equation"),
	@Scene(index = 3, description = "Upper Equation"),
	@Scene(index = 4, description = "Main Equation Grouped"),
	@Scene(index = 5, description = "Equation Ungrouped"),
	@Scene(index = 6, description = "Equation Regrouped"),
	@Scene(index = 7, description = "You shouldn't be seeing this!") })
public class FundTheoremPanel extends Panel {

    /**
     * Initialize all components<br>
     * Add all components to list<br>
     * <code>
     * setSyncParams();<br>
     * syncComponents();
     * </code>
     */
    public FundTheoremPanel() {

	super();
	setNumScenes(8);

	DPoint lowerOrigin = new DPoint(50, 450);
	DPoint upperOrigin = new DPoint(50, 225);
	int lengthOfAxisXInPanel = 300;
	int lengthOfAxisYInPanel = 200;
	int lengthOfAxisXLocal = 15;// 20::1
	int lengthOfAxisYLocal = 10;

	_lowerAxes.setAxesInPanel(lowerOrigin, lengthOfAxisXInPanel,
		lengthOfAxisYInPanel);
	_lowerAxes.setAxesLocal(lengthOfAxisXLocal, lengthOfAxisYLocal);
	final double axesStrokeWidth = 1.0;
	_lowerAxes.setStrokeWidth(axesStrokeWidth);

	_upperAxes.setAxesInPanel(upperOrigin, lengthOfAxisXInPanel,
		lengthOfAxisYInPanel);
	_upperAxes.setAxesLocal(lengthOfAxisXLocal, lengthOfAxisYLocal);
	_upperAxes.setStrokeWidth(axesStrokeWidth);

	setNextButtonArea(lowerOrigin.x, lowerOrigin.y + 20,
		lengthOfAxisXInPanel, Panel.PANEL_HIEGHT - lowerOrigin.y - 20);
	setNextButtonArea(50, PANEL_HIEGHT - 30, PANEL_WIDTH - 50, 30);// default
	// bottom
	// 50
	// pixels

	_curveFunction.setCenter(2, 17);
	_curveFunction.setRadius(10);
	_lowerGraph = _lowerAxes.getPolyLineFromFunction(NUM_INTERVALS, 3, 9,
		_curveFunction);
	final int gray = 0x77;
	_lowerGraph.setColor(new Color(gray, gray, gray));
	_lowerGraph.setStrokeWidth(15.0);

	_curveFunction.setRadius(15);
	_curveFunction.setCenter(-6, 13);
	_upperGraph = _upperAxes.getPolyLineFromFunction(NUM_INTERVALS, 3.5,
		8.7, _curveFunction);
	_upperGraph.setColor(new Color(0xdd, 0x11, 0x44));// light brown
	_upperGraph.setStrokeWidth(30.0);

	final double leftEndpointPanel = 166;
	final double rightEndpointPanel = 200;
	final double topOfRectanglePanel = 285;
	final double bottomOfTrianglePanel = 125;
	final double topOfTrianglePanel = 55;

	_bgRectangle.setRectComponent(new DPoint(leftEndpointPanel,
		topOfRectanglePanel), new DPoint(rightEndpointPanel,
		lowerOrigin.y));

	_fillRectangle.setRectComponent(new DPoint(leftEndpointPanel,
		topOfRectanglePanel), new DPoint(leftEndpointPanel,
		lowerOrigin.y));

	_dAdxTriangle.setTriangle(new DPoint(leftEndpointPanel,
		bottomOfTrianglePanel), new DPoint(rightEndpointPanel,
		topOfTrianglePanel));

	_lowerPoint.setCenter(leftEndpointPanel, lowerOrigin.y);
	_upperPoint.setCenter(leftEndpointPanel, bottomOfTrianglePanel);

	setLabelsOverMainEqLable();
	_mainEqLabelsDragGroupId = createDragGroup();
	addToDragGroup(_mainEqLabelsDragGroupId, _mainEqShortLabel);
	addToDragGroup(_mainEqLabelsDragGroupId, _mainEqDxLabel);
	addToDragGroup(_mainEqLabelsDragGroupId, _fundamentalEqLabel);

	/**
	 * This is neccessary because polylines are generated from axes and
	 * override the autocall properties. All properties are going to be
	 * reapplied
	 * <p>
	 * TODO: Modify whitelist in autocall to work with target types in
	 * addition to annotation types
	 * <p>
	 * TODO: Fix the visibility cache problem here.
	 */
	AutoCaller.m.autoCall(this);
	visibility.build();
	_componentList.clear();
	Tools.addAllComponents(this);
	//
	setSyncParams();
	syncComponents();
    }

    // From Panel
    public void setScene(int scene) {
	super.setScene(scene);
	switch (scene) {
	case 0:
	    syncComponents();
	    break;
	case MAIN_EQUATION_SCENE:// 4
	    setLabelsOverMainEqLable();
	    syncMainEqLabels();
	    addToDragGroup(_mainEqLabelsDragGroupId, _mainEqDxLabel);
	    break;
	case 5:
	    // setMainEqLabelsGrouped(false);
	    removeFromDragGroup(_mainEqLabelsDragGroupId, _mainEqDxLabel);
	    _componentList.bringToTopOfZOrder(_mainEqDxLabel);
	    break;
	case 6:
	    // setMainEqLabelsGrouped(true);
	    addToDragGroup(_mainEqLabelsDragGroupId, _mainEqDxLabel);
	    syncComponents();
	    break;
	case 7:
	    if (getTopLevelAncestor() == null)
		break;
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	}
    }

    /**
     * Ensure the position of the slider is within limits
     */
    @Override
    protected void setSyncParams() {
	// just get the value from the _lowerPoint
	_xValuePanel = _lowerPoint.getCenter().x;
	// make sure _xValuePanel is within limits
	if (_xValuePanel < _bgRectangle.getUpperLeft().x)
	    _xValuePanel = _bgRectangle.getUpperLeft().x;
	else if (_xValuePanel > _bgRectangle.getLowerRight().x)
	    _xValuePanel = _bgRectangle.getLowerRight().x;
    }

    /**
     * Ensures that points are in their correct positions then calls
     * <code>syncMainEqLabels()</code>
     */
    protected void syncComponents() {

	// _lowerPoint
	_lowerPoint.setCenter(_xValuePanel, _lowerAxes.getOrigin().y);

	// _upperPoint
	double triangleHeight = (_dAdxTriangle.getTangentPoint().y - _dAdxTriangle
		.getSecantPoint().y);
	double pctCovered = (_xValuePanel - _bgRectangle.getUpperLeft().x)
		/ (_bgRectangle.getLowerRight().x - _bgRectangle.getUpperLeft().x);
	double upperPointY = _dAdxTriangle.getTangentPoint().y - pctCovered
		* triangleHeight;
	_upperPoint.setCenter(_xValuePanel, upperPointY);

	// fill rect
	if (_xValuePanel == _bgRectangle.getUpperLeft().x) {
	    _fillRectangle.setVisible(false);
	} else {
	    _fillRectangle.setVisible(true);
	    _fillRectangle.setLowerRight(new DPoint(_xValuePanel, _lowerAxes
		    .getOrigin().y));
	}

	syncMainEqLabels();
    }

    // ---------------------------------------

    // Movable Algebra
    /**
     * Creates drag group<br>
     * from: _mainEqShortLabel, _mainEqDxLabel, _fundamentalEqLabel <br>
     * using id: _mainEqLabelsDragGroupId
     * <p>
     * unless set is false then it deletes the drag group
     */
    private void setMainEqLabelsGrouped(boolean set) {
	// removeFromDragGroup(int groupId, Component comp)
	_groupMainEqLabels = set;
	if (_groupMainEqLabels) {
	    addToDragGroup(_mainEqLabelsDragGroupId, _mainEqShortLabel);
	    addToDragGroup(_mainEqLabelsDragGroupId, _mainEqDxLabel);
	    addToDragGroup(_mainEqLabelsDragGroupId, _fundamentalEqLabel);
	} else
	    clearDragGroup(_mainEqLabelsDragGroupId);

    }

    /**
     * Enusres equation labels say poperly combined and sets _fundamentalEqLabel
     * visibility based on MAIN_EQUATION_SCENE
     */
    private void syncMainEqLabels() {
	if (_mainEqDxLabel.getPosition().x < _mainEqShortLabel.getPosition().x + 50) {
	    _mainEqShortLabel.setVisible(false);
	    _fundamentalEqLabel.setVisible(getScene() >= MAIN_EQUATION_SCENE);
	} else {
	    _mainEqShortLabel.setVisible(getScene() >= MAIN_EQUATION_SCENE);
	    _fundamentalEqLabel.setVisible(false);
	}
    }

    /**
     * TODO remove, this is definitely a test method and unimportant to
     * functionality Sets partial labels on top of full equation label
     */
    private void setLabelsOverMainEqLable() {
	DPoint pos = _mainEqLabel.getPosition();// 415,147
	_mainEqShortLabel.setPosition(pos.getTranslation(0, 0));// 415,147
	_mainEqDxLabel.setPosition(pos.getTranslation(193, 0));// 608,147
	_fundamentalEqLabel.setPosition(pos.getTranslation(-7, -30));// 408,117
	_componentList.bringToTopOfZOrder(_mainEqShortLabel);
	_componentList.bringToTopOfZOrder(_mainEqDxLabel);
    }

    // ---------------------------------------

    // ---------------------------------------

    private static final int NUM_INTERVALS = 100;
    /**
     * The scene at which an additional equation label is added.
     */
    private static final int MAIN_EQUATION_SCENE = 4;

    /**
     * @name Upper Axes
     * @description The upper cartesian plane
     */
    @zIndex(layers.axes)
    @Visible
    public Axes _lowerAxes;

    /**
     * @name Lower Axes
     * @description The lower cartesian plane
     */
    @zIndex(layers.axes)
    @Visible
    public Axes _upperAxes;

    /**
     * @name Upper Graph
     * @description The graph line on the upper axes
     */
    @zIndex(layers.graph)
    @Visible
    public PolyLine _lowerGraph;

    /**
     * @name Upper Graph
     * @description The graph line on the lower axes
     */
    @zIndex(layers.graph)
    @Visible
    public PolyLine _upperGraph;

    /**
     * @name Area Fill
     * @description The area of the triangle is filled to show its analogy in
     *              the theorem
     */
    @Visible
    @zIndex(layers.fill)
    @color(index = MagicApplet._LIGHT_GREEN, mode = color.Mode.field, src = MagicApplet.class)
    public RectComponent _bgRectangle;

    /**
     * TODO This is set visible so it will be displayed in the docs, the action
     * handler may not disable it on scene starts yet.
     * 
     * @name Lower Fill
     * @description The integral width is shown in the lower graph
     */
    @Visible
    @zIndex(layers.fill)
    @color("blue")
    public RectComponent _fillRectangle;

    @Visible
    @zIndex(layers.fill)
    @color(index = MagicApplet._LIGHT_GREEN, mode = color.Mode.field, src = MagicApplet.class)
    public SecantTriangle _dAdxTriangle;

    /**
     * @name Lower Point
     * @description The draggable point on the lower graph
     */
    @zIndex(layers.points)
    @Visible
    @Drag
    @color("blue")
    public Circle _lowerPoint;

    /**
     * @name Upper Point
     * @description The point on the upper graph
     */
    @zIndex(layers.points)
    @Visible
    @color("red")
    public Circle _upperPoint;

    Function.LowerSemiCircle _curveFunction = new Function.LowerSemiCircle();

    /**
     * @name Function : F
     * @description The lower graph formula
     */
    @Visible
    @Drag
    @zIndex(layers.label)
    @Position(x = 50, y = 262)
    @Image("24pt/FLabel.gif")
    public Label _fLabel;

    /**
     * @name Function : A
     * @description The upper graph formula
     */
    @Visible
    @Drag
    @zIndex(layers.label)
    @Position(x = 50, y = 140)
    @Image("24pt/ALabel.gif")
    public Label _aLabel;

    @Visible
    @Drag
    @zIndex(layers.label)
    @Position(x = 74, y = 373)
    @Image("32pt/DxDimension left.gif")
    public Label _dxDimensionLabel;

    @Visible
    @Drag
    @zIndex(layers.label)
    @Position(x = 274, y = 331)
    @Image("32pt/FLabel.gif")
    public Label _bigFLabel;

    @Visible
    @Drag
    @zIndex(layers.label)
    @Position(x = 202, y = 66)
    @Image("32pt/DaLabel.gif")
    public Label _daLabel;

    @Visible
    @Drag
    @zIndex(layers.label)
    @Position(x = 158, y = 130)
    @Image("32pt/DxLabel.gif")
    public Label _dxLabel;
    /**
     */
    @Visible
    @Drag
    @zIndex(layers.label)
    @Position(x = 235, y = 288)
    @Image("32pt/VerticalDimension.gif")
    public Label _verticalDimensionLabel;
    /**
     */
    @Visible(1)
    @Drag
    @zIndex(layers.label)
    @Position(x = 352, y = 252)
    @Image("28pt/Lower Area Eq.gif")
    public Label _lowerAreaEqLabel;

    /**
     */
    @Visible(3)
    @Drag
    @zIndex(layers.label)
    @Position(x = 307, y = 67)
    @Image("28pt/Upper Area Eq.gif")
    public Label _upperAreaEqLabel;

    /**
     * @name Slope Equation
     * @description The slope of the triangle is shown on the upper graph
     */
    @Visible(2)
    @Drag
    @zIndex(layers.label)
    @Position(x = 25, y = 20)
    @Image("24pt/Slope Eq.gif")
    public Label _slopeEqLabel;

    @Visible(4)
    @Drag
    @zIndex(layers.label)
    @Position(x = 415, y = 147)
    @Image("32pt/Main Eq.gif")
    public Label _mainEqLabel;

    /**
     * @name Main Equation Short Label
     * @description ?
     */
    @Drag
    @zIndex(layers.label)
    @Image("32pt/Main Eq Short.gif")
    @Position(x = 415, y = 147)
    public Label _mainEqShortLabel;

    /**
     * @name Delta X
     * @description The change in x symbolified
     */
    @Visible(4)
    @Drag
    @zIndex(layers.label)
    @Position(x = 608, y = 147)
    @Image("32pt/DxLabel.gif")
    public Label _mainEqDxLabel;

    /**
     * @name Fundamental Equation
     * @description The fundamental equation of calculus
     */
    @Drag
    @zIndex(layers.label)
    @Position(x = 408, y = 117)
    @Image("32pt/Fundamental Eq.gif")
    public Label _fundamentalEqLabel;

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
	public static final int fill = 20;
	public static final int points = 30;
	public static final int triangle = 60;
    }

    private boolean _groupMainEqLabels = true;
    private int _mainEqLabelsDragGroupId = -1;

    // Sync Param
    private double _xValuePanel = 0;// in Panel coordinates

}
// ---------------------------------------
// ------------------------------------------------
