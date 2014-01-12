package edu.purdue.pivot.skwiki.shared;

public class ChangeSizeHistory extends AbstractLayoutHistory{
	
	private int newX  = 0;
	

	private int newY  = 0;
	
	public ChangeSizeHistory()
	{
		
		
	}
	
	public ChangeSizeHistory(String object, int newX, int newY)
	{
		super(object);
		this.newX = newX;
		this.newY = newY;
	}
	
	public int getNewX() {
		return newX;
	}

	public void setNewX(int newX) {
		this.newX = newX;
	}

	public int getNewY() {
		return newY;
	}

	public void setNewY(int newY) {
		this.newY = newY;
	}

}