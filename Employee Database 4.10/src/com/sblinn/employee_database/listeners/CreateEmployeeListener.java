package com.sblinn.employee_database.listeners;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sblinn.employee_database.objects.Employee;
import com.sblinn.employee_database.objects.Employee.AccessLevel;
import com.sblinn.employee_database.objects.Employee.WorkStatus;
import com.sblinn.employee_database.ui.AdminUI;
import com.sblinn.employee_database.ui.UserInterface;
import com.sblinn.employee_database.utilities.Admin;
import com.sblinn.employee_database.utilities.DisplayMethods;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;


/**
 * <code>CreateEmployeeListener</code> is an implementation of the 
 * <code>ActionListener</code> interface, which is used to handle the creation
 * of new employees to be added to the database.
 * 
 * @author sarablinn
 *
 */
public class CreateEmployeeListener implements ActionListener {

	private AdminUI adminUI;
	
	private ArrayList<JComponent> inputComponents;
	
	private ArrayList<String> inputValues;
	
	private LayoutManager messagePanelLytMgr = new MigLayout(new LC().wrap().alignX("center"));
	
	private JLabel warningLbl = new JLabel();
	
	
	/**
	 * Constructs a <code>CreateEmployeeListener</code>.
	 * 
	 * @param inputComponents <code>ArrayList<JComponent></code>
	 * @param adminUI <code>AdminUI</code>
	 */
	public CreateEmployeeListener(ArrayList<JComponent> inputComponents, AdminUI adminUI) {
		this.adminUI = adminUI;
		this.inputComponents = inputComponents;
	}


	
	/* 
	 * 0 name, 1 surname, 2 dept, 3 title --> jtextfield
	 * 4 status, 5 access level --> jcombobox
	 * 6 new password, 7 confirm password --> jpasswordfield
	 * 
	 * - Check if all the necessary fields have been filled appropriately.
	 * 		** required: name, surname
	 * 		-- all other fields can be filled at another time.
	 * - Create an Employee object from the available info.
	 * - Use Admin.java to add Employee to the DB.
	 * - Check if an employee by the same name exists already
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// PREPARE MESSAGEPANEL 
		if(adminUI.getMessagePanel() != null) {
			adminUI.getMessagePanel().removeAll();
			UserInterface.initialize();
		}
		// CHECK THAT REQUIRED FIELDS (NAME & SURNAME) ARE COMPLETED
		JTextField nameInput = (JTextField) inputComponents.get(0);
		JTextField surnameInput = (JTextField) inputComponents.get(1);
		if(nameInput.getText().isEmpty() || surnameInput.getText().isEmpty() ) {
			Toolkit.getDefaultToolkit().beep();
			displayFeedback("* Name and Surname required.", warningLbl, Color.RED);
		}
		else {
			// CHECK IF PASSWORDS ARE FILLED APPROPRIATELY
			JPasswordField newPassword = (JPasswordField) inputComponents.get(6);
			JPasswordField confirmPassword = (JPasswordField) inputComponents.get(7);
			if(checkPasswords(newPassword, confirmPassword)) {
				// PREPARE UNSAVED CHANGES JOPTIONPANE
				String message;
				if(Admin.getEmployeeID(nameInput.getText(), surnameInput.getText()) != 0) {
					message = "WARNING: " + nameInput.getText() + " " + surnameInput.getText() 
					+ "\n may already exist.\n Are you sure you want to"
					+ "\n save new employee?";
				}
				else {
					message = "Are you sure you want to \nsave new employee?";
				}

				String[] options = {"Cancel", "Save"};

				int result = JOptionPane.showOptionDialog(UserInterface.getFrame(), message, 
						"Unsaved Changes", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, 
						null, options, options[1]);

				if(result == 1) {
					printValues();
					createEmployee(inputComponents);
				}
			}
		}
	}
	

	// ERROR fix so that it checks for null input values and then adds null to the db.
	/**
	 * Creates new <code>Employee</code> from the information input to the input 
	 * components. If active work status is selected, the hire date will be set.
	 * 
	 * @param inputComponents <code>ArrayList<JComponent></code>
	 */
	private void createEmployee(ArrayList<JComponent> inputComponents) {
		String name = ((JTextField) inputComponents.get(0)).getText();
		String surname = ((JTextField) inputComponents.get(1)).getText();
		String dept = ((JTextField) inputComponents.get(2)).getText();
		String title = ((JTextField) inputComponents.get(3)).getText();
		WorkStatus status = (WorkStatus) ((JComboBox<?>) inputComponents.get(4)).getSelectedItem();
		AccessLevel accessLevel = (AccessLevel) ((JComboBox<?>) inputComponents.get(5)).getSelectedItem();
		String password = String.valueOf(((JPasswordField) inputComponents.get(6)).getPassword());
		
		Employee employee = new Employee(0, name, surname, dept, title, 
				null, null, status, accessLevel, password);
		
		// if WorkStatus == INACTIVE then don't set a hire date.
		if(status == WorkStatus.ACTIVE) {
			employee.setHireDate(new GregorianCalendar());
		}
		
		Admin.addNewEmployee(employee);
		
		warningLbl.setFont(new Font(warningLbl.getFont().getName(), Font.BOLD, 
				warningLbl.getFont().getSize()));
		displayFeedback("New employee " + employee.getID() + " saved.", 
				warningLbl, Color.BLUE);
	}
	

