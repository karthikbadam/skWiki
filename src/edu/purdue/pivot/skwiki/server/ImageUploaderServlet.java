/*
 * Adopted and Improved for skWiki by Karthik Badam 
 * 
 * Copyright 2009 Manuel Carrasco Moñino. (manuel_carrasco at users.sourceforge.net) 
 * http://code.google.com/p/gwtupload
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package edu.purdue.pivot.skwiki.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class sends by email, all the fields and files received by GWTUpload
 * servlet.
 * 
 * @author Manolo Carrasco Moñino
 * 
 */

public class ImageUploaderServlet extends UploadAction {

	private static final long serialVersionUID = 1L;

	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	/**
	 * Maintain a list with received files and their content types.
	 */
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();
	Hashtable<String, String> receivedFilePaths = new Hashtable<String, String>();

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rst = null;
		//	ArrayList<Integer> revisionHistoryList = new ArrayList<Integer>();

		System.out.println("Checking out service start~~~~~~~~~~~~~~~");

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");

			//int targetRevision = input.updateRevision;
			//int nonWholeSequence_id = input.nonWholeSequence_id;


			int directFromRevision = 0;

//			String selectStr = "select from_revision from currentrevision where revision = "
//					+targetRevision;
//			//revisionHistoryList.add(targetRevision);
//			System.out.println("targetRevision "+targetRevision);
//			st = connection.createStatement();
//			rs = st.executeQuery(selectStr);
//
//			while(rs.next())
//			{
//				directFromRevision = rs.getInt(1);
//
//				//canvasNameList.add(id);
//			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String response = "";
		int cont = 0;
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				cont++;
				try {
					// / Create a new file based on the remote file name in the
					// client
					// String saveName =
					// item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+",
					// "_");
					// File file =new File("/tmp/" + saveName);

					// / Create a temporary file placed in /tmp (only works in
					// unix)
					// File file = File.createTempFile("upload-", ".bin", new
					// File("/tmp"));

					// / Create a temporary file placed in the default system
					// temp folder
					String uuid = UUID.randomUUID().toString();
					String saveName = uuid + '.'
							+ FilenameUtils.getExtension(item.getName());
					
					//write to database
					
					File file = new File(saveName);
					item.write(file);

					// / Save a list with the received files
					receivedFiles.put(item.getFieldName(), file);
					receivedContentTypes.put(item.getFieldName(),
							item.getContentType());
					receivedFilePaths.put(item.getFieldName(), file.getAbsolutePath());

					// / Send a customized message to the client.
					response += "File saved as " + file.getAbsolutePath();

				} catch (Exception e) {
					throw new UploadActionException(e.getMessage());
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send your customized message to the client.
		return response;
	}

	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String fieldName = request.getParameter(UConsts.PARAM_SHOW);
		//File f = receivedFiles.get(fieldName);
		
		File f = new File(receivedFilePaths.get(fieldName));
		if (f != null) {
			response.setContentType(receivedContentTypes.get(fieldName));
			FileInputStream is = new FileInputStream(f);
			copyFromInputStreamToOutputStream(is, response.getOutputStream());
		} else {
			renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null) {
			file.delete();
		}
	}
}
