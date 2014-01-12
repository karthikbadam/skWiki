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

import gwt.g2d.shared.math.Vector2;

import java.io.Serializable;
import java.util.Arrays;


/**
 * Represents a rectangle.
 * 
 * @author hao1300@gmail.com
 */
public class Rectangle implements Serializable {
	/** An empty rectangle. */
	public static final Rectangle EMPTY_RECTANGLE = new ImmutableRectangle(0, 0, 0, 0);
	
	private static final long serialVersionUID = 8819333315393490701L;
	
	public double x, y, width, height;
	
	public Rectangle() {
	}
	
	public Rectangle(Rectangle rhs) {
		this(rhs.getX(), rhs.getY(), rhs.getWidth(), rhs.getHeight());
	}
	
	public Rectangle(Vector2 position, double width, double height) {
		this(position.getX(), position.getY(), width, height);
	}
	
	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Creates a rectangle at the given center.
	 * 
	 * @param x x-coordinate of the center of the rectangle.
	 * @param y y-coordinate of the center of the rectangle.
	 * @param halfWidth half the width of the rectangle.
	 * @param halfHeight half the height of the rectangle.
	 * @return the rectangle created.
	 */
	public static final Rectangle createAtCenter(double x, double y, double halfWidth, 
			double halfHeight) {
		return new Rectangle(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight);
	}
	
	/**
	 * Creates a rectangle at the given center.
	 * 
	 * @param center the center of the rectangle.
	 * @param halfWidth half the width of the rectangle.
	 * @param halfHeight half the height of the rectangle.
	 * @return the rectangle created.
	 */
	public static final Rectangle createAtCenter(Vector2 center, double halfWidth, 
			double halfHeight) {
		return createAtCenter(center.getX(), center.getY(), halfWidth, halfHeight);
	}
	
	/**
	 * Gets the top-left x-coordinate of the rectangle.
	 */
	public final double getX() {
		return x;
	}
	
	/**
	 * Sets the top-left x-coordinate of the rectangle.
	 */	
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Gets the top-left y-coordinate of the rectangle.
	 */
	public final double getY() {
		return y;
	}
	
	/**
	 * Sets the top-left y-coordinate of the rectangle.
	 */	
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Gets the width of the rectangle.
	 */
	public final double getWidth() {
		return width;
	}
	
	/**
	 * Sets the width of the rectangle.
	 */	
	public void setWidth(double width) {
		this.width = width;
	}
	
	/**
	 * Gets the height of the rectangle.
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Sets the height of the rectangle.
	 */	
	public void setHeight(double height) {
		this.height = height;
	}
	
	/**
	 * Get the location.
	 */
	public Vector2 getLoc() {
		return new Vector2(x,y);
	}
	
	/**
	 * Get the size.
	 */
	public Vector2 getSize() {
		return new Vector2(width, height);
	}
	
	/**
	 * Moves the top left corner of this rectangle to (x, y).
	 * 
	 * @param x
	 * @param y
	 */
	public final void move(double x, double y) {
		setX(x);
		setY(y);
	}
	
	/**
	 * Moves the top left corner of this rectangle to the given position.
	 * 
	 * @param position the position to move the rectangle to.
	 */
	public final void move(Vector2 position) {
		move(position.getX(), position.getY());
	}
	
	@Override
	public final boolean equals(Object obj) {
		return (obj instanceof Rectangle) ? equals((Rectangle) obj) : false;
	}
	
	public final boolean equals(Rectangle rhs) {
		return getX() == rhs.getX() && getY() == rhs.getY() && getWidth() == rhs.getWidth()
				&& getHeight() == rhs.getHeight();
	}
	
	@Override
	public final int hashCode() {
		return Arrays.hashCode(new double[]{getX(), getY(), getWidth(), getHeight()});
	}
	
	@Override
	public String toString() {
		return "[[" + getX() + ", " + getY() + "], [" + getWidth() + ", " + getHeight() + "]]";
	}
	
	/**
	 * An immutable rectangle.
	 */
	private static class ImmutableRectangle extends Rectangle {
		private static final long serialVersionUID = 7659507627055283854L;
		private static final String MODIFICATION_ERROR_MESSAGE = 
				"Cannot modify an immutable rectangle";
		
		public ImmutableRectangle(double x, double y, double width, double height) {
			super(x, y, width, height);
		}
		
		@Override
		public void setX(double arg0) {
			throw new UnsupportedOperationException(MODIFICATION_ERROR_MESSAGE);
		}
		
		@Override
		public void setY(double arg0) {
			throw new UnsupportedOperationException(MODIFICATION_ERROR_MESSAGE);
		}
		
		@Override
		public void setWidth(double arg0) {
			throw new UnsupportedOperationException(MODIFICATION_ERROR_MESSAGE);
		}
		
		@Override
		public void setHeight(double arg0) {
			throw new UnsupportedOperationException(MODIFICATION_ERROR_MESSAGE);
		}
	}
}
