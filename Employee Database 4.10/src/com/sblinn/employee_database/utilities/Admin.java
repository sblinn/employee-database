package com.sblinn.employee_database.utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.sblinn.employee_database.objects.Employee;
import com.sblinn.employee_database.objects.Employee.AccessLevel;
import com.sblinn.employee_database.objects.Employee.WorkStatus;



/**
 * 
 * @author sarablinn
 *
 */
public class Admin {
	
	
	
	
	/**
	 * @deprecated Use <code>AddNewEmployee()</code> method instead.
	 * Adds Employee to the database table provided, assumes no null values, returns
	 * the new Employee's id integer. 
	 * 
	 * @param employee <code>Employee</code>
	 * @param tableName <code>String</code>
	 * 
	 * @return <code>int</code> new Employee ID
	 */
	public static int addEmployee(Employee employee, String tableName) {
		try {
			DBConnector.connectForTestingPurposes();
			String query = "INSERT into " + tableName  
					+ " (name, surname, dept, title, hire_date, termination_date, status, "
					+ "access_level, password) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStatement = 
					DBConnector.connection.prepareStatement(query);
			
			preparedStatement.setString(1, employee.getName());
			preparedStatement.setString(2, employee.getSurname());
			preparedStatement.setString(3, employee.getDept());
			preparedStatement.setString(4, employee.getTitle());
			preparedStatement.setString(5,
					employee.getFormattedDate(employee.getHireDate()));
			preparedStatement.setString(6,
					employee.getFormattedDate(employee.getTerminationDate()));
			preparedStatement.setString(7, employee.getStatus().toString());
			preparedStatement.setString(8, employee.getAccessLevel().toString());
			preparedStatement.setString(9, employee.getPassword());

			preparedStatement.executeUpdate();
			
			ResultSet results = DBConnector.getStatement().executeQuery(
					"SELECT last_insert_id()"); 
			while(results.next()) {
				employee.setID(Integer.parseInt(results.getString("id")));
			}
		} catch (SQLException e) {
			System.out.println("Error while adding Employee: " + 
					" " + employee.getName() + 
					" " + employee.getSurname() );
			e.printStackTrace();
		}
		finally {
			DBConnector.disconnectDatabase();
		}
		return employee.getID();
	}
	
	
	/**
	 * @deprecated Use <code>AddNewEmployee()</code> method instead.
	 * Adds existing <code>Employee</code> object to employees_tbl.
	 * 
	 * @param employee <code>Employee</code>
	 * @return <code>int</code> new Employee ID
	 */
	public static int addEmployee(Employee employee) {
		return addEmployee(employee, "employees_tbl");
	}
	
	
	/**
	 * Adds <code>Employee</code> to the database table provided, checks for null values, 
	 * and returns the new Employee's id integer. 
	 * 
	 * @param employee <code>Employee</code>
	 * @param tableName <code>String</code>
	 * @return <code>int</code> new Employee ID
	 */
	public static int addNewEmployee(Employee employee, String tableName) {
		int newID = 0; 
		try {
			DBConnector.connectForTestingPurposes();
			// ADD EMPLOYEE WITH NAME AND SURNAME
			String query = "INSERT into " + tableName + " (name, surname) values (?,?)";

			PreparedStatement preparedStatement = 
					DBConnector.connection.prepareStatement(query);
			
			preparedStatement.setString(1, employee.getName());
			preparedStatement.setString(2, employee.getSurname());
			preparedStatement.executeUpdate();
			// GET THE NEW EMPLOYEE ID
			ResultSet results = DBConnector.getStatement().executeQuery(
					"SELECT last_insert_id()"); 
			while(results.next()) {
				employee.setID(Integer.parseInt(results.getString(1)));
				newID = employee.getID();
				System.out.println("New ID for " + employee.getName() + " " 
						+ employee.getSurname() + ": " + newID);
			}
			// SET THE REST OF THE DATA
			results = DBConnector.getStatement().executeQuery("select * from employees_tbl");
			ResultSetMetaData metaData = results.getMetaData();
			
			ArrayList<String> columnList = new ArrayList<>();
			ArrayList<String> dataList = employee.getEmployeeFields();
			
			// PUT THE COLUMN NAMES IN THE ARRAYLIST
			for(int i = 1; i <= metaData.getColumnCount(); i ++) {
				columnList.add(metaData.getColumnName(i));
			}
			
			// UPDATE EACH ITEM IN THE DATALIST TO THE COLUMN IN COLUMNLIST
			for(int index = 3; index < columnList.size(); index++) {
				results = DBConnector.getStatement().executeQuery(
						"select * from " + tableName + " where id = " + newID + ";");
				query = "update " + tableName + " set " + columnList.get(index) + " = ?"
						+ "where id = " + newID + ";";
				
				preparedStatement = DBConnector.getConnection().prepareStatement(query);
				if(dataList.get(index) == null || dataList.get(index) == "null" 
						|| dataList.get(index).isEmpty()) {
					DBConnector.getStatement().executeUpdate(
							 "update " + tableName + " set " + columnList.get(index) 
							 + " = null where id = " + newID + ";");
				}
				// check if the new data is the correct character length 
				else {
					if(checkMaxCharLength(columnList.get(index), dataList.get(index))) {
						preparedStatement.setString(1, dataList.get(index));
						preparedStatement.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error while adding Employee: " + 
					" " + employee.getName() + " " + employee.getSurname() );
			e.printStackTrace();
		}
		finally {
			DBConnector.disconnectDatabase();
		}
		return newID;
	}
	
	
	/**
	 * Adds <code>Employee</code> to employees_tbl, checks for null values, and 
	 * returns the new Employee's id integer. 
	 * 
	 * @param employee <code>Employee</code>
	 * @param tableName <code>String</code>
	 * @return <code>int</code> new Employee ID
	 */
	public static int addNewEmployee(Employee employee) {
		return addNewEmployee(employee, "employees_tbl");
	}
	
	
	
	/**
	 * Returns an <code>Employee</code> object constructed by obtaining employee
	 * data corresponding to the given employee ID in the given database table, or
	 * returns <code>null</code> if no employee exists.
	 * 
	 * @param employeeID <code>int</code>
	 * @param tableName <code>String</code>
	 * @return <code>Employee</code>
	 */
	public static Employee getEmployee(int employeeID, String tableName) {
		if(isValidID(employeeID, tableName)) {
			ArrayList<String> eData = getAllEmployeeData(String.valueOf(employeeID));
			/* Employee(int id, String name, String surname, String department,  
				String title, String hireDate, String terminationDate,
				WorkStatus status, AccessLevel accessLevel, String password);
			*/
			Employee employee = new Employee(employeeID, eData.get(1), eData.get(2), 
					eData.get(3), eData.get(4), eData.get(5), eData.get(6), 
					WorkStatus.valueOf(eData.get(7)), AccessLevel.valueOf(eData.get(7)),
					eData.get(8));
			return employee;
		}
		System.out.println("No employee with ID: " + employeeID + " exists.");
		return null;
	}
	
	
	/**
	 * Obtains and returns a <code>String</code> of employee information from 
	 * the database based on the specified category parameter corresponding 
	 * to the database column. 
	 * 
	 * @param employeeID <code>int</code>
	 * @param category <code>String</code> 
	 * @return <code>String</code>
	 */
	public static String getEmployeeData(int employeeID, String category) {
		ResultSet results = null;
		try {
			DBConnector.connectForTestingPurposes();
			if(!isID(employeeID, "employees_tbl")) {
				System.out.println(employeeID + "does not exist in the database.");
			}
			else {
				results = DBConnector.getStatement().executeQuery(
						"select " + category + " from employees_tbl where id = " + 
								employeeID + ";");
				if(results == null) {
					System.out.println("No results for ID: " + employeeID);
				}
				else {
					while(results.next()) {
						return results.getString(category);
					}
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DBConnector.disconnectDatabase(results);
		}
		return null;
	}
	
	
	/**
	 * Obtains and returns a <code>String</code> of employee information from 
	 * the database based on the specified category parameter corresponding 
	 * to the database column. 
	 * 
	 * Takes a <code>String</code> for the employeeID rather than an <code>int</code>.
	 * 
	 * @param employeeID <code>String</code>
	 * @param category <code>String</code> 
	 * @return String 
	 */
	public static String getEmployeeData(String employeeID, String category) {
		return getEmployeeData(Integer.parseInt(employeeID), category);
	}
	
	/**
	 * Gets all of the employee's data in every category and returns it in 
	 * an arraylist.
	 * 
	 * @param employeeID <code>String</code> 
	 * @return <code>ArrayList</code> of <code>String</code> 
	 */
	public static ArrayList<String> getAllEmployeeData(String employeeID){
		ArrayList<String> employeeData = new ArrayList<>();
		
		ResultSet results = null;
		try {
			DBConnector.connectForTestingPurposes();
			if(!Admin.isID(Integer.parseInt(employeeID), "employees_tbl")) {
				System.out.println(employeeID + "does not exist in the database.");
			}
			else {
				results = DBConnector.getStatement().executeQuery(
						"select * from employees_tbl where id = " + employeeID + ";");

				ResultSetMetaData metaData = results.getMetaData();

				while(results.next()) {
					for(int i = 1; i <= metaData.getColumnCount(); i ++) {
						employeeData.add(results.getString(i));
						System.out.println(results.getString(i));
					}
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DBConnector.disconnectDatabase(results);
		}
		return employeeData;
	}
	
	
	/**
	 * Updates the database at the coordinate specified by employee id and 
	 * the category/column name, returns true if successful. Note: Cannot change
	 * employee id.
	 * 
	 * @param employeeID <code>int</code>
	 * @param tableName <code>String</code> name of the table in the database
	 * @param category <code>String</code>
	 * @param newData <code>String</code>
	 * @return <code>boolean</code>
	 */
	public static boolean setEmployeeData(int employeeID, String tableName, 
			String category, String newData) {
		if(checkMaxCharLength(category, newData)) {
			ResultSet results = null;
			try {
				DBConnector.connectForTestingPurposes();
				results = DBConnector.getStatement().executeQuery(
						"select * from " + tableName + " where id = " + employeeID + ";");
				if(results == null) {
					System.out.println("Unable to reset " + category 
							+ " for ID: " + employeeID);
				}
				String query = "update " + tableName + " set " + category + " = ?"
						+ "where id = " + employeeID + ";";
						
				PreparedStatement preparedStatement = 
						DBConnector.getConnection().prepareStatement(query);
				if(newData == null || newData == "null") {
					preparedStatement.setNull(1, Types.NULL);
				}
				else {
					preparedStatement.setString(1, newData);
				}

				preparedStatement.executeUpdate();
				
				/*
				 * DBConnector.connectForTestingPurposes(); results =
				 * DBConnector.getStatement().executeQuery(
				 * "select * from employees_tbl where id = " + employeeID + ";"); if(results ==
				 * null) { System.out.println("Unable to reset " + category + " for ID: " +
				 * employeeID); } else { DBConnector.getStatement().executeUpdate(
				 * "update employees_tbl set " + category + " = \"" + newData + "\" where id = "
				 * + employeeID + ";"); System.out.println("New " + category + " set to " +
				 * newData + " for ID: " + employeeID); return true; }
				 */
				
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			finally {
				DBConnector.disconnectDatabase(results);
			}
		}
		return false;
	}
	
	
	/**
	 * Updates the employees_tbl table in the database at the coordinate specified 
	 * by employee id and the category/column name, returns true if successful. 
	 * Note: Cannot change employee id.
	 * 
	 * @param employeeID <code>int</code>
	 * @param category <code>String</code>
	 * @param newData <code>String</code>
	 * @return <code>boolean</code>
	 */
	public static boolean setEmployeeData(int employeeID, String category, String newData) {
		return setEmployeeData(employeeID, "employees_tbl", category, newData);
	}
	
	
	/**
	 * Updates the database table with <code>null</code> value at the coordinate specified 
	 * by employee id and the category/column name, returns true if successful. 
	 * Note: Cannot change employee id.
	 * 
	 * @param employeeID <code>int</code>
	 * @param tableName <code>String</code> name of the table in the database
	 * @param category <code>String</code>
	 * @return <code>boolean</code>
	 */
	public static boolean setEmployeeDatatoNull(int employeeID, String tableName, 
			String category) {
		ResultSet results = null;
		try {
			DBConnector.connectForTestingPurposes();
			results = DBConnector.getStatement().executeQuery(
					"select * from " + tableName + " where id = " + employeeID + ";");
			if(results == null) {
				System.out.println("Unable to reset " + category + " for ID: " + employeeID);
			}
			else {
				DBConnector.getStatement().executeUpdate(
						 "update " + tableName + " set " + category + " = null where id = "
						 + employeeID + ";"); 
				System.out.println("New " + category + " set to null for ID: " 
						 + employeeID); 
				return true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DBConnector.disconnectDatabase(results);
		}
		return false;
	}
	
	/**
	 * Updates the employees_tbl with <code>null</code> value at the coordinate specified 
	 * by employee id and the category/column name, returns true if successful. 
	 * Note: Cannot change employee id.
	 * 
	 * @param employeeID <code>int</code>
	 * @param category <code>String</code>
	 * @return <code>boolean</code>
	 */
	public static boolean setEmployeeDatatoNull(int employeeID, String category) {
		return setEmployeeDatatoNull(employeeID, "employees_tbl", category);
	}
	
	
	/**
	 * Checks if a given <code>String</code> of data is the correct length 
	 * for the employees_tbl database specs.
	 * 
	 * @param category <code>String</code>
	 * @param newData <code>String</code>
	 * @return <code>boolean</code>
	 */
	public static boolean checkMaxCharLength(String category, String newData) {
		/*
		 *  NAME / SURNAME = 30
		 *  DEPT / TITLE / STATUS / ACCESS_LEVEL / PASSWORD = 20
		 *  HIRE_DATE / TERMINATION_DATE = 50
		 */
		switch(category) {
		case "name": case "surname":
			if(newData.length() <= 30) { return true; }	
			break;
		case "dept": case "title": case "status": 
		case "access_level": case "password":
			if(newData.length() <= 20) { return true; }
			break;
		case "hire_date": case "termination_date":
			if(newData.length() <= 50) { return true; }
			break;
		}
		return false;
	}
	
	
	/**
	 * Gets the user's password from the database.
	 * 
	 * @param userID <code>int</code>
	 * @return String password
	 */
	public static String getPassword(int userID) {
		return getEmployeeData(userID, "password");
	}
	
	
	/**
	 * Sets password for employee indicated by employee ID.
	 * 
	 * @param userID <code>int</code>
	 * @param password
	 */
	public static void setPassword(int userID, String password) {
		setEmployeeData(userID, "password", password);
	}

	
	/**
	 * Terminates the employee. Sets termination date and sets status to INACTIVE.
	 * 
	 * @param employeeID int 
	 */
	public static void terminateEmployee(int employeeID) {
		GregorianCalendar gc = new GregorianCalendar();
		String termDate = gc.getTime().toString();
		
		setEmployeeData(employeeID, "termination_date", termDate);
		setEmployeeData(employeeID, "status", "INACTIVE");
	}
	
	
	/**
	 * Checks if a given employee has been terminated.
	 * 
	 * @param employeeID <code>int</code>
	 * @return boolean
	 */
	public static boolean isTerminated(int employeeID) {
		// is not ACTIVE and has a set termination date
		if (!isActiveEmployee(employeeID, "employees_tbl") && 
				getEmployeeData(employeeID, "termination_date") != null) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Re-hires an employee that was previously terminated; Keeps the original 
	 * hire date, clears the termination date and sets status to ACTIVE.
	 * 
	 * @param employeeID <code>int</code>
	 */
	public static void reHireEmployee(int employeeID) {
		setEmployeeData(employeeID, "termination_date", "null");
		activateEmployeeStatus(employeeID);
	}
	
	
	/**
	 * Sets employee status to ACTIVE.
	 * 
	 * @param employeeID <code>int</code>
	 */
	public static void activateEmployeeStatus(int employeeID) {
		setEmployeeData(employeeID, "status", "ACTIVE");
	}
	
	
	/**
	 * Sets employee status to INACTIVE.
	 * 
	 * @param employeeID <code>int</code>
	 */
	public static void deactivateEmployeeStatus(int employeeID) {
		setEmployeeData(employeeID, "status", "INACTIVE");
	}
	
	
	/**
	 * Determines whether or not a given employee's status is ACTIVE or INACTIVE.
	 * 
	 * @param employeeID <code>int</code>
	 * @param tableName <code>String</code>
	 * @return boolean true if employee status is ACTIVE
	 */
	public static boolean isActiveEmployee(int employeeID, String tableName) {
		ResultSet results = null;
		try {
			DBConnector.connectForTestingPurposes();
			results = DBConnector.getStatement().
					executeQuery("select * from " + tableName + " where (id = " + employeeID + 
							" and status = \"ACTIVE\");");
			if(results != null) {
				return true;
			}
		}
		catch (SQLException e) {
			System.out.println("Error determining status of employee: " + employeeID + " in " +
					tableName + ".");
			e.printStackTrace();
		}
		finally {
			DBConnector.disconnectDatabase(results);
		}
		return false;
	}
	
	
	/**
	 * Searches for an employee by first and last name and returns corresponding
	 * employee ID, if found. 
	 * 
	 * NOTE: Spelling and formatting have to be exact, no checks in place yet
	 * to ensure variations may still be found. 
	 * 
	 * @param name <code>String</code>
	 * @param surname <code>String</code>
	 * @return int returns employee ID as <code>int</code>, returns 0 if 
	 * employee does not exist or cannot be found.
	 */
	public static int getEmployeeID(String name, String surname) {
		ResultSet results = null;
		try {
			DBConnector.connectForTestingPurposes();

			results = DBConnector.getStatement().executeQuery(
					"select * from employees_tbl where name = " + "\"" + name 
					+ "\"" + " AND surname = " + "\"" + surname + "\"" + ";");

			if(results == null) {
				System.out.println("No results for " + name + " " + surname  
						+ ". Make sure the formatted and spelled correctly.");
			}
			else {
				while(results.next()) {
					return results.getInt("id");
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DBConnector.disconnectDatabase(results);
		}
		return 0;
	}
	
	/**
	 * Checks if the employee ID exists in the database, specifically for use
	 * inside of <code>Admin.java</code>.
	 * 
	 * @param employeeID <code>int</code>
	 * @param tableName <code>String</code>
	 * @return boolean true if the employeeID exists in the table.
	 */
	private static boolean isID(int employeeID, String tableName) {
		ResultSet results = null;
		try {
			results = DBConnector.getStatement().
					executeQuery("select * from " + tableName + " where (id = " 
							+ employeeID + ");");
			while(results.next()) {
				if(results != null) {
					return true;
				}
			}
		}
		catch (SQLException e) {
			System.out.println("Error: " + employeeID + " NOT FOUND in " +
					tableName + ".");
			e.printStackTrace();
		}
		finally {
			DBConnector.closeResultSet(results);
		}
		return false;
	}
	
	/**
	 * Checks if the employee ID exists in the database.
	 * 
	 * @param employeeID <code>int</code>
	 * @param tableName <code>String</code>
	 * @return boolean true if the employeeID exists in the table.
	 */
	public static boolean isValidID(int employeeID, String tableName) {
		ResultSet results = null;
		try {
			DBConnector.connectForTestingPurposes();
			results = DBConnector.getStatement().
					executeQuery("select * from " + tableName + " where (id = " 
							+ employeeID + ");");
			while(results.next()) {
				if(results != null) {
					return true;
				}
			}
		}
		catch (SQLException e) {
			System.out.println("Error: " + employeeID + " NOT FOUND in " +
					tableName + ".");
			e.printStackTrace();
		}
		finally {
			DBConnector.disconnectDatabase(results);
		}
		return false;
	}
	
	
	/**
	 * Gets the <code>AccessLevel</code> of the employee: ADMIN or EMPLOYEE.
	 * 
	 * @param employeeID <code>int</code>
	 * @return String <code>AccessLevel</code>
	 */
	public static String getAccessLevel(int employeeID) {
		return getEmployeeData(employeeID, "access_level");
	}
	
	
	/**
	 * Sets the employee's <code>AccessLevel</code> to EMPLOYEE.
	 * 
	 * @param employeeID <code>int</code>
	 */
	public static void setEmployeeAccess(int employeeID) {
		setEmployeeData(employeeID, "access_level", "EMPLOYEE");
	}
	
	
	/**
	 * Sets the employee's <code>AccessLevel</code> to ADMIN.
	 * 
	 * @param employeeID <code>int</code>
	 */
	public static void setAdminAccess(int employeeID) {
		setEmployeeData(employeeID, "access_level", "ADMIN");
	}
	

	
}
