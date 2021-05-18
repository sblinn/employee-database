package com.sblinn.employee_database.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.sblinn.employee_database.objects.Employee.AccessLevel;
import com.sblinn.employee_database.ui.LoginUI;


/**
 * 
 * @author sarablinn
 *
 */
public class AccessListener implements ActionListener {

	/**
	 * 
	 */
	private LoginUI loginUI;
	
	
	/**
	 * Constructor for <code>AccessListener</code> class.
	 * 
	 * @param loginUI <code>LoginUI</code>
	 */
	public AccessListener(LoginUI loginUI) {
		this.loginUI = loginUI;
	}
	
	
	/**
	 * Runs the <code>LoginUI</code> login method which opens the Login window
	 * in the <code>AccessLevel</code> mode corresponding to the button chosen 
	 * by the user.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		String accessType = jb.getText();

		switch (accessType) {
		case "Admin Access":
			loginUI.loginAccess(AccessLevel.ADMIN);
			break;
		case "Employee Access":
			loginUI.loginAccess(AccessLevel.EMPLOYEE);
			break;
		}
	}

	
}
