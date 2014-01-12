package edu.purdue.pivot.skwiki.client.sketch;

import java.util.ArrayList;

import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;

public class LocalHistory {
	
	public final ArrayList<AbstractHistory> returnHistoryList =
			new ArrayList<AbstractHistory>(); 
	
	public int commitFinishIndex = 0;
	public LocalHistory()
	{
		
	}

}
