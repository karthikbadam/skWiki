package edu.purdue.pivot.skwiki.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.purdue.pivot.skwiki.client.CheckoutService;
import edu.purdue.pivot.skwiki.shared.AbstractLayoutHistory;
import edu.purdue.pivot.skwiki.shared.AddToParentHistory;
import edu.purdue.pivot.skwiki.shared.CanvasPack;
import edu.purdue.pivot.skwiki.shared.ChangePosHistory;
import edu.purdue.pivot.skwiki.shared.ChangeSizeHistory;
import edu.purdue.pivot.skwiki.shared.CreateEntityHistory;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.EditorType;
import edu.purdue.pivot.skwiki.shared.ImagePack;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;
import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;
import edu.purdue.pivot.skwiki.shared.history.AddHistory;
import edu.purdue.pivot.skwiki.shared.history.MyColor;
import edu.purdue.pivot.skwiki.shared.history.PathHeadHistory;
import edu.purdue.pivot.skwiki.shared.history.Point;
import edu.purdue.pivot.skwiki.shared.history.RemoveHistory;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CheckoutServiceImpl extends RemoteServiceServlet implements
CheckoutService {
	private String current_database_end = "";




	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}



	@Override
	public DataPack checkout(DataPack input) throws IllegalArgumentException {
		DataPack result = new DataPack();
		current_database_end = input.id;


		ArrayList<String> canvasNameList = new ArrayList<String>();


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
		//	ArrayList<Integer> revisionHistoryList = new ArrayList<Integer>();

		System.out.println("Checking out service start~~~~~~~~~~~~~~~");

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");

			int targetRevision = input.updateRevision;
			int nonWholeSequence_id = input.nonWholeSequence_id;


			int directFromRevision = 0;

			System.out.println("non whole sub sequence is "+nonWholeSequence_id);
			if(nonWholeSequence_id!=0)
			{

				String selectStr = "select from_revision from currentrevision where revision = "
						+targetRevision;
				//revisionHistoryList.add(targetRevision);
				System.out.println("targetRevision "+targetRevision);
				st = connection.createStatement();
				rs = st.executeQuery(selectStr);

				while(rs.next())
				{
					directFromRevision = rs.getInt(1);

					//canvasNameList.add(id);
				}


				System.out.println("From revision~"+directFromRevision);
				System.out.println("check out as whole and sub~");
				/*targetRevision = fromRevision;
				if(fromRevision==0)
					break;*/
			}
			else
			{
				directFromRevision = input.updateRevision;
				System.out.println("Only check out as whole");
			}

			//System.out.println("revisionlist size "+revisionHistoryList.size());
			//	Collections.reverse(revisionHistoryList);
			//******* check out actions


			//************* 1. check out canvas			
			System.out.println("check out canvas start~~~~~~~~");

			//************** check out subrevisions



			System.out.println("check out whole part");
			System.out.println("The whole part "+directFromRevision);
			
			
			//************** check out as whole
			//	int revisionCheckedoutAswhole = input.updateRevision;
			int revisionCheckedoutAswhole = directFromRevision;
			//for(int i = 0; i<= revisionHistoryList.size()-1; i++)
			{
				//System.out.println("iteration "+i);
				//******* check out canvas for one revision
				/*String selectStr = "select * from lastrevision where revision = "
						+(revisionHistoryList.get(i))+" and entity_type="+"\'"+"C"+"\'";
				 */
				String selectStr = "select * from lastrevision where workingrevision = "
						+(revisionCheckedoutAswhole)+" and entity_type="+"\'"+"C"+"\'";



				st = connection.createStatement();
				rs = st.executeQuery(selectStr);

				int last_revision = 0;
				String entity_name;
				while(rs.next())
				{
					entity_name = rs.getString(2);
					last_revision = rs.getInt(5);
					int checkoutrevision = rs.getInt(4) ;
					System.out.println("entity name "+entity_name+",  last revision"+last_revision);
					if(result.updateCanvasMap.containsKey(entity_name))
					{
						System.out.println("entity name already exists in map");
						CanvasPack tempPack = result.updateCanvasMap.get(entity_name);
						//tempPack.updatedHistory
						CanvasPack checkedoutHistory = 
								checkoutCanvasRevisions(last_revision, checkoutrevision, entity_name);
						System.out.println("number of canvas history checkout "+
								checkedoutHistory.updatedHistory.size());
						for(AbstractHistory tempHistory: checkedoutHistory.updatedHistory)
						{
							tempPack.updatedHistory.add(tempHistory);
						}
					}
					else
					{
						System.out.println("entity name  does not exist in map");

						result.updateCanvasMap.put(entity_name, 
								checkoutCanvasRevisions(last_revision, checkoutrevision, entity_name));
					}
					//revisionHistoryList.add(fromRevision);
					//canvasNameList.add(id);
				}
			}

			//*******************************************************************



			//************* check out layout history

			System.out.println("check out layout history start~~~");
			//for(int i = 0; i<= revisionHistoryList.size()-1; i++)
			{
				//System.out.println("iteration "+i);
				//******* check out layout for a whole revision
				/*String selectStr = "select * from lastrevision where revision = "
						+(revisionHistoryList.get(i))+" and entity_type="+"\'"+"L"+"\'";
				 */

				String selectStr = "select * from lastrevision where workingrevision = "
						+(revisionCheckedoutAswhole)+" and entity_type="+"\'"+"L"+"\'";



				st = connection.createStatement();
				rs = st.executeQuery(selectStr);

				int last_revision = 0;
				String entity_name;
				while(rs.next())
				{
					/*entity_name = rs.getString(2);
					last_revision = rs.getInt(4);
					 */

					entity_name = rs.getString(2);
					last_revision = rs.getInt(5);
					int checkoutrevision = rs.getInt(4) ;


					System.out.println("last revision is "+last_revision);
					ArrayList<AbstractLayoutHistory> returnHistory
					= new ArrayList<AbstractLayoutHistory>();

					returnHistory = checkoutLayout(checkoutrevision, last_revision);

					for(AbstractLayoutHistory tempLayoutHistory: returnHistory)
					{
						result.layoutHistoryList.add(tempLayoutHistory);
					}

				}
			}




			//*********************************************************************


			//**************  check out text

			//********************************************


			
			
			//*************** 2. checkout non whole revisons, e.g. some subrevisons

			if(nonWholeSequence_id!=0)
			{
				//**** Canvas
				{
					String selectStr = "select * from subrevision_table where revision = "
							+(targetRevision)+" and entity_type="+"\'"+"C"+"\' and " +
							"sequence_id="+nonWholeSequence_id;

					System.out.println("check out NON- whole layout history start~~~");
					System.out.println("check out partial subrevision of revision "+targetRevision);

					st = connection.createStatement();
					rs = st.executeQuery(selectStr);

					int last_revision = 0;
					String entity_name;
					while(rs.next())
					{
						entity_name = rs.getString(2);
						last_revision = rs.getInt(4);
						int checkoutrevision = rs.getInt(3) ;
						System.out.println("entity name "+entity_name+",  last revision"+last_revision);
						if(result.updateCanvasMap.containsKey(entity_name))
						{
							System.out.println("entity name already exists in map");
							CanvasPack tempPack = result.updateCanvasMap.get(entity_name);
							//tempPack.updatedHistory
							CanvasPack checkedoutHistory = 
									checkoutCanvasRevisions(last_revision, checkoutrevision, entity_name);
							System.out.println("number of canvas history checkout "+
									checkedoutHistory.updatedHistory.size());
							for(AbstractHistory tempHistory: checkedoutHistory.updatedHistory)
							{
								tempPack.updatedHistory.add(tempHistory);
							}
						}
						else
						{
							System.out.println("entity name  does not exist in map");

							result.updateCanvasMap.put(entity_name, 
									checkoutCanvasRevisions(last_revision, checkoutrevision, entity_name));
						}
						//revisionHistoryList.add(fromRevision);
						//canvasNameList.add(id);
					}
				}

				//****** layout
				System.out.println("check out NON- whole layout history start~~~");
				//for(int i = 0; i<= revisionHistoryList.size()-1; i++)
				{
					//System.out.println("iteration "+i);
					//******* check out layout for a whole revision
					/*String selectStr = "select * from lastrevision where revision = "
						+(revisionHistoryList.get(i))+" and entity_type="+"\'"+"L"+"\'";
					 */

					String selectStr = "select * from subrevision_table where revision = "
							+(targetRevision)+" and entity_type="+"\'"+"L"+"\' and " +
							"sequence_id<="+nonWholeSequence_id;



					st = connection.createStatement();
					rs = st.executeQuery(selectStr);

					int last_revision = 0;
					String entity_name;
					while(rs.next())
					{
						/*entity_name = rs.getString(2);
						last_revision = rs.getInt(4);
						 */

						entity_name = rs.getString(2);
						last_revision = rs.getInt(4);
						int checkoutrevision = rs.getInt(3) ;


						System.out.println("last revision is "+last_revision);
						ArrayList<AbstractLayoutHistory> returnHistory
						= new ArrayList<AbstractLayoutHistory>();

						returnHistory = checkoutLayout(checkoutrevision, last_revision);

						for(AbstractLayoutHistory tempLayoutHistory: returnHistory)
						{
							result.layoutHistoryList.add(tempLayoutHistory);
						}

					}
				}


			}//endif nonwholesequence_id !=0
			//**********************************************************





			/*st = connection.createStatement();
			System.out.println("current_database_end "+current_database_end);
			String selectStr = "select entity_id from lastrevision where entity_type="+
					"\'"+"C"+"\' and ";
			//current_database_end+" where revision = "
           // 		+input.updateRevision;

            st = connection.createStatement();
			rs = st.executeQuery(selectStr);

			while(rs.next())
			{
				String id = rs.getString(1);
				canvasNameList.add(id);
				int revision = rs.getInt(2);
				int lowb = rs.getInt(3);
				int highb = rs.getInt(4);
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

				//	} catch (SQLException ex) {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}



		/*System.out.println(canvasNameList.size()+"  canvasNameList");
		for(String tempStr:  canvasNameList)
		{
			result.updateCanvasMap.put(tempStr, 
					checkoutCanvas( input,  tempStr));
		}*/

		/*
		checkoutText(input, result);


		result.layoutHistoryList = checkoutLayout(input);

		checkoutTag(result, input);
		 */
		System.out.println("return layout size "+result.layoutHistoryList.size());

		System.out.println("return canvas map size "+result.updateCanvasMap.size());

		for(AbstractLayoutHistory tempLayoutHistory: result.layoutHistoryList)
		{
			System.out.println(tempLayoutHistory.toString());

		}
		checkoutImage(input, input.updateRevision, result);
		
		result.fromUID = input.id;
		return result;
	}


	private void checkoutImage(DataPack input, int revision, DataPack result) {

		ImagePack imagePack;

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
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");
			st = connection.createStatement();
			String selectTextStr = "select * from image_"
					+ revision;
			ResultSet imageRs = st.executeQuery(selectTextStr);

			while (imageRs.next()) {
				String key = imageRs.getString(1);
				double leftX = (double) imageRs.getFloat(3);
				double topY = (double) imageRs.getFloat(4);
				int width = imageRs.getInt(5);
				int height = imageRs.getInt(6);
				int layoutWidth = imageRs.getInt(7);
				int layoutHeight = imageRs.getInt(8);
				String url = imageRs.getString(9);
				imagePack = new ImagePack(url, leftX, topY, width, height,
						layoutWidth, layoutHeight, key);
				result.updateImageMap.put(key, imagePack);
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




	public CanvasPack checkoutCanvasRevisions(int lastsubRevision, int revision, String id) throws IllegalArgumentException {


		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");


		//String returnInput = input.toString();
		userAgent = escapeHtml(userAgent);
		//String id = input.id;

		String returnStr = "";
		CanvasPack result = new CanvasPack();



		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();


		}

		//System.out.println("id:  "+input.id+"");
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;


		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");
			/*st = connection.createStatement();
			String selectTextStr = "select * from text where revision = "
            		+input.updateRevision+ " and id ="+"\'"+id+"\'";
			ResultSet textRs = st.executeQuery(selectTextStr);

			String text;
			while(textRs.next())
			{
				 text = textRs.getString(3);
				 result.updateHtml = text;
			}*/





			String selectStr = "select * from revisionhistory" +
					//"_"+current_database_end
					//+
					"_"+revision+
					" where subrevision <= "
					+lastsubRevision+ " and id ="+"\'"+id+"\'";

			System.out.println("checkouting out a canvas \n lastsubrevision "+ lastsubRevision);
			System.out.println("revision "+revision+", entity_name "+id);
			System.out.println("selectStr "+selectStr);
			st = connection.createStatement();



			rs = st.executeQuery(selectStr);

			while(rs.next())
			{
				//int revision = rs.getInt(2);
				int lowb = rs.getInt(3);
				int highb = rs.getInt(4);
				System.out.println("lowb, highb: "+lowb+" "+highb);

				String tempSelect = " select * from history_"
						//+current_database_end
						//+"_"
						+revision+
						" " +
						"where id ="+"\'"+id+"\' and historynumber<="+highb
						+" and historynumber >="+lowb;

				Statement itlSt = null;
				itlSt = connection.createStatement();
				ResultSet itlRs = null;
				itlRs = itlSt.executeQuery(tempSelect );
				while(itlRs.next())
				{
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


					if( type == 2)
					{
						tempHistory = new PathHeadHistory();
						tempHistory.position = new Point(x,y);
						( (PathHeadHistory)tempHistory).pathColor
						= new MyColor(r,g, b);
						( (PathHeadHistory)tempHistory).strokeSize
						=strokeSize;
						tempHistory.historyNumber = historyNumber;
						//tempHistory.
					}
					else if( type == 1)
					{
						tempHistory = new AddHistory();
						tempHistory.position = new Point(x,y);

						( (AddHistory)tempHistory).endPos = new Point(ex, ey);

						( (AddHistory)tempHistory).pathColor
						= new MyColor(r,g, b);
						( (AddHistory)tempHistory).strokeSize
						=strokeSize;
						tempHistory.historyNumber = historyNumber;


					}
					else if( type == 3)
					{
						tempHistory = new RemoveHistory();
						tempHistory.position = new Point(x,y);
						( (RemoveHistory)tempHistory).eraserSize
						=eraserSize;
						tempHistory.historyNumber = historyNumber;



					}
					result.updatedHistory.add(tempHistory);
				}

				if (itlSt != null) {
					itlSt.close();
				}
			}

			/*for(AbstractHistory tempHistory: input.updatedHistory)
    		{


    			insertTable = "history ";
                int returnCode = st.executeUpdate(insertHead+insertTable+values);

    		}*/

			//result.updateRevision = input.updateRevision;

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



		/*for(AbstractHistory tempHistory: input.updatedHistory)
		{
			returnStr+=tempHistory.getType();
		}*/

		//return returnStr;
		System.out.println("size of checked canvas "+result.updatedHistory.size());
		return result;
	}


	//public ArrayList<AbstractLayoutHistory> checkoutLayout(DataPack input)
	public ArrayList<AbstractLayoutHistory> checkoutLayout(int revision, int last_subrevision)
	{
		ArrayList<AbstractLayoutHistory> returnHistory
		= new ArrayList<AbstractLayoutHistory>();


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
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");
			st = connection.createStatement();
			String selectTextStr = "select * from layout_"+
					//current_database_end
					//+"_"
					+revision
					+" where subrevision <= "
					+last_subrevision;
			ResultSet textRs = st.executeQuery(selectTextStr);

			String text;
			AbstractLayoutHistory tempEntityHistory = null;
			while(textRs.next())
			{
				String object = textRs.getString(1);
				String entity_type = textRs.getString(6);
				String type = textRs.getString(5);
				String parentName = textRs.getString(2);
				int pos_x = textRs.getInt(3);
				int pos_y = textRs.getInt(4);

				int size_x = textRs.getInt(7);
				int size_y = textRs.getInt(8);

				if(type.equals("c"))
				{
					if(entity_type.equals("CANVAS"))
					{
						tempEntityHistory
						= new CreateEntityHistory(object, EditorType.CANVAS);
					} else if(entity_type.equals("TEXT"))
					{
						tempEntityHistory
						= new CreateEntityHistory(object, EditorType.TEXT);
					} else if(entity_type.equals("IMAGE"))
					{
						tempEntityHistory
						= new CreateEntityHistory(object, EditorType.IMAGE);		
					}
				} else if(type.equals("a"))
				{
					tempEntityHistory = 
							new AddToParentHistory(object, parentName);

				}else if(type.equals("cp"))
				{
					tempEntityHistory = 
							new ChangePosHistory(object, pos_x, pos_y);
				}else if(type.equals("cs"))
				{
					tempEntityHistory = 
							new ChangeSizeHistory(object, size_x, size_y);
				}

				returnHistory.add(tempEntityHistory);


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



		return returnHistory;

	}


	public String checkoutText(DataPack input, DataPack result)
	{
		String returnHtml = "";


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
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");
			st = connection.createStatement();
			
			//TODO  WRONG!!!!!!!
			String selectTextStr = "select * from text_"+input.id//current_database_end
					+" where revision = "
					+input.updateRevision;
			ResultSet textRs = st.executeQuery(selectTextStr);

			String text;
			while(textRs.next())
			{
				String key = textRs.getString(1);
				text = textRs.getString(3);
				returnHtml = text;
				result.updateHtmlMap.put(key, text);
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


		return returnHtml;
	}


	public void checkoutTag(DataPack result, DataPack input)
	{
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
					"jdbc:postgresql://127.0.0.1:5432/postchi_testing", "postgres",
					"fujiko");
			st = connection.createStatement();


			//********* text tags
			String selectTextStr = "select * from tag"
					+" where revision = "
					+input.updateRevision+" and "
					+"entity_type="+"\'"+"Text"+"\'";
			ResultSet textRs = st.executeQuery(selectTextStr);

			String tag = "";
			while(textRs.next())
			{
				String key = textRs.getString(4);
				tag = textRs.getString(5);
				//returnHtml = text;
				result.textTaglMap.put(key, tag);
			}


			//********* text tags
			String selectCanvasStr = "select * from tag"
					+" where revision = "
					+input.updateRevision+" and "
					+"entity_type="+"\'"+"Canvas"+"\'";
			st = connection.createStatement();
			ResultSet canvasRs = st.executeQuery(selectCanvasStr);

			// tag;
			while(canvasRs.next())
			{
				String key = canvasRs.getString(4);
				tag = canvasRs.getString(5);
				//returnHtml = text;
				result.canvasTagMap.put(key, tag);
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
	}





}
