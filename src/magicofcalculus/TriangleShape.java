//
// TriangleShape.java
//
/*
 * javadoc last update 2010.02.18 by WadeVH
 */
package magicofcalculus;

import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import static java.lang.Math.min;
import static java.lang.Math.max;

/**
 * Triangle class implementing {@link Shape}
 * 
 */
public class TriangleShape implements Shape
{

  /**
   * Default constructor
   */
  public TriangleShape() {

  }

  /**
   * Define the three points which make up the triangle.
   * 
   * @param p1
   *          First point
   * @param p2
   *          Second point
   * @param p3
   *          Third point
   */
  public void setTriangle(DPoint p1, DPoint p2, DPoint p3) {
    _p1.setLocation(p1);
    _p2.setLocation(p2);
    _p3.setLocation(p3);
  }

  /**
   * Returns a copy of the triangle's first point
   * 
   * @return A {@link DPoint} containing a copy of the first point (p1)
   */
  public DPoint getP1() {
    return new DPoint(_p1);
  }

  /**
   * Returns a copy of the triangle's second point
   * 
   * @return A {@link DPoint} containing a copy of the second point (p2)
   */
  public DPoint getP2() {
    return new DPoint(_p2);
  }

  /**
   * Returns a copy of the triangle's third point
   * 
   * @return A {@link DPoint} containing a copy of the third point (p3)
   */
  public DPoint getP3() {
    return new DPoint(_p3);
  }

  // From Shape
  /**
   * @{inheritDoc
   */
  public Rectangle getBounds() {
    return getBounds2D().getBounds();

  }

  public Rectangle2D getBounds2D() {
    double left = min(min(_p1.x, _p2.x), _p3.x);
    double right = max(max(_p1.x, _p2.x), _p3.x);
    double top = min(min(_p1.y, _p2.y), _p3.y);
    double bottom = max(max(_p1.y, _p2.y), _p3.y);
    return new Rectangle2D.Double(left, top, right - left, bottom - top);

  }

  /**
   * Checks whether or not this <code>TriangleShape</code> contains the
   * specified point <code>(x,y)</code>
   * 
   * @param x
   *          specified X coordinate
   * @param y
   *          specified Y coordinate
   * @return <code>true</code> if this <code>TriangleShape</code> contains the
   *         point <code>(x,y)</code>
   */
  public boolean contains(double x, double y) {
    DPoint p0 = new DPoint(x, y);
    double[] coordinates = { 0, 0 };
    if (!getAlphBetaCoordinates(p0, coordinates))
      return false;
    double alpha = coordinates[0];
    double beta = coordinates[1];

    // If p0 is in the triangle, the vector p1->p0 must be a linear combination
    // of the side vectors
    // p1->p2 and p1->p3, where the coefficients of the linear combination (the
    // coordinates alpha and beta)
    // are non-negative and sum to less than 1.
    return (alpha >= 0 && beta >= 0 && alpha + beta <= 1);

  }

  /**
   * Checks whether or not this <code>TriangleShape</code> contains the
   * specified {@link Point2D}.<br>
   * 
   * @param p
   *          specified <code>Point2D</code> to test
   * @return <code>true</code> if the specified <code>Point2D</code> is
   *         contained in this <code>TriangleShape</code>; <code>false</code>
   *         otherwise
   */
  public boolean contains(Point2D p) {
    return contains(p.getX(), p.getY());
  }

