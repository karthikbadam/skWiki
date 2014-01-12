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
package gwt.g2d.client.graphics.shapes;

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.shared.math.Rectangle;

/**
 * Represents a rectangle shape.
 * 
 * @author hao1300@gmail.com
 */
public class RectangleShape extends Shape {
	private double x, y, width, height;
	
	public RectangleShape(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public RectangleShape(Rectangle rectangle) {
		this(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	@Override
	public final void draw(Surface surface) {
		surface.getContext().rect(x, y, width, height);
	}
}
