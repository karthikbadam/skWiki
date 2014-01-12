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
 * Adds a point to the current path, connected to the previous one by a 
 * straight line, then adds a second point to the current path, connected to 
 * the previous one by an arc whose properties are described by the arguments.
 * 
 * @author hao1300@gmail.com
 */
public class ArcToVisitor implements ShapeVisitor {
	private final double x0, y0, x1, y1, x2, y2, radius;
	private final boolean connectFromPrev;
	
	/**
	 * <p>
	 * If the point (x0, y0) is equal to the point (x1, y1), or if the point 
	 * (x1, y1) is equal to the point (x2, y2), or if the radius radius is zero, 
	 * then the method must add the point (x1, y1) to the subpath, and connect 
	 * that point to the previous point (x0, y0) by a straight line.
	 * <p>
	 * Otherwise, if the points (x0, y0), (x1, y1), and (x2, y2) all lie on a 
	 * single straight line, then the method must add the point (x1, y1) to the 
	 * subpath, and connect that point to the previous point (x0, y0) by a 
	 * straight line.
	 * <p>
	 * Otherwise, let The Arc be the shortest arc given by circumference of the 
	 * circle that has radius radius, and that has one point tangent to the 
	 * half-infinite line that crosses the point (x0, y0) and ends at the point 
	 * (x1, y1), and that has a different point tangent to the half-infinite line 
	 * that ends at the point (x1, y1) and crosses the point (x2, y2). The points 
	 * at which this circle touches these two lines are called the start and end 
	 * tangent points respectively. The method must connect the point (x0, y0) to 
	 * the start tangent point by a straight line, adding the start tangent point 
	 * to the subpath, and then must connect the start tangent point to the end 
	 * tangent point by The Arc, adding the end tangent point to the subpath.
	 */
	public ArcToVisitor(double x0, double y0, double x1, double y1, 
			double x2, double y2, double radius) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.radius = radius;
		this.connectFromPrev = false;
	}
	
	/**
	 * Let the point (x0, y0) be the last point in the subpath.
	 * <p>
	 * If the point (x0, y0) is equal to the point (x1, y1), or if the point 
	 * (x1, y1) is equal to the point (x2, y2), or if the radius radius is zero, 
	 * then the method must add the point (x1, y1) to the subpath, and connect 
	 * that point to the previous point (x0, y0) by a straight line.
	 * <p>
	 * Otherwise, if the points (x0, y0), (x1, y1), and (x2, y2) all lie on a 
	 * single straight line, then the method must add the point (x1, y1) to the 
	 * subpath, and connect that point to the previous point (x0, y0) by a 
	 * straight line.
	 * <p>
	 * Otherwise, let The Arc be the shortest arc given by circumference of the 
	 * circle that has radius radius, and that has one point tangent to the 
	 * half-infinite line that crosses the point (x0, y0) and ends at the point 
	 * (x1, y1), and that has a different point tangent to the half-infinite line 
	 * that ends at the point (x1, y1) and crosses the point (x2, y2). The points 
	 * at which this circle touches these two lines are called the start and end 
	 * tangent points respectively. The method must connect the point (x0, y0) to 
	 * the start tangent point by a straight line, adding the start tangent point 
	 * to the subpath, and then must connect the start tangent point to the end 
	 * tangent point by The Arc, adding the end tangent point to the subpath.
	 */
	public ArcToVisitor(double x1, double y1, double x2, double y2, double radius) {
		this.x0 = 0;
		this.y0 = 0;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.radius = radius;
		this.connectFromPrev = true;
	}
	
	/**
	 * @see #ArcToVisitor(double, double, double, double, double, double, double)
	 */
	public ArcToVisitor(Vector2 point0, Vector2 point1, Vector2 point2, 
			double radius) {
		this(point0.getX(), point0.getY(), point1.getX(), point1.getY(), 
				point2.getX(), point2.getY(), radius);
	}
	
	/**
	 * @see #ArcToVisitor(double, double, double, double, double)
	 */
	public ArcToVisitor(Vector2 point1, Vector2 point2, double radius) {
		this(point1.getX(), point1.getY(), point2.getX(), point2.getY(), radius);
	}
	
	@Override
	public void visit(Surface surface) {
		if (!connectFromPrev) {
			surface.getContext().moveTo(x0, y0);
		}
		surface.getContext().arcTo(x1, y1, x2, y2, radius);
	}
}
