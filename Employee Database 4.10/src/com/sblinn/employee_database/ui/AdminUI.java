package com.sblinn.employee_database.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import com.sblinn.employee_database.listeners.CreateEmployeeListener;
import com.sblinn.employee_database.listeners.DashboardListener;
import com.sblinn.employee_database.listeners.LoginListener;
import com.sblinn.employee_database.listeners.PasswordFocusListener;
import com.sblinn.employee_database.listeners.ResetEmployeeInfoListener;
import com.sblinn.employee_database.listeners.ResetPasswordListener;
import com.sblinn.employee_database.listeners.TextfieldFocusListener;
import com.sblinn.employee_database.objects.User;
import com.sblinn.employee_database.objects.Employee.AccessLevel;
import com.sblinn.employee_database.objects.Employee.WorkStatus;
import com.sblinn.employee_database.utilities.Admin;
import com.sblinn.employee_database.utilities.DisplayMethods;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;


/**
 * 
 * @author sarablinn
 *
 */
public class AdminUI extends UserInterface implements UserWindow {

	
	
	private JPanel menuPanel;
	
	private JPanel dashboardPanel;
	
	/**
	 * <code>JPanel</code> for messages.
	 */
	private JPanel messagePanel = new JPanel(new MigLayout(
			new LC().alignX("center")));
	
	private JPanel viewEmployeePanel;
	
	private JPanel chooseEmployeePanel;
	
	private JScrollPane scrollPane;
	
	private JTextField employeeIDInput;
	
	private User user;
	
	private String fullUsername;
	
	
	@Override
	public User getUser() {
		return this.user;
	}
	
	@Override
	public JPanel getDashboard() {
		return this.dashboardPanel;
	}
	
	@Override
	public void clearDashboard() {
		if(dashboardPanel != null) {
			dashboardPanel.removeAll();
			dashboardPanel.repaint();
		}
	}
	
	@Override
	public JPanel getMessagePanel() {
		return messagePanel;
	}
	
	@Override
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	@Override
	public void removeComponentAtIndex(int index, JPanel panel) {
		Component[] components = panel.getComponents();
		if(components.length > index) {
			panel.remove(index);
			panel.repaint();
		}
	}
	
	/**
	 * Gets the <code>JPanel</code> used for Admin to view employee
	 * information
	 * 
	 * @return viewEmployeePanel <code>JPanel</code>
	 */
	@Override
	public JPanel getViewEmployeePanel() {
		return viewEmployeePanel;
	}
	
	/**
	 * Removes everything in the <code>viewEmployeePanel</code>.
	 */
	@Override
	public void clearViewEmployeePanel() {
		if(viewEmployeePanel != null) {
			viewEmployeePanel.removeAll();
		}
	}
	
	/**
	 * Returns the <code>chooseEmployeePanel</code> from <code>AdminUI</code>.
	 * 
	 * @return <code>chooseEmployeePanel</code> <code>JPanel</code>
	 */
	public JPanel getChooseEmployeePanel() {
		return chooseEmployeePanel;
	}
	
	/**
	 * Gets the <code>JTextField</code> that the Admin user inputs
	 * an employee ID into. 
	 * 
	 * @return employeeIDInput <code>JTextField</code>
	 */
	public JTextField getEmployeeIDInput() {
		return employeeIDInput;
	}
	
	
	
