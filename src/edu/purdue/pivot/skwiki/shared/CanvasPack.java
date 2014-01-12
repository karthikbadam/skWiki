package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;
import java.util.ArrayList;

import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;

public class CanvasPack implements Serializable{

	
	public ArrayList<AbstractHistory> updatedHistory;
	// = new ArrayList<AbstractHistory>();

	//********** history fucntion
	public boolean newID = false;
	public int updateRevision=0;
	
	
	//********index for ending a part of list of revision
	public int historyIndex = 0;
	public String id = "";
	//public String fromID = "";
	
	
	
	public CanvasPack()
	{
		updatedHistory = new ArrayList<AbstractHistory>();

	}
}
