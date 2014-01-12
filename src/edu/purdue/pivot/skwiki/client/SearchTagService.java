package edu.purdue.pivot.skwiki.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.purdue.pivot.skwiki.shared.DataPack;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("searchTag")
public interface SearchTagService extends RemoteService {
	DataPack searchTag(DataPack name) throws IllegalArgumentException;
}
