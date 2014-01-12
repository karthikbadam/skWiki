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


import com.google.gwt.canvas.dom.client.Context2d;

import gwt.g2d.client.graphics.shapes.ShapeBuilder;
import gwt.g2d.client.graphics.visitor.ArcToVisitor;
import gwt.g2d.client.graphics.visitor.ArcVisitor;
import gwt.g2d.client.graphics.visitor.BezierCurveToVisitor;
import gwt.g2d.client.graphics.visitor.BezierCurveVisitor;
import gwt.g2d.client.graphics.visitor.CircleVisitor;
import gwt.g2d.client.graphics.visitor.EllipseVisitor;
import gwt.g2d.client.graphics.visitor.LineSegmentVisitor;
import gwt.g2d.client.graphics.visitor.LineToVisitor;
import gwt.g2d.client.graphics.visitor.MoveToVisitor;
import gwt.g2d.client.graphics.visitor.QuadraticCurveToVisitor;
import gwt.g2d.client.graphics.visitor.QuadraticCurveVisitor;
import gwt.g2d.client.graphics.visitor.RectangleVisitor;
import gwt.g2d.client.graphics.visitor.RotateVisitor;
import gwt.g2d.client.graphics.visitor.ScaleVisitor;
import gwt.g2d.client.graphics.visitor.SetTransformVisitor;
import gwt.g2d.client.graphics.visitor.TransformVisitor;
import gwt.g2d.client.graphics.visitor.TranslateVisitor;
import gwt.g2d.client.math.Arc;
import gwt.g2d.client.math.Circle;
import gwt.g2d.client.math.Ellipse;
import gwt.g2d.client.math.MathHelper;
import gwt.g2d.client.math.Matrix;
import gwt.g2d.shared.math.Rectangle;
import gwt.g2d.shared.math.Vector2;

/**
 * Interface for manipulating the Surface's path drawing directly. 
 * This class is very similar to {@link ShapeBuilder} except this class will
 * draw to the canvas directly, whereas {@link ShapeBuilder} stores the drawing
 * commands internally. This call may be used in favor of {@link ShapeBuilder}
 * when the shapes are very complex or when new custom shape are to be drawn in 
 * high frequency.
 * 
 * @author hao1300@gmail.com
 */
public class DirectShapeRenderer {
	private final Context2d context;
	
	public DirectShapeRenderer(Surface surface) {
		context = surface.getContext();
	}
	
	/**
	 * Resets the current path.
	 */
	public final DirectShapeRenderer beginPath() {
		context.beginPath();
		return this;
	}
	
	/**
	 * Marks the current subpath as closed, and starts a new subpath with a point 
	 * the same as the start and end of the newly closed subpath.
	 */
	public final DirectShapeRenderer closePath() {
		context.closePath();
		return this;
	}
	
	/**
	 * Further constrains the clipping region to the given path.
	 */
	public final DirectShapeRenderer clip() {
		context.clip();
		return this;
	}
	
	/**
	 * Fills the subpaths with the current fill style.
	 */
	public final DirectShapeRenderer fill() {
		context.fill();
		return this;
	}
	
	/**
	 * Strokes the subpaths with the current stroke style.
	 */
	public final DirectShapeRenderer stroke() {
		context.stroke();
		return this;
	}
	
	/**
	 * Returns true if the given point is in the current path.
	 */
	public final boolean isPointInPath(double x, double y) {
		return context.isPointInPath(x, y);
	}
	
	/**
	 * Returns true if the given point is in the current path.
	 */
	public final boolean isPointInPath(Vector2 point) {
		return context.isPointInPath(point.getX(), point.getY());
	}
	
	/**
	 * @see MoveToVisitor#MoveToVisitor(double, double)
	 */
	public final DirectShapeRenderer moveTo(double x, double y) {
		context.moveTo(x, y);
		return this;
	}
	
	/**
	 * @see MoveToVisitor#MoveToVisitor(Vector2)
	 */
	public final DirectShapeRenderer moveTo(Vector2 position) {
		return moveTo(position.getX(), position.getY());
	}
	
	/**
	 * @see LineToVisitor#LineToVisitor(double, double)
	 */
	public final DirectShapeRenderer drawLineTo(double x, double y) {
		context.lineTo(x, y);
		return this;
	}
	
	/**
	 * @see LineToVisitor#LineToVisitor(Vector2)
	 */
	public final DirectShapeRenderer drawLineTo(Vector2 position) {
		return drawLineTo(position.getX(), position.getY());
	}
	
	/**
	 * @see DashedLineRenderer#drawDashedLine(Context, double, double, double, double, double, double)
	 */
	public final DirectShapeRenderer drawDashedLine(double fromX, double fromY, 
			double toX, double toY, double dashLength, double gapLength) {
		DashedLineRenderer.drawDashedLine(context, fromX, fromY, toX, toY, 
				dashLength, gapLength);
		return this;
	}
	