	/**
	 * Prints to the console, the input components names with the values input 
	 * by the user and adds them to the inputValues <code>ArrayList</code>.
	 */
	private void printValues() {
		inputValues = new ArrayList<>();
		System.out.println();
		for(int index = 0; index < 8; index++) {
			// for JTextFields
			if(index < 4) {
				String info = ((JTextField) inputComponents.get(index)).getText();
				if(info.isEmpty()) {
					info += "null";
				}
				System.out.println(inputComponents.get(index).getName() + ": " + info);
				inputValues.add(info);
			}
			// for JComboBoxes
			else if(index == 4 || index == 5) {
				String selectedValue;
				if(((JComboBox<?>) inputComponents.get(index)).getSelectedItem() != null) {
					selectedValue = ((JComboBox<?>) inputComponents.get(index))
							.getSelectedItem().toString();
				}
				else {
					selectedValue = "null";
				}
				System.out.println(inputComponents.get(index).getName() + ": " + selectedValue);
				inputValues.add(selectedValue);
			}
			// for JPasswordFields
			else if(index > 5) {
				JPasswordField newPassword = (JPasswordField) inputComponents.get(6);
				String passwordInfo;
				if(isEmptyPasswordField(newPassword)) {
					passwordInfo = "null";
				}
				else {
					passwordInfo = String.valueOf(newPassword.getPassword());
				}
				System.out.println(inputComponents.get(index).getName() + ": " 
						+ passwordInfo);
				if(index == 6) {
					inputValues.add(passwordInfo);
				}
			}
		}
		System.out.println();
	}


	/* TODO: to be used if something is implemented that sets off a JDialog notifying
	 * the admin user that there are unsaved edits and do they want to save or continue.
	 */
	/**
	 * Returns true if any of the values in the inputComponents have been filled. 
	 * 
	 * <p>NOTE if more info columns are added to the database or the order of the current
	 * info is changed, this method will have to be updated for the correct indices. </p>
	 * 
	 * @param inputComponents <code>ArrayList<String></code>
	 * @return <code>boolean</code>
	 */
	public boolean isEditedFields(ArrayList<JComponent> inputComponents) {
		for(int index = 0; index < 8; index ++) {
			// for JTextFields
			if(index < 4) {
				//String inputValue = ((JTextField) inputComponents.get(index)).getText();
				if(!((JTextField) inputComponents.get(index)).getText().isEmpty()) {
					return true;
				}
			}
			// for JComboBoxes
			else if(index == 4 || index == 5) {
				String selectedValue = (String) (((JComboBox<?>) inputComponents.get(index))
						.getSelectedItem());
				if(selectedValue != null) {
					return true;
				}
			}
			// for JPasswordFields
			if(!isEmptyPasswordField((JPasswordField) inputComponents.get(6)) || 
					!isEmptyPasswordField((JPasswordField) inputComponents.get(7))) {
				return true;
			}
		}
		return false;	
	}

	/**
	 * Returns true if <code>JPasswordField</code> is considered empty: Is also 
	 * considered empty if the label text is currently displayed in the field.
	 * 
	 * @param passwordField <code>JPasswordField</code>
	 * @return <code>boolean</code>
	 */
	private boolean isEmptyPasswordField(JPasswordField passwordField) {
		if (String.valueOf(passwordField.getPassword()).equals(passwordField.getName())) {
			return true;
		} else if (passwordField.getPassword().length == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the input passwords in both password fields are the same.
	 * 
	 * @param newPassword <code>JPasswordField</code>
	 * @param confirmPassword <code>JPasswordField</code>
	 * @return <code>boolean</code>
	 */
	private boolean isConfirmedPasswords(JPasswordField newPassword, JPasswordField confirmPassword) {
		if(newPassword.getPassword().length == confirmPassword.getPassword().length) {
			if (Arrays.equals(newPassword.getPassword(), confirmPassword.getPassword())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if both or none of the password fields have been filled and they
	 * are equal.
	 * 
	 * @param newPassword <code>JPasswordField</code>
	 * @param confirmPassword <code>JPasswordField</code>
	 * @return <code>boolean</code>
	 */
	private boolean checkPasswords(JPasswordField newPassword, JPasswordField confirmPassword) {
		// CHECK THAT BOTH (OR NO) PASSWORD FIELDS ARE FILLED 
		if((!isEmptyPasswordField(newPassword) && isEmptyPasswordField(confirmPassword)) ||
				(isEmptyPasswordField(newPassword) && !isEmptyPasswordField(confirmPassword)) ) {
			Toolkit.getDefaultToolkit().beep();
			displayFeedback("* Confirm password.", warningLbl, Color.RED);
			return false;
		}
		// CHECK THAT PASSWORDS ARE EQUAL
		if((!isEmptyPasswordField(newPassword) && !isEmptyPasswordField(confirmPassword))) {
			if(!isConfirmedPasswords(newPassword, confirmPassword)) {
				Toolkit.getDefaultToolkit().beep();
				displayFeedback("* Passwords not equal.", warningLbl, Color.RED);
				return false;
			}
		}
		return true;
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
