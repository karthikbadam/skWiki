package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;

public class AbstractLayoutHistory implements Serializable{

	protected String operatingObject = "";
	
	
	public AbstractLayoutHistory()
	{
		
		
	}
	public AbstractLayoutHistory(String object)
	{
		operatingObject = object;
	}
	
	public String getOperatingObject()
	{
		return operatingObject;
	}
}
