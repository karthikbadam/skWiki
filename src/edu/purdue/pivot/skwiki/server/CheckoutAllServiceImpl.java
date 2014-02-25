package edu.purdue.pivot.skwiki.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.purdue.pivot.skwiki.client.CheckoutAllService;
import edu.purdue.pivot.skwiki.shared.AbstractLayoutHistory;
import edu.purdue.pivot.skwiki.shared.AddToParentHistory;
import edu.purdue.pivot.skwiki.shared.CanvasPack;
import edu.purdue.pivot.skwiki.shared.ChangePosHistory;
import edu.purdue.pivot.skwiki.shared.ChangeSizeHistory;
import edu.purdue.pivot.skwiki.shared.CreateEntityHistory;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.EditorType;
import edu.purdue.pivot.skwiki.shared.Patch;
import edu.purdue.pivot.skwiki.shared.TextPack;
import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;
import edu.purdue.pivot.skwiki.shared.history.AddHistory;
import edu.purdue.pivot.skwiki.shared.history.MyColor;
import edu.purdue.pivot.skwiki.shared.history.PathHeadHistory;
import edu.purdue.pivot.skwiki.shared.history.Point;
import edu.purdue.pivot.skwiki.shared.history.RemoveHistory;

@SuppressWarnings("serial")
public class CheckoutAllServiceImpl extends RemoteServiceServlet implements
		CheckoutAllService {
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

	@Override
	public DataPack checkoutAll(DataPack input) throws IllegalArgumentException {
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		userAgent = escapeHtml(userAgent);

		DataPack returnPack = new DataPack();
		current_project_name = input.projectName;
		
		
		/* read database details from file */
		BufferedReader br;
		current_project_name = input.projectName;
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
	        
	        //String everything = sb.toString();
	        //System.out.println("file: "+everything);
	        br.close();
	    
	    } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
	       
	    }
		
		
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

		ArrayList<Integer> downloadRevisions = input.downloadRevisions;

		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			for (int downloadRevision : downloadRevisions) {

				int targetRevision = downloadRevision;
				int nonWholeSequence_id = 0;
				int directFromRevision = 0;

				DataPack result_current = new DataPack();
				result_current.projectName  = current_project_name;
				
				if (nonWholeSequence_id != 0) {

					String selectStr = "select from_revision from currentrevision where revision = "
							+ targetRevision;
					// System.out.println("targetRevision "+targetRevision);
					st = connection.createStatement();
					rs = st.executeQuery(selectStr);

					while (rs.next()) {
						directFromRevision = rs.getInt(1);
					}

				} else {
					directFromRevision = input.updateRevision;
					// System.out.println("Only check out as whole");
				}

				/* checkout canvas history */
				int revisionCheckedoutAswhole = directFromRevision;
				String selectStr = "select * from lastrevision where workingrevision = "
						+ (revisionCheckedoutAswhole)
						+ " and entity_type="
						+ "\'" + "C" + "\'";

				st = connection.createStatement();
				rs = st.executeQuery(selectStr);

				int last_revision = 0;
				String entity_name;
				while (rs.next()) {
					entity_name = rs.getString(2);
					last_revision = rs.getInt(5);
					int checkoutrevision = rs.getInt(4);
					if (result_current.updateCanvasMap.containsKey(entity_name)) {
						CanvasPack tempPack = result_current.updateCanvasMap
								.get(entity_name);
						
						CanvasPack checkedoutHistory = checkoutCanvasRevisions(
								last_revision, checkoutrevision, entity_name);
						for (AbstractHistory tempHistory : checkedoutHistory.updatedHistory) {
							tempPack.updatedHistory.add(tempHistory);
						}
					} else {

						result_current.updateCanvasMap.put(
								entity_name,
								checkoutCanvasRevisions(last_revision,
										checkoutrevision, entity_name));
					}
				}

				/* check out text */
				selectStr = "select * from lastrevision where workingrevision = "
						+ (revisionCheckedoutAswhole)
						+ " and entity_type="
						+ "\'" + "T" + "\'";
				st = connection.createStatement();
				rs = st.executeQuery(selectStr);

				last_revision = 0;
				while (rs.next()) {
					entity_name = rs.getString(2);
					last_revision = rs.getInt(5);
					int checkoutrevision = rs.getInt(4);

					// System.out.println("entity name " + entity_name
					// + ",  last revision" + last_revision);

					if (result_current.updateHtmlMap.containsKey(entity_name)) {
						//System.out.println("entity name already exists in map");
						TextPack tempPack = result_current.updateHtmlMap
								.get(entity_name);
						TextPack checkedoutTextPack = checkoutTextRevisions(
								last_revision, checkoutrevision, entity_name);
						// System.out.println("number of canvas history checkout "
						// + checkedoutTextPack.patches.size());

						for (Patch tempPatch : checkedoutTextPack.patches) {
							tempPack.patches.add(tempPatch);
						}
					} else {
						result_current.updateHtmlMap.put(
								entity_name,
								checkoutTextRevisions(last_revision,
										checkoutrevision, entity_name));
					}
				}

				/* check out layout history */
				selectStr = "select * from lastrevision where workingrevision = "
						+ (revisionCheckedoutAswhole)
						+ " and entity_type="
						+ "\'" + "L" + "\'";

				st = connection.createStatement();
				rs = st.executeQuery(selectStr);

				last_revision = 0;
				while (rs.next()) {

					entity_name = rs.getString(2);
					last_revision = rs.getInt(5);
					int checkoutrevision = rs.getInt(4);

					ArrayList<AbstractLayoutHistory> returnHistory = new ArrayList<AbstractLayoutHistory>();

					returnHistory = checkoutLayout(checkoutrevision,
							last_revision);

					for (AbstractLayoutHistory tempLayoutHistory : returnHistory) {
						result_current.layoutHistoryList.add(tempLayoutHistory);
					}

				}

				/* nonWholeSequence_id is not zero, so there are sub revisions */
				if (nonWholeSequence_id != 0) {
					selectStr = "select * from subrevision_table where revision = "
							+ (targetRevision)
							+ " and entity_type="
							+ "\'"
							+ "C"
							+ "\' and "
							+ "sequence_id="
							+ nonWholeSequence_id;

					st = connection.createStatement();
					rs = st.executeQuery(selectStr);

					last_revision = 0;
					while (rs.next()) {
						entity_name = rs.getString(2);
						last_revision = rs.getInt(4);
						int checkoutrevision = rs.getInt(3);

						if (result_current.updateCanvasMap
								.containsKey(entity_name)) {

							CanvasPack tempPack = result_current.updateCanvasMap
									.get(entity_name);
							CanvasPack checkedoutHistory = checkoutCanvasRevisions(
									last_revision, checkoutrevision,
									entity_name);

							for (AbstractHistory tempHistory : checkedoutHistory.updatedHistory) {
								tempPack.updatedHistory.add(tempHistory);
							}
						} else {

							result_current.updateCanvasMap.put(
									entity_name,
									checkoutCanvasRevisions(last_revision,
											checkoutrevision, entity_name));
						}
					}

					/* checkout layout in case of subrevision */
					selectStr = "select * from subrevision_table where revision = "
							+ (targetRevision)
							+ " and entity_type="
							+ "\'"
							+ "L"
							+ "\' and "
							+ "sequence_id<="
							+ nonWholeSequence_id;

					st = connection.createStatement();
					rs = st.executeQuery(selectStr);

					last_revision = 0;
					while (rs.next()) {
						/*
						 * entity_name = rs.getString(2); last_revision =
						 * rs.getInt(4);
						 */

						entity_name = rs.getString(2);
						last_revision = rs.getInt(4);
						int checkoutrevision = rs.getInt(3);

						ArrayList<AbstractLayoutHistory> returnHistory = new ArrayList<AbstractLayoutHistory>();

						returnHistory = checkoutLayout(checkoutrevision,
								last_revision);

						for (AbstractLayoutHistory tempLayoutHistory : returnHistory) {
							result_current.layoutHistoryList
									.add(tempLayoutHistory);
						}

					}

					/* check text in case of subrevision */
					selectStr = "select * from subrevision_table where revision = "
							+ (targetRevision)
							+ " and entity_type="
							+ "\'"
							+ "T"
							+ "\' and "
							+ "sequence_id="
							+ nonWholeSequence_id;
					st = connection.createStatement();
					rs = st.executeQuery(selectStr);

					last_revision = 0;
					while (rs.next()) {
						entity_name = rs.getString(2);
						last_revision = rs.getInt(4);
						int checkoutrevision = rs.getInt(3);

						// System.out.println("entity name "+entity_name+",  last revision"+last_revision);

						if (result_current.updateHtmlMap
								.containsKey(entity_name)) {
							// System.out.println("entity name already exists in map");
							TextPack tempPack = result_current.updateHtmlMap
									.get(entity_name);

							TextPack checkedoutTextPack = checkoutTextRevisions(
									last_revision, checkoutrevision,
									entity_name);

							// System.out.println("number of text history checkout "+
							// checkedoutTextPack.patches.size());

							for (Patch tempPatch : checkedoutTextPack.patches) {
								tempPack.patches.add(tempPatch);
							}
						} else {
							result_current.updateHtmlMap.put(
									entity_name,
									checkoutTextRevisions(last_revision,
											checkoutrevision, entity_name));
						}

					}

				}

				result_current.fromRevision = downloadRevision;
				returnPack.allData.add(result_current);
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

		return returnPack;
	}
	public CanvasPack checkoutCanvasRevisions(int lastsubRevision,
			int revision, String id) throws IllegalArgumentException {

		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		userAgent = escapeHtml(userAgent);
		CanvasPack result = new CanvasPack();

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

			String selectStr = "select * from revisionhistory" + "_" + revision
					+ " where subrevision <= " + lastsubRevision + " and id ="
					+ "\'" + id + "\'";

			// System.out.println("checkouting out a canvas \n lastsubrevision "
			// + lastsubRevision);
			// System.out.println("revision " + revision + ", entity_name " +
			// id);
			// System.out.println("selectStr " + selectStr);

			st = connection.createStatement();
			rs = st.executeQuery(selectStr);

			while (rs.next()) {
				int lowb = rs.getInt(3);
				int highb = rs.getInt(4);
				
				String tempSelect = " select * from history_" + revision + " "
						+ "where id =" + "\'" + id + "\' and historynumber<="
						+ highb + " and historynumber >=" + lowb;

				Statement itlSt = null;
				itlSt = connection.createStatement();
				ResultSet itlRs = null;
				itlRs = itlSt.executeQuery(tempSelect);
				while (itlRs.next()) {
					AbstractHistory tempHistory = null;
					int type = itlRs.getInt(2);
					int x = itlRs.getInt(3);
					int y = itlRs.getInt(4);
					int ex = itlRs.getInt(5);
					int ey = itlRs.getInt(6);

					int r = itlRs.getInt(7);
					int g = itlRs.getInt(8);
					int b = itlRs.getInt(9);

					int strokeSize = itlRs.getInt(10);
					int eraserSize = itlRs.getInt(11);
					int historyNumber = itlRs.getInt(12);

					if (type == 2) {
						tempHistory = new PathHeadHistory();
						tempHistory.position = new Point(x, y);
						((PathHeadHistory) tempHistory).pathColor = new MyColor(
								r, g, b);
						((PathHeadHistory) tempHistory).strokeSize = strokeSize;
						tempHistory.historyNumber = historyNumber;
					} else if (type == 1) {
						tempHistory = new AddHistory();
						tempHistory.position = new Point(x, y);

						((AddHistory) tempHistory).endPos = new Point(ex, ey);

						((AddHistory) tempHistory).pathColor = new MyColor(r,
								g, b);
						((AddHistory) tempHistory).strokeSize = strokeSize;
						tempHistory.historyNumber = historyNumber;

					} else if (type == 3) {
						tempHistory = new RemoveHistory();
						tempHistory.position = new Point(x, y);
						((RemoveHistory) tempHistory).eraserSize = eraserSize;
						tempHistory.historyNumber = historyNumber;

					}
					result.updatedHistory.add(tempHistory);
				}

				if (itlSt != null) {
					itlSt.close();
				}
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

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return result;
	}

	public ArrayList<AbstractLayoutHistory> checkoutLayout(int revision,
			int last_subrevision) {
		ArrayList<AbstractLayoutHistory> returnHistory = new ArrayList<AbstractLayoutHistory>();

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
			String selectTextStr = "select * from layout_" + revision
					+ " where subrevision <= " + last_subrevision;
			ResultSet textRs = st.executeQuery(selectTextStr);

			AbstractLayoutHistory tempEntityHistory = null;

			while (textRs.next()) {
				String object = textRs.getString(1);
				String entity_type = textRs.getString(6);
				String type = textRs.getString(5);
				String parentName = textRs.getString(2);
				int pos_x = textRs.getInt(3);
				int pos_y = textRs.getInt(4);

				int size_x = textRs.getInt(7);
				int size_y = textRs.getInt(8);

				if (type.equals("c")) {
					if (entity_type.equals("CANVAS")) {
						tempEntityHistory = new CreateEntityHistory(object,
								EditorType.CANVAS);
					} else if (entity_type.equals("TEXT")) {
						tempEntityHistory = new CreateEntityHistory(object,
								EditorType.TEXT);
					} else if (entity_type.equals("IMAGE")) {
						tempEntityHistory = new CreateEntityHistory(object,
								EditorType.IMAGE);
					}
				} else if (type.equals("a")) {
					tempEntityHistory = new AddToParentHistory(object,
							parentName);

				} else if (type.equals("cp")) {
					tempEntityHistory = new ChangePosHistory(object, pos_x,
							pos_y);
				} else if (type.equals("cs")) {
					tempEntityHistory = new ChangeSizeHistory(object, size_x,
							size_y);
				}

				returnHistory.add(tempEntityHistory);

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

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return returnHistory;

	}

	public TextPack checkoutTextRevisions(int lastsubRevision, int revision,
			String id) {
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

		TextPack result = new TextPack();
		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+current_project_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			String selectStr = "select * from patch" + "_" + revision
					+ " where subrevision <= " + lastsubRevision + " and id ="
					+ "\'" + id + "\'";

			st = connection.createStatement();
			rs = st.executeQuery(selectStr);

			while (rs.next()) {
				int lowb = rs.getInt(3);
				int highb = rs.getInt(4);

				String tempSelect = " select * from diff_" + revision + " "
						+ "where id =" + "\'" + id
						+ "\' and diff_sequence_id<=" + highb
						+ " and diff_sequence_id >=" + lowb;

				Statement itlSt = null;
				itlSt = connection.createStatement();
				ResultSet itlRs = null;
				itlRs = itlSt.executeQuery(tempSelect);
				Patch tempPatch = new Patch();
				while (itlRs.next()) {
					int operation = itlRs.getInt(3);
					String text = itlRs.getString(4);
					tempPatch.addDiff(operation, text);
				}
				result.patches.add(tempPatch);
				if (itlSt != null) {
					itlSt.close();
				}
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

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public void checkoutTag(DataPack result, DataPack input) {
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

			/* text tags */
			String selectTextStr = "select * from tag" + " where revision = "
					+ input.updateRevision + " and " + "entity_type=" + "\'"
					+ "Text" + "\'";
			ResultSet textRs = st.executeQuery(selectTextStr);

			String tag = "";
			while (textRs.next()) {
				String key = textRs.getString(4);
				tag = textRs.getString(5);
				result.textTaglMap.put(key, tag);
			}

			/* canvas tags */
			String selectCanvasStr = "select * from tag" + " where revision = "
					+ input.updateRevision + " and " + "entity_type=" + "\'"
					+ "Canvas" + "\'";
			st = connection.createStatement();
			ResultSet canvasRs = st.executeQuery(selectCanvasStr);

			while (canvasRs.next()) {
				String key = canvasRs.getString(4);
				tag = canvasRs.getString(5);
				result.canvasTagMap.put(key, tag);
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

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
