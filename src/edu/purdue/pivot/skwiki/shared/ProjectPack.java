package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;
import java.util.ArrayList;

import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;

public class ProjectPack implements Serializable{

	
	public String projectName = "";
	public String projectDescription ="";
	public ArrayList<String> projectNameList = new ArrayList<String>();
	public ArrayList<String> projectDescripList = new ArrayList<String>();
	
	public ProjectPack()
	{
		
	}
	
	public ProjectPack(String name, String description)
	{
		this.projectName  = name;
		this.projectDescription = description;

	}
}
