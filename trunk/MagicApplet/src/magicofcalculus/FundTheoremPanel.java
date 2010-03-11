//
// AreaFunctionPanel.java
//
package magicofcalculus;

import java.awt.Color;

/**
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
public class FundTheoremPanel extends Panel {

    public FundTheoremPanel() {

	super();
	setNumScenes(8);
	
	DPoint lowerOrigin = new DPoint(50,450);
	DPoint upperOrigin = new DPoint(50,225);
	int lengthOfAxisXInPanel= 300;
	int lengthOfAxisYInPanel= 200;
	int lengthOfAxisXLocal= 15;//20::1
	int lengthOfAxisYLocal= 10;

	_lowerAxes = new Axes(this);
	_lowerAxes.setAxesInPanel(lowerOrigin,lengthOfAxisXInPanel,lengthOfAxisYInPanel);
	_lowerAxes.setAxesLocal(lengthOfAxisXLocal,lengthOfAxisYLocal);
	final double axesStrokeWidth = 1.0;
	_lowerAxes.setStrokeWidth(axesStrokeWidth);
	
	_upperAxes = new Axes(this);
	_upperAxes.setAxesInPanel(upperOrigin,lengthOfAxisXInPanel,lengthOfAxisYInPanel);
	_upperAxes.setAxesLocal(lengthOfAxisXLocal,lengthOfAxisYLocal);
	_upperAxes.setStrokeWidth(axesStrokeWidth);

	setNextButtonArea(lowerOrigin.x,lowerOrigin.y+20,lengthOfAxisXInPanel,Panel.PANEL_HIEGHT-lowerOrigin.y-20);
	setNextButtonArea(50,PANEL_HIEGHT-30,PANEL_WIDTH-50,30);//default bottom 50 pixels 
	
	_curveFunction.setCenter(2,17);
	_curveFunction.setRadius(10);
	_lowerGraph = _lowerAxes.getPolyLineFromFunction(NUM_INTERVALS,3,9,_curveFunction);
	final int gray = 0x77;
	_lowerGraph.setColor(new Color(gray,gray,gray));
	_lowerGraph.setStrokeWidth(15.0);

	_curveFunction.setRadius(15);
	_curveFunction.setCenter(-6,13);
	_upperGraph = _upperAxes.getPolyLineFromFunction(NUM_INTERVALS,3.5,8.7,_curveFunction);
	_upperGraph.setColor(new Color(0xdd,0x11,0x44));//light brown 
	_upperGraph.setStrokeWidth(30.0);

	final double leftEndpointPanel = 166; 
	final double rightEndpointPanel = 200; 
	final double topOfRectanglePanel = 285; 
	final double bottomOfTrianglePanel = 125; 
	final double topOfTrianglePanel = 55; 
	
	_bgRectangle = new RectComponent(this);
	_bgRectangle.setRectComponent(new DPoint(leftEndpointPanel,topOfRectanglePanel),new DPoint(rightEndpointPanel,lowerOrigin.y));
	_bgRectangle.setColor(MagicApplet.LIGHT_GREEN);
	
	_fillRectangle = new RectComponent(this);
	_fillRectangle.setRectComponent(new DPoint(leftEndpointPanel,topOfRectanglePanel),new DPoint(leftEndpointPanel,lowerOrigin.y));
	_fillRectangle.setColor(Color.blue);
	
	_dAdxTriangle = new SecantTriangle(this);
	_dAdxTriangle.setTriangle(new DPoint(leftEndpointPanel,bottomOfTrianglePanel),new DPoint(rightEndpointPanel,topOfTrianglePanel));
	_dAdxTriangle.setColor(MagicApplet.LIGHT_GREEN);
		
	_lowerPoint = new Circle(this);
	_lowerPoint.setCenter(leftEndpointPanel,lowerOrigin.y);
	_lowerPoint.setColor(Color.blue);
	_lowerPoint.setDraggable(true);
	
	_upperPoint = new Circle(this);
	_upperPoint.setCenter(leftEndpointPanel,bottomOfTrianglePanel);
	_upperPoint.setColor(Color.red);
	
	_fLabel = new Label(this);
	_fLabel.setImage("24pt/FLabel.gif");
	_fLabel.setDisplayImage(true);
	_fLabel.setPosition(50,262);
	_fLabel.setDraggable(true);

	_aLabel = new Label(this);
	_aLabel.setImage("24pt/ALabel.gif");
	_aLabel.setDisplayImage(true);
	_aLabel.setPosition(50,140);
	_aLabel.setDraggable(true);

	_dxDimensionLabel = new Label(this);
	_dxDimensionLabel.setImage("32pt/DxDimension left.gif");
	_dxDimensionLabel.setDisplayImage(true);
	_dxDimensionLabel.setPosition(74,373);
	_dxDimensionLabel.setDraggable(true);

	_bigFLabel = new Label(this);
	_bigFLabel.setImage("32pt/FLabel.gif");
	_bigFLabel.setDisplayImage(true);
	_bigFLabel.setPosition(274,331);
	_bigFLabel.setDraggable(true);

	_daLabel = new Label(this);
	_daLabel.setImage("32pt/DaLabel.gif");
	_daLabel.setDisplayImage(true);
	_daLabel.setPosition(202,66);
	_daLabel.setDraggable(true);

	_dxLabel = new Label(this);
	_dxLabel.setImage("32pt/DxLabel.gif");
	_dxLabel.setDisplayImage(true);
	_dxLabel.setPosition(158,130);
	_dxLabel.setDraggable(true);

	_verticalDimensionLabel = new Label(this);
	_verticalDimensionLabel.setImage("32pt/VerticalDimension.gif");
	_verticalDimensionLabel.setDisplayImage(true);
	_verticalDimensionLabel.setPosition(235,288);
	_verticalDimensionLabel.setDraggable(true);

	_lowerAreaEqLabel = new Label(this);
	_lowerAreaEqLabel.setImage("28pt/Lower Area Eq.gif");
	_lowerAreaEqLabel.setDisplayImage(true);
	_lowerAreaEqLabel.setPosition(352,252);
	_lowerAreaEqLabel.setDraggable(true);

	_upperAreaEqLabel = new Label(this);
	_upperAreaEqLabel.setImage("28pt/Upper Area Eq.gif");
	_upperAreaEqLabel.setDisplayImage(true);
	_upperAreaEqLabel.setPosition(307,67);
	_upperAreaEqLabel.setDraggable(true);

	_slopeEqLabel = new Label(this);
	_slopeEqLabel.setImage("24pt/Slope Eq.gif");
	_slopeEqLabel.setDisplayImage(true);
	_slopeEqLabel.setPosition(25,20);
	_slopeEqLabel.setDraggable(true);

	_mainEqLabel = new Label(this);
	_mainEqLabel.setImage("32pt/Main Eq.gif");
	_mainEqLabel.setDisplayImage(true);
	_mainEqLabel.setPosition(415,147);
	_mainEqLabel.setDraggable(true);

	_mainEqShortLabel = new Label(this);
	_mainEqShortLabel.setImage("32pt/Main Eq Short.gif");
	_mainEqShortLabel.setDisplayImage(true);
//	_mainEqShortLabel.setPosition(415,147);
	_mainEqShortLabel.setDraggable(true);

	_mainEqDxLabel = new Label(this);
	_mainEqDxLabel.setImage("32pt/DxLabel.gif");
	_mainEqDxLabel.setDisplayImage(true);
//	_mainEqDxLabel.setPosition(608,147);
	_mainEqDxLabel.setDraggable(true);

	_fundamentalEqLabel = new Label(this);
	_fundamentalEqLabel.setImage("32pt/Fundamental Eq.gif");
	_fundamentalEqLabel.setDisplayImage(true);
//	_fundamentalEqLabel.setPosition(411,145);
//	_fundamentalEqLabel.setPosition(408,117);
	_fundamentalEqLabel.setDraggable(true);

	_componentList.add(0,_lowerAxes);
	_componentList.add(0,_upperAxes);
	_componentList.add(0,_lowerGraph);
	_componentList.add(0,_upperGraph);
	_componentList.add(0,_bgRectangle);
	_componentList.add(0,_fillRectangle);
	_componentList.add(0,_dAdxTriangle);
	_componentList.add(0,_lowerPoint);
	_componentList.add(0,_upperPoint);
	_componentList.add(0,_fLabel);
	_componentList.add(0,_aLabel);
	_componentList.add(0,_dxDimensionLabel);
	_componentList.add(0,_bigFLabel);
	_componentList.add(0,_daLabel);
	_componentList.add(0,_dxLabel);
	_componentList.add(0,_verticalDimensionLabel);
	_componentList.add(0,_lowerAreaEqLabel);
	_componentList.add(0,_upperAreaEqLabel);
	_componentList.add(0,_slopeEqLabel);
	_componentList.add(0,_mainEqLabel);
	_componentList.add(0,_mainEqShortLabel);
	_componentList.add(0,_mainEqDxLabel);
	_componentList.add(0,_fundamentalEqLabel);

	setLabelsOverMainEqLable();
	_mainEqLabelsDragGroupId = createDragGroup();
	addToDragGroup(_mainEqLabelsDragGroupId,_mainEqShortLabel);
	addToDragGroup(_mainEqLabelsDragGroupId,_mainEqDxLabel);
	addToDragGroup(_mainEqLabelsDragGroupId,_fundamentalEqLabel);
	
	setSyncParams();
	syncComponents();
    }

    //From Panel
    protected void setScene(int scene) {
	super.setScene(scene);
	String sceneDescrip="no scene";
	switch (scene) {
	    case 0:
		for (Component comp : _componentList) comp.setVisible(false);
		_lowerAxes.setVisible(true);
		_upperAxes.setVisible(true);
		_lowerGraph.setVisible(true);
		_upperGraph.setVisible(true);
		_bgRectangle.setVisible(true);
//		_fillRectangle.setVisible(true);
		_dAdxTriangle.setVisible(true);
		_upperPoint.setVisible(true);
		_lowerPoint.setVisible(true);
		syncComponents();
		_fLabel.setVisible(true);
		_aLabel.setVisible(true);
		_dxDimensionLabel.setVisible(true);
		_bigFLabel.setVisible(true);
		_daLabel.setVisible(true);
		_dxLabel.setVisible(true);
		_verticalDimensionLabel.setVisible(true);
		sceneDescrip = "Start";
		_lowerAreaEqLabel.setVisible(false);
		break;
	    case 1: 
		_lowerAreaEqLabel.setVisible(true);
		sceneDescrip = "Lower Equation";
		_slopeEqLabel.setVisible(false);
		break;
	    case 2:
		_slopeEqLabel.setVisible(true);
		sceneDescrip = "Slope Equation";
		_upperAreaEqLabel.setVisible(false);
		break;
	    case 3:
		_upperAreaEqLabel.setVisible(true);
		sceneDescrip = "Upper Equation";
		_mainEqLabel.setVisible(false);
		_mainEqShortLabel.setVisible(false);
		_mainEqDxLabel.setVisible(false);
		_fundamentalEqLabel.setVisible(false);
		break;
	    case MAIN_EQUATION_SCENE://4
		setLabelsOverMainEqLable();
		syncMainEqLabels();
		_mainEqLabel.setVisible(true);
//		_mainEqShortLabel.setVisible(true);
		_mainEqDxLabel.setVisible(true);
//		_fundamentalEqLabel.setVisible(false);
//		_fundamentalEqLabel.setVisible(true);//dev
		sceneDescrip = "Main Equation Grouped";
//		setMainEqLabelsGrouped(true);
		addToDragGroup(_mainEqLabelsDragGroupId,_mainEqDxLabel);
		break;
	    case 5:
//		setMainEqLabelsGrouped(false);
		removeFromDragGroup(_mainEqLabelsDragGroupId,_mainEqDxLabel);
		_componentList.bringToTopOfZOrder(_mainEqDxLabel);
		sceneDescrip = "Equation Ungrouped";
		break;
	    case 6:
//		setMainEqLabelsGrouped(true);
		addToDragGroup(_mainEqLabelsDragGroupId,_mainEqDxLabel);
		sceneDescrip = "Equation Regrouped";
		syncComponents();
		break;
	    case 7: 
		sceneDescrip = "You shouldn't be seeing this!";
		((MagicApplet)getTopLevelAncestor()).advancePanel();
		break;
	}
	setSceneString(sceneDescrip);
    }
    protected void setSyncParams() {
	//just get the value from the _lowerPoint
	_xValuePanel = _lowerPoint.getCenter().x;
	//make sure _xValuePanel is within limits
	if (_xValuePanel<_bgRectangle.getUpperLeft().x) _xValuePanel = _bgRectangle.getUpperLeft().x;
	else if (_xValuePanel>_bgRectangle.getLowerRight().x) _xValuePanel = _bgRectangle.getLowerRight().x;
    }
    protected void syncComponents() {

	//_lowerPoint
	_lowerPoint.setCenter(_xValuePanel,_lowerAxes.getOrigin().y);

	//_upperPoint
	double triangleHeight = (_dAdxTriangle.getTangentPoint().y-_dAdxTriangle.getSecantPoint().y);
	double pctCovered = (_xValuePanel-_bgRectangle.getUpperLeft().x)/(_bgRectangle.getLowerRight().x-_bgRectangle.getUpperLeft().x);
	double upperPointY = _dAdxTriangle.getTangentPoint().y - pctCovered*triangleHeight;
	_upperPoint.setCenter(_xValuePanel,upperPointY);

	//fill rect
	if (_xValuePanel==_bgRectangle.getUpperLeft().x) {
	    _fillRectangle.setVisible(false);
	}
	else {
	    _fillRectangle.setVisible(true);
	    _fillRectangle.setLowerRight(new DPoint(_xValuePanel,_lowerAxes.getOrigin().y));
	}
	
	syncMainEqLabels();
    }
    
//---------------------------------------

    //Movable Algebra
    private void setMainEqLabelsGrouped(boolean set) {
// removeFromDragGroup(int groupId, Component comp)
	_groupMainEqLabels = set;
	if (_groupMainEqLabels) {
	    addToDragGroup(_mainEqLabelsDragGroupId,_mainEqShortLabel);
	    addToDragGroup(_mainEqLabelsDragGroupId,_mainEqDxLabel);
	    addToDragGroup(_mainEqLabelsDragGroupId,_fundamentalEqLabel);
	}
	else clearDragGroup(_mainEqLabelsDragGroupId);

    }
    private void syncMainEqLabels() {
	if (_mainEqDxLabel.getPosition().x<_mainEqShortLabel.getPosition().x+50) {
	    _mainEqShortLabel.setVisible(false);
	    _fundamentalEqLabel.setVisible(getScene()>=MAIN_EQUATION_SCENE);
	}
	else {
	    _mainEqShortLabel.setVisible(getScene()>=MAIN_EQUATION_SCENE);
	    _fundamentalEqLabel.setVisible(false);
	}
    }
    private void setLabelsOverMainEqLable() {
	DPoint pos = _mainEqLabel.getPosition();//415,147
	_mainEqShortLabel.setPosition(pos.getTranslation(0,0));//415,147
	_mainEqDxLabel.setPosition(pos.getTranslation(193,0));//608,147
	_fundamentalEqLabel.setPosition(pos.getTranslation(-7,-30));//408,117
	_componentList.bringToTopOfZOrder(_mainEqShortLabel);
	_componentList.bringToTopOfZOrder(_mainEqDxLabel);
    }
    
//---------------------------------------

//---------------------------------------

    private static final int NUM_INTERVALS=100;
    private static final int MAIN_EQUATION_SCENE=4;

    private Axes _lowerAxes = null;
    private Axes _upperAxes = null;

    private PolyLine _lowerGraph = null;
    private PolyLine _upperGraph = null;

    private RectComponent _bgRectangle = null;
    private RectComponent _fillRectangle = null;
    private SecantTriangle _dAdxTriangle = null;
    
    private Circle _lowerPoint = null;
    private Circle _upperPoint = null;

    Function.LowerSemiCircle _curveFunction = new Function.LowerSemiCircle();
	    
    private Label _fLabel = null;
    private Label _aLabel = null;
    private Label _dxDimensionLabel = null;
    private Label _bigFLabel = null;
    private Label _daLabel = null;
    private Label _dxLabel = null;
    private Label _verticalDimensionLabel = null;
    private Label _lowerAreaEqLabel = null;
    private Label _upperAreaEqLabel = null;
    private Label _slopeEqLabel = null;
    private Label _mainEqLabel = null;
    private Label _mainEqShortLabel = null;
    private Label _mainEqDxLabel = null;
    private Label _fundamentalEqLabel = null;

    private boolean _groupMainEqLabels=true;
    private int _mainEqLabelsDragGroupId=-1;
	    
    //Sync Param
    private double _xValuePanel=0;//in Panel coordinates
    
}
//---------------------------------------
//------------------------------------------------
