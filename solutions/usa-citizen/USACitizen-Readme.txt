USACitizen Solution
==================

Purpose of the Solution
----------------------
The purpose of this solution is to demonstate how a United States of America Citizen model might be 
implemented in the Master Data Management (MDM) product suite. Specificaly the Master Index portion 
of MDM. 

This example uses a Citizen definition geared towards the United States of America.  Since this is an
USA Citizen, fields such as the Social Security Number (SSN) are used for both block-search
definitions and matches.

Loading and examining the various files provided with this solution will give a good
basis for understanding Master Index. 
 
 
What is included with this Solution
------------------------------------
o This readme.
o https://open-dm-mi.dev.java.net/Solutions.html 
  which provides an overview of all Solutions currently available.
o https://open-dm-mi.dev.java.net/docs/solutions/usa-Citizen.html
  which provides technical details about the United States of America Citizen solution.
o MDM-Solutions-USACitizen.zip 
  - The Netbeans Master Index project

How to use the solution.
-----------------------
	1. Unzip MDM-Solutions-USACitizen.zip.

	2. Open the following projects in Netbeans 6.1.
	
    TIP: You can open all of these by opening the main USACitizen project
    and selecting the "Open Required Projects" option. 

		USACitizen
		USACitizen-ejb
		USACitizen-war

  3. Create the database.

    Create a new user for database.  And execute following script files in this order
    to prepare the database.

	 		create.sql
			codelist.sql
			systems.sql

  4. Make sure that the GlassFish server is started.

	5. Right click on the USACitizen project and select 
     "Generate Master Index Files".

	6. Build and deploy the USACitizen project.
		a) Right click on the USACitizen project and select 
		   "Build".
		b) Right click on the USACitizen project and select
		   "Undeploy and Deploy".
                   
    This will bring up the "Warning - Select Server" dialog.
    Select "GlassFish V2" as the target server.  

	7. Using the GlassFish Admin Console define the following:
		Resources
		JDBC
		Connection pool
		USACitizen
		JDBC Resources
		jdbc/USACitizenDataSource

		Configuration
		Security
		Realms
		file

		a) Select "Manage Users"
		b) Select "New"
       Enter the following values:
		   User ID: 	mdm
       Group List:     MasterIndex.Admin,Administrator
       Password:   mdm
                          

   8. Bring up the MIDM GUI using the following URL:
      http://localhost:<http_port_default_8080>/USACitizenMIDM
           
   9. Log into the MIDM GUI using the username of 'mdm' and the password of 'mdm'.            

  10. Add new records:
      a) Select the "Source Record" tab.  
      b) Select the "Add" sub-tab.  
      c) Select "SUN" for the System and enter a unique Local ID, such as 444-444-4444.
      d) At a minimum specify the required values in the USACitizen Info section.
      e) Select "Submit" to add the new record.

Examples of what you might try
------------------------------
   o Add the same person but give them different dates of birth.  Be sure to select "MYSQL" as the System for the
     second record.
   o Add different people.
   o Add the same person but give them a different SSN. Be sure to select "MYSQL" as the System for the second 
     record.
   o Check for duplicates.
   o Look at the various tabs and try different queries or actions once you have some 
     duplicates.		
			
Updating the USACitizen model
-----------------------------
   o Make the desired changes to the Master Index configuration files.
      
   o Right click on the USACitizen project and select "Generate Master Index Files".
      
   o Update the database definitions by running the following SQL commands as the 'mdm' user.

   TIP: Be sure to run the drop.sql script first! Then run the create.sql script.
      
	 drop.sql
	 create.sql
	 codelist.sql
	 systems.sql
             
   o  Build and deploy the USACitizen project.      
        a) Right click on the USACitizen project and select
           "Clean and Build".
        b) Right click on the USACitizen project and select
           "Undeploy and Deploy".