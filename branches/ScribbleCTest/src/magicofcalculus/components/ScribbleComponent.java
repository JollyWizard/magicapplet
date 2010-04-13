package magicofcalculus.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import magicofcalculus.Component;
import magicofcalculus.DPoint;
import magicofcalculus.Panel;

/**
 * A public class extending <code>Component</code> ScribbleComponent allows for
 * free-hand strokes to be made within a defined area. The component can accept
 * multiple strokes until it is told to commit and be draggable (unimplemented)
 * <p>
 * The contents of the ScribbleComponent can be <i>committed</i>, which clears
 * the array containing the recorded mouse strokes after rendering the strokes
 * to a raster BufferedImage object. After being committed, the
 * {@link ScribbleComponent#draw(Graphics)} method will render directly from the
 * BufferedImage. Prior to being committed, the
 * {@link ScribbleComponent#draw(Graphics)} method will draw from the mouse
 * stroke array. Committing the content will result in faster drawing.<br>
 * Summary:
 * <ul>
 * Committed ScribbleComponents
 * <li>Render from BufferedImage objects</li>
 * <li>Cannot be modified</li>
 * <li>Can be dragged (not implemented)</li>
 * </ul>
 * <ul>
 * Uncommitted ScribbleComponents
 * <li>Render from an array of DPoints</li>
 * <li>Can be appended</li>
 * <li>Cannot be dragged</li>
 * </ul>
 * The color of the scribble can be altered using
 * {@link #setColor(java.awt.Color)}, but all strokes will be drawn in the most
 * recently set color.
 * <p>
 * <i>The class implements the {@link MouseListener} and
 * {@link MouseMotionListener} interfaces, thus overriding the associated
 * methods in {@link Component}.</i>
 * 
 * @author Wade Harkins (vdtdev@gmail.com)
 * 
 */
