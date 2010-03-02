//
// Panel.java
//
// Subclasses, 6 in order of appearance: 
//   SecantTrianglePanel, BMITrianglePanel, (then SecantTrianglePanel again),
///  RiemannSumsPanel (was RiemannRectsPanel), BMIRectanglePanel (then RiemannSumsPanel again),
//   AreaFunctionPanel, FundTheoremPanel (then AreaFunctionPanel again)
//
package magicofcalculus;

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
import java.util.ArrayList;

/**
 * Every Panel has a set of syncParams that can synchonize the compnents in the
 * Panel. The Panel can go thru the components and make sure they are all sync'd
 * to the params. So, 1. the topZComponent is dragged as usual 2. the Panel sets
 * the syncParams depending on the component just dragged 3. the Panel sync's
 * all the rest of the components
 * 
 * A Panel also has a _dragGroupList
 */
public class Panel extends JPanel implements MouseListener, MouseMotionListener
{

  public Panel() {

    setBorder(BorderFactory.createLineBorder(Color.black));

    setBackgroundColor(new Color(0xdd, 0xdd, 0xdd));

    addMouseListener(this);
    addMouseMotionListener(this);

    setNextButtonArea(50, PANEL_HIEGHT - 50, PANEL_WIDTH - 50, 50);// default
                                                                   // bottom 50
                                                                   // pixels
    setBackButtonArea(0, PANEL_HIEGHT - 50, 50, 50);// default bottom left 50x50
                                                    // pixels
    setVersionLocation(10, PANEL_HIEGHT - 10);

    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
  }

  protected void setBackgroundColor(Color color) {
    _bgColor = color;
    setBackground(_bgColor);
  }

  protected Color getBackgroundColor() {
    return _bgColor;
  }

  protected void setScene(int scene) {// override and call super.setScene(int
                                      // scene) first thing,
    // should have a minimun of 2 scenes since last scene is the advancePanel
    // call
    if (scene < 0 || scene >= _numScenes)
      return;
    else
      _scene = scene;
  }

  protected int getScene() {
    return _scene;
  }

  protected void setNumScenes(int numScenes) {// also resets _scene to
                                              // _numScenes-1, so we'll
    // we'll either advance into scene 0 or reverse into the second to last
    // scene
    if (numScenes <= 0) {
      _numScenes = 0;
      return;
    }
    else {
      _numScenes = numScenes;
      _scene = _numScenes - 1;
    }

  }

  protected int getNumScenes() {
    return _numScenes;
  }

  protected void advanceScene() {
    _sceneAdvancing = true;
    if (_scene >= _numScenes - 1) {
      setScene(0);
      // ((MagicApplet)getTopLevelAncestor()).advancePanel();
    }
    else {
      _scene++;
      setScene(_scene);
    }
  }

  protected void reverseScene() {
    _sceneAdvancing = false;
    if (_scene <= 0) {
      _scene = _numScenes - 1;// leave in last scene?
      ((MagicApplet) getTopLevelAncestor()).reversePanel();
    }
    else {
      _scene--;
      setScene(_scene);
    }
  }

  protected void setSyncParams() {
    // set the sync params from the topZComponent
  }

  protected void syncComponents() {
  }

  protected void setNextButtonArea(double x, double y, double w, double h) {
    _nextButtonArea.setRect(x, y, w, h);
  }

  protected void setBackButtonArea(double x, double y, double w, double h) {
    _backButtonArea.setRect(x, y, w, h);
  }

  protected void setVersionLocation(double x, double y) {
    _versionLocation.setLocation(x, y);
  }

  protected void setSceneString(String sceneDescrip) {
    _sceneString = getClass().getSimpleName();
    _sceneString += ": Scene " + _scene;
    _sceneString += ". " + sceneDescrip;
    repaint(_nextButtonArea);
  }

  protected java.net.URL getResource(Component thisComponent, String str) {
    java.net.URL url = getClass().getResource(MagicApplet.RESOURCE_PATH + str);
    if (url == null)
      MagicApplet.printError(this, "Resource not found for "
          + thisComponent.getClass().getSimpleName() + ": \""
          + MagicApplet.RESOURCE_PATH + str + "\"");
    return url;
  }

  protected void playAudioClip() {
    AudioClip audioClip = ((MagicApplet) getTopLevelAncestor()).getAudioClip();
    if (audioClip != null)
      audioClip.play();
  }

