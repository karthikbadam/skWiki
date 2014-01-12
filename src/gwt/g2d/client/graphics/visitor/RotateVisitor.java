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

/**
 * Rotates clockwise by an angle in radian.
 * 
 * @author hao1300@gmail.com
 */
public class RotateVisitor implements ShapeVisitor {
	private final double angle;
	
	/**
	 * Rotates clockwise by the given angle in radian.
	 */
	public RotateVisitor(double angle) {
		this.angle = angle;
	}
	
	@Override
	public void visit(Surface surface) {
		surface.getContext().rotate(angle);
	}
}
