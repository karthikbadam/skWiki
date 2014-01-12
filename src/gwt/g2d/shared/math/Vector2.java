/*
 * Copyright 2009 Hao Nguyen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.g2d.shared.math;

import static gwt.g2d.client.math.MathHelper.square;

import gwt.g2d.client.math.MathHelper;

import java.io.Serializable;
import java.util.Arrays;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/**
 * Represents a 2D point or vector.
 * 
 * @author hao1300@gmail.com
 */
public class Vector2 implements Serializable {
	/** A vector at (0, 0). */
	public static final Vector2 ZERO = new ImmutableVector2(0.0, 0.0);
	/** A vector at (1, 1). */
	public static final Vector2 ONE = new ImmutableVector2(1.0, 1.0);
	/** A vector at (1, 0). */
	public static final Vector2 UNIT_X = new ImmutableVector2(1.0, 0.0);
	/** A vector at (0, 1). */
	public static final Vector2 UNIT_Y = new ImmutableVector2(0.0, 1.0);
	/** Any value below this is considered zero. */
	public static final double EPS = 0.0000001;
	
	private static final long serialVersionUID = 5543191998324226983L;
	
	public double x, y;
	
	/**
	 * Creates a default vector2 at (0, 0).
	 * Uses Vector2.ZERO to represents (0, 0) instead.
	 */
	public Vector2() {
	}
	
