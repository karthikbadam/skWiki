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
 * Adds the given point to the current subpath, connected to the previous one 
 * by a straight line.
 * The new position will be set to the given position.
 * 
 * @author hao1300@gmail.com
 */
public class LineToVisitor implements ShapeVisitor {
	private final double x, y;
	
	/**
	 * Adds a straight line to the given position.
	 */
	public LineToVisitor(Vector2 position) {
		this(position.getX(), position.getY());
	}
	
	/**
	 * Adds a straight line to (x, y).
	 */
	public LineToVisitor(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void visit(Surface surface) {
		surface.getContext().lineTo(x, y);
	}
}
