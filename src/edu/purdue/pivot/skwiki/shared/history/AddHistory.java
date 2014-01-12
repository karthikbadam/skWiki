package edu.purdue.pivot.skwiki.shared.history;

import com.google.gwt.canvas.dom.client.Context2d.LineJoin;

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.graphics.shapes.ShapeBuilder;
import gwt.g2d.shared.Color;

public class AddHistory extends AbstractHistory {

	
	//Point position;
	public int strokeSize;
	public MyColor  pathColor ;
	public Point endPos;
	
	public AddHistory()
	{
		super();
		strokeSize = 1;
		this.pathColor = new MyColor(0,0,0);
		endPos = new Point(0, 0);
	}
	
	public AddHistory(Point position) {
		super();
		this.position = position;
	}
	
	
	
	public AddHistory(Point position, Point endPos, int strokeSize, Color pathColor) {
		super();
		this.position = position;
		this.endPos = endPos;
		this.strokeSize = strokeSize;
		this.pathColor = new MyColor(pathColor.getR(), pathColor.getG(), pathColor.getB());
		
	}
	
	
	
	@Override
	public String getType()
	{
		
		return "AddHistory";
	}
	
	
	
	@Override
	public void perform(Surface surface)
	{
		double oldx = position.getVector2().getX();
		double oldy = position.getVector2().getY();
		
		double x = 0.3 * endPos.getVector2().getX() + 0.7 * oldx;
		double y = 0.3 * endPos.getVector2().getY() + 0.7 * oldy;
		
		surface.save().setLineJoin(LineJoin.ROUND);
		surface.setStrokeStyle(new Color(pathColor.getR(), pathColor.getG(), pathColor.getB())).setLineWidth(
				strokeSize);		
		surface.strokeShape(new ShapeBuilder()
		.drawLineSegment(position.getVector2(), endPos.getVector2())
		.build());
		
		surface.restore();
		
	}
	/*
	public void perform(HistoryStack myHistorystack)
	{
		
		//**********graphical level
		myHistorystack.getStackTopPath().getGraphPath().lineTo(position.getX(), position.getY());
		
		//**********data level
		myHistorystack.getStackTopPath().addNode(new PathNode(position, NodeType.Middle, strokeSize, pathColor));
		
		
		//*********************** compute the distance
		getDistance();
	}
*/
	
	
}
