//
// DPoint.java
//
package magicofcalculus;

import java.awt.geom.Point2D;
import java.awt.Point;

/**
 * 2D Point inheriting from <code>Point2D.Double</code>
 * @author T Johnson
 */
@SuppressWarnings("serial")
public class DPoint extends Point2D.Double
{
/**
 * No-argument constructor
 */
  public DPoint() {
    super();
  }

  /**
   * Copy constructor used to copy an existing <code>DPoint</code>
   * @param pt <code>DPoint</code> to copy
   */
  public DPoint(DPoint pt) {
    super(pt.x, pt.y);
  }

  /**
   * Constructs a <code>DPoint</code> from a given {@link Point} 
   * @param pt <code>Point</code> object to copy
   */
  public DPoint(Point pt) {
    super(pt.x, pt.y);
  }

  /**
   * Constructor accepting X and Y coordinates
   * @param x Initial x-coordinate 
   * @param y Initial y-coordinate
   */
  public DPoint(double x, double y) {
    super(x, y);
  }

  /**
   * Translate point by <code>(X,Y)</code>
   * @param byX X translation
   * @param byY Y translation
   */
  public void translate(double byX, double byY) {
    x += byX;
    y += byY;
  }

  /**
   * Returns a new <code>DPoint</code> representing the result of the specified translation 
   * @param byX X translation
   * @param byY Y translation
   * @return <code>DPoint</code> containing the result of the translation
   */
  public DPoint getTranslation(double byX, double byY) {
    DPoint returnPoint = new DPoint(this);
    returnPoint.translate(byX, byY);
    return returnPoint;
  }

}
// ---------------------------------------------

