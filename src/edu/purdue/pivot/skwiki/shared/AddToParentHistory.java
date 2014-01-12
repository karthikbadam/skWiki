package edu.purdue.pivot.skwiki.shared;

public class AddToParentHistory extends AbstractLayoutHistory{
	
	private String parentName = "";
	
	
	public AddToParentHistory()
	{
		
		
		
	}
	public AddToParentHistory(String object, String parentName)
	{
		super(object);
		this.parentName = parentName;
	}
	
	public String getParentName()
	{
		return parentName;
	}

}
