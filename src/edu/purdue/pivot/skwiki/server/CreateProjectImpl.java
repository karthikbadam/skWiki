package edu.purdue.pivot.skwiki.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.purdue.pivot.skwiki.client.CreateProjectService;
import edu.purdue.pivot.skwiki.shared.DataPack;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CreateProjectImpl extends RemoteServiceServlet implements
CreateProjectService{

	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	public DataPack createProject(DataPack input) throws IllegalArgumentException {

		String returnInput = input.toString();
		String id = input.id;
		int checkoutRevision = input.updateRevision;

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
					"jdbc:postgresql://127.0.0.1:5432/mainbase", "postgres",
					"fujiko");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";
			int returnCode = st.executeUpdate(" insert into  "+" projects  values ( "
					+"\'"+input.projectInfo.projectName+"\', "
					+"\'"+input.projectInfo.projectDescription+"\' "
					+" );" );

			st = connection.createStatement();

			returnCode = st.executeUpdate(" create database  "
					+input.projectInfo.projectName+" ;" );

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"+input.projectInfo.projectName, "postgres",
					"fujiko");
			
			st = connection.createStatement();

			returnCode = st.executeUpdate("create table currentrevision(" + 
					"id varchar(20)," + 
					"revision int," + 
					"from_revision int," + 
					"comment varchar(200)" + 
					");" + 
					"" + 
					"create table tag(" + 
					"revision int," + 
					"uid varchar(50)," + 
					"entity_type varchar(10)," + 
					"entity_id varchar(30)," + 
					"tag varchar(40)" + 
					");" + 
					"" + 
					"" + 
					"create table lastRevision(" + 
					"uid varchar(50)," + 
					"entity_id varchar(50)," + 
					"workingrevision int," + 
					"revision int," + 
					"last_subRevision int," + 
					"entity_type varchar(10)" + 
					");" + 
					"" + 
					"create table subrevision_table(" + 
					"uid varchar(50)," + 
					"entity_id varchar(50)," + 
					"revision int," + 
					"last_subRevision int," + 
					"entity_type varchar(10)," + 
					"sequence_id int" + 
					");");


			/*rs = st.executeQuery("insert into  "+" projects  values ( "
			         +"project name"+" )");
			//int rowCount = 0;
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				returnPack.maxnonWholeSequence_id = rs.getInt(1);
			}
			System.out.println("max sequence_id "+ returnPack.maxnonWholeSequence_id);*/


			returnPack.projectName = input.projectName;
			returnPack.isSuccess = true;
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			returnPack.isSuccess = false;

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

	public DataPack createUser(DataPack input) throws IllegalArgumentException {



		System.out.println("checout Sub revision start");

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
					"jdbc:postgresql://127.0.0.1:5432/mainbase", "postgres",
					"fujiko");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";
			int returnCode = st.executeUpdate(" insert into  "+" users  values ( "
					+"\'"+input.userInfo.userName+"\', "
					+"md5(\'"+input.userInfo.pwd+"\') "
					+" );" );




			/*rs = st.executeQuery("insert into  "+" projects  values ( "
			         +"project name"+" )");
			//int rowCount = 0;
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				returnPack.maxnonWholeSequence_id = rs.getInt(1);
			}
			System.out.println("max sequence_id "+ returnPack.maxnonWholeSequence_id);*/

			returnPack.isSuccess = (returnCode >= 0);
			returnPack.id = input.userInfo.userName;
			
			returnPack.projectName = input.projectName;
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			returnPack.userInfo.existed = true;


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


	public DataPack authenticate(DataPack input) throws IllegalArgumentException {



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
					"jdbc:postgresql://127.0.0.1:5432/mainbase", "postgres",
					"fujiko");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";
			/*int returnCode = st.executeUpdate(" insert into  "+" users  values ( "
					+"\'"+input.userInfo.userName+"\', "
					+"md5(\'"+input.userInfo.pwd+"\') "
					+" );" );*/

			rs = st.executeQuery("select count(*) from users where " +
					"username = " +"\'"+input.userInfo.userName+"\' and "+
					"pwd = " +"md5(\'"+input.userInfo.pwd+"\');");
			int count =0;
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				count = rs.getInt(1);
			}
			if(count==1)
			{
				returnPack.userInfo.authSuccess = true;
			}
			else
				if(count==0)
				{
					returnPack.userInfo.authSuccess = false;

				}
			
			returnPack.isSuccess = (count >= 0);
			returnPack.id = input.userInfo.userName;

			/*rs = st.executeQuery("insert into  "+" projects  values ( "
			         +"project name"+" )");
			//int rowCount = 0;
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				returnPack.maxnonWholeSequence_id = rs.getInt(1);
			}
			System.out.println("max sequence_id "+ returnPack.maxnonWholeSequence_id);*/


			//returnPack.projectName = input.projectName;
		} catch (SQLException e) {

			//	System.out.println("Connection Failed! Check output console");

			e.printStackTrace();
			returnPack.userInfo.existed = true;
			//	System.out.println(e);

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


	public DataPack getAllUserList(DataPack input) throws IllegalArgumentException {



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
					"jdbc:postgresql://127.0.0.1:5432/mainbase", "postgres",
					"fujiko");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";
			/*int returnCode = st.executeUpdate(" insert into  "+" users  values ( "
					+"\'"+input.userInfo.userName+"\', "
					+"md5(\'"+input.userInfo.pwd+"\') "
					+" );" );*/

			rs = st.executeQuery("select username from users ");
			
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				returnPack.userInfo.userList.add(rs.getString(1));
			}
			


			/*rs = st.executeQuery("insert into  "+" projects  values ( "
			         +"project name"+" )");
			//int rowCount = 0;
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				returnPack.maxnonWholeSequence_id = rs.getInt(1);
			}
			System.out.println("max sequence_id "+ returnPack.maxnonWholeSequence_id);*/


			//returnPack.projectName = input.projectName;
		} catch (SQLException e) {

			//	System.out.println("Connection Failed! Check output console");

			e.printStackTrace();
			returnPack.userInfo.existed = true;
			//	System.out.println(e);

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

	public DataPack getAllProjectList(DataPack input) throws IllegalArgumentException {

		String returnInput = input.toString();
		String id = input.id;
		int checkoutRevision = input.updateRevision;

		//String returnStr = "";'[

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
					"jdbc:postgresql://127.0.0.1:5432/mainbase", "postgres",
					"fujiko");
			st = connection.createStatement();

			//String selectHead="select ";

			//String insertTable = "";
			//String values = "";
			/*int returnCode = st.executeUpdate(" insert into  "+" users  values ( "
					+"\'"+input.userInfo.userName+"\', "
					+"md5(\'"+input.userInfo.pwd+"\') "
					+" );" );*/

			rs = st.executeQuery("select * from projects ");
			
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				returnPack.projectInfo.projectNameList.add(rs.getString(1));
				returnPack.projectInfo.projectDescripList.add(rs.getString(2));
			}
			


			/*rs = st.executeQuery("insert into  "+" projects  values ( "
			         +"project name"+" )");
			//int rowCount = 0;
			while (rs.next()) {
				//System.out.print(rs.getInt(1)+" ");
				returnPack.maxnonWholeSequence_id = rs.getInt(1);
			}
			System.out.println("max sequence_id "+ returnPack.maxnonWholeSequence_id);*/

			returnPack.isSuccess = true;
			//returnPack.projectName = input.projectName;
		} catch (SQLException e) {

			//	System.out.println("Connection Failed! Check output console");

			e.printStackTrace();
			returnPack.userInfo.existed = true;
			
			//	System.out.println(e);

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
