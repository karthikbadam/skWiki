package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;

public class Diff implements Serializable{

	/**
	 * @param args
	 */
	
	public int operation  = 0;//= "";
	public String text = "";
	
	public Diff()
	{
		
	}
	public Diff(int opertion, String text)
	{
		this.operation = opertion;
		this.text = text;
	}
	

}
