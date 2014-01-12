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

/**
 * Represents a 2D arc.
 * 
 * @author hao1300@gmail.com
 */
public class Arc extends Circle {
	private static final long serialVersionUID = -6185219656129001237L;
	
	private double startAngle, endAngle;
	private boolean anticlockwise;
	
	/**
	 * Creates an anticlockwise arc.
	 * 
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param startAngle
	 * @param endAngle
	 */
	public Arc(double centerX, double centerY, double radius, double startAngle, 
			double endAngle) {
		this(new Vector2(centerX, centerY), radius, startAngle, endAngle);
	}
	
	/**
	 * Creates an anticlockwise arc.
	 * 
	 * @param center
	 * @param radius
	 * @param startAngle
	 * @param endAngle
	 */
	public Arc(Vector2 center, double radius, double startAngle, double endAngle) {
		this(center, radius, startAngle, endAngle, true);
	}
	
	public Arc(Vector2 center, double radius, double startAngle, double endAngle, 
			boolean anticlockwise) {
		super(center, radius);
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.anticlockwise = anticlockwise;
	}
	
	public Arc(Arc arc) {
		this(arc.getCenter(), arc.getRadius(), arc.getStartAngle(), arc.getEndAngle(), 
				arc.isAnticlockwise());
	}
	
	
	
	/**
	 * Gets the starting angle of the arc.
	 */
	public final double getStartAngle() {
		return startAngle;
	}
	
	/**
	 * Sets the starting angle of the arc.
	 */
	public final void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
	}
	
	/**
	 * Gets the ending angle of the arc.
	 */
	public final double getEndAngle() {
		return endAngle;
	}
	
	/**
	 * Sets the ending angle of the arc.
	 */
	public final void setEndAngle(double endAngle) {
		this.endAngle = endAngle;
	}
	
	/**
	 * Gets whether the start angle to the end angle should be considered as
	 * anticlockwise direction.
	 */
	public final boolean isAnticlockwise() {
		return anticlockwise;
	}
	
	/**
	 * Sets whether the start angle to the end angle should be considered as 
	 * anticlockwise direction.
	 */
	public final void setAnticlockwise(boolean anticlockwise) {
		this.anticlockwise = anticlockwise;
	}
	
	/**
	 * Offset both the start angle and the end angle by the given amount.
	 */
	public final void offsetAngle(double angle) {
		this.startAngle += angle;
		this.endAngle += angle;
	}
}
