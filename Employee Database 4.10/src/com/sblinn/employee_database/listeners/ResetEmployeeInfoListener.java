package com.sblinn.employee_database.listeners;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sblinn.employee_database.ui.AdminUI;
import com.sblinn.employee_database.ui.UserInterface;
import com.sblinn.employee_database.utilities.Admin;
import com.sblinn.employee_database.utilities.DisplayMethods;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;


/**
 * 
 * @author sarablinn
 *
 */
public class ResetEmployeeInfoListener implements ActionListener {

	private String employeeID;
	
	private ArrayList<String> employeeInfo;
	
	private ArrayList<JComponent> inputComponents;
	
	private String[] categoryLblNames = {"name", "surname", "dept",
			"title", "status", "access_level", "password"};
	
	private LinkedList<JPasswordField> passwordFields;
	
	private AdminUI adminUI;
	
	private LayoutManager messagePanelLytMgr = new MigLayout(new LC().wrap().alignX("center"));
	
	
	public ResetEmployeeInfoListener(ArrayList<String> employeeInfo, 
			ArrayList<JComponent> inputComponents, LinkedList<JPasswordField> passwordFields, 
			AdminUI adminUI) {
		this.employeeInfo = employeeInfo;
		this.inputComponents = inputComponents;
		this.passwordFields = passwordFields;
		this.employeeID = employeeInfo.get(0);
		this.adminUI = adminUI;
	}
	
