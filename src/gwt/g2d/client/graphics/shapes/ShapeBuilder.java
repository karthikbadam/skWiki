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

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.graphics.visitor.ArcToVisitor;
import gwt.g2d.client.graphics.visitor.ArcVisitor;
import gwt.g2d.client.graphics.visitor.BezierCurveToVisitor;
import gwt.g2d.client.graphics.visitor.BezierCurveVisitor;
import gwt.g2d.client.graphics.visitor.CircleVisitor;
import gwt.g2d.client.graphics.visitor.DashedLineVisitor;
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
import gwt.g2d.client.graphics.visitor.ShapeVisitor;
import gwt.g2d.client.graphics.visitor.TransformVisitor;
import gwt.g2d.client.graphics.visitor.TranslateVisitor;
import gwt.g2d.client.math.Arc;
import gwt.g2d.client.math.Circle;
import gwt.g2d.client.math.Ellipse;
import gwt.g2d.client.math.Matrix;
import gwt.g2d.shared.math.Rectangle;
import gwt.g2d.shared.math.Vector2;

import java.util.ArrayList;
import java.util.List;


import com.google.gwt.canvas.dom.client.Context2d;

/**
 * A builder for drawing custom shapes.
 * 
 * @author hao1300@gmail.com
 */
public class ShapeBuilder {
	private final List<ShapeVisitor> shapes = new ArrayList<ShapeVisitor>();
	
	/**
	 * Appends the given shape visitor to the builder.
	 * 
	 * @param shapeVisitor the shape visitor to be added.
	 * @return self to support chaining.
	 */
	public final ShapeBuilder append(ShapeVisitor shapeVisitor) {
		shapes.add(shapeVisitor);
		return this;
	}
	
	/**
	 * @see MoveToVisitor#MoveToVisitor(double, double)
	 */
	public final ShapeBuilder moveTo(double x, double y) {
		return append(new MoveToVisitor(x, y));
	}
	
	/**
	 * @see MoveToVisitor#MoveToVisitor(Vector2)
	 */
	public final ShapeBuilder moveTo(Vector2 position) {
		return append(new MoveToVisitor(position));
	}
	
	/**
	 * @see LineToVisitor#LineToVisitor(double, double)
	 */
	public final ShapeBuilder drawLineTo(double x, double y) {
		return append(new LineToVisitor(x, y));
	}
	
	/**
	 * @see LineToVisitor#LineToVisitor(Vector2)
	 */
	public final ShapeBuilder drawLineTo(Vector2 position) {
		return append(new LineToVisitor(position));
	}

	/**
	 * @see DashedLineVisitor#DashedLineVisitor(double, double, double, double, 
	 * 			double, double)
	 */
	public final ShapeBuilder drawDashedLine(double fromX, double fromY,
			double toX, double toY, double dashLength, double gapLength) {
		return append(new DashedLineVisitor(fromX, fromY, toX, toY, dashLength, gapLength));
	}
	
	/**
	 * @see DashedLineVisitor#DashedLineVisitor(double, double, double, double, 
	 * 			double, double)
	 */
	public final ShapeBuilder drawDashedLine(Vector2 fromPos, Vector2 toPos, 
			double dashLength, double gapLength) {
		return append(new DashedLineVisitor(fromPos, toPos, dashLength, gapLength));
	}
	
	/**
	 * @see LineSegmentVisitor#LineSegmentVisitor(double, double, double, double)
	 */
	public final ShapeBuilder drawLineSegment(double fromX, double fromY, double toX, 
			double toY) {
		return append(new LineSegmentVisitor(fromX, fromY, toX, toY));
	}
	
