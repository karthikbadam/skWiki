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
package gwt.g2d.client.graphics;

import gwt.g2d.client.graphics.shapes.Shape;
import gwt.g2d.client.math.Matrix;
import gwt.g2d.shared.Color;
import gwt.g2d.shared.math.Rectangle;
import gwt.g2d.client.mouse.MouseSurface;
import gwt.g2d.client.mouse.SurfaceClickHandler;
import gwt.g2d.client.mouse.SurfaceMouseDragHandler;
import gwt.g2d.client.mouse.SurfaceMouseMoveHandler;
import gwt.g2d.client.mouse.SurfaceMouseOutHandler;
import gwt.g2d.client.mouse.SurfaceMouseOverHandler;
import gwt.g2d.shared.math.Vector2;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.CanvasPattern;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.Context2d.TextBaseline;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.GestureChangeHandler;
import com.google.gwt.event.dom.client.GestureEndHandler;
import com.google.gwt.event.dom.client.GestureStartHandler;
import com.google.gwt.event.dom.client.HasAllDragAndDropHandlers;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllGestureHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasDragEndHandlers;
import com.google.gwt.event.dom.client.HasDragEnterHandlers;
import com.google.gwt.event.dom.client.HasDragHandlers;
import com.google.gwt.event.dom.client.HasDragLeaveHandlers;
import com.google.gwt.event.dom.client.HasDragOverHandlers;
import com.google.gwt.event.dom.client.HasDragStartHandlers;
import com.google.gwt.event.dom.client.HasDropHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasGestureChangeHandlers;
import com.google.gwt.event.dom.client.HasGestureEndHandlers;
import com.google.gwt.event.dom.client.HasGestureStartHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.HasTouchCancelHandlers;
import com.google.gwt.event.dom.client.HasTouchEndHandlers;
import com.google.gwt.event.dom.client.HasTouchMoveHandlers;
import com.google.gwt.event.dom.client.HasTouchStartHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.Composite;

/**
 * The surface that an application uses to render to the screen.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.g2d-Surface { }</li>
 * </ul>
 * 
 * @see <a href="http://dev.w3.org/html5/spec/Overview.html#the-canvas-element">
 * http://dev.w3.org/html5/spec/Overview.html#the-canvas-element</a>
 * 
 * @author hao1300@gmail.com
 */
public class Surface extends Composite implements HasAllDragAndDropHandlers, HasAllFocusHandlers, HasAllGestureHandlers, HasAllKeyHandlers, HasAllMouseHandlers, HasAllTouchHandlers, HasBlurHandlers, HasClickHandlers, HasDoubleClickHandlers, HasDragEndHandlers, HasDragEnterHandlers, HasDragHandlers, HasDragLeaveHandlers, HasDragOverHandlers, HasDragStartHandlers, HasDropHandlers, HasFocusHandlers, HasGestureChangeHandlers, HasGestureEndHandlers, HasGestureStartHandlers, HasKeyDownHandlers, HasKeyPressHandlers, HasKeyUpHandlers, HasMouseDownHandlers, HasMouseMoveHandlers, HasMouseOutHandlers, HasMouseOverHandlers, HasMouseUpHandlers, HasMouseWheelHandlers, HasTouchCancelHandlers, HasTouchEndHandlers, HasTouchMoveHandlers, HasTouchStartHandlers, HasAttachHandlers, HasHandlers {
	
	// canvas information
	private Canvas canvas;
	private Context2d context;
	
	// registered shapes for the previous draw 
	/*Vector<Shape> fRegisteredShapes = new Vector<Shape>();
	
	// free shape indices
	LinkedList<Integer> fAvailableShapeIndices = new LinkedList<Integer>();
	*/
	// mouse surface
	public MouseSurface fMouseSurface = null;
	
	
	/**
	 * Initialize a surface with a default size of 100 by 100.
	 */
	public Surface() {
		this(100, 100);
	}
	
	/**
	 * Initialize a surface of the given size.
	 * 
	 * @param width width of the surface.
	 * @param height height of the surface.
	 */
	public Surface(int width, int height) {
		
		// create the canvas
		canvas = Canvas.createIfSupported();
		canvas.setWidth(width + "px");
		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		context = canvas.getContext2d();
		initWidget(canvas);
		
		// register an onClick event that is forwarded to registered shapes
		/*addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				processClick(event.getX(), event.getY());
			}
			
		});*/
	}
	
	/**
	 * Initialize a surface of the given size.
	 * 
	 * @param size the size of the surface to initialize.
	 */
	public Surface(Vector2 size) {
		this(size.getIntX(), size.getIntY());
	}
	
