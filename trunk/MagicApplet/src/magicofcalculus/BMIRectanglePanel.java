//
// BMIRectanglePanel.java
//
package magicofcalculus;

import java.awt.Color;

public class BMIRectanglePanel extends Panel {

    public BMIRectanglePanel() {

	super();
//	setNumScenes(3);
	setNumScenes(2);
	
	DPoint origin = new DPoint(0,450);
	int lengthOfAxisX= 450;
	int lengthOfAxisY= 800;
	int lengthOfAxisXLocal= 10;
	int lengthOfAxisYLocal= 800/45;
	_leftXLocal = 0;
	_rightXLocal = 10;
		
	_axes = new Axes(this);
	_axes.setAxesInPanel(origin,lengthOfAxisX,lengthOfAxisY);
	_axes.setAxesLocal(lengthOfAxisXLocal,lengthOfAxisYLocal);
	_axes.setRiemannRectsVisible(true);
//	_axes.setRiemannRightEndPoints(true);
	_axes.setRiemannRightEndPoints(false);
	_axes.setVisible(true);
	_axes.setRiemannRectsColor(Color.blue);
//	_axes.setRiemannRectsStrokeWidth(55.0/2);
	_axes.setRiemannRectsStrokeWidth(1);

	setNextButtonArea(origin.x+50,origin.y+20,lengthOfAxisX,Panel.PANEL_HIEGHT-origin.y-20);

	_leftXPanel = _axes.transformLocalToPanel(new DPoint(_leftXLocal,0)).x;
	_rightXPanel = _axes.transformLocalToPanel(new DPoint(_rightXLocal,0)).x;

	_leftStopXPanel = 55;
	double startXPanel = 193;
	double h = 1;
	double slope = .9;
	_linearFunction.setLinearFunction(new DPoint(0,h),slope);
	_lowerGraph = _axes.getPolyLineFromFunction(NUM_INTERVALS,0,10,_linearFunction);
	_linearFunction.setLinearFunction(new DPoint(0,h+1.2),slope);
//	_linearFunction.setLinearFunction(new DPoint(0,h+slope*_leftStopXPanel/45),slope);
	_upperGraph = _axes.getPolyLineFromFunction(NUM_INTERVALS,0,10,_linearFunction);

	_lowerGraph.setVisible(true);
	_upperGraph.setVisible(true);
	_lowerGraph.setColor(Color.red);
	_upperGraph.setColor(Color.red);
	_lowerGraph.setStrokeWidth(1);
	_upperGraph.setStrokeWidth(1);
	
	_axes.setFillUnderCurveColor(Color.red);
	_axes.setFillBetweenCurves(_upperGraph,_lowerGraph,0,Panel.PANEL_WIDTH);
	_axes.setFillUnderCurveVisible(true);

	_leftPoint = new Circle(this);
	_leftPoint.setCenter(_leftXPanel,origin.y);
	_leftPoint.setColor(Color.red);
//	_leftPoint.setVisible(true);
	
	_rightPoint = new Circle(this);
//	_rightPoint.setCenter(_leftXPanel+.25*(_rightXPanel-_leftXPanel),origin.y);
	_rightPoint.setCenter(startXPanel,origin.y);
	_rightPoint.setColor(Color.blue);
	_rightPoint.setDraggable(true);
	_rightPoint.setVisible(true);
	_rightPoint.setDiameter(2);
//	_rightPoint.setHitMargin(25);

	_dxLabel = new Label(this);
	_dxLabel.setImage("36pt/DxDimension outside.gif");
	_dxLabel.setDisplayImage(true);
	_dxLabel.setPosition(173,320);
	_dxLabel.setDraggable(true);

	_componentList.add(0,_lowerGraph);
	_componentList.add(0,_upperGraph);
	_componentList.add(0,_axes);
	_componentList.add(0,_leftPoint);
	_componentList.add(0,_rightPoint);
	_componentList.add(0,_dxLabel);

//	setScene(0);
	setSyncParams();
	syncComponents();
    }    
    
    //From Panel
    protected void setScene(int scene) {
	super.setScene(scene);
	String sceneDescrip="no scene";
	switch (scene) {
	    case 0:
		sceneDescrip = "Start";
		break;
	    case 1: 
		sceneDescrip = "You shouldn't be seeing this!";
		((MagicApplet)getTopLevelAncestor()).advancePanel();
		break;
	}
	setSceneString(sceneDescrip);
	
    }
    protected void setSyncParams() {
	if (_componentList.get(0)==_rightPoint) {_xValuePanel = _rightPoint.getCenter().x;}
//	if (_xValuePanel<_leftXPanel) _xValuePanel=_leftXPanel;
	if (_xValuePanel<_leftStopXPanel) _xValuePanel=_leftStopXPanel;
	if (_xValuePanel>_rightXPanel) _xValuePanel=_rightXPanel;
    }
    protected void syncComponents() {
    	for (int i=0; i<_componentList.size(); i++) {

	    if (_componentList.get(i)==_rightPoint) {
		_rightPoint.setCenter(_xValuePanel,_axes.getOrigin().y);
	    }

	    else if (_componentList.get(i)==_axes) {
		double xValueLocal = _axes.transformPanelToLocal(new DPoint(_xValuePanel,_axes.getOrigin().y)).x;
		double deltaXLocal = (xValueLocal-_leftXLocal);
		_axes.setRiemannRects(_leftXLocal,_rightXLocal,deltaXLocal,_linearFunction);
		if (_xValuePanel==_leftStopXPanel) {
		    _axes.setRiemannRectsColor(MagicApplet.GREEN);
		    _dxLabel.setVisible(true);
		}
		else {
		    _axes.setRiemannRectsColor(Color.blue);
		    _dxLabel.setVisible(false);
		}
	    }
	}
    }
    
//---------------------------------------

    private static final int NUM_INTERVALS=100;
    
    private Axes _axes = null;
    private PolyLine _lowerGraph = null;
    private PolyLine _upperGraph = null;
    private Circle _leftPoint = null;
    private Circle _rightPoint = null;
    private Label _dxLabel = null;
    
    Function.LinearFunction _linearFunction = new Function.LinearFunction();

    //Sync Param
    private double _xValuePanel=193;//in Panel coordinates

    private double _leftXLocal = 0;
    private double _rightXLocal = 0;
    private double _leftXPanel = 0;
    private double _rightXPanel = 0;
    private double _leftStopXPanel = 0;

}
//---------------------------------------
//------------------------------------------------
