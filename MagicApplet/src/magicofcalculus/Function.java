//
// Function.java
//
package magicofcalculus;

import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Abstract class representing a mathematical function
 * 
 * @author T Johnson
 */
public abstract class Function extends Object {

	public abstract double getYofX(double x);

	public double[] getArrayOfYofX(double[] x) {
		double[] y = new double[x.length];
		for (int i = 0; i < x.length; i++)
			y[i] = getYofX(x[i]);
		return y;
	}

	// ---------------------
	/**
	 * Static class extending <code>Function</code><br>
	 * Represents the function <code>y = sin(x) / x</code>
	 */
	public static class SinXOverX extends Function {
		/**
		 * Overrides <code>getYofX</code> method in <code>Function</code>.
		 * Returns <code>y = f(x)</code>
		 * 
		 * @param x
		 *            Value assigned to the X variable
		 * @return value of <code>Y</code> for <code>sin(X) / X</code>
		 */
		@Override
		public double getYofX(double x) {
			if (x == 0.0)
				return 1;
			else
				return sin(x) / x;
		}
	}

	/**
	 * Static class extending <code>Function</code><br>
	 * Represents a function that determines the integral of
	 * <code> sin(x) / x</code>
	 */
	public static class IntegralOfSinXOverX extends Function {
		/**
		 * Overrides <code>getYofX</code> method in <code>Function</code>.
		 * Returns <code>y = f(x)</code>
		 * 
		 * @param x
		 *            value of X variable
		 * @return The integral of <code>sin(X) / X</code>
		 */
		@Override
		public double getYofX(double x) {// this is a Taylor series expansion
			final int NUM_TERMS = 25;
			int n = 1;
			int sign = 1;
			double xToTheN = x;
			double factorial = 1.0;
			double sum = sign * xToTheN / n / factorial;
			for (int i = 0; i < NUM_TERMS - 1; i++) {
				n += 2;
				sign *= -1;
				xToTheN *= x * x;
				factorial *= n * (n - 1);
				sum += sign * xToTheN / n / factorial;
			}
			return sum;
		}
	}

	/**
	 * Static class that extends <code>Function</code>.<br>
	 * Represents the function <code>y = x^2</code>
	 * 
	 */
	public static class XSquared extends Function {
		/**
		 * Overrides the <code>getYofX</code> method in <code>Function</code>.
		 * Returns <code>y = f(x)</code>
		 * 
		 * @param x
		 *            value of X
		 * @return <code>x</code> to the power of 2
		 */
		@Override
		public double getYofX(double x) {
			return x * x;
		}
	}

	/**
	 * Static class that extends <code>Function</code>.<br>
	 * Represents the function <code>y = x * x + 1</code>
	 * 
	 */
	public static class XSquaredPlusOne extends Function {
		/**
		 * Overrides the <code>getYofX</code> method in <code>Function</code>.
		 * Returns <code>y = f(x)</code>
		 * 
		 * @param x
		 *            value of X
		 * @return value of <code>x * x + 1</code>
		 */
		@Override
		public double getYofX(double x) {
			return x * x + 1;
		}
	}

	/**
	 * Static class that extends <code>Function</code>.<br>
	 * 
	 */
	public static class LowerSemiCircle extends Function {

		/**
		 * Overrides the <code>getYofX</code> method in <code>Function</code>.
		 * Returns <code>y = f(x)</code>
		 * 
		 * @param x
		 *            value of X
		 * @return <code>y = f(x)</code>
		 */
		@Override
		public double getYofX(double x) {
			return _center.y
					- sqrt(_radius * _radius - (x - _center.x)
							* (x - _center.x));
		}

		/**
		 * Set the location of the circle's center from a {@link DPoint}.
		 * 
		 * @param center
		 *            A <code>DPoint</code> specifying the center of the circle
		 */
		public void setCenter(DPoint center) {
			_center.setLocation(center);
		}

		/**
		 * Set the location of the circle's center from two <code>double</code>
		 * integers.
		 * 
		 * @param x
		 *            The X coordinate of the circle's center
		 * @param y
		 *            The Y coordinate of the circle's center
		 */
		public void setCenter(double x, double y) {
			_center.setLocation(x, y);
		}

		/**
		 * Set the radius of the circle.
		 * 
		 * @param radius
		 *            Desired radius value
		 */
		public void setRadius(double radius) {
			_radius = radius;
		}

		/**
		 * Field used to hold the circle's center
		 */
		DPoint _center = new DPoint(); // TODO: (should be private?)

		/**
		 * Field used to hold the circle's radius
		 */
		double _radius = 0.0;
	}

	/**
	 * Static class extending <code>Function</code>. Represents a Linear
	 * Function
	 * 
	 */
	public static class LinearFunction extends Function {

		/**
		 * Overrides the <code>getYofX</code> method in <code>Function</code>.
		 * Returns <code>y = f(x)</code>
		 * 
		 * @param x
		 *            value of X
		 * @return value of <code>y0 + slope * (x - x0)</code>
		 */
		@Override
		public double getYofX(double x) {
			return _point.y + _slope * (x - _point.x);
		}

		/**
		 * Sets the location of the point <code>(x0,y0)</code>
		 * 
		 * @param point
		 *            {@link DPoint} containing the value of
		 *            <code>(x0,y0)</code>
		 */
		public void setPoint(DPoint point) {
			_point.setLocation(point);
		}

		/**
		 * Sets the value of the slope of the Linear Function
		 * 
		 * @param slope
		 *            <code>Double</code> integer representing the value of the
		 *            function's slope
		 */
		public void setSlope(double slope) {
			_slope = slope;
		}

		/**
		 * Define the linear function by a point and slope
		 * 
		 * @param point
		 *            The desired point
		 * @param slope
		 *            The desired slope
		 */
		public void setLinearFunction(DPoint point, double slope) {
			setPoint(point);
			setSlope(slope);
		}

		/**
		 * Define the linear function by two points
		 * 
		 * @param point1
		 *            First point
		 * @param point2
		 *            Second point
		 */
		public void setLinearFunction(DPoint point1, DPoint point2) {
			_point.setLocation(point1);
			double dx = point2.x - point1.x;
			if (dx == 0.0) {
				_slope = 0.0;
			} else {
				double dy = point2.y - point1.y;
				_slope = dy / dx;
			}
		}

		DPoint _point = new DPoint();

		/**
		 * The slope of the linear function
		 */
		double _slope = 0.0;
	}

}
// ------------------------------------
// ------------------------------------
