//
// TriangleIterator.java
//
package magicofcalculus;

import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;
import java.awt.geom.AffineTransform;

/**
 * Iterator class for {@link TriangleShape}.<br>Implements PathIterator
 * 
 * @see java.awt.geom.PathIterator
 */
public class TriangleIterator implements PathIterator
{

  /**
   * Public constructor for TriangleIterator. 
   * Accepts a {@link TriangleShape} and an {@link AffineTransform} as parameters.
   * @param triangle Source triangle
   * @param affine Affine transformation
   */
  public TriangleIterator(TriangleShape triangle, AffineTransform affine) {
    _triangle = triangle;
    _affine = affine;
  }

/**
 * {@inheritDoc}
 */
  public int getWindingRule() {
    return WIND_EVEN_ODD;
  }

  /**
   * Determines if all of the path points have been evaluated.
   * 
   * @return Boolean indicating if all points of the triangle have been evaulated
   */
  public boolean isDone() {
    return (_index >= 4);
  }

  /**
   * Advances to the next point of the triangle 
   */
  public void next() {
    _index++;
  }

  /**
   * Determines the type of segment at the current index of the specified float
   * array.
   * @param coords Array of coordinates
   * @return The segment type of the current point
   */
  public int currentSegment(float[] coords) {
    if (isDone()) {
      throw new NoSuchElementException("quad iterator iterator out of bounds");
    }
    int type = 0;
    switch (_index) {
    case 0:
      coords[0] = (float) _triangle.getP1().x;
      coords[1] = (float) _triangle.getP1().y;
      type = SEG_MOVETO;
      break;
    case 1:
      coords[0] = (float) _triangle.getP2().x;
      coords[1] = (float) _triangle.getP2().y;
      type = SEG_LINETO;
      break;
    case 2:
      coords[0] = (float) _triangle.getP3().x;
      coords[1] = (float) _triangle.getP3().y;
      type = SEG_LINETO;
      break;
    case 3:
      type = SEG_CLOSE;
      break;
    default:
      break;
    }

    if (_affine != null) {
      if (_index < 3)
        _affine.transform(coords, 0, coords, 0, 2);
    }

    return type;

  }

  /**
   * Determines the type of segment at the current index of the specified double
   * array.
   * @param coords Array of coordinates
   * @return The segment type of the current point
   */
  public int currentSegment(double[] coords) {
    if (isDone()) {
      throw new NoSuchElementException("quad iterator iterator out of bounds");
    }
    int type = 0;
    switch (_index) {
    case 0:
      coords[0] = _triangle.getP1().x;
      coords[1] = _triangle.getP1().y;
      type = SEG_MOVETO;
      break;
    case 1:
      coords[0] = _triangle.getP2().x;
      coords[1] = _triangle.getP2().y;
      type = SEG_LINETO;
      break;
    case 2:
      coords[0] = _triangle.getP3().x;
      coords[1] = _triangle.getP3().y;
      type = SEG_LINETO;
      break;
    case 3:
      type = SEG_CLOSE;
      break;
    default:
      break;
    }

    if (_affine != null) {
      if (_index < 3)
        _affine.transform(coords, 0, coords, 0, 2);
    }

    return type;

  }

  // ------------------------------

  /**
   * Holds the TriangleShape set in
   * {@link #TriangleIterator(TriangleShape, AffineTransform)}
   */
  private TriangleShape _triangle = null;
  /**
   * The index of the current point in the triangle
   */
  private int _index = 0;

  /** private field containing an affine transformation */
  private AffineTransform _affine = null;

}
// -----------------------------------------------
// -----------------------------------------------
