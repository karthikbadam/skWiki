package edu.purdue.pivot.skwiki.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.purdue.pivot.skwiki.shared.DataPack;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("getAllRev")
public interface GetAllRevService extends RemoteService {
	DataPack getAllRev(DataPack name) throws IllegalArgumentException;
}
