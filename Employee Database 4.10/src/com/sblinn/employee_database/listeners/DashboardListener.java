package com.sblinn.employee_database.listeners;


import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.sblinn.employee_database.objects.User;
import com.sblinn.employee_database.objects.Employee.AccessLevel;
import com.sblinn.employee_database.ui.AdminUI;
import com.sblinn.employee_database.ui.EmployeeUI;
import com.sblinn.employee_database.ui.UserInterface;
import com.sblinn.employee_database.ui.UserInterface.FrameView;
import com.sblinn.employee_database.utilities.Admin;
import com.sblinn.employee_database.utilities.DisplayMethods;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;



/**
 * Responds to JComponents in Employee UI and Admin UI.
 * 
 * @author sarablinn
 *
 */
public class DashboardListener implements ActionListener {

	
	/**
	 * <code>User</code> object from one of the UI class instances.
	 */
	private User user;
	
	
	private EmployeeUI employeeUI;
	
	
	private AdminUI adminUI;
	
	
	
	/**
	 * Constructs a <code>DashboardListener</code> from <code>EmployeeUI</code>.
	 *
	 * @param employeeUI <code>EmployeeUI</code>
	 */
	public DashboardListener(EmployeeUI employeeUI) {
		this.employeeUI = employeeUI;
		this.user = employeeUI.getUser();
	}
	
	
	/**
	 * Constructs a <code>DashboardListener</code> from <code>AdminUI</code>.
	 *
	 * @param adminUI <code>AdminUI</code>
	 */
	public DashboardListener(AdminUI adminUI) {
		this.adminUI = adminUI;
		this.user = adminUI.getUser();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		String btnName = jb.getName();
		
		String someEmployeeID;
		
		if(UserInterface.getCurrentFrameView().name() != btnName) {
			switch (btnName) {
			
			case "VIEW_CURRENT_USER_INFO": 
				UserInterface.setCurrentFrameView(FrameView.VIEW_CURRENT_USER_INFO);
				employeeUI.clearDashboard();
				employeeUI.showUserInfoPage();
				break;
				
			case "VIEW_CURRENT_USER_ACCOUNT": // button text = "Account"
				UserInterface.setCurrentFrameView(FrameView.VIEW_CURRENT_USER_ACCOUNT);
				if(user.getAccessLevel().equals(AccessLevel.ADMIN)) {
					adminUI.clearDashboard();
					adminUI.showAdminAccountPage();
				}
				if(user.getAccessLevel().equals(AccessLevel.EMPLOYEE)) {
					employeeUI.clearDashboard();
					employeeUI.showAccountPage();
				}
				break;
				
			case "VIEW_CHOOSE_EMPLOYEE_AS_ADMIN":
				UserInterface.setCurrentFrameView(FrameView.VIEW_CHOOSE_EMPLOYEE_AS_ADMIN);
				adminUI.clearDashboard();
				adminUI.showAdminChooseEmployee(FrameView.VIEW_EMPLOYEE_AS_ADMIN, 
						"View Employee Information");
				break;
				
			case "VIEW_CHOOSE_EDIT_EMPLOYEE_AS_ADMIN":
				UserInterface.setCurrentFrameView(FrameView.VIEW_CHOOSE_EDIT_EMPLOYEE_AS_ADMIN);
				adminUI.clearDashboard();
				adminUI.showAdminChooseEmployee(FrameView.VIEW_EDIT_EMPLOYEE_AS_ADMIN,
						"Edit Employee Information");
				break;
				
			case "VIEW_EMPLOYEE_AS_ADMIN": 
				/* Remove component (any previous jscrollpanes) so only the employee chooser exists,
			 	in the case that this isn't the first time an employee id has been submitted. */
				adminUI.removeComponentAtIndex(1, adminUI.getDashboard());
				adminUI.clearViewEmployeePanel();
				someEmployeeID = adminUI.getEmployeeIDInput().getText();
				if(Admin.isValidID(Integer.parseInt(someEmployeeID), "employees_tbl")) {
					// remove any previous error messages
					adminUI.removeComponentAtIndex(3, adminUI.getChooseEmployeePanel());
					adminUI.showAdminViewEmployeeInfoPage(someEmployeeID, adminUI.getScrollPane());
				}
				else {
					JLabel warningLbl = new JLabel();
					displayFeedback("Invalid employee ID", warningLbl,  Color.RED);
				}
				break;
				
			case "VIEW_EDIT_EMPLOYEE_AS_ADMIN":
				/* Remove component (any previous jscrollpanes) so only the employee chooser exists,
				 	in the case that this isn't the first time an employee id has been submitted. */
				adminUI.removeComponentAtIndex(1, adminUI.getDashboard());
				adminUI.clearViewEmployeePanel();
				someEmployeeID = adminUI.getEmployeeIDInput().getText();
				
				if(isValidInputID(someEmployeeID)) {
					// remove any previous error messages
					adminUI.removeComponentAtIndex(3, adminUI.getChooseEmployeePanel());
					adminUI.showAdminEditPage(Integer.parseInt(someEmployeeID), 
							adminUI.getScrollPane());
				}
				break;
			
			case "VIEW_ADD_NEW_EMPLOYEE_AS_ADMIN":
				adminUI.clearDashboard();
				UserInterface.setCurrentFrameView(FrameView.VIEW_ADD_NEW_EMPLOYEE_AS_ADMIN);
				adminUI.showAdminCreateEmployeePage();
			}
		}
	}
	
	
	/**
	 * Returns true if the input ID is a valid ID or is not the same as the 
	 * current Admin user (Admin cannot edit their own info), or else it returns
	 * false and displays an error message.
	 * 
	 * @param employeeID <code>String</code>
	 * @return <code>boolean</code>
	 */
	private boolean isValidInputID(String employeeID) {
		JLabel warningLbl = new JLabel();
		
		if(employeeID.equals(adminUI.getUser().getUserID())) {
			displayFeedback("Error: cannot edit your own info", warningLbl, 
					Color.RED);
			return false;
		}
		else if(!Admin.isValidID(Integer.parseInt(employeeID), "employees_tbl") ) {
			displayFeedback("Invalid employee ID", warningLbl, Color.RED);
			return false;
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
		LayoutManager layoutMgr = new MigLayout(new LC().wrap());
		DisplayMethods.setUpMessage(message, warningLbl, adminUI.getMessagePanel(), 
				layoutMgr, Color.RED);
		adminUI.getChooseEmployeePanel().add(adminUI.getMessagePanel());
		UserInterface.getFrame().pack();
		adminUI.getMessagePanel().setVisible(true);
		adminUI.getMessagePanel().repaint();
	}
	
}
