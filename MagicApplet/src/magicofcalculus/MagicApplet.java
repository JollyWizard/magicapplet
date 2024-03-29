//
// MagicApplet.java
//
package magicofcalculus;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.applet.AudioClip;
import java.awt.Color;

/**
 * Primary MagicApplet class which extends JApplet
 * 
 * @author f108usr13
 * 
 */
public class MagicApplet extends JApplet {

	/**
	 * Calls the <code>adjustPaintRect(Rectangle rect, int expandBy)</code>
	 * method using <code>rect</code> for the input {@link Rectangle} and
	 * <code>0</code> for <code>expandBy</code>
	 * 
	 * @param rect
	 *            Source <code>Rectangle</code>
	 * @return The source <code>Rectangle</code> unmodified
	 */
	public static Rectangle adjustPaintRect(Rectangle rect) {
		return adjustPaintRect(rect, 0);
	}

	/**
	 * Adjusts the size of a {@link Rectangle} object by a specified amount.<br>
	 * The new rectangle has the corners <code>(X1 - amount, Y1 - amount)</code>
	 * and <code>(X2 + amount, Y2 + amount)</code>
	 * 
	 * @param rect
	 *            Source rectangle
	 * @param expandBy
	 *            distance by which to expand the source rectangle
	 * @return A rectangle object expanded by the specified amount
	 */
	public static Rectangle adjustPaintRect(Rectangle rect, int expandBy) {
		expandBy = expandBy + PAINT_RECT_OFFSET;
		if (expandBy < PAINT_RECT_OFFSET)
			expandBy = PAINT_RECT_OFFSET;
		rect.setRect(rect.x - expandBy, rect.y - expandBy, rect.width + 2
				* expandBy, rect.height + 2 * expandBy);
		return rect;
	}

	/**
	 * Displays a diagnostic message
	 * 
	 * @param object
	 *            The object from which the message originates
	 * @param msg
	 *            The desired diagnostic message
	 */
	public static void printDiagnostic(Object object, String msg) {
		String output = object.getClass().getSimpleName() + ":" + msg;
		System.out.println(output);
	}

	/**
	 * Static method which displays an error message and indicates the class
	 * from which the message originated.
	 * 
	 * @param object
	 *            The object from which the message originates
	 * @param msg
	 *            The desired error message
	 */
	public static void printError(Object object, String msg) {
		String output = object.getClass().getSimpleName() + ":" + msg;
		System.err.println(output);
	}

	/**
	 * Displays a mouse diagnostic message
	 * 
	 * @param object
	 *            The object from which the mouse event originates
	 * @param mouse
	 *            {@link MouseEvent} object
	 */
	public static void printMouseDiagnostic(Object object, MouseEvent mouse) {
		Class<? extends Object> objClass = object.getClass();
		String output = objClass.getSimpleName() + ":"
				+ mouse.paramString().split(",")[0] + ",(" + mouse.getX() + ","
				+ mouse.getY() + ")" + ",(" + mouse.getXOnScreen() + ","
				+ mouse.getYOnScreen() + ")";
		if (mouse.getClickCount() > 0)
			output = output.concat(",clicks=" + mouse.getClickCount());
		System.out.println(output);
	}

	/**
	 * Returns a URL pointing to the specified resource relative to the class
	 * path
	 * 
	 * @param str
	 *            Name of resource
	 * @return Class-path relative URL pointing to the specified resource
	 */
	public java.net.URL getResource(String str) {
		java.net.URL url = getClass().getResource(RESOURCE_PATH + str);
		if (url == null)
			printError(this, "Resource not found: \"" + RESOURCE_PATH + str
					+ "\"");
		return url;
	}

	/**
	 * Accessor for the private field {@link _audioClip}, which is either null
	 * or the "ding.wav" {@link AudioClip}
	 * 
	 * @return AudioClip stored in _audioClip field
	 */
	public AudioClip getAudioClip() {
		return _audioClip;
	}

	/**
	 * Initializes the various panels, _panelList, _audioClip, and calls the
	 * createGUI method
	 */
	public void init() {
		printDiagnostic(this, "init()");

		// dev, get panel backgroud color from applet tag
		// String colorStr = getParameter("backgroundColor");
		// if (colorStr!=null) setBackgroundColor(new
		// Color(Integer.parseInt(colorStr,16)));

		_secantApproxPanel = new SecantApproxPanel();
		_riemannSumsPanel = new RiemannSumsPanel();
		_areaFunctionPanel = new AreaFunctionPanel();
		_bmiTrianglePanel = new BMITrianglePanel();
		_bmiRectanglePanel = new BMIRectanglePanel();
		_fundTheoremPanel = new FundTheoremPanel();

		_currentPanelIndex = -1;
		_panelList.add(_secantApproxPanel);
		_panelList.add(_bmiTrianglePanel);
		_panelList.add(_secantApproxPanel);
		_panelList.add(_riemannSumsPanel);
		_panelList.add(_bmiRectanglePanel);
		_panelList.add(_riemannSumsPanel);
		_panelList.add(_areaFunctionPanel);
		_panelList.add(_fundTheoremPanel);
		_panelList.add(_areaFunctionPanel);

		java.net.URL soundUrl = getResource("ding.wav");
		if (soundUrl == null)
			_audioClip = null;
		else
			_audioClip = getAudioClip(soundUrl);

		execute_createGUI();
	}

