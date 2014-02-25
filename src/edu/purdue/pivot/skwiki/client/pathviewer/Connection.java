package edu.purdue.pivot.skwiki.client.pathviewer;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Path;

public class Connection {

	private ViewLabel label1;
	private ViewLabel label2;
	int Vspace = 50;
	
	Path path;// = new Path(startX, startY);

	public Connection(ViewLabel label1, ViewLabel label2) {
		this.label1 = label1;
		this.label2 = label2;

		path = new Path(0,0);
		// path.lineRelativelyTo(endX, endY);
		if (label1.getPosX() == label2.getPosX()) {
			
			///Draw Bezier curve to avoid curve overlap
			if ( Math.abs(label1.getPosY() - label2.getPosY()) > Vspace) {
				
				int x1 = label1.getPosX();
				int y1 = label1.getPosY();
				
				path = new Path(x1, y1);
				path.setStrokeColor("rgba(50, 50, 50, 0.5)");
				path.setFillOpacity(0);
				
				int x2 = label2.getPosX();
				int y2 = label2.getPosY();
				
				//Window.alert("x2 y2"+ x2+" "+y2);
				
				int curveOffset = (int) (40.0 * (1.0 + Math.random()));
				
				int control1x = x1 + (-x1+x2)/3 - curveOffset;
				int control1y = y1 +  (-y1+y2)/3;
				
				int control2x = x1+ 2*(-x1+x2)/3 - curveOffset;
				int control2y = y1 + 2*(-y1+y2)/3;
				
				//Window.alert("control2x control2y "+ control2x+" "+control2y);
				path.moveTo(x1, y1);
				path.curveTo(control1x, control1y, 
						control2x, control2y, x2, y2);
				path.moveTo(x2, y2);
			
			} else if (Math.abs(label1.getPosY() - label2.getPosY()) <= Vspace){
				int x1 = label1.getPosX();
				int y1 = label1.getPosY();

				path = new Path(x1, y1);
				path.setFillOpacity(0);
				path.setStrokeColor("rgba(50, 50, 50, 0.3)");
				path.setStrokeOpacity(0.7);
				
				int x2 = label2.getPosX();
				int y2 = label2.getPosY();
				path.moveTo(x1, y1);
				path.lineTo(x2, y2);
			}
						
		} else if (label1.getPosX() < label2.getPosX()) {
			int x1 = label1.getPosX();
			int y1 = label1.getPosY();

			path = new Path(x1, y1);
			path.setStrokeColor("rgba(50, 50, 50, 0.3)");
			path.setFillOpacity(0);
			path.setStrokeOpacity(0.7);
			
			int x2 = label2.getPosX();
			int y2 = label2.getPosY();
			path.moveTo(x1, y1);
			path.lineTo(x2, y2);
			
			
		} else if (label1.getPosX() > label2.getPosX())  {
			int x1 = label1.getPosX();
			int y1 = label1.getPosY();

			path = new Path(x1, y1);
			path.setStrokeColor("rgba(50, 50, 50, 0.3)");
			path.setFillOpacity(0);
			path.setStrokeOpacity(0.7);
			
			int x2 = label2.getPosX();
			int y2 = label2.getPosY();
			path.moveTo(x1, y1);
			path.lineTo(x2, y2);

		}

		path.close();
	}

//	TouchPanel widget1;
//	TouchPanel widget2;
//
//	public Connection(TouchPanel widget1, TouchPanel widget2) {
//		this.widget1 = widget1;
//		this.widget2 = widget2;
//
//		int width = 150;
//		int height = 100;
//
//		int posX1 = widget1.getAbsoluteLeft() + width / 2;
//		int posY1 = widget1.getAbsoluteTop() + height / 2;
//
//		int posX2 = widget2.getAbsoluteLeft() + width / 2;
//		int posY2 = widget2.getAbsoluteTop() + height / 2;
//
//		path = new Path(posX1, posY1);
//		path.setFillOpacity(0);
//		path.setStrokeColor("gray");
//
//		path.lineTo(posX2, posY2);
//		path.close();
//
//	}

	public void append(DrawingArea canvas) {
		canvas.add(this.path);
	}

}
