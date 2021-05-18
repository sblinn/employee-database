package com.sblinn.employee_database.ui;



import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sblinn.employee_database.listeners.AccessListener;
import com.sblinn.employee_database.listeners.LoginListener;
import com.sblinn.employee_database.objects.Employee.AccessLevel;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;


/**
 * 
 * @author sarablinn
 *
 */
public class LoginUI extends UserInterface {

	
	
	/**
	 * <code>JTextField</code> where user ID is collected.
	 */
	private JTextField userIDInput;
	
	/**
	 * <code>JPasswordField</code> where user password is collected.
	 */
	private JPasswordField passwordInput;
	
	/**
	 * <code>JPanel</code> for login messages.
	 */
	private JPanel messagePanel = new JPanel(new MigLayout(
			new LC().alignX("center")));
	
	/**
	 * <code>AccessLevel</code> enum, ADMIN or EMPLOYEE.
	 */
	public AccessLevel currentAccessLevel;
	
	
	/**
	 * Returns the <code>AccessLevel</code> enum value in <code>LoginUI</code>.
	 * @return <code>mode AccessLevel</code>
	 */
	public AccessLevel getCurrentAccessLevel() {
		return this.currentAccessLevel;
	}
	
	/**
	 * Returns the <code>JPanel messagePanel</code> in <code>LoginUI</code>.
	 * @return <code>messagePanel JPanel</code>
	 */
	public JPanel getMessagePanel() {
		return this.messagePanel;
	}
	
	/**
	 * Returns the <code>JTextField</code> in <code>LoginUI</code> used to obtain
	 * the user ID at login.
	 * @return <code>userIDInput JTextField</code>n
	 */
	public JTextField getUserIDInput() {
		return this.userIDInput;
	}
	
	/**
	 * Returns the <code>JPasswordField passwordInput</code> in <code>LoginUI</code>
	 * used to obtain password input at login.
	 * @return <code>messagePanel JPanel</code>
	 */
	public JPasswordField getPasswordInput() {
		return this.passwordInput;
	}
	
	
	
	/**
	 * Builds first window: the Access window, where user must choose which 
	 * type of access view they would like to use. 
	 */
	public void chooseAccess() {
		resetFrame();
		setFrame(new JFrame("Employee Database Access"));
		getFrame().setResizable(false);
		getFrame().setBounds(100,100, 300, 500); // (x,y,w,h)
		getFrame().setLayout(new MigLayout());
		
		// ACCESS PANEL
		JPanel accessPanel = new JPanel();
		accessPanel.setLayout(new MigLayout(
				new LC().wrap().align("center", "center"),
				new AC().grow().fill() )
		); 
		
		// ACCESS ELEMENTS
		JButton adminAccessBtn = new JButton("Admin Access");
		JButton employeeAccessBtn = new JButton("Employee Access");
		
		// ADD ACCESSLISTENERS TO BUTTONS
		adminAccessBtn.addActionListener(new AccessListener(this));
		employeeAccessBtn.addActionListener(new AccessListener(this));
		
		// ADD ELEMENTS TO ACCESS PANEL
		accessPanel.add(adminAccessBtn);
		accessPanel.add(employeeAccessBtn);
		
		// ADD ACCESSPANEL TO CONTENTPANE
		getFrame().getContentPane().add(accessPanel);
		
		// pack, then request focus so that focus doesn't default to first btn
		getFrame().pack();
		getFrame().getContentPane().requestFocusInWindow();
		
		initialize(); 
	}
	
	/**
	 * Builds the second window: the Login window, where user must log in. 
	 * 
	 * <p>Sets the <code>AccessLevel</code> mode according to what was selected 
	 * in the previous access window. The Login window buttons utilize 
	 * <code>LoginListener</code>, which handles the login process, as well as
	 * checks that the user has the clearance to access ADMIN tools,
	 * if that was the option selected previously.  
	 * 
	 * @param selectedMode <code>AccessLevel</code>
	 */
	public void loginAccess(AccessLevel selectedMode) {
		this.currentAccessLevel = selectedMode;
		resetFrame();
		setFrame(new JFrame("Login as " + selectedMode.toString()));
		getFrame().setResizable(false);
		getFrame().setBounds(100,100, 300, 500); // (x,y,w,h)
		getFrame().setLayout(new MigLayout(new LC().wrap()) );
		
		// LOGIN PANEL
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new MigLayout(
				new LC().wrapAfter(2).fillX(),				// Layout Constraints
				new AC().align("right").gap("rel").grow(),	// Column constraints
				new AC().gap("rel") )						// Row constraints 					
		); 
		
		// SUBMIT PANEL
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new MigLayout(
				new LC(), new AC().gap("rel").grow(),
				new AC().gap("5"))
		);
		
		// LOGIN PANEL ELEMENTS
		JLabel userIDLabel = new JLabel("ID: ");
		JLabel passwordLabel = new JLabel("Password: ");
		
		userIDInput = new JTextField();
		passwordInput = new JPasswordField();
		
		userIDLabel.setLabelFor(userIDInput);
		passwordLabel.setLabelFor(passwordInput);
		
		// SUBMIT PANEL ELEMENTS
		JButton submitBtn = new JButton("Submit");
		JButton cancelBtn = new JButton("Cancel");
		
		// ADD ELEMENTS TO LOGIN PANEL
		loginPanel.add(userIDLabel);
		loginPanel.add(userIDInput, "width 150::"); // min:preferred:max
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordInput, "width 150::");
		
		// ADD ELEMENTS TO SUBMIT PANEL
		submitPanel.add(submitBtn, "width :110:");
		submitPanel.add(cancelBtn, "width :110:");
		
		// ADD LOGINLISTENER TO SUBMIT PANEL BUTTONS
		submitBtn.addActionListener(new LoginListener(this));
		cancelBtn.addActionListener(new LoginListener(this));
		
		// ADD ALL PANELS TO CONTENTPANE
		getFrame().getContentPane().add(loginPanel);
		getFrame().getContentPane().add(submitPanel);
		
		initialize();
	}
	
}
