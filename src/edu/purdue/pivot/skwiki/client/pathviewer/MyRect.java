package edu.purdue.pivot.skwiki.client.pathviewer;

import org.vaadin.gwtgraphics.client.shape.Rectangle;

public class MyRect extends Rectangle{
	
	int index = 0;
	
	public MyRect(int x, int y, int width, int height, int index)
	{
		
		super( x, y, width,  height);
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}
}
