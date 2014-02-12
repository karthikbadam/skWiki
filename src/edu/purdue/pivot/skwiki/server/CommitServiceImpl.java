package edu.purdue.pivot.skwiki.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.purdue.pivot.skwiki.client.CommitService;
import edu.purdue.pivot.skwiki.shared.AbstractLayoutHistory;
import edu.purdue.pivot.skwiki.shared.AddToParentHistory;
import edu.purdue.pivot.skwiki.shared.CanvasPack;
import edu.purdue.pivot.skwiki.shared.ChangePosHistory;
import edu.purdue.pivot.skwiki.shared.ChangeSizeHistory;
import edu.purdue.pivot.skwiki.shared.CreateEntityHistory;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.ImagePack;
import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;
import edu.purdue.pivot.skwiki.shared.history.AddHistory;
import edu.purdue.pivot.skwiki.shared.history.PathHeadHistory;
import edu.purdue.pivot.skwiki.shared.history.RemoveHistory;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CommitServiceImpl extends RemoteServiceServlet implements
		CommitService {

	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	/*
	 * public void checkUpdateID(String id, String fromUID, int revision) {
	 * 
	 * //******* check if the ID is in the id list
	 * 
	 * if(checkIfInList(revision)) //*******if yes, continue committing in the
	 * id ended tables { current_database_end = id;
	 * System.out.println("coninue committing"); }
	 * 
	 * 
	 * 
	 * //******* if not, create a new set of tables with names ending with id
	 * else { current_database_end = id; System.out.println(" creating tables");
	 * createTablesIdEnds(id, fromUID); } }
	 */

	public void createTablesIdEnds(String id, String fromUID, int newRevision) {

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
		ResultSet rst = null;
		int id_count = 0;
		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			int createCode = 0;
			// ******** create history table
			// ****************** build tables based on the branch where this
			// revision is from
			// if(newRevision!=1)
			// ********** the creating table scheme from existing tables is no
			// longer needed
			if (false) {
				createCode = st.executeUpdate("CREATE table history_" + id
						+ "_" + newRevision + " as (select * from history_"
						+ fromUID + "_" + (newRevision - 1) + " " + ");");
				createCode = st.executeUpdate("CREATE table text_" + id + "_"
						+ newRevision + " as (select * from text_" + fromUID
						+ "_" + (newRevision - 1) + " " + ");");
				/*
				 * createCode =
				 * st.executeUpdate("CREATE table revisionhistory_"+id+
				 * "_"+newRevision+
				 * //" as (select * from revisionhistory_"+fromUID
				 * //+"_"+(newRevision-1) //+" " + ");" );
				 */
				createCode = st.executeUpdate("CREATE table layout_" + id + "_"
						+ newRevision + " as (select * from layout_" + fromUID
						+ "_" + (newRevision - 1) + " " + ");");
			}
			// ****************** has 0 revision
			else {
				createCode = st.executeUpdate("CREATE table history"
						+
						// "_"+id+
						"_" + newRevision + "(" + "id varchar(50),"
						+ "type int," + "sx int," + "sy int," + "ex int,"
						+ "ey int," + "r int," + "g int," + "b int,"
						+ "strokeSize int ," + "eraserSize int,"
						+ "historyNumber int" +
						// ") " +
						// "as (select * from history_"+fromUID+" " +
						");");

				// ******** create text table
				st = connection.createStatement();
				createCode = st.executeUpdate("CREATE table text"
						+
						// "_"+id+
						"_" + newRevision + "(" + "id varchar(50),"
						+ "subrevision int," + "" + "textbody varchar(20000)"
						+ ");");
				
				// ******* create image table 
				st = connection.createStatement();
				createCode = st.executeUpdate("CREATE table image"
						+
						// "_"+id+
						"_" + newRevision + "(" + "id varchar(50),"
						+ "subrevision int," + "pox_x int," + "pox_y int," 
						+ "width int," + "height int," 
						+ "size_x int," + "size_y int," + "URL varchar(20000)"
						+ ");");
				

				/*
				 * st = connection.createStatement(); createCode =
				 * st.executeUpdate("CREATE table textRev_"+id+ "_"+newRevision+
				 * "(" +"id varchar(50),"+ "revision int,"+
				 * "last_subrevsision int"+ ");" );
				 */

				/*
				 * //******** create revisionhistory table st =
				 * connection.createStatement(); createCode =
				 * st.executeUpdate("CREATE table revisionhistory_"+id+
				 * "_"+newRevision+ "(" +"id varchar(50),"+ "revision int,"+
				 * "lowb int,"+ "highb int"+ ");" );
				 */

				// ******** create layout table
				st = connection.createStatement();
				createCode = st.executeUpdate("CREATE table layout"
						+
						// "_"+id+
						"_" + newRevision + "(" + "id varchar(50),"
						+ "parentid varchar(20)," + "pox_x int," + "pox_y int,"
						+ "type varchar(10)," + "entity_type varchar(10),"
						+ "size_x int," + "size_y int," + "subrevision int"
						+ ");");
				/*
				 * st = connection.createStatement(); createCode =
				 * st.executeUpdate("CREATE table layoutrev_"+id+
				 * "_"+newRevision+ "(" +"id varchar(50),"+ "revision int,"+
				 * "last_subrevision int"+ ");" );
				 */
			}

			// ******** create revisionhistory table
			st = connection.createStatement();
			createCode = st.executeUpdate("CREATE table revisionhistory"
					+
					// "_"+id+
					"_" + newRevision + "(" + "id varchar(50),"
					+ "subrevision int," + "lowb int," + "highb int" + ");");

			/*
			 * st = connection.createStatement(); createCode =
			 * st.executeUpdate("CREATE table revisionhistoryrev_"+id+
			 * "_"+newRevision+ "(" +"id varchar(50),"+ "revision int,"+
			 * "last_subrevision int"+ ");" );
			 */

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/*
	 * public boolean checkIfInList(int revision) { try {
	 * 
	 * Class.forName("org.postgresql.Driver");
	 * 
	 * } catch (ClassNotFoundException e) {
	 * 
	 * System.out.println("Where is your PostgreSQL JDBC Driver? " +
	 * "Include in your library path!"); e.printStackTrace();
	 * 
	 * 
	 * }
	 * 
	 * 
	 * Connection connection = null; Statement st = null; ResultSet rs = null;
	 * ResultSet rst = null; int id_count = 0; try {
	 * 
	 * connection = DriverManager.getConnection(
	 * "jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres", "fujiko");
	 * st = connection.createStatement();
	 * 
	 * 
	 * 
	 * 
	 * rs = st.executeQuery("SELECT count(*) from"+" currentRevision"+ " where"
	 * +" revision="+"\'"+revision+"\'"); while (rs.next()) { //********** since
	 * the canvas has new revision number already, not +1 here id_count =
	 * rs.getInt(1); System.out.println("newRevision "+id_count); } } catch
	 * (SQLException e) {
	 * 
	 * System.out.println("Connection Failed! Check output console");
	 * e.printStackTrace(); } finally { try { if (rs != null) { rs.close(); } if
	 * (st != null) { st.close(); } if (connection != null) {
	 * connection.close(); }
	 * 
	 * // } catch (SQLException ex) { } catch (Exception ex) {
	 * ex.printStackTrace(); } }
	 * 
	 * if(id_count==0) { return false; } else { return true; } }
	 */

	// private void updateRevisionNumber(DataPack result)
	// {
	// try {
	//
	// Class.forName("org.postgresql.Driver");
	// } catch (ClassNotFoundException e) {
	// System.out.println("Where is your PostgreSQL JDBC Driver? "
	// + "Include in your library path!");
	// e.printStackTrace();
	// }
	//
	//
	// Connection connection = null;
	// Statement st = null;
	// ResultSet rs = null;
	// int id_count = 0;
	// try {
	//
	// connection = DriverManager.getConnection(
	// "jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
	// "fujiko");
	// st = connection.createStatement();
	// rs = st.executeQuery("SELECT count(*) from"+" currentRevision");//+
	// while (rs.next()) {
	//
	// //********** since the canvas has new revision number already, not +1
	// here
	// id_count = rs.getInt(1);
	// System.out.println("newRevision "+id_count);
	// }
	//
	//
	// } catch (SQLException e) {
	//
	// System.out.println("Connection Failed! Check output console");
	// e.printStackTrace();
	//
	// }
	// finally {
	// try {
	// if (rs != null) {
	// rs.close();
	// }
	// if (st != null) {
	// st.close();
	// }
	// if (connection != null) {
	// connection.close();
	// }
	//
	// // } catch (SQLException ex) {
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	// result.updateRevision = id_count;
	//
	// }

	// private int getCurrentRevision(String id, int fromRevision, String
	// comment)
	// {
	// try {
	//
	// Class.forName("org.postgresql.Driver");
	// } catch (ClassNotFoundException e) {
	// System.out.println("Where is your PostgreSQL JDBC Driver? "
	// + "Include in your library path!");
	// e.printStackTrace();
	// }
	//
	//
	// Connection connection = null;
	// Statement st = null;
	// ResultSet rs = null;
	// int id_count = 0;
	// try {
	//
	// connection = DriverManager.getConnection(
	// "jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
	// "fujiko");
	// st = connection.createStatement();
	// rs = st.executeQuery("SELECT count(*) from"+" currentRevision"
	// //+" where id = "+"'"+id+"'"
	// );//+
	// while (rs.next()) {
	//
	// //********** since the canvas has new revision number already, not +1
	// here
	// id_count = rs.getInt(1);
	// System.out.println("current revision "+id_count);
	// }
	//
	//
	// st = connection.createStatement();
	//
	//
	// System.out.println("finish fetch id count "+id_count );
	// /*int newRevision = 0;
	// rs = st.executeQuery("SELECT count(*) from"+" currentrevision");
	// while (rs.next()) {
	//
	// //********** since the canvas has new revision number already, not +1
	// here
	// newRevision = rs.getInt(1)+1;
	//
	//
	// System.out.println("newRevision "+newRevision);
	// }*/
	//
	// String insertHead="insert into ";
	// String insertTable = "currentrevision ";
	// String values = "values "+"("+
	// "\'"+id+"\'"+","+
	// (id_count+1)+","+
	// fromRevision+","+
	// "\'"+comment+"\'"+
	// ")";
	// int textReturnCode = st.executeUpdate(insertHead+insertTable+values );
	// System.out.println("insert into current revision "+(id_count+1));
	//
	//
	// } catch (SQLException e) {
	//
	// System.out.println("Connection Failed! Check output console");
	// e.printStackTrace();
	//
	// }
	// finally {
	// try {
	// if (rs != null) {
	// rs.close();
	// }
	// if (st != null) {
	// st.close();
	// }
	// if (connection != null) {
	// connection.close();
	// }
	//
	// // } catch (SQLException ex) {
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	// return id_count;
	// }

	public void updateTags(DataPack input) {

		String uid = input.id;
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
		ResultSet rst = null;
		int id_count = 0;
		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			rs = st.executeQuery("SELECT count(*) from" + " currentRevision");// +
			// " where" +" id="+"\'"+id+"\'");
			while (rs.next()) {
				// System.out.print(rs.getString(1)+" ");

				// ********** since the canvas has new revision number already,
				// not +1 here
				// newRevision = rs.getInt(1)+1;
				id_count = rs.getInt(1);

				System.out.println("newRevision " + id_count);
			}

			int newRevision = id_count + 1;
			for (int i = 0; i < input.textTaglMap.size(); i++) {
				String key = (String) input.textTaglMap.keySet().toArray()[i];
				String tag = (String) input.textTaglMap.values().toArray()[i];
				// commitOneText(key, tempText);

				// ****** update the return package
				// returnPack.updateCanvasMap.put(key, tempReturnPack);

				String insertHead = "insert into ";
				String insertTable = "tag ";
				String values = "values " + "(" + newRevision + "," + "\'"
						+ uid + "\'" + "," + "\'" + "Text" + "\'" + "," + "\'"
						+ key + "\'" + "," + "\'" + tag + "\'" + ")";
				st = connection.createStatement();
				int textReturnCode = st.executeUpdate(insertHead + insertTable
						+ values);

			}

			for (int i = 0; i < input.canvasTagMap.size(); i++) {
				String key = (String) input.canvasTagMap.keySet().toArray()[i];
				String tag = (String) input.canvasTagMap.values().toArray()[i];
				// commitOneText(key, tempText);

				// ****** update the return package
				// returnPack.updateCanvasMap.put(key, tempReturnPack);

				String insertHead = "insert into ";
				String insertTable = "tag ";
				String values = "values " + "(" + newRevision + "," + "\'"
						+ uid + "\'" + "," + "\'" + "Canvas" + "\'" + ","
						+ "\'" + key + "\'" + "," + "\'" + tag + "\'" + ")";
				st = connection.createStatement();
				int textReturnCode = st.executeUpdate(insertHead + insertTable
						+ values);

			}

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public void copyLastSubrevisionInfo(int newRevision, int fromRevision) {
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rst = null;
		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			/*
			 * int newRevision = 0; rs =
			 * st.executeQuery("SELECT count(*) from"+" currentrevision"); while
			 * (rs.next()) {
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here newRevision = rs.getInt(1)+1;
			 * 
			 * 
			 * System.out.println("newRevision "+newRevision); }
			 */

			String insertHead = "insert into ";
			String insertTable = "lastrevision ";
			String selectStr = " select uid, entity_id, "
					+
					// "workingrevision" +
					newRevision
					+ ", revision, last_subrevision,entity_type from lastrevision ";
			String values = " "
					+ "("
					// "\'"+id+"\'"+","+
					// newRevision+","+
					// fromRevision+","+
					// "\'"+comment+"\'"+
					+ "uid, entity_id, workingrevision, revision, last_subrevision,entity_type"
					+ ")";

			String whereStr = " where workingrevision = " + fromRevision;
			System.out.println(insertHead + insertTable + values + selectStr
					+ whereStr);
			int textReturnCode = st.executeUpdate(insertHead + insertTable
					+ values + selectStr + whereStr);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public int getExistingSubrevison_tableIndex(int targetRevision) {
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rst = null;
		int existingSubRev = 0;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			/*
			 * int newRevision = 0; rs =
			 * st.executeQuery("SELECT count(*) from"+" currentrevision"); while
			 * (rs.next()) {
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here newRevision = rs.getInt(1)+1;
			 * 
			 * 
			 * System.out.println("newRevision "+newRevision); }
			 */

			String insertHead = "insert into ";
			String insertTable = "lastrevision ";
			String selectStr = " select max(sequence_id) from subrevision_table where revision = "
					+
					// "workingrevision" +
					targetRevision;

			// ", revision, last_subrevision,entity_type from lastrevision ";
			/*
			 * String values = " "+"(" //"\'"+id+"\'"+","+ //newRevision+","+
			 * //fromRevision+","+ //"\'"+comment+"\'"+ +
			 * "uid, entity_id, workingrevision, revision, last_subrevision,entity_type"
			 * + ")";
			 */

			// String whereStr =" where workingrevision = "+fromRevision;
			System.out.println(selectStr);
			// int textReturnCode = st.executeUpdate(selectStr );
			rs = st.executeQuery(selectStr);
			while (rs.next()) {

				// ********** since the canvas has new revision number already,
				// not +1 here
				existingSubRev = rs.getInt(1);
				System.out.println("current maximum of sequence_ID in"
						+ "the subrevision_table table " + existingSubRev);
			}

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return existingSubRev;
	}

	@Override
	public DataPack greetServer(DataPack input) throws IllegalArgumentException {

		DataPack returnPack = new DataPack();
		String id = input.id;
		String fromUID = input.fromUID;
		int fromRevision = input.fromRevision;
		String comment = input.comment;
		// ******* check if the ID is in the id list
		// ******* and update the current revision list
		// checkUpdateID(id, fromUID);
		// int newRevision = getCurrentRevision(id, fromRevision, comment)+1;

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
		int id_count = 0;
		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			// System.out.println("finish fetch id count "+id_count );
			/*
			 * int newRevision = 0; rs =
			 * st.executeQuery("SELECT count(*) from"+" currentrevision"); while
			 * (rs.next()) {
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here newRevision = rs.getInt(1)+1;
			 * 
			 * 
			 * System.out.println("newRevision "+newRevision); }
			 */

			String insertHead = "insert into ";
			String insertTable = "currentrevision(id, from_revision, comment) ";
			String values = "values " + "(" + "\'" + id + "\'" + "," +
			/* (id_count+1)+","+ */
			fromRevision + "," + "\'" + comment + "\'" + ")";
			int textReturnCode = st.executeUpdate(insertHead + insertTable
					+ values);
			
			rs = st.executeQuery("SELECT revision from currentRevision"
					+ " where from_revision = " + fromRevision +" and id = "+ "\'" + id + "\'");
			while (rs.next()) {

				// ********** since the canvas has new revision number already,
				// not +1 here
				id_count = rs.getInt(1);
				
			}
			System.out.println("current revision is --" + id_count+" and previous revision --"+fromRevision);
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		returnPack.updateRevision = id_count;

		int newRevision = id_count;

		// int existingMaxSub_IN_subrevisont_table =
		// getExistingSubrevison_tableIndex(targetRevision)
		createTablesIdEnds(id, fromUID, newRevision);
		String current_database_end = id;

		System.out.println("commit~~~~~~");

		// ********* copy revison info from fromRevision to newRevision
		copyLastSubrevisionInfo(newRevision, fromRevision);

		// ********* commit the canvas

		for (int i = 0; i < input.updateCanvasMap.size(); i++) {
			String key = (String) input.updateCanvasMap.keySet().toArray()[i];
			CanvasPack tempCanvasPack = (CanvasPack) input.updateCanvasMap
					.values().toArray()[i];

			// ****** new
			tempCanvasPack.updateRevision = fromRevision;
			CanvasPack tempReturnPack = commitOneCanvas(tempCanvasPack,
					input.fromUID, newRevision, id);

			// ****** update the return package
			returnPack.updateCanvasMap.put(key, tempReturnPack);
		}

		// ********* commit the text
		for (int i = 0; i < input.updateHtmlMap.size(); i++) {
			String key = (String) input.updateHtmlMap.keySet().toArray()[i];
			String tempText = (String) input.updateHtmlMap.values().toArray()[i];
			commitOneText(key, tempText);

			// ****** update the return package
		}

		/* Commit images */
		for (int i = 0; i < input.updateImageMap.size(); i++) {
			String key = (String) input.updateImageMap.keySet().toArray()[i];
			ImagePack tempImagePack = input.updateImageMap.get(key);
			commitOneImage(key, newRevision, tempImagePack);
		}
		
		// ******** commit tags
		updateTags(input);

		// ********* commit the layoutList
		if (input.layoutHistoryList.size() > 0) {
			commitOneLayoutHistory(input, newRevision, current_database_end);
		}

		returnPack.layoutHisotrySettleIndex = input.layoutHistoryList.size();

		// ******* commit update current revision
		// commitCurrentRevision(id, fromRevision,comment, newRevision);

		// updateRevisionNumber(returnPack);

		return returnPack;

	}

	public void commitCurrentRevision(String id, int fromRevision,
			String comment, int newRevision) {
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
		ResultSet rst = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			/*
			 * int newRevision = 0; rs =
			 * st.executeQuery("SELECT count(*) from"+" currentrevision"); while
			 * (rs.next()) {
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here newRevision = rs.getInt(1)+1;
			 * 
			 * 
			 * System.out.println("newRevision "+newRevision); }
			 */

			String insertHead = "insert into ";
			String insertTable = "currentrevision ";
			String values = "values " + "(" + "\'" + id + "\'" + ","
					+ newRevision + "," + fromRevision + "," + "\'" + comment
					+ "\'" + ")";
			int textReturnCode = st.executeUpdate(insertHead + insertTable
					+ values);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public void commitOneLayoutHistory(DataPack input, int newRevision,
			String current_database_end) {

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
		ResultSet rst = null;

		String insertHead = "insert into ";

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			/*
			 * int newRevision = 0; rs =
			 * st.executeQuery("SELECT count(*) from"+" currentrevision"); while
			 * (rs.next()) { //System.out.print(rs.getString(1)+" ");
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here //newRevision = rs.getInt(1)+1; newRevision =
			 * rs.getInt(1)+1;
			 * 
			 * 
			 * System.out.println("newRevision "+newRevision); }
			 */

			String values = "";
			int existingSubRev = 0;
			System.out.println("");
			System.out.println("commit layout Start~~~~~~~~~~");
			System.out.println("new revision " + newRevision);
			System.out.println("number of updated layout histories "
					+ input.layoutHistoryList.size());
			// System.out.println("current user ID as current_database_end "+current_database_end);

			int i = 0;
			for (AbstractLayoutHistory tempHistory : input.layoutHistoryList) {

				System.out.println("commiting layout history, iteration NO."
						+ i);
				st = connection.createStatement();
				rs = st.executeQuery("select count(subrevision) from layout" +
				// "_"+current_database_end
				// +
						"_" + (newRevision) +
						// " where id = "+"\'"+id+"\'"+
						// " and uid = " +"\'"+UID+"\'"+
						// " and revision = " +"\'"+(newRevision-1)+"\'"+
						"; ");
				while (rs.next()) {

					// ********** since the canvas has new revision number
					// already, not +1 here
					existingSubRev = rs.getInt(1);
					System.out.println("current count of layout history in"
							+ "the working layout table " + existingSubRev);
				}

				if (tempHistory instanceof CreateEntityHistory) {
					// insertTable = CreateEntityHistory;
					values = "values "
							+ "("
							+ "\'"
							+ ((CreateEntityHistory) tempHistory)
									.getOperatingObject() + "\', " + "\'"
							+ "parent" + "\', " + 0
							+ ", "
							+ // pos_x
							0
							+ ", "
							+ // pos_y
							"\'" + "c" + "\', " + "\'"
							+ ((CreateEntityHistory) tempHistory).editorType
							+ "\', " + 0 + ", " + // size_x
							0 + ", " + // size_x
							(existingSubRev + 1) + ")";
				} else if (tempHistory instanceof AddToParentHistory) {
					// insertTable = "AddToParentHistory ";
					values = "values "
							+ "("
							+ "\'"
							+ ((AddToParentHistory) tempHistory)
									.getOperatingObject()
							+ "\', "
							+ "\'"
							+ ((AddToParentHistory) tempHistory)
									.getParentName() + "\', " + 0 + ", "
							+ // pos_x
							0 + ", "
							+ // pos_y
							"\'" + "a" + "\', " + "\'" + "null" + "\', " + 0
							+ ", " + // size_x
							0 + ", " + // size_x
							(existingSubRev + 1) + ")";
				} else if (tempHistory instanceof ChangeSizeHistory) {
					// insertTable = "removehistory ";
					values = "values "
							+ "("
							+ "\'"
							+ ((ChangeSizeHistory) tempHistory)
									.getOperatingObject() + "\', " + "\'"
							+ "null" + "\', " + 0
							+ ", "
							+ // pos_x
							0
							+ ", "
							+ // pos_y
							"\'" + "cs" + "\', " + "\'" + "null" + "\', "
							+ ((ChangeSizeHistory) tempHistory).getNewX()
							+ ", " + // size_x
							((ChangeSizeHistory) tempHistory).getNewY() + ", " + // size_y
							(existingSubRev + 1) + ")";
				} else if (tempHistory instanceof ChangePosHistory) {
					// insertTable = "removehistory ";
					values = "values "
							+ "("
							+ "\'"
							+ ((ChangePosHistory) tempHistory)
									.getOperatingObject() + "\', " + "\'"
							+ "null" + "\', "
							+ ((ChangePosHistory) tempHistory).getNewX()
							+ ", "
							+ // pos_x
							((ChangePosHistory) tempHistory).getNewY() + ", "
							+ // pos_y
							"\'" + "cp" + "\', " + "\'" + "null" + "\', " + 0
							+ ", " + // size_x
							0 + ", " + // size_x
							(existingSubRev + 1) + ")";
				}

				String insertTable = "layout" +
				// "_"+current_database_end+"" +
						"_" + newRevision + " ";
				// System.out.println("values "+values);
				int returnCode = st.executeUpdate(insertHead + insertTable
						+ values);

				i++;
			}

			/*
			 * int existingSubRev = 0; st = connection.createStatement(); rs =
			 * st.executeQuery("select max(subrevision) from layout_"+
			 * current_database_end +"_"+(newRevision)+
			 * //" where id = "+"\'"+id+"\'"+ //" and uid = " +"\'"+UID+"\'"+
			 * //" and revision = " +"\'"+(newRevision-1)+"\'"+ "; "); while
			 * (rs.next()) {
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here existingSubRev = rs.getInt(1);
			 * System.out.println("current layout existingSubRev "
			 * +existingSubRev); }
			 * 
			 * st = connection.createStatement(); int lastSubRev = 1;
			 * if(existingSubRev==0) { existingSubRev = 1; }
			 * 
			 * 
			 * 
			 * //********* insert into lastrevision table to store the
			 * "last_subrevision" number String insertTable =
			 * "lastrevision"//+current_database_end //+"_"+newRevision +" ";
			 * values = "values "+"("+ "\'"+input.id+"\', "+
			 * "\'"+"layout"+"\', "+ newRevision+", "+ +(existingSubRev) +", "+
			 * "\'"+"L"+"\'"+ ")";
			 * 
			 * //st.addBatch(insertHead+insertTable+values );
			 * 
			 * //int returnCode[] = st.executeBatch(); //connection.commit();
			 * int returnCode = st.executeUpdate(insertHead+insertTable+values
			 * );
			 */

			System.out
					.println("Commiting lastrevision table with last_subrevision "
							+ (existingSubRev + 1));

			st = connection.createStatement();
			String insertTable = "lastrevision"// +current_database_end
					// +"_"+newRevision
					+ " ";
			values = "values " + "(" + "\'" + input.id + "\', " + "\'"
					+ "layout" + "\', " + newRevision + ", " + newRevision
					+ ", " + +(existingSubRev + 1) + ", " + "\'" + "L" + "\'"
					+ ")";

			// st.addBatch(insertHead+insertTable+values );

			// int returnCode[] = st.executeBatch();
			// connection.commit();
			int returnCode = st.executeUpdate("insert into " + insertTable
					+ values);

			insertTable = "subrevision_table"// +current_database_end
					// +"_"+newRevision
					+ " ";
			values = "values " + "(" + "\'" + current_database_end + "\', "
					+ "\'" + "layout" + "\', " + newRevision + ", "
					+ (existingSubRev + 1) + ", " + "\'" + "L" + "\'" + ", "
					+ +(1) + ")";

			// st.addBatch(insertHead+insertTable+values );

			// int returnCode[] = st.executeBatch();
			// connection.commit();
			returnCode = st.executeUpdate(insertHead + insertTable + values);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public void commitOneText(String key, String updateText) {
		String id = key;
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
		ResultSet rst = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			int newRevision = 0;
			rs = st.executeQuery("SELECT count(*) from" + " currentrevision");
			while (rs.next()) {
				// System.out.print(rs.getString(1)+" ");

				// ********** since the canvas has new revision number already,
				// not +1 here
				// newRevision = rs.getInt(1)+1;
				newRevision = rs.getInt(1) + 1;

				System.out.println("newRevision " + newRevision);
			}

			String insertHead = "insert into ";
			String insertTable = "text_"
			// +current_database_end
			// +"_"
					+ newRevision + " ";
			String values = "values " + "(" + "\'" + id + "\', " + newRevision
					+ ", " + "\'" + updateText + "\' " + ")";
			int textReturnCode = st.executeUpdate(insertHead + insertTable
					+ values);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
	
	public void commitOneImage(String key, int newRevision, ImagePack imagePack) {

		String id = key;
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
		ResultSet rst = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();

			String insertHead = "insert into ";
			String insertTable = "image_" + newRevision + " ";
			String values = "values " + "(" + "\'" + id + "\', " + newRevision
					+ ", " + imagePack.leftX + "," + imagePack.topY + ", "
					+ imagePack.width + "," + imagePack.height + ","
					+ imagePack.layoutWidth + "," + imagePack.layoutHeight
					+ ",\' " + imagePack.URL + "\'" + ")";
			
			int imageReturnCode = st.executeUpdate(insertHead + insertTable
					+ values);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		} finally {
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

				// } catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	

	public CanvasPack commitOneCanvas(CanvasPack input, String fromUID,
			int newRevision, String UID) throws IllegalArgumentException {

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		System.out.println("update hsitory size: "
				+ input.updatedHistory.size() + "");
		String returnInput = input.toString();
		userAgent = escapeHtml(userAgent);
		String id = input.id;
		String returnStr = "";
		int lastHistoryCount = 0;
		CanvasPack returnPack = new CanvasPack();
		String current_from_database_end = fromUID;

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}

		System.out.println("id:  " + input.id + "");
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rst = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing",
					"postgres", "fujiko");
			st = connection.createStatement();
			String insertHead = "insert into ";
			String insertTable = "";
			String values = "";

			// ***************get the first number of all the existing histories
			// ************** in history
			rs = st.executeQuery("SELECT count(*) from" + " history" +
			// "_"+current_from_database_end+
					"_" + (newRevision) + " where id =" + "\'" + id + "\'");// *****
																			// old
																			// revision
																			// number

			while (rs.next()) {
				lastHistoryCount = rs.getInt(1);
			}

			// *********************** insert history
			int counter = lastHistoryCount + 1;
			for (AbstractHistory tempHistory : input.updatedHistory) {
				if (tempHistory instanceof AddHistory) {
					// insertTable = "addhistory ";
					values = "values " + "(" + "\'" + id + "\', "
							+ 1
							+ ", "
							+ tempHistory.getPosition().getVector2().getIntX()
							+ ", "
							+ // sx
							tempHistory.getPosition().getVector2().getIntY()
							+ ", "
							+ // sy
							((AddHistory) tempHistory).endPos.getVector2()
									.getIntX()
							+ ", "
							+ // ex
							((AddHistory) tempHistory).endPos.getVector2()
									.getIntY() + ", " + // ey

							((AddHistory) tempHistory).pathColor.getR() + ", " + // R
							((AddHistory) tempHistory).pathColor.getG() + ", " + // G
							((AddHistory) tempHistory).pathColor.getB() + ", " + // B
							((AddHistory) tempHistory).strokeSize + ", " + // size
							0 + ", " + // eraser size
							counter + ")";
				} else if (tempHistory instanceof PathHeadHistory) {
					// insertTable = "pathheadhistory ";
					values = "values " + "(" + "\'"
							+ id
							+ "\', "
							+ 2
							+ ", "
							+ tempHistory.getPosition().getVector2().getIntX()
							+ ", "
							+ // x
							tempHistory.getPosition().getVector2().getIntY()
							+ ", "
							+ // y
							0 + ", " + 0
							+ ", "
							+ ((PathHeadHistory) tempHistory).pathColor.getR()
							+ ", "
							+ // R
							((PathHeadHistory) tempHistory).pathColor.getG()
							+ ", "
							+ // G
							((PathHeadHistory) tempHistory).pathColor.getB()
							+ ", " + // B
							((PathHeadHistory) tempHistory).strokeSize + ", " + // size
							0 + ", " + // eraser size
							counter + ")";
				}
				if (tempHistory instanceof RemoveHistory) {
					// insertTable = "removehistory ";
					values = "values " + "(" + "\'" + id + "\', " + 3 + ", "
							+ tempHistory.getPosition().getVector2().getIntX()
							+ ", "
							+ // x
							tempHistory.getPosition().getVector2().getIntY()
							+ ", " + // y
							0 + ", " + 0 + ", " + 0 + ", " + // R
							0 + ", " + // G
							0 + ", " + // B
							0 + ", " + // Stroke size
							((RemoveHistory) tempHistory).eraserSize + ", " + // eraser
																				// size
							counter + ")";
				}

				insertTable = "history" +
				// "_"+current_database_end+
						"_" + newRevision + " ";
				st = connection.createStatement();
				int returnCode = st.executeUpdate(insertHead + insertTable
						+ values);

				counter++;
			}

			// ********* commit new histories from the very beginning of this
			// ********* sketching
			int startIndex = lastHistoryCount + 1;
			int endIndex = lastHistoryCount + input.updatedHistory.size();
			/*
			 * int newRevision = 0;
			 * 
			 * //*********** new newRevision update position st =
			 * connection.createStatement(); rs =
			 * st.executeQuery("SELECT count(*) from"+" currentrevision"); while
			 * (rs.next()) {
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here newRevision = rs.getInt(1)+1;
			 * 
			 * }
			 */
			System.out.println("newRevision after count " + newRevision);

			if (input.historyIndex == 0) {
			} else {/*
					 * int historyIndex = input.historyIndex;
					 * System.out.println("historyIndex "+historyIndex+", id "+
					 * id+", input.updaterevision "+input.updateRevision);
					 * 
					 * 
					 * 
					 * 
					 * 
					 * int lastHistoryInRevisionCount = 0; st =
					 * connection.createStatement();
					 * 
					 * rs =
					 * st.executeQuery("SELECT count(*) from"+" revisionhistory_"
					 * +
					 * current_from_database_end+"_"+(newRevision-1)+" where id ="
					 * +"\'"+id+"\'" +" and "+"lowb <="+historyIndex
					 * +" and "+" subrevision= "
					 * +(newRevision-1)//input.updateRevision );
					 * 
					 * while (rs.next()) { lastHistoryInRevisionCount =
					 * rs.getInt(1);
					 * System.out.println("lastHistoryInRevisionCount "
					 * +lastHistoryInRevisionCount); } String selectHead =
					 * "select id, revision, lowb, highb from revisionhistory_"+
					 * current_from_database_end+"_"+(newRevision-1)+""; String
					 * selectCond =
					 * " where lowb <="+historyIndex+" and "+//"highb>"
					 * +historyIndex+
					 * " revision= "+(newRevision-1)+//input.updateRevision+
					 * " and id ="+"\'"+id+"\'";
					 * 
					 * st = connection.createStatement(); rs =
					 * st.executeQuery(selectHead+selectCond);
					 * 
					 * int i =0; while (rs.next()) { if(i >=
					 * lastHistoryInRevisionCount-1) {
					 * System.out.println("break"); break; } String tempID =
					 * rs.getString(1); int tempLowb = rs.getInt(3); int
					 * tempHighb = rs.getInt(4);
					 * 
					 * 
					 * System.out.println("in while, high,low: "+tempHighb+", "+
					 * tempLowb);
					 * System.out.println("newRevision before insert----  "+
					 * newRevision);
					 * 
					 * 
					 * String tempInsert =
					 * "insert into revisionhistory_"+current_database_end
					 * +"_"+newRevision +" values("+
					 * "\'"+tempID+"\'"+", "+newRevision
					 * +", "+tempLowb+", "+tempHighb+")";
					 * 
					 * Statement itlSt = null; itlSt =
					 * connection.createStatement(); int returnCode =
					 * itlSt.executeUpdate(tempInsert ); if (itlSt != null) {
					 * itlSt.close(); } System.out.println("i "+i); i++;
					 * 
					 * } System.out.println("i after commit "+i); { int tempLowb
					 * = rs.getInt(3); int tempHighb = rs.getInt(4);
					 * System.out.println("high,low: "+tempHighb+", "+tempLowb);
					 * int tempHistoryIndex = historyIndex; String tempInsert =
					 * "insert into revisionhistory_"+current_database_end
					 * +"_"+newRevision +" values("+
					 * "\'"+id+"\'"+", "+newRevision
					 * +", "+tempLowb+", "+tempHistoryIndex+")"; st =
					 * connection.createStatement(); int returnCode =
					 * st.executeUpdate(tempInsert ); }
					 */
			}

			// ********* if no new history, don't do anything, and just return
			// an empty pack
			if (endIndex <= startIndex)
				return new CanvasPack();

			// ************ get the existing subrevision count
			int existingSubRev = 0;
			st = connection.createStatement();
			rs = st.executeQuery("select max(subrevision) from revisionhistory"
					+
					// "_"+current_database_end
					// +
					"_" + (newRevision) + " where id = " + "\'" + id + "\'" +
					// " and uid = " +"\'"+UID+"\'"+
					// " and revision = " +"\'"+(newRevision-1)+"\'"+
					"; ");
			while (rs.next()) {

				// ********** since the canvas has new revision number already,
				// not +1 here
				existingSubRev = rs.getInt(1);
				System.out.println("current existingSubRev " + existingSubRev);
			}
			/*
			 * st = connection.createStatement(); rs =
			 * st.executeQuery("select last_subrevision from lastrevision " +
			 * //current_database_end+"_"+newRevision +
			 * " where entity_id = "+"\'"+id+"\'"+ " and uid = " +"\'"+UID+"\'"+
			 * " and revision = " +"\'"+(newRevision-1)+"\'"+ "; "); while
			 * (rs.next()) {
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here existingSubRev = rs.getInt(1);
			 * System.out.println("current lastrevision "+existingSubRev); }
			 */

			// ********* new committed history
			// ****** update the revisiontable
			// if(endIndex> startIndex)
			// {
			insertTable = "revisionhistory"
			// +"_"+current_database_end
					+ "_" + newRevision + " ";
			values = "values " + "(" + "\'" + id + "\', "
					+ (existingSubRev + 1) + ", " + startIndex + ", "
					+ +endIndex + ")";
			int returnCode = st
					.executeUpdate(insertHead + insertTable + values);
			// }

			// ******DOES NOT need commit service to update lastrevision table
			// *********** last subrevision for the from revison for last
			// revision table
			/*
			 * int lastSubRev = 1;
			 * 
			 * if(newRevision-1>=1){ st = connection.createStatement(); rs =
			 * st.executeQuery
			 * ("select max(subrevision) from revisionhistory_"+current_database_end
			 * +"_"+(newRevision-1)+ " where id = "+"\'"+id+"\'"+
			 * //" and uid = " +"\'"+UID+"\'"+ //" and revision = "
			 * +"\'"+(newRevision-1)+"\'"+ "; "); while (rs.next()) {
			 * 
			 * //********** since the canvas has new revision number already,
			 * not +1 here lastSubRev = rs.getInt(1);
			 * System.out.println("current lastrevision "+lastSubRev); }
			 */

			st = connection.createStatement();

			int lastSubRev = 1;

			/*
			 * if(lastSubRev==0) { lastSubRev = 1; }
			 */
			// ********* insert into lastrevision table to store the
			// "last_subrevision" number
			insertTable = "lastrevision"// +current_database_end
					// +"_"+newRevision
					+ " ";
			values = "values " + "(" + "\'" + UID + "\', " + "\'" + id + "\', "
					+ newRevision + ", " + newRevision + ", " + +(lastSubRev)
					+ ", " + "\'" + "C" + "\'" + ")";

			// st.addBatch(insertHead+insertTable+values );

			// int returnCode[] = st.executeBatch();
			// connection.commit();
			returnCode = st.executeUpdate(insertHead + insertTable + values);

			// ************** commit the subrevision_table for non_whole
			// revision check out
			insertTable = "subrevision_table"// +current_database_end
					// +"_"+newRevision
					+ " ";
			values = "values " + "(" + "\'" + UID + "\', " + "\'" + id + "\', "
					+ newRevision + ", " + (lastSubRev) + ", " + "\'" + "C"
					+ "\'" + ", " + +(1) + ")";

			// st.addBatch(insertHead+insertTable+values );

			// int returnCode[] = st.executeBatch();
			// connection.commit();
			returnCode = st.executeUpdate(insertHead + insertTable + values);

			// }

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			System.out.println(e.getNextException());
		} finally {
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

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		int i = 1;
		for (AbstractHistory tempHistory : input.updatedHistory) {
			returnStr += tempHistory.getType();
			tempHistory.historyNumber = lastHistoryCount + i;
			i++;
			returnPack.updatedHistory.add(tempHistory);
		}

		System.out.println("end commit service \n");

		returnPack.updateRevision = input.updateRevision + 1;

		return returnPack;
	}
}
