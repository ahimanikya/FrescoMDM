USAPatient Solution
==================

Purpose of the Solution
----------------------
The purpose of this solution is to demonstate how a United States of America Patient model might be 
implemented in the Master Data Management (MDM) product suite. Specificaly the Master Index portion 
of MDM. 

This example uses a patient definition geared towards the United States of America.  Since this is an
USA Patient, fields such as the Social Security Number (SSN) are used for both block-search
definitions and matches.

Loading and examining the various files provided with this solution will give a good
basis for understanding Master Index. 
 
 
What is included with this Solution
------------------------------------
o This readme.
o https://open-dm-mi.dev.java.net/Solutions.html 
  which provides an overview of all Solutions currently available.
o https://open-dm-mi.dev.java.net/docs/solutions/usa-patient.html
  which provides technical details about the United States of America Patient solution.
o MDM-Solutions-USAPatient.zip 
  - The Netbeans Master Index project

How to use the solution.
-----------------------
	1. Unzip MDM-Solutions-USAPatient.zip.

	2. Open the following projects in Netbeans 6.1.
	
           TIP: You can open all of these by opening the main USAPatient project
           and selecting the "Open Required Projects" option. 
		USAPatient
		USAPatient-ejb
		USAPatient-war

        3. Create the database.
	 	The example creates a user called 'usa'. To create a different user,
		edit the createuser-usa.sql file.


		As a SYSDBA user run the createuser-usa.sql script.

		As the 'usa' user (password usa) run the following scripts. 
		TIP: Be sure to run the create.sql script first!

	 		create.sql
			codelist.sql
			systems.sql
			createIndexes.sql
			createUserCodeData.sql

        4. Make sure that the GlassFish server is started.

	5. Right click on the USA Patient project and select 
           "Generate Master Index Files".

	6. Build and deploy the USAPatient project.
		a) Right click on the USAPatient project and select 
		   "Build".
		b) Right click on the USAPatient project and select
		   "Undeploy and Deploy".
                   
                    This will bring up the "Warning - Select Server" dialog.
                    Select "GlassFish V2" as the target server.  

	7. Using the GlassFish Admin Console define the following:
	    Resources
		JDBC
		  Connection pool
			USAPatient
		  JDBC Resources
			jdbc/USAPatientDataSource

	    Configuration
		Security
		   	Realms
			   file
				a) Select "Manage Users"
				b) Select "New"
				    Enter the following values:
					User ID: 	usa
                                        Group List:     MasterIndex.Admin,Administrator
                                        Password:       usa
                          

       8. Bring up the MIDM GUI using the following URL:
               http://localhost:<http_port_default_8080>/USAPatientMIDM
               
          NOTE: If you changed the default port but can not remember the new port number do 
          one of the following:
            o From NetBeans 6.1 bring up the HTTP monitor. 
              The port is in the Client and Server tab shown as "Port number of HTTP server"
              
            o Edit the domain.xml file found under your JavaCaps6 install directory.  This file can be found
              in the following sub-directory: appserver\domains\domain1\config
              
              Find a line similar to the follow example.  The line you are looking for will have the following values:
                  - http-listener acceptor-threads="1"
                  - default-virtual-server="server"
                  - security-enabled="false"
              Usually this is the first http-listener entry in the domain.xml file.
             
              Example (Notice in this example the port used is 9090 instead of the default HTTP port 8080).
                 <http-listener acceptor-threads="1" address="0.0.0.0" blocking-enabled="false" 
                   default-virtual-server="server" enabled="true" family="inet" id="http-listener-1" 
                   port="9090" security-enabled="false" server-name="" xpowered-by="true">
                   
       9. Log into the MIDM GUI using the username of 'usa' and the password of 'usa'.            

      10. Add new records:
          a) Select the "Source Record" tab.  
          b) Select the "Add" sub-tab.  
          c) Select "Sun" for the System and enter a unique Local ID, such as 444-444-4444.
          d) At a minimum specify the required values in the USAPatient Info section.
          e) Select "Submit" to add the new record.

Examples of what you might try
------------------------------
   o Add the same person but give them different dates of birth.
   o Add different people.
   o Add the same person but give them a different SSN.
   o Check for duplicates.
   o Look at the various tabs and try different queries or actions once you have some 
     duplicates.		
			
Updating the USAPatient model
-----------------------------
   o Make the desired changes to the Master Index configuration files.
      
   o Right click on the USAPatient project and select "Generate Master Index Files".
      
   o Update the database definitions by running the following SQL commands as the 'usa' user.
     TIP: Be sure to run the drop.sql script first! Then run the create.sql script.
      
        drop.sql
        create.sql
	codelist.sql
	systems.sql
	createIndexes.sql      
        createUserCodeData.sql     
             
   o  Build and deploy the USAPatient project.      
        a) Right click on the USAPatient project and select
           "Clean and Build".
        b) Right click on the USAPatient project and select
           "Undeploy and Deploy".