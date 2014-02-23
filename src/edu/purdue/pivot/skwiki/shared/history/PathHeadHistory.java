package edu.purdue.pivot.skwiki.shared.history;

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.shared.Color;

public class PathHeadHistory extends AbstractHistory{
	

	//Point position;
	public int strokeSize;
	public MyColor pathColor;
	
	public PathHeadHistory()
	{
		strokeSize = 1;
		this.pathColor = new MyColor(0,0,0);
	}
	
	public PathHeadHistory(Point position, int strokeSize, Color pathColor) {
		super();
		this.position = position;
		this.strokeSize = strokeSize;
		this.pathColor = new MyColor(pathColor.getR(), pathColor.getB(), pathColor.getB());
	}

	
	public String getColor()
	{
		return pathColor.getColorCode();
	}

	/*public void perform(HistoryStack myHistorystack)
	{
		PPath squiggle = new PPath();
		
		PathProfile tempProfile = new PathProfile(squiggle, strokeSize, pathColor);
		MyPath tempMyPath = new MyPath(squiggle, tempProfile);
		myHistorystack.paths.add(tempMyPath);
		
		
		squiggle.moveTo((float)position.getX(), (float)position.getY());	
		//*******PATH***************** add node start
		tempMyPath.addNode(new PathNode(position, NodeType.Start, strokeSize, pathColor));
		
		
		//********* setup basic path profile
		squiggle.setStrokePaint(pathColor);
		squiggle.setStroke(new BasicStroke((float)(strokeSize)));
		
		
		//*********************** compute the distance
		getDistance();
	}*/
	
	
	
	
	@Override
	public String getType()
	{
		return "PathHeadHistory";
	}
	
	
	@Override
	public void perform(Surface mySurface)
	{
		
		
	}
	

}
