package edu.purdue.pivot.skwiki.client.dnd;


public class LabelInformation {

	
	public String labelType;
	public String labelStr;
	
	public int common;
	public int interested;
	
	public LabelInformation()
	{
		
		
	}
	
	public LabelInformation(String type)
	{
		labelType = type;
		
	}
	
	
	public LabelInformation(String type, String str)
	{
		labelType = type;
		labelStr = str;
		
	}
	
}


