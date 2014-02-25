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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
	private String current_project_name = "";
	private String main_database_name = "";
	private String postgres_name = "postgres";
	private String postgres_password = "fujiko";
	
	
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
	
	Connection connection; 
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		
		connection = null;
		Statement st = null;
		
		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";
	    
		try {
	    	br = new BufferedReader(new FileReader(this.getServletContext().getRealPath("/serverConfig.txt")));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            String first = line.substring(0, line.lastIndexOf(':'));
	            String last = line.substring(line.lastIndexOf(':') + 1);
	            
	            if (first.contains("content_database")) {
	            	current_project_name = last;
	            } 
	            
	            if (first.contains("owner_database")) {
	            	main_database_name = last;
	            }
	            
	            if (first.contains("username")) {
	            	postgres_name = last;
	            }
	            
	            if (first.contains("password")) {
	            	postgres_password = last;
	            }
	            
	        	sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        
	        
	        //String everything = sb.toString();
	        //System.out.println("file: "+everything);
	        br.close();
	    
	    } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
	       
	    }

			
		try {			
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name, "postgres",
					"fujiko");
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
					String uuid = UUID.randomUUID().toString();
					String saveName = uuid + '.'
							+ FilenameUtils.getExtension(item.getName());
					
//					String saveName = item.getName();
					
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
					
					//write filename to database
					
					String selectStr = "insert into images values ("+"\'" + item.getFieldName() + "\'," + "\'" + file.getAbsolutePath() + "\'" + ")";
					st = connection.createStatement();
					int textReturnCode = st.executeUpdate(selectStr);

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
		
		String selectStr = "select path from images where field_name = \'"+fieldName+"\'";
		Statement st = null;
		ResultSet rs = null;
		ResultSet rst = null;
		String receivedFilePath = receivedFilePaths.get(fieldName);
		
		try {
			st = connection.createStatement();
			rs = st.executeQuery(selectStr);
			while(rs.next())
			{
				receivedFilePath = rs.getString(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		File f = new File(receivedFilePath);
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
