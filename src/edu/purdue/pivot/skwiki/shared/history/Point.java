package edu.purdue.pivot.skwiki.shared.history;


import gwt.g2d.shared.math.Vector2;

import java.io.Serializable;


public class Point implements Serializable{
	
	private Vector2 position;
	
	public Point()
	{
		
	}
	
	public Point(int x, int y)
	{
		position = new Vector2(x, y);
	}
	
	public Point(Vector2 pos)
	{
		position = new Vector2(pos.getX(), pos.getY());
	}
	
	
	public Vector2 getVector2()
	{
		return position;
	}
	
	
	

}