	/**
	 * Constructs the <code>User</code> info for an <code>AdminUI</code>.
	 * 
	 * @param user <code>User</code>
	 */
	public AdminUI(User user) {
		this.user = user;
		this.fullUsername = Admin.getEmployeeData(user.getUserID(), "name") + " " 
				+ Admin.getEmployeeData(user.getUserID(), "surname"); 
	}
	
	
	/**
	 * Displays the main Admin page to the GUI.
	 * 
	 * @param user <code>User</code>
	 */
	public void showMainAdminPage(User user) {
		setCurrentFrameView(FrameView.VIEW_ADMIN_MAIN);
		resetFrame();
		setFrame(new JFrame(fullUsername + " (" + user.getUserID() + ") as Admin"));
		
		// SET FRAME SIZE & SPECS
		// Aim for an 8:5 size ratio (my screen = 2560 x 1600)
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		getFrame().setMinimumSize(new Dimension((screenSize.width/3), (screenSize.height/3)));
		getFrame().setSize(new Dimension((screenSize.width/3), (screenSize.height/2)));
		getFrame().setMaximumSize(new Dimension(screenSize.width, screenSize.height));
	
		getFrame().setResizable(false);
		getFrame().setLocation(100, 100);
		
		getFrame().setLayout(new MigLayout(		// layout, column, row constraints
				new LC().fill(),
				new AC().gap("rel"),
				new AC().gap("rel") ) 
			);
		
		// CREATE MENU PANEL
		menuPanel = new JPanel();
			// menuPanel size must be set--other components size is based off this.
		Dimension menuPanelDimensions = new Dimension(getFrame().getWidth()/4, 
				getFrame().getHeight());
		menuPanel.setPreferredSize(menuPanelDimensions);
		menuPanel.setSize(menuPanelDimensions);
		menuPanel.setBackground(Color.LIGHT_GRAY);
		menuPanel.setLayout(new MigLayout(new LC().wrap().gridGapY("rel") ));
		
		// CREATE MENUTITLE PANEL
		JPanel menuTitlePanel = new JPanel();
		menuTitlePanel.setSize(new Dimension(menuPanel.getWidth(), menuPanel.getHeight()/3));
		menuTitlePanel.setBackground(null);
		menuTitlePanel.setLayout(new MigLayout(
				new LC().wrap().insets("10","5","5","10")));
		
		// CREATE DASHBOARD PANEL
		dashboardPanel = new JPanel();
		dashboardPanel.setPreferredSize(new Dimension((getFrame().getWidth()/4)*3, 
				getFrame().getHeight()));
		dashboardPanel.setForeground(dashboardGray);
		dashboardPanel.setLayout(new MigLayout());
		
		// MENUTITLEPANEL COMPONENTS
		JLabel welcomeLbl = new JLabel("Welcome,");
		JLabel nameLbl = new JLabel(fullUsername);
		
		Font labelFont = welcomeLbl.getFont(); // welcome & name will have same font name
		
		welcomeLbl.setFont(new Font(labelFont.getName(), Font.BOLD,
				DisplayMethods.getProperFontSize(welcomeLbl, menuTitlePanel)));
		nameLbl.setFont(new Font(labelFont.getName(), Font.BOLD,
				DisplayMethods.getNameLblFontSize(nameLbl, welcomeLbl, menuTitlePanel)));
		
		// ADD COMPONENTS TO MENUTITLEPANEL 
		menuTitlePanel.add(welcomeLbl, "align left");
		menuTitlePanel.add(nameLbl);
		
		// MENUPANEL COMPONENTS
		JButton viewEmpBtn = new JButton("View Employee");
		viewEmpBtn.setName(FrameView.VIEW_CHOOSE_EMPLOYEE_AS_ADMIN.name());
		JButton editEmpBtn = new JButton("Edit Employee");
		editEmpBtn.setName(FrameView.VIEW_CHOOSE_EDIT_EMPLOYEE_AS_ADMIN.name());
		JButton addNewEmpBtn = new JButton("Add New Employee");
		addNewEmpBtn.setName(FrameView.VIEW_ADD_NEW_EMPLOYEE_AS_ADMIN.name());
		JButton accountBtn = new JButton("Account");
		accountBtn.setName(FrameView.VIEW_CURRENT_USER_ACCOUNT.name());
		JButton logoutBtn = new JButton("Log Out");
		
		JLabel creditLbl = new JLabel("Sara Blinn 2021");
		creditLbl.setFont(new Font(creditLbl.getFont().getName(), Font.ITALIC, 7));
		
		logoutBtn.addActionListener(new LoginListener(new LoginUI()));
		
		// ADD EDASHBOARDLISTENER
		viewEmpBtn.addActionListener(new DashboardListener(this));
		editEmpBtn.addActionListener(new DashboardListener(this));
		addNewEmpBtn.addActionListener(new DashboardListener(this));
		accountBtn.addActionListener(new DashboardListener(this));
		
		// ADD COMPONENTS TO MENU PANEL
		menuPanel.add(menuTitlePanel, new CC().alignX("left").gapBottom("unrelated"));
		menuPanel.add(viewEmpBtn);
		menuPanel.add(editEmpBtn);
		menuPanel.add(addNewEmpBtn);
		menuPanel.add(accountBtn);
		menuPanel.add(logoutBtn);
		menuPanel.add(creditLbl, new CC().pushY().alignY("bottom"));
		
		// DASHBOARDPANEL COMPONENTS (INITIALLY)
		
		// ADD COMPONENTS TO DASHBOARD PANEL
		 
		// ADD PANELS TO CONTENTPANE
		// menuTitlePanel has already been added to menuPanel
		getFrame().getContentPane().add(menuPanel, "dock west");
		getFrame().getContentPane().add(dashboardPanel, new CC().gapRight("5"));

		initialize();
		
	}
	
	
	/**
	 * Displays a textfield on the dashboard the panel which allows the Admin user to
	 * input an employee ID. 
	 * 
	 * <p>The <code>FrameView</code> parameter will become the name of the 
	 * <code>JButton</code> clicked to submit the employee ID. The button name is 
	 * used as an indicator in the <code>DashboardListener</code>. The 
	 * <code>titleName</code> will be the title on the dashboard. 
	 * 
	 * @param frameView <code>FrameView</code>
	 * @param titleName <code>String</code> 
	 */
	public void showAdminChooseEmployee(FrameView frameView, String titleName) {
		dashboardPanel.setLayout(new MigLayout(
				new LC().wrap().fill().insets("10","10","10","5").topToBottom()));
		
		// CREATE ACCOUNT PANEL
		chooseEmployeePanel = new JPanel();
		viewEmployeePanel = new JPanel();
		messagePanel = new JPanel();
		
		chooseEmployeePanel.setMaximumSize(dashboardPanel.getSize());
		chooseEmployeePanel.setSize(dashboardPanel.getSize());
		chooseEmployeePanel.setLayout(new MigLayout(
				new LC().wrap()));
		
		viewEmployeePanel.setSize(dashboardPanel.getSize());
		
		// CREATE ACCOUNT PANEL COMPONENTS
		JTextPane infoTextPane = new JTextPane();
		employeeIDInput = new JTextField();
		JButton submitIDBtn = new JButton("Submit");
		submitIDBtn.setName(frameView.name());

		// FORMATTING
		employeeIDInput.setName("Employee ID");
		employeeIDInput.setText("Employee ID");
		employeeIDInput.setForeground(Color.LIGHT_GRAY);
		
		infoTextPane.setEditable(false);
		infoTextPane.setBackground(null);
		infoTextPane.setContentType("text/html");
		
		String htmlText = "<div style=\" font-family:" + defaultFont.getName() + "\">"
				+ "<h2>" + titleName + "</h2>"
				+ "<p style=\"font-size:" + defaultFont.getSize() + "pt" + "\">" 
				+ "Please submit a valid employee ID to view employee information:" 
				+ "</p>" + "</div>";
		infoTextPane.setText(htmlText);
		
		// ADD COMPONENTS TO ACCOUNT PANEL
		// SET UP THE PASSWORD FIELDS IN A LOOP 
		chooseEmployeePanel.add(infoTextPane);
		chooseEmployeePanel.add(employeeIDInput, "width :150:");
		chooseEmployeePanel.add(submitIDBtn, new CC().gapBefore("rel"));
		
		// ADD LISTENERS
		employeeIDInput.addFocusListener(new TextfieldFocusListener());
		submitIDBtn.addActionListener(new DashboardListener(this));
		
		// ADD ACCOUNTSCROLLPANE (ACCOUNTPANEL) TO DASHBOARD PANEL
		dashboardPanel.add(chooseEmployeePanel);
		initialize();
	}
	
	
	
