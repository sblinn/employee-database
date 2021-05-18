package com.sblinn.employee_database.listeners;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import com.sblinn.employee_database.objects.User;
import com.sblinn.employee_database.ui.UserInterface;
import com.sblinn.employee_database.ui.UserWindow;
import com.sblinn.employee_database.utilities.Admin;
import com.sblinn.employee_database.utilities.DisplayMethods;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;



/**
 * <code>ActionListener</code> implementation which allows a user to 
 * reset the password saved to the employee database in the event that
 * the submit button is clicked.
 * 
 * @author sarablinn
 *
 */
public class ResetPasswordListener implements ActionListener {

	private User currentUser;

	private LinkedList<JPasswordField> passwordFields;

	private UserWindow userWindow;

	/**
	 * Constructs a <code>ResetPasswordListener</code> taking a <code>User</code>
	 * and an array of <i>(3)</i> <code>JPasswordField</code> components.
	 * 
	 * @param currentUser    <code>User</code>
	 * @param passwordFields <code>Array</code> of <code>JPasswordField</code>
	 * @param employeeUI     <code>EmployeeUI</code>
	 */
	public ResetPasswordListener(User currentUser, LinkedList<JPasswordField> passwordFields,
			UserWindow userWindow) {
		this.currentUser = currentUser;
		this.passwordFields = passwordFields;
		this.userWindow = userWindow;
	}

	

	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton jb = (JButton) event.getSource();
		String option = jb.getText();

		switch (option) {
		case "Reset":

			if (checkPasswordFields(passwordFields) == true) {
				String newPassword = String.valueOf(passwordFields.get(1).getPassword());
				Admin.setPassword(Integer.valueOf(currentUser.getUserID()), newPassword);
			}
			break;

		}
	}

	/**
	 * Checks that all three of the reset password fields are valid to reset the
	 * current password.
	 * 
	 * @param passwordFields <code>LinkedList<JPasswordField></code> of three 
	 * JPasswordFields
	 * @return <code>boolean</code> 
	 */
	private boolean checkPasswordFields(LinkedList<JPasswordField> passwordFields) {

		JPasswordField currentPassword = passwordFields.get(0);
		JPasswordField newPassword = passwordFields.get(1);
		JPasswordField confirmPassword = passwordFields.get(2);

		JLabel warningLbl = new JLabel();
		warningLbl.setForeground(Color.RED);

		// confirm that all the fields contain values
		if (!isEmptyField(currentPassword) && !isEmptyField(newPassword) && !isEmptyField(confirmPassword)) {

			String correctPassword = Admin.getPassword(Integer.valueOf(currentUser.getUserID()));
			if (!isCorrectPassword(correctPassword, currentPassword.getPassword())) {
				Toolkit.getDefaultToolkit().beep();
				displayErrorMessage("Incorrect information.", warningLbl);
				return false;
			} 
			else if (!Arrays.equals(newPassword.getPassword(), confirmPassword.getPassword())) {
				Toolkit.getDefaultToolkit().beep();
				displayErrorMessage("Passwords do not match.", warningLbl);
				return false;
			} 
			else {	
				warningLbl.setForeground(Color.LIGHT_GRAY);
				warningLbl.setFont(new Font(warningLbl.getFont().getName(), Font.BOLD, 
						warningLbl.getFont().getSize()));
				displayErrorMessage("Password reset successful.", warningLbl);
				return true;
			}
		} else {
			Toolkit.getDefaultToolkit().beep();
			displayErrorMessage("* All fields required.", warningLbl);
			return false;
		}
	}

	/**
	 * Returns true if <code>JPasswordField</code> is considered empty: Is also 
	 * considered empty if the label text is currently displayed in the field.
	 * 
	 * @param passwordField <code>JPasswordField</code>
	 * @return <code>boolean</code>
	 */
	private boolean isEmptyField(JPasswordField passwordField) {
		if (String.valueOf(passwordField.getPassword()).equals(passwordField.getName())) {
			return true;
		} else if (passwordField.getPassword().length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Compares input password to the <code>correctPass</code> <code>String</code> 
	 * password parameter (which is intended to be the password currently stored
	 * in the database); returns true if all the characters are the same.
	 * 
	 * @param correctPass <code>String</code>
	 * @param inputPass <code>char[]</code>
	 * @return <code>boolean</code>
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
	 * Displays an error message in a <code>JLabel</code>, which will be added to
	 * the <code>messagePanel</code>, when resetting password in the user's dashboard.
	 * 
	 * @param message <code>String</code>
	 * @param warningLbl <code>JLabel</code>
	 */
	private void displayErrorMessage(String message, JLabel warningLbl) {
		LayoutManager layoutMgr = new MigLayout(new LC().wrap().alignX("center"));
		DisplayMethods.setUpMessage(message, warningLbl, 
				userWindow.getMessagePanel(), layoutMgr, Color.RED);
		UserInterface.getFrame().pack();
		userWindow.getMessagePanel().setVisible(true);
	}
}
