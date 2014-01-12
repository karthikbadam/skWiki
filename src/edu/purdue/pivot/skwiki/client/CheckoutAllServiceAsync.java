package edu.purdue.pivot.skwiki.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.purdue.pivot.skwiki.shared.DataPack;

public interface CheckoutAllServiceAsync {
	void checkoutAll(DataPack input, AsyncCallback<DataPack> callback)
			throws IllegalArgumentException;
}