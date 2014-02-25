package edu.purdue.pivot.skwiki.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.purdue.pivot.skwiki.client.CheckProjectListService;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CheckProjectListServiceImpl extends RemoteServiceServlet implements
CheckProjectListService {

	private String current_project_name = "";
	private String main_database_name = "mainbase";
	private String postgres_name = "postgres";
	private String postgres_password = "fujiko";
	
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	public DataPack checkProjectList(DataPack input) throws IllegalArgumentException {
		
		/* read database details from file */
		BufferedReader br;
		current_project_name = "postchi_testing";
		main_database_name = "mainbase";
	    
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
	         
	        br.close();
	    
	    } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
	       
	    }
		
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");


		String returnInput = input.toString();
		userAgent = escapeHtml(userAgent);
		String id = input.id;

		DataPack returnPack = new DataPack();
		
		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();


		}

		System.out.println("id:  "+input.id+"");
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;


		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+main_database_name, "postgres",
					"fujiko");
			st = connection.createStatement();

			rs = st.executeQuery("SELECT count(*) FROM"+" projects ");

			while (rs.next()) {
				returnPack.projectsNameList.add(rs.getString(1));
			}
						
			st = connection.createStatement();

			rs = st.executeQuery("SELECT count(*) FROM"+" users ");

			while (rs.next()) {
				returnPack.userList.add(rs.getString(1));
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
