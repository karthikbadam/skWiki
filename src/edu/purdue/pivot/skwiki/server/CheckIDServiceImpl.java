package edu.purdue.pivot.skwiki.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.purdue.pivot.skwiki.client.CheckIDService;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CheckIDServiceImpl extends RemoteServiceServlet implements
CheckIDService {

	private String current_project_name = "";
	private String main_database_name = "";
	private String postgres_name = "postgres";
	private String postgres_password = "fujiko";
	Properties prop = new Properties();
	
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public DataPack checkID(DataPack input) throws IllegalArgumentException {

		/* read database details from file */
		BufferedReader br;
		InputStream config_file = null;
		
		//current_project_name = input.projectName;
		main_database_name = "mainbase";
	    
		try {
	    	
			//config_file = new FileInputStream(this.getServletContext().getRealPath("serverConfig.properties"));
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
		
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		userAgent = escapeHtml(userAgent);
		DataPack returnPack = new DataPack();
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}

		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			

			/* LOG */
			Date date= new Date();
			System.out.println(date.toString()+",checkid"+","+input.id+",");
			
			
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name, "postgres",
					"fujiko");
			st = connection.createStatement();
		
			rs = st.executeQuery("SELECT count(*) FROM"+" currentrevision ");
			while (rs.next()) {
				returnPack.updateRevision = rs.getInt(1);
			}
			
			st = connection.createStatement();

			rs = st.executeQuery("SELECT * FROM"+" currentrevision ");
			while (rs.next()) {
				String tempID = rs.getString(1);
				int revision = rs.getInt(2);
				int fromRevision = rs.getInt(3);
				String comment = rs.getString(4);
				RevisionHistory tempRevisionHistory = new RevisionHistory(tempID, revision,
						fromRevision,comment);
				returnPack.revisionList.add(tempRevisionHistory);
			}
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return returnPack;
	}
}
