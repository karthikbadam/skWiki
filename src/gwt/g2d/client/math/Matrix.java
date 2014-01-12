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
package gwt.g2d.client.math;

import gwt.g2d.shared.math.Vector2;

import java.util.Arrays;


/**
 * Represents a 3x3 matrix for 2D transformation.
 * 
 * The matrix has the following structure:
 * 
 * <pre>
 * m11 m21 dx
 * m12 m22 dy
 *  0   0   1
 * </pre>
 * 
 * @author hao1300@gmail.com
 */
public class Matrix {
	private double m11, m12, m21, m22, dx, dy;

	/**
	 * Constructs an identity matrix.
	 */
	public Matrix() {
		m11 = 1;
		m22 = 1;
	}

	/**
	 * Creates a new matrix using the given values (the last row is implicit).
	 * 
	 * @param m11
	 * @param m12
	 * @param m21
	 * @param m22
	 * @param dx
	 * @param dy
	 */
	public Matrix(double m11, double m12, double m21, double m22, double dx,
			double dy) {
		set(m11, m12, m21, m22, dx, dy);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param rhs
	 */
	public Matrix(Matrix rhs) {
		set(rhs);
	}

	/**
	 * Sets this matrix to have the given values.
	 * 
	 * @param m11
	 * @param m12
	 * @param m21
	 * @param m22
	 * @param dx
	 * @param dy
	 */
	public final void set(double m11, double m12, double m21, double m22, 
			double dx, double dy) {
		this.m11 = m11;
		this.m12 = m12;
		this.m21 = m21;
		this.m22 = m22;
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * Sets the value of this matrix to be the given matrix.
	 * 
	 * @param rhs
	 */
	public final void set(Matrix rhs) {
		set(rhs.m11, rhs.m12, rhs.m21, rhs.m22, rhs.dx, rhs.dy);
	}
	
	/**
	 * Sets this matrix values to be a clockwise rotation transformation.
	 * This will override all existing values in the matrix. If you want to
	 * transform this matrix by a rotation transformation, considers using
	 * {@link #rotate(double)} instead.
	 * 
	 * @param angle
	 */
	public final void setRotate(double angle) {
		m11 = Math.cos(angle);
		m12 = Math.sin(angle);
		m21 = -m12;
		m22 = m11;
		dx = 0;
		dy = 0;
	}
	
	/**
	 * Sets this matrix values to be a counter-clockwise rotation transformation.
	 * This will override all existing values in the matrix. If you want to
	 * transform this matrix by a rotation transformation, considers using
	 * {@link #rotate(double)} instead.
	 * 
	 * @param angle
	 */
	public final void setRotateCcw(double angle) {
		setRotate(-angle);
	}
	
	/**
	 * Sets this matrix values to be a scale transformation.
	 * This will override all existing values in the matrix. If you want to
	 * transform this matrix by a scale transformation, considers using
	 * {@link #scale(double, double)} instead.
	 * 
	 * @param x
	 * @param y
	 */
	public final void setScale(double x, double y) {
		set(x, 0, 0, y, 0, 0);
	}
	
	/**
	 * Sets this matrix values to be a translation transformation.
	 * This will override all existing values in the matrix. If you want to
	 * transform this matrix by a translation transformation, considers using
	 * {@link #translate(double, double)} instead.
	 * 
	 * @param x
	 * @param y
	 */
	public final void setTranslate(double x, double y) {
		set(1, 0, 0, 1, x, y);
	}
	
	/**
	 * Sets this matrix to be the identity matrix.
	 */
	public final void setIdentity() {
		set(1, 0, 0, 1, 0, 0);
	}
	
	/**
	 * Returns a new matrix that is the result of this * rhs.
	 * 
	 * @param rhs
	 * @return the new matrix
	 */
	public final Matrix multiply(Matrix rhs) {
		return new Matrix(this).mutableMultiply(rhs);
	}

	/**
	 * Multiply this by rhs. Unlike {@link #multiply(Matrix)}, the returned Matrix
	 * is this, so no new Matrix is allocated.
	 * 
	 * @param rhs
	 *          the vector to multiply.
	 * @return self to support chaining.
	 */
	public final Matrix mutableMultiply(Matrix rhs) {
		set(this.m11 * rhs.m11 + this.m12 * rhs.m21,
				this.m11 * rhs.m12 + this.m12 * rhs.m22,
				this.m21 * rhs.m11 + this.m22 * rhs.m21,
				this.m21 * rhs.m12 + this.m22 * rhs.m22,
				this.m11 * rhs.dx + this.m12 * rhs.dy + this.dx,
				this.m21 * rhs.dx + this.m22 * rhs.dy + this.dy);
		return this;
	}
	
	/**
	 * Returns a new matrix that is the result of this rotated clockwise by the 
	 * given angle.
	 * 
	 * @param angle
	 * @return the new matrix
	 */
	public final Matrix rotate(double angle) {
		return new Matrix(this).mutableRotate(angle);
	}

	/**
	 * Returns a new matrix that is the result of this rotated counter-clockwise 
	 * by the given angle.
	 * 
	 * @param angle
	 * @return the new matrix
	 */
	public final Matrix rotateCcw(double angle) {
		return new Matrix(this).mutableRotateCcw(angle);
	}
	
	/**
	 * Rotates this matrix clockwise by angle.
	 * Unlike {@link #rotate(double)}, the returned Matrix is this, so no new 
	 * Matrix is allocated.
	 * 
	 * @param angle
	 * @return self to support chaining.
	 */
	public final Matrix mutableRotate(double angle) {
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		set(m11 * c + m12 * -s, m11 * s + m12 * c,
				m21 * c + m22 * -s, m21 * s + m22 * c,
				dx, dy);
		return this;
	}
	
	/**
	 * Rotates this matrix anti-clockwise by angle.
	 * Unlike {@link #rotateCcw(double)}, the returned Matrix is this, 
	 * so no new Matrix is allocated.
	 * 
	 * @param angle
	 * @return self to support chaining.
	 */
	public final Matrix mutableRotateCcw(double angle) {
		return mutableRotate(-angle);
	}
	
	/**
	 * Returns a new matrix that is this matrix scaled by x and y.
	 * 
	 * @param x
	 * @param y
	 * @return the new scaled matrix.
	 */
	public final Matrix scale(double x, double y) {
		return new Matrix(this).mutableScale(x, y);
	}
	
	/**
	 * Scales this matrix by x and y.
	 * Unlike {@link #scale(double, double)}, the returned Matrix is this, so no 
	 * new Matrix is allocated.
	 * 
	 * @param x
	 * @param y
	 * @return self to support chaining.
	 */
	public final Matrix mutableScale(double x, double y) {
		set(m11 * x, m12 * y, m21 * x, m22 * y, dx, dy);		
		return this;
	}
	
	/**
	 * Returns a new matrix that is this matrix translated by x and y.
	 * 
	 * @param x
	 * @param y
	 * @return a new translated matrix.
	 */
	public final Matrix translate(double x, double y) {
		return new Matrix(this).mutableTranslate(x, y);
	}
	
	/**
	 * Translates this matrix by x and y.
	 * Unlike {@link #translate(double, double)}, the returned Matrix is this, so 
	 * no new Matrix is allocated.
	 * 
	 * @param x
	 * @param y
	 * @return self to support chaining.
	 */
	public final Matrix mutableTranslate(double x, double y) {
		set(m11, m12, m21, m22, m11 * x + m12 * y + dx, m21 * x + m22 * y + dy);		
		return this;
	}
	
	/**
	 * Transform the given vector by this matrix.
	 * 
	 * @param vector
	 * @return a new Vector2
	 */
	public final Vector2 transform(Vector2 vector) {
		return mutableTransform(new Vector2(vector));
	}
	
	/**
	 * Transform the given vector by this matrix.
	 * Unlike {@link #transform(Vector2)}, the returned Vector2 is the given
	 * vector, so no new V is allocated.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public final Vector2 mutableTransform(Vector2 vector) {
		vector.set(m11 * vector.getX() + m12 * vector.getY() + dx,
				m21 * vector.getX() + m22 * vector.getY() + dy);
		return vector;
	}
	
	/**
	 * @param m11 the m11 to set
	 */
	public final void setM11(double m11) {
		this.m11 = m11;
	}

	/**
	 * @return the m11
	 */
	public final double getM11() {
		return m11;
	}

	/**
	 * @param m12 the m12 to set
	 */
	public final void setM12(double m12) {
		this.m12 = m12;
	}

	/**
	 * @return the m12
	 */
	public final double getM12() {
		return m12;
	}

	/**
	 * @param m21 the m21 to set
	 */
	public final void setM21(double m21) {
		this.m21 = m21;
	}

	/**
	 * @return the m21
	 */
	public final double getM21() {
		return m21;
	}

	/**
	 * @param m22 the m22 to set
	 */
	public final void setM22(double m22) {
		this.m22 = m22;
	}

	/**
	 * @return the m22
	 */
	public final double getM22() {
		return m22;
	}

	/**
	 * @param dx the dx to set
	 */
	public final void setDx(double dx) {
		this.dx = dx;
	}

	/**
	 * @return the dx
	 */
	public final double getDx() {
		return dx;
	}

	/**
	 * @param dy the dy to set
	 */
	public final void setDy(double dy) {
		this.dy = dy;
	}

	/**
	 * @return the dy
	 */
	public final double getDy() {
		return dy;
	}

	@Override
	public final boolean equals(Object obj) {
		return (obj instanceof Matrix) ? equals((Matrix) obj) : false;
	}

	public final boolean equals(Matrix rhs) {
		return (m11 == rhs.m11) && (m12 == rhs.m12) && (dx == rhs.dx)
				&& (m21 == rhs.m21) && (m22 == rhs.m22) && (dy == rhs.dy);
	}

	@Override
	public final int hashCode() {
		return Arrays.hashCode(new double[] { m11, m12, dx, m21, m22, dy });
	}
}
