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

	private String current_project_name = "";

	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public DataPack checkID(DataPack input) throws IllegalArgumentException {

		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		userAgent = escapeHtml(userAgent);
		DataPack returnPack = new DataPack();
		current_project_name = input.projectName;
		
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
