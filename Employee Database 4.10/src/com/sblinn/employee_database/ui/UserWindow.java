package com.sblinn.employee_database.ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sblinn.employee_database.objects.User;



public interface UserWindow {

	
	/**
	 * Gets the current <code>User</code>.
	 * 
	 * @return <code>User</code>
	 */
	public User getUser();
	
	
	/**
	 * Gets the dashboard <code>JPanel</code>.
	 * 
	 * @return <code>JPanel</code>
	 */
	public JPanel getDashboard();
	
	
	/**
	 * Clears the dashboard <code>JPanel</code>.
	 */
	public void clearDashboard();
	
	/**
	 * Gets the ViewEmployeePanel <code>JPanel</code>.
	 * 
	 * @return <code>JPanel</code>
	 */
	public JPanel getViewEmployeePanel();
	
	/**
	 * Clears the ViewEmployeePanel <code>JPanel</code>.
	 */
	public void clearViewEmployeePanel();
	
	/**
	* Gets the message panel <code>JPanel</code>.
	 * 
	 * @return <code>JPanel</code>
	 */
	public JPanel getMessagePanel();
	
	/**
	 * Gets the <code>JScrollPane</code> used for housing the viewEmployeePanel.
	 * 
	 * @return <code>JScrollPane</code>
	 */
	public JScrollPane getScrollPane();
	
	/**
	 * Removes the <code>JComponent</code> at the given index from an array
	 * of components for a <code>JPanel</code>, if it exists.
	 * 
	 * <p>Intended use to remove <code>JScrollPane</code> from dashboard in 
	 * <code>AdminUI</code> and <code>EmployeeUI</code> view/edit employee
	 * information pages.</p>
	 * 
	 * @param index <code>int</code>
	 * @param panel <code>JPanel</code>
	 */
	public void removeComponentAtIndex(int index, JPanel panel);
}
