package edu.purdue.pivot.skwiki.client.dnd;

import java.util.ArrayList;

public class dataClass {

	
	ArrayList<String> userList = new ArrayList<String>();
	ArrayList<String> tagList = new ArrayList<String>();
	ArrayList<String> commentList = new ArrayList<String>();
	ArrayList<String> datasetList = new ArrayList<String>();
	
	
	public dataClass()
	{
		   initUserList();
		
		
	}
	
	public void initUserList()
	{
		userList.add(new String("<b>US WeatherGeorge USA</b><br>25 DataSet / 25 comments"));
		userList.add(new String("<b>USGS USA</b><br>243 DataSet / 25 comments"));
		userList.add(new String("<b>USGS USA</b><br>243 DataSet / 25 comments"));
		userList.add(new String("<b>USA NEWS Network</b><br>243 DataSet / 25 comments"));
//		userList.add(new String("abc"));
		
		
		//******************** init data set list
		
		datasetList.add(new String("<b>USA precipitation data</b><br>243 DataSet / 25 comments"));
		datasetList.add(new String("<b>USA Drought 1980-2010</b><br>243 DataSet / 25 comments"));
		datasetList.add(new String("<b>USA Drought 1980-2010</b><br>243 DataSet / 25 comments"));

		
	}
}
