//
// Version.java
//
package magicofcalculus;
public class Version {
    public static final String verString = "32";
}
//------------------------------------------------
/*
 - RiemannSumsPanel
     - put a f(x) height dimension line 
     - make movable algebra, with integral signs
     - make bigger, get it looking right too
     - shade area under the cuvre with color, use Axes.setFillUnderCurve()
 - AreaFunctionPanel
     - make it longer and with a more "instructive" shape.
     - prep it for FundTheoremPanel with a little triangle and rectangle
	 - "What if we put my triangle here, and your rectangle there..."
	 - "and what if we magnify it?".... go on to FundTheoremPanel
- SecantApproximationPanel
     - fix the movable 3 so Lynne won't grab it
     - always paint tangent point last
     - implement snap to grid for movable algebra 

 - FundTheoremPanel
 - MagicApplet
     - isn't there something about re-doing the AdvancePanel() stuff ?
     - make an opening panel with the title and lots of equations 
     - make bg color a applet tag paramter, have to pass thru constructors?
 - Panel
     - make a drag and draw switch for when no Component is being dragged, can draw circle or rubber band a square
     - try using the clip rectangle, at least in drawVersionString()
 - BMIRectangelPanel
     - make the rects outlined like in BMITriangelPanel
     - enlarge hit margin on point
     - put two or three more points at adjacent endpoints
     - drag from anywhere, get left end non moving rect off the screen
     - make movable algebra
     - use a curve like in BMITriangelPanel
 - BMITriangelPanel
 - Axes
     - add more choices re riemann rects
     - add extra logic to setRiemannRects() to handle more than just monotonic increasing functions
 - Component
     - make setHitMargin() and setDragZone()
 - consider a Scribble Component
     - have the upper right area in the SecantApproximationPanel activated to create Scribble's
     - boolean Panel._draggingToCreatePolyline = false;
     - Panel.startDragToCreatePolyline();
	 _draggingToCreatePolyline = true;
	 _newPolyline = new Polyline();
     - Panel.dragToCreatePolyline();
	 _newPolyline.addToEnd(mousePt);
	 repaint(_newPolyline.getBounds());
	 repaint(new Rectangle(that contains last two points in _newPolyline);
	     Rectangle rect = new Rectangle(_newPolyline.getPoint(_newPolyline.getNumPoints()-2));
	     rect.add(_newPolyline.getPoint(_newPolyline.getNumPoints()-1));
     - Panel.stopDragToCreatePolyline();
	 _draggingToCreatePolyline = false;
 	_componentList.add(0,_newPolyline);//at this point it's lost into the _componentList
 - PolyLine
     - add(DPoint pt), remove(index)
     - getPoint(index), setPoint(index)
     - getNumPoints()
 - Function
     - outfit with an AffineTransform in and out
	 - _function.setInputAT(_axes.getATFromPanelToLocal());
	 - _function.setOutputAT(_axes.getATFromLocalToPanel());
 - SecantTriangle
     - has repaint problems when taken beyond the curve (when thicker line?) (the point of the verticie is beyond?)
 - Line 
     - get draw() to handle _infiniteLength
     - make getBezierPolyCoeffsY() for Line, and use it in getPointNearestTo()
 - QuadCurve
     - get draw() to handle _infiniteLength
 - Label
     - work with text size to center in rect
     - use static int computeStringWidth(FontMetrics fm, String str) 
       Compute the width of the string using a font with the specified "metrics" (sizes).
       to get string lengths, use g.getFontMetrics()
 - BezierCurve
     - replace the return int numOf...'s in place of returning the array,
       then the client can use array.length()
 - TriangleShape
     - implement using java.awt.Path2D
 

------------
 - Dev Env
     - Find the Macro language, why is it so hard to find
     - macros for file run and file debug
     - look online for this stuff
     - Setting Code Completion Keyboard Shortcuts
     - Ctrl-L needs a macro
     - Ctrl-K needs to be including the neewline
     - can't find setting for Call Stack Line
     - make a ctrl-o OpenLine macro
 - Future improvements
     - use clip for drawing, esp with Line and Curve
     - use clips in repaint() ?
     - Panel button area
 	 - use Swing buttons
	 - make a "back" button area, would need to implement a "back"

-------------------------------------------

                Versions

----- version 32 -----

- Changed name from state to scene

----- version 31 -----

 - SecantApproximationPanel
     - when points on top of each other, keep tangent line but swinging free
 
 ----- version 30 -----
  - AreaFunctionPanel
     - added scene before movable formulas appear
 - Panel
     - modified addToDragGroup()
 - FundTheoremPanel
     - completed first version

 ----- version 29 -----
 - AreaFunctionPanel
     - removed unecessary component loop in syncComponents()
 - FundTheoremPanel
     - further developed
 
 ----- version 28 -----
 - developed FundTheoremPanel

 ----- version 27 -----
 - Panel
     - re-wrote bottom of screen messages in Panel.setStateString()
 - MagicApplet
     - removed blank cursor code
 - SecantApproximationPanel
     - when points on top of each other, keep tangent line but swinging free
     - show a little dy/dx triangle in Scene 13 
 - RiemannSumsPanel
     - removed RiemannRectangles code
     - added "dx" line segment 
 - AreaFunctionPanel
     - fixed movable algebra
     - removed old code replaced by drag groups
 - FundTheoremPanel
     - installed a rough version  
 
 ----- version 26 -----
 - changed name to MagicApplet

 ----- version 25 -----
 - This version used in Spring 08 presentations 
 - AreaFunctionPanel
     - put in reverse movable algebra
 - BMIRectangelPanel
     - added dx label

 ----- version 24 -----
 - Panel
     - put super.setState(getNumStates()-1) into setNumStates()
     - support multiple _groupedDragList's
     - try using the clip rectangle, at least in drawVersionString()
 
 ----- version 23 -----
 - BMITriangelPanel
     - make labels

 ----- version 22 -----
 - MagicProto
     - redid advancePanel() and advanceState() to allow for jumping from one panel to another and then back again
 - SecantApproximationPanel
     - added extra state at beginning start with blank screen
 - BMIRectangelPanel
     - created 
 - PolyLine
     - fixed bug in getPointList()
 
 ----- version 21 -----
 - BMITriangelPanel
     - created 
 - make a DragMaster interface
 - make html background color black
 - Bigger still
 - SecantApproximationPanel
     - reset positions of the movable algebra at state 8
     - develop movable formulas, positions and what appears in what states
 - Panel
     - change cursor
 - Label
     - use a double for the java.awt.geom Class Rectangle2D.Double
 
 ----- version 20 -----
 - Label
     - test handle error in loading image
 - SecantApproximationPanel
     - make movable algebra
     - make delta x, delta y, and slopeFormula labels
 - MagicProto
     - Make everything bigger
     - Make points easy to hit, esp. Lynne's secantPoint

 ----- version 19 -----
 - SecantApproximationPanel
     - just make it stop at the tangent point
 - MagicProto
     - Implemented Back button routines
     - made advancePanel() circular
     - made getAudioClip() 
 - Axes
     - setRiemannRects()
 - RiemannSumsPanel
     - put in a Function and a Polyline
     - use Axes.setRiemannRects()
     - add points
     - get it moving

 ----- version 18 -----
  - Axes
     - getPolyLineFromFunction()
     - fillUnderCurve()
 - PolyLine 
     - derive from Component 
     - public void ArrayList<DPoint> getPointArray(double fromX, double toX) {}
     - in setDrawingInterval() do some kind of interpolation to draw end of line more accuately
 - MagicProto
     - develop advancePanel() 
 - Function 
     - create class to house Function's and provide a way to pass a Function into a method
     - can do arrays
 - RiemannSumsPanel
     - made skeletal version
 - AreaFunctionPanel
     - retrofit with Function's and Polyline's
     - bug: edge of filled are can have one or two jags sometimes
	 - is there a misalignment between the last _lowerIntPoints on the curve and the point added on the x axis ?
	 - fixed it by using (int)Math.round() instead of just (int)

 ----- version 17 -----
 - Axes
     - make AffineTransforms in the Axes
 - AreaFunctionPanel
     - made first version
 - Panel
     - rename the panels
	 - SecantApproximationPanel
	 - AreaFunctionPanel
 ----- version 16 -----
 - Panel
     - make superclass called Panel, we'll change the names Panel_01 and Panel_02 soon
     - then derive Panel_01 from it
 - Panel_02
     - create it and jump to it with click in the buttonArea
 ----- version 15 -----
 - BezierCurve 
     - make a BezierCurve object and put Line and QuadCurve under that
     - made getParamValues...() return only values between 0 and 1 when not 
       infinite length
 - Panel_01
     - make it so that a _tangentLine drag is stopped like 
       the triangle and secantPoint are now
	 - do it through curve functions that limit themselves with finite length
     - Fix up a button area, 
	 - because a button won't care about a little dragging, 
	     - you can drag on or off
	     - as long as you are in the area when you release, you'll activate the button
 - Axes
     - replace the Line2D.Double's with Line's
	 - takes care of Axes.isHitBy()
 ----- version 14 -----
 - Panel_01
     - annotate states in button area
     - further develop the sync system now that we have the triangle
	 - make separte sunctions within setSyncParams() and syncComponents()
	 - come back again on the top Z Component in syncComponents() 
	     - see if we can fine tune it so the _secantPoint, for example, 
	       doesn't have to be sync'd twice
     - display version number on screen
     - Have the line dissappear and change color when points coincide
 - TriangleShape
     - implement intersects(Rectangle)
 - SecantTriangle 
     - make drag x dir only by playing with setPosition() and getPosition()
     - set stroke width, put it in Component
 - Component
     - moved setStrokeWidth() and _strokeWidth to Component, four out of 6
       subclasses use it the same way
 - deleted version.txt from project
 - added build dir to backups
 ----- version 13 -----
- MagicProto
    - compile version number 
- SecantTriangle / Triangle 
    - make class Triangle implements Shape
    - make class TriangleIterator implements PathIterator
    - make class SecantTriangle, extends Component that uses a Triangle _triangle member
 - SecantTriangle 
     - make it move
 - DeletedSquare.java and others from project
 ----- version 12 -----
- Panel_01
    - debug the intersection behavior, improve Curve.getIntersection()
    - need to finish up the Sync Params w some more flags to use in mouseClicked
    - Instead of enlarging the hit zone for all points, 
      make the secant point always selectable from underneath everything else.
----- version 11 -----
- MagicProto
    - Make the html file nicer
    - Show absolute coordinates in printMouseDiagnostic()
    - Redo applet functions using printDiagnostic()
- Component
    - put Color stuff in Component
- Curve
    - make Curve.setCurve()
- Panel_01
    - do drag with secant point
	- make logic with _dragLineWithSecantPoint in Panel_01.mouseDragged() so that
	    - you can grab the line and the point will follow
	    - you can drag other things
    - put in first Sync Params
    - selection problem with _secantPoint behind the _tangentLine
	- made hit zone for Circle very large

----- version 10 -----

- made class DPoint
    - Got rid of all Point's and Point2D.Double's in the whole project
    - And it fixed the wiggly's
- Line 
    - do drag with secant point
	- way too wiggly, fixed it
	- included logic with _dragLineWithSecantPoint in Panel_01.mouseDragged()
    
- updated Square so it's a really stripped down Component
- standardized text at tops of files

----- version 09 -----

-Bugs
    - all of label not being draw at first
	- resized rect inside of draw(), moved it to setImage()
- Curve
    - getParamValueAtPoint() still doesn't work well around t=0, so
	- redo getParamValueAtPoint() yet again, this time using
	  the getNearestPoint() stuff
    - finally completed getTangentLine()
- Line 
    - got it rotating

----- version 08 -----
- Curve
    - got rid of getYFromX() and getXFromY()
    - made getParamValueAtPoint()

----- version 07 -----
- Backup
    - make config part of bakup
    - do auto backup for version system
- Bugs 
    - circle not cented on curve
	- rewrote logic in Panel_01.mousePressed()
    - dragging label from off the label
	- rewrote logic in Component.DragTo()
    - always painting, ooops
	- there was a call to repaint() from inside Label.draw()
- Label
    - do a transparent image
    - request size of image
- Curve
    - let getPointNearestTo() return the result directly
	it will always have a root since it's a cubic
- Line
    - made Line class, not debugged

----- version 06 -----
- Label
  - make draggable
  - do an image
- separate the axes from the curve
- make Circle _tangentPoint
- redo case statements in Panel_01
  - standardize the dragging system
- created bakup.bat for backups

----- version 05 -----
- get a version system going
- a little work in Panel_01.mouseDragged()
- made isSelectedBy()
- rename _circle _secantPoint
- make Label _curveLabel;
- take out Square

----- version 04 -----
- fix up mouse diag output
- put circle drag on curve in proper place
- do move Circle on Curve
  - still need to start drag in mousePressed()
- can't find setting for cursor color for text doc 
  - was a glitch, went away next session

----- version 03 -----
- Find a good list object
- download docs
- work on Circle
  - look at Shape's
  - locate from center
  - do radius
  - do color
- work on Curve
  - put in axes

----- version 02 -----
- get MouseEventDemo into MagicProto 
- Make a Circle

----- version 01 -----
- make a name space

----- version 00 -----
- vauge beginnings



------------------------
Panel_01

Axes _axes;
Curve _curve;
Label _curveLabel;
Circle _tangentPoint;
Circle _secantPoint;
RotatingLine _tangent;
Triangle _secantTriangle;

_state==0, initialized state
show empty
wait for click
"Let's say we have a curve."

_state==1, curve only
show graph with _axes and _curve
wait for click
"And let's give it a name.  The curve f."

_state==2, curve and label
show _curveLabel
wait for click
"And let's just look at one point on this curve"

_state==3, with point of tangency
show _tangentPoint;
wait for click
"The slope of the curve at this point is thought of as the slope of the 
straight line that just touches there."  

_state==4, rotating tangent
show _tangentLine fixed only at _tangentPoint
wait for dclick, manage mouse drags
"As you can see, you can eyeball it, but you can't get the tilt exactly right by hand.  
So, my idea was to use the formula of the curve to compute the slope of this tangent line.
Whatever I tried, it always came down to this."

_state==5, secant triangle
show _secantPoint, fix _tangentLine at _secantPoint and _tangentPoint,
show _secantTriangle
wait for dclick, handle mouse drags
"Since I can locate points along the curve exactly, I use another point to fix the 
line.  As I move the other point toward the  ... One can only estimate it..."

----------------------------------

mouseClick
switch (_state)
  case 2:
    _pointOfTangency.setVisible();
    break;

mouseDrag
switch (_state)
  case 0:
  case 1:
  case 2:
    break;

---------------------------
---------------------------

- derive from JComponent
  - try little things first, like paintComponent
  - make a list of methods to override
    - gotta be the hit functions and painting at least
- learn more about
  - layout methods 
  - messaging between JComponent's

=====================================================
*/