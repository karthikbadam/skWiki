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

import java.util.ArrayList;
import java.util.List;

/**
 * A shape that is made up to one or more other shapes.
 * 
 * @author hao1300@gmail.com
 */
public abstract class CompositeShapeVisitor implements ShapeVisitor {
	private final List<ShapeVisitor> shapes;
	
	protected CompositeShapeVisitor() {
		shapes = new ArrayList<ShapeVisitor>();
	}
	
	/**
	 * Initializes a composite shape visitor that is expected to hold the
	 * given number of shapes.
	 * 
	 * @param numShapes the number of shapes that this is expected to be able to
	 * 			  store.
	 */
	protected CompositeShapeVisitor(int numShapes) {
		shapes = new ArrayList<ShapeVisitor>(numShapes);
	}
	
	/**
	 * Adds a shape.
	 * 
	 * @param shape the shape to be added.
	 */
	protected void add(ShapeVisitor shape) {
		shapes.add(shape);
	}
	
	/**
	 * Adds an array of shapes.
	 * 
	 * @param shapes the array of shapes to be added.
	 */
	protected void addAll(ShapeVisitor... shapes) {
		for (ShapeVisitor shape : shapes) {
			add(shape);
		}
	}
	
	/**
	 * Clears all shape visitors.
	 */
	protected void clear() {
		shapes.clear();
	}
	
	/**
	 * Gets the shape visitor at the given index.
	 * 
	 * @param index the index of the shape to look up.
	 * @return the shape at the given index.
	 */
	protected ShapeVisitor get(int index) {
		return shapes.get(index);
	}
	
	@Override
	public void visit(Surface surface) {
		for (ShapeVisitor shape : shapes) {
			shape.visit(surface);
		}
	}
}
