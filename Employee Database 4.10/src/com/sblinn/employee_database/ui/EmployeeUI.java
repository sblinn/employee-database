package com.sblinn.employee_database.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import com.sblinn.employee_database.listeners.DashboardListener;
import com.sblinn.employee_database.listeners.LoginListener;
import com.sblinn.employee_database.listeners.PasswordFocusListener;
import com.sblinn.employee_database.listeners.ResetPasswordListener;
import com.sblinn.employee_database.objects.User;
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
public class EmployeeUI extends UserInterface implements UserWindow {

	
	private JPanel menuPanel;
	
	private JPanel dashboardPanel;
	
	private JPanel viewEmployeePanel;
	
	/**
	 * <code>JPanel</code> for messages.
	 */
	private JPanel messagePanel = new JPanel();
	
	private JScrollPane scrollPane;
	
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
		}
	}
	
	/**
	 * Gets the <code>JPanel</code> used for User to view their employee
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
			viewEmployeePanel.repaint();
		}
	}
	
	
	
	/**
	 * Constructs a new <code>EmployeeUI</code> using a <code>User</code> object.
	 * 
	 * @param user <code>User</code>
	 */
	public EmployeeUI(User user) {
		this.user = user;
		this.fullUsername = Admin.getEmployeeData(user.getUserID(), "name") + " " 
				+ Admin.getEmployeeData(user.getUserID(), "surname"); 
	}
	
	
	/**
	 * Display the user's page after login.
	 * 
	 * @param userID <code>int</code>
	 */
	public void showMainUserPage(User user) {
		setCurrentFrameView(FrameView.VIEW_EMPLOYEE_MAIN);
		resetFrame();
		setFrame(new JFrame(fullUsername + " (" + user.getUserID() + ")"));
		
		// SET FRAME SIZE & SPECS
		// Aim for an 8:5 size ratio (my screen = 2560 x 1600)
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		getFrame().setMinimumSize(new Dimension((screenSize.width/3), ((screenSize.height/10)*4)));
		getFrame().setMinimumSize(new Dimension((screenSize.width/3), (screenSize.height/3)));
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
		menuTitlePanel.setLayout(new MigLayout(new LC().wrap()));
		menuTitlePanel.setBackground(null);
		
		// CREATE DASHBOARD PANEL
		dashboardPanel = new JPanel();
		dashboardPanel.setPreferredSize(new Dimension((getFrame().getWidth()/4)*3, 
				getFrame().getHeight()));
		dashboardPanel.setForeground(dashboardGray);
		dashboardPanel.setLayout(new MigLayout());
		
		// MENUTITLEPANEL COMPONENTS
		JLabel welcomeLbl = new JLabel("Welcome,");
		JLabel nameLbl = new JLabel(fullUsername);
			
		welcomeLbl.setFont(new Font(defaultFont.getName(), Font.BOLD,
				DisplayMethods.getProperFontSize(welcomeLbl, menuTitlePanel)));
		nameLbl.setFont(new Font(defaultFont.getName(), Font.BOLD,
				DisplayMethods.getNameLblFontSize(nameLbl, welcomeLbl, menuTitlePanel)));
		
		// ADD COMPONENTS TO MENUTITLEPANEL 
		menuTitlePanel.add(welcomeLbl, "align left");
		menuTitlePanel.add(nameLbl);
		
		// MENUPANEL COMPONENTS
		JButton empInfoBtn = new JButton("Employee Information");
		empInfoBtn.setName(FrameView.VIEW_CURRENT_USER_INFO.name());
		JButton accountBtn = new JButton("Account");
		accountBtn.setName(FrameView.VIEW_CURRENT_USER_ACCOUNT.name());
		JButton logoutBtn = new JButton("Log Out");
		
		JLabel creditLbl = new JLabel("Sara Blinn 2021");
		creditLbl.setFont(new Font(defaultFont.getName(), Font.ITALIC, 7));
		
		// ADD ACTIONLISTENERS
		logoutBtn.addActionListener(new LoginListener(new LoginUI()));
		empInfoBtn.addActionListener(new DashboardListener(this));
		accountBtn.addActionListener(new DashboardListener(this));
		
		// ADD COMPONENTS TO MENU PANEL
		menuPanel.add(menuTitlePanel, new CC().alignX("left").gapBottom("unrelated"));
		menuPanel.add(empInfoBtn);
		menuPanel.add(accountBtn);
		menuPanel.add(logoutBtn);
		menuPanel.add(creditLbl, new CC().pushY().alignY("bottom"));
		
		// DASHBOARDPANEL COMPONENTS (INITIALLY)
		
		// ADD COMPONENTS TO DASHBOARD PANEL
		 
		// ADD PANELS TO CONTENTPANE
		// menuTitlePanel has already been added to menuPanel
		getFrame().getContentPane().add(menuPanel, "dock west");
		getFrame().getContentPane().add(dashboardPanel);

		initialize();
	}
	
	
	/**
	 * Display current user information in dashboardPanel.
	 */
	public void showUserInfoPage() {
		viewEmployeePanel = new JPanel();
		scrollPane = new JScrollPane(viewEmployeePanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		setUpUserInfoPage(user.getUserID(), viewEmployeePanel, dashboardPanel);
		dashboardPanel.add(scrollPane);
		
		initialize();
	}
	
	
	/**
	 * Set up a given <code>JPanel</code> with a given user's info, 
	 * does not display until frame is re-initialized.
	 * 
	 * @param userID <code>String</code>
	 * @param displayPanel <code>JPanel</code>
	 */
	public void setUpUserInfoPage(String userID, JPanel displayPanel, JPanel parentPanel) {
		String username = Admin.getEmployeeData(userID, "name") + " " 
				+ Admin.getEmployeeData(userID, "surname"); 
		displayPanel.setLayout(new MigLayout(new LC().wrap()));
		displayPanel.setSize(parentPanel.getSize());
		
		// CREATE USERINFOPANE
		JTextPane userInfoPane = new JTextPane();
		//userInfoPane.setSize(parentPanel.getSize());
		userInfoPane.setPreferredSize(new Dimension(parentPanel.getWidth()-5, 
				(parentPanel.getHeight()/2)-5));
		
		//userInfoPane.setMargin(new Insets(20, 20, -20, -20));
		userInfoPane.setBorder(BorderFactory.createEmptyBorder());
		userInfoPane.setBackground(null);
		userInfoPane.setEditable(false);
		userInfoPane.setContentType("text/html");
		
		String htmlText = 
				"<div style=\" background-color:" + dashboardGrayHSLStr + ";\">"
				+ "<div style=\" font-family:" + defaultFont.getName() 
				+ "; " + "font-size:" + defaultFont.getSize() + "pt" + ";\">"
				+ "<h2>" + username + "</h2>"  
				+ "<div style=\" font-size:" + defaultFont.getSize() + "pt" + ";\">"
				+ "<p><b>Employee ID: </b>" + userID + "</p>" 
				+ "<p><b>Department: </b>" 
				+ Admin.getEmployeeData(userID, "dept") + "</p>" 
				+ "<p><b>Title: </b>" 
				+ Admin.getEmployeeData(userID, "title") + "</p>" 
				+ "<p><b>Hire Date: </b>"  
				+ Admin.getEmployeeData(userID, "hire_date") + "</p>";
		
		if(Admin.isTerminated(Integer.valueOf(userID))){
			htmlText += "<p><b>Termination Date: </b>" 
				+ Admin.getEmployeeData(userID, "termination_date") + "</p>";
		}
		
		htmlText += "</div></div></div>";
		userInfoPane.setText(htmlText);
		
		displayPanel.add(userInfoPane);
	}
	
	
	/**
	 *  Display current user account information in dashboardPanel.
	 */
	public void showAccountPage() {
		setCurrentFrameView(FrameView.VIEW_CURRENT_USER_ACCOUNT);
		setUpAccountPage(user, dashboardPanel);
	}	

	
	/**
	 * Sets up a given <code>JPanel</code> to allow a given user to
	 * reset current password.
	 * 
	 * @param currentUser <code>User</code>
	 * @param dashboardPanel <code>JPanel</code>
	 */
	public void setUpAccountPage(User currentUser, JPanel dashboardPanel) {
		// CREATE ACCOUNT PANEL
		viewEmployeePanel = new JPanel();
		JPanel accountPanel = new JPanel();

		accountPanel.setSize(new Dimension(dashboardPanel.getWidth(), 
				dashboardPanel.getHeight()));
		
		accountPanel.setLayout(new MigLayout(new LC().wrap().insets("5")));
		
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

		accountPanel.add(resetPasswordBtn, new CC().gapBefore("rel").wrap());
		accountPanel.add(messagePanel, "dock south");
		messagePanel.setVisible(false);
		viewEmployeePanel.add(scrollPane);
		accountPanel.add(resetPasswordBtn, new CC().gapBefore("rel"));

		// ADD LOGIN LISTENER TO RESETPASSWORDBTN
		resetPasswordBtn.addActionListener(new ResetPasswordListener(
				currentUser, passwordFields, this));

		// ADD VIEWEMPLOYEEPANEL TO DASHBOARD PANEL
		dashboardPanel.add(viewEmployeePanel);

		initialize();
	}

}