	/**
	 * Gets the size of the surface.
	 */
	public Vector2 getSize() {
		return new Vector2(canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
	}
	
	/**
	 * Change the size of the surface.
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height) {
		canvas.setWidth(width + "px");
		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
	}
	
	/**
	 * Gets the width of the surface.
	 */
	public int getCoordinateSpaceWidth() {
		return canvas.getCoordinateSpaceWidth();
	}
	
	/**
	 * Gets the height of the surface.
	 */
	public int getCoordinateSpaceHeight() {
		return canvas.getCoordinateSpaceHeight();
	}
	
	
	/**
	 * Sets the width of the surface.
	 */
	public void setCoordinateSpaceWidth(int width) {
		canvas.setCoordinateSpaceWidth(width);
	}
	
	/**
	 * Sets the height of the surface.
	 */
	public void setCoordinateSpaceHeight(int height) {
		canvas.setCoordinateSpaceHeight(height);
	}
	
	/**
	 * Gets the rectangle that encloses this surface.
	 */
	public Rectangle getViewRectangle() {
		return new Rectangle(0, 0, getCoordinateSpaceWidth(), getCoordinateSpaceHeight());
	}

	/**
	 * Gets the canvas.
	 * 
	 * @return the underlying canvas.
	 */
	public Canvas getCanvas() {
		return canvas;
	}
	
	/**
	 * Gets the canvas element.
	 * 
	 * @return the underlying canvas element.
	 */
	public CanvasElement getCanvasElement() {
		return canvas.getCanvasElement();
	}
	
	/**
	 * Gets the context 2D.
	 * 
	 * @return the underlying context implementation for drawing onto the canvas.
	 */
	public Context2d getContext() {
		return context;
	}
	
	/**
	 * Pushes the current state onto the stack.
	 * 
	 * Drawing states consist of:
	 * <ul>
	 * <li>The current transformation matrix.</li>
	 * <li>The current clipping region.</li>
	 * <li>The current values of the following attributes: strokeStyle, fillStyle, 
	 * globalAlpha, lineWidth, lineCap, lineJoin, miterLimit, shadowOffsetX, 
	 * shadowOffsetY, shadowBlur, shadowColor, globalCompositeOperation, font, 
	 * textAlign, textBaseline.</li>
	 * </ul>
	 * 
	 * Note: The current path and the current bitmap are not part of the drawing 
	 * state.
	 * 
	 * @return self to support chaining.
	 */
	public Surface save() {
		context.save();
		return this;
	}
	
	/**
	 * Pops the top state on the stack, restoring the context to that state.
	 * Sees {@link #save()} method on what can being saved and restored.
	 * 
	 * @return self to support chaining.
	 */
	public Surface restore() {
		context.restore();
		return this;
	}
	
	/**
	 * Scales by (x, y) units.
	 * 
	 * @param x
	 * @param y
	 * @return self to support chaining.
	 */
	public Surface scale(double x, double y) {
		context.scale(x, y);
		return this;
	}
	
	/**
	 * Scales by (scales.x, scales.y) units.
	 * 
	 * @param scales
	 * @return self to support chaining.
	 */
	public Surface scale(Vector2 scales) {
		return scale(scales.getX(), scales.getY());
	}
	
	/**
	 * Scales uniformly by scale units.
	 * 
	 * @param scale
	 * @return self to support chaining.
	 */
	public Surface scale(double scale) {
		return scale(scale, scale);
	}
	
	/**
	 * Rotates clockwise by the given angle about the origin.
	 * 
	 * @param angle
	 * @return self to support chaining.
	 */
	public Surface rotate(double angle) {
		context.rotate(angle);
		return this;
	}
	
	/**
	 * Rotates counter-clockwise by the given angle about the origin.
	 * 
	 * @param angle
	 * @return self to support chaining.
	 */
	public Surface rotateCcw(double angle) {
		return rotate(-angle);
	}
	
	/**
	 * Translates the origin of the surface by (x, y) units.
	 * 
	 * @param x
	 * @param y
	 * @return self to support chaining.
	 */
	public Surface translate(double x, double y) {
		context.translate(x, y);
		return this;
	}
	
	/**
	 * Translates the origin of the surface by (x, y) units.
	 * 
	 * @param translation
	 * @return self to support chaining.
	 */
	public Surface translate(Vector2 translation) {
		return translate(translation.getX(), translation.getY());
	}
	
	/**
	 * Multiply the current transformation by the given transformation matrix.
	 * 
	 * The matrix has the following structure:
	 * <pre> 
   * m11 m21 dx
   * m12 m22 dy
   *  0   0   1 
   * </pre>
   * 
	 * @param m11 
	 * @param m12
	 * @param m21
	 * @param m22
	 * @param dx
	 * @param dy
	 * @return self to support chaining.
	 */
	public Surface transform(double m11, double m12, double m21, double m22,
      double dx, double dy) {
		context.transform(m11, m12, m21, m22, dx, dy);
		return this;
	}
	
	/**
	 * Multiply the current transformation by the given transformation matrix.
   * 
	 * @param matrix
	 * @return self to support chaining.
	 */
	public Surface transform(Matrix matrix) {
		return transform(matrix.getM11(), matrix.getM12(), matrix.getM21(), 
				matrix.getM22(), matrix.getDx(), matrix.getDy());
	}
	
	/**
	 * Sets the current transformation to be the given transformation matrix.
	 * 
	 * The matrix has the following structure:
	 * <pre> 
   * m11 m21 dx
   * m12 m22 dy
   *  0   0   1 
   * </pre>
   * 
	 * @param m11 
	 * @param m12
	 * @param m21
	 * @param m22
	 * @param dx
	 * @param dy
	 * @return self to support chaining.
	 */
	public Surface setTransform(double m11, double m12, double m21, double m22,
      double dx, double dy) {
		context.setTransform(m11, m12, m21, m22, dx, dy);
		return this;
	}
	
	/**
	 * Sets the current transformation to be the given transformation matrix.
   * 
	 * @param matrix
	 * @return self to support chaining.
	 */
	public Surface setTransform(Matrix matrix) {
		return setTransform(matrix.getM11(), matrix.getM12(), matrix.getM21(), 
				matrix.getM22(), matrix.getDx(), matrix.getDy());
	}
	
	/**
	 * Sets the current alpha value applied to rendering operations.
	 * Default: 1.0.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setGlobalAlpha(double alpha) {
		context.setGlobalAlpha(alpha);
		return this;
	}
	
	/**
	 * Gets the current alpha value applied to rendering operations.
	 */
	public double getGlobalAlpha() {
		return context.getGlobalAlpha();
	}
	
	/**
	 * Sets the current composition operation.
	 * Default: {@link Composition#SOURCE_OVER}.
	 * 
	 * @param compositeOperation
	 * @return self to support chaining.
	 */
	public Surface setGlobalCompositeOperation(Context2d.Composite compositeOperation) {
		context.setGlobalCompositeOperation(compositeOperation);
		return this;
	}
	
	/**
	 * Gets the current composition operation.
	 */
	public Context2d.Composite getGlobalCompositeOperation() {
		return Context2d.Composite.valueOf(context.getGlobalCompositeOperation());
	}
	
	/**
	 * Sets the fill style.
	 * 
	 * @param color the color for the fill style.
	 * @return self to support chaining.
	 */
	public Surface setFillStyle(Color color) {
		context.setFillStyle(color.getColorCode());
		return this;
	}
	
	/**
	 * Sets the fill style.
	 * 
	 * @param gradient the gradient for the fill style.
	 * @return self to support chaining.
	 */
	public Surface setFillStyle(CanvasGradient gradient) {
		context.setFillStyle(gradient);
		return this;
	}
	
	/**
	 * Sets the fill style.
	 * 
	 * @param pattern the pattern for the fill style.
	 * @return self to support chaining.
	 */
	public Surface setFillStyle(CanvasPattern pattern) {
		context.setFillStyle(pattern);
		return this;
	}
	
	/**
	 * Sets the stroke style.
	 */
	public Surface setStrokeStyle(Color color) {
		context.setStrokeStyle(color.getColorCode());
		return this;
	}
	
	/**
	 * Sets the stroke style.
	 */
	public Surface setStrokeStyle(CanvasGradient gradient) {
		context.setStrokeStyle(gradient);
		return this;
	}
	
	/**
	 * Sets the stroke style.
	 */
	public Surface setStrokeStyle(CanvasPattern pattern) {
		context.setStrokeStyle(pattern);
		return this;
	}
	
	/**
	 * Create a linear gradient.
	 */
	public CanvasGradient createLinearGradient(Vector2 start, Vector2 stop) {
		return context.createLinearGradient(start.x, start.y, stop.x, stop.y);
	}
	
	
	/**
	 * Create a linear gradient.
	 */
	public CanvasGradient createLinearGradient(double x1, double y1, double x2, double y2) {
		return context.createLinearGradient(x1, y1, x2, y2);
	}
	
	
	/**
	 * Create a radial gradient.
	 */
	public CanvasGradient createRadialGradient(Vector2 start, double startRadius, Vector2 stop, double stopRadius) {
		return context.createRadialGradient(start.x, start.y, startRadius, stop.x, stop.y, stopRadius);
	}
	
	
	/**
	 * Create a radial gradient.
	 */
	public CanvasGradient createRadialGradient(double x1, double y1, double r1, double x2, double y2, double r2) {
		return context.createRadialGradient(x1, y1, r1, x2, y2, r2);
	}
	
	
	/**
	 * Add a color stop to a gradient - convenient helper function so Color can be used.
	 */
	public static void addColorStop(CanvasGradient gradient, double offset, Color color) {
		gradient.addColorStop(offset, "rgba(" + color.red + "," + color.green + "," + color.blue + "," + color.alpha + ")");
	}
	
	/**
	 * Sets the width of lines, in coordinate space units. On setting, zero, 
	 * negative, infinite, and NaN values must be ignored, leaving the value 
	 * unchanged.
	 * 
	 * Default: 1.0.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setLineWidth(double lineWidth) {
		context.setLineWidth(lineWidth);		
		return this;
	}
	
	/**
	 * Gets the width of lines, in coordinate space units.
	 */
	public double getLineWidth() {
		return context.getLineWidth();
	}
	
	/**
	 * Sets the type of endings that UAs will place on the end of lines.
	 * 
	 * Default: {@link LineCap#BUTT}.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setLineCap(LineCap lineCap) {
		context.setLineCap(lineCap);
		return this;
	}
	
	/**
	 * Gets the type of endings that UAs will place on the end of lines.
	 */
	public LineCap getLineCap() {
		return LineCap.valueOf(context.getLineCap());
	}
	
	/**
	 * Sets the type of corners that UAs will place where two lines meet.
	 * 
	 * Default: {@link LineJoin#MITER}.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setLineJoin(LineJoin lineJoin) {
		context.setLineJoin(lineJoin);
		return this;
	}
	
	/**
	 * Gets the type of corners that UAs will place where two lines meet.
	 */
	public LineJoin getLineJoin() {
		return LineJoin.valueOf(context.getLineJoin());
	}
	
	/**
	 * Sets the current miter limit ratio.
	 * 
	 * The miter length is the distance from the point where the join occurs to 
	 * the intersection of the line edges on the outside of the join. The miter 
	 * limit ratio is the maximum allowed ratio of the miter length to half the 
	 * line width. If the miter length would cause the miter limit ratio to be 
	 * exceeded, this second triangle must not be rendered.
	 * 
	 * The miter limit ratio can be explicitly set using the miterLimit attribute. 
	 * On setting, zero, negative, infinite, and NaN values must be ignored, 
	 * leaving the value unchanged.
	 * 
	 * Default: 10.0.
	 * 
	 * @return self to support chaining.
	 */
	public Surface setMiterLimit(double miterLimit) {
		context.setMiterLimit(miterLimit);
		return this;
	}
	
	/**
	 * Gets the current miter limit ratio.
	 */
	public double getMiterLimit() {
		return context.getMiterLimit();
	}
	
	/**
	 * Clears all pixels on the surface in the given rectangle to transparent 
	 * black.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface clearRectangle(double x, double y, double width, double height) {
		context.clearRect(x, y, width, height);
		return this;
	}
	
	/**
	 * Clears all pixels on the surface in the given rectangle to transparent 
	 * black.
	 * 
	 * @param rectangle
	 * @return self to support chaining.
	 */
	public Surface clearRectangle(Rectangle rectangle) {
		return clearRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	/**
	 * Paints the specified rectangular area using the fillStyle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface fillRectangle(double x, double y, double width, double height) {
		context.fillRect(x, y, width, height);
		return this;
	}
	
	/**
	 * Paints the specified rectangular area using the fillStyle.
	 * 
	 * @param rectangle
	 * @return self to support chaining.
	 */
	public Surface fillRectangle(Rectangle rectangle) {
		return fillRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	/**
	 * Strokes the specified rectangle's path using the strokeStyle, lineWidth, 
	 * lineJoin, and (if appropriate) miterLimit attributes
	 */
	public Surface strokeRectangle(double x, double y, double width, double height) {
		context.strokeRect(x, y, width, height);
		return this;
	}
	
	/**
	 * Strokes the specified rectangle's path using the strokeStyle, lineWidth, 
	 * lineJoin, and (if appropriate) miterLimit attributes
	 */
	public Surface strokeRectangle(Rectangle rectangle) {
		return strokeRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	/**
	 * Create a new clipping region by calculating the intersection of the 
	 * current clipping region and the area described by the rectangle, using 
	 * the non-zero winding number rule.
	 */
	public Surface clipRectangle(double x, double y, double width, double height) {
		context.rect(x, y, width, height);
		context.clip();
		return this;
	}
	
	/**
	 * Create a new clipping region by calculating the intersection of the 
	 * current clipping region and the area described by the rectangle, using 
	 * the non-zero winding number rule.
	 */
	public Surface clipRectangle(Rectangle rectangle) {
		return clipRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	
	/**
	 * Clears all pixels on the surface in the given rectangle to transparent 
	 * black.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface clearRectangleFromTo(double x1, double y1, double x2, double y2) {
		context.clearRect(x1, y1, x2-x1, y2-y1);
		return this;
	}
	
	
	/**
	 * Paints the specified rectangular area using the fillStyle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface fillRectangleFromTo(double x1, double y1, double x2, double y2) {
		context.fillRect(x1, y1, x2-x1, y2-y1);
		return this;
	}
	
	
	/**
	 * Strokes the specified rectangle's path using the strokeStyle, lineWidth, 
	 * lineJoin, and (if appropriate) miterLimit attributes
	 */
	public Surface strokeRectangleFromTo(double x1, double y1, double x2, double y2) {
		context.strokeRect(x1, y1, x2-x1, y2-y1);
		return this;
	}
	
	
	/**
	 * Create a new clipping region by calculating the intersection of the 
	 * current clipping region and the area described by the rectangle, using 
	 * the non-zero winding number rule.
	 */
	public Surface clipRectangleFromTo(double x1, double y1, double x2, double y2) {
		context.rect(x1, y1, x2-x1, y2-y1);
		context.clip();
		return this;
	}
	
	
	/**
	 * Fills the specified shape using the fillStyle.
	 */
	public Surface fillShape(Shape shape) {
		shape.draw(this);
		context.fill();
		return this;
	}
	
	/**
	 * Strokes the specified shape path using the strokeStyle, lineWidth, 
	 * lineJoin, and (if appropriate) miterLimit attributes
	 */
	public Surface strokeShape(Shape shape) {
		shape.draw(this);
		context.stroke();
		return this;
	}
	
	/**
	 * Create a new clipping region by calculating the intersection of the 
	 * current clipping region and the area described by the given shape, using 
	 * the non-zero winding number rule.
	 */
	public Surface clipShape(Shape shape) {
		shape.draw(this);
		context.clip();
		return this;
	}
	
	
	/**
	 * Enable click registering on arbitrary shapes.
	 */
	public void enableMouseRegistration() {
		
		// already done - only do it once
		if (fMouseSurface != null) return;
		
		// create the mouse surface
		fMouseSurface = new MouseSurface(this);
		
		// register click & mouse handlers
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fMouseSurface.onClick(event.getX(), event.getY());
			}
		});
		addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				fMouseSurface.onMouseDown(event.getX(), event.getY());
			}
			
		});
		addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				fMouseSurface.onMouseUp(event.getX(), event.getY());
			}
			
		});
		addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				fMouseSurface.onMouseOver(event.getX(), event.getY());
			}
		});
		addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				fMouseSurface.onMouseOut(event.getX(), event.getY());
			}
		});
		addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				fMouseSurface.onMouseMove(event.getX(), event.getY());
			}
		});
	}
	
	
	/**
	 * When this function is called, all subsequent draws (rectangles, shapes, images, ...) will
	 * be registered to the same object, until stopMouseRegistration is called. Any handlers added
	 * between startMouseRegistration and stopMouseRegistration will be associated with these draws.
	 */
	public void startMouseRegistration(Long id) {
		if (fMouseSurface == null) return;
		fMouseSurface.startRegister(id);
	}
	
	
	/**
	 * Stop registering draws for mouse detection. Subsequent calls to draws will have no further effect.
	 * @param draw Copy all drawn data to the surface or don't copy it.
	 */
	public void stopMouseRegistration(boolean draw) {
		if (fMouseSurface == null) return;
		fMouseSurface.stopRegister(draw);
	}
	
	
	/**
	 * Stop registering draws for mouse detection. Subsequent calls to draws will have no further effect.
	 * Copies everything that was drawn between start and stop to the surface.
	 */
	public void stopMouseRegistration() {
		stopMouseRegistration(true);
	}
	
	
	/**
	 * Add click handler to the currently open mouse registration.
	 */
	public void addMouseClickHandler(SurfaceClickHandler handler) {
		if (fMouseSurface == null || !fMouseSurface.isActive()) return;
		fMouseSurface.addClickHandler(handler);
	}
	
	
	/**
	 * Add mouse drag handler to the surface. Applies to every mouse registration, as it allows
	 * dragging from and to different registrations.
	 */
	public void addMouseDragHandler(SurfaceMouseDragHandler handler) {
		if (fMouseSurface == null) return;
		fMouseSurface.addMouseDragHandler(handler);
	}
	
	
	/**
	 * Add click handler to the currently open mouse registration.
	 */
	public void addMouseOverHandler(SurfaceMouseOverHandler handler) {
		if (fMouseSurface == null || !fMouseSurface.isActive()) return;
		fMouseSurface.addMouseOverHandler(handler);
	}
	
	
	/**
	 * Add click handler to the currently open mouse registration.
	 */
	public void addMouseOutHandler(SurfaceMouseOutHandler handler) {
		if (fMouseSurface == null || !fMouseSurface.isActive()) return;
		fMouseSurface.addMouseOutHandler(handler);
	}
	
	
	/**
	 * Add click handler to the currently open mouse registration.
	 */
	public void addMouseMoveHandler(SurfaceMouseMoveHandler handler) {
		if (fMouseSurface == null || !fMouseSurface.isActive()) return;
		fMouseSurface.addMouseMoveHandler(handler);
	}
	
	
	/**
	 * Switch the context temporarily with a different one, so that this context can
	 * be used to capture draws for mouse registration.
	 */
	public void replaceContext(Context2d ctx) {
		this.context = ctx;
	}
	
	
	/**
	 * Register a shape for event handling.
	 */
	/*public ShapeRegistration registerShape(Shape shape) {
		
		// index of the shape - for O(1) removal later
		final int idx;
		
		// are there available indices?
		if (fAvailableShapeIndices.size() > 0) {
			idx = fAvailableShapeIndices.get(0);
			fAvailableShapeIndices.removeFirst();
			fRegisteredShapes.set(idx,  shape);
		}
		else {
			fRegisteredShapes.add(shape);
			idx = fRegisteredShapes.size()-1;
		}
		
		// return the shape registration
		return new ShapeRegistration() {

			@Override
			public void removeShape() {
				fRegisteredShapes.set(idx,  null);
				fAvailableShapeIndices.add(idx);
			}
		};
	}*/
	
	
	/**
	 * Process a click event, and forward it to all shapes that are hit.
	 */
	/*private void processClick(double x, double y) {
		for (Shape shape : fRegisteredShapes) {
			if (shape != null) shape.checkHit(this,  x, y);
		}
	}*/
	
	
	
	/**
	 * Is this point in the path that was just drawn?
	 */
	public boolean isPointInPath(double x, double y) {
		return context.isPointInPath(x,  y);
	}
	
	/**
	 * Fills the background with the given color.
	 */
	public Surface fillBackground(Color color) {
		return setFillStyle(color).fillRectangle(getViewRectangle());
	}
	
	/**
	 * Fills the background with the given gradient.
	 */
	public Surface fillBackground(CanvasGradient gradient) {
		return setFillStyle(gradient).fillRectangle(getViewRectangle());
	}
	
	/**
	 * Clears all pixels to the surface to transparent black.
	 * 
	 * @return self to support chaining.
	 */
	public Surface clear() {
		context.clearRect(0.0, 0.0, getCoordinateSpaceWidth(), getCoordinateSpaceHeight());
		return this;
	}
	
	/**
	 * This is equivalent to calling drawFocusRing(widget, x, y, false).
	 * @see #drawFocusRing(Widget, double, double)
	 */
	/*public boolean drawFocusRing(Widget widget, double x, double y) {
		return context.drawFocusRing(widget.getElement(), x, y);
	}*/
	
	/**
	 * Use this method to indicate which focusable part of the canvas is 
	 * currently focused, passing it the widget for which a ring is being drawn. 
	 * This method only draws the focus ring if the widget is focused, so that 
	 * it can simply be called whenever drawing the widget, without checking 
	 * whether the widget is focused or not first. The position of the center 
	 * of the control, or of the editing caret if the control has one, should be 
	 * given in the x and y arguments.
	 * 
	 * WARNING: This method may not be implemented in most browsers yet!
	 * 
 	 * @param canDrawCustom if true, then the focus ring is only drawn if the 
 	 * 				user has configured his system to draw focus rings in a particular 
	 * 				manner. (For example, high contrast focus rings.)
	 */
	/*public boolean drawFocusRing(Widget widget, double x, double y, 
			boolean canDrawCustom) {
		return context.drawFocusRing(widget.getElement(), x, y, canDrawCustom);
	}*/
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, double x, double y) {
		context.drawImage(image, x, y);
		return this;
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param position the position to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, Vector2 position) {
		return drawImage(image, position.getX(), position.getY());
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, double x, double y, double width, 
			double height) {
		context.drawImage(image, x, y, width, height);
		return this;
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param rectangle the rectangle inside which the image is to be drawn.
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, Rectangle rectangle) {
		return drawImage(image, rectangle.getX(), rectangle.getY(), 
				rectangle.getWidth(), rectangle.getHeight());
	}
	
	/**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image image the image to draw.
	 * @param sourceX
	 * @param sourceY
	 * @param sourceWidth
	 * @param sourceHeight
	 * @param destinationX
	 * @param destinationY
	 * @param destinationWidth
	 * @param destinationHeight
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, double sourceX, double sourceY, 
			double sourceWidth, double sourceHeight, double destinationX, 
			double destinationY, double destinationWidth, double destinationHeight) {
		context.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight,
				destinationX, destinationY, destinationWidth, destinationHeight);
		return this;
	}
	
	/**
	 * 
	 *
	 * /**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image
	 * @param sourceRectangle
	 * @param destinationRectangle
	 * @return self to support chaining.
	 */
	public Surface drawImage(CanvasElement image, Rectangle sourceRectangle, 
			Rectangle destinationRectangle) {
		return drawImage(image, sourceRectangle.getX(), sourceRectangle.getY(),
				sourceRectangle.getWidth(), sourceRectangle.getHeight(),
				destinationRectangle.getX(), destinationRectangle.getY(), 
				destinationRectangle.getWidth(), destinationRectangle.getHeight());
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, double x, double y) {
		context.drawImage(image, x, y);
		return this;
	}
	
	/**
	 * Draws the image at the given position.
	 * 
	 * @param image the image to draw.
	 * @param position the position to draw the image.
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, Vector2 position) {
		return drawImage(image, position.getX(), position.getY());
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param x the x-coordinate to draw the image.
	 * @param y the y-coordinate to draw the image.
	 * @param width
	 * @param height
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, double x, double y, double width, 
			double height) {
		context.drawImage(image, x, y, width, height);
		return this;
	}
	
	/**
	 * Draws the image in the specified rectangle.
	 * 
	 * @param image the image to draw.
	 * @param rectangle the rectangle inside which the image is to be drawn.
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, Rectangle rectangle) {
		return drawImage(image, rectangle.getX(), rectangle.getY(), 
				rectangle.getWidth(), rectangle.getHeight());
	}
	
	/**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image image the image to draw.
	 * @param sourceX
	 * @param sourceY
	 * @param sourceWidth
	 * @param sourceHeight
	 * @param destinationX
	 * @param destinationY
	 * @param destinationWidth
	 * @param destinationHeight
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, double sourceX, double sourceY, 
			double sourceWidth, double sourceHeight, double destinationX, 
			double destinationY, double destinationWidth, double destinationHeight) {
		context.drawImage(image, sourceX, sourceY, sourceWidth, sourceHeight,
				destinationX, destinationY, destinationWidth, destinationHeight);
		return this;
	}
	
	/**
	 * /**
	 * Draws the portion of the image in the source rectangle into the 
	 * destination rectangle.
	 * 
	 * @param image
	 * @param sourceRectangle
	 * @param destinationRectangle
	 * @return self to support chaining.
	 */
	public Surface drawImage(ImageElement image, Rectangle sourceRectangle, 
			Rectangle destinationRectangle) {
		return drawImage(image, sourceRectangle.getX(), sourceRectangle.getY(),
				sourceRectangle.getWidth(), sourceRectangle.getHeight(),
				destinationRectangle.getX(), destinationRectangle.getY(), 
				destinationRectangle.getWidth(), destinationRectangle.getHeight());
	}
	
	/**
	 * Instantiate new blank ImageData objects whose dimension is equal to
	 * width x height.
	 * 
	 * @param width
	 * @param height
	 * @return a new ImageData object.
	 */
	public ImageData createImageData(int width, int height) {
		return new ImageData(context.createImageData(width, height));
	}
	
	/**
	 * Creates a CanvasPattern object that uses the given image and repeats in 
	 * the direction(s) given by the repetition argument.
	 * 
	 * @param image
	 * @param repetition
	 * @return a new CanvasPattern object.
	 */
	public CanvasPattern createPattern(CanvasElement image, 
			PatternRepetition repetition) {
		return context.createPattern(image, repetition.toString());
	}
	
	/**
	 * Creates a CanvasPattern object that uses the given image and repeats in 
	 * the direction(s) given by the repetition argument.
	 * 
	 * @param image
	 * @param repetition
	 * @return a new CanvasPattern object.
	 */
	public CanvasPattern createPattern(ImageElement image, 
			PatternRepetition repetition) {
		return context.createPattern(image, repetition.toString());
	}
	
	/**
	 * Instantiate new blank ImageData objects whose dimension is equal to
	 * the dimension given, where (x, y) represents (width, height).
	 * 
	 * @param dimension
	 * @return a new ImageData object.
	 */
	public ImageData createImageData(Vector2 dimension) {
		return createImageData(dimension.getIntX(), dimension.getIntY());
	}
	
	/**
	 * Instantiate new blank ImageData objects whose dimension is equal to
	 * the given imageData.
	 * 
	 * @param imageData
	 * @return a new ImageData object.
	 */
	public ImageData createImageData(ImageData imageData) {
		return new ImageData(context.createImageData(imageData.getGWTImageData()));
	}
	
	
	/**
	 * Returns an ImageData object representing the underlying pixel data for the 
	 * area of the context denoted by the given rectangle, in context coordinate 
	 * space units. Pixels outside the context must be returned as transparent 
	 * black. Pixels must be returned as non-premultiplied alpha values.
	 * 
	 * @param sx the x coordinate of the upper-left corner of the desired area
	 * @param sy the y coordinate of the upper-left corner of the desired area
	 * @param sw the width of the desired area
	 * @param sh the height of the desired area
	 */
	public ImageData getImageData(double sx, double sy, double sw, double sh) {
		return new ImageData(context.getImageData(sx, sy, sw, sh));
	}
	
	
	/**
	 * Returns an ImageData object representing the underlying pixel data for the 
	 * area of the context denoted by the given rectangle, in context coordinate 
	 * space units. Pixels outside the context must be returned as transparent 
	 * black. Pixels must be returned as non-premultiplied alpha values.
	 * 
	 * @param rect
	 */
	public ImageData getImageData(Rectangle rect) {
		return getImageData(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	
	/**
	 * <p>Paints the data from the given ImageData object onto the context.</p>
	 * <p>The globalAlpha and globalCompositeOperation attributes, as well as the 
	 * shadow attributes, are ignored for the purposes of this method call; 
	 * pixels in the context are replaced wholesale, with no composition, alpha 
	 * blending, no shadows, etc.</p>
	 * 
	 * @param imageData
	 * @param x
	 * @param y
	 */
	/*public void putImageData(ImageData imageData, double x, double y) {
		context.putImageData(imageData.getGWTImageData(), x, y);
	}*/
	
	/**
	 * <p>Paints the data from the given ImageData object onto the context.</p>
	 * <p>The globalAlpha and globalCompositeOperation attributes, as well as the 
	 * shadow attributes, are ignored for the purposes of this method call; 
	 * pixels in the context are replaced wholesale, with no composition, alpha 
	 * blending, no shadows, etc.</p>
	 * 
	 * @param imageData
	 * @param position
	 */
	/*public void putImageData(ImageData imageData, Vector2 position) {
		putImageData(imageData, position.getX(), position.getY());
	}*/
	
	/**
	 * Sets the font settings. The syntax is the same as for the CSS 'font' 
	 * property; values that cannot be parsed as CSS font values are ignored.
	 */
	public Surface setFont(String font) {
		context.setFont(font);
		return this;
	}
	
	/**
	 * Gets the font settings.
	 */
	public String getFont() {
		return context.getFont();
	}
	
	/**
	 * Sets the text alignment settings.
	 */
	public Surface setTextAlign(TextAlign textAlign) {
		context.setTextAlign(textAlign);
		return this;
	}
	
	/**
	 * Gets the text alignment settings.
	 */
	public TextAlign getTextAlign() {
		return TextAlign.valueOf(context.getTextAlign());
	}
	
	/**
	 * Sets the text baseline alignment settings.
	 */
	public Surface setTextBaseline(TextBaseline textBaseline) {
		context.setTextBaseline(textBaseline);
		return this;
	}
	
	/**
	 * Gets the text baseline alignment settings.
	 */
	public TextBaseline getTextBaseline() {
		return TextBaseline.valueOf(context.getTextBaseline());
	}
	
	/**
	 * Renders the given text at the given (x, y).
	 */
	public Surface fillText(String text, double x, double y) {
		context.fillText(text, x, y);
		return this;
	}
	
	/**
	 * Renders the given text at the given position.
	 */
	public Surface fillText(String text, Vector2 position) {
		return fillText(text, position.getX(), position.getY());
	}
	
	/**
	 * Renders the given text at the given (x, y), ensuring that the text is
	 * not wider than maxWidth.
	 */
	public Surface fillText(String text, double x, double y, double maxWidth) {
		context.fillText(text, x, y, maxWidth);
		return this;
	}
	
	/**
	 * Renders the given text at the given position, ensuring that the text is
	 * not wider than maxWidth.
	 */
	public Surface fillText(String text, Vector2 position, double maxWidth) {
		return fillText(text, position.getX(), position.getY(), maxWidth);
	}
	
	/**
	 * Renders the given text at the given (x, y).
	 */
	public Surface strokeText(String text, double x, double y) {
		context.strokeText(text, x, y);
		return this;
	}
	
	/**
	 * Renders the given text at the given position.
	 */
	public Surface strokeText(String text, Vector2 position) {
		context.strokeText(text, position.getX(), position.getY());
		return this;
	}
	
	/**
	 * Renders the given text at the given (x, y), ensuring that the text is
	 * not wider than maxWidth.
	 */
	public Surface strokeText(String text, double x, double y, double maxWidth) {
		context.strokeText(text, x, y, maxWidth);
		return this;
	}
	
	/**
	 * Renders the given text at the given position, ensuring that the text is
	 * not wider than maxWidth.
	 */
	public Surface strokeText(String text, Vector2 position, double maxWidth) {
		context.strokeText(text, position.getX(), position.getY(), maxWidth);
		return this;
	}
	
	/**
	 * Returns the advance width with the metrics of the given text in the 
	 * current font.
	 */
	public TextMetrics measureText(String text) {
		return context.measureText(text);
	}
	
	/**
	 * Sets the distance that the shadow will be offset in the positive 
	 * horizontal direction.
	 * 
	 * @param shadowOffsetX
	 */
	public Surface setShadowOffsetX(double shadowOffsetX) {
		context.setShadowOffsetX(shadowOffsetX);
		return this;
	}
	
	/**
	 * Gets the distance that the shadow will be offset in the positive
	 * horizontal direction.
	 */
	public double getShadowOffsetX() {
		return context.getShadowOffsetX();		
	}

	/**
	 * Sets the distance that the shadow will be offset in the positive 
	 * vertical direction.
	 * 
	 * @param shadowOffsetY
	 */
	public Surface setShadowOffsetY(double shadowOffsetY) {
		context.setShadowOffsetY(shadowOffsetY);
		return this;
	}

	/**
	 * Gets the distance that the shadow will be offset in the positive
	 * vertical direction.
	 */
	public double getShadowOffsetY() {
		return context.getShadowOffsetY();
	}
	
	/**
	 * Sets the distance that the shadow will be offset in the positive 
	 * horizontal and vertical direction.
	 * 
	 * @param shadowOffset
	 */
	public Surface setShadowOffset(Vector2 shadowOffset) {
		context.setShadowOffsetX(shadowOffset.getX());
		context.setShadowOffsetY(shadowOffset.getY());
		return this;
	}

	/**
	 * Gets the distance that the shadow will be offset in the positive
	 * horizontal and vertical direction.
	 */
	public Vector2 getShadowOffset() {
		return new Vector2(context.getShadowOffsetX(), context.getShadowOffsetY());
	}
	
	/**
	 * Gets the size of the blurring effect.
	 * 
	 * @param shadowBlur
	 */
	public Surface setShadowBlur(double shadowBlur) {
		context.setShadowBlur(shadowBlur);
		return this;
	}
	
	/**
	 * Gets the size of the blurring effect.
	 */
	public double getShadowBlur() {
		return context.getShadowBlur();
	}

	/**
	 * Sets the color of the shadow.
	 * 
	 * @param shadowColor
	 */
	public Surface setShadowColor(Color shadowColor) {
		context.setShadowColor(shadowColor.getColorCode());
		return this;
	}
	
	/**
	 * Return a data: URL containing a representation of the image as a PNG file.
	 */
	public String toDataURL() {
		return canvas.toDataUrl();
	}
	
	/**
	 * Return a data: URL containing a representation of the image as a the given
	 * type.
	 */
	public String toDataURL(String type) {
		return canvas.toDataUrl(type);
	}

	@Override
	public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
		return canvas.addTouchStartHandler(handler);
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
		return canvas.addTouchMoveHandler(handler);
	}

	@Override
	public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
		return canvas.addTouchEndHandler(handler);
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
		return canvas.addTouchCancelHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return canvas.addMouseWheelHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return canvas.addMouseUpHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return canvas.addMouseOverHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return canvas.addMouseOutHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return canvas.addMouseMoveHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return canvas.addMouseDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return canvas.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return canvas.addKeyPressHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return canvas.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addGestureStartHandler(GestureStartHandler handler) {
		return canvas.addGestureStartHandler(handler);
	}

	@Override
	public HandlerRegistration addGestureEndHandler(GestureEndHandler handler) {
		return canvas.addGestureEndHandler(handler);
	}

	@Override
	public HandlerRegistration addGestureChangeHandler(	GestureChangeHandler handler) {
		return canvas.addGestureChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return canvas.addFocusHandler(handler);
	}

	@Override
	public HandlerRegistration addDropHandler(DropHandler handler) {
		return canvas.addDropHandler(handler);
	}

	@Override
	public HandlerRegistration addDragStartHandler(DragStartHandler handler) {
		return canvas.addDragStartHandler(handler);
	}

	@Override
	public HandlerRegistration addDragOverHandler(DragOverHandler handler) {
		return canvas.addDragOverHandler(handler);
	}

	@Override
	public HandlerRegistration addDragLeaveHandler(DragLeaveHandler handler) {
		return canvas.addDragLeaveHandler(handler);
	}

	@Override
	public HandlerRegistration addDragHandler(DragHandler handler) {
		return canvas.addDragHandler(handler);
	}

	@Override
	public HandlerRegistration addDragEnterHandler(DragEnterHandler handler) {
		return canvas.addDragEnterHandler(handler);
	}

	@Override
	public HandlerRegistration addDragEndHandler(DragEndHandler handler) {
		return canvas.addDragEndHandler(handler);
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return canvas.addDoubleClickHandler(handler);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return canvas.addClickHandler(handler);
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return canvas.addBlurHandler(handler);
	}
}
