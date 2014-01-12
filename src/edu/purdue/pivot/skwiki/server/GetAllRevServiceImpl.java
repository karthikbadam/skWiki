package edu.purdue.pivot.skwiki.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.purdue.pivot.skwiki.client.GetAllRevService;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.SearchTagResult;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GetAllRevServiceImpl extends RemoteServiceServlet implements
GetAllRevService {

	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public DataPack getAllRev(DataPack input) throws IllegalArgumentException {


		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");


		System.err.println(input.updatedHistory.size()+"");
		System.out.println("Server Side code");
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
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");
			st = connection.createStatement();

			

			rs = st.executeQuery("SELECT * FROM"+" tag where entity_id="+
			"'"+input.entity_key+"'");
			//int rowCount = 0;
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				//returnPack.updateRevision = rs.getInt(1);
				int revision = rs.getInt(1);
				String uid = rs.getString(2);
				String entity_id = rs.getString(4);
				String tag = rs.getString(5);
				
				SearchTagResult tempResult = new SearchTagResult(uid, 
						revision, entity_id, tag);
				Integer tempInt = Integer.valueOf(revision);
				returnPack.allRevList.add(tempResult);
			
			}
			
			
			System.out.println("allRevList  size "+returnPack.allRevList.size());
			
			



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
