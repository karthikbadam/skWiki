package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Patch implements Serializable{

	/**
	 * @param args
	 */
	public ArrayList<Diff> diffs = new ArrayList<Diff>();
	
	public Patch()
	{
		
	}
	
	public void addDiff(int opertion, String text)
	{
		diffs.add(new Diff(opertion, text));
	}

}