  /**
   * Tests if this <code>TriangleShape</code> intersects the specified
   * rectangular area
   * 
   * @param x
   *          X coordinate of the specified rectangluar area's upper left corner
   * @param y
   *          Y coordinate of the specified rectangular area's upper left corner
   * @param w
   *          Width of the specified rectangular area
   * @param h
   *          Height of the specified rectangular area
   * @return <code>true</code> if the specified rectangular area intersects this
   *         <code>TriangleShape</code>; otherwise <code>false</code>
   */
  public boolean intersects(double x, double y, double w, double h) {
    DPoint topLeft = new DPoint(x, y);
    DPoint topRight = new DPoint(x + w, y);
    DPoint bottomLeft = new DPoint(x, y + h);
    DPoint bottomRight = new DPoint(x + w, y + h);

    // If at least one corner is in the triangle, then return true.
    if (contains(topLeft) || contains(topRight) || contains(bottomLeft)
        || contains(bottomRight))
      return true;

    // If the rectangle doesn't intersect the bounding box of the triangle,
    // return false.
    Rectangle2D box = getBounds2D();
    if (!box.intersects(x, y, w, h))
      return false;

    // So, we have that the rect intersects the box and there are no corners in
    // the triangle.
    // If there are no corners in the box, then the rect must intersect the
    // triangle.
    // If, however, if there is a corner in the box, we can determine if the
    // rect intersects the
    // triangle by this algorithm. First, the algorithm demands that the
    // triangle hava a positive
    // orientation. Then we check the corners one by one.
    double orientation = getOrientation();
    if (orientation == 0)
      return false;
    if (orientation < 0) {
      DPoint temp = new DPoint(_p3);
      _p3.setLocation(_p2);
      _p2.setLocation(temp);
    }
    if (box.contains(topRight))
      return true;// no matter where it is, a topRight corner will intersect
    DPoint p0 = new DPoint(x, y);// get the "coordinates of p0 off of p1", alpha
    // and beta.
    double[] coordinates = { 0, 0 };
    if (!getAlphBetaCoordinates(p0, coordinates))
      return false;
    double alpha = coordinates[0];
    double beta = coordinates[1];
    if (box.contains(topLeft)) {
      if (beta < 0)
        return false;
      else
        return true;
    }
    double x2 = _p2.x - _p1.x;
    double y2 = _p2.y - _p1.y;// get the normalized point's coordinates
    double x3 = _p3.x - _p1.x;
    double y3 = _p3.y - _p1.y;
    if (box.contains(bottomRight)) {
      if (alpha < 0 || x3 < x2 && y3 < y2 && beta < 0)
        return false;
      else
        return true;
    }
    if (box.contains(bottomLeft)) {
      if (alpha > 0 && beta > 0 && x3 < x2 && y2 < y3)
        return false;
      else
        return true;
    }
    return true;// no corners in the box, rect must intersect triangle

  }

  /**
   * Tests if this <code>TriangleShape</code> intersects a specified
   * {@link Rectangle2D}
   * 
   * @param r
   *          the specified <code>Rectangle2D</code>
   * @return <code>true</code> if the specified rectangular area intersects this
   *         <code>TriangleShape</code>; otherwise <code>false</code>
   */
  public boolean intersects(Rectangle2D r) {
    return intersects(r.getX(), r.getX(), r.getWidth(), r.getHeight());
  }

  /**
   * Tests if this <code>TriangleShape</code> contains the specified rectangle
   * 
   * @param x
   *          X coordinate of the upper left corner of the specified rectangle
   * @param y
   *          Y coordinate of the upper left corner of the specified rectangle
   * @param w
   *          Width of the specified rectangle
   * @param h
   *          Height of the specified rectangle
   * @return <code>true</code> if this <code>TriangleShape</code> contains the
   *         specified rectangular area; otherwise <code>false</code>
   */
  public boolean contains(double x, double y, double w, double h) {
    return (contains(x, y) && contains(x + w, y) && contains(x, y + h) && contains(
        x + w, y + h));
  }

  /**
   * Tests if this <code>TriangleShape</code> contains the specified
   * {@link Rectangle2D}
   * 
   * @param r
   *          the specified <code>Rectangle2D</code>
   * @return <code>true</code> if this <code>TriangleShape</code> contains the
   *         specified <code>Rectangle2D</code>; otherwise <code>false</code>
   */
  public boolean contains(Rectangle2D r) {
    return contains(r.getX(), r.getX(), r.getWidth(), r.getHeight());
  }

