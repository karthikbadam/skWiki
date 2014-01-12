/*
 * Copyright 2011 Hao Nguyen
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

import gwt.g2d.client.graphics.DashedLineRenderer;
import gwt.g2d.client.graphics.Surface;
import gwt.g2d.shared.math.Vector2;

/**
 * Adds a dashed line segment to the current subpath.
 * The new position will be set to the end point of the line.
 * 
 * @author hao1300@gmail.com
 */
public class DashedLineVisitor implements ShapeVisitor {
	private final double fromX, fromY, toX, toY;
	private final double dashLength, gapLength;
	
	/**
	 * Draw a dashed line from fromPosition to toPosition.
	 * 
	 * @param context
	 * @param fromPosition the starting point
	 * @param toPosition ending point
	 * @param dashLength length of the dash
	 * @param gapLength length of the gap in between dashes
	 */
	public DashedLineVisitor(Vector2 fromPosition, Vector2 toPosition,
			double dashLength, double gapLength) {
		this(fromPosition.getX(), fromPosition.getY(), toPosition.getX(), 
				toPosition.getY(), dashLength, gapLength);		
	}
	
	/**
	 * Draw a dashed line from (fromX, fromY) to (toX, toY).
	 * 
	 * @param context
	 * @param fromX x-coordinate of the starting point
	 * @param fromY y-coordinate of the starting point
	 * @param toX x-coordinate of the ending point
	 * @param toY y-coordinate of the ending point
	 * @param dashLength length of the dash
	 * @param gapLength length of the gap in between dashes
	 */
	public DashedLineVisitor(double fromX, double fromY, double toX, double toY,
			double dashLength, double gapLength) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
		this.dashLength = dashLength;
		this.gapLength = gapLength;
	}
	
	@Override
	public void visit(Surface surface) {
		DashedLineRenderer.drawDashedLine(surface.getContext(), fromX, fromY, 
				toX, toY, dashLength, gapLength);
	}
}
