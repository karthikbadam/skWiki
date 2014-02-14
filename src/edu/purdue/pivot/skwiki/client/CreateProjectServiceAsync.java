package edu.purdue.pivot.skwiki.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.purdue.pivot.skwiki.shared.DataPack;

public interface CreateProjectServiceAsync {
	void createProject(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;
	
	void createUser(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;
	
	void authenticate(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;
	
	void getAllUserList(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;
	
	void getAllProjectList(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;

	void addUserToProject(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;

	void removeUserFromProject(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;
}
