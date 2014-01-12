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

import com.google.gwt.event.shared.HandlerRegistration;

import gwt.g2d.client.graphics.Surface;

/**
 * Represents an abstract shape.
 * A custom shape can be created using {@link ShapeBuilder}.
 * 
 * @author hao1300@gmail.com
 */
public abstract class Shape {
	
	/**
	 * Click handler for the Shape class.
	 * @author Karel
	 *
	 */
	public interface ClickHandler {
		public void onClick(double x, double y);
	}
	

	/**
	 * Draws the shape onto the given surface.
	 * 
	 * @param surface the surface to draw the shape to.
	 */
	public abstract void draw(Surface surface);
	
	
	/**
	 * Does this shape's path contain the following point?
	 */
	public void checkHit(Surface surface, double x, double y) {
		
		// draw the shape invisibly, to check for a hit on the path
		double gAlpha = surface.getGlobalAlpha();
		surface.setGlobalAlpha(0.0);
		draw(surface);
		surface.setGlobalAlpha(gAlpha);
		
		// we got a hit!
		if (surface.isPointInPath(x, y)) {
			fClickHandler.onClick(x,  y);
		}
	}
	
	
	/**
	 * Click handler
	 */
	ClickHandler fClickHandler = null;
	
	
	/**
	 * Register a click handler.
	 */
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		fClickHandler = handler;
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				fClickHandler = null;
			}
		};
	}
}
