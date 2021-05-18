package com.sblinn.employee_database;

import com.sblinn.employee_database.ui.UserInterface;

/**
 * Client class for running the Employee Database Application.
 * 
 * @author sarablinn
 *
 */
public class Application {

	public static String serverURL = "jdbc:mysql://localhost:3306/employees_database?serverTimezone=UTC";
	
	public static String serverUsername = "root";
	
	public static String serverPassword = "password";
	
	

			
			
	public static void main(String[] args) {
		UserInterface ui = new UserInterface();
		ui.startApplication();
		
		// "admin" will allow ability to view and edit employee info.
		// "employee" will allow view only their own info.
	}

}
