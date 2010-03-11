//
// AreaFunctionPanel.java
//
package magicofcalculus;

import java.awt.Color;

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

	DPoint lowerOrigin = new DPoint(50, 450);
	DPoint upperOrigin = new DPoint(50, 225);
	int lengthOfAxisXInPanel = 300;
	int lengthOfAxisYInPanel = 200;
	int lengthOfAxisXLocal = 10;
	int lengthOfAxisYLocal = 2;

	_lowerAxes = new Axes(this);
	_lowerAxes.setAxesInPanel(lowerOrigin, lengthOfAxisXInPanel,
		lengthOfAxisYInPanel);
	_lowerAxes.setAxesLocal(lengthOfAxisXLocal, lengthOfAxisYLocal);
	_lowerAxes.setFillUnderCurveVisible(true);
	_lowerAxes.setFillUnderCurveColor(Color.blue);

	_upperAxes = new Axes(this);
	_upperAxes.setAxesInPanel(upperOrigin, lengthOfAxisXInPanel,
		lengthOfAxisYInPanel);
	_upperAxes.setAxesLocal(lengthOfAxisXLocal, lengthOfAxisYLocal);

	setNextButtonArea(lowerOrigin.x, lowerOrigin.y + 20,
		lengthOfAxisXInPanel, Panel.PANEL_HIEGHT - lowerOrigin.y - 20);
	// setBackButtonArea(0,lowerOrigin.y,lowerOrigin.x,Panel.PANEL_HIEGHT-lowerOrigin.y);
	setNextButtonArea(50, PANEL_HIEGHT - 30, PANEL_WIDTH - 50, 30);// default
	// bottom
	// 50
	// pixels

	_lowerPoint = new Circle(this);
	_lowerPoint.setCenter(lowerOrigin);
	_lowerPoint.setColor(Color.blue);
	_lowerPoint.setDraggable(true);

	_upperPoint = new Circle(this);
	_upperPoint.setCenter(upperOrigin);
	_upperPoint.setColor(Color.red);
	_upperPoint.setDraggable(true);

	_lowerGraph = _lowerAxes.getPolyLineFromFunction(NUM_INTERVALS, 0,
		lengthOfAxisXLocal, _curveFunction);
	// _lowerGraph.setColor(Color.red);

	_upperGraph = _upperAxes.getPolyLineFromFunction(NUM_INTERVALS, 0,
		lengthOfAxisXLocal, _areaFunction);
	_upperGraph.setColor(Color.red);

	_fLabel = new Label(this);
	_fLabel.setImage("36pt/FLabel.gif");
	_fLabel.setDisplayImage(true);
	_fLabel.setPosition(145, 295);
	_fLabel.setDraggable(true);

	_aLabel = new Label(this);
	_aLabel.setImage("36pt/ALabel.gif");
	_aLabel.setDisplayImage(true);
	_aLabel.setPosition(145, 113);
	_aLabel.setDraggable(true);

	_curveFormulaLabel = new Label(this);
	_curveFormulaLabel.setImage("24pt/CurveFormulaLabel.gif");
	_curveFormulaLabel.setDisplayImage(true);
	_curveFormulaLabel.setDraggable(true);
	_curveFormulaLabel.setPosition(411, 287);

	_areaFormulaLabel = new Label(this);
	_areaFormulaLabel.setImage("24pt/AreaFormulaLabel.gif");
	_areaFormulaLabel.setDisplayImage(true);
	_areaFormulaLabel.setDraggable(true);
	_areaFormulaLabel.setPosition(454, 148);

	_threeXSquaredLabel = new Label(this);
	_threeXSquaredLabel.setImage("32pt/ThreeXSquaredLabel.gif");
	_threeXSquaredLabel.setDisplayImage(true);
	_threeXSquaredLabel.setDraggable(true);
	_threeXSquaredLabel.setPosition(638, 271);

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

	_twoLabel = new Label(this);
	_twoLabel.setImage("32pt/TwoLabel.gif");
	_twoLabel.setDisplayImage(true);
	_twoLabel.setDraggable(true);

	_componentList.add(0, _lowerAxes);
	_componentList.add(0, _upperAxes);
	_componentList.add(0, _lowerPoint);
	_componentList.add(0, _upperPoint);
	_componentList.add(0, _lowerGraph);
	_componentList.add(0, _upperGraph);
	_componentList.add(0, _fLabel);
	_componentList.add(0, _aLabel);
	_componentList.add(0, _curveFormulaLabel);
	_componentList.add(0, _areaFormulaLabel);
	_componentList.add(0, _threeXSquaredLabel);
	_componentList.add(0, _xLabel);
	_componentList.add(0, _twoLabel);
	_componentList.add(0, _threeLabel);

	setLabelsOverThreeXSquaredLabel();
	_xCubedLabelsDragGroupId = createDragGroup();

	setSyncParams();
	syncComponents();
    }

    /**
     * Modifies scene description and visibility via switch Last scene calls
     * MagicApplet.advancePlayer
     */
    @Override
    protected void setScene(int scene) {
	super.setScene(scene);
	String sceneDescrip = "no scene";
	switch (scene) {
	case 0:
	    sceneDescrip = "Start";
	    for (Component comp : _componentList)
		comp.setVisible(false);
	    _lowerAxes.setVisible(true);
	    _upperAxes.setVisible(true);
	    _lowerPoint.setVisible(true);
	    _upperPoint.setVisible(true);
	    _lowerGraph.setVisible(true);
	    _upperGraph.setVisible(true);
	    _fLabel.setVisible(true);
	    _aLabel.setVisible(true);
	    break;
	case 1:
	    if (_sceneAdvancing)
		((MagicApplet) getTopLevelAncestor()).advancePanel();
	    else
		((MagicApplet) getTopLevelAncestor()).reversePanel();
	    sceneDescrip = "You shouldn't be seeing this!";
	    break;
	case 2:
	    sceneDescrip = "Before Movable Formulas";
	    _curveFormulaLabel.setVisible(false);
	    _threeXSquaredLabel.setVisible(false);
	    _xLabel.setVisible(false);
	    _twoLabel.setVisible(false);
	    _threeLabel.setVisible(false);
	    break;
	case 3:
	    setLabelsOverThreeXSquaredLabel();
	    syncThreeLabel();
	    _curveFormulaLabel.setVisible(true);
	    _threeXSquaredLabel.setVisible(true);
	    _xLabel.setVisible(true);
	    _twoLabel.setVisible(true);
	    _threeLabel.setVisible(true);
	    sceneDescrip = "Movable Formulas Grouped";
	    setXCubedLabelsGrouped(true);
	    break;
	case 4:
	    setXCubedLabelsGrouped(false);
	    sceneDescrip = "Formulas Ungrouped";
	    _areaFormulaLabel.setVisible(false);
	    break;
	case 5:
	    _areaFormulaLabel.setVisible(true);
	    setXCubedLabelsGrouped(true);
	    sceneDescrip = "Area Label and Regrouped";
	    syncComponents();
	    break;
	case 6:
	    sceneDescrip = "You shouldn't be seeing this!";
	    ((MagicApplet) getTopLevelAncestor()).advancePanel();
	    break;
	}
	setSceneString(sceneDescrip);
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
	// _lowerPoint
	_lowerPoint.setCenter(_xValuePanel, _lowerAxes.getOrigin().y);

	// _upperPoint
	// on the upper x axis in Panel coords
	DPoint point = new DPoint(_xValuePanel, _upperAxes.getOrigin().y);
	_upperAxes.transformPanelToLocal(point);
	point.y = _areaFunction.getYofX(point.x);// on curve in local coords
	_upperAxes.transformLocalToPanel(point);
	_upperPoint.setCenter(point);

	// others
	_upperGraph.setDrawingInterval(_upperAxes.getOrigin().x, _xValuePanel);
	_lowerAxes.setFillUnderCurve(_lowerGraph, _lowerAxes.getOrigin().x,
		_xValuePanel);
	_lowerAxes.setFillUnderCurveVisible(_xValuePanel != _lowerAxes
		.getOrigin().x);
	syncThreeLabel();
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
    private static final int NUM_INTERVALS = 100;

    private Axes _lowerAxes = null;
    private Axes _upperAxes = null;
    private Circle _lowerPoint = null;
    private Circle _upperPoint = null;
    private PolyLine _lowerGraph = null;
    private PolyLine _upperGraph = null;
    private Label _fLabel = null;
    private Label _aLabel = null;

    Function _curveFunction = new Function.SinXOverX();
    Function _areaFunction = new Function.IntegralOfSinXOverX();

    private Label _curveFormulaLabel = null;
    private Label _areaFormulaLabel = null;
    private Label _threeXSquaredLabel = null;
    private Label _xLabel = null;
    private Label _twoLabel = null;
    private Label _threeLabel = null;

    private boolean _groupXCubedLabels = true;
    private int _xCubedLabelsDragGroupId = -1;

    // Sync Param
    private double _xValuePanel = 0;// in Panel coordinates

}
// ---------------------------------------
// ------------------------------------------------