public class ScribbleComponent extends Component implements MouseListener,
	MouseMotionListener {

    private java.awt.Color penColor = java.awt.Color.BLACK; // pen color
    private boolean isDrawing = false; // is stroke being recorded
    private ArrayList<DPoint> path = new ArrayList<DPoint>(); // path array

    /**
     * {@link DPoint} object used in the stroke path array to indicate the start
     * of a new stroke.<br>
     * <code>path_break</code> is set to
     * <code>(Double.MAX_VALUE, Double.MAX_VALUE)</code>, which should never be
     * encountered during normal operation of the applet.
     */
    private DPoint path_break = new DPoint(Double.MAX_VALUE, Double.MAX_VALUE);

    // BufferedImage to which the scribble is committed
    private BufferedImage canvas = null;

    // the size of the component
    private Dimension size = null;

    /**
     * Boolean indicating if the contents of the ScribbleComponent have been
     * committed.
     */
    private boolean committed = false;

    /**
     * Scribble component's constructor
     * 
     * @param panel
     *            The panel that will contain the scribble component
     */
    public ScribbleComponent(Panel panel) {
	super(panel);
	size = super._panel.getSize();
	super.setDraggable(committed); // make non-draggable
	super._panel.addMouseListener(this); // trap mouse events
	super._panel.addMouseMotionListener(this);
    }

    /**
     * Sets the size of the ScribbleComponent. <br>
     * 
     * @param size
     *            {@link Dimension} object specifying the size of the component
     */
    public void setSize(Dimension size) {
	this.size = size;
	super._panel.setSize(this.size);
    }

    // may be needed for annotations to work?
    public void setPosition(int x, int y){
	super._panel.setLocation(x,y);
    }
    /**
     * Sets the size of the ScribbleComponent. <br>
     * <i>Changing the size of the component after the contents have been
     * committed will clear it</i>
     * 
     * @param width
     *            integer width to assign to the component
     * @param height
     *            integer height to assign to the component
     */
    public void setSize(int width, int height) {
	this.setSize(new Dimension(width, height));
    }

    /**
     * Set the color of the 'pen' stroke. <br>
     * Default is <code>java.awt.Color.BLACK</code>
     * <p>
     * All strokes will be rendered in the most recently set color. Colors are
     * not finalized until the image is committed.
     * 
     * @param c
     *            {@link java.awt.Color} representing the desired color
     */
    public void setColor(java.awt.Color c) {
	this.penColor = c;
    }

    /**
     * Renders the recorded strokes to the specified Graphics object
     * 
     * @param gc
     *            Graphics object to draw to
     */
    private void renderScribble(Graphics gc) {
	DPoint previous = null; // this will hold the previous point
	Graphics2D g = (Graphics2D) gc;
	g.setColor(this.penColor);
	Iterator<DPoint> pitr = path.iterator();
	while (pitr.hasNext()) {
	    DPoint n = pitr.next();
	    // if the next DPoint isn't a break
	    if (!n.equals(path_break)) {
		// if at the start of drawing, or previous was break
		if (previous == null) {
		    g.drawLine((int) n.x, (int) n.y, (int) n.x, (int) n.y);
		}
		// otherwise draw a line from the previous point to the current
		else {
		    g.drawLine((int) previous.x, (int) previous.y, (int) n.x,
			    (int) n.y);
		}
		previous = n; // copy the current DPoint to previous
	    }
	    // the current DPoint was a break, so clear the previous
	    else {
		previous = null; // clear the previous point so it's not
		// connected
	    }
	}
    }

    /**
     * Renders the strokes to the BufferedImage <code>canvas</code> which can
     * then be rendered using {@link ScribbleComponent#draw}
     */
    private void render() {

	canvas = new BufferedImage(super._panel.getSize().width, super._panel
		.getSize().height, BufferedImage.TYPE_INT_RGB);
	Graphics2D g = canvas.createGraphics();
	renderScribble(g);
    }

    /**
     * Commits the contents of the {@link ScribbleComponent}.
     * <ul>
     * <li>The array of recorded mouse strokes is cleared</li>
     * <li>The graphics content is rasterized to a BufferedImage</li>
     * <li>The component becomes draggable</li>
     * </ul>
     */
    public void commit() {
	this.render();
	this.path = null;
	this.committed = true;
	super.setDraggable(true);
    }

    /**
     * Draws the recorded mouse strokes if uncommitted, otherwise draws the
     * committed raster contents
     */
    public void draw(Graphics g) {
	Graphics2D g2d = (Graphics2D) g; // get a Graphics2D context
	if (committed) {
	    g2d.drawImage(canvas, null, 1, 1); // render buffered image
	}
	// directly draw the output
	else {
	    renderScribble(g);
	}
    }

    public void repaint() {
	_panel.repaint();
    }

    // ========== Mouse Events===============

    @Override
    public void mouseClicked(MouseEvent arg0) {
	// record as single stroke, both in the same position to allow
	// dots to be drawn
	/*
	 * if (arg0.getButton() == MouseEvent.BUTTON1) { path.add(new
	 * DPoint(arg0.getPoint())); // path.add(new DPoint(arg0.getPoint()));
	 * // path.add(path_break); // indicate the end of a stroke }
	 */
	if(arg0.getButton()==MouseEvent.BUTTON2){
	this.commit();
	this.repaint();
	}
	repaint();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub
	//super._panel.mouseEntered(arg0);

    }

    /**
     * Called when the mouse exits the component. If the button is down, the
     * current stroke is immediately stopped.
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent arg0) {
	// used to end the stroke to prevent out-of-bounds points
	if (arg0.getButton() == MouseEvent.BUTTON1) {
	    path.add(path_break);
	    isDrawing = false;
	}
	repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent arg0) {
	// start of a stroke recording
	if (arg0.getButton() == MouseEvent.BUTTON1) {
	    isDrawing = true; // drawing begins
	    path.add(new DPoint(arg0.getPoint()));
	}
	arg0.consume();
	repaint();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent arg0) {
	// end of a stroke
	if (arg0.getButton() == MouseEvent.BUTTON1) {
	    path.add(new DPoint(arg0.getPoint()));
	    path.add(this.path_break);
	    isDrawing = false;

	}
	//arg0.consume();
	repaint();

    }

    // TODO: Check and make sure this actually works
    @Override
    public void mouseDragged(MouseEvent arg0) {
	/*
	 * if (super._draggable) { super.dragTo(new DPoint(arg0.getPoint())); }
	 */
	// determine if both the mouse is down, and a stroke is underway
	/*
	 * if (arg0.getButton() == MouseEvent.BUTTON1 && isDrawing) { // add the
	 * new point to the path path.add(new DPoint(arg0.getPoint())); }
	 */
	if (isDrawing && !committed) {
	    path.add(new DPoint(arg0.getPoint()));
	}

	repaint();

    }

    @Override
    public void mouseMoved(MouseEvent arg0) {

	// determine if both the mouse is down, and a stroke is underway
	/*
	 * if (arg0.getButton() == MouseEvent.BUTTON1 ) { // add the new point
	 * to the path path.add(new DPoint(arg0.getPoint())); }
	 */
	//arg0.consume();
	repaint();

    }

}
