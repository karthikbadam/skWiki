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
import gwt.g2d.shared.math.Rectangle;
import gwt.g2d.shared.math.Vector2;

/**
 * Adds a new Rectangle to the subpath.
 * The new position will be set to the top left corner of the rectangle.
 * 
 * @author hao1300@gmail.com
 */
public class RectangleVisitor implements ShapeVisitor {
	private final double x, y, width, height;
	
	public RectangleVisitor(Rectangle rectangle) {
		this(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	public RectangleVisitor(Vector2 position, double width, double height) {
		this(position.getX(), position.getY(), width, height);
	}
	
	/**
	 * Draws a rectangle to the subpath.
	 * 
	 * @param x left of the rectangle
	 * @param y top of the rectangle
	 * @param width
	 * @param height
	 */
	public RectangleVisitor(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void visit(Surface surface) {
		Context2d context = surface.getContext();
		context.moveTo(x, y);
		context.lineTo(x + width, y);
		context.lineTo(x + width, y + height);
		context.lineTo(x, y + height);
		context.lineTo(x, y);
	}
}
