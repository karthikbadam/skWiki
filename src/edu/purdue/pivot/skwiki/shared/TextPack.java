package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;
import java.util.ArrayList;

import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;

public class TextPack implements Serializable{

	
	/*public ArrayList<String> patchStrList;
	public ArrayList<Integer> patchOpList;*/
	public ArrayList<Patch> patches= new ArrayList<Patch>();
	public String id = "";
	public String updateHtml = "";
	
	public TextPack()
	{
		/*patchStrList = new ArrayList<String>();
		patchOpList = new ArrayList<Integer>();*/
		

	}
	
	public void addPatch(Patch patch)
	{
		patches.add(patch);
	}
}
