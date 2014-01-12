package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;

public class SearchTagResult implements Serializable{
	String id = "";
	int revision = 0;
	//int fromRevision = 0;
	String entity_id = "";
	String tag = "";
			
	//String fromID = "";
	public SearchTagResult()
	{
		
	}
	
	public SearchTagResult(String id, int revision, String entity_id, String tag)
	{
		this.id = id;
		this.revision = revision;
	//	this.fromRevision = fromRevision;
		this.entity_id = entity_id;
		this.tag = tag;
	}
	
	@Override
	public String toString()
	{
		return id+" | "+revision+" from ";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	

	public String getEntity_id() {
		return entity_id;
	}

	public String getTag() {
		return tag;
	}
	
	
}
