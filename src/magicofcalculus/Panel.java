//
// Panel.java
//
// Subclasses, 6 in order of appearance: 
//   SecantTrianglePanel, BMITrianglePanel, (then SecantTrianglePanel again),
///  RiemannSumsPanel (was RiemannRectsPanel), BMIRectanglePanel (then RiemannSumsPanel again),
//   AreaFunctionPanel, FundTheoremPanel (then AreaFunctionPanel again)
//
package magicofcalculus;

import james.Annotations.Visibility;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import static java.lang.Math.abs;
import java.awt.Rectangle;
import java.applet.AudioClip;
import java.awt.Cursor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Every Panel has a set of syncParams that can synchronize the components in
 * the Panel. The Panel can go thru the components and make sure they are all
 * sync'd to the params. So, 1. the topZComponent is dragged as usual 2. the
 * Panel sets the syncParams depending on the component just dragged 3. the
 * Panel sync's all the rest of the components
 * 
 * A Panel also has a _dragGroupList
 */
public class Panel extends JPanel implements MouseListener, MouseMotionListener {

    /**
     * <ol>
     * <li>Construct border</li>
     * <li>set Background</li>
     * <li>add this as mouse Listener</li>
     * <li>setxxxArea: NextButton, BackButton, Version</li>
     * <li>setCursor(Cursor.CROSSHAIR_CURSOR)</li>
     * </ol>
     */
    public Panel() {

	setBorder(BorderFactory.createLineBorder(Color.black));

	setBackgroundColor(new Color(0xdd, 0xdd, 0xdd));

	addMouseListener(this);
	addMouseMotionListener(this);

	setNextButtonArea(50, PANEL_HIEGHT - 50, PANEL_WIDTH - 50, 50);// default
	// bottom 50
	// pixels
	setBackButtonArea(0, PANEL_HIEGHT - 50, 50, 50);// default bottom left
	// 50x50
	// pixels
	setVersionLocation(10, PANEL_HIEGHT - 10);

	setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    /**
     * sets background and stores color to _bgColor
     * 
     * @param color
     */
    protected void setBackgroundColor(Color color) {
	_bgColor = color;
	setBackground(_bgColor);
    }

    /**
     * Accessor: _bgColor
     */
    protected Color getBackgroundColor() {
	return _bgColor;
    }

    /**
     * Accessor: _scene
     */
    protected int getScene() {
	return _scene;
    }

    /**
     * increases max scene <br>
     * _scene = _numScene - 1 <br>
     * 
     * @param numScenes
     *            if &lt; 0 scenes = 0;
     */
    protected void setNumScenes(int numScenes) {// also resets _scene to
	// _numScenes-1, so we'll
	// we'll either advance into scene 0 or reverse into the second to last
	// scene
	if (numScenes <= 0) {
	    _numScenes = 0;
	    return;
	} else {
	    _numScenes = numScenes;
	    _scene = _numScenes - 1;
	}

    }

    /**
     * Accessor: _numScenes
     */
    protected int getNumScenes() {
	return _numScenes;
    }

    /**
     * _sceneAdvancing = true; if (valid scene) setScene(++_scene); else
     * setScene(0)
     */
    protected void advanceScene() {
	_sceneAdvancing = true;
	if (_scene >= _numScenes - 1) {
	    setScene(0);
	    // ((MagicApplet)getTopLevelAncestor()).advancePanel();
	} else {
	    _scene++;
	    setScene(_scene);
	}
    }

    /**
     * _sceneAdvancing = true; if (valid scene) setScene(++_scene);
     */
    protected void reverseScene() {
	_sceneAdvancing = false;
	if (_scene <= 0) {
	    _scene = _numScenes - 1;// leave in last scene?
	    ((MagicApplet) getTopLevelAncestor()).reversePanel();
	} else {
	    _scene--;
	    setScene(_scene);
	}
    }

    /**
     * Empty Method. TODO OVERRIDE HUNT
     */
    protected void setSyncParams() {
	// set the sync params from the topZComponent
    }

    /**
     * Empty Method. TODO OVERRIDE HUNT
     */
    protected void syncComponents() {
    }

    /**
     * Sets the rectangle for the nextButton.
     */
    protected void setNextButtonArea(double x, double y, double w, double h) {
	_nextButtonArea.setRect(x, y, w, h);
    }

    /**
     * Sets the rectangle for the nextButton.
     */
    protected void setBackButtonArea(double x, double y, double w, double h) {
	_backButtonArea.setRect(x, y, w, h);
    }

    /**
     * Sets the rectangle for the Version label.
     */
    protected void setVersionLocation(double x, double y) {
	_versionLocation.setLocation(x, y);
    }

    /**
     * Builds Display name for scene using class and input description
     */
    protected void setSceneString(String sceneDescrip) {
	_sceneString = getClass().getSimpleName();
	_sceneString += ": Scene " + _scene;
	_sceneString += ". " + sceneDescrip;
	repaint(_nextButtonArea);
    }

    /**
     * Retrieves Resource from applet resource context or error to err
     * 
     * @param thisComponent
     * @param str
     * @return
     */
    public java.net.URL getResource(Component thisComponent, String str) {
	java.net.URL url = getClass().getResource(
		MagicApplet.RESOURCE_PATH + str);
	if (url == null)
	    MagicApplet.printError(this, "Resource not found for "
		    + thisComponent.getClass().getSimpleName() + ": \""
		    + MagicApplet.RESOURCE_PATH + str + "\"");
	return url;
    }

    /**
     * Plays audio Clip as retrieved from MagicApplet.
     */
    protected void playAudioClip() {
	AudioClip audioClip = ((MagicApplet) getTopLevelAncestor())
		.getAudioClip();
	if (audioClip != null)
	    audioClip.play();
    }

    /**
     * Creates a new ComponentList in _dragGroupList and returns its index
     * 
     * @return the index of the new Drag Group
     */
    protected int createDragGroup() {// returns the "groupId"
	_dragGroupList.add(new ComponentList());
	return _dragGroupList.size() - 1;
    }

    /**
     * Removes the indicated component list from _dragGroupList
     * 
     * @param groupId
     */
    protected void removeDragGroup(int groupId) {
	if (groupId < 0 || groupId >= _dragGroupList.size())
	    return;
	_dragGroupList.remove(groupId);
    }

    /**
     * Adds component to specified member list of _dragGroupList
     * 
     * @param groupId
     *            The index of the member list
     * @param comp
     *            Component to add
     */
    protected void addToDragGroup(int groupId, Component comp) {
	if (groupId < 0 || groupId >= _dragGroupList.size())
	    return;
	if (_dragGroupList.get(groupId).contains(comp))
	    return;
	_dragGroupList.get(groupId).add(comp);
    }

    /**
     * From component to specified member list of _dragGroupList
     * 
     * @param groupId
     *            The index of the member list
     * @param comp
     *            Component to add
     */
    protected void removeFromDragGroup(int groupId, Component comp) {
	if (groupId < 0 || groupId >= _dragGroupList.size())
	    return;
	_dragGroupList.get(groupId).remove(comp);

    }

    /**
     * Empties specified member list of _dragGroupList
     * 
     * @param groupId
     *            the index of the member list
     */
    protected void clearDragGroup(int groupId) {
	if (groupId < 0 || groupId >= _dragGroupList.size())
	    return;
	_dragGroupList.get(groupId).clear();
    }

    // ---------------------------------------

    /**
     * Forces preferred size using static variables PANEL_WIDTH && PANEL_HEIGHT
     */
    @Override
    // From JPanel
    public Dimension getPreferredSize() {
	return new Dimension(PANEL_WIDTH, PANEL_HIEGHT);
    }

    /**
     * <ol>
     * <li>super.paintComponent(g);
     * <li>for (Component: ComponentList) .draw(g)
     * <li>drawVersionString();
     * <li>drawSceneString();
     * </ol>
     */
    public void paintComponent(Graphics g) {

	super.paintComponent(g);
	for (int i = _componentList.size() - 1; i >= 0; i--) {
	    _componentList.get(i).draw(g);
	}

	drawVersionString(g);
	drawSceneString(g);
    }

    // From MouseListener and MouseMotionListener
    /**
     * <ol>
     * <li>build DPoint From MouseEvent
     * <li>if (buttonArea contains mousepoint) buttonFlag = true;
     * <li>Selected Component
     * <ol>
     * <li>= first.isHitBy(DPoint);
     * <li>!.isSelectable : break;
     * <li>.bringToTopOfZOrder
     * <li>.repaint
     * <li>if (in(ComponentList: _dragGroupList)
     * <ul>
     * <li>(Component: draglist).startDrag()
     * </ul>
     * <li>else drag component </ul>
     * </ol>
     * </ol>
     * 
     * @Nice.Touch The outer i, for(;...), break,i combo;
     */
    public void mousePressed(MouseEvent mouse) {
	MagicApplet.printMouseDiagnostic(this, mouse);// dev

	// setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

	DPoint mousePt = new DPoint(mouse.getPoint());

	// if mouse pressed in a button area...
	if (_nextButtonArea.contains(mousePt)) {
	    _mousePressedInNextButtonArea = true;
	    return;
	} else
	    _mousePressedInNextButtonArea = false;
	if (_backButtonArea.contains(mousePt)) {
	    _mousePressedInBackButtonArea = true;
	    return;
	} else
	    _mousePressedInBackButtonArea = false;

	// otherwise, see if a component is selected
	Component selectedComponent = null;
	int i = 0;
	for (; i < _componentList.size(); i++)
	    if (_componentList.get(i).isHitBy(mousePt))
		break;
	if (i < _componentList.size())
	    selectedComponent = _componentList.get(i);

	// then deal with a selected component
	if (selectedComponent != null) {
	    if (selectedComponent.isSelectable()) {
		_componentList.bringToTopOfZOrder(selectedComponent);
		_componentList.get(0).repaint();
		if (_componentList.get(0).isDraggable()) {
		    _dragTopZComponent = true;
		    boolean topZComponentWasInADragGroup = false;
		    for (ComponentList dragGroup : _dragGroupList) {
			if (dragGroup.contains(_componentList.get(0))) {
			    for (Component comp : dragGroup)
				comp.startDrag(mousePt);
			    topZComponentWasInADragGroup = true;
			    break;
			}
		    }
		    if (!topZComponentWasInADragGroup)
			_componentList.get(0).startDrag(mousePt);
		}
	    }
	}

    }

    public void mouseDragged(MouseEvent mouse) {
	MagicApplet.printMouseDiagnostic(this, mouse);// dev

	if (!_dragTopZComponent)
	    return;
	if (_mousePressedInNextButtonArea)
	    return;
	if (_mousePressedInBackButtonArea)
	    return;

	DPoint mousePt = new DPoint(mouse.getPoint());

	boolean topZComponentWasInADragGroup = false;
	for (ComponentList dragGroup : _dragGroupList) {
	    if (dragGroup.contains(_componentList.get(0))) {
		for (Component comp : dragGroup)
		    comp.dragTo(mousePt);
		topZComponentWasInADragGroup = true;
		break;
	    }
	}
	if (!topZComponentWasInADragGroup)
	    _componentList.get(0).dragTo(mousePt);

	setSyncParams();
	syncComponents();
    }

    public void mouseReleased(MouseEvent mouse) {
	MagicApplet.printMouseDiagnostic(this, mouse);// dev

	// setCursor(_blankCursor);

	if (_mousePressedInNextButtonArea) {// if mouse was pressed in
	    // _nextButtonArea, if it's released
	    // there too, then advance the scene
	    DPoint mousePt = new DPoint(mouse.getPoint());
	    if (_nextButtonArea.contains(mousePt)) {
		advanceScene();
	    } else
		return;
	}

	if (_mousePressedInBackButtonArea) {// if mouse was pressed in
	    // _backtButtonArea, if it's released
	    // there too, then advance the scene
	    DPoint mousePt = new DPoint(mouse.getPoint());
	    if (_backButtonArea.contains(mousePt)) {
		reverseScene();
	    } else
		return;
	}

	if (_dragTopZComponent) {// otherwise, stop dragging, //dev, note you
	    // shouldn't be able to start a drag in the button
	    // area.
	    _dragTopZComponent = false;

	    boolean topZComponentWasInADragGroup = false;
	    for (ComponentList dragGroup : _dragGroupList) {
		if (dragGroup.contains(_componentList.get(0))) {
		    for (Component comp : dragGroup)
			comp.stopDrag();
		    topZComponentWasInADragGroup = true;
		    break;
		}
	    }
	    if (!topZComponentWasInADragGroup)
		_componentList.get(0).stopDrag();
	}
    }

    // ---------------------------------------

    /**
     * Generates Beautified version descriptor and draws on Graphics context
     */
    private void drawVersionString(Graphics g) {
	g.drawString("V." + Version.verString, (int) _versionLocation.x,
		(int) _versionLocation.y);
    }

    /**
     * Simply Draws the Scene String.
     * 
     * @param g
     */
    private void drawSceneString(Graphics g) {
	g.drawString(_sceneString, _nextButtonArea.x + 10, _nextButtonArea.y
		+ _nextButtonArea.height - 10);// use bottom of the
	// _nextButtonArea
    }

    // ---------------------------------------

    /**
     * Static Panel Width
     */
    protected static final int PANEL_WIDTH = 800;
    /**
     * Static panel Height TODO FIX SPELLING
     */
    protected static final int PANEL_HIEGHT = 500;
    /**
     * Scene Index
     */
    private int _scene = 0;
    /**
     * Scene Count
     */
    private int _numScenes = 0;

    /**
     * Busy Flag
     */
    protected boolean _sceneAdvancing = true;

    /**
     * Drag Mode Active TODO USAGE HUNT
     */
    protected boolean _dragTopZComponent = false;

    /**
     * Mouse Listener Flag
     */
    protected boolean _mousePressedInNextButtonArea = false;

    /**
     * Mouse Listener Flag
     */
    protected boolean _mousePressedInBackButtonArea = false;

    /**
     * master list of components?
     * <p>
     */
    public ComponentList _componentList = new ComponentList();

    /**
     * Action Area;
     * <p>
     */
    protected Rectangle _nextButtonArea = new Rectangle();

    /**
     * Action Area; *
     * <p>
     */
    protected Rectangle _backButtonArea = new Rectangle();

    /**
     * The sceneString that is displayed when drawSceneString() is called
     */
    protected String _sceneString = "Initial Scene";

    /**
     * The upper left point of where version is drawn
     */
    protected DPoint _versionLocation = new DPoint();

    /**
     * The Background color of the panel stored locally, may not be in sync with
     * actual background
     */
    protected Color _bgColor = Color.white;

    // protected Cursor _blankCursor=null;

    /**
     * THe list of ComponentList's that are considered drag groups and should
     * move in tandem
     * 
     * TODO find where this is called and automate betters
     */
    protected ArrayList<ComponentList> _dragGroupList = new ArrayList<ComponentList>();

    // ---------------------------------------

    /**
     * Unused from MouseListener and MouseMotionListener
     */
    public void mouseEntered(MouseEvent mouse) {
	MagicApplet.printMouseDiagnostic(this, mouse);
    }

    /**
     * Print diagnostic unless overriden
     */
    public void mouseExited(MouseEvent mouse) {
	MagicApplet.printMouseDiagnostic(this, mouse);
    }

    /**
     * Diagnostic currently disabled
     */
    public void mouseMoved(MouseEvent mouse) {
    }

    /**
     * Mouse Diagnostics by default
     */
    public void mouseClicked(MouseEvent mouse) {
	MagicApplet.printMouseDiagnostic(this, mouse);// dev
	// if mouse not clicked in button region, ignore it
	// in fact, ignore the mouseClicked event all togther
    }

    // ////////////////ADDED BY JAMES:
    /**
     * Stores a cache of the Visibility annotations for each field.
     */
    public HashMap<Field, Visibility> sceneVisibility = new HashMap<Field, Visibility>();

    /**
     * Usage note from original comment: override and call super.setScene(int
     * scene) first thing,
     * <p>
     * sets the _scene variable,cancels if scene is out of bound.
     * 
     * @param scene
     */
    // override and call super.setScene(int
    protected void setScene(int scene) {
	// scene) first thing,
	// should have a minimun of 2 scenes since last scene is the
	// advancePanel call
	if (scene < 0 || scene >= _numScenes)
	    return;

	_scene = scene;
	for (Field f : sceneVisibility.keySet()) {
	    Component c = null;
	    try {
		c = (Component) f.get(this);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    if (c == null) {
		System.err.println("NULL FIELD:" + f.getName());
		continue;
	    }
	    Visibility s = sceneVisibility.get(f);
	    int[] active = s.active();
	    int[] hidden = s.hidden();

	    // Only check active if no hidden values.
	    if (hidden.length == 0) {
		/**
		 * cache value as -1, so a 0 length cache will never be visible
		 */
		int lastActive = -1;
		for (int a : active) {
		    if (a > _scene)
			break;
		    if (a > lastActive) {
			a = lastActive;
		    }
		}
		if (lastActive >= _scene)
		    c.setVisible(true);
		else
		    c.setVisible(false);
		// if the last active is odd and the value is
		// less than the current scene
		if (active.length > 0 && _scene >= active[0]) {
		    // && (!s.toggle() || active.length % 2 == 1))
		    c.setVisible(true);
		} else {
		    c.setVisible(false);
		}
	    } else {
		// if hide values are present
		int closestHide = 0;

		for (int h : hidden) {
		    if (h >= _scene)
			break;
		    if (h > closestHide)
			closestHide = h;
		}

		for (int a : active) {
		    if (a > _scene)
			break;
		    if (a > closestHide) {
			c.setVisible(true);
			break;
		    }
		}
	    }
	}
    }

}
// ---------------------------------------
// ------------------------------------------------
