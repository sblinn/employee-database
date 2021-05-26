# employee-database


Employee Information Database using Java and mySQL.

*not a final product*
TASKS:
  - Create testing package to guide new user to build/install necessary elements to run application (i.e. first ADMIN, employees_tbl with appropriate columns).

Current Requirements (For anyone trying to test):
  - mySQL and MigLayout drivers (included in files), mySQL Workbench
  - Existing mySQL connection
  
To execute:
  - See requirements above. Once necessary items are installed:
    - Create or connect new localhost server connection, keep connection user name and password for next step.
    - Open Application.java in text editor/IDE and change field variable values "serverUsername" and "serverPassword".
    - In mySQL Workbench, run employee_database_mysql-script.txt to create new database, table and first ADMIN account.
  - run: Application.java 
    - Select Admin Access
    - Enter login information for first ADMIN account: ID: 2500, PASSWORD: password
    - Once logged in as ADMIN, user can create and edit employees in the database saved to the localhost.
  - NOTE: TestArena.java contains some fields used to create new random Employee objects for testing purposes. 
