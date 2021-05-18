package com.sblinn.employee_database.ui;



import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;



/**
 * <code>UserInterface</code> sets the application window frame. 
 * 
 * <p>The method <code>startApplication()</code> starts the <code>Runnable</code> 
 * thread and calls a <code>LoginUI</code> method to build the initial window.
 * 
 * @author sarablinn
 *
 */
public class UserInterface {



	/**
	 * <code>UserInterface JFrame</code> for application window.
	 */
	private static JFrame c;
	
	
	/**
	 * <code>HSB Color</code> value for Employee and Admin dashboard 
	 * <code>JPanel</code>.
	 */
	public static final Color dashboardGray = Color.getHSBColor(0.55f, 0.01f, 0.99f);
	
	/**
	 * <code>HSL Color</code> code String for <code>dashboardGray</code>.
	 */
	public static final String dashboardGrayHSLStr = "198, 1%, 99%";
	
	
	
	/**
	 * Default system font.
	 */
	public static final Font defaultFont = new JLabel().getFont();
	
	
	/**
	 * <code>FrameView</code> representing the current view in
	 * Employee and Admin user windows.
	 */
	private static FrameView frameView;
	
	
	/**
	 * Enum values representing the current view in Employee and Admin
	 * user windows. Intended to keep values concrete. 
	 * @author sarablinn
	 *
	 */
	public enum FrameView {
		VIEW_EMPLOYEE_MAIN, VIEW_ADMIN_MAIN,
		VIEW_EMPLOYEE_AS_ADMIN, VIEW_CHOOSE_EMPLOYEE_AS_ADMIN, 
		VIEW_EDIT_EMPLOYEE_AS_ADMIN, VIEW_CHOOSE_EDIT_EMPLOYEE_AS_ADMIN,
		VIEW_ADD_NEW_EMPLOYEE_AS_ADMIN,
		VIEW_CURRENT_USER_INFO, VIEW_CURRENT_USER_ACCOUNT;
	}
	
	
	
	/**
	 * Gets the <code>UserInterface JFrame</code>.
	 * @return JFrame
	 */
	public static JFrame getFrame() {
		return c;
	}
	
	/**
	 * Sets the application's window (<code>UserInterface JFrame</code>) to a given frame.
	 * @param frame 
	 */
	public static void setFrame(JFrame frame) {
		c = frame;
	}
	
	
	/**
	 * Clears the frame.
	 */
	public static void resetFrame() {
		if(c != null) {
			c.dispose();
		}
	}
	
	
	/**
	 * Gets the current <code>FrameView</code> from <code>UserInterface</code>.
	 * @return frameView <code>FrameView</code>
	 */
	public static FrameView getCurrentFrameView() {
		return frameView;
	}
	
	
	/**
	 * Sets the <code>currentView</code> variable to <code>newView</code>.
	 * @param newView <code>String</code>
	 */
	public static void setCurrentFrameView(FrameView newFrameView) {
		frameView = newFrameView;
	}
	
	
	/**
	 * Returns the system default font.
	 * @return defaultFont
	 */
	public static Font getDefaultFont() {
		return defaultFont;
	}
	
	
	
	/**
	 * Launch the application. 
	 */
	public void startApplication() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LoginUI().chooseAccess();
			}
		});
	}
	
	/**
	 * Initialize and make the application window frame visible.
	 */
	public static void initialize() {
		try {
			c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			c.pack();
			c.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
