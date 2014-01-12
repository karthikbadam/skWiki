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
import gwt.g2d.client.math.Matrix;

/**
 * Sets the current transformation to be the given transformation matrix.
 * 
 * @author hao1300@gmail.com
 */
public class SetTransformVisitor implements ShapeVisitor {
	private final double m11, m12, m21, m22, dx, dy;
	
	/**
	 * Sets the current transformation to be the given transformation matrix.
	 * 
	 * @param m11
	 * @param m12
	 * @param m21
	 * @param m22
	 * @param dx
	 * @param dy
	 */
	public SetTransformVisitor(double m11, double m12, double m21, double m22,
      double dx, double dy) {
		this.m11 = m11;
		this.m12 = m12;
		this.m21 = m21;
		this.m22 = m22;
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * Sets the current transformation to be the given transformation matrix.
	 * 
	 * @param matrix
	 */
	public SetTransformVisitor(Matrix matrix) {
		this(matrix.getM11(), matrix.getM12(), matrix.getM21(), matrix.getM22(),
				matrix.getDx(), matrix.getDy());
	}
		
	@Override
	public void visit(Surface surface) {
		surface.getContext().setTransform(m11, m12, m21, m22, dx, dy);
	}
}
