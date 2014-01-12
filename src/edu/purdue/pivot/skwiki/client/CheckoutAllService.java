package edu.purdue.pivot.skwiki.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.purdue.pivot.skwiki.shared.DataPack;
;

@RemoteServiceRelativePath("checkoutall")
public interface CheckoutAllService extends RemoteService {
	DataPack checkoutAll(DataPack name) throws IllegalArgumentException;
}
