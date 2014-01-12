package edu.purdue.pivot.skwiki.shared.history;

import gwt.g2d.client.graphics.Surface;

import java.util.ArrayList;

public class HistoryManager {
	
	public ArrayList<AbstractHistory> historys = new ArrayList<AbstractHistory>();
	private Surface mySurface;
	
	public HistoryManager(Surface mySurface) {
		super();
		this.mySurface = mySurface;
	}
	
	public void addHistory(AbstractHistory aHistory)
	{
		historys.add(aHistory);
	}
	
	public void moveToState(double percent)
	{
		
		int i = (int)Math.round(percent*historys.size())-1;
		
		if( i >=historys.size())
		{
		}
		
		for(int j = 0 ;j <= i; j++)
		{
		   historys.get(j).perform(mySurface);
		}
		
	}
	
	public void setHistory(ArrayList<AbstractHistory> newHistorys)
	{
		historys = newHistorys;
	}
	
	public void clearHistory()
	{
		historys.clear();
	}
		
}
