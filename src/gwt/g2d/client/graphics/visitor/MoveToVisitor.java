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
 * Creates a new subpath with the given point.
 * The new position will be set to the given position.
 * 
 * @author hao1300@gmail.com
 */
public class MoveToVisitor implements ShapeVisitor {
	private final double x, y;
	
	/**
	 * Creates a new subpath at the given position.
	 */
	public MoveToVisitor(Vector2 position) {
		this(position.getX(), position.getY());
	}

	/**
	 * Creates a new subpath at (x, y).
	 */
	public MoveToVisitor(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void visit(Surface surface) {
		surface.getContext().moveTo(x, y);
	}
}
