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
import gwt.g2d.client.math.Circle;
import gwt.g2d.client.math.MathHelper;
import gwt.g2d.shared.math.Vector2;

/**
 * Draws a circle to the current subpath.
 * The new position will be set to the location on the arc at 0 degree.
 * 
 * @author hao1300@gmail.com
 */
public class CircleVisitor implements ShapeVisitor {
	private final double x, y, radius;
	
	public CircleVisitor(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public CircleVisitor(Vector2 center, double radius) {
		this(center.getX(), center.getY(), radius);
	}
	
	public CircleVisitor(Circle circle) {
		this(circle.getCenterX(), circle.getCenterY(), circle.getRadius());
	}
	
	@Override
	public void visit(Surface surface) {
		Context2d context = surface.getContext();
		context.arc(x, y, radius, 0, MathHelper.TWO_PI, true);
	}
}
