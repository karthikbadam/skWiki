package edu.purdue.pivot.skwiki.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.purdue.pivot.skwiki.shared.DataPack;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("createProject")
public interface CreateProjectService extends RemoteService {
	DataPack createProject(DataPack name) throws IllegalArgumentException;
	DataPack createUser(DataPack name) throws IllegalArgumentException;
	DataPack authenticate(DataPack name) throws IllegalArgumentException;
	DataPack getAllUserList(DataPack name) throws IllegalArgumentException;
	DataPack getAllProjectList(DataPack name) throws IllegalArgumentException;
	DataPack addUserToProject(DataPack name) throws IllegalArgumentException;
	DataPack removeUserFromProject(DataPack name) throws IllegalArgumentException;
}