	public Vector2(Vector2 rhs) {
		this.x = rhs.getX();
		this.y = rhs.getY();
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a vector at (value, value).
	 */
	public Vector2(double value) {
		this(value, value);
	}
	
	/**
	 * Catmull-rom interpolation between two vectors.
	 * 
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated vector.
	 */
	public static final Vector2 catmullRom(Vector2 value1, Vector2 value2, Vector2 value3, 
			Vector2 value4, double amount) {
    return new Vector2().mutableCatmullRom(value1, value2, value3, value4, amount);
  }
	
	/**
	 * Hermite interpolation between two vectors.
	 * 
	 * @param value1
	 * @param tangent1
	 * @param value2
	 * @param tangent2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated vector.
	 */
	public static final Vector2 hermite(Vector2 value1, Vector2 tangent1, Vector2 value2, 
			Vector2 tangent2, double amount) {
    return new Vector2().mutableHermite(value1, tangent1, value2, tangent2, amount);
  }
	
	/**
	 * Linear interpolation between two vectors.
	 * 
	 * @param value1
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated vector.
	 */
	public static final Vector2 lerp(Vector2 value1, Vector2 value2, double amount) {
    return new Vector2().mutableLerp(value1, value2, amount);
  }
	
	/**
	 * Creates a vector within the given radius.
	 * 
	 * @param radius
	 * @return a random vector that lies within a circle with (0, 0) as the 
	 * 				 origin and the radius as given.
	 */
	public static final Vector2 random(int radius) {
		Vector2 vector = new Vector2(Math.random(), Math.random());
		double radiusSquared = MathHelper.square(radius);
		while (vector.lengthSquared() > radiusSquared) {
			vector.set(Math.random(), Math.random());
		}
		return vector;
	}
	
	/**
	 * Creates a random vector2 inside the given range.
	 * 
	 * @param maxX
	 * @param maxY
	 * @return a random vector whose x value is in [0, maxX), and y value is
	 * 				 in [0, maxY)
	 */
	public static final Vector2 random(double maxX, double maxY) {
		return new Vector2(Math.random() * maxX, Math.random() * maxY);
	}
	
	/**
	 * Creates a random vector2 inside the given range.
	 * 
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return a random vector whose x value is in [minX, maxX), and y value is
	 * 				 in [minY, maxY)
	 */
	public static final Vector2 random(double minX, double minY, double maxX, 
			double maxY) {
		return new Vector2(Math.random() * (maxX - minX) + minX,
				Math.random() * (maxY - minY) + minY);
	}
	
	/**
	 * Creates a vector inside the given rectangle.
	 * 
	 * @param rectangle
	 * @return a new vector that lies inside the given rectangle.
	 */
	public static final Vector2 random(Rectangle rectangle) {
		return new Vector2(Math.random() * rectangle.getWidth() + rectangle.getX(),
				Math.random() * rectangle.getHeight() + rectangle.getY());
	}
	
	/**
	 * Creates a normalized vector.
	 * 
	 * @return a random normalized vector.
	 */
	public static final Vector2 randomNormalize() {
		Vector2 vector = random(1);
		while (vector.getX() == 0 && vector.getY() == 0) {
			vector = random(1);
		}
		return vector;
	}
	
	/**
	 * Smooth interpolation between two vectors.
	 * 
	 * @param value1 
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated vector.
	 */
	public static final Vector2 smoothStep(Vector2 value1, Vector2 value2, 
			double amount) {
    return new Vector2().mutableSmoothStep(value1, value2, amount);
  }
	
	/**
	 * Gets the x-coordinate.
	 */
	public final double getX() {
		return x;
	}
	
	/**
	 * Gets the x-coordinate as an integer.
	 */
	public final int getIntX() {
		return (int) getX();
	}
	
	/**
	 * Gets the y-coordinate.
	 */
	public final double getY() {
		return y;
	}
	
	/**
	 * Gets the y-coordinate as an integer.
	 */
	public final int getIntY() {
		return (int) getY();
	}
	
	/**
	 * Sets the x-coordinate.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Sets the y-coordinate.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Sets the x and y coordinates of the vector.
	 * 
	 * @param x
	 * @param y
	 */
	public final void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calculates whether this vector is zero (using machine epsilon so that very small vectors are also counted as zero.
	 * 
	 * @return whether this vector is zero. 
	 */
	public final boolean isZero() {
		return this.lengthSquared() < EPS*EPS;
	}
	
	/**
	 * Gets (this.x + rhs.x, this.y + rhs.y).
	 * 
	 * @param rhs the vector to add.
	 * @return a new vector that is this + rhs. 
	 */
	public final Vector2 add(Vector2 rhs) {
		return new Vector2(getX() + rhs.getX(), getY() + rhs.getY());
	}
	
	/**
	 * Gets (this.x + x, this.y + y).
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return a new vector that is this + rhs. 
	 */
	public final Vector2 add(double x, double y) {
		return new Vector2(getX() + x, getY() + y);
	}
	
	/**
	 * Adds rhs to this.
	 * Unlike {@link #add(Vector2)}, the returned vector is this, so no new 
	 * vector is allocated.
	 *  
	 * @param rhs the vector to add.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableAdd(Vector2 rhs) {
		this.x += rhs.x;
		this.y += rhs.y;
		return this;
	}
	
	/**
	 * Adds rhs to this.
	 * Unlike {@link #add(Vector2)}, the returned vector is this, so no new 
	 * vector is allocated.
	 *  
	 * @param rhs the vector to add.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableAdd(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * Gets (this.x - rhs.x, this.y - rhs.y).
	 * 
	 * @param rhs the vector to subtract from this.
	 * @return a new vector that is this - rhs. 
	 */
	public final Vector2 subtract(Vector2 rhs) {
		return new Vector2(this.x - rhs.x, this.y - rhs.y);
	}
	
	/**
	 * Gets (this.x - rhs.x, this.y - rhs.y).
	 * 
	 * @param rhs the vector to subtract from this.
	 * @return a new vector that is this - rhs. 
	 */
	public final Vector2 subtract(double x, double y) {
		return new Vector2(this.x - x, this.y - y);
	}
	
	/**
	 * Subtract rhs from this.
	 * Unlike {@link #subtract(Vector2)}, the returned vector is this, so no new 
	 * vector is allocated.
	 * 
	 * @param rhs the vector to subtract from this.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableSubtract(Vector2 rhs) {
		this.x -= rhs.x;
		this.y -= rhs.y;
		return this;
	}
	
	/**
	 * Subtract rhs from this.
	 * Unlike {@link #subtract(Vector2)}, the returned vector is this, so no new 
	 * vector is allocated.
	 * 
	 * @param rhs the vector to subtract from this.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableSubtract(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * Gets (this.x * rhs.x, this.y * rhs.y).
	 * 
	 * @param rhs the vector to multiply.
	 * @return a new vector that is this * rhs. 
	 */
	public final Vector2 multiply(Vector2 rhs) {
		return new Vector2(this.x * rhs.x, this.y * rhs.y);
	}
	
	/**
	 * Multiply this by rhs.
	 * Unlike {@link #multiply(Vector2)}, the returned vector is this, so no new 
	 * vector is allocated.
	 * 
	 * @param rhs the vector to multiply.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableMultiply(Vector2 rhs) {
		this.x *= rhs.x;
		this.y *= rhs.y;
		return this;
	}
	
	/**
	 * Gets (this.x * rhs.x, this.y * rhs.y).
	 * 
	 * @param rhs the vector to multiply.
	 * @return a new vector that is this * rhs. 
	 */
	public final Vector2 multiply(double x, double y) {
		return new Vector2(this.x * x, this.y * y);
	}
	
	/**
	 * Multiply this by rhs.
	 * Unlike {@link #multiply(Vector2)}, the returned vector is this, so no new 
	 * vector is allocated.
	 * 
	 * @param rhs the vector to multiply.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableMultiply(double x, double y) {
		this.x *= x;
		this.y *= y;
		return this;
	}
	
	/**
	 * Gets (this.x / rhs.x, this.y / rhs.y).
	 * 
	 * @param rhs the vector by which this is to be divided.
	 * @return a new vector that is this / rhs. 
	 */
	public final Vector2 divide(Vector2 rhs) {
		return new Vector2(this.x / rhs.x, this.y / rhs.y);
	}
	
	/**
	 * Divide this by rhs.
	 * Unlike {@link #divide(Vector2)}, the returned vector is this, so no new 
	 * vector is allocated.
	 * 
	 * @param rhs the vector by which this is to be divided.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableDivide(Vector2 rhs) {
		this.x /= rhs.x;
		this.y /= rhs.y;
		return this;
	}
	
	/**
	 * Gets (this.x / rhs.x, this.y / rhs.y).
	 * 
	 * @param rhs the vector by which this is to be divided.
	 * @return a new vector that is this / rhs. 
	 */
	public final Vector2 divide(double x, double y) {
		return new Vector2(this.x / x, this.y / y);
	}
	
	/**
	 * Divide this by rhs.
	 * Unlike {@link #divide(Vector2)}, the returned vector is this, so no new 
	 * vector is allocated.
	 * 
	 * @param rhs the vector by which this is to be divided.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableDivide(double x, double y) {
		this.x /= x;
		this.y /= y;
		return this;
	}
	
	/**
	 * Clamp this vector between min and max, that is, the new vector's x is 
	 * between min's x and max's x, and its y is between min's y and max's y. 
	 * 
	 * @param min
	 * @param max
	 * @return a new vector that is inside [min, max]
	 */
	public final Vector2 clamp(Vector2 min, Vector2 max) {
    return new Vector2(
        MathHelper.clamp(getX(), min.getX(), max.getX()),
        MathHelper.clamp(getY(), min.getY(), max.getY()));
  }
	
	/**
	 * Similar to clamp except the vector returned is this vector whose x and y
	 * have been clamped to between min and max.
	 * 
	 * @param min
	 * @param max
	 * @return self to support chaining.
	 */
	public final Vector2 mutatableClamp(Vector2 min, Vector2 max) {
		setX(MathHelper.clamp(getX(), min.getX(), max.getX()));
    setY(MathHelper.clamp(getY(), min.getY(), max.getY()));
    return this;
	}
	
	/**
	 * Gets the dot product for this and rhs.
	 * 
	 * @param rhs
	 * @return the dot product of this and rhs.
	 */
	public final double dot(Vector2 rhs) {
		return getX() * rhs.getX() + getY() * rhs.getY();
	}
	
	/**
	 * Gets the distance squared from this to rhs.
	 * 
	 * @param rhs
	 * @return the distance squared from this to rhs.
	 */
	public final double distanceSquared(Vector2 rhs) {
		return square(getX() - rhs.getX()) + square(getY() - rhs.getY());
	}
	
	/**
	 * Gets the distance from this to rhs.
	 * 
	 * @param rhs
	 * @return the distance from this to rhs.
	 */
	public final double distance(Vector2 rhs) {
		return Math.sqrt(distanceSquared(rhs));
	}
	
	/**
	 * Gets the length of this vector squared.
	 * 
	 * @return the length of this vector squared.
	 */
	public final double lengthSquared() {
		return square(getX()) + square(getY());
	}
	
	/**
	 * Gets the length of this vector.
	 * 
	 * @return the length of this vector.
	 */
	public final double length() {
		return Math.sqrt(lengthSquared());
	}
	
	/**
	 * Gets a vector whose x is the max of this's x and rhs's x, and y is the 
	 * max of this'y and rhs's y. 
	 * 
	 * @param rhs
	 * @return a new vector whose x and y values are the max of this and rhs.
	 */
	public final Vector2 max(Vector2 rhs) {
    return new Vector2(
        Math.max(getX(), rhs.getX()),
        Math.max(getY(), rhs.getY()));
  }
	
	/**
	 * Similar to max() except the vector returned is this vector whose x and y
	 * values have been set to the result of max().
	 * 
	 * @param rhs
	 * @return self to support chaining.
	 */
	public final Vector2 mutableMax(Vector2 rhs) {
    setX(Math.max(getX(), rhs.getX()));
    setY(Math.max(getY(), rhs.getY()));
    return this;
  }
	
	/**
	 * Gets a vector whose x is the min of this's x and rhs's x, and y is the 
	 * min of this'y and rhs's y. 
	 * 
	 * @param rhs
	 * @return a new vector whose x and y values are the min of this and rhs.
	 */
	public final Vector2 min(Vector2 rhs) {
    return new Vector2(
        Math.min(getX(), rhs.getX()),
        Math.min(getY(), rhs.getY()));
  }
	
	/**
	 * Similar to min() except the vector returned is this vector whose x and y
	 * values have been set to the result of min().
	 * 
	 * @param rhs
	 * @return self to support chaining.
	 */
	public final Vector2 mutableMin(Vector2 rhs) {
    setX(Math.min(getX(), rhs.getX()));
    setY(Math.min(getY(), rhs.getY()));
    return this;
  }
	
	/**
	 * Gets the negate of this vector.
	 * 
	 * @return a new vector whose x and y values have the opposite sign of this
	 * 				 vector's x and y values.
	 */
	public final Vector2 negate() {
      return new Vector2(-getX(), -getY());
  }

	/**
	 * Similar to negate() except the vector returned is this vector whose x and
	 * y values have been set to be the result of negate().
	 * 
	 * @return self to support chaining.
	 */
  public final Vector2 mutableNegate() {
      setX(-getX());
      setY(-getY());
      return this;
  }
  
  /**
   * Gets the normal of this vector.
   * 
   * @return a new vector that is the unit vector of this vector.
   */
  public final Vector2 normalize() {
  	return this.scale(1.0 / length());
  }

  /**
   * Similar to normalize() except the vector returned is this vector whose
   * x and y values have been set to be the result of normalize().
   * 
   * @return self to support chaining.
   */
  public final Vector2 mutableNormalize() {
  	return this.mutableScale(1.0 / length());
  }
  
	/**
	 * Gets (this.x * rhs, this.y * rhs).
	 * 
	 * @param rhs the value to scale this vector by.
	 * @return a new vector that is this * rhs. 
	 */
	public final Vector2 scale(double rhs) {
		return new Vector2(getX() * rhs, getY() * rhs);
	}
	
	/**
	 * Scale this by rhs.
	 * Unlike scale(), the returned vector is this, so no new vector is allocated.
	 * 
	 * @param rhs the value to scale this vector by.
	 * @return self to support chaining.
	 */
	public final Vector2 mutableScale(double rhs) {
		setX(getX() * rhs);
		setY(getY() * rhs);
		return this;
	}
	
	/**
	 * Similar to catmullRom() except the vector returned is this vector whose
	 * x and y have been set to the result of catmullRom().
	 * 
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return self to support chaining.
	 */
	public final Vector2 mutableCatmullRom(Vector2 value1, Vector2 value2, 
			Vector2 value3, Vector2 value4, double amount) {
    setX(MathHelper.catmullRom(value1.getX(), value2.getX(), value3.getX(), 
    		value4.getX(), amount));
    setY(MathHelper.catmullRom(value1.getY(), value2.getY(), value3.getY(), 
				value4.getY(), amount));
    return this;
  }
	
	/**
	 * Similar to hermit() except the vector returned is this vector whose
	 * x and y have been set to the result of hermite().
	 * 
	 * @param value1
	 * @param tangent1
	 * @param value2
	 * @param tangent2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return self to support chaining.
	 */
	public final Vector2 mutableHermite(Vector2 value1, Vector2 tangent1, 
			Vector2 value2, Vector2 tangent2, double amount) {
    setX(MathHelper.hermite(value1.getX(), tangent1.getX(), value2.getX(), 
    		tangent2.getX(), amount));
    setY(MathHelper.hermite(value1.getY(), tangent1.getY(), value2.getY(), 
    		tangent2.getY(), amount));
    return this;
  }
	
	/**
	 * Similar to lerp() except the vector returned is this vector whose
	 * x and y have been set to the result of lerp().
	 * 
	 * @param value1
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return self to support chaining.
	 */
	public final Vector2 mutableLerp(Vector2 value1, Vector2 value2, double amount) {
    setX(MathHelper.lerp(value1.getX(), value2.getX(), amount));
    setY(MathHelper.lerp(value1.getY(), value2.getY(), amount));
    return this;
  }
	
	/**
	 * Similar to smoothStep() except the vector returned is this vector whose
	 * x and y have been set to the result of smoothStep().

	 * 
	 * @param value1 
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated vector.
	 */
	public final Vector2 mutableSmoothStep(Vector2 value1, Vector2 value2, double amount) {
    setX(MathHelper.smoothStep(value1.getX(), value2.getX(), amount));
    setY(MathHelper.smoothStep(value1.getY(), value2.getY(), amount));
    return this;
  }
	
	@Override
	public final boolean equals(Object obj) {
		return (obj instanceof Vector2) ? equals((Vector2) obj) : false;
	}
	
	public final boolean equals(Vector2 rhs) {
		return (this.x - rhs.x) < EPS && (this.y - rhs.y) < EPS;
	}
	
	@Override
	public final int hashCode() {
		return Arrays.hashCode(new double[]{getX(), getY()});
	}
	
	@Override
	public final String toString() {
		return "[" + this.x + ", " + this.y + "]";
	}
	
	
	// regular expression for parsing a vector
	private static RegExp Pattern = RegExp.compile("\\[\\s*([0-9]*[\\.]?[0-9]+)\\s*,\\s*([0-9]*[\\.]?[0-9]+)\\s*\\]");
	
	
	// parse from String
	public static Vector2 parseVector2(String s) {
		MatchResult res = Pattern.exec(s);
		if (res == null) return null;
		return new Vector2(Double.parseDouble(res.getGroup(1)), Double.parseDouble(res.getGroup(2)));
	}
	
	
	
	// clone a vector
	public final Vector2 copy() {
		return new Vector2(this.x, this.y);
	}
	
	/**
	 * An unmodifiable vector2.
	 */
	private static class ImmutableVector2 extends Vector2 {
		private static final long serialVersionUID = -4931305479279295158L;
		private static final String MODIFICATION_ERROR_MESSAGE = 
				"Cannot modify an immutable vector";
		
		public ImmutableVector2(double x, double y) {
			super(x, y);
		}
		
		@Override
		public void setX(double x) {
			throw new UnsupportedOperationException(MODIFICATION_ERROR_MESSAGE);
		}
		
		@Override
		public void setY(double y) {
			throw new UnsupportedOperationException(MODIFICATION_ERROR_MESSAGE);
		}
	}
}
