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
package gwt.g2d.client.graphics.visitor;

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.shared.math.Vector2;

/**
 * Adds the given end point to the current path, connected to the previous one 
 * by a quadratic Bezier curve with the given control point.
 * The new position will be set to the given end point.
 * 
 * @author hao1300@gmail.com
 */
public class QuadraticCurveToVisitor implements ShapeVisitor {
	private final double controlPointX, controlPointY, endPointX, endPointY;
	
	/**
	 * Adds a curve that connects the last point in the subpath to the given 
	 * point (endPointX, endPointY), using the control point
	 * (controlPointX, controlPointY).
	 */
	public QuadraticCurveToVisitor(double controlPointX, double controlPointY, 
			double endPointX, double endPointY) {
		this.controlPointX = controlPointX;
		this.controlPointY = controlPointY; 
		this.endPointX = endPointX;
		this.endPointY = endPointY;
	}
	
	/**
	 * Adds a curve that connects the last point in the subpath to the given 
	 * endPoint, using the control point controlPoint.
	 */
	public QuadraticCurveToVisitor(Vector2 controlPoint, Vector2 endPoint) {
		this(controlPoint.getX(), controlPoint.getY(), endPoint.getX(), endPoint.getY());
	}
	
	@Override
	public void visit(Surface surface) {
		surface.getContext().quadraticCurveTo(controlPointX, controlPointY,
				endPointX, endPointY);
	}
}
