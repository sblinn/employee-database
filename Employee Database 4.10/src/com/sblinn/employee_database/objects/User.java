package com.sblinn.employee_database.objects;

import com.sblinn.employee_database.objects.Employee.AccessLevel;

/**
 * 
 * @author sarablinn
 *
 */
public class User {
	
	/**
	 * 
	 */
	private String userID;
	
	
	/**
	 * 
	 */
	private String password;
	
	
	/**
	 * 
	 */
	private AccessLevel accessLevel;
	
	
	/**
	 * 
	 * @param userID
	 * @param password
	 */
	public User(String userID, String password) {
		this.userID = userID;
		this.password = password;
	}
	
	
	/**
	 * 
	 * @param userID
	 * @param password
	 * @param accessLevel
	 */
	public User(String userID, String password, AccessLevel accessLevel) {
		this(userID, password);
		this.accessLevel = accessLevel;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getUserID() {
		return userID;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public AccessLevel getAccessLevel() {
		return accessLevel;
	}
	
	
}
