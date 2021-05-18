package com.sblinn.employee_database.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * 
 * @author sarablinn
 *
 */
public class Employee {
	
	/**
	 * Work status of an Employee in the database. 
	 * 
	 * @author sarablinn
	 *
	 */
	public enum WorkStatus {
		ACTIVE, INACTIVE
	}
	
	/**
	 * Access level of an Employee in the database.
	 * 
	 * <p>An <code>ADMIN</code> is still an Employee, however having <code>ADMIN</code>
	 * level access allows the employee to log in as both an <code>EMPLOYEE</code> 
	 * or as an <code>ADMIN</code>.</p>
	 * 
	 * @author sarablinn
	 *
	 */
	public enum AccessLevel { 
		ADMIN, EMPLOYEE
	}
	
	
	private int id;
	
	private String name;
	
	private String surname;
	
	private String department;
	
	private String title;
	
	private GregorianCalendar hireDate;
	
	private GregorianCalendar terminationDate;
	
	private WorkStatus status;
	
	private AccessLevel accessLevel;
	
	private String password;
		
	
	/**
	 * Constructs an <code>Employee</code>. Main use for creating <code>Employee</code>
	 * object after obtaining existing employee data from a database.
	 * 
	 * @param name <code>String</code>
	 * @param surname <code>String</code>
	 * @param department <code>String</code>
	 * @param title <code>String</code>
	 * @param status <code>WorkStatus</code>
	 * @param accessLevel <code>AccessLevel</code>
	 */
	public Employee(int id, String name, String surname, String department,  
			String title, String hireDate, String terminationDate,
			WorkStatus status, AccessLevel accessLevel, String password) { 
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.department = department;
		this.title = title;
		this.status = status;
		this.accessLevel = accessLevel;
		this.password = password;

		DateFormat dateformat = new SimpleDateFormat("yyyy MM d");
		dateformat.setLenient(false);
		Date date;
		GregorianCalendar cal = new GregorianCalendar();	
		
		try { 
			// SET EMPLOYEE HIREDATE
			if(hireDate != null) {
				date = dateformat.parse(hireDate);
				cal.setTime(date);
				this.hireDate = cal;
			}
			// SET EMPLOYEE TERMINATIONDATE
			if(terminationDate != null) {
				date = dateformat.parse(terminationDate);
				cal.setTime(date);
				this.hireDate = cal;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Constructs an <code>Employee</code> and sets hire date upon construction. 
	 * 
	 * @param name <code>String</code>
	 * @param surname <code>String</code>
	 * @param department <code>String</code>
	 * @param title <code>String</code>
	 * @param status <code>WorkStatus</code>
	 * @param accessLevel <code>AccessLevel</code>
	 */
	public Employee(int id, String name, String surname, String department, String title, 
			WorkStatus status, AccessLevel accessLevel) { 
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.department = department;
		this.title = title;
		this.status = status;
		this.accessLevel = accessLevel;
		this.hireDate = new GregorianCalendar();	
	}
	
	
	/**
	 * Constructs an <code>Employee</code> with 0 for id. 
	 * 
	 * @param name <code>String</code>
	 * @param surname <code>String</code>
	 * @param department <code>String</code>
	 * @param title <code>String</code>
	 * @param status <code>WorkStatus</code>
	 * @param accessLevel <code>AccessLevel</code>
	 */
	public Employee(String name, String surname, String department, String title, 
			WorkStatus status, AccessLevel accessLevel) { 
		this(0, name, surname, department, title, status, accessLevel);
	}
	
	/**
	 * Constructs a new <code>Employee</code> with ACTIVE <code>WorkStatus</code> 
	 * and EMPLOYEE <code>AccessLevel</code>.
	 * 
	 * @param name <code>String</code>
	 * @param surname <code>String</code>
	 * @param department <code>String</code>
	 * @param title <code>String</code>
	 */
	public Employee(String name, String surname, String department, String title) { 
		this(name, surname, department, title, WorkStatus.ACTIVE, AccessLevel.EMPLOYEE);
	}
	
	/**
	 * Constructs a new <code>Employee</code> without department or title but with 
	 * ACTIVE <code>WorkStatus</code> and EMPLOYEE <code>AccessLevel</code>.
	 * 
	 * @param name <code>String</code>
	 * @param surname <code>String</code>
	 */
	public Employee(String name, String surname) {
		this(name, surname, null, null, WorkStatus.ACTIVE, AccessLevel.EMPLOYEE);
	}
	
	/**
	 * Returns the <code>Employee</code> id or 0 if no value has been set for this
	 * <code>Employee</code> object.
	 * @return id <code>int</code>
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Sets the <code>Employee</code> id to the parameter value.
	 * @param newID <code>int</code>
	 */
	public void setID(int newID) {
		this.id = newID;
	}
	
	/**
	 * Gets the <code>Employee</code> name <code>String</code>.
	 * @return name <code>String</code>
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the <code>Employee</code> name <code>String</code>.
	 * @param name <code>String</code>
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the <code>Employee</code> surname <code>String</code>.
	 * @return surname <code>String</code>
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Sets the <code>Employee</code> surname <code>String</code>.
	 * @param surname <code>String</code>
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Gets the <code>Employee</code> department <code>String</code>.
	 * @return department <code>String</code>
	 */
	public String getDept() {
		return department;
	}

	/**
	 * Sets the <code>Employee</code> department <code>String</code>.
	 * @param dept <code>String</code>
	 */
	public void setDept(String dept) {
		this.department = dept;
	}

	/**
	 * Gets the <code>Employee</code> title <code>String</code>.
	 * @return title <code>String</code>
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the <code>Employee</code> title <code>String</code>.
	 * @param title <code>String</code>
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the <code>Employee</code> hire date in 
	 * <code>GregorianCalendar</code> format.
	 * 
	 * <p>NOTE Use <code>getFormattedDate()</code> to get a simple format string 
	 * representation of any of the date objects, especially if adding this 
	 * information to the database.</p>
	 * 
	 * @return hireDate <code>GregorianCalendar</code>
	 */
	public GregorianCalendar getHireDate() {
		return hireDate;
	}

	/**
	 * Sets the <code>Employee</code> hire date. 
	 * @param hireDate <code>GregorianCalendar</code>
	 */
	public void setHireDate(GregorianCalendar hireDate) {
		this.hireDate = hireDate;
	}
	
	/**
	 * Returns the <code>Employee</code> termination date in 
	 * <code>GregorianCalendar</code> format.
	 * 
	 * <p>NOTE Use <code>getFormattedDate()</code> to get a simple format string 
	 * representation of any of the date objects, especially if adding this 
	 * information to the database.</p>
	 * 
	 * @return terminationDate <code>GregorianCalendar</code>
	 */
	public GregorianCalendar getTerminationDate() {
		return this.terminationDate;
	}
	
	/**
	 * Sets the <code>Employee</code> termination date in 
	 * <code>GregorianCalendar</code> format.
	 * 
	 * @param terminationDate <code>GregorianCalendar</code>
	 */
	public void setTerminationDate(GregorianCalendar terminationDate) {
		this.terminationDate = terminationDate;
	}
	
	/**
	 * Returns a <code>String</code> representation of a given 
	 * <code>GregorianCalendar</code> object representing the Calendar's time value
	 * in the format <code>yyyy MM d</code>.
	 * 
	 * @param cal <code>GregorianCalendar</code>
	 * @return <code>String</code>
	 */
	public String getFormattedDate(GregorianCalendar cal) {
		if(cal != null) {
			DateFormat dateformat = new SimpleDateFormat("yyyy MM d");
			dateformat.setLenient(false);
			return dateformat.format(cal.getTime()).toString();
		}
		return null;
	}

	
	/**
	 * Returns the <code>Employee</code> <code>WorkStatus</code>.
	 * @return status <code>WorkStatus</code>
	 */
	public WorkStatus getStatus() {
		return this.status;
	}
	
	/**
	 * Sets the <code>Employee</code> <code>WorkStatus</code>.
	 * @param status <code>WorkStatus</code>
	 */
	public void setStatus(WorkStatus status) {
		this.status = status;
	}
	
	/**
	 * Returns the <code>Employee</code> <code>AccessLevel</code>.
	 * @return status <code>AccessLevel</code>
	 */
	public AccessLevel getAccessLevel() {
		return this.accessLevel;
	}
	
	/**
	 * Sets the <code>Employee</code> <code>AccessLevel</code>.
	 * @param level <code>AccessLevel</code>
	 */
	public void setAccessLevel(AccessLevel level) {
		this.accessLevel = level;
	}

	/**
	 * Returns the <code>Employee</code> password.
	 * @return password <code>String</code>
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the ArrayList<String> password. 
	 * @param password <code>String</code>
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Returns an <code>ArrayList<String></code> containing <code>String</code>
	 * representations of all of the <code>Employee</code> field values.
	 * 
	 * @return <code>ArrayList<String></code>
	 */
	public ArrayList<String> getEmployeeFields(){
		ArrayList<String> fieldData = new ArrayList<>();
		fieldData.add(String.valueOf(this.id));
		fieldData.add(name);
		fieldData.add(surname);
		fieldData.add(department);
		fieldData.add(title);
		fieldData.add(getFormattedDate(hireDate));
		fieldData.add(getFormattedDate(terminationDate));
		
		if(status == null) {
			fieldData.add("null");
		}
		else {
			fieldData.add(status.toString());
		}
		
		if(accessLevel == null) {
			fieldData.add("null");
		}
		else {
			fieldData.add(accessLevel.toString());
		}
		
		fieldData.add(password);
		
		return fieldData;
	}
	
}
