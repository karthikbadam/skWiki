/*
 * Copyright 2010 Hao Nguyen
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
package gwt.g2d.client.math;

import gwt.g2d.shared.math.Vector2;

import java.io.Serializable;


/**
 * Represents a 2D arc.
 * 
 * @author hao1300@gmail.com
 */
public class Ellipse implements Serializable {	
	private static final long serialVersionUID = -5155909412608761628L;
	private final double x, y, width, height;
	
	/**
	 * Create an ellipse with its top left at (x, y) and with the given width and 
	 * height.
	 */
	public Ellipse(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Ellipse(Vector2 topLeftPosition, double width, double height) {
		this(topLeftPosition.getX(), topLeftPosition.getY(), width, height);
	}
	
	public Ellipse(Ellipse ellipse) {
		this(ellipse.getX(), ellipse.getY(), ellipse.getWidth(), ellipse.getHeight());
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
