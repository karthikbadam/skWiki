package edu.purdue.pivot.skwiki.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.purdue.pivot.skwiki.client.CheckoutSubRev;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CheckoutSubRevImpl extends RemoteServiceServlet implements
CheckoutSubRev {

	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	public DataPack checkoutSubRev(DataPack input) throws IllegalArgumentException {


		
		System.out.println("checout Sub revision start");
		String returnInput = input.toString();
		String id = input.id;
		int checkoutRevision = input.updateRevision;
		
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
					"jdbc:postgresql://127.0.0.1:5432/m_test_1", "postgres",
					"984711");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";

			rs = st.executeQuery("SELECT max(sequence_id) FROM"+" subrevision_table where revision =  "
			         +checkoutRevision);
			//int rowCount = 0;
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				returnPack.maxnonWholeSequence_id = rs.getInt(1);
			}
			System.out.println("max sequence_id "+ returnPack.maxnonWholeSequence_id);


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
