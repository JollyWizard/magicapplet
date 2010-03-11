//
// Label.java
//
package magicofcalculus;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import static java.lang.Math.round;

public class Label extends Component {
   
    public Label(Panel panel) {
	super(panel);
    }

    public void setText(String text) {
	_text = text;
	repaint();
    }
    public void setImage(String filename) {
	java.net.URL imageUrl = _panel.getResource(this,filename);
	if (imageUrl==null) {
	    _imageIcon = null;
	    _rect.setRect(0,0,0,0);
	}
	else {
	    _imageIcon = new ImageIcon(imageUrl);
	    _rect.setRect(_rect.x,_rect.y,_imageIcon.getIconWidth(),_imageIcon.getIconHeight());//this repaints it
	}
    }

    public void setSize(int width, int height) {
	repaint();
	_rect.setRect(_rect.x,_rect.y,width,height);
	repaint();
    }
    public Rectangle2D.Double getRect() {return new Rectangle2D.Double(_rect.x,_rect.y,_rect.width,_rect.height);}
    public double getWidth() {return _rect.width;}
    public double getHeight() {return _rect.height;}

    public void setBottomLeftCorner(DPoint pt) {
	repaint();
	_rect.setRect(pt.x,pt.y-_rect.height,_rect.width,_rect.height);
	repaint();
    }    
    public DPoint getBottomLeftCorner() {
	return new DPoint(_rect.x,_rect.y+_rect.height);
    }

    public void setBgColor(Color color) {
	_bgColor=color;
	repaint();
    }
    public void setFgColor(Color color) {
	_fgColor=color;
	repaint();
    }
    public void setOpaque(boolean set) {
	_opaque = set;
	repaint();
    }
    public void setDisplayImage(boolean set) {
	_displayImage= set;
	repaint();
    }
    public void setShowBorder(boolean set) {
	_showBorder = set;
	repaint();
    }

    //from Component
    public void setPosition(DPoint pt) {
	repaint();
	_rect.setRect(pt.x,pt.y,_rect.width,_rect.height);
	repaint();
    }
    public DPoint getPosition() {return new DPoint(_rect.x,_rect.y);}
    public void draw(Graphics g){

	if (!_visible) return;
	if (_panel==null) return;
	Graphics2D g2 = (Graphics2D) g.create();

	if (_displayImage) {
	    if (_imageIcon!=null) {
		if (_opaque) g2.drawImage(_imageIcon.getImage(),(int)round(_rect.x),(int)round(_rect.y),(int)round(_rect.width),(int)round(_rect.height),_bgColor,_panel);
		else g2.drawImage(_imageIcon.getImage(),(int)round(_rect.x),(int)round(_rect.y),(int)round(_rect.width),(int)round(_rect.height),_panel);
	    }
	}
	else {
	    if (_opaque) {
		g2.setColor(_bgColor);
		g2.fill(_rect);
	    }
	    g2.setColor(_fgColor);
	    g2.drawString(_text,(int)round(_rect.x+TEXT_MARGIN),(int)round(_rect.y+_rect.height-TEXT_MARGIN));
	}

	if (_showBorder) {
	    g2.setColor(Color.black);
	    g2.draw(_rect);
	}
    }
    public void repaint() {
	if (_panel==null) return;
	_panel.repaint(MagicApplet.adjustPaintRect(_rect.getBounds()));
    }
    public boolean isHitBy(DPoint pt) {
	if (!_visible) return false;
	return _rect.contains(pt);
    }
    public void startDrag(DPoint mousePoint) {
	super.startDrag(mousePoint);
	setShowBorder(true);
    }
    public void stopDrag() {
	setShowBorder(false);
    }
    
//------------------------------------------------

    private static int TEXT_MARGIN=2;

    private Rectangle2D.Double _rect = new Rectangle2D.Double();
    private Color _bgColor = Color.white;
    private Color _fgColor = Color.black;
    private boolean _opaque = false;
    private String _text="";
    private ImageIcon _imageIcon=null;
    private boolean _displayImage= false;
    private boolean _showBorder = false;
}	    
//------------------------------------------------
