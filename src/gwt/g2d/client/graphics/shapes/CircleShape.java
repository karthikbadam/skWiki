package gwt.g2d.client.graphics.shapes;


import com.google.gwt.canvas.dom.client.Context2d;

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.math.Circle;
import gwt.g2d.client.math.MathHelper;
import gwt.g2d.shared.math.Vector2;

/**
 * Represents a circular shape.
 * 
 * @author hao1300@gmail.com
 */
public class CircleShape extends Shape {
	
	// center
	private Vector2 fCenter;
	
	// radius
	private double fRadius;
	
	public CircleShape(double centerX, double centerY, double radius) {
		this(new Vector2(centerX, centerY), radius);
	}
	
	public CircleShape(Vector2 center, double radius) {
		fCenter = center;
		fRadius = radius;
	}
	
	public CircleShape(Circle circle) {
		this(circle.getCenter(), circle.getRadius());
	}
	
	public Vector2 getCenter() {
		return fCenter;
	}
	
	public void setCenter(Vector2 center) {
		fCenter = center;
	}

	@Override
	public void draw(Surface surface) {
		Context2d context = surface.getContext();
		context.beginPath();
		context.arc(fCenter.x, fCenter.y, fRadius, 0, MathHelper.TWO_PI, true);
		context.closePath();
	}
}
