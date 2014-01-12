package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;
import java.util.ArrayList;

import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;

public class UserPack implements Serializable{

	
	public String userName = "";
	public String pwd ="";
	public boolean existed = false;
	public boolean authSuccess = false;
	public ArrayList<String> userList = new ArrayList<String>();
	
	public UserPack()
	{
		
	}
	
	public UserPack(String name, String pwd)
	{
		this.userName  = name;
		this.pwd = pwd;

	}
}
