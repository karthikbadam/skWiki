package edu.purdue.pivot.skwiki.client.vector;

import gwt.g2d.shared.math.Vector2;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;


import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

public class VectorPanel extends DrawingArea implements HasMouseDownHandlers,
		HasMouseMoveHandlers, HasMouseUpHandlers, MouseDownHandler,
		MouseUpHandler, MouseMoveHandler, HasFocusHandlers {
	VectorToolbar toolbar;
	String shape = "none";

	public VectorPanel(int width, int height) {

		super(width, height);
		this.sinkEvents(Event.MOUSEEVENTS);
		this.setHeight(height + "px");
		this.setWidth(width + "px");

		toolbar = new VectorToolbar(this);
		this.addMouseDownHandler(this);
		this.addMouseUpHandler(this);
		this.addMouseMoveHandler(this);

	}

	public void addCircle() {
		shape = "circle";
	}

	public void addRectangle() {
		shape = "rectangle";
	}

	public void addLine() {
		shape = "line";
	}

	public void addPolygon() {
		shape = "polygon";
	}

	public void addCurve() {
		shape = "curve";
	}

	public VectorToolbar getToolbar() {
		return toolbar;
	}

	Vector2 circle_center;
	Vector2 line_start;
	Vector2 polygon_start;
	Vector2 rect_start;
	//Shapes
	Circle circle1;
	Line line1;
	Path polygon; 
	Rectangle rect;
	
	Boolean isShape = false;
	int polygon_sides = 0;
	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (shape.equals("circle")) {
			circle_center = new Vector2();
			circle_center.set(event.getX(), event.getY());
			circle1 = new Circle((int) circle_center.getX(),
					(int) circle_center.getY(), 2);
			this.add(circle1);
			circle1.setFillColor("blue");
			isShape = true;
		}
		else if (shape.equals("line")) {
			line_start = new Vector2();
			line_start.set(event.getX(), event.getY());
			line1 = new Line(line_start.getIntX(), line_start.getIntY(), line_start.getIntX()+1, line_start.getIntY()+1);
			this.add(line1);
			line1.setStrokeColor("blue");
			isShape = true;
		}
		else if (shape.equals("polygon") && polygon_sides == 0) {
			polygon_start = new Vector2();
			polygon_start.set(event.getX(), event.getY());
			polygon = new Path(polygon_start.getIntX(), polygon_start.getIntY());
			this.add(polygon);
			polygon.setFillColor("blue");
			isShape = true;
		}
		else if (shape.equals("rectangle")) {
			rect_start = new Vector2();
			rect_start.set(event.getX(), event.getY());
			rect = new Rectangle(rect_start.getIntX(), rect_start.getIntY(), 1, 1);
			this.add(rect);
			rect.setFillColor("blue");
			isShape = true;
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		event.preventDefault();
		int x = event.getX();
		int y = event.getY();
		if (isShape == true && shape.equals("circle")) {
			int radius = (int) Math.sqrt(Math.pow(x - circle_center.getX(), 2)
					+ Math.pow(y - circle_center.getY(), 2));
			circle1.setRadius(radius);
		}
		else if(isShape == true && shape.equals("line")) {
			line1.setX2(x);
			line1.setY2(y);
		}
		else if(isShape == true && shape.equals("rectangle")) {
			if (x < rect_start.getIntX() && y < rect_start.getIntY()){
				rect.setX(x);
				rect.setY(y);
				rect.setWidth(rect_start.getIntX() - x);
				rect.setHeight(rect_start.getIntY() - y);
			} else {
				rect.setWidth(x - rect_start.getIntX());
				rect.setHeight(y - rect_start.getIntY());
			}
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		int x = event.getX();
		int y = event.getY();
		
		if (isShape == true && shape.equals("circle")) {
			int radius = (int) Math.sqrt(Math.pow(x - circle_center.getX(), 2)
					+ Math.pow(y - circle_center.getY(), 2));
			circle1.setRadius(radius);
			isShape = false;
		}
		else if(isShape == true && shape.equals("line")) {
			line1.setX2(x);
			line1.setY2(y);
			isShape = false;
		}
		else if(isShape == true && shape.equals("polygon")) {
			if (Math.abs(x - polygon_start.getIntX()) < 25 && Math.abs(y - polygon_start.getIntY()) < 15) {
				polygon.lineTo(polygon_start.getIntX(), polygon_start.getIntY());
				isShape = false;
				polygon.close();
				polygon_sides = 0;
			}else {
				polygon.lineTo(x, y);
				polygon_sides++;
			}
			
		}
		else if(isShape == true && shape.equals("rectangle")) {
			if (x < rect_start.getIntX() && y < rect_start.getIntY()){
				rect.setX(x);
				rect.setY(y);
				rect.setWidth(rect_start.getIntX() - x);
				rect.setHeight(rect_start.getIntY() - y);
				isShape = false;
			} else {
				rect.setWidth(x - rect_start.getIntX());
				rect.setHeight(y - rect_start.getIntY());
				isShape = false;
			}
		}
		
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addHandler(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addHandler(handler, MouseMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addHandler(handler, FocusEvent.getType());
	}

}
