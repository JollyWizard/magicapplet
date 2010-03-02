//
// RectComponent.java
//
package magicofcalculus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class RectComponent extends Component {
    public RectComponent(Panel panel) {
	super(panel);
    }

    public void setRectComponent(DPoint upperLeft, DPoint lowerRight) {
	setUpperLeft(upperLeft);
	setLowerRight(lowerRight);
    }
    public void setUpperLeft(DPoint point) {
	repaint();
	_upperLeft.setLocation(point);
	resetShape();
	repaint();
    }
    public void setLowerRight(DPoint point) {
	repaint();
	_lowerRight.setLocation(point);
	resetShape();
	repaint();
    }    
    public DPoint getUpperLeft() {return new DPoint(_upperLeft);}
    public DPoint getLowerRight() {return new DPoint(_lowerRight);}
    
    //From Component
    public void setPosition(DPoint p) {setUpperLeft(p);}
    public DPoint getPosition() {return new DPoint(_upperLeft);}
    public void draw(Graphics g){
	if (!_visible) return;
	Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(_color);
	g2.draw(_rectangle);	
	g2.fill(_rectangle);
    }
    public void repaint() {
	if (_panel==null) return;
	_panel.repaint(MagicApplet.adjustPaintRect(_rectangle.getBounds()));
    }
    public boolean isHitBy(DPoint pt) {
	if (!_visible) return false;
	return _rectangle.contains(pt);
    }   
    
//------------------------------------------------

    private DPoint _upperLeft = new DPoint();
    private DPoint _lowerRight = new DPoint();

    private Rectangle2D.Double _rectangle = new Rectangle2D.Double();;
	    
    private void resetShape() {
	_rectangle.setFrameFromDiagonal(_upperLeft,_lowerRight);
    }
}
//------------------------------------------------