  /**
   * Returns a new {@link PathIterator} for this <code>TriangleShape</code>
   * using the specified {@link AffineTransform}
   * 
   * @param at
   *          the specified <code>AffineTransform</code>
   * @return a new <code>PathIterator</code> based on this
   *         <code>TriangleShape</code> and the specified
   *         <code>AffineTransform</code>
   */
  public PathIterator getPathIterator(AffineTransform at) {
    return new TriangleIterator(this, at);
  }

  /**
   * Returns a new {@link PathIterator} for this <code>TriangleShape</code>
   * using the specified {@link AffineTransform}
   * 
   * @param at
   *          the specified <code>AffineTransform</code>
   * @param flatness
   *          not used by method (?)
   * @return a new <code>PathIterator</code> based on this
   *         <code>TriangleShape</code> and the specified
   *         <code>AffineTransform</code>
   */
  public PathIterator getPathIterator(AffineTransform at, double flatness) {
    return new TriangleIterator(this, at);
  }

  // -----------------------------------------------

  /**
   * Point 1 of the triangle
   */
  private DPoint _p1 = new DPoint();

  /**
   * Point 2 of the triangle
   */
  private DPoint _p2 = new DPoint();

  /**
   * Point 3 of the triangle
   */
  private DPoint _p3 = new DPoint();

  /**
   * 
   * @param p0
   *          Point to set at origin
   * @param coordinates
   * @return <code>false</code> if vertices are not colinear; otherwise
   *         <code>true</code>
   */
  private boolean getAlphBetaCoordinates(DPoint p0, double[] coordinates) {
    // Translate the points so that p1 goes to the origin
    double x0 = p0.x - _p1.x;
    double y0 = p0.y - _p1.y;
    double x2 = _p2.x - _p1.x;
    double y2 = _p2.y - _p1.y;
    double x3 = _p3.x - _p1.x;
    double y3 = _p3.y - _p1.y;

    // This is the cross product (p1->p2)x(p1->p3) of the sides of the triagle.
    // It's twice
    // the area of the triangle, and it indicates the orientation of the
    // triangles verticies:
    // >0 if the points are in counter-clockwise order p1,p2,p3, <0 otherwise.
    double p2xp3 = (x2 * y3 - x3 * y2);// p2xp3
    if (p2xp3 == 0)
      return false;// This means the verticies p1,p2,p3 are colinear

    // alpha and beta are the coordinates of the vector p1->p0 relative to the
    // basis {p1->p2,p1->p3},
    // In other words, p1->p0 = alpha*(p1->p2) + beta*(p1->p3), or as indicated
    // below,
    // p0 = (p0xp3/p2xp3)*(p2) + (p2xp0/p2xp3)*(p3)
    double p0xp3 = (x0 * y3 - x3 * y0);
    double p2xp0 = (-x0 * y2 + x2 * y0);
    coordinates[0] = p0xp3 / p2xp3;// alpha
    coordinates[1] = p2xp0 / p2xp3;// beta

    return true;
  }

  /**
   * Returns a <code>double</code> representing the orientation of the triangle.
   * Calculated by taking the cross product of the sides of the triangle.
   * 
   * @return <code>0</code> if the vertices are colinear;<br>
   *         a value greater than <code>0</code> if points are labeled
   *         counter-clockwise;<br>
   *         a value less than <code>0</code> if the points are labeled
   *         clockwise
   */
  private double getOrientation() {
    // Translate the points so that p1 goes to the origin
    double x2 = _p2.x - _p1.x;
    double y2 = _p2.y - _p1.y;
    double x3 = _p3.x - _p1.x;
    double y3 = _p3.y - _p1.y;

    // This is the cross product (p1->p2)x(p1->p3) of the sides of the triagle.
    // It's twice
    // the area of the triangle, and it indicates the orientation of the
    // triangle's verticies:
    // >0 if the points are labeled in counter-clockwise order p1,p2,p3, <0
    // otherwise. If =0,
    // then the verticies p1,p2,p3 are colinear.
    double p2xp3 = (x2 * y3 - x3 * y2);// p2xp3
    return p2xp3;

  }

}
// -----------------------------------------------
// -----------------------------------------------
