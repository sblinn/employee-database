package com.sblinn.employee_database.utilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sblinn.employee_database.Application;


/**
 * Handles connection to relational database. 
 * 
 * All fields and methods are static, therefore, instantiation of <code>DBConnector</code>
 * is not required to use the methods. This specific detail could be altered if desired,
 * or for more complex application.
 * 
 * @author sarablinn
 *
 */
public class DBConnector {

	
	
	private static String staticUrl = "jdbc:mysql://localhost:3306/employees_database?serverTimezone=UTC";

	/**
	 * Static <code>Connection</code> field for DBConnector class.
	 */
	public static Connection connection;
	
	/**
	 * Static <code>Statement</code> field for DBConnector class.
	 */
	public static Statement statement;
	
	/**
	 * Static <code>ResultSet</code> field for DBConnector class.
	 */
	public static ResultSet resultSet;
	
	
	
	/**
	 * <code>DBConnector</code> default blank constructor.
	 */
	private DBConnector() {
		/* no purpose currently */
	}
	
	
	/**
	 * Checks if the database is connected.
	 * @return boolean 
	 */
	public static boolean isConnected() {
		if (connection != null && statement != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Connects to a database using the supplied URL String.
	 * @param url
	 * @param username
	 * @param password
	 */
	public static void connectToDatabase(String url, String username, String password) {
		try {
			connection = DriverManager.getConnection(url, username, password);
			statement = connection.createStatement();

			//System.out.println("\nNew Connection Successful.\n");
		}
		catch (SQLException e) {
			System.out.println("Error while trying to connect to database.");
			e.printStackTrace();

		}
	}
	
	/**
	 * Connects to the database using a static field specifying a localhost URL String.
	 * @param username String
	 * @param password String
	 */
	public static void connectToDatabase(String username, String password) {
		connectToDatabase(staticUrl, username, password);
	}
	
	
	/**
	 * Connects to the database using static methods supplied in <code>Application</code>
	 * class. 
	 */
	public static void connectForTestingPurposes() {
		connectToDatabase(Application.serverURL, Application.serverUsername, Application.serverPassword);
	}
	
	
	/**
	 * Closes all database connections held in <code>DBConnector</code>, including 
	 * static fields for <code>ResultSet</code>, <code>Statement</code> and 
	 * <code>DriverManager</code>.
	 */
	public static void disconnectDatabase() {
		disconnectDatabase(null);
	}
	
	/**
	 * Closes all database connections held in <code>DBConnector</code>, as well as
	 * the <code>ResultSet</code> provided which has been created outside of 
	 * <code>DBConnector</code> class.
	 */
	public static void disconnectDatabase(ResultSet rs) {
		if (isConnected()) {
			closeResultSet(rs);
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
			catch (SQLException e) {
				System.out.println("Error while trying to disconnect.");
			}
		}
	}
	
	/**
	 * Closes a given <code>ResultSet</code> which has been created and used outside of
	 * <code>DBConnector</code>.
	 * @param resultSet 
	 */
	public static void closeResultSet(ResultSet resultSet) {
		if (isConnected()) {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			}
			catch (SQLException e) {
				System.out.println("Error while trying to close ResultSet.");
			}
		}
	}
	
	
	
	/**
	 * Gets the static <code>Connection</code> field from <code>DBConnector</code>.
	 * @return <code>Connection</code> connection
	 */
	public static Connection getConnection() {
		return connection;
	}

	/**
	 * Gets the static <code>Statement</code> field from <code>DBConnector</code>.
	 * @return <code>Statement</code> statement 
	 */
	public static Statement getStatement() {
		return statement;
	}

	/**
	 * Sets the static <code>Statement</code> field from <code>DBConnector</code>.
	 */
	public static void setStatement(Statement statement) {
		DBConnector.statement = statement;
	}

	/**
	 * Gets the static <code>ResultSet</code> field from <code>DBConnector</code>.
	 * @return <code>ResultSet</code> resultSet 
	 */
	public static ResultSet getResultSet() {
		return resultSet;
	}
	
	
	

}
