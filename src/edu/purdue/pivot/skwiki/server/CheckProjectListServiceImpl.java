package edu.purdue.pivot.skwiki.server;

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

	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	public DataPack checkProjectList(DataPack input) throws IllegalArgumentException {


		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");


		System.err.println(input.updatedHistory.size()+"");
		System.out.println("Server Side code");
		String returnInput = input.toString();
		userAgent = escapeHtml(userAgent);
		String id = input.id;

		DataPack returnPack = new DataPack();
		String MAIN_DATABASE_NAME = "main";

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
					"jdbc:postgresql://127.0.0.1:5432/"+MAIN_DATABASE_NAME, "postgres",
					"984711");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";

			rs = st.executeQuery("SELECT count(*) FROM"+" projects ");
			//int rowCount = 0;
			while (rs.next()) {
				System.out.print(rs.getInt(1)+" ");
				returnPack.projectsNameList.add(rs.getString(1));
				//System.out.print(rs.getInt(1)+" ");
				
				//System.out.print(rs.getString(2)+" ");
			//	rowCount++;
			}
			
			System.out.print("project name list size"+returnPack.projectsNameList.size());

			
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+MAIN_DATABASE_NAME, "postgres",
					"984711");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";

			rs = st.executeQuery("SELECT count(*) FROM"+" users ");
			//int rowCount = 0;
			while (rs.next()) {
				System.out.print(rs.getInt(1)+" ");
				returnPack.userList.add(rs.getString(1));
				//System.out.print(rs.getInt(1)+" ");
				
				//System.out.print(rs.getString(2)+" ");
			//	rowCount++;
			}
			
			System.out.print("user name list size"+returnPack.userList.size());

			
			

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


/*
		for(AbstractHistory tempHistory: input.updatedHistory)
		{
			returnStr+=tempHistory.getType();
		}


		if(newID)
		{
			returnPack.newID = true;
		}
		else
		{
			returnPack.newID = false;

		}*/
		//return returnStr;
		return returnPack;
	}
}