	/**
	 * Not used ?
	 */
	public void start() {
		printDiagnostic(this, "start()");
	}

	/**
	 * Not used ?
	 */
	public void stop() {
		printDiagnostic(this, "stop()");
	}

	/**
	 * Called when applet is destroyed. Displays a diagnostic message and then
	 * calls the {@link execute_cleanUp} method
	 */
	public void destroy() {
		printDiagnostic(this, "destroy()");
		execute_cleanUp();
	}

	// ----------------------------------

	/**
	 * Creates the Graphical User Interface
	 */
	private void createGUI() {
		printDiagnostic(this, "createGUI()");
		setLayout(new java.awt.GridLayout(1, 0));
		advancePanel();
	}

	/**
	 * Calls the {@link createGUI} method and displays an error message if any
	 * exceptions are caught
	 */
	private void execute_createGUI() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGUI();
				}
			});
		} catch (Exception e) {
			printError(this, "Error executing createGUI()");
		}
	}

	/**
	 * Removes the panel with index _currentPanelIndex from _panelList. <br>
	 * Called by the {@link execute_cleanUp} method
	 */
	private void cleanUp() {
		remove(_panelList.get(_currentPanelIndex));
	}

	/**
	 * Calls the {@link cleanUp} method and displays an error message if any
	 * exceptions are caught
	 */
	private void execute_cleanUp() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					cleanUp();
				}
			});
		} catch (Exception e) {
			printError(this, "Error executing cleanUp()");
		}
	}

	/**
	 * Advances the scene to the next panel in {@link _panelList}
	 */
	public void advancePanel() {
		printDiagnostic(this, "advancePanel()");
		if (_currentPanelIndex >= _panelList.size())
			return;
		if (_currentPanelIndex >= 0) {
			remove(_panelList.get(_currentPanelIndex));
		}

		_currentPanelIndex++;
		if (_currentPanelIndex >= _panelList.size())
			_currentPanelIndex = 0;
		add(_panelList.get(_currentPanelIndex));

		validate();
		_panelList.get(_currentPanelIndex).advanceScene();
		_panelList.get(_currentPanelIndex).repaint();
	}

	/**
	 * Rewinds the scene by one panel
	 */
	public void reversePanel() {
		printDiagnostic(this, "reversePanel()");
		if (_currentPanelIndex >= _panelList.size())
			return;
		if (_currentPanelIndex >= 0) {
			remove(_panelList.get(_currentPanelIndex));
		}

		_currentPanelIndex--;
		if (_currentPanelIndex < 0)
			_currentPanelIndex = _panelList.size() - 1;
		add(_panelList.get(_currentPanelIndex));

		validate();
		_panelList.get(_currentPanelIndex).reverseScene();// dev
		_panelList.get(_currentPanelIndex).repaint();
	}

	// ----------------------------------

	public static int PAINT_RECT_OFFSET = 2;

	/**
	 * The path that contains the applet's resources
	 */
	public static String RESOURCE_PATH = "resources/";

	/**
	 * A {@link Color} equivalent to <code>(60, 150, 170)</code>
	 */
	public static Color GREEN = new Color(60, 150, 170);

	/**
	 * A {@link Color} equivalent to <code>(90,180,200)</code>
	 */
	public static Color LIGHT_GREEN = new Color(90, 180, 200);

	/**
	 * ArrayList of type {@link Panel} which holds the scene panels in order
	 */
	private ArrayList<Panel> _panelList = new ArrayList<Panel>();

	/**
	 * Index of the current panel
	 */
	private int _currentPanelIndex = 0;

	/**
	 * Instance of {@link SecantApproxPanel} used in scene.
	 */
	private SecantApproxPanel _secantApproxPanel = null;

	/**
	 * Instance of {@link BMITrianglePanel} used in scene.
	 */
	private BMITrianglePanel _bmiTrianglePanel = null;

	/**
	 * Instance of {@link RiemannSumsPanel} used in scene.
	 */
	private RiemannSumsPanel _riemannSumsPanel = null;

	/**
	 * Instance of {@link BMIRectanglePanel} used in scene.
	 */
	private BMIRectanglePanel _bmiRectanglePanel = null;

	/**
	 * Instance of {@link AreaFunctionPanel} used in scene.
	 */
	private AreaFunctionPanel _areaFunctionPanel = null;

	/**
	 * Instance of {@link FundTheoremPanel} used in scene.
	 */
	private FundTheoremPanel _fundTheoremPanel = null;

	/**
	 * AudioClip holding the "ding.wav" sound
	 */
	private AudioClip _audioClip = null;

}
// ------------------------------------------------
// ------------------------------------------------