  protected int createDragGroup() {// returns the "groupId"
    _dragGroupList.add(new ComponentList());
    return _dragGroupList.size() - 1;
  }

  protected void removeDragGroup(int groupId) {
    if (groupId < 0 || groupId >= _dragGroupList.size())
      return;
    _dragGroupList.remove(groupId);
  }

  protected void addToDragGroup(int groupId, Component comp) {
    if (groupId < 0 || groupId >= _dragGroupList.size())
      return;
    if (_dragGroupList.get(groupId).contains(comp))
      return;
    _dragGroupList.get(groupId).add(comp);
  }

  protected void removeFromDragGroup(int groupId, Component comp) {
    if (groupId < 0 || groupId >= _dragGroupList.size())
      return;
    _dragGroupList.get(groupId).remove(comp);

  }

  protected void clearDragGroup(int groupId) {
    if (groupId < 0 || groupId >= _dragGroupList.size())
      return;
    _dragGroupList.get(groupId).clear();
  }

  // ---------------------------------------

  // From JPanel
  public Dimension getPreferredSize() {
    return new Dimension(PANEL_WIDTH, PANEL_HIEGHT);
  }

  public void paintComponent(Graphics g) {

    super.paintComponent(g);
    for (int i = _componentList.size() - 1; i >= 0; i--) {
      _componentList.get(i).draw(g);
    }

    drawVersionString(g);
    drawSceneString(g);
  }

  // From MouseListener and MouseMotionListener
  public void mousePressed(MouseEvent mouse) {
    MagicApplet.printMouseDiagnostic(this, mouse);// dev

    // setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

    DPoint mousePt = new DPoint(mouse.getPoint());

    // if mouse pressed in a button area...
    if (_nextButtonArea.contains(mousePt)) {
      _mousePressedInNextButtonArea = true;
      return;
    }
    else
      _mousePressedInNextButtonArea = false;
    if (_backButtonArea.contains(mousePt)) {
      _mousePressedInBackButtonArea = true;
      return;
    }
    else
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
      }
      else
        return;
    }

    if (_mousePressedInBackButtonArea) {// if mouse was pressed in
                                        // _backtButtonArea, if it's released
                                        // there too, then advance the scene
      DPoint mousePt = new DPoint(mouse.getPoint());
      if (_backButtonArea.contains(mousePt)) {
        reverseScene();
      }
      else
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

  private void drawVersionString(Graphics g) {
    g.drawString("V." + Version.verString, (int) _versionLocation.x,
        (int) _versionLocation.y);
  }

  private void drawSceneString(Graphics g) {
    g.drawString(_sceneString, _nextButtonArea.x + 10, _nextButtonArea.y
        + _nextButtonArea.height - 10);// use bottom of the _nextButtonArea
  }

  // ---------------------------------------

  protected static final int PANEL_WIDTH = 800;

  protected static final int PANEL_HIEGHT = 500;

  private int _scene = 0;

  private int _numScenes = 0;

  protected boolean _sceneAdvancing = true;

  protected boolean _dragTopZComponent = false;

  protected boolean _mousePressedInNextButtonArea = false;

  protected boolean _mousePressedInBackButtonArea = false;

  protected ComponentList _componentList = new ComponentList();

  protected Rectangle _nextButtonArea = new Rectangle();

  protected Rectangle _backButtonArea = new Rectangle();

  protected String _sceneString = "Initial Scene";

  protected DPoint _versionLocation = new DPoint();

  protected Color _bgColor = Color.white;

  // protected Cursor _blankCursor=null;

  protected ArrayList<ComponentList> _dragGroupList =
      new ArrayList<ComponentList>();

  // ---------------------------------------

  // Unused from MouseListener and MouseMotionListener
  public void mouseEntered(MouseEvent mouse) {
    MagicApplet.printMouseDiagnostic(this, mouse);
  }

  public void mouseExited(MouseEvent mouse) {
    MagicApplet.printMouseDiagnostic(this, mouse);
  }

  public void mouseMoved(MouseEvent mouse) {/*
                                             * MagicApplet.printMouseDiagnostic(mouse
                                             * ,this);
                                             */
  }

  public void mouseClicked(MouseEvent mouse) {
    MagicApplet.printMouseDiagnostic(this, mouse);// dev
    // if mouse not clicked in button region, ignore it
    // in fact, ignore the mouseClicked event all togther
  }
}
// ---------------------------------------
// ------------------------------------------------
