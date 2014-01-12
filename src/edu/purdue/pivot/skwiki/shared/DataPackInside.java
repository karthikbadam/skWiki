package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;

public class DataPackInside implements Serializable{
	/*Point position;
	
	
	public DataPackInside(Point pos)
	{
		position= pos;
	}*/
	
	String position;
	

	
	public DataPackInside()
	{
		position = "pos";
	}
	
	public DataPackInside(String position) {
		super();
		this.position = position;
	}

	
	

}
