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

import java.io.Serializable;


/**
 * Represents a circle.
 * 
 * @author hao1300@gmail.com
 */
public class Circle implements Serializable {
	private static final long serialVersionUID = -772707593394400941L;
	
	private Vector2 center;
	private double radius;
	
	public Circle(double centerX, double centerY, double radius) {
		this(new Vector2(centerX, centerY), radius);
	}
	
	public Circle(Vector2 center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Circle(Circle circle) {
		this(circle.getCenter(), circle.getRadius());
	}
	
	/**
	 * Gets the center of the arc.
	 */
	public final Vector2 getCenter() {
		return center;
	}
	
	/**
	 * Gets the center X-coordinate of the arc.
	 */
	public final double getCenterX() {
		return center.getX();
	}
	
	/**
	 * Gets the center Y-coordinate of the arc.
	 */
	public final double getCenterY() {
		return center.getY();
	}
	
	/**
	 * Sets the center of the arc.
	 */
	public final void setCenter(Vector2 center) {
		this.center = center;
	}
	
	/**
	 * Gets the radius of the arc.
	 */
	public final double getRadius() {
		return radius;
	}
	
	/**
	 * Sets the radius of the arc.
	 */
	public final void setRadius(double radius) {
		this.radius = radius;
	}
}