	/**
	 * @see LineSegmentVisitor#LineSegmentVisitor(Vector2, Vector2)
	 */
	public final ShapeBuilder drawLineSegment(Vector2 fromPosition, Vector2 toPosition) {
		return append(new LineSegmentVisitor(fromPosition, toPosition));
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(double, double, double, double, double, boolean)
	 */
	public final ShapeBuilder drawArc(double x, double y, double radius, 
			double startAngle, double endAngle, boolean antiClockwise) {
		return append(new ArcVisitor(x, y, radius, startAngle, endAngle, antiClockwise));
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(Vector2, double, double, double, boolean)
	 */
	public final ShapeBuilder drawArc(Vector2 position, double radius, double startAngle,
			double endAngle, boolean antiClockwise) {
		return append(new ArcVisitor(position, radius, startAngle, endAngle, 
				antiClockwise));
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(Arc)
	 */
	public final ShapeBuilder drawArc(Arc arc) {
		return append(new ArcVisitor(arc));
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(double, double, double, double, double, 
	 * boolean, boolean)
	 */
	public final ShapeBuilder drawArc(double x, double y, double radius, 
			double startAngle, double endAngle, boolean antiClockwise, 
			boolean connectFromPrev) {
		return append(new ArcVisitor(x, y, radius, startAngle, endAngle, 
				antiClockwise, connectFromPrev));
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(Vector2, double, double, double, boolean, 
	 * boolean)
	 */
	public final ShapeBuilder drawArc(Vector2 position, double radius, double startAngle,
			double endAngle, boolean antiClockwise, boolean connectFromPrev) {
		return append(new ArcVisitor(position, radius, startAngle, endAngle, 
				antiClockwise, connectFromPrev));
	}
	
	/**
	 * @see ArcVisitor#ArcVisitor(Arc, boolean)
	 */
	public final ShapeBuilder drawArc(Arc arc, boolean connectFromPrev) {
		return append(new ArcVisitor(arc, connectFromPrev));
	}
	
	/**
	 * @see ArcToVisitor#ArcToVisitor(double, double, double, double, double, 
	 * double, double)
	 */
	public final ShapeBuilder drawArcTo(double x0, double y0, 
			double x1, double y1, double x2, double y2, double radius) {
		return append(new ArcToVisitor(x0, y0, x1, y1, x2, y2, radius));
	}
	
	/**
	 * @see ArcToVisitor#ArcToVisitor(double, double, double, double, double)
	 */
	public final ShapeBuilder drawArcTo(double x1, double y1, double x2, double y2, 
			double radius) {
		return append(new ArcToVisitor(x1, y1, x2, y2, radius));
	}
	
	/**
	 * @see ArcToVisitor#ArcToVisitor(Vector2, Vector2, Vector2, double)
	 */
	public final ShapeBuilder drawArcTo(Vector2 point0, Vector2 point1, 
			Vector2 point2, double radius) {
		return append(new ArcToVisitor(point0, point1, point2, radius));
	}
	
	/**
	 * @see ArcToVisitor#ArcToVisitor(Vector2, Vector2, double)
	 */
	public final ShapeBuilder drawArcTo(Vector2 point1, Vector2 point2, double radius) {
		return append(new ArcToVisitor(point1, point2, radius));
	}
	
	/**
	 * @see CircleVisitor#CircleVisitor(double, double, double)
	 */
	public final ShapeBuilder drawCircle(double x, double y, double radius) {
		return append(new CircleVisitor(x, y, radius));
	}
	
	/**
	 * @see CircleVisitor#CircleVisitor(Vector2, double)
	 */
	public final ShapeBuilder drawCircle(Vector2 center, double radius) {
		return append(new CircleVisitor(center, radius));
	}
	
	/**
	 * @see CircleVisitor#CircleVisitor(Circle)
	 */
	public final ShapeBuilder drawCircle(Circle circle) {
		return append(new CircleVisitor(circle));
	}
	
	/**
	 * @see EllipseVisitor#EllipseVisitor(double, double, double, double)
	 */
	public final ShapeBuilder drawEllipse(double x, double y, double width, double height) {
		return append(new EllipseVisitor(x, y, width, height));
	}
	
	/**
	 * @see EllipseVisitor#EllipseVisitor(Vector2, double, double)
	 */
	public final ShapeBuilder drawEllipse(Vector2 center, double width, double height) {
		return append(new EllipseVisitor(center, width, height));
	}
	
	/**
	 * @see EllipseVisitor#EllipseVisitor(Ellipse)
	 */
	public final ShapeBuilder drawEllipse(Ellipse ellipse) {
		return append(new EllipseVisitor(ellipse));
	}
	
	/**
	 * @see BezierCurveToVisitor#BezierCurveToVisitor(double, double, double, 
	 * double, double, double)
	 */
	public final ShapeBuilder drawBezierCurveTo(double controlPoint1X, 
			double controlPoint1Y, double controlPoint2X, double controlPoint2Y, 
			double endPointX, double endPointY) {
		return append(new BezierCurveToVisitor(controlPoint1X, controlPoint1Y, 
				controlPoint2X, controlPoint2Y, 
				endPointX, endPointY));
	}
	
	/**
	 * @see BezierCurveToVisitor#BezierCurveToVisitor(Vector2, Vector2, Vector2)
	 */
	public final ShapeBuilder drawBezierCurveTo(Vector2 controlPoint1, Vector2 controlPoint2, 
			Vector2 endPoint) {
		return append(new BezierCurveToVisitor(controlPoint1, controlPoint2, endPoint));
	}
	
	/**
	 * @see BezierCurveVisitor#BezierCurveVisitor(Vector2, Vector2, Vector2, 
	 * Vector2)
	 */
	public final ShapeBuilder drawBezierCurve(double startPointX, double startPointY,
			double controlPoint1X, double controlPoint1Y, 
			double controlPoint2X, double controlPoint2Y, 
			double endPointX, double endPointY) {
		return append(new BezierCurveVisitor(startPointX, startPointY, 
				controlPoint1X, controlPoint1Y, 
				controlPoint2X, controlPoint2Y, 
				endPointX, endPointY));
	}
	
	/**
	 * @see BezierCurveVisitor#BezierCurveVisitor(double, double, double, double, 
	 * double, double, double, double)
	 */
	public final ShapeBuilder drawBezierCurve(Vector2 startPoint, Vector2 controlPoint1, 
			Vector2 controlPoint2, Vector2 endpoint) {
		return append(new BezierCurveVisitor(startPoint, controlPoint1, 
				controlPoint2, endpoint));
	}
	
	/**
	 * Use {@link #drawBezierCurveTo(double, double, double, double, double, 
	 * double)} instead.
	 */
	@Deprecated
	public final ShapeBuilder drawCubeCurveTo(double controlPoint1X, 
			double controlPoint1Y, double controlPoint2X, double controlPoint2Y, 
			double endPointX, double endPointY) {
		return append(new gwt.g2d.client.graphics.visitor.CubicCurveToVisitor(
				controlPoint1X, controlPoint1Y, 
				controlPoint2X, controlPoint2Y, 
				endPointX, endPointY));
	}
	
	/**
	 * Use {@link #drawBezierCurveTo(Vector2, Vector2, Vector2)} instead.
	 */
	@Deprecated
	public final ShapeBuilder drawCubeCurveTo(Vector2 controlPoint1, 
			Vector2 controlPoint2, Vector2 endPoint) {
		return append(new gwt.g2d.client.graphics.visitor.CubicCurveToVisitor(
				controlPoint1, controlPoint2, endPoint));
	}
	
	/**
	 * Use {@link #drawBezierCurve(double, double, double, double, double, 
	 * double, double, double)} instead.
	 */
	@Deprecated
	public final ShapeBuilder drawCubicCurve(double startPointX, double startPointY,
			double controlPoint1X, double controlPoint1Y, 
			double controlPoint2X, double controlPoint2Y, 
			double endPointX, double endPointY) {
		return append(new gwt.g2d.client.graphics.visitor.CubicCurveVisitor(
				startPointX, startPointY, 
				controlPoint1X, controlPoint1Y, 
				controlPoint2X, controlPoint2Y, 
				endPointX, endPointY));
	}
	
	/**
	 * Use {@link #drawBezierCurve(Vector2, Vector2, Vector2, Vector2)} instead.
	 */
	@Deprecated
	public final ShapeBuilder drawCubicCurve(Vector2 startPoint, 
			Vector2 controlPoint1, Vector2 controlPoint2, Vector2 endpoint) {
		return append(new gwt.g2d.client.graphics.visitor.CubicCurveVisitor(
				startPoint, controlPoint1, controlPoint2, endpoint));
	}
	
	/**
	 * @see QuadraticCurveToVisitor#QuadraticCurveToVisitor(double, double, 
	 * double, double)
	 */
	public final ShapeBuilder drawQuadraticCurveTo(double controlPointX, 
			double controlPointY, double endPointX, double endPointY) {
		return append(new QuadraticCurveToVisitor(controlPointX, controlPointY, 
				endPointX, endPointY));
	}
	
	/**
	 * @see QuadraticCurveToVisitor#QuadraticCurveToVisitor(Vector2, Vector2)
	 */
	public final ShapeBuilder drawQuadraticCurveTo(Vector2 controlPoint, Vector2 endPoint) {
		return append(new QuadraticCurveToVisitor(controlPoint, endPoint));

	}
	
	/**
	 * This method is incorrectly named. Use 
	 * {@link #drawQuadraticCurve(double, double, double, double, double, double)}
	 * instead.
	 * 
	 * @see QuadraticCurveVisitor#QuadraticCurveVisitor(double, double, double, 
	 * double, double, double)
	 */
	@Deprecated
	public final ShapeBuilder drawQuadraticCurveTo(double startPointX, double startPointY,
			double controlPointX, double controlPointY, double endPointX, double endPointY) {
		return append(new QuadraticCurveVisitor(startPointX, startPointY, 
				controlPointX, controlPointY, endPointX, endPointY));
	}
	
	/**
	 * @see QuadraticCurveVisitor#QuadraticCurveVisitor(double, double, double, 
	 * double, double, double)
	 */
	public final ShapeBuilder drawQuadraticCurve(double startPointX, double startPointY,
			double controlPointX, double controlPointY, double endPointX, double endPointY) {
		return append(new QuadraticCurveVisitor(startPointX, startPointY, 
				controlPointX, controlPointY, endPointX, endPointY));
	}
	
	/**
	 * @see QuadraticCurveVisitor#QuadraticCurveVisitor(Vector2, Vector2, Vector2)
	 */
	public final ShapeBuilder drawQuadraticCurve(Vector2 startPoint, Vector2 controlPoint, 
			Vector2 endPoint) {
		return append(new QuadraticCurveVisitor(startPoint, controlPoint, endPoint));
	}
	
	/**
	 * @see RectangleVisitor#RectangleVisitor(double, double, double, double)
	 */
	public final ShapeBuilder drawRect(double x, double y, double width, double height) {
		return append(new RectangleVisitor(x, y, width, height));
	}
	
	/**
	 * @see RectangleVisitor#RectangleVisitor(Vector2, double, double)
	 */
	public final ShapeBuilder drawRect(Vector2 position, double width, double height) {
		return append(new RectangleVisitor(position, width, height));
	}
	
	/**
	 * @see RectangleVisitor#RectangleVisitor(Rectangle)
	 */
	public final ShapeBuilder drawRect(Rectangle rectangle) {
		return append(new RectangleVisitor(rectangle));
	}
	
	/**
	 * @see ScaleVisitor#ScaleVisitor(double, double)
	 */
	public final ShapeBuilder scale(double x, double y) {
		return append(new ScaleVisitor(x, y));
	}
	
	/**
	 * @see ScaleVisitor#ScaleVisitor(Vector2)
	 */
	public final ShapeBuilder scale(Vector2 scales) {
		return append(new ScaleVisitor(scales));
	}
	
	/**
	 * @see ScaleVisitor#ScaleVisitor(double)
	 */
	public final ShapeBuilder scale(double scale) {
		return append(new ScaleVisitor(scale));
	}
	
	/**
	 * Clockwise rotation.
	 * @see RotateVisitor#RotateVisitor(double)
	 */
	public final ShapeBuilder rotate(double angle) {
		return append(new RotateVisitor(angle));
	}
	
	/**
	 * Counter-clockwise rotation.
	 * @see RotateVisitor#RotateVisitor(double)
	 */
	public final ShapeBuilder rotateCcw(double angle) {
		return append(new RotateVisitor(-angle));
	}
	
	/**
	 * @see TranslateVisitor#TranslateVisitor(double, double)
	 */
	public final ShapeBuilder translate(double x, double y) {
		return append(new TranslateVisitor(x, y));
	}
	
	/**
	 * @see TranslateVisitor#TranslateVisitor(Vector2)
	 */
	public final ShapeBuilder translate(Vector2 translation) {
		return append(new TranslateVisitor(translation));
	}
	
	/**
	 * @see TransformVisitor#TransformVisitor(double, double, double, double, 
	 * 			double, double)
	 */
	public final ShapeBuilder transform(double m11, double m12, double m21, double m22,
      double dx, double dy) {
		return append(new TransformVisitor(m11, m12, m21, m22, dx, dy));
	}
	
	/**
	 * @see TransformVisitor#TransformVisitor(Matrix)
	 */
	public final ShapeBuilder transform(Matrix matrix) {
		return append(new TransformVisitor(matrix));
	}
	
	/**
	 * @see SetTransformVisitor#SetTransformVisitor(double, double, double, 
	 * 			double, double, double)
	 */
	public final ShapeBuilder setTransform(double m11, double m12, double m21, 
			double m22, double dx, double dy) {
		return append(new SetTransformVisitor(m11, m12, m21, m22, dx, dy));
	}
	
	/**
	 * @see SetTransformVisitor#SetTransformVisitor(Matrix)
	 */
	public final ShapeBuilder setTransform(Matrix matrix) {
		return append(new SetTransformVisitor(matrix));
	}
	
	/**
	 * Builds the customized shape.
	 */
	public final Shape build() {
		return new CustomShape();
	}
	
	/**
	 * Represents a custom shape.
	 */
	public final class CustomShape extends Shape {
		@Override
		public final void draw(Surface surface) {
			Context2d context = surface.getContext();
			context.beginPath();
			for (ShapeVisitor shape : shapes) {
				shape.visit(surface);
			}
			context.closePath();
		}
	}
}
