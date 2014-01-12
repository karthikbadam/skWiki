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


import com.google.gwt.canvas.dom.client.Context2d;

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.shared.math.Vector2;

/**
 * Adds the end point to the current path, connected to the starting point by a
 * cubic Bezier curve with the given control points.
 * The new position will be set to the given end point.
 * 
 * @author hao1300@gmail.com
 */
public class BezierCurveVisitor implements ShapeVisitor {
	private final double startPointX, startPointY, controlPoint1X, controlPoint1Y, 
	 		controlPoint2X, controlPoint2Y, endPointX, endPointY;
	
	/**
	 * Adds a curve that connects the point (startPointX, startPointY) to the
	 * point (endPointX, endPointY), using the control points
	 * (controlPoint1X, controlPoint1Y) and (controlPoint2X, controlPoint2Y).
	 */
	public BezierCurveVisitor(double startPointX, double startPointY, 
			double controlPoint1X, double controlPoint1Y, 
			double controlPoint2X, double controlPoint2Y, 
			double endPointX, double endPointY) {
		this.startPointX = startPointX;
		this.startPointY = startPointY;
		this.controlPoint1X = controlPoint1X;
		this.controlPoint1Y = controlPoint1Y;
		this.controlPoint2X = controlPoint2X;
		this.controlPoint2Y = controlPoint2Y; 
		this.endPointX = endPointX;
		this.endPointY = endPointY;
	}
	
	/**
	 * Adds a curve that connects the startPoint to the endPoint, using the 
	 * control points controlPoint1 and controlPoint2.
	 */
	public BezierCurveVisitor(Vector2 startPoint, Vector2 controlPoint1, 
			Vector2 controlPoint2, Vector2 endPoint) {
		this(startPoint.getX(), startPoint.getY(), 
				controlPoint1.getX(), controlPoint1.getY(), 
				controlPoint2.getX(), controlPoint2.getY(), 
				endPoint.getX(), endPoint.getY());
	}
	
	@Override
	public void visit(Surface surface) {
		Context2d context = surface.getContext();
		context.moveTo(startPointX, startPointY);
		context.bezierCurveTo(controlPoint1X, controlPoint1Y, 
				controlPoint2X, controlPoint2Y, 
				endPointX, endPointY);
	}
}
