package edu.purdue.pivot.skwiki.shared.history;

import java.util.ArrayList;

public class HistoryStack {
	
	//***************** record data structures
	public ArrayList<MyPath_Client> paths = new ArrayList<MyPath_Client>();
	
	
	public void clear()
	{
		paths.clear();
	}
	 
	public MyPath_Client getStackTopPath()
	{
		if(paths.size()>=1)
		{return paths.get(paths.size()-1);
		
		}
		else
		{
			System.out.println("paths size is zero");
			return null;
		}
		
	}
	
	
	
	//*************add the paths to layer
	/*public void showPaths(PLayer layer)
	{
		for(MyPath tempPath: paths)
		{
			layer.addChild(tempPath.graphPath);
			
			//break;
		}
		
		//layer.addChild(paths.get(1).graphPath);
	}*/

}