	/**
	 * @see DashedLineRenderer#drawDashedLine(Context, double, double, double, double, double, double)
	 */
	public final DirectShapeRenderer drawDashedLine(Vector2 fromPos, 
			Vector2 toPos, double dashLength, double gapLength) {
		DashedLineRenderer.drawDashedLine(context, fromPos.getX(), fromPos.getY(), 
				toPos.getX(), toPos.getY(), dashLength, gapLength);
		return this;
	}
	
	/**
	 * @see LineSegmentVisitor#LineSegmentVisitor(double, double, double, double)
	 */
	public final DirectShapeRenderer drawLineSegment(double fromX, double fromY, 
			double toX, double toY) {
		return moveTo(fromX, fromY).drawLineTo(toX, toY);
	}
	
	/**
	 * @see LineSegmentVisitor#LineSegmentVisitor(Vector2, Vector2)
	 */
	public final DirectShapeRenderer drawLineSegment(Vector2 fromPosition, Vector2 toPosition) {
		return drawLineSegment(fromPosition.getX(), fromPosition.getY(), 
				toPosition.getX(), toPosition.getY());
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(double, double, double, double, double, boolean)
	 */
	public final DirectShapeRenderer drawArc(double x, double y, double radius, 
			double startAngle, double endAngle, boolean antiClockwise) {
		context.arc(x, y, radius, startAngle, endAngle, antiClockwise);
		return this;
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(Vector2, double, double, double, boolean)
	 */
	public final DirectShapeRenderer drawArc(Vector2 position, double radius, double startAngle,
			double endAngle, boolean antiClockwise) {
		return drawArc(position.getX(), position.getY(), radius, startAngle, endAngle, 
				antiClockwise);
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(Arc)
	 */
	public final DirectShapeRenderer drawArc(Arc arc) {
		return drawArc(arc.getCenter(), arc.getRadius(), arc.getStartAngle(), 
				arc.getEndAngle(), arc.isAnticlockwise());
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(double, double, double, double, double, 
	 * boolean, boolean)
	 */
	public final DirectShapeRenderer drawArc(double x, double y, double radius, 
			double startAngle, double endAngle, boolean antiClockwise, 
			boolean connectFromPrev) {
		if (!connectFromPrev) {
			context.moveTo(x, y);
		}
		return drawArc(x, y, radius, startAngle, endAngle, antiClockwise);
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(Vector2, double, double, double, boolean, 
	 * boolean)
	 */
	public final DirectShapeRenderer drawArc(Vector2 position, double radius, 
			double startAngle, double endAngle, boolean antiClockwise, 
			boolean connectFromPrev) {
		return drawArc(position.getX(), position.getY(), radius, startAngle, endAngle, 
				antiClockwise, connectFromPrev);
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(Arc, boolean)
	 */
	public final DirectShapeRenderer drawArc(Arc arc, boolean connectFromPrev) {
		return drawArc(arc.getCenterX(), arc.getCenterY(), arc.getRadius(), 
				arc.getStartAngle(), arc.getEndAngle(), arc.isAnticlockwise(), 
				connectFromPrev);
	}
	
	/**
	 * @see ArcToVisitor#ArcToVisitor(double, double, double, double, double)
	 */
	public final DirectShapeRenderer drawArcTo(double x1, double y1, double x2, 
			double y2, double radius) {
		context.arcTo(x1, y1, x2, y2, radius);
		return this;
	}
	
	/**
	 * @see ArcToVisitor#ArcToVisitor(double, double, double, double, double, 
	 * double, double)
	 */
	public final DirectShapeRenderer drawArcTo(double x0, double y0, 
			double x1, double y1, double x2, double y2, double radius) {
		return moveTo(x0, y0).drawArcTo(x1, y1, x2, y2, radius);
	}
	
	/**
	 * @see ArcToVisitor#ArcToVisitor(Vector2, Vector2, double)
	 */
	public final DirectShapeRenderer drawArcTo(Vector2 point1, Vector2 point2, 
			double radius) {
		return drawArcTo(point1.getX(), point1.getY(), point2.getX(), point2.getY(), 
				radius);
	}
	
	/**
	 * @see ArcToVisitor#ArcToVisitor(Vector2, Vector2, Vector2, double)
	 */
	public final DirectShapeRenderer drawArcTo(Vector2 point0, Vector2 point1, 
			Vector2 point2, double radius) {
		return moveTo(point0).drawArcTo(point1, point2, radius);
	}
	
	/**
	 * @see EllipseVisitor#EllipseVisitor(double, double, double, double)
	 */
	public final DirectShapeRenderer drawEllipse(double x, double y, 
			double width, double height) {
		return save()
				.translate(x + width / 2, y + height / 2)
				.scale(width / 2, height / 2)
				.drawArc(0, 0, 1, 0, MathHelper.TWO_PI, true)
				.restore();
	}
	
	/**
	 * @see EllipseVisitor#EllipseVisitor(Vector2, double, double)
	 */
	public final DirectShapeRenderer drawEllipse(Vector2 center, 
			double width, double height) {
		return drawEllipse(center.getX(), center.getY(), width, height);
	}
	
	/**
	 * @see EllipseVisitor#EllipseVisitor(Ellipse)
	 */
	public final DirectShapeRenderer drawEllipse(Ellipse ellipse) {
		return drawEllipse(ellipse.getX(), ellipse.getY(), 
				ellipse.getWidth(), ellipse.getHeight());
	}
	
	/**
	 * @see CircleVisitor#CircleVisitor(double, double, double)
	 */
	public final DirectShapeRenderer drawCircle(double x, double y, double radius) {
		return drawArc(x, y, radius, 0, MathHelper.TWO_PI, true);
	}
	
	/**
	 * @see CircleVisitor#CircleVisitor(Vector2, double)
	 */
	public final DirectShapeRenderer drawCircle(Vector2 center, double radius) {
		return drawCircle(center.getX(), center.getY(), radius);
	}
	
	/**
	 * @see CircleVisitor#CircleVisitor(Circle)
	 */
	public final DirectShapeRenderer drawCircle(Circle circle) {
		return drawCircle(circle.getCenterX(), circle.getCenterY(), circle.getRadius());
	}
	
	/**
	 * @see BezierCurveToVisitor#BezierCurveToVisitor(double, double, double, 
	 * double, double, double)
	 */
	public final DirectShapeRenderer drawBezierCurveTo(double controlPoint1X, 
			double controlPoint1Y, double controlPoint2X, double controlPoint2Y, 
			double endPointX, double endPointY) {
		context.bezierCurveTo(controlPoint1X, controlPoint1Y, 
				controlPoint2X, controlPoint2Y, endPointX, endPointY);
		return this;
	}
	
	/**
	 * @see BezierCurveToVisitor#BezierCurveToVisitor(Vector2, Vector2, Vector2)
	 */
	public final DirectShapeRenderer drawBezierCurveTo(Vector2 controlPoint1, Vector2 controlPoint2, 
			Vector2 endPoint) {
		return drawBezierCurveTo(controlPoint1.getX(), controlPoint1.getY(), 
				controlPoint2.getX(), controlPoint2.getY(), 
				endPoint.getX(), endPoint.getY());
	}
	
	/**
	 * @see BezierCurveVisitor#BezierCurveVisitor(Vector2, Vector2, Vector2, 
	 * Vector2)
	 */
	public final DirectShapeRenderer drawBezierCurve(double startPointX, double startPointY,
			double controlPoint1X, double controlPoint1Y, 
			double controlPoint2X, double controlPoint2Y, 
			double endPointX, double endPointY) {
		return moveTo(startPointX, startPointY).drawBezierCurveTo(
				controlPoint1X, controlPoint1Y, 
				controlPoint2X, controlPoint2Y, 
				endPointX, endPointY);
	}
	
	/**
	 * @see BezierCurveVisitor#BezierCurveVisitor(double, double, double, double, 
	 * double, double, double, double)
	 */
	public final DirectShapeRenderer drawBezierCurve(Vector2 startPoint, Vector2 controlPoint1, 
			Vector2 controlPoint2, Vector2 endpoint) {
		return drawBezierCurve(startPoint.getX(), startPoint.getY(), 
				controlPoint1.getX(), controlPoint1.getY(), 
				controlPoint2.getX(), controlPoint2.getY(), 
				endpoint.getX(), endpoint.getY());
	}
	
	/**
	 * @see QuadraticCurveToVisitor#QuadraticCurveToVisitor(double, double, 
	 * double, double)
	 */
	public final DirectShapeRenderer drawQuadraticCurveTo(double controlPointX, 
			double controlPointY, double endPointX, double endPointY) {
		context.quadraticCurveTo(controlPointX, controlPointY,
				endPointX, endPointY);
		return this;
	}
	
	/**
	 * @see QuadraticCurveToVisitor#QuadraticCurveToVisitor(Vector2, Vector2)
	 */
	public final DirectShapeRenderer drawQuadraticCurveTo(Vector2 controlPoint, Vector2 endPoint) {
		return drawQuadraticCurveTo(controlPoint.getX(), controlPoint.getY(), 
				endPoint.getX(), endPoint.getY());

	}
	
	/**
	 * @see QuadraticCurveVisitor#QuadraticCurveVisitor(double, double, double, 
	 * double, double, double)
	 */
	public final DirectShapeRenderer drawQuadraticCurve(
			double startPointX, double startPointY,
			double controlPointX, double controlPointY, 
			double endPointX, double endPointY) {
		return moveTo(startPointX, startPointY).drawQuadraticCurveTo(
				controlPointX, controlPointY, endPointX, endPointY);
	}
	
	/**
	 * @see QuadraticCurveVisitor#QuadraticCurveVisitor(Vector2, Vector2, Vector2)
	 */
	public final DirectShapeRenderer drawQuadraticCurve(Vector2 startPoint, 
			Vector2 controlPoint, Vector2 endPoint) {
		return drawQuadraticCurve(startPoint.getX(), startPoint.getY(), 
				controlPoint.getX(), controlPoint.getY(), 
				endPoint.getX(), endPoint.getY());
	}
	
	/**
	 * @see RectangleVisitor#RectangleVisitor(double, double, double, double)
	 */
	public final DirectShapeRenderer drawRect(double x, double y, double width, 
			double height) {
		context.moveTo(x, y);
		context.lineTo(x + width, y);
		context.lineTo(x + width, y + height);
		context.lineTo(x, y + height);
		context.lineTo(x, y);
		return this;
	}
	
	/**
	 * @see RectangleVisitor#RectangleVisitor(Vector2, double, double)
	 */
	public final DirectShapeRenderer drawRect(Vector2 position, double width, 
			double height) {
		return drawRect(position.getX(), position.getY(), width, height);
	}
	
	/**
	 * @see RectangleVisitor#RectangleVisitor(Rectangle)
	 */
	public final DirectShapeRenderer drawRect(Rectangle rectangle) {
		return drawRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
				rectangle.getHeight());
	}
	
	/**
	 * @see Surface#save()
	 */
	public final DirectShapeRenderer save() {
		context.save();
		return this;
	}
	
	/**
	 * @see Surface#restore()
	 */
	public final DirectShapeRenderer restore() {
		context.restore();
		return this;
	}
	
	/**
	 * @see ScaleVisitor#ScaleVisitor(double, double)
	 */
	public final DirectShapeRenderer scale(double x, double y) {
		context.scale(x, y);
		return this;
	}
	
	/**
	 * @see ScaleVisitor#ScaleVisitor(Vector2)
	 */
	public final DirectShapeRenderer scale(Vector2 scales) {
		return scale(scales.getX(), scales.getY());
	}
	
	/**
	 * @see ScaleVisitor#ScaleVisitor(double)
	 */
	public final DirectShapeRenderer scale(double scale) {
		return scale(scale, scale);
	}
	
	/**
	 * Clockwise rotation.
	 * @see RotateVisitor#RotateVisitor(double)
	 */
	public final DirectShapeRenderer rotate(double angle) {
		context.rotate(angle);
		return this;
	}
	
	/**
	 * Counter-clockwise rotation.
	 * @see RotateVisitor#RotateVisitor(double)
	 */
	public final DirectShapeRenderer rotateCcw(double angle) {
		return rotate(-angle);
	}
	
	/**
	 * @see TranslateVisitor#TranslateVisitor(double, double)
	 */
	public final DirectShapeRenderer translate(double x, double y) {
		context.translate(x, y);
		return this;
	}
	
	/**
	 * @see TranslateVisitor#TranslateVisitor(Vector2)
	 */
	public final DirectShapeRenderer translate(Vector2 translation) {
		return translate(translation.getX(), translation.getY());
	}
	
	/**
	 * @see TransformVisitor#TransformVisitor(double, double, double, double, 
	 * 			double, double)
	 */
	public final DirectShapeRenderer transform(double m11, double m12, 
			double m21, double m22, double dx, double dy) {
		context.transform(m11, m12, m21, m22, dx, dy);
		return this;
	}
	
	/**
	 * @see TransformVisitor#TransformVisitor(Matrix)
	 */
	public final DirectShapeRenderer transform(Matrix matrix) {
		return transform(matrix.getM11(), matrix.getM12(), matrix.getM21(), 
				matrix.getM22(), matrix.getDx(), matrix.getDy());
	}
	
	/**
	 * @see SetTransformVisitor#SetTransformVisitor(double, double, double, 
	 * 			double, double, double)
	 */
	public final DirectShapeRenderer setTransform(double m11, double m12, double m21, 
			double m22, double dx, double dy) {
		context.setTransform(m11, m12, m21, m22, dx, dy);
		return this;
	}
	
	/**
	 * @see SetTransformVisitor#SetTransformVisitor(Matrix)
	 */
	public final DirectShapeRenderer setTransform(Matrix matrix) {
		return setTransform(matrix.getM11(), matrix.getM12(), matrix.getM21(), 
				matrix.getM22(), matrix.getDx(), matrix.getDy());
	}
}
