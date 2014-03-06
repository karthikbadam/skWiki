package edu.purdue.pivot.skwiki.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
import edu.purdue.pivot.skwiki.shared.TextPack;
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

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* create new tables for the commit */
			/* stroke history */
			int createCode = st.executeUpdate("CREATE table history" + "_"
					+ newRevision + "(" + "id varchar(50)," + "type int,"
					+ "sx int," + "sy int," + "ex int," + "ey int," + "r int,"
					+ "g int," + "b int," + "strokeSize int ,"
					+ "eraserSize int," + "historyNumber int" + ");");

			/* tables for diff and patch */
			st = connection.createStatement();
			createCode = st.executeUpdate("CREATE table diff" + "_"
					+ newRevision + "(" + "id varchar(50),"
					+ "diff_sequence_id int," + "opertion int,"
					+ "textbody varchar(2000000)" + ");");

			createCode = st.executeUpdate("CREATE table patch" + "_"
					+ newRevision + "(" + "id varchar(50),"
					+ "subrevision int," + "start_sequence_id int,"
					+ "end_sequence_id int" + ");");

			/* image history */
			st = connection.createStatement();
			createCode = st.executeUpdate("CREATE table image" + "_"
					+ newRevision + "(" + "id varchar(50),"
					+ "subrevision int," + "pox_x int," + "pox_y int,"
					+ "width int," + "height int," + "size_x int,"
					+ "size_y int," + "URL varchar(20000)" + ");");

			/* layout history */
			st = connection.createStatement();
			createCode = st.executeUpdate("CREATE table layout" + "_"
					+ newRevision + "(" + "id varchar(50),"
					+ "parentid varchar(20)," + "pox_x int," + "pox_y int,"
					+ "type varchar(10)," + "entity_type varchar(10),"
					+ "size_x int," + "size_y int," + "subrevision int" + ");");

			/* revision history */
			st = connection.createStatement();
			createCode = st.executeUpdate("CREATE table revisionhistory" + "_"
					+ newRevision + "(" + "id varchar(50),"
					+ "subrevision int," + "lowb int," + "highb int" + ");");

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
		int id_count = 0;
		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			rs = st.executeQuery("SELECT count(*) from" + " currentRevision");

			while (rs.next()) {

				id_count = rs.getInt(1);
				// System.out.println("newRevision " + id_count);
			}

			int newRevision = id_count + 1;

			/* insert tags of each text entity */
			for (int i = 0; i < input.textTaglMap.size(); i++) {
				String key = (String) input.textTaglMap.keySet().toArray()[i];
				String tag = (String) input.textTaglMap.values().toArray()[i];

				String insertHead = "insert into ";
				String insertTable = "tag ";
				String values = "values " + "(" + newRevision + "," + "\'"
						+ uid + "\'" + "," + "\'" + "Text" + "\'" + "," + "\'"
						+ key + "\'" + "," + "\'" + tag + "\'" + ")";
				st = connection.createStatement();
				int textReturnCode = st.executeUpdate(insertHead + insertTable
						+ values);
			}

			/* insert tags of each canvas entity */
			for (int i = 0; i < input.canvasTagMap.size(); i++) {
				String key = (String) input.canvasTagMap.keySet().toArray()[i];
				String tag = (String) input.canvasTagMap.values().toArray()[i];

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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public void copyLastSubrevisionInfo(int newRevision, int fromRevision) {
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			String insertHead = "insert into ";
			String insertTable = "lastrevision ";
			String selectStr = " select uid, entity_id, "
					+ newRevision
					+ ", revision, last_subrevision,entity_type from lastrevision ";
			String values = " "
					+ "("
					+ "uid, entity_id, workingrevision, revision, last_subrevision,entity_type"
					+ ")";

			String whereStr = " where workingrevision = " + fromRevision;
			// System.out.println(insertHead + insertTable + values + selectStr
			// + whereStr);
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

	public int getExistingSubrevision_tableIndex(int targetRevision) {
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		int existingSubRev = 0;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			String selectStr = " select max(sequence_id) from subrevision_table where revision = "
					+ targetRevision;

			rs = st.executeQuery(selectStr);
			while (rs.next()) {

				// ********** since the canvas has new revision number already,
				// not +1 here
				existingSubRev = rs.getInt(1);
				// System.out.println("current maximum of sequence_ID in"
				// + "the subrevision_table table " + existingSubRev);
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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return existingSubRev;
	}

	@Override
	public DataPack commit(DataPack input) throws IllegalArgumentException {

		DataPack returnPack = new DataPack();
		//current_project_name = input.projectName;
		
		/* read database details from file */
		BufferedReader br;
		//main_database_name = "mainbase";
		
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
		
		
		String id = input.id; // user id
		String fromUID = input.fromUID; // previous user id
		int fromRevision = input.fromRevision; // parent revision
		String comment = input.comment; // comments

		Date date= new Date();
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
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/*
			 * insert new revision details - postgres auto increment will take
			 * care of revision id
			 */
			String insertHead = "insert into ";
			String insertTable = "currentrevision(id, from_revision, comment) ";
			String values = "values " + "(" + "\'" + id + "\'" + ","
					+ fromRevision + "," + "\'" + comment + "\'" + ")";
			int textReturnCode = st.executeUpdate(insertHead + insertTable
					+ values);

			/* get the new revision id */
			rs = st.executeQuery("SELECT revision from currentRevision"
					+ " where from_revision = " + fromRevision + " and id = "
					+ "\'" + id + "\'");

			while (rs.next()) {
				id_count = rs.getInt(1);
			}

			
			/* LOG */
			System.out.println(date.toString()+",commit"+","+input.id+","+id_count);
			
			// System.out.println("current revision is --" + id_count
			// + " and previous revision --" + fromRevision);
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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		/* new revision id */
		int newRevision = id_count;
		returnPack.updateRevision = newRevision;

		/* create the necessary tables for saving data */
		createTablesIdEnds(id, fromUID, newRevision);
		String current_database_end = id;

		/* copy revision info from fromRevision to newRevision */
		copyLastSubrevisionInfo(newRevision, fromRevision);

		/* commit the canvas entities */
		for (int i = 0; i < input.updateCanvasMap.size(); i++) {
			String key = (String) input.updateCanvasMap.keySet().toArray()[i];
			CanvasPack tempCanvasPack = (CanvasPack) input.updateCanvasMap
					.values().toArray()[i];

			/* set new revision id */
			tempCanvasPack.updateRevision = fromRevision;
			CanvasPack tempReturnPack = commitOneCanvas(tempCanvasPack,
					input.fromUID, newRevision, id);

			/* update the return package */
			returnPack.updateCanvasMap.put(key, tempReturnPack);
		}

		/* commit the text entities */
		for (int i = 0; i < input.updateHtmlMap.size(); i++) {
			String key = (String) input.updateHtmlMap.keySet().toArray()[i];
			TextPack tempText = (TextPack) input.updateHtmlMap.values()
					.toArray()[i];
			TextPack tempReturnPack = commitOneText(input.id, key, tempText,
					newRevision);

			/* update the return package */
			returnPack.updateHtmlMap.put(key, tempReturnPack);
		}

		/* Commit the image entities */
		for (int i = 0; i < input.updateImageMap.size(); i++) {
			String key = (String) input.updateImageMap.keySet().toArray()[i];
			ImagePack tempImagePack = input.updateImageMap.get(key);
			commitOneImage(key, newRevision, tempImagePack);
		}

		/* commit tags */
		updateTags(input);

		/* commit the layoutList */
		if (input.layoutHistoryList.size() > 0) {
			commitOneLayoutHistory(input, newRevision, current_database_end);
		}

		returnPack.layoutHisotrySettleIndex = input.layoutHistoryList.size();
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

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

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

	/* Commit layout history */
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

		String insertHead = "insert into ";

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			String values = "";
			int existingSubRev = 0;

			int i = 0;
			for (AbstractLayoutHistory tempHistory : input.layoutHistoryList) {

				// System.out.println("committing layout history, iteration NO."
				// + i);
				st = connection.createStatement();
				rs = st.executeQuery("select count(subrevision) from layout"
						+ "_" + (newRevision) + "; ");

				while (rs.next()) {

					existingSubRev = rs.getInt(1);
//					System.out.println("current count of layout history in"
//							+ "the working layout table " + existingSubRev);
				}

				/* insert each history element */
				if (tempHistory instanceof CreateEntityHistory) {
					values = "values "
							+ "("
							+ "\'"
							+ ((CreateEntityHistory) tempHistory)
									.getOperatingObject() + "\', " + "\'"
							+ "parent" + "\', " + 0 + ", " + 0 + ", " + "\'"
							+ "c" + "\', " + "\'"
							+ ((CreateEntityHistory) tempHistory).editorType
							+ "\', " + 0 + ", " + 0 + ", "
							+ (existingSubRev + 1) + ")";

				} else if (tempHistory instanceof AddToParentHistory) {
					values = "values "
							+ "("
							+ "\'"
							+ ((AddToParentHistory) tempHistory)
									.getOperatingObject()
							+ "\', "
							+ "\'"
							+ ((AddToParentHistory) tempHistory)
									.getParentName() + "\', " + 0 + ", " + 0
							+ ", " + "\'" + "a" + "\', " + "\'" + "null"
							+ "\', " + 0 + ", " + 0 + ", "
							+ (existingSubRev + 1) + ")";

				} else if (tempHistory instanceof ChangeSizeHistory) {
					values = "values "
							+ "("
							+ "\'"
							+ ((ChangeSizeHistory) tempHistory)
									.getOperatingObject() + "\', " + "\'"
							+ "null" + "\', " + 0 + ", " + 0 + ", " + "\'"
							+ "cs" + "\', " + "\'" + "null" + "\', "
							+ ((ChangeSizeHistory) tempHistory).getNewX()
							+ ", "
							+ ((ChangeSizeHistory) tempHistory).getNewY()
							+ ", " + (existingSubRev + 1) + ")";

				} else if (tempHistory instanceof ChangePosHistory) {
					values = "values "
							+ "("
							+ "\'"
							+ ((ChangePosHistory) tempHistory)
									.getOperatingObject() + "\', " + "\'"
							+ "null" + "\', "
							+ ((ChangePosHistory) tempHistory).getNewX() + ", "
							+ ((ChangePosHistory) tempHistory).getNewY() + ", "
							+ "\'" + "cp" + "\', " + "\'" + "null" + "\', " + 0
							+ ", " + 0 + ", " + (existingSubRev + 1) + ")";
				}

				String insertTable = "layout" + "_" + newRevision + " ";
				int returnCode = st.executeUpdate(insertHead + insertTable
						+ values);
				i++;
			}

			// System.out
			// .println("Commiting lastrevision table with last_subrevision "
			// + (existingSubRev + 1));

			/* update revision details */
			st = connection.createStatement();
			String insertTable = "lastrevision" + " ";
			values = "values " + "(" + "\'" + input.id + "\', " + "\'"
					+ "layout" + "\', " + newRevision + ", " + newRevision
					+ ", " + +(existingSubRev + 1) + ", " + "\'" + "L" + "\'"
					+ ")";

			int returnCode = st.executeUpdate("insert into " + insertTable
					+ values);

			insertTable = "subrevision_table" + " ";
			values = "values " + "(" + "\'" + current_database_end + "\', "
					+ "\'" + "layout" + "\', " + newRevision + ", "
					+ (existingSubRev + 1) + ", " + "\'" + "L" + "\'" + ", "
					+ +(1) + ")";

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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	/* Commit text entity */
	public TextPack commitOneText(String uid, String key, TextPack textPack,
			int newRevision) {
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
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			int start_sequence_id = 0;
			rs = st.executeQuery("select count(diff_sequence_id) from diff"
					+ "_" + (newRevision) + "; ");
			while (rs.next()) {
				start_sequence_id = rs.getInt(1) + 1;
				// System.out.println("current count of text history in"
				// + "the working layout table " + start_sequence_id);
			}

			int existingSubRev = 0;
			st = connection.createStatement();
			rs = st.executeQuery("select count(diff_sequence_id) from diff"
					+ "_" + (newRevision) + "; ");
			while (rs.next()) {
				existingSubRev = rs.getInt(1);
			}
			
			for (int i = 0; i < textPack.patches.get(0).diffs.size(); i++) {

				/* insert into diff */
				String insertHead = "insert into";
				String insertTable = " diff" + "_" + newRevision + " ";
				String values = "values " + "(" + "\'" + key + "\', "
						+ (existingSubRev + 1) + ","
						+ textPack.patches.get(0).diffs.get(i).operation + ","
						+ "\'" + textPack.patches.get(0).diffs.get(i).text
						+ "\' " + ")";

				int returnCode = st.executeUpdate(insertHead + insertTable
						+ values);
				
				existingSubRev++;
			}

			// System.out.println("commiting patch start sequence "
			// + start_sequence_id + ", end sequence " + existingSubRev
			// + 1);

			int existingPatch = 0;
			rs = st.executeQuery("select count(*) from patch" + "_"
					+ (newRevision) + "; ");
			while (rs.next()) {
				existingPatch = rs.getInt(1);
				// System.out.println("existing patch No in patch table "
				// + existingPatch);
			}

			/* insert into path */
			st = connection.createStatement();
			String insertTable = " patch" + "_" + newRevision + " ";
			String values = "values " + "(" + "\'" + key + "\', "
					+ (existingPatch + 1) + ", " + start_sequence_id + ", "
					+ +(existingSubRev + 1) + ")";

			int returnCode = st.executeUpdate("insert into " + insertTable
					+ values);

			/* insert into lastrevision */
			st = connection.createStatement();
			insertTable = " lastrevision" + " ";
			values = "values " + "(" + "\'" + uid + "\', " + "\'" + key
					+ "\', " + newRevision + ", " + newRevision + ", "
					+ +(existingSubRev + 1) + ", " + "\'" + "T" + "\'" + ")";
			returnCode = st
					.executeUpdate("insert into " + insertTable + values);

			/* insert into subrevision */
			String insertHead = "insert into";
			insertTable = " subrevision_table" + " ";
			values = "values " + "(" + "\'" + uid + "\', " + "\'" + key
					+ "\', " + newRevision + ", " + (existingSubRev + 1) + ", "
					+ "\'" + "T" + "\'" + ", " + +(1) + ")";
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

		TextPack returnPack = new TextPack();
		returnPack.updateHtml = textPack.updateHtml;
		return returnPack;

	}

	/* save image entity */
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
		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
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

	/* save canvas entity */
	public CanvasPack commitOneCanvas(CanvasPack input, String fromUID,
			int newRevision, String UID) throws IllegalArgumentException {

		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		userAgent = escapeHtml(userAgent);

		String id = input.id;
		int lastHistoryCount = 0;
		CanvasPack returnPack = new CanvasPack();

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
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();
			String insertHead = "insert into ";
			String insertTable = "";
			String values = "";

			rs = st.executeQuery("SELECT count(*) from" + " history" + "_"
					+ (newRevision) + " where id =" + "\'" + id + "\'");

			while (rs.next()) {
				lastHistoryCount = rs.getInt(1);
			}

			/* insert stroke history */
			int counter = lastHistoryCount + 1;
			for (AbstractHistory tempHistory : input.updatedHistory) {
				/* stroke move history */
				if (tempHistory instanceof AddHistory) {
					values = "values " + "(" + "\'"
							+ id
							+ "\', "
							+ 1
							+ ", "
							+ tempHistory.getPosition().getVector2().getIntX()
							+ ", "
							+ tempHistory.getPosition().getVector2().getIntY()
							+ ", "
							+ ((AddHistory) tempHistory).endPos.getVector2()
									.getIntX()
							+ ", "
							+ ((AddHistory) tempHistory).endPos.getVector2()
									.getIntY() + ", "
							+ ((AddHistory) tempHistory).pathColor.getR()
							+ ", "
							+ ((AddHistory) tempHistory).pathColor.getG()
							+ ", "
							+ ((AddHistory) tempHistory).pathColor.getB()
							+ ", " + ((AddHistory) tempHistory).strokeSize
							+ ", " + 0 + ", " + counter + ")";

				} else if (tempHistory instanceof PathHeadHistory) {
					/* stroke start History */
					values = "values " + "(" + "\'" + id + "\', " + 2 + ", "
							+ tempHistory.getPosition().getVector2().getIntX()
							+ ", "
							+ tempHistory.getPosition().getVector2().getIntY()
							+ ", " + 0 + ", " + 0 + ", "
							+ ((PathHeadHistory) tempHistory).pathColor.getR()
							+ ", "
							+ ((PathHeadHistory) tempHistory).pathColor.getG()
							+ ", "
							+ ((PathHeadHistory) tempHistory).pathColor.getB()
							+ ", " + ((PathHeadHistory) tempHistory).strokeSize
							+ ", " + 0 + ", " + counter + ")";
				}
				if (tempHistory instanceof RemoveHistory) {
					/* stroke remove history */
					values = "values " + "(" + "\'" + id + "\', " + 3 + ", "
							+ tempHistory.getPosition().getVector2().getIntX()
							+ ", "
							+ tempHistory.getPosition().getVector2().getIntY()
							+ ", " + 0 + ", " + 0 + ", " + 0 + ", " + 0 + ", "
							+ 0 + ", " + 0 + ", "
							+ ((RemoveHistory) tempHistory).eraserSize + ", "
							+ counter + ")";
				}

				insertTable = "history" + "_" + newRevision + " ";
				st = connection.createStatement();
				int returnCode = st.executeUpdate(insertHead + insertTable
						+ values);

				counter++;
			}

			int startIndex = lastHistoryCount + 1;
			int endIndex = lastHistoryCount + input.updatedHistory.size();

			// System.out.println("newRevision after count " + newRevision);

			if (endIndex <= startIndex)
				return new CanvasPack();

			/* get the existing sub-revision count */
			int existingSubRev = 0;
			st = connection.createStatement();
			rs = st.executeQuery("select max(subrevision) from revisionhistory"
					+ "_" + (newRevision) + " where id = " + "\'" + id + "\'"
					+ "; ");
			while (rs.next()) {
				existingSubRev = rs.getInt(1);
				// System.out.println("current existingSubRev " +
				// existingSubRev);
			}

			/* updating the revision history table */
			insertTable = "revisionhistory" + "_" + newRevision + " ";
			values = "values " + "(" + "\'" + id + "\', "
					+ (existingSubRev + 1) + ", " + startIndex + ", "
					+ +endIndex + ")";
			int returnCode = st
					.executeUpdate(insertHead + insertTable + values);

			/* updating the last revision table */
			st = connection.createStatement();
			int lastSubRev = 1;
			insertTable = "lastrevision" + " ";
			values = "values " + "(" + "\'" + UID + "\', " + "\'" + id + "\', "
					+ newRevision + ", " + newRevision + ", " + +(lastSubRev)
					+ ", " + "\'" + "C" + "\'" + ")";
			returnCode = st.executeUpdate(insertHead + insertTable + values);

			/* new subrevision */
			insertTable = "subrevision_table" + " ";
			values = "values " + "(" + "\'" + UID + "\', " + "\'" + id + "\', "
					+ newRevision + ", " + (lastSubRev) + ", " + "\'" + "C"
					+ "\'" + ", " + +(1) + ")";
			returnCode = st.executeUpdate(insertHead + insertTable + values);

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
			tempHistory.historyNumber = lastHistoryCount + i;
			i++;
			returnPack.updatedHistory.add(tempHistory);
		}

		// System.out.println("end commit service \n");
		returnPack.updateRevision = input.updateRevision + 1;

		return returnPack;
	}
}
