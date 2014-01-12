package edu.purdue.pivot.skwiki.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.purdue.pivot.skwiki.client.CheckOutLatestService;
import edu.purdue.pivot.skwiki.shared.DataPack;
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
public class CheckOutLatestServiceImpl extends RemoteServiceServlet implements
		CheckOutLatestService {

	
	
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public DataPack checkOutLatest(DataPack input) throws IllegalArgumentException {
		

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

	
		input.dataPlus1();
		System.err.println(input.updatedHistory.size()+"");
		System.out.println("Server Side code");
		String returnInput = input.toString();
		userAgent = escapeHtml(userAgent);
		String id = input.id;
		
		String returnStr = "";
		DataPack result = new DataPack();
		boolean newID = false;
		

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
					"jdbc:postgresql://127.0.0.1:5432/mytest", "postgres",
					"fujiko");
			
			
			st = connection.createStatement();
			rs = st.executeQuery("SELECT * FROM"+" revisionhistory where id ="+"\'"+id+"\'");
			int rowCount = 0;
			while (rs.next()) {
				System.out.print(rs.getString(1)+" ");
				System.out.print(rs.getString(2)+" ");
				rowCount++;
			}

			if(rowCount==0)
			{
				newID = true;
			}
			else{
				newID = false;
			}

			int lastHistoryCount = 0;
			if(!newID){
				//***************get the first number of all the existing histories
				//************** in history

				//TODO
				rs = st.executeQuery("SELECT max(revision) from"+" revisionhistory where id ="+"\'"+id+"\'");
				while (rs.next()) {
					System.out.print(rs.getString(1)+" ");
					lastHistoryCount = rs.getInt(1);
				}
				
				result.updateRevision = lastHistoryCount;
				System.out.println("lastHistoryCount1111 "+lastHistoryCount);
			}
			
			if(newID)
			{
				result.newID = true;
			}
			else
			{
				result.newID = false;

			}
			
			if(newID)
			{
				return result;
			}
			
			
			
			
			st = connection.createStatement();
			String selectTextStr = "select * from text where revision = "
            		+lastHistoryCount+ " and id ="+"\'"+id+"\'";
			ResultSet textRs = st.executeQuery(selectTextStr);
          
			String text;
			while(textRs.next())
			{
				 text = textRs.getString(3);
				 result.updateHtml = text;
			}
			
			
			
            String selectStr = "select * from revisionhistory where revision = "
            		+lastHistoryCount+ " and id ="+"\'"+id+"\'";
            
            System.out.println("lastHistoryCounton "+ lastHistoryCount);
           
            
			rs = st.executeQuery(selectStr);
			
			while(rs.next())
			{
				int revision = rs.getInt(2);
				int lowb = rs.getInt(3);
				int highb = rs.getInt(4);
				System.out.println("lowb, highb: "+lowb+" "+highb);
				
				String tempSelect = " select * from history " +
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
		return result;
	}
}
