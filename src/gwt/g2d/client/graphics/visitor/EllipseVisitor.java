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
import gwt.g2d.client.math.Ellipse;
import gwt.g2d.client.math.MathHelper;
import gwt.g2d.shared.math.Vector2;

/**
 * Adds a new ellipse to the subpath.
 * The new position will be set to the location on the arc at 0 degree.
 * 
 * @author hao1300@gmail.com
 */
public class EllipseVisitor implements ShapeVisitor {
	private final double x, y, width, height;
	
	/**
	 * Adds an ellipse with its top left at (x, y) and with the given width and 
	 * height.
	 */
	public EllipseVisitor(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public EllipseVisitor(Vector2 position, double width, double height) {
		this(position.getX(), position.getY(), width, height);
	}
	
	public EllipseVisitor(Ellipse ellipse) {
		this(ellipse.getX(), ellipse.getY(), ellipse.getWidth(), ellipse.getHeight());
	}
	
	@Override
	public void visit(Surface surface) {
		Context2d context = surface.getContext();
		context.save();
		context.translate(x + width / 2, y + height / 2);
		context.scale(width / 2, height / 2);
		context.arc(0, 0, 1, 0, MathHelper.TWO_PI, true);
		context.restore();
	}
}
