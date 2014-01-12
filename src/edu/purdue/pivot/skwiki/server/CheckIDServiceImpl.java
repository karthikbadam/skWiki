package edu.purdue.pivot.skwiki.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public DataPack checkID(DataPack input) throws IllegalArgumentException {


		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");


		//input.dataPlus1();
		System.err.println(input.updatedHistory.size()+"");
		System.out.println("Server Side code");
		String returnInput = input.toString();
		userAgent = escapeHtml(userAgent);
		String id = input.id;

		//String returnStr = "";

		//boolean newID = false;
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
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";

			rs = st.executeQuery("SELECT count(*) FROM"+" currentrevision ");
			//int rowCount = 0;
			while (rs.next()) {
				System.out.print(rs.getInt(1)+" ");
				returnPack.updateRevision = rs.getInt(1);
				//System.out.print(rs.getInt(1)+" ");
				
				//System.out.print(rs.getString(2)+" ");
			//	rowCount++;
			}
			
			
			
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";

			rs = st.executeQuery("SELECT * FROM"+" currentrevision ");
			//int rowCount = 0;
			while (rs.next()) {
				String tempID = rs.getString(1);
				int revision = rs.getInt(2);
				int fromRevision = rs.getInt(3);
				String comment = rs.getString(4);
				RevisionHistory tempRevisionHistory = new RevisionHistory(tempID, revision,
						fromRevision,comment);
				returnPack.revisionList.add(tempRevisionHistory);
				//System.out.print(rs.getString(2)+" ");
			//	rowCount++;
			}
			
			System.out.println(returnPack.revisionList.size()+"  revisionlist");

			/*if(rowCount==0)
			{
				newID = true;
			}
			else{
				newID = false;
			}


			if(!newID){
				//***************get the first number of all the existing histories
				//************** in history
				int lastHistoryCount = 0;

				//TODO
				rs = st.executeQuery("SELECT max(revision) from"+" revisionhistory where id ="+"\'"+id+"\'");
				while (rs.next()) {
					System.out.print(rs.getString(1)+" ");
					lastHistoryCount = rs.getInt(1);
				}
				
				returnPack.updateRevision = lastHistoryCount;
			}*/

			
			/*//************* checkout the sequence_id list
			rs = st.executeQuery("SELECT count(*) FROM"+" subrevision_table where  ");
			//int rowCount = 0;
			while (rs.next()) {
				System.out.print(rs.getInt(1)+" ");
				returnPack.updateRevision = rs.getInt(1);
				//System.out.print(rs.getInt(1)+" ");
				
				//System.out.print(rs.getString(2)+" ");
			//	rowCount++;
			}*/


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
