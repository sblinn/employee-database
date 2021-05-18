package com.sblinn.employee_database.listeners;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JButton;

import com.sblinn.employee_database.objects.User;
import com.sblinn.employee_database.objects.Employee.AccessLevel;
import com.sblinn.employee_database.objects.Employee.WorkStatus;
import com.sblinn.employee_database.ui.AdminUI;
import com.sblinn.employee_database.ui.EmployeeUI;
import com.sblinn.employee_database.ui.LoginUI;
import com.sblinn.employee_database.ui.UserInterface;
import com.sblinn.employee_database.utilities.DBConnector;
import com.sblinn.employee_database.utilities.DisplayMethods;

/**
 * CONTAINS METHODS THAT WILL NEED TO BE REPLACED!!
 * 
 * @author sarablinn
 *
 */

public class LoginListener implements ActionListener {


	private LoginUI loginUI;
	
	private String userID;
	
	private char[] inputPassword;

	
	
	/**
	 * Constructs a <code>LoginListener</code> which takes an instance of
	 * <code>LoginUI</code> as a parameter.
	 * 
	 * @param loginUI <code>LoginUI</code>
	 */
	public LoginListener(LoginUI loginUI) {
		this.loginUI = loginUI;
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		JButton jb = (JButton) event.getSource();
		String option = jb.getText();

		switch (option) {
		case "Submit":

			userID = (String) loginUI.getUserIDInput().getText().toString();
			inputPassword = loginUI.getPasswordInput().getPassword();
			
			if (checkLogin(userID, inputPassword) == true) {
				User newUser;
				
				if (loginUI.getCurrentAccessLevel().equals(AccessLevel.ADMIN)) {
					newUser = new User(userID, String.valueOf(inputPassword), 
							AccessLevel.ADMIN);
					AdminUI adminUI = new AdminUI(newUser);
					adminUI.showMainAdminPage(newUser);
				} 
				else if (loginUI.getCurrentAccessLevel().equals(AccessLevel.EMPLOYEE)) {
					newUser = new User(userID, String.valueOf(inputPassword), 
							AccessLevel.EMPLOYEE);
					EmployeeUI employeeUI = new EmployeeUI(newUser);
					employeeUI.showMainUserPage(newUser);
				}
			}
			break;

		case "Cancel":
		case "Log Out":

			if (this.loginUI == null) {
				this.loginUI = new LoginUI();
			}
			loginUI.chooseAccess();
			break;

		}
	}

	/**
	 * Checks if input login info is valid.
	 * 
	 * @return boolean
	 */
	private boolean checkLogin(String userID, char[] inputPassword) {

		ResultSet results = null;

		if (userID.isEmpty() || inputPassword.length == 0) {
			Toolkit.getDefaultToolkit().beep();
			displayLoginErrorMessage("All fields required.");
			return false;
		}

		try {
			DBConnector.connectForTestingPurposes();
			results = DBConnector.getStatement().executeQuery("select * from employees_tbl where id = " + userID + ";");
			String correctPass = null;
			String currentStatus = null;
			String accessLevel = null;
			boolean isResultsExists = false;

			// get the info if it exists, change isResultsExists to true
			while (results.next()) {
				isResultsExists = true;
				correctPass = results.getString("password");
				currentStatus = results.getString("status");
				accessLevel = results.getString("access_level");
			}

			// IF USERID DOESN'T EXIST, NO RESULTS WILL BE FOUND
			if (isResultsExists == false) {
				Toolkit.getDefaultToolkit().beep();
				displayLoginErrorMessage("Invalid user ID.");
				return false;
			}
			// ERROR: NOT AN ACTIVE EMPLOYEE
			if (WorkStatus.INACTIVE.toString().equals(currentStatus)) {
				Toolkit.getDefaultToolkit().beep();
				displayLoginErrorMessage("Inactive account.<br>" + "Contact Admin for help.");
				return false;
			}
			// USER PASSWORD INCORRECT
			if (!isCorrectPassword(correctPass, inputPassword)) {
				Toolkit.getDefaultToolkit().beep();
				displayLoginErrorMessage("Incorrect password");
				return false;
			}
			// USER PASSWORD CORRECT
			if (isCorrectPassword(correctPass, inputPassword)) {
				// ERROR: user selected ADMIN mode and they do not have ADMIN access
				if (loginUI.getCurrentAccessLevel().equals(AccessLevel.ADMIN) && !isAdmin(accessLevel)) {
					Toolkit.getDefaultToolkit().beep();
					String message = "User " + userID + " does not have "
							+ "access as Admin. <br>Select CANCEL to return to " + "main access page.";
					displayLoginErrorMessage(message);
					return false;
				} else {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.disconnectDatabase(results);
		}
		return false;
	}

	/**
	 * Displays a given error message in <code>LoginUI messagePanel</code>.
	 * 
	 * @param errorMessage
	 */
	private void displayLoginErrorMessage(String errorMessage) {
		DisplayMethods.displayHTMLMessage(errorMessage, loginUI.getMessagePanel(), UserInterface.getFrame(), null,
				"RED");
	}

	/**
	 * Compares input password to password in the database.
	 * 
	 * @param correctPass
	 * @param inputPass
	 * @return boolean
	 */
	private boolean isCorrectPassword(String correctPass, char[] inputPass) {
		if (correctPass.length() == inputPass.length) {
			if (Arrays.equals(inputPass, correctPass.toCharArray())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if user selected ADMIN mode and is an ADMIN.
	 * 
	 * @param accessLevel
	 * @return boolean
	 */
	private boolean isAdmin(String accessLevel) {
		if (AccessLevel.ADMIN.toString().equals(accessLevel)) {
			return true;
		}
		return false;
	}



}