	/**
	 * Shows the information of the employee provided by the <code>empID</code>
	 * parameter. 
	 * 
	 * <p>Creates a new <code>User</code> from the employee ID, then creates a 
	 * new instance of <code>EmployeeUI</code> from that instance of <code>User</code>.
	 * Then calls the method <code>setUpUserInfoPage()</code> from <code>EmployeeUI</code>.
	 * 
	 * @param empID <code>int</code>
	 */
	public void showAdminViewEmployeeInfoPage(String employeeID, JScrollPane scrollPane) {
		scrollPane = new JScrollPane(viewEmployeePanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		viewEmployeePanel.setBackground(Color.WHITE);
		
		User newUser = new User(employeeID, null, AccessLevel.EMPLOYEE);
		EmployeeUI employeeUI = new EmployeeUI(newUser);
		employeeUI.setUpUserInfoPage(employeeID, viewEmployeePanel, dashboardPanel);
		
		dashboardPanel.add(scrollPane);
		initialize();
	}
	
	
	/**
	 * Shows Admin account information, allowing Admin to reset password.
	 */
	public void showAdminAccountPage() {
		setUpAdminAccountPage(this.user, dashboardPanel);		
	}

	
	/**
	 * Shows Admin account information on a given <code>JPanel</code> 
	 * to allow the given user to reset current password. 
	 * 
	 * @param currentUser <code>User</code>
	 * @param dashboardPanel <code>JPanel</code>
	 */
	public void setUpAdminAccountPage(User currentUser, JPanel dashboardPanel) {
		// CREATE ACCOUNT PANEL
		viewEmployeePanel = new JPanel();
		JPanel accountPanel = new JPanel();

		accountPanel.setSize(new Dimension(dashboardPanel.getWidth(), 
				dashboardPanel.getHeight()));

		accountPanel.setLayout(new MigLayout(new LC().wrap()));
		
		// CREATE SCROLLPANE
		/* NOTE scrollpane isn't necessary with the current version, since the content 
		 * fits in the window. Keep for now. */ 
		scrollPane = new JScrollPane(accountPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		// CREATE ACCOUNT PANEL COMPONENTS
		JTextPane infoTextPane = new JTextPane();

		LinkedList<JPasswordField> passwordFields = new LinkedList<>();

		JPasswordField currentPassword = new JPasswordField("Current password");
		JPasswordField newPassword = new JPasswordField("New password");
		JPasswordField confirmPassword = new JPasswordField("Confirm password");
		passwordFields.add(currentPassword);
		passwordFields.add(newPassword);
		passwordFields.add(confirmPassword);

		JButton resetPasswordBtn = new JButton("Reset");

		// FORMATTING
		infoTextPane.setEditable(false);
		infoTextPane.setBackground(null);
		infoTextPane.setContentType("text/html");

		String htmlText = "<div style=\" font-family:" + defaultFont.getName() + "\">"
				+ "<h2>" + "Reset Password" + "</h2>"
				+ "<p style=\"font-size:" + defaultFont.getSize() + "pt" + "\">" 
				+ "Please enter the following information:" + "</p>" + "</div>";
		infoTextPane.setText(htmlText);

		// SET UP THE PASSWORD FIELDS IN A LOOP 
		accountPanel.add(infoTextPane);

		String[] fieldNames = {"Current password", "New password", "Confirm password"};

		int i = 0;
		for(JPasswordField p : passwordFields) {
			p.setName(fieldNames[i]);
			p.setText(fieldNames[i]);
			p.setEchoChar((char) 0); // make the characters visible
			p.setForeground(Color.LIGHT_GRAY);
			p.addFocusListener(new PasswordFocusListener());
			accountPanel.add(p, "width 200:200:");
			i++;
		}

		accountPanel.add(resetPasswordBtn, new CC().gapBefore("rel"));
		accountPanel.add(messagePanel);
		messagePanel.setVisible(false);
		viewEmployeePanel.add(scrollPane);

		// ADD LOGIN LISTENER TO RESETPASSWORDBTN
		resetPasswordBtn.addActionListener(new ResetPasswordListener(
				currentUser, passwordFields, this));
		
		dashboardPanel.add(viewEmployeePanel);
		initialize();
	}


	/**
	 * Displays the Admin dashboard page allowing the Admin to edit a given 
	 * employee's information.
	 * 
	 * @param employeeID <code>int</code>
	 */
	public void showAdminEditPage(int employeeID, JScrollPane scrollPane) {
		viewEmployeePanel.setLayout(new MigLayout(new LC().wrap()));
		scrollPane = new JScrollPane(viewEmployeePanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		setUpAdminEditPage(employeeID);
		
		dashboardPanel.add(scrollPane);
		initialize();
	}
	
	
	/**
	 * Sets up viewEmployeePanel to allow the Admin to edit a given 
	 * employee's information; use <code>showAdminEditPage()</code> method
	 * to display to the UI.
	 * 
	 * @param employeeID <code>int</code>
	 */
	public void setUpAdminEditPage(int employeeID) {
		messagePanel = new JPanel();
		JPanel editEmployeePanel = new JPanel();
		editEmployeePanel.setSize(new Dimension (dashboardPanel.getWidth(), 
				dashboardPanel.getHeight()/3*2));
		editEmployeePanel.setLayout(new MigLayout(
				new LC().wrapAfter(2).insets("0")));
		
		String[] categoryLblText = {"Name: ", "Surname: ", "Department: ",
				"Title: ", "Status: ", "Access Level: ", "Password: "};
		
		String[] categoryLblNames = {"name", "surname", "dept",
				"title", "status", "access_level", "password"};
		
		WorkStatus[] statusOptions = {null, WorkStatus.ACTIVE, WorkStatus.INACTIVE};
		AccessLevel[] accessLevelOptions = {null, AccessLevel.EMPLOYEE, AccessLevel.ADMIN};
		
		ArrayList<String> employeeInfo = new ArrayList<>();
		ArrayList<JLabel> categoryLbls = new ArrayList<>();
		ArrayList<JComponent> inputComponents = new ArrayList<>();
		
		// GET EMPLOYEE INFO AND ADD IT TO EMPLOYEEINFO ARRAYLIST
		/* TODO adjust Employee.java class to include all categories then add a new 
		 * method to Admin.java that gets employee data from DB and sets it to an
		 * Employee object, then just use employee.get... methods to obtain info.
		 * TODO then change certain parameters to accept an Employee object instead
		 * of this employeeInfo arrayList?
		 */
		employeeInfo = Admin.getAllEmployeeData(String.valueOf(employeeID));
				
		// CREATE THE JLABELS AND ADD TO CATEGORYLBLS ARRAYLIST
		for(int i = 0; i < categoryLblText.length; i++) {
			JLabel label = new JLabel(categoryLblText[i]);
			label.setName(categoryLblNames[i]);
			label.setFont(new Font(getDefaultFont().getName(), Font.BOLD, 
					getDefaultFont().getSize()));
			categoryLbls.add(label);
		}
		
		/*
		 *  CREATE THE INPUT FIELDS:
		 *  1) CREATE THE 4 JTEXTFIELDS AND ADD THEM TO INPUTCOMPONENTS.
		 *  2) CREATE THE 2 JCOMBOBOXES AND THE 2 JPASSWORDFIELDS
		 *   - naming convention for the 7 input fields: CATEGORYLBLNAME[X] + "Input"
		 */
		for(int i = 0; i < 4; i++) {
			JTextField textfield = new JTextField(employeeInfo.get(i + 1));
			textfield.setForeground(Color.DARK_GRAY);
			textfield.setName(categoryLblNames[i] + "Input");
			inputComponents.add(textfield);
		}
		// CREATE THE STATUS COMBOBOX
		JComboBox<WorkStatus> statusBox = new JComboBox<>(statusOptions);
		statusBox.setName(categoryLblNames[4] + "Input");
		statusBox.setEditable(false);
		if(employeeInfo.get(7) == null) { statusBox.setSelectedIndex(0); }
		else {
			if(employeeInfo.get(7).equals(WorkStatus.ACTIVE.toString())) { 
				statusBox.setSelectedIndex(1); }
			if(employeeInfo.get(7).equals(WorkStatus.INACTIVE.toString())) { 
				statusBox.setSelectedIndex(2); }
		}
		statusBox.setForeground(Color.DARK_GRAY);
		inputComponents.add(statusBox);
		
		// CREATE THE ACCESSLEVEL COMBOBOX
		JComboBox<AccessLevel> accessLevelBox = new JComboBox<>(accessLevelOptions);
		accessLevelBox.setName(categoryLblNames[5] + "Input");
		accessLevelBox.setEditable(false);
		if(employeeInfo.get(8) == null) { accessLevelBox.setSelectedIndex(0); }
		else {
			if(employeeInfo.get(8).equals(AccessLevel.EMPLOYEE.toString())) { 
				accessLevelBox.setSelectedIndex(1); }
			if(employeeInfo.get(8).equals(AccessLevel.ADMIN.toString())) { 
				accessLevelBox.setSelectedIndex(2); }
		}
		accessLevelBox.setForeground(Color.DARK_GRAY);
		inputComponents.add(accessLevelBox);
		
		// CREATE THE PASSWORD FIELDS 
		String[] fieldNames = {"New password", "Confirm password"};
		JPasswordField currentPasswordField = new JPasswordField(employeeInfo.get(9));
		JPasswordField newPasswordField = new JPasswordField(fieldNames[0]);
		JPasswordField confirmPasswordField = new JPasswordField(fieldNames[1]);
		
		LinkedList<JPasswordField> passwordFields = new LinkedList<>();
		passwordFields.add(newPasswordField);
		passwordFields.add(confirmPasswordField);

		int i = 0;
		for(JPasswordField p : passwordFields) {
			p.setName(fieldNames[i]);
			p.setText(fieldNames[i]);
			p.setEchoChar((char) 0); // make the characters visible
			p.setForeground(Color.LIGHT_GRAY);
			p.addFocusListener(new PasswordFocusListener());
			inputComponents.add(p);
			i++;
		}
		passwordFields.addFirst(currentPasswordField);
		
		// ADD JLABELS, JCOMPONENTS & SUBMITBUTTONS TO EDITEMPLOYEEPANEL
		for(int index = 0; index < 8; index++) {
			if(index == 7) {
				editEmployeePanel.add(inputComponents.get(index), "cell 1 7, width 150::150");
			}
			else {
				editEmployeePanel.add(categoryLbls.get(index));
				editEmployeePanel.add(inputComponents.get(index), "width 150::150");
			}
		}
		
		// CREATE THE SAVEEDITS BUTTON
		JButton saveEditsBtn = new JButton("Save Edits");
		saveEditsBtn.addActionListener(new ResetEmployeeInfoListener(employeeInfo, 
				inputComponents, passwordFields, this));
		editEmployeePanel.add(saveEditsBtn, "cell 1 8, gap ");
		
		viewEmployeePanel.add(editEmployeePanel);
		
		messagePanel.setPreferredSize(new Dimension(editEmployeePanel.getWidth(), 20));
		viewEmployeePanel.add(messagePanel);
		messagePanel.setVisible(false);
	}
	
	
	/**
	 * 
	 */
	public void showAdminCreateEmployeePage() {
		// CREATE PANELS
		viewEmployeePanel = new JPanel();
		JPanel infoPanel = new JPanel();
		JPanel createEmployeePanel = new JPanel();
		messagePanel = new JPanel();
		
		// SET PANEL SIZES & LAYOUT MANAGERS 
		dashboardPanel.setLayout(new MigLayout(
				new LC().wrap().fill().insets("0","15", "10", "5").topToBottom()));
		
		viewEmployeePanel.setLayout(new MigLayout(new LC().wrap()));
		createEmployeePanel.setSize(new Dimension (dashboardPanel.getWidth(), 
				(dashboardPanel.getHeight()/3)*2));
		createEmployeePanel.setLayout(new MigLayout(new LC().wrapAfter(2).insets("5")));
		
		messagePanel.setPreferredSize(new Dimension(viewEmployeePanel.getWidth(), 20));
		messagePanel.setVisible(false);
		
		infoPanel.setSize(new Dimension (dashboardPanel.getWidth(), 
				dashboardPanel.getHeight()/3));
		infoPanel.setLayout(new MigLayout(new LC().wrap()));
		
		JTextPane infoTextPane = new JTextPane();
		infoTextPane.setEditable(false);
		infoTextPane.setBackground(null);
		infoTextPane.setContentType("text/html");
		infoTextPane.setSize(infoPanel.getSize());
		
		String htmlText = "<div style=\" font-family:" + defaultFont.getName() + "\">"
				+ "<h2>" + "New Employee" + "</h2>"
				+ "<p style=\"font-size:" + defaultFont.getSize() + "pt" + "\">" 
				+ "Complete and submit the required fields to add new employee. "
				+ "Employee ID will be created upon submission." 
				+ "</p>" + "</div>";
		infoTextPane.setText(htmlText);
		
		String[] categoryLblText = {"Name: ", "Surname: ", "Department: ",
				"Title: ", "Status: ", "Access Level: ", "Password: "};
		
		WorkStatus[] statusOptions = {null, WorkStatus.ACTIVE, WorkStatus.INACTIVE};
		AccessLevel[] accessLevelOptions = {null, AccessLevel.EMPLOYEE, AccessLevel.ADMIN};
		
		ArrayList<JLabel> categoryLbls = new ArrayList<>();
		ArrayList<JComponent> inputComponents = new ArrayList<>();
				
		// CREATE THE JLABELS AND ADD TO CATEGORYLBLS ARRAYLIST
		for(int i = 0; i < categoryLblText.length; i++) {
			JLabel label = new JLabel(categoryLblText[i]);
			label.setFont(new Font(getDefaultFont().getName(), Font.BOLD, 
					getDefaultFont().getSize()));
			categoryLbls.add(label);
		}
		
		/*
		 *  CREATE THE INPUT FIELDS:
		 *  1) CREATE THE 4 JTEXTFIELDS AND ADD THEM TO INPUTCOMPONENTS.
		 *  2) CREATE THE 2 JCOMBOBOXES AND THE 2 JPASSWORDFIELDS
		 */
		for(int i = 0; i < 4; i++) {
			JTextField textfield = new JTextField();
			textfield.setForeground(Color.DARK_GRAY);
			inputComponents.add(textfield);
		}
		
		// CREATE THE STATUS COMBOBOX
		JComboBox<WorkStatus> statusBox = new JComboBox<WorkStatus>(statusOptions);
		statusBox.setEditable(false);
		statusBox.setSelectedIndex(0); 
		statusBox.setForeground(Color.DARK_GRAY);
		inputComponents.add(statusBox);
		
		// CREATE THE ACCESSLEVEL COMBOBOX
		JComboBox<AccessLevel> accessLevelBox = new JComboBox<AccessLevel>(accessLevelOptions);
		accessLevelBox.setEditable(false);
		accessLevelBox.setSelectedIndex(0); 
		accessLevelBox.setForeground(Color.DARK_GRAY);
		inputComponents.add(accessLevelBox);
		
		// CREATE THE PASSWORD FIELDS 
		String[] fieldNames = {"New password", "Confirm password"};
		JPasswordField newPasswordField = new JPasswordField(fieldNames[0]);
		JPasswordField confirmPasswordField = new JPasswordField(fieldNames[1]);
		
		JPasswordField[] passwordFields = {newPasswordField, confirmPasswordField};

		int i = 0;
		for(JPasswordField p : passwordFields) {
			p.setText(fieldNames[i]);
			p.setEchoChar((char) 0); // make the characters visible
			p.setForeground(Color.LIGHT_GRAY);
			p.addFocusListener(new PasswordFocusListener());
			inputComponents.add(p);
			i++;
		}
		
		// ADD JLABELS, JCOMPONENTS & SUBMITBUTTONS TO EDITEMPLOYEEPANEL
		for(int index = 0; index < 8; index++) {
			if(index == 7) { // the confirm password JPasswordField
				createEmployeePanel.add(inputComponents.get(index), "cell 1 7, width 150::150");
			}
			else {
				createEmployeePanel.add(categoryLbls.get(index));
				createEmployeePanel.add(inputComponents.get(index), "width 150::150");
			}
		}

		// CREATE THE RESET BUTTON
		JButton resetFieldsBtn = new JButton("Reset");
		resetFieldsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearDashboard();
				dashboardPanel.repaint();
				showAdminCreateEmployeePage();
			}
		});
		createEmployeePanel.add(resetFieldsBtn, "cell 0 8, width 100::100, align right");

		// CREATE THE SUBMIT BUTTON
		JButton submitBtn = new JButton("Save");
		submitBtn.addActionListener(new CreateEmployeeListener(inputComponents, this));
		createEmployeePanel.add(submitBtn, "cell 1 8, width 100::100");
		
		// CREATE THE SCROLLPANE
		scrollPane = new JScrollPane(viewEmployeePanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		
		// ADD COMPONENTS & PANELS
		infoPanel.add(infoTextPane);
		viewEmployeePanel.add(createEmployeePanel);
		viewEmployeePanel.add(messagePanel);
		dashboardPanel.add(infoPanel);
		dashboardPanel.add(scrollPane);
		
		initialize();
	}


}