	/**
	 * Checks if a component's value changed then updates the database.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(adminUI.getMessagePanel() != null) {
			adminUI.getMessagePanel().removeAll();
			UserInterface.initialize();
		}
		
		String message = "Are you sure you want to \nsave changes made to \nemployee " 
				+ employeeID + "?";
		String[] options = {"Cancel", "Save"};
		
		int result = JOptionPane.showOptionDialog(UserInterface.getFrame(), message, 
				"Unsaved Changes", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, 
				null, options, options[1]);

		if(result == 1) {
			/* employeeInfo arraylist indices:
			 * { 0 = id, 1 = name, 2 = surname, 3 = department
			 *   4 = title, 5 = hire_date, 6 = termination_date
			 *   7 = status, 8 = access_level, 9 = password }
			 */
			boolean isUpdated = false;
			for(int index = 0; index < 8; index ++) {
				// for JTextFields
				if(index < 4) {
					String inputValue = ((JTextField) inputComponents.get(index)).getText();
					String currentValue = employeeInfo.get(index + 1);
					if(!inputValue.equals(currentValue)) {
						Admin.setEmployeeData(Integer.parseInt(employeeID), categoryLblNames[index], 
								inputValue);
						employeeInfo.set(index + 1, inputValue);
						isUpdated = true;
					}
				}
				// for JComboBoxes
				else if(index == 4 || index == 5) {
					String selectedValue = (String) (((JComboBox<?>) inputComponents.get(index))
							.getSelectedItem().toString());
					String currentValue = employeeInfo.get(index + 3);
					if(selectedValue == null && currentValue != null) {
						Admin.setEmployeeDatatoNull(Integer.parseInt(employeeID), categoryLblNames[index]);
						employeeInfo.set(index + 3, selectedValue);
						isUpdated = true;
					}
					else if(currentValue != null && !selectedValue.equals(currentValue)) {
						Admin.setEmployeeData(Integer.parseInt(employeeID), categoryLblNames[index], 
								selectedValue);
						employeeInfo.set(index + 3, selectedValue);
						isUpdated = true;
					}
				}
				// for JPasswordFields
				else if(!isEmptyField((JPasswordField) inputComponents.get(6)) || 
						!isEmptyField((JPasswordField) inputComponents.get(7))) {
					if(checkPasswordFields(passwordFields) == true) {
						String newPassword = String.valueOf(passwordFields.get(1).getPassword());
						Admin.setPassword(Integer.valueOf(employeeID), newPassword);
						employeeInfo.set(9, newPassword);
						isUpdated = true;
					}
				}
			}
			if(isUpdated) {
				JLabel warningLbl = new JLabel();
				warningLbl.setForeground(Color.LIGHT_GRAY);
				warningLbl.setFont(new Font(warningLbl.getFont().getName(), Font.BOLD, 
						warningLbl.getFont().getSize()));
				displayFeedback("Update successful!", 
						warningLbl, Color.BLUE);
			}
		}	
	}
	
	/* TODO: to be used if something is implemented that sets off a JDialog notifying
	 * the admin user that there are unsaved edits and do they want to save or continue.
	 */
	/**
	 * Returns true if the values in the inputComponents are equal to the corresponding
	 * employeeInfo values. 
	 * 
	 * <p>NOTE if more info columns are added to the database or the order of the current
	 * info is changed, this method will have to be updated for the correct indices. </p>
	 * 
	 * @param employeeInfo <code>ArrayList<String></code>
	 * @param inputComponents <code>ArrayList<String></code>
	 * @return <code>boolean</code>
	 */
	public boolean isEditedField(ArrayList<String> employeeInfo, 
			ArrayList<JComponent> inputComponents) {
	
		/* employeeInfo arraylist indices:
		 * { 0 = id, 1 = name, 2 = surname, 3 = department
		 *   4 = title, 5 = hire_date, 6 = termination_date
		 *   7 = status, 8 = access_level, 9 = password }
		 */
		for(int index = 0; index < 8; index ++) {
			
			// for JTextFields
			if(index < 4) {
				String inputValue = ((JTextField) inputComponents.get(index)).getText();
				String currentValue = employeeInfo.get(index + 1);
				if(!inputValue.equals(currentValue)) {
					return true;
				}
			}

			// for JComboBoxes
			else if(index == 4 || index == 5) {
				String selectedValue = (String) (((JComboBox<?>) inputComponents.get(index))
						.getSelectedItem());
				String currentValue = employeeInfo.get(index + 3);
				if(selectedValue == null && currentValue != null) {
					return true;
				}
				else if(currentValue != null && !selectedValue.equals(currentValue)) {
					return true;
				}
			}
			if(!isEmptyField((JPasswordField) inputComponents.get(6)) || 
					!isEmptyField((JPasswordField) inputComponents.get(7))) {
				return true;
			}
		}
		return false;	
	}

	
	/**
	 * Checks that all three of the reset password fields are valid to reset the
	 * current password.
	 * 
	 * @param passwordFields array of the three JPasswordFields
	 * @return boolean
	 */
	private boolean checkPasswordFields(LinkedList<JPasswordField> passwordFields) {

		JPasswordField newPass = passwordFields.get(1);
		JPasswordField confirmPass = passwordFields.get(2);

		JLabel warningLbl = new JLabel();

		if((!isEmptyField(newPass) && isEmptyField(confirmPass)) ||
				(isEmptyField(newPass) && !isEmptyField(confirmPass)) ) {
			Toolkit.getDefaultToolkit().beep();
			displayFeedback("* All fields required.", warningLbl, Color.RED);
			return false;
		}
		// confirm that both the fields contain values
		if (!isEmptyField(newPass) && !isEmptyField(confirmPass)) {
			if (!Arrays.equals(newPass.getPassword(), confirmPass.getPassword())) {
				Toolkit.getDefaultToolkit().beep();
				displayFeedback("Passwords do not match.", warningLbl, Color.RED);
				return false;
			} 
			else {	
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Returns true if JPasswordField is considered empty: Is also considered empty
	 * if the label text is currently displayed in the field.
	 * 
	 * @param passwordField JPasswordField
	 * @return boolean
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
	 * Displays an error message in a <code>JLabel</code> when resetting password 
	 * in the user's dashboard.
	 * 
	 * @param message <code>String</code>
	 * @param warningLbl <code>JLabel</code>
	 * @param textColor <code>Color</code>
	 */
	private void displayFeedback(String message, JLabel warningLbl, Color textColor) {
		DisplayMethods.setUpMessage(message, warningLbl, adminUI.getMessagePanel(), 
				messagePanelLytMgr, textColor);	
		adminUI.getViewEmployeePanel().add(adminUI.getMessagePanel());
		adminUI.getMessagePanel().setVisible(true);
		adminUI.getDashboard().repaint();
	}

}
