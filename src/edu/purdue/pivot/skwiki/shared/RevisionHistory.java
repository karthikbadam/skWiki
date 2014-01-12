package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;

public class RevisionHistory implements Serializable{
	public String id = "";
	int revision = 0;
	int fromRevision = 0;
	String comment = "";
			
	//String fromID = "";
	public RevisionHistory()
	{
		
	}
	
	public RevisionHistory(String id, int revision, int fromRevision, String comment)
	{
		this.id = id;
		this.revision = revision;
		this.fromRevision = fromRevision;
		this.comment = comment;
	}
	
	@Override
	public String toString()
	{
		return id+" | "+revision+" from "+fromRevision;
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

	public int getFromRevision() {
		return fromRevision;
	}

	public void setFromRevision(int fromRevision) {
		this.fromRevision = fromRevision;
	}

	public String getComment() {
		return comment;
	}
	
	
}
