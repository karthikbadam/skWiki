package edu.purdue.pivot.skwiki.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.purdue.pivot.skwiki.shared.DataPack;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface CheckProjectListServiceAsync {
	void checkProjectList(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;
}
