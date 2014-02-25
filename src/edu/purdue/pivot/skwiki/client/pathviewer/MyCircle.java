package edu.purdue.pivot.skwiki.client.pathviewer;

import org.vaadin.gwtgraphics.client.shape.Circle;

public class MyCircle extends Circle {

	int index = 0;
	String color;

	public MyCircle(int x, int y, int radius, int index) {
		super(x, y, radius);
		this.index = index;
		this.setFillColor("#E7E8F0");
		this.setFillOpacity(0.7);
	}

	public int getIndex() {
		return index;
	}
	
	public void setColor(String color) {
		this.setStrokeColor(color);
		this.setStrokeWidth(2);
		this.color = color;
	}
	
	//function to highlight Circle when needed
	public int highlight() {
		//TODO make it glow!
		this.setFillColor(color);
		return 0;
	}

	public int unHighlight() {
		this.setFillColor("#E7E8F0");
		return 0;
	}
}
