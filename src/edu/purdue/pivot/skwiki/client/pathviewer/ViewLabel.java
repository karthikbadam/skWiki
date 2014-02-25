package edu.purdue.pivot.skwiki.client.pathviewer;

import java.util.ArrayList;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Text;

public class ViewLabel {
	private int width = 120;
	private int height = 60;
	private int posX;
	private int posY;
	//String text = "";
	Text textDrawing1 ;
	Text textDrawing2 ;
	//ArrayList<Text> textList = new ArrayList<Text>();
	
	private MyCircle circle;
	private int radius = 15;
	
	public ViewLabel(int x, int y, String text1, String text2, int index)
	{
		posX = x;
		posY = y;
		circle = new MyCircle(posX, posY, radius, index);
		
//		int textPosX = posX - width/2 +5;
//		int textPosY = posY- height/2+10;
		
		textDrawing1 = new Text(posX -5, posY + 5, ""+index);
		textDrawing1.setFontSize(12);
		textDrawing1.setFontFamily("Arial");
		textDrawing1.setStrokeColor("gray");
		
		
		textDrawing2 = new Text(posX - width/2 +5, posY- height/2+15+ 25, text2);
		textDrawing2.setFontSize(12);
		textDrawing2.setFontFamily("Arial");
		textDrawing2.setFillColor("black");
		//textDrawing.setWidth("40px");
	}
	
	public void setColor(String color)
	{	
		textDrawing1.setStrokeColor(color);
		textDrawing1.setFillColor(color);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setRadius() {
		this.radius = radius;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	
	public void appendTo(DrawingArea canvas)
	{
		canvas.add(textDrawing1);
		canvas.add(this.circle);
		//canvas.add(textDrawing2);
	}
	
	public MyCircle getCircle()
	{
		return circle;
	}
	
	
}
