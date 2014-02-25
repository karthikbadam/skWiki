package edu.purdue.pivot.skwiki.server;

import java.io.BufferedReader;
import java.io.FileReader;
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
		CreateProjectService {

	private String main_database_name = "";
	private String postgres_name = "postgres";
	private String postgres_password = "fujiko";
	private String current_project_name = "";
	
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	public DataPack createProject(DataPack input)
			throws IllegalArgumentException {

		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";

		try {
			br = new BufferedReader(new FileReader(this.getServletContext()
					.getRealPath("/serverConfig.txt")));
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

			// String everything = sb.toString();
			// System.out.println("file: "+everything);
			br.close();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {

		}

		String id = input.id;

		DataPack returnPack = new DataPack();

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

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/" + main_database_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* insert into projects */
			int returnCode = st.executeUpdate(" insert into  "
					+ " projects  values ( " + "\'"
					+ input.projectInfo.projectName + "\', " + "\'"
					+ input.projectInfo.projectDescription + "\' " + " );");

			st = connection.createStatement();

			/* insert project-user details */
			returnCode = st.executeUpdate(" insert into  "
					+ " user_project  values ( " + "\'"
					+ input.projectInfo.projectName + "\', " + "\'" + input.id
					+ "\' " + " );");

			/* create new database */
			returnCode = st.executeUpdate(" create database  "
					+ input.projectInfo.projectName + " ;");

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/"
							+ input.projectInfo.projectName, "postgres",
					"fujiko");

			/* add tables to the new database */
			returnCode = st.executeUpdate("create table currentrevision("
					+ "id varchar(20)," + "revision int,"
					+ "from_revision int," + "comment varchar(200)" + ");" + ""
					+ "create table tag(" + "revision int,"
					+ "uid varchar(50)," + "entity_type varchar(10),"
					+ "entity_id varchar(30)," + "tag varchar(40)" + ");" + ""
					+ "create table lastRevision(" + "uid varchar(50),"
					+ "entity_id varchar(50)," + "workingrevision int,"
					+ "revision int," + "last_subRevision int,"
					+ "entity_type varchar(10)" + ");" + ""
					+ "create table subrevision_table(" + "uid varchar(50),"
					+ "entity_id varchar(50)," + "revision int,"
					+ "last_subRevision int," + "entity_type varchar(10),"
					+ "sequence_id int" + "); + " + ""
					+ "create table images (" + "field_name varchar(50), "
					+ "path varchar(50)" + ");");

			returnPack.projectName = input.projectName;
			returnPack.isSuccess = true;
		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			returnPack.isSuccess = false;

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
		return returnPack;
	}

	public DataPack createUser(DataPack input) throws IllegalArgumentException {

		DataPack returnPack = new DataPack();

		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";

		try {
			br = new BufferedReader(new FileReader(this.getServletContext()
					.getRealPath("/serverConfig.txt")));
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
	        
			// String everything = sb.toString();
			// System.out.println("file: "+everything);
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

		System.out.println("id:  " + input.id + "");
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/" + main_database_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* insert new user */
			int returnCode = st.executeUpdate(" insert into  "
					+ " users  values ( " + "\'" + input.userInfo.userName
					+ "\', " + "md5(\'" + input.userInfo.pwd + "\') " + " );");

			returnPack.isSuccess = (returnCode >= 0);
			returnPack.id = input.userInfo.userName;
			returnPack.projectName = input.projectName;

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			returnPack.userInfo.existed = true;

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

		return returnPack;
	}

	public DataPack authenticate(DataPack input)
			throws IllegalArgumentException {

		DataPack returnPack = new DataPack();

		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";

		try {
			br = new BufferedReader(new FileReader(this.getServletContext()
					.getRealPath("/serverConfig.txt")));
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
	        
			// String everything = sb.toString();
			// System.out.println("file: "+everything);
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

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/" + main_database_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* find if the user, password combination exists */
			rs = st.executeQuery("select count(*) from users where "
					+ "username = " + "\'" + input.userInfo.userName
					+ "\' and " + "pwd = " + "md5(\'" + input.userInfo.pwd
					+ "\');");
			int count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count == 1) {
				returnPack.userInfo.authSuccess = true;
			} else if (count == 0) {
				returnPack.userInfo.authSuccess = false;

			}

			returnPack.isSuccess = (count >= 0);
			returnPack.id = input.userInfo.userName;
		} catch (SQLException e) {
			e.printStackTrace();
			returnPack.userInfo.existed = true;

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
		return returnPack;
	}

	public DataPack getAllUserList(DataPack input)
			throws IllegalArgumentException {

		DataPack returnPack = new DataPack();

		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";

		try {
			br = new BufferedReader(new FileReader(this.getServletContext()
					.getRealPath("/serverConfig.txt")));
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
	        
			// String everything = sb.toString();
			// System.out.println("file: "+everything);
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

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/" + main_database_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* get all user names */
			rs = st.executeQuery("select username from users ");

			while (rs.next()) {
				returnPack.userInfo.userList.add(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			returnPack.userInfo.existed = true;

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
		return returnPack;
	}

	public DataPack getAllProjectList(DataPack input)
			throws IllegalArgumentException {

		DataPack returnPack = new DataPack();

		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";

		try {
			br = new BufferedReader(new FileReader(this.getServletContext()
					.getRealPath("/serverConfig.txt")));
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
	        
			// String everything = sb.toString();
			// System.out.println("file: "+everything);
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

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/" + main_database_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* get project list */
			rs = st.executeQuery("select * from projects ");

			while (rs.next()) {
				returnPack.projectInfo.projectNameList.add(rs.getString(1));
				returnPack.projectInfo.projectDescripList.add(rs.getString(2));
			}

			returnPack.isSuccess = true;

		} catch (SQLException e) {

			e.printStackTrace();
			returnPack.userInfo.existed = true;

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
		return returnPack;
	}

	public DataPack addUserToProject(DataPack input)
			throws IllegalArgumentException {

		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";

		try {
			br = new BufferedReader(new FileReader(this.getServletContext()
					.getRealPath("/serverConfig.txt")));
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
	        
			// String everything = sb.toString();
			// System.out.println("file: "+everything);
			br.close();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {

		}

		DataPack returnPack = new DataPack();
		String username = input.id;
		String project_name = input.projectName;

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

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/" + main_database_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* insert user into user_project table */
			int returnCode = st.executeUpdate("insert into  "
					+ " user_project  values ( " + "\'" + username + "\'"
					+ "\'" + project_name + "\'" + " )");

		} catch (SQLException e) {
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

		return returnPack;
	}

	public DataPack removeUserFromProject(DataPack input)
			throws IllegalArgumentException {

		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";

		try {
			br = new BufferedReader(new FileReader(this.getServletContext()
					.getRealPath("/serverConfig.txt")));
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
	        
			// String everything = sb.toString();
			// System.out.println("file: "+everything);
			br.close();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {

		}

		DataPack returnPack = new DataPack();
		String userName = input.id;
		String project_name = input.projectName;

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
					"jdbc:postgresql://127.0.0.1:5432/" + main_database_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* remove user from user_project */
			int returnCode = st.executeUpdate(" remove from  "
					+ " user_project  where  " + "user_name = \'" + userName
					+ "\' and " + "project_name = \'" + project_name + "\' "
					+ " ;");

		} catch (SQLException e) {

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
		return returnPack;
	}

	boolean checkUserInProject(String user_name, String project_name) {

		/* read database details from file */
		BufferedReader br;
		current_project_name = "";
		main_database_name = "";

		try {
			br = new BufferedReader(new FileReader(this.getServletContext()
					.getRealPath("/serverConfig.txt")));
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
	        
			// String everything = sb.toString();
			// System.out.println("file: "+everything);
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

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/" + main_database_name,
					"postgres", "fujiko");
			st = connection.createStatement();

			/* check if user belongs to a project */
			rs = st.executeQuery("select * from user_project where project_name = "
					+ "\'"
					+ project_name
					+ "\' and "
					+ "user_name  = "
					+ "\'"
					+ project_name + "\'" + ";");

			while (rs.next()) {
				return true;
			}

		} catch (SQLException e) {

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

		return false;
	}
}
